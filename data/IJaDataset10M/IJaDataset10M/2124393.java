package com.yougo.mp3tool.core.gui;

import javax.swing.SwingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.yougo.mp3tool.core.extension.function.ProgressTraceableDialog;
import com.yougo.mp3tool.core.extension.function.Worker;

/**
 * Dialog worker:
 *  - creates a worker and passes the dialog ref to it
 *  - starts the worker
 *  - shows the dialog (modal)
 * 
 * The worker knows the work to do and informs the dialog about progress status
 * while completing its job. When the job is done, the dialog is closed and the
 * caller re-gets the control.
 * 
 * @author Ugo Diana
 */
public class JWorker {

    private static final Log log = LogFactory.getLog(JWorker.class);

    private ProgressTraceableDialog pt;

    private Worker worker;

    /**
     * Constructs a new JWorker object.
     * 
     * @param dialog
     * @param worker
     */
    public JWorker(ProgressTraceableDialog dialog, Worker worker) {
        this.pt = dialog;
        this.worker = worker;
    }

    private Thread startThread() {
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    worker.execute(pt);
                } catch (Exception e) {
                    log.error(e);
                }
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        pt.dispose();
                    }
                });
            }
        });
        t.setDaemon(true);
        t.start();
        return t;
    }

    public void start() {
        if (worker.preExecution(pt)) {
            Thread t = startThread();
            pt.show();
            try {
                t.join();
            } catch (InterruptedException e) {
                log.error(e);
            }
            worker.postExecution(pt);
        }
    }
}
