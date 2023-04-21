package org.melati.poem.prepro;

import java.util.Vector;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.io.IOException;
import org.melati.util.StringUtils;

/**
 * An abstract definition of a  <tt>Field</tt> from which 
 * all other <tt>FieldDef</tt>s are derived.
 * 
 */
public abstract class FieldDef {

    protected final TableDef table;

    protected final String name;

    protected final String suffix;

    protected int displayOrder;

    String displayName;

    String description;

    protected final String type;

    protected final String rawType;

    protected final Vector qualifiers;

    final String mainClass;

    final String tableAccessorMethod;

    org.melati.poem.DisplayLevel displayLevel = null;

    org.melati.poem.Searchability searchability = null;

    boolean isNullable;

    boolean isTroidColumn;

    boolean isDeletedColumn;

    int displayOrderPriority = -1;

    boolean isEditable = true;

    boolean sortDescending = false;

    boolean isCreateable = true;

    boolean isIndexed = false;

    boolean isUnique = false;

    boolean isCompareOnly = false;

    int width = -1, height = -1;

    String renderinfo = null;

    /**
  * Constructor.
  *
  * @param table        the {@link TableDef} that this <code>Field</code> is 
  *                     part of 
  * @param name         the name of this field
  * @param type         the POEM type of this field
  * @param rawType      the underlying java type of this field
  * @param displayOrder where to place this field in a list
  * @param qualifiers   all the qualifiers of this field
  * 
  * @throws IllegalityException if a semantic inconsistency is detected
  */
    public FieldDef(TableDef table, String name, String type, String rawType, int displayOrder, Vector qualifiers) throws IllegalityException {
        this.table = table;
        this.name = name;
        this.displayOrder = displayOrder;
        this.suffix = StringUtils.capitalised(name);
        this.type = type;
        this.rawType = rawType;
        this.qualifiers = qualifiers;
        this.mainClass = table.naming.mainClassUnambiguous();
        this.tableAccessorMethod = table.naming.tableAccessorMethod();
        for (int q = 0; q < qualifiers.size(); ++q) ((FieldQualifier) qualifiers.elementAt(q)).apply(this);
    }

    /** @return a name for this class*/
    public String toString() {
        return table.name + "." + name + " (" + (isNullable ? "nullable " : "") + type + ")";
    }

    private static void fieldQualifiers(Vector qualifiers, StreamTokenizer tokens) throws ParsingDSDException, IOException {
        while (tokens.ttype == '(') {
            tokens.nextToken();
            qualifiers.addElement(FieldQualifier.from(tokens));
            DSD.expect(tokens, ')');
            tokens.nextToken();
        }
    }

