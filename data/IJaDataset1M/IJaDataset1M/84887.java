package kr.ac.ssu.imc.durubi.report.viewer.components.maskings;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import com.sun.java.swing.plaf.windows.*;
import java.lang.*;

public class DRPercentMaskingDialog extends JDialog implements ActionListener {

    JComboBox decimalPoint;

    JLabel exam, negative, illustrate, name;

    JButton ok, cancel, up, down;

    JTextField tField;

    String textValue;

    private int[] confirmResultState;

    private int[] cancelResultState;

    private String examData, resultExamData;

    private String perExamData, pre, nxt;

    String preResultExamData;

    private boolean isCanceled = false;

    private boolean isNagative = false;

    private boolean perFlag;

    public DRPercentMaskingDialog(Frame f, String examData, int[] resultState, boolean perFlag) {
        super(f, "����� �Ž�ŷ ���̾�α�", true);
        this.perFlag = perFlag;
        if (examData.indexOf("%") != -1) examData = examData.substring(0, examData.indexOf("%"));
        if (this.perFlag == false) {
            if (examData.indexOf(".") == -1) {
                this.examData = Integer.toString(Integer.parseInt(examData) * 100);
                this.perExamData = this.examData;
                this.resultExamData = Integer.toString(Integer.parseInt(examData) * 100);
            } else {
                this.examData = Double.toString(Double.parseDouble(examData) * 100.00);
                this.perExamData = (this.examData).substring(0, (this.examData).indexOf("."));
                this.nxt = (this.examData).substring((this.examData).indexOf(".") + 1, examData.length());
                this.resultExamData = this.perExamData + "." + this.nxt;
            }
        } else {
            if (examData.indexOf(".") == -1) {
                this.examData = examData;
                this.perExamData = examData;
                this.resultExamData = examData;
            } else {
                this.examData = examData;
                this.perExamData = examData;
                this.resultExamData = examData;
            }
        }
        if (this.examData.charAt(0) == '-' || this.examData.charAt(0) == '(') {
            this.isNagative = true;
        }
        this.setConfirmResultState(resultState);
        this.setCancelResultState(resultState);
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        ok = new JButton("Ȯ��");
        ok.addActionListener(this);
        ok.setActionCommand("ok");
        ok.setBounds(150, 240, 80, 20);
        cancel = new JButton("���");
        cancel.addActionListener(this);
        cancel.setActionCommand("cancel");
        cancel.setBounds(240, 240, 80, 20);
        contentPane.add(ok);
        contentPane.add(cancel);
        up = new JButton("+");
        up.addActionListener(this);
        up.setActionCommand("up");
        up.setBounds(280, 61, 20, 9);
        contentPane.add(up);
        down = new JButton("-");
        down.addActionListener(this);
        down.setActionCommand("down");
        down.setBounds(280, 71, 20, 9);
        contentPane.add(down);
        tField = new JTextField(Integer.toString(confirmResultState[0]));
        textValue = tField.getText();
        tField.addActionListener(this);
        tField.setBounds(160, 60, 120, 20);
        contentPane.add(tField);
        exam = new JLabel(this.perExamData + "%");
        name = new JLabel(" �Ҽ��ڸ��� :");
        exam.setBorder(new TitledBorder("����"));
        exam.setBounds(30, 10, 270, 40);
        name.setBounds(30, 60, 100, 20);
        contentPane.add(exam);
        contentPane.add(name);
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

    public boolean getPerFlag() {
        return this.perFlag;
    }

    public void setPerFlag(boolean input) {
        this.perFlag = input;
    }

    public boolean getIsNagative() {
        return this.isNagative;
    }

    public void setIsNagative(boolean in) {
        this.isNagative = in;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("ok")) {
            setVisible(false);
        } else if (cmd.equals("cancel")) {
            isCanceled = true;
            setVisible(false);
        } else if (cmd.equals("up")) {
            textValue = Integer.toString(Integer.parseInt(textValue) + 1);
            tField.setText(textValue);
            confirmResultState[0] = Integer.parseInt(textValue);
            decimalPointMasking();
            exam.setText(resultExamData + "%");
        } else if (cmd.equals("down")) {
            if (Integer.parseInt(textValue) > 0) {
                textValue = Integer.toString(Integer.parseInt(textValue) - 1);
                tField.setText(textValue);
                confirmResultState[0] = Integer.parseInt(textValue);
                decimalPointMasking();
                exam.setText(resultExamData + "%");
            }
        }
    }

    public void decimalPointMasking() {
        String[] tmp = new String[1];
        String[] str = new String[1];
        boolean isnegative = false;
        if (resultExamData == null) {
            str[0] = "";
        } else {
            str[0] = new String(resultExamData);
        }
        if (confirmResultState[0] > -1) {
            for (int i = 0; i < 1; i++) {
                tmp[i] = "";
                if (str[i].indexOf(".") != -1) {
                    int decimalPoint = str[i].indexOf(".");
                    if ((str[i].length() - decimalPoint - 1) < confirmResultState[0]) {
                        for (int j = 0; j <= confirmResultState[0] - (str[i].length() - decimalPoint - 1); j++) str[i] += "0";
                        resultExamData = str[i];
                    } else {
                        if (confirmResultState[0] == 0) resultExamData = new String(str[i].substring(0, decimalPoint)); else resultExamData = new String(str[i].substring(0, decimalPoint + confirmResultState[0] + 1));
                    }
                } else {
                    if (str[i] == "") str[i] += "0";
                    str[i] += ".";
                    for (int j = 0; j < confirmResultState[0]; j++) str[i] += "0";
                    resultExamData = str[i];
                }
            }
        }
    }
}
