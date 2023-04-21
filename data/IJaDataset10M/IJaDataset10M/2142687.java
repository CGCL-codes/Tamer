package net.sf.ahtutils.xml.xpath.report;

import java.util.ArrayList;
import java.util.List;
import net.sf.ahtutils.test.AbstractXmlTest;
import net.sf.ahtutils.xml.report.Media;
import net.sf.ahtutils.xml.report.TestMedia;
import net.sf.ahtutils.xml.xpath.ReportXpath;
import net.sf.exlp.util.exception.ExlpXpathNotFoundException;
import net.sf.exlp.util.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JaxbUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestReportMediaXpath extends AbstractXmlTest {

    static final Logger logger = LoggerFactory.getLogger(TestReportMediaXpath.class);

    private Media media1, media2, media3, media4;

    private List<Media> mediaList;

    @Before
    public void iniMedia() {
        media1 = TestMedia.create("t1");
        media1.setDir("d1");
        media2 = TestMedia.create("t2");
        media3 = TestMedia.create("t3");
        media4 = TestMedia.create("t3");
        mediaList = new ArrayList<Media>();
        mediaList.add(media1);
        mediaList.add(media2);
        mediaList.add(media3);
        mediaList.add(media4);
    }

    @Test
    public void testType1() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException {
        Media test = ReportXpath.getMedia(mediaList, media1.getType());
        Assert.assertEquals(JaxbUtil.toString(media1), JaxbUtil.toString(test));
    }

    @Test
    public void testType2() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException {
        Media test = ReportXpath.getMedia(mediaList, media2.getType());
        Assert.assertEquals(JaxbUtil.toString(media2), JaxbUtil.toString(test));
    }

    @Test(expected = ExlpXpathNotFoundException.class)
    public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException {
        ReportXpath.getMedia(mediaList, "nullCode");
    }

    @Test(expected = ExlpXpathNotUniqueException.class)
    public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException {
        ReportXpath.getMedia(mediaList, media3.getType());
    }
}
