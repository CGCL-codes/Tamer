package pubweb.user;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class JobDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextArea eventLog;

    private JTextArea stdOut;

    private JTextArea stdErr;

    public JobDialog(JFrame owner, String title) {
        super(owner, title);
        JScrollPane eventScrollPane, stdOutScrollPane, stdErrScrollPane;
        JSplitPane innerSplitPane, outerSplitPane;
        TitledBorder border;
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        eventLog = new JTextArea();
        eventLog.setEditable(false);
        eventScrollPane = new JScrollPane(eventLog);
        border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Event-Log:");
        eventScrollPane.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 3, 2, 2), eventScrollPane.getBorder())));
        stdOut = new JTextArea();
        stdOut.setEditable(false);
        stdOutScrollPane = new JScrollPane(stdOut);
        border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Output (stdout):");
        stdOutScrollPane.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 3, 2, 2), stdOutScrollPane.getBorder())));
        stdErr = new JTextArea();
        stdErr.setEditable(false);
        stdErrScrollPane = new JScrollPane(stdErr);
        border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Output (stderr):");
        stdErrScrollPane.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0, 3, 2, 2), stdErrScrollPane.getBorder())));
        innerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, eventScrollPane, stdOutScrollPane);
        innerSplitPane.setDividerSize(10);
        innerSplitPane.setPreferredSize(new Dimension(600, 400));
        innerSplitPane.setDividerLocation(150);
        outerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, innerSplitPane, stdErrScrollPane);
        outerSplitPane.setDividerSize(10);
        outerSplitPane.setPreferredSize(new Dimension(600, 600));
        outerSplitPane.setDividerLocation(400);
        getContentPane().add(outerSplitPane);
        pack();
        setLocation(owner.getSize().width / 2 - getSize().width / 2 + owner.getX(), owner.getSize().height / 2 - getSize().height / 2 + owner.getY());
    }

    public void appendEventLog(String line) {
        eventLog.append(line + "\n");
        eventLog.setCaretPosition(eventLog.getText().length());
    }

    public void appendStdOut(String line) {
        stdOut.append(line + "\n");
        stdOut.setCaretPosition(stdOut.getText().length());
    }

    public void appendStdErr(String line) {
        stdErr.append(line + "\n");
        stdErr.setCaretPosition(stdErr.getText().length());
    }
}
