package org.fao.waicent.kids.giews.service;

import java.io.IOException;
import org.fao.waicent.attributes.AttributesExternalizer;
import org.fao.waicent.kids.giews.GIEWSConfiguration;
import org.fao.waicent.kids.giews.GIEWSGeonetworkConfiguration;
import org.fao.waicent.kids.giews.GiewsMetadataLoader;
import org.fao.waicent.kids.giews.ImportMetadataWizard;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.fao.waicent.xmap2D.BaseLayer;
import org.fao.waicent.xmap2D.FeatureLayer;
import org.fao.waicent.xmap2D.Layer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class InitMetadataWizard extends kidsService {

    public InitMetadataWizard() {
    }

    public InitMetadataWizard(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        boolean resource = false;
        kidsSession session = request.getSession();
        int comma_index = request.setting.indexOf(",");
        String type = "";
        String param = "";
        if (comma_index > 0) {
            type = request.setting.substring(0, comma_index);
            param = request.setting.substring(comma_index + 1);
        } else {
            type = request.setting;
        }
        GIEWSConfiguration configuration = (GIEWSConfiguration) session.getConfiguration();
        GIEWSGeonetworkConfiguration geonetwork = configuration.getGeonetworkConfiguration();
        ImportMetadataWizard wiz = new ImportMetadataWizard(configuration);
        if (type.compareTo("DATASET") == 0) {
            Layer node = session.getMap().getSelectedFeatureLayer();
            if (node != null && node instanceof FeatureLayer) {
                FeatureLayer sel_layer = (FeatureLayer) node;
                AttributesExternalizer obj = (AttributesExternalizer) (sel_layer).getExternalizerAt(Integer.parseInt(param));
                if (obj != null) {
                    wiz.setResourceType(ImportMetadataWizard.DATASET);
                    wiz.setAttribute(obj);
                    wiz.setLayer(sel_layer);
                    resource = true;
                }
            }
        } else if (type.compareTo("LAYER") == 0) {
            BaseLayer node = (BaseLayer) session.getMap().getLayerNode(param);
            if (node != null) {
                wiz.setResourceType(ImportMetadataWizard.LAYER);
                wiz.setLayer(node);
                String proj_name = session.getMapContext().getSelectedMapName();
                try {
                    String proj_id = GiewsMetadataLoader.getProjID(proj_name);
                    System.out.println("Import MetadataWizard: set ProjID=" + proj_id);
                    wiz.setProjectID(proj_id);
                    resource = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (resource) {
            session.setAttribute("IMPORT_METADATA_WIZARD", wiz);
        }
        consumed = true;
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }
}
