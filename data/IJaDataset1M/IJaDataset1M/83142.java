package com.faunos.skwish.sys;

import java.io.IOException;
import java.nio.ByteBuffer;
import com.faunos.skwish.SkwishException;
import com.faunos.skwish.sys.Index;
import com.faunos.util.io.MemoryFileChannel;
import com.faunos.util.io.Word;
import junit.framework.TestCase;

public class IndexTest extends TestCase {

    public void testEmpty() throws IOException {
        MemoryFileChannel io = new MemoryFileChannel(32);
        final Word wordType = Word.BYTE;
        Index index = Index.writeNewIndex(io, wordType, 0, 0);
        assertEquals(0, index.getEntryCount());
        assertEquals(0, index.getNextId());
        io.position(0);
        Index index2 = new Index(io);
        assertEquals(0, index2.getEntryCount());
        assertEquals(0, index2.getNextId());
        try {
            index2.readOffsets(0, ByteBuffer.allocate(16));
            fail();
        } catch (SkwishException x) {
        }
    }

    public void testSingleEntry() throws IOException {
        for (Word wordType : Word.values()) testSingleEntryImpl(wordType);
    }

    public void testDoubleEntry() throws IOException {
        for (Word wordType : Word.values()) testDoubleEntryImpl(wordType);
    }

    public void testMutipleEntries_01() throws IOException {
        long[] offsets = new long[] { 3, 7, 9, 18, 22, 78, 79, 80, 99, 121, 11001, 23000 };
        Index index = createMultiEntryIndex(offsets, Word.SHORT);
        verifyEntries(offsets, index, 0, 1, Word.BYTE);
        verifyEntries(offsets, index, 4, 5, Word.SHORT);
        verifyEntries(offsets, index, 4, 5, Word.INT);
        verifyEntries(offsets, index, 4, 5, Word.LONG);
        verifyEntries(offsets, index, 1, 10, Word.SHORT);
    }

    public void testMutipleEntries_02() throws IOException {
        long[] offsets = new long[] { 3, 7, 7, 18, 22, 78, 79, 80, 99, 121, 11001, 23000 };
        Index index = createMultiEntryIndex(offsets, Word.SHORT);
        verifyEntries(offsets, index, 0, 1, Word.BYTE);
        verifyEntries(offsets, index, 4, 5, Word.SHORT);
        verifyEntries(offsets, index, 4, 5, Word.INT);
        verifyEntries(offsets, index, 4, 5, Word.LONG);
        verifyEntries(offsets, index, 1, 10, Word.SHORT);
    }

    public void testMutipleEntries_03() throws IOException {
        long[] offsets = new long[] { 3, 3, 9, 18, 22, 78, 79, 80, 99, 121, 11001, 23000 };
        Index index = createMultiEntryIndex(offsets, Word.SHORT);
        ByteBuffer buffer = ByteBuffer.allocate(64);
        final long nid = index.killNext();
        verifyEntries(offsets, index, 0, 1, Word.BYTE);
        verifyEntries(offsets, index, 4, 5, Word.SHORT);
        verifyEntries(offsets, index, 4, 5, Word.INT);
        verifyEntries(offsets, index, 4, 5, Word.LONG);
        verifyEntries(offsets, index, 1, 10, Word.SHORT);
        buffer.clear();
        index.readOffsets(nid - 1, buffer);
        buffer.flip();
        assertEquals(-23001, buffer.getLong());
        assertEquals(23000, buffer.getLong());
    }

    public void testDeleteSingle() throws IOException {
        long[] offsets = new long[] { 3, 7, 9, 18, 23, 78, 79, 80, 99, 121, 11001, 23000 };
        Index index = createMultiEntryIndex(offsets, Word.SHORT);
        verifyEntries(offsets, index, 0, 1, Word.BYTE);
        verifyEntries(offsets, index, 4, 5, Word.SHORT);
        verifyEntries(offsets, index, 4, 5, Word.INT);
        verifyEntries(offsets, index, 4, 5, Word.LONG);
        verifyEntries(offsets, index, 1, 10, Word.SHORT);
        index.delete(1);
        ByteBuffer buffer = ByteBuffer.allocate(64);
        index.readOffsets(1, buffer);
        buffer.flip();
        assertEquals(IndexMetrics.toDeleted(offsets[1]), buffer.getLong());
        assertEquals(offsets[2], buffer.getLong());
        verifyEntries(offsets, index, 2, 9, Word.SHORT);
    }

