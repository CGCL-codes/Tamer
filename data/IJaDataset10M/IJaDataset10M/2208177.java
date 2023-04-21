package org.unitmetrics.java.ui.views.chart;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.data.xy.XYDataset;
import org.unitmetrics.java.ui.views.chart.PackageDistanceDataset.Package;

/**
 * @author Martin Kersten
 */
public class PackageDistanceTooltipGenerator implements XYToolTipGenerator {

    public String generateToolTip(XYDataset dataset, int series, int item) {
        return generateToolTip((PackageDistanceDataset) dataset, series, item);
    }

    private String generateToolTip(PackageDistanceDataset dataset, int series, int item) {
        Package aPackage = dataset.getPackage(item);
        return "<html><b>" + aPackage.getName() + "</b><br><br>" + "Abstractness: " + format(aPackage.getAbstractness()) + "<br>" + "Instability: " + format(aPackage.getInstability()) + "<br>" + "Norm. Distance: " + format(aPackage.getNormalizedDistance()) + "<br>" + "</html>";
    }

    private String format(double value) {
        String valueFormat = "0.00";
        NumberFormat format = new DecimalFormat(valueFormat, new DecimalFormatSymbols(Locale.ENGLISH));
        return format.format(value);
    }
}
