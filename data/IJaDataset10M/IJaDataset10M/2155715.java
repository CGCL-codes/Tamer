package net.teqlo.db.impl;

import java.util.HashMap;
import java.util.Map;
import org.jdom.Element;
import com.sleepycat.dbxml.XmlException;
import net.teqlo.TeqloException;
import net.teqlo.db.RoleLookup;
import net.teqlo.db.ServiceLookup;
import net.teqlo.db.XmlDatabase;
import net.teqlo.util.References;
import net.teqlo.xml.XmlConstantsV10c;

public class RoleLookupImpl implements RoleLookup {

    public static String FQN_VARIABLE = "fqn";

    public static final String ROLE_QUERY = "//tq:role[@fqn eq $fqn]";

    private String serviceFqn = null;

    protected String roleFqn = null;

    protected String roleLabel = null;

    protected String roleFolder = null;

    protected Short roleSerial = null;

    protected int roleValue = 0;

    protected boolean isDefaultRole = false;

    protected Map<RoleLookup, Boolean> includesCache = new HashMap<RoleLookup, Boolean>(4);

    /**
	 * Returns the folder this role is contained in
	 * @return String folder ending in '/'
	 */
    public String getRoleFolder() {
        return this.roleFolder;
    }

    /**
	 * Returns the fully qualified name of this role
	 * @return String fqn of this role
	 */
    public String getRoleFqn() {
        return this.roleFqn;
    }

    public String getRoleLabel() {
        return this.roleLabel;
    }

    /**
	 * Returns the serial number of this role
	 * @return Short serial number
	 */
    public Short getRoleSerial() {
        return this.roleSerial;
    }

    /**
	 * Returns the value of this role. A higher value implies all the facilities of a lower valued role
	 * @return int role value
	 */
    public int getRoleValue() {
        return this.roleValue;
    }

    /**
	 * Returns true if this role is the default role in its folder (only one role can be the default).
	 * @return boolean true if this role is the default
	 */
    public boolean isDefaultRole() {
        return this.isDefaultRole;
    }

    /**
	 * Returns true if this role includes the supplied subrole. The result of this query is cached
	 * in this role lookup for performance
	 * 
	 * TODO: As roleIncludesQuery is now done without an actual database query, we can 
	 *       consider caching the result.
	 */
    public boolean includes(RoleLookup subrole) throws TeqloException {
        if (includesCache.containsKey(subrole)) return includesCache.get(subrole);
        boolean includes = roleIncludesQuery(serviceFqn, getRoleFqn(), subrole.getRoleFqn());
        includesCache.put(subrole, includes);
        return includes;
    }

    private boolean roleIncludesQuery(String serviceFqn, String aboveRoleFqn, String belowRoleFqn) throws TeqloException {
        if (belowRoleFqn.startsWith(folderOf(aboveRoleFqn))) {
            ServiceLookup roleData = XmlDatabase.getInstance().getServiceLookup(serviceFqn);
            RoleLookup aboveRole = roleData.getRoleLookup(aboveRoleFqn);
            RoleLookup belowRole = roleData.getRoleLookup(belowRoleFqn);
            RoleLookup effectiveRole = effectiveRole0(roleData, aboveRole, folderOf(belowRoleFqn));
            return effectiveRole != null && effectiveRole.getRoleValue() >= belowRole.getRoleValue();
        }
        return false;
    }

    private String folderOf(String fqn) {
        int index = fqn.lastIndexOf("/");
        if (index != -1) return fqn.substring(0, index + 1);
        return fqn;
    }

    private RoleLookup effectiveRole0(ServiceLookup roleData, RoleLookup aboveRole, String folder) throws TeqloException {
        if (aboveRole == null) return null;
        if (aboveRole.getRoleFolder().equals(folder)) return aboveRole;
        {
            String stepDownFolder = stepDownFolder(aboveRole.getRoleFolder(), folder);
            RoleLookup stepDownRole = roleData.getDefaultFolderRole(stepDownFolder);
            return effectiveRole0(roleData, stepDownRole, folder);
        }
    }

    private String stepDownFolder(String currentFolder, String targetFolder) {
        int index = targetFolder.indexOf("/", currentFolder.length());
        if (index == -1) return References.asFolder(targetFolder);
        return targetFolder.substring(0, index + 1);
    }

    /**
	 * Factory method to make a role lookup object
	 * @param sl
	 * @param fqn
	 * @returns
	 * @throws XmlException 
	 * @throws XmlException
	 */
    public static RoleLookupImpl make(ServiceLookup sl, Element roleVal) throws XmlException {
        RoleLookupImpl rl = new RoleLookupImpl();
        rl.serviceFqn = sl.getServiceFqn();
        rl.roleFqn = roleVal.getAttributeValue(XmlConstantsV10c.FQN_ATTRIBUTE, "");
        rl.roleLabel = roleVal.getAttributeValue(XmlConstantsV10c.LABEL_ATTRIBUTE);
        String defaultValue = roleVal.getAttributeValue(XmlConstantsV10c.IS_DEFAULT_ATTRIBUTE);
        rl.isDefaultRole = defaultValue != null && defaultValue.equals(XmlConstantsV10c.TRUE_VALUE);
        rl.roleFolder = roleVal.getAttributeValue(XmlConstantsV10c.FOLDER_ATTRIBUTE, "");
        rl.roleSerial = Short.parseShort(roleVal.getAttributeValue(XmlConstantsV10c.SERIAL_ATTRIBUTE, "0"));
        rl.roleValue = Integer.parseInt(roleVal.getAttributeValue(XmlConstantsV10c.VALUE_ATTRIBUTE, "0"));
        return rl;
    }

    @Override
    public String toString() {
        return "Role Lookup: " + this.roleFqn;
    }
}
