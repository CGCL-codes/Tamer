package editorgui;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import editorexceptions.*;
import editorutilities.*;

public class EditorToolBar extends JToolBar implements ActionListener {

    static JButton b[] = new JButton[12];

    public static ImageIcon img[] = new ImageIcon[12];

    String label[] = { "New", "Open", "Save", "Cut", "Copy", "Paste", "Undo", "Redo", "Compile", "Run", "Run Applet", "Print" };

    public EditorToolBar() {
        img[0] = new ImageIcon("resources/new.png");
        img[1] = new ImageIcon("resources/open.png");
        img[2] = new ImageIcon("resources/save.png");
        img[3] = new ImageIcon("resources/cut.png");
        img[4] = new ImageIcon("resources/copy.png");
        img[5] = new ImageIcon("resources/paste.png");
        img[6] = new ImageIcon("resources/undo.png");
        img[7] = new ImageIcon("resources/redo.png");
        img[8] = new ImageIcon("resources/duke_compiler.gif");
        img[9] = new ImageIcon("resources/duke_running.gif");
        img[10] = new ImageIcon("resources/duke_applet.gif");
        img[11] = new ImageIcon();
        for (int i = 0; i < 11; i++) {
            b[i] = new JButton();
            b[i].setIcon(img[i]);
            b[i].setToolTipText(label[i]);
            b[i].addActionListener(this);
            add(b[i]);
        }
    }

    public static JButton getUndoButton() {
        return b[6];
    }

    public static JButton getRedoButton() {
        return b[7];
    }

    public static void setEnabledUndoButton(boolean e) {
        b[6].setEnabled(e);
    }

    public static void addUndoAction(Action a) {
        b[6].addActionListener(a);
    }

    public static void setEnabledRedoButton(boolean e) {
        b[7].setEnabled(e);
    }

    public static void addRedoAction(Action a) {
        b[7].addActionListener(a);
    }

    public void actionPerformed(ActionEvent ae) {
        Object button = ae.getSource();
        EditorSourceFile currentSourceFile = (EditorSourceFile) (EditorFunctions.documentList.getSelectedComponent());
        if (button == b[0]) {
            try {
                EditorFunctions.newFile();
            } catch (FullDocumentListException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "JJ Java Editor", JOptionPane.ERROR_MESSAGE);
            }
        } else if (button == b[1]) {
            new EditorFunctions.OpenFileAction().run();
        } else if (button == b[2]) {
            new EditorFunctions.SaveFileAction().run();
        } else if (button == b[3]) {
            try {
                currentSourceFile.cut();
                currentSourceFile.grabFocus();
            } catch (NullPointerException e) {
            }
        } else if (button == b[4]) {
            try {
                currentSourceFile.copy();
                currentSourceFile.grabFocus();
            } catch (NullPointerException e) {
            }
        } else if (button == b[5]) {
            try {
                currentSourceFile.paste();
                currentSourceFile.grabFocus();
            } catch (NullPointerException e) {
            }
        } else if (button == b[6]) {
            try {
                currentSourceFile.undo();
                currentSourceFile.grabFocus();
            } catch (NullPointerException e) {
            }
        } else if (button == b[7]) {
            try {
                currentSourceFile.redo();
                currentSourceFile.grabFocus();
            } catch (NullPointerException e) {
            }
        } else if (button == b[8]) {
            new EditorFunctions.CompileFileAction().start();
        } else if (button == b[9]) {
            new EditorFunctions.RunFileAction().start();
        } else if (button == b[10]) {
            new EditorFunctions.RunAppletAction().start();
        }
    }
}
