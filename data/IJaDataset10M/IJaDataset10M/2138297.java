package com.once.server.data.export.sql;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.log4j.Logger;
import com.once.server.config.ConfigManager;
import com.once.server.data.IDataConstants;
import com.once.server.data.export.DataExportException;
import com.once.server.data.export.ExportDataWrapper;
import com.once.server.data.model.DataModelFactory;
import com.once.server.data.model.IDataFieldType;
import com.once.server.data.model.MetaModelFactory;
import com.once.server.data.model.sql.ISQLDataFieldInfo;
import com.once.server.data.model.sql.ISQLDataModel;
import com.once.server.data.model.sql.ISQLDataRelationInfo;
import com.once.server.data.source.sql.ISQLResults;
import com.once.server.data.source.sql.SQLDataSourceSelect;
import com.once.server.data.source.sql.SQLDataSourceUpdate;
import com.once.server.data.util.SQLUtils;
import com.once.server.security.IAuthenticator;
import com.once.server.security.SecurityFactory;

public class SQLExportMYOB extends SQLExportCommon {

    private static final Logger m_logger = Logger.getLogger(SQLExportMYOB.class);

    protected boolean m_addColumnNames;

    private static final String SYMBOL_NEW_LINE = String.valueOf((char) 13) + String.valueOf((char) 10);

    public SQLExportMYOB(boolean firstRowContainsColumnNames, String exportFileName) {
        m_addColumnNames = firstRowContainsColumnNames;
        m_exportFileName = exportFileName;
    }

