package org.fao.waicent.kids.giews.service;

import java.io.File;
import java.io.IOException;
import org.fao.waicent.kids.editor.ImportDatasetFromFile;
import org.fao.waicent.kids.editor.ImportDatasetHandler;
import org.fao.waicent.kids.editor.ImportDatasetWizard;
import org.fao.waicent.kids.giews.communication.apimodule.ApiModule;
import org.fao.waicent.kids.giews.communication.utility.DownloadInfo;
import org.fao.waicent.kids.giews.communication.utility.Resources;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GiewsImportDataset extends kidsService {

    public GiewsImportDataset() {
    }

    public GiewsImportDataset(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        kidsSession session = request.getSession();
        ApiModule module = ApiModule.getInstance(session.getGlobalHome() + File.separator + "WEB-INF" + File.separator + "communication_module", session.getConfiguration().getDBIni());
        String idStr = request.setting;
        byte id[] = DownloadInfo.idStringToByte(idStr);
        if (id == null) return true;
        DownloadInfo di = null;
        try {
            di = module.getResourceDownloaded(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (di != null) System.out.println("GiewsImportDataset: " + di.toString()); else return true;
        if (di.getResource().getType() == Resources.DATASET) {
            ImportDatasetWizard wiz = ((ImportDatasetWizard) session.getAttribute("IMPORT_DATASET_WIZARD"));
            ImportDatasetFromFile idff = new ImportDatasetFromFile(wiz);
            idff.uploadFromResourceDownloaded(di.getResource().getPath(), "0");
            wiz.setHandler((ImportDatasetHandler) idff);
            session.setAttribute("IMPORT_DATASET_WIZARD", wiz);
        }
        consumed = true;
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }
}
