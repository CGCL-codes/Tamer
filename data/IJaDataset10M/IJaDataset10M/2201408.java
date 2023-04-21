package de.regnis.visuallayout.model;

/**
 * @author Thomas Singer
 */
public final class VLStretchValue {

    public static final VLStretchValue NO_STRETCH = new VLStretchValue("No stretch", "QHIGStretchValue.NO_STRETCH");

    public static final VLStretchValue STRETCH_IF_REQUIRED = new VLStretchValue("Stretch if necessary", "QHIGStretchValue.STRETCH_IF_REQUIRED");

    public static final VLStretchValue STRETCH_ON_RESIZE = new VLStretchValue("Stretch on resize", "QHIGStretchValue.STRETCH_ON_RESIZE");

    private final String uiName;

    private final String stringRepresentation;

    private VLStretchValue(String uiName, String stringRepresentation) {
        this.uiName = uiName;
        this.stringRepresentation = stringRepresentation;
    }

    public String toString() {
        return uiName;
    }

    public String getStringRepresentation() {
        return stringRepresentation;
    }
}
