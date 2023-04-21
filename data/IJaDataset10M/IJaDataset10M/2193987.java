package org.simplx.xstream;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.simplx.xstream.MixedStreamReader.TextPart;
import java.util.Iterator;

/**
 * A class that copies mixed streams.
 *
 * @author Ken Arnold
 */
public class MixedStreamCopier {

    /** Creates a new {@link MixedStreamCopier}. */
    public MixedStreamCopier() {
    }

    /**
     * Copies a mixed stream to a destination writer.
     *
     * @param source      The mixed stream source.
     * @param destination The copy destination.
     */
    public void copy(MixedStreamReader source, HierarchicalStreamWriter destination) {
        doCopy(source, destination, false);
    }

    private void doCopy(MixedStreamReader source, HierarchicalStreamWriter destination, boolean startNode) {
        if (startNode) destination.startNode(source.getNodeName());
        int attributeCount = source.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            destination.addAttribute(source.getAttributeName(i), source.getAttribute(i));
        }
        Iterator parts = source.partIterator();
        while (parts.hasNext()) {
            Object part = parts.next();
            if (part instanceof TextPart) destination.setValue(((TextPart) part).contents()); else {
                source.moveDown();
                doCopy(source, destination, true);
                source.moveUp();
            }
        }
        if (startNode) destination.endNode();
    }
}
