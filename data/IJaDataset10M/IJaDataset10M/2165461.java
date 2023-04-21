package jsnoopy.swingui;

import jsnoopy.Assert;
import jsnoopy.Trace;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import javax.swing.border.*;

/**
 * Title:        JSnoopy
 * Description:  Regression testing based on event sequences.
 * Copyright:    Copyright (c) 2002
 * Company:      Memorial University of Newfoundland
 * @author Theodore S. Norvell
 * @version 1.0
 *
 * TODO propagate changes made to comment panes back to the traces.
 */
public class DoneReplayDialog extends JDialog {

    private MainWindow owner;

    private JPanel bottomPanel = new JPanel();

    private JPanel messagePanel = new JPanel();

    private JButton replaceButton = new JButton();

    private JPanel buttonPanel = new JPanel();

    private JButton discardButton = new JButton();

    private JButton saveButton = new JButton();

    private JLabel messageLabel = new JLabel();

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private GridBagLayout gridBagLayout2 = new GridBagLayout();

    private JPanel tracePanel = new JPanel();

    private JScrollPane jScrollPane1 = new JScrollPane();

    private JTextPane jTextPane = new JTextPane();

    private BorderLayout borderLayout1 = new BorderLayout();

    private JPanel commentPanel = new JPanel();

    private JPanel jPanel6 = new JPanel();

    private JScrollPane jScrollPane2 = new JScrollPane();

    private JTextPane commentPane = new JTextPane();

    private BorderLayout borderLayout2 = new BorderLayout();

