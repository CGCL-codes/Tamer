package kr.ac.ssu.imc.whitehole.report.designer.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import kr.ac.ssu.imc.whitehole.report.designer.*;
import kr.ac.ssu.imc.whitehole.report.designer.items.RDOQuery;
import kr.ac.ssu.imc.whitehole.report.designer.items.RDOUserDataSet;
import kr.ac.ssu.imc.whitehole.report.designer.items.rdmaskings.*;

/** ���� ���̺? �ʿ��� ������ �����ϱ� ���� ���̾�α� �ڽ� Ŭ���� */
@SuppressWarnings("serial")
public class RDDWordBoxDialog extends JDialog {

    /** ���̾�α� �ڽ��� ������ ���� ���� */
    private int nDialogState;

    /** �޺� �ڽ��� ��� �ڵ����� ���콺 �̹�Ʈ�� ����Ǵ� ���� �����ϱ� ���� �� */
    private boolean bQueriesInit;

    private JPanel tMainPane;

    private JComboBox tQueriesList, tFieldsList;

    JLabel tLbQuery;

    private JButton tMask, tOk, tCancel;

    private RDMaskingModel maskOpt;

    /** ���̾�α� �ڽ��� ���¸� ��Ÿ���� ����� */
    public static final int TD_OK = 0x0001, TD_CANCELED = 0x0002;

    private static final int TD_WIDTH = 330;

    private static final int TD_HEIGHT = 130;

    private static final int TD_XAXIS1 = 10, TD_XAXIS2 = 100;

    private static final int TD_YAXIS1 = 10, TD_YAXIS2 = 40;

    private String dLabel = "���ǹ�";

    /**
	 * ���� �ۻ��ڸ� ���� ���̾�α� �ڽ��� ���Ѵ�
	 * 
	 * @param tOwner
	 *            ���̾�α� �ڽ��� ��Ű ���ؼ� �ʿ��� Frame ��ü.
	 * @param tGlobalData
	 *            �����̳��� �� �ڷ� ��ü.
	 */
    public RDDWordBoxDialog(Window tOwner) {
        super(tOwner, "���� �ۻ��� �����", Dialog.ModalityType.TOOLKIT_MODAL);
        nDialogState = TD_CANCELED;
        bQueriesInit = true;
        initDialog(null, 0);
    }

    public RDDWordBoxDialog(Window tOwner, RDOQuery Q, int F) {
        super(tOwner, "���� �ۻ��� �����", Dialog.ModalityType.TOOLKIT_MODAL);
        nDialogState = TD_CANCELED;
        bQueriesInit = true;
        initDialog(Q, F);
    }

    /**
	 * ���̾�α� �ڽ��� ȭ�鿡 �����ش�
	 * 
	 * @return ���̾�α� �ڽ��� ���� ���� �ǵ�����.
	 */
    public int showDialog() {
        setVisible(true);
        return nDialogState;
    }

    /**
	 * ���õ� ���ǹ� ��ü�� ������ ��´�.
	 * 
	 * @return ������ ���ǹ� ��ü ����Ʈ�� �����ϴ� ���õ� ���ǹ� ��ü�� ������ �����Ѵ�.(0..n-1)
	 */
    public int getQueryIndex() {
        return tQueriesList.getSelectedIndex();
    }

    /**
	 * ���õ� �ʵ��� ������ ��´�.
	 * 
	 * @return ���õ� �ʵ��� ������ �ǵ�����.(0..n-1)
	 */
    public int getFieldIndex() {
        return tFieldsList.getSelectedIndex() - 1;
    }

