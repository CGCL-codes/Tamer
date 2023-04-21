package org.orbeon.oxf.processor.pipeline.ast;

public class ASTParam extends ASTNodeContainer implements ASTDebugSchema {

    public static int INPUT = 0;

    public static int OUTPUT = 1;

    public ASTParam() {
    }

    public ASTParam(int type, String name) {
        this.type = type;
        this.name = name;
    }

    private int type;

    private String name;

    private String schemaHref;

    private String schemaUri;

    private String debug;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchemaHref() {
        return schemaHref;
    }

    public void setSchemaHref(String schemaHref) {
        this.schemaHref = schemaHref;
    }

    public String getSchemaUri() {
        return schemaUri;
    }

    public void setSchemaUri(String schemaUri) {
        this.schemaUri = schemaUri;
    }

    public String getDebug() {
        return debug;
    }

    public void setDebug(String debug) {
        this.debug = debug;
    }

    public void walk(ASTHandler handler) {
        handler.param(this);
    }
}
