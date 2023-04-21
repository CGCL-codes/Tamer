package net.sf.saxon.option.jdom;

import net.sf.saxon.Query;
import net.sf.saxon.trans.XPathException;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Variant of command line net.sf.saxon.Transform do build the source document
 * in JDOM and then proceed with the transformation. This class is provided largely for
 * testing purposes.
 */
public class JDOMQuery extends Query {

    public List preprocess(List sources) throws XPathException {
        try {
            ArrayList jdomSources = new ArrayList(sources.size());
            for (int i = 0; i < sources.size(); i++) {
                SAXSource ss = (SAXSource) sources.get(i);
                SAXBuilder builder = new SAXBuilder();
                org.jdom.Document doc = builder.build(ss.getInputSource());
                DocumentWrapper jdom = new DocumentWrapper(doc, ss.getSystemId(), getConfiguration());
                jdomSources.add(jdom);
            }
            return jdomSources;
        } catch (JDOMException e) {
            throw new XPathException(e);
        } catch (IOException e) {
            throw new XPathException(e);
        }
    }

    public static void main(String[] args) {
        new JDOMQuery().doQuery(args, "JDOMQuery");
    }
}
