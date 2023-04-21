package gov.sns.apps.labbook;

import gov.sns.tools.bricks.*;
import gov.sns.tools.messaging.MessageCenter;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.Image;
import java.util.*;

/** controller for images */
public class ImageController extends AbstractController {

    /** file watcher */
    protected final FileWatcher FILE_WATCHER;

    /** folder watch list model */
    protected final FolderWatchListModel FOLDER_WATCH_LIST_MODEL;

    /** file table model */
    protected final FileTableModel FILE_TABLE_MODEL;

    /** image entry table model */
    protected final ImageEntryTableModel IMAGE_ENTRY_TABLE_MODEL;

    /** file chooser for selecting folders */
    protected JFileChooser _folderChooser;

    /** file chooser for selecting media files */
    protected JFileChooser _mediaChooser;

    /** Constructor */
    public ImageController(final MessageCenter messageCenter, final WindowReference windowReference) {
        super(messageCenter, windowReference);
        FILE_WATCHER = FileWatcher.getImageWatcherInstance(ImageController.class);
        IMAGE_ENTRY_TABLE_MODEL = new ImageEntryTableModel();
        FOLDER_WATCH_LIST_MODEL = new FolderWatchListModel();
        FILE_TABLE_MODEL = new FileTableModel();
        setupImageEntryTable(windowReference);
        setupFolderWatchList(windowReference);
        setupFilePicker(windowReference);
    }

    /**
	 * Validate whether the summary is valid
	 * @return true if the summary is valid and false if not
	 */
    public boolean validate() {
        final List<ImageEntry> entries = getImageEntries();
        for (final ImageEntry entry : entries) {
            final int titleLength = entry.getTitle().length();
            final String comment = entry.getComment();
            if (titleLength == 0) {
                _validationText = "Image title must have at least one character.";
                return false;
            } else if (titleLength > 120) {
                _validationText = "Image title length must be less than 120 characters.  \n\"" + entry.getTitle() + "\" has " + titleLength;
                return false;
            } else if (!entry.getImageFile().exists()) {
                _validationText = "Image file \"" + entry.getTitle() + "\" does not exist";
                return false;
            } else if (comment != null && comment.length() > 4000) {
                _validationText = "Image comment must be less than 4000 characters.  \nImage \"" + entry.getTitle() + "\" has " + comment.length();
                return false;
            }
        }
        _validationText = "Okay";
        return true;
    }

    /** get the image entries */
    public List<ImageEntry> getImageEntries() {
        return IMAGE_ENTRY_TABLE_MODEL.getImageEntries();
    }

