package org.monet.kernel.model;

import java.util.HashSet;

public interface ISecurable {

    public boolean isPublic();

    public boolean isLocked();

    public boolean isLinked();

    public PermissionList getPermissionList();

    public HashSet<String> getGrantedUsers();

    public HashSet<String> getRoles();

    public HashSet<String> getSkills();

    public HashSet<String> getRules();
}
