package com.outOfCaffeineException.jTimeTracking.application.layer1Front.gui.tachesJTable.timers;

import com.outOfCaffeineException.jTimeTracking.application.layer1Front.gui.tachesJTable.TacheTable;
import com.outOfCaffeineException.jTimeTracking.application.layer1Front.gui.tachesJTable.model.TableModel;

public class TacheTimer extends Thread {

    protected boolean continueTh;

    protected TacheTable table;

    public TacheTimer(TacheTable t) {
        continueTh = true;
        table = t;
    }

    public void run() {
        while (continueTh) {
            try {
                ((TableModel) table.getModel()).fireTableDataChanged();
                Thread.sleep(900);
            } catch (Exception e) {
            }
        }
    }

    public void setContinueTh(boolean continueTh) {
        this.continueTh = continueTh;
    }
}
