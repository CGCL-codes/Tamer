package de.mizi.ui.statusbox;

import java.awt.Graphics;

/**
 * This class represents a StatusBoxContent object that consists
 * of a label only.
 * @author mizi
 *
 */
public class LabelOnlyContent extends StatusBoxContent {

    protected String label;

    /**
	 * Constructs a concrete StatusBoxContent object with the given position
	 * relative to the position of the StatusBox object this object belongs to.
	 * This StatusBoxContent object has a single label only.
	 * @param relativeX the relative x position
	 * @param relativeY the relative y position
	 * @param label the label
	 */
    public LabelOnlyContent(int relativeX, int relativeY, String label) {
        super(relativeX, relativeY);
        this.label = label;
    }

    /**
	 * Get the label of this StatusBoxContent object.
	 * @return the label
	 */
    public String getLabel() {
        return label;
    }

    /**
	 * Set the label of this StatusBoxContent object.
	 * @param label the new label
	 */
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void render(Graphics g, int yOffset) {
        g.drawString(label, relativeX, relativeY - yOffset);
    }
}
