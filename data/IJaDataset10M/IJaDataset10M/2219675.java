package kfschmidt.mricalculations;

import java.awt.GridLayout;
import kfschmidt.ijcommon.IJAdapter;
import kfschmidt.ijcommon.IJImageChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import kfschmidt.ijcommon.IJAdapter;

public class TraditionalMCalc extends MRICalculation {

    IJImageChooser mCO2ASLWChooser;

    IJImageChooser mCO2BOLDChooser;

    JTextField mBaseLineStart;

    JTextField mBaseLineStop;

    JTextField mCO2Start;

    JTextField mCO2Stop;

    JTextField mAlpha;

    JTextField mBeta;

    public int getParamPanelHeight() {
        return 70;
    }

    public JPanel getParamPanel() {
        JPanel pan = new JPanel();
        pan.setLayout(new GridLayout(5, 1));
        JPanel subpan0 = new JPanel();
        subpan0.setLayout(new GridLayout(1, 2));
        subpan0.add(new JLabel("CO2 ASLW Stack"));
        mCO2ASLWChooser = new IJImageChooser();
        subpan0.add(mCO2ASLWChooser);
        pan.add(subpan0);
        JPanel subpanb = new JPanel();
        subpanb.setLayout(new GridLayout(1, 2));
        subpanb.add(new JLabel("C02 BOLD Stack"));
        mCO2BOLDChooser = new IJImageChooser();
        subpanb.add(mCO2BOLDChooser);
        pan.add(subpanb);
        JPanel subpan1 = new JPanel();
        subpan1.setLayout(new GridLayout(1, 2));
        mBaseLineStart = new JTextField("2", 4);
        mBaseLineStop = new JTextField("50", 4);
        JPanel subsubpan1 = new JPanel();
        subpan1.add(new JLabel("Baseline start/stop"));
        subsubpan1.add(mBaseLineStart);
        subsubpan1.add(mBaseLineStop);
        subpan1.add(subsubpan1);
        pan.add(subpan1);
        JPanel subpan2 = new JPanel();
        subpan2.setLayout(new GridLayout(1, 2));
        mCO2Start = new JTextField("60", 4);
        mCO2Stop = new JTextField("100", 4);
        JPanel subsubpan2 = new JPanel();
        subpan2.add(new JLabel("CO2 start/stop"));
        subsubpan2.add(mCO2Start);
        subsubpan2.add(mCO2Stop);
        subpan2.add(subsubpan2);
        pan.add(subpan2);
        JPanel subpan3 = new JPanel();
        subpan3.setLayout(new GridLayout(1, 4));
        subpan3.add(new JLabel("alpha"));
        mAlpha = new JTextField("0.35", 4);
        subpan3.add(mAlpha);
        subpan3.add(new JLabel("beta"));
        mBeta = new JTextField("1.5", 4);
        subpan3.add(mBeta);
        pan.add(subpan3);
        return pan;
    }

    public boolean isReady() {
        if (mCO2ASLWChooser.userSelectedImage() && mCO2BOLDChooser.userSelectedImage()) {
            return true;
        } else return false;
    }

    public void doCalculation() {
        try {
            IJAdapter ijada = new IJAdapter();
            double[][][][] aslw = ijada.get4DDataForId(mCO2ASLWChooser.getSelectedIJId());
            double[][][][] bold = ijada.get4DDataForId(mCO2BOLDChooser.getSelectedIJId());
            double[][][][] pct_aslw = getPercentChangeMap(aslw, Integer.parseInt(mBaseLineStart.getText()), Integer.parseInt(mBaseLineStop.getText()), Integer.parseInt(mCO2Start.getText()), Integer.parseInt(mCO2Stop.getText()));
            double[][][][] pct_bold = getPercentChangeMap(bold, Integer.parseInt(mBaseLineStart.getText()), Integer.parseInt(mBaseLineStop.getText()), Integer.parseInt(mCO2Start.getText()), Integer.parseInt(mCO2Stop.getText()));
            setStatusMsg("Calculating M map");
            int slices = pct_bold[0].length;
            int width = pct_bold[0][0].length;
            int height = pct_bold[0][0][0].length;
            double[][][][] m_map = new double[1][slices][width][height];
            double alpha = Double.parseDouble(mAlpha.getText());
            double beta = Double.parseDouble(mBeta.getText());
            for (int s = 0; s < slices; s++) {
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        m_map[0][s][x][y] = pct_bold[0][s][x][y] / (1d - Math.pow((1d + pct_aslw[0][s][x][y]), (alpha - beta)));
                        if (m_map[0][s][x][y] < 0) m_map[0][s][x][y] = 0d;
                    }
                    getUI().setProgress(100 * (s * width * height + y * width) / (width * height * slices));
                }
            }
            setStatusMsg("Finished");
            ijada.takeImage("ASLW % CHG", pct_aslw);
            ijada.takeImage("BOLD % CHG", pct_bold);
            ijada.takeImage("M", m_map);
        } catch (Exception e) {
            getUI().showError(e);
        }
    }

    public String getHelpURL() {
        return "http://www.quickvol.com/ccni/mricalculations.pdf";
    }

    public String getDisplayTitle() {
        return "Davis M Calc";
    }
}
