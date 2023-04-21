package edu.vub.at.objects.natives.grammar;

import edu.vub.at.AmbientTalkTest;
import edu.vub.at.eval.Evaluator;
import edu.vub.at.exceptions.InterpreterException;
import edu.vub.at.exceptions.XArityMismatch;
import edu.vub.at.exceptions.XIllegalParameter;
import edu.vub.at.objects.ATObject;
import edu.vub.at.objects.grammar.ATSymbol;
import edu.vub.at.objects.natives.NATContext;
import edu.vub.at.objects.natives.NATMethod;
import edu.vub.at.objects.natives.NATNumber;
import edu.vub.at.objects.natives.NATObject;
import edu.vub.at.objects.natives.NATTable;

/**
 * Unit test for parameter binding during e.g. function call.
 *
 * @author tvcutsem
 */
public class TestParameterBinding extends AmbientTalkTest {

    private void ensureBound(ATSymbol var, ATObject value) throws InterpreterException {
        assertEquals(value, bindScope_.impl_invokeAccessor(bindScope_, var, NATTable.EMPTY));
    }

    private void ensureBoundToTable(ATSymbol var, ATObject[] expected) throws InterpreterException {
        NATTable val = bindScope_.impl_invokeAccessor(bindScope_, var, NATTable.EMPTY).asNativeTable();
        ATObject[] values = val.elements_;
        assertEquals(expected.length, values.length);
        for (int i = 0; i < values.length; i++) {
            assertEquals(expected[i], values[i]);
        }
    }

    /**
	 * Given a name and parameters, returns a method
	 * def name(parameters) { nil }
	 */
    private NATMethod makeTestMethod(String nam, NATTable pars) {
        try {
            return new NATMethod(AGSymbol.jAlloc(nam), pars, new AGBegin(NATTable.atValue(new ATObject[] { Evaluator.getNil() })), NATTable.EMPTY);
        } catch (InterpreterException e) {
            fail("unexpected exception while creating test fixture: " + e.getMessage());
            return null;
        }
    }

    private NATContext bindCtx_;

    private NATObject bindScope_;

    private final AGSymbol at_a = AGSymbol.jAlloc("a");

    private final AGSymbol at_b = AGSymbol.jAlloc("b");

    private final AGSymbol at_rest = AGSymbol.jAlloc("rest");

    private final AGSymbol at_x = AGSymbol.jAlloc("x");

    private final AGSymbol at_y = AGSymbol.jAlloc("y");

    private NATMethod fun1 = makeTestMethod("fun1", NATTable.EMPTY);

    private NATMethod fun2 = makeTestMethod("fun2", NATTable.atValue(new ATObject[] { at_a, at_b }));

    private NATMethod fun3 = makeTestMethod("fun3", NATTable.atValue(new ATObject[] { at_a, at_b, new AGAssignVariable(at_x, NATNumber.ONE) }));

    private NATMethod fun4 = makeTestMethod("fun4", NATTable.atValue(new ATObject[] { at_a, new AGAssignVariable(at_x, at_a), new AGSplice(at_rest) }));

    private NATMethod fun5 = makeTestMethod("fun5", NATTable.atValue(new ATObject[] { new AGAssignVariable(at_x, AGSelf._INSTANCE_), new AGAssignVariable(at_y, NATNumber.ZERO) }));

    private NATMethod fun6 = makeTestMethod("fun6", NATTable.atValue(new ATObject[] { at_a, new AGSplice(at_rest) }));

    private NATMethod fun7 = makeTestMethod("fun7", NATTable.atValue(new ATObject[] { new AGSplice(at_rest) }));

    public void setUp() throws InterpreterException {
        bindScope_ = new NATObject();
        bindCtx_ = new NATContext(bindScope_, bindScope_);
    }