    /**
  * Creates the appropriate type of <code>FieldDef</code> 
  * from the input stream.
  *
  * @param table        the {@link TableDef} we are dealing with
  * @param tokens       the <code>StreamTokenizer</code> to get tokens from
  * @param displayOrder the ranking of this <code>Field</code>
  *
  * @throws ParsingDSDException 
  *           if an unexpected token is encountered
  * @throws IOException 
  *           if something goes wrong with the file system
  * @throws IllegalityException
  *           if a semantic incoherence is detected
  * @return a new <code>FieldDef</code> of the appropriate type
  */
    public static FieldDef from(TableDef table, StreamTokenizer tokens, int displayOrder) throws ParsingDSDException, IOException, IllegalityException {
        table.addImport("org.melati.poem.AccessPoemException", "both");
        table.addImport("org.melati.poem.ValidationPoemException", "table");
        table.addImport("org.melati.poem.Persistent", "table");
        table.definesColumns = true;
        Vector qualifiers = new Vector();
        fieldQualifiers(qualifiers, tokens);
        if (tokens.ttype != StreamTokenizer.TT_WORD) throw new ParsingDSDException("<field type>", tokens);
        String type = tokens.sval;
        if (type.equals("byte")) {
            if (tokens.nextToken() != '[' || tokens.nextToken() != ']') throw new ParsingDSDException("[", tokens);
            type = "byte[]";
        }
        if (tokens.nextToken() != StreamTokenizer.TT_WORD) throw new ParsingDSDException("<field name>", tokens);
        String name = tokens.sval;
        tokens.nextToken();
        fieldQualifiers(qualifiers, tokens);
        DSD.expect(tokens, ';');
        if (type.equals("Integer")) return new IntegerFieldDef(table, name, displayOrder, qualifiers);
        if (type.equals("Long")) return new LongFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("Double")) return new DoubleFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("Boolean")) return new BooleanFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("String")) return new StringFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("Password")) return new PasswordFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("Date")) return new DateFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("Timestamp")) return new TimestampFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("ColumnType")) return new ColumnTypeFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("DisplayLevel")) return new DisplayLevelFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("Searchability")) return new SearchabilityFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("IntegrityFix")) return new IntegrityFixFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("BigDecimal")) return new BigDecimalFieldDef(table, name, displayOrder, qualifiers); else if (type.equals("byte[]")) return new BinaryFieldDef(table, name, displayOrder, qualifiers); else return new ReferenceFieldDef(table, name, displayOrder, type, qualifiers);
    }

    /**
  * Write out this <code>Column</code>'s base methods.
  *
  * @param w Persistent Base
  * 
  * @throws IOException 
  *           if something goes wrong with the file system
  */
    public void generateBaseMethods(Writer w) throws IOException {
        w.write("\n /**\n" + "  * Retrieves the <code>" + suffix + "</code> value, without locking, \n" + "  * for this <code>" + table.suffix + "</code> <code>Persistent</code>.\n" + "  *\n" + "  * @generator " + "org.melati.poem.prepro.FieldDef" + "#generateBaseMethods \n" + "  * @return the " + rawType + " " + name + "\n" + "  */\n");
        w.write("  public " + rawType + " get" + suffix + "_unsafe() {\n" + "    return " + name + ";\n" + "  }\n" + "\n");
        w.write("\n /**\n" + "  * Sets the <code>" + suffix + "</code> value directly, without checking, \n" + "  * for this " + table.suffix + " <code>Persistent</code>.\n" + "  * \n" + "  * @generator " + "org.melati.poem.prepro.FieldDef" + "#generateBaseMethods \n" + "  * @param cooked  the pre-validated value to set\n" + "  */\n");
        w.write("  public void set" + suffix + "_unsafe(" + rawType + " cooked) {\n" + "    " + name + " = cooked;\n" + "  }\n");
    }

    /**
  * Write out this <code>Column</code>'s field creators. 
  *
  * @param w Persistent Base
  * @throws IOException 
  *           if something goes wrong with the file system
  */
    public void generateFieldCreator(Writer w) throws IOException {
        w.write("\n /**\n" + "  * Retrieves the <code>" + suffix + "</code> value as a <code>Field</code>\n" + "  * from this <code>" + table.suffix + "</code> <code>Persistent</code>.\n" + "  * \n" + "  * @generator " + "org.melati.poem.prepro.FieldDef" + "#generateFieldCreator \n" + "  * @throws AccessPoemException \n" + "  *         if the current <code>AccessToken</code> \n" + "  *         does not confer write access rights\n" + "  * @return the " + rawType + " " + name + "\n" + "  */\n");
        w.write("  public Field get" + suffix + "Field() " + "throws AccessPoemException {\n" + "    Column c = _" + tableAccessorMethod + "()." + "get" + suffix + "Column();\n" + "    return new Field(c.getRaw(this), c);\n" + "  }\n");
    }

    /**
  * Write out this <code>Field</code>'s java declaration string.
  *
  * @param w PersistentBase
  * @throws IOException 
  *           if something goes wrong with the file system
  */
    public abstract void generateJavaDeclaration(Writer w) throws IOException;

    /**
  * Write out this <code>Column</code>'s java declaration string.
  *
  * @param w TableBase
  * @throws IOException 
  *           if something goes wrong with the file system
  */
    public void generateColDecl(Writer w) throws IOException {
        w.write("Column col_" + name);
    }

    /**
  * Write out this <code>Column</code>'s accessors. 
  *
  * @param w TableBase
  * @throws IOException 
  *           if something goes wrong with the file system
  */
    public void generateColAccessor(Writer w) throws IOException {
        w.write("\n /**\n" + "  * Retrieves the <code>" + suffix + "</code> <code>Column</code> for this \n" + "  * <code>" + table.suffix + "</code> <code>Table</code>\n" + "  * \n" + "  * @generator " + "org.melati.poem.prepro.FieldDef" + "#generateColAccessor \n" + "  * @return the " + name + " <code>Column</code>\n" + "  */\n");
        w.write("  public final Column get" + suffix + "Column() {\n" + "    return col_" + name + ";\n" + "  }\n");
    }

    /**
  * Write out this <code>Column</code>'s field accessors as 
  * part of the anonymous definition of the <code>Column</code>.
  *
  * @param w TableBase
  * @throws IOException 
  *           if something goes wrong with the file system
  */
    protected void generateColRawAccessors(Writer w) throws IOException {
        w.write("          public Object getRaw_unsafe(Persistent g)\n" + "              throws AccessPoemException {\n" + "            return ((" + mainClass + ")g)." + "get" + suffix + "_unsafe();\n" + "          }\n" + "\n");
        w.write("          public void setRaw_unsafe(Persistent g, Object raw)\n" + "              throws AccessPoemException {\n" + "            ((" + mainClass + ")g).set" + suffix + "_unsafe((" + rawType + ")raw);\n" + "          }\n");
    }

    /**
  * Write out this <code>Column</code>'s definition 
  * using an anonymous class. 
  *
  * @param w TableBase
  * @throws IOException 
  *           if something goes wrong with the file system
  */
    public void generateColDefinition(Writer w) throws IOException {
        w.write("    defineColumn(col_" + name + " =\n" + "        new Column(this, \"" + name + "\",\n" + "                   " + poemTypeJava() + ",\n" + "                   DefinitionSource.dsd) { \n" + "          public Object getCooked(Persistent g)\n" + "              throws AccessPoemException, PoemException {\n" + "            return ((" + mainClass + ")g).get" + suffix + "();\n" + "          }\n" + "\n" + "          public void setCooked(Persistent g, Object cooked)\n" + "              throws AccessPoemException, ValidationPoemException {\n" + "            ((" + mainClass + ")g).set" + suffix + "((" + type + ")cooked);\n" + "          }\n" + "\n" + "          public Field asField(Persistent g) {\n" + "            return ((" + mainClass + ")g).get" + suffix + "Field();\n" + "          }\n" + "\n");
        if (isTroidColumn || !isEditable) w.write("          protected boolean defaultUserEditable() {\n" + "            return false;\n" + "          }\n" + "\n");
        if (isTroidColumn || !isCreateable) w.write("          protected boolean defaultUserCreateable() {\n" + "            return false;\n" + "          }\n" + "\n");
        if (displayLevel != null) w.write("          protected DisplayLevel defaultDisplayLevel() {\n" + "            return DisplayLevel." + displayLevel.name + ";\n" + "          }\n" + "\n");
        if (searchability != null) w.write("          protected Searchability defaultSearchability() {\n" + "            return Searchability." + searchability.name + ";\n" + "          }\n" + "\n");
        if (displayOrderPriority != -1) w.write("          protected Integer defaultDisplayOrderPriority() {\n" + "            return new Integer(" + displayOrderPriority + ");\n" + "          }\n" + "\n");
        if (sortDescending) w.write("          protected boolean defaultSortDescending() {\n" + "            return true;\n" + "          }\n" + "\n");
        if (displayName != null) w.write("          protected String defaultDisplayName() {\n" + "            return " + StringUtils.quoted(displayName, '"') + ";\n" + "          }\n" + "\n");
        w.write("          protected int defaultDisplayOrder() {\n" + "            return " + displayOrder + ";\n" + "          }\n" + "\n");
        if (description != null) w.write("          protected String defaultDescription() {\n" + "            return " + StringUtils.quoted(description, '"') + ";\n" + "          }\n" + "\n");
        if (isIndexed) w.write("          protected boolean defaultIndexed() {\n" + "            return true;\n" + "          }\n" + "\n");
        if (isUnique) w.write("          protected boolean defaultUnique() {\n" + "            return true;\n" + "          }\n" + "\n");
        if (width != -1) w.write("          protected int defaultWidth() {\n" + "            return " + width + ";\n" + "          }\n" + "\n");
        if (height != -1) w.write("          protected int defaultHeight() {\n" + "            return " + height + ";\n" + "          }\n" + "\n");
        if (renderinfo != null) w.write("          protected String defaultRenderinfo() {\n" + "            return " + StringUtils.quoted(renderinfo, '"') + ";\n" + "          }\n" + "\n");
        generateColRawAccessors(w);
        w.write("        });\n");
    }

    /** @return the Java string for this <code>PoemType</code>. */
    public abstract String poemTypeJava();
}
