package ch.kwa.ee.model.transkription.operation;

import java.util.Iterator;
import ch.kwa.ee.model.transkription.ContainerElement;
import ch.kwa.ee.model.transkription.FoilElement;
import ch.kwa.ee.model.transkription.TranskriptionModelElement;

/**
 * Finds a model foil inside a model tree by its index
 */
public class IndexSearchOperation {

    public static FoilElement find(TranskriptionModelElement parent, int index) {
        if (parent.getStartCharIndex() <= index && parent.getEndCharIndex() >= index) {
            if (parent instanceof ContainerElement) {
                Iterator<? extends TranskriptionModelElement> it = ((ContainerElement) parent).getContainedElements().iterator();
                TranskriptionModelElement foil = null;
                int lastEnd = 0;
                while (it.hasNext()) {
                    if (foil != null) lastEnd = foil.getEndCharIndex();
                    foil = it.next();
                    final FoilElement foil2 = find(foil, index);
                    if (foil2 == null) {
                        if ((lastEnd <= index) && (foil.getStartCharIndex() >= index)) {
                            return find(foil, foil.getStartCharIndex());
                        }
                    } else {
                        return foil2;
                    }
                }
            } else if (parent instanceof FoilElement) {
                return (FoilElement) parent;
            }
        }
        return null;
    }
}
