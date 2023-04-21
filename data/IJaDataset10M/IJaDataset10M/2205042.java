package kfschmidt.qvii.tasks;

import javax.swing.*;
import kfschmidt.data4d.*;
import kfschmidt.ijcommon.IJAdapterQV;
import kfschmidt.qvii.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import kfschmidt.qvii.*;

public class SplitBOLDCBFTask extends Task implements ActionListener {

    QVII mQVII;

    SplitBOLDCBFDialog mDialog;

    boolean isReady = false;

    FloatVolumeTimeSeries vol;

    float alphafirst;

    float alphalast;

    float lambda;

    float t1;

    int[] sliceacq;

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(mDialog.mOk)) {
            if (mDialog.getSelectedVolume() != null) {
                vol = mDialog.getSelectedVolume();
                alphafirst = mDialog.mAlphaFirst;
                alphalast = mDialog.mAlphaLast;
                lambda = mDialog.mLambda;
                t1 = mDialog.mT1;
                sliceacq = mDialog.mSliceAcqOrder;
                isReady = true;
                mDialog.dispose();
            }
        } else {
            setComplete();
            mDialog.dispose();
        }
    }

    protected void splitIntoBOLDAndCBF(FloatVolumeTimeSeries vol, QVII mQVII, float alphafirst, float alphalast, float lambda, float brainT1, int[] slice_excitation_order) {
        float[] alphas = new float[vol.getSlices()];
        float curalpha = alphafirst;
        float alphaincrement = (alphafirst - alphalast) / (float) alphas.length;
        for (int a = 0; a < alphas.length; a++) {
            alphas[slice_excitation_order[a] - 1] = curalpha;
            curalpha += alphaincrement;
        }
        setStatusMsg1("Allocating data structures ..");
        FloatVolumeTimeSeries cbfvol = new FloatVolumeTimeSeries("CALC" + System.currentTimeMillis(), vol.getHeight(), vol.getWidth(), vol.getSlices(), vol.getReps() / 2, vol.getTRs(), vol.getFOVX(), vol.getFOVY(), vol.getThk(), vol.getSpace());
        cbfvol.setDescription("CBF from " + vol.getDescription());
        cbfvol.setScale(vol.getScaleX(), vol.getScaleY(), vol.getScaleZ());
        cbfvol.setRotation(vol.getRotX(), vol.getRotY(), vol.getRotZ());
        cbfvol.setTranslation(vol.getTransX(), vol.getTransY(), vol.getTransZ());
        FloatVolumeTimeSeries boldvol = new FloatVolumeTimeSeries("CALC" + System.currentTimeMillis(), vol.getHeight(), vol.getWidth(), vol.getSlices(), vol.getReps() / 2, vol.getTRs(), vol.getFOVX(), vol.getFOVY(), vol.getThk(), vol.getSpace());
        boldvol.setDescription("BOLD from " + vol.getDescription());
        boldvol.setScale(vol.getScaleX(), vol.getScaleY(), vol.getScaleZ());
        boldvol.setRotation(vol.getRotX(), vol.getRotY(), vol.getRotZ());
        boldvol.setTranslation(vol.getTransX(), vol.getTransY(), vol.getTransZ());
        setStatusMsg1("Computing...");
        double[][][][] bold = boldvol.getData();
        double[][][][] cbf = cbfvol.getData();
        double[][][][] origdata = vol.getData();
        for (int rep = 0; rep < origdata.length; rep = rep + 2) {
            setPctComplete((int) (100d * (double) rep / (double) origdata.length));
            for (int slice = 0; slice < origdata[0].length; slice++) {
                for (int x = 0; x < origdata[0][0].length; x++) {
                    for (int y = 0; y < origdata[0][0][0].length; y++) {
                        if ((rep % 2) == 0) {
                            bold[rep / 2][slice][x][y] = origdata[rep][slice][x][y];
                            if (rep > 0) {
                                cbf[-1 + rep / 2][slice][x][y] = 0.5d * (calcCBF(origdata[rep][slice][x][y], origdata[rep - 1][slice][x][y], alphas[slice], lambda, brainT1) + calcCBF(origdata[rep - 2][slice][x][y], origdata[rep - 1][slice][x][y], alphas[slice], lambda, brainT1));
                            }
                        }
                    }
                }
            }
        }
        mQVII.addVolume(boldvol);
        mQVII.addVolume(cbfvol);
        mQVII.remove(vol);
        setComplete();
    }

    /**
     *  units are in ml/100g/min
     */
    private double calcCBF(double control, double label, double alpha, double lambda, double t1) {
        double ret = ((60d * lambda) / t1) * (control - label) / (label + (2d * alpha - 1d) * control);
        return ret;
    }

    public SplitBOLDCBFTask(TaskUpdateListener listener, QVII parent) {
        super(listener);
        mQVII = parent;
        setStatusMsg1("Extracting BOLD and CBF...");
    }

    public void run() {
        setStartTime(System.currentTimeMillis());
        try {
            mDialog = new SplitBOLDCBFDialog(this);
            while (!isReady && !getComplete()) {
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                }
            }
            if (!getComplete()) {
                splitIntoBOLDAndCBF(vol, mQVII, alphafirst, alphalast, lambda, t1, sliceacq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return "Split into BOLD & CBF";
    }
}

class SplitBOLDCBFDialog extends JFrame implements ActionListener, FocusListener {

    String WIN_TITLE = "Split into BOLD+CBF";

    String SCAN_LABEL = "Scan to split ";

    String TISSUE_LABEL = "Tissue: ";

    String LAMBDA_LABEL = "Part coef";

    String T1_LABEL = "Brain T1(s)";

    String LABEL_EFF_LABEL = "Label eff: ";

    String FIRST_EFF_LABEL = "First slice: ";

    String LAST_EFF_LABEL = "Last slice: ";

    String ACQ_ORDER_LABEL = "Slice Excitation Order:";

    float mLambda = 0.9f;

    float mAlphaFirst = 0.8f;

    float mAlphaLast = 0.8f;

    float mT1 = 1.54f;

    int[] mSliceAcqOrder;

    JComboBox mVolSelector;

    JButton mOk;

    JButton mCancel;

    JTextField mLambdaText;

    JTextField mAlphaFirstText;

    JTextField mAlphaLastText;

    JTextField mT1Text;

    JTextField mAcqOrderText;

    int mWidth = 300;

    int mHeight = 150;

    SplitBOLDCBFTask mParent;

    FloatVolumeTimeSeries[] mFloatVols;

    int mSelectedVolIndex = 0;

    public SplitBOLDCBFDialog(SplitBOLDCBFTask parent) {
        mParent = parent;
        init(parent);
        show();
        syncControls();
    }

    private void handleEvent(Object source) {
        try {
            if (source.equals(mLambdaText)) {
                mLambda = Float.parseFloat(mLambdaText.getText());
            } else if (source.equals(mVolSelector)) {
                if (mVolSelector.getSelectedIndex() > 0) {
                    mSelectedVolIndex = mVolSelector.getSelectedIndex();
                    initDefaultSliceAcqOrder(mFloatVols[mVolSelector.getSelectedIndex() - 1].getSlices());
                } else {
                    mSliceAcqOrder = null;
                }
            } else if (source.equals(mAlphaFirstText)) {
                float alphafirst = Float.parseFloat(mAlphaFirstText.getText());
                mAlphaFirst = alphafirst;
            } else if (source.equals(mAlphaLastText)) {
                float alphalast = Float.parseFloat(mAlphaLastText.getText());
                mAlphaLast = alphalast;
            } else if (source.equals(mT1Text)) {
                float t1 = Float.parseFloat(mT1Text.getText());
                mT1 = t1;
            } else if (source.equals(mAcqOrderText)) {
                parseAcqString(mAcqOrderText.getText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        syncControls();
    }

    public void actionPerformed(ActionEvent ae) {
        handleEvent(ae.getSource());
    }

    public void focusGained(FocusEvent fe) {
    }

    public void focusLost(FocusEvent fe) {
        if (fe.getSource() instanceof JTextField) handleEvent(fe.getSource());
    }

    private void syncControls() {
        mVolSelector.removeActionListener(this);
        mLambdaText.removeActionListener(this);
        mAlphaFirstText.removeActionListener(this);
        mAlphaLastText.removeActionListener(this);
        mT1Text.removeActionListener(this);
        mAcqOrderText.removeActionListener(this);
        mLambdaText.removeFocusListener(this);
        mAlphaFirstText.removeFocusListener(this);
        mAlphaLastText.removeFocusListener(this);
        mT1Text.removeFocusListener(this);
        mAcqOrderText.removeFocusListener(this);
        mLambdaText.setText("" + mLambda);
        mAlphaFirstText.setText("" + mAlphaFirst);
        mAlphaLastText.setText("" + mAlphaLast);
        mT1Text.setText("" + mT1);
        if (mSelectedVolIndex > 0) {
            mVolSelector.setSelectedIndex(mSelectedVolIndex);
            mAcqOrderText.setText(getAcqString());
            mAcqOrderText.setEnabled(true);
            mLambdaText.setEnabled(true);
            mAlphaFirstText.setEnabled(true);
            mAlphaLastText.setEnabled(true);
            mT1Text.setEnabled(true);
        } else {
            mVolSelector.setSelectedIndex(0);
            mAcqOrderText.setText(" ");
            mAcqOrderText.setEnabled(false);
            mLambdaText.setEnabled(false);
            mAlphaFirstText.setEnabled(false);
            mAlphaLastText.setEnabled(false);
            mT1Text.setEnabled(false);
        }
        mVolSelector.addActionListener(this);
        mLambdaText.addActionListener(this);
        mAlphaFirstText.addActionListener(this);
        mAlphaLastText.addActionListener(this);
        mT1Text.addActionListener(this);
        mAcqOrderText.addActionListener(this);
        mLambdaText.addFocusListener(this);
        mAlphaFirstText.addFocusListener(this);
        mAlphaLastText.addFocusListener(this);
        mT1Text.addFocusListener(this);
        mAcqOrderText.addFocusListener(this);
    }

    public FloatVolumeTimeSeries getSelectedVolume() {
        if (mSelectedVolIndex > 0) {
            return mFloatVols[mSelectedVolIndex - 1];
        } else return null;
    }

    private void parseAcqString(String str) throws Exception {
        StringTokenizer tok = new StringTokenizer(str, ",");
        int counter = 0;
        while (tok.hasMoreElements()) {
            mSliceAcqOrder[counter] = Integer.parseInt((String) tok.nextElement());
            counter++;
        }
    }

    private String getAcqString() {
        if (mSliceAcqOrder == null) return " ";
        String s = "" + mSliceAcqOrder[0];
        for (int a = 1; a < mSliceAcqOrder.length; a++) {
            s += "," + mSliceAcqOrder[a];
        }
        return s;
    }

    private void initDefaultSliceAcqOrder(int slices) {
        mSliceAcqOrder = new int[slices];
        if ((slices % 2) == 0) {
            for (int a = 0; a < slices / 2; a++) {
                mSliceAcqOrder[2 * a] = a + 1;
            }
            for (int a = 0; a < slices / 2; a++) {
                mSliceAcqOrder[(2 * a) + 1] = slices / 2 + a + 1;
            }
        } else {
            for (int a = 0; a < slices; a++) {
                mSliceAcqOrder[(2 * a) % slices] = a + 1;
            }
        }
    }

    private void init(ActionListener parent) {
        mVolSelector = new JComboBox();
        mOk = new JButton(Literals.OK);
        mCancel = new JButton(Literals.CANCEL);
        mOk.addActionListener(parent);
        mCancel.addActionListener(parent);
        mLambdaText = new JTextField(3);
        mAlphaFirstText = new JTextField(3);
        mAlphaLastText = new JTextField(3);
        mT1Text = new JTextField(3);
        mAcqOrderText = new JTextField(30);
        setTitle(WIN_TITLE);
        setSize(new Dimension(mWidth, mHeight));
        mFloatVols = mParent.mQVII.getFloatVolumes();
        mVolSelector.addItem("- NONE -");
        if (mFloatVols != null) {
            for (int a = 0; a < mFloatVols.length; a++) {
                mVolSelector.addItem(mFloatVols[a].getDescription());
            }
        }
        JPanel pan1 = new JPanel();
        pan1.setLayout(new GridLayout(1, 2));
        pan1.add(new JLabel(SCAN_LABEL));
        pan1.add(mVolSelector);
        JPanel pan2 = new JPanel();
        pan2.setLayout(new BorderLayout());
        JPanel pan2a = new JPanel();
        pan2a.setLayout(new GridLayout(1, 2));
        JPanel pan2a1 = new JPanel();
        JPanel pan2a2 = new JPanel();
        pan2a1.setLayout(new BorderLayout());
        pan2a2.setLayout(new BorderLayout());
        pan2.add(BorderLayout.WEST, new JLabel(TISSUE_LABEL));
        pan2a1.add(BorderLayout.CENTER, new JLabel(T1_LABEL, JLabel.CENTER));
        pan2a2.add(BorderLayout.CENTER, new JLabel(LAMBDA_LABEL, JLabel.CENTER));
        pan2a1.add(BorderLayout.EAST, mT1Text);
        pan2a2.add(BorderLayout.EAST, mLambdaText);
        pan2a.add(pan2a1);
        pan2a.add(pan2a2);
        pan2.add(BorderLayout.CENTER, pan2a);
        JPanel pan3 = new JPanel();
        pan3.setLayout(new BorderLayout());
        JPanel pan3a = new JPanel();
        pan3a.setLayout(new GridLayout(1, 2));
        JPanel pan3a1 = new JPanel();
        JPanel pan3a2 = new JPanel();
        pan3a1.setLayout(new BorderLayout());
        pan3a2.setLayout(new BorderLayout());
        pan3.add(BorderLayout.WEST, new JLabel(LABEL_EFF_LABEL));
        pan3a1.add(BorderLayout.CENTER, new JLabel(FIRST_EFF_LABEL, JLabel.CENTER));
        pan3a2.add(BorderLayout.CENTER, new JLabel(LAST_EFF_LABEL, JLabel.CENTER));
        pan3a1.add(BorderLayout.EAST, mAlphaFirstText);
        pan3a2.add(BorderLayout.EAST, mAlphaLastText);
        pan3a.add(pan3a1);
        pan3a.add(pan3a2);
        pan3.add(BorderLayout.CENTER, pan3a);
        JPanel pan4 = new JPanel();
        pan4.setLayout(new GridLayout(1, 2));
        pan4.add(new JLabel(ACQ_ORDER_LABEL));
        pan4.add(mAcqOrderText);
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
