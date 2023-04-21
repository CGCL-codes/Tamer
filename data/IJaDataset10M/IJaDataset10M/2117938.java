package au.jSummit.Modules.Chat;

import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileFilter;
import javax.imageio.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import javax.swing.text.*;
import java.util.*;
import au.jSummit.Core.*;

class ChatDialog extends JPanel {

    private final Color textColor = new Color(46, 57, 74);

    private final Color bgColor = Globals.BGCOLOR;

    private final Color buttonColor = Globals.BUTCOLOR;

    private final Color talkingColor = new Color(0, 0, 0);

    private JTextPane conversation;

    private JScrollPane scrollPane;

    private JTextField talkInput;

    private String yourName;

    private String theirName;

    private Font yourFont;

    private Font theirFont;

    private ChatControl theChatControl;

    private JSCore jscCore;

    public ChatDialog(String yourName1, String theirName1, JSCore jsc1) {
        this.setBackground(bgColor);
        yourName = yourName1;
        theirName = theirName1;
        jscCore = jsc1;
        yourFont = new Font("Arial", Font.PLAIN, 14);
        setLayout(new BorderLayout());
        conversation = new JTextPane();
        conversation.setBackground(new Color(255, 255, 255));
        conversation.setDisabledTextColor(talkingColor);
        conversation.setEditable(false);
        scrollPane = new JScrollPane(conversation, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        talkInput = new JTextField();
        talkInput.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == Event.ENTER) {
                    String message = talkInput.getText();
                    talkInput.setText("");
                    add(yourName, message, theChatControl.getFontDialog().getFont(), theChatControl.getColor());
                    PrivateChatPacket packet = new PrivateChatPacket();
                    packet.message = message;
                    packet.sender = yourName;
                    packet.font = theChatControl.getFontDialog().getFont();
                    packet.color = theChatControl.getColor();
                    JSPacket jsPacket = new JSPacket();
                    jsPacket.moduleName = new String("jsPrivateChat");
                    jsPacket.modulePacket = packet;
                    Person destination = (Person) jscCore.getSummitInfo().getMembers().get(theirName);
                    jsPacket.destination = destination;
                    jscCore.sendPacket(jsPacket);
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
                String message = talkInput.getText();
                if (message.length() > 256) talkInput.setText(message.substring(0, 256));
            }
        });
        Calendar rightNow = Calendar.getInstance();
        add(new JLabel("     Private Chat to " + theirName + " ||| Chat opened on " + rightNow.getTime().toString()), BorderLayout.NORTH);
        add(new JLabel("     "), BorderLayout.EAST);
        add(new JLabel("     "), BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(bgColor);
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(new JLabel("     "), BorderLayout.WEST);
        inputPanel.add(new JLabel("     "), BorderLayout.EAST);
        inputPanel.add(new JLabel("     "), BorderLayout.SOUTH);
        inputPanel.add(new JLabel("     "), BorderLayout.NORTH);
        inputPanel.add(talkInput, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
    }

    public void recievePacet(JSPacket jsp) {
        if (jsp.modulePacket instanceof PrivateChatPacket) {
            PrivateChatPacket packet = (PrivateChatPacket) jsp.modulePacket;
            add(packet.sender, packet.message, packet.font, packet.color);
        }
    }

    public void linkControl(ChatControl theChatControl1) {
        theChatControl = theChatControl1;
    }

    public void add(String name, String message, Font font, Color color) {
        int hour = Calendar.getInstance().get(11);
        if (hour > 12) hour = hour - 12;
        int minute = Calendar.getInstance().get(12);
        StyledDocument doc = (StyledDocument) conversation.getDocument();
        Style basicStyle = doc.addStyle("custom", null);
        StyleConstants.setFontFamily(basicStyle, "SansSerif");
        StyleConstants.setFontSize(basicStyle, 12);
        StyleConstants.setBold(basicStyle, false);
        StyleConstants.setItalic(basicStyle, false);
        try {
            doc.insertString(doc.getLength(), " [" + hour + ":", basicStyle);
            if (minute < 10) doc.insertString(doc.getLength(), "0" + minute, basicStyle); else doc.insertString(doc.getLength(), Integer.toString(minute), basicStyle);
            doc.insertString(doc.getLength(), "] <" + name + "> ", basicStyle);
        } catch (BadLocationException e1) {
        }
        doc = (StyledDocument) conversation.getDocument();
        try {
            doc = (StyledDocument) conversation.getDocument();
            Style style;
            style = doc.addStyle("custom", null);
            style = doc.addStyle("custom", null);
            StyleConstants.setFontFamily(style, font.getFontName());
            StyleConstants.setFontSize(style, font.getSize());
            StyleConstants.setBold(style, font.isBold());
            StyleConstants.setItalic(style, font.isItalic());
            StyleConstants.setForeground(style, color);
            doc.insertString(doc.getLength(), message + "\n", style);
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        } catch (BadLocationException e1) {
        }
    }

    public void saveChat() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("privatechat.txt"));
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal != JFileChooser.APPROVE_OPTION) return;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(chooser.getSelectedFile());
            fos.write(conversation.getText().getBytes());
            fos.close();
        } catch (Exception xe) {
            System.err.println(xe);
            return;
        }
    }

    public void clearText() {
        conversation.setText("");
    }

    public JSCore getCore() {
        return jscCore;
    }
}
