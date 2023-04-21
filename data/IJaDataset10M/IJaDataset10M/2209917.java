package com.apelon.ontylogxchg;

import com.apelon.common.SessionMgr;
import com.apelon.common.xml.XML;
import com.apelon.common.sql.*;
import com.apelon.apelonserver.client.*;
import com.apelon.tde.client.*;
import com.apelon.tde.server.*;
import com.apelon.tde.dtd.OntylogDTD;
import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.Iterator;
import java.util.Locale;

public class OntyExpDB implements OntylogDTD {

    public static final int REF_BY_CODE = 1;

    public static final int REF_BY_ID = 2;

    public static final int REF_BY_NAME = 3;

    public static final int EXPORT_VIEW_STATED = 1;

    public static final int EXPORT_VIEW_INFERRED = 2;

    public static final int EXPORT_VIEW_AUTHORING = 3;

    public static final int EXPORT_VIEW_LONGCANON = 4;

    public static final int EXPORT_VIEW_SHORTCANON = 5;

    public static final int IF_EXISTS_REPLACE = 1;

    public static final int IF_EXISTS_IGNORE = 2;

    public static final int IF_EXISTS_ERROR = 3;

    public static final int EXPORT_TYPE_SCHEMA_AND_CONTENT = 1;

    public static final int EXPORT_TYPE_SCHEMA = 2;

    public static final int EXPORT_TYPE_CONTENT = 3;

    protected Connection conn;

    protected ServerConnectionJDBC sc;

    private NamespaceQuery nsQuery = null;

    protected ConceptInfQuery conInfQuery = null;

    protected int ref_by;

    protected int if_exists;

    protected int export_type;

    protected int export_view;

    protected int count;

    protected PrintWriter out;

    protected String exportFile;

    protected int namespace_id;

    protected String namespace_code;

    protected String namespace_name;

    protected String namespace_tag;

    protected String content_license;

    protected Hashtable kind_cache;

    protected Hashtable role_cache;

    protected Hashtable property_cache;

    protected Hashtable assn_cache;

    protected Hashtable qual_cache;

    /**
   * These statements and result sets are used by both the standard export
   * and the change set export
   */
    protected Statement fCon_Stmt = null;

    protected ResultSet fCon_Res = null;

    protected Statement fDC_Stmt = null;

    protected ResultSet fDC_Res = null;

    protected Statement fRO_Stmt = null;

    protected ResultSet fRO_Res = null;

    protected Statement fPR_Stmt = null;

    protected ResultSet fPR_Res = null;

    protected Statement fLPR_Stmt = null;

    protected ResultSet fLPR_Res = null;

    protected Statement fRLPR_Stmt = null;

    protected ResultSet fRLPR_Res = null;

    protected PreparedStatement fRLPR_Val_PS;

    protected Statement fAssn_Stmt = null;

    protected ResultSet fAssn_Res = null;

    private StringBuffer conBuf = new StringBuffer(1024);

    private boolean dc_next = true;

    private int dc_con_gid = -1;

    private String dc_sup_ref = "";

    private int supCon;

    private boolean ro_next = true;

    private int ro_con_gid = -1;

    private int ro_role_gid = -1;

    private int ro_modifier = -1;

    private String ro_value_ref = "";

    private int val_id = -1;

    private String val_code = "";

    private String val_name = "";

    private int ro_rolegroup = -1;

    private boolean pr_next = true;

    private boolean pr_qual_next = true;

    private int con_prop_id = -1;

    private int pr_con_gid = -1;

    private int pr_prop_gid = -1;

    private int pr_qual_gid = -1;

    private String pr_value = "";

    private String pr_qual_value = "";

    private int pr_con_prop_id = -1;

    private boolean l_pr_next = true;

    private int con_long_prop_id = -1;

    private int l_pr_con_gid = -1;

    private int l_pr_prop_gid = -1;

    private String l_pr_value = "";

    private int l_pr_qual_gid = -1;

    private String l_pr_qual_value = "";

    private boolean rl_pr_next = true;

    private int con_real_long_prop_id = -1;

    private int rl_pr_con_gid = -1;

    private int rl_pr_prop_gid = -1;

    private String rl_pr_value = "";

    private int rl_pr_qual_gid = -1;

    private String rl_pr_qual_value = "";

    private boolean assn_next = true;

    private boolean assn_qual_next = true;

    private int con_assn_id = -1;

    private int assn_con_gid = -1;

    private int assn_gid = -1;

    private String assn_value_ref = "";

    private int assn_qual_con_gid = -1;

    private int assn_qual_assn_gid = -1;

    private int assn_qual_gid = -1;

    private String assn_qual_value = "";

    private int assn_con_assn_id = -1;

    private SessionMgr sess_mgr = null;

    private PreparedStatement qual_assn_stmt;

    private ResultSet qual_assn_Res = null;

    private PreparedStatement qual_prop_stmt;

    private ResultSet qual_prop_Res = null;

    private PreparedStatement qual_long_prop_stmt;

    private ResultSet qual_long_prop_Res = null;

    private PreparedStatement qual_real_long_prop_stmt;

    private ResultSet qual_real_long_prop_Res = null;

    private String concept_tag = "conceptDef";

    private Concept[] primitiveParents;

    private Concept[] directParents;

    protected OntyExpDB() {
    }

    public OntyExpDB(Connection conn, String file_name, int ref_by, int if_exists, int export_type, int export_v) throws IOException {
        this.conn = conn;
        this.exportFile = file_name;
        this.ref_by = ref_by;
        this.if_exists = if_exists;
        this.export_type = export_type;
        this.export_view = export_v;
        this.kind_cache = new Hashtable();
        this.role_cache = new Hashtable();
        this.property_cache = new Hashtable();
        this.assn_cache = new Hashtable();
        this.qual_cache = new Hashtable();
    }

    /**
   * Constructor which takes an additional SessionMgr object which allows for
   * feedback to the DataManager GUI
   */
    public OntyExpDB(Connection conn, String file_name, int ref_by, int if_exists, int export_type, int export_view, SessionMgr smgr) throws IOException {
        this(conn, file_name, ref_by, if_exists, export_type, export_view);
        sess_mgr = smgr;
    }

    protected String asRefBy(String name, String code, int id) {
        if (ref_by == REF_BY_NAME) return name;
        if (ref_by == REF_BY_CODE) return code;
        if (ref_by == REF_BY_ID) return String.valueOf(id);
        return "-1";
    }

    /**
   * Close all open statements and result sets
   */
    protected void closeConceptQueryStmtRes() throws SQLException {
        fCon_Res.close();
        fCon_Stmt.close();
        fDC_Res.close();
        fDC_Stmt.close();
        fRO_Res.close();
        fRO_Stmt.close();
        fPR_Res.close();
        fPR_Stmt.close();
        fLPR_Res.close();
        fLPR_Stmt.close();
        fRLPR_Res.close();
        fRLPR_Stmt.close();
        fAssn_Res.close();
        fAssn_Stmt.close();
        fRLPR_Val_PS.close();
        qual_assn_stmt.close();
        if (qual_assn_Res != null) {
            qual_assn_Res.close();
        }
        qual_prop_stmt.close();
        if (qual_prop_Res != null) {
            qual_prop_Res.close();
        }
        qual_long_prop_stmt.close();
        if (qual_long_prop_Res != null) {
            qual_long_prop_Res.close();
        }
        qual_real_long_prop_stmt.close();
        if (qual_real_long_prop_Res != null) {
            qual_real_long_prop_Res.close();
        }
    }

