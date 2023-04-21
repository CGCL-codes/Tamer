package tags;

import java.io.*;
import java.lang.*;
import java.lang.System.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.browser.VFSBrowser;
import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.util.Log;
import org.gjt.sp.jedit.gui.KeyEventWorkaround;

class TagsOptionsPanel extends AbstractOptionPane {

    /***************************************************************************/
    protected JCheckBox openDialogsUnderCursorCheckBox_;

    protected JCheckBox extendThroughMemberOpCheckBox_;

    protected JCheckBox searchInParentDirs_;

    protected JCheckBox useCurrentBufTagFileCheckBox_;

    protected JLabel curBufFileNameLabel_;

    protected JTextField curBufFileNameField_;

    protected JCheckBox searchAllFilesCheckBox_;

    protected JScrollPane pane_;

    protected JTable table_;

    protected TagIndexFileTableModel tableModel_;

    protected Vector tagIndexFilesInfo_;

    protected JButton addButton_;

    protected JButton removeButton_;

    protected JButton moveUpButton_;

    protected JButton moveDownButton_;

    protected JCheckBox debugCheckBox_;

    /***************************************************************************/
    public TagsOptionsPanel() {
        super("tags");
    }

    /***************************************************************************/
    public void _init() {
        Log.log(Log.DEBUG, this, "Current tag index files (init):");
        int numFiles = Tags.tagFiles_.size();
        TagFile tf = null;
        boolean foundCurBufIndexFile = false;
        tagIndexFilesInfo_ = new Vector(5);
        for (int i = 0; i < numFiles; i++) {
            tf = (TagFile) Tags.tagFiles_.elementAt(i);
            foundCurBufIndexFile = foundCurBufIndexFile || tf.currentDirIndexFile_;
            if (tf != null) {
                Log.log(Log.DEBUG, this, tf.toDebugString());
                tagIndexFilesInfo_.add(tf);
            }
        }
        setup();
        if (!foundCurBufIndexFile && jEdit.getBooleanProperty("options.tags.tag-search-current-buff-tag-file")) {
            useCurBufTagFileListener_.actionPerformed(null);
        }
        tf = null;
    }

    /***************************************************************************/
    public void _save() {
        Tags.setSearchAllTagFiles(searchAllFilesCheckBox_.isSelected());
        Tags.setUseCurrentBufTagFile(useCurrentBufTagFileCheckBox_.isSelected());
        jEdit.setProperty("options.tags.current-buffer-file-name", curBufFileNameField_.getText());
        TagsPlugin.debug_ = debugCheckBox_.isSelected();
        jEdit.setBooleanProperty("options.tags.open-dialogs-under-cursor", openDialogsUnderCursorCheckBox_.isSelected());
        jEdit.setBooleanProperty("options.tags.tag-extends-through-dot", extendThroughMemberOpCheckBox_.isSelected());
        Log.log(Log.DEBUG, this, "Search in parent dir:  " + searchInParentDirs_.isSelected());
        jEdit.setBooleanProperty("options.tags.tags-search-parent-dir", searchInParentDirs_.isSelected());
        Log.log(Log.DEBUG, this, "After setting:  " + jEdit.getBooleanProperty("options.tags.tags-search-parent-dir"));
        Log.log(Log.DEBUG, this, "Tag index files (from pane):");
        Tags.clearTagFiles();
        int numTagFiles = tagIndexFilesInfo_.size();
        TagFile tf = null;
        for (int i = 0; i < numTagFiles; i++) {
            tf = (TagFile) tagIndexFilesInfo_.get(i);
            if (tf.path_ == null || (tf.path_.length() == 0 && tf.currentDirIndexFile_)) {
                Tags.setUseCurrentBufTagFile(false);
            } else if (tf != null) {
                Log.log(Log.DEBUG, this, tf.toDebugString());
                Tags.tagFiles_.add(tf);
            }
        }
        tf = null;
    }

