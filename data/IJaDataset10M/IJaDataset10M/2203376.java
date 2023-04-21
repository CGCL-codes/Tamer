package realcix20.guis.utils;

import realcix20.utils.GlobalValueManager;

public class I18NManager {

    public static String getI18NString(String prefix, Object id) {
        String i18nString = null;
        if ((id != null) && (id.toString().length() > 0)) {
            if (id.toString().trim().equals("")) id = "";
            i18nString = TxtManager.getTxt(prefix + "." + id, GlobalValueManager.getApplicationLang());
        }
        if (i18nString == null) i18nString = "";
        return i18nString;
    }
}
