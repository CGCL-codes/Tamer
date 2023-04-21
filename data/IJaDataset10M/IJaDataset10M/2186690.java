package vavi.apps.ipm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import vavi.net.im.protocol.ipm.Ipmessenger;
import vavi.net.im.protocol.ipm.Ipmessenger.Constant;
import vavi.net.im.protocol.ipm.event.CommunicationEvent;
import vavi.net.im.protocol.ipm.event.IpmEvent;

/**
 * IP Messenger Recv Dialog Class
 */
public class ReceiveDialog extends JDialog {

    private Ipmessenger ipmsg;

    private IpmEvent ipme;

    private CommunicationEvent ipmce;

    private String suffix = "";

    private JLabel user;

    private JTextField body;

    private JCheckBox quote;

    /** �ݒ�t�@�C�� */
    private static Preferences userPrefs = Preferences.userNodeForPackage(Ipmessenger.class);

    /** */
    private static ResourceBundle rb = ResourceBundle.getBundle("vavi.net.im.protocol.ipm.resources");

    public ReceiveDialog(JFrame p, Ipmessenger i, CommunicationEvent c, IpmEvent e) {
        super(p, false);
        ipmsg = i;
        ipmce = c;
        ipme = e;
        createWindow(p);
    }

    private void createWindow(final JFrame p) {
        setVisible(false);
        setTitle(rb.getString("recvdlgName"));
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                exitAction();
            }
        });
        setLayout(new BorderLayout());
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add("North", p1);
        JLabel to = new JLabel(rb.getString("msgFromLabel"));
        p1.add(to);
        String tmpuser = null;
        if (ipmce != null) {
            tmpuser = ipmsg.makeListString(ipmce.getPacket());
        } else {
            tmpuser = ipme.getPacket().getUser();
        }
        user = new JLabel(tmpuser);
        p1.add(user);
        body = new JTextField();
        body.setEditable(false);
        body.setText(ipme.getPacket().getExtra());
        add("Center", body);
        JPanel p2 = new JPanel(new BorderLayout());
        add("South", p2);
        JLabel time = new JLabel(ipmsg.makeDateString(ipme.getDate()), JLabel.CENTER);
        p2.add("North", time);
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p2.add("South", p3);
        final JButton reply = new JButton(rb.getString("replyLabel"));
        reply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                CommunicationEvent[] tmpipmce = new CommunicationEvent[1];
                if (ipmce != null) {
                    tmpipmce[0] = ipmce;
                } else {
                    tmpipmce[0] = ipme;
                    tmpipmce[0].getPacket().setExtra(null);
                }
                SendDialog sd = new SendDialog(p, ipmsg, tmpipmce);
                if (quote.isSelected()) {
                    String cr = "\n";
                    String tmpstr = rb.getString("quoter") + body.getText();
                    tmpstr = tmpstr.replace(cr, cr + "> ");
                    sd.setText(tmpstr);
                }
                sd.setVisible(true);
            }
        });
        p3.add(reply);
        quote = new JCheckBox(rb.getString("quoteLabel"));
        quote.setSelected(userPrefs.getBoolean("quoteState", Boolean.parseBoolean(rb.getString("quoteState"))));
        p3.add(quote);
        JButton close = new JButton(rb.getString("closeLabel"));
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                exitAction();
            }
        });
        p3.add(close);
        try {
            int x = userPrefs.getInt("dlgSizeX", Integer.parseInt(rb.getString("dlgSizeX")));
            int y = userPrefs.getInt("dlgSizeY", Integer.parseInt(rb.getString("dlgSizeY")));
            setSize(x, y);
        } catch (MissingResourceException ex) {
            pack();
        }
        Dimension sc = getToolkit().getScreenSize();
        Dimension sz = getSize();
        Random random = new Random();
        setLocation((sc.width / 2) + (random.nextInt() % 64), (sc.height / 2) - (sz.height / 2) + (random.nextInt() % 64));
        if ((ipme.getPacket().getCommand() & Constant.MULTICASTOPT.getValue()) != 0) {
            suffix = " (" + userPrefs.get("multicastLogFlag", rb.getString("multicastLogFlag")) + ")";
        } else if ((ipme.getPacket().getCommand() & Constant.BROADCASTOPT.getValue()) != 0) {
            suffix = " (" + userPrefs.get("broadcastLogFlag", rb.getString("broadcastLogFlag")) + ")";
        }
        if ((ipme.getPacket().getCommand() & Constant.PASSWORDOPT.getValue()) != 0) {
            suffix = suffix + " (" + userPrefs.get("passwdLogFlag", rb.getString("passwdLogFlag")) + ")";
            final String strpass = userPrefs.get("password", rb.getString("password"));
            final JPanel p4 = new JPanel(new FlowLayout());
            add("Center", p4);
            JLabel passwdlabel = new JLabel(rb.getString("inputPasswdLabel"));
            p4.add(passwdlabel);
            final JPasswordField input = new JPasswordField(20);
            input.setEchoChar('*');
            p4.add(input);
            JButton open = new JButton(rb.getString("openLabel"));
            open.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    try {
                        if (strpass.equals(MessageDigester.getMD5(new String(input.getPassword())).getBytes("MS932"))) {
                            ipmsg.sendReadMessage(ipme);
                            remove(p4);
                            add("Center", body);
                            reply.setEnabled(true);
                            quote.setEnabled(true);
                            validate();
                            ipmsg.writeLog("From: " + user.getText(), ipmsg.makeDateString(ipme.getDate()) + suffix, ipme.getPacket().getExtra());
                        } else {
                            getToolkit().beep();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            p4.add(open);
            reply.setEnabled(false);
            quote.setEnabled(false);
            return;
        }
        if ((ipme.getPacket().getCommand() & Constant.SECRETOPT.getValue()) != 0) {
            suffix = suffix + " (" + userPrefs.get("secretLogFlag", rb.getString("secretLogFlag")) + ")";
            final JButton open = new JButton(rb.getString("openLabel"));
            open.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    try {
                        ipmsg.sendReadMessage(ipme);
                        remove(open);
                        add("Center", body);
                        reply.setEnabled(true);
                        quote.setEnabled(true);
                        validate();
                        ipmsg.writeLog("From: " + user.getText(), ipmsg.makeDateString(ipme.getDate()) + suffix, ipme.getPacket().getExtra());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            add("Center", open);
            reply.setEnabled(false);
            quote.setEnabled(false);
            return;
        }
        ipmsg.writeLog("From: " + user.getText(), ipmsg.makeDateString(ipme.getDate()) + suffix, ipme.getPacket().getExtra());
    }

    private void exitAction() {
        if (!quote.isEnabled()) {
            try {
                ipmsg.sendDeleteMessage(ipme);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (userPrefs.getBoolean("resumeState", Boolean.parseBoolean(rb.getString("resumeState")))) {
            userPrefs.putBoolean("quoteState", quote.isSelected());
        }
        Dimension size = getSize();
        userPrefs.putInt("dlgSizeX", size.width);
        userPrefs.putInt("dlgSizeY", size.height);
        ipmsg.decreaseReceiveCount();
        dispose();
    }
}
