package com.tetrasix.majix.uis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ResourceBundle;
import com.tetrasix.util.LogoTetraSix;
import com.tetrasix.majix.Version;

class AboutFrame extends JFrame implements ActionListener {

    private static AboutFrame _theFrame;

    private JButton _buttonClose;

    AboutFrame() {
        ResourceBundle res = ResourceBundle.getBundle("com.tetrasix.majix.uis.AboutFrameResources");
        setTitle(res.getString("frameTitle"));
        JPanel pc = new JPanel();
        JPanel pg = new JPanel();
        pg.setLayout(new GridLayout(3, 1, 10, 10));
        pg.add(new JLabel(Version.getVersion() + " - (" + Version.getDate() + ")"));
        pg.add(new JLabel(res.getString("line2")));
        pg.add(new JLabel(res.getString("copyright")));
        pc.add(pg);
        JPanel ps = new JPanel();
        ps.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        ps.add(_buttonClose = new JButton(res.getString("okButton")));
        _buttonClose.setActionCommand("close");
        _buttonClose.addActionListener(this);
        getContentPane().add("North", new LogoTetraSix());
        getContentPane().add("Center", pc);
        getContentPane().add("South", ps);
        setSize(300, 350);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        setBackground(SystemColor.control);
        center();
        show();
        _theFrame = this;
        LFlist.remove(this);
    }

    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            _theFrame = null;
            LFlist.remove(this);
            dispose();
        } else {
            super.processWindowEvent(e);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if ("close".equals(s)) {
            _theFrame = null;
            LFlist.remove(this);
            dispose();
        } else {
            System.out.println("unexpected action command : " + s);
        }
    }

    void center() {
        Dimension wd = getSize();
        Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((sd.width - wd.width) / 2, (sd.height - wd.height) / 2);
    }

    static void showAbout() {
        if (_theFrame != null) {
            _theFrame.show();
        } else {
            new AboutFrame();
        }
    }
}
