package chat.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import chat.client.gui.Main;
import chat.client.messages.ChatMessage;
import chat.client.messages.FileInfoMessage;

/**
 * a session for a peer-to-peer-chat with one other computer
 * 
 * @author Daniel
 */
public class ChatSession {

    protected final InetAddress addr;

    protected ChatFrame frame;

    protected final int port;

    protected SendThread sendT;

    protected final TextThread tt;

    protected final LinkedList<ChatMessage> incomingMsgs;

    static HashMap<String, Icon> smileys = new HashMap<String, Icon>();

    static {
        try {
            ChatSession.smileys.put("(angel)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/angel.png"))));
            ChatSession.smileys.put("(angry)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/angry.png"))));
            ChatSession.smileys.put("(beer)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/beer.png"))));
            ChatSession.smileys.put(":D", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/biggrin.png"))));
            ChatSession.smileys.put("8.", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/blink.png"))));
            ChatSession.smileys.put("(blush)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/blush.png"))));
            ChatSession.smileys.put("(boom)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/bomb.png"))));
            ChatSession.smileys.put("(cool)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/cool.png"))));
            ChatSession.smileys.put(";(", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/cry.png"))));
            ChatSession.smileys.put("(devil)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/devil.png"))));
            ChatSession.smileys.put("(drool)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/drool.png"))));
            ChatSession.smileys.put("(getlost)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/getlost.png"))));
            ChatSession.smileys.put(":)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/grin.png"))));
            ChatSession.smileys.put(":,", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/happy.png"))));
            ChatSession.smileys.put("(love)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/inlove.png"))));
            ChatSession.smileys.put("(kiss)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/kiss.png"))));
            ChatSession.smileys.put("(kissed)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/kissed.png"))));
            ChatSession.smileys.put("(kissing)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/kissing.png"))));
            ChatSession.smileys.put("(laugh)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/laughing.png"))));
            ChatSession.smileys.put("(music)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/music.png"))));
            ChatSession.smileys.put("(><)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/poo.png"))));
            ChatSession.smileys.put(":|", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/pouty.png"))));
            ChatSession.smileys.put("8)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/rolleyes.png"))));
            ChatSession.smileys.put("(rose)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/rose.png"))));
            ChatSession.smileys.put(":(", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/sad.png"))));
            ChatSession.smileys.put("8o", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/shock.png"))));
            ChatSession.smileys.put(":o", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/shocked.png"))));
            ChatSession.smileys.put("(sick)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/sick.png"))));
            ChatSession.smileys.put("=]", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/sideways.png"))));
            ChatSession.smileys.put("zzZ", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/sleep.png"))));
            ChatSession.smileys.put("=)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/smile.png"))));
            ChatSession.smileys.put(":X", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/stfu.png"))));
            ChatSession.smileys.put(":}", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/stop.png"))));
            ChatSession.smileys.put("(teeth)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/teeth.png"))));
            ChatSession.smileys.put("(-)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/thumbdown.png"))));
            ChatSession.smileys.put("(+)", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/thumbsup.png"))));
            ChatSession.smileys.put(":>", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/tongue.png"))));
            ChatSession.smileys.put("8§", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/wacko.png"))));
            ChatSession.smileys.put(";/", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/wink.png"))));
            ChatSession.smileys.put("8/", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/wrong.png"))));
            ChatSession.smileys.put(":O", new ImageIcon(ImageIO.read(ChatSession.class.getResource("/chat/icons/yawn.png"))));
        } catch (final IOException e) {
            JOptionPane.showMessageDialog(Main.list, "Couldn't load smileys-files.");
            e.printStackTrace();
        }
    }

    ChatSession(final InetAddress addr, final int port) {
        this.addr = addr;
        this.port = port;
        this.incomingMsgs = new LinkedList<ChatMessage>();
        this.sendT = new SendThread(addr, port);
        this.sendT.start();
        this.frame = new ChatFrame(this);
        this.tt = new TextThread();
        this.tt.start();
    }

    /**
	 * @param msg
	 * @param output
	 */
    public void send(final ChatMessage msg, final boolean output) {
        this.sendT.sendMsg(msg);
        if (output) {
            this.addMsg(msg);
        }
    }

    /**
	 * @param msg
	 */
    public void addMsg(final ChatMessage msg) {
        this.incomingMsgs.add(msg);
    }

    /**
	 * closes this {@link ChatSession} and hides the ChatFrame
	 */
    public void close() {
        this.sendT.kill();
        this.tt.kill();
        Connection.getConnection(false).openSessions.remove(this);
        if (this.frame.isVisible()) {
            this.frame.setVisible(false);
        }
        this.frame.dispose();
        System.gc();
        if (Connection.getConnection(false).receiveThread.frt != null) {
            if (Connection.getConnection(false).receiveThread.frt.curSess.equals(this)) {
                Connection.getConnection(false).receiveThread.frt.weiter = false;
            }
        }
        Main.updateTrayPopup();
    }

    /**
	 * @return addr
	 */
    public InetAddress getAddress() {
        return this.addr;
    }

    /**
	 * 
	 */
    public void sendFile() {
        if (this.sendT.isSendingFile()) {
            JOptionPane.showMessageDialog(this.frame, "There is already an outgoing file-transfer", "Warning", JOptionPane.OK_CANCEL_OPTION);
            return;
        }
        final JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            final File f = fc.getSelectedFile();
            this.send(ChatMessage.sendFileName(this.addr.getHostName(), new SimpleDateFormat("hh':'mm':'ss").format(new Date()), f), false);
            this.sendT.setSendFile(f);
            this.frame.addMsg("Send file... " + f.getName() + "\n", false);
        }
    }

    /**
	 * @param download
	 */
    public void sendFileData(final boolean download) {
        if (!download) {
            this.sendT.abortSend();
            this.frame.addMsg("Filetransfer aborted by user", false);
        }
        if (this.sendT.getSendFile() != null) {
            this.sendT.sendFile();
        }
    }

    /**
	 * This method prints an message if the file was successfully sent
	 * 
	 * @param success
	 */
    public void sendFileFinished(final boolean success) {
        if (success) {
            this.frame.addMsg("Filetransfer completed", false);
        } else {
            this.frame.addMsg("Filetransfer aborted", false);
        }
    }

    /**
	 * @param rth
	 * @param msg
	 * @param cl
	 * @throws IOException
	 */
    public void receiveFile(final RecieveThread rth, final FileInfoMessage msg, final InetSocketAddress cl) throws IOException {
        final JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(msg.getFileName()));
        final int i = fc.showSaveDialog(null);
        if (i == JFileChooser.APPROVE_OPTION) {
            if (fc.getSelectedFile().exists()) {
                final int k = JOptionPane.showConfirmDialog(null, "Do you really want to replace this existing file?", "File already exists", JOptionPane.YES_NO_OPTION);
                if (k == JOptionPane.NO_OPTION) {
                    rth.recievedFileInfoMessage(msg, cl);
                    return;
                }
                if (k == ImageObserver.ABORT) {
                    return;
                }
            }
            this.frame.addMsg("Recieving file...", false);
            rth.startFileReceiveThread(msg, fc, cl);
        } else if (i == JFileChooser.CANCEL_OPTION) {
            rth.declineReceivedFile(msg, cl);
        }
    }

    /**
	 * @param success
	 */
    public void receivedFile(final boolean success) {
        if (success) {
            this.frame.addMsg("Filetransfer completed", false);
        } else {
            this.frame.addMsg("Filetransfer aborted", false);
        }
    }

    /**
	 * Prints the given text to the dialog
	 * 
	 * @param text
	 */
    public void printText(final String text) {
        this.frame.addMsg(text, false);
    }

    /**
	 * @author Daniel
	 */
    protected static class ChatFrame extends JFrame {

        private static final long serialVersionUID = -468171148107062726L;

        final transient ChatSession ses;

        final JTextPane textPane;

        JTextField textField;

        JToolBar smileyBar;

        /**
		 * @param ses
		 */
        public ChatFrame(final ChatSession ses) {
            this.ses = ses;
            this.textPane = new JTextPane();
            this.textPane.setEditable(false);
            this.textPane.setAutoscrolls(true);
            this.textPane.setContentType("text/html");
            this.add(new JLabel(ses.getAddress().getHostName()), BorderLayout.NORTH);
            this.add(new JScrollPane(this.textPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
            this.add(this.getSouthPanel(), BorderLayout.SOUTH);
            this.setTitle("Chat-" + ses.getAddress().getHostName());
            this.setSize(620, 500);
            this.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(final WindowEvent e) {
                    ChatFrame.this.setVisible(false);
                    ses.close();
                }

                @Override
                public void windowGainedFocus(final WindowEvent e) {
                    ChatFrame.this.textField.grabFocus();
                }
            });
            this.addFocusListener(new FocusAdapter() {

                @Override
                public void focusGained(final FocusEvent e) {
                    ChatFrame.this.textField.grabFocus();
                }
            });
            this.textPane.addHyperlinkListener(new HyperlinkListener() {

                @Override
                public void hyperlinkUpdate(final HyperlinkEvent e) {
                    try {
                        Runtime.getRuntime().exec(e.getURL().toString());
                        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            final StringTokenizer st = new StringTokenizer(e.getDescription(), " ");
                            if (st.hasMoreTokens()) {
                                final String s = st.nextToken();
                                System.err.println("token: " + s);
                            }
                        }
                    } catch (final IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }

        private JPanel getTypePanel() {
            final JPanel p = new JPanel();
            this.textField = new JTextField();
            this.textField.setColumns(30);
            this.textField.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (ChatFrame.this.textField.getText().length() >= 1) {
                        ChatFrame.this.send();
                    }
                }
            });
            p.add(this.textField, BorderLayout.WEST);
            final JButton send = new JButton("Send");
            send.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    if (ChatFrame.this.textField.getText().length() >= 1) {
                        ChatFrame.this.send();
                    }
                    ChatFrame.this.textField.grabFocus();
                }
            });
            p.add(send, BorderLayout.CENTER);
            final JButton sendFile = new JButton("Send File");
            sendFile.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    ChatFrame.this.ses.sendFile();
                }
            });
            p.add(sendFile, BorderLayout.EAST);
            final JButton smileys = new JButton(ChatSession.smileys.get(":)"));
            smileys.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    ChatFrame.this.smileyBar.setVisible(!ChatFrame.this.smileyBar.isVisible());
                }
            });
            p.add(smileys, BorderLayout.NORTH);
            return p;
        }

        private JPanel getSouthPanel() {
            final JPanel south = new JPanel(new BorderLayout());
            south.add(this.getTypePanel(), BorderLayout.NORTH);
            south.add(this.getSmileyBar(), BorderLayout.SOUTH);
            this.smileyBar.setVisible(false);
            return south;
        }

        private JToolBar getSmileyBar() {
            if (this.smileyBar != null) {
                return this.smileyBar;
            }
            this.smileyBar = new JToolBar();
            this.smileyBar.setLayout(new GridLayout(0, 21));
            synchronized (ChatSession.smileys) {
                for (final String k : ChatSession.smileys.keySet()) {
                    final JButton b = new JButton(ChatSession.smileys.get(k));
                    b.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(final ActionEvent e) {
                            ChatFrame.this.textField.setText(ChatFrame.this.textField.getText() + k + " ");
                            ChatFrame.this.textField.grabFocus();
                        }
                    });
                    b.setToolTipText(k);
                    this.smileyBar.add(b);
                }
            }
            return this.smileyBar;
        }

        void addMsg(final String text, final boolean incoming) {
            final Document doc = this.textPane.getDocument();
            if (doc != null) {
                try {
                    doc.insertString(doc.getLength(), text + "\n", null);
                } catch (final BadLocationException e) {
                }
            }
            this.replaceTextThroughSmileys(text, doc);
            if (Connection.getConnection(false).log != null) {
                if (!text.contains("ping")) {
                    Connection.getConnection(false).log.println(incoming ? "Incoming: " + text : "Outgoing: " + text, this.ses);
                }
            }
        }

        private void replaceTextThroughSmileys(final String text, final Document doc) {
            for (final String k : ChatSession.smileys.keySet()) {
                if (text.contains(k)) {
                    int i = doc.getLength() - text.length();
                    while ((i = this.textPane.getText().indexOf(k, i)) != -1 && i <= this.textPane.getText().length()) {
                        this.textPane.setSelectionStart(i);
                        this.textPane.setSelectionEnd(i += k.length());
                        this.textPane.insertIcon(ChatSession.smileys.get(k));
                    }
                }
            }
        }

        void addMsg(final ChatMessage msg, final String text, final boolean incoming) {
            final Document doc = this.textPane.getDocument();
            int offset = 0;
            if (doc != null) {
                try {
                    doc.insertString(offset = doc.getLength(), text + "\n", null);
                } catch (final BadLocationException e) {
                }
            }
            this.replaceTextThroughSmileys(text, doc);
            try {
                if (doc == null) {
                    return;
                }
                doc.insertString(offset, msg.getSender() + " " + msg.getTime() + " : ", null);
            } catch (final BadLocationException e) {
                e.printStackTrace();
            }
            if (Connection.getConnection(false).log != null) {
                if (!text.contains("ping")) {
                    Connection.getConnection(false).log.println(incoming ? "Incoming: " + msg.getSender() + " " + msg.getTime() + " : " + text : "Outgoing: " + msg.getSender() + " " + msg.getTime() + " : " + text, this.ses);
                }
            }
        }

        void send() {
            System.out.println(this.textField.getText());
            final String text = new String(this.textField.getText().getBytes(), Charset.forName("UTF-8"));
            System.out.println(text);
            final SimpleDateFormat f = new SimpleDateFormat("hh':'mm':'ss");
            this.textField.setText("");
            this.ses.send(ChatMessage.sendMsg(Connection.getConnection(false).getInetAddress().getHostName(), f.format(new Date()), text), true);
        }
    }

    class TextThread extends Thread {

        private boolean weiter;

        public TextThread() {
            this.weiter = true;
        }

        @Override
        public void run() {
            while (this.weiter) {
                while (!ChatSession.this.incomingMsgs.isEmpty()) {
                    final ChatMessage msg = ChatSession.this.incomingMsgs.poll();
                    if (msg.getSender().equals(Connection.getConnection(false).getInetAddress().getHostName())) {
                        ChatSession.this.frame.addMsg(msg, msg.getContent(), false);
                    } else {
                        ChatSession.this.frame.addMsg(msg, msg.getContent(), true);
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void kill() {
            this.weiter = false;
        }
    }

    /**
	 * @return the {@link JFrame} for this Session
	 */
    public JFrame getFrame() {
        return this.frame;
    }
}
