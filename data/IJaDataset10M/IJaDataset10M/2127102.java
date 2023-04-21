package jezuch.utils.starmapper3;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import jezuch.utils.io.IOUtils;
import jezuch.utils.io.ReaderSource;
import jezuch.utils.starmapper3.model.Node;
import jezuch.utils.starmapper3.model.NodeInfo;
import jezuch.utils.starmapper3.model.Region;
import jezuch.utils.starmapper3.model.ReportReader;

/**
 * This is a {@link Report} that reads its data from a {@link Reader}. This is
 * only a thin wrapper around a {@link ReaderSource}, the constructor does
 * nothing more than store its arguments for later use. Proper parse is done in
 * an {@link Iterator} and this is a reason for {@link ReaderSource} that
 * recreates a new {@link Reader} each time it is needed. {@link Iterator}
 * returned from this class ({@link #iterator()}) will read {@link NodeInfo}'s
 * on the fly from a {@link Reader} using the {@link ReaderSource}. Each
 * instance of {@link Iterator} opens a new {@link Reader} - it stays open until
 * the end of stream is reached or error ({@link IOException} or
 * {@link ParseException}) occurs - if you stop processing in the middle it
 * will stay open and will be closed by finalization and it is not desirable.
 * The two mentioned exceptions can't be thrown directly by the {@link Iterator}
 * as its methods do not declare any exceptions, so they're wrapped in a
 * {@link RuntimeException}. A static method
 * {@link IOUtils#checkException(RuntimeException)} can be used to easily unwrap
 * and rethrow them if you remember to catch {@link RuntimeException} where
 * necessary. Note that this system is powerless if an exception is thrown from
 * any other class than {@link ReaderSourceReport}'s {@link Iterator} so use
 * with caution.
 * 
 * @author ksobolewski
 */
public class ReaderSourceReport<N extends Node, R extends Region, I extends NodeInfo<N, R, I>> extends AbstractSet<I> implements Report<N, R, I> {

    private final ReportReader<N, R, I> reader;

    private final ReaderSource source;

    private final NodesMap<N, R> context;

    private final R reporter;

    private final int sourceTime;

    /**
	 * Creates a new {@link ReaderSourceReport} that <em>will</em> read the
	 * {@link NodeInfo}s from a {@link Reader} produced by the given
	 * {@link ReaderSource} using the given {@link ReportReader}. The reporter
	 * returned by {@link NodeInfo}s produced by this {@link Report} will be
	 * the {@link Region} given to this constructor. The given
	 * {@code sourceTime} will be used to calculate (or set) {@link NodeInfo}s'
	 * report age. All is done in a context of a {@link NodesMap}.
	 * 
	 * @param reader
	 *            the {@link ReportReader} that will interpret the data from a
	 *            {@link Reader}
	 * @param source
	 *            athe source for {@link Reader}s from which {@link NodeInfo}s
	 *            will be read
	 * @param context
	 *            the {@link NodesMap} in which the {@link Report} is placed
	 * @param reporter
	 *            the {@link Report}'s reporter
	 * @param sourceTime
	 *            the time from which this {@link Report} is
	 */
    public ReaderSourceReport(ReportReader<N, R, I> reader, ReaderSource source, NodesMap<N, R> context, R reporter, int sourceTime) {
        if (reader == null) throw new NullPointerException("reader");
        if (source == null) throw new NullPointerException("source");
        if (context == null) throw new NullPointerException("context");
        if (reporter == null) throw new NullPointerException("reporter");
        this.reader = reader;
        this.source = source;
        this.context = context;
        this.reporter = reporter;
        this.sourceTime = sourceTime;
    }

    public Map<N, I> getNodesLookup() {
        return new DefaultNodesLookup<N, R, I>(this);
    }

    @Override
    public Iterator<I> iterator() {
        try {
            return reader.getIterator(source, context, reporter, sourceTime);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    @SuppressWarnings("unused")
    public int size() {
        int ret = 0;
        for (NodeInfo<N, R, I> entry : this) ret++;
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Report && ((Report) o).getNodesLookup().equals(getNodesLookup());
    }
}