    /** setup the image entry table */
    protected void setupImageEntryTable(final WindowReference windowReference) {
        final ImagePanel imagePanel = (ImagePanel) windowReference.getView("ImagePreviewPanel");
        final JTextArea commentTextArea = (JTextArea) windowReference.getView("ImageTextArea");
        final JTable imageEntryTable = (JTable) windowReference.getView("ImageTable");
        imageEntryTable.setModel(IMAGE_ENTRY_TABLE_MODEL);
        imageEntryTable.setDragEnabled(true);
        imageEntryTable.setTransferHandler(new ImageEntryListTransferHandler());
        imageEntryTable.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(final MouseEvent event) {
                final int row = imageEntryTable.rowAtPoint(event.getPoint());
                if (row >= 0) {
                    final ImageEntry entry = IMAGE_ENTRY_TABLE_MODEL.getImageEntry(row);
                    if (entry != null) {
                        imageEntryTable.setToolTipText(entry.getImageFile().getAbsolutePath());
                    } else {
                        imageEntryTable.setToolTipText(null);
                    }
                } else {
                    imageEntryTable.setToolTipText(null);
                }
            }
        });
        imageEntryTable.addMouseListener(new MouseAdapter() {

            public void mouseExited(final MouseEvent event) {
                imageEntryTable.setToolTipText(null);
            }
        });
        imageEntryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(final ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    previewSelectedImage(imageEntryTable, imagePanel, commentTextArea);
                }
            }
        });
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        commentTextArea.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(final DocumentEvent event) {
                updateEntry();
            }

            public void insertUpdate(final DocumentEvent event) {
                updateEntry();
            }

            public void removeUpdate(final DocumentEvent event) {
                updateEntry();
            }

            public void updateEntry() {
                final int selectedRow = imageEntryTable.getSelectedRow();
                if (selectedRow >= 0) {
                    final ImageEntry entry = IMAGE_ENTRY_TABLE_MODEL.getImageEntry(selectedRow);
                    if (entry != null) {
                        entry.setComment(commentTextArea.getText());
                        postDocumentChangeEvent();
                    }
                }
            }
        });
    }

    /** preview the selected image in the label */
    protected void previewSelectedImage(final JTable imageTable, final ImagePanel imagePanel, final JTextArea commentTextArea) {
        final int row = imageTable.getSelectedRow();
        if (row >= 0) {
            final ImageEntry entry = IMAGE_ENTRY_TABLE_MODEL.getImageEntry(row);
            if (entry != null) {
                imagePanel.setIcon(entry.getIcon());
                imagePanel.validate();
                if (commentTextArea != null) {
                    commentTextArea.setText(entry.getComment());
                }
            }
        } else {
            imagePanel.setIcon(null);
            commentTextArea.setText("");
        }
        imagePanel.repaint();
    }

    /** setup the files table */
    protected void setupFilePicker(final WindowReference windowReference) {
        final JTable filesTable = (JTable) windowReference.getView("ImageFilesTable");
        filesTable.setModel(FILE_TABLE_MODEL);
        filesTable.setDragEnabled(true);
        filesTable.setTransferHandler(new FileListTransferHandler());
        filesTable.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseMoved(final MouseEvent event) {
                final int row = filesTable.rowAtPoint(event.getPoint());
                if (row >= 0) {
                    final File file = FILE_TABLE_MODEL.getFile(row);
                    if (file != null) {
                        filesTable.setToolTipText(file.getAbsolutePath());
                    } else {
                        filesTable.setToolTipText(null);
                    }
                } else {
                    filesTable.setToolTipText(null);
                }
            }
        });
        filesTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(final MouseEvent event) {
                if (event.getClickCount() == 2) {
                    final int row = filesTable.rowAtPoint(event.getPoint());
                    if (row >= 0) {
                        final File file = FILE_TABLE_MODEL.getFile(row);
                        final ImageEntry entry = new ImageEntry(file);
                        final List<ImageEntry> entries = new ArrayList<ImageEntry>(IMAGE_ENTRY_TABLE_MODEL.getImageEntries());
                        entries.add(entry);
                        IMAGE_ENTRY_TABLE_MODEL.setImageEntries(entries);
                        postDocumentChangeEvent();
                    }
                }
            }

            public void mouseExited(final MouseEvent event) {
                filesTable.setToolTipText(null);
            }
        });
        final JButton filesRefreshButton = (JButton) windowReference.getView("ImageFilesRefreshButton");
        filesRefreshButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                refreshFilesList();
            }
        });
        final JButton mediaBrowseButton = (JButton) windowReference.getView("ImageBrowseButton");
        mediaBrowseButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                final JFileChooser mediaChooser = getMediaChooser();
                final int status = mediaChooser.showOpenDialog(mediaBrowseButton);
                switch(status) {
                    case JFileChooser.CANCEL_OPTION:
                        break;
                    case JFileChooser.APPROVE_OPTION:
                        final File[] selections = mediaChooser.getSelectedFiles();
                        if (selections.length > 0) {
                            final List<ImageEntry> entries = new ArrayList<ImageEntry>(IMAGE_ENTRY_TABLE_MODEL.getImageEntries());
                            for (final File selection : selections) {
                                final ImageEntry entry = new ImageEntry(selection);
                                entries.add(entry);
                            }
                            IMAGE_ENTRY_TABLE_MODEL.setImageEntries(entries);
                        }
                        break;
                    case JFileChooser.ERROR_OPTION:
                        break;
                    default:
                        break;
                }
            }
        });
        refreshFilesList();
    }

    /** refresh the files */
    protected void refreshFilesList() {
        final List<File> files = FILE_WATCHER.listFiles();
        FILE_TABLE_MODEL.setFiles(files);
    }

    /** setup the folder watch list */
    protected void setupFolderWatchList(final WindowReference windowReference) {
        final JList watchList = (JList) windowReference.getView("ImageFolderList");
        watchList.setModel(FOLDER_WATCH_LIST_MODEL);
        FOLDER_WATCH_LIST_MODEL.setFolders(new ArrayList<File>(FILE_WATCHER.getFolders()));
        final JButton folderDeleteButton = (JButton) windowReference.getView("ImageFolderDeleteButton");
        folderDeleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                final int[] selectedIndices = watchList.getSelectedIndices();
                if (selectedIndices.length > 0) {
                    final List<File> selectedFolders = new ArrayList<File>();
                    for (final int index : selectedIndices) {
                        selectedFolders.add(FILE_WATCHER.getFolder(index));
                    }
                    for (final File folder : selectedFolders) {
                        FILE_WATCHER.ignoreFolder(folder);
                    }
                    FOLDER_WATCH_LIST_MODEL.setFolders(new ArrayList<File>(FILE_WATCHER.getFolders()));
                    refreshFilesList();
                }
            }
        });
        final JButton folderAddButton = (JButton) windowReference.getView("ImageFolderAddButton");
        folderAddButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                final JFileChooser folderChooser = getFolderChooser();
                final int status = folderChooser.showOpenDialog(folderAddButton);
                switch(status) {
                    case JFileChooser.CANCEL_OPTION:
                        break;
                    case JFileChooser.APPROVE_OPTION:
                        final File[] selections = folderChooser.getSelectedFiles();
                        if (selections.length > 0) {
                            for (final File selection : selections) {
                                FILE_WATCHER.watchFolder(selection);
                            }
                            FOLDER_WATCH_LIST_MODEL.setFolders(new ArrayList<File>(FILE_WATCHER.getFolders()));
                            refreshFilesList();
                        }
                        break;
                    case JFileChooser.ERROR_OPTION:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /** get the folder chooser creating it if one does not already exist */
    protected JFileChooser getFolderChooser() {
        if (_folderChooser == null) {
            _folderChooser = new JFileChooser();
            _folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            _folderChooser.setMultiSelectionEnabled(true);
        }
        return _folderChooser;
    }

    /** get the file chooser for media creating it if one does not already exist */
    protected JFileChooser getMediaChooser() {
        if (_mediaChooser == null) {
            _mediaChooser = new JFileChooser();
            _mediaChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            _mediaChooser.setMultiSelectionEnabled(true);
        }
        return _mediaChooser;
    }

    /** File list transfer handler */
    class FileListTransferHandler extends TransferHandler {

        /** transfer files from the files list to image entries */
        protected Transferable createTransferable(final JComponent component) {
            final JTable table = (JTable) component;
            final int[] rows = table.getSelectedRows();
            if (rows.length >= 0) {
                final List<File> files = new ArrayList<File>(rows.length);
                for (final int row : rows) {
                    final File file = FILE_TABLE_MODEL.getFile(row);
                    files.add(file);
                }
                return ImageEntry.getTransferable(files);
            } else {
                return null;
            }
        }

        /** provides copy or move operation */
        public int getSourceActions(final JComponent component) {
            return TransferHandler.COPY;
        }
    }

    /** Image entry list tranfer handler */
    class ImageEntryListTransferHandler extends TransferHandler {

        /** rows of the entries copied */
        protected final List<Integer> ROWS_COPIED;

        /** row onto which the entries were dropped */
        protected int dropRow;

        /** Constructor */
        public ImageEntryListTransferHandler() {
            ROWS_COPIED = new ArrayList<Integer>();
        }

        /** transfer files from the files list to image entries */
        protected Transferable createTransferable(final JComponent component) {
            dropRow = -1;
            ROWS_COPIED.clear();
            final JTable table = (JTable) component;
            final int[] rows = table.getSelectedRows();
            if (rows.length >= 0) {
                final List<ImageEntry> entries = new ArrayList<ImageEntry>(rows.length);
                for (final int row : rows) {
                    final ImageEntry entry = IMAGE_ENTRY_TABLE_MODEL.getImageEntry(row);
                    entries.add(entry);
                    ROWS_COPIED.add(row);
                }
                return ImageEntry.getTransferableForEntries(entries);
            } else {
                return null;
            }
        }

        /** provides copy or move operation */
        public int getSourceActions(final JComponent component) {
            return TransferHandler.MOVE;
        }

        /** determine if the table can import at least one of the tranferable flavors */
        public boolean canImport(final JComponent component, final DataFlavor[] flavors) {
            for (DataFlavor flavor : flavors) {
                if (flavor == ImageEntryTransferable.IMAGE_ENTRY_FLAVOR) return true;
            }
            return false;
        }

        /** import the transferable */
        public boolean importData(final JComponent component, final Transferable transferable) {
            final JTable table = (JTable) component;
            try {
                final int selectedRow = table.getSelectedRow();
                dropRow = selectedRow;
                final List<ImageEntry> entries = new ArrayList<ImageEntry>(IMAGE_ENTRY_TABLE_MODEL.getImageEntries());
                final List<ImageEntry> transfers = (List<ImageEntry>) transferable.getTransferData(ImageEntryTransferable.IMAGE_ENTRY_FLAVOR);
                if (selectedRow >= 0) {
                    int row = selectedRow;
                    for (final ImageEntry entry : transfers) {
                        entries.add(row++, entry);
                    }
                } else {
                    for (final ImageEntry entry : transfers) {
                        entries.add(entry);
                    }
                }
                IMAGE_ENTRY_TABLE_MODEL.setImageEntries(entries);
                postDocumentChangeEvent();
                return true;
            } catch (UnsupportedFlavorException exception) {
                exception.printStackTrace();
                return false;
            } catch (java.io.IOException exception) {
                exception.printStackTrace();
                return false;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }

        /** complete the transfer */
        public void exportDone(final JComponent component, final Transferable transferable, final int action) {
            switch(action) {
                case MOVE:
                    final JTable table = (JTable) component;
                    final int selectedRow = table.getSelectedRow();
                    final List<ImageEntry> entries = new ArrayList<ImageEntry>(IMAGE_ENTRY_TABLE_MODEL.getImageEntries());
                    Collections.sort(ROWS_COPIED);
                    Collections.reverse(ROWS_COPIED);
                    if (dropRow < 0 && selectedRow >= 0) {
                        for (final Number rowNumber : ROWS_COPIED) {
                            final int row = rowNumber.intValue();
                            entries.remove(row);
                        }
                    } else if (dropRow >= 0) {
                        final int rowShift = ROWS_COPIED.size();
                        for (final Number rowNumber : ROWS_COPIED) {
                            final int row = rowNumber.intValue();
                            if (row < dropRow) {
                                entries.remove(row);
                            } else {
                                entries.remove(row + rowShift);
                            }
                        }
                    }
                    IMAGE_ENTRY_TABLE_MODEL.setImageEntries(entries);
                    ROWS_COPIED.clear();
                    postDocumentChangeEvent();
                    break;
                default:
                    break;
            }
        }
    }
}
