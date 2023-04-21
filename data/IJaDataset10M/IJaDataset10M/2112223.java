package ru.concretesoft.concretesplitviewer;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author  Mytinski Leonid
 *
 * Основное окно программы.
 * Содержит две кнопки, для открытия "сплитов"; два списка: один для выбора группы, другой для выбора спортсменов;
 * поле для отображения графиков.
 */
public class Main extends javax.swing.JFrame {

    private JFileChooser jFC;

    private LapsTopPanel lapsTopPanel;

    private GroupListModel groupListModel;

    private AthleteListModel athletesModel;

    private GroupSelectionModel groupSelectionModel;

    private SplitViewer[] viewers = new SplitViewer[] { new SecondBestSplitViewer(), new StandardSplitViewer() };

    private File propertiesFile;

    private Properties properties;

    private Collection<String> namesOfReaders;

    private URLClassLoader classLoader;

    private static final String PROPERTIES_FILE_NAME = "properies.xml";

    private static final String DIRECTORY_NAME = ".ConcreteSplitViewer";

    /** Creates new form Main */
    public Main() {
        loadProperties();
        initComponents();
        namesOfReaders = getReaders();
        jFC = new JFileChooser();
        File currentDirectory = new File(properties.getProperty("Directory"));
        if (currentDirectory.exists()) {
            jFC.setCurrentDirectory(currentDirectory);
        }
        groupListModel = new GroupListModel();
        athletesModel = new AthleteListModel(getGraphics().getFontMetrics());
        athletesModel.setGroupsList(jList1);
        lapsTopPanel = new LapsTopPanel(athletesModel, viewers[0]);
        lapsTopPanel.setPreferredSize(new Dimension(100, 20));
        lapsTopPanel.setAlignmentX(1.0f);
        for (int i = 0; i < viewers.length; i++) {
            jComboBox1.addItem(viewers[i]);
            viewers[i].setModel(athletesModel);
        }
        jList1.setModel(groupListModel);
        groupSelectionModel = new GroupSelectionModel(groupListModel);
        groupSelectionModel.addListSelectionListener(athletesModel);
        groupListModel.addListDataListener(groupSelectionModel);
        jList1.setSelectionModel(groupSelectionModel);
        jList2.setModel(athletesModel);
        jList2.setSelectionModel(athletesModel);
        String typeOfLastFile = properties.getProperty("Type_of_last_file");
        File lastFile;
        try {
            lastFile = new File(properties.getProperty("The_Last_File"));
        } catch (java.lang.NullPointerException e) {
            System.out.println("There isn't Last [used] File Name in property. Assume null file name and user should open file manually.");
            lastFile = null;
        }
        if ((lastFile != null) && (lastFile.exists())) {
            try {
                SplitReader splitReader = null;
                SplitReaderWraper splitWraper = new SplitReaderWraper(lastFile, namesOfReaders.toArray(new String[0]), classLoader);
                splitReader = splitWraper.createSplitReader();
                if (splitReader != null) readSplits(splitReader); else JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("ru/concretesoft/concretesplitviewer/i18on").getString("File_Open_Error_Message"), lastFile.getName(), JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else ;
    }

    private String getDefaultReadersDirectory() {
        return Main.class.getResource("/").getFile() + "readers";
    }

    private List<String> getReaders() {
        List<String> names = new ArrayList<String>();
        try {
            List<URL> urls = new ArrayList<URL>();
            urls.add(new URL("file:/"));
            String readersDir = properties.getProperty("ReadersDirectory");
            if (readersDir == null) {
                readersDir = getDefaultReadersDirectory();
                properties.setProperty("ReadersDirectory", readersDir);
                try {
                    saveProperties();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            File directory = new File(readersDir);
            if (directory.isDirectory()) {
                File[] files = directory.listFiles();
                for (File file : files) {
                    if (file.isFile()) {
                        try {
                            urls.add(file.toURI().toURL());
                            JarFile jarFile = new JarFile(file);
                            Manifest manif = jarFile.getManifest();
                            Map<String, Attributes> map = manif.getEntries();
                            Attributes attr = manif.getMainAttributes();
                            if (attr != null) {
                                String namesAll = attr.getValue("Readers");
                                String[] namesS = namesAll.split(" ");
                                for (String n : namesS) {
                                    names.add(n);
                                }
                            }
                        } catch (java.util.zip.ZipException ze) {
                            System.out.println("File isn't looks like jar-file: " + file);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            classLoader = new URLClassLoader(urls.toArray(new URL[0]));
            names.add("ru.concretesoft.concretesplitviewer.OCT2007Reader");
            names.add("ru.concretesoft.concretesplitviewer.OSVReader");
            names.add("ru.concretesoft.concretesplitviewer.SFReader");
        } catch (MalformedURLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return names;
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        jComboBox1 = new javax.swing.JComboBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("ru/concretesoft/concretesplitviewer/i18on");
        setTitle(bundle.getString("ConcreteSplitViewer"));
        getContentPane().setLayout(new java.awt.GridBagLayout());
        jSplitPane1.setResizeWeight(0.9);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 200));
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);
        jPanel2.add(jScrollPane1);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(200, 200));
        jList2.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });
        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);
        jPanel2.add(jScrollPane2);
        jButton3.setText(bundle.getString("set_different_colors"));
        jButton3.setAlignmentX(0.5F);
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);
        jSplitPane1.setRightComponent(jPanel2);
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 400));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setLeftComponent(jPanel1);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jSplitPane1, gridBagConstraints);
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));
        jButton1.setText(bundle.getString("Open"));
        jButton1.setToolTipText(bundle.getString("Open_file"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jPanel3.add(jToolBar1);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jToolBar2.add(jComboBox1);
        jPanel3.add(jToolBar2);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(jPanel3, gridBagConstraints);
        jMenu1.setText(bundle.getString("File"));
        jMenuItem1.setText(bundle.getString("Open_file"));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenu1.add(jSeparator2);
        jMenu1.add(jSeparator1);
        jMenuItem3.setText(bundle.getString("Exit"));
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);
        jMenuBar1.add(jMenu1);
        jMenu2.setText(bundle.getString("Help"));
        jMenuItem4.setText(bundle.getString("About"));
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);
        jMenuBar1.add(jMenu2);
        setJMenuBar(jMenuBar1);
        pack();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        AthleteIcon[] icons = (AthleteIcon[]) athletesModel.getSelectedValues();
        Collection<AthleteIcon> athletes = new ArrayList<AthleteIcon>();
        for (int i = 0; i < icons.length; i++) {
            athletes.add(icons[i]);
        }
        AthleteListModel.setDifferntColorsForAthletes(athletes);
        repaint();
    }

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
        JDialog aboutDialog = new AboutDialog(this);
        aboutDialog.setLocationRelativeTo(jPanel1);
        aboutDialog.setVisible(true);
    }

    /**
     * Exit from program by choosing menu item File->Exit
     *
     * @param  evt  <code>ActionEvent</code>
     */
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    /**
     * Method calls when chose menu item File->"Open OSV"
     * Метод, вызываемый по выбору пункта меню (File)Файл->(Open OSV)Открыть OSV
     *
     * Call method jButton1ActionPerformed
     * Показывает диалоговое окно выбора файла, и если файл выбран, то читает этот файл
     *
     * @param  evt <code>ActionEvent</code>
     * @see jButton1ActionPerformed(java.awt.event.ActionEvent evt)
     */
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        openFileWithDialog();
    }

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {
        SplitViewer sV = (SplitViewer) jComboBox1.getSelectedItem();
        if ((evt.getButton() == MouseEvent.BUTTON2) || (MouseEvent.getMouseModifiersText(evt.getModifiers()).equals("Shift+Button1"))) {
            athletesModel.restoreAllSplits();
        } else if (evt.getButton() == MouseEvent.BUTTON1) {
            sV.removeSplit(evt.getX());
        }
    }

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {
        jPanel1.removeAll();
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            SplitViewer sV = (SplitViewer) evt.getItem();
            lapsTopPanel.setSplitViewer(sV);
            jPanel1.add((javax.swing.JPanel) jComboBox1.getSelectedItem(), java.awt.BorderLayout.CENTER);
            jPanel1.add(lapsTopPanel, java.awt.BorderLayout.NORTH);
            setGlassPane(sV.getGlassPane());
            this.validate();
            repaint();
        } else ;
    }

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {
    }

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {
    }

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {
    }

    /**
     * Метод, вызываемый по нажатию на кнопку, для открытия OSV сплитов
     *
     * Показывает диалоговое окно выбора файла, и если файл выбран, то читает этот файл
     *
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        openFileWithDialog();
    }

    private void openFileWithDialog() {
        Collection<String> extensions = new ArrayList<String>();
        extensions.add("osv");
        extensions.add("txt");
        File file = showFileChooser(extensions);
        if (file != null) {
            try {
                SplitReaderWraper splitWraper = new SplitReaderWraper(file, namesOfReaders.toArray(new String[0]), classLoader);
                SplitReader splitReader = splitWraper.createSplitReader();
                if (splitReader != null) {
                    properties.setProperty("Directory", file.getParent());
                    properties.setProperty("The_Last_File", file.getPath());
                    try {
                        saveProperties();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    readSplits(splitReader);
                } else {
                    JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("ru/concretesoft/concretesplitviewer/i18on").getString("File_Open_Error_Message"), file.getName(), JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else ;
    }

    private void print() {
        SplitViewer sV = (SplitViewer) jComboBox1.getSelectedItem();
        JDialog previewDialog = new PrintPreviewDialog(this, true, (JComponent) sV, jList2);
        previewDialog.setVisible(true);
    }

    /**
     * Чтение сплитов с помощью ридера
     *
     */
    private void readSplits(SplitReader sR) {
        groupListModel.setGroups(sR.getAllGroups());
        setWindowTitle(sR);
    }

    private File showFileChooser(Collection<String> exts) {
        clear();
        MyFileFilter filter = new MyFileFilter(exts);
        jFC.setFileFilter(filter);
        int val = jFC.showOpenDialog(this);
        File selectedFile = null;
        if (val == JFileChooser.APPROVE_OPTION) {
            selectedFile = jFC.getSelectedFile();
        } else ;
        jFC.removeChoosableFileFilter(filter);
        return selectedFile;
    }

    /**
     *
     * Method for loading properties from home directory
     *
     */
    private void loadProperties() {
        String path = System.getProperty("user.home");
        String separator = System.getProperty("file.separator");
        File parent = new File(path + separator + DIRECTORY_NAME);
        propertiesFile = new File(parent, PROPERTIES_FILE_NAME);
        try {
            if (parent.exists()) {
                if (propertiesFile.createNewFile()) {
                    fillDefaultProperties(propertiesFile, path, separator);
                } else {
                    properties = new Properties();
                    java.io.FileInputStream fIS = new java.io.FileInputStream(propertiesFile);
                    properties.loadFromXML(fIS);
                }
            } else {
                parent.mkdir();
                propertiesFile.createNewFile();
                fillDefaultProperties(propertiesFile, path, separator);
            }
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method for generating default properties
     *
     *
     */
    private void fillDefaultProperties(File prop, String path, String separator) throws java.io.IOException {
        properties = new Properties();
        properties.setProperty("Directory", path);
        properties.setProperty("ReadersDirectory", getDefaultReadersDirectory());
        saveProperties(prop);
    }

    /**
     * Method save properties
     *
     *
     * @throws java.io.IOException
     */
    public void saveProperties(File prop) throws java.io.IOException {
        java.io.FileOutputStream fOS = new java.io.FileOutputStream(prop);
        properties.storeToXML(fOS, "Properties");
    }

    public void saveProperties() throws java.io.IOException {
        saveProperties(propertiesFile);
    }

    /** "Обнуление" всех параметров оставшихся от предыдущего файла
     *
     */
    private void clear() {
        athletesModel.setAthletes(null);
        athletesModel.setViewSplits(null);
        jList1.clearSelection();
        jList1.ensureIndexIsVisible(0);
    }

    private void setWindowTitle(SplitReader sR) {
        this.setTitle(java.util.ResourceBundle.getBundle("ru/concretesoft/concretesplitviewer/i18on").getString("ConcreteSplitViewer") + " " + sR.getFileName() + " " + sR.getEventDescription());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton3;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JList jList1;

    private javax.swing.JList jList2;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem3;

    private javax.swing.JMenuItem jMenuItem4;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSplitPane jSplitPane1;

    private javax.swing.JToolBar jToolBar1;

    private javax.swing.JToolBar jToolBar2;
}
