package gov.lanl.Database.JNDI;

import gov.lanl.Utility.ConfigProperties;
import gov.lanl.Database.JNDI.Config;
import gov.lanl.Database.*;
import org.apache.log4j.Logger;
import javax.naming.*;
import javax.naming.directory.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Class declaration
 *
 *
 * @author <a href="mailto:Sascha@Koenig.net">Sascha A. Koenig<a>
 * @version $Revision: 3335 $ $Date: 2006-03-27 18:28:01 -0500 (Mon, 27 Mar 2006) $
 */
public class DatabaseMgr implements gov.lanl.Database.DatabaseMgr {

    private static Logger cat = Logger.getLogger(DatabaseMgr.class.getName());

    private static gov.lanl.Database.DatabaseMgr dbMgr;

    private DirContext dir_ctx = null;

    private String DatabaseAddress;

    private String Username;

    private String Password;

    private String ConfigFileName;

    protected int Debug_Level = 5;

    private UtilityObject Utility_Object;

    private static String UtilityEntryDN;

    /**
     * Constructor
     */
    public DatabaseMgr() {
    }

    /**
     * Turns the debug mode on/off
     * (default is off)
     *
     * @param flag
     */
    public void setDebug(boolean flag) {
        cat.error("Method 'setDebug' not implemented!, set 'DEBUG_LEVEL' in configuration file");
    }

    public void init(String databaseURLString) {
        StringTokenizer st = new StringTokenizer(databaseURLString, ";");
        int num_tokens = st.countTokens();
        DatabaseAddress = st.nextToken();
        Username = st.nextToken();
        Password = st.nextToken();
        ConfigFileName = st.nextToken();
        String Debug_Level_String = null;
        try {
            Debug_Level_String = st.nextToken();
            st = new StringTokenizer(Debug_Level_String, "=");
            num_tokens = st.countTokens();
            if ((num_tokens == 2) && ("DEBUG_LEVEL".equals(st.nextToken()))) {
                Debug_Level = Integer.parseInt(st.nextToken());
            }
        } catch (NumberFormatException e) {
        } catch (NoSuchElementException e) {
        }
        if (Debug_Level < 4) {
            cat.debug(databaseURLString);
        }
        try {
            Config.readConfigFile(ConfigFileName, Debug_Level);
        } catch (IOException e) {
            if (Debug_Level < 6) {
                cat.error("Failed to read JNDI Configuration file " + ConfigFileName + "' " + e);
            }
            System.exit(1);
        }
        reopen();
        if (Debug_Level < 1) {
            String suffix_DN = Config.getJNDINameSuffix();
            cat.debug("Entries under suffix " + suffix_DN);
            try {
                Vector sub_object_names = findSubObjectNames("", true);
                for (Enumeration e = sub_object_names.elements(); e.hasMoreElements(); ) {
                    String name = (String) e.nextElement();
                    cat.debug("\t" + name);
                }
            } catch (DBException e) {
                cat.error("Object not found.");
            }
        }
        UtilityEntryDN = Config.getUtilityEntryDN();
        Utility_Object = getUtilityObject(UtilityEntryDN);
    }

    public static gov.lanl.Database.DatabaseMgr open(ConfigProperties props) {
        String databaseURL = props.getProperty("databaseURL");
        dbMgr = new DatabaseMgr();
        dbMgr.init(databaseURL);
        return dbMgr;
    }

    public void reopen() {
        String suffix_DN = Config.getJNDINameSuffix();
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://" + DatabaseAddress + "/" + suffix_DN);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, Username);
        env.put(Context.SECURITY_CREDENTIALS, Password);
        try {
            dir_ctx = new InitialDirContext(env);
        } catch (NamingException e) {
            if (Debug_Level < 6) {
                cat.error("Failed to connect to JNDI Database. " + e);
            }
            System.exit(1);
        }
    }

    public void close() {
        try {
            dir_ctx.close();
        } catch (NamingException e) {
            if (Debug_Level < 6) {
                cat.error("Failed to disconnect from JNDI Database. " + e.toString());
            }
            System.exit(1);
        }
    }

    public static gov.lanl.Database.DatabaseMgr current() {
        return dbMgr;
    }

    public char getWildChar() {
        return '*';
    }

    public void txn_begin() throws DBException {
    }

    /**
     * Method declaration
     *
     *
     * @throws DBException
     *
     * @see
     */
    public void txn_commit() throws DBException {
    }

    public void txn_abort() {
    }

    public PersistentObject retrieveElement(Object obj, String elementName, String value) throws DBException {
        return retrieveElement(obj, elementName, value, DEEP);
    }