    private void debugOut(String str) {
    }

    protected ConceptInfQuery getConInfQuery() throws TDEException {
        if (conInfQuery == null) {
            initTdeQueries();
        }
        return conInfQuery;
    }

    protected void startConceptQuery() throws Exception {
        initConceptQueries();
    }

    protected void endConceptQuery() throws Exception {
        closeConceptQueryStmtRes();
    }

    protected void exportConcept() throws Exception {
        int numConcepts = 0;
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery("select count(*) from dl_concept");
        if (r.next()) {
            numConcepts = r.getInt(1);
        }
        r.close();
        s.close();
        int numExported = 0;
        int[] stages = new int[] { 19, 28, 37, 46, 55, 64, 73, 82, 90, 99 };
        int nextPercent = 10;
        startConceptQuery();
        while (fCon_Res.next()) {
            numExported++;
            double curPercent = (numExported / (numConcepts * 1.0)) * 100;
            if (curPercent > nextPercent) {
                reportSessionProgress(stages[(nextPercent / 10) - 1], "Exported " + numExported + " concepts...", true);
                nextPercent += 10;
            }
            int i = 1;
            int gid = fCon_Res.getInt(i++);
            int id = fCon_Res.getInt(i++);
            String code = fCon_Res.getString(i++);
            String name = fCon_Res.getString(i++);
            int kind_gid = fCon_Res.getInt(i++);
            String primitive = fCon_Res.getString(i++);
            conBuf.setLength(0);
            putConceptXML(conBuf, name, code, id, primitive, kind_gid, gid);
            out.println(conBuf.toString());
        }
        endConceptQuery();
    }

    private void initTdeQueries() throws TDEException {
        if (sc == null) {
            throw new TDEException("ServerConnection is not set.");
        }
        nsQuery = (NamespaceQuery) TdeQuery.createInstance(sc, null, TdeQuery.NSPQUERY);
        nsQuery.openNamespace(namespace_id, NamespaceQuery.ID);
        conInfQuery = (ConceptInfQuery) TdeQuery.createInstance(sc, nsQuery.getNameSpace(), TdeQuery.CONINFQUERY);
    }

    protected void putConceptXML(StringBuffer buf, String name, String code, int id, String primitive, int kind_gid, int gid) throws SQLException, TDEException {
        int conceptID = id;
        buf.append(XML.asStartTag(concept_tag));
        buf.append(XML.asTagValue(ELM_NAME, name));
        buf.append(XML.asTagValue(ELM_CODE, code));
        buf.append(XML.asTagValue(ELM_ID, String.valueOf(id)));
        buf.append(namespace_tag);
        if (primitive.equals("T")) {
            buf.append(XML.asEmptyTag(ELM_PRIMITIVE));
        }
        if (kind_gid != 0) {
            buf.append(XML.asTagValue(ELM_KIND, (String) kind_cache.get(new Integer(kind_gid))));
        }
        putDefiningConcepts(buf, gid, conceptID);
        putDefiningRoles(buf, gid, conceptID);
        putProperties(buf, gid);
        putAssociations(buf, gid);
        buf.append(XML.asEndTag(concept_tag));
    }

    private void putDefiningConcepts(StringBuffer buf, int gid, int conceptID) throws TDEException, SQLException {
        buf.append(XML.asStartTag(ELM_DEFINING_CONCEPTS));
        if (export_view == EXPORT_VIEW_LONGCANON || export_view == EXPORT_VIEW_SHORTCANON) {
            Concept[] concepts = getConInfQuery().getMostProximatePrimitives(conceptID);
            primitiveParents = concepts;
            for (int j = 0; j < concepts.length; j++) {
                dc_sup_ref = asRefBy(concepts[j].getName(), concepts[j].getCode(), concepts[j].getId());
                buf.append(XML.asTagValue(ELM_CONCEPT, dc_sup_ref));
            }
        } else {
            ArrayList supCollection = new ArrayList();
            while (true) {
                if (dc_next) {
                    if (fDC_Res.next()) {
                        int dc_i = 1;
                        dc_con_gid = fDC_Res.getInt(dc_i++);
                        int sup_id = fDC_Res.getInt(dc_i++);
                        supCon = sup_id;
                        String sup_code = fDC_Res.getString(dc_i++);
                        String sup_name = fDC_Res.getString(dc_i++);
                        dc_sup_ref = asRefBy(sup_name, sup_code, sup_id);
                    } else {
                        dc_con_gid = -1;
                    }
                }
                dc_next = false;
                if (gid == dc_con_gid) {
                    supCollection.add(new Integer(supCon));
                    buf.append(XML.asTagValue(ELM_CONCEPT, dc_sup_ref));
                    dc_next = true;
                } else {
                    break;
                }
            }
            directParents = new Concept[supCollection.size()];
            for (int i = 0; i < supCollection.size(); i++) {
                Concept con = new Concept("Sup");
                con.setId(((Integer) supCollection.get(i)).intValue());
                directParents[i] = con;
            }
        }
        buf.append(XML.asEndTag(ELM_DEFINING_CONCEPTS));
    }

