package org.spantus.work.test;

import org.spantus.chart.SignalSelectionListener;
import org.spantus.logger.Logger;

public class SignalSelectionListenerMock implements SignalSelectionListener {

    Logger log = Logger.getLogger(getClass());

    public void selectionChanged(Double from, Double length) {
        log.debug("Selection changed: from=" + from + "; length= " + length);
    }

    public void play() {
        log.debug("Play");
    }
}
