package src.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.backend.*;
import java.io.File;

public class CommentFrame extends JFrame {

    private Model model;

    private int levelIndex;

    public CommentFrame(Model model, int levelIndex) {
        try {
            this.model = model;
            this.levelIndex = levelIndex;
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        FixedSizePlainDocument doc = new FixedSizePlainDocument(StaticFunctions.MAX_JUCE_COMMENT_SIZE);
        getContentPane().setLayout(gridBagLayout1);
        txtComment.setBorder(BorderFactory.createLineBorder(Color.black));
        txtComment.setDocument(doc);
        txtComment.setText("");
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);
        btnOK.setText("OK");
        btnOK.addActionListener(new CommentFrame_btnOK_actionAdapter(this));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new CommentFrame_btnCancel_actionAdapter(this));
        this.setJMenuBar(jMenuBar1);
        jMenu1.setText("Commands");
        jMenuItem1.setText("Read from file");
        jMenuItem1.addActionListener(new CommentFrame_jMenuItem1_actionAdapter(this));
        jMenuBar1.add(jMenu1);
        jMenu1.add(jMenuItem1);
        jScrollPane1.getViewport().add(txtComment);
        this.getContentPane().add(jScrollPane1, new GridBagConstraints(0, 0, 2, 5, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 200, 0));
        this.getContentPane().add(btnOK, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 18, 0));
        this.getContentPane().add(btnCancel, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
        txtComment.setText(model.getComment(levelIndex));
        setSize(410, 345);
        setTitle("Level Comment - " + model.getLevelSelectName());
        StaticFunctions.centerFrame(this);
        setVisible(true);
    }

    JTextArea txtComment = new JTextArea();

    JButton btnOK = new JButton();

    JButton btnCancel = new JButton();

    JMenuBar jMenuBar1 = new JMenuBar();

    JMenu jMenu1 = new JMenu();

    JMenuItem jMenuItem1 = new JMenuItem();

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    JScrollPane jScrollPane1 = new JScrollPane();

    public void btnCancel_actionPerformed(ActionEvent e) {
        model.removeChildFrame(this);
        this.dispose();
    }

    public void btnOK_actionPerformed(ActionEvent e) {
        model.setComment(levelIndex, txtComment.getText());
        model.removeChildFrame(this);
        this.dispose();
    }

    public void jMenuItem1_actionPerformed(ActionEvent e) {
        String openFile = "";
        final JFileChooser fc = new JFileChooser();
        String path = Preferences.MAPS_PATH;
        try {
            if (!path.equals("")) fc.setCurrentDirectory(new File(path));
        } catch (Exception ex) {
            fc.setCurrentDirectory(new File("user.dir"));
        }
        int retVal = fc.showOpenDialog(CommentFrame.this);
        if (retVal == JFileChooser.APPROVE_OPTION) openFile = fc.getSelectedFile().getPath(); else if (retVal == JFileChooser.CANCEL_OPTION) return;
        GetFile theFile = new GetFile(openFile);
        String text = theFile.getNumChars(512);
        txtComment.setText(text);
        theFile.close();
    }
}

class CommentFrame_jMenuItem1_actionAdapter implements ActionListener {

    private CommentFrame adaptee;

    CommentFrame_jMenuItem1_actionAdapter(CommentFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jMenuItem1_actionPerformed(e);
    }
}

class CommentFrame_btnOK_actionAdapter implements ActionListener {

    private CommentFrame adaptee;

    CommentFrame_btnOK_actionAdapter(CommentFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnOK_actionPerformed(e);
    }
}

class CommentFrame_btnCancel_actionAdapter implements ActionListener {

    private CommentFrame adaptee;

    CommentFrame_btnCancel_actionAdapter(CommentFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnCancel_actionPerformed(e);
    }
}

class FixedSizePlainDocument extends PlainDocument {

    int maxSize;

    public FixedSizePlainDocument(int limit) {
        maxSize = limit;
    }

    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        if ((getLength() + str.length()) <= maxSize) {
            super.insertString(offset, str, a);
        } else {
            throw new BadLocationException("Insertion location is not valid", offset);
        }
    }
}
