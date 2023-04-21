package hatenaSwing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;

public class LinkFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JButton cancelButton;

    private JButton okButton;

    private JTextField urlField;

    private DefaultStyledDocument doc;

    private MutableAttributeSet mattrset;

    private MouseClickedTextPane postPane;

    public void setPostPane(final MouseClickedTextPane p) {
        this.postPane = p;
    }

    public void setDoc(final DefaultStyledDocument d) {
        this.doc = d;
    }

    public void setMattrsetfinal(MutableAttributeSet m) {
        this.mattrset = m;
    }

    private JButton makeButton(final String s, final String command) {
        final JButton b = new JButton();
        b.setText(s);
        b.addActionListener(this);
        b.setActionCommand(command);
        return b;
    }

    public LinkFrame() {
        super("Link");
        this.setPreferredSize(new Dimension(400, 100));
        this.setLocation(300, 300);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.urlField = new JTextField();
        this.okButton = makeButton("OK", "OK");
        this.cancelButton = makeButton("Cancel", "Cancel");
        this.urlField.setText("http://");
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.urlField, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(this.cancelButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.okButton))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.urlField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.okButton).addComponent(this.cancelButton)).addContainerGap()));
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            int start = this.postPane.getSelectionStart();
            int end = this.postPane.getSelectionEnd();
            InsertString is = new InsertString();
            is.setDoc(this.doc);
            if (start == end) is.setBeforeString("[" + this.urlField.getText() + ":title"); else is.setBeforeString("[" + this.urlField.getText() + ":title=");
            is.setSelectionStart(start);
            is.setAfterString("]");
            is.setSelectionEnd(end);
            is.setMattrsetfinal(this.mattrset);
            is.setPostPane(this.postPane);
            is.insert();
            this.setVisible(false);
            this.dispose();
        } else if (e.getActionCommand().equals("Cancel")) {
            this.setVisible(false);
            this.dispose();
        }
    }
}
