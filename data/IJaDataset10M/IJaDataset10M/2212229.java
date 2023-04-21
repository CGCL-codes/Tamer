package net.sf.ahtutils.xml.cloud.facebook;

import java.io.File;
import java.io.FileNotFoundException;
import net.sf.exlp.util.xml.JaxbUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlToken extends AbstractXmlFacebookTest {

    static final Logger logger = LoggerFactory.getLogger(TestXmlToken.class);

    @BeforeClass
    public static void initFiles() {
        fXml = new File(rootDir, "token.xml");
    }

    @Test
    public void test() throws FileNotFoundException {
        Token actual = create();
        Token expected = (Token) JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Token.class);
        assertJaxbEquals(expected, actual);
    }

    private static Token create() {
        return create(false);
    }

    public static Token create(boolean withChilds) {
        Token xml = new Token();
        xml.setCode("myCode");
        xml.setValue("myValue");
        xml.setExpires(getXmlDefaultDate());
        xml.setExpiresIn(10);
        return xml;
    }

    public void save() {
        save(create(), fXml);
    }
}
