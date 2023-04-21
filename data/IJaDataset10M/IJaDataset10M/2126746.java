package net.ko.http.views;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import net.ko.framework.Ko;
import net.ko.http.objects.KQueryString;
import net.ko.http.objects.KRequest;
import net.ko.interfaces.KMaskInterface;
import net.ko.kobject.KListObject;
import net.ko.kobject.KMask;
import net.ko.kobject.KObject;
import net.ko.utils.KRegExpr;
import net.ko.utils.KString;

/**
 * Liste tabulaire d'objets (KListObject) gérée avec pagination
 * intégrant des formulaires de mise à jour, insertion et suppression de données
 * @see net.ko.http.views.KPagination
 * @see net.ko.http.views.KNavBarre
 * @see net.ko.http.views.KHttpForm
 * @author jcheron
 *
 */
public class KPageList extends KAbstractView implements KMaskInterface {

    private Class<KObject> clazz;

    private String className;

    private KPagination pagination;

    private KHttpForm form;

    private int mode = -1;

    private KMask mask = null;

    private int countFields;

    private int rowCount = 10;

    private int pageNavCount;

    private String imgFolder;

    private String sql;

    private String header;

    private String footer;

    private boolean isShowCaption;

    private String beforeField;

    private String afterfield;

    private String editBtn = "...";

    private String editFormUrl = "";

    private String listContentUrl = "";

    private String requestUrl = "";

    private String messageAndUpdateUrl = "";

    private KMask innerMask = null;

    private boolean hasEditBtn = false;

    private boolean hasAddBtn = false;

    private boolean isAjax = true;

    private int messageDelay = 500;

    private boolean hasNavBarre;

    private boolean hasPageCounter;

    private boolean hasFiltre;

    private String ajaxDivMessageSubmit = "_ajx";

    private String ajaxDivContentRefresh = "_ajxContent";

    private String idForm = "frm";

    private boolean isFormModal = true;

    private boolean koDetails = false;

    private String selectStyle = "{'color':'white','backgroundColor':'#9D3819','filter': 'alpha(opacity=60)','opacity':'0.6'}";

    private boolean checked = false;

    private String checkedMask = "<td><input {checkedValue} type='checkbox' id='{id}{getUniqueIdHash}' name='{name}' value='{getFirstKeyValue}'></td>";

    private String id = "id";

    private String name = "name";

    private String value = "";

    private boolean createAjaxDivs = false;

    protected String pageContent = "";

    protected String updateMessageMask = null;

    /**
	 * Instancie une nouvelle liste affichant les objets de type clazz issus de la base de données<br/>
	 * L'instruction sql utilisée est dans ce cas celle définie dans la classe clazz
	 * @param clazz classe dérivée de KObject
	 * @param request objet requête Http
	 */
    @SuppressWarnings("rawtypes")
    public KPageList(Class clazz, HttpServletRequest request) {
        this(clazz, request, "", "");
    }

