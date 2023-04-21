package com.apelon.ontylogxchg;

import java.io.*;
import java.sql.*;
import java.util.*;
import com.apelon.common.sql.*;
import com.apelon.common.util.*;

/**
 * OntyImpDB handles the storing of concepts and new ontylog terms in
 * set of import tables.
 *
 * @see OntyImpSaxHandler
 * @see OntyImporter
 *
 * @version 1.0
 * @author Apelon, Inc. 2/30/00
 */
public class OntyImpDB implements OntyImpConstants {

    public static boolean VALIDATE_PROPERTY_VALUES = false;

    public static boolean VALIDATE_QUALIFIER_VALUES = false;

    private static String terms_ref_by = REF_BY_NAME;

    private Connection conn = null;

    private String current_concept_ref = null;

    private String current_namespace_ref = null;

    private String currentProperty = null;

    private String currentLocale = null;

    private String currentRole = null;

    private String currentAssociation = null;

    private boolean nextConceptPropId;

    private boolean nextConceptRoleId;

    private boolean nextConceptAssnId;

    private ArrayList regProps = new ArrayList();

    private ArrayList longProps = new ArrayList();

    private ArrayList realLongProps = new ArrayList();

    private ArrayList roleQuals = new ArrayList();

    private ArrayList assnQuals = new ArrayList();

    private ArrayList propQuals = new ArrayList();

    private HashMap picklist_items = new HashMap();

    private HashMap qual_picklist_items = new HashMap();

    private int cnt = 0;

    private int fImportErrorCount = 0;

    /**
   * These tables are truncated when we run the import.
   *
   * CMH: 1/11/00 Added stated_version so that is truncated on import
   */
    private String[] table_names = { "dl_import_namespace", "dl_import_kind", "dl_import_role_def", "dl_import_property_def", "dl_import_long_property", "dl_import_real_long_property", "dl_import_concept", "dl_import_defining_concept", "dl_import_defining_role", "dl_import_property", "dl_import_long_property", "dl_import_real_long_property", "dl_import_errors", "dl_kind_errors", "dl_import_locale_def", "dl_import_loc_property", "dl_import_property_picklist", "import_content_license", "stated_version" };

    private String[] part_table_names = { "dl_import_concept", "dl_import_defining_concept", "dl_import_defining_role", "dl_import_property", "dl_import_long_property", "dl_import_real_long_property", "dl_import_loc_property", "import_content_license" };

    private PreparedStatement concept_stmt = null;

    private PreparedStatement defcon_stmt = null;

    private PreparedStatement defrole_stmt = null;

    private PreparedStatement prop_stmt = null;

    private PreparedStatement long_prop_stmt = null;

    private PreparedStatement real_long_prop_stmt = null;

    private PreparedStatement loc_prop_stmt = null;

    private Statement stmt = null;

    private int conceptPropId = 0;

    private int conceptLocPropId = 0;

    private int conceptLongPropId = 0;

    private int conceptRealLongPropId = 0;

    private int conceptSynonymPropId = 0;

    private int conceptAssnId = 0;

    private int conceptRoleId = 0;

    private PreparedStatement qual_loc_prop_stmt;

    private PreparedStatement qual_real_long_prop_stmt;

    private PreparedStatement qual_prop_stmt;

    private PreparedStatement qual_long_prop_stmt;

    private PreparedStatement qual_assn_stmt;

    private PreparedStatement qual_role_stmt;

    private PreparedStatement assn_stmt;

    private void debugOut(String s) {
    }

    private boolean isRoleQualifer(String s) {
        if (roleQuals.size() > 0) {
            return roleQuals.contains(s);
        }
        return false;
    }

    private boolean isAssnQualifer(String s) {
        if (assnQuals.size() > 0) {
            return assnQuals.contains(s);
        }
        return false;
    }

    private boolean isPropQualifer(String s) {
        if (propQuals.size() > 0) {
            return propQuals.contains(s);
        }
        return false;
    }

    private boolean isRegProp(String s) {
        if (regProps.size() > 0) {
            return regProps.contains(s);
        }
        return false;
    }

    private boolean isLongProp(String s) {
        if (longProps.size() > 0) {
            return longProps.contains(s);
        }
        return false;
    }

    private boolean isRealLongProp(String s) {
        if (realLongProps.size() > 0) {
            return realLongProps.contains(s);
        }
        return false;
    }

    private void printStrArray(String[] sa) {
        for (int i = 0; i < sa.length; i++) {
            debugOut("array element: " + sa[i]);
        }
    }

    public static void termsReferencedBy(String s) {
        terms_ref_by = s;
    }

    public static String getTermsReferencedBy() {
        return terms_ref_by;
    }

    public void setCurrentConcept(String sa) {
        current_concept_ref = sa;
    }

    public void setCurrentNamespace(String sa) {
        current_namespace_ref = sa;
    }

    public void setCurrentProperty(String currentProperty) {
        this.currentProperty = currentProperty;
        this.nextConceptPropId = (currentProperty == null ? false : true);
    }

    public void setCurrentLocale(String currentLocale) {
        this.currentLocale = currentLocale;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
        this.nextConceptRoleId = (currentRole == null ? false : true);
    }

    public void setCurrentAssociation(String currentAssociation) {
        this.currentAssociation = currentAssociation;
        this.nextConceptAssnId = (currentAssociation == null ? false : true);
    }

