package org.alliance.ui.windows;

import com.stendahls.nif.ui.OptionDialog;
import com.stendahls.nif.ui.mdi.MDIManager;
import com.stendahls.nif.ui.mdi.MDIWindow;
import org.alliance.core.file.hash.Hash;
import org.alliance.ui.T;
import org.alliance.ui.UISubsystem;
import org.alliance.ui.util.CutCopyPastePopup;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: maciek
 * Date: 2006-jan-05
 * Time: 13:21:55
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractChatMessageMDIWindow extends AllianceMDIWindow implements Runnable {

    protected static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    protected static final DateFormat SHORT_FORMAT = new SimpleDateFormat("HH:mm");

    protected static final Color COLORS[] = { new Color(0x0068a7), new Color(0x009606), new Color(0xa13eaa), new Color(0x008b76), new Color(0xb77e24), new Color(0xef0000), new Color(0xb224b7), new Color(0xb77e24) };

    protected JEditorPane textarea;

    protected JTextField chat;

    protected String html = "";

    private TreeSet<ChatLine> chatLines;

    private static final int MAXIMUM_NUMBER_OF_CHAT_LINES = 50;

    private boolean needToUpdateHtml;

    private boolean alive = true;

    private ChatLine previousChatLine = null;

    protected AbstractChatMessageMDIWindow(MDIManager manager, String mdiWindowIdentifier, UISubsystem ui) throws Exception {
        super(manager, mdiWindowIdentifier, ui);
        chatLines = new TreeSet<ChatLine>(new Comparator<ChatLine>() {

            public int compare(ChatLine o1, ChatLine o2) {
                long diff = o1.tick - o2.tick;
                if (diff <= Integer.MIN_VALUE) diff = Integer.MIN_VALUE + 1;
                if (diff >= Integer.MAX_VALUE) diff = Integer.MAX_VALUE - 1;
                return (int) diff;
            }
        });
    }

    protected abstract void send(final String text) throws IOException, Exception;

    public abstract String getIdentifier();

    protected void postInit() {
        textarea = new JEditorPane("text/html", "");
        new CutCopyPastePopup(textarea);
        JScrollPane sp = (JScrollPane) xui.getComponent("scrollpanel");
        sp.setViewportView(textarea);
        chat = (JTextField) xui.getComponent("chat1");
        new CutCopyPastePopup(chat);
        textarea.setEditable(false);
        textarea.setBackground(Color.white);
        textarea.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        String link = e.getDescription();
                        if (link.startsWith("http://")) {
                            String allowedChars = "abcdefghijklmnopqrstuvwxyzåäö0123456789-.;/?:@&=+$_.!~*'()#";
                            for (int i = 0; i < link.length(); i++) {
                                if (allowedChars.indexOf(link.toLowerCase().charAt(i)) == -1) {
                                    OptionDialog.showInformationDialog(ui.getMainWindow(), "Character " + link.charAt(i) + " is not allowed in link.");
                                    return;
                                }
                            }
                            ui.openURL(link);
                        } else {
                            String[] hashes = link.split("\\|");
                            for (String s : hashes) if (T.t) T.trace("Part: " + s);
                            int guid = Integer.parseInt(hashes[0]);
                            if (OptionDialog.showQuestionDialog(ui.getMainWindow(), "Add " + (hashes.length - 1) + " files to downloads?")) {
                                ArrayList<Integer> al = new ArrayList<Integer>();
                                al.add(guid);
                                for (int i = 1; i < hashes.length; i++) {
                                    ui.getCore().getNetworkManager().getDownloadManager().queDownload(new Hash(hashes[i]), "Link from chat", al);
                                }
                                ui.getMainWindow().getMDIManager().selectWindow(ui.getMainWindow().getDownloadsWindow());
                            }
                        }
                    } catch (IOException e1) {
                        ui.getCore().reportError(e1, this);
                    }
                }
            }
        });
        Thread t = new Thread(this);
        t.start();
        super.postInit();
    }

    public void EVENT_chat1(ActionEvent e) throws Exception {
        chatMessage();
    }

    private void chatMessage() throws Exception {
        if (chat.getText().trim().length() == 0) return;
        if (chat.getText().trim().equals("/clear")) {
            chatLines.clear();
            regenerateHtml();
            needToUpdateHtml = true;
            chat.setText("");
            return;
        }
        send(escapeHTML(chat.getText()));
    }

    public void EVENT_chat2(ActionEvent e) throws Exception {
        chatMessage();
    }

    private String escapeHTML(String text) {
        text = text.replace("<", "&lt;");
        String pattern = "http://";
        int i = 0;
        while ((i = text.indexOf(pattern, i)) != -1) {
            int end = text.indexOf(' ', i);
            if (end == -1) end = text.length();
            String s = text.substring(0, i);
            s += "<a href=\"" + text.substring(i, end) + "\">" + text.substring(i, end) + "</a>";
            i = s.length();
            if (end < text.length() - 1) s += text.substring(end);
            text = s;
        }
        return text;
    }

    public void addMessage(String from, String message, long tick, boolean messageHasBeenQueuedAwayForAWhile) {
        if (chatLines != null && chatLines.size() > 0) {
            ChatLine l = chatLines.last();
            if (!messageHasBeenQueuedAwayForAWhile) {
                tick = System.currentTimeMillis();
            }
        }
        if (tick > System.currentTimeMillis()) {
            tick = System.currentTimeMillis();
        }
        int n = from.hashCode();
        if (n < 0) n = -n;
        n %= COLORS.length;
        Color c = COLORS[n];
        while (chatLinesContainTick(tick)) tick++;
        ChatLine cl = new ChatLine(from, message, tick, c);
        chatLines.add(cl);
        boolean removeFirstLine = chatLines.size() > MAXIMUM_NUMBER_OF_CHAT_LINES;
        if (removeFirstLine) chatLines.remove(chatLines.first());
        if (chatLines.last() == cl && !removeFirstLine) {
            html += creattHtmlChatLine(cl);
        } else {
            regenerateHtml();
        }
        needToUpdateHtml = true;
    }

    private boolean chatLinesContainTick(long tick) {
        for (ChatLine cl : chatLines) {
            if (cl.tick == tick) return true;
        }
        return false;
    }

    private void regenerateHtml() {
        if (T.t) T.info("Regenerating entire html for chat");
        html = "";
        for (ChatLine chatLine : chatLines) {
            html += creattHtmlChatLine(chatLine);
        }
    }

    public void run() {
        while (alive) {
            if (needToUpdateHtml) {
                needToUpdateHtml = false;
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        textarea.setText(html);
                    }
                });
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private String creattHtmlChatLine(ChatLine cl) {
        String s;
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        if (previousChatLine != null && f.format(new Date(cl.tick)).equals(f.format(new Date(previousChatLine.tick)))) {
            s = "[" + SHORT_FORMAT.format(new Date(cl.tick)) + "]";
        } else {
            s = "<b>[" + FORMAT.format(new Date(cl.tick)) + "]</b>";
        }
        s = "<font color=\"#9f9f9f\">" + s + " <font color=\"" + toHexColor(cl.color) + "\">" + cl.from + ":</font> <font color=\"" + toHexColor(cl.color.darker()) + "\">" + cl.message + "</font><br>";
        previousChatLine = cl;
        return s;
    }

    protected String toHexColor(Color color) {
        return "#" + Integer.toHexString(color.getRGB() & 0xffffff);
    }

    private class ChatLine {

        String from, message;

        long tick;

        Color color;

        public ChatLine(String from, String message, long tick, Color color) {
            this.from = from;
            this.message = message;
            this.tick = tick;
            this.color = color;
        }
    }

    public void windowClosed() {
        super.windowClosed();
        alive = false;
    }

    public void save() throws Exception {
    }

    public void revert() throws Exception {
    }

    public void serialize(ObjectOutputStream out) throws IOException {
    }

    public MDIWindow deserialize(ObjectInputStream in) throws IOException {
        return null;
    }
}
