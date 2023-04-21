package net.sf.otrcutmp4.model.xml.cut;

import java.io.File;
import java.io.FileNotFoundException;
import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.otrcutmp4.test.OtrXmlTstBootstrap;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlAuthor extends AbstractXmlCutTest {

    static final Logger logger = LoggerFactory.getLogger(TestXmlAuthor.class);

    @BeforeClass
    public static void initFiles() {
        fXml = new File(rootDir, "author.xml");
    }

    @Test
    public void testDownload() throws FileNotFoundException {
        Author actual = create();
        Author expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Author.class);
        assertJaxbEquals(expected, actual);
    }

    private static Author create() {
        return create(true);
    }

    public static Author create(boolean withChilds) {
        Author xml = new Author();
        xml.setValue("myAuthor");
        return xml;
    }

    public void save() {
        save(create(), fXml);
    }

    public static void main(String[] args) {
        OtrXmlTstBootstrap.init();
        TestXmlAuthor.initFiles();
        TestXmlAuthor test = new TestXmlAuthor();
        test.save();
    }
}
