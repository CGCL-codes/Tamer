package org.isi.monet.core.model;

import java.util.HashMap;

public class Session extends BaseModel {

    private Account oAccount;

    private HashMap<String, Object> hmAttributes;

    public Session() {
        super();
        this.oAccount = new Account();
        this.hmAttributes = new HashMap<String, Object>();
    }

    public Account getAccount() {
        return this.oAccount;
    }

    public Boolean setAccount(Account oAccount) {
        this.oAccount = oAccount;
        return true;
    }

    public Boolean existVariable(String sVariable) {
        return this.hmAttributes.containsKey(sVariable);
    }

    public Object getVariable(String sVariable) {
        return this.hmAttributes.get(sVariable);
    }

    public Boolean setVariable(String sVariable, Object oValue) {
        if (oValue == null) this.hmAttributes.remove(sVariable); else this.hmAttributes.put(sVariable, oValue);
        return true;
    }
}
