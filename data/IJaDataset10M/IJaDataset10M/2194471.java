package org.ensembl.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.ensembl.datamodel.Exon;
import org.ensembl.datamodel.ExternalDatabase;
import org.ensembl.datamodel.Location;
import org.ensembl.datamodel.MiscFeature;
import org.ensembl.datamodel.Transcript;
import org.ensembl.datamodel.impl.AttributeImpl;
import org.ensembl.datamodel.impl.ExonImpl;
import org.ensembl.datamodel.impl.ExternalDatabaseImpl;
import org.ensembl.datamodel.impl.MiscFeatureImpl;
import org.ensembl.datamodel.impl.TranscriptImpl;
import org.ensembl.probemapping.MappableTranscript;

/**
 * Tests the ProbeMapping package.
 * @author <a href="mailto:craig@ebi.ac.uk">Craig Melsopp</a>
 */
public class ProbeMappingTest extends TestCase {

    ExternalDatabase xDB = new ExternalDatabaseImpl();

    public ProbeMappingTest(String name) {
        super(name);
        xDB.setName("array1");
        xDB.setInternalID(3);
    }

    public void testProbedTranscript() throws ParseException {
        Transcript t = (Transcript) sampleTranscripts().get(0);
        int flank3 = 2000;
        MappableTranscript pt = new MappableTranscript(t, flank3);
        assertEquals(t.getCDNALocation().getLength() + flank3, pt.getLocation().getLength());
    }

    public void testDummyData() throws Exception {
    }

    public void testFilterProbeSetsByMode() throws Exception {
    }

    public void testFilterProbeSetsByTranscriptCount() throws Exception {
    }

    public void testFilterByTranscriptFrequency() throws Exception {
        MappableTranscript pt0 = createMappableTranscript();
        MappableTranscript pt1 = createMappableTranscript();
        MappableTranscript pt2 = createMappableTranscript();
        MappableTranscript pt3 = createMappableTranscript();
    }

    private MappableTranscript createMappableTranscript() throws Exception {
        Transcript t = (Transcript) sampleTranscripts().get(0);
        return new MappableTranscript(t, 2000);
    }

    private List sampleTranscripts() throws ParseException {
        List es = new ArrayList();
        Exon e = new ExonImpl(1, new Location("chromosome:1:50-150"));
        es.add(e);
        Transcript tt = new TranscriptImpl();
        tt.setLocation(e.getLocation());
        tt.setExons(es);
        List l = new ArrayList();
        l.add(tt);
        return l;
    }

    /**
   * @return
   */
    private List sampleProbeMiscFeatures() throws Exception {
        List l = new ArrayList();
        l.add(createMiscFeature(new Location("chromosome:1:100-125"), "probeName", "Probe name", "the name of the probe", "HG-Focus:215193_x_at:332:117;"));
        l.add(createMiscFeature(new Location("chromosome:1:11050-11075"), "probeName", "Probe name", "the name of the probe", "HG-Focus:215193_x_at:332:118;"));
        return l;
    }

    /**
   * @param location
   * @param string
   * @param string2
   * @param string3
   * @param string4
   * @return
   */
    private MiscFeature createMiscFeature(Location location, String code, String name, String description, String value) {
        MiscFeature mf;
        mf = new MiscFeatureImpl();
        mf.setLocation(location);
        mf.add(new AttributeImpl(code, name, description, value));
        return mf;
    }
}
