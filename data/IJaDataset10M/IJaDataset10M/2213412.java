package com.peterhi.application;

import java.util.Properties;
import java.util.Map;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public final class Launcher extends JFrame implements Runnable, ActionListener {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Launcher());
    }

    private final JTextArea ta = new JTextArea();

    private final JScrollPane s = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    public Launcher() {
        setPreferredSize(new Dimension(400, 300));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        init();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void run() {
    }

    public void actionPerformed(ActionEvent e) {
    }

    private void init() {
        add(s, BorderLayout.CENTER);
        Properties props = System.getProperties();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : props.entrySet()) {
            sb.append("" + entry.getKey());
            sb.append(" = ");
            sb.append("" + entry.getValue());
            sb.append("\n");
        }
        ta.setText(sb.toString());
    }
}
