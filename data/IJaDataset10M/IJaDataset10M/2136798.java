package org.mobicents.media.server.impl.naming;

import java.util.ArrayList;

/**
 * Generates concrete names for endpoint using specified pattern.
 *
 * The local endpoint name is case-insensitive.  The syntax of the local
 * endpoint name is hierarchical, where the least specific component of
 * the name is the leftmost term, and the most specific component is the
 * rightmost term.  The precise syntax depends on the type of endpoint
 * being named and MAY start with a term that identifies the endpoint
 * type.
 *
 * Patterns follows to the name rules but allows to underspecify the individual
 * terms as numeric range. The range syntax is similar to regular expression
 * symbolic class syntax.
 *
 * Example: mobicents/aap/[1..100] 
 * 
 * @author kulikov
 */
public class EndpointNameGenerator {

    private ArrayList<Term> terms = new ArrayList();

    /**
     * Modifies name pattern.
     *
     * @param pattern the new pattern value
     */
    public void setPattern(String pattern) {
        String[] parts = pattern.split("/");
        for (String part : parts) {
            part = part.trim();
            if (part.length() == 0) {
                continue;
            }
            Term term = part.startsWith("[") ? new NumericRange(part) : new Term(part);
            if (terms.size() > 0) {
                terms.get(terms.size() - 1).setChild(term);
            }
            terms.add(term);
        }
    }

    /**
     * Indicates is it possible to generate more names.
     *
     * @return true if more names can be generated.
     */
    public boolean hasMore() {
        return terms.size() > 0 && terms.get(0).hasMore();
    }

    /**
     * Next generated name.
     *
     * @return the next generated name of endpoint.
     */
    public String next() {
        return terms.get(0).next();
    }

    private class Term {

        protected String term;

        private boolean hasMore = true;

        protected Term child;

        public Term(String term) {
            this.term = term;
        }

        public void reset() {
            hasMore = true;
        }

        public void setChild(Term child) {
            this.child = child;
        }

        public boolean hasMore() {
            return child != null ? child.hasMore() : hasMore;
        }

        public String getTerm() {
            return term;
        }

        public String next() {
            if (child == null) hasMore = false;
            return child != null ? term + "/" + child.next() : term;
        }
    }

    private class NumericRange extends Term {

        private int low, high;

        private int value;

        public NumericRange(String term) {
            super(term);
            term = term.substring(1, term.length() - 1);
            term = term.replaceAll("]", "");
            String tokens[] = term.split("\\.\\.");
            low = Integer.parseInt(tokens[0]);
            high = Integer.parseInt(tokens[1]);
            value = low;
        }

        @Override
        public void reset() {
            term = term.substring(1, term.length() - 1);
            term = term.replaceAll("]", "");
            String tokens[] = term.split("\\.\\.");
            low = Integer.parseInt(tokens[0]);
            high = Integer.parseInt(tokens[1]);
            value = low;
        }

        @Override
        public boolean hasMore() {
            return child == null ? value <= high : value < high || child.hasMore();
        }

        @Override
        public String next() {
            if (child != null && !child.hasMore()) {
                value++;
                child.reset();
            }
            String s = child != null ? Integer.toString(value) + "/" + child.next() : Integer.toString(value);
            if (child == null) value++;
            return s;
        }

        @Override
        public String getTerm() {
            return Integer.toString(value);
        }
    }
}
