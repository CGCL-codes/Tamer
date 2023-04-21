package org.fao.waicent.kids.editor.service;

import java.io.IOException;
import org.fao.waicent.attributes.Attributes;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.fao.waicent.xmap2D.FeatureLayer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SaveMatrix extends kidsService {

    public SaveMatrix() {
    }

    public SaveMatrix(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        kidsSession session = request.getSession();
        try {
            session.change("SET_MATRIX", "");
            if (session.getAttributes().isModified()) {
                session.change("SAVE_DATASET", "");
                session.getAttributes().setModified(false);
            } else if (session.getMap().getSelectedFeatureLayer() != null) {
                FeatureLayer flayer = session.getMap().getSelectedFeatureLayer();
                int attribute_index = flayer.getSelectedAttributesIndex();
                Attributes attributes = flayer.getAttributes();
                attributes.setSaveMatrix(true);
                flayer.saveAttributes(attributes, attribute_index);
                flayer.getAttributes().setSaveMatrix(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        consumed = true;
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }
}