    public File export(String loginSession, String name, String columnsPart, String wherePart, String sortPart, boolean distinct, long startRow, long rowsCount, List<String> groupColumnNames, List<String> constNames, List<String> constValues, String powerShiftStatusAlias) throws DataExportException {
        try {
            String powerShiftModel;
            ArrayList<String> powerShiftPrimaries;
            powerShiftPrimaries = new ArrayList<String>();
            if (powerShiftStatusAlias != null) {
                if (wherePart == null || wherePart.indexOf("\"" + IDataConstants.FIELD_NAME_PRIMARY + "\"") == -1) {
                    wherePart = ((wherePart != null && wherePart.length() > 0) ? "(" + wherePart + ") AND " : "") + "(" + quoteAlias(powerShiftStatusAlias) + " <> 't' OR " + quoteAlias(powerShiftStatusAlias) + " IS NULL)";
                }
                powerShiftModel = (powerShiftStatusAlias.indexOf(".") > -1) ? powerShiftStatusAlias.substring(0, powerShiftStatusAlias.indexOf(".")) : null;
            } else {
                powerShiftModel = null;
            }
            String originalColumnsPart;
            originalColumnsPart = columnsPart;
            if (columnsPart != null && columnsPart.length() > 0) {
                String[] sortColumns = columnsPart.split("\\,");
                columnsPart = "";
                for (Iterator<String> i = groupColumnNames.iterator(); i.hasNext(); ) {
                    String groupColumn = i.next();
                    for (int j = 0; j < sortColumns.length; j++) {
                        if (groupColumn.equals(sortColumns[j].split("\\s")[0])) {
                            groupColumn = sortColumns[j];
                            sortColumns[j] = null;
                            break;
                        }
                    }
                    columnsPart = columnsPart + groupColumn + ", ";
                }
                for (int j = 0; j < sortColumns.length; j++) {
                    if (sortColumns[j] != null) {
                        ISQLDataRelationInfo relation = (ISQLDataRelationInfo) MetaModelFactory.getInstance().getMetaModel(name.substring(0, name.indexOf("."))).getRelation(name.substring(name.indexOf(".") + 1));
                        String eachColumn = dequoteAlias(sortColumns[j].trim());
                        if (eachColumn.indexOf(".") == -1) continue;
                        String modelName = eachColumn.substring(0, eachColumn.indexOf("."));
                        String aliasName = eachColumn.substring(eachColumn.indexOf(".") + 1);
                        ISQLDataModel model;
                        model = relation.getModel(modelName);
                        if (model == null) continue;
                        if (model.getDataField(aliasName) == null) continue;
                        if (columnsPart.endsWith(", ") == false && columnsPart.length() > 0) columnsPart += ", ";
                        columnsPart = columnsPart + sortColumns[j] + ", ";
                    }
                }
                if (columnsPart.endsWith(", ")) {
                    columnsPart = columnsPart.substring(0, columnsPart.length() - 2);
                }
            }
            if (groupColumnNames == null) groupColumnNames = new ArrayList<String>();
            String newColumn;
            if (columnsPart != null && columnsPart.length() > 0) {
                for (Iterator<String> i = groupColumnNames.iterator(); i.hasNext(); ) {
                    newColumn = (String) i.next();
                    if (columnsPart.indexOf(newColumn) == -1) {
                        if (columnsPart.endsWith(", ") == false && columnsPart.length() > 0) columnsPart += ", ";
                        columnsPart += newColumn;
                    }
                }
            }
            HashMap<String, ISQLDataFieldInfo> allAliases = new HashMap<String, ISQLDataFieldInfo>();
            SQLDataSourceSelect src;
            if (name.indexOf(".") != -1) {
                ISQLDataRelationInfo relation = (ISQLDataRelationInfo) MetaModelFactory.getInstance().getMetaModel(name.substring(0, name.indexOf("."))).getRelation(name.substring(name.indexOf(".") + 1));
                List<ISQLDataModel> models = relation.getModels();
                for (Iterator<ISQLDataModel> m = models.iterator(); m.hasNext(); ) {
                    ISQLDataModel model = m.next();
                    List<ISQLDataFieldInfo> aliases = model.getSQLDataFields();
                    for (Iterator<ISQLDataFieldInfo> i = aliases.iterator(); i.hasNext(); ) {
                        ISQLDataFieldInfo alias = i.next();
                        allAliases.put(SQLUtils.constructAliasName(model.getName(), alias.getAlias()), alias);
                    }
                }
                src = new SQLDataSourceSelect(relation);
            } else {
                ISQLDataModel model = (ISQLDataModel) DataModelFactory.getInstance().getDataModel(name);
                List<ISQLDataFieldInfo> aliases = model.getSQLDataFields();
                for (Iterator<ISQLDataFieldInfo> i = aliases.iterator(); i.hasNext(); ) {
                    ISQLDataFieldInfo alias = (ISQLDataFieldInfo) i.next();
                    allAliases.put(SQLUtils.constructAliasName(model.getName(), alias.getAlias()), alias);
                }
                src = new SQLDataSourceSelect(model);
            }
            List<ISQLDataModel> allModels = src.getAllModels();
            String mainPrimaryAliasName = SQLUtils.constructAliasName(((ISQLDataModel) allModels.get(0)).getName(), IDataConstants.FIELD_NAME_PRIMARY);
            if (columnsPart != null && columnsPart.length() > 0 && columnsPart.indexOf(mainPrimaryAliasName) == -1) {
                columnsPart += ", " + mainPrimaryAliasName;
            }
            if (powerShiftModel != null) {
                if (columnsPart != null && columnsPart.indexOf(quoteAlias(powerShiftModel + "." + IDataConstants.FIELD_NAME_PRIMARY)) == -1) {
                    if (columnsPart.length() > 0) {
                        columnsPart += ", ";
                    }
                    columnsPart += quoteAlias(powerShiftModel + "." + IDataConstants.FIELD_NAME_PRIMARY);
                }
            }
            int index;
            ISQLResults res = src.execute(loginSession, columnsPart, wherePart, sortPart, distinct, startRow, rowsCount);
            File file = createExportFile(ExportDataWrapper.EXPORT_TYPE_CSV);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            String[] columnList = originalColumnsPart.split(",");
            for (index = 0; index < columnList.length; index++) {
                columnList[index] = columnList[index].trim();
            }
            if (m_addColumnNames) {
                String columnHeaders;
                String columnValue;
                columnHeaders = "";
                for (index = 0; index < columnList.length; index++) {
                    columnValue = dequoteAlias(columnList[index]);
                    if (columnValue.indexOf(".") > -1) columnValue = columnValue.substring(columnValue.indexOf(".") + 1);
                    if (index > 0) columnHeaders += ",";
                    columnHeaders += columnValue;
                }
                writer.write(columnHeaders + ((groupColumnNames.size() > 0) ? "" : SYMBOL_NEW_LINE));
            }
            for (index = 0; index < columnList.length; index++) {
                columnList[index] = dequoteAlias(columnList[index]);
            }
            Map<String, String> currGroup = new HashMap<String, String>(groupColumnNames.size());
            Map<String, String> prevGroup = new HashMap<String, String>(groupColumnNames.size());
            for (Iterator<String> i = groupColumnNames.iterator(); i.hasNext(); ) {
                String groupColumn = i.next();
                while (groupColumn.indexOf("\"") > -1) {
                    groupColumn = groupColumn.substring(0, groupColumn.indexOf("\"")) + groupColumn.substring(groupColumn.indexOf("\"") + 1);
                }
                currGroup.put(groupColumn, null);
                prevGroup.put(groupColumn, null);
            }
            String csvLine;
            while (res.next()) {
                csvLine = "";
                for (Iterator<String> i = groupColumnNames.iterator(); i.hasNext(); ) {
                    String column = i.next();
                    while (column.indexOf("\"") > -1) {
                        column = column.substring(0, column.indexOf("\"")) + column.substring(column.indexOf("\"") + 1);
                    }
                    prevGroup.put(column, currGroup.get(column));
                }
                for (int i = 0; i < res.getColumnsCount(); i++) {
                    String column = res.getColumnName(i);
                    if (currGroup.containsKey(column)) {
                        currGroup.put(column, res.getString(i));
                    }
                }
                if (groupColumnNames.size() > 0) {
                    for (Iterator<String> i = groupColumnNames.iterator(); i.hasNext(); ) {
                        String column = (String) i.next();
                        while (column.indexOf("\"") > -1) {
                            column = column.substring(0, column.indexOf("\"")) + column.substring(column.indexOf("\"") + 1);
                        }
                        String curr = currGroup.get(column);
                        String prev = prevGroup.get(column);
                        if (curr == null) {
                            if (prev != null) {
                                csvLine += SYMBOL_NEW_LINE;
                                break;
                            }
                            continue;
                        }
                        if (!curr.equals(prev)) {
                            csvLine += SYMBOL_NEW_LINE;
                            break;
                        }
                    }
                }
                String csvValue;
                int scan;
                boolean found;
                csvValue = "";
                for (index = 0; index < columnList.length; index++) {
                    if (index > 0) {
                        csvLine += ",";
                    }
                    if (res.isColumnPresent(columnList[index]) == true) {
                        csvValue = formatValue(columnList[index], res.getString(columnList[index]), allAliases);
                    } else {
                        found = false;
                        for (scan = 0; scan < constNames.size(); scan++) {
                            if (columnList[index].equals(constNames.get(scan)) == true) {
                                found = true;
                                csvValue = (String) constValues.get(scan);
                            }
                        }
                        if (found == false) {
                            csvValue = "#ERROR#";
                        }
                    }
                    if (csvValue == null) {
                        csvValue = "";
                    }
                    csvLine += csvValue;
                }
                powerShiftPrimaries.add(powerShiftPrimaries.size(), res.getString(powerShiftModel + "." + IDataConstants.FIELD_NAME_PRIMARY));
                writer.write(csvLine + SYMBOL_NEW_LINE);
            }
            writer.close();
            if (powerShiftStatusAlias != null) {
                SQLDataSourceUpdate powerShifter;
                HashMap<String, String> rowValues;
                ISQLDataModel powerModel;
                ISQLDataRelationInfo relation;
                String powerWherePart;
                if (powerShiftPrimaries.size() > 0) {
                    powerWherePart = quoteAlias(powerShiftModel + "." + IDataConstants.FIELD_NAME_PRIMARY) + " IN (";
                    for (index = 0; index < powerShiftPrimaries.size(); index++) {
                        if (index > 0) {
                            powerWherePart += ", ";
                        }
                        powerWherePart += (String) powerShiftPrimaries.get(index);
                    }
                    powerWherePart += ")";
                } else {
                    powerWherePart = quoteAlias(powerShiftModel + "." + IDataConstants.FIELD_NAME_PRIMARY) + " IS NULL";
                }
                relation = (ISQLDataRelationInfo) MetaModelFactory.getInstance().getMetaModel(name.substring(0, name.indexOf("."))).getRelation(name.substring(name.indexOf(".") + 1));
                if (powerShiftStatusAlias.indexOf(".") > -1) {
                    powerModel = (ISQLDataModel) relation.getModel(powerShiftStatusAlias.substring(0, powerShiftStatusAlias.indexOf(".")));
                } else {
                    powerModel = (ISQLDataModel) relation.getModel(powerShiftStatusAlias);
                }
                if (powerModel != null) {
                    powerShifter = new SQLDataSourceUpdate(powerModel);
                    rowValues = new HashMap<String, String>();
                    rowValues.put(quoteAlias(powerShiftStatusAlias), "t");
                    powerShiftModel = quoteAlias(powerShiftStatusAlias).substring(0, quoteAlias(powerShiftStatusAlias).indexOf("."));
                    IAuthenticator authentication = SecurityFactory.getInstance().getAuthenticator();
                    powerShifter.execute(loginSession, "(" + powerWherePart + ") AND " + powerShiftModel + ".\"" + IDataConstants.FIELD_NAME_OWNER_ORGANISATION + "\" = '" + authentication.getUserOwnerOrganisation(loginSession) + "'", rowValues);
                }
            }
            return file;
        } catch (Throwable t) {
            m_logger.error(t, t);
            throw new DataExportException("Unable to perform export to MYOB", t);
        }
    }

