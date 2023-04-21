package edu.vub.at.actors.natives;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import edu.vub.at.actors.ATAsyncMessage;
import edu.vub.at.actors.ATFarReference;
import edu.vub.at.actors.ATLetter;
import edu.vub.at.actors.id.ATObjectID;
import edu.vub.at.actors.natives.NATActorMirror.NATLetter;
import edu.vub.at.actors.net.ConnectionListener;
import edu.vub.at.eval.Evaluator;
import edu.vub.at.exceptions.InterpreterException;
import edu.vub.at.exceptions.XArityMismatch;
import edu.vub.at.exceptions.XIllegalOperation;
import edu.vub.at.exceptions.XObjectOffline;
import edu.vub.at.exceptions.XSelectorNotFound;
import edu.vub.at.exceptions.XTypeMismatch;
import edu.vub.at.objects.ATBoolean;
import edu.vub.at.objects.ATClosure;
import edu.vub.at.objects.ATContext;
import edu.vub.at.objects.ATField;
import edu.vub.at.objects.ATMethod;
import edu.vub.at.objects.ATNil;
import edu.vub.at.objects.ATObject;
import edu.vub.at.objects.ATTable;
import edu.vub.at.objects.ATTypeTag;
import edu.vub.at.objects.coercion.NativeTypeTags;
import edu.vub.at.objects.grammar.ATSymbol;
import edu.vub.at.objects.mirrors.NativeClosure;
import edu.vub.at.objects.mirrors.PrimitiveMethod;
import edu.vub.at.objects.natives.NATBoolean;
import edu.vub.at.objects.natives.NATByCopy;
import edu.vub.at.objects.natives.NATNil;
import edu.vub.at.objects.natives.NATObject;
import edu.vub.at.objects.natives.NATTable;
import edu.vub.at.objects.natives.NATText;
import edu.vub.at.objects.natives.grammar.AGSymbol;
import edu.vub.at.util.logging.Logging;

/**
 * 
 * NATFarReference is the root of the native classes that represent native far references.
 * The AmbientTalk/2 implementation distinguishes between two kinds of far references:
 * local and remote far references. The former denote far references to objects hosted by
 * actors on the same virtual machine. The latter ones denote far references to remote objects
 * that are hosted on a separate virtual (and usually even physical) machine.
 * 
 * This abstract superclass encapsulates all of the code that these two kinds of far references
 * have in common. The variabilities are delegated to the subclasses. Subclasses should implement
 * an abstract method (transmit) which is invoked by this class when the far reference receives
 * a message to be forwarded to the remote principal.
 * 
 * Note that far references are pass by copy and resolve to either a near or a new
 * actor-local far reference.
 * 
 * Far references encapsulate the same types as the remote object they represent.
 * As such it becomes possible to perform a type test on a far reference as if it
 * was performed on the local object itself!
 * 
 * @author tvcutsem
 * @author smostinc
 */
public abstract class NATFarReference extends NATByCopy implements ATFarReference, ConnectionListener {

    private final ATObjectID objectId_;

    private final ATTypeTag[] types_;

    /** the state of connectivity of the far reference
	 * note that this variable is not transient, it will be passed when the ref is
	 * parameter-passed such that the passed ref is initialized in the correct state
	 * Note also that access to this variable is synchronized since it may be modified 
	 * by ELVirtualMachine and  a thread of FarReferencesThreadPool for remote far references.
	 */
    protected boolean connected_;

    private transient Vector disconnectedListeners_;

    private transient Vector reconnectedListeners_;

    private transient Vector takenOfflineListeners_;

    private final transient ELActor owner_;

    /**
     *  'outbox' stores all messages sent to the receiver object hosted by another actor.
	 *  Each far reference represents an outbox of an actor. 
	 *  It contains objects that implement the {@link ATLetter} interface.
	 *  
	 *  Note that access to the outbox is synchronized because it may be modified by:
	 *  -the owner ELActor and ELVirtualMachine in case of local far references.
	 *  -the owner ELActor and a thread of FarReferencesThreadPool in case of remote far references.
     */
    protected transient LinkedList outbox_ = new LinkedList();

