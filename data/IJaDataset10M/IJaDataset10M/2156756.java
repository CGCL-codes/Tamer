package org.fao.waicent.kids.server.service;

import java.io.IOException;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScatterIndicatorY extends kidsService {

    /********************************************************************/
    public ScatterIndicatorY() {
    }

    public ScatterIndicatorY(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    /********************************************************************/
    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        kidsSession session = request.getSession();
        session.setAttribute("SCATTER_INDICATOR_Y", new Integer(request.setting));
        consumed = true;
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }
}
