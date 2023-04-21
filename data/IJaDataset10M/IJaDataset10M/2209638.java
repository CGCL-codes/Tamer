package org.equanda.tool.shared.ui;

import javolution.lang.TextBuilder;
import org.equanda.tool.shared.Id;
import org.equanda.tool.shared.ToolListener;
import static org.equanda.tool.shared.ToolMain.I18N;
import org.equanda.tool.shared.worker.WorkerThread;

/**
 * Start class for Import Client in the case the user wants the window to be hidden
 *
 * @author NetRom team
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class ToolClientHidden implements ToolClient {

    private WorkerThread workThread;

    private Id appId;

    public ToolClientHidden(WorkerThread thread, Id newAppId) {
        workThread = thread;
        workThread.setListener(new ToolAdapter());
        appId = newAppId;
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                System.out.println(I18N.txt("shutdown-command-received", appId));
                System.out.println(I18N.txt("waiting-to-shutdown", appId));
                workThread.stopWork();
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                }
            }
        });
    }

    private void setInfo(String text) {
        System.out.println(text);
    }

    private void setStatus(String text) {
        System.out.println(text);
    }

    private void showError(String info) {
        System.out.println(I18N.txt("error", appId) + info);
    }

    /** Set the new worker thread. */
    public void setWorker(WorkerThread worker) {
        workThread = worker;
    }

    public void startThread() {
        workThread.start();
    }

    /** implements the import listener */
    class ToolAdapter implements ToolListener {

        public void notifyInfo(final String status, final String file, final String extra) {
            TextBuilder output = TextBuilder.newInstance();
            output.append(I18N.txt(status, appId));
            output.append(' ');
            output.append(extra == null ? "" : extra);
            setInfo(output.toString());
        }

        public void notifyStatus(final String status, final String extra) {
            TextBuilder output = TextBuilder.newInstance();
            output.append(I18N.txt(status, appId));
            output.append(' ');
            output.append(extra == null ? "" : extra);
            setStatus(output.toString());
        }

        public void notifyError(String info) {
            showError(info);
        }

        public void notifyFinished() {
            Runtime.getRuntime().halt(0);
        }
    }
}
