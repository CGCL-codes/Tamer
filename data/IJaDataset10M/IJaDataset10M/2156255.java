package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.TextAssetReference;

public class SpanAttributes extends MCSAttributes {

    /**
     * The src property.
     */
    private TextAssetReference src;

    /**
     * @return the source
     */
    public TextAssetReference getSrc() {
        return src;
    }

    public void setSrc(TextAssetReference src) {
        this.src = src;
    }

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public SpanAttributes() {
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
        setTagName("span");
    }
}
