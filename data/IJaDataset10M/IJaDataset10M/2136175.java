package magictool.explore;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import magictool.ExpFile;
import magictool.GrpFile;
import magictool.VerticalLayout;

/**
 * CritDialog is a class to enable the user to select a criteria for forming groups
 */
public class CritDialog extends JDialog implements KeyListener {

    private String tests[] = { "<", ">" };

    private String mods[] = { "Min.", "Max.", "Avg." };

    private String mods2[] = { "value", "jump" };

    private String bool[] = { "all", "any" };

    private String cont[] = { "contains", "does not contain" };

    private JPanel dialogContentPane = new JPanel();

    private JPanel crit1Panel = new JPanel();

    private JPanel crit2Panel = new JPanel();

    private JPanel crit4Panel = new JPanel();

    private JPanel crit3Panel = new JPanel();

    private BorderLayout borderLayout1 = new BorderLayout();

    private VerticalLayout dialogVFlowLayout1 = new VerticalLayout();

    private FlowLayout critFlowLayout = new FlowLayout();

    private JLabel crit2Label = new JLabel();

    private JLabel crit3Label = new JLabel();

    private JLabel crit4Label = new JLabel();

    private JTextField crit1TextField = new JTextField();

    private JTextField crit2TextField = new JTextField();

    private JTextField crit4TextField = new JTextField();

    private JCheckBox crit1CheckBox = new JCheckBox();

    private JCheckBox crit2CheckBox = new JCheckBox();

    private JCheckBox crit3CheckBox = new JCheckBox();

    private JCheckBox crit4CheckBox = new JCheckBox();

    private JComboBox crit1ComboBox1 = new JComboBox(mods);

    private JComboBox crit1ComboBox2 = new JComboBox(mods2);

    private JComboBox crit1ComboBox3 = new JComboBox(tests);

    private JComboBox crit2ComboBox2 = new JComboBox(tests);

    private JComboBox crit3ComboBox2 = new JComboBox(tests);

    private JComboBox crit4ComboBox = new JComboBox(tests);

    private JComboBox crit3ComboBox1;

    private JComboBox crit2ComboBox1;

    private ButtonGroup buttonGroup1 = new ButtonGroup();

    private TitledBorder titledBorder1;

    private JButton critcancelButton = new JButton("Cancel");

    private JPanel confirmPanel = new JPanel();

    private JButton critokButton = new JButton("OK");

    private VerticalLayout verticalLayout1 = new VerticalLayout();

    private JTextField crit3TextField = new JTextField();

    private JComboBox crit7ComboBox;

    private JPanel crit7Panel = new JPanel();

    private FlowLayout critFlowLayout1 = new FlowLayout();

    private JLabel crit7Label = new JLabel();

    private JCheckBox crit7CheckBox = new JCheckBox();

    private JTextField crit8TextField = new JTextField();

    private JComboBox crit8ComboBox = new JComboBox(cont);

    private JPanel crit8Panel = new JPanel();

    private FlowLayout critFlowLayout2 = new FlowLayout();

    private JLabel crit8Label = new JLabel();

    private JCheckBox crit8CheckBox = new JCheckBox();

    private JTextField crit11TextField = new JTextField();

    private JComboBox crit11ComboBox = new JComboBox(cont);

    private JPanel crit11Panel = new JPanel();

    private FlowLayout critFlowLayout3 = new FlowLayout();

    private JLabel crit11Label = new JLabel();

    private JCheckBox crit11CheckBox = new JCheckBox();

    private FlowLayout critFlowLayout4 = new FlowLayout();

    private JComboBox crit5ComboBox = new JComboBox(cont);

    private JPanel crit5Panel1 = new JPanel();

    private JLabel crit5Label = new JLabel();

    private JCheckBox crit5CheckBox = new JCheckBox();

    private JTextField crit5TextField = new JTextField();

    private FlowLayout critFlowLayout5 = new FlowLayout();

    private JComboBox crit6ComboBox = new JComboBox(cont);

    private JPanel crit6Panel = new JPanel();

    private JLabel crit6Label = new JLabel();

    private JCheckBox crit6CheckBox = new JCheckBox();

    private JTextField crit6TextField = new JTextField();

    private FlowLayout critFlowLayout6 = new FlowLayout();

    private JComboBox crit9ComboBox = new JComboBox(cont);

    private JPanel crit9Panel = new JPanel();

    private JLabel crit9Label = new JLabel();

    private JCheckBox crit9CheckBox = new JCheckBox();

    private JTextField crit9TextField = new JTextField();

    private FlowLayout critFlowLayout7 = new FlowLayout();

    private JComboBox crit10ComboBox = new JComboBox(cont);

    private JPanel crit10Panel = new JPanel();

    private JLabel crit10Label = new JLabel();

    private JCheckBox crit10CheckBox = new JCheckBox();

    private JTextField crit10TextField = new JTextField();

    private JPanel critBooleanPanel = new JPanel();

    private JLabel booLabel1 = new JLabel();

    private JComboBox booComboBox = new JComboBox(bool);

    private JLabel booLabel2 = new JLabel();

    private FlowLayout flowLayout1 = new FlowLayout();

    private JButton clearButton = new JButton();

    /**name of the expression files for the main frame*/
    protected ExpFile expMain;

    /**name of the group files for the main frame*/
    protected GrpFile grpMain;

    /**CritDialog is a class to enable the user to select a criteria for forming groups
 * @param expMain expression file to choose genes from
 * @param parent parent frame
 */
    public CritDialog(ExpFile expMain, Frame parent) {
        super(parent);
        this.expMain = expMain;
        try {
            jbInit();
        } catch (Exception e) {
        }
    }

    /**CritDialog is a class to enable the user to select a criteria for forming groups with default specified criteria
 * @param expMain expression file to choose genes from
 * @param parent parent frame
 * @param criteria default criteria to be selected in dialog window
 */
    public CritDialog(ExpFile expMain, Frame parent, Criteria criteria) {
        super(parent);
        this.expMain = expMain;
        try {
            jbInit();
        } catch (Exception e) {
        }
        setCriteria(criteria);
    }

