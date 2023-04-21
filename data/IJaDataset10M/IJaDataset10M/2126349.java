package org.jcvi.common.core.seq.read.trace.archive;

import java.io.IOException;
import org.jcvi.common.core.datastore.DataStoreException;
import org.jcvi.common.core.seq.read.trace.archive.AbstractFolderTraceArchiveDataStore;
import org.jcvi.common.core.seq.read.trace.archive.TraceArchiveInfo;
import org.jcvi.common.core.seq.read.trace.archive.TraceArchiveTrace;
import org.junit.Before;
import org.junit.Test;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class TestAbstractFolderTraceArchiveMultiTrace {

    private static class FolderTraceArchiveMultiTraceTestDouble extends AbstractFolderTraceArchiveDataStore {

        public FolderTraceArchiveMultiTraceTestDouble(String rootDirPath, TraceArchiveInfo traceArchiveInfo) {
            super(rootDirPath, traceArchiveInfo);
        }

        @Override
        public TraceArchiveTrace get(String id) throws DataStoreException {
            return null;
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        protected TraceArchiveTrace createTraceArchiveTrace(String id) throws DataStoreException {
            return null;
        }
    }

    FolderTraceArchiveMultiTraceTestDouble sut;

    TraceArchiveInfo mockTraceArchiveInfo;

    String rootDir = "rootDir";

    DataStoreException expectedDataStoreException = new DataStoreException("expected");

    @Before
    public void setup() {
        mockTraceArchiveInfo = createMock(TraceArchiveInfo.class);
        sut = new FolderTraceArchiveMultiTraceTestDouble(rootDir, mockTraceArchiveInfo);
    }

    @Test
    public void constructor() {
        assertSame(rootDir, sut.getRootDirPath());
        assertSame(mockTraceArchiveInfo, sut.getTraceArchiveInfo());
    }

    @Test(expected = NullPointerException.class)
    public void nullRootFolderShouldThrowNPE() {
        new FolderTraceArchiveMultiTraceTestDouble(null, mockTraceArchiveInfo);
    }

    @Test(expected = NullPointerException.class)
    public void nullTraceInfoShouldThrowNPE() {
        new FolderTraceArchiveMultiTraceTestDouble(rootDir, null);
    }

    @Test
    public void contains() throws DataStoreException {
        String hasId = "HasId";
        String doesntHaveId = "DoesntHaveId";
        expect(mockTraceArchiveInfo.contains(hasId)).andReturn(true);
        expect(mockTraceArchiveInfo.contains(doesntHaveId)).andReturn(false);
        replay(mockTraceArchiveInfo);
        assertTrue(sut.contains(hasId));
        assertFalse(sut.contains(doesntHaveId));
        verify(mockTraceArchiveInfo);
    }

    @Test
    public void containsThrowsDataStoreExceptionShouldWrapInTraceDecoderException() throws DataStoreException {
        String errorId = "this id will throw a DataStoreException";
        expect(mockTraceArchiveInfo.contains(errorId)).andThrow(expectedDataStoreException);
        replay(mockTraceArchiveInfo);
        try {
            sut.contains(errorId);
            fail("should throw TraceDecoderException on error");
        } catch (DataStoreException e) {
            assertEquals(expectedDataStoreException.getMessage(), e.getMessage());
        }
        verify(mockTraceArchiveInfo);
    }

    @Test
    public void getNumberOfTraces() throws DataStoreException {
        long size = 1234;
        expect(mockTraceArchiveInfo.getNumberOfRecords()).andReturn(size);
        replay(mockTraceArchiveInfo);
        assertEquals(size, sut.getNumberOfRecords());
        verify(mockTraceArchiveInfo);
    }

    @Test
    public void exceptionThrownOnGetNumberOfTraces() throws DataStoreException {
        expect(mockTraceArchiveInfo.getNumberOfRecords()).andThrow(expectedDataStoreException);
        replay(mockTraceArchiveInfo);
        try {
            sut.getNumberOfRecords();
            fail("should throw TraceDecoderException on error");
        } catch (DataStoreException e) {
            assertEquals(expectedDataStoreException.getMessage(), e.getMessage());
        }
        verify(mockTraceArchiveInfo);
        verify(mockTraceArchiveInfo);
    }
}