    protected NATFarReference(ATObjectID objectId, ATTypeTag[] types, ELActor owner, boolean isConnected) {
        int size = types.length;
        types_ = new ATTypeTag[size + 1];
        if (size > 0) System.arraycopy(types, 0, types_, 0, size);
        types_[size] = NativeTypeTags._FARREF_;
        objectId_ = objectId;
        connected_ = isConnected;
        owner_ = owner;
        owner_.getHost().connectionManager_.addConnectionListener(objectId_.getVirtualMachineId(), this);
    }

    public ATObjectID impl_getObjectId() {
        return objectId_;
    }

    public ATTypeTag[] getTypes() {
        return types_;
    }

    public int hashCode() {
        return objectId_.hashCode();
    }

    public boolean isNativeFarReference() {
        return true;
    }

    public NATFarReference asNativeFarReference() throws XTypeMismatch {
        return this;
    }

    public boolean isFarReference() {
        return true;
    }

    public ATFarReference asFarReference() throws XTypeMismatch {
        return this;
    }

    public synchronized void connected() {
        if (!connected_) {
            connected_ = true;
            notifyStateToSendLoop(true);
            notifyConnected();
        }
    }

    public synchronized void disconnected() {
        if (connected_) {
            connected_ = false;
            notifyStateToSendLoop(false);
            notifyDisconnected();
        }
    }

    public synchronized void takenOffline() {
        connected_ = false;
        notifyStateToSendLoop(false);
        notifyTakenOffline();
    }

    protected abstract void notifyStateToSendLoop(boolean state);

    public synchronized void addDisconnectionListener(ATObject listener) {
        if (disconnectedListeners_ == null) {
            disconnectedListeners_ = new Vector(1);
        }
        disconnectedListeners_.add(listener);
        if (!connected_) {
            triggerListener(listener, "when:disconnected:");
        }
    }

    public synchronized void addReconnectionListener(ATObject listener) {
        if (reconnectedListeners_ == null) {
            reconnectedListeners_ = new Vector(1);
        }
        reconnectedListeners_.add(listener);
    }

    public synchronized void removeDisconnectionListener(ATObject listener) {
        if (disconnectedListeners_ != null) {
            disconnectedListeners_.remove(listener);
        }
    }

    public synchronized void removeReconnectionListener(ATObject listener) {
        if (reconnectedListeners_ != null) {
            reconnectedListeners_.remove(listener);
        }
    }

    public synchronized void addTakenOfflineListener(ATObject listener) {
        if (takenOfflineListeners_ == null) {
            takenOfflineListeners_ = new Vector(1);
        }
        takenOfflineListeners_.add(listener);
    }

    public synchronized void removeTakenOfflineListener(ATObject listener) {
        if (takenOfflineListeners_ != null) {
            takenOfflineListeners_.remove(listener);
        }
    }

    public synchronized void notifyConnected() {
        if (reconnectedListeners_ != null) {
            Logging.RemoteRef_LOG.debug("notifyConnected for " + this.toString());
            for (Iterator reconnectedIter = reconnectedListeners_.iterator(); reconnectedIter.hasNext(); ) {
                triggerListener((ATObject) reconnectedIter.next(), "when:reconnected:");
            }
        }
    }

    public synchronized void notifyDisconnected() {
        if (disconnectedListeners_ != null) {
            Logging.RemoteRef_LOG.debug("notifyDisconnected for " + this.toString());
            for (Iterator disconnectedIter = disconnectedListeners_.iterator(); disconnectedIter.hasNext(); ) {
                triggerListener((ATObject) disconnectedIter.next(), "when:disconnected:");
            }
        }
    }

    /**
	 * Taking offline an object results in a "logical" disconnection of the far remote reference.
	 * This means that the ref becomes expired but also disconnected.
	 * Thus, all disconnectedlisteners and takenOfflineListeners are notified.
	 */
    public synchronized void notifyTakenOffline() {
        if (takenOfflineListeners_ != null) {
            for (Iterator expiredIter = takenOfflineListeners_.iterator(); expiredIter.hasNext(); ) {
                triggerListener((ATObject) expiredIter.next(), "when:takenOffline:");
            }
        }
        notifyDisconnected();
    }

    /**
	 * After deserialization, ensure that only one unique remote reference exists for
	 * my target.
	 */
    public ATObject meta_resolve() throws InterpreterException, XObjectOffline {
        return ELActor.currentActor().resolve(objectId_, types_, connected_);
    }

