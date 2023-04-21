package com.jiexplorer.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import com.jiexplorer.JIExplorer;
import com.jiexplorer.filetask.FileTask;
import com.jiexplorer.filetask.FileTaskListener;
import com.jiexplorer.filetask.ScanFileTask;
import com.jiexplorer.gui.cattree.JICatTreeNode;
import com.jiexplorer.gui.datetree.JIDateTreeNode;
import com.jiexplorer.gui.preferences.JIPreferences;
import com.jiexplorer.gui.tree.JITreeNode;
import com.jiexplorer.util.DiskObject;
import com.jiexplorer.util.JIExplorerContext;
import com.jiexplorer.util.JIObservable;
import com.jiexplorer.util.JIObserver;
import com.jiexplorer.util.JIUtility;

public class StatusBarPanel extends Box implements JIObserver, FileTaskListener {

    /**
	 *
	 */
    private static final long serialVersionUID = 4869926816624225711L;

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StatusBarPanel.class);

    private static StatusBarPanel instance;

    private JLabel lblSrc;

    private JLabel lblObject;

    private JLabel lblDim;

    private JLabel lblSize;

    private JLabel lblDbType;

    private JLabel lblFreeMemory;

    private JLabel lblDesc;

    private JProgressBar progressBar;

    private FileTask fileTask = null;

    /**
	* Auto-generated main method to display this
	* JPanel inside a new JFrame.
	*/
    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        frame.getContentPane().add(new StatusBarPanel());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static StatusBarPanel getInstance() {
        if (instance == null) {
            instance = new StatusBarPanel("JIExplorer");
        }
        return instance;
    }

    private StatusBarPanel() {
        super(BoxLayout.X_AXIS);
        initGUI();
    }

    private StatusBarPanel(final String txt) {
        super(BoxLayout.X_AXIS);
        initGUI();
        lblDesc.setText(txt);
    }

    private void initGUI() {
        try {
            final Toolkit kit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = kit.getScreenSize();
            this.setPreferredSize(new java.awt.Dimension(589, 22));
            {
                lblSrc = new JLabel();
                this.add(lblSrc);
                lblSrc.setText("  ");
                lblSrc.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                lblSrc.setPreferredSize(new Dimension((int) (0.15 * screenSize.width), 20));
            }
            {
                lblObject = new JLabel();
                this.add(lblObject);
                lblObject.setText("  ");
                lblObject.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                lblObject.setPreferredSize(new Dimension((int) (0.15 * screenSize.width), 20));
            }
            {
                lblDim = new JLabel();
                this.add(lblDim);
                lblDim.setText("  ");
                lblDim.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                lblDim.setPreferredSize(new Dimension((int) (0.15 * screenSize.width), 20));
            }
            {
                lblSize = new JLabel();
                this.add(lblSize);
                lblSize.setText("  ");
                lblSize.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                lblSize.setPreferredSize(new Dimension((int) (0.1 * screenSize.width), 20));
            }
            {
                lblDesc = new JLabel();
                this.add(lblDesc);
                lblDesc.setText("  ");
                lblDesc.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                lblDesc.setPreferredSize(new Dimension((int) (0.1 * screenSize.width), 20));
            }
            {
                lblFreeMemory = new JLabel();
                this.add(lblFreeMemory);
                lblFreeMemory.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                lblFreeMemory.setText(memoryInf());
                lblFreeMemory.setPreferredSize(new Dimension((int) (0.15 * screenSize.width), 20));
            }
            {
                lblDbType = new JLabel();
                this.add(lblDbType);
                lblDbType.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                lblDbType.setText("  ");
                lblDbType.setPreferredSize(new Dimension((int) (0.05 * screenSize.width), 20));
                lblDbType.setText(dbInf());
            }
            {
                progressBar = ProgressMonitor.getInstance();
                this.add(progressBar);
                progressBar.setStringPainted(true);
                progressBar.setPreferredSize(new Dimension((int) (0.15 * screenSize.width), 20));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void useProgressBar(final int size) {
        this.progressBar.setStringPainted(true);
        this.progressBar.setValue(0);
    }

    public void dropProgressBar() {
        this.progressBar.setValue(0);
    }

    public final void startProgressBar(final int cnt) {
        if (this.progressBar != null) {
            this.progressBar.setMaximum(cnt);
            this.progressBar.setValue(0);
        }
    }

    public final void updateProgressBar(final int value) {
        if (this.progressBar != null) {
            this.progressBar.setValue(value);
        }
    }

    public void update(final JIObservable list, final Object obj) {
        log.debug("StatusBar::update - " + obj + "  Source - " + list.getClass().getName());
        lblFreeMemory.setText(memoryInf());
        if (obj instanceof String) {
            if (((String) obj).equals(JIObservable.SECTION_CHANGED)) {
                final DiskObject[] list_selection = JIExplorer.instance().getContext().getSelectedDiskObjects();
                final int index = JIExplorer.instance().getContext().getLastSelectedDiskObjectIndex();
                if ((list_selection != null) && (index < list_selection.length) && (index > -1)) {
                    this.lblObject.setText(list_selection[index].getName());
                    this.lblSize.setText("Size: " + list_selection[index].getSize());
                    this.lblDim.setText("Dimension: " + list_selection[index].getDim());
                    if (list_selection.length > 1) {
                        long size = 0;
                        for (final DiskObject dobj : list_selection) {
                            size += dobj.getLength();
                        }
                        this.lblDesc.setText(list_selection.length + " object(s) Size(" + JIUtility.length2KB(size) + ")");
                    } else {
                        this.lblDesc.setText("JIExplorer");
                    }
                } else {
                    this.lblObject.setText("  ");
                    this.lblSize.setText("  ");
                    this.lblDim.setText("  ");
                }
            } else if (((String) obj).equals(JIObservable.DIRECTORY_LOADED)) {
                final JIExplorerContext context = JIExplorer.instance().getContext();
                final String prefixStr;
                final StringBuffer buf = new StringBuffer();
                switch(context.getState()) {
                    case JIExplorerContext.DIRECTORY_STATE:
                        final Vector<JITreeNode> list_selection = JIExplorer.instance().getContext().getSelectedDirNodes();
                        final int index = JIExplorer.instance().getContext().getLastSelectedDirNodesIndex();
                        prefixStr = "Directory: " + list_selection.elementAt(index).getFile().getName();
                        if (true) {
                        }
                        break;
                    case JIExplorerContext.KEY_WORDS_STATE:
                        final String[] keyWords = JIExplorer.instance().getContext().getSelectedKeyWords();
                        for (final String keyWord : keyWords) {
                            buf.append(keyWord + ", ");
                        }
                        prefixStr = "KeyWords: " + buf.substring(0, buf.length() - 2);
                        break;
                    case JIExplorerContext.CATEGORY_STATE:
                        final Vector<JICatTreeNode> categories = JIExplorer.instance().getContext().getSelectedCatNodes();
                        for (final JICatTreeNode node : categories) {
                            buf.append(node + ", ");
                        }
                        prefixStr = "Categories: " + buf.substring(0, buf.length() - 2);
                        break;
                    case JIExplorerContext.DATE_STATE:
                        final Vector<JIDateTreeNode> dates = JIExplorer.instance().getContext().getSelectedDateNodes();
                        for (final JIDateTreeNode node : dates) {
                            buf.append(node.getDisplayFormat() + ", ");
                        }
                        prefixStr = "Dates: " + buf.substring(0, buf.length() - 2);
                        break;
                    default:
                        prefixStr = "";
                }
                final int imageCnt = context.getImageCnt();
                if (prefixStr != null) {
                    this.lblSrc.setText(prefixStr + " - (" + imageCnt + " Objects)");
                }
            } else if (((String) obj).equals(JIObservable.DIRECTORY_LOADING)) {
                final Vector<JITreeNode> list_selection = JIExplorer.instance().getContext().getSelectedDirNodes();
                final int index = JIExplorer.instance().getContext().getLastSelectedDirNodesIndex();
                if ((list_selection != null) && (list_selection.size() > 0)) {
                    final Object objt = list_selection.elementAt(index).getFile();
                    if ((objt != null) && (objt instanceof File)) {
                        final File sVal = (File) objt;
                        this.lblObject.setText("Loading ... " + sVal.getName());
                    } else {
                        this.lblObject.setText("Loading ... ");
                    }
                    this.lblSize.setText(" ");
                    this.lblDim.setText(" ");
                    this.lblDesc.setText("JIExplorer ");
                }
            } else if (((String) obj).equals(JIObservable.DIRECTORY_SIZE)) {
                final JIExplorerContext context = JIExplorer.instance().getContext();
                String prefixStr = null;
                final StringBuffer buf = new StringBuffer();
                switch(context.getState()) {
                    case JIExplorerContext.DIRECTORY_STATE:
                        final Vector<JITreeNode> list_selection = JIExplorer.instance().getContext().getSelectedDirNodes();
                        final int index = JIExplorer.instance().getContext().getLastSelectedDirNodesIndex();
                        prefixStr = "Directory: " + list_selection.elementAt(index).getFile().getName();
                        if (JIPreferences.getInstance().isLoadAllImageIcons() && !JIExplorer.instance().getContext().isStatusBarProgressTaskRunning()) {
                            JIExplorer.instance().getContext().setStatusBarProgressTaskRunning(true);
                            log.debug("StatusBar::update - fileTask == null");
                            final Thread w = new Thread() {

                                @Override
                                public void run() {
                                    try {
                                        final Vector<JITreeNode> nodes = JIExplorer.instance().getContext().getSelectedDirNodes();
                                        final Vector<File> dirs = new Vector<File>();
                                        final Vector<File> files = new Vector<File>();
                                        for (final JITreeNode node : nodes) {
                                            getAllImageFiles(dirs, files, node.getFile(), false);
                                        }
                                        StatusBarPanel.this.fileTask = new ScanFileTask(dirs, files);
                                        useProgressBar((int) StatusBarPanel.this.fileTask.getTotal());
                                        log.debug("StatusBar::update - progress = " + StatusBarPanel.this.fileTask.getTotal());
                                        StatusBarPanel.this.fileTask.setListener(StatusBarPanel.this);
                                        StatusBarPanel.this.fileTask.run();
                                    } catch (final Throwable t) {
                                        t.printStackTrace();
                                    } finally {
                                        dropProgressBar();
                                        JIExplorer.instance().getContext().setStatusBarProgressTaskRunning(false);
                                    }
                                }
                            };
                            w.start();
                        }
                        break;
                    case JIExplorerContext.KEY_WORDS_STATE:
                        final String[] keyWords = JIExplorer.instance().getContext().getSelectedKeyWords();
                        if (keyWords != null) {
                            for (final String keyWord : keyWords) {
                                buf.append(keyWord + ", ");
                            }
                            prefixStr = "KeyWords: " + buf.substring(0, buf.length() - 2);
                        }
                        break;
                    case JIExplorerContext.CATEGORY_STATE:
                        final Vector<JICatTreeNode> categories = JIExplorer.instance().getContext().getSelectedCatNodes();
                        for (final JICatTreeNode node : categories) {
                            buf.append(node + ", ");
                        }
                        prefixStr = "Categories: " + buf.substring(0, buf.length() - 2);
                        break;
                    case JIExplorerContext.DATE_STATE:
                        final Vector<JIDateTreeNode> dates = JIExplorer.instance().getContext().getSelectedDateNodes();
                        for (final JIDateTreeNode node : dates) {
                            buf.append(node.getDisplayFormat() + ", ");
                        }
                        prefixStr = "Dates: " + buf.substring(0, buf.length() - 2);
                        break;
                    default:
                        prefixStr = "";
                }
                final int imageCnt = context.getImageCnt();
                if (prefixStr != null) {
                    this.lblSrc.setText(prefixStr + " - (" + imageCnt + " Objects)");
                }
            } else if (((String) obj).equals(JIObservable.PROGERSS_START)) {
                if (!JIExplorer.instance().getContext().isStatusBarProgressTaskRunning()) {
                    JIExplorer.instance().getContext().setStatusBarProgressTaskRunning(true);
                    log.debug("StatusBar::update - fileTask == null");
                    final Thread w = new Thread() {

                        @Override
                        public void run() {
                            try {
                                final Vector<JITreeNode> nodes = JIExplorer.instance().getContext().getSelectedDirNodes();
                                final Vector<File> dirs = new Vector<File>();
                                final Vector<File> files = new Vector<File>();
                                for (final JITreeNode node : nodes) {
                                    getAllImageFiles(dirs, files, node.getFile(), true);
                                }
                                StatusBarPanel.this.fileTask = new ScanFileTask(dirs, files);
                                useProgressBar((int) StatusBarPanel.this.fileTask.getTotal());
                                log.debug("StatusBar::update - progress = " + StatusBarPanel.this.fileTask.getTotal());
                                StatusBarPanel.this.fileTask.setListener(StatusBarPanel.this);
                                StatusBarPanel.this.fileTask.run();
                            } catch (final Throwable t) {
                                t.printStackTrace();
                            } finally {
                                dropProgressBar();
                                JIExplorer.instance().getContext().setStatusBarProgressTaskRunning(false);
                            }
                        }
                    };
                    w.start();
                }
            } else if (((String) obj).equals(JIObservable.PROGERSS_CANCELLED)) {
                if (this.fileTask != null) {
                    this.fileTask.setCancelled(true);
                    JIExplorer.instance().getContext().setStatusBarProgressTaskRunning(false);
                    this.fileTask = null;
                }
            }
        }
    }

    public void fileTaskCancelled(final FileTask filetask) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                dropProgressBar();
            }
        });
    }

    public void fileTaskCompleted(final FileTask filetask) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                dropProgressBar();
            }
        });
    }

    public void fileTaskProgress(final FileTask filetask) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (!filetask.isDone() && !filetask.isCancelled()) {
                    StatusBarPanel.this.updateProgressBar((int) filetask.getOverallProgress());
                }
            }
        });
    }

    public boolean promptAllOverride(final FileTask filetask) {
        return false;
    }

    public boolean promptOverride(final FileTask filetask) {
        return false;
    }

    public boolean promptRetryCreate(final FileTask filetask) {
        return false;
    }

    public boolean promptRetryDelete(final FileTask filetask) {
        return false;
    }

    public void getAllImageFiles(final Vector<File> dirs, final Vector<File> files, final File dir, final boolean recursive) {
        dir.listFiles(new FilenameFilter() {

            public boolean accept(final File dir, final String name) {
                final File isDir = new File(dir, name);
                if (isDir.isDirectory() && recursive && JIPreferences.getInstance().isLoadIconsRecursively()) {
                    dirs.add(isDir);
                    getAllImageFiles(dirs, files, isDir, true);
                } else {
                    files.add(isDir);
                }
                return isDir.isDirectory();
            }
        });
    }

    public final String dbInf() {
        return JIPreferences.getInstance().getDatabaseType();
    }

    public final String memoryInf() {
        return "Free: " + Runtime.getRuntime().freeMemory() / 1024 / 1024 + " Total: " + Runtime.getRuntime().totalMemory() / 1024 / 1024 + " Max: " + Runtime.getRuntime().maxMemory() / 1024 / 1024;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getLblObject() {
        return lblObject;
    }

    public JLabel getLblDim() {
        return lblDim;
    }

    public JLabel getLblSize() {
        return lblSize;
    }

    public JLabel getLblDbType() {
        return lblDbType;
    }

    public JLabel getLblFreeMemory() {
        return lblFreeMemory;
    }

    public JLabel getLblDesc() {
        return lblDesc;
    }

    public JLabel getLblSrc() {
        return lblSrc;
    }
}
