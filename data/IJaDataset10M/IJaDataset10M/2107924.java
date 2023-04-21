package pl.taab.scrachi.gwt.client.dynamicLocale;

import java.util.HashMap;
import com.google.gwt.i18n.client.Dictionary;

public class LocaleModel {

    private final HashMap dicts = new HashMap();

    private String locale;

    public LocaleModel() {
        super();
        dicts.put("EN", Dictionary.getDictionary("LiteralsEN"));
        dicts.put("PL", Dictionary.getDictionary("LiteralsPL"));
    }

    public String getLiteral(String name) {
        Dictionary d = (Dictionary) dicts.get(getLocale());
        String str = d.get(name);
        return str;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
        DynamicLocale.INSTANCE.applyLocaleChange(this);
    }
}
