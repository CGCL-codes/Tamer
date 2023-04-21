package org.riverock.portlet.member;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import javax.portlet.PortletRequest;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.riverock.common.collections.MapTools;
import org.riverock.common.tools.RsetTools;
import org.riverock.common.tools.StringTools;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.exception.GenericException;
import org.riverock.generic.tools.XmlTools;
import org.riverock.portlet.schema.member.*;
import org.riverock.portlet.schema.member.types.ContentTypeActionType;
import org.riverock.portlet.schema.member.types.FieldsTypeJspTypeType;
import org.riverock.portlet.schema.member.types.ParameterTypeType;
import org.riverock.portlet.schema.member.types.PrimaryKeyTypeType;
import org.riverock.portlet.schema.member.types.RestrictTypeTypeType;
import org.riverock.portlet.schema.member.types.SqlCheckParameterTypeTypeType;
import org.riverock.portlet.schema.member.types.TargetModuleTypeActionType;
import org.riverock.portlet.schema.member.types.TypeFieldType;
import org.riverock.portlet.tools.RequestTools;
import org.riverock.portlet.tools.SiteUtils;
import org.riverock.interfaces.sso.a3.AuthSession;

/**
 * User: Admin
 * Date: Nov 19, 2002
 * Time: 9:57:07 PM
 *
 * $Id: MemberServiceClass.java,v 1.17 2006/05/23 13:20:21 serg_main Exp $
 */
public final class MemberServiceClass {

    private static final Logger log = Logger.getLogger(MemberServiceClass.class);

    private static class RestrictDescription {

        int type;

        String nameField = null;

        RestrictDescription(int type_, String nameField_) {
            this.type = type_;
            this.nameField = nameField_;
        }
    }

    private static final Object syncDebug = new Object();

    private static final RestrictDescription restrictDesc[] = { new RestrictDescription(RestrictTypeTypeType.FIRM_TYPE, "ID_FIRM"), new RestrictDescription(RestrictTypeTypeType.SITE_TYPE, "ID_SITE"), new RestrictDescription(RestrictTypeTypeType.USER_TYPE, "ID_USER") };

    public static String getString(MultiLangStringType str, Locale loc) {
        return getString(str, loc, "");
    }

    public static String getString(MultiLangStringType str, Locale loc, String defaultString) {
        if (log.isDebugEnabled()) {
            log.debug("str: " + str);
            log.debug("loc: " + loc);
            log.debug("defaultString: " + defaultString);
        }
        if (str == null) {
            return defaultString;
        }
        if (str.getIsUseProperties() != null && Boolean.TRUE.equals(str.getIsUseProperties())) {
            StringManager strMan = StringManager.getManager(str.getStorage(), loc);
            try {
                return strMan.getStr(str.getStringData());
            } catch (GenericException e) {
                return defaultString;
            }
        } else {
            if (str.getIsInit() != null && Boolean.TRUE.equals(str.getIsInit())) {
                return str.getStringData();
            }
            str.setStringData(str.getStringData() == null ? defaultString : str.getStringData());
            str.setIsInit(Boolean.TRUE);
            return str.getStringData();
        }
    }

    public static boolean isUpdateAction(ContentType content) {
        for (int k = 0; k < content.getTargetModuleCount(); k++) {
            TargetModuleType ta = content.getTargetModule(k);
            if (ta.getAction().getType() == TargetModuleTypeActionType.CHANGE_TYPE || ta.getAction().getType() == TargetModuleTypeActionType.DELETE_TYPE || ta.getAction().getType() == TargetModuleTypeActionType.LOOKUP_TYPE) {
                return true;
            }
        }
        return false;
    }

    public static ContentType getContent(ModuleType module, int actionType) {
        for (int i = 0; i < module.getContentCount(); i++) {
            ContentType cnt = module.getContent(i);
            if (cnt.getAction().getType() == actionType) return cnt;
        }
        return null;
    }

    public static FieldsType getField(QueryAreaType qa, String nameField) {
        if (nameField == null || qa == null || qa.getFieldsCount() == 0) return null;
        for (int k = 0; k < qa.getFieldsCount(); k++) {
            FieldsType ft = qa.getFields(k);
            if (nameField.equals(ft.getName())) return ft;
        }
        return null;
    }

    public static String getRealName(FieldsType ff) {
        return (ff.getName());
    }

    public static ModuleType getModule(MemberArea ma, String nameModule_) {
        if (nameModule_ == null) return null;
        for (int i = 0; i < ma.getModuleCount(); i++) {
            ModuleType m = ma.getModule(i);
            if (nameModule_.equals(m.getName())) return m;
        }
        return null;
    }

