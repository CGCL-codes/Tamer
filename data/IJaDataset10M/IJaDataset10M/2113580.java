package fmpp.testsuite;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import fmpp.Engine;
import fmpp.LocalDataBuilder;
import fmpp.TemplateEnvironment;

/**
 * @author D�niel D�k�ny
 * @version $Id: TestXmlLocalDataBuilder.java,v 1.1 2004/01/19 20:08:30 ddekany Exp $
 */
public class TestXmlLocalDataBuilder implements LocalDataBuilder {

    public Map build(Engine eng, TemplateEnvironment env) throws Exception {
        Document doc = (Document) env.getXmlDocument();
        Map ld = new HashMap();
        ld.put("a", doc.getDocumentElement().getLocalName());
        return ld;
    }
}
