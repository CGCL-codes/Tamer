package OLD.net.nothinginteresting.datamappers2.generate.java;

import java.util.Set;
import java.util.TreeSet;
import OLD.net.nothinginteresting.datamappers2.generate.GenerateUtils;
import OLD.net.nothinginteresting.datamappers2.models.ClassField;
import OLD.net.nothinginteresting.datamappers2.models.Schema;
import OLD.net.nothinginteresting.datamappers2.models.SchemaClass;
import OLD.net.nothinginteresting.datamappers2.models.TableReference;

/**
 * @author Dmitri Gorbenko
 * 
 */
public class JavaClassGenerator {

    protected final Schema schemaModel;

    protected final SchemaClass classModel;

    private final Set<String> imports = new TreeSet<String>();

    public JavaClassGenerator(Schema schemaModel, SchemaClass classModel) {
        super();
        this.schemaModel = schemaModel;
        this.classModel = classModel;
    }

    public String generate() {
        String _package = getPackage();
        String classCode = getClassCode();
        return _package + getImports() + classCode;
    }

    protected String getClassCode() {
        String result = "public class " + classModel.getClassName() + " extends " + getParentClassName() + "\n";
        result += "{\n";
        result += getValues();
        result += getFields();
        result += getReferenceGetters();
        result += getFieldsGettersSetters();
        result += getInner();
        result += "}\n";
        return result;
    }

    /**
	 * @return
	 */
    private String getReferenceGetters() {
        String result = "";
        for (TableReference r : classModel.getOutReferences()) {
            if (!r.isInheritance()) result += getReferenceGetter(r);
        }
        return result;
    }

    /**
	 * @param r
	 * @return
	 */
    private String getReferenceGetter(TableReference r) {
        String parentClassName = r.getParentClassModel().getClassName();
        String result = "public " + parentClassName + " get" + parentClassName + "()\n";
        result += "{\n";
        result += "\t//Criteria<" + parentClassName + "> criteria = Criteria.equals(" + parentClassName + ".moId, getMoId());\n";
        result += "\treturn null" + "" + ";\n";
        result += "}\n\n";
        return GenerateUtils.addTabs(result, 1) + "\n";
    }

    /**
	 * @return
	 */
    private String getInner() {
        InnerJavaClassGenerator gen = new InnerJavaClassGenerator(schemaModel, classModel);
        String result = gen.generate();
        imports.addAll(gen.getImportsSet());
        return result;
    }

    /**
	 * @return
	 */
    protected String getFields() {
        String result = "";
        if (classModel.getStorage() != null) {
        }
        return GenerateUtils.addTabs(result, 1) + "\n";
    }

    /**
	 * @param fieldModel
	 * @return
	 */
    protected String getField(ClassField fieldModel) {
        addImport("net.nothinginteresting.datamappers2.Field");
        addImport("net.nothinginteresting.datamappers2.Datamappers");
        return "static public final " + getFieldType(fieldModel) + " " + fieldModel.getNameFormatted() + " = Datamappers.findField(\"" + schemaModel.getName() + "\", \"" + classModel.getClassName() + "\", \"" + fieldModel.getName() + "\");\n";
    }

    /**
	 * @param fieldModel
	 * @return
	 */
    protected String getFieldType(ClassField fieldModel) {
        return "Field<" + classModel.getClassName() + ", " + fieldModel.getDataType() + ">";
    }

    protected String getParentClassName() {
        return classModel.getParentClassName();
    }

    private String getValues() {
        String result = "";
        if (classModel.getStorage() != null) {
        }
        return GenerateUtils.addTabs(result, 1) + "\n";
    }

    @SuppressWarnings("unused")
    private String getValue(ClassField fieldModel) {
        return "private " + fieldModel.getDataType() + " _" + fieldModel.getNameFormatted() + ";\n";
    }

    private String getFieldsGettersSetters() {
        String result = "";
        if (classModel.getStorage() != null) {
        }
        return result;
    }

    @SuppressWarnings("unused")
    private boolean isInheritanceKeyField(ClassField fieldModel) {
        return fieldModel.isPrimary() && classModel.hasParent();
    }

    @SuppressWarnings("unused")
    private String getFieldGettersSetters(ClassField fieldModel) {
        return getFieldGetter(fieldModel) + getFieldSetter(fieldModel);
    }

    private String getFieldGetter(ClassField fieldModel) {
        String result = "public " + fieldModel.getDataType() + " get" + fieldModel.getAccessorName() + "()\n";
        result += "{\n";
        result += GenerateUtils.addTabs("return _" + fieldModel.getNameFormatted() + ";\n", 1);
        result += "}\n\n";
        return GenerateUtils.addTabs(result, 1) + "\n";
    }

    private String getFieldSetter(ClassField fieldModel) {
        String result = "public void set" + fieldModel.getAccessorName() + "(" + fieldModel.getDataType() + " " + fieldModel.getNameFormatted() + ")\n";
        result += "{\n";
        result += GenerateUtils.addTabs("this._" + fieldModel.getNameFormatted() + " = " + fieldModel.getNameFormatted() + ";\n", 1);
        result += "}\n\n";
        return GenerateUtils.addTabs(result, 1) + "\n";
    }

    protected String getImports() {
        String result = "";
        for (String name : imports) {
            result += "import " + name + ";\n";
        }
        return result + "\n";
    }

    protected Set<String> getImportsSet() {
        return imports;
    }

    protected String getPackage() {
        return "package " + schemaModel.getJavaPackage() + ";\n\n";
    }

    protected void addImport(String name) {
        imports.add(name);
    }
}
