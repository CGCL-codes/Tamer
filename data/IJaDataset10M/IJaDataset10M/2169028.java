package com.teknokala.xtempore.xml.writer;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.teknokala.xtempore.util.DebugLogger;

/**
 * @author Timo Santasalo <timo.santasalo@teknokala.com>
 */
public class OutputContext {

    private Logger log = LoggerFactory.getLogger(getClass());

    private final DebugLogger dbg = new DebugLogger(log);

    private SubContext ctx = new SubContext(0, null);

    private int depth = 0;

    private boolean tagOpen = false;

    private boolean xmlDeclarationWritten = false;

    private String charset;

    public OutputContext(String charset) {
        super();
        this.charset = charset;
    }

    public String getCharset() {
        return charset;
    }

    public void setXmlDeclarationWritten(boolean xmlDeclarationWritten) {
        this.xmlDeclarationWritten = xmlDeclarationWritten;
    }

    public boolean isXmlDeclarationWritten() {
        return xmlDeclarationWritten;
    }

    public void setTagOpen(boolean tagOpen) {
        this.tagOpen = tagOpen;
    }

    public boolean isTagOpen() {
        return tagOpen;
    }

    public void incDepth() {
        depth++;
        assert dbg.log("depth increased to {}", depth);
    }

    public void decDepth() {
        depth--;
        assert dbg.log("depth decreased to {}", depth);
    }

    public void setDepth(int depth) {
        this.depth = depth;
        assert dbg.log("depth set to {}", depth);
    }

    public int getDepth() {
        return depth;
    }

    public void push() {
        if (ctx.level < depth) {
            ctx = new SubContext(depth, ctx);
            assert dbg.log("pushed context to stack");
        }
    }

    public void pop() {
        if (ctx.level > depth) {
            ctx = ctx.parent;
            assert dbg.log("popped context to stack");
        }
    }

    public int getLevel() {
        return ctx.level;
    }

    public void registerPrefix(String prefix) {
        ctx.registerPrefix(prefix);
        assert dbg.log("registered prefix: {}", prefix);
    }

    public boolean containsPrefix(String prefix) {
        return ctx.containsPrefix(prefix);
    }

    private static class SubContext {

        private final int level;

        private final SubContext parent;

        private List<String> data = new ArrayList<String>();

        public SubContext(final int level, final SubContext parent) {
            super();
            this.level = level;
            this.parent = parent;
        }

        public void registerPrefix(String prefix) {
            data.add(prefix);
        }

        public boolean containsPrefix(String prefix) {
            return data.contains(prefix) || parent != null && parent.containsPrefix(prefix);
        }
    }
}
