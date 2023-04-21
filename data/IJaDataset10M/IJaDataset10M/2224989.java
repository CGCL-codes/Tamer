package gca.separation.probability;

import gca.separation.probability.BivariateProbabilityDistribution;
import gca.separation.probability.distributions.SlopeMovementDistribution;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;

/**
 * A graphical test application for probability distributions.
 *
 * @author Jason Rohrer
 */
public class DistributionTester extends JFrame {

    protected int mNumSamples = 0;

    protected int mNumBins = 30;

    protected int mXCountBins[];

    protected int mYCountBins[];

    protected BivariateProbabilityDistribution mDistribution = (BivariateProbabilityDistribution) (new SlopeMovementDistribution(1, 0, 2.0));

    protected double mRangeStart = -1;

    protected double mRangeEnd = 1;

    public static void main(String inArgs[]) {
        new DistributionTester();
    }

    DistributionTester() {
        super("Distribution Tester");
        mXCountBins = new int[mNumBins];
        mYCountBins = new int[mNumBins];
        setSize(200, 200);
        setVisible(true);
        while (true) {
            double sample[] = mDistribution.sample();
            int xBinNumber = (int) ((mNumBins) * (sample[0] - mRangeStart) / (mRangeEnd - mRangeStart));
            int yBinNumber = (int) ((mNumBins) * (sample[1] - mRangeStart) / (mRangeEnd - mRangeStart));
            if (xBinNumber == mNumBins) {
                xBinNumber--;
            }
            if (yBinNumber == mNumBins) {
                yBinNumber--;
            }
            mNumSamples++;
            mXCountBins[xBinNumber]++;
            mYCountBins[yBinNumber]++;
            repaint();
        }
    }

    public void paint(Graphics inGraphics) {
        int binSpread = getWidth() / mNumBins;
        Image buffer = this.createImage(getWidth(), getHeight());
        Graphics buffGraphics = buffer.getGraphics();
        buffGraphics.setColor(Color.white);
        buffGraphics.fillRect(0, 0, getWidth(), getHeight());
        buffGraphics.setColor(Color.black);
        int height = getHeight();
        int maxXCount = 0;
        int minXCount = mNumSamples;
        int maxYCount = 0;
        int minYCount = mNumSamples;
        for (int i = 0; i < mNumBins; i++) {
            if (mXCountBins[i] > maxXCount) {
                maxXCount = mXCountBins[i];
            }
            if (mXCountBins[i] < minXCount) {
                minXCount = mXCountBins[i];
            }
            if (mYCountBins[i] > maxYCount) {
                maxYCount = mYCountBins[i];
            }
            if (mYCountBins[i] < minYCount) {
                minYCount = mYCountBins[i];
            }
        }
        for (int i = 0; i < mNumBins; i++) {
            buffGraphics.drawLine(binSpread * i, (int) (0.5 * height), binSpread * i, (int) (0.5 * height - 0.5 * height * (double) (mXCountBins[i]) / maxXCount));
            buffGraphics.drawLine(binSpread * i, height, binSpread * i, (int) (height - 0.5 * height * (double) (mYCountBins[i]) / maxYCount));
        }
        inGraphics.drawImage(buffer, 0, 0, null);
    }
}
