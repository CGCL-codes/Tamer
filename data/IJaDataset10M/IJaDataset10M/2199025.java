package droid;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.exolab.castor.xml.Unmarshaller;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import droid.dao.SequenceDao;
import droid.domain.InternalSignature;
import droid.util.UnmarshallerFactory;

public class TestJarSequenceDao implements SequenceDao {

    private UnmarshallerFactory unmarshallerFactory;

    private InternalSignature internalSignature;

    private List<InternalSignature> internalSignatures;

    public void intialize() throws Exception {
        unmarshallerFactory = new UnmarshallerFactory();
        Unmarshaller unmarshaller = unmarshallerFactory.createUnmarshaller(InternalSignature.class);
        URL url = ResourceUtils.getURL("classpath:droid/JarInternalSignature.xml");
        Document doc = parseXmlUrl(url, false);
        Element docElement = (Element) doc.getDocumentElement();
        internalSignature = (InternalSignature) unmarshaller.unmarshal(docElement);
        internalSignatures = new ArrayList<InternalSignature>();
        internalSignatures.add(internalSignature);
    }

    public List<InternalSignature> retrieveKnownInternalSignatures() {
        return new ArrayList<InternalSignature>(internalSignatures);
    }

    public Document parseXmlUrl(URL url, boolean validating) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        System.out.println("Using document builder factory [" + factory.getClass().getName() + "]");
        factory.setValidating(validating);
        Document doc = factory.newDocumentBuilder().parse(url.openStream());
        return doc;
    }
}
