package com.myapp.util.soundsorter.wizard.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import com.myapp.util.swing.Util;

final class InitProgressBar implements IInitProgressListener {

    private JFrame jf;

    private JProgressBar bar;

    private JLabel msgHeaderLabel;

    private JLabel msgLabel;

    private int max;

    public InitProgressBar(int max) {
        this.max = max;
        bar = new JProgressBar(0, max);
        bar.setValue(0);
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(bar, BorderLayout.CENTER);
        JPanel south = new JPanel(new GridLayout(0, 1));
        msgHeaderLabel = new JLabel("Beginne mit dem ermitteln der Genres für " + max + " Verzeichnisse...");
        msgLabel = new JLabel("Warte auf Daten...");
        Font font = msgLabel.getFont();
        Font smallerFont = new Font(font.getName(), font.getStyle(), font.getSize() - 1);
        msgLabel.setFont(smallerFont);
        south.add(msgHeaderLabel);
        south.add(msgLabel);
        contentPane.add(south, BorderLayout.SOUTH);
        jf = new JFrame("init...");
        jf.setContentPane(contentPane);
        jf.setPreferredSize(new Dimension(800, 100));
        jf.pack();
        Util.centerFrame(jf);
        Util.quitOnClose(jf);
        jf.setVisible(true);
    }

    @Override
    public void notifyDirInitialized(String absolutePath) {
        int value = bar.getValue() + 1;
        bar.setValue(value);
        msgHeaderLabel.setText("Ermittle Genres: " + value + " / " + max);
        msgLabel.setText("Aktueller Ordner : " + absolutePath);
    }

    public void exit() {
        jf.setVisible(false);
    }
}
