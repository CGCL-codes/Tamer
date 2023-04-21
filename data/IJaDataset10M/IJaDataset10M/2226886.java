package org.waveprotocol.wave.client.editor.selection.html;

import com.google.gwt.dom.client.Node;
import org.waveprotocol.wave.model.document.util.FocusedPointRange;
import org.waveprotocol.wave.model.document.util.Point;
import org.waveprotocol.wave.model.document.util.PointRange;

/**
 * Selection implementation that does nothing. This is for user agents where
 * we are not providing proper editor support, such as mobile clients.
 *
 * @author danilatos@google.com (Daniel Danilatos)
 */
public class SelectionImplDisabled extends SelectionImpl {

    /** {@inheritDoc} */
    @Override
    public void set(Point<Node> point) {
    }

    /** {@inheritDoc} */
    @Override
    public void set(Point<Node> start, Point<Node> end) {
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {
    }

    @Override
    FocusedPointRange<Node> get() {
        return null;
    }

    @Override
    PointRange<Node> getOrdered() {
        return null;
    }

    @Override
    boolean isOrdered() {
        return true;
    }

    @Override
    boolean selectionExists() {
        return false;
    }

    @Override
    void restoreSelection() {
    }

    @Override
    void saveSelection() {
    }
}
