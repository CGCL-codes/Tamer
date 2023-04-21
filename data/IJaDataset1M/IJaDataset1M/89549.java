package net.sourceforge.freejava.flow.units.builtin.sources;

import java.io.IOException;
import java.io.Reader;
import net.sourceforge.freejava.collection.iterator.ImmediateIteratorX;
import net.sourceforge.freejava.flow.units.SOSourceUnit;
import net.sourceforge.freejava.flow.units.builtin.DefaultConfig;
import net.sourceforge.freejava.io.resource.builtin.ReaderSource;
import net.sourceforge.freejava.io.resource.preparation.IStreamReadPreparation;
import net.sourceforge.freejava.meta.codereview.GeneratedByCopyPaste;

public class ReaderSourceUnit extends SOSourceUnit {

    private final IStreamReadPreparation readPreparation;

    private final boolean allowOverlap;

    private ImmediateIteratorX<char[], ? extends IOException> blocks;

    public ReaderSourceUnit(Reader in, boolean allowOverlap, int blockSize) throws IOException {
        this.readPreparation = new ReaderSource(in).forRead().setBlockSize(blockSize);
        this.allowOverlap = allowOverlap;
        reset();
    }

    public ReaderSourceUnit(Reader in) throws IOException {
        this(in, true, DefaultConfig.defaultBlockSize);
    }

    @Override
    public void reset() throws IOException {
        blocks = readPreparation.charBlocks(allowOverlap);
    }

    @Override
    public void flush() throws IOException {
    }

    /**
     * @seecopy {@link InputStreamSourceUnit#pump(int)}
     */
    @GeneratedByCopyPaste
    @Override
    public boolean pump(int timeout) throws IOException, InterruptedException {
        char[] block = blocks.next();
        if (block == null && blocks.isEnded()) return false;
        send(block);
        return true;
    }
}
