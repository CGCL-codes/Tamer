package org.xmlsh.xproc.compiler;

import java.util.ArrayList;
import java.util.List;
import net.sf.saxon.s9api.Axis;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmNodeKind;
import net.sf.saxon.s9api.XdmSequenceIterator;
import org.xmlsh.xproc.util.XProcException;

class Otherwise {

    List<OutputOrLog> outputs = new ArrayList<OutputOrLog>();

    SubPipeline subpipeline = new SubPipeline();

    static Otherwise create(XdmNode node) {
        Otherwise o = new Otherwise();
        o.parse(node);
        return o;
    }

    protected void parse(XdmNode node) {
        parseChildren(node);
    }

    private void parseChildren(XdmNode parent) {
        Output output = null;
        Log log = null;
        XdmSequenceIterator children = parent.axisIterator(Axis.CHILD);
        while (children.hasNext()) {
            XdmItem item = children.next();
            if (item instanceof XdmNode) {
                XdmNode child = (XdmNode) item;
                if (child.getNodeKind() != XdmNodeKind.ELEMENT) continue;
                QName name = child.getNodeName();
                if (name.equals(Names.kOUTPUT)) output = Output.create(child); else if (name.equals(Names.kLOG)) log = Log.create(child);
                if (output != null || log != null) {
                    outputs.add(new OutputOrLog(output, log));
                    output = null;
                    log = null;
                    continue;
                }
                subpipeline.parse(child);
            }
        }
    }

    public void serialize(OutputContext c) throws XProcException {
        c.addBodyLine("else");
        subpipeline.serialize(c);
        c.addBodyLine("");
    }
}
