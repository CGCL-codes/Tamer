package org.tzi.use.gen.assl.dynamics;

import org.tzi.use.gen.assl.statics.GInstrTry_Assoc_LinkendSeqs;
import org.tzi.use.gen.assl.statics.GInstruction;
import org.tzi.use.gen.assl.statics.GValueInstruction;
import org.tzi.use.uml.ocl.value.Value;
import org.tzi.use.uml.ocl.value.ObjectValue;
import org.tzi.use.uml.ocl.value.CollectionValue;
import org.tzi.use.uml.sys.MCmd;
import org.tzi.use.uml.sys.MCmdInsertLink;
import org.tzi.use.uml.sys.MCmdDeleteLink;
import org.tzi.use.uml.sys.MObject;
import org.tzi.use.uml.sys.MSystemException;
import org.tzi.use.util.cmd.CommandFailedException;
import org.tzi.use.util.cmd.CannotUndoException;
import org.tzi.use.util.ListUtil;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.math.BigInteger;
import org.tzi.use.uml.ocl.expr.Expression;
import org.tzi.use.uml.ocl.expr.ExpVariable;

class GEvalInstrTry_Assoc_LinkendSeqs extends GEvalInstruction implements IGCaller {

    private GInstrTry_Assoc_LinkendSeqs fInstr;

    private IGCaller fCaller;

    private ListIterator fIterator;

    private List fObjectLists;

    private GInstruction fLastEvaluatedInstruction;

    public GEvalInstrTry_Assoc_LinkendSeqs(GInstrTry_Assoc_LinkendSeqs instr) {
        fInstr = instr;
    }

    public void eval(GConfiguration conf, IGCaller caller, IGCollector collector) throws GEvaluationException {
        collector.detailPrintWriter().println("evaluating `" + fInstr + "'");
        fCaller = caller;
        fIterator = fInstr.linkendSequences().listIterator();
        fObjectLists = new ArrayList();
        fLastEvaluatedInstruction = (GInstruction) fIterator.next();
        GCreator.createFor(fLastEvaluatedInstruction).eval(conf, this, collector);
        fIterator.previous();
    }

    public void feedback(GConfiguration conf, Value value, IGCollector collector) throws GEvaluationException {
        if (value.isUndefined()) {
            collector.invalid(buildCantExecuteMessage(fInstr, (GValueInstruction) fLastEvaluatedInstruction));
            return;
        }
        List objects = new ArrayList();
        Iterator it = ((CollectionValue) value).iterator();
        while (it.hasNext()) {
            Value elem = (Value) it.next();
            if (elem.isUndefined()) {
                collector.invalid("Can't execute `" + fInstr + "', because the result of `" + fLastEvaluatedInstruction + "' contains an undefined value.");
                return;
            }
            objects.add(((ObjectValue) elem).value());
        }
        fObjectLists.add(objects);
        if (fIterator.hasNext()) {
            fLastEvaluatedInstruction = (GInstruction) fIterator.next();
            GCreator.createFor(fLastEvaluatedInstruction).eval(conf, this, collector);
            fIterator.previous();
        } else tryLinks(conf, collector);
    }