    private void putDefiningRoles(StringBuffer buf, int gid, int conceptID) throws TDEException, SQLException {
        int cur_role_group = 0;
        buf.append(XML.asStartTag(ELM_DEFINING_ROLES));
        if (export_view == EXPORT_VIEW_SHORTCANON) {
            MutableRoleCollection mrc = getConInfQuery().getDiffRoleCollection(conceptID, primitiveParents);
            Iterator iter = mrc.getSingleRoles();
            while (iter.hasNext()) {
                Role role = (Role) iter.next();
                RoleModifier rm = role.getModifier();
                ro_modifier = rm.getId();
                ro_value_ref = asRefBy(role.getValue().getName(), role.getValue().getCode(), role.getValue().getId());
                buf.append(XML.asStartTag(ELM_ROLE));
                if (ro_modifier == com.apelon.tde.client.RoleModifier.ALL.getId()) {
                    buf.append(XML.asEmptyTag(ELM_ALL));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOME.getId()) {
                    buf.append(XML.asEmptyTag(ELM_SOME));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.POSS.getId()) {
                    buf.append(XML.asEmptyTag(ELM_POSS));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.NOTALL.getId()) {
                    buf.append(XML.asEmptyTag(ELM_NOTALL));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.NOTSOME.getId()) {
                    buf.append(XML.asEmptyTag(ELM_NOTSOME));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOMENOT.getId()) {
                    buf.append(XML.asEmptyTag(ELM_SOMENOT));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.ALLNOT.getId()) {
                    buf.append(XML.asEmptyTag(ELM_ALLNOT));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOMEOR.getId()) {
                    buf.append(XML.asEmptyTag(ELM_SOMEOR));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.ALLOR.getId()) {
                    buf.append(XML.asEmptyTag(ELM_ALLOR));
                }
                buf.append((String) role_cache.get(new Integer(role.getRoleDef().getId())));
                buf.append(XML.asTagValue(ELM_VALUE, ro_value_ref));
                buf.append(XML.asEndTag(ELM_ROLE));
            }
            iter = mrc.getRoleGroups();
            while (iter.hasNext()) {
                RoleGroup rg = (RoleGroup) iter.next();
                buf.append(XML.asStartTag(ELM_ROLE_GROUP));
                Iterator roles = rg.iterator();
                while (roles.hasNext()) {
                    Role role = (Role) roles.next();
                    RoleModifier rm = role.getModifier();
                    ro_modifier = rm.getId();
                    ro_value_ref = asRefBy(role.getValue().getName(), role.getValue().getCode(), role.getValue().getId());
                    buf.append(XML.asStartTag(ELM_ROLE));
                    if (ro_modifier == com.apelon.tde.client.RoleModifier.ALL.getId()) {
                        buf.append(XML.asEmptyTag(ELM_ALL));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOME.getId()) {
                        buf.append(XML.asEmptyTag(ELM_SOME));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.POSS.getId()) {
                        buf.append(XML.asEmptyTag(ELM_POSS));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.NOTALL.getId()) {
                        buf.append(XML.asEmptyTag(ELM_NOTALL));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.NOTSOME.getId()) {
                        buf.append(XML.asEmptyTag(ELM_NOTSOME));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOMENOT.getId()) {
                        buf.append(XML.asEmptyTag(ELM_SOMENOT));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.ALLNOT.getId()) {
                        buf.append(XML.asEmptyTag(ELM_ALLNOT));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOMEOR.getId()) {
                        buf.append(XML.asEmptyTag(ELM_SOMEOR));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.ALLOR.getId()) {
                        buf.append(XML.asEmptyTag(ELM_ALLOR));
                    }
                    buf.append((String) role_cache.get(new Integer(role.getRoleDef().getId())));
                    buf.append(XML.asTagValue(ELM_VALUE, ro_value_ref));
                    buf.append(XML.asEndTag(ELM_ROLE));
                }
                buf.append(XML.asEndTag(ELM_ROLE_GROUP));
            }
        } else if (export_view == EXPORT_VIEW_AUTHORING) {
            MutableRoleCollection mrc = getConInfQuery().getDiffRoleCollection(conceptID, directParents);
            Iterator iter = mrc.getSingleRoles();
            while (iter.hasNext()) {
                Role role = (Role) iter.next();
                RoleModifier rm = role.getModifier();
                ro_modifier = rm.getId();
                ro_value_ref = asRefBy(role.getValue().getName(), role.getValue().getCode(), role.getValue().getId());
                buf.append(XML.asStartTag(ELM_ROLE));
                if (ro_modifier == com.apelon.tde.client.RoleModifier.ALL.getId()) {
                    buf.append(XML.asEmptyTag(ELM_ALL));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOME.getId()) {
                    buf.append(XML.asEmptyTag(ELM_SOME));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.POSS.getId()) {
                    buf.append(XML.asEmptyTag(ELM_POSS));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.NOTALL.getId()) {
                    buf.append(XML.asEmptyTag(ELM_NOTALL));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.NOTSOME.getId()) {
                    buf.append(XML.asEmptyTag(ELM_NOTSOME));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOMENOT.getId()) {
                    buf.append(XML.asEmptyTag(ELM_SOMENOT));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.ALLNOT.getId()) {
                    buf.append(XML.asEmptyTag(ELM_ALLNOT));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOMEOR.getId()) {
                    buf.append(XML.asEmptyTag(ELM_SOMEOR));
                } else if (ro_modifier == com.apelon.tde.client.RoleModifier.ALLOR.getId()) {
                    buf.append(XML.asEmptyTag(ELM_ALLOR));
                }
                buf.append((String) role_cache.get(new Integer(role.getRoleDef().getId())));
                buf.append(XML.asTagValue(ELM_VALUE, ro_value_ref));
                buf.append(XML.asEndTag(ELM_ROLE));
            }
            iter = mrc.getRoleGroups();
            while (iter.hasNext()) {
                RoleGroup rg = (RoleGroup) iter.next();
                buf.append(XML.asStartTag(ELM_ROLE_GROUP));
                Iterator roles = rg.iterator();
                while (roles.hasNext()) {
                    Role role = (Role) roles.next();
                    RoleModifier rm = role.getModifier();
                    ro_modifier = rm.getId();
                    ro_value_ref = asRefBy(role.getValue().getName(), role.getValue().getCode(), role.getValue().getId());
                    buf.append(XML.asStartTag(ELM_ROLE));
                    if (ro_modifier == com.apelon.tde.client.RoleModifier.ALL.getId()) {
                        buf.append(XML.asEmptyTag(ELM_ALL));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOME.getId()) {
                        buf.append(XML.asEmptyTag(ELM_SOME));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.POSS.getId()) {
                        buf.append(XML.asEmptyTag(ELM_POSS));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.NOTALL.getId()) {
                        buf.append(XML.asEmptyTag(ELM_NOTALL));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.NOTSOME.getId()) {
                        buf.append(XML.asEmptyTag(ELM_NOTSOME));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOMENOT.getId()) {
                        buf.append(XML.asEmptyTag(ELM_SOMENOT));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.ALLNOT.getId()) {
                        buf.append(XML.asEmptyTag(ELM_ALLNOT));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOMEOR.getId()) {
                        buf.append(XML.asEmptyTag(ELM_SOMEOR));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.ALLOR.getId()) {
                        buf.append(XML.asEmptyTag(ELM_ALLOR));
                    }
                    buf.append((String) role_cache.get(new Integer(role.getRoleDef().getId())));
                    buf.append(XML.asTagValue(ELM_VALUE, ro_value_ref));
                    buf.append(XML.asEndTag(ELM_ROLE));
                }
                buf.append(XML.asEndTag(ELM_ROLE_GROUP));
            }
        } else {
            while (true) {
                if (ro_next) {
                    if (fRO_Res.next()) {
                        int ro_i = 1;
                        ro_con_gid = fRO_Res.getInt(ro_i++);
                        ro_role_gid = fRO_Res.getInt(ro_i++);
                        ro_modifier = fRO_Res.getInt(ro_i++);
                        int val_id = fRO_Res.getInt(ro_i++);
                        String val_code = fRO_Res.getString(ro_i++);
                        String val_name = fRO_Res.getString(ro_i++);
                        ro_rolegroup = fRO_Res.getInt(ro_i++);
                        ro_value_ref = asRefBy(val_name, val_code, val_id);
                    } else {
                        ro_con_gid = -1;
                    }
                }
                ro_next = false;
                if (gid == ro_con_gid) {
                    if (ro_rolegroup != 0) {
                        if (cur_role_group != 0 && ro_rolegroup != cur_role_group) {
                            buf.append(XML.asEndTag(ELM_ROLE_GROUP));
                        }
                        if (cur_role_group != ro_rolegroup) {
                            buf.append(XML.asStartTag(ELM_ROLE_GROUP));
                        }
                        cur_role_group = ro_rolegroup;
                    } else {
                        cur_role_group = 0;
                    }
                    buf.append(XML.asStartTag(ELM_ROLE));
                    if (ro_modifier == com.apelon.tde.client.RoleModifier.ALL.getId()) {
                        buf.append(XML.asEmptyTag(ELM_ALL));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOME.getId()) {
                        buf.append(XML.asEmptyTag(ELM_SOME));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.POSS.getId()) {
                        buf.append(XML.asEmptyTag(ELM_POSS));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.NOTALL.getId()) {
                        buf.append(XML.asEmptyTag(ELM_NOTALL));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.NOTSOME.getId()) {
                        buf.append(XML.asEmptyTag(ELM_NOTSOME));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOMENOT.getId()) {
                        buf.append(XML.asEmptyTag(ELM_SOMENOT));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.ALLNOT.getId()) {
                        buf.append(XML.asEmptyTag(ELM_ALLNOT));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.SOMEOR.getId()) {
                        buf.append(XML.asEmptyTag(ELM_SOMEOR));
                    } else if (ro_modifier == com.apelon.tde.client.RoleModifier.ALLOR.getId()) {
                        buf.append(XML.asEmptyTag(ELM_ALLOR));
                    } else {
                        throw new TDEException("export error occurred in creating role modifier value.");
                    }
                    buf.append((String) role_cache.get(new Integer(ro_role_gid)));
                    buf.append(XML.asTagValue(ELM_VALUE, ro_value_ref));
                    buf.append(XML.asEndTag(ELM_ROLE));
                    ro_next = true;
                } else {
                    break;
                }
            }
            if (cur_role_group != 0) {
                buf.append(XML.asEndTag(ELM_ROLE_GROUP));
            }
        }
        buf.append(XML.asEndTag(ELM_DEFINING_ROLES));
    }

