package org.joox;

import org.w3c.dom.Element;

/**
 * @author Lukas Eder
 */
class DefaultContext implements Context {

    private final Element match;

    private final Element element;

    private final int matchIndex;

    private final int elementIndex;

    private final int matchSize;

    private final int elementSize;

    DefaultContext(Element match, int matchIndex, int matchSize, Element element, int elementIndex, int elementSize) {
        this.match = match;
        this.matchIndex = matchIndex;
        this.matchSize = matchSize;
        this.element = element;
        this.elementIndex = elementIndex;
        this.elementSize = elementSize;
    }

    DefaultContext(Element match, int matchIndex, int matchSize) {
        this(match, matchIndex, matchSize, match, matchIndex, matchSize);
    }

    @Override
    public final Element element() {
        return element;
    }

    @Override
    public int elementIndex() {
        return elementIndex;
    }

    @Override
    public int elementSize() {
        return elementSize;
    }

    @Override
    public Element match() {
        return match;
    }

    @Override
    public int matchIndex() {
        return matchIndex;
    }

    @Override
    public int matchSize() {
        return matchSize;
    }
}
