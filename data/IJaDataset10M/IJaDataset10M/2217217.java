package net.sf.otrcutmp4.controller.processor.exlp;

import java.io.File;
import java.io.FileNotFoundException;
import net.sf.exlp.util.exception.ExlpConfigurationException;
import net.sf.exlp.util.io.StringIO;
import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.otrcutmp4.controller.processor.exlp.parser.LinkListParser;
import net.sf.otrcutmp4.model.xml.otr.Linklist;
import net.sf.otrcutmp4.test.AbstractUtilTest;
import net.sf.otrcutmp4.test.OtrUtilTstBootstrap;
import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLinklistConverter extends AbstractUtilTest {

    static final Logger logger = LoggerFactory.getLogger(TestLinklistConverter.class);

    private static String srcLinklist;

    private static File dstXml;

    @BeforeClass
    public static void initFiles() {
        srcLinklist = StringIO.loadTxt("src/test/resources/data/linklist.txt");
        dstXml = new File("src/test/resources/xml", "linklist.xml");
    }

    @Test
    public void testConverter() throws FileNotFoundException {
        LinklistConverter llc = new LinklistConverter();
        Linklist actual = llc.convert(srcLinklist);
        Linklist expected = JaxbUtil.loadJAXB(dstXml.getAbsolutePath(), Linklist.class);
        assertJaxbEquals(expected, actual);
    }

    @Test
    public void testNormal() throws FileNotFoundException {
        LinklistConverter llc = new LinklistConverter();
        Linklist actual = llc.convert(srcLinklist);
        Assert.assertEquals("Not only 3 download type", 3, actual.getDownload().size());
        Assert.assertEquals("Not normal type", LinkListParser.secNormal, actual.getDownload().get(0).getType());
        Assert.assertEquals("Not normal type", LinkListParser.secPrio, actual.getDownload().get(1).getType());
        Assert.assertEquals("Not normal type", LinkListParser.secPay, actual.getDownload().get(2).getType());
    }

    public void save() {
        LinklistConverter llc = new LinklistConverter();
        Linklist xml = llc.convert(srcLinklist);
        JaxbUtil.debug(this.getClass(), xml);
        JaxbUtil.save(dstXml, xml, nsPrefixMapper, true);
    }

    public static void main(String[] args) throws ExlpConfigurationException {
        Configuration config = OtrUtilTstBootstrap.init();
        TestLinklistConverter.initPrefixMapper();
        TestLinklistConverter.initFiles();
        TestLinklistConverter test = new TestLinklistConverter();
        test.save();
        String txt = StringIO.loadTxt(config.getString("test.ll.error1"));
        LinklistConverter llc = new LinklistConverter();
        Linklist xml = llc.convert(txt);
        JaxbUtil.debug(xml);
    }
}
