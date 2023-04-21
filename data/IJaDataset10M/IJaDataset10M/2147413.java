package org.waveprotocol.wave.client.editor.selection.html;

import com.google.gwt.dom.client.Node;
import org.waveprotocol.wave.common.logging.LoggerBundle;
import org.waveprotocol.wave.model.document.util.FocusedPointRange;
import org.waveprotocol.wave.model.document.util.Point;
import org.waveprotocol.wave.model.document.util.PointRange;

/**
 * Standard (Safari, Firefox) browser-specific selection implementation
 */
abstract class SelectionImpl {

    /**
   * Shorthand selection debug logger
   */
    static LoggerBundle logger = NativeSelectionUtil.LOG;

    /**
   * Fast implementation to check if there is a selection or not.
   * @return true if there is a selection
   */
    abstract boolean selectionExists();

    /**
   * @return Current selection
   */
    abstract FocusedPointRange<Node> get();

    /**
   * @return Current selection
   */
    abstract PointRange<Node> getOrdered();

    /**
   * @return true if the selection is ordered
   */
    abstract boolean isOrdered();

    /**
   * Sets selection
   * @param anchor
   * @param focus
   */
    abstract void set(Point<Node> anchor, Point<Node> focus);

    /**
   * Sets selection
   *
   * @param point
   */
    abstract void set(Point<Node> focus);

    /**
   * Clears the selection
   */
    abstract void clear();

    /**
   * Saves the selection internally in a manner optimised for each browser
   */
    abstract void saveSelection();

    /**
   * Restores the selection saved with {@link #saveSelection()}
   *
   * Behaviour is undefined if the DOM has been changed since the selection
   * was saved.
   */
    abstract void restoreSelection();
}
