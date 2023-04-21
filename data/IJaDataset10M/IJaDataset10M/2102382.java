package rql4j.domain;

public class Languagevariant {

    private String guid;

    private String action;

    private Integer checked;

    private String codeTable;

    private Integer isMainLanguage;

    private String language;

    private String name;

    private String rfcLanguageId;

    private String textDirection;

    public Languagevariant() {
    }

    public Languagevariant(String guid) {
        this.guid = guid;
    }

    public String getGuid() {
        return guid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public String getCodeTable() {
        return codeTable;
    }

    public void setCodeTable(String codeTable) {
        this.codeTable = codeTable;
    }

    public Integer getMainLanguage() {
        return isMainLanguage;
    }

    public void setMainLanguage(Integer mainLanguage) {
        isMainLanguage = mainLanguage;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRfcLanguageId() {
        return rfcLanguageId;
    }

    public void setRfcLanguageId(String rfcLanguageId) {
        this.rfcLanguageId = rfcLanguageId;
    }

    public String getTextDirection() {
        return textDirection;
    }

    public void setTextDirection(String textDirection) {
        this.textDirection = textDirection;
    }

    @Override
    public String toString() {
        return "Languagevariant{" + "guid='" + guid + '\'' + ", action='" + action + '\'' + ", checked=" + checked + ", codeTable='" + codeTable + '\'' + ", isMainLanguage=" + isMainLanguage + ", language='" + language + '\'' + ", name='" + name + '\'' + ", rfcLanguageId='" + rfcLanguageId + '\'' + ", textDirection='" + textDirection + '\'' + '}';
    }
}
