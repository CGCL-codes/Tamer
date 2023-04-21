package org.expasy.jpl.matching.annotation.parser;

import java.text.ParseException;
import junit.framework.Assert;
import org.expasy.jpl.experimental.ms.peaklist.JPLFragmentationAnnotatedSpectrum;
import org.expasy.jpl.matching.annotation.parser.JPLSptxtEntryParser;
import org.expasy.jpl.matching.exception.JPLSptxtParseException;
import org.junit.Before;
import org.junit.Test;

public class JPLSptxtAnnotSpectrumParserTest {

    JPLSptxtEntryParser parser;

    String nameDownToFullName = "Name: IYQY[243]IQSR/3\nLibID: 0\nMW: 1152.5438" + "\nPrecursorMZ: 384.1813\nStatus: Normal\nFullName: R.IYQY[243]IQSR.F/3\n";

    String commentsData = "Comment: AvePrecursorMz=384.4051 BinaryFileOffset=378 " + "ConsFracAssignedPeaks=0.708 DeltaCn=0.1070 DotConsensus=0.85,0.02;0/2 " + "FracUnassigned=0.00,0/5;0.02,2/20;0.06,19/65 Inst=0 MaxRepSN=47.9 " + "Mods=1/3,Y,Phospho NAA=8 NMC=0 NTT=2 Nreps=2/2 OrigMaxIntensity=4.1e+03 " + "Parent=384.181 Pep=Tryptic Prob=0.9332 ProbRange=0.989579,0.9332,0.8886,0.844 " + "Protein=8/IPI00182717/IPI00000352/IPI00014344/IPI00215873/IPI00219250/IPI00219251/IPI00219252/IPI00332215 " + "RepFracAssignedPeaks=0.492 RepNumPeaks=216.5/56.5 RetentionTime=0.0,0.0,0.0 " + "SN=200.0 Sample=1/TiO2,2,2 Se=1^S2:dc=0.1320/0.0250,fv=2.0476/0.1727," + "pb=0.8886/0.0446,xc=2.4030/0.0450 Spec=Consensus XCorr=2.4480\n";

    String peakNumberData = "NumPeaks: 10\n";

    String peaksData = "229.0984	48.5	y4-45^2/-0.54	2/2 0.0240|0.38\n" + "231.0454	10.0	?	2/2 0.0020|0.56\n" + "242.1390	87.8	?	2/2 0.0196|0.72\n" + "243.1096	67.1	y4-18^2/-0.04,y5-18^3/-0.33	2/2 0.0289|0.62\n" + "243.9398	51.4	y2-18/-0.20,y4-17^2/0.30,y5-17^3/0.17	2/2 0.0034|0.67\n" + "245.1060	24.0	y2-17*^4/-0.02,a5^3/-0.01	2/2 0.0539|0.24\n" + "262.0760	2395.6	y2/-0.08	2/2 0.0318|0.07\n" + "263.1250	189.8	y2-18i^3/0.05	2/2 0.0343|0.29\n" + "276.6047	35.8	b2/-0.55,y6-45^3/-0.52,b4-98^2/0.97	2/2 0.1259|0.48\n" + "280.2172	31.6	?	2/2 0.0353|0.08\n";

    String sptxtSpectrumString = nameDownToFullName + commentsData + peakNumberData + peaksData;

    String sptxtSpectrumBadPeakNumberString1 = nameDownToFullName + commentsData + "NumPeaks: 10\n" + peaksData;

    String sptxtSpectrumBadPeakNumberString2 = nameDownToFullName + commentsData + "NumPeaks: 8\n" + peaksData;

    String sptxtSpectrumNoPeaksString = nameDownToFullName + commentsData + peakNumberData;

    @Before
    public void setUp() throws Exception {
        parser = JPLSptxtEntryParser.createAnnotationEnabled();
    }

    @Test
    public void testGetBuilder() {
        Assert.assertNotNull(parser.getBuilder());
    }

    @Test
    public void testPeakParser() throws ParseException {
        String[] annots = { "y3^3/-0.63,b3-17^3/0.04	2/2 0.0455|0.42", "?	2/2 0.0313|1.02", "y1/-0.12	2/2 0.1313|0.21", "y3-18^2/0.07,y3-17^2/-0.42	2/2 0.0176|0.25", "b4-80^3/0.99	2/2 0.0093|0.87", "y3^2/-0.17,b3-17^2/0.84	2/2 0.0892|0.34" };
        parser.parse(sptxtSpectrumString);
        for (String annot : annots) {
            System.out.println(parser.getAnnotations(annot));
        }
    }

    @Test
    public void testParseNBuild() throws JPLSptxtParseException {
        parser.parse(sptxtSpectrumString);
        JPLFragmentationAnnotatedSpectrum spectrum = parser.build();
        System.out.println(spectrum);
        Assert.assertEquals("IYQY[243]IQSR", spectrum.getPeptide());
        Assert.assertEquals(10, spectrum.getNbPeak());
        Assert.assertEquals(229.0984, spectrum.getMzAt(0));
        Assert.assertEquals(2, spectrum.getAnnotationsAtPeak(3).size());
        Assert.assertEquals(3, spectrum.getAnnotationsAtPeak(4).size());
        System.out.println(spectrum.getProteinTag());
        Assert.assertEquals(97, spectrum.getProteinTag().length());
    }

    @Test(expected = JPLSptxtParseException.class)
    public void sptxtSpectrumBadPeakNumberString2() throws JPLSptxtParseException {
        parser.parse(sptxtSpectrumBadPeakNumberString2);
        parser.build();
    }
}
