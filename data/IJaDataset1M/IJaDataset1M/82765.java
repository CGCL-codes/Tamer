package org.carlmanaster.allelogram.gui.mouse;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;
import org.carlmanaster.allelogram.gui.AllelogramApplet;
import org.carlmanaster.allelogram.gui.BinBoundary;
import org.carlmanaster.allelogram.gui.Chart;
import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.Genotype;
import org.carlmanaster.allelogram.util.PlatformUtil;

public class AllelogramMouseDispatcher extends MouseDispatcher {

    private final AllelogramApplet applet;

    private final Chart chart;

    public AllelogramMouseDispatcher(AllelogramApplet applet, Chart chart) {
        this.applet = applet;
        this.chart = chart;
    }

    protected ClickerDragger findDelegate(MouseEvent event) {
        boolean commandKey = PlatformUtil.isMac() ? event.isMetaDown() : event.isControlDown();
        Point point = event.getPoint();
        Allele allele = chart.alleleAt(point);
        if (allele != null) return new AlleleClicker(applet, allele);
        BinBoundary boundary = chart.binBoundaryAt(point.y);
        if (boundary != null) return new BinBoundaryDragger(applet, chart, boundary);
        if (!commandKey && chart.isShowingNormalization() && applet.getSortClassifier() != null) {
            allele = chart.closestAllele(point.x);
            Genotype genotype = allele.getGenotype();
            List<Genotype> genotypes = applet.genotypesMatching(genotype);
            return new NormalizationDragger(chart, genotypes);
        }
        if (true) return new Zoomer(chart);
        return doNothing();
    }
}
