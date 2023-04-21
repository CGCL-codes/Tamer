package tei.cr.teiDocument.vocabulary;

import java.util.HashMap;
import java.util.Map;

class TeiModuleImpl extends AbstractTeiClass implements TeiModule {

    private int moduleID;

    private final String moduleName;

    private final Map teiElementMembers;

    public void setTeiMember(TeiObject[] members) {
        throw new UnsupportedOperationException("Cannot modify the member of a module.");
    }

    public TeiObject[] getMember() {
        return null;
    }

    public boolean containsMember(TeiObject teiObject) {
        return false;
    }

    protected TeiModuleImpl(String moduleName, int moduleID, TeiElement[] members) {
        this.moduleID = moduleID;
        this.moduleName = moduleName;
        this.teiElementMembers = new HashMap(members.length);
        for (int i = 0; i < members.length; i++) {
        }
    }

    public int getModuleID() {
        return moduleID;
    }

    protected void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public String getModuleName() {
        return moduleName;
    }

    public TeiElement[] getTeiElementMembers() {
        return (TeiElement[]) teiElementMembers.keySet().toArray(new TeiElement[] {});
    }

    public boolean isElementMemberOf(TeiElement teiElement) {
        return teiElementMembers.containsKey(teiElement);
    }

    protected void removeTeiElement(TeiElement element) {
        teiElementMembers.remove(element);
    }

    protected void addTeiElement(TeiElement element) {
        teiElementMembers.put(element, null);
    }
}
