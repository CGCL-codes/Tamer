package net.sourceforge.olduvai.lrac.ui;

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;
import net.sourceforge.olduvai.lrac.LiveRACLogPlayer;
import net.sourceforge.olduvai.lrac.LiveRACSplitAxisLogger;

public class VisualLogRequest extends JDialog {

    static final String TITLE = "Logging request";

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    static final String LABELTEXT = "<html>Can we send <b><i>anonymized</i></b> application usage data back to the server?  No individually identifiable information and no data details will be transmitted.";

    static final String MOREINFOTEXT = "<html>LiveRAC is an experimental visualization system.  In order to better understand how our users are using the visualization, we would like to collect data on the different \"representation levels\" that they use to look at the data.  The log does NOT collect any information concerning the values inside of each grid cell.  Instead, only the number of grid lines on the X and Y axis, and the position of the grid lines is transmitted. This allows us to see how users of LiveRAC interacted with the system without revealing any information about the data they were looking at.";

    final LiveRACSplitAxisLogger[] loggers;

    public VisualLogRequest(Frame owner, LiveRACSplitAxisLogger[] inLoggers) throws HeadlessException {
        super(owner, TITLE, false);
        this.loggers = inLoggers;
        setSize(250, 300);
        final int parentWidth = owner.getWidth();
        final int parentHeight = owner.getHeight();
        final int parentX = owner.getLocation().x;
        final int parentY = owner.getLocation().y;
        setLocation(parentX + (parentWidth / 2), parentY + (parentHeight / 2));
        setLayout(new MigLayout("fill", "[][]", "[][][][nogrid,top,40]"));
        setAlwaysOnTop(true);
        JButton moreInfoButton = new JButton("More information");
        moreInfoButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                moreInfo();
            }
        });
        JButton viewRawButton = new JButton("View raw log data");
        viewRawButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showLog();
            }
        });
        JButton viewVisualButton = new JButton("View log data visually");
        viewVisualButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (LiveRACSplitAxisLogger l : loggers) {
                    try {
                        l.flush();
                    } catch (IOException e1) {
                        System.err.println("error flushing stream for log file: " + l.getFullPath());
                    }
                }
                LiveRACLogPlayer lp = new LiveRACLogPlayer(loggers[0].getPath(), loggers[0].getFilename(), false);
            }
        });
        JButton yesButton = new JButton("Yes");
        yesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (LiveRACSplitAxisLogger l : loggers) {
                    try {
                        l.cleanup();
                        l.uploadLog();
                    } catch (IOException e1) {
                        System.err.println("Error uploading log: " + l.getFullPath());
                    }
                }
                setVisible(false);
                dispose();
            }
        });
        JButton noButton = new JButton("No");
        noButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        add(new JLabel(LABELTEXT), "grow");
        add(moreInfoButton, "wrap, pad 5 5 5 5");
        add(viewRawButton, "skip, pad 5 5 5 5, wrap");
        add(viewVisualButton, "skip, pad 5 5 5 5, wrap");
        add(noButton, "grow, pad 10 5 5 5");
        add(yesButton, "grow, pad 10 5 5 5");
        pack();
        setVisible(true);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        JFrame f = new JFrame("Test");
        f.setSize(300, 300);
        f.setLocation(300, 300);
        VisualLogRequest vlr = new VisualLogRequest(f, null);
        vlr.setVisible(true);
    }

    private void showLog() {
        JDialog logDialog = new JDialog(this, "Raw log text", true);
        JTextPane text = new JTextPane();
        logDialog.add(new JScrollPane(text));
        try {
            text.setText("-- X log: \n" + loggers[0].readLogFile() + "\n-- Y log: \n" + loggers[1].readLogFile());
        } catch (IOException e) {
            text.setText("Unable to read log file. :(");
        }
        logDialog.setSize(640, 480);
        logDialog.setVisible(true);
    }

    private void moreInfo() {
        JOptionPane.showMessageDialog(this, MOREINFOTEXT, "More information", JOptionPane.INFORMATION_MESSAGE);
    }
}
