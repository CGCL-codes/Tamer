package net.sourceforge.eclipsetrader.core.db;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class SecurityGroup extends PersistentObject {

    String code = "";

    String description = "";

    Currency currency;

    SecurityGroup parentGroup;

    List<PersistentObject> childrens = new ArrayList<PersistentObject>();

    public SecurityGroup() {
    }

    public SecurityGroup(Integer id) {
        super(id);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        setChanged();
    }

    public Currency getCurrency() {
        if (currency == null && parentGroup != null) return parentGroup.getCurrency();
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public SecurityGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(SecurityGroup group) {
        if (group != null && !group.equals(this.parentGroup)) setChanged(); else if (group == null && this.parentGroup != null) setChanged();
        if (this.parentGroup != null) this.parentGroup.childrens.remove(this);
        this.parentGroup = group;
        if (this.parentGroup != null) this.parentGroup.childrens.add(this);
    }

    public List<PersistentObject> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<PersistentObject> childrens) {
        this.childrens = childrens;
    }
}
