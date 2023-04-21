package org.xmlsh.xproc.compiler;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import net.sf.saxon.s9api.Axis;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmNodeKind;
import net.sf.saxon.s9api.XdmSequenceIterator;

class ForEach extends AbstractStep {

    static class ForEachOption extends OutputOrLog {

        IterationSource iteration_source;

        ForEachOption(IterationSource iteration_source, Output output, Log log) {
            super(output, log);
            this.iteration_source = iteration_source;
        }
    }

    List<ForEachOption> foreach_options = new ArrayList<ForEachOption>();

    SubPipeline subpipeline = new SubPipeline();

    static ForEach create(XdmNode node) {
        ForEach step = new ForEach();
        step.parse(node);
        return step;
    }

    protected void parse(XdmNode node) {
        super.parse(node);
        parseChildren(node);
    }

    private void parseChildren(XdmNode parent) {
        IterationSource iteration_source = null;
        Output output = null;
        Log log = null;
        XdmSequenceIterator children = parent.axisIterator(Axis.CHILD);
        while (children.hasNext()) {
            XdmItem item = children.next();
            if (item instanceof XdmNode) {
                XdmNode child = (XdmNode) item;
                QName name = child.getNodeName();
                if (child.getNodeKind() != XdmNodeKind.ELEMENT) continue;
                if (name.equals(Names.kITERATION_SOURCE)) iteration_source = IterationSource.create(child); else if (name.equals(Names.kOUTPUT)) output = Output.create(child); else if (name.equals(Names.kLOG)) log = Log.create(child);
                if (iteration_source != null && (output != null || log != null)) {
                    foreach_options.add(new ForEachOption(iteration_source, output, log));
                    iteration_source = null;
                    output = null;
                    log = null;
                    continue;
                }
                subpipeline.parse(child);
            }
        }
    }

    @Override
    void serialize(OutputContext c) {
    }
}
