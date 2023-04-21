package com.vmware.vcloud;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import org.dom4j.Element;
import com.vmware.vcloud.rest.RestClient;

/**
 * @author Steve Jin (sjin@vmware.com)
 */
public class CatalogItem extends VCloudObject {

    String licenseCost = null;

    VAppTemplate vAppTemplate = null;

    public CatalogItem(RestClient rc, String url, String name) throws MalformedURLException {
        super(rc, url, name);
    }

    @Override
    protected void parse() throws MalformedURLException {
        Element entity = root.element("Entity");
        String href = entity.attributeValue("href");
        String name = entity.attributeValue("name");
        vAppTemplate = new VAppTemplate(rc, href, name);
        List<?> props = root.elements("Property");
        for (int i = 0; i < props.size(); i++) {
            Element prop = (Element) props.get(i);
            if ("LicensingCost".equals(prop.attributeValue("key"))) {
                this.licenseCost = prop.getText();
            }
        }
    }

    public VAppTemplate getVAppTemplate() throws IOException {
        loadContentIfNotYet();
        return vAppTemplate;
    }
}
