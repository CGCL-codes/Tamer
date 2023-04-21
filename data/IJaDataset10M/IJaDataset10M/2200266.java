package jp.co.lattice.vProcessor.base;

import java.awt.*;
import jp.co.lattice.vProcessor.node.*;
import jp.co.lattice.vProcessor.com.*;
import jp.co.lattice.vKernel.core.c0.*;

/**
 * @author	  created by Eishin Matsui (00/04/28-)
 */
public class x3pBinToImage extends x3pRoot {

    /**
	 * static�ϐ���p�̂��߂̓����N���X
	 */
    public static class Global {

        /** ���[�J���ϐ��p�� new ��p�o�b�t�@�G���A		*/
        private int tiAryBase64DecodeMain[] = new int[4];

        /**
		 * �R���X�g���N�^
		 * @param  dt		(( I )) �O���[�o���f�[�^
		 */
        public Global(x3pGlobal dt) {
        }
    }

    /** ���N���X�p�̃O���[�o���f�[�^		*/
    private final Global Gbl() {
        return ((x3pBaseGblElm) global.GBaseX3p()).gBinToImage;
    }

    /**
	 * �R���X�g���N�^
	 * @param  dt		(( I )) �O���[�o���f�[�^
	 */
    public x3pBinToImage(x3pGlobal dt) {
        super(dt);
    }

    /**
	 */
    public final Image Exec(char src[]) throws lvThrowable {
        byte[] data = Base64Decode(src);
        return MakeImage(data);
    }

    private final byte[] Base64Decode(char src[]) throws lvThrowable {
        int num = NumBase64Decode(src);
        byte dst[] = new byte[num];
        Base64DecodeMain(src, dst);
        return dst;
    }

    private final int NumBase64Decode(char src[]) {
        int num = 0;
        for (int i = 0; i < src.length; i++) {
            char c = src[i];
            if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || ('0' <= c && c <= '9') || c == '+' || c == '/') num++;
        }
        int len = ((num + 4 - 1) / 4) * 3;
        if (num >= 1 && src[num - 1] == '=') {
            if (num >= 2 && src[num - 2] == '=') len -= 2; else len--;
        }
        return len;
    }

    private final void Base64DecodeMain(char src[], byte dst[]) throws lvThrowable {
        int n[] = Gbl().tiAryBase64DecodeMain;
        int cntSrc = 0;
        int cntDst = 0;
        int i, j;
        int num = (dst.length + 3 - 1) / 3;
        for (i = 0; i < num; i++) {
            for (j = 0; j < 4; j++) n[j] = -1;
            for (j = 0; j < 4; j++) {
                if (cntSrc >= src.length) break;
                n[j] = Base64DecodeOne(src[cntSrc]);
                cntSrc++;
            }
            int nSrc = j;
            Err().Assert((nSrc >= 2), "x3pBinToImage.Base64DecodeMain(0)");
            int val = 0;
            for (j = 0; j < 4; j++) {
                if (n[j] >= 0) val |= n[j] & 0x3f;
                if (j < (4 - 1)) val <<= 6;
            }
            for (j = 0; j < 3; j++) {
                int k = 3 - 1 - j;
                if (k < (nSrc - 1)) dst[cntDst + k] = (byte) (val & 0xff);
                val >>= 8;
            }
            cntDst += nSrc - 1;
        }
        Err().Assert((cntDst == dst.length), "x3pBinToImage.Base64DecodeMain(1)");
    }

    private final int Base64DecodeOne(char c) {
        if ('A' <= c && c <= 'Z') return (c - 'A'); else if ('a' <= c && c <= 'z') return (c - 'a' + 26); else if ('0' <= c && c <= '9') return (c - '0' + 52); else if (c == '+') return 62; else if (c == '/') return 63;
        return -1;
    }

    private final Image MakeImage(byte src[]) throws lvThrowable {
        Image img = Toolkit.getDefaultToolkit().createImage(src);
        Frame frame = new Frame();
        MediaTracker tracker = new MediaTracker(frame);
        tracker.addImage(img, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            Err().Assert(false, "x3pBinToImage.MakeImage(0)");
        }
        return img;
    }
}
