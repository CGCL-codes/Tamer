package org.simpleframework.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.simpleframework.http.store.Storage;
import org.simpleframework.http.store.Store;
import junit.framework.TestCase;

public class FixedConsumerTest extends TestCase implements Storage, Store {

    private ByteArrayOutputStream buffer;

    public String getContent(String charset) {
        return null;
    }

    public InputStream getInputStream() {
        return null;
    }

    public void write(int octet) throws IOException {
        buffer.write(octet);
    }

    public void write(byte[] array, int off, int size) throws IOException {
        buffer.write(array, off, size);
    }

    public void close() throws IOException {
        return;
    }

    public int size() {
        return buffer.size();
    }

    public Store getStore() {
        return this;
    }

    public Store getStore(int size) {
        return this;
    }

    public void testConsumer() throws IOException {
        testConsumer(10, 10, 10);
        testConsumer(1024, 10, 1024);
        testConsumer(1024, 1024, 1024);
        testConsumer(1024, 1024, 1023);
        testConsumer(1024, 1, 1024);
        testConsumer(1, 1, 1);
        testConsumer(2, 2, 2);
        testConsumer(3, 1, 2);
    }

    public void testConsumer(int entitySize, int dribble, int limitSize) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StringBuffer buf = new StringBuffer();
        limitSize = Math.min(entitySize, limitSize);
        for (int i = 0, line = 0; i < entitySize; i++) {
            String text = "[" + String.valueOf(i) + "]";
            line += text.length();
            buf.append(text);
            if (line >= 48) {
                buf.append("\n");
                line = 0;
            }
        }
        buffer = new ByteArrayOutputStream();
        String requestBody = buf.toString();
        FixedConsumer consumer = new FixedConsumer(this, limitSize);
        Cursor cursor = new DribbleCursor(new StringCursor(requestBody), dribble);
        byte[] requestBytes = requestBody.getBytes("UTF-8");
        while (!consumer.isFinished()) {
            consumer.consume(cursor);
        }
        byte[] consumedBytes = buffer.toByteArray();
        assertEquals(buffer.size(), limitSize);
        for (int i = 0; i < limitSize; i++) {
            if (consumedBytes[i] != requestBytes[i]) {
                throw new IOException("Fixed consumer modified the request!");
            }
        }
    }
}
