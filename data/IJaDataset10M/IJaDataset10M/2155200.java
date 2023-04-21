package cz.cuni.mff.ksi.jinfer.basicxsd.preprocessing;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents a result of preprocessing.
 * @author rio
 */
public final class PreprocessingResult {

    private final List<Element> originalElements;

    private final List<Element> toposortedElements;

    private final Map<String, Boolean> globalElementFlags;

    PreprocessingResult(final List<Element> originalElements, final List<Element> toposortedElements, final Map<String, Boolean> globalElementFlags) {
        this.originalElements = originalElements;
        this.toposortedElements = toposortedElements;
        this.globalElementFlags = globalElementFlags;
    }

    /**
   * Returns an element reference by its name.
   * @param elementName Name of a desired element.
   * @return Valid reference or <code>null</code> if the element is not present in the grammar.
   */
    public Element getElementByName(final String elementName) {
        for (Element element : originalElements) {
            if (element.getName().equalsIgnoreCase(elementName)) {
                return element;
            }
        }
        return null;
    }

    /**
   * Decides whether a given element's type can be considered a global type.
   * @param elementName Name of an element.
   */
    public boolean isElementGlobal(final String elementName) {
        return globalElementFlags.get(elementName).booleanValue();
    }

    /**
   * Returns a list of global elements. Global elements are those which types
   * have been considered global types.
   * @return Global elements.
   */
    public List<Element> getGlobalElements() {
        final List<Element> globalElements = new LinkedList<Element>();
        for (Element element : toposortedElements) {
            if (isElementGlobal(element.getName())) {
                globalElements.add(element);
            }
        }
        return globalElements;
    }

    /**
   * Returns the root element.
   * @return Root element.
   */
    public Element getRootElement() {
        return toposortedElements.get(toposortedElements.size() - 1);
    }
}
