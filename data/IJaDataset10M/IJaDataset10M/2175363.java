package org.freehep.j3d.plot;

import java.io.*;

/**
 * @author Joy Kyriakopulos (joyk@fnal.gov)
 * @version $Id: AxisLabelCalculator.java,v 1.1 2010/05/10 17:43:44 jiecui Exp $
 */
public class AxisLabelCalculator {

    private double data_min = 0d, data_max = 1d;

    private double plot_min = 0d, plot_max = 1d;

    private AxisLabel[] labels;

    private int nDivisions = 0;

    private boolean labelsValid;

    public void createNewLabels(double min, double max) {
        data_min = min;
        data_max = max;
        int minNumberOfDivisions = 1;
        int maxNumberOfDivisions = 10;
        double log10 = Math.log(10.0);
        int maxCharsPerLabel = 5;
        labelsValid = true;
        double log_max = data_max == 0d ? 0d : Math.log(Math.abs(data_max)) / log10;
        final int int_log_max = (int) Math.floor(log_max);
        int scale_power = 0;
        if (int_log_max >= maxCharsPerLabel) scale_power = int_log_max; else if (int_log_max <= -maxCharsPerLabel) scale_power = int_log_max;
        final DoubleNumberFormatter format = new DoubleNumberFormatter(scale_power);
        final double difference = data_max - data_min;
        final double pow = Math.floor(Math.log(difference) / log10) - 1.0;
        int fractDigits = 0;
        if (scale_power > 0) fractDigits = scale_power - (int) pow; else if (pow < -0.5) fractDigits = scale_power - (int) pow;
        final double conversion = Math.pow(10.0, pow);
        int intMin = round(data_min / conversion, false);
        int intMax = round(data_max / conversion, true);
        plot_min = data_min;
        plot_max = data_max;
        final int naturalNumberOfDivisions = intMax - intMin;
        int nDivisions = 0;
        final float idealMinFraction = 0.5f;
        int nUnits = 1;
        if (naturalNumberOfDivisions < maxNumberOfDivisions) {
            final float proximity = (float) naturalNumberOfDivisions / (float) maxNumberOfDivisions;
            boolean niceDivisionFound = false;
            if (proximity < idealMinFraction) {
                final int[] divisions = { 2, 4, 5, 10, 20 };
                for (int i = 0; i < divisions.length; i++) {
                    final int candidate = divisions[i];
                    if (proximity * candidate <= 1.0) {
                        niceDivisionFound = true;
                        nUnits = candidate;
                        continue;
                    }
                    break;
                }
            }
            if (niceDivisionFound) {
                nDivisions = naturalNumberOfDivisions * nUnits + (int) ((plot_max / conversion - intMax) * nUnits);
                if (pow < 0.5 || scale_power > 0) fractDigits++;
                if ((nUnits == 4 || nUnits == 20) && (pow < 1.5 || scale_power > 0)) fractDigits++;
            } else {
                nDivisions = Math.max(naturalNumberOfDivisions, minNumberOfDivisions);
            }
        } else if (naturalNumberOfDivisions > maxNumberOfDivisions) {
            nDivisions = 1;
            final int[] skips = { 2, 5, 10, 20, 25, 50 };
            for (int i = 0; i < skips.length; i++) {
                final int nDivisionsThisTry = naturalNumberOfDivisions / skips[i];
                if (nDivisionsThisTry > maxNumberOfDivisions) {
                    continue;
                }
                nUnits = skips[i];
                nDivisions = nDivisionsThisTry;
                if (nUnits >= 10 && nUnits != 25 && fractDigits > 0) fractDigits--;
                if (intMin % nUnits != 0) {
                    final int increase = intMin > 0 ? nUnits - intMin % nUnits : -intMin % nUnits;
                    if (increase > intMax - (intMax - intMin) / nUnits * nUnits - intMin) nDivisions--;
                    intMin += increase;
                }
                break;
            }
        } else nDivisions = Math.max(naturalNumberOfDivisions, minNumberOfDivisions);
        double minLabelValue = intMin * conversion;
        final double inc = naturalNumberOfDivisions < maxNumberOfDivisions ? conversion / nUnits : conversion * nUnits;
        if (naturalNumberOfDivisions < maxNumberOfDivisions && minLabelValue - inc >= plot_min) {
            int nLost = (int) ((minLabelValue - inc) / inc);
            minLabelValue -= nLost * inc;
            nDivisions += nLost;
        }
        labels = new AxisLabel[nDivisions + 1];
        this.nDivisions = nDivisions;
        format.setFractionDigits(fractDigits);
        for (int j = 0; j < labels.length; j++) {
            final double labelValue = minLabelValue + j * inc;
            labels[j] = new AxisLabel();
            labels[j].text = format.format(labelValue);
            labels[j].position = (labelValue - plot_min) / (plot_max - plot_min);
            if (labels[j].position < 0.) labels[j].position = 0.;
        }
    }

    private int charsReq(int pow) {
        if (pow < 0) return -pow + 2; else return pow + 1;
    }

    private int round(final double d, final boolean down) {
        final double minProximity = 0.0001;
        final double round = Math.round(d);
        if (d == round || Math.abs(d - round) < (d != 0.0 ? minProximity * Math.abs(d) : 0.000001)) {
            return (int) round;
        } else {
            return down ? (int) Math.floor(d) : (int) Math.ceil(d);
        }
    }

    public String[] getLabels() {
        int len = labels.length;
        String[] lab = new String[len];
        for (int i = 0; i < len; ++i) lab[i] = labels[i].text;
        return lab;
    }

    public double[] getPositions() {
        int len = labels.length;
        double[] pos = new double[len];
        for (int i = 0; i < len; ++i) pos[i] = labels[i].position;
        return pos;
    }

    public void printLabels() {
        System.out.println("data_min = " + data_min + ", data_max = " + data_max);
        System.out.println("plot_min = " + plot_min + ", plot_max = " + plot_max);
        System.out.println("nDivisions = " + nDivisions + ", labelsValid = " + labelsValid);
        int i;
        for (i = 0; i < labels.length; ++i) {
            System.out.println("label " + (i + 1) + ": " + labels[i].text + "  position: " + labels[i].position);
        }
    }

    private class AxisLabel {

        String text;

        double position;
    }

    /**
    * Just for testing
    */
    public static void main(String[] argv) {
        AxisLabelCalculator t = new AxisLabelCalculator();
        t.createNewLabels(0., 1.);
        t.printLabels();
        t.createNewLabels(0., 100.);
        t.printLabels();
        t.createNewLabels(0.0001, 0.001);
        t.printLabels();
        t.createNewLabels(5., 10.);
        t.printLabels();
        t.createNewLabels(.001, .050);
        t.printLabels();
        t.createNewLabels(.00001, .00005);
        t.printLabels();
        t.createNewLabels(-.001, .00005);
        t.printLabels();
        t.createNewLabels(-.001, .005);
        t.printLabels();
        t.createNewLabels(-2., 5.);
        t.printLabels();
        t.createNewLabels(.05, 10.);
        t.printLabels();
    }
}
