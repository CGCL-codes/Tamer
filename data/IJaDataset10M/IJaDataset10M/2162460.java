package com.googlecode.pondskum.gui.swing.tablet;

import com.googlecode.pinthura.util.StopWatch;
import com.googlecode.pinthura.util.builder.StopWatchBuilder;
import com.googlecode.pondskum.gui.swing.notifyer.BigpondSwingWorker;
import java.util.List;

public final class TabletSwingWorker extends BigpondSwingWorker {

    private final UpdatableTablet tablet;

    private StopWatch stopWatch;

    TabletSwingWorker(final UpdatableTablet tablet) {
        this.tablet = tablet;
        stopWatch = new StopWatchBuilder().build();
    }

    @Override
    protected void preConnect() {
        stopWatch.start();
    }

    @Override
    protected void postConnect() {
        tablet.updateStatus("Time taken: " + stopWatch.getTimeInSeconds() + " seconds");
    }

    @Override
    protected void process(final List<String> statusList) {
        for (String update : statusList) {
            tablet.updateStatus(update);
        }
    }

    @Override
    protected void done() {
        if (!isCancelled()) {
            tablet.setTabletData(getUsageInformation());
            return;
        }
        tablet.updateStatus("There was an error retrieving your usage data." + getSimpleExceptionMessage());
        notifyFailureListeners();
    }
}
