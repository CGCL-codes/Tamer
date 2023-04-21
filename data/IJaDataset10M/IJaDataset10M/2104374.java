package net.sf.jukebox.aggregator;

/**
 * This object is intended to work in the context of {@link RunnableAggregator}.
 * 
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2007-2008
 */
public interface SafeRunnable extends Runnable {

    /**
     * Work to do in case {@link Runnable#run()} was never called.
     */
    public void cancel();
}