    public ATObject meta_receive(ATAsyncMessage message) throws InterpreterException {
        NATOutboxLetter letter = new NATOutboxLetter(outbox_, this, message);
        this.transmit(letter);
        return Evaluator.getNil();
    }

    protected abstract void transmit(ATLetter letter) throws InterpreterException;

    /**
	 * The only operation that is allowed to be synchronously invoked on far references is '=='
	 * @throws XIllegalOperation Cannot synchronously invoke a method on a far reference
	 */
    public ATObject impl_invoke(ATObject delegate, ATSymbol atSelector, ATTable arguments) throws InterpreterException {
        if (atSelector.equals(NATNil._EQL_NAME_)) {
            return super.impl_invoke(delegate, atSelector, arguments);
        }
        throw new XIllegalOperation("Cannot invoke " + atSelector + " on far reference " + this);
    }

    public ATObject meta_invokeField(ATObject receiver, ATSymbol selector) throws InterpreterException {
        if (selector.equals(NATNil._EQL_NAME_)) {
            return super.meta_invokeField(receiver, selector);
        }
        throw new XIllegalOperation("Cannot synchronously access " + selector + " on far reference " + this);
    }

    /**
	 * @return true if and only if the far object is queried for responses to basic operations such as ==
	 */
    public ATBoolean meta_respondsTo(ATSymbol atSelector) throws InterpreterException {
        return super.meta_respondsTo(atSelector);
    }

    /**
	 * @throws XSelectorNotFound to ensure proper semantics should the interpreter be
	 * extended such that it allows extending a far reference in the future.
	 */
    public ATClosure meta_doesNotUnderstand(ATSymbol selector) throws InterpreterException {
        return super.meta_doesNotUnderstand(selector);
    }

    /**
	 * References to objects hosted by another actor are forced to be unique. Therefore
	 * cloning them throws an XIllegalOperation to avoid inconsistencies by performing
	 * state updates (through sent messages) after a clone operation. 
	 * 
	 * TODO(discuss) clone: farObject may create a clone on the other actor.
	 */
    public ATObject meta_clone() throws InterpreterException {
        throw new XIllegalOperation("Cannot clone far reference " + this);
    }

    /**
	 * Cannot create a new instance using a farObject, this should be done either by 
	 * sending rather than invoking new(args) such that the correct method is triggered
	 * or by invoking newInstance on a farMirror, which will send the call as well. 
	 */
    public ATObject meta_newInstance(ATTable initargs) throws InterpreterException {
        throw new XIllegalOperation("Cannot create new instance of far reference " + this);
    }

    /**
	 * @throws XIllegalOperation - cannot select in objects hosted by another actor.
	 */
    public ATClosure meta_select(ATObject receiver, ATSymbol atSelector) throws InterpreterException {
        if (atSelector.equals(NATNil._EQL_NAME_)) {
            return super.meta_select(receiver, atSelector);
        }
        throw new XIllegalOperation("Cannot select " + atSelector + " from far reference " + this);
    }

    /**
	 * @throws XIllegalOperation - cannot lookup in objects hosted by another actor.
	 */
    public ATObject impl_call(ATSymbol selector, ATTable arguments) throws InterpreterException {
        throw new XIllegalOperation("Cannot lookup " + selector + " from far reference " + this);
    }

    /**
	 * @throws XIllegalOperation - cannot define in objects hosted by another actor.
	 */
    public ATNil meta_defineField(ATSymbol name, ATObject value) throws InterpreterException {
        throw new XIllegalOperation("Cannot define field " + name + " in far reference " + this);
    }

    /**
	 * @throws XIllegalOperation - cannot assign in objects hosted by another actor.
	 */
    public ATNil meta_assignField(ATObject receiver, ATSymbol name, ATObject value) throws InterpreterException {
        throw new XIllegalOperation("Cannot assign field " + name + " in far reference " + this);
    }

    /**
	 * @throws XIllegalOperation - cannot assign in objects hosted by another actor.
	 */
    public ATNil meta_assignVariable(ATSymbol name, ATObject value) throws InterpreterException {
        throw new XIllegalOperation("Cannot assign variable " + name + " in far reference " + this);
    }

    /**
     * @return false unless this == original
     */
    public ATBoolean meta_isCloneOf(ATObject original) throws InterpreterException {
        return NATBoolean.atValue(this == original);
    }

