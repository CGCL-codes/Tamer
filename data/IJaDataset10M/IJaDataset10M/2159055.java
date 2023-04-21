package jalview;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class MailTextPopup extends Popup {

    TextField tf;

    Label tfLabel;

    Mail mail;

    Label format;

    Choice f;

    OutputGenerator og;

    public MailTextPopup(Frame parent, String title, OutputGenerator og) {
        super(parent, title);
        this.og = og;
        tf = new TextField(40);
        tf.setText(og.getMailProperties().address);
        tfLabel = new Label("Mail address : ");
        format = new Label("Alignment format");
        f = new Choice();
        for (int i = 0; i < FormatProperties.formats.size(); i++) {
            f.addItem((String) FormatProperties.formats.elementAt(i));
        }
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 20, 20, 20);
        add(tfLabel, gb, gbc, 0, 0, 1, 1);
        add(tf, gb, gbc, 1, 0, 4, 1);
        add(apply, gb, gbc, 0, 2, 1, 1);
        add(close, gb, gbc, 1, 2, 1, 1);
        add(format, gb, gbc, 0, 1, 1, 1);
        add(f, gb, gbc, 1, 1, 1, 1);
        this.pack();
        this.show();
    }

    public boolean handleEvent(Event evt) {
        if (evt.target == apply && evt.id == 1001) {
            String address = tf.getText();
            if (address.indexOf('@') == -1) {
                System.out.println("Invalid mail address (enter name@my.email.server )");
                this.hide();
                this.dispose();
            } else {
                System.out.println("Mail address " + address);
                og.getMailProperties().address = address;
                mail = new Mail();
                String recipient = og.getMailProperties().address;
                String author = "<michele@ebi.ac.uk>";
                String subject = "Jalview alignment";
                String text = "";
                System.out.println("Mail Server = " + og.getMailProperties().server);
                if (og.getMailProperties().server != null && !(og.getMailProperties().server.equals(""))) {
                    mail.send(og.getMailProperties().server, recipient, author, subject, text);
                    sendText();
                    mail.finish();
                    java.net.InetAddress ia = null;
                    String pog = "";
                    try {
                        ia = java.net.InetAddress.getLocalHost();
                        pog = ia.getHostName();
                    } catch (Exception e) {
                        System.err.println(e.toString());
                    }
                    subject = "Jalview sent to " + og.getMailProperties().address + " (" + pog + ")";
                    System.out.println("Mail server = " + og.getMailProperties().server);
                    mail.send(og.getMailProperties().server, "michele@ebi.ac.uk", author, subject, text);
                }
                mail.finish();
                this.hide();
                this.dispose();
            }
            return true;
        } else {
            return super.handleEvent(evt);
        }
    }

    public void sendText() {
        String text = "";
        text = og.getText(f.getSelectedItem());
        mail.send(mail.out, text);
    }

    public static void main(String[] args) {
        AlignFrame af = new AlignFrame(null, "lipase.msf", "File", "MSF");
        af.resize(700, 300);
        af.show();
        MailTextPopup mp = new MailTextPopup(af, "Popup", af);
    }
}
