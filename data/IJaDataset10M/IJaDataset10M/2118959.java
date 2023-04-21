package org.streets.eis.ext.analysis.internal.impl;

import org.slf4j.Logger;
import org.streets.commons.util.StringUtils;
import org.streets.database.SQLConnection;
import org.streets.database.datadict.ColumnDefinition;
import org.streets.database.datadict.DataDict;
import org.streets.database.datadict.TableDefinition;
import org.streets.database.datadict.TableRelations;
import org.streets.eis.entity.User;
import org.streets.eis.ext.analysis.entities.JoinTable;
import org.streets.eis.ext.analysis.entities.QueryStruct;

public class RecordPermitSqlExtenderImpl extends PermitSqlExtenderImpl {

    private static final long serialVersionUID = 1L;

    private static final Integer PERMIT_REJECT = 0;

    private static final Integer PERMIT_READONLY = 1;

    private static final Integer PERMIT_ALL = 2;

    private static final String ORGANID = "organid";

    public RecordPermitSqlExtenderImpl(Logger logger, DataDict dict, TableRelations relations, SQLConnection connection) {
        super(logger, dict, relations, connection);
    }

    /**
	 * 给表加上限制条件
	 */
    @Override
    public QueryStruct extend(User user, QueryStruct struct) {
        for (JoinTable table : struct.getTables()) {
            String recordRestriction = buildRecordRestrictions(user, table);
            table.setRestrictions(recordRestriction);
        }
        return struct;
    }

    /**
	 * 给表记录添加权限限制：
	 * 1.限定ID范围;
	 * 		table.id in (select record_id from eis_role_record_perms 
	 *      where entity_name = 'tableName' and perm_value = 1 
	 *      and (role_id = 'role' or.....))
	 * 2.限定组织机构范围;
	 * 		table.orgid in(= user.role2.orgid or table.orgid=user.role2.orgid;
	 * 
	 */
    private String buildRecordRestrictions(User user, JoinTable table) {
        String s1 = appendRecordRestrictionByRole(user, table.getTableDef().getName(), table.getAlias());
        String s2 = appendRecordRestrictionByOrg(user, table.getTableDef(), table.getAlias());
        if ((!(StringUtils.isEmpty(s1))) && (!(StringUtils.isEmpty(s2)))) {
            return "(" + s1 + ") and (" + s2 + ")";
        } else if ((!(StringUtils.isEmpty(s1))) && ((StringUtils.isEmpty(s2)))) {
            return s1;
        }
        if (((StringUtils.isEmpty(s1))) && (!(StringUtils.isEmpty(s2)))) {
            return s2;
        }
        return null;
    }

    /**
	 * 添加角色记录过滤权限
	 * @param user
	 * @param node
	 */
    private String appendRecordRestrictionByRole(User user, String tableName, String alias) {
        StringBuffer sb = new StringBuffer();
        String tmp = buildRecordRestrictionByRole(user, tableName, PERMIT_READONLY, PERMIT_ALL);
        if (!(StringUtils.isEmpty(tmp))) {
            if (StringUtils.isEmpty(alias)) {
                sb.append(" (" + tableName + ".id in (" + tmp + ")) and");
            } else {
                sb.append(" (" + alias + ".id in (" + tmp + ")) and");
            }
        }
        if (sb.length() > 3) {
            sb.setLength(sb.length() - 3);
        }
        return sb.toString();
    }

    /**
	 * 添加组织机构权限
	 * @param user
	 * @param node
	 */
    private String appendRecordRestrictionByOrg(User user, TableDefinition tableDef, String alias) {
        StringBuffer sb = new StringBuffer();
        if (hasColumn(tableDef, ORGANID)) {
            String tmp = buildRecordRestrictionByOrg(user, PERMIT_READONLY, PERMIT_ALL);
            if (!(StringUtils.isEmpty(tmp))) {
                if (StringUtils.isEmpty(alias)) {
                    sb.append(" (" + tableDef.getName() + "." + ORGANID + " in (" + tmp + ")) and");
                } else {
                    sb.append(" (" + alias + "." + ORGANID + " in (" + tmp + ")) and");
                }
            }
        }
        if (sb.length() > 3) {
            sb.setLength(sb.length() - 3);
        }
        return sb.toString();
    }

    private String buildRecordRestrictionByRole(User user, String tableName, Integer... permValues) {
        String[] roles = StringUtils.split(user.getRoleIds(), ",");
        if (roles.length > 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(" select record_id from eis_role_record_perms ");
            sb.append(" where entity_name = " + StringUtils.quote(tableName).toUpperCase());
            String perms = buildPermitValueRestriction(permValues);
            if (!StringUtils.isEmpty(perms)) {
                sb.append(" and (" + perms + ")");
            }
            String roleIds = buildRoleIdRestriction(roles);
            if (!StringUtils.isEmpty(roleIds)) {
                sb.append(" and (" + roleIds + ")");
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    private String buildRecordRestrictionByOrg(User user, Integer... permValues) {
        String[] roles = StringUtils.split(user.getRoleIds(), ",");
        if (roles.length > 0) {
            StringBuffer sb = new StringBuffer();
            sb.append(" select ORGAN_ID from EIS_ROLE_ORGAN_PERMS ");
            sb.append(" where 1=1 ");
            String perms = buildPermitValueRestriction(permValues);
            if (!StringUtils.isEmpty(perms)) {
                sb.append(" and (" + perms + ")");
            }
            String roleIds = buildRoleIdRestriction(roles);
            if (!StringUtils.isEmpty(roleIds)) {
                sb.append(" and (" + roleIds + ")");
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    private boolean hasColumn(TableDefinition table, String columnName) {
        for (ColumnDefinition column : table.getColumns()) {
            if (columnName.equalsIgnoreCase(column.getName())) {
                return true;
            }
        }
        return false;
    }
}
