package com.google.devtools.depan.javascript.graph;

/**
 * Define how a JavaScript functionentity is represented in a dependency graph.
 * 
 * @author <a href="leeca@google.com">Lee Carver</a>
 */
public class JavaScriptFunctionElement extends JavaScriptElement {

    private final String functionName;

    /**
   * Construct a graph element for JavaScript function entity.
   */
    public JavaScriptFunctionElement(String variableName) {
        this.functionName = variableName;
    }

    @Override
    public void accept(JavaScriptElementVisitor visitor) {
        visitor.visitFunction(this);
    }

    @Override
    public String friendlyString() {
        return getElementName();
    }

    @Override
    public String getJavaScriptId() {
        return functionName;
    }
}
