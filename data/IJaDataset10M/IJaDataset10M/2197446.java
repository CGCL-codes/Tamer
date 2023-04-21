package net.yapbam.gui.dialogs;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingWorker;
import net.astesana.ajlib.swing.dialog.AbstractDialog;
import net.yapbam.gui.LocalizationData;

@SuppressWarnings("serial")
public abstract class LongTaskDialog<T, V> extends AbstractDialog<T, V> {

    private static final long DEFAULT_DELAY = 500;

    private static final int MINIMUM_TIME_VISIBLE = 1000;

    private long setVisibleTime;

    private long delay;

    private SwingWorker<?, ?> worker;

    private SwingWorker<Object, Void> showWorker;

    /** Constructor.
	 * @param owner The dialog owner
	 * @param title The dialog title
	 * @param data 
	 */
    public LongTaskDialog(Window owner, String title, T data) {
        super(owner, title, data);
        this.delay = DEFAULT_DELAY;
        this.okButton.setVisible(false);
        this.cancelButton.setText(LocalizationData.get("GenericButton.cancel"));
        this.cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                worker.cancel(true);
            }
        });
    }

    /** Sets the delay before the dialog is shown.
	 * <BR>This class allows you to specify a delay in opening the dialog.
	 * @param delay The delay in ms. Long.MAX_VALUE to prevent the dialog to be shown.
	 * The default value is 500 ms.
	 * <BR>Note that this method must be called before calling setVisible with a true argument.
	 */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            this.worker = getWorker();
            worker.execute();
            this.showWorker = new SwingWorker<Object, Void>() {

                @Override
                protected Object doInBackground() throws Exception {
                    Thread.sleep(delay);
                    return null;
                }

                @Override
                protected void done() {
                    if (!worker.isDone()) {
                        doShow();
                    }
                }
            };
            this.showWorker.execute();
        } else {
            if (this.showWorker != null) this.showWorker.cancel(true);
            long delay = MINIMUM_TIME_VISIBLE - (System.currentTimeMillis() - this.setVisibleTime);
            try {
                if (delay > 0) {
                    Thread.sleep(delay);
                }
            } catch (InterruptedException e) {
            }
            super.setVisible(visible);
        }
    }

    protected abstract SwingWorker<?, ?> getWorker();

    private void doShow() {
        this.setVisibleTime = System.currentTimeMillis();
        super.setVisible(true);
    }
}
