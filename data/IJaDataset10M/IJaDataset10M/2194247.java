package com.beeblebrox;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;
import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.CompoundSkin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import com.l2fprod.gui.plaf.skin.LinuxLookAndFeel;

class App {

    public static final String linesep = System.getProperty("line.separator");

    public static final String filesep = System.getProperty("file.separator");

    public static final String defaultSkinFile = "gtk" + filesep + "gtkrc";

    public static void main(String args[]) throws Exception {
        LookAndFeel defLookAndFeel = UIManager.getLookAndFeel();
        String lookAndFeelFile = defaultSkinFile;
        if (args.length == 1) {
            lookAndFeelFile = args[0];
        }
        try {
            Skin skin = SkinLookAndFeel.loadSkin(lookAndFeelFile);
            SkinLookAndFeel.setSkin(skin);
            UIManager.setLookAndFeel(new SkinLookAndFeel());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            UIManager.setLookAndFeel(defLookAndFeel);
        }
        try {
            JEngine eng = JEngine.getInstance();
            Frame frame = new Frame(eng);
        } catch (java.lang.UnsatisfiedLinkError e) {
            e.printStackTrace();
            String libname;
            if (System.getProperty("os.name").compareTo("Linux") == 0) {
                libname = "libbrox.so";
            } else {
                libname = "brox.dll";
            }
            String msg = new String("<html>&nbsp;A required library: <strong>" + libname + "</strong><br>" + "&nbsp;</strong>could not be found in the library path.<br>" + "&nbsp;Please correct this problem and try again.</html>");
            new TerminalErrorDialog(msg);
        } catch (AccessDeniedError e) {
            e.printStackTrace();
            String msg = new String("<html>&nbsp;You do not have sufficient access.<br>" + "&nbsp;You must be root or Administrator.<br>");
            new TerminalErrorDialog(msg);
        }
    }
}

class TerminalErrorDialog extends JFrame implements ActionListener {

    JButton btnClose = new JButton("Close");

    JPanel panel = new JPanel(new BorderLayout());

    JLabel lblError;

    TerminalErrorDialog(String htmlMsg) {
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
        setTitle("Critical Error");
        lblError = new JLabel(htmlMsg);
        btnClose.addActionListener(this);
        setSize(lblError.getPreferredSize().width + 50, btnClose.getPreferredSize().height + lblError.getPreferredSize().height + 50);
        panel.add(lblError, BorderLayout.CENTER);
        panel.add(btnClose, BorderLayout.SOUTH);
        getContentPane().add(panel);
        centerScreen();
        setVisible(true);
        requestFocus();
    }

    public void actionPerformed(ActionEvent e) {
        System.exit(1);
    }

    public void centerScreen() {
        Dimension dim = getToolkit().getScreenSize();
        Rectangle abounds = getBounds();
        setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
    }
}
