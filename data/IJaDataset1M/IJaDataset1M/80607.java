package kfschmidt.qvii.tasks;

import javax.swing.*;
import kfschmidt.data4d.*;
import kfschmidt.ijcommon.IJAdapterQV;
import kfschmidt.qvii.*;
import kfschmidt.stackrenderer.SlicePlan;
import kfschmidt.stackrenderer.VolumeSampler;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import kfschmidt.qvii.*;

public class VolumeMathTask extends Task implements ActionListener {

    QVII mQVII;

    VolumeMathDialog mDialog;

    boolean isReady = false;

    FloatVolumeTimeSeries mVol1;

    int mOp;

    double mNumber;

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(mDialog.mOk)) {
            try {
                mVol1 = mDialog.getSelectedVol1();
                mNumber = mDialog.getNumber();
                mOp = mDialog.getSelectedOp();
                isReady = true;
                mDialog.dispose();
            } catch (Exception e) {
                showErrorMsg("Problem parsing number: try again");
            }
        } else {
            setComplete();
            mDialog.dispose();
        }
    }

    public QVII getQVII() {
        return mQVII;
    }

    protected void operateOnVol(FloatVolumeTimeSeries vol1, int operation, double number) {
        setStatusMsg1("Performing operation ..");
        double[][][][] mergedata = vol1.getData();
        for (int rep = 0; rep < mergedata.length; rep++) {
            setPctComplete((int) (100d * (double) rep / (double) mergedata.length));
            for (int slice = 0; slice < mergedata[0].length; slice++) {
                for (int x = 0; x < mergedata[0][0].length; x++) {
                    for (int y = 0; y < mergedata[0][0][0].length; y++) {
                        if (operation == VolumeMathDialog.ADD) {
                            mergedata[rep][slice][x][y] += number;
                        } else if (operation == VolumeMathDialog.SUBTRACT) {
                            mergedata[rep][slice][x][y] -= number;
                        } else if (operation == VolumeMathDialog.MULTIPLY) {
                            mergedata[rep][slice][x][y] *= number;
                        } else if (operation == VolumeMathDialog.DIVIDE) {
                            mergedata[rep][slice][x][y] /= number;
                        } else if (operation == VolumeMathDialog.POWER) {
                            mergedata[rep][slice][x][y] = Math.pow(mergedata[rep][slice][x][y], number);
                        }
                    }
                }
            }
        }
        mQVII.dispatchProjectUpdate(QVIIProjectListener.UPDATE_ALL, vol1);
        setComplete();
    }

    public VolumeMathTask(TaskUpdateListener listener, QVII parent) {
        super(listener);
        mQVII = parent;
        setStatusMsg1("Volume Math...");
    }

    public void run() {
        setStartTime(System.currentTimeMillis());
        try {
            mDialog = new VolumeMathDialog(this);
            while (!isReady && !getComplete()) {
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                }
            }
            if (!getComplete()) {
                operateOnVol(mVol1, mOp, mNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return "Volume Math";
    }

    public void showErrorMsg(String msg) {
        JOptionPane.showMessageDialog(mDialog, Literals.CP_ERROR_PREFIX + msg, Literals.CP_ERROR_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }
}

class VolumeMathDialog extends JFrame {

    public static final int ADD = 0;

    public static final int SUBTRACT = 1;

    public static final int MULTIPLY = 2;

    public static final int DIVIDE = 3;

    public static final int POWER = 4;

    public static String[] mOpStrings = { "ADD", "SUBTRACT", "MULTIPLY", "DIVIDE", "POWER" };

    String WIN_TITLE = "Volume Math";

    String VOL1_LABEL = "Volume: ";

    String OP_LABEL = "Operation: ";

    String NUMBER_LABEL = "Value:";

    JComboBox mVol1Selector;

    JComboBox mOpSelector;

    JTextField mNumberText;

    JButton mOk;

    JButton mCancel;

    int mWidth = 300;

    int mHeight = 150;

    VolumeMathTask mParent;

    FloatVolumeTimeSeries[] mFloatVols;

    SlicePlan[] mPlans;

    int mSelectedVolIndex = 0;

    public VolumeMathDialog(VolumeMathTask parent) {
        mParent = parent;
        init(parent);
        show();
    }

    public FloatVolumeTimeSeries getSelectedVol1() {
        if (mVol1Selector.getSelectedIndex() > 0) {
            return mFloatVols[mVol1Selector.getSelectedIndex() - 1];
        } else return null;
    }

    public int getSelectedOp() {
        return mOpSelector.getSelectedIndex();
    }

    public double getNumber() throws Exception {
        return Double.parseDouble(mNumberText.getText());
    }

    private void init(ActionListener parent) {
        setTitle(WIN_TITLE);
        setSize(new Dimension(mWidth, mHeight));
        mOk = new JButton(Literals.OK);
        mCancel = new JButton(Literals.CANCEL);
        mOk.addActionListener(parent);
        mCancel.addActionListener(parent);
        mVol1Selector = new JComboBox();
        mOpSelector = new JComboBox();
        mNumberText = new JTextField();
        mFloatVols = mParent.mQVII.getFloatVolumes();
        mVol1Selector.addItem("- NONE -");
        if (mFloatVols != null) {
            for (int a = 0; a < mFloatVols.length; a++) {
                mVol1Selector.addItem(mFloatVols[a].getDescription());
            }
        }
        for (int a = 0; a < mOpStrings.length; a++) {
            mOpSelector.addItem(mOpStrings[a]);
        }
        JPanel pan1 = new JPanel();
        pan1.setLayout(new GridLayout(1, 2));
        pan1.add(new JLabel(VOL1_LABEL));
        pan1.add(mVol1Selector);
        JPanel pan2 = new JPanel();
        pan2.setLayout(new GridLayout(1, 2));
        pan2.add(new JLabel(OP_LABEL));
        pan2.add(mOpSelector);
        JPanel pan3 = new JPanel();
        pan3.setLayout(new GridLayout(1, 2));
        pan3.add(new JLabel(NUMBER_LABEL));
        pan3.add(mNumberText);
        JPanel pan4 = new JPanel();
        JPanel butpan = new JPanel();
        butpan.setLayout(new GridLayout(1, 3));
        butpan.add(new JLabel(" "));
        butpan.add(mCancel);
        butpan.add(mOk);
        pan1.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pan2.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pan3.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pan4.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        butpan.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        getContentPane().setLayout(new GridLayout(5, 1));
        getContentPane().add(pan1);
        getContentPane().add(pan2);
        getContentPane().add(pan3);
        getContentPane().add(pan4);
        getContentPane().add(butpan);
    }
}