    /**
     * @return false unless this == original
     */
    public ATBoolean meta_isRelatedTo(ATObject object) throws InterpreterException {
        return this.meta_isCloneOf(object);
    }

    /**
	 * @throws XIllegalOperation - cannot add fields to an object in another actor.
	 */
    public ATNil meta_addField(ATField field) throws InterpreterException {
        return super.meta_addField(field);
    }

    /**
	 * @throws XIllegalOperation - cannot add methods to an object in another actor.
	 */
    public ATNil meta_addMethod(ATMethod method) throws InterpreterException {
        return super.meta_addMethod(method);
    }

    /**
	 * @throws XSelectorNotFound - as the far object has no fields of its own
	 */
    public ATField meta_grabField(ATSymbol fieldName) throws InterpreterException {
        return super.meta_grabField(fieldName);
    }

    /**
	 * @return a method if and only if the requested selector is a default operator such as == 
	 * @throws XSelectorNotFound otherwise
	 */
    public ATMethod meta_grabMethod(ATSymbol methodName) throws InterpreterException {
        return super.meta_grabMethod(methodName);
    }

    /**
	 * @return an empty table
	 */
    public ATTable meta_listFields() throws InterpreterException {
        return super.meta_listFields();
    }

    /**
	 * @return a table of default methods
	 */
    public ATTable meta_listMethods() throws InterpreterException {
        return super.meta_listMethods();
    }

    public NATText meta_print() throws InterpreterException {
        return NATText.atValue("<far ref:" + objectId_.getDescription() + ">");
    }

    /**
	 * The types of a far reference are the types of the remote object
	 * it points to, plus the FarReference type.
	 */
    public ATTable meta_typeTags() throws InterpreterException {
        return NATTable.atValue(types_);
    }

    /**
     * Two far references are equal if and only if they point to the same object Id.
     */
    public ATBoolean base__opeql__opeql_(ATObject other) throws InterpreterException {
        if (this == other) {
            return NATBoolean._TRUE_;
        } else if (other.isNativeFarReference()) {
            ATObjectID otherId = other.asNativeFarReference().impl_getObjectId();
            return NATBoolean.atValue(objectId_.equals(otherId));
        } else {
            return NATBoolean._FALSE_;
        }
    }

    /**
     * Performs listener&lt;-apply([ [] ])
     * 
     * @param type the kind of listener, used for logging/debugging purposes only
     */
    private void triggerListener(ATObject listener, String type) {
        Evaluator.trigger(owner_, listener, NATTable.EMPTY, type);
    }

    public static class NATDisconnectionSubscription extends NATObject {

        private static final AGSymbol _REFERENCE_ = AGSymbol.jAlloc("reference");

        private static final AGSymbol _HANDLER_ = AGSymbol.jAlloc("handler");

        private static final AGSymbol _CANCEL_ = AGSymbol.jAlloc("cancel");

        public NATDisconnectionSubscription(final NATFarReference reference, ATClosure handler) throws InterpreterException {
            this.meta_defineField(_REFERENCE_, reference);
            this.meta_defineField(_HANDLER_, handler);
            this.meta_addMethod(new PrimitiveMethod(_CANCEL_, NATTable.EMPTY) {

                public ATObject base_apply(ATTable arguments, ATContext ctx) throws InterpreterException {
                    int arity = arguments.base_length().asNativeNumber().javaValue;
                    if (arity != 0) {
                        throw new XArityMismatch("cancel", 0, arity);
                    }
                    NATDisconnectionSubscription scope = NATDisconnectionSubscription.this;
                    NATFarReference reference = scope.impl_invokeAccessor(scope, _REFERENCE_, NATTable.EMPTY).asNativeFarReference();
                    if (reference instanceof NATFarReference) {
                        NATFarReference remote = (NATFarReference) reference;
                        ATObject handler = scope.impl_callField(_HANDLER_);
                        remote.removeDisconnectionListener(handler.asClosure());
                    }
                    return Evaluator.getNil();
                }
            });
        }

        public NATText meta_print() throws InterpreterException {
            return NATText.atValue("<disconnection subscription:" + this.impl_invokeAccessor(this, _REFERENCE_, NATTable.EMPTY) + ">");
        }
    }

    public static class NATReconnectionSubscription extends NATObject {

        private static final AGSymbol _REFERENCE_ = AGSymbol.jAlloc("reference");

