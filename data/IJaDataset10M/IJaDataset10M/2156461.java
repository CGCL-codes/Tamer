package org.isurf.spmiddleware.reader;

/**
 * Mock implementation of {@link ReaderManager} for unit testing.
 */
public class MockReaderManager implements ReaderManager {

    /** {@inheritDoc} */
    public Reader getReader(String logicalReader) {
        return new MockReader();
    }

    /** {@inheritDoc} */
    public void register(ReaderProfile readerProfile) {
    }
}
