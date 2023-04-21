package net.sourceforge.jdefprog.annorules.rules;

import java.lang.annotation.Annotation;
import net.sourceforge.jdefprog.annocheck.AnnoOnParameterChecker;
import net.sourceforge.jdefprog.annorules.AnnoRules;
import net.sourceforge.jdefprog.annorules.MessageEmitter;

public class JustOnStringRule<L, E extends L, ANNOTATION extends Annotation> extends AnnoRules<L, E, ANNOTATION, AnnoOnParameterChecker<L, E>> {

    @Override
    protected boolean constraintsOnContext(E element, AnnoOnParameterChecker<L, E> checker, MessageEmitter<L> msgEmitter) {
        return checker.allowOnlyClasses(element, new Class<?>[] { String.class });
    }
}
