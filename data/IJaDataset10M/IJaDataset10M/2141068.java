package se.inera.ifv.medcert.services.converters;

import iso.v21090.dt.v1.II;
import org.dozer.CustomConverter;
import se.inera.ifv.v2.PatientType;

/**
 * @author Pär Wenåker
 *
 */
public class PatientSsnConverter implements CustomConverter {

    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
        if (sourceFieldValue == null) return null;
        if (sourceClass.equals(II.class)) {
            return ((II) sourceFieldValue).getExtension();
        } else if (sourceClass.equals(String.class)) {
            II ii = new II();
            ii.setRoot(getOIDPerson((String) sourceFieldValue));
            ii.setExtension((String) sourceFieldValue);
            return ii;
        }
        throw new IllegalArgumentException("Wrong Type:" + sourceClass);
    }

    /**
     * Method to determine if personnummer eller samordningsnummer.
     * Samordningsnummer börjar räkna dag på 60 och uppåt
     * @param ssn
     * @return If valid samordningsnumber return this oid otherwise always return personummer oid
     */
    private String getOIDPerson(String ssn) {
        try {
            if (Integer.parseInt(ssn.substring(6, 8)) > 60) {
                return "1.2.752.129.2.1.3.3";
            }
        } catch (NumberFormatException nfEx) {
        }
        return "1.2.752.129.2.1.3.1";
    }
}
