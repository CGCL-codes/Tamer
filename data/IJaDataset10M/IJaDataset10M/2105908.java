package medisnap.telematik;

import medisnap.dblayer.*;

/**
 *
 * @author jan
 */
public interface TelematikPatient {

    public TelematikPatient createTelematikPatient(valuePatient vp);

    public boolean getGender();

    public String getFirstName();

    public String getLastName();
}
