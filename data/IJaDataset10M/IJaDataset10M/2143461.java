package de.ulrich_fuchs.jtypeset.test;

import java.awt.Font;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import de.ulrich_fuchs.jtypeset.Layouter;
import de.ulrich_fuchs.jtypeset.MeasureFlowContext;
import de.ulrich_fuchs.jtypeset.Page;
import de.ulrich_fuchs.jtypeset.PdfPage;
import de.ulrich_fuchs.jtypeset.stream.ElementStream;
import de.ulrich_fuchs.jtypeset.stream.FloatCluster;
import de.ulrich_fuchs.jtypeset.stream.InlineImageChunk;
import de.ulrich_fuchs.jtypeset.stream.ParagraphCluster;
import de.ulrich_fuchs.jtypeset.stream.TextChunk;
import de.ulrich_fuchs.jtypeset.stream.FloatCluster.EAlign;
import de.ulrich_fuchs.jtypeset.stream.FloatCluster.ESize;

/**
 *
 * @author  ulrich
 */
public class TestFloat extends FloatCluster {

    public Map<Double, Double> heights;

    /** Creates a new instance of TestFloat */
    protected TestFloat(ElementStream substream) {
        super(substream);
        setMaxSize(ESize.LARGE);
        setMinSize(ESize.MINI);
        alignment = EAlign.RIGHT;
        heights = new TreeMap<Double, Double>();
    }

    public static TestFloat newTestFloat() {
        ElementStream s = new ElementStream();
        ParagraphCluster p = new ParagraphCluster();
        InlineImageChunk img;
        try {
            img = new InlineImageChunk(new URL("file:///home/ulrich/europa_physisch.jpg"), 2500, 2392, 0.0);
            s.appendElement(p);
        } catch (Exception exc) {
            System.err.println(exc);
        }
        p = new ParagraphCluster();
        Font f1 = Font.decode("Nimbus Roman No9 L-6");
        TextChunk t = new TextChunk("Ein Klavier, ein Klavier!", f1);
        p.getSubstream().appendElement(t);
        s.appendElement(p);
        return new TestFloat(s);
    }

    /** param metricsPage the page that will be used to retreive all metrics data */
    public double getHeight(double width, Page metricsPage) {
        Double cachedHeight = heights.get(width);
        if (cachedHeight != null) return cachedHeight;
        MeasureFlowContext mfc = new MeasureFlowContext(null, width);
        mfc.metricsPage = metricsPage;
        Layouter lo = new Layouter();
        getSubstream().reset();
        lo.testLayout(getSubstream(), mfc);
        getSubstream().reset();
        heights.put(width, mfc.getCurrentPosition().y + 1);
        return mfc.getCurrentPosition().y + 1;
    }
}
