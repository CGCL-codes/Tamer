package hatenaSwing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.undo.UndoManager;

public class RightmouseClickedPopupMenu extends JPopupMenu implements ActionListener {

    private MouseClickedTextPane postPane;

    private DefaultStyledDocument doc;

    private MutableAttributeSet attr;

    private JComboBox fontSizeBox;

    private static final long serialVersionUID = 1L;

    private JMenuItem boldItem;

    private JMenuItem italicItem;

    private JMenuItem delItem;

    private JMenuItem fontSizeItem;

    private JMenuItem colorItem;

    private JMenuItem linkItem;

    private JMenuItem ulItem;

    private JMenuItem keyWordItem;

    private JMenuItem olItem;

    private JMenuItem undoItem;

    private JMenuItem redoItem;

    private UndoManager undoManager;

    private JSeparator separator;

    public void setUndoManager(UndoManager u) {
        this.undoManager = u;
    }

    public void setFontSizeBox(JComboBox c) {
        this.fontSizeBox = c;
    }

    public void setPostPane(MouseClickedTextPane m) {
        this.postPane = m;
    }

    public void setDoc(DefaultStyledDocument d) {
        this.doc = d;
    }

    public void setAttr(MutableAttributeSet a) {
        this.attr = a;
    }

    public RightmouseClickedPopupMenu() {
        super("RightmouseClickedPopupMenu");
        this.separator = new JSeparator();
        this.undoItem = makeMenuItem("Undo", KeyEvent.VK_Z);
        this.redoItem = makeMenuItem("Redo", KeyEvent.VK_Y);
        add(this.separator);
        this.boldItem = makeMenuItem("Bold", KeyEvent.VK_B);
        this.italicItem = makeMenuItem("Italic", KeyEvent.VK_I);
        this.delItem = makeMenuItem("Del", KeyEvent.VK_D);
        this.fontSizeItem = makeMenuItem("FontSize", KeyEvent.VK_F);
        this.keyWordItem = makeMenuItem("KeyWord", KeyEvent.VK_K);
        this.linkItem = makeMenuItem("Link", KeyEvent.VK_L);
        this.ulItem = makeMenuItem("ul", KeyEvent.VK_U);
        this.olItem = makeMenuItem("ol", KeyEvent.VK_O);
    }

