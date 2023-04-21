package net.conquiris.search;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import net.conquiris.api.search.ManagedReaderSupplier;
import net.conquiris.api.search.Reader;
import net.conquiris.api.search.ReaderSupplier;
import org.apache.lucene.index.IndexReader;
import com.google.common.base.Stopwatch;
import com.google.common.io.Closeables;

/**
 * Default managed reader supplier implementation.
 * @author Andres Rodriguez
 */
final class DefaultManagedReaderSupplier extends AbstractReaderSupplier implements ManagedReaderSupplier {

    /** Reader supplier to manage. */
    private final ReaderSupplier source;

    /** Reader hold time in ms. */
    private final long holdTime;

    /** Current watch. */
    private final Stopwatch watch;

    /** Current reader. */
    private volatile Reader reader;

    /** Reused count. */
    private final AtomicLong reused = new AtomicLong();

    /** Reopened count. */
    private final AtomicLong reopened = new AtomicLong();

    /**
	 * Constructor.
	 * @param source Reader supplier to manage.
	 * @param holdTime Reader hold time (ms). If negative, zero will be used.
	 */
    DefaultManagedReaderSupplier(ReaderSupplier source, long holdTime) {
        this.source = checkNotNull(source, "The unmanaged reader source must be provided");
        this.holdTime = Math.max(0, holdTime);
        this.watch = this.holdTime > 0 ? new Stopwatch() : null;
    }

    public long getReused() {
        return reused.get();
    }

    public long getReopened() {
        return reopened.get();
    }

    @Override
    Reader doGet() throws IOException {
        while (true) {
            final Reader current = reader;
            final Reader opened;
            if (current == null) {
                opened = checkNotNull(source.get(), "The source supplier returned a null reader");
            } else {
                opened = null;
            }
            final Reader ref = tryGet(opened);
            if (ref != null) {
                return ref;
            }
        }
    }

    /**
	 * Try get a new managed reference.
	 * @param opened Opened reader.
	 * @return The managed reference or null if the operation has to be retried.
	 */
    private synchronized Reader tryGet(Reader opened) throws IOException {
        boolean used = false;
        boolean ok = false;
        try {
            final Reader current = reader;
            if (current == null) {
                if (opened != null) {
                    start(opened);
                    used = true;
                    if (reader == null) {
                        ok = true;
                        return opened;
                    }
                } else {
                    ok = true;
                    return null;
                }
            } else if (watch == null || watch.elapsedMillis() > holdTime) {
                IndexReader indexReader = reader.get();
                IndexReader reopened = IndexReader.openIfChanged(indexReader);
                if (reopened != null) {
                    start(Reader.of(reopened, true));
                    this.reopened.incrementAndGet();
                } else {
                    this.reused.incrementAndGet();
                }
            } else {
                this.reused.incrementAndGet();
            }
            reader.get().incRef();
            ok = true;
            return reader;
        } finally {
            if (!ok) {
                dispose();
            }
            if (opened != null && !used) {
                Closeables.closeQuietly(opened.get());
            }
        }
    }

    public synchronized void dispose() {
        if (reader != null) {
            Closeables.closeQuietly(reader.get());
            reader = null;
        }
        if (watch != null) {
            watch.reset();
        }
    }

    private void start(Reader newReader) {
        dispose();
        if (newReader.isReopenable()) {
            this.reader = newReader;
            if (watch != null) {
                watch.start();
            }
        }
    }
}
