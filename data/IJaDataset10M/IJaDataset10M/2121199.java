package net.jwde;

import java.io.File;
import java.net.URL;
import net.jwde.object.JWDE;
import net.jwde.object.SupplierProduct;
import org.apache.log4j.Logger;
import org.jdom.Document;

public class JWDERunner {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ObjectPersister objectPersister;

    private InfoExtractor infoExtractor;

    private DOM2Object<JWDE, SupplierProduct> dom2Object;

    public JWDERunner() {
        objectPersister = new ObjectPersisterImpl();
        infoExtractor = new InfoExtractorImpl();
        dom2Object = new DOM2ObjectImpl();
    }

    public String sanitizeString(String string) {
        string = string.replace("\\", "" + File.separatorChar);
        string = string.replace("/", "" + File.separatorChar);
        return string;
    }

    public void run(URL configURL) {
        Document jwdeDoc = infoExtractor.extract(configURL);
        JWDE jwde = dom2Object.process(configURL, jwdeDoc);
        objectPersister.persist(configURL, jwde);
    }

    public Document extract(URL inputURL, URL transformURL, URL outputURL) {
        return infoExtractor.extract(inputURL, transformURL, outputURL);
    }

    public ObjectPersister getObjectPersister() {
        return objectPersister;
    }

    public DOM2Object<JWDE, SupplierProduct> getDOM2Object() {
        return dom2Object;
    }

    public InfoExtractor getInfoExtractor() {
        return infoExtractor;
    }
}
