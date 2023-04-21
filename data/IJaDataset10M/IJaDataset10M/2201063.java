package com.loribel.tools.web.gui.action;

import java.util.List;
import com.loribel.commons.exception.GB_LoadException;
import com.loribel.commons.swing.GB_ButtonObjectActionReportImpl;
import com.loribel.commons.swing.tools.GB_ButtonTools;
import com.loribel.commons.util.CTools;
import com.loribel.tools.web.abstraction.GBW_BORepository;
import com.loribel.tools.web.action.report.GBW_ActionHtmlContentSave;
import com.loribel.tools.web.bo.GBW_HtmlContentBO;
import com.loribel.tools.web.tools.GBW_HtmlContentTools;

/**
 * Button.
 *
 * @author Gr�gory Borelli
 */
public class GBW_ButtonHtmlContentSave extends GB_ButtonObjectActionReportImpl {

    private GBW_ActionHtmlContentSave action;

    private GBW_BORepository repository;

    public GBW_ButtonHtmlContentSave(GBW_BORepository a_repository) {
        super();
        repository = a_repository;
        action = new GBW_ActionHtmlContentSave();
        setObjectActionReport(action);
        GB_ButtonTools.decoreBigIcon(this);
    }

    public List getItemsToTreat() throws GB_LoadException {
        GBW_HtmlContentBO[] l_items = GBW_HtmlContentTools.getAllHtmlContent(repository, true);
        return CTools.toList(l_items);
    }
}
