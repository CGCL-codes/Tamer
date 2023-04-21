package edu.ucla.stat.SOCR.chart.gui;

import java.text.AttributedString;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.PieDataset;

/**
 * A custom label generator (returns null for one item as a test).
 */
public class CustomPieSectionLabelGenerator implements PieSectionLabelGenerator {

    /**
     * Generates a label for a pie section.
     *
     * @param dataset  the dataset (<code>null</code> not permitted).
     * @param key  the section key (<code>null</code> not permitted).
     *
     * @return the label (possibly <code>null</code>).
     */
    public String generateSectionLabel(PieDataset dataset, Comparable key) {
        String result = null;
        if (dataset != null) {
            if (!key.equals("PHP")) {
                result = key.toString();
            }
        }
        return result;
    }

    public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
        return null;
    }
}
