package net.sf.ediknight.common.edi.directory;

import java.util.ArrayList;
import java.util.List;
import net.sf.ediknight.edi.directory.ElementNode;
import net.sf.ediknight.edi.directory.Segment;

/**
 *
 */
public final class SegmentImpl implements Segment {

    private static final long serialVersionUID = 2073765751191590008L;

    private String id;

    private String name;

    private String description;

    private String mode;

    private boolean deprecated;

    private List<ElementNode> elementNodes = new ArrayList<ElementNode>();

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        if (name == null) {
            name = Names.toSegmentName(this);
        }
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param descr The description to set.
     */
    public void setDescription(String descr) {
        if (descr == null) {
            throw new IllegalArgumentException();
        }
        description = Character.toUpperCase(descr.charAt(0)) + descr.substring(1).toLowerCase();
        name = null;
    }

    /**
     * @param descr the description to set
     */
    public void appendDescription(String descr) {
        if (descr == null) {
            throw new IllegalArgumentException();
        }
        if (this.description == null) {
            this.description = descr;
        } else {
            this.description += " " + descr;
        }
        name = null;
    }

    /**
     * @return true if this is an interactive segment
     */
    public boolean isInteractiveMode() {
        return "I".equals(mode);
    }

    /**
     * @return true if this is a batch mode segment
     */
    public boolean isBatchMode() {
        return !"I".equals(mode);
    }

    /**
     * @return the segment mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode the segment mode (BATCH or INTERACTIVE)
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return true if this segment is deprecated
     */
    public boolean isDeprecated() {
        return deprecated;
    }

    /**
     * @param deprecated true if this segment is deprecated
     */
    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    /**
     * Add an element to this segment.
     *
     * @param elementNode an element node
     */
    public void addElement(ElementNodeImpl elementNode) {
        elementNodes.add(elementNode);
    }

    /**
     * @return the elements of this segment
     */
    public List<ElementNode> getElementNodes() {
        return elementNodes;
    }

    /**
     * Throws an {@link IllegalStateException} if the segment is not completely
     * defined.
     */
    public void checkCompleteness() {
        if (id == null) {
            throw new IllegalStateException("Segment has no id");
        }
        if (description == null) {
            throw new IllegalStateException("Segment has no description");
        }
        if (mode == null) {
            throw new IllegalStateException("Invalid mode");
        }
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Segment other) {
        return id.compareTo(other.getId());
    }

    /**
     * {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "segment (" + getId() + ", " + getName() + ")";
    }
}
