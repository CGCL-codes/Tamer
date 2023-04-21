package graphics;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.net.*;
import java.io.*;
import model.*;

public class DiscussionPanel extends JPanel implements ActionListener, HyperlinkListener, Observer {

    private MilooWindow parent;

    private HistoryManager history_manager;

    private JEditorPane editorPane;

    private JPopupMenu popup_nav;

    private JMenuItem menu_actu, menu_sourcecode;

    private String currURL = null;

    private JScrollPane scrollPane;

    private JFrame fenetre;

    public DiscussionPanel(MilooWindow parent) {
        this.parent = parent;
        initialize();
        setLayout(new BorderLayout());
        history_manager = new HistoryManager();
        history_manager.addObserver(this);
        currURL = history_manager.getHistory();
        popup_nav = new JPopupMenu();
        menu_sourcecode = new JMenuItem("Source");
        menu_sourcecode.addActionListener(this);
        menu_sourcecode.setMnemonic('S');
        popup_nav.add(menu_sourcecode);
        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(this);
        editorPane.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                checkTrigger(e);
            }

            public void mouseReleased(MouseEvent e) {
                checkTrigger(e);
            }

            private void checkTrigger(MouseEvent e) {
                if (e.isPopupTrigger()) popup_nav.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        scrollPane = new JScrollPane(editorPane);
        add(scrollPane, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createTitledBorder("Historique de la conversation"));
    }

    private void initialize() {
        fenetre = new JFrame("Chatting Box");
        fenetre.add(this);
        fenetre.setLocation(200, 100);
        fenetre.pack();
        fenetre.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent ev) {
                System.out.println("Fenetre DiscussionPanel closing");
                parent.unselectChattingBoxMenu();
            }
        });
    }

    public void show() {
        fenetre.setVisible(true);
    }

    public void hide() {
        fenetre.setVisible(false);
    }

    public void update(Observable o, Object arg) {
        System.out.println("DiscussionPanel Update !");
        updatePanel();
    }

    public void updateHistory(String login) {
        history_manager.setLoginUser(login);
        currURL = history_manager.getHistory();
        updatePanel();
    }

    public HistoryManager getHistoryManager() {
        return history_manager;
    }

    private boolean beginsWith(String source, String begin) {
        for (int i = 0; i < begin.length(); i++) {
            char c1 = source.charAt(i);
            char c2 = begin.charAt(i);
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }

    private void updatePanel() {
        if (!beginsWith(currURL, "http://") && !beginsWith(currURL, "file://")) currURL = "file://localhost/" + currURL;
        try {
            editorPane.setText(getHistoryContent());
            System.out.println("currURL = " + currURL);
            editorPane.setPage(currURL);
            editorPane.setCaretPosition(editorPane.getDocument().getLength());
        } catch (MalformedURLException e) {
            System.err.println("error update");
        } catch (FileNotFoundException e) {
            System.err.println("error update");
        } catch (IOException e) {
            System.err.println("error update");
        } catch (Exception e) {
            System.err.println("error update");
        }
    }

    public void clearDiscussionPanel() {
        editorPane.setText("");
    }

    private String getHistoryContent() {
        return history_manager.getHistoryContent();
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            currURL = e.getURL().toString();
            updatePanel();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "Source") {
            if (currURL != null) {
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(500, 300);
    }
}
