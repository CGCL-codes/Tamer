package de.schwarzrot.ui.control;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileSystemView;
import org.springframework.context.MessageSource;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.DefaultEventListModel;
import ca.odell.glazedlists.swing.DefaultEventSelectionModel;
import de.schwarzrot.app.support.ApplicationServiceProvider;
import de.schwarzrot.ui.control.support.AbstractDialogBase;
import de.schwarzrot.ui.render.FileListCellRenderer;

public class FileChooser extends AbstractDialogBase {

    public static final int SELECT_FILE_MUST_EXIST = 0x0101;

    public static final int SELECT_FILE_MAY_EXIST = 0x0102;

    public static final int SELECT_DIRECTORY = 0x0104;

    public static final int LOCAL_FILESYSTEM = 0x0201;

    public static final int REMOTE_FILESYSTEM = 0x0202;

    public static final int PROBE_FILESYSTEM = 0x0204;

    private static final long serialVersionUID = 713L;

    private static FileSystemView localFileSystemView;

    private static FileSystemView remoteFileSystemView;

    public FileChooser(String initialSelection) {
        this(initialSelection, PROBE_FILESYSTEM, SELECT_FILE_MUST_EXIST);
    }

    public FileChooser(String initialSelection, int fileSystemMode) {
        this(initialSelection, fileSystemMode, SELECT_FILE_MUST_EXIST);
    }

    public FileChooser(String initialSelection, int fileSystemMode, int fileSelectionMode) {
        super(DialogMode.CANCEL_APPROVE);
        init(initialSelection, fileSystemMode, fileSelectionMode);
    }

    public int getFileSelectionMode() {
        return fileSelectionMode;
    }

    public File getSelectedFile() {
        File rv = null;
        List<File> tmp = fileSelection.getSelected();
        if (tmp != null && tmp.size() > 0) rv = tmp.get(0);
        if (tmp == null && fileSelectionMode == SELECT_DIRECTORY) rv = directory;
        return rv;
    }

    public void setFileSelectionMode(int fileSelectionMode) {
        switch(fileSelectionMode) {
            case SELECT_DIRECTORY:
                this.fileSelectionMode = SELECT_DIRECTORY;
                break;
            case SELECT_FILE_MAY_EXIST:
                this.fileSelectionMode = SELECT_FILE_MAY_EXIST;
                break;
            case SELECT_FILE_MUST_EXIST:
                this.fileSelectionMode = SELECT_FILE_MUST_EXIST;
                break;
            default:
                throw new IllegalArgumentException("given value is not a valid file selection mode!");
        }
    }

