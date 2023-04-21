package com.loribel.tools.web.action.report;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.Icon;
import com.loribel.commons.abstraction.ENCODING;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_LabelIconOwner;
import com.loribel.commons.abstraction.GB_LongAction;
import com.loribel.commons.net.GB_UrlTools;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.FTools;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.Info;
import com.loribel.commons.util.collection.GB_StringListFile;
import com.loribel.commons.util.comparator.GB_ComparatorTools;
import com.loribel.tools.web.abstraction.GBW_BORepository;
import com.loribel.tools.web.abstraction.GBW_FilesReportMgr;
import com.loribel.tools.web.abstraction.GBW_FilesReportMgr.NAME;

/**
 * Action pour valider les rapports g�n�r�s.
 * 
 * Fait un trie sur les fichiers suivants :
 * - GBW_FilesReportMgr.NAME.LIENS_EXTERNES
 * - GBW_FilesReportMgr.NAME.RESOURCE
 * - GBW_FilesReportMgr.NAME.RESOURCE2
 * - GBW_FilesReportMgr.NAME.PDF
 * - GBW_FilesReportMgr.NAME.IMAGE
 * - GBW_FilesReportMgr.NAME.URL_OK
 * - GBW_FilesReportMgr.NAME.URL_XHTML
 *   
 * @author Gregory Borelli
 */
public class GBW_ActionCheckReportFiles implements GB_LongAction, GB_LabelIconOwner {

    private GBW_BORepository repository;

    private GBW_FilesReportMgr reportMgr;

    public GBW_ActionCheckReportFiles(GBW_BORepository a_repository) {
        super();
        repository = a_repository;
        reportMgr = repository.getFilesReportsMgr();
    }

    private void addUrlsPhysique(GB_StringListFile a_list, String a_name) throws IOException {
        List l_urls = reportMgr.getList(a_name);
        int len = CTools.getSize(l_urls);
        for (int i = 0; i < len; i++) {
            String l_url = (String) l_urls.get(i);
            l_url = GB_UrlTools.removeParameters(l_url);
            a_list.add(l_url);
        }
    }

    /**
     * G�n�re le fichier NAME.URL_PHYSIQUE
     */
    protected void createFileUrlPhysique() throws Throwable {
        GB_StringListFile l_listFile = reportMgr.createList(NAME.URL_PHYSIQUE, false);
        addUrlsPhysique(l_listFile, NAME.URL_OK);
        addUrlsPhysique(l_listFile, NAME.DOC_SRV_COMPLEXE);
        addUrlsPhysique(l_listFile, NAME.DOC_SRV_TO_TREAT);
        addUrlsPhysique(l_listFile, NAME.DOC_SRV_IGNORE);
    }

    public Object doAction() throws Throwable {
        createFileUrlPhysique();
        orderFile(GBW_FilesReportMgr.NAME.LIENS_EXTERNES);
        orderFile(GBW_FilesReportMgr.NAME.RESOURCE);
        orderFile(GBW_FilesReportMgr.NAME.RESOURCE_CSS);
        orderFile(GBW_FilesReportMgr.NAME.RESOURCE_JS);
        orderFile(GBW_FilesReportMgr.NAME.RESOURCE2);
        orderFile(GBW_FilesReportMgr.NAME.PDF);
        orderFile(GBW_FilesReportMgr.NAME.IMAGE);
        orderFile(GBW_FilesReportMgr.NAME.URL_OK);
        orderFile(GBW_FilesReportMgr.NAME.URL_STATIQUE);
        orderFile(GBW_FilesReportMgr.NAME.URL_SERVICE);
        orderFile(GBW_FilesReportMgr.NAME.URL_DYNAMIQUE);
        orderFile(GBW_FilesReportMgr.NAME.URL_PHYSIQUE);
        orderFile(GBW_FilesReportMgr.NAME.URL_XHTML);
        orderFile(GBW_FilesReportMgr.NAME.URL_DIR_APACHE);
        orderFile(GBW_FilesReportMgr.NAME.ERROR);
        orderFile(GBW_FilesReportMgr.NAME.ERROR_401);
        orderFile(GBW_FilesReportMgr.NAME.ERROR_404);
        orderFile(GBW_FilesReportMgr.NAME.ERROR_500);
        orderFile(GBW_FilesReportMgr.NAME.JS);
        orderFile(GBW_FilesReportMgr.NAME.JS_REDIRECTION_AUTO);
        orderFile(GBW_FilesReportMgr.NAME.JS_REDIRECTION_MANU);
        orderFile(GBW_FilesReportMgr.NAME.JS_TODO);
        return Boolean.TRUE;
    }

    public void doGuiAfter(Component a_parent, Object a_value) throws Throwable {
    }

    public boolean doGuiBefore(Component a_parent) throws Throwable {
        return true;
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = "Modifie les fichiers rapport pour qu'ils soient plus pertinents...";
        Icon l_icon = GB_IconTools.get(AA.ICON.X32_FILE_ACTION);
        String l_desc = "Ordonne les URL dans certains fichiers, ...";
        return GB_LabelIconTools.newLabelIcon(l_label, l_icon, l_desc);
    }

    private void orderFile(String a_name) throws IOException {
        File l_file = repository.getFilesReportsMgr().getFile(a_name, true);
        if (l_file == null) {
            return;
        }
        Info.setInfo("Traitement du fichier " + l_file.getName());
        String[] l_lines = FTools.readLines(l_file, ENCODING.ISO_8859_1);
        GB_ComparatorTools.sortStrings(l_lines, true, true);
        FTools.writeLines(l_file, l_lines, ENCODING.ISO_8859_1);
        Info.restoreInfo();
    }
}
