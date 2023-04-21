package org.netxilia.server.js.data;

public class JsAreaReference {

    public JsCellReference topLeft;

    public JsCellReference bottomRight;

    public JsAreaReference(JsCellReference topLeft, JsCellReference bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }
}
