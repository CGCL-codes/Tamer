package org.go.matchers;

import org.go.util.Key;

/**
 * Matches using an NOT operator on another Matcher. 
 *  
 * @author jhouse
 */
public class NotMatcher<T extends Key> implements Matcher<T> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8473213221391664494L;

    /**
	 * Create a NotMatcher that reverses the result of the given matcher.
	 */
    public static <U extends Key> NotMatcher<U> not(Matcher<U> operand) {
        return new NotMatcher<U>(operand);
    }

    protected Matcher<T> operand;

    protected NotMatcher(Matcher<T> operand) {
        if (operand == null) throw new IllegalArgumentException("Non-null operand required!");
        this.operand = operand;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        NotMatcher other = (NotMatcher) obj;
        if (operand == null) {
            if (other.operand != null) return false;
        } else if (!operand.equals(other.operand)) return false;
        return true;
    }

    public Matcher<T> getOperand() {
        return operand;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((operand == null) ? 0 : operand.hashCode());
        return result;
    }

    @Override
    public boolean isMatch(T key) {
        return !operand.isMatch(key);
    }
}
