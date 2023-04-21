package com.google.devtools.depan.javascript.graph;

/**
 * @author <a href="leeca@google.com">Lee Carver</a>
 */
public class JavaScriptVariableElement extends JavaScriptElement {

    private final String variableName;

    /**
   * 
   */
    public JavaScriptVariableElement(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public void accept(JavaScriptElementVisitor visitor) {
        visitor.visitVariable(this);
    }

    @Override
    public String friendlyString() {
        return getElementName();
    }

    @Override
    public String getJavaScriptId() {
        return variableName;
    }
}
