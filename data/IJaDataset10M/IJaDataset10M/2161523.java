package org.isi.monet.core.model;

import java.util.HashMap;
import java.util.Iterator;
import net.sf.json.JSONObject;
import org.isi.monet.core.constants.AccountType;
import org.isi.monet.core.constants.Strings;

public class Account extends BaseModel {

    protected String codeBusinessUnit;

    protected String Type;

    protected Node oRootNode;

    protected User oUser;

    protected HashMap<String, Profile> hmProfiles;

    protected TeamList oTeamList;

    protected RoleList oRoleList;

    protected SkillList oSkillList;

    public static final String TEAMLIST = "TeamList";

    public static final String ROLELIST = "RoleList";

    public static final String SKILLLIST = "SkillList";

    public Account() {
        super();
        this.codeBusinessUnit = Strings.EMPTY;
        this.Type = AccountType.NONE;
        this.oRootNode = new Node();
        this.oUser = new User();
        this.hmProfiles = new HashMap<String, Profile>();
        this.oTeamList = new TeamList();
        this.oRoleList = new RoleList();
        this.oSkillList = new SkillList();
    }

    public String getCodeBusinessUnit() {
        return this.codeBusinessUnit;
    }

    public Boolean setCodeBusinessUnit(String codeBusinessUnit) {
        this.codeBusinessUnit = codeBusinessUnit;
        return true;
    }

    public String getType() {
        return this.Type;
    }

    public Boolean setType(String Type) {
        this.Type = Type.toLowerCase();
        return true;
    }

    public Boolean isBackAccount() {
        return this.Type.equals(AccountType.BACK);
    }

    public Boolean isFrontAccount() {
        return this.Type.equals(AccountType.FRONT);
    }

    public Boolean isModelerAccount() {
        return this.Type.equals(AccountType.MODELER);
    }

    public Boolean isHolder() {
        Iterator<Object> oIterator = this.getRoleList().getIterator();
        while (oIterator.hasNext()) {
            Role oRole = (Role) oIterator.next();
            if (oRole.getName().equals(Role.HOLDER)) return true;
        }
        return false;
    }

    public Node getRootNode() {
        return this.oRootNode;
    }

    public User getUser() {
        return this.oUser;
    }

    public Boolean setUser(User oUser) {
        this.oUser = oUser;
        return true;
    }

    public HashMap<String, Profile> getProfiles() {
        return this.hmProfiles;
    }

    public Profile getProfile(String code) {
        if (!this.hmProfiles.containsKey(code)) return null;
        return this.hmProfiles.get(code);
    }

    public Boolean setProfile(String code, Profile oProfile) {
        this.hmProfiles.put(code, oProfile);
        return true;
    }

    public TeamList getTeamList() {
        onLoad(this, Account.TEAMLIST);
        return this.oTeamList;
    }

    public Boolean setTeamList(TeamList oTeamList) {
        this.oTeamList = oTeamList;
        this.addLoadedAttribute(Account.TEAMLIST);
        return true;
    }

    public RoleList getRoleList() {
        onLoad(this, Account.ROLELIST);
        return this.oRoleList;
    }

    public Boolean setRoleList(RoleList oRoleList) {
        this.oRoleList = oRoleList;
        this.addLoadedAttribute(Account.ROLELIST);
        return true;
    }

    public SkillList getSkillList() {
        onLoad(this, Account.SKILLLIST);
        return this.oSkillList;
    }

    public Boolean setSkillList(SkillList oSkillList) {
        this.oSkillList = oSkillList;
        this.addLoadedAttribute(Account.SKILLLIST);
        return true;
    }

    public String serializeToJSON() {
        String sResult = Strings.EMPTY;
        Boolean bPartialLoading = this.isPartialLoading();
        sResult = "\"id\":\"" + this.getId() + "\",";
        sResult += "\"type\":\"" + this.getType() + "\",";
        sResult += "\"user\":" + this.getUser().serializeToJSON() + ",";
        if (bPartialLoading) this.disablePartialLoading();
        sResult += "\"rootnode\":" + this.getRootNode().serializeToJSON();
        if (bPartialLoading) this.enablePartialLoading();
        return "{" + sResult + "}";
    }

    public String serializeToJSON(String sProfile) {
        Profile oProfile;
        String sResult = Strings.EMPTY;
        Boolean bPartialLoading = this.isPartialLoading();
        sResult = "\"id\":\"" + this.getId() + "\",";
        sResult += "\"type\":\"" + this.getType() + "\",";
        sResult += "\"user\":" + this.getUser().serializeToJSON() + ",";
        if (bPartialLoading) this.disablePartialLoading();
        sResult += "\"rootnode\":" + this.getRootNode().serializeToJSON() + ",";
        if (bPartialLoading) this.enablePartialLoading();
        if (!this.hmProfiles.containsKey(sProfile)) return Strings.EMPTY;
        oProfile = this.hmProfiles.get(sProfile);
        sResult += "\"profile\":" + oProfile.serializeToJSON();
        return "{" + sResult + "}";
    }

    public Boolean unserializeFromJSON(String sData) {
        JSONObject joData = JSONObject.fromObject(sData);
        this.oUser.unserializeFromJSON(((JSONObject) joData.get("user")).toString());
        return true;
    }
}
