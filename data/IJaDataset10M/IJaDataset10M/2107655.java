package org.dcm4che.srom;

import org.dcm4che.data.Dataset;
import java.util.Date;

/**
 * The <code>Patient</code> interface represents some of the fields of the
 * <i>DICOM Patient Module</i>.
 * 
 * @author  gunter.zeilinger@tiani.com
 * @version 1.0
 *
 * @see "DICOM Part 3: Information Object Definitions,
 * Annex C.7.1.1 Patient Module"
 */
public interface Patient {

    /**
     * Inner static class that represents a enumeration of 
     * the patient sex.
     */
    public static final class Sex {

        private String type;

        /** Female patient sex. */
        public static final Sex FEMALE = new Sex("F");

        /** Male patient sex. */
        public static final Sex MALE = new Sex("M");

        /** 
         * Other patient sex. 
         * Used if sex of patient is unknown or something else that
         * male or female.
         */
        public static final Sex OTHER = new Sex("O");

        /**
         * Constructor of patient sex.
         * 
         * @param type  a String character code specifying the patient sex.
         *              Allowed values are "M" for male "F" for female
         *              "O" for other.
         */
        private Sex(String type) {
            this.type = type;
        }

        /**
         * Returns the text representation of patient sex.
         */
        public String toString() {
            return type;
        }

        /**
         * Returns the type save patient sex value of a specified String.
         * 
         * @param s  the patient sex as string.
         * @throws IllegalArgumentException  if parameter <code>s</code>
         * is not "M", "F" or "O".
         */
        public static Sex valueOf(String s) {
            if (s == null || s.length() == 0) return null;
            if (s.length() == 1) switch(s.charAt(0)) {
                case 'F':
                    return FEMALE;
                case 'M':
                    return MALE;
                case 'O':
                    return OTHER;
            }
            throw new IllegalArgumentException(s);
        }
    }

    /**
     * Returns the DICOM <i>Patient ID</i>.
     * <br>DICOM Tag: <code>(0010,0020)</code>
     *
     * @return  the Patient ID.
     */
    public String getPatientID();

    /**
     * Returns the DICOM <i>Patient Name</i>.
     * <br>DICOM Tag: <code>(0010,0010)</code>
     *
     * @return  the Patient Name.
     */
    public String getPatientName();

    /**
     * Returns the DICOM <i>Patient's Sex</i>.
     * <br>DICOM Tag: <code>(0010,0040)</code>
     *
     * @return  the Patient's Sex.
     */
    public Sex getPatientSex();

    /**
     * Returns the DICOM <i>Patient's Birth Date</i>.
     * <br>DICOM Tag: <code>(0010,0030)</code>
     * 
     * @return  the Patient's Birth Date.
     */
    public Date getPatientBirthDate();

    public void toDataset(Dataset ds);
}