        private static final AGSymbol _HANDLER_ = AGSymbol.jAlloc("handler");

        private static final AGSymbol _CANCEL_ = AGSymbol.jAlloc("cancel");

        public NATReconnectionSubscription(final NATFarReference reference, ATClosure handler) throws InterpreterException {
            this.meta_defineField(_REFERENCE_, reference);
            this.meta_defineField(_HANDLER_, handler);
            this.meta_addMethod(new PrimitiveMethod(_CANCEL_, NATTable.EMPTY) {

                public ATObject base_apply(ATTable arguments, ATContext ctx) throws InterpreterException {
                    int arity = arguments.base_length().asNativeNumber().javaValue;
                    if (arity != 0) {
                        throw new XArityMismatch("cancel", 0, arity);
                    }
                    NATReconnectionSubscription scope = NATReconnectionSubscription.this;
                    NATFarReference reference = scope.impl_invokeAccessor(scope, _REFERENCE_, NATTable.EMPTY).asNativeFarReference();
                    if (reference instanceof NATFarReference) {
                        NATFarReference remote = (NATFarReference) reference;
                        ATObject handler = scope.impl_callField(_HANDLER_);
                        remote.removeReconnectionListener(handler.asClosure());
                    }
                    return Evaluator.getNil();
                }
            });
        }

        public NATText meta_print() throws InterpreterException {
            return NATText.atValue("<reconnection subscription:" + this.impl_invokeAccessor(this, _REFERENCE_, NATTable.EMPTY) + ">");
        }
    }

    public static class NATExpiredSubscription extends NATObject {

        private static final AGSymbol _REFERENCE_ = AGSymbol.jAlloc("reference");

        private static final AGSymbol _HANDLER_ = AGSymbol.jAlloc("handler");

        private static final AGSymbol _CANCEL_ = AGSymbol.jAlloc("cancel");

        public NATExpiredSubscription(final NATFarReference reference, ATClosure handler) throws InterpreterException {
            this.meta_defineField(_REFERENCE_, reference);
            this.meta_defineField(_HANDLER_, handler);
            this.meta_addMethod(new PrimitiveMethod(_CANCEL_, NATTable.EMPTY) {

                public ATObject base_apply(ATTable arguments, ATContext ctx) throws InterpreterException {
                    int arity = arguments.base_length().asNativeNumber().javaValue;
                    if (arity != 0) {
                        throw new XArityMismatch("cancel", 0, arity);
                    }
                    NATExpiredSubscription scope = NATExpiredSubscription.this;
                    NATFarReference reference = scope.impl_invokeAccessor(scope, _REFERENCE_, NATTable.EMPTY).asNativeFarReference();
                    if (reference instanceof NATFarReference) {
                        NATFarReference remote = (NATFarReference) reference;
                        ATObject handler = scope.impl_callField(_HANDLER_);
                        remote.removeTakenOfflineListener(handler.asClosure());
                    }
                    return Evaluator.getNil();
                }
            });
        }

        public NATText meta_print() throws InterpreterException {
            return NATText.atValue("<expired subscription:" + this.impl_invokeAccessor(this, _REFERENCE_, NATTable.EMPTY) + ">");
        }
    }

    /**
	  * An outbox letter is a named subclass of NATLetter (See {@link ATLetter} interface), 
	  * wrapping the AmbientTalk message that is going to be transmitted and its serialized version.
	  * The constructor is still executed by the {@link ELActor} that schedules a message to be transmitted. 
	  * This ensures that the serialization of the message happens in the correct thread.
	  */
    public static class NATOutboxLetter extends NATLetter {

        /**  the serialized version of the original message to be sent.
		 * Used to be able to retract a message without involving 
		 * serialization/desearialization because sometimes o != resolve(pass(o))
		 */
        private final Packet serializedMessage_;

        public NATOutboxLetter(LinkedList outbox, ATObject receiver, ATObject message) throws InterpreterException {
            super(outbox, receiver, message);
            serializedMessage_ = new Packet(message.toString(), NATTable.of(receiver, message));
        }

        public ATLetter asLetter() {
            return this;
        }

        public NATOutboxLetter asNativeOutboxLetter() {
            return this;
        }

        public Packet impl_getSerializedMessage() {
            return serializedMessage_;
        }
    }
}
