package com.volantis.mcs.protocols;

/**
 * This class should be removed and CanvasAttributes should be used instead.
 * Currently we make the canvas attributes available from this class.
 */
public class BodyAttributes extends MCSAttributes {

    /**
     * The canvas attributes.
     */
    private CanvasAttributes canvasAttributes;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public BodyAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {
        setTagName("canvas");
        canvasAttributes = null;
    }

    /**
     * Set the value of the canvasAttributes property.
     *
     * @param canvasAttributes The new value of the canvasAttributes property.
     */
    public void setCanvasAttributes(CanvasAttributes canvasAttributes) {
        this.canvasAttributes = canvasAttributes;
    }

    /**
     * Get the value of the canvasAttributes property.
     *
     * @return The value of the canvasAttributes property.
     */
    public CanvasAttributes getCanvasAttributes() {
        return canvasAttributes;
    }
}