    protected String formatValue(String aliasName, String aliasValue, Map<String, ISQLDataFieldInfo> allAliases) {
        ConfigManager settings;
        String result;
        TimeZone oldDefault;
        aliasName = quoteAlias(aliasName);
        if (aliasName != null) {
            if (IDataConstants.FIELD_VALUE_NULL.equalsIgnoreCase(aliasValue) == false) {
                if (getAliasType(aliasName, allAliases).equals(IDataFieldType.DATE) == true) {
                    if (aliasValue.indexOf(".") > -1) {
                        aliasValue = aliasValue.substring(0, aliasValue.indexOf("."));
                    }
                    Date date = new Date(new Long(aliasValue).longValue() * 1000);
                    settings = ConfigManager.getInstance();
                    oldDefault = TimeZone.getDefault();
                    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
                    SimpleDateFormat df = new SimpleDateFormat(settings.getExportDateFormat());
                    aliasValue = df.format(date);
                    TimeZone.setDefault(oldDefault);
                }
                if (getAliasType(aliasName, allAliases).equals(IDataFieldType.FLOAT) == true) {
                    aliasValue = "" + (((float) Math.round(Float.parseFloat(aliasValue) * 100)) / 100);
                }
            }
        }
        if (aliasValue != null) {
            result = aliasValue.replaceAll("\"", "\"\"").replace((char) 1, (char) 32);
            if (aliasValue.indexOf(",") != -1 || aliasValue.indexOf("\"") != -1) {
                result = "\"" + result + "\"";
            }
            if (IDataConstants.FIELD_VALUE_NULL.equals(result) == true) {
                result = "";
            }
        } else {
            result = "";
        }
        return result;
    }

    protected String getAliasType(String aliasName, Map<String, ISQLDataFieldInfo> allAliases) {
        ISQLDataFieldInfo alias = allAliases.get(aliasName);
        return alias != null ? alias.getType() : null;
    }

    protected String quoteAlias(String alias) {
        String quotedAlias;
        if (alias != null) {
            if (alias.indexOf(".") > -1) {
                quotedAlias = alias.substring(0, alias.indexOf(".")) + "\".\"" + alias.substring(alias.indexOf(".") + 1);
            } else {
                quotedAlias = alias;
            }
            quotedAlias = "\"" + quotedAlias + "\"";
        } else {
            quotedAlias = null;
        }
        return quotedAlias;
    }

    private String dequoteAlias(String alias) {
        if (alias.indexOf("\".\"") > -1) {
            alias = alias.substring(1, alias.indexOf("\".\"")) + "." + alias.substring(alias.indexOf("\".\"") + 3, alias.length() - 1);
        } else {
            if (alias.indexOf("\"") == 0) {
                alias = alias.substring(1, alias.length() - 1);
            }
        }
        return alias;
    }
}
