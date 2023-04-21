package com.loribel.tools.web.action;

import java.io.IOException;
import java.io.Writer;
import com.loribel.commons.util.STools;
import com.loribel.tools.web.bo.GBW_HtmlLinkBO;

/**
 * Action pour exporter les titres des GBW_HtmlLinkBO.
 *  
 * @author Gregory Borelli
 */
public class GBW_ActionExportTitles extends GBW_HtmlLinkWriterAction {

    public GBW_ActionExportTitles() {
        super();
    }

    public GBW_ActionExportTitles(Writer a_writer) {
        super(a_writer);
    }

    public void doAction(Object a_item) throws IOException {
        GBW_HtmlLinkBO l_item = (GBW_HtmlLinkBO) a_item;
        String l_line = l_item.getUrl() + "\t";
        l_line += STools.toNotNull(l_item.getTitle()) + "\t";
        l_line += STools.toNotNull(l_item.getTitleAuto()) + "\t";
        l_line += STools.toNotNull(l_item.getTitleH1()) + "\t";
        l_line += STools.toNotNull(l_item.getTitleHtml()) + "\t";
        l_line += STools.toNotNull(l_item.getTitleLink()) + "\t";
        getWriter().write(l_line + AA.SL);
    }

    public void doAfter() {
    }

    public void doBefore() throws IOException {
        writeHeader();
    }

    private void writeHeader() throws IOException {
        String l_line = "URL\t";
        l_line += "Title\t";
        l_line += "Title Auto\t";
        l_line += "Title H1\t";
        l_line += "Title Html\t";
        l_line += "Title Link\t";
        getWriter().write(l_line + AA.SL);
    }
}
