package com.ibm.awb.launcher;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Console extends Frame implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = -3813053602916153118L;

    private Button _clear_button = new Button("Clear");

    private Button _close_button = new Button("Close");

    private TextArea _log_text_area = new TextArea(15, 82);

    public Console() {
        super("Aglets Daemon Console");
        this.redirect();
        this.add("Center", this._log_text_area);
        Panel p = new Panel();
        p.setLayout(new BorderLayout());
        p.add("West", this._clear_button);
        p.add("East", this._close_button);
        this.add("South", p);
        this.pack();
        this._clear_button.addActionListener(this);
        this._close_button.addActionListener(this);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent ev) {
                Console.this.setVisible(false);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (this._close_button.getActionCommand().equals(ev.getActionCommand())) {
            this.setVisible(false);
        } else if (this._clear_button.getActionCommand().equals(ev.getActionCommand())) {
            this._log_text_area.setText("");
        }
    }

    public void redirect() {
        LogWriter lw = new LogWriter(this._log_text_area);
        java.io.PrintStream ps = new java.io.PrintStream(lw);
        System.setOut(ps);
        System.setErr(ps);
    }
}
