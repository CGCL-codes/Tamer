package org.berlin.pino.dev.analy.metric.antlr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.berlin.pino.dev.analy.antlr.JavaTokenTypes;
import antlr.collections.AST;

public class Qualifier {

    private String pkg = "";

    private List<String> imports = new ArrayList<String>();

    private Map<String, String> aliases = new HashMap<String, String>();

    public void compilationUnit(AST ast) {
        searchAST(ast, "");
    }

    private void searchAST(AST ast, String prefix) {
        while (ast != null) {
            if (ast.getType() == JavaTokenTypes.PACKAGE_DEF) {
                setPackage(ast.getFirstChild());
            } else if (ast.getType() == JavaTokenTypes.IMPORT) {
                addImport(stringify(ast.getFirstChild()));
            } else if (ast.getType() == JavaTokenTypes.STATIC_IMPORT) {
            } else if (ast.getType() == JavaTokenTypes.CLASS_DEF) {
                aliasType(ast.getFirstChild(), prefix);
            } else {
                searchAST(ast.getFirstChild(), prefix);
            }
            ast = ast.getNextSibling();
        }
    }

    private void aliasType(AST ast, String prefix) {
        String fullName = prefix + ast.getNextSibling().getText();
        String qualifiedName = pkg + fullName;
        addAlias(fullName, qualifiedName);
        searchAST(ast, fullName + "$");
    }

    private void setPackage(AST ast) {
        ast = ast.getNextSibling();
        setPackage(stringify(ast));
    }

    public String stringify(AST ast) {
        if (ast == null) return "";
        String text = "";
        AST child = ast.getFirstChild();
        if (child != null) text += stringify(child);
        text += ast.getText();
        if (child != null) text += stringify(child.getNextSibling());
        return text;
    }

    public void setPackage(String pkg) {
        this.pkg = pkg + ".";
    }

    public String qualify(String context, String ident) {
        String[] innerClasses = context.split("\\$");
        for (int i = innerClasses.length; i >= 0; --i) {
            String innerClass = join(innerClasses, i) + ident.replace('.', '$');
            if (aliases.containsKey(innerClass)) {
                return aliases.get(innerClass);
            }
        }
        if (aliases.containsKey(ident)) {
            return aliases.get(ident);
        }
        if (ident.contains(".")) {
            return ident;
        }
        for (String imprt : imports) {
            if (imprt.endsWith("." + ident)) {
                return imprt;
            }
        }
        try {
            String javaDefault = "java.lang." + ident;
            Class.forName(javaDefault);
            return javaDefault;
        } catch (ClassNotFoundException e) {
        }
        return pkg + ident;
    }

    private String join(String[] strings, int firstN) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < firstN; i++) {
            builder.append(strings[i]);
            builder.append("$");
        }
        return builder.toString();
    }

    public void addImport(String imprt) {
        imports.add(imprt);
    }

    public void addAlias(String ident, String qualified) {
        aliases.put(ident, qualified);
        aliases.put(qualified, qualified);
        aliases.put(qualified.replace('$', '.'), qualified);
    }

    public String qualify(String context, AST ast) {
        return qualify(context, stringify(ast));
    }

    public String getPackage() {
        return pkg;
    }
}
