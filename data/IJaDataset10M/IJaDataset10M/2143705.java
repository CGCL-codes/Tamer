package tw.edu.shu.im.iccio;

/**
 * N-component LUT-based input profile.
 */
public class ICCInputProfileLut extends ICCProfile {

    public ICCInputProfileLut() {
        super();
    }

    public ICCInputProfileLut(ICCInputProfileLut copy) throws ICCProfileException {
        super(copy);
    }

    public ICCInputProfileLut(ICCProfileHeader header, ICCProfileTagTable tagTable) {
        super(header, tagTable);
    }

    /**
	 * Return the required tag type strings for this profile class.
	 * Override this method in the derived classes of ICCProfile for proper validation.
	 * @return String array of required tag types.
	 */
    protected String[] requiredTagStrings() {
        String[] requiredTags = new String[] { "desc", "profileDescriptionTag", "wtpt", "mediaWhitePointTag", "cprt", "copyrightTag", "chad", "chromaticAdaptationTag", "A2B0", "AToB0Tag" };
        return requiredTags;
    }
}
