package steal.analysis;

import java.util.HashMap;
import java.util.List;
import spoon.reflect.code.CtAbstractInvocation;
import spoon.reflect.eval.SymbolicEvaluationPath;
import spoon.reflect.eval.SymbolicInstance;
import spoon.reflect.reference.CtTypeReference;
import steal.Element;
import steal.Expression;
import steal.Interaction;
import steal.Name;
import steal.Object;
import steal.Operation;
import steal.Repeat;
import steal.Sequence;
import steal.Type;
import steal.visitor.Visitor;

public class Analyzer implements Visitor {

    HashMap<Name, SymbolicInstance> objects = new HashMap<Name, SymbolicInstance>();

    HashMap<Name, CtAbstractInvocation> operations = new HashMap<Name, CtAbstractInvocation>();

    HashMap<Name, SymbolicInstance> types = new HashMap<Name, SymbolicInstance>();

    int cursor = 1;

    boolean result = true;

    SymbolicInstance<?> curInst = null;

    SymbolicEvaluationPath path;

    public Analyzer(SymbolicEvaluationPath path) {
        this.path = path;
    }

    public Analyzer(SymbolicEvaluationPath path, int cursor) {
        this.path = path;
        this.cursor = cursor;
    }

    private boolean analyze(Element element) {
        if (element != null) element.accept(this);
        return result;
    }

    public void visitInteraction(Interaction interaction) {
        if (!EOP()) {
            curInst = path.getStep(cursor).getFrame().getCaller();
            if (analyze(interaction.getClient())) {
                if (analyze(interaction.getOperation())) {
                    curInst = path.getStep(cursor).getFrame().getThis();
                    if (analyze(interaction.getServer())) {
                        result = true;
                        return;
                    }
                }
            }
        }
        result = false;
    }

    public void visitObject(Object object) {
        analyze(object.getType());
        if (result && !object.isFree()) {
            if (objects.get(object.getName()) == null) {
                System.out.println(object.getName() + " not in " + objects);
                throw new RuntimeException("unbound object variable '" + object.getName() + "' in expression " + object.getParent());
            }
            result = objects.get(object.getName()).equalsRef(curInst);
        }
        if (result && object.getBinding() != null) {
            objects.put(object.getBinding(), curInst);
        }
        curInst = null;
    }

    private boolean EOP() {
        return !(cursor < path.getStepCount());
    }

    public void visitSequence(Sequence sequence) {
        if (result = analyze(sequence.getCurrent())) {
            cursor++;
            result = analyze(sequence.getNext());
        }
    }

    public void visitName(Name name) {
    }

    private boolean analyzeParameters(Operation operation) {
        if (operation.getParameters() == null) return result = true;
        List<SymbolicInstance<?>> args = path.getStep(cursor).getFrame().getArguments();
        if (args.size() != operation.getParameters().size()) {
            return result = false;
        }
        int index = 0;
        for (SymbolicInstance i : args) {
            curInst = i;
            if (!analyze(operation.getParameters().get(index++))) {
                return false;
            }
        }
        return result;
    }

    public void visitOperation(Operation operation) {
        if (!analyzeParameters(operation)) return;
        CtAbstractInvocation inv = path.getStep(cursor).getFrame().getInvocation();
        if (!operation.getName().isFree()) {
            if (operations.get(operation.getName()) != null) {
                result = operations.get(operation.getName()).getExecutable().getSimpleName().equals(operation.getName().getValue());
            } else {
                result = inv.getExecutable().getSimpleName().equals(operation.getName().getValue());
            }
        }
        if (result && operation.getBinding() != null) {
            operations.put(operation.getBinding(), inv);
        }
    }

    @SuppressWarnings("unchecked")
    private boolean stop(Expression stop, boolean ignore) {
        if (stop == null || ignore) return false;
        Analyzer a = new Analyzer(path, cursor);
        a.objects = (HashMap<Name, SymbolicInstance>) objects.clone();
        a.operations = (HashMap<Name, CtAbstractInvocation>) operations.clone();
        stop.accept(a);
        if (a.result) cursor--;
        return a.result;
    }

    public void visitRepeat(Repeat repeat) {
        switch(repeat.getKind()) {
            case ZERO2MANY:
                analyzeRepeat(repeat, 0);
                break;
            case ONE2MANY:
                analyzeRepeat(repeat, 1);
                break;
        }
    }

    private void analyzeRepeat(Repeat repeat, int minMatches) {
        Expression stop = null;
        if (repeat.getParent() instanceof Sequence) {
            Sequence s = (Sequence) repeat.getParent();
            if (s.getCurrent() == repeat) {
                stop = s.getNext();
            }
        }
        while (!EOP() && !stop(stop, minMatches > 0) && analyze(repeat.getExpression())) {
            cursor++;
            minMatches--;
        }
        if (!result) {
            cursor--;
            result = true;
        }
        if (minMatches > 0) result = false;
    }

    public void visitType(Type type) {
        if (curInst == null) return;
        if (!type.isFree()) {
            SymbolicInstance i = types.get(type.getName());
            result = i.getConcreteType().equals(curInst.getConcreteType());
        }
        if (type.getBound() != null) {
            CtTypeReference r = curInst.getConcreteType().getFactory().Type().createReference(type.getBound().getName().getValue());
            result = curInst.getConcreteType().isSubtypeOf(r);
        }
        if (result && type.getBinding() != null) {
            types.put(type.getBinding(), curInst);
        }
    }
}
