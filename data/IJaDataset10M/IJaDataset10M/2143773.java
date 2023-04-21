package net.maizegenetics.baseplugins;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.maizegenetics.pal.alignment.Alignment;
import net.maizegenetics.pal.alignment.AlignmentMask;
import net.maizegenetics.pal.alignment.ExportUtils;
import net.maizegenetics.pal.alignment.Phenotype;
import net.maizegenetics.pal.alignment.PhenotypeUtils;
import net.maizegenetics.plugindef.AbstractPlugin;
import net.maizegenetics.plugindef.DataSet;
import net.maizegenetics.plugindef.Datum;
import net.maizegenetics.prefs.TasselPrefs;
import net.maizegenetics.pal.report.TableReport;
import net.maizegenetics.pal.report.TableReportUtils;
import net.maizegenetics.gui.DialogUtils;
import net.maizegenetics.util.ExceptionUtils;
import net.maizegenetics.util.Utils;
import net.maizegenetics.pal.distance.DistanceMatrix;
import net.maizegenetics.pal.distance.WriteDistanceMatrix;
import net.maizegenetics.tassel.TASSELMainFrame;
import javax.swing.*;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;

/**
 *
 * @author terry
 */
public class ExportPlugin extends AbstractPlugin {

    private static final Logger myLogger = Logger.getLogger(ExportPlugin.class);

    private FileLoadPlugin.TasselFileType myFileType = FileLoadPlugin.TasselFileType.Hapmap;

    private String mySaveFile = null;

    private boolean myIsDiploid = false;

    /** Creates a new instance of ExportPlugin */
    public ExportPlugin(Frame parentFrame, boolean isInteractive) {
        super(parentFrame, isInteractive);
    }

