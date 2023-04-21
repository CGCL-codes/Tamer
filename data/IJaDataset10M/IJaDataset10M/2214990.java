package vqwiki;

import java.util.*;
import java.io.Serializable;

/**
 * @author garethc
 * Date: Dec 17, 2002
 */
public class WikiGroup implements Serializable {

    private String name;

    private Map permissionMap;

    private Collection members;

    private boolean admin;

    private boolean delete;

    private boolean create;

    public WikiGroup(String name) {
        this.name = name;
        this.permissionMap = new HashMap();
        this.members = new HashSet();
    }

    public void addMember(WikiMember member) {
        this.members.add(member);
    }

    public void addPermission(TopicGroup topicGroup, Permission permission) {
        Set permissions = getPermissionsFor(topicGroup);
        permissions.add(permission);
    }

    public void revokePermission(TopicGroup topicGroup, Permission permission) {
        Set permissions = getPermissionsFor(topicGroup);
        permissions.remove(permission);
    }

    public boolean hasPermission(TopicGroup topicGroup, Permission permission) {
        Set permissions = getPermissionsFor(topicGroup);
        return permissions.contains(permission);
    }

    private Set getPermissionsFor(TopicGroup topicGroup) {
        Set permissions = null;
        if (this.permissionMap.containsKey(topicGroup)) {
            permissions = (Set) this.permissionMap.get(topicGroup);
        }
        if (permissions == null) {
            permissions = new HashSet();
            this.permissionMap.put(topicGroup, permissions);
        }
        return permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection getMembers() {
        return this.members;
    }

    public static Collection getDefaultGroups() {
        Collection groups = new HashSet();
        groups.add(new WikiGroup("public"));
        return groups;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WikiGroup)) return false;
        final WikiGroup wikiGroup = (WikiGroup) o;
        if (name != null ? !name.equals(wikiGroup.name) : wikiGroup.name != null) return false;
        return true;
    }

    public int hashCode() {
        return (name != null ? name.hashCode() : 0);
    }
}
