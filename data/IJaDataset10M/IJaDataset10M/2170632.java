package sinalgo.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import sinalgo.gui.GuiHelper;

/**
 * This is a JDialog that shows the percentual progress of a action.
 */
@SuppressWarnings("serial")
public class PercentualProgressDialog extends JDialog {

    private ProgressBarUser pBU;

    private JPanel jp = new JPanel();

    private JProgressBar jPB = new JProgressBar(0, 100);

    private JButton cancel = new JButton("Cancel");

    private JPanel buttonPanel = new JPanel();

    /**
	 * This is the constructor for the progress bar.
	 *
	 * @param pbu The ProgressBarUser using this progress bar.
	 * @param title The title of the Dialog.
	 */
    public PercentualProgressDialog(ProgressBarUser pbu, String title) {
        super();
        this.setTitle(title);
        create(pbu);
    }

    /**
	 * Constructs a progress bar that is attached to a parent and is modal (blocks
	 * the parent until the progressbar is closed).
	 *
	 * @param pbu The ProgressBarUser using this progress bar.
	 * @param parent The parent JDialog to attach the ProgressBar to.
	 * @param title The title of the Dialog.
	 */
    public PercentualProgressDialog(ProgressBarUser pbu, JDialog parent, String title) {
        super(parent, title, true);
        create(pbu);
    }

    /**
	 * Creates a ProgressBar depending on the progressbarUser and the title of the 
	 * 
	 * @param pbu The ProgressBarUser that uses this ProgressBar.
	 */
    public void create(ProgressBarUser pbu) {
        GuiHelper.setWindowIcon(this);
        pBU = pbu;
        jPB.setStringPainted(true);
        jp.add(jPB);
        buttonPanel.add(cancel);
        this.setLayout(new BorderLayout());
        this.add(BorderLayout.NORTH, jp);
        this.add(BorderLayout.SOUTH, buttonPanel);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setSize(180, 90);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pBU.cancelClicked();
                setTitle("Undoing...");
                cancel.setEnabled(false);
            }
        });
    }

    /**
	 * This method initializes the ProgressBar and starts the update Thread.
	 */
    public void init() {
        UpdateThread updateThread = new UpdateThread();
        updateThread.start();
        this.setVisible(true);
    }

    /**
	 * This method is used to destroy the Progress. It sets it invisible.
	 */
    public void finish() {
        this.dispose();
    }

    /**
	 * This method resets the value of the progress bar.
	 * 
	 * @param percent The percentage of the progress.
	 */
    public void setPercentage(double percent) {
        jPB.setValue((int) (percent));
    }

    private class UpdateThread extends Thread {

        public void run() {
            pBU.performMethod();
        }
    }
}
