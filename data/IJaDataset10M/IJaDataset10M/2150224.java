package org.ujac.util.codegen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import org.ujac.util.db.AttributeDefinition;
import org.ujac.util.db.DatabaseSchemaReader;
import org.ujac.util.exi.ExpressionException;

/**
 * Name: CmpEjbGenerator<br>
 * Description: A class generating source code of CMP Entity Beans. 
 *   It automatically generates xdoclet tags, making it possible
 *   to generate the missing interfaces and descriptors at build time.<br>
 * <br>Log: $Log$
 * <br>Log: Revision 1.1  2003/12/15 00:31:44  lauerc
 * <br>Log: Initial revision.
 * <br>Log:
 * @author $Author: lauerc $
 * @version $Revision: 856 $
 */
public class CmpEjbGenerator extends BaseCodeGenerator {

    /** The table definition. */
    private TableDefinition tableDefinition;

    /**
   * Constructs a CmpEjbGenerator instance with no specific arguments.
   * @param srcRoot The source directory root.
   * @param className The class name.
   * @param tableDefinition The table definition.
   */
    public CmpEjbGenerator(String srcRoot, String className, TableDefinition tableDefinition) {
        super(srcRoot, className, tableDefinition.getAttributes());
        this.tableDefinition = tableDefinition;
    }

    /**
   * Reads the template, evaluates the template and writes the result to the destination stream.
   * @exception IOException In case an I/O problem occurred.
   * @exception ExpressionException In case the evaluation of a code generator template failed.
   */
    public void generate() throws IOException, ExpressionException {
        Map params = new HashMap();
        params.put("className", getClassName());
        params.put("package", getClassPackage());
        params.put("attributes", getAttributes());
        if (tableDefinition.getSchema() != null) {
            params.put("table", tableDefinition.getSchema() + "." + tableDefinition.getName());
        } else {
            params.put("table", tableDefinition.getName());
        }
        params.put("tableName", tableDefinition.getName());
        params.put("schema", tableDefinition.getSchema());
        String primaryKeyClassName = null;
        AttributeDefinition[] pkAttrs = tableDefinition.getPrimaryKeys();
        if (pkAttrs.length == 0) {
            throw new RuntimeException("No primary keys detected for table '" + tableDefinition.getName() + "'.");
        }
        if (pkAttrs.length == 1) {
            primaryKeyClassName = pkAttrs[0].getFullyQualifiedType();
        }
        params.put("primaryKeyClass", primaryKeyClassName);
        InputStream templateStream = this.getClass().getResourceAsStream("/org/ujac/util/codegen/cmp-ejb.template");
        File sourceFile = new File(getSourceRoot() + "/" + getClassSourceFilePath() + getClassName() + "Bean.java");
        sourceFile.getParentFile().mkdirs();
        OutputStream destinationStream = new FileOutputStream(sourceFile);
        generateCode(params, templateStream, destinationStream);
        destinationStream.close();
        templateStream.close();
        params.put("className", getClassName() + "Model");
        templateStream = this.getClass().getResourceAsStream("/org/ujac/util/codegen/bean.template");
        sourceFile = new File(getSourceRoot() + "/" + getClassSourceFilePath() + getClassName() + "Model.java");
        sourceFile.getParentFile().mkdirs();
        destinationStream = new FileOutputStream(sourceFile);
        generateCode(params, templateStream, destinationStream);
        destinationStream.close();
        templateStream.close();
    }

    /**
   * The main method for command line access.
   * @param args The command line arguments.
   */
    public static void main(String[] args) {
        if (args.length < 7) {
            System.err.println("Usage: org.ujac.util.codegen.BeanCreator <driver> <url> <user> <passwd> <table> <source root> <class name>");
            System.exit(1);
        }
        String driver = args[0];
        String url = args[1];
        String user = args[2];
        String passwd = args[3];
        String table = args[4];
        String srcRoot = args[5];
        String className = args[6];
        try {
            DatabaseSchemaReader dsr = new DatabaseSchemaReader(driver, url, user, passwd);
            TableDefinition td = dsr.getTableDefinition(table);
            CmpEjbGenerator generator = new CmpEjbGenerator(srcRoot, className, td);
            generator.generate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExpressionException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