    public DoneReplayDialog(MainWindow owner) {
        super(owner, "Replay complete", true);
        this.owner = owner;
        try {
            jbInit();
            finishInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(gridBagLayout2);
        bottomPanel.setLayout(gridBagLayout1);
        messagePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        replaceButton.setToolTipText("Save the new trace replacing the old trace.");
        replaceButton.setText("Replace old trace");
        replaceButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                replaceOldTrace();
            }
        });
        buttonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        discardButton.setToolTipText("Discard the new trace");
        discardButton.setText("Discard new trace");
        discardButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DoneReplayDialog.this.setVisible(false);
            }
        });
        saveButton.setToolTipText("Save the new trace in a file.");
        saveButton.setText("Save as new file");
        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveNewToFile();
            }
        });
        this.setModal(true);
        this.setTitle("Replay Complete");
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setMinimumSize(new Dimension(200, 200));
        jScrollPane1.setPreferredSize(new Dimension(200, 200));
        jTextPane.setMinimumSize(new Dimension(25, 80));
        jTextPane.setPreferredSize(new Dimension(40, 80));
        tracePanel.setLayout(borderLayout1);
        commentPanel.setLayout(borderLayout3);
        jPanel6.setLayout(borderLayout2);
        jScrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setMinimumSize(new Dimension(200, 100));
        jScrollPane2.setPreferredSize(new Dimension(200, 100));
        jLabel1.setText("Comment");
        jLabel2.setText("Traces (Old is Yellow, New is Pink)");
        tracePanel.setBorder(BorderFactory.createEtchedBorder());
        bottomPanel.setBorder(BorderFactory.createEtchedBorder());
        commentPanel.setBorder(BorderFactory.createEtchedBorder());
        this.getContentPane().add(bottomPanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
        buttonPanel.add(saveButton, null);
        buttonPanel.add(replaceButton, null);
        buttonPanel.add(discardButton, null);
        bottomPanel.add(messagePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        messagePanel.add(messageLabel, null);
        this.getContentPane().add(tracePanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        tracePanel.add(jScrollPane1, BorderLayout.CENTER);
        tracePanel.add(jLabel2, BorderLayout.NORTH);
        this.getContentPane().add(commentPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
        commentPanel.add(jPanel6, BorderLayout.CENTER);
        jPanel6.add(jScrollPane2, BorderLayout.CENTER);
        commentPanel.add(jLabel1, BorderLayout.NORTH);
        jScrollPane2.getViewport().add(commentPane, null);
        jScrollPane1.getViewport().add(jTextPane, null);
        bottomPanel.add(buttonPanel, new GridBagConstraints(0, 1, 2, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    void finishInit() {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontFamily(def, "SansSerif");
        Style regular0 = jTextPane.addStyle("regular", def);
        Style regular1 = commentPane.addStyle("regular", def);
        Style s0 = jTextPane.addStyle("old", regular0);
        Style s1 = commentPane.addStyle("old", regular1);
        StyleConstants.setBackground(s0, Color.yellow);
        StyleConstants.setBackground(s1, Color.yellow);
        s0 = jTextPane.addStyle("new", regular0);
        StyleConstants.setBackground(s0, Color.pink);
        s1 = commentPane.addStyle("new", regular1);
        StyleConstants.setBackground(s1, Color.pink);
    }

    void setData(boolean equal, Trace newTrace, Trace oldTrace) {
        boolean equalComments = newTrace.getComment().equals(oldTrace.getComment());
        if (equal) {
            if (equalComments) {
                messageLabel.setText("Passed");
                saveButton.setEnabled(false);
                replaceButton.setEnabled(false);
            } else {
                messageLabel.setText("Passed, but comments differ");
            }
        } else {
            messageLabel.setText("Discrepency found");
        }
        if (equalComments) {
            addText(newTrace.getComment(), "regular", commentPane);
        } else {
            System.out.println("Old Comment: " + oldTrace.getComment());
            System.out.println("New Comment: " + newTrace.getComment());
            Object[] v = stringToArray(oldTrace.getComment());
            Object[] h = stringToArray(newTrace.getComment());
            displayDifferencesToPane(v, h, commentPane);
        }
        if (equal) {
            copyTraceToPane(newTrace);
        } else {
            Object[] v = traceToArray(oldTrace);
            Object[] h = traceToArray(newTrace);
            displayDifferencesToPane(v, h, jTextPane);
        }
    }

    void copyTraceToPane(Trace trace) {
        for (int k = 0; k < trace.size(); ++k) {
            String eventString = trace.get(k).toString();
            addText(eventString + "\n", "regular", jTextPane);
        }
    }

    void displayDifferencesToPane(Object[] v, Object[] h, JTextPane pane) {
        int[] e = calculateEditInstructions(v, h);
        int i = -1, j = -1;
        for (int k = 0; k < e.length; ++k) {
            switch(e[k]) {
                case BOTH_DIAGONAL:
                    {
                        i += 1;
                        j += 1;
                        Assert.check(i < v.length);
                        Assert.check(j < h.length);
                        String eventString = v[i].toString();
                        addText(eventString + "\n", "regular", pane);
                        Assert.check(v[i].equals(h[j]));
                    }
                    break;
                case OLD_VERTICAL:
                    {
                        i += 1;
                        Assert.check(i < v.length);
                        String eventString = v[i].toString();
                        addText(eventString + "\n", "old", pane);
                    }
                    break;
                case NEW_HORIZONTAL:
                    {
                        j += 1;
                        Assert.check(j < h.length);
                        String eventString = h[j].toString();
                        addText(eventString + "\n", "new", pane);
                    }
                    break;
                default:
                    Assert.check(false);
            }
        }
        Assert.check(i == v.length - 1);
        Assert.check(j == h.length - 1);
    }

    private Object[] traceToArray(Trace t) {
        Object[] r = new Object[t.size()];
        for (int i = 0, sz = t.size(); i < sz; ++i) {
            r[i] = t.get(i);
        }
        return r;
    }

    private Object[] stringToArray(String s) {
        Vector v = new Vector();
        int i = 0, j = 0, len = s.length();
        while (i < len) {
            if (j == len) {
                v.addElement(s.substring(i, j));
                i = j;
                j = i;
            } else if (s.charAt(j) == '\n') {
                v.addElement(s.substring(i, j));
                i = j + 1;
                j = i;
            } else {
                j += 1;
            }
        }
        Object[] r = new Object[v.size()];
        for (int k = 0, sz = v.size(); k < sz; ++k) r[k] = v.elementAt(k);
        return r;
    }

    private static final int OLD_VERTICAL = -1, NEW_HORIZONTAL = +1, BOTH_DIAGONAL = 0;

    private int[] calculateEditInstructions(Object[] v, Object[] h) {
        final int m = v.length;
        final int n = h.length;
        int[][] a = new int[m][n];
        int[][] d = new int[m][n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (v[i].equals(h[j])) {
                    int diag = (i == 0 || j == 0) ? 0 : a[i - 1][j - 1];
                    a[i][j] = 1 + diag;
                    d[i][j] = BOTH_DIAGONAL;
                } else {
                    int up = (i == 0) ? 0 : a[i - 1][j];
                    int left = (j == 0) ? 0 : a[i][j - 1];
                    if (up > left || up == left && j > i) {
                        a[i][j] = up;
                        d[i][j] = OLD_VERTICAL;
                    } else {
                        a[i][j] = left;
                        d[i][j] = NEW_HORIZONTAL;
                    }
                }
            }
        }
        Vector temp = new Vector();
        {
            int i = m - 1;
            int j = n - 1;
            while (i >= 0 || j >= 0) {
                if (j < 0 || i >= 0 && d[i][j] == OLD_VERTICAL) {
                    temp.addElement(new Integer(OLD_VERTICAL));
                    --i;
                } else if (i < 0 || j >= 0 && d[i][j] == NEW_HORIZONTAL) {
                    temp.addElement(new Integer(NEW_HORIZONTAL));
                    --j;
                } else {
                    temp.addElement(new Integer(BOTH_DIAGONAL));
                    --i;
                    --j;
                }
            }
        }
        int sz = temp.size();
        int[] e = new int[sz];
        for (int k = 0; k < sz; ++k) {
            e[k] = ((Integer) temp.elementAt(sz - 1 - k)).intValue();
        }
        return e;
    }

    private void addText(String str, String styleName, JTextPane pane) {
        Document doc = pane.getDocument();
        int len = doc.getLength();
        try {
            doc.insertString(len, str, pane.getStyle(styleName));
        } catch (javax.swing.text.BadLocationException e) {
        }
    }

    void saveNewToFile() {
        owner.saveDialog(new File("./untitiled.jst"));
        setVisible(false);
    }

    void replaceOldTrace() {
        boolean success = owner.replaceOldTrace();
        if (success) setVisible(false);
    }

    private BorderLayout borderLayout3 = new BorderLayout();

    private JLabel jLabel1 = new JLabel();

    private JLabel jLabel2 = new JLabel();
}