    public void preloadPickListRestrictions() throws SQLException {
        String name = null;
        String code = null;
        String id = null;
        int gid = 0;
        PreparedStatement get_picklist = conn.prepareStatement("Select value from " + " dl_property_picklist where prop_gid = ?");
        ResultSet rs_values = null;
        Statement stmt = conn.createStatement();
        ResultSet rs_names = stmt.executeQuery("Select name, code, id, gid from dl_property_def");
        while (rs_names.next()) {
            name = rs_names.getString(1);
            code = rs_names.getString(2);
            id = Integer.toString(rs_names.getInt(3));
            gid = rs_names.getInt(4);
            get_picklist.setInt(1, gid);
            rs_values = get_picklist.executeQuery();
            while (rs_values.next()) {
                addPickListItemForProp(name, code, id, rs_values.getString(1));
            }
            rs_values.close();
        }
        rs_names.close();
        stmt.close();
        get_picklist.close();
    }

    public void preloadQualPickListRestrictions() throws SQLException {
        String name = null;
        String code = null;
        String id = null;
        int gid = 0;
        PreparedStatement get_picklist = conn.prepareStatement("Select value from " + " dl_qualifier_picklist where qual_gid = ?");
        ResultSet rs_values = null;
        Statement stmt = conn.createStatement();
        ResultSet rs_names = stmt.executeQuery("Select name, code, id, gid from dl_qualifier_def");
        while (rs_names.next()) {
            name = rs_names.getString(1);
            code = rs_names.getString(2);
            id = Integer.toString(rs_names.getInt(3));
            gid = rs_names.getInt(4);
            get_picklist.setInt(1, gid);
            rs_values = get_picklist.executeQuery();
            while (rs_values.next()) {
                addPickListItemForQual(name, code, id, rs_values.getString(1));
            }
            rs_values.close();
        }
        rs_names.close();
        stmt.close();
        get_picklist.close();
    }

    public void addPickListItemForProp(String propname, String code, String id, String value) {
        String key = null;
        if (terms_ref_by.equals(REF_BY_NAME)) {
            key = propname;
        }
        if (terms_ref_by.equals(REF_BY_CODE)) {
            key = code;
        }
        if (terms_ref_by.equals(REF_BY_ID)) {
            key = id;
        }
        Cons tmp = (Cons) picklist_items.get(key);
        if (tmp == null) {
            tmp = new Cons(5);
        }
        tmp.push(value);
        picklist_items.put(key, tmp);
    }

    public void addPickListItemForQual(String qualname, String code, String id, String value) {
        String key = null;
        if (terms_ref_by.equals(REF_BY_NAME)) {
            key = qualname;
        }
        if (terms_ref_by.equals(REF_BY_CODE)) {
            key = code;
        }
        if (terms_ref_by.equals(REF_BY_ID)) {
            key = id;
        }
        Cons tmp = (Cons) qual_picklist_items.get(key);
        if (tmp == null) {
            tmp = new Cons(5);
        }
        tmp.push(value);
        qual_picklist_items.put(key, tmp);
    }

