package net.ko.http.views;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.ko.kobject.KListObject;
import net.ko.kobject.KObject;
import net.ko.types.HtmlControlType;
import net.ko.utils.KString;
import net.ko.utils.KStrings;

/**
 * Contrôle visuel HTML permettant d'afficher ou de modifier la valeur d'un champ/membre
 * @author jcheron
 *
 */
public class KHtmlFieldControl extends KFieldControl {

    private String value;

    private Object listObject;

    private String options;

    private String before = "";

    private String after = "";

    private String listMask = "";

    private String valuesSep = ";";

    private KHttpForm owner = null;

    public KHtmlFieldControl() {
        fieldType = HtmlControlType.khcNone;
        caption = "";
        value = "";
        name = "";
        id = "";
        listObject = null;
        options = "";
    }

    /**
	 * Constructeur d'un contrôle visuel HTML
	 * @param caption texte du label associé au contrôle
	 * @param value valeur à afficher
	 * @param name nom du contrôle name HTML
	 * @param id identifiant HTML
	 * @param fieldType type de contrôle visuel
	 * @param listObject liste associée (KListObject ou Array)
	 * @param options chaîne libre permettant d'insérer des éléments HTML non pris en charge 
	 * @param required détermine si la saisie est obligatoire
	 */
    public KHtmlFieldControl(String caption, String value, String name, String id, HtmlControlType fieldType, Object listObject, String options, boolean required) {
        this.caption = caption;
        this.value = value;
        this.name = name;
        if (id == null || id.equals("")) this.id = name; else this.id = id;
        this.fieldType = fieldType;
        this.listObject = listObject;
        this.options = options;
        this._required = required;
    }

    /**
	 * Constructeur d'un contrôle visuel HTML
	 * @param caption texte du label associé au contrôle
	 * @param value valeur à afficher
	 * @param name nom du contrôle name HTML
	 * @param id identifiant HTML
	 * @param fieldType type de contrôle visuel
	 * @param listObject liste associée (KListObject ou Array)
	 * @param options chaîne libre permettant d'insérer des éléments HTML non pris en charge 
	 */
    public KHtmlFieldControl(String caption, String value, String name, String id, HtmlControlType fieldType, Object listObject, String options) {
        this(caption, value, name, id, fieldType, listObject, options, false);
    }

