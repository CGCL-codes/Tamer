package org.exist.client;

import java.util.Date;
import org.exist.xmldb.XmldbURI;

/**
 * Description of a resource, suitable for display by the graphical
 * client for instance.
 *
 * @author gpothier
 */
public abstract class ResourceDescriptor {

    private XmldbURI name;

    private String owner;

    private String group;

    private String permissions;

    private Date date;

    public ResourceDescriptor(XmldbURI aName, String aOwner, String aGroup, String aPermissions, Date date) {
        name = aName;
        owner = aOwner;
        group = aGroup;
        permissions = aPermissions;
        this.date = date;
    }

    public String getGroup() {
        return group;
    }

    public XmldbURI getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getPermissions() {
        return permissions;
    }

    public Date getDate() {
        return date;
    }

    public abstract boolean isCollection();

    public static class Document extends ResourceDescriptor {

        public Document(XmldbURI aName, String aOwner, String aGroup, String aPermissions, Date date) {
            super(aName, aOwner, aGroup, aPermissions, date);
        }

        public boolean isCollection() {
            return false;
        }
    }

    public static class Collection extends ResourceDescriptor {

        public Collection(XmldbURI aName) {
            super(aName, null, null, null, null);
        }

        public Collection(XmldbURI aName, String aOwner, String aGroup, String aPermissions, Date date) {
            super(aName, aOwner, aGroup, aPermissions, date);
        }

        public boolean isCollection() {
            return true;
        }
    }
}