    /**
	 * @param clazz classe dérivée de KObject
	 * @param request objet requête Http
	 * @param sql Instruction sql permettant d'extraire les enregistrements de la base de données
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public KPageList(Class clazz, HttpServletRequest request, String sql) {
        this(clazz, request, sql, "");
    }

    /**
	 * @param clazz classe dérivée de KObject
	 * @param request objet requête Http
	 * @param sql Instruction sql permettant d'extraire les enregistrements de la base de données
	 * @param idForm id HTML du formulaire de modification/ajout d'un objet
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public KPageList(Class clazz, HttpServletRequest request, String sql, String idForm) {
        this(clazz, request, sql, idForm, KRequest.GETPOST("mode", request, 1));
    }

    /**
	 * @param clazz classe dérivée de KObject
	 * @param request objet requête Http
	 * @param sql Instruction sql permettant d'extraire les enregistrements de la base de données
	 * @param idForm id HTML du formulaire de modification/ajout d'un objet
	 * @param mode mode d'ouverture 1 pour liste, 2 pour formulaire, 3 pour validation de formulaire
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public KPageList(Class clazz, HttpServletRequest request, String sql, String idForm, int mode) {
        super(request);
        setSelectStyle(selectStyle);
        this.clazz = clazz;
        this.pagination = new KPagination(request, clazz);
        className = this.clazz.getSimpleName();
        this.mode = mode;
        this.idForm = idForm;
        switch(mode) {
            case 1:
                this.sql = sql;
                this.hasNavBarre = false;
                this.hasPageCounter = false;
                this.rowCount = 10;
                this.pageNavCount = 5;
                this.header = "<table border='0' cellpadding='1' cellspacing='0' width='100%'>\n";
                this.footer = "</table>\n";
                this.beforeField = "<td><div class='td'>";
                this.afterfield = "</div></td>";
                mask = null;
                innerMask = null;
                try {
                    initKoClass();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getMask();
                break;
            case 2:
                createForm();
                try {
                    formLoad();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                createForm();
            default:
                break;
        }
    }

    private String getCheckedString(String id, String name) {
        String result = "";
        if (checked && !"".equals(checkedMask)) result = checkedMask.replace("{id}", id).replace("{name}", name);
        return result;
    }

    /**
	 * @return retourne l'url correspondant au formulaire de modification/insertion d'un objet
	 */
    public String getEditFormUrl() {
        if ("".equals(editFormUrl) || editFormUrl == null) editFormUrl = request.getRequestURI();
        return editFormUrl;
    }

    /**
	 * @return retourne l'url de la page affichant la liste des objets
	 */
    public String getListContentUrl() {
        if ("".equals(listContentUrl) || listContentUrl == null) listContentUrl = request.getRequestURI();
        return listContentUrl;
    }

    /**
	 * @return retourne l'url de la page mettant à jour un objet après validation du formulaire
	 */
    public String getMessageAndUpdateUrl() {
        if ("".equals(messageAndUpdateUrl) || messageAndUpdateUrl == null) messageAndUpdateUrl = getEditFormUrl();
        return messageAndUpdateUrl;
    }

    /**
	 * @return retourne l'url de la page mettant à jour un objet après validation du formulaire
	 */
    public String getRequestUrl() {
        if ("".equals(requestUrl) || requestUrl == null) requestUrl = request.getRequestURI();
        return requestUrl;
    }

    /**
	 * @return retourne l'url de la page mettant à jour un objet après validation du formulaire
	 */
    public String getIdForm() {
        if ("".equals(idForm) || idForm == null) idForm = "frm";
        return idForm;
    }

    /**
	 * Modifie les paramètres de la zone effectuant la mise à jour de l'objet
	 * @param ajaxDivMessageSubmit id HTML de la zone affichant le message de mise à jour
	 * @param messageAndUpdateUrl URL de la page de mise à jour et d'affichage de message
	 */
    public void setMessageAndUpdateParams(String ajaxDivMessageSubmit, String messageAndUpdateUrl) {
        this.ajaxDivMessageSubmit = ajaxDivMessageSubmit;
        this.messageAndUpdateUrl = messageAndUpdateUrl;
    }

    /**
	 * Modifie les paramètres de la zone responsable du rafraichissement de la liste d'objets
	 * @param ajaxDivContentRefresh id HTML de la zone affichant la liste
	 * @param listContentUrl URL de la liste
	 */
    public void setListContentRefreshParams(String ajaxDivContentRefresh, String listContentUrl) {
        this.ajaxDivContentRefresh = ajaxDivContentRefresh;
        this.listContentUrl = listContentUrl;
    }

    /**
	 * Modifie les paramètres du formulaire de modification/ajout d'objet
	 * @param idForm id HTML du formulaire
	 * @param editFormUrl URL du formulaire
	 */
    public void setEditFormParams(String idForm, String editFormUrl) {
        this.idForm = idForm;
        this.editFormUrl = editFormUrl;
    }