    public PersistentObject retrieveElement(Object obj, String elementName, String value, int deep) throws DBException {
        if ((obj == null) || (elementName == null) || (value == null)) {
            if (Debug_Level < 3) {
                cat.error("Error on method retrieveElement - input parameter is null");
            }
            throw new DBException();
        }
        String[] obj_attribute_names = ReflectionInformation.getObjectAttributeNames(obj);
        int num_attributes = obj_attribute_names.length;
        boolean found = false;
        for (int i = 0; i < num_attributes; i++) {
            if (elementName.equals(obj_attribute_names[i])) {
                found = true;
                break;
            }
        }
        if (!found) {
            if (Debug_Level < 3) {
                cat.error("Error on method retrieveElement - input parameter elementName is not valid");
            }
            throw new DBException();
        }
        String DistinguishedName = Config.getDistinguishedName(obj, elementName, value);
        if (DistinguishedName != null) {
            boolean deep_flag = (deep == DEEP);
            return getJNDIObject(DistinguishedName, deep_flag);
        } else {
            Vector idents = accessElements(obj, elementName, value, deep);
            if (idents.size() > 0) {
                return (PersistentObject) idents.firstElement();
            } else {
                return null;
            }
        }
    }

    public void updateElement(PersistentObject obj) throws DBException {
        updateElement(obj, DEEP);
    }

    public void updateElement(PersistentObject obj, int deep) throws DBException {
        if (obj == null) {
            if (Debug_Level < 3) {
                cat.error("Error on method updateElement - input parameter is null");
            }
            throw new DBException();
        }
        boolean deep_flag = (deep == DEEP);
        updateObject(obj, deep_flag);
    }

    public void deleteElement(PersistentObject obj) throws DBException {
        if (obj == null) {
            if (Debug_Level < 3) {
                cat.error("Error on method deleteElement - input parameter is null");
            }
            throw new DBException();
        }
        deleteObject(obj);
    }

    public void insertElement(PersistentObject obj, String name) throws DBException {
        if (obj == null) {
            if (Debug_Level < 3) {
                cat.error("Error on method insertElement - object parameter is null");
            }
            throw new DBException();
        }
        addObject(obj);
    }

    public Vector accessElements(PersistentObject obj, String element, String element_value) throws DBException {
        return accessElements(obj, element, element_value, SHALLOW);
    }

    /**
     * Obtain a Vector of elements from the Object obj already obtained (via	shallow	copy)
     * JNDI does not have any easy way to access a previously retrieved object,
     * so get the object again and then get the elements.
     * This will return a vector of all objects that have an attribute
     * whos name is element that has a value of value.
     * It will also get all their sub-objects
     *
     * @param obj		The class for the object to be retrieved
     * @param element	The name of the element to be matched
     * @param element_value		The value of the element for searching
     * if value == null, then get the specified element from the specified object
     * @param deep		Indicates if sub-objects are to be retrieved
     * @return			A Vector containing all objects found
     */
    private Vector accessElements(Object obj, String element, String element_value, int deep) throws DBException {
        if ((obj == null) || (element == null) || (element_value == null)) {
            if (Debug_Level < 3) {
                cat.error("Error on method accessElements - input parameter is null");
            }
            throw new DBException();
        }
        String attr_name = Config.translateAttributeName(obj.getClass().getName(), element);
        if (attr_name == null) {
            throw new DBException();
        }
        String search_filter = "(" + attr_name + "=" + element_value + ")";
        return retrieveElements(obj, search_filter, deep);
    }

    public Vector retrieveElements(Object obj, String[] elements, String[] name) throws DBException {
        if ((obj == null) || (elements == null) || (name == null)) {
            if (Debug_Level < 3) {
                cat.error("Error on method retrieveElements - input parameter is null");
            }
            throw new DBException();
        }
        if (elements.length != name.length) {
            throw new DBException();
        }
        String search_filter = "(&";
        int num_elements = elements.length;
        for (int i = 0; i < num_elements; i++) {
            String attr_name = Config.translateAttributeName(obj.getClass().getName(), elements[i]);
            if (attr_name == null) {
                throw new DBException();
            }
            search_filter += "(" + attr_name + "=" + name[i] + ")";
        }
        search_filter += ")";
        return retrieveElements(obj, search_filter, DEEP);
    }

    public Vector retrieveElements(Object obj, gov.lanl.Database.SearchFilter query, int deep) throws DBException {
        SearchFilter localquery = (SearchFilter) query;
        localquery.setObject(obj);
        String JNDISearchFilterString = localquery.toString();
        return retrieveElements(obj, JNDISearchFilterString, deep);
    }

