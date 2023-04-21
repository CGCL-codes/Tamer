package com.knitml.core.utils;

import static junit.framework.Assert.assertEquals;
import java.io.InputStream;
import javax.measure.Measurable;
import javax.measure.quantity.Length;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import com.knitml.core.common.ValidationException;
import com.knitml.core.units.Gauge;
import com.knitml.core.xml.ParsingUtils;

public class ParsingUtilsTests {

    private static final String PACKAGE_NAME = ParsingUtilsTests.class.getPackage().getName();

    private Document getDocument(String xmlFile) throws Exception {
        InputStream is = null;
        try {
            String fileName = "/" + PACKAGE_NAME.replace('.', '/') + "/" + xmlFile;
            is = this.getClass().getResourceAsStream(fileName);
            SAXReader reader = new SAXReader();
            return reader.read(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @Test
    public void deriveStitchesPerInchFromDocument() throws Exception {
        Document document = getDocument("st-per-in.xml");
        Measurable<? extends Gauge> gauge = ParsingUtils.getGaugeForElement(document.getRootElement());
        assertEquals("3.5 st/in", gauge.toString());
    }

    @Test
    public void deriveStitchesPerCentimeterFromDocument() throws Exception {
        Document document = getDocument("st-per-cm.xml");
        Measurable<? extends Gauge> gauge = ParsingUtils.getGaugeForElement(document.getRootElement());
        assertEquals("3.5 st/cm", gauge.toString());
    }

    @Test
    public void deriveInchesFromDocument() throws Exception {
        Document document = getDocument("in.xml");
        Measurable<Length> length = ParsingUtils.getLengthForElement(document.getRootElement());
        assertEquals("3 in", length.toString());
    }

    @Test
    public void deriveCentimetersFromDocument() throws Exception {
        Document document = getDocument("cm.xml");
        Measurable<Length> length = ParsingUtils.getLengthForElement(document.getRootElement());
        assertEquals("3 cm", length.toString());
    }

    @Test(expected = ValidationException.class)
    public void attemptToDeriveGaugeFromLength() throws Exception {
        Document document = getDocument("st-per-cm.xml");
        ParsingUtils.getLengthForElement(document.getRootElement());
    }

    @Test(expected = ValidationException.class)
    public void attemptToDeriveLengthFromGauge() throws Exception {
        Document document = getDocument("in.xml");
        ParsingUtils.getGaugeForElement(document.getRootElement());
    }
}