    public void setFileSystemMode(int fileSystemMode) {
        switch(fileSystemMode) {
            case LOCAL_FILESYSTEM:
                this.fileSystemMode = LOCAL_FILESYSTEM;
                break;
            case REMOTE_FILESYSTEM:
                this.fileSystemMode = REMOTE_FILESYSTEM;
                break;
            case PROBE_FILESYSTEM:
                this.fileSystemMode = PROBE_FILESYSTEM;
                break;
            default:
                throw new IllegalArgumentException("given value is not a valid file system mode!");
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected JComponent buildPanel() {
        directory = initialSelection;
        boolean debug = true;
        if (debug) {
            File[] rootEntries = fsv.getRoots();
            for (File possibleRoot : rootEntries) {
                System.err.println("\nFileChooser::checkRoot - root entry is: " + possibleRoot.getAbsolutePath());
            }
        }
        rescanDirectory();
        if (msgSource == null) msgSource = ApplicationServiceProvider.getService(MessageSource.class);
        this.setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel();
        JButton top = componentFactory.createDialogButton(getName(), "top");
        JButton up = componentFactory.createDialogButton(getName(), "up");
        JButton newDir = componentFactory.createDialogButton(getName(), "newFolder");
        top.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                change2RootDirectory();
            }
        });
        up.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                change2ParentDirectory();
            }
        });
        newDir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionevent) {
                createDirectory();
            }
        });
        infoPanel.setLayout(new FlowLayout());
        lbDirectory = new JLabel();
        lbDirectory.setText(directory.getAbsolutePath());
        infoPanel.add(lbDirectory);
        infoPanel.add(top);
        infoPanel.add(up);
        infoPanel.add(newDir);
        this.add(infoPanel, BorderLayout.NORTH);
        fileList = new JList(model);
        JScrollPane sp = new JScrollPane(fileList);
        sp.setPreferredSize(new Dimension(400, 300));
        this.add(sp, BorderLayout.CENTER);
        fileList.setCellRenderer(new FileListCellRenderer());
        fileList.setVisibleRowCount(0);
        fileList.setLayoutOrientation(JList.VERTICAL_WRAP);
        fileSelection = new DefaultEventSelectionModel<File>(files);
        fileList.setSelectionModel(fileSelection);
        fileList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseevent) {
                if (mouseevent.getClickCount() > 1) {
                    List<File> selected = fileSelection.getSelected();
                    if (selected != null && selected.size() > 0) {
                        File selectedFile = selected.get(0);
                        if (fsv.isTraversable(selectedFile)) change2Directory(selectedFile);
                    }
                }
            }
        });
        return this;
    }

    protected void change2Directory(File subDir) {
        directory = subDir;
        lbDirectory.setText(directory.getAbsolutePath());
        rescanDirectory();
    }

    protected void change2ParentDirectory() {
        if (!fsv.isRoot(directory)) {
            directory = directory.getParentFile();
            lbDirectory.setText(directory.getAbsolutePath());
            rescanDirectory();
        }
    }

    protected void change2RootDirectory() {
        File tmp = directory;
        while (!fsv.isRoot(tmp) && tmp.getParentFile() != null) tmp = tmp.getParentFile();
        if (tmp != null && fsv.isRoot(tmp)) {
            directory = tmp;
            lbDirectory.setText(directory.getAbsolutePath());
        }
        rescanDirectory();
    }

    protected void createDirectory() {
        String s = (String) JOptionPane.showInputDialog(this, "Please enter a name for the directory to create: ", isRemoteFS() ? "create remote directory" : "create directory", JOptionPane.PLAIN_MESSAGE, null, null, "newFolder");
        try {
            File newDirectory = fsv.createNewFolder(directory);
            File target = new File(directory, s);
            newDirectory.renameTo(target);
            target = fsv.createFileObject(directory, s);
            if (fsv.isTraversable(target)) change2Directory(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getDialogTitle() {
        return title;
    }

    protected int getFileSystemMode() {
        return fileSystemMode;
    }

    protected FileSystemView getFileSystemView() {
        return fsv;
    }

    protected File getInitialSelection() {
        return initialSelection;
    }

    protected FileSystemView getLocalFileSystemView() {
        if (localFileSystemView == null) localFileSystemView = FileSystemView.getFileSystemView();
        return localFileSystemView;
    }

    protected FileSystemView getRemoteFileSystemView() {
        if (remoteFileSystemView == null) remoteFileSystemView = new RemoteFileSystemView();
        return remoteFileSystemView;
    }

    protected void init(String initialValue, int fileSystemMode, int fileSelectionMode) {
        File tmp = new File(".");
        this.initialSelection = new File(initialValue);
        setFileSystemMode(fileSystemMode);
        setFileSelectionMode(fileSelectionMode);
        switch(fileSystemMode) {
            case LOCAL_FILESYSTEM:
                remoteFS = false;
                break;
            case REMOTE_FILESYSTEM:
                remoteFS = true;
                break;
            default:
                remoteFS = !(initialSelection.getAbsolutePath().compareTo(tmp.getAbsolutePath()) == 0 || initialSelection.exists());
        }
        fsv = remoteFS ? getRemoteFileSystemView() : getLocalFileSystemView();
        files = new BasicEventList<File>();
        model = new DefaultEventListModel<File>(files);
    }

    protected boolean isRemoteFS() {
        return remoteFS;
    }

    @Override
    protected void performApprove() {
        File result = getSelectedFile();
        boolean ok2Close = false;
        switch(fileSelectionMode) {
            case SELECT_DIRECTORY:
                ok2Close = result != null && result.exists() && result.isDirectory();
                break;
            case SELECT_FILE_MUST_EXIST:
                ok2Close = result != null && result.exists() && !result.isDirectory();
                break;
            default:
                ok2Close = result != null && !result.isDirectory();
                break;
        }
        if (!ok2Close) throw new RuntimeException("invalid selection - don't close dialog");
    }

    protected void replaceList(File[] data) {
        files.getReadWriteLock().writeLock().lock();
        files.clear();
        if (data != null && data.length > 0) {
            for (File cur : data) files.add(cur);
        }
        files.getReadWriteLock().writeLock().unlock();
    }

    protected void rescanDirectory() {
        while (!fsv.isTraversable(directory)) directory = fsv.getParentDirectory(directory);
        if (directory == null) directory = fsv.getDefaultDirectory();
        replaceList(fsv.getFiles(directory, true));
        this.invalidate();
    }

    private EventList<File> files;

    private DefaultEventListModel<File> model;

    private DefaultEventSelectionModel<File> fileSelection;

    private File initialSelection;

    private File directory;

    private String title;

    private JLabel lbDirectory;

    private JList fileList;

    private FileSystemView fsv;

    private boolean remoteFS;

    private int fileSelectionMode;

    private int fileSystemMode;
}
