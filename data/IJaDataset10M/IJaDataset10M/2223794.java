package org.fao.waicent.kids.editor.service;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.fao.waicent.kids.editor.ImportDatasetWizard;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AddIXFDataset extends kidsService {

    public AddIXFDataset() {
    }

    public AddIXFDataset(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        kidsSession session = request.getSession();
        ImportDatasetWizard wiz = ((ImportDatasetWizard) session.getAttribute("IMPORT_DATASET_WIZARD"));
        wiz.setCurrentStep(wiz.BUILD);
        HttpServletRequest req = session.getHttpServletRequest();
        if (req.getParameter("setting").equals("0")) {
        } else if (req.getParameter("setting").equals("1")) {
        }
        consumed = true;
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }
}
