package net.sourceforge.jdefprog.mcl.interpret.analysys;

import net.sourceforge.jdefprog.mcl.interpret.Interpreter;
import net.sourceforge.jdefprog.mcl.interpret.MclUsingCase;
import net.sourceforge.jdefprog.mcl.interpret.OldTypesCollector;
import net.sourceforge.jdefprog.mcl.interpret.OldTypesSource;
import net.sourceforge.jdefprog.mcl.interpret.OldValuesCollector;
import net.sourceforge.jdefprog.mcl.interpret.OldValuesSource;
import net.sourceforge.jdefprog.mcl.interpret.context.builders.TypeResolver;
import net.sourceforge.jdefprog.mcl.interpret.context.factories.TypeAccessContextFactory;
import net.sourceforge.jdefprog.mcl.interpret.context.factories.ValuedAccessContextFactory;
import net.sourceforge.jdefprog.mcl.interpret.context.type.TypedContext;
import net.sourceforge.jdefprog.mcl.interpret.context.valued.ValuedAccessContext;

/**
 * Entity that collects all the information needed to build an Interpreter but a
 * ValuedAccessContext that can be specified later (if needed).
 * 
 * @author Federico Tomassetti (f.tomassetti@gmail.com)
 */
abstract class InterpreterBuilder {

    abstract Interpreter build(MclUsingCase usingCase, ValuedAccessContext ctx);

    public static InterpreterBuilder forTyped(TypeAccessContextFactory typeFactory, TypedContext typeContext, TypeResolver tr, boolean booleanResult) {
        return new TypedInterpreterBuilder(typeFactory, typeContext, tr, booleanResult);
    }

    public static InterpreterBuilder forOldValued(TypeAccessContextFactory typeFactory, ValuedAccessContextFactory vf, TypeResolver typeResolutor, OldValuesCollector ovc, boolean fbr) {
        return new OldValuedInterpreterBuilder(typeFactory, vf, typeResolutor, ovc, fbr);
    }

    public static InterpreterBuilder forCurrValued(TypeAccessContextFactory tf, ValuedAccessContextFactory vf, boolean fb, TypeResolver tr, OldValuesSource ovs) {
        return new CurrValuedInterpreterBuilder(tf, vf, fb, tr, ovs);
    }

    private static class TypedInterpreterBuilder extends InterpreterBuilder {

        OldTypesCollector oldTypesBuffer;

        TypedContext typeContext;

        TypeAccessContextFactory typeFactory;

        TypeResolver classResolutor;

        boolean booleanResult;

        public TypedInterpreterBuilder(TypeAccessContextFactory typeFactory, TypedContext typeContext, TypeResolver tr, boolean booleanResult) {
            this.typeFactory = typeFactory;
            this.typeContext = typeContext;
            this.classResolutor = tr;
            this.booleanResult = booleanResult;
        }

        public Interpreter build(MclUsingCase usingCase, ValuedAccessContext ctx) {
            if (null != ctx) throw new IllegalArgumentException("ctx is not null");
            return Interpreter.createTypeEvaluator(usingCase, typeFactory, typeContext, classResolutor, booleanResult);
        }
    }

    private static class OldValuedInterpreterBuilder extends InterpreterBuilder {

        OldValuesCollector oldValuesBuffer;

        TypedContext typeContext;

        TypeAccessContextFactory typeFactory;

        TypeResolver classResolutor;

        ValuedAccessContextFactory valuedFactory;

        private boolean fbr;

        public OldValuedInterpreterBuilder(TypeAccessContextFactory typeFactory, ValuedAccessContextFactory vf, TypeResolver typeResolutor, OldValuesCollector ovc, boolean fbr) {
            this.typeFactory = typeFactory;
            this.valuedFactory = vf;
            this.classResolutor = typeResolutor;
            this.oldValuesBuffer = ovc;
            this.fbr = fbr;
        }

        public Interpreter build(MclUsingCase usingCase, ValuedAccessContext ctx) {
            if (null == ctx) throw new IllegalArgumentException("ctx not null");
            return Interpreter.createValuedOldEvaluator(usingCase, typeFactory, valuedFactory, ctx, fbr, classResolutor, oldValuesBuffer);
        }
    }

    private static class CurrValuedInterpreterBuilder extends InterpreterBuilder {

        OldValuesSource oldValuesBuffer;

        TypedContext typeContext;

        TypeAccessContextFactory typeFactory;

        TypeResolver classResolutor;

        boolean forceBooleanResult;

        ValuedAccessContextFactory valuedFactory;

        public CurrValuedInterpreterBuilder(TypeAccessContextFactory tf, ValuedAccessContextFactory vf, boolean fb, TypeResolver tr, OldValuesSource ovs) {
            this.typeFactory = tf;
            this.valuedFactory = vf;
            this.forceBooleanResult = fb;
            this.classResolutor = tr;
            this.oldValuesBuffer = ovs;
        }

        public Interpreter build(MclUsingCase usingCase, ValuedAccessContext ctx) {
            if (null == ctx) throw new IllegalArgumentException("ctx is null");
            return Interpreter.createValuedCurrEvaluator(usingCase, typeFactory, valuedFactory, ctx, forceBooleanResult, classResolutor, oldValuesBuffer);
        }
    }
}
