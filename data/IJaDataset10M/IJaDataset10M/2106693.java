package org.norecess.nolatte.primitives.group;

import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.Primitive;
import org.norecess.nolatte.interpreters.IInterpreter;
import org.norecess.nolatte.system.NoLatteVariables;

public class MemberPredicate extends Primitive {

    private static final long serialVersionUID = 6959035789708772710L;

    private final IMemberPredicateFactory myFactory;

    public MemberPredicate(IMemberPredicateFactory factory) {
        myFactory = factory;
        getParameters().addPositional(NoLatteVariables.DATUM);
        getParameters().addPositional(NoLatteVariables.GROUP);
    }

    public Datum apply(IInterpreter interpreter) {
        return apply(interpreter, interpreter.getEnvironment().get(NoLatteVariables.DATUM), interpreter.getEnvironment().get(NoLatteVariables.GROUP));
    }

    private Datum apply(IInterpreter interpreter, Datum element, Datum group) {
        return group.accept(myFactory.createVisitor(myFactory.createPredicate(interpreter.getDataTypeFilter(), element)));
    }
}
