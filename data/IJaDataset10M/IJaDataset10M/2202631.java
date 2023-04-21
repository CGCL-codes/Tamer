package org.openscience.jchempaint.controller;

/**
 * @cdk.module control
 */
public interface IChemModelEventRelayHandler {

    /**
     * Signals that the connectivity table of the structure has changed, for
     * example, an atom or bond was added or removed. This implies that the
     * coordinates have changed too.
     */
    public void structureChanged();

    /**
     * Signals that the atom or bond properties have changed, like atom symbol
     * or bond order. This excludes coordinate changes, for which
     * {@link #coordinatesChanged()} is used.
     */
    public void structurePropertiesChanged();

    /**
     * Signals that the coordinates of the structure or the coordinates
     * boundaries of the structure have changed.
     */
    public void coordinatesChanged();

    /**
     * Signals that a selection was added, removed or changed.
     */
    public void selectionChanged();

    public void zoomChanged();
}
