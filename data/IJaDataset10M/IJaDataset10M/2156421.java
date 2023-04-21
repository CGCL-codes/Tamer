package edu.vub.at.signals.natives;

import edu.vub.at.eval.Evaluator;
import edu.vub.at.exceptions.InterpreterException;
import edu.vub.at.objects.ATClosure;
import edu.vub.at.objects.ATMessage;
import edu.vub.at.objects.ATMethodInvocation;
import edu.vub.at.objects.ATMutable;
import edu.vub.at.objects.ATObject;
import edu.vub.at.objects.ATTable;
import edu.vub.at.objects.coercion.NativeTypeTags;
import edu.vub.at.objects.grammar.ATSymbol;
import edu.vub.at.objects.natives.NATClosure;
import edu.vub.at.objects.natives.NATContext;
import edu.vub.at.objects.natives.NATMethod;
import edu.vub.at.objects.natives.NATMethodInvocation;
import edu.vub.at.objects.natives.NATNumber;
import edu.vub.at.objects.natives.NATTable;
import edu.vub.at.objects.natives.NATText;
import edu.vub.at.objects.natives.OBJLexicalRoot;
import edu.vub.at.objects.natives.grammar.AGBegin;
import edu.vub.at.objects.natives.grammar.AGSymbol;
import edu.vub.at.signals.ATBehavior;
import edu.vub.at.signals.ATEventSource;
import edu.vub.at.signals.ATSignal;
import edu.vub.at.util.logging.Logging;

/**
 * @author smostinc
 *
 */
public class NATBehavior extends NATSignal implements ATBehavior {

    private static class NATValueHandler extends NATSignalHandler {

        private NATBehavior itsBehavior;

        public NATValueHandler(ATObject theConsumer) {
            super(theConsumer);
        }

        public void initialize(NATBehavior theBehavior) {
            itsBehavior = theBehavior;
        }

        public void impl_handleResult(ATObject theResult) throws InterpreterException {
            if (theResult != Evaluator.getNil()) itsBehavior.setValue(theResult);
        }
    }

    private class NATUpdaterProxy extends NATEventSource {

        public NATUpdaterProxy(int theDependencyHeight) {
            super(theDependencyHeight);
        }

        protected void inner_performUpdate(ATSignal theParent, ATMessage theUpdate) {
            try {
                setValue(getFirstArgument(theUpdate));
            } catch (InterpreterException e) {
                e.printStackTrace();
            }
        }

        private ATObject getFirstArgument(ATMessage theMessage) throws InterpreterException {
            if (theMessage.base_selector() == Evaluator._APPLY_) {
                return theMessage.base_arguments().base_at(NATNumber.ONE).asTable().base_at(NATNumber.ONE);
            } else {
                return theMessage.base_arguments().base_at(NATNumber.ONE);
            }
        }
    }

    private static ATObject _DEFAULT_CONSUMER_;

    static {
        try {
            _DEFAULT_CONSUMER_ = new NATClosure(new NATMethod(AGSymbol.jAlloc("identity"), NATTable.of(AGSymbol.jAlloc("value")), new AGBegin(NATTable.of(AGSymbol.jAlloc("value"))), NATTable.EMPTY), new NATContext(OBJLexicalRoot._INSTANCE_, Evaluator.getNil()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected final ATEventSource itsEvents;

    protected ATObject itsCachedValue;

    protected final ATObject itsUnwrappedConsumer;

    public NATBehavior(ATEventSource events, ATObject initialValue) throws InterpreterException {
        this(events, initialValue, _DEFAULT_CONSUMER_);
    }

    public NATBehavior(ATEventSource events, ATObject initialValue, ATObject consumer) throws InterpreterException {
        super(events.dependencyHeight() + 1, new NATValueHandler(consumer));
        ((NATValueHandler) itsConsumer).initialize(this);
        itsEvents = events;
        itsCachedValue = initialValue;
        itsUnwrappedConsumer = consumer;
        itsEvents.meta_addDependent(this);
    }

    public ATObject meta_consumer() {
        return itsUnwrappedConsumer;
    }

    public ATEventSource meta_events() {
        return itsEvents;
    }

    public ATObject meta_value() {
        return itsCachedValue;
    }

    private ATEventSource parallelUpdater = null;

    protected final void setValue(ATObject newValue) {
        if (newValue instanceof ATBehavior) {
            if (parallelUpdater != null) parallelUpdater.unhook();
            ATBehavior newBehavior = (ATBehavior) newValue;
            updateDependencyHeight(newBehavior.dependencyHeight() + 1);
            parallelUpdater = new NATUpdaterProxy(dependencyHeight());
            Logging.ReactiveProgramming_LOG.debug("Creating a parallel updater: " + parallelUpdater + " for " + this);
            try {
                newBehavior.meta_addDependent(parallelUpdater);
            } catch (InterpreterException e) {
                e.printStackTrace();
            }
            newValue = newBehavior.meta_value();
        }
        if ((itsCachedValue instanceof ATMutable) || itsCachedValue != newValue) {
            itsCachedValue = newValue;
            try {
                propagateNewValue(itsCachedValue);
            } catch (InterpreterException e) {
                e.printStackTrace();
            }
        }
    }

    /** 
	 * TODO Parameter passing of behaviors is currently not supported, the current value of the
	 * behavior is passed instead.
	 */
    public ATObject meta_pass() throws InterpreterException {
        return itsCachedValue.meta_pass();
    }

    /**
	 * TODO Parameter passing of behaviors is currently not supported, the current value of the
	 * behavior is passed instead.
	 */
    public ATObject meta_resolve() throws InterpreterException {
        throw new RuntimeException("Cannot meaningfully resolve a behaviour.");
    }

    public NATText meta_print() throws InterpreterException {
        return NATText.atValue("<behavior:" + itsCachedValue.meta_print().javaValue + ">");
    }

    public ATTable meta_typeTags() throws InterpreterException {
        return NATTable.of(NativeTypeTags._BEHAVIOR_);
    }

    public boolean isBehavior() throws InterpreterException {
        return true;
    }

    public ATBehavior asBehavior() throws InterpreterException {
        return this;
    }

    /**
	 * Invoking a non-lifted method on a behavior creates a new behavior whose initial value 
	 * is defined by invoking the method on the current value of the behavior.    Subsequent
	 * updates to the behavior will trigger recomputation of the method.
	 * <p>
	 * Invoking lifted methods on the other hand, does not create a dependent computation at
	 * all. The method is simply invoked on the cached value,   though the receiver is still
	 * correctly passed as a behavior.
	 */
    public ATObject meta_invoke(ATObject receiver, ATMethodInvocation invocation) throws InterpreterException {
        ATClosure theClosure = itsCachedValue.meta_select(receiver, invocation.base_selector());
        if (!theClosure.base_method().base_annotations().base_contains(NativeTypeTags._LIFTED_).asNativeBoolean().javaValue) {
            return NATLiftedBehavior.fromClosure(theClosure, invocation.base_arguments().asNativeTable().elements_);
        } else {
            return theClosure.base_apply(invocation.base_arguments());
        }
    }

    public ATObject meta_invokeField(ATObject receiver, ATSymbol selector) throws InterpreterException {
        return meta_invoke(receiver, new NATMethodInvocation(selector, NATTable.EMPTY, NATTable.EMPTY));
    }

    /**
     * NATBehavior instances are to be considered AmbientTalk objects such that they can be coerced
     * into Java interface types.
     */
    public boolean isAmbientTalkObject() {
        return true;
    }
}
