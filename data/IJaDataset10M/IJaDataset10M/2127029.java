package in.co.codedoc.cg.velocity;

import in.co.codedoc.cg.BeanUtil;
import in.co.codedoc.cg.TypeUtil;
import in.co.codedoc.cg.annotations.DBType;
import in.co.codedoc.cg.annotations.IsMappedToJSON;
import in.co.codedoc.cg.annotations.IsMappedToTable;
import in.co.codedoc.cg.annotations.Property;
import in.co.codedoc.cg.annotations.TriState;
import java.io.IOException;
import java.io.Writer;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.MirroredTypeException;

public class PropertyAccessBlockDirective extends CGBlockDirective {

    public PropertyAccessBlockDirective() {
        super("property annotation object");
    }

    @Override
    public String getName() {
        return "PropertyAccessBlock";
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        TypeDeclaration currentClass = (TypeDeclaration) context.get("currentClass");
        boolean isMappedToTable = currentClass.getAnnotation(IsMappedToTable.class) != null;
        boolean isMappedToJSON = currentClass.getAnnotation(IsMappedToJSON.class) != null;
        Property property = (Property) node.jjtGetChild(0).value(context);
        String defaultPropertyName = "";
        String defaultTypeName = "";
        for (int i = 1; i < node.jjtGetNumChildren() - 1; i++) {
            if ((i + 2) < node.jjtGetNumChildren() - 1) {
                if (getArgument(context, node, i).equals("true")) {
                    defaultPropertyName = getArgument(context, node, i + 1);
                    defaultTypeName = getArgument(context, node, i + 2);
                    break;
                }
            }
        }
        String propertyName = property.name();
        if (propertyName == null || propertyName.equals("")) {
            propertyName = defaultPropertyName;
        }
        String typeName = "";
        try {
            typeName = property.datatype().getCanonicalName();
        } catch (MirroredTypeException ex) {
            typeName = ex.getTypeMirror().toString();
        }
        boolean needsImport = true;
        if (typeName == null || typeName.equals("") || typeName.equals("in.co.codedoc.cg.annotations.NoType")) {
            if (property.datatypeName() != null && !property.datatypeName().equals("")) {
                typeName = property.datatypeName();
            } else {
                typeName = defaultTypeName;
                needsImport = false;
            }
        }
        String componentTypeName = "";
        try {
            componentTypeName = property.componentType().getCanonicalName();
        } catch (MirroredTypeException ex) {
            componentTypeName = ex.getTypeMirror().toString();
        }
        if (typeName.equals("java.util.List") && (componentTypeName != null) && (!componentTypeName.equals("in.co.codedoc.cg.annotations.NoType"))) {
            typeName = typeName + '<' + componentTypeName + '>';
        }
        String dbName = property.dbName();
        if ((dbName == null || dbName.equals("")) && ((isMappedToTable && (property.dbMapped() != TriState.False)) || (!isMappedToTable && (property.dbMapped() == TriState.True)))) {
            dbName = propertyName;
        }
        if ((!(dbName == null || dbName.equals(""))) && !isMappedToTable) {
            throw new RuntimeException("dbName of property " + property + " is set(even if implcitily) BUT @IsMappedToTable is not found on class " + currentClass.getQualifiedName());
        }
        String jsonName = property.jsonName();
        if ((jsonName == null || jsonName.equals("")) && ((isMappedToJSON && (property.jsonMapped() != TriState.False)) || (!isMappedToJSON && (property.jsonMapped() == TriState.True)))) {
            jsonName = propertyName;
        }
        if ((!(jsonName == null || jsonName.equals(""))) && !isMappedToJSON) {
            throw new RuntimeException("jsonName of property " + property + " is set(even if implcitily) BUT @IsMappedToJSON is not found on class " + currentClass.getQualifiedName());
        }
        context.put("importStatement", needsImport ? TypeUtil.GetImportStatement(typeName) : "");
        context.put("qualifiedTypeName", typeName);
        context.put("typeName", typeName = TypeUtil.GetTypeName(typeName));
        context.put("ucaseName", BeanUtil.GetUCased(propertyName));
        context.put("lcaseName", BeanUtil.GetLCased(propertyName));
        context.put("dbName", dbName);
        if (!(dbName == null || dbName.equals(""))) {
            context.put("javaTypeName", GetJavaTypeName(typeName));
            context.put("dbTypeName", property.dbType() == DBType.IMPLICIT ? GetImplicitDBType(typeName) : "DBType." + property.dbType().toString());
            context.put("javaSideOfOutputMap", GetJavaSideOfOutputMap(typeName));
            context.put("isAnIDType", IsAnIdType(typeName));
        }
        context.put("jsonName", jsonName);
        context.put("jsonType", GetJSONTypeName(typeName));
        context.put("isIdentity", property.identity() ? true : false);
        context.put("isVersion", property.version() ? true : false);
        context.put("isPrimitiveType", TypeUtil.IsPrimitiveType(typeName));
        try {
            node.jjtGetChild(node.jjtGetNumChildren() - 1).render(context, writer);
        } finally {
            context.remove("importStatement");
            context.remove("typeName");
            context.remove("qualifiedTypeName");
            context.remove("ucaseName");
            context.remove("lcaseName");
            context.remove("dbName");
            if (!(dbName == null || dbName.equals(""))) {
                context.remove("javaType");
                context.remove("dbTypeName");
                context.remove("javaSideOfOutputMap");
                context.remove("isAnIDType");
            }
            context.remove("jsonName");
            context.remove("jsonType");
            context.remove("isIdentity");
            context.remove("isVersion");
            context.remove("isPrimitiveType");
        }
        return true;
    }

