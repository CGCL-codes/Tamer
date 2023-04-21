package com.hyk.compress.compressor.gz;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.hyk.compress.compressor.Compressor;
import com.hyk.io.buffer.ChannelDataBuffer;

/**
 * @author Administrator
 * 
 */
public class GZipCompressor implements Compressor {

    public static final String NAME = "gz";

    @Override
    public ChannelDataBuffer compress(ChannelDataBuffer data) throws IOException {
        ChannelDataBuffer ret = ChannelDataBuffer.allocate(data.readableBytes() / 3);
        return compress(data, ret);
    }

    @Override
    public ChannelDataBuffer decompress(ChannelDataBuffer data) throws IOException {
        ChannelDataBuffer ret = ChannelDataBuffer.allocate(data.readableBytes() * 3);
        GZIPInputStream gis = new GZIPInputStream(data.getInputStream());
        int b;
        while ((b = gis.read()) != -1) {
            ret.getOutputStream().write(b);
        }
        gis.close();
        ret.flip();
        return ret;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ChannelDataBuffer compress(ChannelDataBuffer data, ChannelDataBuffer out) throws IOException {
        GZIPOutputStream gos = new GZIPOutputStream(out.getOutputStream());
        for (ByteBuffer buf : ChannelDataBuffer.asByteBuffers(data)) {
            byte[] raw = buf.array();
            int offset = buf.position();
            int len = buf.remaining();
            gos.write(raw, offset, len);
        }
        gos.flush();
        gos.finish();
        gos.close();
        return out;
    }
}