    private Vector retrieveElements(Object obj, String search_filter, int deep) throws DBException {
        if ((obj == null) || (search_filter == null)) {
            if (Debug_Level < 3) {
                cat.error("Error on method retrieveElements - input parameter is null");
            }
            throw new DBException();
        }
        boolean deep_flag = (deep == DEEP);
        return findObjects(obj, search_filter, deep_flag);
    }

    public Vector retrieveElements(Object obj, String operator, String element, String[] values, int deep) throws DBException {
        if ((obj == null) || (operator == null) || (element == null) || (values == null)) {
            if (Debug_Level < 5) {
                cat.error("Error on method retrieveElements - input parameter is null");
            }
            throw new DBException();
        }
        String attr_name;
        attr_name = Config.translateAttributeName(obj.getClass().getName(), element);
        String search_filter;
        if ("AND".equals(operator)) {
            search_filter = "(&";
        } else {
            search_filter = "(|";
        }
        int num_values = values.length;
        for (int i = 0; i < num_values; i++) {
            search_filter += "(" + attr_name + "=" + values[i] + ")";
        }
        search_filter += ")";
        return retrieveElements(obj, search_filter, deep);
    }

    public Vector retrieveElements(Object obj, String operator, String element, int[] values, int deep) throws DBException {
        if ((obj == null) || (operator == null) || (element == null) || (values == null)) {
            if (Debug_Level < 3) {
                cat.error("Error on method retrieveElements - input parameter is null");
            }
            throw new DBException();
        }
        int num_values = values.length;
        String[] string_values = new String[num_values];
        for (int i = 0; i < num_values; i++) {
            string_values[i] = "" + values[i];
        }
        return retrieveElements(obj, operator, element, string_values, deep);
    }

    public long getNextSeq(Class clazz, String fieldName) {
        long CurrentSequenceNumber = readSequenceNumber();
        CurrentSequenceNumber++;
        try {
            writeSequenceNumber(CurrentSequenceNumber);
        } catch (DBException e) {
            System.exit(1);
        }
        return CurrentSequenceNumber;
    }

    private Vector findObjects(Object obj, String search_filter, boolean deep_flag) throws DBException {
        Vector Result = new Vector();
        SearchControls ctls = new SearchControls();
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        try {
            NamingEnumeration res = dir_ctx.search("", search_filter, ctls);
            while (res.hasMoreElements()) {
                try {
                    SearchResult searchitem = (SearchResult) res.next();
                    Attributes attrs = searchitem.getAttributes();
                    DirContextObject dirctx_obj = new DirContextObject(attrs);
                    Object found_obj = dirctx_obj.getObject();
                    Result.addElement(found_obj);
                    String DistinguishedName = searchitem.getName();
                    if (deep_flag) {
                        getJNDISubObjects(DistinguishedName, found_obj);
                    }
                } catch (NamingException e) {
                    if (Debug_Level < 4) {
                        cat.error(e.toString());
                    }
                }
            }
        } catch (NamingException e) {
            if (Debug_Level < 4) {
                cat.error(e.toString());
            }
        }
        return Result;
    }

    private Vector findSubObjects(String Dn, boolean all_levels) throws DBException {
        Vector Result = new Vector();
        SearchControls ctls = new SearchControls();
        if (all_levels) {
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        } else {
            ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        }
        try {
            NamingEnumeration res = dir_ctx.search(Dn, "(objectclass=*)", ctls);
            while (res.hasMoreElements()) {
                try {
                    SearchResult searchitem = (SearchResult) res.next();
                    Attributes attrs = searchitem.getAttributes();
                    DirContextObject dirctx_obj = new DirContextObject(attrs);
                    Object found_obj = dirctx_obj.getObject();
                    Result.addElement(found_obj);
                } catch (NamingException e) {
                    if (Debug_Level < 4) {
                        cat.error(e.toString());
                    }
                }
            }
        } catch (NamingException e) {
            if (Debug_Level < 5) {
                cat.error(e.toString());
            }
            throw new DBException();
        }
        return Result;
    }

    private Vector findSubObjectNames(String Dn, boolean all_levels) throws DBException {
        Vector Result = new Vector();
        SearchControls ctls = new SearchControls();
        if (all_levels) {
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        } else {
            ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        }
        ctls.setReturningAttributes(null);
        try {
            NamingEnumeration res = dir_ctx.search(Dn, "(objectclass=*)", ctls);
            while (res.hasMoreElements()) {
                try {
                    SearchResult searchitem = (SearchResult) res.next();
                    String DistinguishedName = searchitem.getName();
                    Result.addElement(DistinguishedName);
                } catch (NamingException e) {
                    if (Debug_Level < 4) {
                        cat.error(e.toString());
                    }
                }
            }
        } catch (NamingException e) {
            if (Debug_Level < 5) {
                cat.error(e.toString());
            }
            throw new DBException();
        }
        return Result;
    }

