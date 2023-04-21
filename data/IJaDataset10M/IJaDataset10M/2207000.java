package au.edu.uq.itee.maenad.pronto.importer.bioportal;

import au.edu.uq.itee.maenad.pronto.importer.base.AbstractCategoryManager;
import au.edu.uq.itee.maenad.pronto.importer.util.XmlHelper;
import java.io.IOException;
import java.net.URL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BioPortalCategoryManager extends AbstractCategoryManager {

    static final String TOP_CATEGORY_NAME = "~BioPortal~";

    private static final String URI_BASE = "http://www.itee.uq.edu.au/maenad/pronto/categories/bioportal";

    private static final String BIOPORTAL_CATEGORY_PATH = "/categories";

    private final String baseUrl;

    public BioPortalCategoryManager(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public void createCategories(URL targetUrl, String username, String password) throws IOException {
        long topLevelId = submitCategory(targetUrl, username, password, TOP_CATEGORY_NAME, URI_BASE);
        storeIdByCategoryName(TOP_CATEGORY_NAME, Long.valueOf(topLevelId));
        Document bioportalCategoryFile = XmlHelper.downloadXmlFile(baseUrl + BIOPORTAL_CATEGORY_PATH);
        NodeList entries = bioportalCategoryFile.getElementsByTagName("categoryBean");
        for (int i = 0; i < entries.getLength(); i++) {
            Element entry = (Element) entries.item(i);
            String id = ((Element) entry.getElementsByTagName("id").item(0)).getTextContent();
            String name = ((Element) entry.getElementsByTagName("name").item(0)).getTextContent();
            long newCatId = submitCategory(targetUrl, username, password, name, URI_BASE + "#" + id, topLevelId);
            storeIdByCategoryName(id, newCatId);
        }
    }
}