    /**
	 * Tests parameter binding for 0-arity methods
	 */
    public void testZeroArity() throws InterpreterException {
        fun1.base_applyInScope(NATTable.EMPTY, bindCtx_);
        try {
            fun1.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO }), bindCtx_);
            fail("Expected XArityMismatch exception");
        } catch (XArityMismatch e) {
        }
    }

    /**
	 * Tests parameter binding for methods with mandatory arguments only.
	 */
    public void testMandatory() throws InterpreterException {
        fun2.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO, NATNumber.ONE }), bindCtx_);
        ensureBound(at_a, NATNumber.ZERO);
        ensureBound(at_b, NATNumber.ONE);
    }

    /**
	 * Tests for too few mandatory arguments
	 */
    public void testTooFewMandatory() throws InterpreterException {
        try {
            fun2.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO }), bindCtx_);
            fail("Expected XArityMismatch exception");
        } catch (XArityMismatch e) {
        }
    }

    /**
	 * Tests for too many mandatory arguments
	 */
    public void testTooManyMandatory() throws InterpreterException {
        try {
            fun2.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO, NATNumber.ONE, NATNumber.atValue(2) }), bindCtx_);
            fail("Expected XArityMismatch exception");
        } catch (XArityMismatch e) {
        }
    }

    /**
	 * Tests parameter binding for methods with mandatory arguments and optional arguments.
	 */
    public void testMandatoryAndOptional() throws InterpreterException {
        fun3.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO, NATNumber.ONE, NATNumber.atValue(2) }), bindCtx_);
        ensureBound(at_a, NATNumber.ZERO);
        ensureBound(at_b, NATNumber.ONE);
        ensureBound(at_x, NATNumber.atValue(2));
    }

    /**
	 * Tests parameter binding for methods with mandatory arguments and an
	 * optional argument that is not given.
	 */
    public void testMandatoryAndDefaultOptional() throws InterpreterException {
        fun3.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO, NATNumber.ONE }), bindCtx_);
        ensureBound(at_a, NATNumber.ZERO);
        ensureBound(at_b, NATNumber.ONE);
        ensureBound(at_x, NATNumber.ONE);
    }

    /**
	 * Tests whether parameter binding fails if given too many optional arguments.
	 */
    public void testMandatoryAndTooManyOptional() throws InterpreterException {
        try {
            fun3.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO, NATNumber.ONE, NATNumber.atValue(2), NATNumber.atValue(3) }), bindCtx_);
            fail("Expected XArityMismatch exception");
        } catch (XArityMismatch e) {
        }
    }

    /**
	 * Tests application with both mandatory, optional and rest arguments
	 */
    public void testMandOptAndRest() throws InterpreterException {
        fun4.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO, NATNumber.ONE, NATNumber.atValue(2), NATNumber.atValue(3) }), bindCtx_);
        ensureBound(at_a, NATNumber.ZERO);
        ensureBound(at_x, NATNumber.ONE);
        ensureBoundToTable(at_rest, new ATObject[] { NATNumber.atValue(2), NATNumber.atValue(3) });
    }

    /**
	 * Tests application with mandatory and optional arguments.
	 * The rest arguments should be []
	 */
    public void testMandOptAndNoRest() throws InterpreterException {
        fun4.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO, NATNumber.ONE }), bindCtx_);
        ensureBound(at_a, NATNumber.ZERO);
        ensureBound(at_x, NATNumber.ONE);
        ensureBound(at_rest, NATTable.EMPTY);
    }

    /**
	 * Tests application with mandatory arguments.
	 * The optional argument should be initialized to its default expression.
	 * Note that this default expression also tests for let*-like behaviour!
	 * The rest arguments should be []
	 */
    public void testMandNoOptAndNoRest() throws InterpreterException {
        fun4.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.atValue(3) }), bindCtx_);
        ensureBound(at_a, NATNumber.atValue(3));
        ensureBound(at_x, NATNumber.atValue(3));
        ensureBound(at_rest, NATTable.EMPTY);
    }

    /**
	 * Tests application with only optional arguments, all of which are given at call time.
	 */
    public void testOnlyOptionalArguments() throws InterpreterException {
        fun5.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO, NATNumber.ONE }), bindCtx_);
        ensureBound(at_x, NATNumber.ZERO);
        ensureBound(at_y, NATNumber.ONE);
    }

    /**
	 * Tests application with only optional arguments, only one of which is given at call time.
	 */
    public void testOnlyOptArgsWithOneFilledIn() throws InterpreterException {
        fun5.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO }), bindCtx_);
        ensureBound(at_x, NATNumber.ZERO);
        ensureBound(at_y, NATNumber.ZERO);
    }

    /**
	 * Tests application with only optional arguments, none of which are given at call time.
	 */
    public void testOnlyOptArgsWithNoneFilledIn() throws InterpreterException {
        fun5.base_applyInScope(NATTable.EMPTY, bindCtx_);
        ensureBound(at_x, bindScope_);
        ensureBound(at_y, NATNumber.ZERO);
    }

    /**
	 * Tests application with one mandatory argument and zero rest arguments.
	 */
    public void testOneMandatoryAndNoRestArgs() throws InterpreterException {
        fun6.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO }), bindCtx_);
        ensureBound(at_a, NATNumber.ZERO);
        ensureBound(at_rest, NATTable.EMPTY);
    }

    /**
	 * Tests application with one mandatory argument and two rest arguments.
	 */
    public void testOneMandatoryAndTwoRestArgs() throws InterpreterException {
        fun6.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO, NATNumber.ONE, NATNumber.atValue(2) }), bindCtx_);
        ensureBound(at_a, NATNumber.ZERO);
        ensureBoundToTable(at_rest, new ATObject[] { NATNumber.ONE, NATNumber.atValue(2) });
    }

    /**
	 * Tests only rest arguments, with none given.
	 */
    public void testZeroRestArgs() throws InterpreterException {
        fun7.base_applyInScope(NATTable.EMPTY, bindCtx_);
        ensureBound(at_rest, NATTable.EMPTY);
    }

    /**
	 * Tests only rest arguments, with one given.
	 */
    public void testOneRestArg() throws InterpreterException {
        fun7.base_applyInScope(NATTable.atValue(new ATObject[] { NATNumber.ZERO }), bindCtx_);
        ensureBoundToTable(at_rest, new ATObject[] { NATNumber.ZERO });
    }

    /**
	 * Tests whether mandatory arguments specified after optional arguments
	 * results in a proper XIllegalParameter exception.
	 */
    public void testIllegalMandatoryAfterOptional() throws InterpreterException {
        try {
            new NATMethod(AGSymbol.jAlloc("fun8"), NATTable.atValue(new ATObject[] { new AGAssignVariable(at_x, NATNumber.ZERO), at_a }), new AGBegin(NATTable.atValue(new ATObject[] { Evaluator.getNil() })), NATTable.EMPTY);
            fail("Expected XIllegalParameter exception");
        } catch (XIllegalParameter e) {
        }
    }

    /**
	 * Tests whether mandatory arguments specified after the rest parameter
	 * results in a proper XIllegalParameter exception.
	 */
    public void testIllegalMandatoryAfterRest() throws InterpreterException {
        try {
            new NATMethod(AGSymbol.jAlloc("fun9"), NATTable.atValue(new ATObject[] { new AGSplice(at_rest), at_a }), new AGBegin(NATTable.atValue(new ATObject[] { Evaluator.getNil() })), NATTable.EMPTY);
            fail("Expected XIllegalParameter exception");
        } catch (XIllegalParameter e) {
        }
    }

    /**
	 * Tests whether optional arguments specified after the rest parameter
	 * results in a proper XIllegalParameter exception.
	 */
    public void testIllegalOptionalAfterRest() throws InterpreterException {
        try {
            new NATMethod(AGSymbol.jAlloc("fun10"), NATTable.atValue(new ATObject[] { new AGSplice(at_rest), new AGAssignVariable(at_x, NATNumber.ZERO) }), new AGBegin(NATTable.atValue(new ATObject[] { Evaluator.getNil() })), NATTable.EMPTY);
            fail("Expected XIllegalParameter exception");
        } catch (XIllegalParameter e) {
        }
    }

    /**
	 * Tests whether optional and rest parameters also work using multiple-definition
	 */
    public void testMultiAssignmentWithOptionalAndRestParameters() throws InterpreterException {
        AGMultiDefinition multiDef = new AGMultiDefinition(fun4.base_parameters(), NATTable.atValue(new ATObject[] { NATNumber.ZERO, NATNumber.ONE, NATNumber.atValue(2) }));
        multiDef.meta_eval(bindCtx_);
        ensureBound(at_a, NATNumber.ZERO);
        ensureBound(at_x, NATNumber.ONE);
        ensureBoundToTable(at_rest, new ATObject[] { NATNumber.atValue(2) });
    }
}
