package org.xmlsh.xproc.compiler;

import net.sf.saxon.s9api.XdmNode;

class Import {

    String href;

    static Import create(XdmNode node) {
        Import imp = new Import();
        imp.parse(node);
        return imp;
    }

    void parse(XdmNode node) {
        href = XProcUtil.getAttrString(node, "href");
    }
}
