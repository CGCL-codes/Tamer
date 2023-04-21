package sf.sssim.ui.connector;

/**
 * A Speakon connector.
 * 
 * @author Chris Howie
 */
public class SpeakonConnector extends GenderConnector {

    /**
	 * Creates a new Speakon connector.
	 * 
	 * @param gender
	 *            the gender of the new connector, which must be <CODE>
	 *            GENDER_MALE</CODE> or <CODE>GENDER_FEMALE</CODE>.
	 * @throws IllegalArgumentException
	 *             if <CODE>gender</CODE> is nethier <CODE>GENDER_MALE
	 *             </CODE> nor <CODE>GENDER_FEMALE</CODE>.
	 */
    public SpeakonConnector(int gender) {
        super(gender, true);
    }
}
