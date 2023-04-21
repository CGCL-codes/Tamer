package codeGenerator.dao.mysql;

import java.io.*;
import java.sql.*;
import java.util.Vector;
import codeGenerator.Utility;
import codeGenerator.base.BaseGenerator;
import codeGenerator.dao.Field;
import codeGenerator.vo.BeanGenerator;
import codeGenerator.CommentsData;

/**
 * <p>Title: Project Code Generator</p>
 * <p>Description: This class generates DAO interface for the given Tables for
 * MySQL Server database (tested for mysql 4.0.20a)
 * It supports following datatypes:
 * int, char, varchar, Date, DateTime, Time and Timestamp</p>
 * <p>Copyright � 2008 Shaz Solutions. All Rights Reserved.</p>
 * <p>Company: Shaz Solutions</p>
 * @author Shahzad Masud
 * @created June 04, 2008
 * @version 1.0
 */
public class InterfaceGenerator extends BaseGenerator {

    private String tableName = null;

    private String[] imports = null;

    private String prefix = null;

    private String suffix = null;

    private String fieldSuffix = null;

    private String fullFieldSuffix = null;

    private String[] parentInterfaces = null;

    private String interfaceName = null;

    private String associatedBeanName = null;

    private String exceptionName = null;

    private String fieldNames = null;

    private Field[] fields = null;

    private String fullFieldNames = null;

    private StringBuffer fieldDeclaration = new StringBuffer();

    private CommentsData cd = null;

    /**
   * This constructor initializes the CommentsData object and fields object.
   * @param cd object having class level comment values.
   * @param fields Array of <code>codeGenerator.dao.Field</code> class objects
   * against which DAO interface is to be generated.
   */
    public InterfaceGenerator(CommentsData cd, Field[] fields) {
        if (cd == null || fields == null || fields.length < 1) {
            throw new NullPointerException("Any of the given input parameters can not be null or empty.");
        }
        this.cd = cd;
        this.fields = fields;
    }

