package org.fao.waicent.kids.editor.service;

import java.io.IOException;
import org.fao.waicent.kids.editor.ChartEditor;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ApplyChartEdits extends kidsService {

    public ApplyChartEdits() {
    }

    public ApplyChartEdits(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        kidsSession session = request.getSession();
        ChartEditor c_ed = ((ChartEditor) session.getAttribute("CHART_EDITOR"));
        c_ed.update(session.getHttpServletRequest());
        session.getContext().getAttributes().getGraphExtent().setDefinition(c_ed.getChart(), session.getDefinitionIndex());
        c_ed.apply();
        session.change("LOAD_CHART", Integer.toString(session.getContext().getAttributes().getGraphExtent().getDefinitionSize() - 1));
        session.getAttributes().setModified(true);
        consumed = true;
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }
}
