package net.sourceforge.oradoc.db;

public class AutoCompleteContext {

    private String line;

    private String key;

    private boolean hasOpenBracket;

    private int posX;

    private int posY;

    private int width;

    private int height;

    private boolean isObjectItem;

    private String objName;

    private String objItemName;

    private String project;

    private String profile;

    private String separator;

    private boolean isUseWildcardsObjName;

    private boolean isUseWildcardsObjItemName;

    private boolean isRetreiveObjNameRef;

    private String orderBy;

    private boolean showTemplates;

    public boolean isRetreiveObjNameRef() {
        return isRetreiveObjNameRef;
    }

    public void setRetreiveObjNameRef(boolean isRetreiveObjNameRef) {
        this.isRetreiveObjNameRef = isRetreiveObjNameRef;
    }

    public AutoCompleteContext() {
        isUseWildcardsObjName = false;
        isUseWildcardsObjItemName = false;
        isRetreiveObjNameRef = false;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public boolean isObjectItem() {
        return isObjectItem;
    }

    public void setObjectItem(boolean isObjectItem) {
        this.isObjectItem = isObjectItem;
    }

    public String getObjItemName() {
        return objItemName;
    }

    public void setObjItemName(String objItemName) {
        this.objItemName = objItemName;
        if (objItemName != null && objItemName.indexOf("*") != -1) {
            setUseWildcardsObjItemName(true);
        } else {
            setUseWildcardsObjItemName(false);
        }
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
        if (objName != null && objName.indexOf("*") != -1) {
            setUseWildcardsObjName(true);
        } else {
            setUseWildcardsObjName(false);
        }
    }

    public boolean hasOpenBracket() {
        return hasOpenBracket;
    }

    public void setHasOpenBracket(boolean hasOpenBracket) {
        this.hasOpenBracket = hasOpenBracket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public String getProject() {
        return this.project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public boolean isUseWildcardsObjItemName() {
        return isUseWildcardsObjItemName;
    }

    public void setUseWildcardsObjItemName(boolean isUseWildcardsObjItemName) {
        this.isUseWildcardsObjItemName = isUseWildcardsObjItemName;
    }

    public boolean isUseWildcardsObjName() {
        return isUseWildcardsObjName;
    }

    public void setUseWildcardsObjName(boolean isUseWildcardsObjName) {
        this.isUseWildcardsObjName = isUseWildcardsObjName;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isShowTemplates() {
        return showTemplates;
    }

    public void setShowTemplates(boolean showTemplates) {
        this.showTemplates = showTemplates;
    }
}