    private void putProperties(StringBuffer buf, int gid) throws SQLException {
        buf.append(XML.asStartTag(ELM_PROPERTIES));
        while (true) {
            if (pr_next) {
                if (fPR_Res.next()) {
                    int pr_i = 1;
                    con_prop_id = fPR_Res.getInt(pr_i++);
                    pr_con_gid = fPR_Res.getInt(pr_i++);
                    pr_prop_gid = fPR_Res.getInt(pr_i++);
                    pr_value = fPR_Res.getString(pr_i++);
                } else {
                    pr_con_gid = -1;
                }
            }
            pr_next = false;
            if (gid == pr_con_gid) {
                buf.append(XML.asStartTag(ELM_PROPERTY));
                buf.append((String) property_cache.get(new Integer(pr_prop_gid)));
                buf.append(XML.asTagValue(ELM_VALUE, pr_value));
                buf.append(getPropQualifiers(con_prop_id));
                buf.append(XML.asEndTag(ELM_PROPERTY));
                pr_next = true;
            } else {
                break;
            }
        }
        while (true) {
            if (l_pr_next) {
                if (fLPR_Res.next()) {
                    int pr_i = 1;
                    con_long_prop_id = fLPR_Res.getInt(pr_i++);
                    l_pr_con_gid = fLPR_Res.getInt(pr_i++);
                    l_pr_prop_gid = fLPR_Res.getInt(pr_i++);
                    l_pr_value = fLPR_Res.getString(pr_i++);
                } else {
                    l_pr_con_gid = -1;
                }
            }
            l_pr_next = false;
            if (gid == l_pr_con_gid) {
                buf.append(XML.asStartTag(ELM_PROPERTY));
                buf.append((String) property_cache.get(new Integer(l_pr_prop_gid)));
                buf.append(XML.asTagValue(ELM_VALUE, l_pr_value));
                buf.append(getLongPropQualifiers(con_long_prop_id));
                buf.append(XML.asEndTag(ELM_PROPERTY));
                l_pr_next = true;
            } else {
                break;
            }
        }
        while (true) {
            if (rl_pr_next) {
                if (fRLPR_Res.next()) {
                    int pr_i = 1;
                    con_real_long_prop_id = fRLPR_Res.getInt(pr_i++);
                    rl_pr_con_gid = fRLPR_Res.getInt(pr_i++);
                    rl_pr_prop_gid = fRLPR_Res.getInt(pr_i++);
                } else {
                    rl_pr_con_gid = -1;
                }
            }
            rl_pr_next = false;
            if (gid == rl_pr_con_gid) {
                rl_pr_value = "";
                fRLPR_Val_PS.setInt(1, con_real_long_prop_id);
                ResultSet rset = fRLPR_Val_PS.executeQuery();
                if (rset.next()) {
                    byte[] b = rset.getBytes(1);
                    rl_pr_value = new String(b);
                }
                rset.close();
                buf.append(XML.asStartTag(ELM_PROPERTY));
                buf.append((String) property_cache.get(new Integer(rl_pr_prop_gid)));
                buf.append(XML.asTagValue(ELM_VALUE, rl_pr_value));
                buf.append(getRealLongPropQualifiers(con_real_long_prop_id));
                buf.append(XML.asEndTag(ELM_PROPERTY));
                rl_pr_next = true;
            } else {
                break;
            }
        }
        buf.append(XML.asEndTag(ELM_PROPERTIES));
    }

    private void putAssociations(StringBuffer buf, int gid) throws SQLException {
        boolean hasAssnsTag = false;
        while (true) {
            if (assn_next) {
                if (fAssn_Res.next()) {
                    int assn_i = 1;
                    con_assn_id = fAssn_Res.getInt(assn_i++);
                    assn_con_gid = fAssn_Res.getInt(assn_i++);
                    assn_gid = fAssn_Res.getInt(assn_i++);
                    int val_id = fAssn_Res.getInt(assn_i++);
                    String val_code = fAssn_Res.getString(assn_i++);
                    String val_name = fAssn_Res.getString(assn_i++);
                    assn_value_ref = asRefBy(val_name, val_code, val_id);
                } else {
                    assn_con_gid = -1;
                }
            }
            assn_next = false;
            if (gid == assn_con_gid) {
                if (!hasAssnsTag) {
                    buf.append(XML.asStartTag(ELM_ASSOCIATIONS));
                    hasAssnsTag = true;
                }
                buf.append(XML.asStartTag(ELM_ASSOCIATION));
                buf.append((String) assn_cache.get(new Integer(assn_gid)));
                buf.append(XML.asTagValue(ELM_VALUE, assn_value_ref));
                buf.append(getAssnQualifiers(con_assn_id));
                buf.append(XML.asEndTag(ELM_ASSOCIATION));
                assn_next = true;
            } else {
                break;
            }
        }
        if (hasAssnsTag) {
            buf.append(XML.asEndTag(ELM_ASSOCIATIONS));
        }
    }

