package org.deniv.jbiblex.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import org.deniv.jbiblex.domain.TitleInfo;
import org.deniv.jbiblex.explorer.LibExplorer;
import org.deniv.jbiblex.explorer.SearchException;
import org.deniv.jbiblex.explorer.TitleVisitor.TitleField;

public class SearchPanel extends JPanel implements HierarchyListener {

    private LibExplorer libExplorer;

    private JTextField searchText;

    private JButton searchBtn;

    private TitleInfoTableModel booksModel = new TitleInfoTableModel();

    private JTable booksTable;

    private JPopupMenu rowSelectionPopup;

    private JFileChooser fileChooser = new JFileChooser();

    private Action saveToAction;

    private MainWindow mainWindow;

    public SearchPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        JPanel searchCtrlPanel = new JPanel();
        add(searchCtrlPanel, BorderLayout.PAGE_START);
        booksTable = new JTable(booksModel);
        booksTable.setCellSelectionEnabled(false);
        booksTable.setColumnSelectionAllowed(false);
        booksTable.setRowSelectionAllowed(true);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(booksTable), BorderLayout.PAGE_END);
        searchCtrlPanel.add(searchText = new JTextField(70));
        searchText.setToolTipText(getQueryHelp());
        searchCtrlPanel.add(searchBtn = new JButton(">>"));
        searchBtn.addActionListener(new LibrarySearchListener());
        rowSelectionPopup = new JPopupMenu();
        saveToAction = new SaveToAction("Save to ...");
        rowSelectionPopup.add(saveToAction);
        mainWindow.addMenuAction(MainMenu.FILE_MENU_KEY, saveToAction);
        booksTable.add(rowSelectionPopup);
        booksTable.addMouseListener(new TableMouseListener());
        addHierarchyListener(this);
    }

    private String getQueryHelp() {
        StringBuilder helpText = new StringBuilder("<html><b>Query examples:</b><br/>");
        String searchSuffix = ":&lt;search string&gt;<br/>";
        for (TitleField field : TitleField.values()) {
            if (field.isIndexed()) {
                helpText.append(field.toString() + searchSuffix);
            }
        }
        helpText.append("</html>");
        return helpText.toString();
    }

    public void openLibrary(String libPathStr) {
        if (libPathStr != null) {
            final File libDirectory = new File(libPathStr);
            libExplorer = new LibExplorer(libDirectory);
            if (!libExplorer.isIndexed()) {
                JOptionPane.showMessageDialog(SearchPanel.this, "Can't find the library index, starting reindexing", "Index not found", JOptionPane.INFORMATION_MESSAGE);
                IndexingProgressDialog indexingDlg = new IndexingProgressDialog(mainWindow, libDirectory);
                indexingDlg.index();
            }
        } else {
            libExplorer = null;
        }
    }

    public void hierarchyChanged(HierarchyEvent e) {
        JRootPane rootPane = getRootPane();
        if (rootPane != null) {
            rootPane.setDefaultButton(searchBtn);
        }
    }

    private void saveFile() {
        File selectedFile = null;
        int bookIndex = booksTable.convertRowIndexToModel(booksTable.getSelectedRow());
        if (bookIndex < 0) {
            return;
        }
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int fileSelectionResult = fileChooser.showSaveDialog(SearchPanel.this);
        if (JFileChooser.APPROVE_OPTION == fileSelectionResult) {
            selectedFile = fileChooser.getSelectedFile();
            if (selectedFile.exists()) {
                JOptionPane.showMessageDialog(SearchPanel.this, "The file " + selectedFile.getName() + " already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            return;
        }
        TitleInfo bookTitle = booksModel.getRowTitle(bookIndex);
        try {
            libExplorer.extractToFile(bookTitle, selectedFile);
        } catch (SearchException e) {
            JOptionPane.showMessageDialog(SearchPanel.this, e.getMessage(), "Error extracting book", JOptionPane.ERROR_MESSAGE);
        }
    }

    private final class LibrarySearchListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            booksModel.clear();
            try {
                if (libExplorer == null) {
                    JOptionPane.showMessageDialog(SearchPanel.this, "Use File/Open Library first to open your library", "No library defined", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                ArrayList<TitleInfo> searchResult = libExplorer.searchTitles(searchText.getText());
                booksModel.addTitles(searchResult);
            } catch (SearchException e) {
                JOptionPane.showMessageDialog(SearchPanel.this, e.getMessage(), "Library Search Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private final class TableMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            processEvent(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            processEvent(e);
        }

        private void processEvent(MouseEvent e) {
            saveToAction.setEnabled((booksTable.getSelectedRow() >= 0));
            if (rowSelectionPopup.isPopupTrigger(e) && (booksTable.getSelectedRow() >= 0)) {
                rowSelectionPopup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private final class SaveToAction extends AbstractAction {

        public SaveToAction(String name) {
            super(name);
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            saveFile();
        }
    }
}
