package com.jiexplorer.gui.thumb;

import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileSystemView;
import sun.management.FileSystem;
import com.jiexplorer.JIExplorer;
import com.jiexplorer.db.JIThumbnailCache;
import com.jiexplorer.db.JIThumbnailService;
import com.jiexplorer.filetask.DeleteFileTask;
import com.jiexplorer.filetask.ProgressDialog;
import com.jiexplorer.gui.BatchRenameDialog;
import com.jiexplorer.gui.StatusBarPanel;
import com.jiexplorer.gui.cattree.JICatTreeFrame;
import com.jiexplorer.gui.dnd.ListTransferHandler;
import com.jiexplorer.gui.dnd.TransferActionListener;
import com.jiexplorer.gui.keyword.KeyWordsFrame;
import com.jiexplorer.gui.metadata.JIMetaDataFrame;
import com.jiexplorer.gui.preferences.JIPreferences;
import com.jiexplorer.gui.viewer.JIViewer;
import com.jiexplorer.util.DiskObject;
import com.jiexplorer.util.JIExplorerContext;
import com.jiexplorer.util.JIObservable;
import com.jiexplorer.util.JIObserver;
import com.jiexplorer.util.JIUtility;
import com.jiexplorer.util.OpenWith;
import com.jiexplorer.util.OrderedDiskObjectList;

public final class JIThumbnailList extends JList implements JIObservable {

