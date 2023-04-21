package edu.vub.at.actors.natives;

import edu.vub.at.actors.ATAsyncMessage;
import edu.vub.at.eval.Evaluator;
import edu.vub.at.exceptions.InterpreterException;
import edu.vub.at.exceptions.XArityMismatch;
import edu.vub.at.exceptions.XTypeMismatch;
import edu.vub.at.objects.ATContext;
import edu.vub.at.objects.ATMessage;
import edu.vub.at.objects.ATNumber;
import edu.vub.at.objects.ATObject;
import edu.vub.at.objects.ATTable;
import edu.vub.at.objects.ATText;
import edu.vub.at.objects.ATTypeTag;
import edu.vub.at.objects.coercion.NativeTypeTags;
import edu.vub.at.objects.grammar.ATSymbol;
import edu.vub.at.objects.mirrors.PrimitiveMethod;
import edu.vub.at.objects.natives.FieldMap;
import edu.vub.at.objects.natives.MethodDictionary;
import edu.vub.at.objects.natives.NATMessage;
import edu.vub.at.objects.natives.NATMethodInvocation;
import edu.vub.at.objects.natives.NATNumber;
import edu.vub.at.objects.natives.NATObject;
import edu.vub.at.objects.natives.NATTable;
import edu.vub.at.objects.natives.NATText;
import edu.vub.at.objects.natives.grammar.AGSymbol;
import edu.vub.at.parser.SourceLocation;
import edu.vub.util.TempFieldGenerator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;

/**
 * Instances of the class NATAsyncMessage represent first-class asynchronous messages.
 * 
 * @author tvcutsem
 */
public class NATAsyncMessage extends NATMessage implements ATAsyncMessage {

    /** def process(bhv) { nil } */
    private static final PrimitiveMethod _PRIM_PRO_ = new PrimitiveMethod(AGSymbol.jAlloc("process"), NATTable.atValue(new ATObject[] { AGSymbol.jAlloc("bhv") })) {

        private static final long serialVersionUID = -1307795172754072220L;

        public ATObject base_apply(ATTable arguments, ATContext ctx) throws InterpreterException {
            int arity = arguments.base_length().asNativeNumber().javaValue;
            if (arity != 1) {
                throw new XArityMismatch("process", 1, arity);
            }
            return ctx.base_lexicalScope().asAsyncMessage().prim_process(ctx.base_receiver().asAsyncMessage(), arguments.base_at(NATNumber.ONE));
        }
    };

    /** def getLocationLine() { nil } */
    private static final PrimitiveMethod _PRIM_LINE_ = new PrimitiveMethod(AGSymbol.jAlloc("getLocationLine"), NATTable.EMPTY) {

        public ATObject base_apply(ATTable arguments, ATContext ctx) throws InterpreterException {
            int arity = arguments.base_length().asNativeNumber().javaValue;
            if (arity != 0) {
                throw new XArityMismatch("getLocationLine", 0, arity);
            }
            return ctx.base_lexicalScope().asAsyncMessage().prim_getLocationLine();
        }
    };

    /** def getLocationFilename() { nil } */
    private static final PrimitiveMethod _PRIM_FILE_ = new PrimitiveMethod(AGSymbol.jAlloc("getLocationFilename"), NATTable.EMPTY) {

        public ATObject base_apply(ATTable arguments, ATContext ctx) throws InterpreterException {
            int arity = arguments.base_length().asNativeNumber().javaValue;
            if (arity != 0) {
                throw new XArityMismatch("getLocationFilename", 0, arity);
            }
            return ctx.base_lexicalScope().asAsyncMessage().prim_getLocationFilename();
        }
    };

    /**
     * Create a new asynchronous message.
     * @param sel the selector of the asynchronous message
     * @param arg the arguments of the asynchronous message
     * @param types the types for the message. Isolate and AsyncMessage types are automatically added.
     */
    public NATAsyncMessage(ATSymbol sel, ATTable arg, ATTable types) throws InterpreterException {
        super(sel, arg, types, NativeTypeTags._ASYNCMSG_);
        super.meta_addMethod(_PRIM_PRO_);
        super.meta_addMethod(_PRIM_LINE_);
        super.meta_addMethod(_PRIM_FILE_);
    }

