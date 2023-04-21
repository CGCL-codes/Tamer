package src.world.ship.components;

import java.io.IOException;
import src.exceptions.MultiplayerException;
import src.multiplayer.xmlParser.EleconicsXmlParser;
import src.multiplayer.xmlProcessor.XmlProcessor;

/**
 * Class 'Stealth' manages the Ship Component that allows
 * Ships to conceal their presence to nearby Ships.  This
 * Component functions by reducing the effective Scan range
 * of enemy Ships.
 * 
 * @author Forrest Dillaway
 */
public class Stealth extends Component implements Comparable {

    /**
     * The Xml element node tag name
     */
    public static final String ELEMENT_NAME = "Stealth";

    public static final String REDUCTION_FACTOR = "ReductionFactor";

    private static final String SMALL_NAME = "Small Stealth";

    private static final String MEDIUM_NAME = "Medium Stealth";

    private static final String LARGE_NAME = "Large Stealth";

    private static final String SPECIALIZED_NAME = "Specialized Stealth";

    private static final String MISSILE_NAME = "Missile Stealth";

    private static final float INITIAL_SMALL_MAX_MASS = 60.0F;

    private static final float INITIAL_SMALL_MAX_SPACE = 20.0F;

    private static final float INITIAL_SMALL_MAX_INTERNAL_HIT_POINTS = 5.0F;

    private static final float INITIAL_SMALL_MAX_EXTERNAL_HIT_POINTS = 7.5F;

    private static final float INITIAL_SMALL_MAX_REDUCTION_FACTOR = 0.94f;

    private static final float INITIAL_MEDIUM_MAX_MASS = 180.0F;

    private static final float INITIAL_MEDIUM_MAX_SPACE = 50.0F;

    private static final float INITIAL_MEDIUM_MAX_INTERNAL_HIT_POINTS = 10.0F;

    private static final float INITIAL_MEDIUM_MAX_EXTERNAL_HIT_POINTS = 15.0F;

    private static final float INITIAL_MEDIUM_MAX_REDUCTION_FACTOR = 0.87f;

    private static final float INITIAL_LARGE_MAX_MASS = 360.0F;

    private static final float INITIAL_LARGE_MAX_SPACE = 100.0F;

    private static final float INITIAL_LARGE_MAX_INTERNAL_HIT_POINTS = 15.0F;

    private static final float INITIAL_LARGE_MAX_EXTERNAL_HIT_POINTS = 22.5F;

    private static final float INITIAL_LARGE_MAX_REDUCTION_FACTOR = 0.8f;

    /**
     * Percentage Reduction to Effective Scan Range
     */
    private float reductionFactor;

    /**
     * Max Percentage Reduction to Effective Scan Range
     */
    private float reductionFactorMax;

    /**
     * Construct a Stealth of the specified Weapon size
     * @param stealthSize The Weapon size of the Stealth
     */
    public Stealth(int stealthSize) {
        super(stealthSize);
    }

    /**
     * @see src.multiplayer.XmlReady#XmlReady(EleconicsXmlParser)
     */
    public Stealth(EleconicsXmlParser parser) throws MultiplayerException, IOException {
        super(parser);
    }

    /**
     * Return the Xml element node name
     * @return The Xml element node name
     */
    public String elementName() {
        return ELEMENT_NAME;
    }

    /**
     * @see src.multiplayer.XmlReady#ToXml(XmlProcessor)
     */
    public void ToXml(XmlProcessor data) {
        super.ToXml(data);
    }

    /**
     * Stream to Xml the Component-specific elements
     * @param data The XmlProcessor to convert the Component
     *    elements to an Xml Stream
     */
    protected void ToPersonalXml(XmlProcessor data) {
        data.XmlNode(REDUCTION_FACTOR, Float.toString(reductionFactor));
    }

    /**
     * Read from the Xml Stream
     * @see src.multiplayer.XmlReady#ToXml(EleconicsXmlParser)
     */
    public void FromXml(EleconicsXmlParser parser) throws IOException, MultiplayerException {
        super.FromXml(parser);
    }

    /**
     * @see src.multiplayer.XmlReady#FromPersonalXml(multiplayer.EleconicsXmlParser)
     */
    protected void FromPersonalXml(EleconicsXmlParser parser) throws IOException, MultiplayerException {
        parser.openNode(REDUCTION_FACTOR);
        reductionFactor = parser.readFloat();
        parser.closeNode(REDUCTION_FACTOR);
        parser.closeNode(ELEMENT_NAME);
    }

