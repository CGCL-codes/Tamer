package com.xtech.common.entities;

import com.xtech.common.Filter;

/**
 * @author jscruz
 * @since XERP
 */
public class TaskOwnerFilter extends Filter {

    String owner;

    public TaskOwnerFilter() {
        setAllowMultiple(false);
    }

    public TaskOwnerFilter(String user) {
        this();
        setOwner(user);
    }

    public boolean fliterEntity(Entity e) {
        if (e != null && e instanceof ProjectTask) {
            ProjectTask i = (ProjectTask) e;
            return i.getOwner().equals(owner);
        } else {
            return false;
        }
    }

    /**
	 * @return
	 * @author jscruz
	 * @since XERP
	 */
    public String getOwner() {
        return owner;
    }

    /**
	 * @param string
	 * @author jscruz
	 * @since XERP
	 */
    public void setOwner(String string) {
        owner = string;
    }
}
