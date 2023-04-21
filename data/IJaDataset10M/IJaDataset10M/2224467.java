package com.loribel.tools.web.action.htmllink;

import com.loribel.tools.web.abstraction.GBW_BORepository;
import com.loribel.tools.web.abstraction.GBW_FilesUserMgr;
import com.loribel.tools.web.bo.GBW_HtmlLinkBO;

/**
 * Action pour ajouter un type MENU aux objets de type {@link GBW_HtmlLinkBO}.
 * 
 * Traite une liste d'URL (dans un fichier).
 *  
 * @author Gregory Borelli
 */
public class GBW_ActionHtmlLinkTypeMenu extends GBW_ActionHtmlLinkTypeWithFile {

    public GBW_ActionHtmlLinkTypeMenu(GBW_BORepository a_repository) {
        super(a_repository, GBW_HtmlLinkBO.TYPE.MENU);
        setUserFileName(GBW_FilesUserMgr.NAME.URL_MENU);
        setUseAllLngs(true);
    }
}
