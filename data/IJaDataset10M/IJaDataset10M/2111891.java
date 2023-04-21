package edu.vub.at.objects.natives;

import edu.vub.at.actors.natives.ELActor;
import edu.vub.at.exceptions.InterpreterException;
import edu.vub.at.exceptions.XArityMismatch;
import edu.vub.at.objects.ATContext;
import edu.vub.at.objects.ATMirrorFactory;
import edu.vub.at.objects.ATObject;
import edu.vub.at.objects.ATTable;
import edu.vub.at.objects.mirrors.PrimitiveMethod;
import edu.vub.at.objects.natives.grammar.AGSymbol;
import edu.vub.at.util.logging.Logging;

public class NATMirrorFactory extends NATObject implements ATMirrorFactory {

    private static final AGSymbol _CRM_NAME_ = AGSymbol.jAlloc("createMirror");

    /** def createMirror(reflectee) { actor.createMirror(reflectee) } */
    private static final PrimitiveMethod _PRIM_CRM_ = new PrimitiveMethod(AGSymbol.jAlloc("createMirror"), NATTable.atValue(new ATObject[] { AGSymbol.jAlloc("reflectee") })) {

        private static final long serialVersionUID = -6864350589143196204L;

        public ATObject base_apply(ATTable arguments, ATContext ctx) throws InterpreterException {
            if (!arguments.base_length().equals(NATNumber.ONE)) {
                throw new XArityMismatch("createMirror", 1, arguments.base_length().asNativeNumber().javaValue);
            }
            return ctx.base_lexicalScope().asMirrorFactory().base_createMirror(arguments.base_at(NATNumber.ONE));
        }
    };

    public NATMirrorFactory() {
        try {
            this.meta_addMethod(_PRIM_CRM_);
        } catch (InterpreterException e) {
            Logging.Actor_LOG.fatal("Error while initializing default mirror factory", e);
        }
    }

    public ATObject base_createMirror(ATObject reflectee) throws InterpreterException {
        return ELActor.currentActor().getActorMirror().base_createMirror(reflectee);
    }

    public ATMirrorFactory asMirrorFactory() throws InterpreterException {
        return this;
    }
}
