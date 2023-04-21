package org.progeeks.parser.regex;

import java.util.*;
import java.util.regex.*;

/**
 *  CompositePattern implementation that selects the first
 *  pattern in a collection of patterns that matches the supplied
 *  input.  The various search methods behave as follows:
 *  <ul>
 *     <li>lookingAt() : the first child matcher with a lookingAt() that
 *                       returns true will be used for the result.
 *     <li>matches() : the first child matcher with a matches() that returns
 *                       true will be used for the result.
 *     <li>find() : the child with the smallest start index after returning
 *                      true from its find() method will be used for the
 *                      result.  This means that in many cases all options
 *                      must be attempted.  A short-circuit will automatically
 *                      occur if start() returns 0.
 *  </ul>
 *
 *  @version   $Revision: 1.6 $
 *  @author    Paul Speed
 */
public class OrPattern extends AbstractCompositePattern {

    private List children = new ArrayList();

    private CompositePattern[] childArray;

    public OrPattern() {
    }

    public OrPattern(String name) {
        super(name);
    }

    /**
     *  Sets the child list of CompositePatterns.
     */
    public void setChildren(List list) {
        if (children == list) return;
        children.clear();
        children.addAll(list);
        childArray = null;
    }

    /**
     *  Returns the list of child composite patterns.
     */
    public List getChildren() {
        return (children);
    }

    /**
     *  Returns the array of children for easier access.
     */
    protected CompositePattern[] getChildArray() {
        if (childArray != null) return (childArray);
        childArray = new CompositePattern[children.size()];
        childArray = (CompositePattern[]) children.toArray(childArray);
        return (childArray);
    }

    /**
     *  Returns a CompositeMatcher capable of processing the input
     *  stream using this pattern.
     */
    public CompositeMatcher matcher(CharSequence s) {
        return (new MatcherAdapter(this, s));
    }

    private static class MatcherAdapter extends AbstractMatcher {

        private OrPattern pattern;

        private CharSequence input;

        private CompositePattern[] children;

        private CompositeMatcher found = null;

        private int start = 0;

        private int end = 0;

        public MatcherAdapter(OrPattern pattern, CharSequence s) {
            this.pattern = pattern;
            this.input = s;
            resetState();
            this.children = pattern.getChildArray();
        }

        /**
         *  Returns the CompositePattern that is interpreted by this matcher.
         */
        public CompositePattern getPattern() {
            return (pattern);
        }

        /**
         *  Resets this matcher's internal state.
         */
        public CompositeMatcher resetState() {
            return (resetState(input));
        }

        /**
         *  Resets this matcher and changes the character sequence upon which
         *  it operates.
         */
        public CompositeMatcher resetState(CharSequence input) {
            this.found = null;
            this.input = input;
            this.start = 0;
            this.end = 0;
            return (this);
        }

        /**
         *  Returns the start index of the previous match.
         */
        public int start() {
            if (found == null) throw new IllegalStateException("No match information available.");
            return (start);
        }

        /**
         *  Returns the index of the last character matched plus one.
         */
        public int end() {
            if (found == null) throw new IllegalStateException("No match information available.");
            return (end);
        }

        /**
         *  Attempts to match the entire input sequence against the pattern.
         */
        public boolean matches() {
            if (children.length == 0) return (false);
            for (int i = 0; i < children.length; i++) {
                CompositeMatcher m = children[i].matcher(input);
                if (m.matches()) {
                    found = m;
                    start = m.start();
                    end = m.end();
                    return (true);
                }
            }
            return (false);
        }

        /**
         *  Attempts to find the next subsequence of the input sequence that matches
         *  the pattern.
         */
        public boolean find() {
            if (children.length == 0) return (false);
            CharSequence s = input;
            if (end > 0) s = s.subSequence(end, s.length());
            int minStart = Integer.MAX_VALUE;
            int minEmptyStart = Integer.MAX_VALUE;
            found = null;
            CompositeMatcher emptyFound = null;
            for (int i = 0; i < children.length; i++) {
                CompositeMatcher m = children[i].matcher(s);
                if (m.find()) {
                    int j = m.start();
                    if (m.end() == j && j < minEmptyStart) {
                        minEmptyStart = minStart;
                        emptyFound = m;
                    } else if (m.end() != minStart && j < minStart) {
                        minStart = m.start();
                        found = m;
                        if (minStart == 0) break;
                    }
                }
            }
            if (found == null) found = emptyFound;
            if (found != null) {
                start = end + found.start();
                end = end + found.end();
                return (true);
            }
            return (false);
        }

        /**
         *  Attempts to match the first part of the input sequence against the pattern.
         */
        public boolean lookingAt() {
            if (children.length == 0) return (false);
            for (int i = 0; i < children.length; i++) {
                CompositeMatcher m = children[i].matcher(input);
                if (m.lookingAt()) {
                    found = m;
                    start = m.start();
                    end = m.end();
                    return (true);
                }
            }
            return (false);
        }

        protected Object getParsedValue() {
            if (found == null) return (null);
            return (found.getProduction());
        }
    }
}
