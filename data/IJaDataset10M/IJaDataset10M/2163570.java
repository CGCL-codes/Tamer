package spamwatch.gui.message;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import spamwatch.base.store.ContentInfo;
import spamwatch.base.store.EMailStore;
import spamwatch.gui.message.action.SendAction;
import spamwatch.util.StringUtil;

public class ComposeView extends JFrame {

    private JTextField toTF;

    private JTextField fromTF;

    private JTextField subjectTF;

    private JEditorPane displayPane;

    private SendAction sendAction;

    private EMailStore source;

    public ComposeView(SendAction sendAction, EMailStore source) {
        this.sendAction = sendAction;
        this.source = source;
        initGUI();
        initValues(source);
        setSize(800, 600);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (source != null) {
            displayPane.requestFocusInWindow();
        } else {
            toTF.requestFocusInWindow();
        }
    }

    private void initValues(EMailStore source) {
        fromTF.setText(sendAction.getDefaultSendAddress());
        if (source != null) {
            fromTF.setText(source.getRecipients()[0].getEmailAdress());
            toTF.setText(source.getSender()[0].getEmailAdress());
            subjectTF.setText("Re: " + source.getSubject());
            ContentInfo ci = source.getContents().next();
            String content = ci.getContent();
            if (ci.getType().contains("html")) {
                content = StringUtil.unescapeHTML(StringUtil.convertToText(content));
            }
            content = "\n\n" + toTF.getText() + " wrote:\n" + content;
            displayPane.setSize(displayPane.getParent().getSize());
            displayPane.setText(content);
            displayPane.setCaretPosition(0);
            displayPane.scrollRectToVisible(new Rectangle());
        } else {
            displayPane.setText("");
        }
    }

    private void initGUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Compose new message");
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        JPanel labelPanel = new JPanel(new GridLayout(0, 1, 0, 0));
        JPanel valuePanel = new JPanel(new GridLayout(0, 1, 0, 0));
        labelPanel.add(new JLabel("From:"));
        fromTF = new JTextField();
        valuePanel.add(fromTF);
        labelPanel.add(new JLabel("To:"));
        toTF = new JTextField();
        valuePanel.add(toTF);
        labelPanel.add(new JLabel("Subject:"));
        subjectTF = new JTextField();
        valuePanel.add(subjectTF);
        topPanel.add(labelPanel, BorderLayout.WEST);
        topPanel.add(valuePanel, BorderLayout.CENTER);
        cp.add(topPanel, BorderLayout.NORTH);
        displayPane = new JEditorPane();
        displayPane.setEditable(true);
        cp.add(new JScrollPane(displayPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);
        cp.add(new JButton(sendAction), BorderLayout.SOUTH);
    }

    public String getContent() {
        return displayPane.getText();
    }

    public String getFrom() {
        return fromTF.getText();
    }

    public String getTo() {
        return toTF.getText();
    }

    public String getSubject() {
        return subjectTF.getText();
    }
}
