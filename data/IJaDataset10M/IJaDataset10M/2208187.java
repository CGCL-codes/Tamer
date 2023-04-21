package maze.commons.ee.jdom.impl;

import java.util.List;
import maze.commons.ee.jdom.ListBuilder;
import maze.commons.ee.jdom.util.Contained;
import org.jdom.Element;

/**
 * 
 * @author Normunds Mazurs
 */
public abstract class AbstractXmlContainedListBuilder<E> extends AbstractXmlContainedBuilder implements ListBuilder<E> {

    @SuppressWarnings("rawtypes")
    private final Contained<List> containedList = new Contained<List>();

    @SuppressWarnings("rawtypes")
    protected abstract List createOutputList();

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<E> build() {
        if (containedList.hasContained()) {
            return containedList.getContained();
        }
        final List outputList = createOutputList();
        for (final Object n : getNodesSelector().selectNodes()) {
            if (n instanceof Element) {
                outputList.add(createOutputObject((Element) n));
            }
        }
        return containedList.setContained(outputList);
    }
}