    /**
	 * analyse le contenu du contrôle et retourne la chaîne associée
	 */
    public String toString() {
        String ret = "<div class='form-row'>";
        String type = this.fieldType.toString();
        String innerHtml = mapListToString();
        String ajaxDiv = "";
        String ajaxLoader = "";
        if (fieldType.isAjax()) {
            if (listObject instanceof KListObject) ajaxDiv = "<input id='ckList-ajax-clsList-" + this.name + "' type='hidden' value='" + ((KListObject<KObject>) listObject).getClassName() + "'>";
            if (owner != null) ajaxDiv += "<input id='ckList-ajax-cls-" + this.name + "' type='hidden' value='" + owner.getKobject().getClass().getName() + "'>";
            ajaxDiv += "<div id='ckList-ajax-" + this.name + "' class='ckListAjax'></div>\n";
            ajaxLoader = "<div id='ckList-ajax-loader-" + this.name + "' class='ckListAjaxLoader' style='display:none'></div>";
        }
        if (this._required) this.options += " required ";
        switch(fieldType) {
            case khcText:
            case khcPassWord:
            case khcFile:
            case khcEmail:
            case khcUrl:
            case khcTel:
            case khcDateTime:
            case khcDate:
            case khcMonth:
            case khcWeek:
            case khcTime:
            case khcDateTimeLocal:
            case khcNumber:
            case khcRange:
            case khcColor:
            case khcSearch:
                ret += "<div class='label'><label for='" + this.id + "'>" + this.caption + "</label></div>" + "<div class='field'><input type='" + type + "' id='" + this.id + "' name='" + this.name + "' value='" + this.value + "' " + this.options + "/></div>";
                if (fieldType.equals(HtmlControlType.khcDate)) ret += "<script type='text/javascript'>new Forms.DatePicker('" + this.id + "','dtp" + this.id + "','date');</script>";
                break;
            case khcCheckBox:
                ret += "<div class='label'></div>";
                if (!innerHtml.equals("")) ret += "<fieldset class='checkboxList'><legend class='label'>" + caption + "</legend>" + innerHtml + "</fieldset>"; else {
                    String checked = "";
                    if (KString.isBooleanTrue(this.value)) checked = "checked";
                    ret += "<div class='field'><input onclick='javascript:this.value=(this.checked?\"true\":\"false\");' type='" + type + "' id='" + this.id + "' name='" + this.name + "' " + checked + " " + this.options + "/><label for='" + this.name + "'>" + this.caption + "</label></div>";
                }
                break;
            case khcRadio:
                ret += "<div class='label'></div>";
                if (!innerHtml.equals("")) ret += "<fieldset><legend class='label'>" + caption + "</legend>" + innerHtml + "</fieldset>"; else ret += "<div class='field'><input type='" + type + "' id='" + this.id + "' name='" + this.name + "' value='" + this.value + "' " + this.options + "/><label for='" + this.name + "'>" + this.caption + "</label></div>";
                break;
            case khcHidden:
                ret = "<input type='" + type + "' id='" + this.id + "' name='" + this.name + "' value='" + this.value + "'/>";
                break;
            case khcCmb:
                ret += "<div class='label'><label for='" + this.id + "'>" + this.caption + "</label></div>" + "<div class='field'><" + type + " id='" + this.id + "' name='" + this.name + "' " + this.options + ">" + innerHtml + "</select></div>";
                break;
            case khcList:
                String strMultiple = "";
                if (_multiple) strMultiple = " multiple ";
                ret += "<div class='label'><label for='" + this.id + "'>" + this.caption + "</label></div>" + "<div class='field'><" + type + " id='" + this.id + "' name='" + this.name + "' " + this.options + " " + strMultiple + ">" + innerHtml + "</select></div>";
                break;
            case khcPageList:
                ret += "<fieldset><legend>" + caption + "</legend>" + innerHtml + "</fieldset>\n";
                break;
            case khcCheckedList:
            case khcRadioList:
            case khcCheckedDataList:
            case khcRadioDataList:
            case khcCheckedAjaxList:
            case khcRadioAjaxList:
                String searchText = "&nbsp;<span id='ckListInfo-" + this.name + "' class='ckListInfo'></span>";
                if (HtmlControlType.khcRadioDataList.equals(fieldType) || HtmlControlType.khcCheckedDataList.equals(fieldType) || fieldType.isAjax()) searchText = "&nbsp;<input id='ckListSearchText-" + this.name + "' class='ckListSearchText' value='Rechercher...' type='text'>";
                String listCaption = "<div onselectstart='javascript:return false;' class='ckListCaption'><span id='ckListCaption-" + this.name + "'>" + this.caption + "</span>" + searchText + "</div>";
                String jsClass = "Radio";
                if (HtmlControlType.khcCheckedList.equals(fieldType) || HtmlControlType.khcCheckedDataList.equals(fieldType) || HtmlControlType.khcCheckedAjaxList.equals(fieldType)) {
                    jsClass = "Checked";
                    listCaption = "<div onselectstart='javascript:return false;' class='ckListCaption'><span class='ckListCaption-Checkall' id='ckListCaption-checkall-" + this.name + "'>&nbsp;</span>" + "<span id='ckListCaption-" + this.name + "'>" + this.caption + "</span>" + searchText + "</div>";
                }
                ret += "<div class='ckListOuter'>" + listCaption + "\n" + ajaxLoader + "<div id='ckList-inner-" + this.name + "' class='ckListInner'><div id='ckList-" + this.name + "'>" + innerHtml + "</div>" + ajaxDiv + "</div></div>\n";
                ret += "<input type='hidden' id='" + this.name + "' name='" + this.name + "' value='" + this.value + "'/>";
                ret += "<script type='text/javascript'>var ckList" + this.name + "=new Forms." + jsClass + "List('" + this.name + "');</script>\n";
                break;
            case khcDataList:
                String idDataList = "dataList-" + this.id;
                ret += "<div class='label'><label for='" + this.id + "'>" + this.caption + "</label></div>" + "<div class='field'><input type='text' id='" + this.id + "' name='" + this.name + "' value='" + this.value + "' " + this.options + "list='" + idDataList + "'/></div>\n";
                ret += "<datalist id='" + idDataList + "'>" + innerHtml + "</datalist>\n";
                break;
            case khcTextarea:
                ret += "<div class='label'><label for='" + this.id + "'>" + this.caption + "</label></div>" + "<div class='field'><" + type + " id='" + this.id + "' name='" + this.name + "' " + this.options + ">" + this.value + "</" + type + "></div>\n";
                break;
            case khcLabel:
                ret += "<div class='label'><label for='" + this.id + "'>" + this.caption + "</label><label " + this.options + ">" + this.value + "</label></div>";
                ret += "<input type='hidden' id='" + this.id + "' name='" + this.name + "' value='" + this.value + "'/>";
                break;
            case khcButton:
            case khcSubmit:
                ret += "<div class='field'><input type='" + type + "' id='" + this.id + "' name='" + this.name + "' value='" + this.value + "' " + this.options + "/></div>";
                break;
            case khcFieldSet:
                ret += "<fieldset id='" + this.id + "' " + this.options + "><legend>" + this.caption + "</legend>";
                break;
            case khcCustom:
                ret += this.value;
                break;
            default:
                break;
        }
        return before + ret + after;
    }

