package gui.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import opt.Options;
import gui.dialogsv2.ChooserFile;
import gui.tree.TreeObject;
import java.util.Vector;

@SuppressWarnings("rawtypes")
public class CompareSynchOptsDialog extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1765797023585121921L;

    public boolean cancel = false;

    private Options options;

    private Vector List3NF;

    private Vector ListVault;

    private Vector ListStage;

    private Vector ListStar;

    private Vector ListMDM;

    private Vector ListExp;

    public TreeObject selectedSourceModel;

    public TreeObject selectedTargetModel;

    private JPanel contentPane;

    private JButton ButtonCancel;

    private JButton ButtonOk;

    private JPanel PanelButtons;

    private JLabel LabelPickModel1;

    private JLabel LabelPickModel2;

    private JLabel LabelPickModelType;

    private JLabel LabelTargetFile;

    private JTextField TextFieldReportFile;

    private JComboBox ComboModel1;

    private JComboBox ComboModel2;

    private JComboBox ComboModelType;

    private JButton ButtonBrowse;

    private JPanel PanelFields;

    public CompareSynchOptsDialog(Frame w, Options iOpts) {
        super(w);
        options = iOpts;
        initializeComponent(w);
        setOptions(iOpts);
        if (this.cancel == false) this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    private void setOptions(Options iOpts) {
        TreeObject root;
        String curType;
        int totalCount = 0;
        this.cancel = false;
        List3NF = new Vector();
        ListStage = new Vector();
        ListVault = new Vector();
        ListStar = new Vector();
        ListMDM = new Vector();
        ListExp = new Vector();
        root = (TreeObject) options.globalTreeList.getRootNode();
        java.util.Enumeration treeEnum = root.children();
        while (treeEnum.hasMoreElements()) {
            TreeObject node = (TreeObject) treeEnum.nextElement();
            TreeObject subObj = node;
            if (subObj.classType().startsWith("ROOT-")) {
                curType = subObj.classType();
                java.util.Enumeration subEnum = node.children();
                int childCount = node.getChildCount();
                while (subEnum.hasMoreElements() && childCount > 0) {
                    TreeObject subNode = (TreeObject) subEnum.nextElement();
                    subObj = (TreeObject) subNode;
                    if (subNode != node && subObj != null) {
                        if (curType.equals("ROOT-NORMALIZED")) {
                            List3NF.add(subObj);
                            totalCount++;
                        } else if (curType.equals("ROOT-STAGING")) {
                            ListStage.add(subObj);
                            totalCount++;
                        } else if (curType.equals("ROOT-VAULT")) {
                            ListVault.add(subObj);
                            totalCount++;
                        } else if (curType.equals("ROOT-STAR")) {
                            ListStar.add(subObj);
                            totalCount++;
                        } else if (curType.equals("ROOT-EXPMART")) {
                            ListExp.add(subObj);
                            totalCount++;
                        } else if (curType.equals("ROOT-MDM")) {
                            ListMDM.add(subObj);
                            totalCount++;
                        }
                    }
                }
            }
        }
        if (totalCount == 0) {
            new showMessage(this, "Invalid Option", "No models have been loaded for comparison.");
            this.cancel = true;
        } else {
            setComboBoxes("Normalized");
        }
    }

    public void resetGlobalOpts(Options options) {
    }

    /** 
 	 * This method is called from within the constructor to initialize the form. 
 	 * WARNING: Do NOT modify this code. The content of this method is always regenerated 
 	 * by the Windows Form Designer. Otherwise, retrieving design might not work properly. 
 	 * Tip: If you must revise this method, please backup this GUI file for JFrameBuilder 
 	 * to retrieve your design properly in future, before revising this method. 
 	 */
    private void initializeComponent(Frame w) {
        contentPane = (JPanel) this.getContentPane();
        ButtonCancel = new JButton();
        ButtonOk = new JButton();
        PanelButtons = new JPanel();
        LabelPickModel1 = new JLabel();
        LabelPickModel2 = new JLabel();
        LabelPickModelType = new JLabel();
        LabelTargetFile = new JLabel();
        TextFieldReportFile = new JTextField();
        ComboModel1 = new JComboBox();
        ComboModel2 = new JComboBox();
        ComboModelType = new JComboBox();
        ButtonBrowse = new JButton();
        PanelFields = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.add(PanelButtons, BorderLayout.SOUTH);
        contentPane.add(PanelFields, BorderLayout.CENTER);
        ButtonCancel.setText("Cancel");
        ButtonCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ButtonCancel_actionPerformed(e);
            }
        });
        ButtonOk.setText("Compare");
        ButtonOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ButtonOk_actionPerformed(e);
            }
        });
        PanelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        PanelButtons.add(ButtonOk, 0);
        PanelButtons.add(ButtonCancel, 1);
        LabelPickModel1.setText("Select Source Model");
        LabelPickModel2.setText("Select Target Model");
        LabelPickModelType.setText("Pick the Model Type");
        LabelTargetFile.setText("Select Report File");
        TextFieldReportFile.setToolTipText("Full path to output report file");
        ComboModel1.setToolTipText("This is the left-hand side model of the compare");
        ComboModel2.setToolTipText("This is the right-hand side of the data model to compare");
        ComboModelType.addItem("Normalized");
        ComboModelType.addItem("Staging");
        ComboModelType.addItem("Data Vault");
        ComboModelType.addItem("Star Schema");
        ComboModelType.addItem("Exploration Mart");
        ComboModelType.addItem("Master Data Model");
        ComboModelType.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ComboModelType_actionPerformed(e);
            }
        });
        ButtonBrowse.setText("...");
        ButtonBrowse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ButtonBrowse_actionPerformed(e);
            }
        });
        PanelFields.setLayout(null);
        addComponent(PanelFields, LabelPickModel1, 4, 47, 121, 16);
        addComponent(PanelFields, LabelPickModel2, 8, 75, 119, 18);
        addComponent(PanelFields, LabelPickModelType, 9, 15, 114, 18);
        addComponent(PanelFields, TextFieldReportFile, 8, 122, 310, 22);
        addComponent(PanelFields, ComboModel1, 128, 44, 223, 22);
        addComponent(PanelFields, ComboModel2, 128, 72, 223, 22);
        addComponent(PanelFields, ComboModelType, 128, 12, 223, 22);
        addComponent(PanelFields, ButtonBrowse, 322, 119, 29, 26);
        this.setTitle("Compare and Synchronize Models");
        int width = 370;
        int height = 223;
        Dimension frameSize = w.getSize();
        Point p1 = w.getLocation();
        this.setSize(new Dimension(width, height));
        this.setLocation(p1.x + ((frameSize.width - width) / 2), p1.y + ((frameSize.height - height) / 2));
        this.setModal(true);
        this.setResizable(false);
    }

    /** Add Component Without a Layout Manager (Absolute Positioning) */
    private void addComponent(Container container, Component c, int x, int y, int width, int height) {
        c.setBounds(x, y, width, height);
        container.add(c);
    }

    private void ButtonCancel_actionPerformed(ActionEvent e) {
        this.cancel = true;
        this.setVisible(false);
    }

    private void ButtonOk_actionPerformed(ActionEvent e) {
        selectedSourceModel = (TreeObject) ComboModel1.getSelectedItem();
        selectedTargetModel = (TreeObject) ComboModel2.getSelectedItem();
        if (selectedSourceModel == null) {
            new showMessage(this, "Invalid Option", "You must choose a source data model to compare.");
            return;
        }
        if (selectedTargetModel == null) {
            new showMessage(this, "Invalid Option", "You must choose a target data model to compare.");
            return;
        }
        String rptText = this.TextFieldReportFile.getText();
        if (rptText.length() > 0) {
            options.setvRptFile(new File(rptText));
            options.setCurrentDirectoryFromFile("RPT", options.vRptFile());
        }
        this.cancel = false;
        this.setVisible(false);
    }

    private void ComboModelType_actionPerformed(ActionEvent e) {
        Object o = ComboModelType.getSelectedItem();
        setComboBoxes(o.toString());
    }

    private void setComboBoxes(String aType) {
        Vector myVector = List3NF;
        if (aType.equals("Normalized")) {
            myVector = List3NF;
        } else if (aType.equals("Staging")) {
            myVector = ListStage;
        } else if (aType.equals("Star Schema")) {
            myVector = ListStar;
        } else if (aType.equals("Data Vault")) {
            myVector = ListVault;
        } else if (aType.equals("Exploration Mart")) {
            myVector = ListExp;
        } else if (aType.equals("Master Data Model")) {
            myVector = ListMDM;
        }
        int count = 0;
        ComboModel1.removeAllItems();
        ComboModel2.removeAllItems();
        for (Object anObj : myVector) {
            ComboModel1.insertItemAt(anObj, count);
            ComboModel2.insertItemAt(anObj, count);
            if (count == 0) {
                ComboModel1.setSelectedIndex(0);
                ComboModel2.setSelectedIndex(0);
            }
            count++;
        }
    }

    private void ButtonBrowse_actionPerformed(ActionEvent e) {
        ChooserFile chooser = new ChooserFile("Diff Report Files", "rpt,txt");
        File fn = chooser.browseForFile(false, options.getCurrentRPTDirectory(), false);
        if (fn != null) {
            TextFieldReportFile.setText(fn.getAbsolutePath());
            options.setvRptFile(fn);
        }
    }
}
