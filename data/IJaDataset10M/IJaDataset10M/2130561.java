package com.judoscript.jamaica.parser;

public class ASTClassDeclaration extends ASTInterfaceDeclaration {

    int accessFlags = 0;

    String superName = null;

    public ASTClassDeclaration(int id) {
        super(id);
    }

    public ASTClassDeclaration(JamaicaParser p, int id) {
        super(p, id);
    }

    public void setAccessFlags(int flags) {
        accessFlags = flags;
    }

    public int getAccessFlags() {
        return accessFlags;
    }

    public void setSuper(String n) {
        superName = n;
    }

    public String getSuper() {
        return superName == null ? "java.lang.Object" : superName;
    }

    public void setInterfaces(String[] itfs) {
        interfaces = itfs;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    /** Accept the visitor. **/
    public Object jjtAccept(JamaicaParserVisitor visitor, Object data) throws Exception {
        return visitor.visit(this, data);
    }
}
