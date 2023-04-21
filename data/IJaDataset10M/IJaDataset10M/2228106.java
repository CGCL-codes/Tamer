package com.jlz.beans.def;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.julewa.db.DB;
import com.julewa.db.Entity;

@Component
@Scope(Entity.SCOPE)
public class SiteBean extends Entity<Integer> {

    @DB.COLUMN
    String domain;

    @DB.COLUMN
    int type;

    @DB.COLUMN
    String description;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