    public float getReductionFactor() {
        return reductionFactor;
    }

    /**
     * Set the values for the small Component version
     */
    protected void setSmallSize() {
        setDetails(INITIAL_SMALL_MAX_MASS, INITIAL_SMALL_MAX_SPACE, INITIAL_SMALL_MAX_INTERNAL_HIT_POINTS, INITIAL_SMALL_MAX_EXTERNAL_HIT_POINTS);
        setPersonalDetails(INITIAL_SMALL_MAX_REDUCTION_FACTOR);
    }

    /**
     * Set the values for the medium Component version
     */
    protected void setMediumSize() {
        setDetails(INITIAL_MEDIUM_MAX_MASS, INITIAL_MEDIUM_MAX_SPACE, INITIAL_MEDIUM_MAX_INTERNAL_HIT_POINTS, INITIAL_MEDIUM_MAX_EXTERNAL_HIT_POINTS);
        setPersonalDetails(INITIAL_MEDIUM_MAX_REDUCTION_FACTOR);
    }

    /**
     * Set the values for the large Component version
     */
    protected void setLargeSize() {
        setDetails(INITIAL_LARGE_MAX_MASS, INITIAL_LARGE_MAX_SPACE, INITIAL_LARGE_MAX_INTERNAL_HIT_POINTS, INITIAL_LARGE_MAX_EXTERNAL_HIT_POINTS);
        setPersonalDetails(INITIAL_LARGE_MAX_REDUCTION_FACTOR);
    }

    /**
     * Set the parameters for the Communication Component
     * @param reductionFactorMax The initial max reduction to scanning
     *    of this Ship.
     */
    protected void setPersonalDetails(float percentReductionMax) {
        this.reductionFactorMax = percentReductionMax;
        this.reductionFactor = percentReductionMax;
    }

    /**
     * @return Returns the reductionFactorMax.
     */
    public float getReductionFactorMax() {
        return reductionFactorMax;
    }

    /**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(Object o) {
        Stealth s = (Stealth) o;
        if (reductionFactorMax < s.reductionFactorMax) {
            return -1;
        } else if (reductionFactorMax == s.reductionFactorMax) {
            return 0;
        }
        return 1;
    }

    /**
	 * @see src.world.ship.components.Component#missileComponentDisplayName()
	 */
    protected String missileComponentDisplayName() {
        return MISSILE_NAME;
    }

    /**
	 * @see src.world.ship.components.Component#specializedComponentDisplayName()
	 */
    protected String specializedComponentDisplayName() {
        return SPECIALIZED_NAME;
    }

    /**
	 * @see src.world.ship.components.Component#largeComponentDisplayName()
	 */
    protected String largeComponentDisplayName() {
        return LARGE_NAME;
    }

    /**
	 * @see src.world.ship.components.Component#mediumComponentDisplayName()
	 */
    protected String mediumComponentDisplayName() {
        return MEDIUM_NAME;
    }

    /**
	 * @see src.world.ship.components.Component#smallComponentDisplayName()
	 */
    protected String smallComponentDisplayName() {
        return SMALL_NAME;
    }

    /**
     * @see src.world.ship.components.Component#getDescription()
     */
    public String getDescription() {
        StringBuffer returnString = new StringBuffer();
        String newLine = System.getProperty("line.separator");
        returnString.append("Name: ");
        returnString.append(getName());
        returnString.append(newLine);
        returnString.append("Mass: ");
        returnString.append(getMass());
        returnString.append(newLine);
        returnString.append("Space: ");
        returnString.append(getSpace());
        returnString.append(newLine);
        returnString.append("Internal Hit Points: ");
        returnString.append(getInternalHitPoints());
        returnString.append(newLine);
        returnString.append("External Hit Points: ");
        returnString.append(getExternalHitPoints());
        returnString.append(newLine);
        returnString.append("Reduction Factor: ");
        returnString.append(getReductionFactor());
        returnString.append(newLine);
        return returnString.toString();
    }

    /**
	 * Returns a copy of the component, without any deterioration
	 * @return the copy
	 */
    public Component getCopy() {
        return new Stealth(this.componentSize);
    }
}
