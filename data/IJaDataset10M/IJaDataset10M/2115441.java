package org.vqwiki.security;

/**
 * TODO - document class
 */
public class PermissionGroup {

    private String name = null;

    private Permission[] perms;

    private PermissionGroup() {
    }

    protected PermissionGroup(String name) {
        this.name = name;
    }

    protected PermissionGroup(String name, Permission[] perms) {
        this.name = name;
        this.perms = perms;
    }
}
