package org.piax.trans.ts.udp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.grlea.log.SimpleLogger;
import org.piax.trans.util.ByteBufferUtil;

/**
 * メッセージをMTUサイズ以下のfragmentに分割、または、
 * MTUサイズ以下のfragmentに分割されたメッセージを再構成するための
 * 機能をまとめたクラス。
 * 
 * @author     Mikio Yoshida
 * @version    2.2.0
 */
public class Fragments {

    private static final SimpleLogger log = new SimpleLogger(Fragments.class);

    /**
     * fragmentの破棄を判断する時間(ms)。
     * ここでは十分に長い時間を指定すればよい。
     */
    private static final int DECK_EXPIRED_TIME = 60 * 60 * 1000;

    static final int PACKET_HEADER_SIZE = 2 + 2;

    public static int duplicated = 0;

    public static int skipped = 0;

    public static int losses = 0;

    /**
     * メッセージをfragmentに分解したパケットを構成するクラス。
     * <p>
     * fragmentの再構成のために、msgIdとシーケンシャル番号を内部に持つ。
     * 外部から設定するシーケンシャル番号は0でスタートするintであるが、
     * 内部表現（内部seq番号）は次のルールによりエンコードされたshortの整数となる。
     * ここで、Fをfragment数とする。
     * <ul>
     * <li> 0番目の場合、1-F 
     * <li> k番目の場合、F-k-1
     * </ul>
     * 例えば、fragmentの数が4である場合、順に、-3, 2, 1, 0 という_seq番号が
     * 割り振られる。
     * このエンコーディングにより、先頭と最後のfragmentの識別が可能になり、
     * 先頭のfragmentを取得した際に、メッセージ全体のバイト数を推定可能となる。
     */
    static class FragmentPacket {

        short msgId = 0;

        /** 内部seq番号 */
        short _seq = 0;

        byte[] bbuf;

        int boff;

        int blen;

        /**
         * FragmentPacketを生成する。
         * 
         * @param msgId msgId
         * @param seq 0から番号付けられたシーケンス
         * @param fragNum 全fragment数
         * @param bbuf fragment byte列を持つ配列
         * @param boff fragment byte列のoffset
         * @param blen fragment byte列の長さ
         */
        FragmentPacket(short msgId, int seq, int fragNum, byte[] bbuf, int boff, int blen) {
            this.msgId = msgId;
            _seq = encodedSeq(seq, fragNum);
            this.bbuf = bbuf;
            this.boff = boff;
            this.blen = blen;
        }

        /**
         * パケットのbyte列からFragmentPacketを再構成する。
         * 
         * @param pac パケットを格納するbyte配列
         * @param len パケットの長さ
         */
        FragmentPacket(byte[] pac, int len) {
            ByteBuffer b = ByteBuffer.wrap(pac, 0, len);
            msgId = b.getShort();
            _seq = b.getShort();
            bbuf = pac;
            boff = PACKET_HEADER_SIZE;
            blen = len - PACKET_HEADER_SIZE;
            ;
        }

        private static short encodedSeq(int seq, int fragNum) {
            return (short) ((seq == 0) ? 1 - fragNum : fragNum - seq - 1);
        }

        private int size() {
            return PACKET_HEADER_SIZE + blen;
        }

        /**
         * FragmentPacketをパケットデータに変換する
         * 
         * @return パケットデータ
         */
        byte[] toBytes() {
            ByteBuffer b = ByteBuffer.allocate(size());
            b.putShort(msgId);
            b.putShort(_seq);
            b.put(bbuf, boff, blen);
            return b.array();
        }
    }

    /**
     * fragmentを集めて元のメッセージを構成するためのクラス。
     * <p>
     * fragmentの受信のreorder, duplicate, lossに対応する。
     * fragmentがlossした場合は、Deckオブジェクトは未完成のままになる。
     * 未完成のDeckが、一巡して同じmsgIdを持つfragmentを受信した際に再利用
     * されることを防ぐために、タイムスタンプを用いた失効判断を行う。
     */
    static class Deck {

        /**
         * 受信処理を延期したfragmentを保持するためのクラス。
         * 先頭のfragmentを受信するまでの間だけ使用される。
         */
        static class DeferredFragment {

            int seq;

            byte[] data;

            DeferredFragment(int seq, byte[] bbuf, int boff, int blen) {
                this.seq = seq;
                data = new byte[blen];
                System.arraycopy(bbuf, boff, data, 0, blen);
            }
        }

        /** 
         * 最初のfragmentを受信した時刻(ms)。
         * 再構成に失敗した古いMsgBufferを破棄するために用いる。
         */
        final long timeStamp = System.currentTimeMillis();

        /**
         * fragmentのbyte長を保持する。
         * 先頭のfragmentを受信した時点で値が確定する。
         */
        int fragLen = 0;

        /**
         * fragment数を保持する。
         * 先頭のfragmentを受信した時点で値が確定する。
         */
        int fragNum = 0;

        /**
         * 受信したfragmentを再構成するByteBuffer。
         * メッセージ長を計算するためには、先頭のfragmentを受信する必要があるため、
         * 先頭のfragmentを受信するまではnewされない。
         */
        ByteBuffer bb = null;

        /**
         * 処理済みの最も進んだ内部seq番号を保持する。
         * 尚、内部seq番号は ..,2,1,0と降順に振られている。
         */
        int currentSeq = -1;

        /**
         * 受信の際に、飛ばしてしまった内部seq番号を保持する。
         */
        List<Integer> skippedSeqs = new ArrayList<Integer>();

