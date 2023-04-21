package org.xmlsh.xproc.compiler;

import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.trans.XPathException;
import org.xmlsh.xproc.util.XProcException;

class Inline extends Binding {

    String[] exclude_inline_prefixes;

    XdmNode node;

    @Override
    void parse(XdmNode node) {
        exclude_inline_prefixes = XProcUtil.getAttrList(node, "exclude-inline-prefixes");
        this.node = XProcUtil.getFirstChild(node);
    }

    @Override
    void serialize(OutputContext c) throws XProcException {
        c.addBodyLine("<<EOF");
        try {
            c.addBody(XProcUtil.serialize(node));
        } catch (XPathException e) {
            throw new XProcException(e);
        }
        c.addBodyLine("");
        c.addBodyLine("EOF");
    }

    @Override
    boolean isInput() {
        return true;
    }
}
