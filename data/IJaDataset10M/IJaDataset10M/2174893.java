package tw.edu.shu.im.iccio;

/**
 * N-component LUT-based output profile.
 */
public class ICCOutputProfileLut extends ICCProfile {

    public ICCOutputProfileLut() {
        super();
    }

    public ICCOutputProfileLut(ICCInputProfileLut copy) throws ICCProfileException {
        super(copy);
    }

    public ICCOutputProfileLut(ICCProfileHeader header, ICCProfileTagTable tagTable) {
        super(header, tagTable);
    }

    /**
	 * Return the required tag type strings for this profile class.
	 * Override this method in the derived classes of ICCProfile for proper validation.
	 * @return String array of required tag types.
	 */
    protected String[] requiredTagStrings() {
        String[] requiredTags = new String[] { "desc", "profileDescriptionTag", "wtpt", "mediaWhitePointTag", "cprt", "copyrightTag", "chad", "chromaticAdaptationTag", "A2B0", "AToB0Tag", "B2A0", "BToA0Tag", "A2B1", "AToB1Tag", "B2A1", "BToA1Tag", "A2B2", "AToB2Tag", "B2A2", "BToA2Tag", "gamt", "gamutTag" };
        return requiredTags;
    }
}
