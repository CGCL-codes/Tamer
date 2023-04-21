package org.aspencloud.simple9.builder.gen.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.aspencloud.simple9.persist.persistables.PersistableSet;

public class HasManyBuilder extends PropertyBuilder {

    public HasManyBuilder(PropertyDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public ArrayList<String> getImports() {
        ArrayList<String> imports = new ArrayList<String>();
        imports.add(PersistableSet.class.getCanonicalName());
        return imports;
    }

    @Override
    public Map<String, String> getDeclarations() {
        Map<String, String> vars = new HashMap<String, String>();
        vars.put(descriptor.variable(), getVariableDeclaration());
        return vars;
    }

    private String getVariableDeclaration() {
        StringBuilder sb = new StringBuilder();
        sb.append(getType()).append(" ").append(descriptor.variable()).append(" = new ");
        sb.append(getType()).append("(this, \"").append(descriptor.variable()).append("\", ").append(descriptor.type());
        if (descriptor.hasOpposite()) {
            if (descriptor.opposite().hasMany()) {
                sb.append(".class, \"").append(descriptor.opposite().getName()).append("\", true);");
            } else {
                sb.append(".class, \"").append(descriptor.opposite().getName()).append("\");");
            }
        } else {
            sb.append(".class);");
        }
        return sb.toString();
    }

    @Override
    public Map<String, String> getMethods() {
        Map<String, String> methods = new HashMap<String, String>();
        methods.put(descriptor.hasserName(), getHasserMethod());
        methods.put(descriptor.getterName(), getGetterMethod());
        return methods;
    }

    private String getHasserMethod() {
        StringBuilder sb = new StringBuilder();
        sb.append("\tpublic boolean " + descriptor.hasserName() + "() {\n");
        sb.append("\t\treturn !" + descriptor.variable() + ".isEmpty();\n");
        sb.append("\t}");
        return sb.toString();
    }

    private String getGetterMethod() {
        String src = "\tpublic " + getType() + " " + descriptor.getterName() + "() {\n" + "\t\treturn " + descriptor.variable() + ";\n" + "\t}";
        return src;
    }

    private String getType() {
        return PersistableSet.class.getSimpleName() + "<" + descriptor.type() + ">";
    }
}