        /**
         * 先頭のfragmentを受信するまで処理を延期したfragmentを保持する。
         * 先頭のfragmentを受信するまでの間だけ使用される。
         */
        List<DeferredFragment> deferredFrags = new ArrayList<DeferredFragment>();

        /**
         * Deckが失効したかどうかを判定する。
         * 
         * @return Deckが失効した場合 true、それ以外は false
         */
        boolean isExpired() {
            return System.currentTimeMillis() > timeStamp + DECK_EXPIRED_TIME;
        }

        /**
         * 受信したfragment（seq番号とbyte列）をDeckに書き加える。
         * 最後のfragmentを書き加えて、元のメッセージを完成した場合は、返り値
         * として、再構成したメッセージを持つByteBufferを返す。
         * 未完成の場合は、nullが返る。
         * 
         * @param seq fragmentの持つseq番号
         * @param bbuf fragmentのbyte列を持つ配列
         * @param boff fragmentのbyte列のoffset
         * @param blen fragmentのbyte列の長さ
         * @return メッセージを完成した場合はそのByteBuffer、それ以外はnull
         */
        ByteBuffer put(int seq, byte[] bbuf, int boff, int blen) {
            if (bb == null) {
                if (seq >= 0) {
                    deferredFrags.add(new DeferredFragment(seq, bbuf, boff, blen));
                    return null;
                }
                fragLen = blen;
                fragNum = -seq + 1;
                bb = ByteBufferUtil.newByteBuffer(fragLen * fragNum);
                ByteBufferUtil.copy2Buffer(bbuf, boff, blen, bb, 0);
                currentSeq = -seq;
                ByteBuffer ret = null;
                for (DeferredFragment frag : deferredFrags) {
                    ret = put(frag.seq, frag.data, 0, frag.data.length);
                }
                return ret;
            }
            if (currentSeq > seq) {
                int skippedNum = currentSeq - seq - 1;
                if (skippedNum > 0) {
                    skipped += skippedNum;
                    log.info(skippedNum + "fragments skipped");
                    for (int i = currentSeq - 1; i > seq; i--) {
                        skippedSeqs.add(new Integer(i));
                    }
                }
                currentSeq = seq;
            } else if (seq > currentSeq) {
                if (!skippedSeqs.remove(new Integer(seq))) {
                    duplicated++;
                    log.info("duplicated fragment received");
                    return null;
                }
            } else {
                duplicated++;
                log.info("duplicated fragment received");
                return null;
            }
            int bbOff = (fragNum - seq - 1) * fragLen;
            ByteBufferUtil.copy2Buffer(bbuf, boff, blen, bb, bbOff);
            if (currentSeq == 0 && skippedSeqs.size() == 0) {
                return bb;
            }
            return null;
        }

        /**
         * 未処理のfragment数を返す。
         * 
         * @return 未処理のfragment数
         */
        int unprocessedNum() {
            if (bb == null) return 1;
            return currentSeq + skippedSeqs.size();
        }
    }

    private final Map<String, Deck> decks;

    public Fragments() {
        decks = new HashMap<String, Deck>();
    }

    /**
     * 指定された条件を持つパケットbyte列を生成する。
     * 
     * @param msgId msgId
     * @param seq 0から番号付けられたシーケンス
     * @param fragNum 全fragment数
     * @param bbuf fragment byte列を持つ配列
     * @param boff fragment byte列のoffset
     * @param blen fragment byte列の長さ
     */
    public byte[] newPacketBytes(short msgId, int seq, int fragNum, byte[] bbuf, int boff, int blen) {
        return new FragmentPacket(msgId, seq, fragNum, bbuf, boff, blen).toBytes();
    }

    /**
     * Deckを振り分けるためのtag文字列を生成する。
     * パケットを受信した際に、受信byte列からFragmentPacketを構成するが、
     * 送信元のアドレス（IP,port）とmsgIdのペアを識別子として、
     * Deckに振り分ける必要がある。tag文字列はこの識別子として生成する。
     * 
     * @param srcAddr 送信元のアドレス
     * @param msgId msgId
     * @return tag文字列
     */
    public String getTag(InetSocketAddress srcAddr, short msgId) {
        return srcAddr.getAddress().getHostAddress() + ":" + srcAddr.getPort() + "+" + msgId;
    }

    /**
     * fragmentの再構成処理を行う。
     * 指定された送信元アドレスとFragmentPacketのmsgIdを使い適切なDeckに
     * fragmentを追加していく。
     * 最後のfragmentを書き加えて、元のメッセージを完成した場合は、返り値
     * として、再構成したメッセージを持つByteBufferを返す。
     * 未完成の場合は、nullが返る。
     * 
     * @param srcAddr 送信元のアドレス
     * @param fpac FragmentPacket
     * @return メッセージを完成した場合はそのByteBuffer、それ以外はnull
     */
    public ByteBuffer put(InetSocketAddress srcAddr, FragmentPacket fpac) {
        String tag = getTag(srcAddr, fpac.msgId);
        Deck deck = decks.get(tag);
        if (deck == null) {
            deck = new Deck();
            decks.put(tag, deck);
        } else if (deck.isExpired()) {
            losses += deck.unprocessedNum();
            deck = new Deck();
            decks.put(tag, deck);
        }
        ByteBuffer b = deck.put(fpac._seq, fpac.bbuf, fpac.boff, fpac.blen);
        if (b != null) {
            decks.remove(tag);
        }
        return b;
    }
}
