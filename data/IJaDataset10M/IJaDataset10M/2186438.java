package be.vds.jtb.swing.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSelector extends JPanel {

    private static final long serialVersionUID = 523483750918315127L;

    public static final String PROPERTY_FILE_CHANGED = "file.changed";

    private JTextField fileTf;

    private String filePath;

    private List<FileFilter> filters = new ArrayList<FileFilter>();

    private JButton fileButton;

    private JFileChooser chooser;

    public JButton getFileButton() {
        return fileButton;
    }

    public void setAcceptAllFilters(boolean acceptAllFilters) {
        chooser.setAcceptAllFileFilterUsed(acceptAllFilters);
    }

    public void addFileFilter(FileFilter filter) {
        filters.add(filter);
    }

    public FileSelector() {
        init();
    }

    private void init() {
        intiFileChooser();
        this.setLayout(new BorderLayout(0, 0));
        this.setOpaque(false);
        this.add(createFileTextField(), BorderLayout.CENTER);
        this.add(createButton(), BorderLayout.EAST);
    }

    public JTextField getFileTf() {
        return fileTf;
    }

    private JComponent createFileTextField() {
        fileTf = new JTextField();
        fileTf.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                filePath = fileTf.getText();
                firePropertyChange(PROPERTY_FILE_CHANGED, null, filePath);
            }
        });
        return fileTf;
    }

    private JComponent createButton() {
        Action action = new AbstractAction() {

            private static final long serialVersionUID = 3148252269112057974L;

            public void actionPerformed(ActionEvent e) {
                if (null != filePath) chooser.setSelectedFile(new File(filePath));
                if (0 < filters.size()) {
                    for (FileFilter filter : filters) {
                        chooser.addChoosableFileFilter(filter);
                    }
                }
                if (chooser.isAcceptAllFileFilterUsed()) {
                    chooser.setAcceptAllFileFilterUsed(true);
                }
                File file = new File(fileTf.getText());
                if (null != file && file.exists()) {
                    chooser.setSelectedFile(file);
                }
                int i = chooser.showOpenDialog(null);
                if (i == JFileChooser.OPEN_DIALOG) {
                    File f = chooser.getSelectedFile();
                    String extens = getExtension(f);
                    String[] array = null;
                    try {
                        array = ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions();
                    } catch (ClassCastException e1) {
                    }
                    if (array != null) {
                        if (null == extens || !isExtensionContained(array, extens)) {
                            if (1 == array.length) {
                                f = new File(f.getAbsolutePath() + "." + array[0]);
                            }
                        }
                    }
                    registerFile(f, true);
                }
            }

            private boolean isExtensionContained(String[] array, String extens) {
                for (int i = 0; i < array.length; i++) {
                    if (array[i].equals(extens)) {
                        return true;
                    }
                }
                return false;
            }
        };
        fileButton = new JButton(action);
        fileButton.setText("...");
        fileButton.setPreferredSize(new Dimension(20, 20));
        return fileButton;
    }

    private void intiFileChooser() {
        chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileHidingEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    private void registerFile(File file, boolean fireEvent) {
        this.filePath = file.getAbsolutePath();
        if (file != null) {
            fileTf.setText(file.getPath());
        }
        fileTf.selectAll();
        if (fireEvent) firePropertyChange(PROPERTY_FILE_CHANGED, null, filePath);
    }

    public void setFileSelectionMode(int fileSelectionMode) {
        chooser.setFileSelectionMode(fileSelectionMode);
    }

    public File getSelectedFile() {
        if (null == filePath || filePath.trim().length() == 0) return null;
        return new File(filePath.trim());
    }

    public void setSelectedFile(File file) {
        if (file == null) {
            reset();
        } else {
            registerFile(file, false);
        }
    }

    public void reset() {
        filePath = null;
        fileTf.setText(null);
    }

    private String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public void setShowHiddenFiles(boolean showHiddenFiles) {
        chooser.setFileHidingEnabled(showHiddenFiles);
    }

    public void setButtonText(String text) {
        fileButton.setText(text);
    }

    public void setButtonIcon(Icon icon) {
        fileButton.setIcon(icon);
    }

    public void setButtonBorderPainted(boolean b) {
        fileButton.setBorderPainted(b);
    }

    public void setButtonContentAreaFilled(boolean b) {
        fileButton.setContentAreaFilled(b);
    }

    @Override
    public void setEnabled(boolean enabled) {
        fileTf.setEnabled(enabled);
        fileTf.setEditable(enabled);
        fileButton.setEnabled(enabled);
    }
}
