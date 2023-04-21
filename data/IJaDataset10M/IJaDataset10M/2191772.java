package gate.jape.constraint;

import java.util.regex.Matcher;

/**
 * Implementation of the =~ predicate, which succeeds if any part of the
 * annotation value matches the given regular expression, and fails
 * otherwise.
 */
public class RegExpFindPredicate extends AbstractRegExpPredicate {

    @Override
    protected boolean matcherResult(Matcher m) {
        return m.find();
    }

    public String getOperator() {
        return REGEXP_FIND;
    }
}
