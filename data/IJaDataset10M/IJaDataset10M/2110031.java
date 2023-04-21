package org.fao.waicent.kids.giews.service;

import java.io.IOException;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xpath.XPathAPI;
import org.fao.waicent.kids.giews.GIEWSConfiguration;
import org.fao.waicent.kids.giews.GIEWSGeonetworkConfiguration;
import org.fao.waicent.kids.giews.GiewsMetadataLoader;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.fao.waicent.util.Debug;
import org.fao.waicent.xmap2D.BaseLayer;
import org.fao.waicent.xmap2D.FeatureLayer;
import org.fao.waicent.xmap2D.GroupLayer;
import org.fao.waicent.xmap2D.layer.BaseGroupLayer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GiewsCreateMetadata extends kidsService {

    public GiewsCreateMetadata() {
    }

    public GiewsCreateMetadata(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        kidsSession kids = request.getSession();
        BaseLayer node = (BaseLayer) kids.getMap().getLayerNode(request.setting);
        GIEWSConfiguration configuration = (GIEWSConfiguration) kids.getConfiguration();
        GIEWSGeonetworkConfiguration geonetwork = configuration.getGeonetworkConfiguration();
        String contextPath = geonetwork.getFullContextPath() + "/" + configuration.getLanguageCode();
        String proj_id = "";
        String layer_name = "";
        GroupLayer group = node.getParent();
        String group_name = null;
        String raster_group_name = null;
        if (group != null && group instanceof BaseGroupLayer) {
            group_name = group.getName();
            GroupLayer raster_group = group.getParent();
            if (raster_group != null && raster_group instanceof BaseGroupLayer) {
                raster_group_name = raster_group.getName();
            }
        }
        if (node != null) {
            Debug.println("***** CreateMetadataServlet ****");
            String oldMDID = "";
            String proj_name = "";
            try {
                proj_name = kids.getMapContext().getSelectedMapName();
                proj_id = GiewsMetadataLoader.getProjID(proj_name);
                layer_name = node.getName();
                if (node instanceof FeatureLayer) oldMDID = GiewsMetadataLoader.getMetadataFeature(layer_name, proj_id, group_name); else oldMDID = GiewsMetadataLoader.getMetadataRaster(layer_name, proj_id, group_name, raster_group_name);
            } catch (Exception e) {
            }
            if (oldMDID != null && oldMDID.compareTo("") != 0) {
                String url = contextPath + "/" + geonetwork.getMd_delete() + "?id=" + oldMDID;
                try {
                    URL page = new URL(url);
                    page.getContent();
                } catch (Exception e) {
                }
            }
            HttpServletRequest req = kids.getHttpServletRequest();
            String url = contextPath + "/" + geonetwork.getMd_insert();
            Element root_element = null;
            try {
                URL page = new URL(url);
                DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = docBuilder.parse(page.openConnection().getInputStream());
                root_element = (Element) XPathAPI.selectSingleNode((Node) document, "/response/id");
            } catch (Exception ex) {
                System.out.println("Error in getting XML contents for Metadata" + ex.getMessage());
                ex.printStackTrace();
            }
            if (root_element != null) {
                String md_id = root_element.getChildNodes().item(0).getNodeValue();
                Debug.println("***** Create MD_ID = " + md_id);
                int res = 0;
                try {
                    if (node instanceof FeatureLayer) {
                        res = GiewsMetadataLoader.updateMetadataFeature(md_id, layer_name, proj_id, group_name);
                    } else {
                        res = GiewsMetadataLoader.updateMetadataRaster(md_id, layer_name, proj_id, group_name, raster_group_name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (res == 1) {
                    url = contextPath + "/" + geonetwork.getMd_store() + "?id=" + md_id;
                } else {
                    url = contextPath + "/" + geonetwork.getMd_abort() + "?id=" + md_id;
                }
                try {
                    URL page = new URL(url);
                    page.getContent();
                } catch (Exception e) {
                }
            } else {
                Debug.println("***** Error Create Metadata ****");
            }
            consumed = true;
        }
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }
}
