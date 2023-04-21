package be.vds.jtbdive.client.swing.component;

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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSelector extends JPanel {

    public static final String PROPERTY_FILE_CHANGED = "file.changed";

    private JTextField fileTf;

    private int fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES;

    private String file;

    private List<FileNameExtensionFilter> filters = new ArrayList<FileNameExtensionFilter>();

    private boolean acceptAllFilters = true;

    private JButton fileButton;

    public JButton getFileButton() {
        return fileButton;
    }

    public void setAcceptAllFilters(boolean acceptAllFilters) {
        this.acceptAllFilters = acceptAllFilters;
    }

    public void addFileNameExtensionFilter(FileNameExtensionFilter filter) {
        filters.add(filter);
    }

    public FileSelector() {
        init();
    }

    private void init() {
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
            public void keyTyped(KeyEvent e) {
                file = fileTf.getText().trim();
                if (e.getKeyCode() == KeyEvent.VK_ENTER) firePropertyChange(PROPERTY_FILE_CHANGED, null, file);
            }
        });
        return fileTf;
    }

    private JComponent createButton() {
        Action action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                if (null != file) chooser.setSelectedFile(new File(file));
                chooser.setFileSelectionMode(fileSelectionMode);
                chooser.setAcceptAllFileFilterUsed(acceptAllFilters);
                if (0 < filters.size()) {
                    for (FileFilter filter : filters) {
                        chooser.addChoosableFileFilter(filter);
                    }
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
        fileButton.setContentAreaFilled(false);
        fileButton.setText("select");
        fileButton.setPreferredSize(new Dimension(20, 20));
        return fileButton;
    }

    private void registerFile(File file, boolean fireEvent) {
        this.file = file.getAbsolutePath();
        if (file != null) {
            fileTf.setText(file.getPath());
        } else {
            fileTf.setText(null);
        }
        fileTf.selectAll();
        if (fireEvent) firePropertyChange(PROPERTY_FILE_CHANGED, null, file);
    }

    public void setFileSelectionMode(int fileSelectionMode) {
        this.fileSelectionMode = fileSelectionMode;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(200, 30);
        JPanel p = new JPanel(new BorderLayout());
        FileSelector fsc = new FileSelector();
        fsc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fsc.addFileNameExtensionFilter(new FileNameExtensionFilter("XML file", "xml"));
        p.add(fsc, BorderLayout.CENTER);
        f.getContentPane().add(p);
        f.pack();
        f.setVisible(true);
    }

    public File getSelectedFile() {
        if (null == file || file.trim().length() == 0) return null;
        return new File(file.trim());
    }

    public void setSelectedFile(File file) {
        registerFile(file, false);
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

    @Override
    public void setEnabled(boolean enabled) {
        fileTf.setEnabled(enabled);
        fileTf.setEditable(enabled);
        fileButton.setEnabled(enabled);
    }
}
