package com.loribel.tools.web.gui.vm;

import javax.swing.Icon;
import javax.swing.JComponent;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_LongActionBuild;
import com.loribel.commons.abstraction.GB_ObjectActionReport;
import com.loribel.commons.exception.GB_LoadException;
import com.loribel.commons.gui.GB_ViewManagerAbstract;
import com.loribel.commons.swing.GB_PanelRows;
import com.loribel.commons.swing.GB_PanelRowsTitle;
import com.loribel.commons.swing.GB_PanelRowsTools;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.tools.web.abstraction.GBW_BORepository;
import com.loribel.tools.web.action.htmllink.GBW_ActionHtmlLinkCorrectAuto;
import com.loribel.tools.web.gui.action.GBW_ButtonActionHtmlLink;

/**
 * VM for actions on GBW_RepositoryActions.
 * 
 * @author Gregory Borelli
 */
public class GBW_HtmlLinkListToolsVM extends GB_ViewManagerAbstract {

    /**
     * Inner class.
     */
    private class MyView extends GB_PanelRowsTitle implements GB_LongActionBuild {

        MyView() throws GB_LoadException {
            super();
            GB_PanelRowsTools.setStyleView(this);
            setTitle(getLabelIcon(), useTitle);
            init();
        }

        private void addPanelTypeAuto() {
            GB_ObjectActionReport l_action;
            GB_PanelRows l_panel = new GB_PanelRows();
            l_action = new GBW_ActionHtmlLinkCorrectAuto(repository);
            l_panel.addRowFill(new GBW_ButtonActionHtmlLink(repository, l_action));
            this.addWithTitle(l_panel, "Actions");
        }

        private void init() throws GB_LoadException {
            this.addPanelTypeAuto();
            this.addRowEnd();
        }
    }

    private boolean useTitle;

    private GBW_BORepository repository;

    public GBW_HtmlLinkListToolsVM(GBW_BORepository a_repository, boolean a_useTitle) {
        repository = a_repository;
        useTitle = a_useTitle;
    }

    protected JComponent buildView() throws GB_LoadException {
        return new MyView();
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = "Actions";
        Icon l_icon = GB_IconTools.get(AA.ICON.X16_PROCESS);
        String l_desc = null;
        return GB_LabelIconTools.newLabelIcon(l_label, l_icon, l_desc);
    }
}