    private Boolean IsAnIdType(String typeName) {
        return (typeName.indexOf("IdSetId") >= 0) || (typeName.indexOf("IdListId") >= 0) || typeName.endsWith("Id");
    }

    private Object GetJavaSideOfOutputMap(String typeName) {
        if (typeName.equals("int")) {
            return "IntOutputColumnMap";
        } else if (typeName.equals("boolean")) {
            return "BooleanOutputColumnMap";
        } else if (typeName.equals("short")) {
            return "ShortOutputColumnMap";
        } else if (typeName.equals("long")) {
            return "LongOutputColumnMap";
        } else if (typeName.equals("float")) {
            return "FloatOutputColumnMap";
        } else if (typeName.equals("double")) {
            return "DoubleOutputColumnMap";
        } else if (typeName.equals("String")) {
            return "StringOutputColumnMap";
        } else if (typeName.indexOf("IdSetId") >= 0) {
            return "LongOutputColumnMap";
        } else if (typeName.indexOf("IdListId") >= 0) {
            return "LongOutputColumnMap";
        } else if (typeName.endsWith("Id")) {
            return "LongOutputColumnMap";
        } else {
            return "ObjectOutputColumnMap";
        }
    }

    private String GetJavaTypeName(String typeName) {
        if (typeName.equals("int")) {
            return "Integer.TYPE";
        } else if (typeName.equals("boolean")) {
            return "Boolean.TYPE";
        } else if (typeName.equals("short")) {
            return "Short.TYPE";
        } else if (typeName.equals("long")) {
            return "Long.TYPE";
        } else if (typeName.equals("float")) {
            return "Float.TYPE";
        } else if (typeName.equals("double")) {
            return "Double.TYPE";
        } else if (typeName.indexOf("IdSetId") >= 0) {
            return "Long.TYPE";
        } else if (typeName.indexOf("IdListId") >= 0) {
            return "Long.TYPE";
        } else if (typeName.endsWith("Id")) {
            return "Long.TYPE";
        } else {
            return typeName + ".class";
        }
    }

    private String GetJSONTypeName(String typeName) {
        if (typeName.equals("short") || typeName.equals("Short") || typeName.equals("int") || typeName.equals("Integer") || typeName.equals("long") || typeName.equals("Long")) {
            return "JSONLongValue";
        } else if (typeName.equals("boolean") || typeName.equals("Boolean")) {
            return "JSONBooleanValue";
        } else if (typeName.equals("float") || typeName.equals("Float") || typeName.equals("double") || typeName.equals("Double")) {
            return "JSONDoubleValue";
        } else if (typeName.equals("String")) {
            return "JSONStringValue";
        } else if (typeName.indexOf("List") == 0 || typeName.lastIndexOf("[]") == (typeName.length() - 2)) {
            return "JSONArrayValue";
        } else {
            return "JSONObjectValue";
        }
    }

    private String GetImplicitDBType(String typeName) {
        if (typeName.equals("String")) {
            return "DBType.CHAR";
        } else if (typeName.equals("boolean")) {
            return "DBType.BOOLEAN";
        } else if (typeName.equals("short")) {
            return "DBType.SMALLINT";
        } else if (typeName.equals("int")) {
            return "DBType.INT";
        } else if (typeName.equals("long")) {
            return "DBType.BIGINT";
        } else if (typeName.equals("double")) {
            return "DBType.DOUBLE";
        } else if (typeName.endsWith("Id")) {
            return "DBType.BIGINT";
        } else {
            throw new RuntimeException("No defualt output mapping available for " + typeName);
        }
    }
}
