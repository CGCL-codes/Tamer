package edu.vub.at.objects.natives;

import edu.vub.at.exceptions.InterpreterException;
import edu.vub.at.exceptions.XArityMismatch;
import edu.vub.at.exceptions.XIllegalArgument;
import edu.vub.at.objects.ATAbstractGrammar;
import edu.vub.at.objects.ATBoolean;
import edu.vub.at.objects.ATContext;
import edu.vub.at.objects.ATObject;
import edu.vub.at.objects.ATTable;
import edu.vub.at.objects.ATTypeTag;
import edu.vub.at.objects.grammar.ATApplication;
import edu.vub.at.objects.grammar.ATAssignVariable;
import edu.vub.at.objects.grammar.ATClosureLiteral;
import edu.vub.at.objects.grammar.ATSymbol;
import edu.vub.at.objects.natives.grammar.NATAbstractGrammar;

/**
 * @author smostinc
 *
 */
public class AGCase extends NATAbstractGrammar {

    private ATSymbol bindingForm_ = null;

    private ATSymbol typeName_ = null;

    private ATTable arguments_ = NATTable.EMPTY;

    private ATAbstractGrammar pattern_;

    private ATClosureLiteral consequence_;

    public AGCase(ATAbstractGrammar pattern, ATClosureLiteral consequence) throws InterpreterException {
        if (pattern instanceof ATAssignVariable) {
            ATAssignVariable assignment = (ATAssignVariable) pattern;
            bindingForm_ = assignment.base_name();
            pattern = assignment.base_valueExpression();
        }
        if (pattern instanceof ATSymbol) {
            typeName_ = pattern.asSymbol();
        } else if (pattern instanceof ATApplication) {
            ATApplication application = (ATApplication) pattern;
            typeName_ = application.base_function().asSymbol();
            arguments_ = application.base_arguments();
        }
        pattern_ = pattern;
        consequence_ = consequence;
    }

    public NATText meta_print() throws InterpreterException {
        return NATText.atValue("case " + (bindingForm_ == null ? "" : (bindingForm_.toString() + " := ")) + pattern_.meta_print().javaValue + " => " + consequence_.meta_print().javaValue);
    }

    public ATBoolean base_isApplicable(ATContext theContext, ATObject theObjectToBeMatched) throws InterpreterException {
        ATTypeTag theTypeTag = theContext.base_lexicalScope().impl_callField(typeName_).asTypeTag();
        return theObjectToBeMatched.meta_isTaggedAs(theTypeTag);
    }

    public ATObject base_attemptToExecute(ATObject theObjectToBeMatched, ATContext theContext) throws InterpreterException {
        ATTypeTag theTypeTag = theContext.base_lexicalScope().impl_callField(typeName_).asTypeTag();
        ATTable theArguments = NATTable.EMPTY;
        NATCallframe bindings = new NATCallframe(theContext.base_lexicalScope());
        if (bindingForm_ != null) bindings.meta_defineField(bindingForm_, theObjectToBeMatched);
        if (theTypeTag instanceof NATPatternType) {
            NATPatternType thePattern = (NATPatternType) theTypeTag;
            theArguments = thePattern.base_unapply(theObjectToBeMatched).asTable();
        }
        ATObject[] parameters = arguments_.asNativeTable().elements_;
        ATObject[] arguments = theArguments.asNativeTable().elements_;
        if (parameters.length != arguments.length) throw new XArityMismatch(theTypeTag.base_typeName().toString(), parameters.length, arguments.length);
        for (int i = 0; i < parameters.length; i++) {
            ATObject parameter = parameters[i];
            ATObject argument = arguments[i];
            if (parameter instanceof ATSymbol) {
                ATSymbol theVariableName = parameter.asSymbol();
                if (!bindings.hasLocalField(theVariableName)) {
                    bindings.meta_defineField((ATSymbol) parameter, argument);
                    continue;
                } else {
                    parameter = bindings.getLocalField(theVariableName);
                }
            }
            if (!parameter.base__opeql__opeql_(argument).asNativeBoolean().javaValue) {
                throw new XIllegalArgument("No valid match since " + parameter + " != " + argument);
            }
        }
        NATContext theExtendedContext = new NATContext(bindings, theContext.base_receiver());
        return consequence_.meta_eval(theExtendedContext).asClosure().base_apply(NATTable.EMPTY);
    }
}