    /** ���̾�α� �ڽ��� ������ �ʱ�ȭ�ϰ�, ������Ʈ���� ��ġ�Ѵ�. */
    private void initDialog(RDOQuery Q, int F) {
        setBounds(200, 200, TD_WIDTH, TD_HEIGHT);
        setModal(true);
        tMainPane = new JPanel();
        setContentPane(tMainPane);
        tMainPane.setLayout(null);
        tLbQuery = new JLabel(dLabel);
        tLbQuery.setBounds(TD_XAXIS1, TD_YAXIS1, 80, 22);
        tMainPane.add(tLbQuery);
        if (Q == null) tQueriesList = createQueriesList(); else tQueriesList = createQueriesList(Q);
        tQueriesList.setBounds(TD_XAXIS2, TD_YAXIS1, 220, 22);
        tMainPane.add(tQueriesList);
        JLabel tLbFields = new JLabel("�ʵ�");
        tLbFields.setBounds(TD_XAXIS1, TD_YAXIS2, 80, 22);
        tMainPane.add(tLbFields);
        if (Q == null) tFieldsList = createFieldsList(); else tFieldsList = createFieldsList(Q, F);
        tFieldsList.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.ITEM_STATE_CHANGED) {
                    bQueriesInit = false;
                    updateFields();
                    bQueriesInit = true;
                }
            }
        });
        bQueriesInit = false;
        if (Q == null) updateFields(); else updateFields(Q, F);
        bQueriesInit = true;
        tFieldsList.setBounds(TD_XAXIS2, TD_YAXIS2, 220, 22);
        tMainPane.add(tFieldsList);
        tMask = createMask();
        tMask.setBounds(TD_XAXIS2 - 40, TD_YAXIS2 + 30, 80, 22);
        tMainPane.add(tMask);
        tOk = createOk();
        tOk.setBounds(TD_XAXIS2 + 50, TD_YAXIS2 + 30, 80, 22);
        tMainPane.add(tOk);
        tCancel = createCancel();
        tCancel.setBounds(TD_XAXIS2 + 140, TD_YAXIS2 + 30, 80, 22);
        tMainPane.add(tCancel);
        return;
    }

    /**
	 * ���ǹ� ��ü ����Ʈ�� ��� �޺� �ڽ��� ���ؼ� �����Ѵ�.
	 * 
	 * @return ��� �޺� �ڽ��� ���ϵȴ�.
	 */
    private JComboBox createQueriesList() {
        JComboBox tReturn = new JComboBox();
        Report tGDDoc = ReportDesigner.getActiveReport();
        if (tGDDoc.udsUsed()) {
            for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) tReturn.addItem(((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getName());
            dLabel = "�����ͼ�";
            this.tLbQuery.setText(dLabel);
        } else {
            for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) tReturn.addItem(((RDOQuery) tGDDoc.tListOfQueries.get(i)).getName());
        }
        tReturn.setSelectedIndex(0);
        tReturn.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                String selectedQuery = tQueriesList.getSelectedItem().toString();
                Report tGDDoc = ReportDesigner.getActiveReport();
                if (tGDDoc.udsUsed()) {
                    for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) if (selectedQuery.equals(((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getName())) {
                        tFieldsList.removeAllItems();
                        tFieldsList.addItem("");
                        for (int j = 0; ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)) != null && j < ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getResultColumnCount(); j++) {
                            String sFieldName = ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getName() + ":" + ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getColumnName(j);
                            tFieldsList.addItem(sFieldName);
                        }
                        break;
                    }
                } else {
                    for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) if (selectedQuery.equals(((RDOQuery) tGDDoc.tListOfQueries.get(i)).getName())) {
                        tFieldsList.removeAllItems();
                        tFieldsList.addItem("");
                        for (int j = 0; ((RDOQuery) tGDDoc.tListOfQueries.get(i)) != null && j < ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getResultColumnCount(); j++) {
                            String sFieldName = ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getName() + ":" + ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getColumnName(j);
                            tFieldsList.addItem(sFieldName);
                        }
                        break;
                    }
                }
            }
        });
        return tReturn;
    }

    private JComboBox createQueriesList(RDOQuery Q) {
        JComboBox tReturn = new JComboBox();
        int QQ = 0;
        Report tGDDoc = ReportDesigner.getActiveReport();
        if (tGDDoc.udsUsed()) {
            for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) {
                tReturn.addItem(((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getName());
                if (Q.getName().equalsIgnoreCase(((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getName())) QQ = i;
            }
            dLabel = "�����ͼ�";
            this.tLbQuery.setText(dLabel);
        } else {
            for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) {
                tReturn.addItem(((RDOQuery) tGDDoc.tListOfQueries.get(i)).getName());
                if (Q.getName().equalsIgnoreCase(((RDOQuery) tGDDoc.tListOfQueries.get(i)).getName())) QQ = i;
            }
        }
        tReturn.setSelectedIndex(QQ);
        tReturn.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                String selectedQuery = tQueriesList.getSelectedItem().toString();
                Report tGDDoc = ReportDesigner.getActiveReport();
                if (tGDDoc.udsUsed()) {
                    for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) if (selectedQuery.equals(((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getName())) {
                        tFieldsList.removeAllItems();
                        tFieldsList.addItem("");
                        for (int j = 0; ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)) != null && j < ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getResultColumnCount(); j++) {
                            String sFieldName = ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getName() + ":" + ((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getColumnName(j);
                            tFieldsList.addItem(sFieldName);
                        }
                        break;
                    }
                } else {
                    for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) if (selectedQuery.equals(((RDOQuery) tGDDoc.tListOfQueries.get(i)).getName())) {
                        tFieldsList.removeAllItems();
                        tFieldsList.addItem("");
                        for (int j = 0; ((RDOQuery) tGDDoc.tListOfQueries.get(i)) != null && j < ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getResultColumnCount(); j++) {
                            String sFieldName = ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getName() + ":" + ((RDOQuery) tGDDoc.tListOfQueries.get(i)).getColumnName(j);
                            tFieldsList.addItem(sFieldName);
                        }
                        break;
                    }
                }
            }
        });
        return tReturn;
    }

    /**
	 * ������ ���ǹ��� �ʵ���� ����� ��� �޺� �ڽ��� ���ؼ� �ǵ�����.
	 */
    private JComboBox createFieldsList() {
        JComboBox tReturn = new JComboBox();
        Report tGDDoc = ReportDesigner.getActiveReport();
        RDOQuery tQuery;
        if (tGDDoc.udsUsed()) tQuery = tGDDoc.getUDS(tQueriesList.getSelectedIndex()); else tQuery = tGDDoc.getQuery(tQueriesList.getSelectedIndex());
        tReturn.addItem("");
        for (int i = 0; tQuery != null && i < tQuery.getResultColumnCount(); i++) {
            String sFieldName = tQuery.getColumnName(i);
            tReturn.addItem(sFieldName);
        }
        tReturn.setSelectedIndex(0);
        return tReturn;
    }

    private JComboBox createFieldsList(RDOQuery Q, int F) {
        JComboBox tReturn = new JComboBox();
        Report tGDDoc = ReportDesigner.getActiveReport();
        int QQ = 0;
        RDOQuery tQuery;
        if (tGDDoc.udsUsed()) {
            for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) {
                if (Q.getName().equalsIgnoreCase(((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getName())) QQ = i;
            }
            tQuery = tGDDoc.getUDS(QQ);
        } else {
            for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) {
                if (Q.getName().equalsIgnoreCase(((RDOQuery) tGDDoc.tListOfQueries.get(i)).getName())) QQ = i;
            }
            tQuery = tGDDoc.getQuery(QQ);
        }
        tReturn.addItem("");
        for (int i = 0; tQuery != null && i < tQuery.getResultColumnCount(); i++) {
            String sFieldName = tQuery.getColumnName(i);
            tReturn.addItem(sFieldName);
        }
        tReturn.setSelectedIndex(F);
        return tReturn;
    }

    /**
	 * OK ��ư ��ü�� ���ؼ� �����Ѵ�.
	 * 
	 * @return ��� ��ư ��ü�� �����Ѵ�.
	 */
    private JButton createMask() {
        JButton tReturn = new JButton("�� ��");
        tReturn.setActionCommand("mask");
        tReturn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                processMask();
            }
        });
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

    /**
	 * Cancel ��ư ��ü�� ���ؼ� �����Ѵ�.
	 * 
	 * @return ��� ��ư ��ü�� �����Ѵ�.
	 */
    private JButton createCancel() {
        JButton tReturn = new JButton("��  ��");
        tReturn.setActionCommand("cancel");
        tReturn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JButton tButton = (JButton) e.getSource();
                if (tButton.getActionCommand().equals("cancel")) {
                    setVisible(false);
                    nDialogState = RDDWordBoxDialog.TD_CANCELED;
                }
            }
        });
        return tReturn;
    }

    /** ���� ��ư�� �������� ���� ó�� */
    private void processMask() {
        RDMaskDialog wordBoxMaskDialog = new RDMaskDialog((Frame) this.getParent());
        if (this.maskOpt != null) {
            wordBoxMaskDialog.setMModel(this.maskOpt);
            wordBoxMaskDialog.maskingElemsInitailize();
        }
        wordBoxMaskDialog.showDialog();
        if (wordBoxMaskDialog.isConfirmed()) {
            this.maskOpt = wordBoxMaskDialog.getMModel();
        }
    }

    /** OK ��ư�� �������� ���� ó��. */
    private void processOK() {
        if (tFieldsList.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "�ʵ尡 �������� �ʾҽ��ϴ�!", "�˸�", JOptionPane.ERROR_MESSAGE);
            return;
        }
        setVisible(false);
        nDialogState = RDDWordBoxDialog.TD_OK;
    }

    /**
	 * ���ǹ� ��Ͽ��� ���õ� ���ǹ� �ٲ������� �ʵ� ����� �����Ѵ�.
	 */
    private void updateFields() {
        if (bQueriesInit) return;
        Report tGDDoc = ReportDesigner.getActiveReport();
        RDOQuery tQuery;
        if (tGDDoc.udsUsed()) tQuery = tGDDoc.getUDS(tQueriesList.getSelectedIndex()); else tQuery = tGDDoc.getQuery(tQueriesList.getSelectedIndex());
        tFieldsList.removeAllItems();
        tFieldsList.addItem("");
        for (int i = 0; i < tQuery.getResultColumnCount(); i++) {
            tFieldsList.addItem(tQuery.getColumnName(i));
        }
        tFieldsList.setSelectedIndex(0);
    }

    private void updateFields(RDOQuery Q, int F) {
        if (bQueriesInit) return;
        Report tGDDoc = ReportDesigner.getActiveReport();
        int QQ = 0;
        tFieldsList.removeAllItems();
        RDOQuery tQuery;
        if (tGDDoc.udsUsed()) {
            for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) {
                if (Q.getName().equalsIgnoreCase(((RDOUserDataSet) tGDDoc.tListOfQueries.get(i)).getName())) QQ = i;
            }
            tQuery = tGDDoc.getUDS(QQ);
        } else {
            for (int i = 0; i < tGDDoc.tListOfQueries.size(); i++) {
                if (Q.getName().equalsIgnoreCase(((RDOQuery) tGDDoc.tListOfQueries.get(i)).getName())) QQ = i;
            }
            tQuery = tGDDoc.getQuery(QQ);
        }
        tFieldsList.removeAllItems();
        tFieldsList.addItem("");
        for (int i = 0; i < tQuery.getResultColumnCount(); i++) {
            tFieldsList.addItem(tQuery.getColumnName(i));
        }
        tFieldsList.setSelectedIndex(F + 1);
    }

    public void setMaskOpt(RDMaskingModel maskOpt) {
        this.maskOpt = maskOpt;
    }

    public RDMaskingModel getMaskOpt() {
        return this.maskOpt;
    }
}
