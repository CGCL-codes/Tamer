package com.loribel.tools.web.action;

import java.io.File;
import javax.swing.Icon;
import com.loribel.commons.abstraction.GB_ActionReport;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_LabelIconOwner;
import com.loribel.commons.abstraction.GB_ObjectActionReport2;
import com.loribel.commons.util.GB_ActionReportTools;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.STools;
import com.loribel.tools.web.abstraction.GBW_BORepository;
import com.loribel.tools.web.bo.GBW_HtmlLinkBO;
import com.loribel.tools.web.gui.vm.AA;
import com.loribel.tools.web.tools.GBW_HtmlLinkFileTools;

/**
 * Initialise le champ epure � ${nameEpure} si le fichier est trouv� dans le r�pertoire correspondand.
 * 
 * Action sur {@link GBW_HtmlLinkBO}.
 *  
 * @author Gregory Borelli
 */
public class GBW_ActionCheckEpureExist implements GB_ObjectActionReport2, GB_LabelIconOwner {

    static final String HTML_EPURE_MANUAL = "html-epure-manual";

    static final String HTML_EPURE_IGNORE = "html-epure-ignore";

    private GBW_BORepository repository;

    private File dirEpure;

    private String nameEpure;

    private boolean overwrite;

    public GBW_ActionCheckEpureExist(GBW_BORepository a_repository, String a_nameEpure, boolean a_overwrite) {
        super();
        repository = a_repository;
        nameEpure = a_nameEpure;
        overwrite = a_overwrite;
        dirEpure = new File(repository.getRepoDirs().getDirBaseWeb(), nameEpure);
    }

    /**
     * Si le fichier n'est pas trouv� => newReportNothingToDo("Non trouv�...");
     * Si le fichier trouv� => utilise la m�thode isAccept(a_link, l_file);
     */
    protected GB_ActionReport doActionOnLink(GBW_HtmlLinkBO a_link) throws Exception {
        GB_ActionReport retour = null;
        boolean l_mustSave = false;
        String l_epure = a_link.getEpureHtml();
        if (nameEpure.equals(l_epure)) {
            retour = GB_ActionReportTools.newReportSuccess("Reset epure");
            a_link.setEpureHtml(null);
            l_mustSave = true;
        }
        if (STools.isNotNull(l_epure)) {
            if (!overwrite) {
                return GB_ActionReportTools.newReportNothingToDo("Existe d�j�: " + l_epure);
            }
        }
        File l_file = getFile(a_link);
        if (l_file == null) {
            return GB_ActionReportTools.newReportNothingToDo("Non trouv� dans " + nameEpure);
        }
        if (l_file.exists()) {
            retour = isAccept(a_link, l_file);
            if (retour.getState() == GB_ActionReportTools.STATE_MAP_ACTION.STATE_SUCCESS) {
                a_link.setEpureHtml(nameEpure);
                l_mustSave = true;
            }
        } else {
            return GB_ActionReportTools.newReportNothingToDo("Non trouv�: " + l_file.getAbsolutePath());
        }
        if (l_mustSave) {
            a_link.save(0, true);
        }
        return retour;
    }

    public GB_ActionReport doActionRpt(Object a_item) throws Exception {
        GBW_HtmlLinkBO l_link = (GBW_HtmlLinkBO) a_item;
        return doActionOnLink(l_link);
    }

    public void doAfter() throws Exception {
    }

    public boolean doBefore() throws Exception {
        dirEpure.mkdirs();
        return true;
    }

    /**
     * Retourne le fichier associ� a a_link.
     * Cette methode peut etre surcharger pour utiliser des fichiers custom.
     */
    protected File getFile(GBW_HtmlLinkBO a_link) {
        return GBW_HtmlLinkFileTools.getFile(a_link, dirEpure, "html", true);
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = "Mise � jour de epure avec [" + nameEpure + "]...";
        Icon l_icon = GB_IconTools.get(AA.ICON.X32_FILE_ACTION);
        String l_desc = "Met � jour le champ epure. ";
        if (overwrite) {
            l_desc += "Force le remplacement.";
        } else {
            l_desc += "Ne remplace pas une valeur existante.";
        }
        return GB_LabelIconTools.newLabelIcon(l_label, l_icon, l_desc);
    }

    public GBW_BORepository getRepository() {
        return repository;
    }

    /**
     * Returns the report for a_link and file.
     * Permet de tester le contenu du fichier et accepter ou refuser son utilisation.
     */
    protected GB_ActionReport isAccept(GBW_HtmlLinkBO a_link, File a_file) throws Exception {
        return GB_ActionReportTools.newReportSuccess("Epure = " + a_link.getEpureHtml());
    }
}