    public DataSet performFunction(DataSet input) {
        try {
            if (input.getSize() != 1) {
                String message = "Please select one and only one item.";
                if (isInteractive()) {
                    JOptionPane.showMessageDialog(getParentFrame(), message);
                } else {
                    myLogger.error("performFunction: " + message);
                }
                return null;
            }
            try {
                Object data = input.getData(0).getData();
                if (data instanceof Alignment) {
                    performFunctionForAlignment((Alignment) data);
                    return null;
                } else if (data instanceof Phenotype) {
                    performFunctionForPhenotype((Phenotype) data);
                    return null;
                } else if (data instanceof DistanceMatrix) {
                    performFunctionForDistanceMatrix((DistanceMatrix) data);
                    return null;
                } else if (data instanceof TableReport) {
                    performFunctionForTableReport((TableReport) data);
                    return null;
                } else {
                    String message = "Don't know how to export data type: " + data.getClass().getName();
                    if (isInteractive()) {
                        JOptionPane.showMessageDialog(getParentFrame(), message);
                    } else {
                        myLogger.error("performFunction: " + message);
                    }
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                StringBuilder builder = new StringBuilder();
                builder.append(Utils.shortenStrLineLen(ExceptionUtils.getExceptionCauses(e), 50));
                String str = builder.toString();
                if (isInteractive()) {
                    DialogUtils.showError(str, getParentFrame());
                } else {
                    myLogger.error(str);
                }
                return null;
            }
        } finally {
            fireProgress(100);
        }
    }

    public void performFunctionForDistanceMatrix(DistanceMatrix input) {
        if (isInteractive()) {
            setSaveFile(getFileByChooser());
        }
        if ((mySaveFile == null) || (mySaveFile.length() == 0)) {
            return;
        }
        try {
            File theFile = new File(Utils.addSuffixIfNeeded(mySaveFile, ".txt"));
            WriteDistanceMatrix.saveDelimitedDistanceMatrix(input, theFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("ExportPlugin: performFunctionForDistanceMatrix: Problem writing file: " + mySaveFile);
        }
    }

    public void performFunctionForTableReport(TableReport input) {
        if (isInteractive()) {
            setSaveFile(getFileByChooser());
        }
        if ((mySaveFile == null) || (mySaveFile.length() == 0)) {
            return;
        }
        try {
            File theFile = new File(Utils.addSuffixIfNeeded(mySaveFile, ".txt"));
            TableReportUtils.saveDelimitedTableReport(input, "\t", theFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("ExportPlugin: performFunctionForTableReport: Problem writing file: " + mySaveFile);
        }
    }

    public void performFunctionForPhenotype(Phenotype input) {
        if (isInteractive()) {
            setSaveFile(getFileByChooser());
        }
        if ((mySaveFile == null) || (mySaveFile.length() == 0)) {
            return;
        }
        File theFile = null;
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            theFile = new File(Utils.addSuffixIfNeeded(mySaveFile, ".txt"));
            fw = new FileWriter(theFile);
            pw = new PrintWriter(fw);
            PhenotypeUtils.saveAs(input, pw);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("ExportPlugin: performFunctionForPhenotype: Problem writing file: " + mySaveFile);
        } finally {
            try {
                pw.close();
                fw.close();
            } catch (Exception e) {
            }
        }
    }

    public void performFunctionForAlignment(Alignment inputAlignment) {
        if (isInteractive()) {
            ExportPluginDialog theDialog = new ExportPluginDialog();
            theDialog.setLocationRelativeTo(getParentFrame());
            theDialog.setVisible(true);
            if (theDialog.isCancel()) {
                return;
            }
            myFileType = theDialog.getTasselFileType();
            theDialog.dispose();
            setSaveFile(getFileByChooser());
        }
        if ((mySaveFile == null) || (mySaveFile.length() == 0)) {
            return;
        }
        if (myFileType == FileLoadPlugin.TasselFileType.Hapmap) {
            int n = 0;
            DefaultMutableTreeNode node = null;
            if (isInteractive()) {
                DiploidOptionDialog diploidDialog = new DiploidOptionDialog();
                diploidDialog.setLocationRelativeTo(getParentFrame());
                diploidDialog.setVisible(true);
                myIsDiploid = diploidDialog.getDiploid();
                node = (DefaultMutableTreeNode) ((TASSELMainFrame) this.getParentFrame()).getDataTreePanel().getTree().getLastSelectedPathComponent();
                n = node.getChildCount();
            }
            boolean foundImputed = false;
            if ((n == 0) || (!isInteractive())) {
                ExportUtils.writeToHapmap(inputAlignment, myIsDiploid, mySaveFile, '\t', this);
            } else {
                int i = 0;
                while (i < n && !foundImputed) {
                    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) node.getChildAt(i);
                    Datum currentDatum = (Datum) currentNode.getUserObject();
                    Object currentMask = currentDatum.getData();
                    if (currentMask instanceof AlignmentMask) {
                        AlignmentMask.MaskType maskType = ((AlignmentMask) currentMask).getMaskType();
                        if (maskType == AlignmentMask.MaskType.imputed) {
                            ImputeDisplayOptionDialog imputeOptionDialog = new ImputeDisplayOptionDialog();
                            imputeOptionDialog.setLocationRelativeTo(getParentFrame());
                            imputeOptionDialog.setVisible(true);
                            if (imputeOptionDialog.getDisplayImputed()) {
                                ExportUtils.writeToHapmap(inputAlignment, (AlignmentMask) currentMask, myIsDiploid, mySaveFile, '\t', this);
                                foundImputed = true;
                            } else if (i == (n - 1)) {
                                ExportUtils.writeToHapmap(inputAlignment, myIsDiploid, mySaveFile, '\t', this);
                            }
                        }
                    }
                    i++;
                }
            }
        } else if (myFileType == FileLoadPlugin.TasselFileType.Plink) {
            ExportUtils.writeToPlink(inputAlignment, mySaveFile, '\t');
        } else if (myFileType == FileLoadPlugin.TasselFileType.Flapjack) {
            ExportUtils.writeToFlapjack(inputAlignment, mySaveFile, '\t');
        } else if (myFileType == FileLoadPlugin.TasselFileType.Phylip_Seq) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(new FileWriter(Utils.addSuffixIfNeeded(mySaveFile, ".phy")));
                ExportUtils.printSequential(inputAlignment, out);
            } catch (Exception e) {
                throw new IllegalStateException("ExportPlugin: performFunction: Problem writing file: " + mySaveFile);
            } finally {
                out.flush();
                out.close();
            }
        } else if (myFileType == FileLoadPlugin.TasselFileType.Phylip_Inter) {
            PrintWriter out = null;
            try {
                out = new PrintWriter(new FileWriter(Utils.addSuffixIfNeeded(mySaveFile, ".phy")));
                ExportUtils.printInterleaved(inputAlignment, out);
            } catch (Exception e) {
                throw new IllegalStateException("ExportPlugin: performFunction: Problem writing file: " + mySaveFile);
            } finally {
                out.flush();
                out.close();
            }
        } else if (myFileType == FileLoadPlugin.TasselFileType.Table) {
            ExportUtils.saveDelimitedAlignment(inputAlignment, "\t", mySaveFile);
        } else {
            throw new IllegalStateException("ExportPlugin: performFunction: Unknown Alignment File Format: " + myFileType);
        }
    }

    /**
     * Icon for this plugin to be used in buttons, etc.
     *
     * @return ImageIcon
     */
    public ImageIcon getIcon() {
        URL imageURL = ExportPlugin.class.getResource("images/Export16.gif");
        if (imageURL == null) {
            return null;
        } else {
            return new ImageIcon(imageURL);
        }
    }

    /**
     * Button name for this plugin to be used in buttons, etc.
     *
     * @return String
     */
    public String getButtonName() {
        return "Export";
    }

    /**
     * Tool Tip Text for this plugin
     *
     * @return String
     */
    public String getToolTipText() {
        return "Export data to files on your computer.";
    }

    public String getSaveFile() {
        return mySaveFile;
    }

    public void setSaveFile(String saveFile) {
        mySaveFile = saveFile;
    }

    public void setSaveFile(File saveFile) {
        if (saveFile == null) {
            mySaveFile = null;
        } else {
            mySaveFile = saveFile.getPath();
        }
    }

    public void setAlignmentFileType(FileLoadPlugin.TasselFileType type) {
        myFileType = type;
    }

    public void setIsDiploid(boolean isDiploid) {
        myIsDiploid = isDiploid;
    }

    private File getFileByChooser() {
        JFileChooser fileSave = new JFileChooser(TasselPrefs.getSaveDir());
        fileSave.setMultiSelectionEnabled(false);
        File result = null;
        int returnVal = fileSave.showSaveDialog(getParentFrame());
        if (returnVal == JFileChooser.SAVE_DIALOG || returnVal == JFileChooser.APPROVE_OPTION) {
            result = fileSave.getSelectedFile();
            TasselPrefs.putSaveDir(fileSave.getCurrentDirectory().getPath());
        }
        return result;
    }

    class ExportPluginDialog extends JDialog {

        private boolean myIsCancel = true;

        private ButtonGroup myButtonGroup = new ButtonGroup();

        private JRadioButton myHapMapRadioButton = new JRadioButton("Write Hapmap");

        private JRadioButton myPlinkRadioButton = new JRadioButton("Write Plink");

        private JRadioButton myFlapjackRadioButton = new JRadioButton("Write Flapjack");

        private JRadioButton myPhylipRadioButton = new JRadioButton("Write Phylip (Sequential)");

        private JRadioButton myPhylipInterRadioButton = new JRadioButton("Write Phylip (Interleaved)");

        private JRadioButton myTabTableRadioButton = new JRadioButton("Write Tab Delimited");

        public ExportPluginDialog() {
            super((Frame) null, "Export...", true);
            try {
                jbInit();
                pack();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void jbInit() throws Exception {
            setTitle("Export...");
            setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            setUndecorated(false);
            getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            Container contentPane = getContentPane();
            BoxLayout layout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
            contentPane.setLayout(layout);
            JPanel main = getMain();
            contentPane.add(main);
            pack();
            setResizable(false);
            myButtonGroup.add(myHapMapRadioButton);
            myButtonGroup.add(myPlinkRadioButton);
            myButtonGroup.add(myFlapjackRadioButton);
            myButtonGroup.add(myPhylipRadioButton);
            myButtonGroup.add(myPhylipInterRadioButton);
            myButtonGroup.add(myTabTableRadioButton);
            myHapMapRadioButton.setSelected(true);
        }

        private JPanel getMain() {
            JPanel inputs = new JPanel();
            BoxLayout layout = new BoxLayout(inputs, BoxLayout.Y_AXIS);
            inputs.setLayout(layout);
            inputs.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            inputs.add(Box.createRigidArea(new Dimension(1, 10)));
            inputs.add(getLabel());
            inputs.add(Box.createRigidArea(new Dimension(1, 10)));
            inputs.add(getOptionPanel());
            inputs.add(Box.createRigidArea(new Dimension(1, 10)));
            inputs.add(getButtons());
            inputs.add(Box.createRigidArea(new Dimension(1, 10)));
            return inputs;
        }

        private JPanel getLabel() {
            JPanel result = new JPanel();
            BoxLayout layout = new BoxLayout(result, BoxLayout.Y_AXIS);
            result.setLayout(layout);
            result.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            JLabel jLabel1 = new JLabel("Choose File Type to Export.");
            jLabel1.setFont(new Font("Dialog", Font.BOLD, 18));
            result.add(jLabel1);
            return result;
        }

        private JPanel getOptionPanel() {
            JPanel result = new JPanel();
            BoxLayout layout = new BoxLayout(result, BoxLayout.Y_AXIS);
            result.setLayout(layout);
            result.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            result.setBorder(BorderFactory.createEtchedBorder());
            result.add(myHapMapRadioButton);
            result.add(myPlinkRadioButton);
            result.add(myFlapjackRadioButton);
            result.add(myPhylipRadioButton);
            result.add(myPhylipInterRadioButton);
            result.add(myTabTableRadioButton);
            result.add(Box.createRigidArea(new Dimension(1, 20)));
            return result;
        }

        private JPanel getButtons() {
            JButton okButton = new JButton();
            JButton cancelButton = new JButton();
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    cancelButton_actionPerformed(e);
                }
            });
            okButton.setText("OK");
            okButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    okButton_actionPerformed(e);
                }
            });
            JPanel result = new JPanel(new FlowLayout(FlowLayout.CENTER));
            result.add(okButton);
            result.add(cancelButton);
            return result;
        }

        public FileLoadPlugin.TasselFileType getTasselFileType() {
            if (myHapMapRadioButton.isSelected()) {
                return FileLoadPlugin.TasselFileType.Hapmap;
            }
            if (myPlinkRadioButton.isSelected()) {
                return FileLoadPlugin.TasselFileType.Plink;
            }
            if (myFlapjackRadioButton.isSelected()) {
                return FileLoadPlugin.TasselFileType.Flapjack;
            }
            if (myPhylipRadioButton.isSelected()) {
                return FileLoadPlugin.TasselFileType.Phylip_Seq;
            }
            if (myPhylipInterRadioButton.isSelected()) {
                return FileLoadPlugin.TasselFileType.Phylip_Inter;
            }
            if (myTabTableRadioButton.isSelected()) {
                return FileLoadPlugin.TasselFileType.Table;
            }
            return null;
        }

        private void okButton_actionPerformed(ActionEvent e) {
            myIsCancel = false;
            setVisible(false);
        }

        private void cancelButton_actionPerformed(ActionEvent e) {
            myIsCancel = true;
            setVisible(false);
        }

        public boolean isCancel() {
            return myIsCancel;
        }
    }
}

