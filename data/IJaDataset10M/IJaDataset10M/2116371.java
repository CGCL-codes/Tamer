package easycreate.easytype;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.io.*;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.JInternalFrame.*;

public class IFrame extends JInternalFrame {

    static int openFrameCount = 0;

    static final int yOffset = 30, xOffset = 30;

    public int count = 0;

    public JTextPane textPane = new JTextPane();

    StyledDocument styledDoc;

    AbstractDocument doc;

    public static final int MAX_CHARACTERS = 300;

    Color c = Color.black;

    int fnt_size = 20;

    String curr_font = "Ariel";

    String filename;

    AbstractDocument clipboard;

    int start, end, length, doc_length;

    Boolean b = false, i = false, u = false, l = false, ce = false, r = false;

    AttributeSet cut_attrs;

    public IFrame(int open) {
        super("Document" + (++open), true, true, true, true);
        setFrameIcon(new ImageIcon("shared_img//e.png"));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new Dimension(screenSize.width - 300, screenSize.height - 300));
        textPane.setCaretPosition(0);
        textPane.setMargin(new Insets(20, 20, 20, 20));
        styledDoc = textPane.getStyledDocument();
        if (styledDoc instanceof AbstractDocument) {
            doc = (AbstractDocument) styledDoc;
            doc.setDocumentFilter(new DocFilter(MAX_CHARACTERS));
        }
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        this.add(scrollPane);
        setLocation(xOffset * open, yOffset * open);
    }

    public void cut() {
        textPane.cut();
    }

    public void copy() {
        textPane.copy();
    }

    public void paste() {
        textPane.paste();
    }

    public void select() {
        textPane.selectAll();
    }

    public void edit(boolean bold, boolean italic, boolean underline, boolean left, boolean center, boolean right) {
        b = bold;
        i = italic;
        u = underline;
        l = left;
        ce = center;
        r = right;
        MutableAttributeSet attrs = textPane.getInputAttributes();
        MutableAttributeSet align = textPane.getInputAttributes();
        StyleConstants.setFontFamily(attrs, curr_font);
        StyleConstants.setFontSize(attrs, fnt_size);
        StyleConstants.setForeground(attrs, c);
        if (bold == true) {
            StyleConstants.setBold(attrs, true);
        } else if (bold == false) {
            StyleConstants.setBold(attrs, false);
        }
        if (italic == true) {
            StyleConstants.setItalic(attrs, true);
        } else if (italic == false) {
            StyleConstants.setItalic(attrs, false);
        }
        if (underline == true) {
            StyleConstants.setUnderline(attrs, true);
        } else if (underline == false) {
            StyleConstants.setUnderline(attrs, false);
        }
        if (left == true) {
            StyleConstants.setAlignment(align, 0);
        }
        if (center == true) {
            StyleConstants.setAlignment(align, 1);
        }
        if (right == true) {
            StyleConstants.setAlignment(align, 2);
        }
        textPane.setCharacterAttributes(attrs, false);
        textPane.setParagraphAttributes(align, false);
    }

    protected void colourPick() {
        String[] cols = new String[] { "Black", "Red", "Blue", "Yellow", "Green", "Pink", "Purple" };
        int choice = JOptionPane.showOptionDialog(null, "             Which coloured text would you like?", "Font Colour ", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, cols, cols[0]);
        if (choice == 0) c = Color.black; else if (choice == 1) c = Color.red; else if (choice == 2) c = Color.blue; else if (choice == 3) c = Color.yellow; else if (choice == 4) c = new Color(0, 128, 0); else if (choice == 5) c = new Color(255, 102, 255); else if (choice == 6) c = new Color(128, 0, 128);
    }

    protected void fntSize() {
        String[] size = new String[] { "Small", "Medium", "Large" };
        int choice = JOptionPane.showOptionDialog(null, "             What size writing would you like?", "Font Size", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, size, size[0]);
        if (choice == 0) {
            fnt_size = 20;
        } else if (choice == 1) {
            fnt_size = 30;
        } else if (choice == 2) {
            fnt_size = 50;
        }
    }

    protected void fnt() {
        String[] fnts = new String[] { "Normal", "Curly" };
        int choice = JOptionPane.showOptionDialog(null, "           Which style of writing would you like?", "Font Colour", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, fnts, fnts[0]);
        if (choice == 0) curr_font = "Ariel"; else if (choice == 1) curr_font = "French Script MT";
    }

    public AttributeSet getAttrs() {
        return textPane.getCharacterAttributes();
    }

    protected Boolean[] keepCurrentAttributes() {
        Boolean[] curr_attrs = new Boolean[] { b, i, u, l, ce, r };
        MutableAttributeSet attrs = textPane.getInputAttributes();
        MutableAttributeSet align = textPane.getInputAttributes();
        textPane.setCharacterAttributes(attrs, false);
        textPane.setParagraphAttributes(align, false);
        return curr_attrs;
    }

    protected Boolean[] getSelected() {
        MutableAttributeSet attrs = textPane.getInputAttributes();
        MutableAttributeSet align = textPane.getInputAttributes();
        Boolean bold = StyleConstants.isBold(attrs);
        Boolean italic = StyleConstants.isItalic(attrs);
        Boolean underline = StyleConstants.isUnderline(attrs);
        curr_font = StyleConstants.getFontFamily(attrs);
        fnt_size = StyleConstants.getFontSize(attrs);
        c = StyleConstants.getForeground(attrs);
        int algn = StyleConstants.getAlignment(attrs);
        Boolean left = false, right = false, center = false;
        if (algn == 0) {
            left = true;
        } else if (algn == 1) {
            center = true;
        } else if (algn == 2) {
            right = true;
        }
        Boolean[] curr_attrs = new Boolean[] { bold, italic, underline, left, center, right };
        return curr_attrs;
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public AbstractDocument getDoc() {
        return doc;
    }

    public int save() {
        File di = new File("easycreate//easytype//Saved_Docs");
        String[] file_lis = di.list(filter);
        filename = this.getTitle();
        Boolean flag = false;
        for (int b = 0; b < file_lis.length; b++) {
            if (filename.equals(file_lis[b])) {
                flag = true;
            }
        }
        if (flag == false) {
            filename = JOptionPane.showInputDialog("What name would you like to call this letter?") + ".doc";
            if (filename.equals("null.doc")) {
                return 1;
            }
            File files = new File("easycreate//easytype//Saved_Docs");
            String[] file_list = files.list(filter);
            for (int i = 0; i < file_list.length; i++) {
                if (file_list[i].equals(filename)) {
                    JOptionPane.showMessageDialog(null, "Sorry this file already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    return 1;
                }
            }
            this.setTitle(filename);
            if (filename.length() > 50) {
                JOptionPane.showMessageDialog(null, "Sorry, what you wrote is not possible.\n" + "Please make sure you entered just numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        String dir = "easycreate//easytype//Saved_Docs";
        File f = new File(dir, filename);
        RTFEditorKit rtf = new RTFEditorKit();
        try {
            OutputStream out = new FileOutputStream(f);
            rtf.write(out, doc, 0, doc.getLength());
            out.close();
        } catch (Exception exSave) {
        }
        return 0;
    }

    public int open() {
        File dir = new File("easycreate//easytype//Saved_Docs");
        String[] file_list = dir.list(filter);
        if (file_list.length == 0) {
            JOptionPane.showMessageDialog(null, "Sorry, you currently don't have any saved files", "Error", JOptionPane.ERROR_MESSAGE);
            return 1;
        }
        int choice = JOptionPane.showOptionDialog(null, "           Which file would you like to open?", "Open File", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, file_list, file_list[0]);
        File f = new File(dir, file_list[choice]);
        this.setTitle(file_list[choice]);
        RTFEditorKit rtf = new RTFEditorKit();
        try {
            InputStream in = new FileInputStream(f);
            rtf.read(in, doc, 0);
            textPane.setDocument(doc);
            in.close();
        } catch (Exception exOpen) {
        }
        return 0;
    }

    public void openExt(String file) {
        File dir = new File("easycreate//easytype//Saved_Docs");
        File f = new File(dir, file);
        this.setTitle(file);
        RTFEditorKit rtf = new RTFEditorKit();
        try {
            InputStream in = new FileInputStream(f);
            rtf.read(in, doc, 0);
            textPane.setDocument(doc);
            in.close();
        } catch (Exception exOpen) {
        }
    }

    public int template() {
        File dir = new File("easycreate//easytype//Templates");
        String[] file_list = dir.list(filter);
        if (file_list.length == 0) {
            JOptionPane.showMessageDialog(null, "Sorry, there are no templates available", "Error", JOptionPane.ERROR_MESSAGE);
            return 1;
        }
        int choice = JOptionPane.showOptionDialog(null, "           Which template would you like to use?", "Open File", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, file_list, file_list[0]);
        File f = new File(dir, file_list[choice]);
        RTFEditorKit rtf = new RTFEditorKit();
        try {
            InputStream in = new FileInputStream(f);
            rtf.read(in, doc, 0);
            textPane.setDocument(doc);
            in.close();
        } catch (Exception exOpen) {
        }
        return 0;
    }

    FilenameFilter filter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return name.endsWith(".doc");
        }
    };
}
