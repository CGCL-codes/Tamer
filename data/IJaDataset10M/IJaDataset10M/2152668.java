package org.wikiup.romulan.gom.imp.info;

import org.wikiup.core.inf.Document;
import org.wikiup.core.inf.DocumentAware;
import org.wikiup.core.inf.Getter;
import org.wikiup.core.util.Documents;
import org.wikiup.core.util.StringUtil;
import org.wikiup.romulan.gom.inf.NamedModelInf;

public class I18NInformation implements NamedModelInf, DocumentAware, Getter<Object> {

    private Document configure;

    private String i18nBase;

    public String getName() {
        return "info";
    }

    public void aware(Document desc) {
        configure = desc;
        i18nBase = Documents.getDocumentValue(desc, "i18n-base", null);
    }

    public Object get(String name) {
        return StringUtil.connect(i18nBase, name, '.');
    }
}
