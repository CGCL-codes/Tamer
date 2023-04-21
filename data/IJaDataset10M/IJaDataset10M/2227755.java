package gui.outputpanel;

import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;

public class RightMouseMenu extends PopupMenu {

    private static Logger log = Logger.getLogger(RightMouseMenu.class.getName());

    private JTextComponent jTextComponent;

    private final JTextField buffer = new JTextField();

    private String cut;

    private String copy;

    private String paste;

    private String delete;

    private String selectAll;

    private String selectAllCopy;

    private String selectAllCopySave;

    public RightMouseMenu(JTextComponent jTextComponent) {
        super();
        setFont(new java.awt.Font("Tahoma", 0, 11));
        this.jTextComponent = jTextComponent;
        this.cut = "Cut";
        this.copy = "Copy";
        this.paste = "Paste";
        this.delete = "Clear";
        this.selectAll = "Select All";
        this.selectAllCopy = "Select All & Copy";
        this.selectAllCopySave = "Select All & Copy & Save";
        jTextComponent.add(this);
        MyListner myListner = new MyListner();
        jTextComponent.addMouseListener(myListner);
        addActionListener(myListner);
    }

    private void resetItem() {
        removeAll();
        boolean isTestSel = jTextComponent.getSelectedText() != null;
        boolean isEditable = jTextComponent.isEditable();
        addMenuItem(selectAll, jTextComponent.isEnabled());
        addMenuItem(selectAllCopy, jTextComponent.isEnabled());
        addMenuItem(selectAllCopySave, jTextComponent.isEnabled());
        addSeparator();
        addMenuItem(cut, isTestSel && isEditable);
        addMenuItem(copy, isTestSel);
        addMenuItem(paste, isEditable);
        addSeparator();
        addMenuItem(delete, isEditable);
    }

    private void addMenuItem(String label, boolean isEnabled) {
        MenuItem menuItem = new MenuItem(label);
        menuItem.setEnabled(isEnabled);
        add(menuItem);
    }

    private void copy() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        TransferHandler transferHandler = jTextComponent.getTransferHandler();
        transferHandler.exportToClipboard(jTextComponent, clipboard, TransferHandler.COPY);
    }

    private void paste() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        TransferHandler transferHandler = jTextComponent.getTransferHandler();
        transferHandler.importData(jTextComponent, clipboard.getContents(null));
    }

    private class MyListner extends MouseAdapter implements ActionListener {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == 3) {
                resetItem();
                Point point = jTextComponent.getMousePosition();
                if (point != null) {
                    show(jTextComponent, point.x, point.y);
                }
            }
        }

        public void actionPerformed(ActionEvent e) {
            String source = e.getActionCommand();
            if (source.equals(copy)) {
                copy();
            } else if (source.equals(paste)) {
                paste();
            } else if (source.equals(cut)) {
                copy();
                jTextComponent.replaceSelection("");
            } else if (source.equals(delete)) {
                jTextComponent.selectAll();
                jTextComponent.replaceSelection("");
            } else if (source.equals(selectAll)) {
                jTextComponent.selectAll();
            } else if (source.equals(selectAllCopy)) {
                jTextComponent.selectAll();
                copy();
                jTextComponent.select(jTextComponent.getText().length(), jTextComponent.getText().length());
            } else if (source.equals(selectAllCopySave)) {
                jTextComponent.selectAll();
                copy();
                saveClipboard();
                jTextComponent.select(jTextComponent.getText().length(), jTextComponent.getText().length());
            }
        }

        private void saveClipboard() {
            try {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                TransferHandler transferHandler = jTextComponent.getTransferHandler();
                buffer.setText("");
                transferHandler.importData(buffer, clipboard.getContents(null));
                JFileChooser fc = new JFileChooser();
                File selFile = null;
                fc.showSaveDialog(null);
                selFile = fc.getSelectedFile();
                Writer output = null;
                if (selFile != null) {
                    output = new BufferedWriter(new FileWriter(selFile));
                    output.write(buffer.getText());
                    output.close();
                }
            } catch (IOException ex) {
                log.error(ex.getMessage());
                JOptionPane.showMessageDialog(null, "Some error occure on saving clipboard. [" + ex.getMessage() + "]", "Error message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
