package de.felixbruns.jotify.gateway.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.sun.net.httpserver.HttpExchange;
import de.felixbruns.jotify.cache.SubstreamCache;
import de.felixbruns.jotify.exceptions.ProtocolException;
import de.felixbruns.jotify.media.File;
import de.felixbruns.jotify.media.Track;
import de.felixbruns.jotify.player.SpotifyOggHeader;
import de.felixbruns.jotify.protocol.Protocol;
import de.felixbruns.jotify.protocol.channel.Channel;
import de.felixbruns.jotify.protocol.channel.ChannelListener;

public class ChannelStreamer implements ChannelListener {

    private Cipher cipher;

    private Key key;

    private byte[] iv;

    private Track track;

    private Protocol protocol;

    private int channelOffset;

    private int channelLength;

    private int channelTotal;

    private SpotifyOggHeader header;

    private HttpExchange exchange;

    private OutputStream output;

    private SubstreamCache cache;

    private byte[] cacheData;

    private int total = 0;

    public ChannelStreamer(Protocol protocol, Track track, byte[] key, HttpExchange exchange) {
        this.exchange = exchange;
        this.output = exchange.getResponseBody();
        this.cache = new SubstreamCache();
        try {
            this.cipher = Cipher.getInstance("AES/CTR/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("AES not available! Aargh!");
        } catch (NoSuchPaddingException e) {
            System.err.println("No padding not available... haha!");
        }
        this.key = new SecretKeySpec(key, "AES");
        this.iv = new byte[] { (byte) 0x72, (byte) 0xe0, (byte) 0x67, (byte) 0xfb, (byte) 0xdd, (byte) 0xcb, (byte) 0xcf, (byte) 0x77, (byte) 0xeb, (byte) 0xe8, (byte) 0xbc, (byte) 0x64, (byte) 0x3f, (byte) 0x63, (byte) 0x0d, (byte) 0x93 };
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.key, new IvParameterSpec(this.iv));
        } catch (InvalidKeyException e) {
            System.err.println("Invalid key!");
        } catch (InvalidAlgorithmParameterException e) {
            System.err.println("Invalid IV!");
        }
        this.track = track;
        this.protocol = protocol;
        this.channelOffset = 0;
        this.channelLength = 160 * 1024 * 5 / 8;
        this.header = null;
        String hash = this.cache.hash(this.track, this.channelOffset, this.channelLength);
        if (this.cache != null && this.cache.contains("substream", hash)) {
            this.cache.load("substream", hash, this);
        } else {
            try {
                this.protocol.sendSubstreamRequest(this, this.track, this.track.getFile(File.BITRATE_160), this.channelOffset, this.channelLength);
            } catch (ProtocolException e) {
                return;
            }
        }
    }

    @Override
    public void channelHeader(Channel channel, byte[] header) {
        this.cacheData = new byte[this.channelLength];
        this.channelTotal = 0;
    }

    @Override
    public void channelData(Channel channel, byte[] data) {
        int off, w, x, y, z;
        for (int i = 0; i < data.length; i++) {
            this.cacheData[this.channelTotal + i] = data[i];
        }
        byte[] ciphertext = new byte[data.length + 1024];
        byte[] keystream = new byte[16];
        for (int block = 0; block < data.length / 1024; block++) {
            off = block * 1024;
            w = block * 1024 + 0 * 256;
            x = block * 1024 + 1 * 256;
            y = block * 1024 + 2 * 256;
            z = block * 1024 + 3 * 256;
            for (int i = 0; i < 1024 && (block * 1024 + i) < data.length; i += 4) {
                ciphertext[off++] = data[w++];
                ciphertext[off++] = data[x++];
                ciphertext[off++] = data[y++];
                ciphertext[off++] = data[z++];
            }
            for (int i = 0; i < 1024 && (block * 1024 + i) < data.length; i += 16) {
                try {
                    keystream = this.cipher.doFinal(this.iv);
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < 16; j++) {
                    ciphertext[block * 1024 + i + j] ^= keystream[j] ^ this.iv[j];
                }
                for (int j = 15; j >= 0; j--) {
                    this.iv[j] += 1;
                    if ((this.iv[j] & 0xFF) != 0) {
                        break;
                    }
                }
                try {
                    this.cipher.init(Cipher.ENCRYPT_MODE, this.key, new IvParameterSpec(this.iv));
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            off = 0;
            if (this.header == null) {
                byte[] bytes = Arrays.copyOfRange(ciphertext, 0, 167);
                this.header = SpotifyOggHeader.decode(bytes);
                System.out.format("Header: 0x%08x\n", (this.header.getBytes() & ~4095) - 167);
                this.exchange.sendResponseHeaders(200, (this.header.getBytes() & ~4095) - 167);
                off = 167;
            }
            this.output.write(ciphertext, off, data.length - off);
            this.output.flush();
            this.channelTotal += data.length;
            this.total += data.length;
        } catch (Exception e) {
        }
    }

    @Override
    public void channelEnd(Channel channel) {
        String hash = this.cache.hash(this.track, this.channelOffset, this.channelLength);
        if (this.cache != null && !this.cache.contains("substream", hash)) {
            this.cache.store("substream", hash, this.cacheData, this.channelTotal);
        }
        try {
            if (this.channelTotal < this.channelLength) {
                this.output.close();
                System.out.format("Stream: 0x%08x\n", this.total - 167);
                return;
            }
            this.channelOffset += this.channelLength;
            hash = this.cache.hash(this.track, this.channelOffset, this.channelLength);
            if (this.cache != null && this.cache.contains("substream", hash)) {
                this.cache.load("substream", hash, this);
            } else {
                this.protocol.sendSubstreamRequest(this, this.track, this.track.getFile(File.BITRATE_160), this.channelOffset, this.channelLength);
            }
        } catch (IOException e) {
        } catch (ProtocolException e) {
        }
        Channel.unregister(channel.getId());
    }

    @Override
    public void channelError(Channel channel) {
    }
}
