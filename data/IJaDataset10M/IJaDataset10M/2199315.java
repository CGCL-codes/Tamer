package kr.ac.ssu.imc.whitehole.report.designer.items.rdsimpleList;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;
import kr.ac.ssu.imc.whitehole.report.designer.*;
import kr.ac.ssu.imc.whitehole.report.designer.items.RDOQuery;
import kr.ac.ssu.imc.whitehole.report.designer.items.RDOUserDataSet;
import kr.ac.ssu.imc.whitehole.report.designer.items.RDOVariable;

public class RDSLLabelPropertyDialog extends JDialog {

    /** ���̾�α� �ڽ��� ������ ���� ����*/
    private int nDialogState;

    /** ���̾�α� �ڽ��� ���¸� ��Ÿ���� �����*/
    public static final int TD_OK = 0x0001, TD_CANCELED = 0x0002;

    /** Ÿ�� ���*/
    public static final int EMPTY = 0x00, LABEL = 0x05, QUERY_RESULT = 0x06, FUNCTION = 0x07, IMAGE = 0x08, INDEX = 0xff, SUMMARY = 0xfe, PARAMETER = 0x09;

    public static final int STATIC = 0x00, DYNAMIC = 0x01;

    private RDSLLabel selectedLabel;

    private JPanel mainPanel;

    private JPanel buttonPanel;

    private JButton ok;

    private JButton cancel;

    private JTabbedPane tabbedPane;

    private JTextField thickness;

    private JCheckBox north;

    private JCheckBox south;

    private JCheckBox east;

    private JCheckBox west;

    private RDSLFuncPanel fPanel;

    private Window owner;

    private RDSimpleListPanel simpleListPanel = null;

    private RDCSimpleListPanel cSimpleListPanel = null;

    private JComboBox fields = null;

    private JComboBox variables = null;

    private JCheckBox checkBox = null;

    private int lType;

    private String summFunc;

    private String summOper;

    public RDSLLabelPropertyDialog(Window owner, RDSimpleListPanel simpleListPanel) {
        super(owner, "���̺� �Ӽ� ����", Dialog.ModalityType.TOOLKIT_MODAL);
        this.owner = owner;
        nDialogState = TD_CANCELED;
        this.simpleListPanel = simpleListPanel;
        this.selectedLabel = simpleListPanel.getSelectedLabel();
        initDialog(false);
        setLocation(owner.getX() + (owner.getWidth() - getWidth()) / 2, owner.getY() + (owner.getHeight() - getHeight()) / 2);
        setSize(300, 300);
        setResizable(false);
    }

    public RDSLLabelPropertyDialog(Window owner, RDSimpleListPanel simpleListPanel, boolean isGroupped) {
        super(owner, "���̺� �Ӽ� ����", Dialog.ModalityType.TOOLKIT_MODAL);
        this.owner = owner;
        nDialogState = TD_CANCELED;
        this.simpleListPanel = simpleListPanel;
        this.selectedLabel = simpleListPanel.getSelectedLabel();
        initDialog(isGroupped);
        setLocation(owner.getX() + (owner.getWidth() - getWidth()) / 2, owner.getY() + (owner.getHeight() - getHeight()) / 2);
        setSize(300, 300);
        setResizable(false);
    }

    public RDSLLabelPropertyDialog(Window owner, RDCSimpleListPanel cSimpleListPanel) {
        super(owner, "���̺� �Ӽ� ����", Dialog.ModalityType.TOOLKIT_MODAL);
        this.owner = owner;
        nDialogState = TD_CANCELED;
        this.cSimpleListPanel = cSimpleListPanel;
        this.selectedLabel = cSimpleListPanel.getSelectedLabel();
        initDialogForC(false);
        setLocation(owner.getX() + (owner.getWidth() - getWidth()) / 2, owner.getY() + (owner.getHeight() - getHeight()) / 2);
        setSize(300, 300);
        setResizable(false);
    }