    protected void exportKind() throws SQLException {
        exportKind(true);
    }

    protected void exportKind(boolean printOut) throws SQLException {
        String query;
        query = "SELECT gid, id, code, name, reference " + "  FROM dl_kind " + " WHERE namespace_id = " + namespace_id + " ";
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(query);
        while (res.next()) {
            int i = 1;
            int gid = res.getInt(i++);
            int id = res.getInt(i++);
            String code = res.getString(i++);
            String name = res.getString(i++);
            String reference = res.getString(i++);
            String[] attributes = new String[] { ATT_REFERENCE + "=\"" + (reference.equals("T") ? "true" : "false") + "\"" };
            if (printOut) {
                out.println(XML.asStartTagWithAtts(ELM_KIND_DEF, attributes));
                out.println(XML.asTagValue(ELM_NAME, name));
                out.println(XML.asTagValue(ELM_CODE, code));
                out.println(XML.asTagValue(ELM_ID, String.valueOf(id)));
                out.println(namespace_tag);
                out.println(XML.asEndTag(ELM_KIND_DEF));
                out.println();
            }
            kind_cache.put(new Integer(gid), asRefBy(name, code, id));
        }
        res.close();
        stmt.close();
    }

    protected void exportNamespace(boolean writeResults, boolean setref_by) throws SQLException, Exception {
        String query;
        int id;
        query = "SELECT id, code, name, id_type " + "  FROM dl_namespace ";
        Statement stmt1 = conn.createStatement();
        ResultSet res1 = stmt1.executeQuery(query);
        while (res1.next()) {
            int i = 1;
            id = res1.getInt(i++);
            String code = res1.getString(i++);
            String name = res1.getString(i++);
            String id_type = res1.getString(i++);
            if (setref_by) {
                if (id_type.equals("N")) ref_by = REF_BY_NAME; else if (id_type.equals("C")) ref_by = REF_BY_CODE; else if (id_type.equals("I")) ref_by = REF_BY_ID; else throw new Exception("Expected \"N\", \"C\", or \"I\" for " + "namespace id_type, but found \"" + id_type + "\" instead.");
            }
            query = "SELECT license_text  from content_license where namespace_id = " + id;
            Statement stmt2 = conn.createStatement();
            ResultSet res2 = stmt2.executeQuery(query);
            byte[] bytes = null;
            if (res2.next()) {
                bytes = res2.getBytes(1);
                content_license = new String(bytes);
            }
            res2.close();
            stmt2.close();
            if (writeResults) {
                out.println(XML.asStartTag(ELM_NAMESPACE_DEF));
                out.println(XML.asTagValue(ELM_NAME, name));
                out.println(XML.asTagValue(ELM_CODE, code));
                out.println(XML.asTagValue(ELM_ID, String.valueOf(id)));
                if (content_license != null) out.println(XML.asTagValue(ELM_CONTENT_LICENSE, content_license));
                out.println(XML.asEndTag(ELM_NAMESPACE_DEF));
                out.println();
            }
            namespace_id = id;
            namespace_code = code;
            namespace_name = name;
            namespace_tag = XML.asTagValue(ELM_NAMESPACE, asRefBy(name, code, id));
        }
        res1.close();
        stmt1.close();
    }

    protected void exportProperty() throws SQLException {
        exportProperty(true);
    }

    protected void exportProperty(boolean printOut) throws SQLException {
        String query;
        query = "SELECT gid, id, code, name, rng_gid, contains_index " + "  FROM dl_property_def " + " WHERE namespace_id = " + namespace_id + " ";
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(query);
        while (res.next()) {
            int i = 1;
            int gid = res.getInt(i++);
            int id = res.getInt(i++);
            String code = res.getString(i++);
            String name = res.getString(i++);
            int rng_gid = res.getInt(i++);
            String contains_index = res.getString(i++);
            if (printOut) {
                out.println(XML.asStartTag(ELM_PROPERTY_DEF));
                out.println(XML.asTagValue(ELM_NAME, name));
                out.println(XML.asTagValue(ELM_CODE, code));
                out.println(XML.asTagValue(ELM_ID, String.valueOf(id)));
                out.println(namespace_tag);
                if (rng_gid == 0) {
                    out.println(XML.asTagValue(ELM_RANGE, "string"));
                } else if (rng_gid == 1) {
                    out.println(XML.asTagValue(ELM_RANGE, "long_string"));
                } else if (rng_gid == 2) {
                    out.println(XML.asTagValue(ELM_RANGE, "real_long_string"));
                }
                if ((contains_index != null) && (contains_index.equals("T"))) {
                    out.println(XML.asEmptyTag(ELM_CONTAINS_INDEX));
                }
                outputPickList(out, gid);
                out.println(XML.asEndTag(ELM_PROPERTY_DEF));
                out.println();
            }
            property_cache.put(new Integer(gid), XML.asTagValue(ELM_NAME, asRefBy(name, code, id)));
        }
        res.close();
        stmt.close();
    }

    protected void exportAssociation() throws SQLException {
        exportAssociation(true);
    }

    protected void exportAssociation(boolean printOut) throws SQLException {
        String query;
        query = "SELECT gid, id, code, name, inverse_name, displayable " + "  FROM dl_association_def " + " WHERE namespace_id = " + namespace_id + " ";
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(query);
        while (res.next()) {
            int i = 1;
            int gid = res.getInt(i++);
            int id = res.getInt(i++);
            String code = res.getString(i++);
            String name = res.getString(i++);
            String inverseName = res.getString(i++);
            String displayable = res.getString(i++);
            if (printOut) {
                out.println(XML.asStartTag(ELM_ASSOCIATION_DEF));
                out.println(XML.asTagValue(ELM_NAME, name));
                out.println(XML.asTagValue(ELM_CODE, code));
                out.println(XML.asTagValue(ELM_ID, String.valueOf(id)));
                out.println(namespace_tag);
                if (inverseName != null) {
                    out.println(XML.asTagValue(ELM_INVERSE_NAME, inverseName));
                }
                if ((displayable != null) && (displayable.equals("T"))) {
                    out.println(XML.asEmptyTag(ELM_DISPLAYABLE));
                }
                out.println(XML.asEndTag(ELM_ASSOCIATION_DEF));
                out.println();
            }
            assn_cache.put(new Integer(gid), XML.asTagValue(ELM_NAME, asRefBy(name, code, id)));
        }
        res.close();
        stmt.close();
    }

    protected void exportQualifier() throws SQLException {
        exportQualifier(true);
    }