    private void addObject(Object obj) throws DBException {
        DirContextObject dirct_obj = new DirContextObject(obj);
        String dn = Config.getDistinguishedName(obj);
        try {
            dir_ctx.bind(dn, dirct_obj);
        } catch (NamingException e) {
            if (Debug_Level < 5) {
                cat.error("entry " + dn + " not added. Reason: " + e.toString());
            }
            throw new DBException();
        }
        Vector sub_objects = getClassSubObjects(obj);
        for (Enumeration en = sub_objects.elements(); en.hasMoreElements(); ) {
            Object sub_obj = en.nextElement();
            addObject(sub_obj);
        }
    }

    private PersistentObject getJNDIObject(String DName, boolean deep_flag) throws DBException {
        DirContextObject dirct_obj = null;
        try {
            Attributes attrs = dir_ctx.getAttributes(DName);
            dirct_obj = new DirContextObject(attrs);
        } catch (NamingException e) {
            if (Debug_Level < 4) {
                cat.error("Failed to retrieve object " + DName + " in database. " + e.toString());
            }
            return null;
        } catch (ClassCastException e) {
            if (Debug_Level < 5) {
                cat.error("Failed to cast object " + DName + " to DirContextObject. " + e.toString());
            }
            throw new DBException();
        }
        Object ret_obj = dirct_obj.getObject();
        if (deep_flag) {
            getJNDISubObjects(DName, ret_obj);
        }
        return (PersistentObject) ret_obj;
    }

    private void getJNDISubObjects(String DName, Object obj) throws DBException {
        Vector sub_objects_vector = findSubObjects(DName, false);
        if ((sub_objects_vector != null) && (sub_objects_vector.size() > 0)) {
            int num_sub_objects = sub_objects_vector.size();
            ObjectFactory objFactory = Config.getObjectFactory();
            Object[] sub_objects = new Object[num_sub_objects];
            sub_objects_vector.copyInto(sub_objects);
            Object[] sub_object_tree = objFactory.toTree(sub_objects);
            String obj_field_name = objFactory.getObjectField(obj, sub_object_tree[0]);
            addObjectTree(obj, obj_field_name, sub_object_tree);
        }
    }

    private Vector getClassSubObjects(Object obj) throws DBException {
        Vector Result = new Vector();
        Vector obj_containers = Config.getContainers(obj);
        for (Enumeration en = obj_containers.elements(); en.hasMoreElements(); ) {
            String cinfo = (String) en.nextElement();
            StringTokenizer st = new StringTokenizer(cinfo, ";");
            String container_type = st.nextToken();
            String container_attr = st.nextToken();
            Field attr_field = ReflectionInformation.getObjectAttributeField(obj, container_attr);
            String dn = Config.getDistinguishedName(obj);
            if ("java.util.Vector".equals(container_type)) {
                Vector container;
                try {
                    container = (Vector) attr_field.get(obj);
                } catch (IllegalAccessException e) {
                    if (Debug_Level < 5) {
                        cat.error("Error: entry " + dn + " could not add contained objects. " + e.toString());
                    }
                    throw new DBException();
                }
                if (container != null) {
                    for (Enumeration enum1 = container.elements(); enum1.hasMoreElements(); ) {
                        Object sub_obj = (Object) enum1.nextElement();
                        Result.addElement(sub_obj);
                    }
                }
            } else {
                if (Debug_Level < 5) {
                    cat.error("entry " + dn + " has a container that is not a Vector.");
                }
                throw new DBException();
            }
        }
        return Result;
    }

