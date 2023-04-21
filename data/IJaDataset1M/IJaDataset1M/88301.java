package tyRuBa.engine;

import java.io.PrintStream;
import java.util.ArrayList;
import tyRuBa.modes.BindingList;
import tyRuBa.modes.CompositeType;
import tyRuBa.modes.ConstructorType;
import tyRuBa.modes.PredInfo;
import tyRuBa.modes.PredInfoProvider;
import tyRuBa.modes.Type;
import tyRuBa.modes.TypeConstructor;
import tyRuBa.modes.TupleType;
import tyRuBa.modes.TypeMapping;
import tyRuBa.modes.TypeModeError;

public abstract class ModedRuleBaseIndex implements PredInfoProvider {

    /** Create indexed rulebase for rb */
    ModedRuleBaseIndex() {
    }

    /** return an array of ModedRuleBase corresponding to the predicate name */
    protected abstract ModedRuleBaseCollection getModedRuleBases(PredicateIdentifier predID) throws TypeModeError;

    /** return the rulebase corresponding to predID which allow for bindings and
	 *  has the "best" mode of execution */
    public RuleBase getBest(PredicateIdentifier predID, BindingList bindings) {
        try {
            return getModedRuleBases(predID).getBest(bindings);
        } catch (TypeModeError e) {
            throw new Error("this should never happen");
        }
    }

    /** 
	 * insert predicate information to the set of known predicates 
	 *  for each predicate modes in the predicate info, a new ModedRuleBase is 
	 *  created and inserted into the hash map 
	 */
    public abstract void insert(PredInfo p) throws TypeModeError;

    /** typecheck r, then insert it in all of the rulebases corresponding to
	 *  r's predID */
    public final void insert(RBComponent r) throws TypeModeError {
        RBComponent converted = r.convertToNormalForm();
        TupleType resultTypes = converted.typecheck(this);
        ModedRuleBaseCollection rulebases = getModedRuleBases(converted.getPredId());
        rulebases.insertInEach(converted, this, resultTypes);
    }

    public abstract void dumpFacts(PrintStream out);

    public CompositeType addType(CompositeType type) throws TypeModeError {
        TypeConstructor newTypeConst = type.getTypeConstructor();
        TypeConstructor oldTypeConst = findTypeConst(newTypeConst.getName(), newTypeConst.getTypeArity());
        if (oldTypeConst != null) {
            if (oldTypeConst.isInitialized()) {
                throw new TypeModeError("Duplicate declaration for type " + type);
            } else {
                oldTypeConst.setParameter(type.getArgs());
                return (CompositeType) oldTypeConst.apply(type.getArgs(), false);
            }
        } else {
            newTypeConst.setParameter(type.getArgs());
            basicAddTypeConst(newTypeConst);
            return type;
        }
    }

    public abstract void addFunctorConst(Type repAs, CompositeType type);

    public abstract ConstructorType findConstructorType(FunctorIdentifier id);

    public abstract TypeConstructor findTypeConst(String typeName);

    public abstract TypeConstructor findTypeConst(String typeName, int arity);

    protected abstract void basicAddTypeConst(TypeConstructor typeConst);

    public abstract void addTypePredicate(TypeConstructor TypeConstructor, ArrayList subTypes);

    public final PredInfo getPredInfo(PredicateIdentifier predId) throws TypeModeError {
        PredInfo result = maybeGetPredInfo(predId);
        if (result == null) {
            if (predId.getArity() == 1) {
                TypeConstructor t = findTypeConst(predId.getName());
                if (t != null) {
                    NativePredicate.defineTypeTest(this, predId, t);
                    return maybeGetPredInfo(predId);
                }
            } else if (predId.getArity() == 2) {
                String name = predId.getName();
                if (name.startsWith("convertTo")) {
                    TypeConstructor t = findTypeConst(name.substring("convertTo".length()));
                    if (t != null) {
                        NativePredicate.defineConvertTo(this, t);
                        return maybeGetPredInfo(predId);
                    }
                }
            }
            throw new TypeModeError("Unknown predicate " + predId);
        } else {
            return result;
        }
    }

    public abstract void addTypeMapping(TypeMapping mapping, FunctorIdentifier id) throws TypeModeError;

    public abstract TypeMapping findTypeMapping(Class forWhat);

    /** Back up the factbases */
    public abstract void backup();
}