    public void setTableName(String newTableName) {
        this.tableName = newTableName;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setFileName(String newFileName) {
        this.fileName = newFileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setPackageName(String newPackageName) {
        this.packageName = newPackageName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setImports(String[] newImports) {
        this.imports = newImports;
    }

    public String[] getImports() {
        return this.imports;
    }

    public void setPrefix(String newPrefix) {
        this.prefix = newPrefix;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setSuffix(String newSuffix) {
        this.suffix = newSuffix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setFieldSuffix(String newFieldSuffix) {
        fieldSuffix = newFieldSuffix;
    }

    public String getFieldSuffix() {
        return fieldSuffix;
    }

    public void setFullFieldSuffix(String newFullFieldSuffix) {
        fullFieldSuffix = newFullFieldSuffix;
    }

    public String getFullFieldSuffix() {
        return fullFieldSuffix;
    }

    public void setParentInterfaces(String[] newParentInterfaces) {
        this.parentInterfaces = newParentInterfaces;
    }

    public String[] getParentInterfaces() {
        return this.parentInterfaces;
    }

    public void setBeanName(String newBeanName) {
        this.associatedBeanName = newBeanName;
    }

    public String getBeanName() {
        return this.associatedBeanName;
    }

    public void setExceptionName(String newExceptionName) {
        this.exceptionName = newExceptionName;
    }

    public String getExceptionName() {
        return this.exceptionName;
    }

    private void setFieldNames() {
        StringBuffer fieldNames = new StringBuffer("");
        StringBuffer fullFieldNames = new StringBuffer("");
        for (int i = 0; i < fields.length; i++) {
            fieldDeclaration.append("\n");
            fieldDeclaration.append(Utility.getTab(1));
            fieldDeclaration.append("public static final String ");
            fieldDeclaration.append(fields[i].getName().toUpperCase());
            fieldDeclaration.append(getFieldSuffix());
            fieldDeclaration.append(" = \"");
            fieldDeclaration.append(fields[i].getName());
            fieldDeclaration.append("\";\n");
            fieldDeclaration.append("\n");
            fieldDeclaration.append(Utility.getTab(1));
            fieldDeclaration.append("public static final String ");
            fieldDeclaration.append(fields[i].getName().toUpperCase());
            fieldDeclaration.append(getFullFieldSuffix());
            fieldDeclaration.append(" = TABLE_NAME + \".");
            fieldDeclaration.append(fields[i].getName());
            fieldDeclaration.append("\";\n");
            if (i == 0) {
                fieldNames.append(fields[i].getName());
                fullFieldNames.append(fields[i].getTableName());
                fullFieldNames.append(".");
                fullFieldNames.append(fields[i].getName());
            } else {
                fieldNames.append(", ");
                fieldNames.append(fields[i].getName());
                fullFieldNames.append(", ");
                fullFieldNames.append(fields[i].getTableName());
                fullFieldNames.append(".");
                fullFieldNames.append(fields[i].getName());
            }
        }
        this.fieldNames = fieldNames.toString();
        this.fullFieldNames = fullFieldNames.toString();
    }

    public void createDAOInterface() {
        if (this.getTableName() == null || this.getBeanName() == null) {
            System.out.println("Table Name and/or Bean Name can not be null.");
            return;
        }
        this.setFieldNames();
        StringBuffer contents = new StringBuffer();
        interfaceName = "";
        if (this.getPrefix() != null) {
            interfaceName += this.getPrefix();
        }
        interfaceName += this.getTableName();
        if (this.getSuffix() != null) {
            interfaceName += this.getSuffix();
        }
        contents.append(this.getHeader());
        contents.append(this.getInsertMethod());
        contents.append(this.getInsertConMethod());
        contents.append(this.getUpdateMethod());
        contents.append(this.getUpdateConMethod());
        contents.append(this.getDeleteMethod());
        contents.append(this.getDeleteConMethod());
        contents.append(this.getDeleteArrayMethod());
        contents.append(this.getDeleteArrayConMethod());
        contents.append(this.getSetMethod());
        contents.append("} // end of interface ");
        contents.append(interfaceName);
        contents.append("\n");
        setClassName(interfaceName);
        Utility.writeFile(getOutputFileName(), contents.toString());
    }

    private String getHeader() {
        if (interfaceName == null) {
            return null;
        }
        StringBuffer contents = new StringBuffer();
        if (this.getPackageName() != null) {
            contents.append("package ");
            contents.append(this.getPackageName());
            contents.append(";\n\n");
        }
        if (this.getImports() != null) {
            String[] imports = this.getImports();
            for (int i = 0; i < imports.length; i++) {
                contents.append("import ");
                contents.append(imports[i]);
                contents.append(";\n");
            }
        }
        contents.append("\n/**\n");
        contents.append(" * <p>Title: ");
        contents.append(cd.getTitle());
        contents.append(" </p>\n");
        if (cd.getDescription() == null) {
            contents.append(" * <p>Description: This class is responsible for all the\n");
            contents.append(" * database operations to be performed on ");
            contents.append(this.getTableName());
            contents.append(" Table.</p>\n");
        } else {
            contents.append(" * <p>Description: ");
            contents.append(cd.getDescription());
            contents.append("</p>\n");
        }
        contents.append(" * <p>Copyright � ");
        contents.append(cd.getCopyRightYear());
        contents.append(" ");
        contents.append(cd.getCompanyName());
        contents.append(". All Rights Reserved.</p>\n");
        contents.append(" * <p>Company: ");
        contents.append(cd.getCompanyName());
        contents.append("</p>\n");
        contents.append(" * @author ");
        contents.append(cd.getAuthorName());
        contents.append("\n");
        contents.append(" * @created ");
        contents.append(cd.getDate());
        contents.append("\n");
        contents.append(" * @version ");
        contents.append(cd.getVersion());
        contents.append("\n");
        contents.append(" */\n");
        contents.append("public interface ");
        contents.append(interfaceName);
        contents.append(" ");
        if (this.getParentInterfaces() != null && this.getParentInterfaces().length > 0) {
            String[] interfaces = this.getParentInterfaces();
            contents.append("extends ");
            contents.append(interfaces[0]);
            for (int i = 1; i < interfaces.length; i++) {
                contents.append(", ");
                contents.append(interfaces[i]);
            }
            contents.append(" ");
        }
        contents.append("{");
        contents.append("\n\n");
        contents.append(Utility.getTab(1));
        contents.append("public static final String TABLE_NAME = \"");
        contents.append(getTableName());
        contents.append("\";\n");
        contents.append(fieldDeclaration);
        contents.append("\n");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * Name of fields of ");
        contents.append(this.getTableName());
        contents.append(" Table separated by comma\n");
        contents.append(Utility.getTab(1));
        contents.append(" * for the use in queries.\n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public static final String FIELD_NAMES = \"");
        contents.append(this.fieldNames);
        contents.append("\";\n\n");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * Name of fields of ");
        contents.append(this.getTableName());
        contents.append(" Table, qualified by table name,\n");
        contents.append(Utility.getTab(1));
        contents.append(" * ");
        contents.append(this.getTableName());
        contents.append(" separated by comma for the use in queries.\n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public static final String FULL_FIELD_NAMES = \"");
        contents.append(this.fullFieldNames);
        contents.append("\";\n\n");
        return contents.toString();
    }

    private String getSetMethod() {
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method creates a new ");
        contents.append(this.getBeanName());
        contents.append(" object. Sets its fields with\n");
        contents.append(Utility.getTab(1));
        contents.append(" * values read from the given ResultSet object and returns it.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param rst ResultSet object containing a ");
        contents.append(getTableName());
        contents.append(" record.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @return ");
        contents.append(this.getBeanName());
        contents.append(" object with fields set to values of a\n");
        contents.append(Utility.getTab(1));
        contents.append(" * ");
        contents.append(this.getTableName());
        contents.append(" record.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws SQLException \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public ");
        contents.append(this.getBeanName());
        contents.append(" set");
        contents.append(this.getTableName());
        contents.append("(ResultSet rst)");
        contents.append("\n");
        contents.append(Utility.getTab(3));
        contents.append("throws SQLException;\n\n");
        return contents.toString();
    }

    /**
   * This method returns generated code for delete method. This delete method
   * takes a number which represents a primary key Id of a record and deletes
   * it.
   * @return String containing lines of code for deleting a record.
   */
    private String getDeleteMethod() {
        String inputArgument = fields[0].getCamelCaseName();
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method hard deletes a record from ");
        contents.append(this.getTableName());
        contents.append(" Table whose primary key \n");
        contents.append(Utility.getTab(1));
        contents.append(" * is passed to this method.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param ");
        contents.append(inputArgument);
        contents.append(" Database Unique Id of the record that is to be deleted.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public void delete");
        contents.append(this.getTableName());
        contents.append("(int ");
        contents.append(inputArgument);
        contents.append(", String instId");
        contents.append(")");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }

    private String getDeleteConMethod() {
        String inputArgument = fields[0].getCamelCaseName();
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method hard deletes a record from ");
        contents.append(this.getTableName());
        contents.append(" Table whose primary key \n");
        contents.append(Utility.getTab(1));
        contents.append(" * is passed to this method. In addition, it does not handle transaction\n");
        contents.append(Utility.getTab(1));
        contents.append(" * if a live database connection object is passed to it.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param ");
        contents.append(inputArgument);
        contents.append(" Database Unique Id of the record that is to be deleted.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param con Database Connection object if the transaction is to be\n");
        contents.append(Utility.getTab(1));
        contents.append(" * handled outside this method.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public void delete");
        contents.append(this.getTableName());
        contents.append("(int ");
        contents.append(inputArgument);
        contents.append(", String instId");
        contents.append(", Connection con)");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }

    private String getDeleteArrayMethod() {
        String inputArgument = fields[0].getCamelCaseName() + "s";
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method hard deletes all the records from ");
        contents.append(this.getTableName());
        contents.append(" Table\n");
        contents.append(Utility.getTab(1));
        contents.append(" * whose primary key is found in the given array.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param ");
        contents.append(inputArgument);
        contents.append(" Array of Database Unique Ids of the records\n");
        contents.append(Utility.getTab(1));
        contents.append(" * that are to be deleted.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public void delete");
        contents.append(this.getTableName());
        contents.append("(int ");
        contents.append(inputArgument);
        contents.append("[]");
        contents.append(", String instId");
        contents.append(")");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }

    private String getDeleteArrayConMethod() {
        String inputArgument = fields[0].getCamelCaseName() + "s";
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method hard deletes all the records from ");
        contents.append(this.getTableName());
        contents.append(" Table\n");
        contents.append(Utility.getTab(1));
        contents.append(" * whose primary key\n");
        contents.append(Utility.getTab(1));
        contents.append(" * is found in the given array. In addition it does not handle transaction\n");
        contents.append(Utility.getTab(1));
        contents.append(" * itself if a live database connection object is passed to it\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param ");
        contents.append(inputArgument);
        contents.append(" Array of Database Unique Ids of the records\n");
        contents.append(Utility.getTab(1));
        contents.append(" * that are to be deleted.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param con Database Connection object if the transaction is to be\n");
        contents.append(Utility.getTab(1));
        contents.append(" * handled outside this method.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public void delete");
        contents.append(this.getTableName());
        contents.append("(int ");
        contents.append(inputArgument);
        contents.append("[]");
        contents.append(", String instId");
        contents.append(", Connection con)");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }

    private String getInsertMethod() {
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method inserts a new record in the database using the values set in\n");
        contents.append(Utility.getTab(1));
        contents.append(" * the given ");
        contents.append(this.getBeanName());
        contents.append(" object.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * A database generated primary key for newly inserted record is returned.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param ");
        contents.append(Utility.firstLetterSmall(getTableName()));
        contents.append(" object containing the values to be inserted as new record.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @return Database Unique Id of the newly inserted record.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public int insert");
        contents.append(this.getTableName());
        contents.append("(");
        contents.append(this.getBeanName());
        contents.append(" ");
        contents.append(Utility.firstLetterSmall(getTableName()));
        contents.append(",String instId");
        contents.append(")");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }

    /**
   * This method returns generated code for insert method. This insert method
   * takes a value object of a class with all the data members set which are
   * to be inserted in a table as a new record. The generated method also lets
   * the calling method handle transaction if a live database connection is
   * passed to the method being generated.
   * @return String containing lines of code for insert method.
   */
    private String getInsertConMethod() {
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method inserts a new record in the database using the values set in\n");
        contents.append(Utility.getTab(1));
        contents.append(" * the given ");
        contents.append(this.getBeanName());
        contents.append(" object.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * A database generated primary key for newly inserted record is returned.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method does not handle transaction itself if a live database\n");
        contents.append(Utility.getTab(1));
        contents.append(" * connection is passed to it.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param ");
        contents.append(Utility.firstLetterSmall(getTableName()));
        contents.append(" object containing the values to be inserted as new record.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param con Database Connection object if the transaction is to be\n");
        contents.append(Utility.getTab(1));
        contents.append(" * handled outside this method.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @return Database Unique Id of the newly inserted record.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public int insert");
        contents.append(this.getTableName());
        contents.append("(");
        contents.append(this.getBeanName());
        contents.append(" ");
        contents.append(Utility.firstLetterSmall(getTableName()));
        contents.append(", String instId");
        contents.append(", Connection con)");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }

    private String getSearchMethod() {
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method returns array of ");
        contents.append(this.getBeanName());
        contents.append(" objects representing database\n");
        contents.append(Utility.getTab(1));
        contents.append(" * records which satisfy the given criteria.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method does not append any string to where clause of the query if the\n");
        contents.append(Utility.getTab(1));
        contents.append(" * given criteria is null or zero length, hence returning all the database\n");
        contents.append(Utility.getTab(1));
        contents.append(" * record.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param criteria Array of SQLCriterion child classes to define fields and\n");
        contents.append(Utility.getTab(1));
        contents.append(" * their values to be compared to make the Where clause portion of the query.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param operator AbstractDAO.AND or AbstractDAO.OR values which define\n");
        contents.append(Utility.getTab(1));
        contents.append(" * Database AND / OR operators between the criteria.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @return Array of ");
        contents.append(this.getBeanName());
        contents.append(" objects representing records from database.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public ");
        contents.append(this.getBeanName());
        contents.append("[] search");
        contents.append(this.getTableName());
        contents.append("(SQLCriterion[] criteria, int operator)");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }

    /**
   * This method returns code for a method which returns one record against
   * the given database unique id of a record.
   * @return code generated for fetching one record based on given id.
   */
    private String getGet1Method() {
        String inputArgument = fields[0].getCamelCaseName();
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method returns an object of ");
        contents.append(this.getBeanName());
        contents.append(" representing a database\n");
        contents.append(Utility.getTab(1));
        contents.append(" * record whose database unique id is given to it.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method throws TransactionFailedException if there is no record found\n");
        contents.append(Utility.getTab(1));
        contents.append(" * against the given id.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param fields fields to be added in select part of query.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param ");
        contents.append(inputArgument);
        contents.append(" Database unique id of a record which is to be returned in\n");
        contents.append(Utility.getTab(1));
        contents.append(" * the form of an object of ");
        contents.append(getBeanName());
        contents.append(".\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @return Object of ");
        contents.append(this.getBeanName());
        contents.append(" representing record from database\n");
        contents.append(Utility.getTab(1));
        contents.append(" * whose id was given.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" in case there are some database\n");
        contents.append(Utility.getTab(1));
        contents.append(" * problems or the given id is invalid.\n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public ");
        contents.append(this.getBeanName());
        contents.append(" get");
        contents.append(this.getTableName());
        contents.append("(String[] fields, int ");
        contents.append(inputArgument);
        contents.append(")");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }

    /**
   * This method returns the code generated for searchTableName method. The
   * generated code is used for select queries on a table.
   * @return String having code for search method.
   */
    private String getSearchOrderByMethod() {
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method returns array of ");
        contents.append(this.getBeanName());
        contents.append(" objects representing database\n");
        contents.append(Utility.getTab(1));
        contents.append(" * records which satisfy the given criteria.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method does not append any string to where clause of the query if the\n");
        contents.append(Utility.getTab(1));
        contents.append(" * given criteria is null or zero length, hence returning all the database\n");
        contents.append(Utility.getTab(1));
        contents.append(" * record.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param criteria Array of SQLCriterion child classes to define fields and\n");
        contents.append(Utility.getTab(1));
        contents.append(" * their values to be compared to make the Where clause portion of the query.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param operator AbstractDAO.AND or AbstractDAO.OR values which define\n");
        contents.append(Utility.getTab(1));
        contents.append(" * Database AND / OR operators between the criteria.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param sortBy Name of the table field by which the records would be sorted.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param limit Number of records to be returned from this query.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * Number less than one means all the records are to be returned.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @return Array of ");
        contents.append(this.getBeanName());
        contents.append(" objects representing records from database.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public ");
        contents.append(this.getBeanName());
        contents.append("[] search");
        contents.append(this.getTableName());
        contents.append("(SQLCriterion[] criteria, int operator,\n");
        contents.append(Utility.getTab(4));
        contents.append("String sortBy, int limit)");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }

    private String getSearchFieldsOrderByMethod() {
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method returns array of ");
        contents.append(this.getBeanName());
        contents.append(" objects representing database\n");
        contents.append(Utility.getTab(1));
        contents.append(" * records which satisfy the given criteria.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method does not append any string to where clause of the query if the\n");
        contents.append(Utility.getTab(1));
        contents.append(" * given criteria is null or zero length, hence returning all the database\n");
        contents.append(Utility.getTab(1));
        contents.append(" * record.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param fields Array of Strings representing database fields which are to be\n");
        contents.append(Utility.getTab(1));
        contents.append(" * included in select part of the query.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param criteria Array of SQLCriterion child classes to define fields and\n");
        contents.append(Utility.getTab(1));
        contents.append(" * their values to be compared to make the Where clause portion of the query.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param operator AbstractDAO.AND or AbstractDAO.OR values which define\n");
        contents.append(Utility.getTab(1));
        contents.append(" * Database AND / OR operators between the criteria.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param sortBy Name of the table field by which the records would be sorted.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param limit Number of records to be returned from this query.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * Number less than one means all the records are to be returned.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @return Array of ");
        contents.append(this.getBeanName());
        contents.append(" objects representing records from database.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public ");
        contents.append(this.getBeanName());
        contents.append("[] search");
        contents.append(this.getTableName());
        contents.append("(String[] fields, SQLCriterion[] criteria,\n");
        contents.append(Utility.getTab(4));
        contents.append("int operator, String sortBy, int limit)");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }

    private String getUpdateMethod() {
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method updates a database record with the values set in the given\n");
        contents.append(Utility.getTab(1));
        contents.append(" * ");
        contents.append(this.getBeanName());
        contents.append(" object against the primary key set in ");
        contents.append(Utility.firstLetterSmall(getTableName()));
        contents.append("Id\n");
        contents.append(Utility.getTab(1));
        contents.append(" * field of the given");
        contents.append(this.getBeanName());
        contents.append(" object.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param ");
        contents.append(Utility.firstLetterSmall(getTableName()));
        contents.append(" object containing the fields with which a record\n");
        contents.append(Utility.getTab(1));
        contents.append(" * is to be updated.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public void update");
        contents.append(this.getTableName());
        contents.append("(");
        contents.append(this.getBeanName());
        contents.append(" ");
        contents.append(Utility.firstLetterSmall(getTableName()));
        contents.append(", String instId");
        contents.append(")");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }

    private String getUpdateConMethod() {
        StringBuffer contents = new StringBuffer("");
        contents.append(Utility.getTab(1));
        contents.append("/**\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method updates a database record with the values set in the given\n");
        contents.append(Utility.getTab(1));
        contents.append(" * ");
        contents.append(this.getBeanName());
        contents.append(" object against the primary key set in ");
        contents.append(Utility.firstLetterSmall(getTableName()));
        contents.append("Id\n");
        contents.append(Utility.getTab(1));
        contents.append(" * field of the given");
        contents.append(this.getBeanName());
        contents.append(" object.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * This method does not handle transaction if a live database connection\n");
        contents.append(Utility.getTab(1));
        contents.append(" * is provided to it.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param ");
        contents.append(Utility.firstLetterSmall(getTableName()));
        contents.append(" object containing the fields with which a record\n");
        contents.append(Utility.getTab(1));
        contents.append(" * is to be updated.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @param con Database Connection object if the transaction is to be\n");
        contents.append(Utility.getTab(1));
        contents.append(" * handled outside this method.\n");
        contents.append(Utility.getTab(1));
        contents.append(" * @throws ");
        contents.append(this.getExceptionName());
        contents.append(" \n");
        contents.append(Utility.getTab(1));
        contents.append(" */\n");
        contents.append(Utility.getTab(1));
        contents.append("public void update");
        contents.append(this.getTableName());
        contents.append("(");
        contents.append(this.getBeanName());
        contents.append(" ");
        contents.append(Utility.firstLetterSmall(getTableName()));
        contents.append(", String instId");
        contents.append(", Connection con)");
        if (this.getExceptionName() != null) {
            contents.append("\n");
            contents.append(Utility.getTab(3));
            contents.append("throws ");
            contents.append(this.getExceptionName());
        }
        contents.append(";\n\n");
        return contents.toString();
    }
}
