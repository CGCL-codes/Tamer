package net.sourceforge.ftpowl.gui;

import java.io.File;
import java.util.Iterator;
import java.util.TreeSet;
import net.sourceforge.ftpowl.controller.EventManager;
import net.sourceforge.ftpowl.model.FileInfoViewData;
import net.sourceforge.ftpowl.model.FileTreeItemData;
import net.sourceforge.ftpowl.util.shop.FTPOwlMessageBoxShop;
import net.sourceforge.ftpowl.util.shop.FTPOwlPaintShop;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Displays informations of a file. The right side of the FileBrowser.
 * 
 * @author <a href="mailto:admiral_kay@users.sourceforge.net" title="Kay
 *         Patzwald">Kay Patzwald </a>
 *  
 */
public class FileInfoView extends Composite implements IRefreshable {

    private Menu contextMenu;

    protected FileBrowser fileBrowser;

    private Table fileTable;

    private File openDir;

    private File selectedFile;

    /**
	 * The Constructor of FileInfoView
	 * 
	 * @param parent
	 * @param style
	 */
    public FileInfoView(Composite parent, int style, FileBrowser browser) {
        super(parent, style);
        this.fileBrowser = browser;
        initComponents();
    }

    /**
	 * Add a context-menu to the fileTable
	 */
    private void addContextMenu() {
        contextMenu = new Menu(GUI.getShell(), SWT.POP_UP);
        MenuItem itemNew = new MenuItem(contextMenu, SWT.CASCADE);
        itemNew.setText("&Neu");
        Menu menuNew = new Menu(GUI.getShell(), SWT.DROP_DOWN);
        itemNew.setMenu(menuNew);
        MenuItem itemNewDir = new MenuItem(menuNew, SWT.PUSH);
        itemNewDir.setText("&Ordner");
        itemNewDir.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                e.data = getOpenDir();
                EventManager.getInstance().actionCreateNewDirectory(e);
            }
        });
        new MenuItem(contextMenu, SWT.SEPARATOR);
        MenuItem itemCut = new MenuItem(contextMenu, SWT.PUSH);
        itemCut.setText("&Ausschneiden");
        itemCut.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                e.data = getSelectedFiles();
                EventManager.getInstance().actionMoveFile(e);
            }
        });
        MenuItem itemCopy = new MenuItem(contextMenu, SWT.PUSH);
        itemCopy.setText("&Kopieren");
        itemCopy.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                e.data = getSelectedFiles();
                EventManager.getInstance().actionCopyFile(e);
            }
        });
        MenuItem itemPaste = new MenuItem(contextMenu, SWT.PUSH);
        itemPaste.setText("&Einfügen");
        itemPaste.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                e.data = getOpenDir();
                EventManager.getInstance().actionPasteFile(e);
            }
        });
        MenuItem itemRename = new MenuItem(contextMenu, SWT.PUSH);
        itemRename.setText("&Umbenennen");
        itemRename.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                e.data = getSelectedFile();
                EventManager.getInstance().actionRenameFile(e);
            }
        });
        MenuItem itemDelete = new MenuItem(contextMenu, SWT.PUSH);
        itemDelete.setText("&Löschen");
        itemDelete.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                e.data = getSelectedFiles();
                EventManager.getInstance().actionDeleteFile(e);
            }
        });
        new MenuItem(contextMenu, SWT.SEPARATOR);
        MenuItem itemRefresh = new MenuItem(contextMenu, SWT.PUSH);
        itemRefresh.setText("&Aktualisieren");
        itemRefresh.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                FileBrowser.refreshAllViews();
            }
        });
        fileTable.setMenu(contextMenu);
    }

    /**
	 * 
	 * TODO Comment missing
	 * @return
	 */
    public FileBrowser getFileBrowser() {
        return fileBrowser;
    }

    /**
	 * Getter of openFile
	 * 
	 * @return Returns the openFile.
	 */
    public File getOpenDir() {
        return openDir;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    /**
	 * Init all components
	 *  
	 */
    private void initComponents() {
        this.setLayout(new FillLayout());
        this.setBackground(GUI.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        if (File.listRoots()[0].toString().equals("A:\\") || File.listRoots()[0].toString().equals("B:\\")) {
            updateView(new File("C:\\"), fileBrowser);
        } else {
            updateView(File.listRoots()[0], fileBrowser);
        }
    }

    /**
	 * TODO Comment missing
	 * 
	 */
    public void refresh() {
        updateView(openDir, fileBrowser);
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    /**
	 * Update the view.
	 * 
	 * @param file The File that should be updated.
	 * @param browser The parent browser.
	 */
    public void updateView(File file, FileBrowser browser) {
        openDir = file;
        File[] files = file.listFiles();
        browser.setTitle(file.getAbsolutePath());
        if (files != null) {
            if (this.getChildren().length != 0) while (this.getChildren().length != 0) this.getChildren()[0].dispose();
            String[] titles = createNewTable();
            fillTable(file, browser, files);
            for (int i = 2; i < titles.length; i++) {
                fileTable.getColumn(i).pack();
            }
        } else {
            if (this.getChildren().length != 0) while (this.getChildren().length != 0) this.getChildren()[0].dispose();
            FTPOwlMessageBoxShop.getMessageBox(FTPOwlMessageBoxShop.ACCESSDENIED).open();
        }
        this.layout();
    }

    /**
	 * TODO Comment missing
	 * @param file
	 * @param browser
	 * @param files
	 */
    private void fillTable(File file, FileBrowser browser, File[] files) {
        TableItem item = null;
        if (file.getParent() != null) {
            item = new TableItem(fileTable, SWT.NONE);
            item.setImage(0, FTPOwlPaintShop.getImage(FTPOwlPaintShop.I_FOLDER_UP));
            item.setText(2, "Ordner");
            item.setText(1, "..");
            item.setText(3, "0");
            item.setData(new FileInfoViewData(null, browser));
        }
        TreeSet sortedFiles = new TreeSet();
        for (int i = 0; i < files.length; i++) {
            sortedFiles.add(files[i]);
        }
        Iterator it = sortedFiles.iterator();
        while (it.hasNext()) {
            File file2 = (File) it.next();
            if (file2.isDirectory()) {
                item = new TableItem(fileTable, SWT.NONE);
                item.setImage(0, FTPOwlPaintShop.getImage(FTPOwlPaintShop.I_FOLDER));
                item.setText(2, "Ordner");
                item.setText(1, file2.getName());
                item.setText(3, String.valueOf(file2.length()));
                item.setData(new FileInfoViewData(file2, browser));
            }
        }
        it = sortedFiles.iterator();
        while (it.hasNext()) {
            File file2 = (File) it.next();
            if (!file2.isDirectory()) {
                item = new TableItem(fileTable, SWT.NONE);
                item.setImage(0, FTPOwlPaintShop.getImage(FTPOwlPaintShop.I_FILE));
                item.setText(2, "Datei");
                item.setText(1, file2.getName());
                item.setText(3, String.valueOf(file2.length()));
                item.setData(new FileInfoViewData(file2, browser));
            }
        }
    }

    /**
	 * TODO Comment missing
	 * @return
	 */
    private String[] createNewTable() {
        fileTable = new Table(this, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.VIRTUAL);
        addContextMenu();
        fileTable.setLinesVisible(true);
        fileTable.setHeaderVisible(true);
        fileTable.addListener(SWT.MouseDoubleClick, new Listener() {

            public void handleEvent(Event e2) {
                EventManager.getInstance().actionFileDoubleClick(e2);
            }
        });
        fileTable.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                EventManager.getInstance().actionTableItemSelected(e);
            }
        });
        fileTable.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                event.data = fileBrowser;
                EventManager.getInstance().actionTableMouseDown(event);
            }
        });
        String[] titles = { "", "Name", "Dateityp", "Größe" };
        TableColumn col = new TableColumn(fileTable, SWT.NONE);
        col.setText(titles[0]);
        col.setWidth(20);
        col.setResizable(false);
        col.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                e.data = fileBrowser;
                EventManager.getInstance().actionTableColumnSelected(e);
            }
        });
        col = new TableColumn(fileTable, SWT.NONE);
        col.setText(titles[1]);
        col.setWidth(150);
        col.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                e.data = fileBrowser;
                EventManager.getInstance().actionTableColumnSelected(e);
            }
        });
        for (int i = 2; i < titles.length; i++) {
            col = new TableColumn(fileTable, SWT.NONE);
            col.setText(titles[i]);
            col.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent e) {
                    e.data = fileBrowser;
                    EventManager.getInstance().actionTableColumnSelected(e);
                }
            });
        }
        return titles;
    }

    /**
	 * 
	 * Update the view
	 * 
	 * @param e The SelectionEvent.
	 */
    public void updateView(SelectionEvent e) {
        updateView((TreeItem) e.item);
    }

    /**
	 * 
	 * Update the view
	 * 
	 * @param treeItem The TreeItem that should be updated.
	 */
    public void updateView(TreeItem treeItem) {
        FileTreeItemData data = (FileTreeItemData) treeItem.getData();
        data.getFileBrowser().getFileTree().handleFolderIcons(treeItem);
        updateView(data.getFile(), data.getFileBrowser());
    }

    /** Getter of fileTable
	 * @return Returns the fileTable.
	 */
    public Table getFileTable() {
        return fileTable;
    }

    protected File[] getSelectedFiles() {
        TableItem[] items = getFileTable().getSelection();
        File[] files = new File[items.length];
        for (int i = 0; i < items.length; i++) {
            FileInfoViewData data = (FileInfoViewData) items[i].getData();
            files[i] = data.getFile();
        }
        return files;
    }
}
