package org.tzi.use.gen.assl.dynamics;

import org.tzi.use.gen.assl.statics.GInstrInsert_Assoc_Linkends;
import org.tzi.use.gen.assl.statics.GInstruction;
import org.tzi.use.gen.assl.statics.GValueInstruction;
import org.tzi.use.uml.ocl.value.Value;
import org.tzi.use.uml.ocl.value.ObjectValue;
import org.tzi.use.uml.sys.MCmd;
import org.tzi.use.uml.sys.MCmdInsertLink;
import org.tzi.use.util.cmd.CommandFailedException;
import org.tzi.use.util.cmd.CannotUndoException;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Iterator;
import org.tzi.use.uml.ocl.expr.Expression;
import org.tzi.use.uml.ocl.expr.ExpVariable;
import org.tzi.use.uml.sys.MObject;

class GEvalInstrInsert_Assoc_Linkends extends GEvalInstruction implements IGCaller {

    private GInstrInsert_Assoc_Linkends fInstr;

    private IGCaller fCaller;

    private ListIterator fIterator;

    private List fObjectNames;

    public GEvalInstrInsert_Assoc_Linkends(GInstrInsert_Assoc_Linkends instr) {
        fInstr = instr;
    }

    public void eval(GConfiguration conf, IGCaller caller, IGCollector collector) throws GEvaluationException {
        collector.detailPrintWriter().println("evaluating `" + fInstr + "'");
        fCaller = caller;
        fIterator = fInstr.linkEnds().listIterator();
        fObjectNames = new ArrayList();
        GCreator.createFor((GInstruction) fIterator.next()).eval(conf, this, collector);
        fIterator.previous();
    }

    public void feedback(GConfiguration conf, Value value, IGCollector collector) throws GEvaluationException {
        if (value.isUndefined()) {
            collector.invalid(buildCantExecuteMessage(fInstr, (GValueInstruction) fInstr.linkEnds().get(fIterator.previousIndex())));
            return;
        }
        fObjectNames.add(((ObjectValue) value).value().name());
        if (fIterator.hasNext()) {
            GCreator.createFor((GInstruction) fIterator.next()).eval(conf, this, collector);
            fIterator.previous();
        } else createLink(conf, collector);
    }

    private void createLink(GConfiguration conf, IGCollector collector) throws GEvaluationException {
        Expression[] exprs = new Expression[fObjectNames.size()];
        Iterator it = fObjectNames.iterator();
        int i = 0;
        while (it.hasNext()) {
            MObject obj = conf.systemState().objectByName((String) it.next());
            exprs[i++] = new ExpVariable(obj.name(), obj.type());
        }
        MCmd cmd = new MCmdInsertLink(conf.systemState(), exprs, fInstr.association());
        try {
            collector.basicPrintWriter().println(cmd.getUSEcmd());
            cmd.execute();
            fCaller.feedback(conf, null, collector);
            if (collector.expectSubsequentReporting()) {
                collector.subsequentlyPrependCmd(cmd);
            }
            collector.basicPrintWriter().println("undo: " + cmd.getUSEcmd());
            cmd.undo();
        } catch (CommandFailedException e) {
            collector.invalid(e);
        } catch (CannotUndoException e) {
            throw new GEvaluationException(e);
        }
    }

    public String toString() {
        return "GEvalInstrInsert_Assoc_Linkends";
    }
}
