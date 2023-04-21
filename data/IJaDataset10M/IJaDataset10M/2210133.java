package org.fao.waicent.kids.giews.charts.service;

import java.io.IOException;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SetEditorChartPanel extends kidsService {

    public SetEditorChartPanel() {
    }

    public SetEditorChartPanel(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        kidsSession session = request.getSession();
        String setting = request.setting;
        int comma_index = setting.indexOf(",");
        if (comma_index != -1) {
            int panel_idx = Integer.parseInt(setting.substring(0, comma_index).trim());
            session.setChartEditorFigureIndex(panel_idx);
            int tab_idx = Integer.parseInt(setting.substring(comma_index + 1).trim());
            session.setChartEditorTabIndex(tab_idx);
            int panel = session.getChartEditorFigureIndex();
            int tab = session.getChartEditorTabIndex();
            System.out.println("----panel = " + (panel));
            System.out.println("----tab = " + (tab));
        }
        consumed = true;
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }
}
