package cz.cvut.phone.pp.constants;

/**
 * This class is just a list with constants
 * @author Jiří Havelka
 */
public class LanguageConstants {

    /**
     * Value of tax (number, not in %)... for example if tax is 10%, put "0.1"
     */
    public static final double DPH = 0.19;

    /**
     * Maximum length of String describing the type of service.
     */
    public static final int MAX_SERVICETYPE_LENGTH = 256;

    /**
     * Maximum length of String with "phone number" 
     * (can be also something like "data.provider.com").
     */
    public static final int MAX_PHONENR_LENGTH = 50;

    /**
     * Code representing an unrecognized service.
     */
    public static final int SERVICECODE_UNKNOWN = 0;

    /**
     * When there is at least one of these Strings in service type, 
     * it is propably an SMS.
     */
    public static final String[] DEFINE_SMS = { "SMS", "text", "zpráv" };

    /**
     * Code representing an SMS.
     */
    public static final int SERVICECODE_SMS = 200;

    /**
     * When there is at least one of these Strings in service type, 
     * it is propably a call.
     */
    public static final String[] DEFINE_CALL = { "hovor", "volání", "mobilní", "pevné" };

    /**
     * Code representing a call.
     */
    public static final int SERVICECODE_CALL = 100;

    /**
     * Name of the logger.
     */
    public static final String LOGGER_NAME = "phoneis.phoneparser";
}
