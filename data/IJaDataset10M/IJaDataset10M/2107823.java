package org.fredy.id3tidy.ui;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import net.miginfocom.swing.MigLayout;
import org.fredy.id3tidy.ID3Tag;
import org.fredy.id3tidy.ID3TagEnum;
import org.fredy.id3tidy.ID3TagSelection;
import org.fredy.id3tidy.ID3TidyException;
import org.fredy.id3tidy.util.FileUtils;
import org.fredy.id3tidy.util.ID3Tidy;

/**
 * ID3Tidy frame.
 * @author fredy
 */
public class ID3TidyFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ID3TidyFrame.class.getName());

    private final ResourceBundle rb;

    private FileNamePatternPanel fileNamePatternPanel;

    private ID3TagEditorPanel id3TagEditorPanel;

    private DeleteTrimPanel deleteTrimPanel;

    private PlaylistPanel playlistPanel;

    private FileListPanel fileListPanel;

    private CreatedByPanel createdByPanel;

    public ID3TidyFrame() {
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("id3tidy", locale);
        initLookAndFeel();
        initComponents();
        pack();
    }

    private void initLookAndFeel() {
        String lookAndFeel = "com.jgoodies.looks.plastic.Plastic3DLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e1) {
                logger.severe(e1.getMessage());
                e1.printStackTrace();
            }
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, rb.getString(ResourceBundleKey.ERROR_TITLE.toString()), JOptionPane.ERROR_MESSAGE);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(rb.getString(ResourceBundleKey.LABEL_APPLICATION.toString()));
        setLayout(new MigLayout("", "[grow,fill]", "[][][grow,fill][]"));
        fileNamePatternPanel = new FileNamePatternPanel();
        id3TagEditorPanel = new ID3TagEditorPanel();
        deleteTrimPanel = new DeleteTrimPanel();
        playlistPanel = new PlaylistPanel();
        fileListPanel = new FileListPanel();
        createdByPanel = new CreatedByPanel();
        add(fileNamePatternPanel, "wrap, grow");
        JPanel panel = new JPanel(new MigLayout("", "[grow,fill][]", ""));
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.add(id3TagEditorPanel, "grow");
        JPanel subPanel = new JPanel(new MigLayout("", "[grow,fill][grow,fill]", ""));
        subPanel.setBorder(BorderFactory.createEtchedBorder());
        subPanel.add(deleteTrimPanel, "grow, wrap");
        subPanel.add(playlistPanel, "grow");
        panel.add(subPanel);
        add(panel, "wrap, grow");
        add(fileListPanel, "wrap, grow");
        add(createdByPanel, "wrap, grow");
        enableComponents(false);
    }

    private void enableAllComponents(boolean enabled) {
        enableComponents(enabled, fileNamePatternPanel.getComponents());
        enableComponents(enabled, id3TagEditorPanel.getComponents());
        enableComponents(enabled, deleteTrimPanel.getComponents());
        enableComponents(enabled, playlistPanel.getComponents());
        enableComponents(enabled, fileListPanel.getComponents());
    }

    private void enableComponents(boolean enabled) {
        enableAllComponents(enabled);
        fileListPanel.enableComponents(true);
    }

    private void enableComponents(boolean enabled, Component[] components) {
        for (Component c : components) {
            if (c instanceof JPanel) {
                enableComponents(enabled, ((JPanel) c).getComponents());
            } else {
                c.setEnabled(enabled);
            }
        }
    }

    class FileNamePatternPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private static final String DEFAULT_SEPARATOR = " - ";

        private static final int MIN_PATTERN = 1;

        private static final int MAX_PATTERN = 13;

        private JCheckBox overwriteCheckBox;

        private JButton addPatternButton;

        private JButton removePatternButton;

        private JButton updateButton;

        private LinkedList<JComponent> patternComponents = new LinkedList<JComponent>();

        public FileNamePatternPanel() {
            setBorder(BorderFactory.createEtchedBorder());
            setLayout(new MigLayout("", "[grow,fill]", ""));
            initComponents();
        }

        private void initComponents() {
            final JPanel panel1 = new JPanel(new MigLayout());
            panel1.setBorder(BorderFactory.createEtchedBorder());
            add(panel1, "wrap");
            panel1.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_FILENAME_PATTERN.toString())));
            JComboBox tagsComboBox = new JComboBox(ID3TagEnum.values());
            panel1.add(tagsComboBox);
            patternComponents.add(tagsComboBox);
            addPattern(patternComponents, panel1);
            JPanel panel2 = new JPanel(new MigLayout());
            panel2.setBorder(BorderFactory.createEtchedBorder());
            add(panel2, "wrap");
            addPatternButton = new JButton(rb.getString(ResourceBundleKey.BUTTON_ADD_PATTERN.toString()));
            addPatternButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (patternComponents.size() <= MAX_PATTERN - 2) {
                        addPattern(patternComponents, panel1);
                        panel1.updateUI();
                        ID3TidyFrame.this.pack();
                    }
                    if (patternComponents.size() == MAX_PATTERN) {
                        addPatternButton.setEnabled(false);
                    }
                    removePatternButton.setEnabled(true);
                }
            });
            panel2.add(addPatternButton);
            removePatternButton = new JButton(rb.getString(ResourceBundleKey.BUTTON_REMOVE_PATTERN.toString()));
            removePatternButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (patternComponents.size() >= MIN_PATTERN + 2) {
                        removePattern(patternComponents, panel1);
                        panel1.updateUI();
                        ID3TidyFrame.this.pack();
                    }
                    if (patternComponents.size() == MIN_PATTERN) {
                        removePatternButton.setEnabled(false);
                    }
                    addPatternButton.setEnabled(true);
                }
            });
            panel2.add(removePatternButton);
            JPanel panel3 = new JPanel(new MigLayout());
            panel3.setBorder(BorderFactory.createEtchedBorder());
            add(panel3);
            updateButton = new JButton(rb.getString(ResourceBundleKey.BUTTON_UPDATE.toString()));
            updateButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ID3TidyFrame.this.enableAllComponents(false);
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() throws Exception {
                            try {
                                List<Object> parts = new ArrayList<Object>();
                                for (JComponent component : patternComponents) {
                                    if (component instanceof JComboBox) {
                                        parts.add(((JComboBox) component).getSelectedItem());
                                    } else if (component instanceof JTextField) {
                                        parts.add(((JTextField) component).getText());
                                    }
                                }
                                ID3Tidy.update(fileListPanel.getFiles(), parts, overwriteCheckBox.isSelected());
                            } catch (ID3TidyException e) {
                                showErrorMessage(e.getMessage());
                                logger.severe(e.getMessage());
                            } catch (Exception e) {
                                logger.severe(e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            ID3TidyFrame.this.enableAllComponents(true);
                        }
                    };
                    worker.execute();
                }
            });
            panel3.add(updateButton);
            overwriteCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_OVERWRITE.toString()));
            overwriteCheckBox.setSelected(true);
            panel3.add(overwriteCheckBox);
        }

        private void addPattern(LinkedList<JComponent> patterns, JPanel panel) {
            JTextField separatorTextField = new JTextField(DEFAULT_SEPARATOR);
            separatorTextField.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ID3TidyFrame.this.pack();
                }
            });
            panel.add(separatorTextField);
            patterns.add(separatorTextField);
            JComboBox tagsComboxBox = new JComboBox(ID3TagEnum.values());
            panel.add(tagsComboxBox);
            patterns.add(tagsComboxBox);
        }

        private void removePattern(LinkedList<JComponent> patterns, JPanel panel) {
            JComponent component = patterns.getLast();
            panel.remove(component);
            patterns.removeLast();
            component = patterns.getLast();
            panel.remove(component);
            patterns.removeLast();
        }
    }

    class ID3TagEditorPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private static final int NUM_COLUMN = 30;

        private JTextField artistTextField;

        private JTextField titleTextField;

        private JTextField albumTextField;

        private JTextField genreTextField;

        private JTextField yearTextField;

        private JTextField trackTextField;

        private JTextField commentTextField;

        private JCheckBox overwriteCheckBox;

        private JButton updateButton;

        public ID3TagEditorPanel() {
            setBorder(BorderFactory.createEtchedBorder());
            setLayout(new MigLayout("", "[grow,fill]", ""));
            initComponents();
        }

        private void initComponents() {
            JPanel panel1 = new JPanel(new MigLayout("", "[][grow,fill]", ""));
            panel1.setBorder(BorderFactory.createEtchedBorder());
            add(panel1, "wrap");
            panel1.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_ARTIST.toString())));
            artistTextField = new JTextField(NUM_COLUMN);
            panel1.add(artistTextField, "wrap, grow");
            panel1.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_TITLE.toString())));
            titleTextField = new JTextField(NUM_COLUMN);
            panel1.add(titleTextField, "wrap, grow");
            panel1.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_ALBUM.toString())));
            albumTextField = new JTextField(NUM_COLUMN);
            panel1.add(albumTextField, "wrap, grow");
            panel1.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_GENRE.toString())));
            genreTextField = new JTextField(NUM_COLUMN);
            panel1.add(genreTextField, "wrap, grow");
            panel1.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_YEAR.toString())));
            yearTextField = new JTextField(NUM_COLUMN);
            panel1.add(yearTextField, "wrap, grow");
            panel1.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_TRACK.toString())));
            trackTextField = new JTextField(NUM_COLUMN);
            panel1.add(trackTextField, "wrap, grow");
            panel1.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_COMMENT.toString())));
            commentTextField = new JTextField(NUM_COLUMN);
            panel1.add(commentTextField, "wrap, grow");
            JPanel panel2 = new JPanel(new MigLayout());
            panel2.setBorder(BorderFactory.createEtchedBorder());
            add(panel2);
            updateButton = new JButton(rb.getString(ResourceBundleKey.BUTTON_UPDATE.toString()));
            updateButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ID3TidyFrame.this.enableAllComponents(false);
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() throws Exception {
                            try {
                                ID3Tag tag = new ID3Tag();
                                tag.setArtist(artistTextField.getText());
                                tag.setAlbum(albumTextField.getText());
                                tag.setTitle(titleTextField.getText());
                                tag.setGenre(genreTextField.getText());
                                tag.setComment(commentTextField.getText());
                                tag.setTrack(trackTextField.getText());
                                tag.setYear(yearTextField.getText());
                                ID3Tidy.update(fileListPanel.getSelectedFile(), tag, overwriteCheckBox.isSelected());
                            } catch (ID3TidyException e) {
                                showErrorMessage(e.getMessage());
                                logger.severe(e.getMessage());
                            } catch (Exception e) {
                                logger.severe(e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            ID3TidyFrame.this.enableAllComponents(true);
                        }
                    };
                    worker.execute();
                }
            });
            panel2.add(updateButton);
            overwriteCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_OVERWRITE.toString()));
            overwriteCheckBox.setSelected(true);
            panel2.add(overwriteCheckBox);
        }

        public void updateTextFields(ID3Tag tag) {
            artistTextField.setText(tag.getArtist());
            titleTextField.setText(tag.getTitle());
            albumTextField.setText(tag.getAlbum());
            genreTextField.setText(tag.getGenre());
            yearTextField.setText(tag.getYear());
            trackTextField.setText(tag.getTrack());
            commentTextField.setText(tag.getComment());
        }

        public void clearTextFields() {
            artistTextField.setText("");
            titleTextField.setText("");
            albumTextField.setText("");
            genreTextField.setText("");
            yearTextField.setText("");
            trackTextField.setText("");
            commentTextField.setText("");
        }
    }

    class FileListPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private static final String MP3_EXTENSION = ".mp3";

        private static final String M3U_EXTENSION = ".m3u";

        private JList fileList;

        private DefaultListModel fileListModel;

        private JButton openButton;

        private JButton upButton;

        private JButton downButton;

        private JButton deleteButton;

        private JFileChooser fileChooser;

        public FileListPanel() {
            setBorder(BorderFactory.createEtchedBorder());
            setLayout(new MigLayout("", "[grow,fill][]", "[grow,fill]"));
            initComponents();
        }

        private void initComponents() {
            fileChooser = new JFileChooser();
            fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setFileFilter(new FileFilter() {

                @Override
                public String getDescription() {
                    return rb.getString(ResourceBundleKey.FILECHOOSER_ID3TIDY_SUPPORTED_FILES.toString());
                }

                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    if (f.getName().toLowerCase().endsWith(MP3_EXTENSION) || f.getName().toLowerCase().endsWith(M3U_EXTENSION)) {
                        return true;
                    }
                    return false;
                }
            });
            fileListModel = new DefaultListModel();
            fileList = new JList(fileListModel);
            fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            fileList.setBorder(BorderFactory.createEtchedBorder());
            fileList.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent evt) {
                    if (fileList.getSelectedIndex() == -1) {
                        return;
                    }
                    File selectedFile = (File) fileListModel.getElementAt(fileList.getSelectedIndex());
                    try {
                        ID3Tag tag = ID3Tidy.read(selectedFile);
                        id3TagEditorPanel.updateTextFields(tag);
                    } catch (ID3TidyException e) {
                        showErrorMessage(rb.getString(ResourceBundleKey.ERROR_UNABLE_TO_READ.toString()) + selectedFile);
                        logger.severe(e.getMessage());
                    } catch (Exception e) {
                        logger.severe(e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
            fileList.setTransferHandler(new TransferHandler() {

                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                public boolean importData(JComponent comp, Transferable t) {
                    if (!(comp instanceof JList)) {
                        return false;
                    }
                    if (!t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        return false;
                    }
                    try {
                        List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                        addFiles(files.toArray(new File[files.size()]));
                        ID3TidyFrame.this.enableAllComponents(true);
                        ID3TidyFrame.this.pack();
                        return true;
                    } catch (UnsupportedFlavorException ufe) {
                        logger.severe(ufe.getMessage());
                        ufe.printStackTrace();
                    } catch (IOException ioe) {
                        logger.severe(ioe.getMessage());
                        ioe.printStackTrace();
                    }
                    return false;
                }

                public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
                    if (comp instanceof JList) {
                        for (int i = 0; i < transferFlavors.length; i++) {
                            if (!transferFlavors[i].equals(DataFlavor.javaFileListFlavor)) {
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                }
            });
            JScrollPane scrollPane = new JScrollPane(fileList);
            add(scrollPane, "grow");
            openButton = new JButton(rb.getString(ResourceBundleKey.BUTTON_OPEN.toString()));
            openButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = fileChooser.showOpenDialog(ID3TidyFrame.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        addFiles(fileChooser.getSelectedFiles());
                        ID3TidyFrame.this.enableAllComponents(true);
                        ID3TidyFrame.this.pack();
                    }
                }
            });
            upButton = new JButton(rb.getString(ResourceBundleKey.BUTTON_UP.toString()));
            upButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] selectedIndices = fileList.getSelectedIndices();
                    if (selectedIndices.length == 1) {
                        int index = selectedIndices[0];
                        if (index > 0) {
                            File f = (File) fileListModel.get(index);
                            File tmp = (File) fileListModel.get(index - 1);
                            fileListModel.setElementAt(f, index - 1);
                            fileListModel.setElementAt(tmp, index);
                            fileList.setSelectedIndex(index - 1);
                        }
                    }
                }
            });
            downButton = new JButton(rb.getString(ResourceBundleKey.BUTTON_DOWN.toString()));
            downButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] selectedIndices = fileList.getSelectedIndices();
                    if (selectedIndices.length == 1) {
                        int index = selectedIndices[0];
                        if (index < fileListModel.size() - 1) {
                            File f = (File) fileListModel.get(index);
                            File tmp = (File) fileListModel.get(index + 1);
                            fileListModel.setElementAt(f, index + 1);
                            fileListModel.setElementAt(tmp, index);
                            fileList.setSelectedIndex(index + 1);
                        }
                    }
                }
            });
            deleteButton = new JButton(rb.getString(ResourceBundleKey.BUTTON_DELETE.toString()));
            deleteButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int[] selectedIndeces = fileList.getSelectedIndices();
                    for (int i = (selectedIndeces.length - 1); i >= 0; i--) {
                        fileListModel.remove(selectedIndeces[i]);
                        if (fileListModel.getSize() >= 1) {
                            fileList.setSelectedIndex(selectedIndeces[i] - 1);
                        }
                    }
                    if (fileListModel.getSize() == 0) {
                        ID3TidyFrame.this.enableComponents(false);
                        id3TagEditorPanel.clearTextFields();
                    }
                }
            });
            JPanel panel = new JPanel(new MigLayout("", "[grow,fill]", ""));
            panel.setBorder(BorderFactory.createEtchedBorder());
            panel.add(openButton, "wrap, grow");
            panel.add(upButton, "wrap, grow");
            panel.add(downButton, "wrap, grow");
            panel.add(deleteButton, "wrap, grow");
            add(panel);
        }

        private void addFiles(File... files) {
            for (File file : files) {
                for (File f : FileUtils.getFileListing(file, MP3_EXTENSION, M3U_EXTENSION)) {
                    if (f.getName().toLowerCase().endsWith(M3U_EXTENSION)) {
                        List<File> files1 = ID3Tidy.readM3UFile(f);
                        for (File f1 : files1) {
                            fileListModel.addElement(f1);
                        }
                        playlistPanel.setM3UFile(f.getPath());
                    } else {
                        fileListModel.addElement(f);
                    }
                }
            }
        }

        public File getSelectedFile() {
            return (File) fileList.getSelectedValue();
        }

        public List<File> getFiles() {
            List<File> files = new ArrayList<File>();
            Enumeration<?> elements = fileListModel.elements();
            while (elements.hasMoreElements()) {
                files.add((File) elements.nextElement());
            }
            return files;
        }

        public void enableComponents(boolean enabled) {
            openButton.setEnabled(enabled);
            ;
        }
    }

    class DeleteTrimPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private JCheckBox trimAllCheckBox;

        private JCheckBox trimArtistCheckBox;

        private JCheckBox trimTitleCheckBox;

        private JCheckBox trimAlbumCheckBox;

        private JCheckBox trimGenreCheckBox;

        private JCheckBox trimCommentCheckBox;

        private JCheckBox deleteAllCheckBox;

        private JCheckBox deleteArtistCheckBox;

        private JCheckBox deleteTitleCheckBox;

        private JCheckBox deleteAlbumCheckBox;

        private JCheckBox deleteGenreCheckBox;

        private JCheckBox deleteCommentCheckBox;

        private JButton updateButton;

        public DeleteTrimPanel() {
            setBorder(BorderFactory.createEtchedBorder());
            setLayout(new MigLayout("", "[grow,fill]", ""));
            initComponents();
        }

        private void initComponents() {
            JPanel panel1 = new JPanel(new MigLayout());
            panel1.setBorder(BorderFactory.createEtchedBorder());
            add(panel1, "grow, wrap");
            panel1.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_TRIM.toString())));
            trimAllCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_ALL.toString()));
            trimAllCheckBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    enableTrimCheckBoxes(!trimAllCheckBox.isSelected());
                    if (trimAllCheckBox.isSelected()) {
                        resetTrimCheckBoxes();
                    }
                }
            });
            panel1.add(trimAllCheckBox);
            trimArtistCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_ARTIST.toString()));
            panel1.add(trimArtistCheckBox);
            trimTitleCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_TITLE.toString()));
            panel1.add(trimTitleCheckBox);
            trimAlbumCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_ALBUM.toString()));
            panel1.add(trimAlbumCheckBox);
            trimGenreCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_GENRE.toString()));
            panel1.add(trimGenreCheckBox);
            trimCommentCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_COMMENT.toString()));
            panel1.add(trimCommentCheckBox, "wrap");
            panel1.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_DELETE.toString())));
            deleteAllCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_ALL.toString()));
            deleteAllCheckBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    enableDeleteCheckBoxes(!deleteAllCheckBox.isSelected());
                    if (deleteAllCheckBox.isSelected()) {
                        resetDeleteCheckBoxes();
                    }
                }
            });
            panel1.add(deleteAllCheckBox);
            deleteArtistCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_ARTIST.toString()));
            panel1.add(deleteArtistCheckBox);
            deleteTitleCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_TITLE.toString()));
            panel1.add(deleteTitleCheckBox);
            deleteAlbumCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_ALBUM.toString()));
            panel1.add(deleteAlbumCheckBox);
            deleteGenreCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_GENRE.toString()));
            panel1.add(deleteGenreCheckBox);
            deleteCommentCheckBox = new JCheckBox(rb.getString(ResourceBundleKey.LABEL_COMMENT.toString()));
            panel1.add(deleteCommentCheckBox, "wrap");
            JPanel panel2 = new JPanel(new MigLayout());
            panel2.setBorder(BorderFactory.createEtchedBorder());
            updateButton = new JButton(rb.getString(ResourceBundleKey.BUTTON_UPDATE.toString()));
            updateButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ID3TidyFrame.this.enableAllComponents(false);
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() throws Exception {
                            try {
                                trimID3Tags();
                                deleteID3Tags();
                            } catch (ID3TidyException e) {
                                showErrorMessage(e.getMessage());
                                logger.severe(e.getMessage());
                            } catch (Exception e) {
                                logger.severe(e.getMessage());
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void done() {
                            ID3TidyFrame.this.enableAllComponents(true);
                        }
                    };
                    worker.execute();
                }
            });
            panel2.add(updateButton);
            add(panel2, "grow, wrap");
        }

        private void trimID3Tags() {
            ID3TagSelection selection = new ID3TagSelection();
            if (trimAllCheckBox.isSelected()) {
                selection.setAlbumSelected(true);
                selection.setArtistSelected(true);
                selection.setTitleSelected(true);
                selection.setCommentSelected(true);
                selection.setGenreSelected(true);
            } else {
                selection.setAlbumSelected(trimAlbumCheckBox.isSelected());
                selection.setArtistSelected(trimArtistCheckBox.isSelected());
                selection.setTitleSelected(trimTitleCheckBox.isSelected());
                selection.setCommentSelected(trimCommentCheckBox.isSelected());
                selection.setGenreSelected(trimGenreCheckBox.isSelected());
            }
            ID3Tidy.trim(fileListPanel.getFiles(), selection);
        }

        private void deleteID3Tags() {
            ID3TagSelection selection = new ID3TagSelection();
            if (deleteAllCheckBox.isSelected()) {
                selection.setAlbumSelected(true);
                selection.setArtistSelected(true);
                selection.setTitleSelected(true);
                selection.setCommentSelected(true);
                selection.setGenreSelected(true);
            } else {
                selection.setAlbumSelected(deleteAlbumCheckBox.isSelected());
                selection.setArtistSelected(deleteArtistCheckBox.isSelected());
                selection.setTitleSelected(deleteTitleCheckBox.isSelected());
                selection.setCommentSelected(deleteCommentCheckBox.isSelected());
                selection.setGenreSelected(deleteGenreCheckBox.isSelected());
            }
            ID3Tidy.delete(fileListPanel.getFiles(), selection);
        }

        private void enableTrimCheckBoxes(boolean enabled) {
            trimArtistCheckBox.setEnabled(enabled);
            trimTitleCheckBox.setEnabled(enabled);
            trimAlbumCheckBox.setEnabled(enabled);
            trimGenreCheckBox.setEnabled(enabled);
            trimCommentCheckBox.setEnabled(enabled);
        }

        private void resetTrimCheckBoxes() {
            trimArtistCheckBox.setSelected(false);
            trimTitleCheckBox.setSelected(false);
            trimAlbumCheckBox.setSelected(false);
            trimGenreCheckBox.setSelected(false);
            trimCommentCheckBox.setSelected(false);
        }

        private void enableDeleteCheckBoxes(boolean enabled) {
            deleteArtistCheckBox.setEnabled(enabled);
            deleteTitleCheckBox.setEnabled(enabled);
            deleteAlbumCheckBox.setEnabled(enabled);
            deleteGenreCheckBox.setEnabled(enabled);
            deleteCommentCheckBox.setEnabled(enabled);
        }

        private void resetDeleteCheckBoxes() {
            deleteArtistCheckBox.setSelected(false);
            deleteTitleCheckBox.setSelected(false);
            deleteAlbumCheckBox.setSelected(false);
            deleteGenreCheckBox.setSelected(false);
            deleteCommentCheckBox.setSelected(false);
        }
    }

    class PlaylistPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private static final String M3U_EXTENSION = ".m3u";

        private JTextField saveToTextField;

        private JButton saveToButton;

        private ButtonGroup buttonGroup;

        private JRadioButton absolutePathRadioButton;

        private JRadioButton relativePathRadioButton;

        private JButton createButton;

        private JFileChooser fileChooser;

        public PlaylistPanel() {
            setBorder(BorderFactory.createEtchedBorder());
            setLayout(new MigLayout("", "[grow,fill]", ""));
            initComponents();
        }

        private void initComponents() {
            fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileFilter() {

                @Override
                public String getDescription() {
                    return rb.getString(ResourceBundleKey.FILECHOOSER_M3U.toString());
                }

                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    if (f.getName().toLowerCase().endsWith(M3U_EXTENSION)) {
                        return true;
                    }
                    return false;
                }
            });
            JPanel panel1 = new JPanel(new MigLayout("", "[][grow,fill][]", ""));
            panel1.setBorder(BorderFactory.createEtchedBorder());
            add(panel1, "grow, wrap");
            panel1.add(new JLabel(rb.getString(ResourceBundleKey.LABEL_M3U_FILE.toString())));
            saveToButton = new JButton(rb.getString(ResourceBundleKey.BUTTON_SAVETO.toString()));
            saveToButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = fileChooser.showSaveDialog(ID3TidyFrame.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        saveToTextField.setText(fileChooser.getSelectedFile().toString());
                        ID3TidyFrame.this.pack();
                    }
                }
            });
            saveToTextField = new JTextField();
            saveToTextField.setEditable(false);
            panel1.add(saveToTextField, "grow");
            panel1.add(saveToButton, "wrap");
            buttonGroup = new ButtonGroup();
            absolutePathRadioButton = new JRadioButton(rb.getString(ResourceBundleKey.RADIO_BUTTON_ABSOLUTE_PATH.toString()));
            absolutePathRadioButton.setSelected(true);
            buttonGroup.add(absolutePathRadioButton);
            panel1.add(absolutePathRadioButton);
            relativePathRadioButton = new JRadioButton(rb.getString(ResourceBundleKey.RADIO_BUTTON_RELATIVE_PATH.toString()));
            buttonGroup.add(relativePathRadioButton);
            panel1.add(relativePathRadioButton);
            JPanel panel2 = new JPanel(new MigLayout());
            panel2.setBorder(BorderFactory.createEtchedBorder());
            createButton = new JButton(rb.getString(ResourceBundleKey.BUTTON_CREATE.toString()));
            createButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ID3TidyFrame.this.enableAllComponents(false);
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() throws Exception {
                            ID3Tidy.createM3UFile(fileListPanel.getFiles(), new File(saveToTextField.getText()), absolutePathRadioButton.isSelected());
                            return null;
                        }

                        @Override
                        protected void done() {
                            ID3TidyFrame.this.enableAllComponents(true);
                        }
                    };
                    worker.execute();
                }
            });
            panel2.add(createButton);
            add(panel2, "grow, wrap");
        }

        public void setM3UFile(String path) {
            saveToTextField.setText(path);
        }
    }

    class CreatedByPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        public CreatedByPanel() {
            setBorder(BorderFactory.createEtchedBorder());
            add(new JLabel(rb.getString(ResourceBundleKey.LABEL_CREATED_BY.toString())));
        }
    }
}