    public static String buildInsertSQL(ContentType content1, String fromParam1, ModuleType mod1, DatabaseAdapter dbDyn, String serverName, ModuleManager moduleManager, AuthSession authSession) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Unmarshal sqlCache object");
            log.debug("#7.004.001 SqlCache() " + content1.getQueryArea().getSqlCache());
            synchronized (syncDebug) {
                try {
                    XmlTools.writeToFile(content1.getQueryArea().getSqlCache(), SiteUtils.getTempDir() + File.separatorChar + "member-content-site-start.xml", "windows-1251");
                } catch (Exception ee) {
                    log.info("Error write debug to file");
                }
            }
        }
        SqlCacheType sqlCacheType = new SqlCacheType();
        sqlCacheType.setIsInit(Boolean.FALSE);
        content1.getQueryArea().setSqlCache(sqlCacheType);
        SqlClause sc = new SqlClause();
        String prevPK = null;
        String prevAlias = null;
        if (fromParam1 != null && fromParam1.length() > 0) {
            StringTokenizer st = new StringTokenizer(fromParam1, ",");
            while (st.hasMoreTokens()) {
                String modName = st.nextToken();
                ModuleType module = moduleManager.getModule(modName);
                ContentType cnt = getContent(module, ContentTypeActionType.INDEX_TYPE);
                if (cnt != null && cnt.getQueryArea() != null) {
                    SqlClause lookupSc = buildSelectClause(content1, cnt, module, dbDyn, serverName, authSession);
                    if (lookupSc.from.length() > 0 && lookupSc.select.length() > 0) {
                        if (sc.from.length() != 0) sc.from += ',';
                        sc.from += lookupSc.from;
                        if (log.isDebugEnabled()) log.debug("#22.33.01 " + sc.from);
                        if (sc.where.length() != 0) sc.where += " and ";
                        if (log.isDebugEnabled()) log.debug("#22.33.02 " + sc.where);
                        sc.where += MemberProcessingRenderRequest.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.' + cnt.getQueryArea().getPrimaryKey() + "=?";
                        if (log.isDebugEnabled()) log.debug("#22.33.07 " + sc.where);
                        if (prevPK != null) {
                            if (sc.where.length() != 0) sc.where += " and ";
                            sc.where += prevAlias + prevPK + "=" + MemberProcessingRenderRequest.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.' + prevPK;
                        }
                        if (log.isDebugEnabled()) log.debug("#22.33.09 " + sc.where);
                        if (lookupSc.where.length() > 0) {
                            if (sc.where.length() != 0) sc.where += " and ";
                            sc.where += lookupSc.where;
                        }
                        if (log.isDebugEnabled()) log.debug("#22.33.11 " + sc.where);
                        prevPK = cnt.getQueryArea().getPrimaryKey();
                        prevAlias = MemberProcessingRenderRequest.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.';
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("Unmarshal sqlCache object");
                    synchronized (syncDebug) {
                        XmlTools.writeToFile(content1.getQueryArea().getSqlCache(), SiteUtils.getTempDir() + File.separatorChar + "member-content-site-from-" + module.getName() + ".xml", "windows-1251");
                    }
                }
            }
        }
        if (log.isDebugEnabled()) log.debug("#22.33.41 " + sc.from);
        String insTable = (content1.getQueryArea().getTable(0)).getTable();
        String sql_1 = "insert into " + insTable + "(" + content1.getQueryArea().getPrimaryKey();
        for (int k = 0; k < content1.getQueryArea().getFieldsCount(); k++) {
            FieldsType ff = content1.getQueryArea().getFields(k);
            if (Boolean.TRUE.equals(ff.getIsShow()) && (ff.getJspType().getType() != FieldsTypeJspTypeType.BIGTEXT_TYPE)) {
                sql_1 += ',' + ff.getName();
            }
        }
        if (prevPK != null) {
            sql_1 += ',' + mod1.getLookupPK();
        }
        if (mod1.getSelfLookup() != null && mod1.getSelfLookup().getCurrentField() != null && mod1.getSelfLookup().getTopField() != null) {
            sql_1 += ',' + mod1.getSelfLookup().getTopField().getName();
        }
        int countQuestion = 0;
        if (content1.getQueryArea().getRestrict() != null && content1.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE && !checkRestrictField(content1, RestrictTypeTypeType.FIRM_TYPE)) {
            sql_1 += ",ID_FIRM";
            countQuestion++;
        }
        if (content1.getQueryArea().getRestrict() != null && content1.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE && !checkRestrictField(content1, RestrictTypeTypeType.SITE_TYPE)) {
            sql_1 += ",ID_SITE";
            countQuestion++;
        }
        if (content1.getQueryArea().getRestrict() != null && content1.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE && !checkRestrictField(content1, RestrictTypeTypeType.USER_TYPE)) {
            sql_1 += ",ID_USER";
            countQuestion++;
        }
        sql_1 += ")";
        String sql_ = "?" + StringTools.getMultypleString(",?", countQuestion);
        for (int k = 0; k < content1.getQueryArea().getFieldsCount(); k++) {
            FieldsType ff = content1.getQueryArea().getFields(k);
            if (Boolean.TRUE.equals(ff.getIsShow()) && (ff.getJspType().getType() != FieldsTypeJspTypeType.BIGTEXT_TYPE)) {
                if (ff.getJspType().getType() == FieldsTypeJspTypeType.DATE_TEXT_TYPE) sql_ += ", " + dbDyn.getNameDateBind(); else sql_ += ",?";
            }
        }
        if (prevPK != null) {
            sql_ += ",?";
        }
        if (mod1.getSelfLookup() != null && mod1.getSelfLookup().getCurrentField() != null && mod1.getSelfLookup().getTopField() != null) {
            sql_ += ",?";
        }
        if (content1.getQueryArea().getRestrict() != null && content1.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE) {
            if (checkRestrictField(content1, RestrictTypeTypeType.FIRM_TYPE)) {
                if (!Boolean.TRUE.equals(content1.getQueryArea().getSqlCache().getIsInit())) {
                    switch(dbDyn.getFamaly()) {
                        case DatabaseManager.MYSQL_FAMALY:
                            break;
                        default:
                            SqlFromType fromType = new SqlFromType();
                            fromType.setTable("V$_READ_LIST_FIRM");
                            fromType.setAlias(null);
                            content1.getQueryArea().getSqlCache().addFrom(fromType);
                            content1.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(null, "ID_FIRM", null, TypeFieldType.FIELD, null, null, "?", TypeFieldType.PARAMETER));
                            content1.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(null, "USER_LOGIN", null, TypeFieldType.FIELD, null, null, "?", TypeFieldType.PARAMETER));
                            break;
                    }
                }
            } else {
                if (!Boolean.TRUE.equals(content1.getQueryArea().getSqlCache().getIsInit())) {
                    SqlFromType fromType = new SqlFromType();
                    fromType.setTable("SEQ_WM_AUTH_USER");
                    fromType.setAlias(null);
                    content1.getQueryArea().getSqlCache().addFrom(fromType);
                    content1.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(null, "USER_LOGIN", null, TypeFieldType.FIELD, null, null, "?", TypeFieldType.PARAMETER));
                }
            }
            if (!Boolean.TRUE.equals(content1.getQueryArea().getSqlCache().getIsInit())) {
                addParameter(content1, "", SqlCheckParameterTypeTypeType.RESTRICT_FIRM, ParameterTypeType.NUMBER);
            }
        }
        if (log.isDebugEnabled()) log.debug("#15.1 restrict.from " + sc.from);
        if (content1.getQueryArea().getRestrict() != null && content1.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE) {
            if (!Boolean.TRUE.equals(content1.getQueryArea().getSqlCache().getIsInit())) {
                addParameter(content1, "", SqlCheckParameterTypeTypeType.RESTRICT_SITE);
                SqlFromType fromType = new SqlFromType();
                fromType.setTable("WM_PORTAL_VIRTUAL_HOST");
                fromType.setAlias(MemberProcessingRenderRequest.aliasSiteRestrict);
                content1.getQueryArea().getSqlCache().addFrom(fromType);
                content1.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(MemberProcessingRenderRequest.aliasSiteRestrict, "NAME_VIRTUAL_HOST", null, TypeFieldType.FIELD, null, null, "lower(?)", TypeFieldType.PARAMETER));
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Unmarshal sqlCache object");
            synchronized (syncDebug) {
                try {
                    XmlTools.writeToFile(content1.getQueryArea().getSqlCache(), SiteUtils.getTempDir() + File.separatorChar + "member-content-site.xml", "windows-1251");
                } catch (Exception ee) {
                    log.info("Error write debug to file");
                }
            }
        }
        if (content1.getQueryArea().getRestrict() != null && content1.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE) {
            if (!Boolean.TRUE.equals(content1.getQueryArea().getSqlCache().getIsInit())) {
                SqlFromType fromType = new SqlFromType();
                fromType.setTable("WM_AUTH_USER");
                fromType.setAlias(MemberProcessingRenderRequest.aliasUserRestrict);
                content1.getQueryArea().getSqlCache().addFrom(fromType);
                content1.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(MemberProcessingRenderRequest.aliasUserRestrict, "USER_LOGIN", null, TypeFieldType.FIELD, null, null, "?", TypeFieldType.PARAMETER));
                addParameter(content1, "", SqlCheckParameterTypeTypeType.RESTRICT_USER, ParameterTypeType.NUMBER);
            }
        }
        if (mod1.getSelfLookup() != null && mod1.getSelfLookup().getCurrentField() != null && mod1.getSelfLookup().getTopField() != null) {
        }
        sql_ = "values (" + sql_ + ")";
        if (log.isDebugEnabled()) {
            log.debug("#11.1 insert SQL\n" + sql_1 + sql_);
            log.debug("#11.2 SqlCache() " + content1.getQueryArea().getSqlCache());
        }
        return sql_1 + sql_;
    }

    public static boolean checkRestrictField(ContentType content1, int restrictType) {
        RestrictDescription desc = null;
        for (final RestrictDescription newVar : restrictDesc) {
            if (newVar.type == restrictType) {
                desc = newVar;
                break;
            }
        }
        if (desc != null) {
            if (desc.nameField.equalsIgnoreCase(content1.getQueryArea().getPrimaryKey())) return true;
            for (int k = 0; k < content1.getQueryArea().getFieldsCount(); k++) {
                FieldsType ff = content1.getQueryArea().getFields(k);
                if (desc.nameField.equalsIgnoreCase(ff.getName()) && Boolean.TRUE.equals(ff.getIsShow())) return true;
            }
        }
        return false;
    }

    public static SqlClause buildSelectClause(ContentType contentMain, ContentType cnt, ModuleType module, DatabaseAdapter db_, String serverName, AuthSession authSession) {
        SqlClause sc = new SqlClause();
        if (!Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit())) {
            addParameterFromPK(contentMain, module.getName() + '.' + cnt.getQueryArea().getPrimaryKey());
            contentMain.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(MemberProcessingRenderRequest.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()), cnt.getQueryArea().getPrimaryKey(), null, TypeFieldType.FIELD, null, null, "?", TypeFieldType.PARAMETER));
        }
        boolean isAppend = false;
        for (int k = 0; k < cnt.getQueryArea().getFieldsCount(); k++) {
            FieldsType ff = cnt.getQueryArea().getFields(k);
            if (FieldsTypeJspTypeType.BIGTEXT_TYPE != ff.getJspType().getType()) {
                if (!isAppend) isAppend = true; else sc.select += ',';
                sc.select += (MemberProcessingRenderRequest.prepareTableAlias(ff.getRefTable(), module.getName()) + '.' + ff.getName());
            }
        }
        isAppend = false;
        for (int k = 0; k < cnt.getQueryArea().getTableCount(); k++) {
            TableType table = cnt.getQueryArea().getTable(k);
            if (!isAppend) isAppend = true; else sc.from += ',';
            sc.from += table.getTable() + ' ' + MemberProcessingRenderRequest.prepareTableAlias(table.getRef(), module.getName());
            if (!Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit())) {
                SqlFromType fromType = new SqlFromType();
                fromType.setTable(table.getTable());
                fromType.setAlias(MemberProcessingRenderRequest.prepareTableAlias(table.getRef(), module.getName()));
                contentMain.getQueryArea().getSqlCache().addFrom(fromType);
            }
        }
        if (!Boolean.TRUE.equals(module.getIsDelete())) {
            sc.where = MemberProcessingRenderRequest.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()) + ".IS_DELETED=0 ";
            if (!Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit())) {
                contentMain.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(MemberProcessingRenderRequest.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()), "IS_DELETED", null, TypeFieldType.FIELD, null, null, "0", TypeFieldType.DATA));
            }
        }
        if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE) {
            if (sc.where.length() != 0) sc.where += " and ";
            switch(db_.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();
                    sc.where += MemberProcessingRenderRequest.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()) + ".ID_FIRM in (" + idList + ") ";
                    break;
                default:
                    sc.where += MemberProcessingRenderRequest.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()) + ".ID_FIRM in ( " + "select " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, module.getName()) + ".ID_FIRM " + "from v$_read_list_firm " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, module.getName()) + " " + "where " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, module.getName()) + ".user_login=? " + ") ";
                    break;
            }
            if (!Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit())) {
                addParameter(contentMain, "", SqlCheckParameterTypeTypeType.RESTRICT_FIRM);
                SqlFromType fromType = new SqlFromType();
                fromType.setTable("v$_read_list_firm");
                fromType.setAlias(MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, module.getName()));
                contentMain.getQueryArea().getSqlCache().addFrom(fromType);
                contentMain.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(MemberProcessingRenderRequest.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()), "ID_FIRM", null, TypeFieldType.FIELD, MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, module.getName()), "ID_FIRM", null, TypeFieldType.FIELD));
                contentMain.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, module.getName()), "USER_LOGIN", null, TypeFieldType.FIELD, null, null, "?", TypeFieldType.PARAMETER));
            }
        }
        if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE) {
            if (sc.where.length() != 0) sc.where += " and ";
            switch(db_.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idSite = MemberTools.getGrantedSiteId(db_, serverName);
                    if (log.isDebugEnabled()) log.debug("siteId list: " + idSite);
                    sc.where += " ID_SITE in (" + idSite + ") ";
                    break;
                default:
                    sc.from += ", WM_PORTAL_VIRTUAL_HOST " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, module.getName()) + ' ';
                    sc.where += MemberProcessingRenderRequest.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()) + ".ID_SITE=" + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, module.getName()) + ".ID_SITE and " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, module.getName()) + ".name_virtual_host=lower(?) ";
                    break;
            }
            if (!Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit())) {
                addParameter(contentMain, "", SqlCheckParameterTypeTypeType.RESTRICT_SITE);
                SqlFromType fromType = new SqlFromType();
                fromType.setTable("WM_PORTAL_VIRTUAL_HOST ");
                fromType.setAlias(MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, module.getName()));
                contentMain.getQueryArea().getSqlCache().addFrom(fromType);
                contentMain.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(MemberProcessingRenderRequest.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()), "ID_SITE", null, TypeFieldType.FIELD, MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, module.getName()), "ID_SITE", null, TypeFieldType.FIELD));
                contentMain.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, module.getName()), "NAME_VIRTUAL_HOST", null, TypeFieldType.FIELD, null, null, "lower(?)", TypeFieldType.PARAMETER));
            }
        }
        if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE) {
            sc.from += ", WM_AUTH_USER " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, module.getName()) + ' ';
            if (sc.where.length() != 0) sc.where += " and ";
            sc.where += MemberProcessingRenderRequest.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()) + ".id_user=" + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, module.getName()) + ".id_user and " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, module.getName()) + ".user_login=? ";
            if (!Boolean.TRUE.equals(contentMain.getQueryArea().getSqlCache().getIsInit())) {
                addParameter(contentMain, "", SqlCheckParameterTypeTypeType.RESTRICT_USER);
                SqlFromType fromType = new SqlFromType();
                fromType.setTable("WM_AUTH_USER");
                fromType.setAlias(MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, module.getName()));
                contentMain.getQueryArea().getSqlCache().addFrom(fromType);
                contentMain.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(MemberProcessingRenderRequest.prepareTableAlias(contentMain.getQueryArea().getMainRefTable(), module.getName()), "ID_USER", null, TypeFieldType.FIELD, MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, module.getName()), "ID_USER", null, TypeFieldType.FIELD));
                contentMain.getQueryArea().getSqlCache().addWhere(MemberProcessingRenderRequest.getWhere(MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, module.getName()), "USER_LOGIN", null, TypeFieldType.FIELD, null, null, "?", TypeFieldType.PARAMETER));
            }
        }
        boolean isInitWhere = false;
        if (cnt.getQueryArea().getWhere() != null && cnt.getQueryArea().getWhere().length() > 0) {
            if (log.isDebugEnabled()) log.debug("join current WHERE condition with WHERE condition from LOOKUP module");
            if (sc.where.length() > 0) sc.where += " and ";
            sc.where += cnt.getQueryArea().getWhere();
            isInitWhere = true;
        }
        if (cnt.getQueryArea().getWhereCompelxCount() > 0) {
            if (isInitWhere) throw new IllegalStateException("You must define only one element neither <Where> or <WhereComplex>");
            if (log.isDebugEnabled()) log.debug("join current WHERE (WhereComptlex element) condition with WHERE condition from LOOKUP module");
            if (sc.where.length() > 0) sc.where += " and ";
            for (int i = 0; i < cnt.getQueryArea().getWhereCompelxCount(); i++) {
                SqlWhereType where = cnt.getQueryArea().getWhereCompelx(i);
                if (i != 0) sc.where += " and ";
                sc.where += (MemberProcessingRenderRequest.prepareTableAlias(where.getLeft().getAlias(), module.getName()) + '.' + where.getLeft().getColumn() + '=' + MemberProcessingRenderRequest.prepareTableAlias(where.getRight().getAlias(), module.getName()) + '.' + where.getRight().getColumn());
            }
        }
        isAppend = false;
        for (int k = 0; k < cnt.getQueryArea().getOrderCount(); k++) {
            SortOrderType order = cnt.getQueryArea().getOrder(k);
            if (!isAppend) isAppend = true; else sc.order += ',';
            String orderAlias = null;
            for (int j = 0; j < cnt.getQueryArea().getFieldsCount(); j++) {
                FieldsType ff = cnt.getQueryArea().getFields(j);
                if (order.getField().equals(ff.getName())) {
                    orderAlias = MemberProcessingRenderRequest.prepareTableAlias(ff.getRefTable(), module.getName()) + '.';
                    break;
                }
            }
            if (orderAlias == null) {
                if (log.isInfoEnabled()) log.info("Looking for order field '" + order.getField() + "' failed. May be need check member file");
                orderAlias = "";
            }
            sc.order += (orderAlias + order.getField() + ' ' + (order.getDirection() != null ? order.getDirection().toString() : ""));
        }
        if (log.isDebugEnabled()) {
            log.debug("buildSelectClause, select - " + sc.select);
            log.debug("buildSelectClause, from - " + sc.from);
            log.debug("buildSelectClause, where - " + sc.where);
            log.debug("buildSelectClause, order - " + sc.order);
        }
        return sc;
    }

    public static void addParameterFromPK(ContentType content, String name) {
        SqlCheckParameterType checkParameterType = new SqlCheckParameterType();
        checkParameterType.setParameter(name);
        ParameterTypeType type = ParameterTypeType.valueOf(content.getQueryArea().getPrimaryKeyType().toString());
        checkParameterType.setParameterType(type);
        checkParameterType.setType(SqlCheckParameterTypeTypeType.HTTP_REQUEST);
        content.getQueryArea().getSqlCache().addCheckParam(checkParameterType);
    }

    public static void addParameter(ContentType content, String name, SqlCheckParameterTypeTypeType typeParam) {
        addParameter(content, name, typeParam, ParameterTypeType.STRING);
    }

    private static void addParameter(ContentType content, String name, SqlCheckParameterTypeTypeType typeRestrictParam, ParameterTypeType typeParam) {
        SqlCheckParameterType checkParameterType = new SqlCheckParameterType();
        content.getQueryArea().getSqlCache().addCheckParam(checkParameterType);
        checkParameterType.setParameter(name);
        checkParameterType.setParameterType(typeParam);
        checkParameterType.setType(typeRestrictParam);
    }

    public static boolean checkRole(PortletRequest request, ContentType content_) {
        if (content_ == null || request == null) return false;
        for (int i = 0; i < content_.getSecurityRoleRefCount(); i++) {
            SecurityRoleRefType role = content_.getSecurityRoleRef(i);
            if (request.isUserInRole(role.getRoleName())) return true;
        }
        return false;
    }

    public static String buildUpdateSQL(DatabaseAdapter dbDyn, ContentType content, String fromParam, ModuleType mod, boolean isUsePrimaryKey, Map map, String remoteUser, String serverName, ModuleManager moduleManager, AuthSession authSession) throws Exception {
        if (content == null || content.getQueryArea() == null) return null;
        String insTable = (content.getQueryArea().getTable(0)).getTable();
        String sql_ = "update " + insTable + ' ' + " set ";
        boolean isNotComma = true;
        if (hasYesNoField(map, mod, content) && !isUsePrimaryKey) {
            if (log.isDebugEnabled()) log.debug("Build update clause for YES_1_NO_N");
            for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++) {
                FieldsType ff = content.getQueryArea().getFields(k);
                if (Boolean.TRUE.equals(ff.getIsShow()) && Boolean.TRUE.equals(ff.getIsEdit()) && (ff.getJspType().getType() == FieldsTypeJspTypeType.YES_1_NO_N_TYPE)) {
                    if (isNotComma) isNotComma = false; else sql_ += ',';
                    sql_ += ff.getName() + "=0";
                }
            }
        } else {
            for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++) {
                FieldsType ff = content.getQueryArea().getFields(k);
                if (Boolean.TRUE.equals(ff.getIsShow()) && Boolean.TRUE.equals(ff.getIsEdit()) && (ff.getJspType().getType() != FieldsTypeJspTypeType.BIGTEXT_TYPE)) {
                    if (isNotComma) isNotComma = false; else sql_ += ',';
                    sql_ += (ff.getName() + "=");
                    if (ff.getJspType().getType() == FieldsTypeJspTypeType.DATE_TEXT_TYPE) sql_ += dbDyn.getNameDateBind(); else sql_ += "?";
                }
            }
        }
        String where_ = "";
        SqlClause sc = new SqlClause();
        SqlClause lookupSc;
        String prevPK = null;
        String prevAlias = null;
        if (fromParam != null && fromParam.length() > 0) {
            StringTokenizer st = new StringTokenizer(fromParam, ",");
            while (st.hasMoreTokens()) {
                String modName = st.nextToken();
                ModuleType module = moduleManager.getModule(modName);
                ContentType cnt = getContent(module, ContentTypeActionType.INDEX_TYPE);
                if (cnt != null && cnt.getQueryArea() != null) {
                    lookupSc = buildSelectClause(content, cnt, module, dbDyn, serverName, authSession);
                    if (lookupSc.from.length() > 0 && lookupSc.select.length() > 0) {
                        if (sc.from.length() != 0) sc.from += ',';
                        sc.from += lookupSc.from;
                        if (sc.where.length() != 0) sc.where += " and ";
                        sc.where += MemberProcessingRenderRequest.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.' + cnt.getQueryArea().getPrimaryKey() + "=?";
                        if (prevPK != null) {
                            if (sc.where.length() != 0) sc.where += " and ";
                            sc.where += prevAlias + prevPK + "=" + MemberProcessingRenderRequest.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.' + prevPK;
                        }
                        if (lookupSc.where.length() > 0) {
                            if (sc.where.length() != 0) sc.where += " and ";
                            sc.where += lookupSc.where;
                        }
                        prevPK = cnt.getQueryArea().getPrimaryKey();
                        prevAlias = MemberProcessingRenderRequest.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.';
                    }
                }
            }
            if (prevPK != null) {
                if (where_.length() > 0) where_ += " and ";
                where_ += (prevPK + " in (" + processSubQuery(dbDyn, "select " + prevAlias + prevPK + " from " + sc.from + " where " + sc.where, fromParam, map, serverName, remoteUser, moduleManager) + ")");
            }
        }
        if (isUsePrimaryKey) {
            if (where_.length() > 0) where_ += " and ";
            where_ += content.getQueryArea().getPrimaryKey() + "=?";
        }
        if (!Boolean.TRUE.equals(mod.getIsDelete())) {
            if (where_.length() > 0) where_ += " and ";
            where_ += "is_deleted=0";
        }
        if (log.isDebugEnabled()) log.debug("start prepare self lookup");
        if (mod.getSelfLookup() != null && mod.getSelfLookup().getCurrentField() != null && mod.getSelfLookup().getTopField() != null) {
            if (where_.length() > 0) where_ += " and ";
            where_ += mod.getSelfLookup().getTopField().getName() + "=?";
        }
        if (log.isDebugEnabled()) log.debug("start prepare 'firm' restriction");
        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE) {
            if (where_.length() > 0) where_ += " and ";
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();
                    where_ += " ID_FIRM in (" + idList + ") ";
                    break;
                default:
                    where_ += "ID_FIRM in " + " (select " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, mod.getName()) + '.' + "ID_FIRM " + "  from v$_read_list_firm " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, mod.getName()) + "  where " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, mod.getName()) + '.' + "user_login = ? ) ";
                    break;
            }
        }
        if (log.isDebugEnabled()) log.debug("start prepare 'site' restriction");
        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE) {
            if (where_.length() > 0) where_ += " and ";
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    if (log.isDebugEnabled()) log.debug("get list of siteId for host " + serverName);
                    String idSite = MemberTools.getGrantedSiteId(dbDyn, serverName);
                    if (log.isDebugEnabled()) log.debug("siteId list: " + idSite);
                    where_ += " ID_SITE in (" + idSite + ") ";
                    break;
                default:
                    where_ += "ID_SITE in " + " (select " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, mod.getName()) + '.' + "ID_SITE " + "  from WM_PORTAL_VIRTUAL_HOST " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, mod.getName()) + "  where " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, mod.getName()) + ".NAME_VIRTUAL_HOST = lower(?) ) ";
                    break;
            }
        }
        if (log.isDebugEnabled()) log.debug("start prepare 'user' restriction");
        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE) {
            if (where_.length() > 0) where_ += " and ";
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idUser = authSession.getGrantedUserId();
                    where_ += " ID_USER in (" + idUser + ") ";
                    break;
                default:
                    where_ += "id_user in " + " (select " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, mod.getName()) + '.' + "id_user " + "  from  WM_AUTH_USER " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, mod.getName()) + "  where " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, mod.getName()) + ".user_login=? ) ";
                    break;
            }
        }
        SqlCacheType sqlCacheType = new SqlCacheType();
        sqlCacheType.setIsInit(Boolean.FALSE);
        content.getQueryArea().setSqlCache(sqlCacheType);
        log.debug("return SQL");
        return sql_ + (where_ != null && where_.trim().length() > 0 ? (" where " + where_) : "");
    }

    private static String processSubQuery(DatabaseAdapter adapter, String sql, String fromParam, Map map, String serverName, String remoteUser, ModuleManager moduleManager) throws Exception {
        if (log.isDebugEnabled()) log.debug("lookup query SQL: " + sql);
        PreparedStatement ps = null;
        ResultSet rs = null;
        switch(adapter.getFamaly()) {
            case DatabaseManager.MYSQL_FAMALY:
                try {
                    ps = adapter.prepareStatement(sql);
                    bindSubQueryParam(ps, 1, fromParam, adapter, map, serverName, remoteUser, moduleManager);
                    rs = ps.executeQuery();
                    String r = "";
                    while (rs.next()) {
                        if (r.length() != 0) r += ", ";
                        r += rs.getString(1);
                    }
                    if (r.length() == 0) return "-1"; else return r;
                } finally {
                    DatabaseManager.close(rs, ps);
                }
            default:
                return sql;
        }
    }

    public static boolean hasYesNoField(final Map map, final ModuleType mod1, final ContentType content1) {
        for (int k = 0; k < content1.getQueryArea().getFieldsCount(); k++) {
            FieldsType ff = content1.getQueryArea().getFields(k);
            if (ff.getJspType().getType() == FieldsTypeJspTypeType.YES_1_NO_N_TYPE && MapTools.getInt(map, mod1.getName() + '.' + getRealName(ff), 0) == 1) {
                if (log.isDebugEnabled()) log.debug("yes1-noN field - " + mod1.getName() + '.' + getRealName(ff) + " value - " + MapTools.getInt(map, mod1.getName() + '.' + getRealName(ff)));
                return true;
            }
        }
        return false;
    }

    public static String buildDeleteSQL(DatabaseAdapter dbDyn, ModuleType mod, ContentType content, String fromParam, Map map, String remoteUser, String serverName, ModuleManager moduleManager, AuthSession authSession) throws Exception {
        String insTable = content.getQueryArea().getTable(0).getTable();
        String sql_;
        if (Boolean.TRUE.equals(mod.getIsDelete())) {
            sql_ = "delete from " + insTable + ' ';
        } else {
            sql_ = "update " + insTable + ' ' + " set " + "is_deleted=1 ";
        }
        String where_ = "";
        SqlClause sc = new SqlClause();
        SqlClause lookupSc;
        String prevPK = null;
        String prevAlias = null;
        if (fromParam != null && fromParam.length() > 0) {
            StringTokenizer st = new StringTokenizer(fromParam, ",");
            while (st.hasMoreTokens()) {
                String modName = st.nextToken();
                ModuleType module = moduleManager.getModule(modName);
                ContentType cnt = getContent(module, ContentTypeActionType.INDEX_TYPE);
                if (cnt != null && cnt.getQueryArea() != null) {
                    lookupSc = buildSelectClause(content, cnt, module, dbDyn, serverName, authSession);
                    if (lookupSc.from.length() > 0 && lookupSc.select.length() > 0) {
                        if (sc.from.length() != 0) sc.from += ',';
                        sc.from += lookupSc.from;
                        if (sc.where.length() != 0) sc.where += " and ";
                        sc.where += MemberProcessingRenderRequest.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.' + cnt.getQueryArea().getPrimaryKey() + "=?";
                        if (prevPK != null) {
                            if (sc.where.length() != 0) sc.where += " and ";
                            sc.where += prevAlias + prevPK + "=" + MemberProcessingRenderRequest.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.' + prevPK;
                        }
                        if (lookupSc.where.length() > 0) {
                            if (sc.where.length() != 0) sc.where += " and ";
                            sc.where += lookupSc.where;
                        }
                        prevPK = cnt.getQueryArea().getPrimaryKey();
                        prevAlias = MemberProcessingRenderRequest.prepareTableAlias(cnt.getQueryArea().getMainRefTable(), modName) + '.';
                    }
                }
            }
            if (prevPK != null) {
                if (where_.length() > 0) where_ += " and ";
                where_ += prevPK + " in (" + processSubQuery(dbDyn, "select " + prevAlias + prevPK + " from " + sc.from + " where " + sc.where, fromParam, map, serverName, remoteUser, moduleManager) + ")";
            }
        }
        for (int k = 0; k < content.getQueryArea().getFieldsCount(); k++) {
            FieldsType ff = content.getQueryArea().getFields(k);
            if (ff.getJspType().getType() == FieldsTypeJspTypeType.YES_1_NO_N_TYPE) {
                if (log.isDebugEnabled()) log.debug("yes1-noN field - " + mod.getName() + '.' + getRealName(ff));
                if (where_.length() > 0) where_ += " and ";
                where_ += ff.getName() + "=0";
            }
        }
        if (where_.length() > 0) where_ += " and ";
        where_ += content.getQueryArea().getPrimaryKey() + "=?";
        if (!Boolean.TRUE.equals(mod.getIsDelete())) where_ += " and " + "is_deleted = 0";
        if (mod.getSelfLookup() != null && mod.getSelfLookup().getCurrentField() != null && mod.getSelfLookup().getTopField() != null) {
            where_ += " and " + mod.getSelfLookup().getTopField().getName() + "=?";
        }
        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE) {
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idList = authSession.getGrantedCompanyId();
                    where_ += " and ID_FIRM in (" + idList + ") ";
                    break;
                default:
                    where_ += " and " + "ID_FIRM in " + " (select " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, mod.getName()) + '.' + "ID_FIRM " + "  from v$_read_list_firm " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, mod.getName()) + "  where " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasFirmRestrict, mod.getName()) + '.' + "USER_LOGIN=? ) ";
                    break;
            }
        }
        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE) {
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idSite = MemberTools.getGrantedSiteId(dbDyn, serverName);
                    where_ += " and ID_SITE in (" + idSite + ") ";
                    break;
                default:
                    where_ += " and " + "ID_SITE in " + " (select " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, mod.getName()) + '.' + "ID_SITE " + "  from WM_PORTAL_VIRTUAL_HOST " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, mod.getName()) + "  where " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasSiteRestrict, mod.getName()) + ".NAME_VIRTUAL_HOST=lower(?) ) ";
                    break;
            }
        }
        if (content.getQueryArea().getRestrict() != null && content.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE) {
            switch(dbDyn.getFamaly()) {
                case DatabaseManager.MYSQL_FAMALY:
                    String idUser = authSession.getGrantedUserId();
                    where_ += " and ID_USER in (" + idUser + ") ";
                    break;
                default:
                    where_ += " and " + "ID_USER in " + " (select " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, mod.getName()) + '.' + "ID_USER " + "  from  WM_AUTH_USER" + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, mod.getName()) + "  where " + MemberProcessingRenderRequest.prepareTableAlias(MemberProcessingRenderRequest.aliasUserRestrict, mod.getName()) + ".USER_LOGIN=?) ";
                    break;
            }
        }
        return sql_ + (where_ != null && where_.trim().length() > 0 ? (" where " + where_) : "");
    }

    public static int bindSubQueryParam(PreparedStatement ps, int numParam, String fromParam, DatabaseAdapter dbDyn, Map map, String serverName, String remoteUser, ModuleManager moduleManager) throws Exception {
        if (StringUtils.isNotBlank(fromParam)) {
            StringTokenizer st = new StringTokenizer(fromParam, ",");
            while (st.hasMoreTokens()) {
                String modName = st.nextToken();
                ContentType cnt = moduleManager.getContent(modName, ContentTypeActionType.INDEX_TYPE);
                if (cnt != null && cnt.getQueryArea() != null) {
                    if (cnt.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.NUMBER_TYPE) {
                        final Long longParam = MapTools.getLong(map, modName + '.' + cnt.getQueryArea().getPrimaryKey());
                        if (log.isDebugEnabled()) log.debug("Param  #" + numParam + ", value: " + longParam);
                        RsetTools.setLong(ps, numParam++, longParam);
                    } else if (cnt.getQueryArea().getPrimaryKeyType().getType() == PrimaryKeyTypeType.STRING_TYPE) {
                        final String stringParam = RequestTools.getString(map, modName + '.' + cnt.getQueryArea().getPrimaryKey());
                        if (log.isDebugEnabled()) log.debug("Param  #" + numParam + ", value: " + stringParam);
                        ps.setString(numParam++, stringParam);
                    } else throw new Exception("Wrong type of primary key");
                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.FIRM_TYPE) {
                        switch(dbDyn.getFamaly()) {
                            case DatabaseManager.MYSQL_FAMALY:
                                break;
                            default:
                                if (log.isDebugEnabled()) log.debug("Param  #" + numParam + ", value: " + remoteUser);
                                ps.setString(numParam++, remoteUser);
                                break;
                        }
                    }
                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.SITE_TYPE) {
                        switch(dbDyn.getFamaly()) {
                            case DatabaseManager.MYSQL_FAMALY:
                                break;
                            default:
                                if (log.isDebugEnabled()) log.debug("Param  #" + numParam + ", value: " + serverName);
                                ps.setString(numParam++, serverName);
                                break;
                        }
                    }
                    if (cnt.getQueryArea().getRestrict() != null && cnt.getQueryArea().getRestrict().getType().getType() == RestrictTypeTypeType.USER_TYPE) {
                        switch(dbDyn.getFamaly()) {
                            case DatabaseManager.MYSQL_FAMALY:
                                break;
                            default:
                                if (log.isDebugEnabled()) log.debug("Param  #" + numParam + ", value: " + remoteUser);
                                ps.setString(numParam++, remoteUser);
                                break;
                        }
                    }
                }
            }
        }
        return numParam;
    }
}