    private void updateObject(Object obj, boolean deep_flag) throws DBException {
        String dn = Config.getDistinguishedName(obj);
        DirContextObject dirct_obj = new DirContextObject(obj);
        boolean sub_objects_present = true;
        try {
            Vector sub_objects = findSubObjectNames(dn, false);
            if (sub_objects.size() == 0) {
                sub_objects_present = false;
            }
        } catch (DBException e) {
            sub_objects_present = false;
        }
        if (!sub_objects_present) {
            try {
                dir_ctx.rebind(dn, dirct_obj);
            } catch (NamingException e) {
                if (Debug_Level < 5) {
                    cat.error("entry " + dn + " not replaced. Reason: " + e.toString());
                }
                throw new DBException();
            }
        } else {
            String obj_type = Config.getJNDIObjectTypeAttributeName();
            String DN_attr_name = Config.getDNAttributeName(obj);
            Attributes attrs = null;
            try {
                attrs = dirct_obj.getAttributes("");
            } catch (NameNotFoundException e) {
                if (Debug_Level < 5) {
                    cat.error("Failed to modify object " + dn + " in database. " + e.toString());
                }
                throw new DBException();
            }
            Vector mod_vector = new Vector();
            for (Enumeration e = attrs.getAll(); e.hasMoreElements(); ) {
                Attribute attr = (Attribute) e.nextElement();
                String attr_name = attr.getID();
                if (("objectclass".equals(attr_name)) || (DN_attr_name.equals(attr_name)) || (obj_type.equals(attr_name))) {
                    continue;
                }
                mod_vector.addElement(new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr));
            }
            int num_mods = mod_vector.size();
            ModificationItem[] mods = new ModificationItem[num_mods];
            mod_vector.copyInto(mods);
            try {
                dir_ctx.modifyAttributes(dn, mods);
            } catch (NamingException e) {
                if (Debug_Level < 5) {
                    cat.error("Failed to modify object " + dn + " in database. " + e.toString());
                }
                throw new DBException();
            }
        }
        if (deep_flag) {
            Vector sub_objects = getClassSubObjects(obj);
            for (Enumeration en = sub_objects.elements(); en.hasMoreElements(); ) {
                Object sub_obj = en.nextElement();
                updateObject(sub_obj, deep_flag);
            }
        }
    }

    private void deleteObject(Object obj) throws DBException {
        String dn = Config.getDistinguishedName(obj);
        Vector sub_objects = findSubObjects(dn, false);
        for (Enumeration e = sub_objects.elements(); e.hasMoreElements(); ) {
            Object sub_obj = e.nextElement();
            deleteObject(sub_obj);
        }
        try {
            dir_ctx.unbind(dn);
        } catch (NamingException e) {
            if (Debug_Level < 5) {
                cat.error("Failed to delete object " + dn + " from database. " + e.toString());
            }
            throw new DBException();
        }
    }

    private long readSequenceNumber() {
        return Utility_Object.getSequenceNumber();
    }

    private void writeSequenceNumber(long seq_num) throws DBException {
        Utility_Object.setSequenceNumber(seq_num);
        setUtilityObject();
    }

    private UtilityObject getUtilityObject(String dn) {
        DirContextObject dirct_obj = null;
        try {
            Attributes attrs = dir_ctx.getAttributes(dn);
            dirct_obj = new DirContextObject(attrs);
        } catch (NamingException e) {
            if (Debug_Level < 6) {
                cat.error("Failed to read utility object " + UtilityEntryDN + " from database. " + e.toString());
            }
            System.exit(1);
        } catch (ClassCastException e) {
            if (Debug_Level < 6) {
                cat.error("Failed to cast utility object " + UtilityEntryDN + " to DirContextObject. " + e.toString());
            }
            System.exit(1);
        }
        Attributes attrs = null;
        try {
            attrs = dirct_obj.getAttributes("");
        } catch (NameNotFoundException e) {
            if (Debug_Level < 6) {
                cat.error("Failed to find sequence number in utility object " + UtilityEntryDN + ". " + e.toString());
            }
            System.exit(1);
        }
        UtilityObject found_obj = new UtilityObject();
        found_obj.setAttributes(attrs);
        return found_obj;
    }

    private void setUtilityObject() throws DBException {
        ModificationItem[] mods = new ModificationItem[1];
        Attribute seq_num_attr = Utility_Object.getSequenceNumberAttribute();
        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, seq_num_attr);
        try {
            dir_ctx.modifyAttributes(UtilityEntryDN, mods);
        } catch (NamingException e) {
            if (Debug_Level < 5) {
                cat.error("sequence number for " + UtilityEntryDN + " not modified. " + e.toString());
            }
            throw new DBException();
        }
    }

    private void newUtilityObject() throws DBException {
        Utility_Object = new UtilityObject();
        Utility_Object.setSequenceNumber(0);
        Attributes attrs = Utility_Object.getAttributes();
        DirContextObject dirct_obj = new DirContextObject(Utility_Object.getObjectType(), attrs);
        try {
            dir_ctx.bind(UtilityEntryDN, dirct_obj);
        } catch (NamingException e) {
            if (Debug_Level < 5) {
                cat.error("entry " + UtilityEntryDN + " not added. Reason: " + e.toString());
            }
            throw new DBException();
        }
    }

    private void addObjectTree(Object obj, String obj_field_name, Object[] sub_object_tree) {
        String obj_name = obj.getClass().getName();
        Field obj_field = null;
        try {
            obj_field = obj.getClass().getField(obj_field_name);
        } catch (NoSuchFieldException e) {
            cat.error("Object " + obj_name + " has no such field " + obj_field_name + ". " + e.toString());
            System.exit(1);
        }
        Class field_class = obj_field.getType();
        String field_type = field_class.getName();
        Vector sub_object_tree_vector = null;
        if ("java.util.Vector".equals(field_type)) {
            sub_object_tree_vector = new Vector();
            for (int i = 0; i < sub_object_tree.length; i++) {
                sub_object_tree_vector.addElement(sub_object_tree[i]);
            }
        }
        try {
            if ("java.util.Vector".equals(field_type)) {
                obj_field.set(obj, sub_object_tree_vector);
            } else {
                obj_field.set(obj, sub_object_tree);
            }
        } catch (IllegalArgumentException e) {
            cat.error("Illegal argument trying to write to field " + obj_field_name + " of object " + obj_name + ". " + e.toString());
            System.exit(1);
        } catch (IllegalAccessException e) {
            cat.error("Illegal access trying to write to field " + obj_field_name + " of object " + obj_name + ".  " + e.toString());
            System.exit(1);
        }
    }

    /**
     * Class declaration
     *
     *
     * @author <a href="mailto:Sascha@Koenig.net">Sascha A. Koenig<a>
     * @version $Revision: 3335 $ $Date: 2006-03-27 18:28:01 -0500 (Mon, 27 Mar 2006) $
     */
    class DirContextObject implements DirContext {

        private String type;

        private Attributes myAttrs;

        /**
         * Constructor declaration
         *
         *
         * @param obj
         *
         * @see
         */
        public DirContextObject(Object obj) {
            type = obj.getClass().getName();
            myAttrs = new BasicAttributes(true);
            Attribute oc = new BasicAttribute("objectclass");
            String ClassObjectClass = Config.getJNDIClassObjectClassName(obj);
            if (ClassObjectClass != null) {
                oc.add(ClassObjectClass);
            }
            oc.add(Config.getJNDIObjectClassName());
            oc.add("top");
            myAttrs.put(oc);
            String obj_type = Config.getJNDIObjectTypeAttributeName();
            Attribute ot = new BasicAttribute(obj_type);
            ot.add(type);
            myAttrs.put(ot);
            String[] ObjClassFields = ReflectionInformation.getObjectAttributeNames(obj);
            int num_fields = ObjClassFields.length;
            for (int i = 0; i < num_fields; i++) {
                Object field_value = Config.getObjectAttribute(obj, ObjClassFields[i]);
                if (field_value == null) {
                    continue;
                }
                String JNDI_attr_name = Config.translateAttributeName(type, ObjClassFields[i]);
                Attribute basic_attr = new BasicAttribute(JNDI_attr_name);
                if (ReflectionInformation.isObjectAttributeTypeArray(obj, ObjClassFields[i])) {
                    Field attr_field = ReflectionInformation.getObjectAttributeField(obj, ObjClassFields[i]);
                    byte[] barray = null;
                    try {
                        barray = (byte[]) attr_field.get(obj);
                    } catch (IllegalAccessException e) {
                        barray = new byte[] { (byte) 0 };
                    }
                    basic_attr.add(barray);
                } else {
                    basic_attr.add((String) field_value);
                }
                myAttrs.put(basic_attr);
            }
        }

        /**
         * Constructor declaration
         *
         *
         * @param obj_type
         * @param attrs
         *
         * @see
         */
        public DirContextObject(String obj_type, Attributes attrs) {
            myAttrs = attrs;
            type = obj_type;
        }

        /**
         * Constructor declaration
         *
         *
         * @param attrs
         *
         * @see
         */
        public DirContextObject(Attributes attrs) {
            myAttrs = attrs;
            String obj_type_name = Config.getJNDIObjectTypeAttributeName();
            Attribute ot = attrs.get(obj_type_name);
            try {
                type = (String) ot.get();
            } catch (NamingException e) {
                if (Debug_Level < 6) {
                    cat.error("Failure to find object type of new DirContextObject. " + e.toString());
                }
                System.exit(1);
            }
        }

        /**
         * Method declaration
         *
         *
         * @return
         *
         * @see
         */
        public String getNameInNamespace() {
            return "";
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @return
         *
         * @throws NameNotFoundException
         *
         * @see
         */
        public Attributes getAttributes(String name) throws NameNotFoundException {
            if (!name.equals("")) {
                throw new NameNotFoundException();
            }
            return (Attributes) myAttrs.clone();
        }

        /**
         * Method declaration
         *
         *
         * @return
         *
         * @see
         */
        public Object getObject() {
            Class obj_class = null;
            Object new_object = null;
            try {
                obj_class = Class.forName(type);
                new_object = obj_class.newInstance();
            } catch (ClassNotFoundException e) {
                if (Debug_Level < 6) {
                    cat.error("Class " + type + " not found.");
                }
                System.exit(1);
            } catch (InstantiationException e) {
                if (Debug_Level < 6) {
                    cat.error("Failed to create a new instance of class " + obj_class.getName() + ". " + e.toString());
                }
                System.exit(1);
            } catch (IllegalAccessException e) {
                if (Debug_Level < 6) {
                    cat.error("Failed to create a new instance of class " + obj_class.getName() + ". " + e.toString());
                }
                System.exit(1);
            }
            String[] ObjClassFields = ReflectionInformation.getObjectAttributeNames(new_object);
            int num_fields = ObjClassFields.length;
            for (int i = 0; i < num_fields; i++) {
                String JNDI_attr_name = Config.translateAttributeName(obj_class.getName(), ObjClassFields[i]);
                JNDI_attr_name = JNDI_attr_name.toLowerCase();
                Attribute attr = myAttrs.get(JNDI_attr_name);
                Object new_value = null;
                try {
                    if (attr != null) {
                        if (ReflectionInformation.isObjectAttributeTypeArray(new_object, ObjClassFields[i])) {
                            String temp_string = (String) attr.get();
                            byte[] temp_byte_array = temp_string.getBytes();
                            gov.lanl.Database.JNDI.Config.setObjectAttribute(new_object, ObjClassFields[i], temp_byte_array);
                            continue;
                        } else {
                            new_value = attr.get();
                        }
                    }
                } catch (NamingException e) {
                    if (Debug_Level < 6) {
                        cat.error("Failed to get value of attribute " + JNDI_attr_name + ". " + e.toString());
                    }
                    System.exit(1);
                }
                Config.setObjectAttribute(new_object, ObjClassFields[i], new_value);
            }
            return new_object;
        }

        /**
         * Method declaration
         *
         *
         * @return
         *
         * @see
         */
        public String toString() {
            return type;
        }

        /**
         * Method declaration
         *
         *
         * @param str
         * @param obj
         * @param attrs
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void bind(String str, Object obj, javax.naming.directory.Attributes attrs) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param obj
         * @param attrs
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void bind(javax.naming.Name name, Object obj, javax.naming.directory.Attributes attrs) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         * @param attrs
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.directory.DirContext createSubcontext(String str, javax.naming.directory.Attributes attrs) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param attrs
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.directory.DirContext createSubcontext(javax.naming.Name name, javax.naming.directory.Attributes attrs) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         * @param strarr
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.directory.Attributes getAttributes(String str, String[] strarr) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.directory.Attributes getAttributes(javax.naming.Name name) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param strarr
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.directory.Attributes getAttributes(javax.naming.Name name, String[] strarr) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.directory.DirContext getSchema(String str) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.directory.DirContext getSchema(javax.naming.Name name) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.directory.DirContext getSchemaClassDefinition(String str) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.directory.DirContext getSchemaClassDefinition(javax.naming.Name name) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         * @param num
         * @param attrs
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void modifyAttributes(String str, int num, javax.naming.directory.Attributes attrs) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         * @param moditems
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void modifyAttributes(String str, javax.naming.directory.ModificationItem[] moditems) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param num
         * @param attrs
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void modifyAttributes(javax.naming.Name name, int num, javax.naming.directory.Attributes attrs) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param moditems
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void modifyAttributes(javax.naming.Name name, javax.naming.directory.ModificationItem[] moditems) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         * @param obj
         * @param attrs
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void rebind(String str, Object obj, javax.naming.directory.Attributes attrs) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param obj
         * @param attrs
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void rebind(javax.naming.Name name, Object obj, javax.naming.directory.Attributes attrs) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str1
         * @param str2
         * @param srchctls
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration search(String str1, String str2, javax.naming.directory.SearchControls srchctls) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str1
         * @param str2
         * @param objarr
         * @param srchctls
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration search(String str1, String str2, Object[] objarr, javax.naming.directory.SearchControls srchctls) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         * @param attrs
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration search(String str, javax.naming.directory.Attributes attrs) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         * @param attrs
         * @param strarray
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration search(String str, javax.naming.directory.Attributes attrs, String[] strarray) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param str
         * @param srchctls
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration search(javax.naming.Name name, String str, javax.naming.directory.SearchControls srchctls) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param str
         * @param objarr
         * @param srchctls
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration search(javax.naming.Name name, String str, Object[] objarr, javax.naming.directory.SearchControls srchctls) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param attrs
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration search(javax.naming.Name name, javax.naming.directory.Attributes attrs) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param attrs
         * @param strarr
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration search(javax.naming.Name name, javax.naming.directory.Attributes attrs, String[] strarr) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         * @param obj
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public Object addToEnvironment(String str, Object obj) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         * @param obj
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void bind(String str, Object obj) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param obj
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void bind(javax.naming.Name name, Object obj) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void close() throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str1
         * @param str2
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public String composeName(String str1, String str2) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name1
         * @param name2
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.Name composeName(javax.naming.Name name1, javax.naming.Name name2) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.Context createSubcontext(String str) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.Context createSubcontext(javax.naming.Name name) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void destroySubcontext(String str) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void destroySubcontext(javax.naming.Name name) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public Hashtable getEnvironment() throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NameParser getNameParser(String str) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NameParser getNameParser(javax.naming.Name name) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration list(String str) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration list(javax.naming.Name name) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration listBindings(String str) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public javax.naming.NamingEnumeration listBindings(javax.naming.Name name) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public Object lookup(String str) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public Object lookup(javax.naming.Name name) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public Object lookupLink(String str) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public Object lookupLink(javax.naming.Name name) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         * @param obj
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void rebind(String str, Object obj) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         * @param obj
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void rebind(javax.naming.Name name, Object obj) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         *
         * @return
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public Object removeFromEnvironment(String str) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str1
         * @param str2
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void rename(String str1, String str2) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name1
         * @param name2
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void rename(javax.naming.Name name1, javax.naming.Name name2) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param str
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void unbind(String str) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }

        /**
         * Method declaration
         *
         *
         * @param name
         *
         * @throws OperationNotSupportedException
         *
         * @see
         */
        public void unbind(javax.naming.Name name) throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    /**
     * Class declaration
     *
     *
     * @author <a href="mailto:Sascha@Koenig.net">Sascha A. Koenig<a>
     * @version $Revision: 3335 $ $Date: 2006-03-27 18:28:01 -0500 (Mon, 27 Mar 2006) $
     */
    class UtilityObject {

        private long sequence_number;

        private final String SequenceNumberAttributeName = "sequencenumberentry";

        /**
         * Constructor declaration
         *
         *
         * @see
         */
        public UtilityObject() {
        }

        /**
         * Method declaration
         *
         *
         * @return
         *
         * @see
         */
        public String getObjectType() {
            return this.getClass().getName();
        }

        /**
         * Method declaration
         *
         *
         * @return
         *
         * @see
         */
        public Attributes getAttributes() {
            Attributes attrs = new BasicAttributes(true);
            Attribute oc = new BasicAttribute("objectclass");
            oc.add(Config.getJNDIUtilityObjectClassName());
            oc.add(Config.getJNDIObjectClassName());
            oc.add("top");
            attrs.put(oc);
            Attribute ot = new BasicAttribute("PIDSObjectType");
            ot.add(getObjectType());
            attrs.put(ot);
            Attribute osn = new BasicAttribute(SequenceNumberAttributeName);
            osn.add("" + sequence_number);
            attrs.put(osn);
            return attrs;
        }

        /**
         * Method declaration
         *
         *
         * @param new_attrs
         *
         * @see
         */
        public void setAttributes(Attributes new_attrs) {
            String seq_num_string = null;
            try {
                Attribute osn = new_attrs.get(SequenceNumberAttributeName);
                seq_num_string = (String) osn.get();
            } catch (NamingException e) {
                cat.error("Failure to find sequence number attribute for utility object. " + e.toString());
                System.exit(1);
            }
            if (seq_num_string == null) {
                cat.error("Sequence number string from attributes is null.");
                System.exit(1);
            }
            long seq_num = 0;
            try {
                seq_num = Long.parseLong(seq_num_string);
            } catch (NumberFormatException e) {
                cat.error("Sequence number string from attributes is not valid. " + e.toString());
                System.exit(1);
            }
            setSequenceNumber(seq_num);
        }

        /**
         * Method declaration
         *
         *
         * @return
         *
         * @see
         */
        public Attribute getSequenceNumberAttribute() {
            Attribute osn = new BasicAttribute(SequenceNumberAttributeName);
            osn.add("" + sequence_number);
            return osn;
        }

        /**
         * Method declaration
         *
         *
         * @return
         *
         * @see
         */
        public long getSequenceNumber() {
            return sequence_number;
        }

        /**
         * Method declaration
         *
         *
         * @param new_num
         *
         * @see
         */
        public void setSequenceNumber(long new_num) {
            sequence_number = new_num;
        }
    }

    /**
     * Method declaration
     *
     *
     * @param objectPackage
     *
     * @see
     */
    public void setObjectFactory(String objectPackage) {
    }

    /**
     * this method returns an instance of the specified
     * PersistentObjectFactory
     */
    public PersistentObjectFactory getObjectFactory() {
        return null;
    }
}
