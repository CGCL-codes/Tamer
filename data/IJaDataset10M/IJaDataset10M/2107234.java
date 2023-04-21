package gov.sandia.ccaffeine.dc.user_iface.gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class CcaffeineGUITestWidget extends JFrame implements Runnable, CcaffeineGUIWidget {

    private static final long serialVersionUID = -1191569432113805756L;

    String msg = "Hello Ccaffeine World";

    private GlobalData global = null;

    public CcaffeineGUITestWidget() {
        super(CcaffeineGUITestWidget.class.getName());
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("still here ...");
        }
    }

    public void setArgs(String[] sv) {
        String newmsg = "Got Args:";
        for (int i = 0; i < sv.length; i++) {
            newmsg += " ";
            newmsg += sv[i];
        }
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JButton(msg), BorderLayout.CENTER);
        p.add(new JButton(newmsg), BorderLayout.CENTER);
        getContentPane().add(p);
        setSize(300, 300);
        if (global != null) {
            this.setLocation(global.getDialogLocation());
        }
        setVisible(true);
    }

    public void setArguments(String[] args) {
        setArgs(args);
    }

    public void setGlobalData(GlobalData globalData) {
        this.global = globalData;
    }
}
