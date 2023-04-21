package org.riverock.dbrevision.manager;

/**
 * User: SergeMaslyukov
 * Date: 29.07.2007
 * Time: 13:11:08
 */
public class RevisionBean {

    private String moduleName;

    private String currentVerson;

    private String lastPatch;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getCurrentVerson() {
        return currentVerson;
    }

    public void setCurrentVerson(String currentVerson) {
        this.currentVerson = currentVerson;
    }

    public String getLastPatch() {
        return lastPatch;
    }

    public void setLastPatch(String lastPatch) {
        this.lastPatch = lastPatch;
    }
}
