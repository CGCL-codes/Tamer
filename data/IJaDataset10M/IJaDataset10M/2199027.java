package org.vardb.blast;

import org.vardb.util.CCommandLine;
import org.vardb.util.CStringHelper;

public class CPsiBlastParams extends CAbstractBlastParams {

    protected int wordsize = 3;

    protected int iterations = 1;

    public CPsiBlastParams() {
        super("blastpgp");
        this.database = "vardbaa";
    }

    public CPsiBlastParams(String query) {
        this();
        this.query = query;
    }

    public CPsiBlastParams(String query, int iterations) {
        this(query);
        this.iterations = iterations;
    }

    public int getWordsize() {
        return this.wordsize;
    }

    public void setWordsize(final int wordsize) {
        this.wordsize = wordsize;
    }

    public int getIterations() {
        return this.iterations;
    }

    public void setIterations(final int iterations) {
        this.iterations = iterations;
    }

    @Override
    protected void addArgs(CCommandLine commands) {
        commands.addArg("-j", this.iterations);
        commands.addArg("-b", this.maxresults);
        commands.addArg("-W", this.wordsize);
    }

    @Override
    public String toString() {
        return CStringHelper.toString(this);
    }
}
