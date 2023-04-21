package com.agimatec.dbtransform;

import com.agimatec.commons.config.MapNode;
import com.agimatec.commons.generator.FreemarkerFileGenerator;
import com.agimatec.sql.meta.*;
import com.agimatec.sql.meta.script.DDLExpressions;
import com.agimatec.sql.meta.script.DDLScriptSqlMetaFactory;
import com.agimatec.sql.meta.script.ExtractExpr;
import freemarker.template.TemplateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Description: Transform a single sql script<br/>
 * User: roman.stumm <br/>
 * Date: 17.12.2007 <br/>
 * Time: 13:36:21 <br/>
 * Copyright: Agimatec GmbH
 */
public class ScriptTransformator extends DDLScriptSqlMetaFactory {

    private static final Log log = LogFactory.getLog(ScriptTransformator.class);

    private final PrintWriter target;

    private final CatalogConversion catalogConversion;

    private final FreemarkerFileGenerator templateEngine;

    public ScriptTransformator(DDLExpressions ddlSpecification, PrintWriter target, CatalogConversion catalogConversion, FreemarkerFileGenerator templateEngine) {
        super(ddlSpecification);
        this.target = target;
        this.catalogConversion = catalogConversion;
        this.templateEngine = templateEngine;
    }

    @Override
    protected void init() {
        getBuilders().put("table-alter-columns", new TableAlterColumnsBuilder());
        getBuilders().put("drop-trigger", new DropTriggerBuilder());
        getBuilders().put("dezign-create-table", new TransformDezignCreateTableBuilder());
        getBuilders().put("drop-table", new DropTableBuilder());
        getBuilders().put("create-index", new CreateIndexBuilder());
    }

    public void visitComment(String theComment) throws SQLException {
        target.println(theComment);
    }

    /**
     * parse the statement and create the adequate parts of the Catalog
     *
     * @param statement - a DDL statement (Oracle syntax)
     * @return 0
     * @throws SQLException
     */
    public int visitStatement(String statement) throws SQLException {
        int found = 0;
        for (ExtractExpr theExpr : getDdlSpec().getExpressions()) {
            Map values = getExtractor().extract(statement, theExpr);
            if (values != null) {
                found++;
                if (log.isDebugEnabled()) {
                    log.debug("FOUND " + theExpr.getName() + " in: " + statement);
                    log.debug(values);
                }
                CatalogBuilder builder = getBuilders().get(theExpr.getName());
                if (builder != null) {
                    target.println("\n-- from postgres: [ " + statement + ";]");
                    try {
                        builder.process(new MapNode(values), getCatalog());
                    } catch (Exception e) {
                        log.error("error processing " + values, e);
                    }
                }
                break;
            }
        }
        if (found == 0) {
            target.println(statement + ";");
        }
        return 0;
    }

    public void doCommit() throws SQLException {
        target.println("COMMIT;");
    }

    public void doRollback() throws SQLException {
        target.println("ROLLBACK;");
    }

    protected class TransformDezignCreateTableBuilder extends DezignCreateTableBuilder {

        public void process(MapNode values, CatalogDescription catalog) throws IOException, TemplateException {
            super.process(values, catalog);
            final String tableName = strip(values.getString("table"));
            final TableDescription td = getTable(catalog, tableName);
            TableDescription newTd = td.deepCopy();
            catalogConversion.transformTable(newTd, td);
            templateEngine.putModel("table", newTd);
            templateEngine.setTemplateName("create-table.ftl");
            templateEngine.generate(target);
        }
    }

    protected class CreateIndexBuilder extends CatalogBuilder {

        @Override
        public void process(MapNode map, CatalogDescription catalog) throws IOException, TemplateException {
            IndexDescription index = new IndexDescription();
            index.setIndexName(strip(map.getString("indexName")));
            List<Map<String, String>> cols = map.getList("columns");
            for (Map<String, String> col : cols) {
                index.addColumn(col.get("column"), col.get("desc"));
            }
            index.setTableName(strip(map.getString("table")));
            index.setTableSpace(strip(map.getString("tableSpace")));
            index.setUnique("UNIQUE".equalsIgnoreCase(map.getString("unique")));
            templateEngine.putModel("index", index);
            templateEngine.setTemplateName("create-index.ftl");
            templateEngine.generate(target);
        }
    }

    protected class TableAlterColumnsBuilder extends CatalogBuilder {

        @Override
        public void process(MapNode values, CatalogDescription catalog) throws IOException, TemplateException {
            TableDescription td = getTable(catalog, values.getString("table"));
            List columns = values.getList("tableElement");
            templateEngine.putModel("table", td);
            int idx = 0;
            for (Object column : columns) {
                if (idx > 0) target.println(",");
                Map map = (Map) column;
                if (map.containsKey("add-column")) {
                    genAddColumn(map, td);
                } else if (map.containsKey("alter-column-type")) {
                    genAlterColumnType(map);
                } else if (map.containsKey("alter-column-drop-notnull")) {
                    genColumnDropNotNull(map);
                } else if (map.containsKey("alter-column-set-notnull")) {
                    genColumnSetNotNull(map);
                } else if (map.containsKey("constraint")) {
                    genAddConstraint(map, values);
                } else if (map.containsKey("add-foreign-key")) {
                    genAddForeignKey(map, values);
                } else if (map.containsKey("drop-column")) {
                    genDropColumn(map);
                } else if (map.containsKey("drop-constraint")) {
                    genDropConstraint(map);
                }
            }
        }