    /***************************************************************************/
    protected void setup() {
        openDialogsUnderCursorCheckBox_ = new JCheckBox(jEdit.getProperty("options.tags.open-dialogs-under-cursor.label"));
        openDialogsUnderCursorCheckBox_.setSelected(jEdit.getBooleanProperty("options.tags.open-dialogs-under-cursor"));
        addComponent(openDialogsUnderCursorCheckBox_);
        extendThroughMemberOpCheckBox_ = new JCheckBox(jEdit.getProperty("options.tags.tag-extends-through-dot.label"));
        extendThroughMemberOpCheckBox_.setSelected(jEdit.getBooleanProperty("options.tags.tag-extends-through-dot"));
        addComponent(extendThroughMemberOpCheckBox_);
        searchAllFilesCheckBox_ = new JCheckBox(jEdit.getProperty("options.tags.tag-search-all-files.label"));
        searchAllFilesCheckBox_.setSelected(Tags.getSearchAllTagFiles());
        addComponent(searchAllFilesCheckBox_);
        useCurrentBufTagFileCheckBox_ = new JCheckBox(jEdit.getProperty("options.tags.tag-search-current-buff-tag-file.label"));
        useCurrentBufTagFileCheckBox_.setSelected(Tags.getUseCurrentBufTagFile());
        useCurrentBufTagFileCheckBox_.addActionListener(useCurBufTagFileListener_);
        addComponent(useCurrentBufTagFileCheckBox_);
        curBufFileNameLabel_ = new JLabel(jEdit.getProperty("options.tags.current-buffer-file-name.label"));
        curBufFileNameField_ = new JTextField(jEdit.getProperty("options.tags.current-buffer-file-name"), 20);
        curBufFileNameField_.getDocument().addDocumentListener(curBufFilenameListener_);
        curBufFileNameLabel_.setLabelFor(curBufFileNameField_);
        JPanel p = new JPanel(new BorderLayout(5, 0));
        p.add(curBufFileNameLabel_, BorderLayout.WEST);
        p.add(curBufFileNameField_, BorderLayout.CENTER);
        addComponent(p);
        searchInParentDirs_ = new JCheckBox(jEdit.getProperty("options.tags.tags-search-parent-dirs.label"));
        searchInParentDirs_.setSelected(Tags.getSearchInParentDirs());
        addComponent(searchInParentDirs_);
        p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        tableModel_ = new TagIndexFileTableModel();
        table_ = new JTable(tableModel_);
        pane_ = new JScrollPane(table_, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        table_.getSelectionModel().addListSelectionListener(listSelectionListener_);
        table_.setPreferredScrollableViewportSize(new Dimension(250, 110));
        p.add(pane_, BorderLayout.CENTER);
        JPanel bp = new JPanel(new GridLayout(1, 0, 5, 5));
        addButton_ = new JButton(jEdit.getProperty("options.tags.tag-search-files-add.label"));
        addButton_.addActionListener(addButtonListener_);
        addButton_.setMnemonic(KeyEvent.VK_A);
        bp.add(addButton_);
        removeButton_ = new JButton(jEdit.getProperty("options.tags.tag-search-files-remove.label"));
        removeButton_.addActionListener(removeButtonListener_);
        removeButton_.setMnemonic(KeyEvent.VK_R);
        bp.add(removeButton_);
        moveUpButton_ = new JButton(jEdit.getProperty("options.tags.tag-search-files-move-up.label"));
        moveUpButton_.addActionListener(moveUpButtonListener_);
        moveUpButton_.setMnemonic(KeyEvent.VK_U);
        bp.add(moveUpButton_);
        moveDownButton_ = new JButton(jEdit.getProperty("options.tags.tag-search-files-move-down.label"));
        moveDownButton_.addActionListener(moveDownButtonListener_);
        moveDownButton_.setMnemonic(KeyEvent.VK_D);
        bp.add(moveDownButton_);
        p.add(bp, BorderLayout.SOUTH);
        addComponent(p);
        debugCheckBox_ = new JCheckBox("Debug");
        debugCheckBox_.setSelected(TagsPlugin.debug_);
        updateGUI();
        p = bp = null;
    }

    /***************************************************************************/
    protected ActionListener addButtonListener_ = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            int selectedRow = table_.getSelectedRow();
            if (selectedRow == -1) selectedRow = 0;
            String newTagFiles[] = null;
            newTagFiles = GUIUtilities.showVFSFileDialog(null, null, VFSBrowser.OPEN_DIALOG, false);
            if (newTagFiles != null) {
                for (int i = 0; i < newTagFiles.length; i++) {
                    tagIndexFilesInfo_.add(selectedRow, new TagFile(newTagFiles[i], TagFile.DEFAULT_CATAGORY));
                }
                tableModel_.fireTableRowsInserted(selectedRow, selectedRow + newTagFiles.length);
                table_.getSelectionModel().clearSelection();
                table_.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
            }
            updateGUI();
        }
    };

    /***************************************************************************/
    protected ActionListener removeButtonListener_ = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            int selectedRows[] = table_.getSelectedRows();
            TagFile tf = null;
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                tf = (TagFile) tagIndexFilesInfo_.get(i);
                if (tf.currentDirIndexFile_) {
                    useCurrentBufTagFileCheckBox_.setSelected(false);
                }
                tagIndexFilesInfo_.removeElementAt(selectedRows[i]);
            }
            tf = null;
            if (selectedRows.length != 0) tableModel_.fireTableRowsDeleted(selectedRows[0], selectedRows[selectedRows.length - 1]);
            int newSize = tagIndexFilesInfo_.size();
            if (newSize != 0) {
                int index = selectedRows[0];
                if (index > (newSize - 1)) index = newSize - 1;
                table_.getSelectionModel().clearSelection();
                table_.getSelectionModel().setSelectionInterval(index, index);
            }
            updateGUI();
        }
    };

    /***************************************************************************/
    protected ActionListener moveUpButtonListener_ = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            moveItem(-1);
            updateGUI();
        }
    };

    /***************************************************************************/
    protected ActionListener moveDownButtonListener_ = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            moveItem(+1);
            updateGUI();
        }
    };

    /***************************************************************************/
    protected void moveItem(int indexDir) {
        int selectedRow = table_.getSelectedRow();
        int newIndex = 0;
        Object element = tagIndexFilesInfo_.get(selectedRow);
        newIndex = selectedRow + indexDir;
        tagIndexFilesInfo_.removeElementAt(selectedRow);
        tagIndexFilesInfo_.add(newIndex, element);
        if (indexDir > 0) tableModel_.fireTableRowsUpdated(selectedRow, selectedRow + indexDir); else tableModel_.fireTableRowsUpdated(selectedRow + indexDir, selectedRow);
        table_.getSelectionModel().clearSelection();
        table_.getSelectionModel().setSelectionInterval(newIndex, newIndex);
    }

    /***************************************************************************/
    protected ListSelectionListener listSelectionListener_ = new ListSelectionListener() {

        public void valueChanged(ListSelectionEvent e) {
            updateGUI();
        }
    };

    /***************************************************************************/
    protected ActionListener useCurBufTagFileListener_ = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            updateGUI();
            if (useCurrentBufTagFileCheckBox_.isSelected()) {
                String path = curBufFileNameField_.getText();
                path = path.trim();
                TagFile tf = new TagFile(path, TagFile.DEFAULT_CATAGORY);
                tf.currentDirIndexFile_ = true;
                tagIndexFilesInfo_.add(0, tf);
                tableModel_.fireTableRowsInserted(0, 0);
                table_.getSelectionModel().clearSelection();
                tf = null;
            } else {
                int numTagIndexFiles = tagIndexFilesInfo_.size();
                TagFile tf = null;
                for (int i = 0; i < numTagIndexFiles; i++) {
                    tf = (TagFile) tagIndexFilesInfo_.get(i);
                    if (tf.currentDirIndexFile_) {
                        tagIndexFilesInfo_.removeElementAt(i);
                        tableModel_.fireTableRowsDeleted(i, i);
                        break;
                    }
                }
            }
        }
    };

    /***************************************************************************/
    protected DocumentListener curBufFilenameListener_ = new DocumentListener() {

        public void changedUpdate(DocumentEvent e) {
            update(e);
        }

        public void insertUpdate(DocumentEvent e) {
            update(e);
        }

        public void removeUpdate(DocumentEvent e) {
            update(e);
        }

        public void update(DocumentEvent e) {
            String name = curBufFileNameField_.getText();
            name = name.trim();
            int numTagIndexFiles = tagIndexFilesInfo_.size();
            TagFile tf = null;
            for (int i = 0; i < numTagIndexFiles; i++) {
                tf = (TagFile) tagIndexFilesInfo_.get(i);
                if (tf.currentDirIndexFile_) {
                    Log.log(Log.DEBUG, this, tf.path_ + " > " + name);
                    TagFile newTagFile = new TagFile(name, TagFile.DEFAULT_CATAGORY);
                    newTagFile.currentDirIndexFile_ = true;
                    tagIndexFilesInfo_.removeElementAt(i);
                    tagIndexFilesInfo_.add(i, newTagFile);
                    tableModel_.fireTableRowsUpdated(i, i);
                    newTagFile = null;
                }
            }
            name = null;
            tf = null;
        }
    };

    /***************************************************************************/
    protected void updateGUI() {
        int selectedRows[] = table_.getSelectedRows();
        int selectionCount = (selectedRows != null) ? selectedRows.length : 0;
        int numItems = tagIndexFilesInfo_.size();
        removeButton_.setEnabled(selectionCount != 0 && numItems != 0);
        moveUpButton_.setEnabled(selectionCount == 1 && selectedRows[0] != 0 && numItems != 0);
        moveDownButton_.setEnabled(selectionCount == 1 && selectedRows[0] != (numItems - 1) && numItems != 0);
        boolean selected = useCurrentBufTagFileCheckBox_.isSelected();
        curBufFileNameLabel_.setEnabled(selected);
        curBufFileNameField_.setEnabled(selected);
    }

    /***************************************************************************/
    class TagIndexFileTableModel extends AbstractTableModel {

        /*************************************************************************/
        public int getColumnCount() {
            return 2;
        }

        ;

        /*************************************************************************/
        public int getRowCount() {
            return tagIndexFilesInfo_.size();
        }

        /*************************************************************************/
        public String getColumnName(int col) {
            if (col == 0) return jEdit.getProperty("options.tags.tag-index-file-path-column.label"); else return jEdit.getProperty("options.tags.tag-index-file-enabled-column.label");
        }

        /*************************************************************************/
        public Object getValueAt(int r, int c) {
            TagFile tf = (TagFile) tagIndexFilesInfo_.get(r);
            return (c == 0) ? (Object) tf.getPath() : (Object) new Boolean(tf.isEnabled());
        }

        /*************************************************************************/
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*************************************************************************/
        public boolean isCellEditable(int r, int c) {
            return (c == 1);
        }

        /*************************************************************************/
        public void setValueAt(Object v, int r, int c) {
            if (c == 1) {
                TagFile tf = (TagFile) tagIndexFilesInfo_.get(r);
                tf.setEnabled(((Boolean) v).booleanValue());
                tf = null;
            }
        }
    }
}
