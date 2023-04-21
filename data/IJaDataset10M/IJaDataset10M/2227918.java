package org.jdna.bmt.web.client.ui.filechooser;

import java.io.Serializable;

public class JSFileResult implements Serializable {

    public JSFile dir;

    public JSFile parent;

    public JSFile[] children;

    public JSFileResult() {
    }

    public JSFileResult(JSFile parent, JSFile dir, JSFile[] children) {
        this.parent = parent;
        this.dir = dir;
        this.children = children;
    }

    public JSFile getDir() {
        return dir;
    }

    public void setDir(JSFile dir) {
        this.dir = dir;
    }

    public JSFile getParent() {
        return parent;
    }

    public void setParent(JSFile parent) {
        this.parent = parent;
    }

    public JSFile[] getChildren() {
        return children;
    }

    public void setChildren(JSFile[] children) {
        this.children = children;
    }
}
