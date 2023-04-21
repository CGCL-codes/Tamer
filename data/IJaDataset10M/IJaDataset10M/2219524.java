package org.mobicents.protocols.ss7.isup.message.parameter;

/**
 * Start time:12:30:35 2009-07-23<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public interface ConnectedNumber extends ISUPParameter, NAINumber {

    public static final int _PARAMETER_CODE = 0x21;

    /**
	 * numbering plan indicator indicator value. See Q.763 - 3.9d
	 */
    public static final int _NPI_ISDN = 1;

    /**
	 * numbering plan indicator indicator value. See Q.763 - 3.9d
	 */
    public static final int _NPI_DATA = 3;

    /**
	 * numbering plan indicator indicator value. See Q.763 - 3.9d
	 */
    public static final int _NPI_TELEX = 4;

    /**
	 * numbering plan indicator indicator value. See Q.763 - 3.9d
	 */
    public static final int _NPI_PRIVATE = 5;

    /**
	 * address presentation restricted indicator indicator value. See Q.763 -
	 * 3.10e
	 */
    public static final int _APRI_ALLOWED = 0;

    /**
	 * address presentation restricted indicator indicator value. See Q.763 -
	 * 3.10e
	 */
    public static final int _APRI_RESTRICTED = 1;

    /**
	 * address presentation restricted indicator indicator value. See Q.763 -
	 * 3.10e
	 */
    public static final int _APRI_NOT_AVAILABLE = 2;

    /**
	 * address presentation restricted indicator indicator value. See Q.763 -
	 * 3.16d
	 */
    public static final int _APRI_SPARE = 3;

    /**
	 * screening indicator indicator value. See Q.763 - 3.10f
	 */
    public static final int _SI_USER_PROVIDED_VERIFIED_PASSED = 1;

    /**
	 * screening indicator indicator value. See Q.763 - 3.10f
	 */
    public static final int _SI_NETWORK_PROVIDED = 3;

    public int getNumberingPlanIndicator();

    public void setNumberingPlanIndicator(int numberingPlanIndicator);

    public int getAddressRepresentationRestrictedIndicator();

    public void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator);

    public int getScreeningIndicator();

    public void setScreeningIndicator(int screeningIndicator);
}
