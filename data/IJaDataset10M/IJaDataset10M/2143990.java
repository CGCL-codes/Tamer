package edu.vub.at.objects.coercion;

import edu.vub.at.objects.natives.NATStripe;

/**
 * This class serves to hold references to the native stripes with which native
 * AmbientTalk objects are tagged.
 *
 * @author tvcutsem
 */
public final class NativeStripes {

    public static final NATStripe _ISOLATE_ = NATStripe.atValue("Isolate");

    public static final NATStripe _META_ = NATStripe.atValue("Meta");

    public static final NATStripe _BOOLEAN_ = NATStripe.atValue("Boolean");

    public static final NATStripe _CLOSURE_ = NATStripe.atValue("Closure");

    public static final NATStripe _CONTEXT_ = NATStripe.atValue("Context");

    public static final NATStripe _FIELD_ = NATStripe.atValue("Field");

    public static final NATStripe _HANDLER_ = NATStripe.atValue("Handler");

    public static final NATStripe _METHOD_ = NATStripe.atValue("Method");

    public static final NATStripe _MESSAGE_ = NATStripe.atValue("Message");

    public static final NATStripe _METHODINV_ = NATStripe.atValue("MethodInvocation", _MESSAGE_);

    public static final NATStripe _DELEGATION_ = NATStripe.atValue("Delegation", _MESSAGE_);

    public static final NATStripe _ASYNCMSG_ = NATStripe.atValue("AsyncMessage", _MESSAGE_);

    public static final NATStripe _MIRROR_ = NATStripe.atValue("Mirror");

    public static final NATStripe _ACTORMIRROR_ = NATStripe.atValue("ActorMirror");

    public static final NATStripe _STRIPE_ = NATStripe.atValue("Stripe");

    public static final NATStripe _ABSTRACTGRAMMAR_ = NATStripe.atValue("AbstractGrammar");

    public static final NATStripe _STATEMENT_ = NATStripe.atValue("Statement", _ABSTRACTGRAMMAR_);

    public static final NATStripe _EXPRESSION_ = NATStripe.atValue("Expression", _STATEMENT_);

    public static final NATStripe _TABLE_ = NATStripe.atValue("Table", _EXPRESSION_);

    public static final NATStripe _TEXT_ = NATStripe.atValue("Text", _EXPRESSION_);

    public static final NATStripe _NUMERIC_ = NATStripe.atValue("Numeric", _EXPRESSION_);

    public static final NATStripe _NUMBER_ = NATStripe.atValue("Number", _EXPRESSION_);

    public static final NATStripe _FRACTION_ = NATStripe.atValue("Fraction", _EXPRESSION_);

    public static final NATStripe _SYMBOL_ = NATStripe.atValue("Symbol", _EXPRESSION_);

    public static final NATStripe _BEGIN_ = NATStripe.atValue("Begin", _ABSTRACTGRAMMAR_);

    public static final NATStripe _SPLICE_ = NATStripe.atValue("Splice", _EXPRESSION_);

    public static final NATStripe _UQSPLICE_ = NATStripe.atValue("UnquoteSplice", _EXPRESSION_);

    public static final NATStripe _MSGCREATION_ = NATStripe.atValue("MessageCreation", _EXPRESSION_);

    public static final NATStripe _DEFINITION_ = NATStripe.atValue("Definition", _STATEMENT_);

    public static final NATStripe _EXCEPTION_ = NATStripe.atValue("Exception");

    public static final NATStripe _ARITYMISMATCH_ = NATStripe.atValue("ArityMismatch", _EXCEPTION_);

    public static final NATStripe _CLASSNOTFOUND_ = NATStripe.atValue("ClassNotFound", _EXCEPTION_);

    public static final NATStripe _DUPLICATESLOT_ = NATStripe.atValue("DuplicateSlot", _EXCEPTION_);

    public static final NATStripe _ILLAPP_ = NATStripe.atValue("IllegalApplication", _EXCEPTION_);

    public static final NATStripe _ILLARG_ = NATStripe.atValue("IllegalArgument", _EXCEPTION_);

    public static final NATStripe _ILLIDX_ = NATStripe.atValue("IllegalIndex", _EXCEPTION_);

    public static final NATStripe _ILLOP_ = NATStripe.atValue("IllegalOperation", _EXCEPTION_);

    public static final NATStripe _ILLPARAM_ = NATStripe.atValue("IllegalParameter", _EXCEPTION_);

    public static final NATStripe _ILLQUOTE_ = NATStripe.atValue("IllegalQuote", _EXCEPTION_);

    public static final NATStripe _ILLSPLICE_ = NATStripe.atValue("IllegalSplice", _EXCEPTION_);

    public static final NATStripe _ILLUQUOTE_ = NATStripe.atValue("IllegalUnquote", _EXCEPTION_);

    public static final NATStripe _IDXOUTOFBOUNDS_ = NATStripe.atValue("IndexOutOfBounds", _EXCEPTION_);

    public static final NATStripe _IOPROBLEM_ = NATStripe.atValue("IOProblem", _EXCEPTION_);

    public static final NATStripe _NOTINSTANTIATABLE_ = NATStripe.atValue("NotInstantiatable", _EXCEPTION_);

    public static final NATStripe _PARSEERROR_ = NATStripe.atValue("ParseError", _EXCEPTION_);

    public static final NATStripe _REFLECTIONFAILURE_ = NATStripe.atValue("ReflectionFailure", _EXCEPTION_);

    public static final NATStripe _SELECTORNOTFOUND_ = NATStripe.atValue("SelectorNotFound", _EXCEPTION_);

    public static final NATStripe _SYMBIOSISFAILURE_ = NATStripe.atValue("SymbiosisFailure", _EXCEPTION_);

    public static final NATStripe _TYPEMISMATCH_ = NATStripe.atValue("TypeMismatch", _EXCEPTION_);

    public static final NATStripe _UNASSIGNABLEFIELD_ = NATStripe.atValue("UnassignableField", _EXCEPTION_);

    public static final NATStripe _UNDEFINEDFIELD_ = NATStripe.atValue("UndefinedField", _EXCEPTION_);

    public static final NATStripe _CUSTOMEXCEPTION_ = NATStripe.atValue("CustomException", _EXCEPTION_);

    public static final NATStripe _JAVAEXCEPTION_ = NATStripe.atValue("JavaException", _EXCEPTION_);

    public static final NATStripe _IMPORTCONFLICT_ = NATStripe.atValue("ImportConflict", _EXCEPTION_);

    public static final NATStripe _OBJECTOFFLINE_ = NATStripe.atValue("ObjectOffline", _EXCEPTION_);
}
