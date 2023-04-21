package kr.ac.ssu.imc.durubi.report.designer.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import kr.ac.ssu.imc.durubi.report.designer.*;
import kr.ac.ssu.imc.durubi.report.designer.components.*;
import kr.ac.ssu.imc.durubi.report.designer.manager.DRManager;

@SuppressWarnings("serial")
public class DRSImageDialog extends JDialog implements ActionListener {

    private int nDialogState;

    private JPanel tMainPane;

    private JTextField tImageURL;

    private String ImageURL;

    private JButton tOk, tCancel;

    public static final int TD_OK = 0x0001;

    public static final int TD_CANCELED = 0x0002;

    private static final int TD_WIDTH = 380;

    private static final int TD_HEIGHT = 135;

    private static final int TD_XAXIS1 = 40;

    private static final int TD_YAXIS1 = 10;

    private static final int TD_YAXIS2 = 40;

    private String sVariableLists[] = null;

    private JComboBox tVariables;

    private int sType = 0;

    private String VName = "";

    public DRSImageDialog(Window tOwner) {
        super(tOwner, Dialog.ModalityType.TOOLKIT_MODAL);
        Vector<DROVariable> tVarsList = DRManager.getActiveReport().tListOfVars;
        sVariableLists = new String[tVarsList.size() + 1];
        sVariableLists[0] = "";
        for (int i = 0; i < tVarsList.size(); i++) sVariableLists[i + 1] = ((DROVariable) tVarsList.get(i)).getName();
        initDialog("", 0, null);
    }

    public DRSImageDialog(Window tOwner, String ImP, int IType, String VI) {
        super(tOwner, Dialog.ModalityType.TOOLKIT_MODAL);
        setBounds(200, 200, TD_WIDTH, TD_HEIGHT);
        setModal(true);
        tMainPane = new JPanel();
        Vector<DROVariable> tVarsList = DRManager.getActiveReport().tListOfVars;
        sVariableLists = new String[tVarsList.size() + 1];
        sVariableLists[0] = "";
        for (int i = 0; i < tVarsList.size(); i++) sVariableLists[i + 1] = ((DROVariable) tVarsList.get(i)).getName();
        sType = IType;
        setContentPane(tMainPane);
        tMainPane.setLayout(null);
        initDialog(ImP, IType, VI);
    }

    public int showDialog() {
        setVisible(true);
        return nDialogState;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public String getVN() {
        return VName;
    }

    public int getSType() {
        return sType;
    }

    private void processOK() {
        ImageURL = tImageURL.getText();
        int VN;
        VN = tVariables.getSelectedIndex();
        if (sType == 0) {
            if (ImageURL.length() < 1) {
                JOptionPane.showMessageDialog(this, "�׸� URL�� �Է��ϼ���!", "�˸�", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        } else {
            if (VN == 0) {
                JOptionPane.showMessageDialog(this, "������ ������ �ּ���!", "�˸�", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        VName = sVariableLists[VN];
        setVisible(false);
        nDialogState = DRSImageDialog.TD_OK;
    }

    private void initDialog(String ImP, int IType, String pVN) {
        setBounds(200, 200, TD_WIDTH, TD_HEIGHT);
        setModal(true);
        tMainPane = new JPanel();
        setContentPane(tMainPane);
        tMainPane.setLayout(null);
        JRadioButton URLType = new JRadioButton();
        URLType.setActionCommand("url");
        JRadioButton VarType = new JRadioButton();
        VarType.setActionCommand("var");
        ButtonGroup groupType = new ButtonGroup();
        groupType.add(URLType);
        groupType.add(VarType);
        URLType.addActionListener(this);
        VarType.addActionListener(this);
        URLType.setBounds(15, TD_YAXIS1 + 5, 10, 10);
        VarType.setBounds(15, TD_YAXIS2 + 5, 10, 10);
        tMainPane.add(URLType);
        tMainPane.add(VarType);
        JLabel tLbRows = new JLabel("�׸� URL");
        tLbRows.setBounds(TD_XAXIS1, TD_YAXIS1, 70, 22);
        tMainPane.add(tLbRows);
        tImageURL = createURLTB(ImP);
        tImageURL.setBounds(TD_XAXIS1 + 70, TD_YAXIS1, 250, 22);
        tMainPane.add(tImageURL);
        JLabel tLbRows2 = new JLabel("��   ��");
        tLbRows2.setBounds(TD_XAXIS1, TD_YAXIS2, 70, 22);
        tMainPane.add(tLbRows2);
        tVariables = new JComboBox();
        int ps = 0;
        for (int j = 0; j < sVariableLists.length; j++) {
            tVariables.addItem(sVariableLists[j]);
            if (sVariableLists[j].equalsIgnoreCase(pVN)) ps = j;
        }
        tVariables.setSelectedIndex(ps);
        tVariables.setBounds(TD_XAXIS1 + 70, TD_YAXIS2, 250, 22);
        tMainPane.add(tVariables);
        if (IType == 0) {
            URLType.setSelected(true);
            tImageURL.setEnabled(true);
            tVariables.setEnabled(false);
        } else {
            VarType.setSelected(true);
            tImageURL.setEnabled(false);
            tVariables.setEnabled(true);
        }
        tOk = createOk();
        tOk.setBounds(TD_XAXIS1 + 60, TD_YAXIS2 + 30, 80, 22);
        tMainPane.add(tOk);
        tCancel = createCancel();
        tCancel.setBounds(TD_XAXIS1 + 190, TD_YAXIS2 + 30, 80, 22);
        tMainPane.add(tCancel);
        return;
    }

    private JTextField createURLTB(String ImP) {
        JTextField tReturn = new JTextField(ImP);
        tReturn.setColumns(40);
        return tReturn;
    }

    private JButton createOk() {
        JButton tReturn = new JButton("Ȯ  ��");
        tReturn.setActionCommand("ok");
        tReturn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                processOK();
            }
        });
        return tReturn;
    }

    private JButton createCancel() {
        JButton tReturn = new JButton("��  ��");
        tReturn.setActionCommand("cancel");
        tReturn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JButton tButton = (JButton) e.getSource();
                if (tButton.getActionCommand().equals("cancel")) {
                    setVisible(false);
                    nDialogState = DRSImageDialog.TD_CANCELED;
                }
            }
        });
        return tReturn;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("url")) {
            sType = 0;
            tImageURL.setEnabled(true);
            tVariables.setEnabled(false);
        } else {
            sType = 1;
            tImageURL.setEnabled(false);
            tVariables.setEnabled(true);
        }
    }
}