        private void genDropConstraint(Map map) throws IOException, TemplateException {
            MapNode node = new MapNode((Map) map.get("drop-constraint"));
            String colName = node.getString("constraintName");
            templateEngine.setTemplateName("alter-table-drop-constraint.ftl");
            templateEngine.putModel("constraintName", colName);
            templateEngine.generate(target);
        }

        private void genDropColumn(Map map) throws IOException, TemplateException {
            MapNode node = new MapNode((Map) map.get("drop-column"));
            String colName = node.getString("column");
            templateEngine.setTemplateName("alter-table-drop-column.ftl");
            templateEngine.putModel("columnName", colName);
            templateEngine.generate(target);
        }

        private void genAddForeignKey(Map map, MapNode values) throws IOException, TemplateException {
            MapNode node = new MapNode((Map) map.get("add-foreign-key"));
            ForeignKeyDescription fk = new ForeignKeyDescription();
            fk.setConstraintName(strip(node.getString("constraintName")));
            fk.setOnDeleteRule(strip(node.getString("onDeleteRule")));
            fk.setRefTableName(strip(node.getString("refTable")));
            fk.setTableName(strip(values.getString("table")));
            fk.setTableSpace(strip(node.getString("tableSpace/tableSpace")));
            List icols = node.getList("columns");
            List rcols = node.getList("refcolumns");
            int i = 0;
            for (Object icol : icols) {
                Map eachCol = (Map) icol;
                Map rcol = (Map) rcols.get(i++);
                fk.addColumnPair(strip((String) eachCol.get("column")), strip((String) rcol.get("column")));
            }
            templateEngine.setTemplateName("alter-table-add-foreignkey.ftl");
            ForeignKeyDescription newFk = fk.deepCopy();
            catalogConversion.transformForeignKey(newFk, fk);
            templateEngine.putModel("foreignKey", newFk);
            templateEngine.generate(target);
        }

        private void genAddConstraint(Map map, MapNode values) throws IOException, TemplateException {
            MapNode node = new MapNode((Map) map.get("constraint"));
            IndexDescription id = new IndexDescription();
            id.setTableName(strip(values.getString("table")));
            id.setTableSpace(strip(node.getString("tableSpace/tableSpace")));
            id.setIndexName(strip(node.getString("constraintName")));
            id.setUnique(getBool(node, "unique"));
            List icols = node.getList("columns");
            for (Object icol : icols) {
                Map eachCol = (Map) icol;
                id.addColumn(strip((String) eachCol.get("column")));
            }
            templateEngine.setTemplateName("alter-table-add-constraint.ftl");
            templateEngine.putModel("index", id);
            templateEngine.generate(target);
        }

        private void genColumnSetNotNull(Map map) throws IOException, TemplateException {
            MapNode node = new MapNode((Map) map.get("alter-column-set-notnull"));
            String colName = node.getString("column");
            templateEngine.setTemplateName("alter-table-set-notnull.ftl");
            templateEngine.putModel("columnName", colName);
            templateEngine.generate(target);
        }

        private void genColumnDropNotNull(Map map) throws IOException, TemplateException {
            MapNode node = new MapNode((Map) map.get("alter-column-drop-notnull"));
            String colName = node.getString("column");
            templateEngine.setTemplateName("alter-table-drop-notnull.ftl");
            templateEngine.putModel("columnName", colName);
            templateEngine.generate(target);
        }

        private void genAlterColumnType(Map map) throws IOException, TemplateException {
            MapNode node = new MapNode((Map) map.get("alter-column-type"));
            String colName = node.getString("column");
            ColumnDescription colDef = new ColumnDescription();
            colDef.setNullable(true);
            colDef.setColumnName(colName);
            setColType(node, colDef);
            ColumnDescription newColDef = colDef.deepCopy();
            catalogConversion.transformColumn(newColDef, colDef);
            templateEngine.putModel("column", newColDef);
            templateEngine.setTemplateName("alter-table-modify-column.ftl");
            templateEngine.generate(target);
        }

        private void genAddColumn(Map map, TableDescription td) throws IOException, TemplateException {
            ColumnDescription colDef = buildColumnDescription(new MapNode((Map) map.get("add-column")), td);
            ColumnDescription newColDef = colDef.deepCopy();
            catalogConversion.transformColumn(newColDef, colDef);
            templateEngine.putModel("column", newColDef);
            templateEngine.setTemplateName("alter-table-add-column.ftl");
            templateEngine.generate(target);
        }
    }

    protected class DropTriggerBuilder extends CatalogBuilder {

        @Override
        public void process(MapNode values, CatalogDescription catalog) throws IOException, TemplateException {
            templateEngine.putModel("trigger", values.getString("trigger"));
            templateEngine.setTemplateName("drop-trigger.ftl");
            templateEngine.generate(target);
        }
    }

    protected class DropTableBuilder extends CatalogBuilder {

        @Override
        public void process(MapNode values, CatalogDescription catalog) throws IOException, TemplateException {
            templateEngine.putModel("table", values.getString("table"));
            templateEngine.setTemplateName("drop-table.ftl");
            templateEngine.generate(target);
        }
    }
}