    public void testDeleteRange_01() throws IOException {
        long[] offsets = new long[] { 3, 7, 9, 18, 23, 78, 79, 80, 99, 121, 11001, 23000 };
        Index index = createMultiEntryIndex(offsets, Word.SHORT);
        assertEquals(99, index.delete(3, 5));
        ByteBuffer buffer = ByteBuffer.allocate(64);
        index.readOffsets(3, buffer, 6);
        buffer.flip();
        final long expDelOff = IndexMetrics.toDeleted(offsets[3]);
        for (int i = 5; i-- > 0; ) assertEquals(expDelOff, buffer.getLong());
        assertEquals(offsets[3 + 5], buffer.getLong());
    }

    public void testDeleteRange_02() throws IOException {
        long[] offsets = new long[] { 3, 7, 9, 18, 22, 78, 79, 80, 99, 121, 11001, 23000 };
        Index index = createMultiEntryIndex(offsets, Word.SHORT);
        index.delete(4);
        index.delete(5, 4);
        ByteBuffer buffer = ByteBuffer.allocate(64);
        index.readOffsets(4, buffer, 6);
        buffer.flip();
        final long expDelOff = IndexMetrics.toDeleted(offsets[4]);
        for (int i = 5; i-- > 0; ) assertEquals(expDelOff, buffer.getLong());
        assertEquals(offsets[4 + 5], buffer.getLong());
    }

    private void testSingleEntryImpl(Word wordType) throws IOException {
        MemoryFileChannel io = new MemoryFileChannel(32, false);
        Index index = Index.writeNewIndex(io, wordType, 0, 0);
        final int startOffset = 0;
        final int endOffset = 2;
        ByteBuffer offsets = ByteBuffer.allocate(16);
        offsets.putLong(endOffset).flip();
        assertEquals(0, index.getNextId());
        final long entryNo = index.pushNext(endOffset - startOffset);
        assertEquals(0, entryNo);
        assertEquals(1, index.getEntryCount());
        assertEquals(1, index.getNextId());
        offsets.clear();
        index.readOffsets(0, offsets);
        offsets.flip();
        assertEquals(startOffset, offsets.getLong());
        assertEquals(endOffset, offsets.getLong());
    }

    public static Index createMultiEntryIndex(long[] offsets, Word wordType) throws IOException {
        MemoryFileChannel io = new MemoryFileChannel(32, false);
        long lastOffset = offsets[0];
        Index index = Index.writeNewIndex(io, wordType, 0, lastOffset);
        for (int i = 1; i < offsets.length; ++i) {
            long offset = offsets[i];
            if (offset < 0) {
                fail();
            } else {
                index.pushNext(offset - lastOffset);
                lastOffset = offset;
            }
        }
        assertEquals(offsets.length - 1, index.getEntryCount());
        return index;
    }

    public static void verifyEntries(long[] offsets, Index index, int startId, int count, Word readFormat) throws IOException {
        ByteBuffer out = ByteBuffer.allocate((count + 1) * 8);
        index.readOffsets(startId, out, count);
        out.flip();
        if (startId == 0) {
            assertEquals(index.getMetrics().getBaseOffset(), out.getLong());
            ++startId;
            --count;
        }
        while (count-- > 0) assertEquals(offsets[startId++], out.getLong());
    }

    private void testDoubleEntryImpl(Word wordType) throws IOException {
        MemoryFileChannel io = new MemoryFileChannel(32, false);
        Index index = Index.writeNewIndex(io, wordType, 0, 0);
        final int offset1 = 0;
        final int offset2 = 2;
        final int offset3 = 5;
        assertEquals(0, index.getNextId());
        index.pushNext(offset2 - offset1);
        ByteBuffer offsets = ByteBuffer.allocate(24);
        final long entryNo = index.pushNext(offset3 - offset2);
        assertEquals(1, entryNo);
        assertEquals(2, index.getEntryCount());
        assertEquals(2, index.getNextId());
        offsets.clear();
        index.readOffsets(0, offsets, 2);
        offsets.flip();
        assertEquals(offset1, offsets.getLong());
        assertEquals(offset2, offsets.getLong());
        assertEquals(offset3, offsets.getLong());
    }
}
