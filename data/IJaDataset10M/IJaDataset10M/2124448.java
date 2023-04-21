package de.bea.services.vidya.client.datasource.types;

public class WSLanguage {

    protected java.lang.String langName;

    protected java.lang.String langSuffix;

    protected java.lang.String localsDef;

    protected int position;

    public WSLanguage() {
    }

    public WSLanguage(java.lang.String langName, java.lang.String langSuffix, java.lang.String localsDef, int position) {
        this.langName = langName;
        this.langSuffix = langSuffix;
        this.localsDef = localsDef;
        this.position = position;
    }

    public java.lang.String getLangName() {
        return langName;
    }

    public void setLangName(java.lang.String langName) {
        this.langName = langName;
    }

    public java.lang.String getLangSuffix() {
        return langSuffix;
    }

    public void setLangSuffix(java.lang.String langSuffix) {
        this.langSuffix = langSuffix;
    }

    public java.lang.String getLocalsDef() {
        return localsDef;
    }

    public void setLocalsDef(java.lang.String localsDef) {
        this.localsDef = localsDef;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
