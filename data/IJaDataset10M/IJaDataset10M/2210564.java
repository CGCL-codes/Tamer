package org.openejb.alt.assembler.modern.jar.ejb11;

/**
 * Metadata for an EJB JAR security role.  You will have to look in the
 * server-specific metadata to see what physical roles this logical role
 * maps to.
 *
 * @author Aaron Mulder (ammulder@alumni.princeton.edu)
 * @version $Revision: 1.2 $
 */
public class SecurityRoleMetaData {

    private String name;

    private String description;

    public SecurityRoleMetaData() {
    }

    public SecurityRoleMetaData(SecurityRoleMetaData source) {
        name = source.name;
        description = source.description;
    }

    public void setRoleName(String name) {
        this.name = name;
    }

    public String getRoleName() {
        return name;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return name;
    }
}