    protected void exportQualifier(boolean printOut) throws SQLException {
        String query;
        query = "SELECT gid, id, code, name, qualifier_type " + "  FROM dl_qualifier_def " + " WHERE namespace_id = " + namespace_id + " ";
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(query);
        while (res.next()) {
            int i = 1;
            int gid = res.getInt(i++);
            int id = res.getInt(i++);
            String code = res.getString(i++);
            String name = res.getString(i++);
            String type = res.getString(i++);
            String[] atribs = new String[] { ATT_TYPE + "=\"" + type + "\"" };
            if (printOut) {
                out.println(XML.asStartTagWithAtts(ELM_QUALIFIER_DEF, atribs));
                out.println(XML.asTagValue(ELM_NAME, name));
                out.println(XML.asTagValue(ELM_CODE, code));
                out.println(XML.asTagValue(ELM_ID, String.valueOf(id)));
                out.println(namespace_tag);
                outputQualifierPickList(out, gid);
                out.println(XML.asEndTag(ELM_QUALIFIER_DEF));
                out.println();
            }
            qual_cache.put(new Integer(gid), XML.asTagValue(ELM_NAME, asRefBy(name, code, id)));
        }
        res.close();
        stmt.close();
    }

    protected void exportRole() throws SQLException {
        exportRole(true);
    }

    protected void exportRole(boolean printOut) throws SQLException {
        String query;
        query = "SELECT gid, id, code, name, dom_gid, rng_gid, right_iden_gid, parent_gid " + "  FROM dl_role_def " + " WHERE namespace_id = " + namespace_id + " ";
        Statement stmt = conn.createStatement();
        ResultSet res = stmt.executeQuery(query);
        while (res.next()) {
            int i = 1;
            int gid = res.getInt(i++);
            int id = res.getInt(i++);
            String code = res.getString(i++);
            String name = res.getString(i++);
            int dom_gid = res.getInt(i++);
            int rng_gid = res.getInt(i++);
            int right_iden_gid = res.getInt(i++);
            int parent_gid = res.getInt(i++);
            if (printOut) {
                out.println(XML.asStartTag("roleDef"));
                out.println(XML.asTagValue("name", name));
                out.println(XML.asTagValue("code", code));
                out.println(XML.asTagValue("id", String.valueOf(id)));
                out.println(namespace_tag);
                out.println(XML.asTagValue("domain", (String) kind_cache.get(new Integer(dom_gid))));
                out.println(XML.asTagValue("range", (String) kind_cache.get(new Integer(rng_gid))));
                if (right_iden_gid != 0) {
                    out.println(XML.asTagValue("rightId", getRoleAsRefBy(right_iden_gid)));
                }
                if (parent_gid != 0) {
                    out.println(XML.asTagValue("parentRole", getRoleAsRefBy(parent_gid)));
                }
                out.println(XML.asEndTag("roleDef"));
                out.println();
            }
            role_cache.put(new Integer(gid), XML.asTagValue("name", asRefBy(name, code, id)));
        }
        res.close();
        stmt.close();
    }

    private String getRoleAsRefBy(int gid) throws SQLException {
        String value = null;
        String query = "SELECT id, code, name " + "  FROM dl_role_def " + " WHERE namespace_id = " + namespace_id + " " + "   AND gid = " + gid;
        Statement r_stmt = conn.createStatement();
        ResultSet r_res = r_stmt.executeQuery(query);
        if (r_res.next()) {
            int i = 1;
            int r_id = r_res.getInt(i++);
            String r_code = r_res.getString(i++);
            String r_name = r_res.getString(i++);
            value = asRefBy(r_name, r_code, r_id);
        }
        r_res.close();
        r_stmt.close();
        return value;
    }

    /**
   * Build the XML representation of a concept def based on the parameters
   * passed as well as the global result sets built in performConceptQueries
   */
    protected String getConceptDef(String name, String code, int id, String primitive, int kind_gid, int gid) throws Exception {
        conBuf.setLength(0);
        putConceptXML(conBuf, name, code, id, primitive, kind_gid, gid);
        return conBuf.toString();
    }

    /**
   * Return today's date in a GMT format
   */
    protected String getGMTDate() {
        Date d = new Date();
        DateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss 'GMT'", Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("Africa/Casablanca"));
        return df.format(d);
    }

    protected void initConceptQueries() throws SQLException {
        String query;
        query = "SELECT gid, id, code, name, kind_gid, primitive " + "  FROM dl_concept " + " WHERE namespace_id = " + namespace_id + " " + " ORDER BY gid ";
        fCon_Stmt = conn.createStatement();
        fCon_Res = fCon_Stmt.executeQuery(query);
        if (export_view == EXPORT_VIEW_STATED) {
            query = "SELECT con_gid, sup.id, sup.code, sup.name " + "  FROM dl_defining_concept, dl_concept con, " + "       dl_concept sup " + " WHERE con.namespace_id = " + namespace_id + " " + "   AND con.gid = con_gid " + "   AND sup.gid = sup_gid " + " ORDER BY con_gid ";
            concept_tag = "conceptDef";
        } else if (export_view == EXPORT_VIEW_INFERRED || export_view == EXPORT_VIEW_AUTHORING) {
            query = "SELECT con, sup.id, sup.code, sup.name " + "  FROM direct_sups, dl_concept con, " + "       dl_concept sup " + " WHERE con.namespace_id = " + namespace_id + " " + "   AND con.gid = con " + "   AND sup.gid = sup " + " ORDER BY con";
            if (export_view == EXPORT_VIEW_INFERRED) {
                concept_tag = "conceptInf";
            }
            if (export_view == EXPORT_VIEW_AUTHORING) {
                concept_tag = "conceptAuthoringForm";
            }
        } else if (export_view == EXPORT_VIEW_LONGCANON) {
            concept_tag = "conceptLongCanonForm";
        } else if (export_view == EXPORT_VIEW_SHORTCANON) {
            concept_tag = "conceptShortCanonForm";
        }
        fDC_Stmt = conn.createStatement();
        fDC_Res = fDC_Stmt.executeQuery(query);
        if (export_view == EXPORT_VIEW_STATED) {
            query = "SELECT con_gid, role_gid, modifier_id, val.id, val.code, val.name, rolegroup " + "  FROM dl_defining_role, dl_concept con, " + "       dl_concept val " + " WHERE con.namespace_id = " + namespace_id + " " + "   AND con.gid = con_gid " + "   AND val.gid = value_gid " + " ORDER BY con_gid, rolegroup ";
        } else if (export_view == EXPORT_VIEW_INFERRED || export_view == EXPORT_VIEW_LONGCANON) {
            query = "SELECT con, role, modifier, val.id, val.code, val.name, rolegroup " + "  FROM complete_role_con, dl_concept con, " + "       dl_concept val " + " WHERE con.namespace_id = " + namespace_id + " " + "   AND con.gid = con " + "   AND val.gid = val" + " ORDER BY con, rolegroup ";
        }
        fRO_Stmt = conn.createStatement();
        fRO_Res = fRO_Stmt.executeQuery(query);
        query = "SELECT con_prop_id, con_gid, prop_gid, value " + "  FROM dl_property_str, dl_concept con " + " WHERE con.namespace_id = " + namespace_id + " " + "   AND con.gid = con_gid " + " ORDER BY con_gid, prop_gid ";
        fPR_Stmt = conn.createStatement();
        fPR_Res = fPR_Stmt.executeQuery(query);
        query = "SELECT con_long_prop_id, con_gid, prop_gid, value " + "  FROM dl_long_property_str, dl_concept con " + " WHERE con.namespace_id = " + namespace_id + " " + "   AND con.gid = con_gid " + " ORDER BY con_gid, prop_gid ";
        fLPR_Stmt = conn.createStatement();
        fLPR_Res = fLPR_Stmt.executeQuery(query);
        query = "SELECT con_real_long_prop_id, con_gid, prop_gid" + "  FROM dl_real_long_property_str, dl_concept con " + " WHERE con.namespace_id = " + namespace_id + " " + "   AND con.gid = con_gid " + " ORDER BY con_gid, prop_gid ";
        fRLPR_Stmt = conn.createStatement();
        fRLPR_Res = fRLPR_Stmt.executeQuery(query);
        query = "SELECT value " + "  FROM dl_real_long_property_str" + " WHERE con_real_long_prop_id = ?";
        fRLPR_Val_PS = conn.prepareStatement(query);
        query = "SELECT con_assn_id, con_gid, assn_gid, val.id, val.code, val.name " + "  FROM dl_con_assn, dl_concept con, dl_concept val " + " WHERE con.namespace_id = " + namespace_id + " " + "   AND con.gid = con_gid " + "   AND val.gid = value_gid " + " ORDER BY con_gid, assn_gid ";
        fAssn_Stmt = conn.createStatement();
        fAssn_Res = fAssn_Stmt.executeQuery(query);
        query = "SELECT qual_gid, value " + "FROM  dl_qual_con_assn " + "WHERE con_assn_id = ? ";
        qual_assn_stmt = conn.prepareStatement(query);
        query = "SELECT qual_gid, value " + "FROM dl_qual_prop " + "WHERE con_prop_id = ? ";
        qual_prop_stmt = conn.prepareStatement(query);
        query = "SELECT qual_gid, value " + "FROM dl_qual_long_prop " + "WHERE con_long_prop_id = ? ";
        qual_long_prop_stmt = conn.prepareStatement(query);
        query = "SELECT qual_gid, value " + "FROM dl_qual_real_long_prop " + "WHERE con_real_long_prop_id = ? ";
        qual_real_long_prop_stmt = conn.prepareStatement(query);
    }