    private boolean isValidatePropertyValue(String propname, String value) {
        Cons tmp = (Cons) picklist_items.get(propname);
        if (tmp == null) {
            return true;
        } else {
            for (int k = 0; k < tmp.size(); k++) {
                if (((String) tmp.at(k)).equalsIgnoreCase(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValidateQualifierValue(String qualname, String value) {
        Cons tmp = (Cons) qual_picklist_items.get(qualname);
        if (tmp == null) {
            return true;
        } else {
            for (int k = 0; k < tmp.size(); k++) {
                if (((String) tmp.at(k)).equalsIgnoreCase(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * Convenience method for parsing integers from strings. If there is
   *  a failure, log it in the import table so we can retrieve it later.
   *  If there is a failure, we can't enter the build process, so just toss
   *  a -1 in there
   */
    protected int parseInt(String toParse, String objectType, String objectName) throws SQLException {
        try {
            return Integer.parseInt(toParse);
        } catch (Exception ex) {
            fImportErrorCount++;
            insertImportError("INVALID_ID", "Invalid id found for " + objectType + "(" + objectName + ") : " + toParse);
            return -1;
        }
    }

    /**
   * This method is used for importing content only. Before we store any data into dl_property_str,
   * dl_long_property_str, or dl_real_long_property_str, we need to check if the property is in
   * dl_property_def, dl_long_property_def, or dl_real_long_property_def table. This method stores
   * all properties into corresponding Cons(regProps, longProps, realLongProps). Then we could
   * call isRegProp(String s), isLongProp(String s) or isRealLongProp(String s) to check the property.
   *
   */
    protected void storeCons() throws SQLException {
        String query = "";
        int rng_gid = -1;
        String props_ref = "";
        if (terms_ref_by.equals(REF_BY_NAME)) {
            query += " SELECT name, rng_gid ";
        }
        if (terms_ref_by.equals(REF_BY_CODE)) {
            query += " SELECT code, rng_gid ";
        }
        if (terms_ref_by.equals(REF_BY_ID)) {
            query += " SELECT id, rng_gid ";
        }
        query += " FROM dl_property_def ";
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(query);
        while (res.next()) {
            props_ref = res.getString(1);
            rng_gid = Integer.parseInt(res.getString(2));
            if (rng_gid == 0) {
                regProps.add(props_ref);
            } else if (rng_gid == 1) {
                longProps.add(props_ref);
            } else if (rng_gid == 2) {
                realLongProps.add(props_ref);
            }
        }
        res.close();
        stmt.close();
        query = "";
        String qualType = "";
        String qualRef = "";
        if (terms_ref_by.equals(REF_BY_NAME)) {
            query += " SELECT name, qualifier_type ";
        }
        if (terms_ref_by.equals(REF_BY_CODE)) {
            query += " SELECT code, qualifier_type ";
        }
        if (terms_ref_by.equals(REF_BY_ID)) {
            query += " SELECT id, qualifier_type ";
        }
        query += " FROM dl_qualifier_def ";
        stmt = conn.createStatement();
        res = stmt.executeQuery(query);
        while (res.next()) {
            qualRef = res.getString(1);
            qualType = res.getString(2);
            if (qualType.equals("CP")) {
                propQuals.add(qualRef);
            } else if (qualType.equals("CA")) {
                assnQuals.add(qualRef);
            } else if (qualType.equals("CR")) {
                roleQuals.add(qualRef);
            }
        }
        res.close();
        stmt.close();
    }

    public void storeNamespace(String[] sa) throws SQLException {
        String q = "INSERT INTO dl_import_namespace " + "(id, code, name, id_type) " + " VALUES ( ?, ?, ?, ? )";
        PreparedStatement nsp_stmt = conn.prepareStatement(q);
        nsp_stmt.setInt(1, parseInt(sa[IDX_DEF_ID], "NAMESPACEDEF", sa[IDX_DEF_NAME]));
        nsp_stmt.setString(2, sa[IDX_DEF_CODE]);
        nsp_stmt.setString(3, sa[IDX_DEF_NAME]);
        nsp_stmt.setString(4, terms_ref_by);
        nsp_stmt.executeUpdate();
        conn.commit();
        nsp_stmt.close();
    }

    public void storeKind(String[] sa) throws SQLException {
        String q = "INSERT INTO dl_import_kind " + "(id, code, name, namespace_ref, reference) " + "VALUES ( ?, ?, ?, ? , ?) ";
        PreparedStatement kdef_stmt = conn.prepareStatement(q);
        kdef_stmt.setInt(1, parseInt(sa[IDX_DEF_ID], "KINDDEF", sa[IDX_DEF_NAME]));
        kdef_stmt.setString(2, sa[IDX_DEF_CODE]);
        kdef_stmt.setString(3, sa[IDX_DEF_NAME]);
        kdef_stmt.setString(4, sa[IDX_DEF_NAMESPACE]);
        kdef_stmt.setString(5, sa[IDX_DEF_REFERENCE]);
        kdef_stmt.executeUpdate();
        conn.commit();
        kdef_stmt.close();
    }

    public void storeRoleDef(String[] sa) throws SQLException {
        String q = "INSERT INTO dl_import_role_def " + "(id, code, name, namespace_ref, domain_ref, range_ref, right_iden_ref, parent_ref) " + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ? )";
        PreparedStatement rdef_stmt = conn.prepareStatement(q);
        rdef_stmt.setInt(1, parseInt(sa[IDX_DEF_ID], "ROLEDEF", sa[IDX_DEF_NAME]));
        rdef_stmt.setString(2, sa[IDX_DEF_CODE]);
        rdef_stmt.setString(3, sa[IDX_DEF_NAME]);
        rdef_stmt.setString(4, sa[IDX_DEF_NAMESPACE]);
        rdef_stmt.setString(5, sa[IDX_DEF_DOMAIN]);
        rdef_stmt.setString(6, sa[IDX_DEF_RANGE]);
        rdef_stmt.setString(7, sa[IDX_DEF_RIGHTID]);
        rdef_stmt.setString(8, sa[IDX_DEF_PARENT_ROLE]);
        rdef_stmt.executeUpdate();
        conn.commit();
        rdef_stmt.close();
    }

    public void storeContentLicense(int id, String license) throws SQLException {
        String q = "INSERT INTO import_content_license (namespace_id, license_text) values (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(q);
        stmt.setInt(1, id);
        byte[] bytes = license.getBytes();
        if (bytes.length > 2000) {
            stmt.setBinaryStream(2, new BufferedInputStream(new ByteArrayInputStream(bytes), bytes.length), bytes.length);
        } else {
            stmt.setBytes(2, bytes);
        }
        stmt.executeUpdate();
        stmt.close();
    }

    private void storePickListForProp(String propname, String code, String id) throws SQLException {
        String key = null;
        if (terms_ref_by.equals(REF_BY_NAME)) {
            key = propname;
        }
        if (terms_ref_by.equals(REF_BY_CODE)) {
            key = code;
        }
        if (terms_ref_by.equals(REF_BY_ID)) {
            key = id;
        }
        int theid = Integer.parseInt(id);
        Cons tmp = (Cons) picklist_items.get(key);
        if (tmp == null) {
            return;
        }
        String value = null;
        String q = "INSERT INTO dl_import_property_picklist " + "(prop_gid,value) VALUES ( ?, ? )";
        PreparedStatement plist_stmt = conn.prepareStatement(q);
        plist_stmt.setInt(1, theid);
        for (int k = 0; k < tmp.size(); k++) {
            value = (String) tmp.at(k);
            plist_stmt.setString(2, value);
            plist_stmt.executeUpdate();
        }
        plist_stmt.close();
    }

    private void storePickListForQual(String qualname, String code, String id) throws SQLException {
        String key = null;
        if (terms_ref_by.equals(REF_BY_NAME)) {
            key = qualname;
        }
        if (terms_ref_by.equals(REF_BY_CODE)) {
            key = code;
        }
        if (terms_ref_by.equals(REF_BY_ID)) {
            key = id;
        }
        int theid = Integer.parseInt(id);
        Cons tmp = (Cons) qual_picklist_items.get(key);
        if (tmp == null) {
            return;
        }
        String value = null;
        String q = "INSERT INTO dl_import_qualifier_picklist " + "(qual_gid,value) VALUES ( ?, ? )";
        PreparedStatement plist_stmt = conn.prepareStatement(q);
        plist_stmt.setInt(1, theid);
        for (int k = 0; k < tmp.size(); k++) {
            value = (String) tmp.at(k);
            plist_stmt.setString(2, value);
            plist_stmt.executeUpdate();
        }
        plist_stmt.close();
    }

    public void storePropertyDef(String[] sa) throws SQLException {
        storePickListForProp(sa[IDX_DEF_NAME], sa[IDX_DEF_CODE], sa[IDX_DEF_ID]);
        String q = "INSERT INTO dl_import_property_def " + "(id, code, name, namespace_ref, range_ref, localized, contains_index) " + "VALUES ( ?, ?, ?, ?, ?, ?, ? )";
        PreparedStatement pdef_stmt = conn.prepareStatement(q);
        pdef_stmt.setInt(1, parseInt(sa[IDX_DEF_ID], "PROPERTYDEF", sa[IDX_DEF_NAME]));
        pdef_stmt.setString(2, sa[IDX_DEF_CODE]);
        pdef_stmt.setString(3, sa[IDX_DEF_NAME].trim());
        pdef_stmt.setString(4, sa[IDX_DEF_NAMESPACE]);
        pdef_stmt.setString(5, sa[IDX_DEF_RANGE]);
        pdef_stmt.setString(6, sa[IDX_DEF_LOCALIZED]);
        pdef_stmt.setString(7, sa[IDX_DEF_CONTAINS_INDEX]);
        pdef_stmt.executeUpdate();
        String props_ref = "";
        if (terms_ref_by.equals(REF_BY_NAME)) {
            props_ref = sa[IDX_DEF_NAME];
        }
        if (terms_ref_by.equals(REF_BY_CODE)) {
            props_ref = sa[IDX_DEF_CODE];
        }
        if (terms_ref_by.equals(REF_BY_ID)) {
            props_ref = sa[IDX_DEF_ID];
        }
        if (sa[IDX_DEF_RANGE].equals("string")) {
            debugOut("we have a reg prop");
            regProps.add(props_ref);
        } else if (sa[IDX_DEF_RANGE].equals("long_string")) {
            debugOut("we have a long prop");
            longProps.add(props_ref);
        } else if (sa[IDX_DEF_RANGE].equals("real_long_string")) {
            realLongProps.add(props_ref);
        }
        conn.commit();
        pdef_stmt.close();
    }

    public void storeLocaleDef(String[] sa) throws SQLException {
        String q = " INSERT INTO dl_import_locale_def " + " (id, code, name, super_locale, namespace_ref) " + " VALUES (?, ?, ?, ?, ? ) ";
        PreparedStatement ldef_stmt = conn.prepareStatement(q);
        ldef_stmt.setInt(1, parseInt(sa[IDX_DEF_ID], "LOCALEDEF", sa[IDX_DEF_NAME]));
        ldef_stmt.setString(2, sa[IDX_DEF_CODE]);
        ldef_stmt.setString(3, sa[IDX_DEF_NAME]);
        ldef_stmt.setString(4, sa[IDX_DEF_SUPER]);
        ldef_stmt.setString(5, sa[IDX_DEF_NAMESPACE]);
        ldef_stmt.executeUpdate();
        conn.commit();
        ldef_stmt.close();
    }

    public void storeAssociationDef(String[] sa) throws SQLException {
        debugOut("storing assn def " + sa[IDX_DEF_NAME]);
        String q = " INSERT INTO dl_import_association_def " + " (id, code, name, namespace_ref, inverse_name, displayable) " + " VALUES (?, ?, ?, ?, ?, ? ) ";
        PreparedStatement ldef_stmt = conn.prepareStatement(q);
        ldef_stmt.setInt(1, parseInt(sa[IDX_DEF_ID], "ASSOCIATIONDEF", sa[IDX_DEF_NAME]));
        ldef_stmt.setString(2, sa[IDX_DEF_CODE]);
        ldef_stmt.setString(3, sa[IDX_DEF_NAME]);
        ldef_stmt.setString(4, sa[IDX_DEF_NAMESPACE]);
        if (sa[IDX_DEF_INVERSE_NAME] == null || sa[IDX_DEF_INVERSE_NAME].length() == 0) {
            ldef_stmt.setNull(5, Types.VARCHAR);
        } else {
            ldef_stmt.setString(5, sa[IDX_DEF_INVERSE_NAME]);
        }
        if (sa[IDX_DEF_DISPLAYABLE] == null || sa[IDX_DEF_DISPLAYABLE].length() == 0) {
            ldef_stmt.setString(6, "F");
        } else {
            ldef_stmt.setString(6, sa[IDX_DEF_DISPLAYABLE]);
        }
        ldef_stmt.executeUpdate();
        conn.commit();
        ldef_stmt.close();
        debugOut("assn def stored");
    }

    public void storeQualifierDef(String[] sa) throws SQLException {
        debugOut("storing qual def " + sa[IDX_DEF_NAME]);
        storePickListForQual(sa[IDX_DEF_NAME], sa[IDX_DEF_CODE], sa[IDX_DEF_ID]);
        String q = " INSERT INTO dl_import_qualifier_def " + " (id, code, name, namespace_ref, qualifier_type) " + " VALUES (?, ?, ?, ?, ? ) ";
        PreparedStatement ldef_stmt = conn.prepareStatement(q);
        ldef_stmt.setInt(1, parseInt(sa[IDX_DEF_ID], "QUALIFIERDEF", sa[IDX_DEF_NAME]));
        ldef_stmt.setString(2, sa[IDX_DEF_CODE]);
        ldef_stmt.setString(3, sa[IDX_DEF_NAME]);
        ldef_stmt.setString(4, sa[IDX_DEF_NAMESPACE]);
        ldef_stmt.setString(5, sa[IDX_DEF_QUALIFIER_TYPE]);
        ldef_stmt.executeUpdate();
        String qualRef = "";
        if (terms_ref_by.equals(REF_BY_NAME)) {
            qualRef = sa[IDX_DEF_NAME];
        } else if (terms_ref_by.equals(REF_BY_CODE)) {
            qualRef = sa[IDX_DEF_CODE];
        } else if (terms_ref_by.equals(REF_BY_ID)) {
            qualRef = sa[IDX_DEF_ID];
        }
        if (sa[IDX_DEF_QUALIFIER_TYPE].equals("CR")) {
            roleQuals.add(qualRef);
        } else if (sa[IDX_DEF_QUALIFIER_TYPE].equals("CA")) {
            assnQuals.add(qualRef);
        } else if (sa[IDX_DEF_QUALIFIER_TYPE].equals("CP")) {
            propQuals.add(qualRef);
        }
        conn.commit();
        ldef_stmt.close();
        debugOut("stored qual def");
    }

    public void storeConcept(String[] sa) throws SQLException {
        if (terms_ref_by.equals(REF_BY_NAME)) {
            current_concept_ref = sa[IDX_DEF_NAME];
        }
        if (terms_ref_by.equals(REF_BY_CODE)) {
            current_concept_ref = sa[IDX_DEF_CODE];
        }
        if (terms_ref_by.equals(REF_BY_ID)) {
            current_concept_ref = sa[IDX_DEF_ID];
        }
        current_namespace_ref = sa[IDX_DEF_NAMESPACE];
        debugOut("storing concept " + current_concept_ref);
        concept_stmt.setInt(1, parseInt(sa[IDX_DEF_ID], "CONCEPTDEF", sa[IDX_DEF_NAME]));
        concept_stmt.setString(2, sa[IDX_DEF_CODE]);
        concept_stmt.setString(3, sa[IDX_DEF_NAME]);
        concept_stmt.setString(4, sa[IDX_DEF_NAMESPACE]);
        concept_stmt.setString(5, sa[IDX_DEF_KIND]);
        concept_stmt.setString(6, sa[IDX_DEF_PRIMITIVE]);
        concept_stmt.executeQuery();
        if ((++cnt % 500) == 0) {
            conn.commit();
        }
        debugOut("concept stored");
    }

    public void storeDefCon(String sa) throws SQLException {
        debugOut("storing def con " + sa + " for " + current_concept_ref);
        defcon_stmt.setString(1, current_concept_ref);
        defcon_stmt.setString(2, sa);
        defcon_stmt.setString(3, current_namespace_ref);
        defcon_stmt.executeQuery();
        debugOut("def con stored for " + current_concept_ref);
    }

    public void storeDefRole(String[] sa) throws SQLException {
        debugOut("storing def role " + sa[IDX_ROLE_NAME] + " for " + current_concept_ref);
        defrole_stmt.setInt(1, getConceptRoleId());
        defrole_stmt.setString(2, current_concept_ref);
        defrole_stmt.setString(3, sa[IDX_ROLE_NAME]);
        defrole_stmt.setString(4, sa[IDX_ROLE_MODIFIER]);
        defrole_stmt.setString(5, sa[IDX_ROLE_VALUE]);
        if (sa[IDX_ROLE_GROUP] != null) {
            defrole_stmt.setInt(6, parseInt(sa[IDX_ROLE_GROUP], "DEFININGROLE", "concept: " + current_concept_ref + " role: " + sa[IDX_ROLE_NAME]));
        } else {
            defrole_stmt.setInt(6, 0);
        }
        defrole_stmt.setString(7, current_namespace_ref);
        defrole_stmt.executeQuery();
        debugOut("def role stored for " + current_concept_ref);
    }

    public void storeAssociation(String[] sa) throws SQLException {
        debugOut("storing assn " + sa[IDX_ASSN_NAME] + " for " + current_concept_ref);
        assn_stmt.setInt(1, getConceptAssnId());
        assn_stmt.setString(2, current_concept_ref);
        assn_stmt.setString(3, sa[IDX_ASSN_NAME]);
        assn_stmt.setString(4, sa[IDX_ASSN_VALUE]);
        assn_stmt.setString(5, current_namespace_ref);
        assn_stmt.executeQuery();
        debugOut("assn stored for " + current_concept_ref);
    }

    public void storeProp(String[] sa) throws SQLException {
        if (VALIDATE_PROPERTY_VALUES) {
            if (!isValidatePropertyValue(sa[IDX_PROP_NAME], sa[IDX_PROP_VALUE])) {
                throw new SQLException(sa[IDX_PROP_VALUE] + " is not a valid value for property: " + sa[IDX_PROP_NAME]);
            }
        }
        String p_name = sa[IDX_PROP_NAME];
        debugOut("storing property " + p_name + " for " + current_concept_ref);
        if (isRegProp(p_name)) {
            if (sa[IDX_PROP_VALUE].trim().length() > 255) {
                throw new SQLException("Property value : [" + sa[IDX_PROP_VALUE].substring(0, 20) + "...]" + " is too long for property: " + sa[IDX_PROP_NAME] + "; CONCEPTDEF - " + current_concept_ref);
            }
            if (sa[1] != null) {
                loc_prop_stmt.setInt(1, getConceptLocPropId());
                loc_prop_stmt.setString(2, current_concept_ref);
                loc_prop_stmt.setString(3, sa[IDX_PROP_NAME]);
                loc_prop_stmt.setString(4, sa[IDX_PROP_LOC]);
                loc_prop_stmt.setString(5, sa[IDX_PROP_VALUE]);
                loc_prop_stmt.setString(6, current_namespace_ref);
                loc_prop_stmt.executeQuery();
                debugOut("loc property stored for " + current_concept_ref);
            } else {
                prop_stmt.setInt(1, getConceptPropId());
                prop_stmt.setString(2, current_concept_ref);
                prop_stmt.setString(3, sa[IDX_PROP_NAME]);
                prop_stmt.setString(4, sa[IDX_PROP_VALUE]);
                prop_stmt.setString(5, current_namespace_ref);
                prop_stmt.executeQuery();
                debugOut("property stored for " + current_concept_ref);
            }
        } else if (isLongProp(p_name)) {
            if (sa[IDX_PROP_VALUE].trim().length() > 1023) {
                throw new SQLException("Property value : [" + sa[IDX_PROP_VALUE].substring(0, 20) + "...]" + " is too long for property: " + sa[IDX_PROP_NAME] + "; CONCEPTDEF - " + current_concept_ref);
            }
            long_prop_stmt.setInt(1, getConceptLongPropId());
            long_prop_stmt.setString(2, current_concept_ref);
            long_prop_stmt.setString(3, sa[IDX_PROP_NAME]);
            long_prop_stmt.setString(4, sa[IDX_PROP_VALUE]);
            long_prop_stmt.setString(5, current_namespace_ref);
            long_prop_stmt.executeQuery();
            debugOut("long property stored for " + current_concept_ref);
        } else if (isRealLongProp(p_name)) {
            real_long_prop_stmt.setInt(1, getConceptRealLongPropId());
            real_long_prop_stmt.setString(2, current_concept_ref);
            real_long_prop_stmt.setString(3, sa[IDX_PROP_NAME]);
            byte[] bytes = sa[IDX_PROP_VALUE].getBytes();
            if (bytes.length > 2000) {
                real_long_prop_stmt.setBinaryStream(4, new BufferedInputStream(new ByteArrayInputStream(bytes), bytes.length), bytes.length);
            } else {
                real_long_prop_stmt.setBytes(4, bytes);
            }
            real_long_prop_stmt.setString(5, current_namespace_ref);
            real_long_prop_stmt.executeQuery();
            debugOut("real long property stored for " + current_concept_ref);
        } else {
            insertImportError("INVALID_PROPERTY", "Invalid property (" + p_name + ") found for CONCEPTDEF - " + current_concept_ref);
        }
    }

    public void storeRoleQualifer(String[] sa) throws SQLException {
        debugOut("storing qual " + sa[IDX_QUAL_NAME] + " for role " + currentRole + " for " + current_concept_ref);
        String qualRef = sa[IDX_QUAL_NAME];
        if (!isRoleQualifer(qualRef)) {
            insertImportError("INVALID_QUALIFIER", "Invalid Qualifier (" + qualRef + ") found for ROLEDEF - " + currentRole + "; CONCEPTDEF - " + current_concept_ref);
        }
        if (sa[IDX_QUAL_VALUE].trim().length() > 255) {
            throw new SQLException("Qualifier value : [" + sa[IDX_QUAL_VALUE].substring(0, 20) + "...]" + " is too long for qualifier: " + sa[IDX_QUAL_NAME] + "; ROLEDEF - " + currentRole + "; CONCEPTDEF - " + current_concept_ref);
        }
        qual_role_stmt.setInt(1, getConceptRoleId());
        qual_role_stmt.setString(2, sa[IDX_QUAL_NAME]);
        qual_role_stmt.setString(3, sa[IDX_QUAL_VALUE]);
        qual_role_stmt.executeQuery();
        debugOut("role qualifier stored for " + current_concept_ref);
    }

    public void storeAssociationQualifer(String[] sa) throws SQLException {
        debugOut("storing qual " + sa[IDX_QUAL_NAME] + " for assn " + currentAssociation + " for " + current_concept_ref);
        if (VALIDATE_QUALIFIER_VALUES) {
            if (!isValidateQualifierValue(sa[IDX_QUAL_NAME], sa[IDX_QUAL_VALUE])) {
                throw new SQLException(sa[IDX_QUAL_VALUE] + " is not a valid value for qualifier: " + sa[IDX_QUAL_NAME]);
            }
        }
        String qualRef = sa[IDX_QUAL_NAME];
        if (!isAssnQualifer(qualRef)) {
            insertImportError("INVALID_QUALIFIER", "Invalid Qualifier (" + qualRef + ") found for ASSOCIATIONDEF - " + currentAssociation + "; CONCEPTDEF - " + current_concept_ref);
        }
        if (sa[IDX_QUAL_VALUE].trim().length() > 255) {
            throw new SQLException("Qualifier value : [" + sa[IDX_QUAL_VALUE].substring(0, 20) + "...]" + " is too long for qualifier: " + sa[IDX_QUAL_NAME] + "; ASSOCIATIONDEF - " + currentAssociation + "; CONCEPTDEF - " + current_concept_ref);
        }
        qual_assn_stmt.setInt(1, getConceptAssnId());
        qual_assn_stmt.setString(2, sa[IDX_QUAL_NAME]);
        qual_assn_stmt.setString(3, sa[IDX_QUAL_VALUE]);
        qual_assn_stmt.executeQuery();
        debugOut("assn qualifier stored for " + current_concept_ref);
    }

    public void storePropQualifer(String[] sa) throws SQLException {
        debugOut("storing qual " + sa[IDX_QUAL_NAME] + " for property " + currentProperty + " for " + current_concept_ref);
        if (VALIDATE_QUALIFIER_VALUES) {
            if (!isValidateQualifierValue(sa[IDX_QUAL_NAME], sa[IDX_QUAL_VALUE])) {
                throw new SQLException(sa[IDX_QUAL_VALUE] + " is not a valid value for qualifier: " + sa[IDX_QUAL_NAME]);
            }
        }
        String qualRef = sa[IDX_QUAL_NAME];
        if (isRegProp(currentProperty) || isLongProp(currentProperty) || isRealLongProp(currentProperty)) {
            if (!isPropQualifer(qualRef)) {
                insertImportError("INVALID_QUALIFIER", "Invalid Qualifier (" + qualRef + ") found for PROPERTYDEF - " + currentProperty + "; CONCEPTDEF - " + current_concept_ref);
                return;
            }
        }
        if (isRegProp(currentProperty)) {
            if (sa[IDX_QUAL_VALUE].trim().length() > 255) {
                throw new SQLException("Qualifier value : [" + sa[IDX_QUAL_VALUE].substring(0, 20) + "...]" + " is too long for qualifier: " + sa[IDX_QUAL_NAME] + "; PROPERTYDEF - " + currentProperty + "; CONCEPTDEF - " + current_concept_ref);
            }
            if (currentLocale != null) {
                qual_loc_prop_stmt.setInt(1, getConceptLocPropId());
                qual_loc_prop_stmt.setString(2, sa[IDX_QUAL_NAME]);
                qual_loc_prop_stmt.setString(3, sa[IDX_QUAL_VALUE]);
                qual_loc_prop_stmt.executeQuery();
                debugOut("loc prop qualifier stored for " + current_concept_ref);
            } else {
                qual_prop_stmt.setInt(1, getConceptPropId());
                qual_prop_stmt.setString(2, sa[IDX_QUAL_NAME]);
                qual_prop_stmt.setString(3, sa[IDX_QUAL_VALUE]);
                qual_prop_stmt.executeQuery();
                debugOut("prop qualifier stored for " + current_concept_ref);
            }
        } else if (isLongProp(currentProperty)) {
            if (sa[IDX_QUAL_VALUE].trim().length() > 255) {
                throw new SQLException("Qualifier value : [" + sa[IDX_QUAL_VALUE].substring(0, 20) + "...]" + " is too long for qualifier: " + sa[IDX_QUAL_NAME] + "; PROPERTYDEF - " + currentProperty + "; CONCEPTDEF - " + current_concept_ref);
            }
            qual_long_prop_stmt.setInt(1, getConceptLongPropId());
            qual_long_prop_stmt.setString(2, sa[IDX_QUAL_NAME]);
            qual_long_prop_stmt.setString(3, sa[IDX_QUAL_VALUE]);
            qual_long_prop_stmt.executeQuery();
            debugOut("long prop qualifier stored for " + current_concept_ref);
        } else if (isRealLongProp(currentProperty)) {
            if (sa[IDX_QUAL_VALUE].trim().length() > 255) {
                throw new SQLException("Qualifier value : [" + sa[IDX_QUAL_VALUE].substring(0, 20) + "...]" + " is too long for qualifier: " + sa[IDX_QUAL_NAME] + "; PROPERTYDEF - " + currentProperty + "; CONCEPTDEF - " + current_concept_ref);
            }
            qual_real_long_prop_stmt.setInt(1, getConceptRealLongPropId());
            qual_real_long_prop_stmt.setString(2, sa[IDX_QUAL_NAME]);
            qual_real_long_prop_stmt.setString(3, sa[IDX_QUAL_VALUE]);
            qual_real_long_prop_stmt.executeQuery();
            debugOut("real long prop qualifier stored for " + current_concept_ref);
        }
    }

    protected int getConceptPropId() {
        if (nextConceptPropId) {
            conceptPropId++;
            nextConceptPropId = false;
        }
        return conceptPropId;
    }

    protected int getConceptLocPropId() {
        if (nextConceptPropId) {
            conceptLocPropId++;
            nextConceptPropId = false;
        }
        return conceptLocPropId;
    }

    protected int getConceptLongPropId() {
        if (nextConceptPropId) {
            conceptLongPropId++;
            nextConceptPropId = false;
        }
        return conceptLongPropId;
    }

    protected int getConceptRealLongPropId() {
        if (nextConceptPropId) {
            conceptRealLongPropId++;
            nextConceptPropId = false;
        }
        return conceptRealLongPropId;
    }

    protected int getConceptSynonymPropId() {
        if (nextConceptPropId) {
            conceptSynonymPropId++;
            nextConceptPropId = false;
        }
        return conceptSynonymPropId;
    }

    protected int getConceptAssnId() {
        if (nextConceptAssnId) {
            conceptAssnId++;
            nextConceptAssnId = false;
        }
        return conceptAssnId;
    }

    protected int getConceptRoleId() {
        if (nextConceptRoleId) {
            conceptRoleId++;
            nextConceptRoleId = false;
        }
        return conceptRoleId;
    }

    private void initSeqBasedIds() throws SQLException {
        Statement stmt = conn.createStatement();
        conceptRoleId = getMaxConId(stmt, "con_role_id", "dl_defining_role");
        conceptPropId = getMaxConId(stmt, "con_prop_id", "dl_property_str");
        conceptLocPropId = getMaxConId(stmt, "con_loc_prop_id", "dl_loc_property_str");
        conceptLongPropId = getMaxConId(stmt, "con_long_prop_id", "dl_long_property_str");
        conceptRealLongPropId = getMaxConId(stmt, "con_real_long_prop_id", "dl_real_long_property_str");
        conceptAssnId = getMaxConId(stmt, "con_assn_id", "dl_con_assn");
        stmt.close();
    }

    private int getMaxConId(Statement stmt, String col, String table) throws SQLException {
        int id = 0;
        String query = "SELECT MAX(" + col + ") FROM " + table;
        ResultSet rset = stmt.executeQuery(query);
        if (rset.next()) {
            id = rset.getInt(1);
        }
        rset.close();
        return id;
    }

    private void initStatements() throws SQLException {
        String query;
        query = "INSERT INTO dl_import_concept " + " (id, code, name, namespace_ref, kind_ref, primitive) " + " VALUES ( ?, ?, ?, ?, ?, ?)";
        concept_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_defining_concept " + " (con_ref, sup_ref, namespace_ref ) " + " VALUES ( ?, ?, ? )";
        defcon_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_defining_role " + " (con_role_id, con_ref, role_ref, modifier, value_ref, rolegroup, namespace_ref ) " + " VALUES ( ?, ?, ?, ?, ?, ?, ? )";
        defrole_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_property " + " (con_prop_id, con_ref, prop_ref, value_ref, namespace_ref ) " + " VALUES (?, ?, ?, ?, ? )";
        prop_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_loc_property " + " (con_loc_prop_id, con_ref, prop_ref, local_ref, value_ref, namespace_ref ) " + " VALUES (?, ?, ?, ?, ?, ? )";
        loc_prop_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_long_property " + " (con_long_prop_id, con_ref, prop_ref, value_ref, namespace_ref ) " + " VALUES (?, ?, ?, ?, ? )";
        long_prop_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_real_long_property " + " (con_real_long_prop_id, con_ref, prop_ref, value_ref, namespace_ref ) " + " VALUES (?, ?, ?, ?, ? )";
        real_long_prop_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_con_assn " + " (con_assn_id, con_ref, assn_ref, value_ref, namespace_ref ) " + " VALUES (?, ?, ?, ?, ?)";
        assn_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_qual_prop " + " (con_prop_id, qual_ref, value_ref ) " + " VALUES (?, ?, ?)";
        qual_prop_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_qual_loc_prop " + " (con_loc_prop_id, qual_ref, value_ref ) " + " VALUES (?, ?, ?)";
        qual_loc_prop_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_qual_long_prop " + " (con_long_prop_id, qual_ref, value_ref ) " + " VALUES (?, ?, ?)";
        qual_long_prop_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_qual_real_long_prop " + " (con_real_long_prop_id, qual_ref, value_ref ) " + " VALUES (?, ?, ?)";
        qual_real_long_prop_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_qual_con_assn " + " (con_assn_id, qual_ref, value_ref ) " + " VALUES (?, ?, ?)";
        qual_assn_stmt = conn.prepareStatement(query);
        query = "INSERT INTO dl_import_qual_role " + " (con_role_id, qual_ref, value_ref ) " + " VALUES (?, ?, ?)";
        qual_role_stmt = conn.prepareStatement(query);
        stmt = conn.createStatement();
    }

    public OntyImpDB(Connection c) throws SQLException {
        conn = c;
        initSeqBasedIds();
        initStatements();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cleanImportTables(int import_type) throws SQLException {
        String q = null;
        for (int i = 0; i < COMMON_IMP_TABLES.length; i++) {
            q = "TRUNCATE TABLE " + COMMON_IMP_TABLES[i];
            stmt.executeUpdate(q);
        }
        if (import_type == SCHEMA_AND_CONTENT) {
            for (int i = 0; i < SCHEMA_IMP_TABLES.length; i++) {
                q = "TRUNCATE TABLE " + SCHEMA_IMP_TABLES[i];
                stmt.executeUpdate(q);
            }
            for (int i = 0; i < CONTENT_IMP_TABLES.length; i++) {
                q = "TRUNCATE TABLE " + CONTENT_IMP_TABLES[i];
                stmt.executeUpdate(q);
            }
        }
        if (import_type == SCHEMA) {
            for (int i = 0; i < SCHEMA_IMP_TABLES.length; i++) {
                q = "TRUNCATE TABLE " + SCHEMA_IMP_TABLES[i];
                stmt.executeUpdate(q);
            }
        }
        if (import_type == CONTENT) {
            for (int i = 0; i < CONTENT_IMP_TABLES.length; i++) {
                q = "TRUNCATE TABLE " + CONTENT_IMP_TABLES[i];
                stmt.executeUpdate(q);
            }
            preloadPickListRestrictions();
            preloadQualPickListRestrictions();
        }
        fImportErrorCount = 0;
    }

    public void dropImportIndices() {
    }

    public void createImportIndices() {
    }

    /**
   * Insert the method's description here.
   * Creation date: (3/20/2001 4:24:29 PM)
   * Closes Statement objects opened in initStatements
   */
    public void closeAllStatements() {
        try {
            concept_stmt.close();
            defcon_stmt.close();
            defrole_stmt.close();
            prop_stmt.close();
            loc_prop_stmt.close();
            long_prop_stmt.close();
            real_long_prop_stmt.close();
            assn_stmt.close();
            qual_prop_stmt.close();
            qual_loc_prop_stmt.close();
            qual_long_prop_stmt.close();
            qual_real_long_prop_stmt.close();
            qual_assn_stmt.close();
            qual_role_stmt.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
   * This method writes an entry into the import error table. This occurs
   * when we encounter an invalid value...for instance, an id that is not
   * an integer
   * @param errorType    The type of the error (e.g. INVALID_ID)
   * @param errorMsg     Error Message
   * @throws SQLException
   */
    public void insertImportError(String errorType, String errorMsg) throws SQLException {
        Statement stmt = conn.createStatement();
        if (errorType.length() > 32) {
            errorType = errorType.substring(0, 31);
        }
        if (errorMsg.length() > 512) {
            errorMsg = errorMsg.substring(0, 511);
        }
        stmt.executeUpdate("INSERT INTO dl_import_errors VALUES (" + "'" + errorType + "', " + "'" + errorMsg + "')");
        conn.commit();
        stmt.close();
    }

    public int getImportErrorCount() {
        return fImportErrorCount;
    }

    /**
   * This method gets Oracle character set being used
   * @throws SQLException
   * @return encoding name
   */
    public String getDBEncoding() throws SQLException {
        return SQL.getOracleDBEncoding(conn);
    }
}