    private Map<String, String> listToMap() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        if (this.listObject instanceof KListObject) {
            @SuppressWarnings("rawtypes") KListObject kl = ((KListObject) this.listObject);
            for (Object o : kl) {
                try {
                    KObject ko = (KObject) o;
                    String idItem = ko.getFirstKeyValue().toString();
                    String valueItem = "";
                    if ("".equals(listMask)) valueItem = ko.toString(); else valueItem = ko.showWithMask(listMask);
                    result.put(idItem, valueItem);
                } catch (Exception e) {
                }
            }
        } else if (this.listObject instanceof KStrings) {
            Map<String, Object> ks = ((KStrings) this.listObject).getStrings();
            for (Map.Entry<String, Object> e : ks.entrySet()) {
                result.put(e.getKey(), (String) e.getValue());
            }
        } else {
            if (this.listObject != null) {
                String sl = this.listObject.toString();
                sl = sl.replace("{", "").replace("}", "");
                this.listObject = new KStrings(sl, ",", ":");
                result = listToMap();
            }
        }
        return result;
    }

    public String mapListToString() {
        String result = "";
        if (HtmlControlType.khcPageList.equals(this.fieldType)) {
            if (owner != null) {
                KPageList kpage = owner.hasAndBelongsToManyList(name, 1);
                if (kpage != null) {
                    kpage.setEditable(false);
                    kpage.getDefaultComplete();
                    result += kpage.toString();
                }
            }
        } else {
            Map<String, String> list = listToMap();
            String type = this.fieldType.toString();
            int i = 0;
            for (Map.Entry<String, String> item : list.entrySet()) {
                String idItem = item.getKey();
                String valueItem = item.getValue();
                String selected = "";
                String style = "";
                switch(this.fieldType) {
                    case khcDataList:
                        result += "<option>" + valueItem + "</option>\n";
                        break;
                    case khcCmb:
                    case khcList:
                        if ((valuesSep + this.value + valuesSep).contains(valuesSep + idItem + valuesSep)) selected = " selected ";
                        result += "<option" + selected + " value='" + idItem + "' ondblclick='if(this.selected)this.parentNode.selectedIndex=-1;'>" + valueItem + "</option>\n";
                        break;
                    case khcCheckedList:
                        selected = "ckItem";
                        if ((valuesSep + this.value + valuesSep).contains(valuesSep + idItem + valuesSep)) selected = "ckItem-selected";
                        result += "<div class='" + selected + "' id='ckItem-" + this.name + "-" + idItem + "' " + this.options + ">" + valueItem + "</div>\n";
                        break;
                    case khcRadioList:
                        selected = "rItem";
                        if ((this.value).equals(idItem)) selected = "rItem-selected";
                        result += "<div class='" + selected + "' id='ckItem-" + this.name + "-" + idItem + "' " + this.options + ">" + valueItem + "</div>\n";
                        break;
                    case khcCheckedDataList:
                    case khcCheckedAjaxList:
                        selected = "ckItem";
                        if ((valuesSep + this.value + valuesSep).contains(valuesSep + idItem + valuesSep)) selected = "ckItem-selected"; else style = "style='display:none'";
                        result += "<div class='" + selected + "' id='ckItem-" + this.name + "-" + idItem + "' " + this.options + " " + style + ">" + valueItem + "</div>\n";
                        break;
                    case khcRadioDataList:
                    case khcRadioAjaxList:
                        selected = "rItem";
                        if ((this.value).equals(idItem)) selected = "rItem-selected"; else style = "style='display:none'";
                        result += "<div class='" + selected + "' id='ckItem-" + this.name + "-" + idItem + "' " + this.options + " " + style + ">" + valueItem + "</div>\n";
                        break;
                    case khcCheckBox:
                    case khcRadio:
                        _multiple = true;
                        if ((valuesSep + this.value + valuesSep).contains(valuesSep + idItem + valuesSep)) selected = " checked ";
                        result += "<div class='field'><input " + selected + " type='" + type + "' id='" + this.id + i + "' name='" + this.name + "' value='" + idItem + "' " + this.options + "><label for='" + this.name + i + "'>" + valueItem + "</label>\n";
                        break;
                    default:
                        break;
                }
                i++;
            }
        }
        return result;
    }

    public String getValue() {
        return value;
    }

    /**
	 * Retourne la valeur du contrôle contenue dans les paramètres de la requête<br/>
	 * traitement spécial des checkboxes qui retournent null si elle ne sont pas cochées en java
	 * @param request requête Http
	 * @return valeur du contrôle
	 */
    public String getValue(HttpServletRequest request) {
        String result = null;
        if (!_multiple && HtmlControlType.khcCheckBox.equals(fieldType)) {
            result = "false";
            if (request.getParameter(name) != null) result = "true";
        } else if (_multiple) return KStrings.implode(valuesSep, request.getParameterValues(name)); else result = request.getParameter(name);
        return result;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getListObject() {
        return listObject;
    }

    public void setListObject(Object listObject) {
        this.listObject = listObject;
    }

    public String getOptions() {
        return options;
    }

    /**
	 * @param options
	 */
    public void setOptions(String options) {
        this.options = options;
    }

    public String getBefore() {
        return before;
    }

    /**
	 * Chaîne à insérer avant le contrôle visuel
	 * @param before chaîne HTML
	 */
    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    /**
	 * Chaîne à insérer arès le contrôle visuel
	 * @param after chaîne HTML
	 */
    public void setAfter(String after) {
        this.after = after;
    }

    /**
	 * @return listMask
	 */
    public String getListMask() {
        return listMask;
    }

    /**
	 * Modifie le masque d'affichage de la valeur de la liste
	 * le masque est utilisé avec la méthode showWithMask
	 * @param listMask
	 */
    public void setListMask(String listMask) {
        this.listMask = listMask;
    }

    public static KHtmlFieldControl createList(String idNameCaption, Object listObject) {
        return createList(idNameCaption, "", idNameCaption, idNameCaption, listObject, "");
    }

    public static KHtmlFieldControl createList(String idNameCaption, String value, Object listObject) {
        return createList(idNameCaption, value, idNameCaption, idNameCaption, listObject, "");
    }

    public static KHtmlFieldControl createList(String idName, String caption, String value, Object listObject) {
        return createList(caption, value, idName, idName, listObject, "");
    }

    /**
	 * Retourne une liste HTML alimentée par l'objet listObject passé en paramètre
	 * @param caption étiquette de la liste
	 * @param value valeur par défaut
	 * @param name nom HTML
	 * @param id identifiant HTML
	 * @param listObject liste de type KListObject ou KStrings
	 * @param options options HTML supplémentaires
	 * @return
	 */
    public static KHtmlFieldControl createList(String caption, String value, String name, String id, Object listObject, String options) {
        return new KHtmlFieldControl(caption, value, name, id, HtmlControlType.khcList, listObject, options, true);
    }

    public KHttpForm getOwner() {
        return owner;
    }

    public void setOwner(KHttpForm owner) {
        this.owner = owner;
    }
}
