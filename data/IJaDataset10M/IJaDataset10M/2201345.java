package tyRuBa.engine;

import java.util.Vector;
import tyRuBa.engine.compilation.CompilationContext;
import tyRuBa.engine.compilation.Compiled;
import tyRuBa.engine.factbase.*;
import tyRuBa.modes.BindingList;
import tyRuBa.modes.Factory;
import tyRuBa.modes.Mode;
import tyRuBa.modes.PredicateMode;
import tyRuBa.modes.TupleType;
import tyRuBa.modes.TypeModeError;

/** 
 * A ModedRuleBase is a rulebase that has a mode associate to it. A Rulebase
 * stores a collection of Logic inference rules.
 */
public class ModedRuleBase extends RuleBase {

    private RBComponentVector rules = null;

    private FactBase facts;

    private Mode currMode = null;

    private Vector[] currTypes = null;

    private PredicateIdentifier predId;

    /** Constructor */
    public ModedRuleBase(QueryEngine engine, PredicateMode predMode, FactBase allTheFacts, PredicateIdentifier predId) {
        super(engine, predMode);
        this.facts = allTheFacts;
        this.predId = predId;
        ensureRuleBase();
        currTypes = new Vector[predMode.getParamModes().getNumBound()];
        for (int i = 0; i < currTypes.length; i++) {
            currTypes[i] = new Vector();
        }
    }

    public void insert(RBComponent r, ModedRuleBaseIndex insertedFrom, TupleType inferredTypes) throws TypeModeError {
        try {
            PredicateMode thisRBMode = getPredMode();
            RBComponent converted = r.convertToMode(thisRBMode, Factory.makeModeCheckContext(insertedFrom));
            if (getPredMode().toBeCheck()) {
                BindingList bindings = thisRBMode.getParamModes();
                TupleType boundTypes = Factory.makeTupleType();
                for (int i = 0; i < bindings.size(); i++) {
                    if (bindings.get(i).isBound()) {
                        boundTypes.add(inferredTypes.get(i));
                    }
                }
                if (currMode == null) {
                    currMode = converted.getMode();
                    for (int i = 0; i < currTypes.length; i++) {
                        currTypes[i].add(boundTypes.get(i));
                    }
                } else if (currTypes.length == 0) {
                    currMode = currMode.add(converted.getMode());
                } else {
                    boolean hasOverlap = true;
                    for (int i = 0; i < currTypes.length; i++) {
                        hasOverlap = boundTypes.get(i).hasOverlapWith(currTypes[i], hasOverlap);
                    }
                    if (hasOverlap && currTypes.length > 0) {
                        currMode = currMode.add(converted.getMode());
                    } else {
                        currMode = currMode.noOverlapWith(converted.getMode());
                    }
                }
                if (currMode.compatibleWith(getMode())) {
                    privateInsert(converted, insertedFrom);
                } else {
                    throw new TypeModeError("Inferred mode exceeds declared mode in " + converted.getPredName() + "\n" + "inferred mode: " + currMode + "	declared mode: " + getMode());
                }
            } else {
                privateInsert(converted, insertedFrom);
            }
        } catch (TypeModeError e) {
            throw new TypeModeError("while converting " + r + " to mode: " + getPredMode() + "\n" + e.getMessage());
        }
    }

    /**
	 * This is called *after* converting into mode
	 */
    private void privateInsert(RBComponent converted, ModedRuleBaseIndex insertedFrom) throws TypeModeError {
        ensureRuleBase();
        rules.insert(converted);
    }

    private void ensureRuleBase() {
        if (rules == null) {
            rules = new RBComponentVector();
        }
    }

    public String toString() {
        return "/******** BEGIN ModedRuleBase ***********************/\n" + "Predicate mode: " + getPredMode() + "\n" + "Inferred mode: " + currMode + "\n" + rules + "\n" + "/******** END ModedRuleBase *************************/";
    }

    public int hashCode() {
        throw new Error("That's strange... who wants to know my hashcode??");
    }

    protected Compiled compile(CompilationContext context) {
        if (rules != null) {
            return facts.compile(getPredMode(), context).disjoin(rules.compile(context));
        } else {
            return facts.compile(getPredMode(), context);
        }
    }
}
