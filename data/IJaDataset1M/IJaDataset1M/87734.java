package kr.ac.ssu.imc.durubi.report.viewer.components.maskings;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import com.sun.java.swing.plaf.windows.*;
import java.lang.*;

public class DREtcMaskingDialog extends JDialog implements ActionListener, ListSelectionListener {

    JList dateFormat;

    JLabel exam, date;

    JButton ok, cancel;

    String textValue;

    private static String[] format = { "�����ȣ", "��ȭ��ȣ", "�ڵ����ȣ", "�ֹε�Ϲ�ȣ" };

    private int[] confirmResultState;

    private int[] cancelResultState;

    String examData, resultExamData;

    private boolean isCanceled = false;

    public DREtcMaskingDialog(Frame f, String examData, int[] resultState) {
        super(f, "��Ÿ �Ž�ŷ ���̾�α�", true);
        this.examData = examData;
        this.resultExamData = examData;
        this.setConfirmResultState(resultState);
        this.setCancelResultState(resultState);
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        ok = new JButton("Ȯ��");
        ok.addActionListener(this);
        ok.setActionCommand("ok");
        ok.setBounds(150, 235, 80, 20);
        cancel = new JButton("���");
        cancel.addActionListener(this);
        cancel.setActionCommand("cancel");
        cancel.setBounds(240, 235, 80, 20);
        contentPane.add(ok);
        contentPane.add(cancel);
        exam = new JLabel(examData);
        date = new JLabel(" ��� : ");
        exam.setBorder(new TitledBorder("����"));
        exam.setBounds(30, 10, 270, 40);
        date.setBounds(40, 55, 40, 20);
        contentPane.add(exam);
        contentPane.add(date);
        dateFormat = new JList(format);
        dateFormat.setVisibleRowCount(4);
        dateFormat.addListSelectionListener(this);
        dateFormat.setCellRenderer(new MyCellRenderer());
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(dateFormat);
        scrollPane.setBounds(40, 75, 250, 145);
        scrollPane.setVisible(true);
        contentPane.add(scrollPane);
        setBounds(0, 0, 340, 290);
        setVisible(true);
    }

    public int[] getConfirmResultState() {
        return confirmResultState;
    }

    public int[] getCancelResultState() {
        return cancelResultState;
    }

    public void setConfirmResultState(int[] result) {
        confirmResultState = new int[result.length];
        for (int i = 0; i < result.length; i++) {
            this.confirmResultState[i] = result[i];
        }
    }

    public void setCancelResultState(int[] result) {
        cancelResultState = new int[result.length];
        for (int i = 0; i < result.length; i++) {
            this.cancelResultState[i] = result[i];
        }
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("ok")) {
            setVisible(false);
        } else if (cmd.equals("cancel")) {
            isCanceled = true;
            setVisible(false);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(dateFormat)) {
            for (int i = 0; i < dateFormat.getModel().getSize(); i++) {
                if (dateFormat.isSelectedIndex(i) == true) {
                    confirmResultState[i + 2] = 1;
                } else confirmResultState[i + 2] = 0;
            }
        }
    }

    class EtcCellRenderer extends JLabel implements ListCellRenderer {

        public EtcCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            setBackground(isSelected ? Color.lightGray : Color.white);
            setForeground(isSelected ? Color.darkGray : Color.black);
            return this;
        }
    }
}
