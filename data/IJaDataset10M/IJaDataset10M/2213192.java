package org.jcvi.fastX.fasta.seq;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jcvi.datastore.CachedDataStore;
import org.jcvi.datastore.DataStoreException;
import org.jcvi.fastX.fasta.FastaParser;
import org.jcvi.fastX.fasta.LargeFastaIdIterator;
import org.jcvi.fastX.fasta.SequenceFastaRecordUtil;
import org.jcvi.io.IOUtil;
import org.jcvi.util.CloseableIterator;

/**
 * {@code LargeNucleotideFastaFileDataStore} is an implementation
 * of {@link AbstractNucleotideFastaFileDataStore} which does not
 * store any Fasta record data 
 * in memory except it's size (which is lazy loaded).
 * This means that each get() or contain() requires re-parsing the fastq file
 * which can take some time.  It is recommended that instances are wrapped
 * in {@link CachedDataStore}.
 * @author dkatzel
 *
 *
 */
public class LargeNucleotideFastaFileDataStore extends AbstractNucleotideFastaFileDataStore {

    private static final Pattern NEXT_ID_PATTERN = Pattern.compile("^>(\\S+)");

    private final File fastaFile;

    private Integer size;

    /**
     * Construct a {@link LargeNucleotideFastaFileDataStore}
     * for the given Fasta file and the given {@link NucleotideFastaRecordFactory}.
     * @param fastaFile the Fasta File to use, can not be null.
     * @param fastaRecordFactory the NucleotideFastaRecordFactory implementation to use.
     * @throws NullPointerException if fastaFile is null.
     */
    public LargeNucleotideFastaFileDataStore(File fastaFile, NucleotideFastaRecordFactory fastaRecordFactory) {
        super(fastaRecordFactory);
        if (fastaFile == null) {
            throw new NullPointerException("fasta file can not be null");
        }
        this.fastaFile = fastaFile;
    }

    /**
     * Convenience constructor using the {@link DefaultNucleotideFastaRecordFactory}.
     * This call is the same as {@link #LargeNucleotideFastaFileDataStore(File,NucleotideFastaRecordFactory)
     * new LargeNucleotideFastaFileDataStore(fastaFile,DefaultNucleotideFastaRecordFactory.getInstance());}
     * @see LargeNucleotideFastaFileDataStore#LargeQualityFastaFileDataStore(File, NucleotideFastaRecordFactory)
     */
    public LargeNucleotideFastaFileDataStore(File fastaFile) {
        super();
        if (fastaFile == null) {
            throw new NullPointerException("fasta file can not be null");
        }
        this.fastaFile = fastaFile;
    }

    @Override
    public boolean visitRecord(String id, String comment, String entireBody) {
        return true;
    }

    @Override
    public boolean contains(String id) throws DataStoreException {
        checkNotYetClosed();
        InputStream in = null;
        try {
            in = getRecordFor(id);
            return in != null;
        } catch (FileNotFoundException e) {
            throw new DataStoreException("could not get record for " + id, e);
        } finally {
            IOUtil.closeAndIgnoreErrors(in);
        }
    }

    @Override
    public synchronized NucleotideSequenceFastaRecord get(String id) throws DataStoreException {
        checkNotYetClosed();
        InputStream in = null;
        DefaultNucleotideFastaFileDataStore datastore = null;
        try {
            in = getRecordFor(id);
            if (in == null) {
                return null;
            }
            datastore = new DefaultNucleotideFastaFileDataStore(getFastaRecordFactory());
            FastaParser.parseFasta(in, datastore);
            return datastore.get(id);
        } catch (FileNotFoundException e) {
            throw new DataStoreException("could not get record for " + id, e);
        } finally {
            IOUtil.closeAndIgnoreErrors(in, datastore);
        }
    }

    @Override
    public synchronized CloseableIterator<String> getIds() throws DataStoreException {
        checkNotYetClosed();
        return LargeFastaIdIterator.createNewIteratorFor(fastaFile);
    }

    @Override
    public synchronized int size() throws DataStoreException {
        checkNotYetClosed();
        if (size == null) {
            try {
                Scanner scanner = new Scanner(fastaFile);
                int counter = 0;
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    Matcher matcher = NEXT_ID_PATTERN.matcher(line);
                    if (matcher.find()) {
                        counter++;
                    }
                }
                size = counter;
            } catch (FileNotFoundException e) {
                throw new IllegalStateException("could not get record count");
            }
        }
        return size;
    }

    @Override
    public synchronized CloseableIterator<NucleotideSequenceFastaRecord> iterator() {
        checkNotYetClosed();
        LargeNucleotideFastaIterator iter = new LargeNucleotideFastaIterator(fastaFile);
        iter.start();
        return iter;
    }

    private InputStream getRecordFor(String id) throws FileNotFoundException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(fastaFile);
            String expectedHeader = String.format(">%s", id);
            String line = scanner.nextLine();
            boolean done = false;
            while (!done) {
                if (line.startsWith(expectedHeader)) {
                    String currentId = SequenceFastaRecordUtil.parseIdentifierFromIdLine(line);
                    if (id.equals(currentId)) {
                        done = true;
                        continue;
                    }
                }
                if (!scanner.hasNextLine()) {
                    done = true;
                    continue;
                }
                line = scanner.nextLine();
            }
            if (!scanner.hasNextLine()) {
                return null;
            }
            StringBuilder record = new StringBuilder(line).append("\n");
            line = scanner.nextLine();
            while (!line.startsWith(">") && scanner.hasNextLine()) {
                record.append(line).append("\n");
                line = scanner.nextLine();
            }
            if (!scanner.hasNextLine()) {
                record.append(line).append("\n");
            }
            return new ByteArrayInputStream(record.toString().getBytes());
        } finally {
            IOUtil.closeAndIgnoreErrors(scanner);
        }
    }
}
