package com.taobao.b2c.neaten.javascript;

import com.taobao.b2c.neaten.BaseNode;
import com.taobao.b2c.neaten.Node;

/**
 * @author xiaoxie
 */
public class JavaScriptNode extends BaseNode {

    public void setOriginalSource(String source) {
        super.setOriginalSource(source);
        this.setContentType(Node.CONTENT_TYPE_CODE);
        this.setName("javascript");
        this.setFormatType(Node.FORMAT_TYPE_CONTENT);
        parseName(source);
    }

    private void parseName(String tagSource) {
        tagSource = tagSource.toLowerCase().trim();
        if ("{".equals(tagSource)) {
            this.setFormatType(Node.FORMAT_TYPE_OPEN);
            this.setName("{");
        }
        if ("}".equals(tagSource)) {
            this.setFormatType(Node.FORMAT_TYPE_CLOSE);
            this.setName("}");
        }
        if (tagSource.startsWith("if")) {
            this.setFormatType(Node.FORMAT_TYPE_OPEN_FIRST_LINE);
            this.setName("if-else");
        }
        if (tagSource.startsWith("else")) {
            this.setFormatType(Node.FORMAT_TYPE_OPEN_FIRST_LINE);
            this.setName("if-else");
        }
        if (tagSource.startsWith("for")) {
            this.setFormatType(Node.FORMAT_TYPE_OPEN_FIRST_LINE);
            this.setName("for");
        }
    }

    public boolean isMatch(Node node) {
        if (node == null || node.getName() == null) {
            return false;
        }
        if (this.getName().equals("}")) {
            if (node.getName().equals("{")) return true;
        }
        return false;
    }
}
