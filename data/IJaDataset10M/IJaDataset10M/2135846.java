package org.expasy.jpl.core.ms.spectrum.editor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections15.Transformer;
import org.expasy.jpl.commons.base.cond.Condition;
import org.expasy.jpl.commons.base.cond.ConditionImpl;
import org.expasy.jpl.commons.base.io.Serializer;
import org.expasy.jpl.core.ms.export.MSRenderer;
import org.expasy.jpl.core.ms.spectrum.PeakList;
import org.expasy.jpl.core.ms.spectrum.editor.intensity.OverExpressedPeakTransformer;
import org.expasy.jpl.core.ms.spectrum.filter.PeakListsFilter;
import org.expasy.jpl.core.ms.spectrum.peak.Peak;
import org.expasy.jpl.core.ms.spectrum.stat.MS1MS2PeakDists;
import org.junit.BeforeClass;
import org.junit.Test;

public class JPLIntensityAttenuatorTest {

    static IntensityTransformer editor;

    static OverExpressedPeakTransformer attenuator;

    static MS1MS2PeakDists dists;

    static List<PeakListsFilter> PRECURSOR_CHARGE_PEAKLIST_FILTERS;

    static List<PeakList> pls;

    public static void createFilters() {
        Transformer<PeakList, Integer> PL_TO_PREC_CHARGE = new Transformer<PeakList, Integer>() {

            public Integer transform(PeakList spectrum) {
                Peak prec = spectrum.getPrecursor();
                if (prec == null) {
                    return 0;
                } else {
                    return prec.getCharge();
                }
            }
        };
        PRECURSOR_CHARGE_PEAKLIST_FILTERS = new ArrayList<PeakListsFilter>();
        for (int i = 1; i < 10; i++) {
            Condition<PeakList> cond = new ConditionImpl.Builder<PeakList, Integer>(i).accessor(PL_TO_PREC_CHARGE).build();
            PeakListsFilter filter = PeakListsFilter.newInstance();
            filter.enableOnPlaceEdition();
            filter.addCondition(cond);
            PRECURSOR_CHARGE_PEAKLIST_FILTERS.add(filter);
        }
    }

    /**
	 * TODO: do not execute these tests if file "etd.ser" not found
	 */
    @BeforeClass
    public static void setUp() throws Exception {
        createFilters();
        Serializer<ArrayList<PeakList>> serializer = Serializer.newInstance();
        String filename = ClassLoader.getSystemResource("etd.ser").getFile();
        pls = serializer.deserialize(filename);
        PRECURSOR_CHARGE_PEAKLIST_FILTERS.get(2).transform(pls);
        System.out.println(pls.size() + " spectra");
        dists = new MS1MS2PeakDists.Builder(pls.iterator()).build();
        attenuator = new OverExpressedPeakTransformer(dists);
        editor = new IntensityTransformer(attenuator);
    }

    @Test
    public void testAttenuator() throws FileNotFoundException {
        System.out.println("threshold = " + attenuator.getRatioThreshold());
        PeakList pl1 = pls.get(0);
        PeakList pl2 = editor.transform(pl1);
        System.out.println(pl1);
        System.out.println(pl2);
        MSRenderer renderer = MSRenderer.newInstance();
        renderer.setXAxisLegend("mass-to-charge");
        renderer.exportChart(pl1, "/tmp/g1", "graph-attenuate1");
        renderer.exportChart(pl2, "/tmp/g2", "graph-attenuate2");
    }

    public void filterAll() throws IOException {
        Serializer<ArrayList<PeakList>> serializer = Serializer.newInstance();
        List<PeakList> pls = serializer.deserialize("/home/def/Documents/SIB/jpl/data/serial/etd.ser");
        System.out.println(pls.size() + " spectra");
        editor = new IntensityTransformer(new OverExpressedPeakTransformer(dists));
        editor.enableOnPlaceEdition();
        for (PeakList pl : pls) {
            attenuator.transform(pl);
        }
    }
}
