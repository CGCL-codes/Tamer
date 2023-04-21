package com.jme3.network.serializing.serializers;

import com.jme3.network.message.GZIPCompressedMessage;
import com.jme3.network.message.Message;
import com.jme3.network.serializing.Serializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Serializes GZIP messages.
 *
 * @author Lars Wesselius
 */
public class GZIPSerializer extends Serializer {

    public <T> T readObject(ByteBuffer data, Class<T> c) throws IOException {
        try {
            GZIPCompressedMessage result = new GZIPCompressedMessage();
            byte[] byteArray = new byte[data.remaining()];
            data.get(byteArray);
            GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(byteArray));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] tmp = new byte[9012];
            int read;
            while (in.available() > 0 && ((read = in.read(tmp)) > 0)) {
                out.write(tmp, 0, read);
            }
            result.setMessage((Message) Serializer.readClassAndObject(ByteBuffer.wrap(out.toByteArray())));
            return (T) result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.toString());
        }
    }

    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        if (!(object instanceof GZIPCompressedMessage)) return;
        Message message = ((GZIPCompressedMessage) object).getMessage();
        ByteBuffer tempBuffer = ByteBuffer.allocate(512000);
        Serializer.writeClassAndObject(tempBuffer, message);
        ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutput = new GZIPOutputStream(byteArrayOutput);
        gzipOutput.write(tempBuffer.array());
        gzipOutput.flush();
        gzipOutput.finish();
        gzipOutput.close();
        buffer.put(byteArrayOutput.toByteArray());
    }
}
