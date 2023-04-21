package mod.datagen.summary.tableref;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.dlib.tools.Util;
import druid.core.AttribSet;
import druid.core.DataTypeLib;
import druid.core.DruidException;
import druid.core.config.Config;
import druid.data.DatabaseNode;
import druid.data.FieldAttribs;
import druid.data.FieldNode;
import druid.data.ProjectNode;
import druid.data.TableNode;
import druid.data.datatypes.TypeInfo;
import druid.interfaces.Logger;
import druid.interfaces.ModuleOptions;
import druid.interfaces.SummaryGenModule;
import druid.util.decoder.OnClauseDecoder;

public class TableReference implements SummaryGenModule {

    private DatabaseNode dbaseNode;

    private ProjectNode projNode;

    private String LF = Config.os.lineSep;

    public String getId() {
        return "tableRef";
    }

    public String getVersion() {
        return "1.0";
    }

    public String getAuthor() {
        return "Andrea Carboni";
    }

    public String getDescription() {
        return "Generates a summary of the foreign key relationships";
    }

    public ModuleOptions getModuleOptions(int env) {
        return null;
    }

    public String getFormat() {
        return "Table references";
    }

    public boolean isDirectoryBased() {
        return false;
    }

    public boolean hasLargePanel() {
        return false;
    }

    public void generate(Logger logger, DatabaseNode dbNode) {
        dbaseNode = dbNode;
        projNode = (ProjectNode) dbNode.getParent();
        logger.logHeader("Table Reference File");
        String sOutput = dbNode.modsConfig.getValue(this, "output");
        try {
            Writer w = new FileWriter(sOutput);
            writeHeader(w);
            List<TableNode> tables = dbaseNode.getObjects(TableNode.class);
            for (int i = 0; i < tables.size(); i++) genTableReference(w, (TableNode) tables.get(i), tables);
            writeSeparator(w);
            w.close();
            logger.log(Logger.INFO, "");
            logger.log(Logger.INFO, "Done.");
        } catch (IOException e) {
            logger.log(Logger.ALERT, "(?) Exception occured --> " + e.getMessage());
        }
    }

    private void genTableReference(Writer w, TableNode tableNode, List<TableNode> tables) throws IOException {
        OnClauseDecoder onClauseDec = new OnClauseDecoder();
        String name = tableNode.attrSet.getString("name");
        w.write("#== " + name + " ");
        for (int i = 0; i < 80 - 5 - name.length(); i++) w.write("=");
        w.write(LF);
        w.write(LF);
        for (TableNode otherTableNode : tables) for (int i = 0; i < otherTableNode.getChildCount(); i++) {
            FieldNode otherFieldNode = (FieldNode) otherTableNode.getChild(i);
            int tableId = otherFieldNode.attrSet.getInt("refTable");
            int fieldId = otherFieldNode.attrSet.getInt("refField");
            String otherTableName = otherTableNode.attrSet.getString("name");
            String otherFieldName = otherFieldNode.attrSet.getString("name");
            String onUpdate = otherFieldNode.attrSet.getString("onUpdate");
            String onDelete = otherFieldNode.attrSet.getString("onDelete");
            if (tableId != 0) {
                if (tableNode == dbaseNode.getTableByID(tableId)) {
                    w.write(Util.pad(otherTableName + "." + otherFieldName, 40) + " --> ");
                    FieldNode fieldNode = tableNode.getFieldByID(fieldId);
                    if (fieldNode == null) w.write("<FIELD DEL>" + LF); else {
                        String fieldName = fieldNode.attrSet.getString("name");
                        w.write(fieldName + " ( ");
                        if (!onUpdate.equals(FieldNode.NOACTION)) {
                            w.write("on update " + onClauseDec.decode(onUpdate));
                            if (!onDelete.equals(FieldNode.NOACTION)) w.write(" | ");
                        }
                        if (!onDelete.equals(FieldNode.NOACTION)) w.write("on delete " + onClauseDec.decode(onDelete));
                        w.write(" )" + LF);
                    }
                }
            }
        }
        w.write(LF);
    }

    private void writeHeader(Writer w) throws IOException {
        int build = projNode.attrSet.getInt("build");
        String dbName = dbaseNode.attrSet.getString("name");
        writeSeparator(w);
        w.write("#==   Table reference file for Database : " + dbName + " (Build " + build + ")" + LF);
        w.write("#==" + LF);
        w.write("#==   Date of creation: " + Util.getCurrentDate() + LF);
        writeSeparator(w);
        w.write(LF);
    }

    private void writeSeparator(Writer w) throws IOException {
        w.write("#=======================================" + "========================================" + LF);
    }
}
