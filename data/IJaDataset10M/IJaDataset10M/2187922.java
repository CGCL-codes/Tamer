package net.sourceforge.jdefprog.annorules.rules;

import java.lang.annotation.Annotation;
import net.sourceforge.jdefprog.annocheck.AnnoOnParameterChecker;

public class OnNumbersInConcreteMethodsRule<L, E extends L, A extends Annotation> extends ComposedRule<L, E, A, AnnoOnParameterChecker<L, E>> {

    public OnNumbersInConcreteMethodsRule() {
        this.addRule(new ParamJustOnConcreteMethodsRule<L, E, A>());
        this.addRule(new JustOnNumbersRule<L, E, A>());
    }
}
