package org.monet.backmobile.model;

public class Language extends org.monet.kernel.model.Language {

    private static Language oInstance;

    public static synchronized Language getInstance() {
        if (oInstance == null) {
            oInstance = new Language();
        }
        return (org.monet.backmobile.model.Language) oInstance;
    }
}
