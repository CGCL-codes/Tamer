package org.joone.edit;

import javax.swing.*;
import org.joone.log.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.beans.*;

/**
 * This class is a wrapper class, which add two additional
 * functionality(directory history and file preview)
 * to the original JFileChooser.
 *
 * It is based on EFileChooser.java written by Klaus Berg.
 */
public class JooneFileChooser extends JFileChooser {

    /**
     * Logger
     * */
    private static final ILogger log = LoggerFactory.getLogger(JooneFileChooser.class);

    private String m_dir;

    private FileInputStream m_fis;

    private ObjectInputStream m_ois;

    private Vector m_dirList;

    private static final String APPLICATION_NAME = "Joone Edit";

    private HistoryAndPreviewPanel m_historyAndPreviewPanel;

    private JComboBox m_comboBox;

    private TextPreviewer m_previewer;

    private static final long serialVersionUID = 8488231091781173351L;

    /** Constructing a modified JFileChooser */
    public JooneFileChooser(String baseDir) {
        super(baseDir);
        m_dir = System.getProperty("user.home") + System.getProperty("file.separator") + APPLICATION_NAME + "_DIRECTORY_HISTORY.cfg";
        if (new File(m_dir).exists()) {
            try {
                m_fis = new FileInputStream(m_dir);
            } catch (FileNotFoundException fnfe) {
                log.warn("File '" + m_dir + "' not found. Message is : " + fnfe.getMessage());
            }
            try {
                m_ois = new ObjectInputStream(m_fis);
            } catch (StreamCorruptedException sce) {
                (new File(m_dir)).delete();
                m_dirList = new Vector();
            } catch (IOException ioe) {
                log.warn("File '" + m_dir + "' input/output error. Message is : " + ioe.getMessage());
                m_dirList = new Vector();
            }
            try {
                if (m_ois != null) {
                    m_dirList = (Vector) m_ois.readObject();
                    if (m_dirList == null) m_dirList = new Vector();
                } else m_dirList = new Vector();
            } catch (OptionalDataException ode) {
                log.warn("File '" + m_dir + "' does not contain object.");
                m_dirList = new Vector();
            } catch (IOException ioe) {
                log.warn("File '" + m_dir + "' input/output error. Message is : " + ioe.getMessage());
                m_dirList = new Vector();
            } catch (ClassNotFoundException cnfe) {
                log.warn("ClassNotFoundException thrown. Message is : " + cnfe.getMessage());
                m_dirList = new Vector();
            }
            try {
                if (m_ois != null) m_ois.close();
                m_fis.close();
            } catch (IOException ioe) {
                log.warn("File '" + m_dir + "' input/output error. Message is : " + ioe.getMessage());
            }
        } else m_dirList = new Vector();
        setMultiSelectionEnabled(false);
        m_historyAndPreviewPanel = new HistoryAndPreviewPanel();
        setAccessory(m_historyAndPreviewPanel);
        addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) {
                    m_previewer.clear();
                }
                if (e.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
                    File f = (File) e.getNewValue();
                    if (f != null && f.isFile()) {
                        m_previewer.showFileContents(f);
                        addDirectory(f);
                    }
                }
            }
        });
    }

    public JooneFileChooser() {
        this(".");
    }

    /** saving the history of the directory entries */
    public void saveDirectoryEntries() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(m_dir);
        } catch (FileNotFoundException fnfe) {
            log.warn("File '" + m_dir + "' not found.", fnfe);
        }
        try {
            oos = new ObjectOutputStream(fos);
            oos.writeObject(m_dirList);
            oos.flush();
            oos.close();
            if (fos != null) {
                fos.close();
            }
        } catch (IOException ioe) {
            log.warn("File '" + m_dir + "' input/output error. Message is : " + ioe.getMessage(), ioe);
        }
    }

    /**
     * Saving directory into directory list.
     *
     * @param	p_file	the directory name that is to be added to the directory
     *			list. The directory will be move to front of the list if
     *			it appeals in the previous directory history, else it
     *			is added to the front of the list.
     */
    private void addDirectory(File p_file) {
        if (p_file == null || p_file.getName().equals("")) return;
        String absolutePath = p_file.getAbsolutePath();
        int posDirSep = absolutePath.lastIndexOf(System.getProperty("file.separator"));
        String d = absolutePath.substring(0, posDirSep);
        m_dirList.removeElement(d);
        m_dirList.add(0, d);
        updateJComboBox();
    }

    /** updating the values in the JComboBox */
    private void updateJComboBox() {
        m_comboBox.revalidate();
        m_comboBox.setSelectedIndex(0);
    }

    /** Demo application that is used for testing */
    public static void main(String[] args) {
        final JFrame f = new JFrame("Joone FileChooser Demo");
        final JooneFileChooser fc = new JooneFileChooser();
        f.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                fc.saveDirectoryEntries();
                f.setVisible(false);
                f.dispose();
                System.exit(0);
            }
        });
        JMenuBar mb = new JMenuBar();
        f.setJMenuBar(mb);
        JMenu m = new JMenu("File");
        m.setMnemonic('F');
        mb.add(m);
        JMenuItem o = new JMenuItem("Open...");
        o.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fc.setCurrentDirectory(new File("."));
                String filename = e.toString();
                int val = fc.showOpenDialog(f);
                if (val == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if (file != null && !file.getName().equals("")) filename = file.getName();
                }
            }
        });
        m.add(o);
        f.pack();
        f.setSize(310, 130);
        f.setVisible(true);
    }

    /**
     * HistoryAndPreviewPanel class is used to create a ComboBox,
     * which store the directory history and a textArea, which
     * is used to preview the content of the file.
     */
    private final class HistoryAndPreviewPanel extends JPanel {

        private static final long serialVersionUID = 8111982139735281484L;

        public HistoryAndPreviewPanel() {
            setPreferredSize(new Dimension(250, 250));
            setBorder(BorderFactory.createEtchedBorder());
            setLayout(new BorderLayout());
            m_comboBox = new JComboBox(m_dirList);
            m_comboBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String dir = (String) m_comboBox.getSelectedItem();
                    JooneFileChooser.this.setCurrentDirectory(new File(dir));
                    JLabel label = new JLabel(dir);
                    label.setFont(m_comboBox.getFont());
                    if (label.getPreferredSize().width > m_comboBox.getSize().width) m_comboBox.setToolTipText(dir); else m_comboBox.setToolTipText(null);
                }
            });
            add(m_comboBox, BorderLayout.NORTH);
            m_previewer = new TextPreviewer();
            add(m_previewer, BorderLayout.CENTER);
        }
    }

    /**
     * TextPreviewer class is used to view the content of a file in the
     * JFileChooser.
     */
    private class TextPreviewer extends JComponent {

        private JTextArea m_textArea = new JTextArea();

        private JScrollPane m_scroller = new JScrollPane(m_textArea);

        private char[] m_buf = new char[500];

        private static final long serialVersionUID = -1771309681222930860L;

        /** Creating a textArea and scrollePane */
        public TextPreviewer() {
            m_textArea.setEditable(false);
            setLayout(new BorderLayout());
            add(m_scroller, BorderLayout.CENTER);
        }

        /**
         * Displaying the content of the file in the textArea
         *
         * @param	p_file	the file that is to be display in the textArea.
         */
        public void showFileContents(File p_file) {
            m_textArea.setText(readFile(p_file));
            m_textArea.setCaretPosition(0);
        }

        /** clear the testArea */
        public void clear() {
            m_textArea.setText("");
        }

        /**
         * Reading the content of the file.
         *
         * @param	p_file	the content of the file that is to be read.
         * @return	the content of the file.
         */
        private String readFile(File p_file) {
            String str = null;
            FileReader reader = null;
            try {
                reader = new FileReader(p_file);
            } catch (FileNotFoundException fnfe) {
                log.warn("File '" + p_file + "' not found.", fnfe);
                return str;
            }
            try {
                int nch = reader.read(m_buf, 0, m_buf.length);
                if (nch != -1) str = new String(m_buf, 0, nch);
                reader.close();
            } catch (IOException ioe) {
                log.warn("File '" + m_dir + "' input/output error.", ioe);
            }
            return str;
        }
    }
}