    private void tryLinks(GConfiguration conf, IGCollector collector) throws GEvaluationException {
        int MAX_LINKS = 62;
        List combinations = ListUtil.combinations(fObjectLists);
        if (combinations.size() > MAX_LINKS) {
            collector.invalid("Can't execute `" + fInstr + "', because there" + "are more than 2^" + MAX_LINKS + "combinations.");
            return;
        }
        List cmdList = new ArrayList(combinations.size());
        List cmdsToRemoveExistingLinks = new ArrayList();
        Iterator combIt = combinations.iterator();
        while (combIt.hasNext()) {
            List objects = (List) combIt.next();
            Iterator objectsIt = objects.iterator();
            List names = new ArrayList(objects.size());
            while (objectsIt.hasNext()) names.add(((MObject) objectsIt.next()).name());
            Expression[] exprs = new Expression[objects.size()];
            Iterator it = objects.iterator();
            int i = 0;
            while (it.hasNext()) {
                MObject obj = (MObject) it.next();
                exprs[i++] = new ExpVariable(obj.name(), obj.type());
            }
            cmdList.add(new MCmdInsertLink(conf.systemState(), exprs, fInstr.association()));
            try {
                if (conf.systemState().hasLink(fInstr.association(), objects)) cmdsToRemoveExistingLinks.add(new MCmdDeleteLink(conf.systemState(), exprs, fInstr.association()));
            } catch (MSystemException e) {
                throw new GEvaluationException(e);
            }
        }
        Object[] cmds = cmdList.toArray();
        Iterator toRemove = cmdsToRemoveExistingLinks.iterator();
        try {
            while (toRemove.hasNext()) {
                MCmd cmd = (MCmd) toRemove.next();
                collector.basicPrintWriter().println(cmd.getUSEcmd());
                cmd.execute();
            }
        } catch (CommandFailedException e) {
            throw new GEvaluationException(e);
        }
        BigInteger combination = BigInteger.ZERO;
        BigInteger previousCombination = combination;
        long count = (new BigInteger("2")).pow(cmdList.size()).longValue();
        int lowestSetBit;
        try {
            while (combination.longValue() < count && !collector.canStop()) {
                BigInteger addedBits = combination.andNot(previousCombination);
                lowestSetBit = addedBits.getLowestSetBit();
                while (lowestSetBit != -1) {
                    MCmd cmd = (MCmd) cmds[lowestSetBit];
                    collector.basicPrintWriter().println(cmd.getUSEcmd());
                    cmd.execute();
                    addedBits = addedBits.andNot(BigInteger.ZERO.setBit(lowestSetBit));
                    lowestSetBit = addedBits.getLowestSetBit();
                }
                BigInteger removedBits = previousCombination.andNot(combination);
                lowestSetBit = removedBits.getLowestSetBit();
                while (lowestSetBit != -1) {
                    MCmd cmd = (MCmd) cmds[lowestSetBit];
                    collector.basicPrintWriter().println("undo: " + cmd.getUSEcmd());
                    cmd.undo();
                    removedBits = removedBits.andNot(BigInteger.ZERO.setBit(lowestSetBit));
                    lowestSetBit = removedBits.getLowestSetBit();
                }
                fCaller.feedback(conf, null, collector);
                if (collector.expectSubsequentReporting()) {
                    BigInteger comb = combination;
                    lowestSetBit = comb.getLowestSetBit();
                    while (lowestSetBit != -1) {
                        collector.subsequentlyPrependCmd((MCmd) cmds[lowestSetBit]);
                        comb = comb.andNot(BigInteger.ZERO.setBit(lowestSetBit));
                        lowestSetBit = comb.getLowestSetBit();
                    }
                }
                previousCombination = combination;
                combination = combination.add(BigInteger.ONE);
            }
        } catch (CommandFailedException e) {
            throw new GEvaluationException(e);
        } catch (CannotUndoException e) {
            throw new GEvaluationException(e);
        }
        try {
            lowestSetBit = previousCombination.getLowestSetBit();
            while (lowestSetBit != -1) {
                MCmd cmd = (MCmd) cmds[lowestSetBit];
                collector.basicPrintWriter().println("undo: " + cmd.getUSEcmd());
                cmd.undo();
                previousCombination = previousCombination.andNot(BigInteger.ZERO.setBit(lowestSetBit));
                lowestSetBit = previousCombination.getLowestSetBit();
            }
            Iterator toReinsert = cmdsToRemoveExistingLinks.iterator();
            while (toReinsert.hasNext()) {
                MCmd cmd = (MCmd) toReinsert.next();
                collector.basicPrintWriter().println("undo: " + cmd.getUSEcmd());
                cmd.undo();
            }
        } catch (CannotUndoException e) {
            throw new GEvaluationException(e);
        }
    }

    public String toString() {
        return "GEvalInstrTry_Assoc_LinkendSeqs";
    }
}
