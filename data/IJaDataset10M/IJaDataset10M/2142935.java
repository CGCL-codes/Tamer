package org.dbwiki.web.ui.printer;

import org.dbwiki.exception.WikiException;
import org.dbwiki.web.html.HtmlLinePrinter;
import org.dbwiki.web.request.WikiRequest;
import org.dbwiki.web.request.parameter.RequestParameter;
import org.dbwiki.web.request.parameter.RequestParameterAction;
import org.dbwiki.web.server.DatabaseWiki;
import org.dbwiki.web.server.WikiServerConstants;
import org.dbwiki.web.ui.CSS;

/** Prints out a form that will generate file edit requests
 * 
 * @author jcheney
 *
 */
public class FileEditor implements HtmlContentPrinter {

    private WikiRequest<?> _request;

    private String _title;

    public FileEditor(WikiRequest<?> request, String title) {
        _request = request;
        _title = title;
    }

    public void print(HtmlLinePrinter printer) throws WikiException {
        int fileType = -1;
        if (_request.parameters().hasParameter(RequestParameter.ParameterTemplate)) {
            fileType = WikiServerConstants.RelConfigFileColFileTypeValTemplate;
        } else if (_request.parameters().hasParameter(RequestParameter.ParameterStyleSheet)) {
            fileType = WikiServerConstants.RelConfigFileColFileTypeValCSS;
        } else if (_request.parameters().hasParameter(RequestParameter.ParameterURLDecoding)) {
            fileType = WikiServerConstants.RelConfigFileColFileTypeValURLDecoding;
        }
        printer.paragraph(_title, CSS.CSSHeadline);
        printer.openFORM("frmEditor", "POST", _request.parameters().get(RequestParameter.ParameterResource).value());
        printer.addHIDDEN(DatabaseWiki.ParameterDatabaseID, Integer.toString(_request.wiki().id()));
        printer.addHIDDEN(DatabaseWiki.ParameterFileType, Integer.toString(fileType));
        printer.openTABLE(CSS.CSSFormContainer);
        printer.openTR();
        printer.openTD(CSS.CSSFormContainer);
        printer.addTEXTAREA(DatabaseWiki.ParameterFileContent, "110", "30", true, _request.wiki().getContent(fileType));
        printer.openPARAGRAPH(CSS.CSSButtonLine);
        printer.openCENTER();
        printer.addREALBUTTON("submit", "action", RequestParameterAction.ActionUpdate, "<img src=\"/pictures/button_save.gif\">");
        printer.text("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        printer.addREALBUTTON("submit", "action", RequestParameterAction.ActionCancel, "<img src=\"/pictures/button_cancel.gif\">");
        printer.closeCENTER();
        printer.closePARAGRAPH();
        printer.closeFORM();
        printer.closeTD();
        printer.closeTR();
        printer.closeTABLE();
    }
}
