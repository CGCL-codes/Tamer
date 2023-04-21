package org.datascooter.impl;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.datascooter.bundle.DSMapper;
import org.datascooter.bundle.EntityBundle;
import org.datascooter.bundle.attribute.PersistAttribute;
import org.datascooter.bundle.attribute.PersistReferenceAttribute;
import org.datascooter.exception.DataManagerException;
import org.datascooter.exception.EntityNotMappedException;
import org.datascooter.exception.SnipManagerException;
import org.datascooter.inface.ISnipBuilder;
import org.datascooter.inface.ISnipManager;
import org.datascooter.meta.Column;
import org.datascooter.meta.MetaLink;
import org.datascooter.meta.MetaTable;
import org.datascooter.utils.DSSettings;
import org.datascooter.utils.LangUtils;
import org.datascooter.utils.SnipUtils;
import org.datascooter.utils.TypeUtils;
import org.datascooter.utils.policy.SnipType;

public class ClientTableManager {

    private ISnipManager snipManager;

    private boolean logDDL;

    private ISnipBuilder builder;

    private final DataManager dataManager;

    /**
	 * Constructs TableManager
	 * 
	 */
    public ClientTableManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.snipManager = dataManager.getSnipManager();
        logDDL = DSSettings.isLogDDL();
        builder = dataManager.getBuilder();
    }

    /**
	 * Provides procedure of verifying of database tables
	 * 
	 * @throws EntityNotMappedException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
    @SuppressWarnings("nls")
    public void verifyTables() throws SnipManagerException, SQLException, EntityNotMappedException, SecurityException, NoSuchMethodException {
        logAll("***Verification of tables  started  at " + TypeUtils.formatDate(new Date()));
        for (EntityBundle bundle : DSMapper.getBundleList()) {
            try {
                if (bundle.isUncheckable || bundle.getTableName() == null || bundle.getTableName().length() == 0) {
                    continue;
                }
                verifyTable(bundle);
                dataManager.getCasheManager().init(bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (DSSettings.isBuildForeignKeys()) {
            createKeys();
        }
        logAll("***Verification of tables  completed  at " + TypeUtils.formatDate(new Date()));
    }

    private void createKeys() throws EntityNotMappedException, SnipManagerException, SecurityException, NoSuchMethodException {
        List<EntityBundle> dest = new ArrayList<EntityBundle>();
        for (EntityBundle bundle : DSMapper.getBundleList()) {
            dest.add(bundle);
        }
        for (EntityBundle bundle : dest) {
            if (bundle.isUncheckable || bundle.getTableName() == null || bundle.getTableName().length() == 0) {
                continue;
            }
            for (Entry<String, PersistReferenceAttribute> entry : bundle.refEntityMap.entrySet()) {
                PersistReferenceAttribute value = entry.getValue();
                EntityBundle bundle1 = DSMapper.getBundle(value.getEntity());
                if (!value.getType().equals(bundle1.id.getType())) {
                    continue;
                }
                String format = MessageFormat.format(SnipUtils.FK, builder.getCaseSensitiveTableName(bundle.getTableName()), bundle.getTableName() + LangUtils.getRandomIndexString(3), value.getColumnName(), builder.getCaseSensitiveTableName(bundle1.getTableName()), bundle1.id.getColumnName());
                snipManager.execute(new Snip(null, SnipType.EXECUTE, format, null, null, null, -1, null));
            }
        }
    }

    /**
	 * Verifying table for assurance that entity may be saved
	 * 
	 * @throws BuildClauseException
	 * @throws EntityNotMappedException
	 * @throws SnipManagerException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
    public void verifyTable(EntityBundle bundleIn) throws SnipManagerException, EntityNotMappedException, SecurityException, NoSuchMethodException {
        if (bundleIn.table != null) {
            EntityBundle bundle = bundleIn;
            if (bundleIn.embeddable) {
                bundle = DSMapper.getBundle(bundleIn.parentEntity);
            }
            String tableName, schema = null;
            tableName = builder.getCaseSensitiveTableName(bundle.getTableName());
            if (tableName.contains(SnipUtils.DOT)) {
                String[] split = tableName.split(SnipUtils.DOT);
                if (split.length == 2) {
                    schema = split[0];
                    tableName = split[1];
                }
            } else {
                schema = null;
            }
            if (bundle.attrArray == null) {
                return;
            }
            if (!snipManager.isTableExists(schema, tableName)) {
                log(MessageFormat.format("   Table {0}  for class {1} not exists.", tableName, bundle.entity));
                snipManager.execute(builder.build(DataSnipFactory.create(bundle.entity), null));
                log(MessageFormat.format("   Table {0} for class {1} created successfully.", tableName, bundle.entity));
            } else {
                Map<String, Column> currentColumns = snipManager.getCurrentColumns(schema, tableName);
                for (PersistAttribute attribute : bundle.attrArray) {
                    checkColumn(bundle.entity, attribute, currentColumns);
                }
            }
            log(MessageFormat.format("Table {0} for class {1} verified.", tableName, bundle.entity));
        }
    }

    /**
	 * Check cokumn existence and type
	 * 
	 * @param entity
	 * @param attribute
	 * @param currentColumns
	 * @throws SnipManagerException
	 * @throws EntityNotMappedException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws BuildClauseException
	 */
    public void checkColumn(String entity, PersistAttribute attribute, Map<String, Column> currentColumns) throws SnipManagerException, EntityNotMappedException, SecurityException, NoSuchMethodException {
        Column column = currentColumns.get(attribute.getColumnName().toLowerCase());
        if (column != null) {
            if (!builder.equalType(attribute, column)) {
                logSituation(entity, attribute, column);
                if (column.partOfPrimary) {
                    throw new DataManagerException("***Part of primary key is differ than mapping : :  Entity: " + entity + "  attribute: " + attribute.name + "  column: " + column.name);
                } else {
                    snipManager.execute(builder.buildUpdateColumnSnip(entity, attribute));
                }
            }
        } else {
            snipManager.execute(builder.buildCreateColumnSnip(entity, attribute));
        }
    }

    private void logSituation(String entity, PersistAttribute attribute, Column column) {
        Logger.getLogger(this.getClass().getName()).warning(MessageFormat.format("***Attribute not equal column: entity:  {10}  builder class : {11}\nAttribute\n  {0}  type-  {2}  scale-  {3}  precision- {4} nullable- {5}  \n" + "Column\n  {1}  type-  {6}  scale-  {7}  precision- {8} nullable- {9}\n", attribute.name, column.name, attribute.getType(), attribute.getScale(), attribute.getPrecision(), attribute.isNullable(), column.dataTypeName, column.scale, column.precision, column.nullable(), entity, builder.getClass().getName()));
    }

    private void log(String message) {
        if (logDDL) {
            Logger.getLogger(this.getClass().getName()).info(snipManager.getName() + "   " + message);
        }
    }

    private void logAll(String message) {
        Logger.getLogger(this.getClass().getName()).info(snipManager.getName() + "   " + message);
    }

    public boolean tableExists(EntityBundle bundle) throws SnipManagerException {
        String tableName, schema = null;
        tableName = builder.getCaseSensitiveTableName(bundle.getTableName());
        if (tableName.contains(".")) {
            String[] split = tableName.split(SnipUtils.DOT);
            if (split.length == 2) {
                schema = split[0];
                tableName = split[1];
            }
        } else {
            schema = null;
        }
        return snipManager.isTableExists(schema, tableName);
    }

    public boolean tableExists(Object obj) throws SnipManagerException, EntityNotMappedException, SecurityException, NoSuchMethodException {
        if (obj instanceof EntityBundle) {
            return tableExists((EntityBundle) obj);
        }
        EntityBundle bundle = DSMapper.getBundle(obj);
        if (bundle.embeddable) {
            try {
                bundle = DSMapper.getBundle(bundle.parentEntity);
            } catch (EntityNotMappedException e) {
                e.printStackTrace();
                throw new DataManagerException(snipManager.getName() + "  -Not found parent-  " + bundle.entity, e);
            }
        }
        return tableExists(bundle);
    }

    public void dropAll(String shemaName, boolean include, String... tableNames) throws SnipManagerException {
        logAll("***Drop All  started - shema: " + shemaName + "  include: " + include + "  tableNames: " + Arrays.toString(tableNames) + "  at " + TypeUtils.formatDate(new Date()));
        dropKeys(shemaName);
        List<MetaTable> sorted = getSortedTables(shemaName, include, tableNames);
        Collections.reverse(sorted);
        Iterator<MetaTable> iterator = sorted.iterator();
        while (iterator.hasNext()) {
            String format = null;
            if (shemaName != null && shemaName.length() > 0) {
                format = MessageFormat.format(SnipUtils.DROP_TABLE, shemaName + SnipUtils.DOT + iterator.next().getName());
            } else {
                format = MessageFormat.format(SnipUtils.DROP_TABLE, iterator.next().getName());
            }
            snipManager.execute(new Snip(null, SnipType.EXECUTE, format, null, null, null, -1, null));
            log(format);
        }
        logAll("***Drop All  completed -" + " at " + TypeUtils.formatDate(new Date()));
    }

    private void dropKeys(String shemaName) throws SnipManagerException {
        for (MetaTable table : snipManager.getTables(shemaName)) {
            try {
                for (MetaLink link : table.getImportedKeys()) {
                    String keyClause = MessageFormat.format(SnipUtils.DROP_FK, link.FKTABLE_NAME, link.FK_NAME);
                    snipManager.execute(new Snip(null, SnipType.EXECUTE, keyClause, null, null, null, -1, null));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void clearAll(String shemaName, boolean include, String... tableNames) throws SnipManagerException {
        logAll("***Clear All  started -  shema: " + shemaName + "  include: " + include + "  tableNames: " + Arrays.toString(tableNames) + "  at " + TypeUtils.formatDate(new Date()));
        List<MetaTable> sorted = getSortedTables(shemaName, include, tableNames);
        Collections.reverse(sorted);
        Iterator<MetaTable> iterator = sorted.iterator();
        while (iterator.hasNext()) {
            String format = null;
            if (shemaName != null && shemaName.length() > 0) {
                format = MessageFormat.format(SnipUtils.DELETE_FROM, shemaName + SnipUtils.DOT + iterator.next().getName(), "", "");
            } else {
                format = MessageFormat.format(SnipUtils.DELETE_FROM, iterator.next().getName(), "", "");
            }
            snipManager.execute(new Snip(null, SnipType.EXECUTE, format, null, null, null, -1, null));
            logAll(format);
        }
        logAll("***Clear All  completed -" + " at " + TypeUtils.formatDate(new Date()));
    }

    private List<MetaTable> getSortedTables(String shemaName, boolean include, String... tableNames) throws SnipManagerException {
        List<MetaTable> tables = snipManager.getTables(shemaName);
        if (tableNames != null && tableNames.length > 0) {
            Arrays.sort(tableNames);
            Iterator<MetaTable> iterator = tables.iterator();
            while (iterator.hasNext()) {
                if (include) {
                    if (Arrays.binarySearch(tableNames, iterator.next().getName()) < 0) {
                        iterator.remove();
                    }
                } else {
                    if (Arrays.binarySearch(tableNames, iterator.next().getName()) >= 0) {
                        iterator.remove();
                    }
                }
            }
        }
        return LangUtils.sortTablesByReferences(tables);
    }
}
