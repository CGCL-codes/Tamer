package jmax.editors.qlist;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.AWTEvent.*;
import java.io.*;
import javax.swing.*;
import jmax.commons.*;
import jmax.fts.*;
import jmax.mda.*;
import jmax.ui.*;
import jmax.dialogs.*;

/** 
 * The qlist editor. It uses a QListPanel to show its data and
 * a couple of buttons (set, get) to synchronize with FTS.
 */
public class QList extends MaxEditorPanel implements ClipboardOwner {

    QListPanel itsQListPanel;

    FtsAtomList itsDataObject;

    QListFindDialog qListFindDialog;

    Dimension preferred = new Dimension(512, 412);

    /**
   * Constructor with a FtsAtomList. It prepares the graphic components
   * of the window.*/
    public QList(FtsAtomList theDataObject, QListDataEditor editor) {
        super(editor);
        itsDataObject = theDataObject;
        setLayout(new BorderLayout());
        setBackground(Color.white);
        itsQListPanel = new QListPanel();
        itsQListPanel.fillContent(itsDataObject);
        add(BorderLayout.CENTER, itsQListPanel);
        JPanel aPanel = new JPanel();
        aPanel.setLayout(new GridLayout(1, 2));
        JButton setButton = new JButton("Set");
        setButton.addActionListener(new MaxAction() {

            public void doAction() {
                itsDataObject.setValuesAsText(itsQListPanel.getText());
                itsQListPanel.fillContent(itsDataObject);
            }
        });
        JButton getButton = new JButton("Get");
        getButton.addActionListener(new MaxAction() {

            public void doAction() {
                itsDataObject.forceUpdate();
                itsQListPanel.fillContent(itsDataObject);
            }
        });
        aPanel.add(setButton);
        aPanel.add(getButton);
        aPanel.validate();
        add(BorderLayout.NORTH, aPanel);
        qListFindDialog = new QListFindDialog(this, itsQListPanel);
        validate();
        setBounds(100, 100, getPreferredSize().width, getPreferredSize().height);
        setVisible(true);
    }

    String getTitle() {
        return "Qlist";
    }

    protected void Find() {
        qListFindDialog.open();
    }

    protected void FindAgain() {
        qListFindDialog.find();
    }

    private boolean editMenuCustomized = false;

    private boolean fileMenuCustomized = false;

    public JMenu getMenuByName(String name) {
        if (name.equals("Edit")) {
            JMenu menu = super.getMenuByName(name);
            if (!editMenuCustomized) {
                editMenu.remove(getUndoMenu());
                editMenu.remove(getRedoMenu());
                editMenu.addSeparator();
                JMenuItem item = new JMenuItem("Find");
                editMenu.add(item);
                item.addActionListener(new MaxAction() {

                    public void doAction() {
                        Find();
                    }
                });
                item = new JMenuItem("Find again");
                editMenu.add(item);
                item.addActionListener(new MaxAction() {

                    public void doAction() {
                        FindAgain();
                    }
                });
            }
            editMenuCustomized = true;
            return menu;
        } else if (name.equals("File")) {
            JMenu menu = super.getMenuByName(name);
            if (!fileMenuCustomized) {
                fileMenu.remove(getSaveMenu());
                fileMenu.remove(getSaveAsMenu());
                fileMenu.remove(getSaveToMenu());
                fileMenu.addSeparator();
                JMenuItem importItem = new JMenuItem("Import");
                importItem.addActionListener(new MaxAction() {

                    public void doAction() {
                        Import();
                    }
                });
                fileMenu.add(importItem);
                JMenuItem exportItem = new JMenuItem("Export");
                exportItem.addActionListener(new MaxAction() {

                    public void doAction() {
                        Export();
                    }
                });
                fileMenu.add(exportItem);
            }
            fileMenuCustomized = true;
            return menu;
        } else return super.getMenuByName(name);
    }

    public Dimension getPreferredSize() {
        return preferred;
    }

    public Dimension getMinimumSize() {
        return preferred;
    }

    public void SaveAs() {
    }

    void Import() {
        File file = MaxFileChooser.chooseFileToOpen(getEditor(), "Import");
        if (file != null) {
            try {
                StringBuilder buf = new StringBuilder();
                BufferedReader in = new BufferedReader(new FileReader(file));
                while (in.ready()) {
                    buf.append(in.readLine());
                    buf.append("\n");
                }
                itsQListPanel.itsTextArea.setText(buf.toString());
                itsDataObject.setValuesAsText(itsQListPanel.itsTextArea.getText());
            } catch (java.io.IOException e) {
                getEditor().getUIContext().error(getEditor(), "Open", "Cannot open qlist: " + e);
            }
        }
    }

    public void SaveTo() {
    }

    public void Export() {
        File file;
        Writer w;
        file = MaxFileChooser.chooseFileToSave(getEditor(), new File("qlist.txt"), "Export");
        if (file == null) return;
        try {
            w = new FileWriter(file);
            w.write(itsQListPanel.itsTextArea.getText());
            w.close();
        } catch (java.io.IOException e) {
            getEditor().getUIContext().error(getEditor(), "Export", "Cannot save qlist: " + e);
        }
    }

    public void Print() {
    }

    protected void Cut() {
        if (itsQListPanel.itsTextArea.getSelectedText() != null) {
            Copy();
            String s = itsQListPanel.itsTextArea.getText();
            itsQListPanel.itsTextArea.setText(s.substring(0, itsQListPanel.itsTextArea.getSelectionStart()) + s.substring(itsQListPanel.itsTextArea.getSelectionEnd(), s.length()));
        }
    }

    protected void Copy() {
        if (itsQListPanel.itsTextArea.getSelectedText() != null) {
            String toCopy = itsQListPanel.itsTextArea.getSelectedText();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(toCopy), this);
        }
    }

    protected void Paste() {
        Transferable clipboardContent = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
        if (clipboardContent.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String toPaste = (String) clipboardContent.getTransferData(DataFlavor.stringFlavor);
                itsQListPanel.itsTextArea.insert(toPaste, itsQListPanel.itsTextArea.getCaretPosition());
                itsQListPanel.itsTextArea.requestFocusInWindow();
            } catch (Exception e) {
                System.err.println("error in paste: " + e);
            }
        }
    }

    public void lostOwnership(Clipboard c, Transferable t) {
    }
}
