package net.sf.adatagenerator.ex.cdc1.api;

import java.io.Serializable;

public interface Cdc1Record extends Cloneable, Serializable {

    public enum RecordField {

        DOB(String.class), FirstName(String.class), LastName(String.class), MiddleName(String.class), MomFirst(String.class), MomLast(String.class), MomMaiden(String.class), MomMiddle(String.class), Sex(String.class), Suffix(String.class), VacCode(String.class), VacDate(String.class), VacMfr(String.class), VacName(String.class);

        public final Class<?> type;

        private RecordField(Class<?> type) {
            this.type = type;
        }
    }

    Object clone() throws CloneNotSupportedException;

    /**
	 * Copies all values from <code>r</code> to this record. If <code>r</code>
	 * is null or <code>r</code> is this record, this record will be left
	 * unchanged.
	 */
    void copy(Cdc1Record r);

    /**
	 * @return the lastName
	 */
    String getLastName();

    /**
	 * @param lastName
	 *            the lastName to set
	 */
    void setLastName(String lastName);

    /**
	 * @return the firstName
	 */
    String getFirstName();

    /**
	 * @param firstName
	 *            the firstName to set
	 */
    void setFirstName(String firstName);

    /**
	 * @return the middleName
	 */
    String getMiddleName();

    /**
	 * @param middleName
	 *            the middleName to set
	 */
    void setMiddleName(String middleName);

    /**
	 * @return the suffix
	 */
    String getSuffix();

    /**
	 * @param suffix
	 *            the suffix to set
	 */
    void setSuffix(String suffix);

    /**
	 * @return the DOB
	 */
    String getDOB();

    /**
	 * @param DOB
	 *            the DOB to set
	 */
    void setDOB(String DOB);

    /**
	 * @return the sex
	 */
    String getSex();

    /**
	 * @param sex
	 *            the sex to set
	 */
    void setSex(String sex);

    /**
	 * @return the momMaiden
	 */
    String getMomMaiden();

    /**
	 * @param momMaiden
	 *            the momMaiden to set
	 */
    void setMomMaiden(String momMaiden);

    /**
	 * @return the momLast
	 */
    String getMomLast();

    /**
	 * @param momLast
	 *            the momLast to set
	 */
    void setMomLast(String momLast);

    /**
	 * @return the momFirst
	 */
    String getMomFirst();

    /**
	 * @param momFirst
	 *            the momFirst to set
	 */
    void setMomFirst(String momFirst);

    /**
	 * @return the momMiddle
	 */
    String getMomMiddle();

    /**
	 * @param momMiddle
	 *            the momMiddle to set
	 */
    void setMomMiddle(String momMiddle);

    /**
	 * @return the vacName
	 */
    String getVacName();

    /**
	 * @param vacName
	 *            the vacName to set
	 */
    void setVacName(String vacName);

    /**
	 * @return the vacCode
	 */
    String getVacCode();

    /**
	 * @param vacCode
	 *            the vacCode to set
	 */
    void setVacCode(String vacCode);

    /**
	 * @return the vacMfr
	 */
    String getVacMfr();

    /**
	 * @param vacMfr
	 *            the vacMfr to set
	 */
    void setVacMfr(String vacMfr);

    /**
	 * @return the vacDate
	 */
    String getVacDate();

    /**
	 * @param vacDate
	 *            the vacDate to set
	 */
    void setVacDate(String vacDate);
}
