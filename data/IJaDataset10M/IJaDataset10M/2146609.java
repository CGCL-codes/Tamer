package hermes;

import java.awt.Toolkit;
import org.jdesktop.swingworker.SwingWorker;

/**
 * AbstractResultExtracterWrapper.java
 *
 * Created on 23 november 2006, 13:00
 * @author Jethro Borsje
 */
public abstract class AbstractResultExtracterWrapper extends SwingWorker<Void, Void> implements ResultExtracterWrapper {

    public void done() {
        Toolkit.getDefaultToolkit().beep();
    }

    public void setQueryExecutionProgress(double p_queryExecutionProgress) {
        double progress = 15 + p_queryExecutionProgress / 100 * 60;
        setProgress((int) Math.round(progress));
    }

    public void setResultDisplayProgress(double p_resultDisplayProgress) {
        double progress = 75 + p_resultDisplayProgress / 100 * 24;
        setProgress((int) Math.round(progress));
    }
}