    public JMenuItem makeMenuItem(String s, int key) {
        JMenuItem mi = new JMenuItem();
        mi.setText(s);
        mi.setActionCommand(s);
        mi.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.CTRL_MASK));
        mi.setMnemonic(key);
        mi.addActionListener(this);
        add(mi);
        return mi;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Bold")) {
            InsertString is = new InsertString();
            is.setDoc(this.doc);
            is.setBeforeString("<span style=\"font-weight:bold;\">");
            is.setSelectionStart(this.postPane.getSelectionStart());
            is.setAfterString("</span>");
            is.setSelectionEnd(this.postPane.getSelectionEnd());
            is.setMattrsetfinal(this.attr);
            is.setPostPane(this.postPane);
            is.insert();
        } else if (e.getActionCommand().equals("Italic")) {
            InsertString is = new InsertString();
            is.setDoc(this.doc);
            is.setBeforeString("<span style=\"font-style:italic;\">");
            is.setSelectionStart(this.postPane.getSelectionStart());
            is.setAfterString("</span>");
            is.setSelectionEnd(this.postPane.getSelectionEnd());
            is.setMattrsetfinal(this.attr);
            is.setPostPane(this.postPane);
            is.insert();
        } else if (e.getActionCommand().equals("Del")) {
            InsertString is = new InsertString();
            is.setDoc(this.doc);
            is.setBeforeString("<del>");
            is.setSelectionStart(this.postPane.getSelectionStart());
            is.setAfterString("</del>");
            is.setSelectionEnd(this.postPane.getSelectionEnd());
            is.setMattrsetfinal(this.attr);
            is.setPostPane(this.postPane);
            is.insert();
        } else if (e.getActionCommand().equals("FontSize")) {
            String[] fontSizeStrings = { "<span style=\"font-size:xx-large;\">", "<span style=\"font-size:x-large;\">", "<span style=\"font-size:large;\">", "<span style=\"font-size:medium;\">", "<span style=\"font-size:small;\">", "<span style=\"font-size:x-small;\">", "<span style=\"font-size:xx-small;\">" };
            int index = this.fontSizeBox.getSelectedIndex();
            if (index != -1) {
                InsertString is = new InsertString();
                is.setDoc(this.doc);
                is.setBeforeString(fontSizeStrings[index]);
                is.setSelectionStart(this.postPane.getSelectionStart());
                is.setAfterString("</span>");
                is.setSelectionEnd(this.postPane.getSelectionEnd());
                is.setMattrsetfinal(this.attr);
                is.setPostPane(this.postPane);
                is.insert();
            }
        } else if (e.getActionCommand().equals("Color")) {
            ColorFrame cf = new ColorFrame();
            cf.setPostPane(this.postPane);
            cf.setMattrsetfinal(this.attr);
            cf.setDoc(this.doc);
        } else if (e.getActionCommand().equals("KeyWord")) {
            InsertString is = new InsertString();
            is.setDoc(this.doc);
            is.setBeforeString("[[");
            is.setSelectionStart(this.postPane.getSelectionStart());
            is.setAfterString("]]");
            is.setSelectionEnd(this.postPane.getSelectionEnd());
            is.setMattrsetfinal(this.attr);
            is.setPostPane(this.postPane);
            is.insert();
        } else if (e.getActionCommand().equals("Link")) {
            LinkFrame lf = new LinkFrame();
            lf.setPostPane(this.postPane);
            lf.setMattrsetfinal(this.attr);
            lf.setDoc(this.doc);
        } else if (e.getActionCommand().equals("ul")) {
            InsertString is = new InsertString();
            is.setDoc(this.doc);
            is.setBeforeString("-");
            is.setSelectionStart(this.postPane.getSelectionStart());
            is.setAfterString("");
            is.setSelectionEnd(this.postPane.getSelectionEnd());
            is.setMattrsetfinal(this.attr);
            is.setPostPane(this.postPane);
            is.insert();
        } else if (e.getActionCommand().equals("ol")) {
            InsertString is = new InsertString();
            is.setDoc(this.doc);
            is.setBeforeString("+");
            is.setSelectionStart(this.postPane.getSelectionStart());
            is.setAfterString("");
            is.setSelectionEnd(this.postPane.getSelectionEnd());
            is.setMattrsetfinal(this.attr);
            is.setPostPane(this.postPane);
            is.insert();
        } else if (e.getActionCommand().equals("quot")) {
            InsertString is = new InsertString();
            is.setDoc(this.doc);
            is.setBeforeString(">>");
            is.setSelectionStart(this.postPane.getSelectionStart());
            is.setAfterString("<<");
            is.setSelectionEnd(this.postPane.getSelectionEnd());
            is.setMattrsetfinal(this.attr);
            is.setPostPane(this.postPane);
            is.insert();
        } else if (e.getActionCommand().equals("foot")) {
            InsertString is = new InsertString();
            is.setDoc(this.doc);
            is.setBeforeString("((");
            is.setSelectionStart(this.postPane.getSelectionStart());
            is.setAfterString("))");
            is.setSelectionEnd(this.postPane.getSelectionEnd());
            is.setMattrsetfinal(this.attr);
            is.setPostPane(this.postPane);
            is.insert();
        } else if (e.getActionCommand().equals("Undo")) {
            if (this.undoManager.canUndo()) {
                this.undoManager.undo();
            }
        } else if (e.getActionCommand().equals("Redo")) {
            if (this.undoManager.canRedo()) {
                this.undoManager.redo();
            }
        }
    }
}
