package org.aspencloud.simple9.builder.gen.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.aspencloud.simple9.util.StringUtils;
import org.aspencloud.simple9.util.Utils;

public class HasOneBuilder extends PropertyBuilder {

    public HasOneBuilder(PropertyDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public ArrayList<String> getImports() {
        ArrayList<String> imports = new ArrayList<String>();
        if (descriptor.hasImport()) imports.add(descriptor.fullType());
        imports.add("static " + Utils.class.getCanonicalName() + ".notEqual");
        return imports;
    }

    @Override
    public Map<String, String> getDeclarations() {
        Map<String, String> vars = new HashMap<String, String>();
        vars.put(descriptor.variable(), descriptor.type() + " " + descriptor.variable() + " = null;");
        return vars;
    }

    @Override
    public Map<String, String> getMethods() {
        Map<String, String> methods = new HashMap<String, String>();
        methods.put(descriptor.hasserName(), getHasserMethod());
        methods.put(descriptor.getterName(), getGetterMethod());
        methods.put(descriptor.setterName(), getSetterMethod());
        return methods;
    }

    private String getHasserMethod() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tpublic boolean " + descriptor.hasserName() + "() {\n");
        sb.append("\t\treturn " + descriptor.variable() + " != null;\n");
        sb.append("\t}");
        return sb.toString();
    }

    private String getGetterMethod() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tpublic " + descriptor.type() + " " + descriptor.getterName() + "() {\n");
        sb.append("\t\tif(" + descriptor.variable() + " != null && !" + descriptor.variable() + ".isResolved()) {\n" + "\t\t\t" + descriptor.variable() + ".load();\n" + "\t\t}\n" + "\t\treturn " + descriptor.variable() + ";\n");
        sb.append("\t}");
        return sb.toString();
    }

    private String getSetterMethod() {
        String src = "\tpublic void " + descriptor.setterName() + "(" + descriptor.type() + " " + descriptor.variable() + ") {\n" + "\t\tif(notEqual(this." + descriptor.variable() + ", " + descriptor.variable() + ")) {\n" + "\t\t\t" + descriptor.type() + " oldValue = this." + descriptor.variable() + ";\n" + "\t\t\tthis." + descriptor.variable() + " = " + descriptor.variable() + ";\n" + "\t\t\tif(autoCommit && !isNew()) {\n" + "\t\t\t\tpersistor.update(this, \"" + descriptor.variable() + "\");\n" + "\t\t\t} else {\n" + "\t\t\t\tdirtyProperties.put(\"" + descriptor.variable() + "\", " + descriptor.variable() + ");\n" + "\t\t\t}\n";
        if (descriptor.hasOpposite()) {
            if (descriptor.opposite().hasMany()) {
                String opposites = descriptor.opposite().getName();
                src += "\t\t\tif(oldValue != null) {\n";
                src += "\t\t\t\toldValue." + opposites + "().remove(this);\n";
                src += "\t\t\t}\n";
                src += "\t\t\tif(" + descriptor.variable() + " != null) {\n";
                src += "\t\t\t\t" + descriptor.variable() + "." + opposites + "().add((" + descriptor.modelType() + ") this);\n";
                src += "\t\t\t}\n";
            } else {
                String oppositeSetter = StringUtils.setterName(descriptor.opposite().getName());
                src += "\t\t\tif(" + descriptor.variable() + " != null) {\n";
                src += "\t\t\t\t" + descriptor.variable() + "." + oppositeSetter + "((" + descriptor.modelType() + ") this);\n";
                src += "\t\t\t} else {\n";
                src += "\t\t\t\toldValue." + oppositeSetter + "(null);\n";
                src += "\t\t\t}\n";
            }
        }
        src += "\t\t\tfirePropertyChange(PROP." + descriptor.enumProp() + ", oldValue, " + descriptor.variable() + ");\n" + "\t\t}\n" + "\t}";
        return src;
    }
}
