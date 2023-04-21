package org.expasy.jpl.matching.annotation.parser;

import java.io.IOException;
import java.util.List;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.expasy.jpl.experimental.ms.peaklist.JPLFragmentationAnnotatedSpectrum;
import org.expasy.jpl.matching.exception.JPLAnnotatedSpectrumBuilderException;
import org.expasy.jpl.matching.exception.JPLSptxtParseException;
import org.junit.Before;
import org.junit.Test;

public class JPLSptxtParserTest {

    static Log logger = LogFactory.getLog(JPLSptxtParserTest.class);

    JPLSptxtFileParser parser;

    private static String filename = ClassLoader.getSystemResource("test.sptxt").getFile();

    @Before
    public void setUp() throws Exception {
        parser = JPLSptxtFileParser.createAnnotationEnabled();
    }

    @Test
    public void testParseNBuild() throws JPLSptxtParseException, IOException {
        parser.parse(filename);
        List<JPLFragmentationAnnotatedSpectrum> spectra = parser.build();
        for (JPLFragmentationAnnotatedSpectrum spectrum : spectra) {
            logger.info(spectrum.getPrecursor());
            logger.info(spectrum);
        }
        Assert.assertEquals(3, spectra.size());
    }

    @Test(expected = JPLAnnotatedSpectrumBuilderException.class)
    public void testBadParseNBuild() throws JPLSptxtParseException, IOException {
        parser.parse(ClassLoader.getSystemResource("test_5-15.mgf").getFile());
        List<JPLFragmentationAnnotatedSpectrum> spectra = parser.build();
        Assert.assertEquals(2, spectra.size());
    }
}
