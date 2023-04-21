package joelib2.gui.example;

import joelib2.gui.util.JLog4JPanel;
import joelib2.gui.util.MolFileChooser;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import org.apache.log4j.Category;

/**
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.6 $, $Date: 2005/02/17 16:48:32 $
 */
public class SimpleGUIExample extends javax.swing.JFrame {

    private static Category logger = Category.getInstance(SimpleGUIExample.class.getName());

    private static final String iconLocation = "joelib2/data/images/joe_bws.gif";

    private javax.swing.JTabbedPane applicationsTabs;

    private javax.swing.JPanel convertPanel;

    private javax.swing.JPanel filePanel;

    private javax.swing.JTabbedPane infoPanel;

    private javax.swing.JTextField inputFile;

    private javax.swing.JLabel inputFileLabel;

    private javax.swing.JPanel inputFilePanel;

    private JLog4JPanel log4JFrame;

    private javax.swing.JTextField outputFile;

    private javax.swing.JLabel outputFileLabel;

    private javax.swing.JPanel outputFilePanel;

    private javax.swing.JButton process;

    private javax.swing.JButton selectInputFile;

    private javax.swing.JButton selectOutputFile;

    private javax.swing.JButton selectSmartsFile;

    private javax.swing.JTextField smartsFile;

    private javax.swing.JLabel smartsFileLabel;

    private javax.swing.JPanel smartsFilePanel;

    private javax.swing.JPanel statisticPanel;

    private SimpleGUIExample() {
    }

    /**
    *  The main program for the ConvertSkip class
    *
    * @param args  The command line arguments
    */
    public static void main(String[] args) {
        SimpleGUIExample joelibGUI = new SimpleGUIExample();
        joelibGUI.initComponents();
        joelibGUI.show();
    }

    void process_actionPerformed(ActionEvent e) {
        if (logger.isDebugEnabled()) {
            logger.debug(e);
        }
        switch(applicationsTabs.getSelectedIndex()) {
            case 0:
                logger.info("Process file conversion");
                ((ConvertPanel) convertPanel).startConvert(inputFile.getText(), outputFile.getText());
                break;
            case 1:
                logger.info("Process statistic");
                ((StatisticPanel) statisticPanel).startStatistic(inputFile.getText(), outputFile.getText());
                break;
        }
    }

