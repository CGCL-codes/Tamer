package net.maizegenetics.ui.chart;

import java.util.Random;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DummyReport implements pal.misc.TableReport {

    static Random random = new Random();

    public DummyReport() {
    }

    public Object[] getTableColumnNames() {
        String[] basicLabels = { "Site_Type", "StartSite", "XGauss", "YGAUSS" };
        return basicLabels;
    }

    public Object[][] getTableData() {
        Object[][] data;
        java.text.NumberFormat nf = new java.text.DecimalFormat();
        nf.setMaximumFractionDigits(8);
        int basicCols = 4, labelOffset;
        data = new String[500][basicCols];
        for (int i = 0; i < 500; i++) {
            labelOffset = 0;
            data[i][labelOffset++] = "Hap" + (char) ((i % 4) + 65);
            data[i][labelOffset++] = "" + i;
            data[i][labelOffset++] = "" + nf.format(random.nextGaussian());
            data[i][labelOffset++] = "" + nf.format(random.nextGaussian() + .3);
        }
        labelOffset = 0;
        data[25][labelOffset++] = "HapX" + (char) ((25 % 4) + 65);
        data[25][labelOffset++] = "BLAH";
        data[25][labelOffset++] = "NaN";
        data[25][labelOffset++] = "BLAH";
        return data;
    }

    public String getTableTitle() {
        return "Diversity estimates";
    }
}
