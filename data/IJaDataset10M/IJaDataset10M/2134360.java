package org.dlib.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressDialog extends JDialog {

    private JLabel lbText = new JLabel("");

    private JProgressBar progrBar = new JProgressBar();

    private int iCounter;

    public ProgressDialog(Frame f, String title) {
        super(f, title, true);
        initDialog();
    }

    public ProgressDialog(JDialog d, String title) {
        super(d, title, true);
        initDialog();
    }

    private void initDialog() {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        FlexLayout flexL = new FlexLayout(1, 2, 0, 4);
        flexL.setColProp(0, FlexLayout.EXPAND);
        p.setLayout(flexL);
        p.add("0,0,x", lbText);
        p.add("0,1,x", progrBar);
        getContentPane().add(p, BorderLayout.CENTER);
        progrBar.setBorderPainted(true);
        progrBar.setStringPainted(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    public void run(Runnable run) {
        setSize(350, 78);
        setLocationRelativeTo(getParent());
        new Thread(run).start();
        setVisible(true);
        dispose();
    }

    public void advance(final String text) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                lbText.setText(text);
                progrBar.setValue(++iCounter);
            }
        });
    }

    public void reset(final int maxVal) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progrBar.setMinimum(0);
                progrBar.setMaximum(maxVal);
                progrBar.setValue(0);
                lbText.setText("");
                iCounter = 0;
            }
        });
    }

    public void stop() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                setVisible(false);
            }
        });
    }
}
