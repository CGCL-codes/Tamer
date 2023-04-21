package de.tudarmstadt.ukp.wikipedia.parser;

/**
 * This is a simple ContentElement, wich occures in a NestedList.
 * @author CJacobi
 *
 */
public class NestedListElement extends ContentElement implements NestedList {

    public String toString() {
        return "NLC_IS_CONTENT: true\n" + super.toString();
    }
}
