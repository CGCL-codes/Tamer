package nu.lazy8.util.gen;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *  Description of the Class
 *
 * @author     Lazy Eight Data HB, Thomas Dilts
 * @created    den 5 mars 2002
 */
public class WorkingDialog extends JDialog {

    JLabel filler1, filler2;

    JProgressBar progressBar;

    AnimationThread thread;

    /**
   *  Constructor for the WorkingDialog object
   *
   * @param  frame  Description of the Parameter
   */
    public WorkingDialog(javax.swing.JFrame frame) {
        super(frame, Translator.getTranslation("Working..."), false);
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100) {

            public Dimension getPreferredSize() {
                return new Dimension(300, super.getPreferredSize().height);
            }
        };
        getContentPane().setLayout(new GridLayout(3, 1));
        filler1 = new JLabel(" ");
        filler2 = new JLabel(" ");
        getContentPane().add(filler1);
        getContentPane().add(progressBar);
        getContentPane().add(filler2);
        thread = new AnimationThread();
        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    /**
   *  Description of the Method
   *
   * @param  iNewProg  Description of the Parameter
   */
    public void SetProgress(int iNewProg) {
        progressBar.setValue(iNewProg);
        filler1.paintImmediately(0, 0, 10000, 10000);
        progressBar.paintImmediately(0, 0, 10000, 10000);
        filler2.paintImmediately(0, 0, 10000, 10000);
    }

    /**
   *  Adds a feature to the Notify attribute of the WorkingDialog object
   */
    public void addNotify() {
        super.addNotify();
        thread.start();
    }

    /**
   *  Description of the Method
   */
    public void removeNotify() {
        super.removeNotify();
        thread.stopNow = true;
    }

    class AnimationThread extends Thread {

        boolean stopNow;

        /**
     *Constructor for the AnimationThread object
     */
        AnimationThread() {
            super("About box animation thread");
            stopNow = false;
            setPriority(Thread.MIN_PRIORITY);
        }

        public void run() {
            int whichMessage = 0;
            final String[] theMessages = { Translator.getTranslation("Working..."), Translator.getTranslation("Please wait"), Translator.getTranslation("This may take some time") };
            for (; ; ) {
                if (stopNow) {
                    return;
                }
                long start = System.currentTimeMillis();
                if (whichMessage >= theMessages.length) {
                    whichMessage = 0;
                }
                setTitle(theMessages[whichMessage]);
                whichMessage++;
                if (stopNow) {
                    return;
                }
                try {
                    Thread.sleep(Math.max(0, 3000 - (System.currentTimeMillis() - start)));
                } catch (InterruptedException ie) {
                }
                if (stopNow) {
                    return;
                }
                WorkingDialog.this.repaint();
            }
        }
    }
}
