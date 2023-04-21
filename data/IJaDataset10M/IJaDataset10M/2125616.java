package org.fao.waicent.kids.editor.service;

import java.io.IOException;
import org.fao.waicent.kids.editor.ImportDatasetWizard;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.fao.waicent.xmap2D.FeatureLayer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class InitImportDatasetWizardFrImportLayer extends kidsService {

    public InitImportDatasetWizardFrImportLayer() {
    }

    public InitImportDatasetWizardFrImportLayer(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        kidsSession session = request.getSession();
        FeatureLayer layer = ((FeatureLayer) session.getMap().getLayer(session.getMap().size() - 1));
        ImportDatasetWizard wiz = new ImportDatasetWizard(layer, true);
        session.setAttribute("IMPORT_DATASET_WIZARD", wiz);
        consumed = true;
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }
}
