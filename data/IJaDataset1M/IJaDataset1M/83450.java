package org.ascape.util.vis;

import java.awt.Color;

/**
 * An interface for classes which define the Relation rendering of an entity
 * relation graphical view.
 *
 * @author    Roger Critchlow
 * @version   2.9
 * @history   2.9 Moved into main Ascape.
 * @history   1.0 (Class version) 06/05/01 initial definition
 * @since     1.0
 */
public interface RelationFeature {

    /**
     * Answers whether this relation includes a link from the source to the
     * destination.
     *
     * @param source       the object on the domain side of the relation.
     * @param destination  the object on the range side of the relation.
     * @return             a boolean indicating if the relation is included
     */
    public boolean includesRelation(Object source, Object destination);

    /**
     * Get the name of this relation.
     *
     * @return   the name
     */
    public String getName();

    /**
     * Gets the lineWidth for the RelationFeature object.
     *
     * @param source       parameter
     * @param destination  parameter
     * @return             the lineWidth
     */
    public double getLineWidth(Object source, Object destination);

    /**
     * Gets the lineColor for the RelationFeature object.
     *
     * @param source       parameter
     * @param destination  parameter
     * @return             the lineColor
     */
    public Color getLineColor(Object source, Object destination);

    /**
     * Gets the glyphPosition for the RelationFeature object.
     *
     * @param source       parameter
     * @param destination  parameter
     * @return             the glyphPosition
     */
    public double getGlyphPosition(Object source, Object destination);

    /**
     * Gets the glyphColor for the RelationFeature object.
     *
     * @param source       parameter
     * @param destination  parameter
     * @return             the glyphColor
     */
    public Color getGlyphColor(Object source, Object destination);
}