    void selectInputFile_actionPerformed(ActionEvent e) {
        if (logger.isDebugEnabled()) {
            logger.debug(e);
        }
        MolFileChooser fileChooser = MolFileChooser.instance();
        JFileChooser load = fileChooser.getLoadFileChooser();
        int returnVal = load.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            inputFile.setText(load.getSelectedFile().getAbsolutePath());
        }
    }

    void selectOutputFile_actionPerformed(ActionEvent e) {
        if (logger.isDebugEnabled()) {
            logger.debug(e);
        }
        MolFileChooser fileChooser = MolFileChooser.instance();
        JFileChooser save = fileChooser.getSaveFileChooser();
        int returnVal = save.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            outputFile.setText(save.getSelectedFile().getAbsolutePath());
        }
    }

    void selectSmartsFile_actionPerformed(ActionEvent e) {
        if (logger.isDebugEnabled()) {
            logger.debug(e);
        }
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            smartsFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void exitForm(java.awt.event.WindowEvent evt) {
        if (logger.isDebugEnabled()) {
            logger.debug(evt);
        }
        System.exit(0);
    }

    private void initComponents() {
        log4JFrame = new JLog4JPanel();
        logger.info("Welcome to the JOELib2-GUI !");
        logger.info("CVS$Revision: 1.6 $".replace('$', ' '));
        logger.info("CVS$Date: 2005/02/17 16:48:32 $".replace('$', ' '));
        logger.info("License: GNU GPL");
        logger.info("-----------------------------");
        filePanel = new javax.swing.JPanel();
        inputFilePanel = new javax.swing.JPanel();
        inputFileLabel = new javax.swing.JLabel();
        inputFile = new javax.swing.JTextField();
        selectInputFile = new javax.swing.JButton();
        outputFilePanel = new javax.swing.JPanel();
        outputFileLabel = new javax.swing.JLabel();
        outputFile = new javax.swing.JTextField();
        selectOutputFile = new javax.swing.JButton();
        smartsFilePanel = new javax.swing.JPanel();
        smartsFileLabel = new javax.swing.JLabel();
        smartsFile = new javax.swing.JTextField();
        selectSmartsFile = new javax.swing.JButton();
        process = new javax.swing.JButton();
        applicationsTabs = new javax.swing.JTabbedPane();
        convertPanel = new ConvertPanel();
        statisticPanel = new StatisticPanel();
        infoPanel = new InfoPanel();
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        this.setTitle("Primitive JOELib GUI example");
        JOELibTestGUIActionListener frameAction = new JOELibTestGUIActionListener();
        filePanel.setLayout(new java.awt.GridLayout(4, 0));
        URL icon = this.getClass().getClassLoader().getResource(iconLocation);
        if (icon == null) {
            logger.error("Icon not found at " + iconLocation);
        } else {
            setIconImage(Toolkit.getDefaultToolkit().createImage(icon));
        }
        String path = "/joelib2/data/images/openFile.gif";
        URL dummyURL = this.getClass().getResource(path);
        ImageIcon openFileIcon = null;
        if (dummyURL != null) {
            openFileIcon = new ImageIcon(dummyURL);
            openFileIcon.setImage(openFileIcon.getImage().getScaledInstance(64, 19, Image.SCALE_AREA_AVERAGING));
        } else {
            logger.error("Could not load icon " + path);
        }
        path = "/joelib2/data/images/closeFile.gif";
        dummyURL = this.getClass().getResource(path);
        ImageIcon closeFileIcon = null;
        if (dummyURL != null) {
            closeFileIcon = new ImageIcon(dummyURL);
            closeFileIcon.setImage(closeFileIcon.getImage().getScaledInstance(64, 19, Image.SCALE_AREA_AVERAGING));
        } else {
            logger.error("Could not load icon " + path);
        }
        path = "/joelib2/data/images/smarts.gif";
        dummyURL = this.getClass().getResource(path);
        ImageIcon smartsFileIcon = null;
        if (dummyURL != null) {
            smartsFileIcon = new ImageIcon(dummyURL);
            smartsFileIcon.setImage(smartsFileIcon.getImage().getScaledInstance(64, 19, Image.SCALE_AREA_AVERAGING));
        } else {
            logger.error("Could not load icon " + path);
        }
        path = "/joelib2/data/images/process.gif";
        dummyURL = this.getClass().getResource(path);
        ImageIcon processIcon = null;
        if (dummyURL != null) {
            processIcon = new ImageIcon(dummyURL);
        } else {
            logger.error("Could not load icon " + path);
        }
        Dimension labelMinDim = new Dimension(75, 28);
        inputFileLabel.setPreferredSize(labelMinDim);
        inputFileLabel.setMaximumSize(labelMinDim);
        inputFileLabel.setMinimumSize(labelMinDim);
        inputFileLabel.setText("Input file: ");
        inputFilePanel.add(inputFileLabel);
        inputFile.setText("");
        Dimension textFieldMinDim = new Dimension(300, 28);
        inputFile.setPreferredSize(textFieldMinDim);
        inputFile.setMinimumSize(textFieldMinDim);
        inputFile.setMaximumSize(textFieldMinDim);
        inputFilePanel.add(inputFile);
        if (openFileIcon != null) {
            selectInputFile.setIcon(openFileIcon);
        } else {
            selectInputFile.setText("Input");
        }
        selectInputFile.setToolTipText("Select input file");
        inputFilePanel.add(selectInputFile);
        selectInputFile.addActionListener(frameAction);
        filePanel.add(inputFilePanel);
        outputFileLabel.setPreferredSize(labelMinDim);
        outputFileLabel.setMaximumSize(labelMinDim);
        outputFileLabel.setMinimumSize(labelMinDim);
        outputFileLabel.setText("Output file:");
        outputFilePanel.add(outputFileLabel);
        outputFile.setText("");
        outputFile.setPreferredSize(textFieldMinDim);
        outputFile.setMinimumSize(textFieldMinDim);
        outputFile.setMaximumSize(textFieldMinDim);
        outputFilePanel.add(outputFile);
        if (openFileIcon != null) {
            selectOutputFile.setIcon(closeFileIcon);
        } else {
            selectOutputFile.setText("Output");
        }
        selectOutputFile.setToolTipText("Select output file");
        selectOutputFile.addActionListener(frameAction);
        outputFilePanel.add(selectOutputFile);
        filePanel.add(outputFilePanel);
        smartsFileLabel.setPreferredSize(labelMinDim);
        smartsFileLabel.setMaximumSize(labelMinDim);
        smartsFileLabel.setMinimumSize(labelMinDim);
        smartsFileLabel.setText("SMARTS file:");
        smartsFilePanel.add(smartsFileLabel);
        smartsFile.setText("");
        smartsFile.setPreferredSize(textFieldMinDim);
        smartsFile.setMinimumSize(textFieldMinDim);
        smartsFile.setMaximumSize(textFieldMinDim);
        smartsFilePanel.add(smartsFile);
        if (smartsFileIcon != null) {
            selectSmartsFile.setIcon(smartsFileIcon);
        } else {
            selectSmartsFile.setText("SMARTS");
        }
        selectSmartsFile.setToolTipText("Select SMARTS file");
        selectSmartsFile.addActionListener(frameAction);
        smartsFilePanel.add(selectSmartsFile);
        if (processIcon != null) {
            process.setIcon(processIcon);
        } else {
            process.setText("Process");
        }
        process.setToolTipText("Process selected application");
        process.addActionListener(frameAction);
        filePanel.add(process);
        getContentPane().add(filePanel, java.awt.BorderLayout.NORTH);
        applicationsTabs.addTab("Convert", convertPanel);
        applicationsTabs.addTab("Statistic", statisticPanel);
        applicationsTabs.addTab("Info", infoPanel);
        applicationsTabs.addTab("Logging", log4JFrame);
        getContentPane().add(applicationsTabs, java.awt.BorderLayout.CENTER);
        pack();
    }

    class JOELibTestGUIActionListener implements java.awt.event.ActionListener {

        public void actionPerformed(ActionEvent event) {
            Object object = event.getSource();
            if (object == selectInputFile) {
                selectInputFile_actionPerformed(event);
            } else if (object == selectOutputFile) {
                selectOutputFile_actionPerformed(event);
            } else if (object == selectSmartsFile) {
                selectSmartsFile_actionPerformed(event);
            } else if (object == process) {
                process_actionPerformed(event);
            }
        }
    }
}