class ImputeDisplayOptionDialog extends JDialog {

    boolean displayImputed = true;

    private JPanel mainPanel = new JPanel();

    private JLabel lbl = new JLabel();

    private JButton yesButton = new JButton();

    private JButton noButton = new JButton();

    private GridBagLayout gridBagLayout = new GridBagLayout();

    public ImputeDisplayOptionDialog() {
        super((Frame) null, "File Loader", true);
        try {
            initUI();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void initUI() throws Exception {
        lbl.setFont(new java.awt.Font("Dialog", 1, 12));
        lbl.setText("Would you like Imputed data to be exported in lower case?");
        mainPanel.setMinimumSize(new Dimension(480, 150));
        mainPanel.setPreferredSize(new Dimension(480, 150));
        mainPanel.setLayout(gridBagLayout);
        yesButton.setMaximumSize(new Dimension(63, 27));
        yesButton.setMinimumSize(new Dimension(63, 27));
        yesButton.setText("Yes");
        yesButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                yesButton_actionPerformed(e);
            }
        });
        noButton.setMaximumSize(new Dimension(63, 27));
        noButton.setMinimumSize(new Dimension(63, 27));
        noButton.setText("No");
        noButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                noButton_actionPerformed(e);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        mainPanel.add(lbl, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
        mainPanel.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.PAGE_END, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void yesButton_actionPerformed(ActionEvent e) {
        displayImputed = true;
        setVisible(false);
    }

    private void noButton_actionPerformed(ActionEvent e) {
        displayImputed = false;
        setVisible(false);
    }

    public boolean getDisplayImputed() {
        return displayImputed;
    }
}

class DiploidOptionDialog extends JDialog {

