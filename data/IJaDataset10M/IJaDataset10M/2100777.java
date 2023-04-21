package org.ceno.tracker.cli.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.TimerTask;

/**
 * @author Andre Albert &lt;andre.albert82@googlemail.com&gt
 * @created 23.01.2010
 * @since 0.0.2
 */
public class TimerTaskScheduler extends TimerTask implements IScheduler {

    private Collection<IRefreshable> refreshables = new HashSet<IRefreshable>();

    /**
	 *{@inheritDoc}
	 **/
    @Override
    public void run() {
        for (IRefreshable r : refreshables) {
            r.refresh();
        }
    }

    /**
	*{@inheritDoc}
	**/
    @Override
    public void degister(IRefreshable refreshable) {
        refreshables.remove(refreshable);
    }

    /**
	*{@inheritDoc}
	**/
    @Override
    public void register(IRefreshable refreshable) {
        refreshables.add(refreshable);
    }
}