    public void performExport() throws SQLException, Exception {
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exportFile), "UTF8")));
        reportSessionProgress(-1, "Printing prolog and terminology tag...", true);
        printProlog();
        printStartTerminology();
        if (export_type == EXPORT_TYPE_SCHEMA) {
            reportSessionProgress(2, "Exporting namespace info...", true);
            exportNamespace(true, false);
            reportSessionProgress(20, "Exporting kind definitions...", true);
            exportKind();
            reportSessionProgress(40, "Exporting role definitions...", true);
            exportRole();
            reportSessionProgress(60, "Exporting property definitions...", true);
            exportProperty();
            reportSessionProgress(70, "Exporting association definitions...", true);
            exportAssociation();
            reportSessionProgress(70, "Exporting qualifier definitions...", true);
            exportQualifier();
        }
        if (export_type == EXPORT_TYPE_CONTENT) {
            exportNamespace(false, false);
            exportKind(false);
            exportRole(false);
            exportProperty(false);
            exportAssociation(false);
            exportQualifier(false);
            try {
                reportSessionProgress(2, "Exporting concept definitions...", true);
                exportConcept();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (export_type == EXPORT_TYPE_SCHEMA_AND_CONTENT) {
            reportSessionProgress(2, "Exporting namespace info...", true);
            exportNamespace(true, false);
            reportSessionProgress(3, "Exporting kind definitions...", true);
            exportKind();
            reportSessionProgress(4, "Exporting role definitions...", true);
            exportRole();
            reportSessionProgress(6, "Exporting property definitions...", true);
            exportProperty();
            reportSessionProgress(8, "Exporting association definitions...", true);
            exportAssociation();
            reportSessionProgress(8, "Exporting qualifier definitions...", true);
            exportQualifier();
            reportSessionProgress(10, "Exporting concept definitions...", true);
            exportConcept();
        }
        printEndTerminology();
        reportSessionProgress(100, "Export complete.", true);
        out.flush();
        out.close();
    }

    protected void printEndTerminology() {
        out.println(XML.asEndTag(ELM_TERMINOLOGY));
    }

    protected void printProlog() {
        out.println(XML.createProlog(ELM_TERMINOLOGY, com.apelon.tde.dtd.DTD.ONTYLOG, false));
        out.println();
    }

    protected void printStartTerminology() {
        String refBy = "code";
        String ifExistsAction = "replace";
        if (ref_by == REF_BY_NAME) {
            refBy = "name";
        } else if (ref_by == REF_BY_ID) {
            refBy = "id";
        }
        if (if_exists == IF_EXISTS_IGNORE) {
            ifExistsAction = "ignore";
        } else if (if_exists == IF_EXISTS_ERROR) {
            ifExistsAction = "error";
        }
        String[] attributes = { ATT_REF_BY + "=\"" + refBy + "\"", ATT_IF_EXISTS_ACTION + "=\"" + ifExistsAction + "\"" };
        out.println(XML.asStartTagWithAtts(ELM_TERMINOLOGY, attributes));
        out.println();
    }

    protected void reportSessionProgress(int stage, String message, boolean addLogEntry) {
        if (sess_mgr == null) return;
        if (stage > -1) sess_mgr.setCurStageInfo(stage, message);
        if (addLogEntry) sess_mgr.addLogEntry(message);
    }

    private void outputPickList(PrintWriter out, int gid) throws SQLException {
        String query = "SELECT value FROM dl_property_picklist WHERE prop_gid = " + gid;
        Statement stmt_picklist = conn.createStatement();
        ResultSet res_picklist = stmt_picklist.executeQuery(query);
        String val = null;
        if (res_picklist.next()) {
            val = res_picklist.getString(1);
            out.println(XML.asStartTag(ELM_PICK_LIST));
            out.println(XML.asTagValue(ELM_PICK_LIST_ITEM, val));
            while (res_picklist.next()) out.println(XML.asTagValue(ELM_PICK_LIST_ITEM, res_picklist.getString(1)));
            out.println(XML.asEndTag(ELM_PICK_LIST));
        }
        stmt_picklist.close();
        res_picklist.close();
    }

    private void outputQualifierPickList(PrintWriter out, int gid) throws SQLException {
        String query = "SELECT value FROM dl_qualifier_picklist WHERE qual_gid = " + gid;
        Statement stmt_picklist = conn.createStatement();
        ResultSet res_picklist = stmt_picklist.executeQuery(query);
        String val = null;
        if (res_picklist.next()) {
            val = res_picklist.getString(1);
            out.println(XML.asStartTag(ELM_PICK_LIST));
            out.println(XML.asTagValue(ELM_PICK_LIST_ITEM, val));
            while (res_picklist.next()) out.println(XML.asTagValue(ELM_PICK_LIST_ITEM, res_picklist.getString(1)));
            out.println(XML.asEndTag(ELM_PICK_LIST));
        }
        stmt_picklist.close();
        res_picklist.close();
    }

    /**
   * Retrieve qualifiers for the passed in id and create the appropriate XML for
   * the qualifiers
   * @param id con_assn_id
   * @return xml string
   * @throws SQLException
   */
    private String getAssnQualifiers(int con_assn_id) throws SQLException {
        boolean hasAssnQualsTag = false;
        StringBuffer result = new StringBuffer();
        qual_assn_stmt.setInt(1, con_assn_id);
        qual_assn_Res = qual_assn_stmt.executeQuery();
        while (qual_assn_Res.next()) {
            int assn_qual_i = 1;
            assn_qual_gid = qual_assn_Res.getInt(assn_qual_i++);
            assn_qual_value = qual_assn_Res.getString(assn_qual_i++);
            if (!hasAssnQualsTag) {
                result.append(XML.asStartTag(ELM_QUALIFIERS));
                hasAssnQualsTag = true;
            }
            result.append(XML.asStartTag(ELM_QUALIFIER));
            result.append((String) qual_cache.get(new Integer(assn_qual_gid)));
            result.append(XML.asTagValue(ELM_VALUE, assn_qual_value));
            result.append(XML.asEndTag(ELM_QUALIFIER));
        }
        if (hasAssnQualsTag) {
            result.append(XML.asEndTag(ELM_QUALIFIERS));
        }
        return result.toString();
    }

    /**
   * Retrieve qualifiers for the passed in id and create the appropriate XML for
   * the qualifiers
   * @param id con_prop_id
   * @return xml string
   * @throws SQLException
   */
    private String getPropQualifiers(int con_prop_id) throws SQLException {
        boolean hasPropQualsTag = false;
        StringBuffer result = new StringBuffer();
        qual_prop_stmt.setInt(1, con_prop_id);
        qual_prop_Res = qual_prop_stmt.executeQuery();
        while (qual_prop_Res.next()) {
            int prop_i = 1;
            pr_qual_gid = qual_prop_Res.getInt(prop_i++);
            pr_qual_value = qual_prop_Res.getString(prop_i++);
            if (!hasPropQualsTag) {
                result.append(XML.asStartTag(ELM_QUALIFIERS));
                hasPropQualsTag = true;
            }
            result.append(XML.asStartTag(ELM_QUALIFIER));
            result.append((String) qual_cache.get(new Integer(pr_qual_gid)));
            result.append(XML.asTagValue(ELM_VALUE, pr_qual_value));
            result.append(XML.asEndTag(ELM_QUALIFIER));
        }
        if (hasPropQualsTag) {
            result.append(XML.asEndTag(ELM_QUALIFIERS));
        }
        return result.toString();
    }

    /**
   * Retrieve qualifiers for the passed in id and create the appropriate XML for
   * the qualifiers
   * @param id con_long_prop_id
   * @return xml string
   * @throws SQLException
   */
    private String getLongPropQualifiers(int con_long_prop_id) throws SQLException {
        boolean hasLongPropQualsTag = false;
        StringBuffer result = new StringBuffer();
        qual_long_prop_stmt.setInt(1, con_long_prop_id);
        qual_long_prop_Res = qual_long_prop_stmt.executeQuery();
        while (qual_long_prop_Res.next()) {
            int l_prop_i = 1;
            l_pr_qual_gid = qual_long_prop_Res.getInt(l_prop_i++);
            l_pr_qual_value = qual_long_prop_Res.getString(l_prop_i++);
            if (!hasLongPropQualsTag) {
                result.append(XML.asStartTag(ELM_QUALIFIERS));
                hasLongPropQualsTag = true;
            }
            result.append(XML.asStartTag(ELM_QUALIFIER));
            result.append((String) qual_cache.get(new Integer(l_pr_qual_gid)));
            result.append(XML.asTagValue(ELM_VALUE, l_pr_qual_value));
            result.append(XML.asEndTag(ELM_QUALIFIER));
        }
        if (hasLongPropQualsTag) {
            result.append(XML.asEndTag(ELM_QUALIFIERS));
        }
        return result.toString();
    }

    /**
   * Retrieve qualifiers for the passed in id and create the appropriate XML for
   * the qualifiers
   * @param id con_real_long_prop_id
   * @return xml string
   * @throws SQLException
   */
    private String getRealLongPropQualifiers(int con_real_long_prop_id) throws SQLException {
        boolean hasRealLongPropQualsTag = false;
        StringBuffer result = new StringBuffer();
        qual_real_long_prop_stmt.setInt(1, con_real_long_prop_id);
        qual_real_long_prop_Res = qual_real_long_prop_stmt.executeQuery();
        while (qual_real_long_prop_Res.next()) {
            int l_prop_i = 1;
            rl_pr_qual_gid = qual_real_long_prop_Res.getInt(l_prop_i++);
            rl_pr_qual_value = qual_real_long_prop_Res.getString(l_prop_i++);
            if (!hasRealLongPropQualsTag) {
                result.append(XML.asStartTag(ELM_QUALIFIERS));
                hasRealLongPropQualsTag = true;
            }
            result.append(XML.asStartTag(ELM_QUALIFIER));
            result.append((String) qual_cache.get(new Integer(rl_pr_qual_gid)));
            result.append(XML.asTagValue(ELM_VALUE, rl_pr_qual_value));
            result.append(XML.asEndTag(ELM_QUALIFIER));
        }
        if (hasRealLongPropQualsTag) {
            result.append(XML.asEndTag(ELM_QUALIFIERS));
        }
        return result.toString();
    }

    private String getQualifiers(PreparedStatement ps) throws SQLException {
        StringBuffer result = new StringBuffer();
        ResultSet rset = ps.executeQuery();
        int qualGid = -1;
        String val = null;
        if (rset.next()) {
            qualGid = rset.getInt(1);
            val = rset.getString(2);
            result.append(XML.asStartTag(ELM_QUALIFIERS));
            result.append(XML.asStartTag(ELM_QUALIFIER));
            result.append((String) qual_cache.get(new Integer(qualGid)));
            result.append(XML.asTagValue(ELM_VALUE, val));
            result.append(XML.asEndTag(ELM_QUALIFIER));
            while (rset.next()) {
                qualGid = rset.getInt(1);
                val = rset.getString(2);
                result.append(XML.asStartTag(ELM_QUALIFIER));
                result.append((String) qual_cache.get(new Integer(qualGid)));
                result.append(XML.asTagValue(ELM_VALUE, val));
                result.append(XML.asEndTag(ELM_QUALIFIER));
            }
            result.append(XML.asEndTag(ELM_QUALIFIERS));
        }
        rset.close();
        return result.toString();
    }

    protected int getExportConceptCount() {
        int count = 0;
        try {
            String query = "SELECT count(*)" + " FROM dl_concept " + " WHERE namespace_id = " + namespace_id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
        }
        return count;
    }

    public void setServerConnection(ConnectionParams cps) throws Exception {
        sc = new ServerConnectionJDBC(cps);
        sc.setQueryServer(com.apelon.tde.server.QueryServer.class, TdeQuery.TDE_HEADER);
    }
}
