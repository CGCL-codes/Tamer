package org.deri.iris.rules.compiler;

import java.util.ArrayList;
import junit.framework.TestCase;
import org.deri.iris.Configuration;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.builtins.AddBuiltin;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.simple.SimpleRelationFactory;

public class TestBuiltin extends TestCase {

    protected void setUp() throws Exception {
    }

    public void testHasInputAllBound() throws Exception {
        Configuration configuration = new Configuration();
        IRelation inputRelation = configuration.relationFactory.createRelation();
        inputRelation.add(Helper.createTuple(1, 2, 3));
        inputRelation.add(Helper.createTuple(4, 4, 8));
        inputRelation.add(Helper.createTuple(1, 2, 4));
        inputRelation.add(Helper.createTuple(1, 3, 3));
        ITuple criteria = Helper.createTuple("X", "Y", "Z");
        View view = new View(inputRelation, criteria, configuration.relationFactory);
        IBuiltinAtom builtinPredicate = new AddBuiltin(criteria.toArray(new ITerm[0]));
        Builtin builtin = new Builtin(view.variables(), builtinPredicate, true, configuration);
        IRelation result = builtin.process(view.getView());
        assertEquals(2, result.size());
    }

    public void testTooManyUnbound() {
        Configuration configuration = new Configuration();
        IRelation inputRelation = configuration.relationFactory.createRelation();
        ITuple criteria = Helper.createTuple("X");
        View view = new View(inputRelation, criteria, configuration.relationFactory);
        ITuple builtinTuple = Helper.createTuple("X", "Y", "Z");
        IBuiltinAtom builtinPredicate = new AddBuiltin(builtinTuple.toArray(new ITerm[0]));
        try {
            new Builtin(view.variables(), builtinPredicate, true, configuration);
            fail("Builtin should have thrown an exception");
        } catch (Exception e) {
        }
    }

    public void testHasInputOneUnbound() throws Exception {
        Configuration configuration = new Configuration();
        IRelation inputRelation = configuration.relationFactory.createRelation();
        inputRelation.add(Helper.createTuple(1, 2));
        inputRelation.add(Helper.createTuple(4, 4));
        inputRelation.add(Helper.createTuple(1, 2));
        inputRelation.add(Helper.createTuple(1, 3));
        ITuple criteria = Helper.createTuple("X", "Y");
        View view = new View(inputRelation, criteria, configuration.relationFactory);
        ITuple builtinTuple = Helper.createTuple("X", "Y", "Z");
        IBuiltinAtom builtinPredicate = new AddBuiltin(builtinTuple.toArray(new ITerm[0]));
        Builtin builtin = new Builtin(view.variables(), builtinPredicate, true, configuration);
        IRelation output = builtin.process(view.getView());
        assertEquals(3, output.size());
        assertEquals(Helper.createTerm("X"), builtin.getOutputVariables().get(0));
        assertEquals(Helper.createTerm("Y"), builtin.getOutputVariables().get(1));
        assertEquals(Helper.createTerm("Z"), builtin.getOutputVariables().get(2));
    }

    public void testNoInput() throws Exception {
        Configuration configuration = new Configuration();
        ITuple builtinTuple = Helper.createTuple(3, 4, "X");
        IBuiltinAtom builtinPredicate = new AddBuiltin(builtinTuple.toArray(new ITerm[0]));
        Builtin builtin = new Builtin(new ArrayList<IVariable>(), builtinPredicate, true, configuration);
        IRelation output = builtin.process(mRelationWithOneZeroLengthTuple);
        assertEquals(Helper.createTerm("X"), builtin.getOutputVariables().get(0));
        assertEquals(1, output.size());
        assertEquals(Helper.createTerm(7), output.get(0).get(0));
    }

    private static final IRelation mRelationWithOneZeroLengthTuple = new SimpleRelationFactory().createRelation();

    static {
        mRelationWithOneZeroLengthTuple.add(Factory.BASIC.createTuple());
    }
}
