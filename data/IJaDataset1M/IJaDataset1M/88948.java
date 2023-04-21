package de.evandor.easyc.bundlesGenerator.model;

import java.util.Map;

/**
 * A template for the model holds information about the
 * associated tablename, the identifier column (primary key)
 * and an ibator file.
 * 
 * 
 * <br>
 * $Date: 2008-10-07 12:51:16 $<br>
 * $Id: ModelTemplate.java,v 1.1 2008-10-07 12:51:16 carsten Exp $<br>
 * $Revision: 1.1 $<br>
 * 
 * @author Carsten Graef
 * 
 */
public class ModelTemplate {

    private String tableName;

    private String identifierColumn;

    private String ibatorFile;

    private String hibernateBuildFile;

    private Map<String, Column> columns;

    public Map<String, Column> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, Column> columns) {
        this.columns = columns;
    }

    public String getIbatorFile() {
        return ibatorFile;
    }

    public void setIbatorFile(String ibatorFile) {
        this.ibatorFile = ibatorFile;
    }

    public String getHibernateBuildFile() {
        return hibernateBuildFile;
    }

    public void setHibernateBuildFile(String hibernateBuildFile) {
        this.hibernateBuildFile = hibernateBuildFile;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdentifierColumn() {
        return identifierColumn;
    }

    public void setIdentifierColumn(String identifierColumn) {
        this.identifierColumn = identifierColumn;
    }
}
