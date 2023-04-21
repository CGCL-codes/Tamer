package org.tolven.model;

import java.util.Date;
import org.tolven.app.entity.MenuData;

public abstract class ModelObject {

    private Date now;

    private MenuData placeholder;

    public ModelObject(MenuData placeholder, Date now) {
        this.placeholder = placeholder;
        this.now = now;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public MenuData getPlaceholder() {
        return placeholder;
    }
}
