package com.byterefinery.rmbench.util.xml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.draw2d.geometry.Point;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import com.byterefinery.rmbench.RMBenchPlugin;
import com.byterefinery.rmbench.exceptions.SystemException;
import com.byterefinery.rmbench.external.model.IDataType;
import com.byterefinery.rmbench.model.Model;
import com.byterefinery.rmbench.model.diagram.DForeignKey;
import com.byterefinery.rmbench.model.diagram.DTable;
import com.byterefinery.rmbench.model.diagram.Diagram;
import com.byterefinery.rmbench.model.schema.CheckConstraint;
import com.byterefinery.rmbench.model.schema.Column;
import com.byterefinery.rmbench.model.schema.ForeignKey;
import com.byterefinery.rmbench.model.schema.Index;
import com.byterefinery.rmbench.model.schema.PrimaryKey;
import com.byterefinery.rmbench.model.schema.Schema;
import com.byterefinery.rmbench.model.schema.Table;
import com.byterefinery.rmbench.model.schema.UniqueConstraint;

/**
 * Serializes a Model into XML
 * 
 * @author cse
 */
public class ModelWriter implements XMLConstants {

    public static void write(Model model, OutputStream outputStream) throws SystemException {
        ModelWriter writer = new ModelWriter();
        try {
            XmlSerializer xs = setupSerializer(outputStream);
            writer.writeModel(model, xs);
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new SystemException(e);
            }
        }
    }

    private static XmlSerializer setupSerializer(OutputStream outputStream) throws Exception {
        XmlPullParserFactory factory;
        factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlSerializer serializer = factory.newSerializer();
        Writer out = new BufferedWriter(new OutputStreamWriter(outputStream));
        serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "    ");
        serializer.setOutput(out);
        return serializer;
    }

    private ModelWriter() {
    }

    private void writeModel(Model model, XmlSerializer serializer) throws Exception {
        serializer.startDocument("UTF-8", null);
        serializer.setPrefix("xsi", XSI_NAMESPACE);
        serializer.setPrefix("", NAMESPACE);
        serializer.startTag(NAMESPACE, Elements.MODEL);
        serializer.attribute(XSI_NAMESPACE, Attributes.SCHEMALOC, NAMESPACE + " " + NAMESPACELOC_1_0);
        serializer.attribute(ATT_NAMESPACE, Attributes.VERSION, "1.0");
        serializer.attribute(ATT_NAMESPACE, Attributes.NAME, model.getName());
        String name = RMBenchPlugin.getExtensionManager().getDatabaseExtension(model.getDatabaseInfo()).getId();
        serializer.attribute(ATT_NAMESPACE, Attributes.DBINFO, name);
        name = RMBenchPlugin.getExtensionManager().getNameGeneratorExtension(model.getNameGenerator()).getId();
        serializer.attribute(ATT_NAMESPACE, Attributes.GENERATOR, name);
        serializer.startTag(NAMESPACE, Elements.SCHEMAS);
        for (Iterator<Schema> it = model.getSchemas().iterator(); it.hasNext(); ) {
            writeSchema(it.next(), serializer);
        }
        serializer.endTag(NAMESPACE, Elements.SCHEMAS);
        serializer.startTag(NAMESPACE, Elements.DIAGRAMS);
        for (Iterator<Diagram> it = model.getDiagrams().iterator(); it.hasNext(); ) {
            writeDiagram(it.next(), serializer);
        }
        serializer.endTag(NAMESPACE, Elements.DIAGRAMS);
        serializer.endTag(NAMESPACE, Elements.MODEL);
        serializer.endDocument();
    }

    private void writeSchema(Schema schema, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.SCHEMA);
        if (schema.getCatalogName() != null) {
            serializer.attribute(ATT_NAMESPACE, Attributes.CATALOG, schema.getCatalogName());
        }
        serializer.attribute(ATT_NAMESPACE, Attributes.NAME, schema.getName());
        for (Iterator<Table> it = schema.getTables().iterator(); it.hasNext(); ) {
            writeTable(it.next(), serializer);
        }
        serializer.endTag(NAMESPACE, Elements.SCHEMA);
    }

    private void writeTable(Table table, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.TABLE);
        serializer.attribute(ATT_NAMESPACE, Attributes.NAME, table.getName());
        if (table.getType() != null) serializer.attribute(ATT_NAMESPACE, Attributes.TYPE, table.getType());
        for (Iterator<Column> it = table.getColumns().iterator(); it.hasNext(); ) {
            writeColumn(it.next(), serializer);
        }
        if (table.getPrimaryKey() != null) {
            writePrimaryKey(table.getPrimaryKey(), serializer);
        }
        for (Iterator<ForeignKey> it = table.getForeignKeys().iterator(); it.hasNext(); ) {
            writeForeignKey(it.next(), serializer);
        }
        for (Iterator<Index> it = table.getIndexes().iterator(); it.hasNext(); ) {
            writeIndex(it.next(), serializer);
        }
        for (Iterator<UniqueConstraint> it = table.getUniqueConstraints().iterator(); it.hasNext(); ) {
            writeUniqueConstraint(it.next(), serializer);
        }
        for (Iterator<CheckConstraint> it = table.getCheckConstraints().iterator(); it.hasNext(); ) {
            writeCheckConstraint(it.next(), serializer);
        }
        if (table.getComment() != null) writeComment(table, serializer);
        if (table.getDescription() != null) writeDescription(table.getDescription(), serializer);
        serializer.endTag(NAMESPACE, Elements.TABLE);
    }

    private void writeColumn(Column column, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.COLUMN);
        serializer.attribute(ATT_NAMESPACE, Attributes.NAME, column.getName());
        IDataType dataType = column.getDataType();
        serializer.attribute(ATT_NAMESPACE, Attributes.TYPE, dataType.getPrimaryName());
        serializer.attribute(ATT_NAMESPACE, Attributes.NULLABLE, String.valueOf(column.getNullable()));
        if (dataType.acceptsSize()) serializer.attribute(ATT_NAMESPACE, Attributes.SIZE, String.valueOf(column.getSize()));
        if (dataType.acceptsScale()) serializer.attribute(ATT_NAMESPACE, Attributes.SCALE, String.valueOf(column.getScale()));
        if (dataType.hasExtra() && dataType.getExtra() != null) serializer.attribute(ATT_NAMESPACE, Attributes.EXTRA, dataType.getExtra());
        if ((column.getDefault() != null) && (column.getDefault().length() > 0)) {
            serializer.startTag(NAMESPACE, Elements.DEFAULT);
            serializer.cdsect(column.getDefault());
            serializer.endTag(NAMESPACE, Elements.DEFAULT);
        }
        if ((column.getComment() != null) && (column.getComment().length() > 0)) {
            serializer.startTag(NAMESPACE, Elements.COMMENT);
            serializer.cdsect(column.getComment());
            serializer.endTag(NAMESPACE, Elements.COMMENT);
        }
        serializer.endTag(NAMESPACE, Elements.COLUMN);
    }

    private void writePrimaryKey(PrimaryKey primaryKey, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.PRIMARY_KEY);
        if (primaryKey.getName() != null) serializer.attribute(ATT_NAMESPACE, Attributes.NAME, primaryKey.getName());
        Column[] cols = primaryKey.getColumns();
        for (int i = 0; i < cols.length; i++) {
            serializer.startTag(NAMESPACE, Elements.COLUMN_REF);
            serializer.attribute(ATT_NAMESPACE, Attributes.NAME, cols[i].getName());
            serializer.endTag(NAMESPACE, Elements.COLUMN_REF);
        }
        serializer.endTag(NAMESPACE, Elements.PRIMARY_KEY);
    }

    private void writeForeignKey(ForeignKey foreignKey, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.FOREIGN_KEY);
        if (foreignKey.getName() != null) serializer.attribute(ATT_NAMESPACE, Attributes.NAME, foreignKey.getName());
        if (foreignKey.getDeleteAction() != null) serializer.attribute(ATT_NAMESPACE, Attributes.DELETE_RULE, foreignKey.getDeleteAction().getName());
        if (foreignKey.getUpdateAction() != null) serializer.attribute(ATT_NAMESPACE, Attributes.UPDATE_RULE, foreignKey.getUpdateAction().getName());
        serializer.startTag(NAMESPACE, Elements.TARGET);
        serializer.attribute(ATT_NAMESPACE, Attributes.SCHEMA, foreignKey.getTargetTable().getSchema().getName());
        serializer.attribute(ATT_NAMESPACE, Attributes.TABLE, foreignKey.getTargetTable().getName());
        serializer.endTag(NAMESPACE, Elements.TARGET);
        Column[] cols = foreignKey.getColumns();
        for (int i = 0; i < cols.length; i++) {
            serializer.startTag(NAMESPACE, Elements.COLUMN_REF);
            serializer.attribute(ATT_NAMESPACE, Attributes.NAME, cols[i].getName());
            serializer.endTag(NAMESPACE, Elements.COLUMN_REF);
        }
        serializer.endTag(NAMESPACE, Elements.FOREIGN_KEY);
    }

    private void writeUniqueConstraint(UniqueConstraint constraint, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.UNIQUE);
        serializer.attribute(ATT_NAMESPACE, Attributes.NAME, constraint.getName());
        Column[] cols = constraint.getColumns();
        for (int i = 0; i < cols.length; i++) {
            serializer.startTag(NAMESPACE, Elements.COLUMN_REF);
            serializer.attribute(ATT_NAMESPACE, Attributes.NAME, cols[i].getName());
            serializer.endTag(NAMESPACE, Elements.COLUMN_REF);
        }
        serializer.endTag(NAMESPACE, Elements.UNIQUE);
    }

    private void writeCheckConstraint(CheckConstraint constraint, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.CHECK);
        serializer.attribute(ATT_NAMESPACE, Attributes.NAME, constraint.getName());
        serializer.startTag(NAMESPACE, Elements.EXPRESSION);
        serializer.cdsect(constraint.getExpression());
        serializer.endTag(NAMESPACE, Elements.EXPRESSION);
        serializer.endTag(NAMESPACE, Elements.CHECK);
    }

    private void writeIndex(Index index, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.INDEX);
        if (index.getName() != null) serializer.attribute(ATT_NAMESPACE, Attributes.NAME, index.getName());
        serializer.attribute(ATT_NAMESPACE, Attributes.UNIQUE, String.valueOf(index.isUnique()));
        Column[] cols = index.getColumns();
        for (int i = 0; i < cols.length; i++) {
            serializer.startTag(NAMESPACE, Elements.COLUMN_REF);
            serializer.attribute(ATT_NAMESPACE, Attributes.NAME, cols[i].getName());
            serializer.endTag(NAMESPACE, Elements.COLUMN_REF);
        }
        serializer.endTag(NAMESPACE, Elements.INDEX);
    }

    private void writeDiagram(Diagram diagram, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.DIAGRAM);
        serializer.attribute(ATT_NAMESPACE, Attributes.NAME, diagram.getName());
        serializer.attribute(ATT_NAMESPACE, Attributes.SCHEMA, diagram.getDefaultSchema().getName());
        Collection<DTable> tables = diagram.getDTables();
        for (Iterator<DTable> it = tables.iterator(); it.hasNext(); ) {
            writeDTable(it.next(), serializer);
        }
        serializer.endTag(NAMESPACE, Elements.DIAGRAM);
    }

    private void writeDTable(DTable dTable, XmlSerializer serializer) throws Exception {
        Table table = dTable.getTable();
        serializer.startTag(NAMESPACE, Elements.TABLE_REF);
        serializer.attribute(ATT_NAMESPACE, Attributes.SCHEMA, table.getSchema().getName());
        serializer.attribute(ATT_NAMESPACE, Attributes.NAME, table.getName());
        serializer.attribute(ATT_NAMESPACE, Attributes.COLLAPSED, String.valueOf(dTable.isCollapsed()));
        if (dTable.getLocation() != null) writeLocation(dTable.getLocation(), serializer);
        for (Iterator<DForeignKey> it = dTable.getForeignKeys().iterator(); it.hasNext(); ) {
            writeDForeignKey(it.next(), serializer);
        }
        serializer.endTag(NAMESPACE, Elements.TABLE_REF);
    }

    private void writeDForeignKey(DForeignKey dforeignkey, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.FOREIGN_KEY_REF);
        serializer.attribute(ATT_NAMESPACE, Attributes.NAME, dforeignkey.getForeignKey().getName());
        serializer.startTag(NAMESPACE, Elements.SOURCE);
        serializer.attribute(ATT_NAMESPACE, Attributes.EDGE, Integer.toString(dforeignkey.getSourceEdge()));
        serializer.attribute(ATT_NAMESPACE, Attributes.SLOTNUMBER, Integer.toString(dforeignkey.getSourceSlot()));
        serializer.endTag(NAMESPACE, Elements.SOURCE);
        serializer.startTag(NAMESPACE, Elements.TARGET);
        serializer.attribute(ATT_NAMESPACE, Attributes.EDGE, Integer.toString(dforeignkey.getTargetEdge()));
        serializer.attribute(ATT_NAMESPACE, Attributes.SLOTNUMBER, Integer.toString(dforeignkey.getTargetSlot()));
        serializer.endTag(NAMESPACE, Elements.TARGET);
        serializer.endTag(NAMESPACE, Elements.FOREIGN_KEY_REF);
    }

    private void writeLocation(Point location, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.LOCATION);
        serializer.attribute(ATT_NAMESPACE, Attributes.X, String.valueOf(location.x));
        serializer.attribute(ATT_NAMESPACE, Attributes.Y, String.valueOf(location.y));
        serializer.endTag(NAMESPACE, Elements.LOCATION);
    }

    private void writeComment(Table table, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.COMMENT);
        serializer.text(table.getComment());
        serializer.endTag(NAMESPACE, Elements.COMMENT);
    }

    private void writeDescription(String description, XmlSerializer serializer) throws Exception {
        serializer.startTag(NAMESPACE, Elements.DESCRIPTION);
        serializer.text(description);
        serializer.endTag(NAMESPACE, Elements.DESCRIPTION);
    }
}