    /**
	 *
	 */
    private static final long serialVersionUID = -6972344708370677092L;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JIThumbnailList.class);

    private int editingIndex = -1;

    private int lastClickIndex = -1;

    private long lastClickTime = -1;

    private JIListCellEditor listCellEditor;

    private final Frame frame;

    private final JScrollPane viewport;

    private final TransferActionListener actionListener;

    private final JMenuItem jMenuItemOpen;

    private final JMenuItem jMenuItemOpenWith;

    private final JMenuItem jMenuItemRefresh;

    private final JMenuItem jMenuItemRefreshIcon;

    private final JMenuItem jMenuItemRemoveFromDB;

    private final JMenuItem jMenuItemRemoveKeyWords;

    private final JMenuItem jMenuItemRemoveCategories;

    private final JMenuItem jMenuItemRefreshList;

    private final JMenuItem jMenuItemRename;

    private final JMenuItem jMenuItemBatchRename;

    private final JMenuItem jMenuItemDelete;

    private final JMenuItem jMenuItemKeyWords;

    private final JMenuItem jMenuItemCatagory;

    private final JMenuItem jMenuItemMetadata;

    private final JMenuItem jMenuItemCut;

    private final JMenuItem jMenuItemCopy;

    private final JMenuItem jMenuItemPaste;

    private final JPopupMenu jDesktopPopupMenu;

    private KeyWordsFrame keyWordsFrame;

    private JICatTreeFrame catagorysFrame;

    private final JIToggleSelectionModel selectionModel;

    private final Vector<JIObserver> obs;

    private boolean changed;

    private DiskObject lastSelectedDiskObject = null;

    public JIThumbnailList(final Frame jie, final JScrollPane viewport) {
        this(jie, viewport, JIPreferences.getInstance().getThumbnailScrollMode(), null);
    }

    public JIThumbnailList(final Frame jie, final JScrollPane viewport, final OrderedDiskObjectList dObjList) {
        this(jie, viewport, JIPreferences.getInstance().getThumbnailScrollMode(), dObjList);
    }

    public JIThumbnailList(final Frame jie, final JScrollPane viewport, final int scrollMode) {
        this(jie, viewport, scrollMode, null);
    }

    public JIThumbnailList(final Frame jie, final JScrollPane viewport, final int scrollMode, final OrderedDiskObjectList dObjList) {
        super();
        boolean useSelection = false;
        if (dObjList != null) {
            this.setModel(new JIListModel(this, dObjList));
            useSelection = true;
        } else {
            this.setModel(new JIListModel(this));
        }
        this.frame = jie;
        this.viewport = viewport;
        this.obs = new Vector<JIObserver>();
        this.changed = false;
        this.actionListener = new TransferActionListener();
        this.jMenuItemOpen = new JMenuItem();
        this.jMenuItemOpenWith = new JMenu();
        this.jMenuItemRefresh = new JMenu();
        this.jMenuItemRefreshIcon = new JMenuItem();
        this.jMenuItemRemoveFromDB = new JMenuItem();
        this.jMenuItemRemoveKeyWords = new JMenuItem();
        this.jMenuItemRemoveCategories = new JMenuItem();
        this.jMenuItemRefreshList = new JMenuItem();
        this.jMenuItemRename = new JMenuItem();
        this.jMenuItemBatchRename = new JMenuItem();
        this.jMenuItemDelete = new JMenuItem();
        this.jMenuItemKeyWords = new JMenuItem();
        this.jMenuItemCatagory = new JMenuItem();
        this.jMenuItemMetadata = new JMenuItem();
        this.jMenuItemCut = new JMenuItem();
        this.jMenuItemCopy = new JMenuItem();
        this.jMenuItemPaste = new JMenuItem();
        this.jDesktopPopupMenu = new JPopupMenu();
        this.selectionModel = new JIToggleSelectionModel();
        setSelectionModel(this.selectionModel);
        setDragEnabled(true);
        setTransferHandler(new ListTransferHandler());
        setCellRenderer(new JIListRenderer());
        setFixedCellHeight(JIPreferences.getInstance().getIconDim().height);
        setFixedCellWidth(JIPreferences.getInstance().getIconDim().width);
        setVisibleRowCount(-1);
        if (JIPreferences.getInstance().getImagesViewType().equals(JIPreferences.PREVIEW_VIEW)) {
            setLayoutOrientation(1);
        } else {
            setLayoutOrientation(JIPreferences.getInstance().getThumbnailScrollMode());
        }
        initActions();
        addKeyListener(new JIThumbnailKeyAdapter());
        if (useSelection) {
            JIExplorer.instance().getContext();
        }
        setVerifyInputWhenFocusTarget(false);
        JIThumbnailCache.getInstance().invalidate();
    }

    public final JIListModel getThumbnailListModel() {
        return (JIListModel) getModel();
    }

    public final void setFrame() {
        this.listCellEditor = new JIListCellEditor(this);
    }

    public final Frame getFrame() {
        return this.frame;
    }

    public final JViewport getViewPort() {
        return this.viewport.getViewport();
    }

    public final boolean isEditing() {
        if (this.editingIndex > -1) {
            return true;
        }
        return false;
    }

    @Override
    public final int getScrollableUnitIncrement(final Rectangle visibleRect, final int orientation, final int direction) {
        if (isEditing()) {
            editingCanceled();
        }
        int row;
        if ((orientation == SwingConstants.VERTICAL) && (direction < 0) && ((row = getFirstVisibleIndex()) != -1)) {
            final Rectangle r = getCellBounds(row, row);
            if ((r.y == visibleRect.y) && (row != 0)) {
                final Point loc = r.getLocation();
                loc.y--;
                final int prevIndex = locationToIndex(loc);
                final Rectangle prevR = getCellBounds(prevIndex, prevIndex);
                if ((prevR == null) || (prevR.y >= r.y)) {
                    return 0;
                }
                return prevR.height;
            }
        }
        return super.getScrollableUnitIncrement(visibleRect, orientation, direction);
    }

    @Override
    public final String getToolTipText(final MouseEvent evt) {
        if (!JIPreferences.getInstance().isThumbnailToolTips()) {
            return null;
        }
        final int index = locationToIndex(evt.getPoint());
        if (index < 0) {
            return "";
        }
        final Object item = getModel().getElementAt(index);
        if (((DiskObject) item).getName() == null) {
            return null;
        }
        return "<html>" + ((DiskObject) item).getName() + "<br>" + "Size: " + ((DiskObject) item).getSize() + "<br>" + ((((DiskObject) item).getDimension().width == -1) ? "" : "Dimension: " + ((DiskObject) item).getDim() + "<br>") + "Date: " + ((DiskObject) item).getShortDate() + "</html>";
    }

    public final void reset() {
        setFixedCellHeight(JIPreferences.getInstance().getIconDim().height);
        setFixedCellWidth(JIPreferences.getInstance().getIconDim().width);
        if (JIPreferences.getInstance().getImagesViewType().equals(JIPreferences.PREVIEW_VIEW)) {
            setLayoutOrientation(1);
        } else {
            setLayoutOrientation(JIPreferences.getInstance().getThumbnailScrollMode());
        }
        ((JIListModel) getModel()).reload();
        initOpenWith();
        setVisibleRowCount(-1);
        clearSelection();
        ensureIndexIsVisible(0);
    }

    public final void openSelection() {
        final int index = getSelectedIndex();
        final OrderedDiskObjectList imageList = ((JIListModel) getModel()).getDiskObjectList();
        imageList.setCurrentIndex(index);
        new JIViewer(imageList);
    }

    public final void nextPage(final KeyEvent e) {
        final int lastIndex = getLastVisibleIndex();
        if (getLayoutOrientation() != JList.HORIZONTAL_WRAP) {
            e.consume();
            final int firstIndex = getFirstVisibleIndex();
            final int visibleRows = getVisibleRowCount();
            final int visibleColums = (int) (((float) (lastIndex - firstIndex) / (float) visibleRows) + .5);
            final int visibleItems = visibleRows * visibleColums;
            final int val = (lastIndex + visibleItems >= getModel().getSize()) ? getModel().getSize() - 1 : lastIndex + visibleItems;
            clearSelection();
            setSelectedIndex(val);
            fireSelectionValueChanged(val, val, false);
        } else {
            clearSelection();
            setSelectedIndex(lastIndex);
            fireSelectionValueChanged(lastIndex, lastIndex, false);
        }
    }

    public final void lastPage(final KeyEvent e) {
        final int lastIndex = getLastVisibleIndex();
        if (getLayoutOrientation() != JList.HORIZONTAL_WRAP) {
            e.consume();
            final int firstIndex = getFirstVisibleIndex();
            final int visibleRows = getVisibleRowCount();
            final int visibleColums = (int) (((float) (lastIndex - firstIndex) / (float) visibleRows) + .5);
            final int visibleItems = visibleRows * visibleColums;
            final int val = ((firstIndex - 1) - visibleItems < 0) ? 0 : (firstIndex - 1) - visibleItems;
            clearSelection();
            setSelectedIndex(val);
            fireSelectionValueChanged(val, val, false);
        } else {
            clearSelection();
            setSelectedIndex(lastIndex);
            fireSelectionValueChanged(lastIndex, lastIndex, false);
        }
    }

    public final void jiThumbnail_keyPressed(final KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_PAGE_DOWN:
                nextPage(e);
                break;
            case KeyEvent.VK_PAGE_UP:
                lastPage(e);
                break;
            case KeyEvent.VK_ENTER:
                openSelection();
                e.consume();
                break;
        }
    }

    public final Rectangle getScreenLableBounds(final int i) {
        final Rectangle recb = getCellBounds(i, i);
        final JViewport jviewport = getViewPort();
        final Point p = SwingUtilities.convertPoint(this, recb.x, recb.y, jviewport);
        SwingUtilities.convertPointToScreen(p, jviewport);
        recb.x = p.x;
        recb.y = p.y + (recb.height - 22);
        recb.height = 22;
        return recb;
    }

    public final void editingStart() {
        this.editingIndex = getSelectedIndex();
        final String name = ((DiskObject) getSelectedValue()).getName();
        final Rectangle rec = getScreenLableBounds(getSelectedIndex());
        this.listCellEditor.edit(name, rec);
    }

    public final void editingStopped(final String newName) {
        if (this.editingIndex > -1) {
            final JIListModel model = getThumbnailListModel();
            final File dir = ((DiskObject) model.getElementAt(this.editingIndex)).getFile();
            if (!newName.trim().equals("")) {
                final File newDir = new File(dir.getParentFile(), newName);
                if (!newDir.exists()) {
                    dir.renameTo(newDir);
                    JIThumbnailService.getInstance().moveFile(dir.getPath(), newDir.getPath());
                    model.setElementAt(new DiskObject(newDir), this.editingIndex);
                    model.notifyAsUpdated(this.editingIndex);
                }
            }
        }
        this.editingIndex = -1;
    }

    public void editingCanceled() {
        this.editingIndex = -1;
        this.listCellEditor.setVisible(false);
    }

    private final void initOpenWith() {
        this.jMenuItemOpenWith.removeAll();
        this.jMenuItemOpenWith.setText("Open With");
        final Vector<OpenWith> openWithCmds = JIThumbnailService.getInstance().getOpenWith();
        for (final OpenWith openWith : openWithCmds) {
            final JMenuItem openItem = new JMenuItem();
            openItem.setText(openWith.getCommandName());
            openItem.setAction(new AbstractAction(openWith.getCommandName()) {

                /**
				 *
				 */
                private static final long serialVersionUID = -5408544415314922041L;

                public void actionPerformed(final ActionEvent e) {
                    final Thread runner = new Thread() {

                        @Override
                        public void run() {
                            Runnable runnable = new Runnable() {

                                public void run() {
                                    final Object[] list = JIThumbnailList.this.getSelectedValues();
                                    final String[] args = new String[list.length];
                                    int cnt = 0;
                                    for (Object obj : list) {
                                        args[cnt++] = ((DiskObject) obj).getPath();
                                    }
                                    openWith.run(args);
                                }
                            };
                            SwingUtilities.invokeLater(runnable);
                        }
                    };
                    runner.start();
                }
            });
            this.jMenuItemOpenWith.add(openItem);
        }
    }

    private final void initActions() {
        this.jMenuItemOpen.setText("Open");
        this.jMenuItemOpen.setAction(new AbstractAction("Open") {

            /**
			 *
			 */
            private static final long serialVersionUID = 7770033253414532608L;

            public void actionPerformed(final ActionEvent e) {
                final Thread runner = new Thread() {

                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                final int index = getSelectedIndex();
                                final OrderedDiskObjectList imageList = ((JIListModel) getModel()).getDiskObjectList();
                                imageList.setCurrentIndex(index);
                                new JIViewer(imageList);
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                };
                runner.start();
            }
        });
        initOpenWith();
        this.jMenuItemRefreshIcon.setText("Refresh Icon");
        this.jMenuItemRefreshIcon.setAction(new AbstractAction("Refresh Icon") {

            /**
			 *
			 */
            private static final long serialVersionUID = -7134072000008442328L;

            public void actionPerformed(final ActionEvent e) {
                final Thread runner = new Thread() {

                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                DiskObject[] dObjs = JIThumbnailList.this.getSelectedValues();
                                JIThumbnailService.getInstance().refreshThumbnails(dObjs);
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                };
                runner.start();
            }
        });
        this.jMenuItemRemoveFromDB.setText("Remove From DB");
        this.jMenuItemRemoveFromDB.setAction(new AbstractAction("Remove From DB") {

            /**
			 *
			 */
            private static final long serialVersionUID = 6594013764567931836L;

            public void actionPerformed(final ActionEvent e) {
                final Thread runner = new Thread() {

                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                DiskObject[] dObjs = JIThumbnailList.this.getSelectedValues();
                                JIThumbnailService.getInstance().removeFromDBFor(dObjs);
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                };
                runner.start();
            }
        });
        this.jMenuItemRemoveKeyWords.setText("Remove Key Words");
        this.jMenuItemRemoveKeyWords.setAction(new AbstractAction("Remove Key Words") {

            /**
			 *
			 */
            private static final long serialVersionUID = 387627212656276593L;

            public void actionPerformed(final ActionEvent e) {
                final Thread runner = new Thread() {

                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                DiskObject[] dObjs = JIThumbnailList.this.getSelectedValues();
                                JIThumbnailService.getInstance().removeKeyWordsFor(dObjs);
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                };
                runner.start();
            }
        });
        this.jMenuItemRemoveCategories.setText("Remove Categories");
        this.jMenuItemRemoveCategories.setAction(new AbstractAction("Remove Categories") {

            /**
			 *
			 */
            private static final long serialVersionUID = -2735386845803142478L;

            public void actionPerformed(final ActionEvent e) {
                final Thread runner = new Thread() {

                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                DiskObject[] dObjs = JIThumbnailList.this.getSelectedValues();
                                JIThumbnailService.getInstance().removeCategoriesFor(dObjs);
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                };
                runner.start();
            }
        });
        this.jMenuItemRefreshList.setText("Refresh List");
        this.jMenuItemRefreshList.setAction(new AbstractAction("Refresh List") {

            /**
			 *
			 */
            private static final long serialVersionUID = -7251048462021844506L;

            public void actionPerformed(final ActionEvent e) {
                final Thread runner = new Thread() {

                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                JIThumbnailList.this.getThumbnailListModel().reload();
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                };
                runner.start();
            }
        });
        this.jMenuItemKeyWords.setText("Key Words");
        this.jMenuItemKeyWords.setAction(new AbstractAction("Key Words") {

            /**
			 *
			 */
            private static final long serialVersionUID = 7424335156157608096L;

            public void actionPerformed(final ActionEvent e) {
                final Thread runner = new Thread() {

                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                if ((JIThumbnailList.this.keyWordsFrame == null) || JIThumbnailList.this.keyWordsFrame.isDisplayable()) {
                                    JIThumbnailList.this.keyWordsFrame = new KeyWordsFrame();
                                }
                                JIThumbnailList.this.keyWordsFrame.loadListModels();
                                JIThumbnailList.this.keyWordsFrame.requestFocus();
                                JIThumbnailList.this.keyWordsFrame.toFront();
                                JIThumbnailList.this.keyWordsFrame.setVisible(true);
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                };
                runner.start();
            }
        });
        this.jMenuItemCatagory.setText("Categories");
        this.jMenuItemCatagory.setAction(new AbstractAction("Categories") {

            /**
			 *
			 */
            private static final long serialVersionUID = -6563762962749839794L;

            public void actionPerformed(final ActionEvent e) {
                final Thread runner = new Thread() {

                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                if ((JIThumbnailList.this.catagorysFrame != null) && JIThumbnailList.this.catagorysFrame.isDisplayable()) {
                                    JIThumbnailList.this.catagorysFrame.setTitle();
                                    JIThumbnailList.this.catagorysFrame.requestFocus();
                                    JIThumbnailList.this.catagorysFrame.toFront();
                                } else {
                                    JIThumbnailList.this.catagorysFrame = new JICatTreeFrame();
                                }
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                };
                runner.start();
            }
        });
        this.jMenuItemMetadata.setText("Metadata");
        this.jMenuItemMetadata.setAction(new AbstractAction("Metadata") {

            /**
			 *
			 */
            private static final long serialVersionUID = -1071301932200150349L;

            public void actionPerformed(final ActionEvent e) {
                final Thread runner = new Thread() {

                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                Object obj = getSelectedValue();
                                if (obj != null) {
                                    new JIMetaDataFrame();
                                }
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                };
                runner.start();
            }
        });
        this.jMenuItemRename.setText("Rename");
        this.jMenuItemRename.setAction(new AbstractAction("Rename") {

            /**
			 *
			 */
            private static final long serialVersionUID = -6020862639879576761L;

            public void actionPerformed(final ActionEvent e) {
                editingStart();
            }
        });
        this.jMenuItemBatchRename.setText("Batch Rename");
        this.jMenuItemBatchRename.setAction(new AbstractAction("Batch Rename") {

            /**
			 *
			 */
            private static final long serialVersionUID = 2244076267910031489L;

            public void actionPerformed(final ActionEvent e) {
                if ((getSelectedIndices() != null) && (getSelectedIndices().length > 0)) {
                    final BatchRenameDialog brd = new BatchRenameDialog();
                    brd.setVisible(true);
                }
            }
        });
        this.jMenuItemDelete.setText("Delete");
        this.jMenuItemDelete.setAction(new AbstractAction("Delete") {

            /**
			 *
			 */
            private static final long serialVersionUID = -5510529849936122701L;

            public void actionPerformed(final ActionEvent e) {
                final Thread runner = new Thread() {

                    @Override
                    public void run() {
                        Runnable runnable = new Runnable() {

                            public void run() {
                                DiskObject[] diskObjects = getSelectedValues();
                                if (JOptionPane.showConfirmDialog(null, "Do you want to delete \nfile \"" + (diskObjects[0]).getName() + "\" ?", "JIExplorer", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                                    return;
                                }
                                if ((diskObjects != null) && (diskObjects.length > 0)) {
                                    clearSelection();
                                    final ArrayList<File> list = new ArrayList<File>();
                                    for (Object element : diskObjects) {
                                        list.add(((DiskObject) element).getFile());
                                    }
                                    DeleteFileTask deleteTask = new DeleteFileTask(list);
                                    ProgressDialog pd = new ProgressDialog(((Frame) getRootPane().getParent()), deleteTask.getOperationName(), deleteTask, null);
                                    pd.run();
                                    for (DiskObject element : diskObjects) {
                                        getThumbnailListModel().removeElement((element));
                                    }
                                }
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                };
                runner.start();
            }
        });
        registerKeyboardAction(this.jMenuItemDelete.getAction(), KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_FOCUSED);
        this.jMenuItemCut.setText("Cut");
        this.jMenuItemCut.setActionCommand((String) TransferHandler.getCutAction().getValue(Action.NAME));
        this.jMenuItemCut.addActionListener(this.actionListener);
        this.jMenuItemCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        this.jMenuItemCut.setMnemonic(KeyEvent.VK_U);
        this.jMenuItemCopy.setText("Copy");
        this.jMenuItemCopy.setActionCommand((String) TransferHandler.getCopyAction().getValue(Action.NAME));
        this.jMenuItemCopy.addActionListener(this.actionListener);
        this.jMenuItemCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        this.jMenuItemCopy.setMnemonic(KeyEvent.VK_C);
        this.jMenuItemPaste.setText("Paste");
        this.jMenuItemPaste.setActionCommand((String) TransferHandler.getPasteAction().getValue(Action.NAME));
        this.jMenuItemPaste.addActionListener(this.actionListener);
        this.jMenuItemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        this.jMenuItemPaste.setMnemonic(KeyEvent.VK_P);
        addMouseListener(new PopupTrigger());
    }

    @Override
    public DiskObject[] getSelectedValues() {
        final Object[] objs = super.getSelectedValues();
        final DiskObject[] dObjs = new DiskObject[objs.length];
        int cnt = 0;
        for (final Object obj : objs) {
            dObjs[cnt++] = (DiskObject) obj;
        }
        return dObjs;
    }

    public int getLastSelectedIndex() {
        final Object[] objs = super.getSelectedValues();
        final Object obj = super.getSelectedValue();
        int cnt = 0;
        for (final Object o : objs) {
            if (o.equals(obj)) {
                return cnt;
            }
            cnt++;
        }
        return cnt - 1;
    }

    protected final void listValueChanged(final ListSelectionEvent e) {
        if (this.lastSelectedDiskObject == null) {
            this.lastSelectedDiskObject = (DiskObject) getModel().getElementAt(e.getLastIndex());
        }
        JIExplorer.instance().getContext().setSelectedDiskObjects(this.getSelectedValues(), this.lastSelectedDiskObject);
        this.lastSelectedDiskObject = null;
        setChanged();
        notifyObservers(JIObservable.SECTION_CHANGED);
        clearChanged();
    }

    public final void listMouseClicked(final MouseEvent e) {
        final int index = this.locationToIndex(e.getPoint());
        if (getSelectionModel().isSelectedIndex(index)) {
            long clickTime = new Date().getTime();
            final DiskObject selectedObject = (DiskObject) this.getModel().getElementAt(index);
            File selectedFile;
            if ((selectedObject == null) || !(selectedObject instanceof DiskObject)) {
                selectedFile = null;
                clearSelection();
                this.lastClickIndex = -1;
                this.lastClickTime = -1;
                return;
            }
            ensureIndexIsVisible(index);
            selectedFile = selectedObject.getFile();
            if (SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() == 2)) {
                if (JIUtility.isSupportedImage(selectedObject.getSuffix())) {
                    try {
                        final OrderedDiskObjectList imageList = ((JIListModel) getModel()).getDiskObjectList();
                        imageList.setCurrentIndex(index);
                        new JIViewer(imageList);
                    } catch (final Exception de) {
                        JOptionPane.showMessageDialog(this, de.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (selectedObject.getFile().isDirectory()) {
                    JIExplorer.instance().getJDirTree().setSelectedDir(selectedObject);
                }
                e.consume();
            } else if (SwingUtilities.isLeftMouseButton(e) && (e.getClickCount() == 1)) {
                if ((index == this.lastClickIndex) && (this.lastClickTime > -1)) {
                    if ((clickTime - this.lastClickTime > 400) && (clickTime - this.lastClickTime < 2500) && ((this.getSelectedIndices() == null) || (getSelectedIndices().length == 1))) {
                        final Rectangle r = this.getCellBounds(index, index);
                        if (r.contains(e.getPoint())) {
                            this.setSelectedIndex(index);
                        }
                    }
                    clickTime = -1;
                }
            }
            this.lastClickIndex = index;
            this.lastClickTime = clickTime;
        }
    }

    public final void listMouseEvent(final MouseEvent e) {
        if (e.isPopupTrigger() == true) {
            try {
                final Object obj = getSelectedValue();
                if (obj == null) {
                    this.jDesktopPopupMenu.removeAll();
                    this.jDesktopPopupMenu.add(this.jMenuItemRefreshList);
                    this.jDesktopPopupMenu.addSeparator();
                    this.jDesktopPopupMenu.add(this.jMenuItemPaste);
                    if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
                        this.jMenuItemPaste.setEnabled(true);
                    } else {
                        this.jMenuItemPaste.setEnabled(false);
                    }
                    this.jDesktopPopupMenu.show(this, e.getX(), e.getY());
                } else {
                    final File selectedFile = ((DiskObject) getSelectedValue()).getFile();
                    if (!selectedFile.isDirectory()) {
                        this.jDesktopPopupMenu.removeAll();
                        this.jDesktopPopupMenu.add(this.jMenuItemOpen);
                        this.jDesktopPopupMenu.addSeparator();
                        if ((JIThumbnailService.getInstance().getOpenWith() != null) && (JIThumbnailService.getInstance().getOpenWith().size() > 0)) {
                            this.jDesktopPopupMenu.add(this.jMenuItemOpenWith);
                            this.jDesktopPopupMenu.addSeparator();
                        }
                        this.jDesktopPopupMenu.add(this.jMenuItemRefreshList);
                        this.jMenuItemRefresh.setText("Refresh Selected");
                        this.jMenuItemRefresh.add(this.jMenuItemRefreshIcon);
                        this.jMenuItemRefresh.add(this.jMenuItemRemoveFromDB);
                        this.jMenuItemRefresh.add(this.jMenuItemRemoveKeyWords);
                        this.jMenuItemRefresh.add(this.jMenuItemRemoveCategories);
                        this.jDesktopPopupMenu.add(this.jMenuItemRefresh);
                        this.jDesktopPopupMenu.addSeparator();
                        this.jDesktopPopupMenu.add(this.jMenuItemKeyWords);
                        this.jDesktopPopupMenu.add(this.jMenuItemCatagory);
                        this.jDesktopPopupMenu.add(this.jMenuItemMetadata);
                        this.jDesktopPopupMenu.addSeparator();
                        this.jDesktopPopupMenu.add(this.jMenuItemCut);
                        this.jDesktopPopupMenu.add(this.jMenuItemCopy);
                        this.jDesktopPopupMenu.add(this.jMenuItemPaste);
                        this.jDesktopPopupMenu.add(this.jMenuItemDelete);
                        this.jDesktopPopupMenu.add(this.jMenuItemRename);
                        this.jDesktopPopupMenu.add(this.jMenuItemBatchRename);
                        if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
                            this.jMenuItemPaste.setEnabled(true);
                        } else {
                            this.jMenuItemPaste.setEnabled(false);
                        }
                        this.jDesktopPopupMenu.show(this, e.getX(), e.getY());
                    } else {
                        this.jDesktopPopupMenu.removeAll();
                        this.jDesktopPopupMenu.add(this.jMenuItemOpen);
                        this.jDesktopPopupMenu.addSeparator();
                        if ((JIPreferences.getInstance().getOpenWith() != null) && (JIPreferences.getInstance().getOpenWith().size() > 0)) {
                            this.jDesktopPopupMenu.add(this.jMenuItemOpenWith);
                            this.jDesktopPopupMenu.addSeparator();
                        }
                        this.jDesktopPopupMenu.add(this.jMenuItemRefreshList);
                        this.jDesktopPopupMenu.addSeparator();
                        this.jDesktopPopupMenu.add(this.jMenuItemCopy);
                        if (JIExplorer.instance().getContext().getState() == JIExplorerContext.DIRECTORY_STATE) {
                            this.jDesktopPopupMenu.add(this.jMenuItemPaste);
                        }
                        this.jDesktopPopupMenu.add(this.jMenuItemRename);
                        this.jDesktopPopupMenu.add(this.jMenuItemBatchRename);
                        if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
                            this.jMenuItemPaste.setEnabled(true);
                        } else {
                            this.jMenuItemPaste.setEnabled(false);
                        }
                        this.jDesktopPopupMenu.show(this, e.getX(), e.getY());
                    }
                }
            } catch (final Exception exp) {
            } finally {
                e.consume();
            }
        }
    }

    final class PopupTrigger extends MouseAdapter {

        @Override
        public void mousePressed(final MouseEvent e) {
            final int index = JIThumbnailList.this.locationToIndex(e.getPoint());
            final DiskObject selectedObject = (DiskObject) JIThumbnailList.this.getModel().getElementAt(index);
            if ((selectedObject != null) && (selectedObject instanceof DiskObject)) {
                JIThumbnailList.this.lastSelectedDiskObject = selectedObject;
            }
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    listMouseEvent(e);
                }
            });
        }

        @Override
        public final void mouseClicked(final MouseEvent e) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    listMouseClicked(e);
                }
            });
        }

        @Override
        public void mouseReleased(final MouseEvent e) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    listMouseEvent(e);
                }
            });
        }
    }

    /**
	 * Adds an observer to the set of observers for this object, provided
	 * that it is not the same as some observer already in the set.
	 * The order in which notifications will be delivered to multiple
	 * observers is not specified. See the class comment.
	 *
	 * @param   o   an observer to be added.
	 * @throws NullPointerException   if the parameter o is null.
	 */
    public synchronized void addObserver(final JIObserver o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (!this.obs.contains(o)) {
            this.obs.addElement(o);
        }
    }

    /**
	 * Deletes an observer from the set of observers of this object.
	 * Passing <CODE>null</CODE> to this method will have no effect.
	 * @param   o   the observer to be deleted.
	 */
    public synchronized void deleteObserver(final JIObserver o) {
        this.obs.removeElement(o);
    }

    /**
	 * If this object has changed, as indicated by the
	 * <code>hasChanged</code> method, then notify all of its observers
	 * and then call the <code>clearChanged</code> method to
	 * indicate that this object has no longer changed.
	 * <p>
	 * Each observer has its <code>update</code> method called with two
	 * arguments: this observable object and <code>null</code>. In other
	 * words, this method is equivalent to:
	 * <blockquote><tt>
	 * notifyObservers(null)</tt></blockquote>
	 *
	 * @see     java.util.Observable#clearChanged()
	 * @see     java.util.Observable#hasChanged()
	 * @see     java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
    public void notifyObservers() {
        notifyObservers(null);
    }

    /**
	 * If this object has changed, as indicated by the
	 * <code>hasChanged</code> method, then notify all of its observers
	 * and then call the <code>clearChanged</code> method to indicate
	 * that this object has no longer changed.
	 * <p>
	 * Each observer has its <code>update</code> method called with two
	 * arguments: this observable object and the <code>arg</code> argument.
	 *
	 * @param   arg   any object.
	 * @see     java.util.Observable#clearChanged()
	 * @see     java.util.Observable#hasChanged()
	 * @see     java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
    public void notifyObservers(final Object arg) {
        Object[] arrLocal;
        synchronized (this) {
            if (!this.changed) {
                return;
            }
            arrLocal = this.obs.toArray();
            clearChanged();
        }
        for (int i = arrLocal.length - 1; i >= 0; i--) {
            ((JIObserver) arrLocal[i]).update(this, arg);
        }
    }

    public void notifyStatusBar(final Object arg) {
        Object[] arrLocal;
        synchronized (this) {
            arrLocal = this.obs.toArray();
            clearChanged();
        }
        for (int i = arrLocal.length - 1; i >= 0; i--) {
            if (arrLocal[i] instanceof StatusBarPanel) {
                ((JIObserver) arrLocal[i]).update(this, arg);
            }
        }
    }

    /**
	 * Clears the observer list so that this object no longer has any observers.
	 */
    public synchronized void deleteObservers() {
        this.obs.removeAllElements();
    }

    /**
	 * Marks this <tt>Observable</tt> object as having been changed; the
	 * <tt>hasChanged</tt> method will now return <tt>true</tt>.
	 */
    public synchronized void setChanged() {
        this.changed = true;
    }

    /**
	 * Indicates that this object has no longer changed, or that it has
	 * already notified all of its observers of its most recent change,
	 * so that the <tt>hasChanged</tt> method will now return <tt>false</tt>.
	 * This method is called automatically by the
	 * <code>notifyObservers</code> methods.
	 *
	 * @see     java.util.Observable#notifyObservers()
	 * @see     java.util.Observable#notifyObservers(java.lang.Object)
	 */
    public synchronized void clearChanged() {
        this.changed = false;
    }

    /**
	 * Tests if this object has changed.
	 *
	 * @return  <code>true</code> if and only if the <code>setChanged</code>
	 *          method has been called more recently than the
	 *          <code>clearChanged</code> method on this object;
	 *          <code>false</code> otherwise.
	 * @see     java.util.Observable#clearChanged()
	 * @see     java.util.Observable#setChanged()
	 */
    public synchronized boolean hasChanged() {
        return this.changed;
    }

    /**
	 * Returns the number of observers of this <tt>Observable</tt> object.
	 *
	 * @return  the number of observers of this object.
	 */
    public synchronized int countObservers() {
        return this.obs.size();
    }

    final class JIThumbnailKeyAdapter extends java.awt.event.KeyAdapter {

        /** Creates a new instance of JIThumbnailKeyListener */
        public JIThumbnailKeyAdapter() {
        }

        /** key event handlers */
        @Override
        public final void keyPressed(final KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                JIThumbnailList.this.selectionModel.setShiftKey(true);
            }
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                JIThumbnailList.this.selectionModel.setCntrlKey(true);
            }
            final Thread runner = new Thread() {

                @Override
                public void run() {
                    Runnable runnable = new Runnable() {

                        public void run() {
                            JIThumbnailList.this.jiThumbnail_keyPressed(e);
                        }
                    };
                    SwingUtilities.invokeLater(runnable);
                }
            };
            runner.start();
        }

        @Override
        public final void keyReleased(final KeyEvent e) {
            JIThumbnailList.this.selectionModel.setShiftKey(false);
            JIThumbnailList.this.selectionModel.setCntrlKey(false);
        }
    }
}