    public RDSLLabelPropertyDialog(Window owner, RDCSimpleListPanel cSimpleListPanel, boolean isGroupped) {
        super(owner, "���̺� �Ӽ� ����", Dialog.ModalityType.TOOLKIT_MODAL);
        this.owner = owner;
        nDialogState = TD_CANCELED;
        this.cSimpleListPanel = cSimpleListPanel;
        this.selectedLabel = cSimpleListPanel.getSelectedLabel();
        initDialogForC(isGroupped);
        setLocation(owner.getX() + (owner.getWidth() - getWidth()) / 2, owner.getY() + (owner.getHeight() - getHeight()) / 2);
        setSize(300, 300);
        setResizable(false);
    }

    private void initDialogForC(boolean isGroupped) {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane(JTabbedPane.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        JPanel border = new JPanel(null);
        JLabel thicknessLabel = new JLabel("�� �β� : ");
        thicknessLabel.setBounds(10, 10, 50, 20);
        border.add(thicknessLabel);
        thickness = new JTextField(new Integer(selectedLabel.oModel.oBorder.nBorderWidth).toString());
        thickness.setBounds(70, 10, 50, 20);
        border.add(thickness);
        JPanel visible = new JPanel(new GridLayout(4, 1));
        visible.setBorder(new TitledBorder("�׵θ� ǥ�� ����"));
        visible.setBounds(10, 40, 110, 110);
        border.add(visible);
        north = new JCheckBox("��", selectedLabel.oModel.oBorder.bBorderNorth);
        visible.add(north);
        south = new JCheckBox("�Ʒ�", selectedLabel.oModel.oBorder.bBorderSouth);
        visible.add(south);
        west = new JCheckBox("����", selectedLabel.oModel.oBorder.bBorderWest);
        visible.add(west);
        east = new JCheckBox("������", selectedLabel.oModel.oBorder.bBorderEast);
        visible.add(east);
        JPanel type = new JPanel(null);
        if (cSimpleListPanel != null) {
            this.fPanel = new RDSLFuncPanel(cSimpleListPanel);
        } else this.fPanel = new RDSLFuncPanel(cSimpleListPanel);
        this.fPanel.setEnabled(false);
        JPanel title = new JPanel(new GridLayout(2, 2));
        title.setBorder(new TitledBorder("���̺� Ÿ��"));
        title.setBounds(10, 10, 275, 80);
        type.add(title);
        fields = new JComboBox(getColumnNamesForC());
        fields.setBounds(10, 100, 275, 20);
        type.add(fields);
        variables = new JComboBox(getVariableNamesForC());
        variables.setBounds(10, 132, 275, 20);
        type.add(variables);
        if (getVariableNamesForC().size() < 1) variables.setEnabled(false);
        ButtonGroup bg = new ButtonGroup();
        JRadioButton pRButton = new JRadioButton("�Ű�����");
        pRButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                lType = RDSLLabelPropertyDialog.PARAMETER;
                selectParameterMode();
            }
        });
        bg.add(pRButton);
        title.add(pRButton);
        JRadioButton tRButton = new JRadioButton("�ؽ�Ʈ");
        tRButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                lType = RDSLLabelPropertyDialog.LABEL;
                selectStaticMode();
            }
        });
        bg.add(tRButton);
        title.add(tRButton);
        JRadioButton iRButton = new JRadioButton("�ε���");
        iRButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                lType = RDSLLabelPropertyDialog.INDEX;
                selectStaticMode();
            }
        });
        bg.add(iRButton);
        title.add(iRButton);
        if (cSimpleListPanel.getSelectedBand().getType() == RDSLBand.DATA) {
            JRadioButton dRButton = new JRadioButton("���ǹ�");
            dRButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    lType = RDSLLabelPropertyDialog.QUERY_RESULT;
                    selectDynamicMode();
                }
            });
            bg.add(dRButton);
            title.add(dRButton);
            JRadioButton imRButton = new JRadioButton("�̹��� ���ǹ� ���̺�");
            imRButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    lType = RDSLLabelPropertyDialog.IMAGE;
                    selectDynamicMode();
                }
            });
            bg.add(imRButton);
            title.add(imRButton);
            if (cSimpleListPanel != null) {
                Report tGDDoc = ReportDesigner.getActiveReport();
                if (tGDDoc.udsUsed()) dRButton.setText("�����ͼ�");
                imRButton.setEnabled(false);
            }
            if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.QUERY_RESULT) {
                dRButton.setSelected(true);
                lType = RDSLAbstractLabelModel.QUERY_RESULT;
                fields.setSelectedItem(selectedLabel.oModel.sText);
            }
            if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.IMAGE) {
                imRButton.setSelected(true);
                lType = RDSLAbstractLabelModel.IMAGE;
                fields.setSelectedItem(selectedLabel.oModel.sText);
            }
            if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.INDEX) {
                iRButton.setSelected(true);
                lType = RDSLAbstractLabelModel.INDEX;
                fields.setEnabled(false);
            }
        }
        if (cSimpleListPanel.getSelectedBand().getType() == RDSLBand.FOOTER) {
            JRadioButton sRButton = new JRadioButton("��� �ʵ�");
            sRButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fPanel.setEnabled(true);
                    lType = RDSLLabelPropertyDialog.SUMMARY;
                    selectStaticMode();
                }
            });
            if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.SUMMARY) {
                sRButton.setSelected(true);
                lType = RDSLAbstractLabelModel.SUMMARY;
                fields.setEnabled(false);
                this.summFunc = selectedLabel.oModel.getSummFunc();
                this.summOper = selectedLabel.oModel.getSummOper();
                this.fPanel.funList.setSelectedItem(this.summFunc + "()");
                this.fPanel.colList.setSelectedItem(this.summOper);
            }
            bg.add(sRButton);
            title.add(sRButton);
        }
        if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.LABEL) {
            tRButton.setSelected(true);
            lType = RDSLAbstractLabelModel.LABEL;
            fields.setEnabled(false);
        }
        if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.PARAMETER) {
            pRButton.setSelected(true);
            lType = RDSLAbstractLabelModel.PARAMETER;
            fields.setEnabled(false);
        }
        if (!isGroupped) tabbedPane.add("Ÿ��", type);
        if (cSimpleListPanel.getSelectedBand().getType() == RDSLBand.FOOTER && !isGroupped) {
            tabbedPane.add("��� �ʵ�", fPanel);
        }
        tabbedPane.add("�׵θ�", border);
        JPanel labelProperty = new JPanel(null);
        checkBox = new JCheckBox("���̺� �պ�", selectedLabel.oModel.willBeMerged);
        checkBox.setBounds(10, 10, 100, 30);
        labelProperty.add(checkBox);
        if (selectedLabel.oModel.nType != RDSLAbstractLabelModel.QUERY_RESULT) {
            checkBox.setSelected(false);
            checkBox.setEnabled(false);
        }
        tabbedPane.add("�Ӽ�", labelProperty);
        buttonPanel = new JPanel(new FlowLayout());
        ok = new JButton("Ȯ��");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ok_actionPerformed(e);
            }
        });
        buttonPanel.add(ok);
        cancel = new JButton("���");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancel_actionPerformed(e);
            }
        });
        buttonPanel.add(cancel);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void initDialog(boolean isGroupped) {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane(JTabbedPane.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        JPanel border = new JPanel(null);
        JLabel thicknessLabel = new JLabel("�� �β� : ");
        thicknessLabel.setBounds(10, 10, 50, 20);
        border.add(thicknessLabel);
        thickness = new JTextField(new Integer(selectedLabel.oModel.oBorder.nBorderWidth).toString());
        thickness.setBounds(70, 10, 50, 20);
        border.add(thickness);
        JPanel visible = new JPanel(new GridLayout(4, 1));
        visible.setBorder(new TitledBorder("�׵θ� ǥ�� ����"));
        visible.setBounds(10, 40, 110, 110);
        border.add(visible);
        north = new JCheckBox("��", selectedLabel.oModel.oBorder.bBorderNorth);
        visible.add(north);
        south = new JCheckBox("�Ʒ�", selectedLabel.oModel.oBorder.bBorderSouth);
        visible.add(south);
        west = new JCheckBox("����", selectedLabel.oModel.oBorder.bBorderWest);
        visible.add(west);
        east = new JCheckBox("������", selectedLabel.oModel.oBorder.bBorderEast);
        visible.add(east);
        JPanel type = new JPanel(null);
        if (simpleListPanel != null) {
            this.fPanel = new RDSLFuncPanel(simpleListPanel);
        } else this.fPanel = new RDSLFuncPanel(cSimpleListPanel);
        this.fPanel.setEnabled(false);
        JPanel title = new JPanel(new GridLayout(2, 2));
        title.setBorder(new TitledBorder("���̺� Ÿ��"));
        title.setBounds(10, 10, 275, 80);
        type.add(title);
        fields = new JComboBox(getColumnNames());
        fields.setBounds(10, 100, 275, 20);
        type.add(fields);
        variables = new JComboBox(getVariableNames());
        variables.setBounds(10, 132, 275, 20);
        type.add(variables);
        if (getVariableNames().size() < 1) variables.setEnabled(false);
        ButtonGroup bg = new ButtonGroup();
        JRadioButton pRButton = new JRadioButton("�Ű�����");
        pRButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                lType = RDSLLabelPropertyDialog.PARAMETER;
                selectParameterMode();
            }
        });
        bg.add(pRButton);
        title.add(pRButton);
        JRadioButton tRButton = new JRadioButton("�ؽ�Ʈ");
        tRButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                lType = RDSLLabelPropertyDialog.LABEL;
                selectStaticMode();
            }
        });
        bg.add(tRButton);
        title.add(tRButton);
        if (simpleListPanel.getSelectedBand().getType() == RDSLBand.DATA) {
            JRadioButton dRButton = new JRadioButton("���ǹ�");
            dRButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    lType = RDSLLabelPropertyDialog.QUERY_RESULT;
                    selectDynamicMode();
                }
            });
            JRadioButton iRButton = new JRadioButton("�ε���");
            iRButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    lType = RDSLLabelPropertyDialog.INDEX;
                    selectStaticMode();
                }
            });
            bg.add(iRButton);
            title.add(iRButton);
            bg.add(dRButton);
            title.add(dRButton);
            JRadioButton imRButton = new JRadioButton("DB�̹���");
            imRButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    lType = RDSLLabelPropertyDialog.IMAGE;
                    selectDynamicMode();
                }
            });
            bg.add(imRButton);
            title.add(imRButton);
            if (simpleListPanel != null) {
                Report tGDDoc = ReportDesigner.getActiveReport();
                if (tGDDoc.udsUsed()) dRButton.setText("�����ͼ�");
                imRButton.setEnabled(false);
            }
            if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.QUERY_RESULT) {
                dRButton.setSelected(true);
                lType = RDSLAbstractLabelModel.QUERY_RESULT;
                fields.setSelectedItem(selectedLabel.oModel.sText);
            }
            if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.IMAGE) {
                imRButton.setSelected(true);
                lType = RDSLAbstractLabelModel.IMAGE;
                fields.setSelectedItem(selectedLabel.oModel.sText);
            }
            if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.INDEX) {
                iRButton.setSelected(true);
                lType = RDSLAbstractLabelModel.INDEX;
                fields.setEnabled(false);
            }
        }
        if (simpleListPanel.getSelectedBand().getType() == RDSLBand.FOOTER) {
            JRadioButton sRButton = new JRadioButton("��� �ʵ�");
            sRButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fPanel.setEnabled(true);
                    lType = RDSLLabelPropertyDialog.SUMMARY;
                    selectStaticMode();
                }
            });
            if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.SUMMARY) {
                sRButton.setSelected(true);
                lType = RDSLAbstractLabelModel.SUMMARY;
                fields.setEnabled(false);
                this.summFunc = selectedLabel.oModel.getSummFunc();
                this.summOper = selectedLabel.oModel.getSummOper();
                this.fPanel.funList.setSelectedItem(this.summFunc + "()");
                this.fPanel.colList.setSelectedItem(this.summOper);
            }
            bg.add(sRButton);
            title.add(sRButton);
        }
        if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.LABEL) {
            tRButton.setSelected(true);
            lType = RDSLAbstractLabelModel.LABEL;
            fields.setEnabled(false);
        }
        if (selectedLabel.oModel.nType == RDSLAbstractLabelModel.PARAMETER) {
            pRButton.setSelected(true);
            lType = RDSLAbstractLabelModel.PARAMETER;
            fields.setEnabled(false);
        }
        if (!isGroupped) tabbedPane.add("Ÿ��", type);
        if (simpleListPanel.getSelectedBand().getType() == RDSLBand.FOOTER && !isGroupped) {
            tabbedPane.add("��� �ʵ�", fPanel);
        }
        tabbedPane.add("�׵θ�", border);
        JPanel labelProperty = new JPanel(null);
        checkBox = new JCheckBox("���̺� �պ�", selectedLabel.oModel.willBeMerged);
        checkBox.setBounds(10, 10, 100, 30);
        labelProperty.add(checkBox);
        if (selectedLabel.oModel.nType != RDSLAbstractLabelModel.QUERY_RESULT) {
            checkBox.setSelected(false);
            checkBox.setEnabled(false);
        }
        tabbedPane.add("�Ӽ�", labelProperty);
        buttonPanel = new JPanel(new FlowLayout());
        ok = new JButton("Ȯ��");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ok_actionPerformed(e);
            }
        });
        buttonPanel.add(ok);
        cancel = new JButton("���");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancel_actionPerformed(e);
            }
        });
        buttonPanel.add(cancel);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    void ok_actionPerformed(ActionEvent e) {
        for (int i = 0; i < thickness.getText().length(); i++) {
            if (!Character.isDigit(thickness.getText().charAt(i))) {
                JOptionPane.showMessageDialog(owner, "�ڿ����� �Է��� �ֽʽÿ�!", "�˸�", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        if (new Integer(thickness.getText()).intValue() < 0) {
            JOptionPane.showMessageDialog(owner, "�� ������ �ּҰ��� 0�Դϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.summFunc = this.fPanel.getSummFuncName();
        finishDialog();
    }

    void cancel_actionPerformed(ActionEvent e) {
        cancelDialog();
    }

    /** ���̾�α� �ڽ��� ȭ�鿡 �����ش�
	 *  @return ���̾�α� �ڽ��� ���� ���� �ǵ�����.
	 */
    public int showDialog() {
        this.show();
        return nDialogState;
    }

    public void cancelDialog() {
        nDialogState = TD_CANCELED;
        this.setVisible(false);
    }

    public void finishDialog() {
        if (fields.getSelectedIndex() == 0 && fields.isEnabled()) {
            JOptionPane.showMessageDialog(ReportDesigner.getDialogOwner(), "�ݵ�� �ʵ带 ������ �ֽʽÿ�!", "�˸�", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        nDialogState = TD_OK;
        this.setVisible(false);
    }

    public int getThickness() {
        return new Integer(thickness.getText()).intValue();
    }

    public boolean isNorthLineVisible() {
        return north.isSelected();
    }

    public boolean isSouthLineVisible() {
        return south.isSelected();
    }

    public boolean isWestLineVisible() {
        return west.isSelected();
    }

    public boolean isEastLineVisible() {
        return east.isSelected();
    }

    /** ������ü�� �ʵ���� ��ȯ�Ѵ� */
    private Vector getColumnNames() {
        String dataObjectName = simpleListPanel.model.getDataName();
        Vector columnNames = new Vector();
        Report tGDDoc = ReportDesigner.getActiveReport();
        if (!tGDDoc.udsUsed()) {
            for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) {
                if (dataObjectName.equals(((RDOQuery) tGDDoc.tListOfQueries.get(i)).getName())) {
                    columnNames.addElement("");
                    for (int j = 0; j < ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getResultColumnCount(); j++) {
                        String temp = dataObjectName + "." + ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getColumnName(j);
                        boolean alreadyExist = false;
                        for (int k = 0; k < simpleListPanel.getSelectedBand().getComponentCount(); k++) if (simpleListPanel.getSelectedBand().getComponentAt(k) instanceof RDSLLabel) {
                            RDSLLabel label = (RDSLLabel) simpleListPanel.getSelectedBand().getComponentAt(k);
                        }
                        if (!alreadyExist) columnNames.addElement(dataObjectName + "." + ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getColumnName(j));
                    }
                    break;
                }
            }
        } else {
            for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) {
                if (dataObjectName.equals(((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getName())) {
                    columnNames.addElement("");
                    for (int j = 0; j < ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getResultColumnCount(); j++) {
                        String temp = dataObjectName + "." + ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getColumnName(j);
                        boolean alreadyExist = false;
                        for (int k = 0; k < simpleListPanel.getSelectedBand().getComponentCount(); k++) if (simpleListPanel.getSelectedBand().getComponentAt(k) instanceof RDSLLabel) {
                            RDSLLabel label = (RDSLLabel) simpleListPanel.getSelectedBand().getComponentAt(k);
                        }
                        if (!alreadyExist) columnNames.addElement(dataObjectName + "." + ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getColumnName(j));
                    }
                    break;
                }
            }
        }
        return columnNames;
    }

    private Vector getColumnNamesForC() {
        String dataObjectName = cSimpleListPanel.model.getDataName();
        Vector columnNames = new Vector();
        Report tGDDoc = ReportDesigner.getActiveReport();
        if (tGDDoc.udsUsed()) for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) {
            if (dataObjectName.equals(((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getName())) {
                columnNames.addElement("");
                for (int j = 0; j < ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getResultColumnCount(); j++) {
                    String temp = dataObjectName + "." + ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getColumnName(j);
                    boolean alreadyExist = false;
                    for (int k = 0; k < cSimpleListPanel.getSelectedBand().getComponentCount(); k++) if (cSimpleListPanel.getSelectedBand().getComponentAt(k) instanceof RDSLLabel) {
                        RDSLLabel label = (RDSLLabel) cSimpleListPanel.getSelectedBand().getComponentAt(k);
                    }
                    if (!alreadyExist) columnNames.addElement(dataObjectName + "." + ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getColumnName(j));
                }
                break;
            }
        } else for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) {
            if (dataObjectName.equals(((RDOQuery) tGDDoc.tListOfQueries.get(i)).getName())) {
                columnNames.addElement("");
                for (int j = 0; j < ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getResultColumnCount(); j++) {
                    String temp = dataObjectName + "." + ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getColumnName(j);
                    boolean alreadyExist = false;
                    for (int k = 0; k < cSimpleListPanel.getSelectedBand().getComponentCount(); k++) if (cSimpleListPanel.getSelectedBand().getComponentAt(k) instanceof RDSLLabel) {
                        RDSLLabel label = (RDSLLabel) cSimpleListPanel.getSelectedBand().getComponentAt(k);
                    }
                    if (!alreadyExist) columnNames.addElement(dataObjectName + "." + ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getColumnName(j));
                }
                break;
            }
        }
        return columnNames;
    }

    /** �������� ��ȯ�Ѵ� */
    private Vector getVariableNames() {
        Vector variableNames = new Vector();
        Report tGDDoc = ReportDesigner.getActiveReport();
        for (int i = 0; i < tGDDoc.tListOfVars.size(); i++) variableNames.addElement(((RDOVariable) tGDDoc.tListOfVars.get(i)).getName());
        return variableNames;
    }

    private Vector getVariableNamesForC() {
        Vector variableNames = new Vector();
        Report tGDDoc = ReportDesigner.getActiveReport();
        for (int i = 0; i < tGDDoc.tListOfVars.size(); i++) variableNames.addElement(((RDOVariable) tGDDoc.tListOfVars.get(i)).getName());
        return variableNames;
    }

    private void selectParameterMode() {
        fields.setEnabled(false);
    }

    private void selectStaticMode() {
        if (fields.getModel().getSize() != 0) fields.setSelectedIndex(0);
        fields.setEnabled(false);
    }

    private void selectDynamicMode() {
        fields.setEnabled(true);
        fields.setSelectedItem(selectedLabel.oModel.sText);
    }

    public int getType() {
        return this.lType;
    }

    public String getField() {
        return fields.getSelectedItem().toString();
    }

    public String getVariableName() {
        return variables.getSelectedItem().toString();
    }

    public String getSummFunc() {
        return this.fPanel.getSummFuncName();
    }

    public String getSummOper() {
        return this.fPanel.getSummOperatee();
    }

    public boolean willBeMerged() {
        return checkBox.isSelected();
    }
}
