package de.iqcomputing.flap;

import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import javax.swing.border.*;

public class MessageStatusPanel extends JPanel {

    public static final Insets insets0_0_3_3 = new Insets(0, 0, 3, 3);

    public static final Insets insets0_0_3_0 = new Insets(0, 0, 3, 0);

    public static final Insets insets0_0_0_3 = new Insets(0, 0, 0, 3);

    public static final Insets insets0_0_0_0 = new Insets(0, 0, 0, 0);

    protected JLabel lbSender;

    protected JLabel lbSubject;

    protected JLabel lbDate;

    protected DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.getDefault());

    public MessageStatusPanel() {
        createGUI();
    }

    protected void createGUI() {
        setLayout(new GridLayout(1, 0, 8, 0));
        add(createSenderSubjectPanel());
        add(createDatePanel());
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), BorderFactory.createEmptyBorder(1, 4, 1, 4)));
    }

    protected JPanel createSenderSubjectPanel() {
        JPanel panel = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        Component c;
        panel.setLayout(gb);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0;
        c = new JLabel("Sender:");
        gbc.insets = insets0_0_0_3;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gb.setConstraints(c, gbc);
        panel.add(c);
        c.setFont(c.getFont().deriveFont(Font.BOLD));
        c.setForeground(UIManager.getColor("textText"));
        lbSender = new JLabel();
        gbc.insets = insets0_0_0_0;
        gbc.gridx = 1;
        gbc.weightx = 1;
        gb.setConstraints(lbSender, gbc);
        panel.add(lbSender);
        lbSender.setFont(lbSender.getFont().deriveFont(Font.PLAIN));
        lbSender.setForeground(UIManager.getColor("textText"));
        c = new JLabel("Subject:");
        gbc.insets = insets0_0_0_3;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gb.setConstraints(c, gbc);
        panel.add(c);
        c.setFont(c.getFont().deriveFont(Font.BOLD));
        c.setForeground(UIManager.getColor("textText"));
        lbSubject = new JLabel();
        gbc.insets = insets0_0_0_0;
        gbc.gridx = 1;
        gbc.weightx = 1;
        gb.setConstraints(lbSubject, gbc);
        panel.add(lbSubject);
        lbSubject.setFont(lbSubject.getFont().deriveFont(Font.PLAIN));
        lbSubject.setForeground(UIManager.getColor("textText"));
        return panel;
    }

    protected JPanel createDatePanel() {
        JPanel panel = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        Component c;
        panel.setLayout(gb);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0;
        c = new JLabel("Date:");
        gbc.insets = insets0_0_0_3;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gb.setConstraints(c, gbc);
        panel.add(c);
        c.setFont(c.getFont().deriveFont(Font.BOLD));
        c.setForeground(UIManager.getColor("textText"));
        lbDate = new JLabel();
        gbc.insets = insets0_0_0_0;
        gbc.gridx = 1;
        gbc.weightx = 1;
        gb.setConstraints(lbDate, gbc);
        panel.add(lbDate);
        lbDate.setFont(lbDate.getFont().deriveFont(Font.PLAIN));
        lbDate.setForeground(UIManager.getColor("textText"));
        c = new JLabel(" ");
        gbc.insets = insets0_0_0_3;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gb.setConstraints(c, gbc);
        panel.add(c);
        c = new JLabel(" ");
        gbc.insets = insets0_0_0_0;
        gbc.gridx = 1;
        gbc.weightx = 1;
        gb.setConstraints(c, gbc);
        panel.add(c);
        return panel;
    }

    public void setSelectedMessage(MimeMessage msg) {
        if (msg != null) {
            Folder folder = msg.getFolder();
            FolderManager fm = Flap.getFolderManager();
            boolean folderOpen = false;
            try {
                fm.open(folder, FolderManager.READ_ONLY);
                folderOpen = true;
                {
                    String sender = "<unknown>";
                    try {
                        InternetAddress fromAddress = null;
                        Address[] from = msg.getFrom();
                        if (from != null) {
                            if (from.length > 0) {
                                if (from[0] instanceof InternetAddress) fromAddress = (InternetAddress) from[0];
                            }
                        }
                        if (fromAddress != null) {
                            sender = fromAddress.toString();
                            try {
                                sender = MimeUtility.decodeText(sender);
                            } catch (UnsupportedEncodingException e) {
                            }
                        }
                    } catch (MessagingException e) {
                    }
                    lbSender.setText(sender);
                }
                {
                    String subject = "<unknown>";
                    try {
                        subject = msg.getSubject();
                    } catch (MessagingException e) {
                    }
                    if (subject == null) subject = "";
                    {
                        int attachments = getAttachmentsCount(msg);
                        if (attachments > 0) subject += " [" + attachments + " attachments]";
                    }
                    lbSubject.setText(subject);
                }
                {
                    String d = "<unknown>";
                    try {
                        Date date = msg.getSentDate();
                        if (date != null) d = dateTimeFormat.format(date);
                    } catch (MessagingException e) {
                    }
                    lbDate.setText(d);
                }
            } catch (MessagingException e) {
                lbSender.setText("<ERROR>");
                lbSubject.setText("<ERROR>");
                lbDate.setText("<ERROR>");
            } finally {
                if (folderOpen) {
                    try {
                        fm.close(folder, false);
                    } catch (MessagingException e) {
                    }
                }
            }
        } else {
            lbSender.setText("");
            lbSubject.setText("");
            lbDate.setText("");
        }
    }

    protected int getAttachmentsCount(MimeMessage message) {
        int cnt = 0;
        try {
            if (message.isMimeType("multipart/*")) {
                Object content = message.getContent();
                if (content instanceof MimeMultipart) {
                    MimeMultipart multiPart = (MimeMultipart) content;
                    cnt = multiPart.getCount();
                    if (cnt > 0) {
                        BodyPart firstPart = multiPart.getBodyPart(0);
                        if (firstPart instanceof MimeBodyPart) {
                            if (((MimeBodyPart) firstPart).getContent() instanceof String) cnt--;
                        }
                    }
                }
            }
        } catch (MessagingException e) {
        } catch (IOException e) {
        }
        return cnt;
    }
}
