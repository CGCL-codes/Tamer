package net.sf.genomeview.gui.viztracks.hts;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import net.sf.genomeview.core.Configuration;
import net.sf.genomeview.data.provider.PileProvider;
import net.sf.genomeview.data.provider.Status;
import net.sf.genomeview.gui.Convert;
import net.sf.genomeview.gui.StaticUtils;
import net.sf.jannot.Location;
import net.sf.jannot.pileup.DoublePile;
import net.sf.jannot.pileup.Pile;

/**
 * 
 * @author Thomas Abeel
 * 
 */
class LineChartBuffer implements VizBuffer {

    private Location visible;

    private PileProvider provider;

    private PileupTrackModel ptm;

    public LineChartBuffer(Location visible, PileProvider provider, PileupTrackModel ptm) {
        this.visible = visible;
        this.provider = provider;
        this.ptm = ptm;
    }

    private static final double LOG2 = Math.log(2);

    private double log2(double d) {
        return Math.log(d) / LOG2;
    }

    @Override
    public int draw(Graphics2D g, int yOffset, double screenWidth) {
        int graphLineHeigh = Configuration.getInt("shortread:graphLineHeight");
        for (Status t : provider.getStatus(visible.start, visible.end)) {
            int x1 = Convert.translateGenomeToScreen(t.start(), visible, screenWidth);
            int x2 = Convert.translateGenomeToScreen(t.end(), visible, screenWidth);
            g.setColor(Color.red);
            if (t.isQueued()) g.setColor(Color.ORANGE);
            if (t.isRunning()) g.setColor(Color.GREEN);
            if (!t.isReady()) g.fillRect(x1, yOffset, x2 - x1 + 1, graphLineHeigh);
        }
        GeneralPath conservationGP = new GeneralPath();
        conservationGP.moveTo(-5, yOffset + graphLineHeigh);
        double div = provider.getMaxPile();
        if (ptm.isLogscaling()) {
            div = log2(provider.getMaxPile());
        }
        for (Pile p : provider.get(visible.start, visible.end)) {
            int x1 = Convert.translateGenomeToScreen(p.start(), visible, screenWidth);
            int x2 = Convert.translateGenomeToScreen(p.start() + p.getLength(), visible, screenWidth);
            double val = p.getTotal();
            if (ptm.isLogscaling()) {
                val = log2(val + 1);
            }
            val /= div;
            conservationGP.lineTo((x2 + x1) / 2, yOffset + (1 - val) * (graphLineHeigh - 4) + 2);
        }
        g.setColor(Color.BLACK);
        conservationGP.lineTo(screenWidth + 5, yOffset + graphLineHeigh);
        g.draw(conservationGP);
        g.setColor(Color.BLACK);
        g.drawLine(0, yOffset, 5, yOffset);
        g.drawString("" + div, 10, yOffset + 10);
        g.drawString("0", 10, yOffset + graphLineHeigh);
        return graphLineHeigh;
    }

    @Override
    public String getTooltip(int mouseX) {
        return null;
    }
}
