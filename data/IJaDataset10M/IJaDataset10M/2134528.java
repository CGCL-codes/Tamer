package org.fao.waicent.kids.editor.service;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.fao.waicent.kids.editor.ChartEditor;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ChangeChartColors extends kidsService {

    public ChangeChartColors() {
    }

    public ChangeChartColors(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        kidsSession session = request.getSession();
        ChartEditor c_ed = ((ChartEditor) session.getAttribute("CHART_EDITOR"));
        HttpServletRequest req = session.getHttpServletRequest();
        c_ed.update(req);
        String setting = request.setting;
        int style = 0;
        int comma_index = setting.indexOf(",");
        if (comma_index == -1) {
            session.setLegendColorTheme(Integer.parseInt(setting));
        } else {
            session.setLegendColorTheme(Integer.parseInt(setting.substring(0, comma_index).trim()));
        }
        if (setting.indexOf(",") != -1) {
            style = 10;
            String f = setting.substring(setting.indexOf(",") + 1, setting.lastIndexOf(",")).toLowerCase();
            String l = setting.substring(setting.lastIndexOf(",") + 1, setting.length()).toLowerCase();
            c_ed.changeColors(style, f, l);
        } else {
            style = Integer.parseInt(setting);
            c_ed.changeColors(style);
        }
        session.getAttributes().setModified(true);
        consumed = true;
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }
}
