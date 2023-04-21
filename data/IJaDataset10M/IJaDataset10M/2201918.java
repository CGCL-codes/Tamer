package diet.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Chat window interface with separate text entry area. Text is only sent when participant presses
 * "ENTER"/"RETURN"/"SEND". Can be configured with single window (similar to existing commercial chat
 * tools) or with multiple windows.
 * @author user
 */
public class JChatFrameMultipleWindowsWithSendButton extends JChatFrame {

    JCollectionOfChatWindows jccw;

    BorderLayout borderLayout1 = new BorderLayout();

    BorderLayout borderLayout2 = new BorderLayout();

    BorderLayout borderLayout3 = new BorderLayout();

    JPanel jPanel1 = new JPanel();

    JButton jSENDButton = new JButton();

    JPanel jPanel2 = new JPanel();

    JScrollPane jTextEntryAreaScrollPane = new JScrollPane();

    JTextArea jEnterTextArea = new JTextArea();

    JLabel jLabeldisplay = new JLabel("Normal operation");

    int participantsOwnWindowForTextEntry;

    InputDocumentListener jtaDocumentListener;

    public JChatFrameMultipleWindowsWithSendButton(ClientEventHandler clevh, int numberOfWindows, int numberOfRows, int numberOfColumns, boolean isVertical, boolean hasStatusWindow, int windowOfOwnText, int numberOfRowsInTextEntry) {
        super(clevh);
        try {
            this.getContentPane().setLayout(borderLayout1);
            jccw = new JCollectionOfChatWindows(numberOfWindows, numberOfRows, numberOfColumns, isVertical, hasStatusWindow, windowOfOwnText);
            this.getContentPane().add(jccw, BorderLayout.NORTH);
            jSENDButton.setHorizontalTextPosition(SwingConstants.CENTER);
            jPanel1.setLayout(borderLayout2);
            this.getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
            jPanel1.add(jPanel2, BorderLayout.EAST);
            this.participantsOwnWindowForTextEntry = windowOfOwnText;
            jPanel2.add(jSENDButton, java.awt.BorderLayout.EAST);
            jSENDButton.setPreferredSize(new Dimension(73, 64));
            jSENDButton.setText("SEND");
            jSENDButton.addActionListener(new JChatFrameSENDButtonActionListener());
            jSENDButton.setMargin(new Insets(0, 0, 0, 0));
            jEnterTextArea.setRows(numberOfRowsInTextEntry);
            jEnterTextArea.setLineWrap(true);
            jTextEntryAreaScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jtaDocumentListener = new InputDocumentListener(jEnterTextArea);
            jEnterTextArea.getDocument().addDocumentListener(jtaDocumentListener);
            jTextEntryAreaScrollPane.getViewport().add(jEnterTextArea);
            jEnterTextArea.addKeyListener(new JChatFrameKeyEventListener());
            jPanel1.add(jTextEntryAreaScrollPane, BorderLayout.CENTER);
            this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            setVisible(true);
            this.pack();
            this.validate();
        } catch (Exception ex) {
            System.err.println("COULD NOT SET UP CHAT INTERFACE");
        }
    }

    @Override
    public void setLabelAndTextEntryEnabled(int windowOfOwnText, String label, boolean textIsInRed, boolean setWindowEnabled) {
        Vector jTextAreas = jccw.getTextAreas();
        Vector scrollPanes = jccw.getScrollPanes();
        Vector jLabels = jccw.getJLabels();
        if (windowOfOwnText >= jLabels.size()) return;
        JLabel jls = (JLabel) jLabels.elementAt(windowOfOwnText);
        if (windowOfOwnText >= scrollPanes.size()) return;
        JTextArea jta = jEnterTextArea;
        SwingUtilities.invokeLater(new DoSetLabelAndTextEntryAndSendButtonEnabled(jta, jSENDButton, jls, label, textIsInRed, setWindowEnabled));
    }

