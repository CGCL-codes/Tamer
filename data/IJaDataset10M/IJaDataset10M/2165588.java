package org.zmpp.zscreen;

import org.zmpp.vm.StatusLine;

/**
 * This status line model implementation takes advantage of the fact that
 * the standard screen model consists of two windows and therefore wraps
 * a screen model, updates go to the upper window.
 * @author Wei-ju Wu
 * @version 1.0
 */
public class StatusLineModel implements StatusLine {

    private BufferedScreenModel screenModel;

    public StatusLineModel(BufferedScreenModel screenModel) {
        this.screenModel = screenModel;
    }

    public void updateStatusScore(String objectName, int score, int steps) {
        System.out.println("(Status) Object: " + objectName + " " + score + "/" + steps);
    }

    public void updateStatusTime(String objectName, int hours, int minutes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
