package au.gov.nla.aons.constants;

import au.gov.nla.aons.format.domain.AonsFormat;

/**
 * This is the list of possible values an {@link AonsFormat} object may have for
 * it's risk assessment state {@link AonsFormat#getState()}.
 * 
 * @author dlevy
 * 
 */
public class AonsFormatStates {

    /**
     * This value is given when an {@link AonsFormat} is first created
     */
    public static final String NO_RISK_ASSESSMENT = "No Risk Assessment";

    /**
     * This is the state value given when a risk assessment has been performed
     */
    public static final String RISK_ASSESSMENT_PERFORMED = "Risk Assessment Performed";

    /**
     * This is the state value given when a risk assessment has expired
     */
    public static final String RISK_ASSESSMENT_EXPIRED = "Risk Assessment Expired";
}