    private void createForm() {
        try {
            form = new KHttpForm(((KObject) clazz.newInstance()), request, true, getIdForm());
            form.setResponse(response, false);
            form.setInsertMode(KRequest.GETPOST("insertMode", request, "0").equals("1"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void formLoad() throws SecurityException, IllegalArgumentException, SQLException, NoSuchFieldException, IllegalAccessException, IOException, InstantiationException, ClassNotFoundException {
        if (form != null) {
            form.getDefaultComplete("", null);
        }
    }

    private String formSubmit() throws SecurityException, IllegalArgumentException, SQLException, NoSuchFieldException, IllegalAccessException, IOException, InstantiationException, ClassNotFoundException {
        String result = "";
        if (form != null) {
            if (updateMessageMask != null) form.setUpdateMessageMask(updateMessageMask);
            form.loadAndSubmit();
            result = "<div id='message' class='infoMessage'>&nbsp;" + form.getUpdateMessage() + "</div>";
        }
        return result;
    }

    /**
	 * Spécifie le chemin vers le dossier contenant les images de la navBarre
	 * @see net.ko.http.views.KNavBarre
	 * @param imgFolder dossier
	 */
    public void setImgFolder(String imgFolder) {
        this.imgFolder = imgFolder;
    }

    private String endLine(int nb) {
        String ret = "<td>" + this.getEditBtn() + "</td></tr><tr id='idTR{getUniqueIdHash}' style='display:none'  class='Klist'>";
        ret += "<td id='idTD{getUniqueIdHash}' colspan='" + nb + "'></td>";
        return ret;
    }

    private void initKoClass() throws InstantiationException, IllegalAccessException {
        KObject ko = (KObject) clazz.newInstance();
        ArrayList<String> fields = ko.getFieldNames();
        innerMask = new KMask();
        String aMask = generateMask(fields);
        innerMask.addMask(aMask);
        innerMask.addMask(aMask);
        countFields = fields.size();
    }

    private String generateMask(ArrayList<String> fields) {
        String result = "";
        String details = "";
        if (koDetails) details = "[...]";
        for (String f : fields) {
            result += beforeField + "{" + f + details + "}" + afterfield;
        }
        return result;
    }

    private KMask getMask() {
        String ck = getCheckedString(id, name);
        countFields = KRegExpr.getCount("<td>", "</td>", this.innerMask.get(0));
        String btn = endLine(countFields);
        mask = new KMask();
        mask.addMask("<tr id='line{getUniqueIdHash}'>" + ck + this.innerMask.get(0) + "<td></td></tr>");
        int i = 1;
        if (innerMask.count() > 1) {
            for (String s : innerMask) {
                mask.addMask("<tr id='line{getUniqueIdHash}' class='selection" + i + "'>" + ck + s + btn + "</tr>");
                i++;
            }
        } else mask.addMask("<tr id='line{getUniqueIdHash}' class='odd'>" + ck + innerMask.get(0) + btn + "</tr>");
        if (!isShowCaption && mask.count() > 2) mask.remove(0);
        return this.mask;
    }

    private String getEditBtn() {
        String result = "";
        if (hasEditBtn) {
            if (this.isAjax) result = "<a class='sBtn' href='#' onclick='showfrmEdit" + "(\"{keyValuesToUrl}\",\"" + this.clazz.getName() + "\",\"line{getUniqueIdHash}\")'>Modifier</a>"; else {
                result = "<a class='sBtn' href='" + getEditFormUrl() + "?{keyValuesToUrl}'>" + editBtn + "</a>";
            }
        }
        return result;
    }

    private KFieldControl addAddBtn() {
        return addHTML("_addBtn", "{_addBtn}");
    }

    private String getAddBtn() {
        String result = "";
        if (this.isAjax) result = "<a href='#' class='sBtn' onclick='showfrmAdd" + "(\"0\",\"" + this.clazz.getName() + "\")'>Ajouter...</a>"; else {
            result = "<a class='sBtn' href='" + getEditFormUrl() + "'>Ajouter...</a>";
        }
        return result;
    }

    /**
	 * Détermine si les en-têtes de colonnes seront affichées, permettant de trier les enregistrement suivant la valeur du champ
	 * @param isShowCaption vrai si les en-têtes des colonnes doivent être affichées
	 */
    public void setIsShowCaption(boolean isShowCaption) {
        this.isShowCaption = isShowCaption;
    }

    /**
	 * Retourne la liste complète des objets à afficher
	 * @return la liste des objets affichés
	 */
    public KListObject<KObject> getKListObject() {
        return this.pagination.getKListObject();
    }

    /**
	 * Détermine le contenu HTML du bouton Editer de chaque objet 
	 * @param btn contenu HTML du bouton
	 */
    public void setEditBtn(String btn) {
        this.editBtn = btn;
    }

    /**
	 * Retourne le contenu HTML de la zone pageCounter
	 * affichant le nombre de pages
	 * @return contenu HTML
	 */
    public String getPageCounter() {
        String onkeyup = "onkeyup=\"Forms.Utils.getOnKeyUp(event,13,'" + this.ajaxDivContentRefresh + "','" + this.listContentUrl + "?" + request.getQueryString() + "','_nb='+this.value);\"";
        String ret = "<input type='hidden' name='cls' value='" + clazz.getName() + "'>" + "<input type='hidden' name='ss' value='" + this.pagination.sortedSens + "'>" + "<input type='hidden' name='sf' value='" + this.pagination.sortedField + "'>" + "<input type='hidden' name='page' value='" + this.getActivePage() + "'>" + "<label style='vertical-align: middle' for='nb'>Afficher&nbsp;</label>" + "<input style='width:20px' width='20px' type='text' value='" + this.rowCount + "' id='_nb' name='_nb' " + onkeyup + ">" + "<label style='vertical-align: middle' for='_nb'>&nbsp;enregistrements sur les " + this.pagination.totalRowCount + "</label>" + "<br><b>" + this.pagination.nbPages + " page(s) au total</b>";
        return ret;
    }

    /**
	 * Retourne le numéro de la page active,passé dans l'URL avec le paramètre "page"
	 * @return numéro de la page active
	 */
    public int getActivePage() {
        return KRequest.GETPOST("page", request, 1);
    }

    public String getFiltre() {
        String onkeyup = "onkeyup=\"Forms.Utils.postOnKeyUp(event,13,'" + this.ajaxDivContentRefresh + "','" + this.listContentUrl + "?" + KQueryString.setValue(request.getQueryString(), "page", "1") + "','_filtre='+this.value);\"";
        String ret = "";
        ret += "<fieldset>\n";
        ret += "<legend>Filtrer sur " + className + "</legend>\n";
        ret += "<input type='search' id='_filtre' name='_filtre' value='" + KRequest.POST("_filtre", request, "") + "' " + onkeyup + ">\n";
        ret += "</fieldset>\n";
        return ret;
    }

    /**
	 * Ajoute la zone HTML "filtre" si le membre hasFiltre est vrai
	 * @return control visuel filtre
	 */
    public KFieldControl addFiltre() {
        return this.addHTML("_filtre", "{_filtre}");
    }

    /**
	 * Ajoute la zone barre de navigation entre les pages "navBarre" si le membre hasNavBarre vaut vrai
	 */
    public void addNavBarre() {
        addNavBarre(5, 10, "");
    }

    /**
	 * Ajoute la zone barre de navigation entre les pages "navBarre" si le membre hasNavBarre vaut vrai
	 * @param pageNavCount nombre de pages à afficher dans la barre
	 * @param rowCount 
	 * @param imgFolder dossier contenant les images de la barre de navigation
	 * @return la zone HTML créée
	 */
    public KFieldControl addNavBarre(int pageNavCount, int rowCount, String imgFolder) {
        this.hasNavBarre = true;
        this.pageNavCount = pageNavCount;
        this.rowCount = rowCount;
        return this.addHTML("_navBarre", "\n{_navBarre}\n");
    }

    /**
	 * @return contrôle visuel HTML contenant la zone "pageCounter"
	 */
    public KFieldControl addPageCounter() {
        this.hasPageCounter = true;
        return this.addHTML("_pageCounter", "\n{_pageCounter}\n");
    }

    /**
	 * Charge la page à afficher en tenant compte du numéro de page passé dans l'URL,du filtre, du nombre d'enregistements à afficher
	 */
    public void load() {
        int p = getActivePage();
        if (KRequest.keyExists("_nb", request)) this.rowCount = KRequest.GETPOST("_nb", request, 20);
        String filtre = KRequest.POST("_filtre", request);
        if (filtre != null) try {
            this.sql = KObject.makeSQLFilter_(filtre, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.pagination.setRequestUrl(getListContentUrl());
        this.pagination.setAjaxDivContentRefresh(ajaxDivContentRefresh);
        this.pagination.setAjax(this.isAjax);
        this.pagination.setSql(this.sql);
        this.pagination.setPageNavCount(pageNavCount);
        this.pagination.setRowCount(rowCount);
        this.pagination.setIsShowCaptions(this.isShowCaption);
        this.pagination.setNoLimit(!hasNavBarre);
        this.pagination.mask = getMask();
        this.pagination.setValue(value);
        this.pagination.gotoo(p);
        this.pageContent = this.header + this.pagination + this.footer;
    }

    /**
	 * Charge de façon complète la liste si le paramètre de l'URL mode vaut 1<br/>
	 * Appelle dans l'ordre : addFiltre, addNavBarre, load, addPageCounter, getAddBtn
	 * @see net.ko.http.views.KPageList.#addFiltre()
	 * @see net.ko.http.views.KPageList.#addNavBarre()
	 * @see net.ko.http.views.KPageList.#load()
	 * @see net.ko.http.views.KPageList.#addPageCounter()
	 * @see net.ko.http.views.KPageList.#getAddBtn()
	 */
    public void getDefaultComplete() {
        if (mode == 1) {
            if (hasFiltre) this.addFiltre();
            this.addHTML("_listContent", "{_ajx}{_listContent}");
            if (hasNavBarre) this.addNavBarre();
            this.load();
            if (!fieldControls.containsKey("_page")) this.addHTML("_page", "{_page}");
            if (hasPageCounter) this.addPageCounter();
            this.addHTML("listContentEnd", "{/_listContent}");
            if (hasAddBtn) this.addAddBtn();
        }
    }

    public String fieldControlsToString() {
        return fieldControls.toString();
    }

    /**
	 * Retourne la chaîne HTML affichant en fonction du mode passé automatiquement dans l'URL<br/>
	 * <ul>
	 * <li>si mode vaut 1 : la liste des objets</li>
	 * <li>si mode vaut 2 : le chargmement et l'affichage du formulaire de modification/insertion</li>
	 * <li>si mode vaut 3 : la soumission du formulaire par la méthode post</li>
	 * </ul>
	 */
    public String toString() {
        String result = "";
        switch(mode) {
            case 1:
                setLoaded(true);
                result += fieldControlsToString();
                String navBarre = "";
                String pageCounter = "";
                String addBtn = "";
                String filtreContent = "";
                String _ajxDiv = "";
                String _ajxContentDiv = "";
                String _ajxContentDivEnd = "";
                if (_ajx) {
                    result = KRegExpr.getPart("{_listContent}", "{/_listContent}", result);
                }
                if (createAjaxDivs && !_ajx) {
                    _ajxDiv = "<div id='" + ajaxDivMessageSubmit + "'></div>";
                    _ajxContentDiv = "<div id='" + ajaxDivContentRefresh + "'>";
                    _ajxContentDivEnd = "</div>";
                }
                if (this.hasNavBarre) navBarre = this.pagination.showNavBarre(this.imgFolder);
                if (this.hasPageCounter) pageCounter = this.getPageCounter();
                if (hasAddBtn) addBtn = getAddBtn();
                if (hasFiltre) filtreContent = getFiltre();
                result = result.replace("{_ajx}", _ajxDiv);
                result = result.replace("{_listContent}", _ajxContentDiv);
                result = result.replace("{/_listContent}", _ajxContentDivEnd);
                result = result.replace("{_navBarre}", navBarre);
                result = result.replace("{_pageCounter}", pageCounter);
                result = result.replace("{_page}", pageContent);
                result = result.replace("{_filtre}", filtreContent);
                result = result.replace("{_addBtn}", addBtn);
                String ajax = "";
                if (this.isAjax) {
                    try {
                        ajax += KJavaScript.kGetAjxForm_("frmEdit", this.clazz, "Modification", this.getEditFormUrl(), this.getListContentUrl(), this.getMessageAndUpdateUrl(), this.getIdForm(), this.messageDelay, request.getQueryString(), ajaxDivMessageSubmit, ajaxDivContentRefresh, isFormModal, this.selectStyle);
                        ajax += KJavaScript.kGetAjxForm_("frmAdd", this.clazz, "Ajout", this.getEditFormUrl(), this.getListContentUrl(), this.getMessageAndUpdateUrl(), this.getIdForm(), this.messageDelay, request.getQueryString(), ajaxDivMessageSubmit, ajaxDivContentRefresh, isFormModal, this.selectStyle);
                    } catch (Exception e) {
                    }
                }
                if (_ajx) {
                    result = result + ajax;
                } else {
                    result = getPageHeader() + result + ajax + getPageFooter();
                }
                break;
            case 2:
                result = form.toString();
                break;
            case 3:
                try {
                    result = formSubmit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return result;
    }

    public String getBeforeField() {
        return beforeField;
    }

    /**
	 * Détermine les éléments HTML à insérer avant chaque membre de chaque objet à afficher dans la liste
	 * @param beforeField éléments HTML
	 */
    public void setBeforeField(String beforeField) {
        if (this.beforeField != beforeField) {
            this.beforeField = beforeField;
            try {
                initKoClass();
            } catch (Exception e) {
            }
        }
    }

    public String getAfterfield() {
        return afterfield;
    }

    /**
	 * Détermine les éléments HTML à insérer après chaque membre de chaque objet à afficher dans la liste
	 * @param afterfield élément HTML
	 */
    public void setAfterfield(String afterfield) {
        if (this.afterfield != afterfield) {
            this.afterfield = afterfield;
            try {
                initKoClass();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void swapFields(String fieldName1, String fieldName2) {
        if (innerMask != null) innerMask.swapFields(fieldName1, fieldName2);
    }

    @Override
    public boolean removeField(String fieldName) {
        boolean result = false;
        if (innerMask != null) result = innerMask.removeField(fieldName);
        return result;
    }

    @Override
    public void replaceField(String oldFieldName, String newFieldName) {
        if (innerMask != null) innerMask.replaceField(oldFieldName, newFieldName);
    }

    @Override
    public void replaceField(String oldFieldName, String newFieldName, String newCaption) {
        replaceField(oldFieldName, newFieldName);
        setFieldCaption(newFieldName, newCaption);
    }

    @Override
    public void removeFields(String[] fieldNames) {
        for (String f : fieldNames) removeField(f);
    }

    public void removeFieldsByIndex(String indexes) {
        if (innerMask != null) innerMask.remove(KString.toArrayOfInt(indexes, Ko.krequestValueSep()));
    }

    public void keepFieldsByIndex(String indexes) {
        if (innerMask != null) innerMask.keep(indexes);
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPageNavCount() {
        return pageNavCount;
    }

    public void setPageNavCount(int pageNavCount) {
        this.pageNavCount = pageNavCount;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public boolean isShowCaption() {
        return isShowCaption;
    }

    public void setShowCaption(boolean isShowCaption) {
        this.isShowCaption = isShowCaption;
    }

    public KMask getInnerMask() {
        return innerMask;
    }

    public void setInnerMask(KMask innerMask) {
        this.innerMask = innerMask;
    }

    public boolean isHasEditBtn() {
        return hasEditBtn;
    }

    /**
	 * @param hasEditBtn
	 */
    public void setHasEditBtn(boolean hasEditBtn) {
        this.hasEditBtn = hasEditBtn;
    }

    public boolean isAjax() {
        return isAjax;
    }

    /**
	 * @param isAjax
	 */
    public void setAjax(boolean isAjax) {
        this.isAjax = isAjax;
    }

    public int getMessageDelay() {
        return messageDelay;
    }

    /**
	 * @param messageDelay
	 */
    public void setMessageDelay(int messageDelay) {
        this.messageDelay = messageDelay;
    }

    public boolean isHasNavBarre() {
        return hasNavBarre;
    }

    /**
	 * @param hasNavBarre
	 */
    public void setHasNavBarre(boolean hasNavBarre) {
        this.hasNavBarre = hasNavBarre;
    }

    public boolean isHasPageCounter() {
        return hasPageCounter;
    }

    /**
	 * @param hasPageCounter
	 */
    public void setHasPageCounter(boolean hasPageCounter) {
        this.hasPageCounter = hasPageCounter;
    }

    public String getImgFolder() {
        return imgFolder;
    }

    /**
	 * @param editFormUrl URL de la page affichant le formulaire de Modification/Ajout d'un objet
	 */
    public void setEditFormUrl(String editFormUrl) {
        this.editFormUrl = editFormUrl;
    }

    /**
	 * Retourne la zone HTML (identifiée par son ID) qui affichera le résultat de la mise à jour de l'objet
	 * après validation d'un formulaire d'insertion ou de modification
	 * @return id de la zone d'affichage
	 */
    public String getAjaxDivMessageSubmit() {
        return ajaxDivMessageSubmit;
    }

    /**
	 * Définit la zone HTML (identifiée par son ID) qui affichera le résultat de la mise à jour de l'objet
	 * après validation d'un formulaire d'insertion ou de modification
	 * @param ajaxDivMessageSubmit
	 */
    public void setAjaxDivMessageSubmit(String ajaxDivMessageSubmit) {
        this.ajaxDivMessageSubmit = ajaxDivMessageSubmit;
    }

    public String getAjaxDivContentRefresh() {
        return ajaxDivContentRefresh;
    }

    /**
	 * Détermine la zone HTML (identifiée par son ID) qui affichera avec ajax la liste mise à jour après validation du formulaire<br/>
	 * la liste complète doit appartenir à cette zone
	 * @param ajaxDivContentRefresh
	 */
    public void setAjaxDivContentRefresh(String ajaxDivContentRefresh) {
        this.ajaxDivContentRefresh = ajaxDivContentRefresh;
    }

    /**
	 * Supprime le contrôle visuel nommé fieldName du formulaire de mise à jour ou d'insertion de l'objet
	 * @param fieldName
	 */
    public void formRemoveFieldControl(String fieldName) {
        if (form != null) {
            form.removeFieldControl(fieldName);
        }
    }

    /**
	 * Echange la position de deux contrôles visuels du formulaire de mise à jour ou d'insertion de l'objet
	 * @param fieldName1 nom du premier contrôle
	 * @param fieldName2 nom du second contrôle
	 */
    public void formSwapFcPos(String fieldName1, String fieldName2) {
        if (form != null) {
            form.swapFcPos(fieldName1, fieldName2);
        }
    }

    /**
	 * Retourne le contrôle visuel nommé fieldName du formulaire de mise à jour ou d'insertion de l'objet
	 * @param fieldName nom du contrôle
	 * @return le contrôle visuel si il existe, null dans le cas contraire
	 */
    public KFieldControl formGetFieldControl(String fieldName) {
        KFieldControl result = null;
        if (form != null) result = form.getFieldControl(fieldName);
        return result;
    }

    /**
	 * Retourne l'étiquette (en-tête) d'une colonne de la liste
	 * @param fieldName nom du champ/membre/méthode
	 * @return l'étiquette d'une colonne de la liste
	 */
    public String getFieldCaption(String fieldName) {
        return pagination.getFieldCaption(fieldName);
    }

    /**
	 * Modifie l'étiquette (en-tête) d'une colonne de la liste
	 * @param fieldName nom du champ/membre/méthode
	 * @param caption
	 */
    public void setFieldCaption(String fieldName, String caption) {
        pagination.setFieldCaption(fieldName, caption);
    }

    /**
	 * Retourne le formulaire associé à la liste ou null s'il n'existe pas
	 * @return formulaire de modification/insertion
	 */
    public KHttpForm getForm() {
        return form;
    }

    /**
	 * Retourne vrai si la form est modale<br/>
	 * Une forme modale est affichée au dessus de la page et empêche l'utilisateur de cliquer en dessous
	 * @return vrai si la form est modale
	 */
    public boolean isFormModal() {
        return isFormModal;
    }

    /**
	 * Détermine si la fenêtre doit être modale
	 * @param isFormModal vrai si la form doit être modale
	 */
    public void setFormModal(boolean isFormModal) {
        this.isFormModal = isFormModal;
    }

    /**
	 * @return vrai si la liste possède un bouton Ajouter
	 */
    public boolean isHasAddBtn() {
        return hasAddBtn;
    }

    /**
	 * Détermine si la page comporte un bouton "Ajouter"
	 * @param hasAddBtn vrai si la page affiche un bouton "Ajouter"
	 */
    public void setHasAddBtn(boolean hasAddBtn) {
        this.hasAddBtn = hasAddBtn;
    }

    /**
	 * Détermine si chaque objet/enregistrement doit comporter un bouton "Modifier" et si la page contiendra un bouton "Ajouter"
	 * @param isEditable vrai si chaque objet/enregistrement affiche le bouton "Modifier" et si la page contient un bouton "Ajouter"
	 */
    public void setEditable(boolean isEditable) {
        hasAddBtn = isEditable;
        hasEditBtn = isEditable;
        setKoDetails(isEditable);
    }

    @Override
    public void addField(String beforeField, String fieldName, String afterField) {
        if (innerMask != null) innerMask.addField(beforeField, fieldName, afterField);
    }

    /**
	 * Ajoute un champ à afficher dans une colonne de la liste
	 * @param fieldName champ à ajouter
	 */
    public void addField(String fieldName) {
        addField(beforeField, fieldName, afterfield);
    }

    /**
	 * @param listContentUrl URL de la page affichant la liste des objets
	 */
    public void setListContentUrl(String listContentUrl) {
        this.listContentUrl = listContentUrl;
    }

    /**
	 * @param messageAndUpdateUrl URL de la page mettant à jour un objet après validation du formulaire
	 */
    public void setMessageAndUpdateUrl(String messageAndUpdateUrl) {
        this.messageAndUpdateUrl = messageAndUpdateUrl;
    }

    /**
	 * @param idForm id HTML du formulaire permettant la modification/ajout d'un objet
	 */
    public void setIdForm(String idForm) {
        this.idForm = idForm;
    }

    /**
	 * @return vrai si les détails sont activés
	 */
    public boolean isKoDetails() {
        return koDetails;
    }

    /**
	 * Détermine si le bouton Détails est accessible sur chaque objet de la liste
	 * @param koDetails vrai si les détails sont activés
	 */
    public void setKoDetails(boolean koDetails) {
        if (innerMask != null) {
            this.koDetails = koDetails;
            innerMask.setKoDetails(koDetails);
        }
    }

    public Class<KObject> getClazz() {
        return clazz;
    }

    public void setClazz(Class<KObject> clazz) {
        this.clazz = clazz;
    }

    /**
	 * @param clazz classe à comparer avec la classe affichée dans la liste
	 * @return vrai si clazz est égal à la classe affichée dans la liste
	 */
    public boolean koClassEquals(Class<KObject> clazz) {
        return this.clazz.equals(clazz);
    }

    public String getSelectStyle() {
        return selectStyle;
    }

    public void setSelectStyle(String selectStyle) {
        this.selectStyle = selectStyle.replace("'", "\"");
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getCheckedMask() {
        return checkedMask;
    }

    public void setCheckedMask(String checkedMask) {
        this.checkedMask = checkedMask;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue() {
        KListObject<KObject> kl = getKListObject();
        if (kl != null) value = kl.getKeyValuesForHasAndBelongsToMany(";");
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        if (this.pagination != null) this.pagination.setRequestUrl(requestUrl);
    }

    public void setEditable() {
        setEditable(!hasEditBtn);
    }

    public boolean isCreateAjaxDivs() {
        return createAjaxDivs;
    }

    public void setCreateAjaxDivs(boolean createAjaxDivs) {
        this.createAjaxDivs = createAjaxDivs;
    }

    public boolean isHasFiltre() {
        return hasFiltre;
    }

    public void setHasFiltre(boolean hasFiltre) {
        this.hasFiltre = hasFiltre;
    }

    public String getUpdateMessageMask() {
        return updateMessageMask;
    }

    public void setUpdateMessageMask(String updateMessageMask) {
        this.updateMessageMask = updateMessageMask;
    }
}
