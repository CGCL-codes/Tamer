package net.sf.ahtutils.xml.mail;

import java.io.File;
import net.sf.ahtutils.test.AbstractXmlTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractXmlMailTest extends AbstractXmlTest {

    static final Logger logger = LoggerFactory.getLogger(AbstractXmlMailTest.class);

    protected static final String rootDir = "src/test/resources/data/xml/mail";

    protected static File fXml;
}
