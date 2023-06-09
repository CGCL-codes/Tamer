package org.jfree.chart.entity;

import java.awt.Shape;
import org.jfree.chart.HashUtilities;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.util.ObjectUtilities;

/**
 * An entity to represent the labels on a {@link CategoryAxis}.
 *
 * @since 1.0.3
 */
public class CategoryLabelEntity extends TickLabelEntity {

    /** The category key. */
    private Comparable key;

    /**
     * Creates a new entity.
     *
     * @param key  the category key.
     * @param area  the hotspot.
     * @param toolTipText  the tool tip text.
     * @param urlText  the URL text.
     */
    public CategoryLabelEntity(Comparable key, Shape area, String toolTipText, String urlText) {
        super(area, toolTipText, urlText);
        this.key = key;
    }

    /**
     * Returns the category key.
     *
     * @return The category key.
     */
    public Comparable getKey() {
        return this.key;
    }

    /**
     * Tests this instance for equality with an arbitrary object.
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CategoryLabelEntity)) {
            return false;
        }
        CategoryLabelEntity that = (CategoryLabelEntity) obj;
        if (!ObjectUtilities.equal(this.key, that.key)) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Returns a hash code for this instance.
     *
     * @return A hash code.
     */
    public int hashCode() {
        int result = super.hashCode();
        result = HashUtilities.hashCode(result, this.key);
        return result;
    }

    /**
     * Returns a string representation of this entity.  This is primarily
     * useful for debugging.
     *
     * @return A string representation of this entity.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("CategoryLabelEntity: ");
        buf.append("category=");
        buf.append(this.key);
        buf.append(", tooltip=" + getToolTipText());
        buf.append(", url=" + getURLText());
        return buf.toString();
    }
}
