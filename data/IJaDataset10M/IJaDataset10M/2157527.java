package joc.internal.transformer;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import joc.BeforeCondition;
import joc.Condition;
import joc.JOCCondition;
import joc.PostCondition;
import joc.internal.compiler.EmptyExp;
import joc.internal.compiler.StandaloneExp;
import joc.internal.transformer.helper.BackdoorAnnotationLoader;

public class CustomPostConditionBehaviorTransformer extends DeclaredBehaviorTransformer {

    public CustomPostConditionBehaviorTransformer(Transformer transformer) {
        super(transformer);
    }

    @Override
    public void transform(CtClass clazz, CtBehavior behavior) throws Exception {
        getLogger().info("behavior " + behavior.getLongName());
        for (CtBehavior consultedBehavior : getConsultedBehaviors(behavior)) {
            getLogger().info("consulted behavior " + consultedBehavior.getLongName());
            if (consultedBehavior.hasAnnotation(PostCondition.class)) {
                String[] pcClasses = new BackdoorAnnotationLoader(consultedBehavior).getClassArrayValue(PostCondition.class, "value");
                for (String pcClass : pcClasses) {
                    applyPostCondition(behavior, transformer.getPool().get(pcClass));
                }
            }
        }
    }

    private void applyPostCondition(CtBehavior behavior, CtClass pcClass) throws Exception {
        if (!pcClass.hasAnnotation(JOCCondition.class)) {
            throw new JOCTransformationException("Post-Condition " + pcClass.getName() + " must have annotation JOCCondition.");
        }
        String pcClassName = pcClass.getName();
        getLogger().info("behavior " + behavior.getLongName() + " has post-condition " + pcClassName);
        StandaloneExp insertBefore = initializeConditionString(pcClassName);
        StandaloneExp insertAfter = new EmptyExp();
        for (CtMethod pcMethod : pcClass.getDeclaredMethods()) {
            if (pcMethod.hasAnnotation(BeforeCondition.class)) {
                insertBefore = insertBefore.append(callConditionString(pcClass, pcMethod, false, behavior, false));
            } else if (pcMethod.hasAnnotation(Condition.class)) {
                insertAfter = insertAfter.append(callConditionString(pcClass, pcMethod, true, behavior, true));
            }
        }
        getLogger().info("insertBefore: " + insertBefore);
        getLogger().info("insertAfter: " + insertAfter);
        insertBefore.insertBefore(behavior);
        insertAfter.insertAfter(behavior);
    }
}
