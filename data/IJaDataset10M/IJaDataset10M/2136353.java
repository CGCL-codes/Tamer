package net.sourceforge.freejava.regex;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.freejava.collection.iterator.AbstractImmediateIterableX;
import net.sourceforge.freejava.collection.iterator.AbstractImmediateIteratorX;
import net.sourceforge.freejava.collection.iterator.ImmIterIterator;
import net.sourceforge.freejava.collection.iterator.ImmediateIteratorX;
import net.sourceforge.freejava.collection.iterator.IteratorX;
import net.sourceforge.freejava.util.exception.NotImplementedException;

public class DelimitedTokenizer extends AbstractImmediateIterableX<String, IOException> {

    private List<Delimiter> delimiters;

    private int state;

    private Reader in;

    public DelimitedTokenizer(Reader in, Delimiter... delimiters) {
        this(in, Arrays.asList(delimiters));
    }

    public DelimitedTokenizer(Reader in, List<Delimiter> delimiters) {
        if (in == null) throw new NullPointerException("in");
        if (delimiters == null) throw new NullPointerException("delimiters");
        this.delimiters = delimiters;
    }

    private static final int TD_OFFSET = 100;

    private static final int TD_SPAN = 100;

    private static final int TD_ESC_OFFSET;

    private static final int TD_MAXLEN;

    static {
        TD_ESC_OFFSET = TD_SPAN / 2;
        TD_MAXLEN = TD_SPAN / 2;
    }

    void next() throws IOException {
        int c;
        while ((c = in.read()) != -1) {
        }
    }

    class Iter extends AbstractImmediateIteratorX<String, IOException> {

        @Override
        public String next() throws IOException {
            throw new NotImplementedException();
        }
    }

    @Override
    public IteratorX<String, IOException> iterator() {
        return new ImmIterIterator<String, IOException>(new Iter());
    }

    @Override
    public ImmediateIteratorX<String, ? extends IOException> iterator(boolean allowOverlap) {
        return new Iter();
    }
}
