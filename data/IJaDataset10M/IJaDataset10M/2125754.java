package gate.creole.coref.matchers;

import gate.Annotation;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Matcher;

/**
 *
 */
public class False extends AbstractMatcher {

    public False(String annotationType, String antecedentType) {
        super(annotationType, antecedentType);
    }

    @Override
    public boolean matches(Annotation[] anaphors, int antecedent, int anaphor, CorefBase owner) {
        return false;
    }
}
