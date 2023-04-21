package de.huxhorn.sulky.codec.filebuffer;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparseCodecFileBufferTest extends CodecFileBufferTestBase {

    private final Logger logger = LoggerFactory.getLogger(SparseCodecFileBufferTest.class);

    protected void initSparse() {
        sparse = true;
    }

    @Test
    public void readWriteNoMetaSet() {
        CodecFileBuffer<String> instance = new CodecFileBuffer<String>(magicValue, sparse, null, codec, dataFile, indexFile, fileHeaderStrategy);
        for (int i = values.length - 1; i >= 0; i--) {
            instance.set(i, values[i]);
        }
        assertEquals(values.length, (int) instance.getSize());
        for (int i = 0; i < values.length; i++) {
            String value = instance.get(i);
            if (logger.isInfoEnabled()) logger.info("Element #{}={}", i, value);
            assertEquals("Element #" + i + " differs!", values[i], value);
        }
        int index = 0;
        for (String value : instance) {
            if (logger.isInfoEnabled()) logger.info("Element #{}={}", index, value);
            assertEquals("Element #" + index + " differs!", values[index], value);
            index++;
        }
        FileHeader fileHeader = instance.getFileHeader();
        assertEquals(new MetaData(sparse), fileHeader.getMetaData());
        CodecFileBuffer<String> otherInstance = new CodecFileBuffer<String>(magicValue, sparse, null, codec, dataFile, indexFile, fileHeaderStrategy);
        assertEquals(values.length, (int) otherInstance.getSize());
        FileHeader otherHeader = otherInstance.getFileHeader();
        assertEquals(magicValue, otherHeader.getMagicValue());
        assertEquals(new MetaData(sparse), otherHeader.getMetaData());
    }
}