    boolean displayDiploid = true;

    private JPanel mainPanel = new JPanel();

    private JLabel lbl = new JLabel();

    private JButton yesButton = new JButton();

    private JButton noButton = new JButton();

    private GridBagLayout gridBagLayout = new GridBagLayout();

    public DiploidOptionDialog() {
        super((Frame) null, "File Loader", true);
        try {
            initUI();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void initUI() throws Exception {
        lbl.setFont(new java.awt.Font("Dialog", 1, 12));
        lbl.setText("Would you like SNPs to be exported as Diploids?");
        mainPanel.setMinimumSize(new Dimension(480, 150));
        mainPanel.setPreferredSize(new Dimension(480, 150));
        mainPanel.setLayout(gridBagLayout);
        yesButton.setMaximumSize(new Dimension(63, 27));
        yesButton.setMinimumSize(new Dimension(63, 27));
        yesButton.setText("Yes");
        yesButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                yesButton_actionPerformed(e);
            }
        });
        noButton.setMaximumSize(new Dimension(63, 27));
        noButton.setMinimumSize(new Dimension(63, 27));
        noButton.setText("No");
        noButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                noButton_actionPerformed(e);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        mainPanel.add(lbl, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
        mainPanel.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.PAGE_END, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
        this.add(mainPanel, BorderLayout.CENTER);
    }

    private void yesButton_actionPerformed(ActionEvent e) {
        displayDiploid = true;
        setVisible(false);
    }

    private void noButton_actionPerformed(ActionEvent e) {
        displayDiploid = false;
        setVisible(false);
    }

    public boolean getDiploid() {
        return displayDiploid;
    }
}
