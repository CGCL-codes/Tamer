package astcentric.structure.validation.regex;

import astcentric.structure.basic.Node;
import astcentric.structure.regex.Matcher;

/**
 * Factory for a {@link Matcher} for items of type <code>T</code>
 * defined by a {@link Node}. 
 */
public interface MatcherFactory<T> {

    /**
   * Creates a matcher a specified by the specification node.
   */
    public Matcher<T> create(Node specification);
}
