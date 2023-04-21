package cwsexamples.wss.retailersystem;

import java.util.Iterator;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.ws_i.sampleapplications.supplychainmanagement._2002_08.retailcatalog.CatalogItem;
import org.ws_i.sampleapplications.supplychainmanagement._2002_08.retailcatalog.CatalogType;
import cwsexamples.wss.util.WSIConstants;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;

public class Catalog extends Artifact {

    private CatalogType catalog;

    @OPERATION
    void init() throws JAXBException {
        log("init");
        catalog = WSIConstants.initCatalog();
    }

    @OPERATION
    void insertProduct(CatalogItem item) {
        catalog.getItem().add(item);
    }

    @OPERATION
    void removeProduct(int productNumber) {
        List<CatalogItem> items = catalog.getItem();
        Iterator<CatalogItem> it = items.iterator();
        while (it.hasNext()) {
            CatalogItem currentItem = it.next();
            if (currentItem.getProductNumber() == productNumber) {
                items.remove(currentItem);
                break;
            }
        }
    }

    @OPERATION
    void containsProduct(int productNumber, OpFeedbackParam<Boolean> res) {
        List<CatalogItem> items = catalog.getItem();
        Iterator<CatalogItem> it = items.iterator();
        while (it.hasNext()) {
            CatalogItem current = it.next();
            if (current.getProductNumber() == productNumber) {
                res.set(true);
                return;
            }
        }
        res.set(false);
    }

    @OPERATION
    void getCatalog(OpFeedbackParam<CatalogType> res) {
        res.set(catalog);
    }
}