    public void jbInit() {
        titledBorder1 = new TitledBorder(BorderFactory.createLineBorder(new Color(153, 153, 153), 2), "Select Criteria To Form Group");
        String chroms[];
        if ((chroms = expMain.getChromosomes()).length == 0) {
            chroms = new String[1];
            chroms[0] = "No Chromosomes Listed";
        }
        crit7ComboBox = new JComboBox(chroms);
        this.setModal(true);
        this.setResizable(false);
        this.getContentPane().setLayout(verticalLayout1);
        dialogContentPane.setBorder(titledBorder1);
        critcancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                critcancelButton_actionPerformed(e);
            }
        });
        critokButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                critokButton_actionPerformed(e);
            }
        });
        rootPane.setDefaultButton(critokButton);
        crit3TextField.setPreferredSize(new Dimension(50, 21));
        crit3TextField.setOpaque(false);
        crit3TextField.setEnabled(false);
        crit7ComboBox.setPreferredSize(new Dimension(100, 21));
        crit7ComboBox.setMinimumSize(new Dimension(30, 24));
        crit7ComboBox.setEnabled(false);
        crit7Panel.setLayout(critFlowLayout1);
        critFlowLayout1.setAlignment(FlowLayout.LEFT);
        crit7Label.setEnabled(false);
        crit7Label.setText("Chromosome");
        crit7CheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                crit7CheckBox_actionPerformed(e);
            }
        });
        crit8TextField.setEnabled(false);
        crit8TextField.setOpaque(false);
        crit8TextField.setPreferredSize(new Dimension(50, 21));
        crit8TextField.setColumns(10);
        crit8ComboBox.setPreferredSize(new Dimension(100, 21));
        crit8ComboBox.setMinimumSize(new Dimension(30, 24));
        crit8ComboBox.setEnabled(false);
        crit8Panel.setLayout(critFlowLayout2);
        critFlowLayout2.setAlignment(FlowLayout.LEFT);
        crit8Label.setEnabled(false);
        crit8Label.setText("Comment");
        crit8CheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                crit8CheckBox_actionPerformed(e);
            }
        });
        crit11TextField.setEnabled(false);
        crit11TextField.setOpaque(false);
        crit11TextField.setPreferredSize(new Dimension(50, 21));
        crit11TextField.setColumns(10);
        crit11ComboBox.setPreferredSize(new Dimension(100, 21));
        crit11ComboBox.setMinimumSize(new Dimension(30, 24));
        crit11ComboBox.setEnabled(false);
        crit11Panel.setLayout(critFlowLayout3);
        critFlowLayout3.setAlignment(FlowLayout.LEFT);
        crit11Label.setEnabled(false);
        crit11Label.setText("Cellular Component");
        crit11CheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                crit11CheckBox_actionPerformed(e);
            }
        });
        critFlowLayout4.setAlignment(FlowLayout.LEFT);
        crit5ComboBox.setEnabled(false);
        crit5ComboBox.setMinimumSize(new Dimension(30, 24));
        crit5ComboBox.setPreferredSize(new Dimension(100, 21));
        crit5Panel1.setLayout(critFlowLayout4);
        crit5Label.setEnabled(false);
        crit5Label.setText("Gene Name");
        crit5CheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                crit5CheckBox_actionPerformed(e);
            }
        });
        crit5TextField.setEnabled(false);
        crit5TextField.setOpaque(false);
        crit5TextField.setPreferredSize(new Dimension(50, 21));
        crit5TextField.setColumns(10);
        critFlowLayout5.setAlignment(FlowLayout.LEFT);
        crit6ComboBox.setEnabled(false);
        crit6ComboBox.setMinimumSize(new Dimension(30, 24));
        crit6ComboBox.setPreferredSize(new Dimension(100, 21));
        crit6Panel.setLayout(critFlowLayout5);
        crit6Label.setEnabled(false);
        crit6Label.setText("Gene Alias");
        crit6CheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                crit6CheckBox_actionPerformed(e);
            }
        });
        crit6TextField.setEnabled(false);
        crit6TextField.setOpaque(false);
        crit6TextField.setPreferredSize(new Dimension(50, 21));
        crit6TextField.setColumns(10);
        critFlowLayout6.setAlignment(FlowLayout.LEFT);
        crit9ComboBox.setEnabled(false);
        crit9ComboBox.setMinimumSize(new Dimension(30, 24));
        crit9ComboBox.setPreferredSize(new Dimension(100, 21));
        crit9Panel.setLayout(critFlowLayout6);
        crit9Label.setEnabled(false);
        crit9Label.setText("Biological Process");
        crit9CheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                crit9CheckBox_actionPerformed(e);
            }
        });
        crit9TextField.setEnabled(false);
        crit9TextField.setOpaque(false);
        crit9TextField.setPreferredSize(new Dimension(50, 21));
        crit9TextField.setColumns(10);
        critFlowLayout7.setAlignment(FlowLayout.LEFT);
        crit10ComboBox.setEnabled(false);
        crit10ComboBox.setMinimumSize(new Dimension(30, 24));
        crit10ComboBox.setPreferredSize(new Dimension(100, 21));
        crit10Panel.setLayout(critFlowLayout7);
        crit10Label.setEnabled(false);
        crit10Label.setText("Molecular Function");
        crit10CheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                crit10CheckBox_actionPerformed(e);
            }
        });
        crit10TextField.setEnabled(false);
        crit10TextField.setOpaque(false);
        crit10TextField.setPreferredSize(new Dimension(50, 21));
        crit10TextField.setColumns(10);
        crit2Label.setEnabled(false);
        crit3Label.setEnabled(false);
        crit4Label.setEnabled(false);
        clearButton.setText("Clear All");
        clearButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                clearButton_actionPerformed(e);
            }
        });
        this.getContentPane().add(dialogContentPane);
        this.setBounds(50, 50, 480, 350);
        crit3ComboBox1 = new JComboBox(expMain.getLabelArray());
        crit2ComboBox1 = new JComboBox(expMain.getLabelArray());
        dialogContentPane.setLayout(dialogVFlowLayout1);
        crit1ComboBox1.setEnabled(false);
        crit1ComboBox1.setMinimumSize(new Dimension(50, 21));
        crit1ComboBox1.setPreferredSize(new Dimension(70, 21));
        crit1ComboBox2.setEnabled(false);
        crit1ComboBox2.setMinimumSize(new Dimension(50, 21));
        crit1ComboBox2.setPreferredSize(new Dimension(70, 21));
        crit1ComboBox3.setEnabled(false);
        crit1ComboBox3.setMinimumSize(new Dimension(30, 21));
        crit1ComboBox3.setPreferredSize(new Dimension(50, 21));
        crit1TextField.setEnabled(false);
        crit1TextField.setOpaque(false);
        crit1TextField.setPreferredSize(new Dimension(50, 21));
        crit2Label.setText("Value in column labeled");
        crit2TextField.setEnabled(false);
        crit2TextField.setOpaque(false);
        crit2TextField.setPreferredSize(new Dimension(50, 21));
        crit3Label.setText("Value in column labeled");
        crit1Panel.setLayout(critFlowLayout);
        critFlowLayout.setAlignment(FlowLayout.LEFT);
        crit3Panel.setLayout(critFlowLayout);
        crit2Panel.setLayout(critFlowLayout);
        crit3ComboBox2.setPreferredSize(new Dimension(50, 21));
        crit3ComboBox2.setMinimumSize(new Dimension(30, 21));
        crit3ComboBox2.setEnabled(false);
        crit2ComboBox2.setPreferredSize(new Dimension(50, 21));
        crit2ComboBox2.setMinimumSize(new Dimension(30, 21));
        crit2ComboBox2.setEnabled(false);
        crit4Panel.setLayout(critFlowLayout);
        crit4TextField.setEnabled(false);
        crit4TextField.setOpaque(false);
        crit4TextField.setPreferredSize(new Dimension(50, 21));
        crit4ComboBox.setPreferredSize(new Dimension(50, 21));
        crit4ComboBox.setMinimumSize(new Dimension(30, 21));
        crit4ComboBox.setEnabled(false);
        crit4Label.setText("Standard deviation");
        crit1CheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                crit1CheckBox_actionPerformed(e);
            }
        });
        crit2CheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                crit2CheckBox_actionPerformed(e);
            }
        });
        crit3CheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                crit3CheckBox_actionPerformed(e);
            }
        });
        crit4CheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                crit4CheckBox_actionPerformed(e);
            }
        });
        crit7Panel.add(crit7CheckBox);
        crit7Panel.add(crit7Label);
        crit7Panel.add(crit7ComboBox, null);
        crit8Panel.add(crit8CheckBox);
        crit8Panel.add(crit8Label);
        crit8Panel.add(crit8ComboBox, null);
        crit8Panel.add(crit8TextField, null);
        crit11Panel.add(crit11CheckBox);
        crit11Panel.add(crit11Label);
        crit11Panel.add(crit11ComboBox, null);
        crit11Panel.add(crit11TextField, null);
        crit5Panel1.add(crit5CheckBox);
        crit5Panel1.add(crit5Label);
        crit5Panel1.add(crit5ComboBox, null);
        crit5Panel1.add(crit5TextField, null);
        crit6Panel.add(crit6CheckBox);
        crit6Panel.add(crit6Label);
        crit6Panel.add(crit6ComboBox, null);
        crit6Panel.add(crit6TextField, null);
        crit9Panel.add(crit9CheckBox);
        crit9Panel.add(crit9Label);
        crit9Panel.add(crit9ComboBox, null);
        crit9Panel.add(crit9TextField, null);
        crit10Panel.add(crit10CheckBox);
        crit10Panel.add(crit10Label);
        crit10Panel.add(crit10ComboBox, null);
        crit10Panel.add(crit10TextField, null);
        crit1Panel.add(crit1CheckBox);
        crit1Panel.add(crit1ComboBox1);
        crit1Panel.add(crit1ComboBox2);
        crit1Panel.add(crit1ComboBox3);
        crit1Panel.add(crit1TextField);
        crit2Panel.add(crit2CheckBox);
        crit2Panel.add(crit2Label);
        crit2Panel.add(crit2ComboBox1, null);
        crit2Panel.add(crit2ComboBox2);
        crit2Panel.add(crit2TextField);
        crit3Panel.add(crit3CheckBox);
        crit3Panel.add(crit3Label);
        crit3Panel.add(crit3ComboBox1, null);
        crit3Panel.add(crit3ComboBox2);
        crit3Panel.add(crit3TextField, null);
        crit4Panel.add(crit4CheckBox);
        crit4Panel.add(crit4Label);
        crit4Panel.add(crit4ComboBox);
        crit4Panel.add(crit4TextField);
        critBooleanPanel.add(booLabel1, null);
        critBooleanPanel.add(booComboBox, null);
        critBooleanPanel.add(booLabel2, null);
        this.getContentPane().add(confirmPanel, BorderLayout.CENTER);
        crit3ComboBox1.setEnabled(false);
        crit3ComboBox1.setMinimumSize(new Dimension(30, 24));
        crit3ComboBox1.setPreferredSize(new Dimension(100, 21));
        crit2ComboBox1.setEnabled(false);
        crit2ComboBox1.setPreferredSize(new Dimension(100, 21));
        critBooleanPanel.setLayout(flowLayout1);
        booLabel1.setText("Group genes matching");
        booLabel2.setText("selected criteria");
        flowLayout1.setAlignment(FlowLayout.LEFT);
        booComboBox.setPreferredSize(new Dimension(50, 21));
        dialogContentPane.add(crit1Panel, null);
        dialogContentPane.add(crit2Panel, null);
        dialogContentPane.add(crit3Panel, null);
        dialogContentPane.add(crit4Panel, null);
        dialogContentPane.add(crit5Panel1, null);
        dialogContentPane.add(crit6Panel, null);
        dialogContentPane.add(crit7Panel, null);
        dialogContentPane.add(crit8Panel, null);
        dialogContentPane.add(crit9Panel, null);
        dialogContentPane.add(crit10Panel, null);
        dialogContentPane.add(crit11Panel, null);
        dialogContentPane.add(critBooleanPanel, null);
        confirmPanel.add(critokButton, null);
        confirmPanel.add(critcancelButton, null);
        confirmPanel.add(clearButton, null);
        this.addKeyListenerRecursively(this);
        this.pack();
    }

    /**
   * returns the criteria used to filter genes
   * @return criteria used to filter genes
   */
    public Criteria getCriteria() {
        Criteria criteria = new Criteria();
        if (crit1CheckBox.isSelected()) criteria.setValue(crit1ComboBox1.getSelectedIndex(), crit1ComboBox2.getSelectedIndex() == 0, crit1ComboBox3.getSelectedIndex() == 0, Double.parseDouble(crit1TextField.getText()));
        if (crit2CheckBox.isSelected()) criteria.setColumnValue1(crit2ComboBox1.getSelectedIndex(), crit2ComboBox2.getSelectedIndex() == 0, Double.parseDouble(crit2TextField.getText()));
        if (crit3CheckBox.isSelected()) criteria.setColumnValue2(crit3ComboBox1.getSelectedIndex(), crit3ComboBox2.getSelectedIndex() == 0, Double.parseDouble(crit3TextField.getText()));
        if (crit4CheckBox.isSelected()) criteria.setStdDev(crit4ComboBox.getSelectedIndex() == 0, Double.parseDouble(crit4TextField.getText()));
        if (crit5CheckBox.isSelected()) criteria.setGeneName(crit5ComboBox.getSelectedIndex() == 0, crit5TextField.getText());
        if (crit6CheckBox.isSelected()) criteria.setGeneAlias(crit6ComboBox.getSelectedIndex() == 0, crit6TextField.getText());
        if (crit7CheckBox.isSelected()) criteria.setChromosome(crit7ComboBox.getSelectedIndex());
        if (crit8CheckBox.isSelected()) criteria.setComment(crit8ComboBox.getSelectedIndex() == 0, crit8TextField.getText());
        if (crit9CheckBox.isSelected()) criteria.setBioProcess(crit9ComboBox.getSelectedIndex() == 0, crit9TextField.getText());
        if (crit10CheckBox.isSelected()) criteria.setMolecularFunction(crit10ComboBox.getSelectedIndex() == 0, crit10TextField.getText());
        if (crit11CheckBox.isSelected()) criteria.setCellComment(crit11ComboBox.getSelectedIndex() == 0, crit11TextField.getText());
        criteria.matching = (booComboBox.getSelectedIndex() == 0);
        return criteria;
    }

    /**
   * sets the criteria used to filter genes
   * @param criteria settings to filter genes
   */
    public void setCriteria(Criteria criteria) {
        try {
            booComboBox.setSelectedIndex(criteria.matching ? 0 : 1);
            if (((Boolean) criteria.value[0]).booleanValue()) setValue(((Integer) criteria.value[1]).intValue(), ((Boolean) criteria.value[2]).booleanValue(), ((Boolean) criteria.value[3]).booleanValue(), ((Double) criteria.value[4]).doubleValue()); else clearValue();
            if (((Boolean) criteria.column1[0]).booleanValue()) setColumnValue1(((Integer) criteria.column1[1]).intValue(), ((Boolean) criteria.column1[2]).booleanValue(), ((Double) criteria.column1[3]).doubleValue()); else clearColumnValue1();
            if (((Boolean) criteria.column2[0]).booleanValue()) setColumnValue2(((Integer) criteria.column2[1]).intValue(), ((Boolean) criteria.column2[2]).booleanValue(), ((Double) criteria.column2[3]).doubleValue()); else clearColumnValue2();
            if (((Boolean) criteria.comment[0]).booleanValue()) setComment(((Boolean) criteria.comment[1]).booleanValue(), criteria.comment[2].toString()); else clearComment();
            if (((Boolean) criteria.alias[0]).booleanValue()) setGeneAlias(((Boolean) criteria.alias[1]).booleanValue(), criteria.alias[2].toString()); else clearGeneAlias();
            if (((Boolean) criteria.name[0]).booleanValue()) setGeneName(((Boolean) criteria.name[1]).booleanValue(), criteria.name[2].toString()); else clearGeneName();
            if (((Boolean) criteria.function[0]).booleanValue()) setMolecularFunction(((Boolean) criteria.function[1]).booleanValue(), criteria.function[2].toString()); else clearMolecularFunction();
            if (((Boolean) criteria.std[0]).booleanValue()) setStdDev(((Boolean) criteria.std[1]).booleanValue(), ((Double) criteria.std[2]).doubleValue()); else clearStdDev();
            if (((Boolean) criteria.chromosome[0]).booleanValue()) setChromosome(((Integer) criteria.chromosome[1]).intValue()); else clearChromosome();
            if (((Boolean) criteria.bio[0]).booleanValue()) setBioProcess(((Boolean) criteria.bio[1]).booleanValue(), criteria.bio[2].toString()); else clearBioProcess();
            if (((Boolean) criteria.cell[0]).booleanValue()) setCellComment(((Boolean) criteria.cell[1]).booleanValue(), criteria.cell[2].toString()); else clearCellComment();
        } catch (Exception e1) {
            clearAll();
        }
    }

    /**
    * clears all the settings
    */
    public void clearAll() {
        booComboBox.setSelectedIndex(0);
        clearValue();
        clearColumnValue1();
        clearColumnValue2();
        clearComment();
        clearGeneAlias();
        clearGeneName();
        clearMolecularFunction();
        clearStdDev();
        clearChromosome();
        clearBioProcess();
        clearCellComment();
    }

    /**
   * changes the settings for the value criteria
   * @param style whether looking for minimum (0), maximum (1), or average (2) value
   * @param value whether looking for value (true) or jump in value (false)
   * @param greater whether looking for greater (true) or less than (false) a value
   * @param number criteria value
   */
    public void setValue(int style, boolean value, boolean greater, double number) {
        crit1CheckBox.setSelected(true);
        crit1ComboBox1.setSelectedIndex(style);
        crit1ComboBox2.setSelectedIndex(value ? 0 : 1);
        crit1ComboBox3.setSelectedIndex(greater ? 0 : 1);
        crit1TextField.setText(String.valueOf(number));
        crit1ComboBox1.setEnabled(true);
        crit1ComboBox2.setEnabled(true);
        crit1ComboBox3.setEnabled(true);
        crit1TextField.setEnabled(true);
    }

    /**
   * clears the settings for the value criteria
   */
    public void clearValue() {
        crit1CheckBox.setSelected(false);
        crit1ComboBox1.setSelectedIndex(0);
        crit1ComboBox2.setSelectedIndex(0);
        crit1ComboBox3.setSelectedIndex(0);
        crit1TextField.setText("");
        crit1ComboBox1.setEnabled(false);
        crit1ComboBox2.setEnabled(false);
        crit1ComboBox3.setEnabled(false);
        crit1TextField.setEnabled(false);
    }

    /**
   * changes the settings for the first column value criteria
   * @param column column number
   * @param greater whether looking for greater (true) or less than (false) a value
   * @param number criteria value
   */
    public void setColumnValue1(int column, boolean greater, double number) {
        crit2CheckBox.setSelected(true);
        crit2ComboBox1.setSelectedIndex(column);
        crit2ComboBox2.setSelectedIndex(greater ? 0 : 1);
        crit2TextField.setText(String.valueOf(number));
        crit2ComboBox1.setEnabled(true);
        crit2ComboBox2.setEnabled(true);
        crit2TextField.setEnabled(true);
        crit2Label.setEnabled(true);
    }

    /**
   * clears the settings for the first column value criteria
   */
    public void clearColumnValue1() {
        crit2CheckBox.setSelected(false);
        crit2ComboBox1.setSelectedIndex(0);
        crit2ComboBox2.setSelectedIndex(0);
        crit2TextField.setText("");
        crit2ComboBox1.setEnabled(false);
        crit2ComboBox2.setEnabled(false);
        crit2TextField.setEnabled(false);
        crit2Label.setEnabled(false);
    }

    /**
   * changes the settings for the second column value criteria
   * @param column column number
   * @param greater whether looking for greater (true) or less than (false) a value
   * @param number criteria value
   */
    public void setColumnValue2(int column, boolean greater, double number) {
        crit3CheckBox.setSelected(true);
        crit3ComboBox1.setSelectedIndex(column);
        crit3ComboBox2.setSelectedIndex(greater ? 0 : 1);
        crit3TextField.setText(String.valueOf(number));
        crit3ComboBox1.setEnabled(true);
        crit3ComboBox2.setEnabled(true);
        crit3TextField.setEnabled(true);
        crit3Label.setEnabled(true);
    }

    /**
   * clears the settings for the second column value criteria
   */
    public void clearColumnValue2() {
        crit3CheckBox.setSelected(false);
        crit3ComboBox1.setSelectedIndex(0);
        crit3ComboBox2.setSelectedIndex(0);
        crit3TextField.setText("");
        crit3ComboBox1.setEnabled(false);
        crit3ComboBox2.setEnabled(false);
        crit3TextField.setEnabled(false);
        crit3Label.setEnabled(false);
    }

    /**
   * changes the settings for the standard deviation criteria
   * @param greater whether looking for greater (true) or less than (false) a value
   * @param number criteria value
   */
    public void setStdDev(boolean greater, double number) {
        crit4CheckBox.setSelected(true);
        crit4ComboBox.setSelectedIndex(greater ? 0 : 1);
        crit4TextField.setText(String.valueOf(number));
        crit4ComboBox.setEnabled(true);
        crit4TextField.setEnabled(true);
        crit4Label.setEnabled(true);
    }

    /**
   * clears the settings for the standard deviation criteria
   */
    public void clearStdDev() {
        crit4CheckBox.setSelected(false);
        crit4ComboBox.setSelectedIndex(0);
        crit4TextField.setText("");
        crit4ComboBox.setEnabled(false);
        crit4TextField.setEnabled(false);
        crit4Label.setEnabled(false);
    }

    /**
   * changes the settings for the gene name criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param name criteria string
   */
    public void setGeneName(boolean contains, String name) {
        crit5CheckBox.setSelected(true);
        crit5ComboBox.setSelectedIndex(contains ? 0 : 1);
        crit5TextField.setText(name);
        crit5ComboBox.setEnabled(true);
        crit5TextField.setEnabled(true);
        crit5Label.setEnabled(true);
    }

    /**
   * clears the settings for the gene name criteria
   */
    public void clearGeneName() {
        crit5CheckBox.setSelected(false);
        crit5ComboBox.setSelectedIndex(0);
        crit5TextField.setText("");
        crit5ComboBox.setEnabled(false);
        crit5TextField.setEnabled(false);
        crit5Label.setEnabled(false);
    }

    /**
   * changes the settings for the gene alias criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param name criteria string
   */
    public void setGeneAlias(boolean contains, String name) {
        crit6CheckBox.setSelected(true);
        crit6ComboBox.setSelectedIndex(contains ? 0 : 1);
        crit6TextField.setText(name);
        crit6ComboBox.setEnabled(true);
        crit6TextField.setEnabled(true);
        crit6Label.setEnabled(true);
    }

    /**
   * clears the settings for the gene alias criteria
   */
    public void clearGeneAlias() {
        crit6CheckBox.setSelected(false);
        crit6ComboBox.setSelectedIndex(0);
        crit6TextField.setText("");
        crit6ComboBox.setEnabled(false);
        crit6TextField.setEnabled(false);
        crit6Label.setEnabled(false);
    }

    /**
   * changes the settings for the chromosome criteria
   * @param chrom chromosome to select
   */
    public void setChromosome(int chrom) {
        crit7CheckBox.setSelected(true);
        crit7ComboBox.setSelectedIndex(chrom);
        crit7ComboBox.setEnabled(true);
        crit7Label.setEnabled(true);
    }

    /**
   * clears the settings for the chromosome criteria
   */
    public void clearChromosome() {
        crit7CheckBox.setSelected(false);
        crit7ComboBox.setSelectedIndex(0);
        crit7ComboBox.setEnabled(false);
        crit7Label.setEnabled(false);
    }

    /**
   * changes the settings for the comment criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param name criteria string
   */
    public void setComment(boolean contains, String name) {
        crit8CheckBox.setSelected(true);
        crit8ComboBox.setSelectedIndex(contains ? 0 : 1);
        crit8TextField.setText(name);
        crit8ComboBox.setEnabled(true);
        crit8TextField.setEnabled(true);
        crit8Label.setEnabled(true);
    }

    /**
   * clears the settings for the comment criteria
   */
    public void clearComment() {
        crit8CheckBox.setSelected(false);
        crit8ComboBox.setSelectedIndex(0);
        crit8TextField.setText("");
        crit8ComboBox.setEnabled(false);
        crit8TextField.setEnabled(false);
        crit8Label.setEnabled(false);
    }

    /**
   * changes the settings for the biological process criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param name criteria string
   */
    public void setBioProcess(boolean contains, String name) {
        crit9CheckBox.setSelected(true);
        crit9ComboBox.setSelectedIndex(contains ? 0 : 1);
        crit9TextField.setText(name);
        crit9ComboBox.setEnabled(true);
        crit9TextField.setEnabled(true);
        crit9Label.setEnabled(true);
    }

    /**
   * clears the settings for the biological process criteria
   */
    public void clearBioProcess() {
        crit9CheckBox.setSelected(false);
        crit9ComboBox.setSelectedIndex(0);
        crit9TextField.setText("");
        crit9ComboBox.setEnabled(false);
        crit9TextField.setEnabled(false);
        crit9Label.setEnabled(false);
    }

    /**
   * changes the settings for the molecular function criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param name criteria string
   */
    public void setMolecularFunction(boolean contains, String name) {
        crit10CheckBox.setSelected(true);
        crit10ComboBox.setSelectedIndex(contains ? 0 : 1);
        crit10TextField.setText(name);
        crit10ComboBox.setEnabled(true);
        crit10TextField.setEnabled(true);
        crit10Label.setEnabled(true);
    }

    /**
   * clears the settings for the molecular function criteria
   */
    public void clearMolecularFunction() {
        crit10CheckBox.setSelected(false);
        crit10ComboBox.setSelectedIndex(0);
        crit10TextField.setText("");
        crit10ComboBox.setEnabled(false);
        crit10TextField.setEnabled(false);
        crit10Label.setEnabled(false);
    }

    /**
   * clears the setting for matching
   * @param matching whether necessary to match all (true) or any (false) criteria
   */
    public void setMatching(boolean matching) {
        booComboBox.setSelectedIndex(matching ? 0 : 1);
    }

    /**
   * changes the settings for the cellular comment criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param name criteria string
   */
    public void setCellComment(boolean contains, String name) {
        crit11CheckBox.setSelected(true);
        crit11ComboBox.setSelectedIndex(contains ? 0 : 1);
        crit11TextField.setText(name);
        crit11ComboBox.setEnabled(true);
        crit11TextField.setEnabled(true);
        crit11Label.setEnabled(true);
    }

    /**
   * clears the settings for the cellular comment criteria
   */
    public void clearCellComment() {
        crit11CheckBox.setSelected(false);
        crit11ComboBox.setSelectedIndex(0);
        crit11TextField.setText("");
        crit11ComboBox.setEnabled(false);
        crit11TextField.setEnabled(false);
        crit11Label.setEnabled(false);
    }

    private void critcancelButton_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    /**
     * This is the code to form a group based on various criteria
     * Checks each checkbox and adds members to the group accordingly.
     * @param e
     *
     * THIS MAY NEED TO BE A SEPARATE FUNCTION OR GO SOMEWHERE ELSE,
     * OUTSIDE OF THE EXPLOREFRAME CODE
     *
     * DOES NOT TEST FOR VALID VALUES
     */
    private void critokButton_actionPerformed(ActionEvent e) {
        Thread thread = new Thread() {

            public void run() {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                boolean madeGroup = true;
                grpMain = new GrpFile();
                try {
                    if (crit1CheckBox.isSelected()) {
                        TreeSet crit1treeset = new TreeSet();
                        if (crit1ComboBox2.getSelectedItem() == "value") {
                            double min = 0, max = 0, avg;
                            double limit = Double.parseDouble(crit1TextField.getText());
                            for (int row = 0; row <= expMain.numGenes() - 1; row++) {
                                min = expMain.getMin(row);
                                max = expMain.getMax(row);
                                avg = expMain.getAvg(row);
                                if (((crit1ComboBox1.getSelectedItem() == "Min.") && (crit1ComboBox3.getSelectedItem() == "<") && (min < limit)) || ((crit1ComboBox1.getSelectedItem() == "Min.") && (crit1ComboBox3.getSelectedItem() == ">") && (min > limit)) || ((crit1ComboBox1.getSelectedItem() == "Max.") && (crit1ComboBox3.getSelectedItem() == "<") && (max < limit)) || ((crit1ComboBox1.getSelectedItem() == "Max.") && (crit1ComboBox3.getSelectedItem() == ">") && (max > limit)) || ((crit1ComboBox1.getSelectedItem() == "Avg.") && (crit1ComboBox3.getSelectedItem() == "<") && (avg < limit)) || ((crit1ComboBox1.getSelectedItem() == "Avg.") && (crit1ComboBox3.getSelectedItem() == ">") && (avg > limit))) crit1treeset.add(expMain.getGeneName(row));
                            }
                        }
                        if (crit1ComboBox2.getSelectedItem() == "jump") {
                            double min = 0, max = 0, avg;
                            double limit = Double.parseDouble(crit1TextField.getText());
                            for (int row = 0; row <= expMain.numGenes() - 1; row++) {
                                min = expMain.getMinJump(row);
                                max = expMain.getMaxJump(row);
                                avg = expMain.getAvgJump(row);
                                if (((crit1ComboBox1.getSelectedItem() == "Min.") && (crit1ComboBox3.getSelectedItem() == "<") && (min < limit)) || ((crit1ComboBox1.getSelectedItem() == "Min.") && (crit1ComboBox3.getSelectedItem() == ">") && (min > limit)) || ((crit1ComboBox1.getSelectedItem() == "Max.") && (crit1ComboBox3.getSelectedItem() == "<") && (max < limit)) || ((crit1ComboBox1.getSelectedItem() == "Max.") && (crit1ComboBox3.getSelectedItem() == ">") && (max > limit)) || ((crit1ComboBox1.getSelectedItem() == "Avg.") && (crit1ComboBox3.getSelectedItem() == "<") && (avg < limit)) || ((crit1ComboBox1.getSelectedItem() == "Avg.") && (crit1ComboBox3.getSelectedItem() == ">") && (avg > limit))) crit1treeset.add(expMain.getGeneName(row));
                            }
                        }
                        if (booComboBox.getSelectedItem() == "any") grpMain.meetAny(crit1treeset);
                        if (booComboBox.getSelectedItem() == "all") grpMain.meetAll(crit1treeset);
                    }
                    if (crit2CheckBox.isSelected()) {
                        TreeSet crit2treeset = new TreeSet();
                        int column = expMain.getLabelNum(crit2ComboBox1.getSelectedItem());
                        double limit = Double.parseDouble(crit2TextField.getText());
                        for (int row = 0; row <= expMain.numGenes() - 1; row++) {
                            if (((crit2ComboBox2.getSelectedItem() == "<") && (expMain.getDataPoint(row, column) < limit)) || ((crit2ComboBox2.getSelectedItem() == ">") && (expMain.getDataPoint(row, column) > limit))) crit2treeset.add(expMain.getGeneName(row));
                        }
                        if (booComboBox.getSelectedItem() == "any") grpMain.meetAny(crit2treeset);
                        if (booComboBox.getSelectedItem() == "all") grpMain.meetAll(crit2treeset);
                    }
                    if (crit3CheckBox.isSelected()) {
                        TreeSet crit3treeset = new TreeSet();
                        int column = expMain.getLabelNum(crit3ComboBox1.getSelectedItem());
                        double limit = Double.parseDouble(crit3TextField.getText());
                        for (int row = 0; row <= expMain.numGenes() - 1; row++) {
                            if (((crit3ComboBox2.getSelectedItem() == "<") && (expMain.getDataPoint(row, column) < limit)) || ((crit3ComboBox2.getSelectedItem() == ">") && (expMain.getDataPoint(row, column) > limit))) crit3treeset.add(expMain.getGeneName(row));
                        }
                        if (booComboBox.getSelectedItem() == "any") grpMain.meetAny(crit3treeset);
                        if (booComboBox.getSelectedItem() == "all") grpMain.meetAll(crit3treeset);
                    }
                    if (crit4CheckBox.isSelected()) {
                        TreeSet crit4treeset = new TreeSet();
                        double stddev = 0;
                        double limit = Double.parseDouble(crit4TextField.getText());
                        for (int row = 0; row <= expMain.numGenes() - 1; row++) {
                            stddev = expMain.getSD(row);
                            if (((crit4ComboBox.getSelectedItem() == "<") && (stddev < limit)) || ((crit4ComboBox.getSelectedItem() == ">") && (stddev > limit))) crit4treeset.add(expMain.getGeneName(row));
                        }
                        if (booComboBox.getSelectedItem() == "any") grpMain.meetAny(crit4treeset);
                        if (booComboBox.getSelectedItem() == "all") grpMain.meetAll(crit4treeset);
                    }
                    if (crit5CheckBox.isSelected()) {
                        TreeSet crit5treeset = new TreeSet();
                        String name = "";
                        String cont = crit5TextField.getText();
                        if (!cont.trim().equals("")) {
                            for (int row = 0; row < expMain.numGenes(); row++) {
                                name = expMain.getGeneName(row);
                                if (name != null) {
                                    if (name.toLowerCase().lastIndexOf(cont.toLowerCase()) == -1) {
                                        if (crit5ComboBox.getSelectedIndex() == 1) crit5treeset.add(name);
                                    } else if (crit5ComboBox.getSelectedIndex() == 0) crit5treeset.add(name);
                                }
                            }
                        }
                        if (booComboBox.getSelectedItem() == "any") grpMain.meetAny(crit5treeset);
                        if (booComboBox.getSelectedItem() == "all") grpMain.meetAll(crit5treeset);
                    }
                    if (crit6CheckBox.isSelected()) {
                        TreeSet crit6treeset = new TreeSet();
                        String alias = "";
                        String cont = crit6TextField.getText();
                        if (!cont.trim().equals("")) {
                            for (int row = 0; row < expMain.numGenes(); row++) {
                                alias = expMain.getGene(row).getAlias();
                                if (alias != null) {
                                    if (alias.toLowerCase().lastIndexOf(cont.toLowerCase()) == -1) {
                                        if (crit6ComboBox.getSelectedIndex() == 1) crit6treeset.add(expMain.getGeneName(row));
                                    } else if (crit6ComboBox.getSelectedIndex() == 0) crit6treeset.add(expMain.getGeneName(row));
                                }
                            }
                        }
                        if (booComboBox.getSelectedItem() == "any") grpMain.meetAny(crit6treeset);
                        if (booComboBox.getSelectedItem() == "all") grpMain.meetAll(crit6treeset);
                    }
                    if (crit7CheckBox.isSelected()) {
                        TreeSet crit7treeset = new TreeSet();
                        String chrom = "";
                        for (int row = 0; row < expMain.numGenes(); row++) {
                            chrom = expMain.getGene(row).getChromo();
                            if (chrom != null && chrom.equals(crit7ComboBox.getSelectedItem().toString())) {
                                crit7treeset.add(expMain.getGeneName(row));
                            }
                        }
                        if (booComboBox.getSelectedItem() == "any") grpMain.meetAny(crit7treeset);
                        if (booComboBox.getSelectedItem() == "all") grpMain.meetAll(crit7treeset);
                    }
                    if (crit8CheckBox.isSelected()) {
                        TreeSet crit8treeset = new TreeSet();
                        String com = "";
                        String cont = crit8TextField.getText();
                        if (!cont.trim().equals("")) {
                            for (int row = 0; row < expMain.numGenes(); row++) {
                                com = expMain.getGene(row).getComments();
                                if (com != null) {
                                    if (com.toLowerCase().lastIndexOf(cont.toLowerCase()) == -1) {
                                        if (crit8ComboBox.getSelectedIndex() == 1) crit8treeset.add(expMain.getGeneName(row));
                                    } else if (crit8ComboBox.getSelectedIndex() == 0) crit8treeset.add(expMain.getGeneName(row));
                                }
                            }
                        }
                        if (booComboBox.getSelectedItem() == "any") grpMain.meetAny(crit8treeset);
                        if (booComboBox.getSelectedItem() == "all") grpMain.meetAll(crit8treeset);
                    }
                    if (crit9CheckBox.isSelected()) {
                        TreeSet crit9treeset = new TreeSet();
                        String proc = "";
                        String cont = crit9TextField.getText();
                        if (!cont.trim().equals("")) {
                            for (int row = 0; row < expMain.numGenes(); row++) {
                                proc = expMain.getGene(row).getProcess();
                                if (proc != null) {
                                    if (proc.toLowerCase().lastIndexOf(cont.toLowerCase()) == -1) {
                                        if (crit9ComboBox.getSelectedIndex() == 1) crit9treeset.add(expMain.getGeneName(row));
                                    } else if (crit9ComboBox.getSelectedIndex() == 0) crit9treeset.add(expMain.getGeneName(row));
                                }
                            }
                        }
                        if (booComboBox.getSelectedItem() == "any") grpMain.meetAny(crit9treeset);
                        if (booComboBox.getSelectedItem() == "all") grpMain.meetAll(crit9treeset);
                    }
                    if (crit10CheckBox.isSelected()) {
                        TreeSet crit10treeset = new TreeSet();
                        String func = "";
                        String cont = crit10TextField.getText();
                        if (!cont.trim().equals("")) {
                            for (int row = 0; row < expMain.numGenes(); row++) {
                                func = expMain.getGene(row).getFunction();
                                if (func != null) {
                                    if (func.toLowerCase().lastIndexOf(cont.toLowerCase()) == -1) {
                                        if (crit10ComboBox.getSelectedIndex() == 1) crit10treeset.add(expMain.getGeneName(row));
                                    } else if (crit10ComboBox.getSelectedIndex() == 0) crit10treeset.add(expMain.getGeneName(row));
                                }
                            }
                        }
                        if (booComboBox.getSelectedItem() == "any") grpMain.meetAny(crit10treeset);
                        if (booComboBox.getSelectedItem() == "all") grpMain.meetAll(crit10treeset);
                    }
                    if (crit11CheckBox.isSelected()) {
                        TreeSet crit11treeset = new TreeSet();
                        String comp = "";
                        String cont = crit11TextField.getText();
                        if (!cont.trim().equals("")) {
                            for (int row = 0; row < expMain.numGenes(); row++) {
                                comp = expMain.getGene(row).getComponent();
                                if (comp != null) {
                                    if (comp.toLowerCase().lastIndexOf(cont.toLowerCase()) == -1) {
                                        if (crit11ComboBox.getSelectedIndex() == 1) crit11treeset.add(expMain.getGeneName(row));
                                    } else if (crit11ComboBox.getSelectedIndex() == 0) crit11treeset.add(expMain.getGeneName(row));
                                }
                            }
                        }
                        if (booComboBox.getSelectedItem() == "any") grpMain.meetAny(crit11treeset);
                        if (booComboBox.getSelectedItem() == "all") grpMain.meetAll(crit11treeset);
                    }
                    if (crit1CheckBox.isSelected() || crit2CheckBox.isSelected() || crit3CheckBox.isSelected() || crit4CheckBox.isSelected() || crit5CheckBox.isSelected() || crit6CheckBox.isSelected() || crit7CheckBox.isSelected() || crit8CheckBox.isSelected() || crit9CheckBox.isSelected() || crit10CheckBox.isSelected() || crit11CheckBox.isSelected()) {
                        grpMain.setTitle("Temporary Group");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Please Select One Or More Criteria To Filter The Data");
                    }
                } catch (Exception critException) {
                    JOptionPane.showMessageDialog(null, "Error! One Or More Of Your Textfield Entries Is Not The Correct Form! Please Try Again");
                }
                setCursor(Cursor.getDefaultCursor());
            }
        };
        thread.start();
    }

    /**
   * returns the group file of genes which match the criteria entered
   * @return group file of genes which match the criteria entered
   */
    public GrpFile getValue() {
        return grpMain;
    }

    private void crit1CheckBox_actionPerformed(ActionEvent e) {
        if (crit1CheckBox.isSelected()) {
            crit1ComboBox1.setEnabled(true);
            crit1ComboBox2.setEnabled(true);
            crit1ComboBox3.setEnabled(true);
            crit1TextField.setEnabled(true);
        } else if (crit1CheckBox.isSelected() == false) {
            crit1ComboBox1.setEnabled(false);
            crit1ComboBox2.setEnabled(false);
            crit1ComboBox3.setEnabled(false);
            crit1TextField.setEnabled(false);
        }
    }

    private void crit2CheckBox_actionPerformed(ActionEvent e) {
        if (crit2CheckBox.isSelected()) {
            crit2Label.setEnabled(true);
            crit2ComboBox1.setEnabled(true);
            crit2ComboBox2.setEnabled(true);
            crit2TextField.setEnabled(true);
        } else if (crit2CheckBox.isSelected() == false) {
            crit2Label.setEnabled(false);
            crit2ComboBox1.setEnabled(false);
            crit2ComboBox2.setEnabled(false);
            crit2TextField.setEnabled(false);
        }
    }

    private void crit3CheckBox_actionPerformed(ActionEvent e) {
        if (crit3CheckBox.isSelected()) {
            crit3Label.setEnabled(true);
            crit3ComboBox2.setEnabled(true);
            crit3ComboBox1.setEnabled(true);
            crit3TextField.setEnabled(true);
        } else if (crit3CheckBox.isSelected() == false) {
            crit3Label.setEnabled(false);
            crit3ComboBox2.setEnabled(false);
            crit3ComboBox1.setEnabled(false);
            crit3TextField.setEnabled(false);
        }
    }

    private void crit4CheckBox_actionPerformed(ActionEvent e) {
        if (crit4CheckBox.isSelected()) {
            crit4Label.setEnabled(true);
            crit4ComboBox.setEnabled(true);
            crit4TextField.setEnabled(true);
            crit4TextField.setOpaque(true);
        } else if (crit4CheckBox.isSelected() == false) {
            crit4Label.setEnabled(false);
            crit4ComboBox.setEnabled(false);
            crit4TextField.setEnabled(false);
            crit4TextField.setOpaque(false);
        }
    }

    private void crit5CheckBox_actionPerformed(ActionEvent e) {
        if (crit5CheckBox.isSelected()) {
            crit5Label.setEnabled(true);
            crit5ComboBox.setEnabled(true);
            crit5TextField.setEnabled(true);
            crit5TextField.setOpaque(true);
        } else if (crit5CheckBox.isSelected() == false) {
            crit5Label.setEnabled(false);
            crit5ComboBox.setEnabled(false);
            crit5TextField.setEnabled(false);
            crit5TextField.setOpaque(false);
        }
    }

    private void crit6CheckBox_actionPerformed(ActionEvent e) {
        if (crit6CheckBox.isSelected()) {
            crit6Label.setEnabled(true);
            crit6ComboBox.setEnabled(true);
            crit6TextField.setEnabled(true);
            crit6TextField.setOpaque(true);
        } else if (crit6CheckBox.isSelected() == false) {
            crit6Label.setEnabled(false);
            crit6ComboBox.setEnabled(false);
            crit6TextField.setEnabled(false);
            crit6TextField.setOpaque(false);
        }
    }

    private void crit7CheckBox_actionPerformed(ActionEvent e) {
        if (crit7CheckBox.isSelected()) {
            crit7ComboBox.setEnabled(true);
            crit7Label.setEnabled(true);
        } else if (crit7CheckBox.isSelected() == false) {
            crit7ComboBox.setEnabled(false);
            crit7Label.setEnabled(false);
        }
    }

    private void crit8CheckBox_actionPerformed(ActionEvent e) {
        if (crit8CheckBox.isSelected()) {
            crit8Label.setEnabled(true);
            crit8ComboBox.setEnabled(true);
            crit8TextField.setEnabled(true);
            crit8TextField.setOpaque(true);
        } else if (crit8CheckBox.isSelected() == false) {
            crit8Label.setEnabled(false);
            crit8ComboBox.setEnabled(false);
            crit8TextField.setEnabled(false);
            crit8TextField.setOpaque(false);
        }
    }

    private void crit9CheckBox_actionPerformed(ActionEvent e) {
        if (crit9CheckBox.isSelected()) {
            crit9Label.setEnabled(true);
            crit9ComboBox.setEnabled(true);
            crit9TextField.setEnabled(true);
            crit9TextField.setOpaque(true);
        } else if (crit9CheckBox.isSelected() == false) {
            crit9Label.setEnabled(false);
            crit9ComboBox.setEnabled(false);
            crit9TextField.setEnabled(false);
            crit9TextField.setOpaque(false);
        }
    }

    private void crit10CheckBox_actionPerformed(ActionEvent e) {
        if (crit10CheckBox.isSelected()) {
            crit10Label.setEnabled(true);
            crit10ComboBox.setEnabled(true);
            crit10TextField.setEnabled(true);
            crit10TextField.setOpaque(true);
        } else if (crit10CheckBox.isSelected() == false) {
            crit10Label.setEnabled(false);
            crit10ComboBox.setEnabled(false);
            crit10TextField.setEnabled(false);
            crit10TextField.setOpaque(false);
        }
    }

    private void crit11CheckBox_actionPerformed(ActionEvent e) {
        if (crit11CheckBox.isSelected()) {
            crit11Label.setEnabled(true);
            crit11ComboBox.setEnabled(true);
            crit11TextField.setEnabled(true);
            crit11TextField.setOpaque(true);
        } else if (crit11CheckBox.isSelected() == false) {
            crit11Label.setEnabled(false);
            crit11ComboBox.setEnabled(false);
            crit11TextField.setEnabled(false);
            crit11TextField.setOpaque(false);
        }
    }

    /**
   * Criteria holds the settings for the criteria dialog
   */
    protected class Criteria {

        /**holds the settings for the value criteria*/
        protected Object[] value;

        /**holds the settings for the first column value criteria*/
        protected Object[] column1;

        /**holds the settings for the second column value criteria*/
        protected Object[] column2;

        /**holds the settings for the gene name criteria*/
        protected Object[] name;

        /**holds the settings for the gene alias criteria*/
        protected Object[] alias;

        /**holds the settings for the chromosome criteria*/
        protected Object[] chromosome;

        /**holds the settings for the comment criteria*/
        protected Object[] comment;

        /**holds the settings for the biological process criteria*/
        protected Object[] bio;

        /**holds the settings for the molecular function criteria*/
        protected Object[] function;

        /**holds the settings for the cellular comment criteria*/
        protected Object[] cell;

        /**holds the settings for the standard deviation criteria*/
        protected Object[] std;

        /**holds whether to match all (true) or any (false) criteria*/
        protected boolean matching;

        /**
     * Construct the class which holds the settings for the criteria dialog
     */
        public Criteria() {
            value = new Object[5];
            column1 = new Object[4];
            column2 = new Object[4];
            name = new Object[3];
            alias = new Object[3];
            chromosome = new Object[2];
            comment = new Object[3];
            bio = new Object[3];
            function = new Object[3];
            cell = new Object[3];
            std = new Object[3];
            clearAll();
        }

        /**
    * clears all the settings
    */
        public void clearAll() {
            matching = true;
            clearValue();
            clearColumnValue1();
            clearColumnValue2();
            clearComment();
            clearGeneAlias();
            clearGeneName();
            clearMolecularFunction();
            clearStdDev();
            clearChromosome();
            clearBioProcess();
            clearCellComment();
        }

        /**
   * changes the settings for the value criteria
   * @param style whether looking for minimum (0), maximum (1), or average (2) value
   * @param val whether looking for value (true) or jump in value (false)
   * @param less whether looking for less than (true) or greater than (false) a value
   * @param number criteria value
   */
        public void setValue(int style, boolean val, boolean less, double number) {
            value[0] = new Boolean(true);
            value[1] = new Integer(style);
            value[2] = new Boolean(val);
            value[3] = new Boolean(less);
            value[4] = new Double(number);
        }

        /**
   * clears the settings for the value criteria
   */
        public void clearValue() {
            value[0] = new Boolean(false);
            value[1] = new Integer(0);
            value[2] = new Boolean(false);
            value[3] = new Boolean(false);
            value[4] = new Double(Double.NaN);
        }

        /**
   * changes the settings for the first column value criteria
   * @param column column number
   * @param less whether looking for less than (true) or greater than (false) a value
   * @param number criteria value
   */
        public void setColumnValue1(int column, boolean less, double number) {
            column1[0] = new Boolean(true);
            column1[1] = new Integer(column);
            column1[2] = new Boolean(less);
            column1[3] = new Double(number);
        }

        /**
   * clears the settings for the first column value criteria
   */
        public void clearColumnValue1() {
            column1[0] = new Boolean(false);
            column1[1] = new Integer(0);
            column1[2] = new Boolean(false);
            column1[3] = new Double(Double.NaN);
        }

        /**
   * changes the settings for the second column value criteria
   * @param column column number
   * @param less whether looking for less than (true) or greater than (false) a value
   * @param number criteria value
   */
        public void setColumnValue2(int column, boolean less, double number) {
            column1[0] = new Boolean(true);
            column1[1] = new Integer(column);
            column1[2] = new Boolean(less);
            column1[3] = new Double(number);
        }

        /**
   * clears the settings for the second column value criteria
   */
        public void clearColumnValue2() {
            column2[0] = new Boolean(false);
            column2[1] = new Integer(0);
            column2[2] = new Boolean(false);
            column2[3] = new Double(Double.NaN);
        }

        /**
   * changes the settings for the standard deviation criteria
   * @param less whether looking for less than (true) or greater than (false) a value
   * @param number criteria value
   */
        public void setStdDev(boolean less, double number) {
            std[0] = new Boolean(true);
            std[1] = new Boolean(less);
            std[2] = new Double(number);
        }

        /**
   * clears the settings for the standard deviation criteria
   */
        public void clearStdDev() {
            std[0] = new Boolean(false);
            std[1] = new Boolean(false);
            std[2] = new Double(Double.NaN);
        }

        /**
   * changes the settings for the gene name criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param gname criteria string
   */
        public void setGeneName(boolean contains, String gname) {
            name[0] = new Boolean(true);
            name[1] = new Boolean(contains);
            name[2] = new String(gname);
        }

        /**
   * clears the settings for the gene name criteria
   */
        public void clearGeneName() {
            name[0] = new Boolean(false);
            name[1] = new Boolean(false);
            name[2] = new String("");
        }

        /**
   * changes the settings for the gene alias criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param al criteria string
   */
        public void setGeneAlias(boolean contains, String al) {
            alias[0] = new Boolean(true);
            alias[1] = new Boolean(contains);
            alias[2] = new String(al);
        }

        /**
   * clears the settings for the gene alias criteria
   */
        public void clearGeneAlias() {
            alias[0] = new Boolean(false);
            alias[1] = new Boolean(false);
            alias[2] = new String("");
        }

        /**
   * changes the settings for the chromosome criteria
   * @param chrom chromosome to select
   */
        public void setChromosome(int chrom) {
            chromosome[0] = new Boolean(true);
            chromosome[1] = new Integer(chrom);
        }

        /**
   * clears the settings for the chromosome criteria
   */
        public void clearChromosome() {
            chromosome[0] = new Boolean(false);
            chromosome[1] = new Integer(0);
        }

        /**
   * changes the settings for the comment criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param com criteria string
   */
        public void setComment(boolean contains, String com) {
            comment[0] = new Boolean(true);
            comment[1] = new Boolean(contains);
            comment[2] = new String(com);
        }

        /**
   * clears the settings for the comment criteria
   */
        public void clearComment() {
            comment[0] = new Boolean(false);
            comment[1] = new Boolean(false);
            comment[2] = new String("");
        }

        /**
   * changes the settings for the biological process criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param biol criteria string
   */
        public void setBioProcess(boolean contains, String biol) {
            bio[0] = new Boolean(true);
            bio[1] = new Boolean(contains);
            bio[2] = new String(biol);
        }

        /**
   * clears the settings for the biological process criteria
   */
        public void clearBioProcess() {
            bio[0] = new Boolean(false);
            bio[1] = new Boolean(false);
            bio[2] = new String("");
        }

        /**
   * changes the settings for the molecular function criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param func criteria string
   */
        public void setMolecularFunction(boolean contains, String func) {
            function[0] = new Boolean(true);
            function[1] = new Boolean(contains);
            function[2] = new String(func);
        }

        /**
   * clears the settings for the molecular function criteria
   */
        public void clearMolecularFunction() {
            function[0] = new Boolean(false);
            function[1] = new Boolean(false);
            function[2] = new String("");
        }

        /**
   * clears the setting for matching
   * @param matching whether necessary to match all (true) or any (false) criteria
   */
        public void setMatching(boolean matching) {
            this.matching = matching;
        }

        /**
   * changes the settings for the cellular comment criteria
   * @param contains whether name contains (true) or does not contain (false) a string
   * @param c criteria string
   */
        public void setCellComment(boolean contains, String c) {
            cell[0] = new Boolean(true);
            cell[1] = new Boolean(contains);
            cell[2] = new String(c);
        }

        /**
   * clears the settings for the cellular comment criteria
   */
        public void clearCellComment() {
            cell[0] = new Boolean(false);
            cell[1] = new Boolean(false);
            cell[2] = new String("");
        }
    }

    private void clearButton_actionPerformed(ActionEvent e) {
        clearAll();
    }

    private void addKeyListenerRecursively(Component c) {
        c.removeKeyListener(this);
        c.addKeyListener(this);
        if (c instanceof Container) {
            Container cont = (Container) c;
            Component[] children = cont.getComponents();
            for (int i = 0; i < children.length; i++) {
                addKeyListenerRecursively(children[i]);
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_MASK).getKeyCode() && e.isControlDown()) {
            this.dispose();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
