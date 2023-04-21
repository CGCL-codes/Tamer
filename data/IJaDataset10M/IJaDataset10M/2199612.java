package dms.core.user.role.logic;

import dms.core.logic.BizModel;

public interface Role extends BizModel {

    public static final String ROLE_ADMIN = "Administrator";

    public static final String ROLE_EDITOR = "Editor";

    public static final String ROLE_USER = "User";

    public static final String FLD_NAME = "name";

    public abstract Long getId();

    public abstract void setId(Long id);

    public abstract String getName();

    public abstract void setName(String name);
}
