package org.mftech.dawn.server.projects;

import java.util.Map;

public interface Role {

    boolean isAllowed(int insert);

    void setRoleId(int roleId);

    int getRoleId();

    String getName();

    void setName(String name);

    Map<Integer, Boolean> getOperations();

    void addOperation(int operation, boolean allowed);

    String getName(int id);
}
