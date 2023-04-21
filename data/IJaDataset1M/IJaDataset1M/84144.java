package org.expasy.jpl.msident.format.pepxml;

import org.expasy.jpl.commons.collection.graph.Edge;
import org.expasy.jpl.commons.collection.graph.KPartiteGraphImpl;
import org.expasy.jpl.commons.collection.graph.Node;
import org.expasy.jpl.msident.format.pepxml.MSMSRunSummaryElement.Base;
import org.expasy.jpl.msident.format.pepxml.MSMSRunSummaryElement.SpectrumQueryListElement;
import org.expasy.jpl.msident.format.pepxml.io.MSMSRunSummary2MSnRunTransformer;
import org.expasy.jpl.msident.format.pepxml.io.MSnRun;
import org.junit.Before;
import org.junit.Test;

public class MSMSRunSummaryElementTest {

    Base baseElt;

    SpectrumQueryListElement sqlElt;

    MSMSRunSummaryElement mmrsElt;

    @Before
    public void setUp() throws Exception {
        baseElt = new Base("/data/albu-co3.mgf", "raw", ".mgf");
        baseElt.addSearchSummary(SearchSummaryTest.createElement());
        SpectrumQueryElement spectrumQuery = new SpectrumQueryElement("2008_12_11_ALS_08_LY01_J00_c.01099.01099.3", 1099, 1099, 1312.81, 1545.784, 3, 33, 1);
        spectrumQuery.addSearchHit(SpectrumQueryTest.createSearchHitElement(), 1);
        spectrumQuery.addSearchHit(SpectrumQueryTest.createSearchHitElement(), 2);
        sqlElt = new SpectrumQueryListElement();
        sqlElt.addSpectrumQuery(spectrumQuery);
        mmrsElt = new MSMSRunSummaryElement(baseElt, sqlElt);
    }

    @Test
    public void test() {
        System.out.println(mmrsElt.toPepXMLString());
    }

    @Test
    public void testTransformation() {
        MSMSRunSummary2MSnRunTransformer transformer = new MSMSRunSummary2MSnRunTransformer();
        MSnRun run = transformer.transform(mmrsElt);
        KPartiteGraphImpl<Node, Edge> g = run.getGraph();
        System.out.println(g.getVertexCount());
        System.out.println(g.getEdgeCount());
        System.out.println(g.getEdges());
        System.out.println(run.getGraph());
        System.out.println(run.getSearchEngine(1));
        System.out.println(run.getEnzyme());
    }
}
