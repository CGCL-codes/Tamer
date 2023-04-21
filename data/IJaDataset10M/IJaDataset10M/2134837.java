package org.epoline.impexp.jsf.businesslogic.dl;

import java.io.Serializable;

/**
 * Data holder object that stores info about a docCode and how it should be send
 * Used for creating mailitems in Mailbox.
 */
public class MailCreationData implements Serializable {

    private String code;

    private String descEN;

    private String descFR;

    private String descDE;

    private int mailType;

    public MailCreationData(String code, String descEN, String descFR, String descDE, int type) {
        super();
        this.code = code;
        this.descEN = descEN;
        this.descFR = descFR;
        this.descDE = descDE;
        this.mailType = type;
    }

    /**
	 * Returns the code.
	 * @return String
	 */
    public String getCode() {
        return code;
    }

    /**
	 * Returns the descDE.
	 * @return String
	 */
    public String getDescDE() {
        return descDE;
    }

    /**
	 * Returns the descEN.
	 * @return String
	 */
    public String getDescEN() {
        return descEN;
    }

    /**
	 * Returns the descFR.
	 * @return String
	 */
    public String getDescFR() {
        return descFR;
    }

    /**
	 * Returns the mailType.
	 * @return int
	 */
    public int getMailType() {
        return mailType;
    }

    /**
	 * Sets the code.
	 * @param code The code to set
	 */
    public void setCode(String code) {
        this.code = code;
    }

    /**
	 * Sets the descDE.
	 * @param descDE The descDE to set
	 */
    public void setDescDE(String descDE) {
        this.descDE = descDE;
    }

    /**
	 * Sets the descEN.
	 * @param descEN The descEN to set
	 */
    public void setDescEN(String descEN) {
        this.descEN = descEN;
    }

    /**
	 * Sets the descFR.
	 * @param descFR The descFR to set
	 */
    public void setDescFR(String descFR) {
        this.descFR = descFR;
    }

    /**
	 * Sets the mailType.
	 * @param mailType The mailType to set
	 */
    public void setMailType(int mailType) {
        this.mailType = mailType;
    }
}