    @Override
    public void setLabel(int windowNumber, String label, boolean textIsInRed) {
        Vector jTextAreas = jccw.getTextAreas();
        Vector scrollPanes = jccw.getScrollPanes();
        Vector jLabels = jccw.getJLabels();
        if (windowNumber >= jLabels.size()) return;
        final JLabel jls = (JLabel) jLabels.elementAt(windowNumber);
        final String labl = label;
        final boolean textIsRed = textIsInRed;
        if (windowNumber >= scrollPanes.size()) return;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                jls.setText(labl);
                if (textIsRed) {
                    jls.setForeground(Color.RED);
                } else {
                    jls.setForeground(Color.BLACK);
                }
            }
        });
    }

    @Override
    public void appendWithCaretCheck(String text, int windowNumber) {
        Vector jTextAreas = jccw.getTextAreas();
        Vector scrollPanes = jccw.getScrollPanes();
        if (windowNumber >= scrollPanes.size()) return;
        JTextArea jta = (JTextArea) jTextAreas.elementAt(windowNumber);
        JScrollPane jsp = (JScrollPane) scrollPanes.elementAt(windowNumber);
        SwingUtilities.invokeLater(new DoAppendTextWithCaretCheck(jta, jsp, text));
    }

    @Override
    public void setTextEntryField(String s) {
        SwingUtilities.invokeLater(new DoSetTextEntryField(jEnterTextArea, s));
    }

    @Override
    public int getParticipantsOwnWindow() {
        return this.participantsOwnWindowForTextEntry;
    }

    @Override
    public String getTextEnteredInField() {
        return jEnterTextArea.getText();
    }

    public void sendButtonPressed(ActionEvent e) {
        getClientEventHandler().sendButtonPressed();
    }

    void keyPressed(KeyEvent e) {
        getClientEventHandler().keyPressFilter(e);
    }

    void keyReleased(KeyEvent e) {
        this.getClientEventHandler().keyReleaseFilter(e);
    }

    class JCollectionOfChatWindows extends JPanel {

        private Vector scrollPanes = new Vector();

        private Vector jLabels = new Vector();

        private Vector jTextAreas = new Vector();

        BoxLayout blyout;

        String labelSpacePadding = "                                         ";

        boolean hasStatusWindow;

        int windowOfParticipant = 0;

        public JCollectionOfChatWindows(int numberOfWindows, int numberOfRows, int numberOfColumns, boolean isVertical, boolean hasStatusWindow, int windowOfParticipant) {
            super();
            if (isVertical) {
                blyout = new BoxLayout(this, BoxLayout.Y_AXIS);
            } else {
                blyout = new BoxLayout(this, BoxLayout.X_AXIS);
                labelSpacePadding = "";
            }
            this.hasStatusWindow = hasStatusWindow;
            this.setLayout(blyout);
            this.windowOfParticipant = windowOfParticipant;
            for (int i = 0; i < numberOfWindows; i++) {
                JTextArea jTexta = new JTextArea(numberOfRows, numberOfColumns);
                jTextAreas.addElement(jTexta);
                JLabel jlt = new JLabel("Network status ok" + labelSpacePadding);
                jlt.setFont(new java.awt.Font("Dialog", 0, 12));
                jTexta.setLineWrap(true);
                jTexta.setFocusable(false);
                JScrollPane jscr = new JScrollPane();
                jscr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                jscr.getViewport().add(jTexta);
                scrollPanes.addElement(jscr);
                this.add(jscr);
                if (hasStatusWindow) {
                    jLabels.addElement(jlt);
                    this.add(jlt);
                }
            }
        }

        public Vector getTextAreas() {
            return jTextAreas;
        }

        public Vector getScrollPanes() {
            return scrollPanes;
        }

        public Vector getJLabels() {
            return jLabels;
        }
    }

    class JChatFrameKeyEventListener extends java.awt.event.KeyAdapter {

        JChatFrameKeyEventListener() {
        }

        public void keyPressed(KeyEvent e) {
            getClientEventHandler().keyPressFilter(e);
            System.out.println("EVENTLISTENER DETERMINES KEYPRESSED");
        }

        public void keyReleased(KeyEvent e) {
            getClientEventHandler().keyReleaseFilter(e);
            System.out.println("EVENTLISTENER DETERMINES KEYRELEASED");
        }
    }

    class JChatFrameSENDButtonActionListener implements java.awt.event.ActionListener {

        JChatFrameSENDButtonActionListener() {
        }

        public void actionPerformed(ActionEvent e) {
            System.out.println("EVENTLISTENER DETERMINES SEND BUTTON PRESSED");
            getClientEventHandler().sendButtonPressed();
        }
    }

    public class DoScrolling implements Runnable {

        JScrollPane jsp;

        public DoScrolling(JScrollPane jsp) {
            this.jsp = jsp;
        }

        public void run() {
            jsp.validate();
            jsp.repaint();
            jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
            jsp.validate();
            jsp.repaint();
        }
    }

    public class DoSetTextEntryField implements Runnable {

        JTextArea jta;

        String text;

        public DoSetTextEntryField(JTextArea jta, String text) {
            this.jta = jta;
            this.text = text;
        }

        public void run() {
            jta.getDocument().removeDocumentListener(jtaDocumentListener);
            jta.setText(text);
            jEnterTextArea.getDocument().addDocumentListener(jtaDocumentListener);
        }
    }

    public class DoAppendTextWithCaretCheck implements Runnable {

        JTextArea jta;

        String text;

        JScrollPane jsp;

        public DoAppendTextWithCaretCheck(JTextArea jta, JScrollPane jsp, String text) {
            this.jta = jta;
            this.jsp = jsp;
            this.text = text;
        }

        public void run() {
            jta.append(text);
            SwingUtilities.invokeLater(new DoScrolling(jsp));
        }
    }

    public class DoSetLabelAndTextEntryAndSendButtonEnabled implements Runnable {

        JTextArea jta = new JTextArea();

        JScrollPane jsp = new JScrollPane();

        boolean setEnabled;

        JLabel label;

        String text;

        boolean textIsInRed;

        JButton jSENDB;

        public DoSetLabelAndTextEntryAndSendButtonEnabled(JTextArea jta, JButton jSENDB, JLabel label, String text, boolean textIsInRed, boolean setEnabled) {
            this.jta = jta;
            this.jsp = jsp;
            this.setEnabled = setEnabled;
            this.label = label;
            this.text = text;
            this.textIsInRed = textIsInRed;
            this.jSENDB = jSENDB;
        }

        public void run() {
            try {
                jta.setEnabled(setEnabled);
                jta.setEditable(setEnabled);
                jta.setFocusable(setEnabled);
                jSENDB.setEnabled(setEnabled);
                if (textIsInRed) {
                    label.setForeground(Color.RED);
                } else {
                    label.setForeground(Color.BLACK);
                }
                label.setText(text);
                if (setEnabled) {
                    jta.requestFocus();
                }
            } catch (Exception e) {
                System.err.println("Error changing blocked status of window");
            }
        }
    }

    class InputDocumentListener implements DocumentListener {

        JTextArea jTextAreaSource;

        public InputDocumentListener(JTextArea jTextAreaSource) {
            this.jTextAreaSource = jTextAreaSource;
        }

        public void insertUpdate(DocumentEvent e) {
            updateInsert(e);
        }

        public void removeUpdate(DocumentEvent e) {
            updateRemove(e);
        }

        public void changedUpdate(DocumentEvent e) {
        }

        public void updateInsert(DocumentEvent e) {
            int offset = e.getOffset();
            int length = e.getLength();
            try {
                int documentlength = jEnterTextArea.getDocument().getLength();
                int insrtIndex = (documentlength - length) - offset;
                getClientEventHandler().textEntryDocumentHasChangedInsert(jTextAreaSource.getText().substring(offset, offset + length), insrtIndex, length, jTextAreaSource.getText());
            } catch (Error e2) {
                System.err.println("OFFSET ERROR " + offset + " , " + jTextAreaSource.getText().length() + " " + jTextAreaSource.getText() + " " + e2.getStackTrace());
            }
        }

        public void updateRemove(DocumentEvent e) {
            int offset = e.getOffset();
            int length = e.getLength();
            int documentlength = jEnterTextArea.getDocument().getLength();
            int insrtIndex = (documentlength + length) - offset;
            try {
                getClientEventHandler().wYSIWYGDocumentHasChangedRemove(insrtIndex, length);
            } catch (Exception e2) {
                System.err.println("ERROR ATTEMPTING TO CAPTURE A DELETE");
            }
        }
    }

    public void closeDown() {
        this.jEnterTextArea.setEnabled(false);
        this.jLabeldisplay.setEnabled(false);
        this.jTextEntryAreaScrollPane.setEnabled(false);
        this.setVisible(false);
        super.dispose();
    }
}
