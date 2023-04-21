package com.jlz.beans.user;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.jlz.managers.user.Admins;
import com.julewa.User;
import com.julewa.db.DB;
import com.julewa.db.Entity;

@Component
@Scope(Entity.SCOPE)
public class AdminBean extends Entity<Long> implements User {

    @DB.COLUMN
    String name;

    @DB.COLUMN
    String usercode;

    @DB.COLUMN
    String password;

    @DB.COLUMN
    int type = Admins.MANAGER;

    @DB.COLUMN
    int state;

    @DB.COLUMN
    String description = null;

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