    public static NATAsyncMessage createExternalAsyncMessage(ATSymbol sel, ATTable args, ATTable types) throws InterpreterException {
        ATObject[] unwrapped = types.asNativeTable().elements_;
        ATTypeTag[] fulltypes = new ATTypeTag[unwrapped.length + 1];
        for (int i = 0; i < unwrapped.length; i++) {
            fulltypes[i] = unwrapped[i].asTypeTag();
        }
        fulltypes[unwrapped.length] = NativeTypeTags._EXTERNAL_MSG_;
        return new NATAsyncMessage(sel, args, NATTable.atValue(fulltypes));
    }

    /**
     * Copy constructor.
     */
    private NATAsyncMessage(FieldMap map, Vector state, LinkedList originalCustomFields, MethodDictionary methodDict, ATObject dynamicParent, ATObject lexicalParent, byte flags, ATTypeTag[] types, Set freeVars) throws InterpreterException {
        super(map, state, originalCustomFields, methodDict, dynamicParent, lexicalParent, flags, types, freeVars);
    }

    /**
     * If cloning is not adapted for asynchronous messages, the result of cloning a
     * NATAsyncMessage is a NATObject, which is fine except that NATObject does not know
     * of prim_sendTo!
     */
    protected NATObject createClone(FieldMap map, Vector state, LinkedList originalCustomFields, MethodDictionary methodDict, ATObject dynamicParent, ATObject lexicalParent, byte flags, ATTypeTag[] types, Set freeVars) throws InterpreterException {
        return new NATAsyncMessage(map, state, originalCustomFields, methodDict, dynamicParent, lexicalParent, flags, types, freeVars);
    }

    /**
     * When process is invoked from the Java-level, invoke the primitive implementation
     * with self bound to 'this'.
     */
    public ATObject base_process(ATObject receiver) throws InterpreterException {
        return this.prim_process(this, receiver);
    }

    /**
     * The default implementation is to invoke the method corresponding to this message's selector.
     */
    public ATObject prim_process(ATAsyncMessage self, ATObject receiver) throws InterpreterException {
        NATMethodInvocation inv = new NATMethodInvocation(self.base_selector(), self.base_arguments(), self.meta_typeTags());
        inv.impl_setLocation(self.impl_getLocation());
        return receiver.meta_invoke(receiver, inv);
    }

    /**
     * To evaluate an asynchronous message send, the asynchronous
     * message object is asked to be <i>sent</t> by the sender object.
     * I.e.: <tt>(reflect: sender).send(receiver, asyncmsg)</tt>
     * 
     * @return NIL, by default. Overridable by the sender.
     */
    public ATObject prim_sendTo(ATMessage self, ATObject receiver, ATObject sender) throws InterpreterException {
        return sender.meta_send(receiver, self.asAsyncMessage());
    }

    public NATText meta_print() throws InterpreterException {
        return NATText.atValue("<async msg:" + base_selector() + Evaluator.printAsList(base_arguments()).javaValue + ">");
    }

    public NATText impl_asCode(TempFieldGenerator objectMap) throws InterpreterException {
        if (objectMap.contains(this)) {
            return objectMap.getName(this);
        }
        NATText code = NATText.atValue("<-" + base_selector().toString() + Evaluator.codeAsList(objectMap, base_arguments()).javaValue);
        NATText name = objectMap.put(this, code);
        return name;
    }

    public ATAsyncMessage asAsyncMessage() throws XTypeMismatch {
        return this;
    }

    public ATObject base_getLocationLine() throws InterpreterException {
        return prim_getLocationLine();
    }

    public ATObject base_getLocationFilename() throws InterpreterException {
        return prim_getLocationFilename();
    }

    public ATObject prim_getLocationLine() throws InterpreterException {
        if (null != super.impl_getLocation()) {
            return NATNumber.atValue(super.impl_getLocation().line);
        } else {
            return Evaluator.getNil();
        }
    }

    public ATObject prim_getLocationFilename() throws InterpreterException {
        if (null != super.impl_getLocation()) {
            return NATText.atValue(super.impl_getLocation().fileName);
        } else {
            return Evaluator.getNil();
        }
    }
}
