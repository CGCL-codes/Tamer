package ssv.interaction.model.spoon2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import spoon.reflect.Factory;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtSimpleType;
import spoon.reflect.declaration.CtType;
import spoon.reflect.eval.SymbolicEvaluationStep;
import spoon.reflect.eval.SymbolicEvaluator;
import spoon.reflect.eval.SymbolicEvaluatorObserver;
import spoon.reflect.eval.SymbolicInstance;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.eval.VisitorSymbolicEvaluator;
import ssv.interaction.model.Interaction;
import ssv.interaction.model.InteractionModelObserver;
import ssv.interaction.model.InteractionModelView;

public class SpoonInteractionModelView implements SymbolicEvaluatorObserver, InteractionModelView {

    private List<InteractionModelObserver> modelObservers = new ArrayList<InteractionModelObserver>();

    private List<SymbolicInstance<?>> postInvokeInstances = new ArrayList<SymbolicInstance<?>>();

    private Collection<CtMethod<?>> postInvokeMethods = new HashSet<CtMethod<?>>();

    public List<CtMethod<?>> getPostInvokeMethodForType(CtTypeReference<?> type) {
        List<CtMethod<?>> methods = new ArrayList<CtMethod<?>>();
        for (CtMethod<?> method : postInvokeMethods) {
            if (method.getDeclaringType().getClass().isAssignableFrom(type.getClass())) {
                methods.add(method);
            }
        }
        return methods;
    }

    public void onEndPath(SymbolicEvaluator evaluator) {
        for (SymbolicInstance<?> i : postInvokeInstances) {
            for (CtMethod<?> method : getPostInvokeMethodForType(i.getConcreteType())) {
                System.out.println("********** end-path invoking: " + method.getSimpleName());
                evaluator.invoke(i, method, null);
            }
        }
        notifySequenceEnd();
    }

    public void onEnterStep(SymbolicEvaluator evaluator, SymbolicEvaluationStep step) {
        try {
            Interaction interaction = new SpoonInteractionAdapter(step);
            notifyInteraction(interaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyInteraction(Interaction interaction) {
        for (InteractionModelObserver m : modelObservers) {
            m.onInteraction(interaction);
        }
    }

    public void onExitStep(VisitorSymbolicEvaluator evaluator, SymbolicEvaluationStep step) {
        try {
            Interaction interaction = new SpoonInteractionAdapter(step);
            notifyInteraction(interaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStartPath(SymbolicEvaluator evaluator) {
        System.out.println("doing something");
        notifySequenceStart();
    }

    public void addModelObserver(InteractionModelObserver modelObserver) {
        modelObservers.add(modelObserver);
    }

    public CtMethod<?> getCallback(SymbolicInstance<?> instance, Class<?> clazz, String methodName) {
        CtTypeReference<?> type = instance.getConcreteType();
        CtMethod<?> method = null;
        if (clazz.isAssignableFrom(type.getActualClass())) {
            Factory factory = type.getFactory();
            CtSimpleType<?> simpleType = factory.Type().getFactory().Class().create(type.getQualifiedName());
            method = ((CtType<?>) simpleType).getMethod(methodName, factory.Type().createReference(clazz));
        }
        return method;
    }

    public void onNew(VisitorSymbolicEvaluator evaluator, SymbolicInstance<?> instance) {
    }

    public void notifySequenceEnd() {
        for (InteractionModelObserver m : modelObservers) {
            m.onSequenceEnd();
        }
    }

    public void notifySequenceStart() {
        for (InteractionModelObserver m : modelObservers) {
            m.onSequenceStart();
        }
    }
}
