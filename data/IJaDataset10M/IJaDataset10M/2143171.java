package org.jcvi.trace.sanger.phd;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jcvi.datastore.CachedDataStore;
import org.jcvi.datastore.DataStoreException;
import org.jcvi.datastore.DataStoreIterator;
import org.jcvi.io.IOUtil;
import org.jcvi.util.AbstractLargeIdIterator;
import org.jcvi.util.CloseableIterator;

/**
 * {@code LargePhdDataStore} is a {@link PhdDataStore} implementation
 * to be used a very large phd files or phdballs.  No data contained in this
 * phd file is stored in memory except it's size (which is lazy loaded).
 * This means that each get() or contain() requires re-parsing the phd file
 * which can take some time.  It is recommended that instances of 
 * {@link LargePhdDataStore} are wrapped by {@link CachedDataStore}
 * @author dkatzel
 *
 *
 */
public class LargePhdDataStore implements PhdDataStore {

    private final File phdFile;

    private Integer size = null;

    boolean closed = false;

    /**
     * @param phdFile
     */
    public LargePhdDataStore(File phdFile) {
        if (!phdFile.exists()) {
            throw new IllegalArgumentException("phd file does not exists " + phdFile.getAbsolutePath());
        }
        this.phdFile = phdFile;
    }

    private void checkIfClosed() {
        if (closed) {
            throw new IllegalStateException("datastore is closed");
        }
    }

    @Override
    public synchronized boolean contains(String id) throws DataStoreException {
        checkIfClosed();
        InputStream in = null;
        try {
            in = getRecordFor(id);
            return in != null;
        } catch (FileNotFoundException e) {
            throw new DataStoreException("could not parse phd file " + phdFile, e);
        } finally {
            IOUtil.closeAndIgnoreErrors(in);
        }
    }

    @Override
    public synchronized Phd get(String id) throws DataStoreException {
        checkIfClosed();
        InputStream streamOfRecord;
        try {
            streamOfRecord = getRecordFor(id);
            DefaultPhdFileDataStore datastore = new DefaultPhdFileDataStore();
            PhdParser.parsePhd(streamOfRecord, datastore);
            return datastore.get(id);
        } catch (FileNotFoundException e) {
            throw new DataStoreException("could not parse phd for " + id, e);
        }
    }

    @Override
    public synchronized CloseableIterator<String> getIds() throws DataStoreException {
        checkIfClosed();
        try {
            return new PhdIdIterator();
        } catch (FileNotFoundException e) {
            throw new DataStoreException("could not parse phd file " + phdFile, e);
        }
    }

    @Override
    public synchronized int size() throws DataStoreException {
        checkIfClosed();
        if (size == null) {
            Scanner scanner = null;
            int count = 0;
            try {
                scanner = new Scanner(phdFile);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("BEGIN_SEQUENCE")) {
                        count++;
                    }
                }
                size = count;
            } catch (FileNotFoundException e) {
                throw new DataStoreException("could not parse phd file " + phdFile, e);
            } finally {
                IOUtil.closeAndIgnoreErrors(scanner);
            }
        }
        return size;
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

    @Override
    public synchronized CloseableIterator<Phd> iterator() {
        checkIfClosed();
        return new DataStoreIterator<Phd>(this);
    }

    private InputStream getRecordFor(String id) throws FileNotFoundException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(phdFile);
            final Pattern BEGIN_SEQUENCE_PATTERN = Pattern.compile("BEGIN_SEQUENCE\\s+" + id);
            String line = scanner.nextLine();
            Matcher matcher = BEGIN_SEQUENCE_PATTERN.matcher(line);
            while (!matcher.find() && scanner.hasNextLine()) {
                line = scanner.nextLine();
                matcher = BEGIN_SEQUENCE_PATTERN.matcher(line);
            }
            if (!scanner.hasNextLine()) {
                return null;
            }
            StringBuilder result = new StringBuilder();
            while (scanner.hasNextLine() && !line.startsWith("END_SEQUENCE")) {
                result.append(line + "\n");
                line = scanner.nextLine();
            }
            return new ByteArrayInputStream(result.toString().getBytes());
        } finally {
            IOUtil.closeAndIgnoreErrors(scanner);
        }
    }

    private class PhdIdIterator extends AbstractLargeIdIterator {

        final Pattern BEGIN_SEQUENCE_PATTERN = Pattern.compile("BEGIN_SEQUENCE\\s+(\\S+)");

        private PhdIdIterator() throws FileNotFoundException {
            super(phdFile);
        }

        @Override
        protected void advanceToNextId(Scanner scanner) {
            String line = scanner.nextLine();
            while (!line.startsWith("END_SEQUENCE") && scanner.hasNextLine()) {
                line = scanner.nextLine();
            }
        }

        @Override
        protected String getNextId(Scanner scanner) {
            Matcher matcher = BEGIN_SEQUENCE_PATTERN.matcher(scanner.nextLine());
            matcher.find();
            return matcher.group(1);
        }
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean isClosed() throws DataStoreException {
        return closed;
    }
}
