package org.javadelic.bajjer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.javadelic.burrow.JabEvent;
import org.javadelic.burrow.JabListener;
import org.javadelic.burrow.JabUtil;

/**
 *
 * @author  Jim Schultz
 */
public class BajjerRawMode extends JPanel implements JabListener, ActionListener {

    private JTextPane logHistory;

    private StyledDocument doc;

    private SimpleAttributeSet attribs;

    private JTextArea inputMessage;

    private JPanel panel;

    private GregorianCalendar cal;

    private SimpleDateFormat dateFormat;

    public static boolean bDebug = false;

    private JScrollPane logHistoryPane;

    private JScrollPane inputMessagePane;

    /** Creates new BajjerRawMode */
    public BajjerRawMode() {
        cal = new GregorianCalendar();
        dateFormat = new SimpleDateFormat("HH:mm");
        panel = new JPanel(new BorderLayout());
        doc = new DefaultStyledDocument();
        initializeStyles(doc);
        logHistory = new JTextPane(doc);
        logHistoryPane = new JScrollPane(logHistory);
        logHistory.setEditable(false);
        logHistoryPane.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Log"));
        logHistory.registerKeyboardAction(copyListener, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK, false), JComponent.WHEN_FOCUSED);
        logHistory.registerKeyboardAction(copyListener, KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK, false), JComponent.WHEN_FOCUSED);
        JPanel say = new JPanel(new BorderLayout());
        say.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Send Raw XML"));
        inputMessage = new JTextArea(4, 40);
        inputMessage.setLineWrap(true);
        inputMessage.requestFocus();
        inputMessagePane = new JScrollPane(inputMessage);
        inputMessagePane.registerKeyboardAction(pasteListener, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK, false), JComponent.WHEN_FOCUSED);
        JPanel south = new JPanel();
        south.setBorder(new EtchedBorder());
        JButton sendButton = new JButton("Send XML");
        sendButton.addActionListener(this);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        south.add(clearButton);
        south.add(sendButton);
        JPanel east = new JPanel(new GridLayout(3, 1, 3, 3));
        east.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Templates"));
        JButton message = new JButton("Message");
        message.addActionListener(this);
        JButton presence = new JButton("Presence");
        presence.addActionListener(this);
        JButton iq = new JButton("IQ");
        iq.addActionListener(this);
        east.add(message);
        east.add(presence);
        east.add(iq);
        say.add(inputMessagePane, "Center");
        say.add(south, "South");
        say.add(east, "East");
        panel.add(logHistoryPane, "Center");
        panel.add(say, "South");
        panel.setVisible(true);
        Bajjer.jabberServer.addListener((JabListener) this, JabUtil.XML_SENT);
        Bajjer.jabberServer.addListener((JabListener) this, JabUtil.XML_RECEIVED);
    }

    /** 
   * Returns the main panel.
   * The JPanel object is then inserted into the JTabbedPane in BajjerClient.
   */
    public JPanel getPanel() {
        return panel;
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("Clear")) {
            inputMessage.setText("");
        } else if (evt.getActionCommand().equals("Send XML")) {
            String message = inputMessage.getText();
            if (message.length() > 0) {
                Bajjer.jabberServer.transmitXML(message);
            }
            inputMessage.setText("");
        } else if (evt.getActionCommand().equals("Message")) {
            inputMessage.setText("<message from='' to='' type=''>\n" + "  <body></body>\n" + "  <subject></subject>\n" + "</message>");
        } else if (evt.getActionCommand().equals("Presence")) {
            inputMessage.setText("<presence from='' to='' type=''>\n" + "  <show></show>\n" + "  <status></status>\n" + "</presence>");
        } else if (evt.getActionCommand().equals("IQ")) {
            inputMessage.setText("<iq type='get' to=''>\n" + "  <query xmlns=''></query>\n" + "</iq>");
        }
    }

    public void messageReceived(JabEvent evt) {
    }

    public void presenceReceived(JabEvent evt) {
    }

    public void iqReceived(JabEvent evt) {
    }

    public void xmlSentToServer(JabEvent evt) {
        debug("BajjerRawMode - xmlSendToServer");
        try {
            cal = new GregorianCalendar();
            doc.insertString(doc.getLength(), "[" + dateFormat.format(cal.getTime()) + "] ", doc.getStyle("Basic"));
            doc.insertString(doc.getLength(), "SENT", doc.getStyle("RedText"));
            doc.insertString(doc.getLength(), evt.getXml() + "\n", doc.getStyle("BlueText"));
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        JViewport view = logHistoryPane.getViewport();
        Point pos = view.getViewPosition();
        Dimension viewSize = view.getViewSize();
        pos.translate(0, (int) viewSize.getHeight());
        view.setViewPosition(pos);
    }

    public void xmlReceivedFromServer(JabEvent evt) {
        debug("BajjerRawMode - xmlReceivedFromServer");
        try {
            cal = new GregorianCalendar();
            doc.insertString(doc.getLength(), "[" + dateFormat.format(cal.getTime()) + "] ", doc.getStyle("Basic"));
            doc.insertString(doc.getLength(), "RCVD", doc.getStyle("GreenText"));
            doc.insertString(doc.getLength(), evt.getXml() + "\n", doc.getStyle("BlueText"));
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        JViewport view = logHistoryPane.getViewport();
        Point pos = view.getViewPosition();
        Dimension viewSize = view.getViewSize();
        pos.translate(0, (int) viewSize.getHeight());
        view.setViewPosition(pos);
    }

    /**
   * Used to initialze the styles used by the StyledDocument.
   * Located here just to keep the main routines a little cleaner
   */
    private void initializeStyles(StyledDocument doc) {
        Style basicStyle = doc.addStyle("Basic", null);
        StyleConstants.setFontFamily(basicStyle, "SansSerif");
        StyleConstants.setFontSize(basicStyle, 14);
        StyleConstants.setForeground(basicStyle, Color.black);
        StyleConstants.setSpaceAbove(basicStyle, 6);
        StyleConstants.setSpaceBelow(basicStyle, 0);
        Style s = doc.addStyle("RedText", basicStyle);
        StyleConstants.setForeground(s, Color.red);
        s = doc.addStyle("GreenText", basicStyle);
        StyleConstants.setForeground(s, new Color(0, 100, 0));
        s = doc.addStyle("BlueText", basicStyle);
        StyleConstants.setForeground(s, Color.blue);
        s = doc.addStyle("PurpleText", basicStyle);
        StyleConstants.setForeground(s, Color.magenta);
    }

    private void debug(String msg) {
        if (bDebug) {
            System.out.println(msg);
        }
    }

    ActionListener pasteListener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            try {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable clipData = clip.getContents(clip);
                String paste = (String) clipData.getTransferData(DataFlavor.stringFlavor);
                inputMessage.setText(paste);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    };

    ActionListener copyListener = new ActionListener() {

        public void actionPerformed(ActionEvent evt) {
            try {
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                int start = logHistory.getSelectionStart();
                int end = logHistory.getSelectionEnd();
                String selection = logHistory.getText(start, end - start);
                StringSelection data = new StringSelection(selection);
                clip.setContents(data, data);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    };
}
