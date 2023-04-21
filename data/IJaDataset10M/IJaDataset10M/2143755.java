package org.jcvi.common.io.idReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.jcvi.common.core.io.IOUtil;
import org.jcvi.common.core.util.iter.CloseableIterator;

/**
 * {@code DefaultFileIdReader}
 * is an {@link IdReader}
 * that reads in ids from a file, each
 * line should only have 1 id.
 * @author dkatzel
 *
 *
 */
public class DefaultFileIdReader<T> implements IdReader<T> {

    /**
     * 
     */
    private static final char CR = '\n';

    private final File file;

    private final IdParser<T> idParser;

    private Integer numberOfIds = null;

    public DefaultFileIdReader(File file, IdParser<T> idParser) {
        this.file = file;
        this.idParser = idParser;
    }

    private int countNumberOfLines() throws IdReaderException {
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] c = new byte[8192];
            int count = 0;
            int readChars = 0;
            while ((readChars = inputStream.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == CR) {
                        ++count;
                    }
                }
            }
            return count;
        } catch (IOException e) {
            throw new IdReaderException("error reading number of lines", e);
        } finally {
            IOUtil.closeAndIgnoreErrors(inputStream);
        }
    }

    @Override
    public CloseableIterator<T> getIds() throws IdReaderException {
        try {
            return new FileIdIterator<T>(new FileInputStream(file), idParser);
        } catch (FileNotFoundException e) {
            throw new IdReaderException("error trying to read file", e);
        }
    }

    @Override
    public void close() throws IOException {
    }

    private static class FileIdIterator<T> implements CloseableIterator<T> {

        private Scanner scanner;

        private IdParser<T> idParser;

        private String nextValidString;

        boolean needToLookAhead = true;

        boolean hasNext;

        boolean isClosed = false;

        FileIdIterator(InputStream in, IdParser<T> idParser) {
            scanner = new Scanner(in, IOUtil.UTF_8_NAME);
            this.idParser = idParser;
        }

        private synchronized void getNextValidString() {
            needToLookAhead = false;
            boolean done = false;
            hasNext = false;
            while (!done && scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                if (idParser.isValidId(line)) {
                    hasNext = true;
                    nextValidString = line;
                    done = true;
                }
            }
        }

        @Override
        public synchronized boolean hasNext() {
            if (needToLookAhead) {
                getNextValidString();
            }
            if (!hasNext) {
                close();
            }
            return hasNext;
        }

        @Override
        public synchronized T next() {
            if (isClosed) {
                throw new NoSuchElementException("iterator is closed");
            }
            if (needToLookAhead) {
                getNextValidString();
            }
            T id = idParser.parseIdFrom(nextValidString);
            needToLookAhead = true;
            return id;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove() not allowed");
        }

        /**
        * {@inheritDoc}
        */
        @Override
        public synchronized void close() {
            hasNext = false;
            needToLookAhead = false;
            scanner.close();
            isClosed = true;
        }
    }

    @Override
    public Iterator<T> iterator() {
        try {
            return getIds();
        } catch (IdReaderException e) {
            throw new IllegalStateException("could not create iterator over ids", e);
        }
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public synchronized int getNumberOfIds() throws IdReaderException {
        if (numberOfIds == null) {
            numberOfIds = countNumberOfLines();
        }
        return numberOfIds;
    }
}
