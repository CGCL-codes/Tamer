package org.openhealthexchange.openpixpdq.ihe.pdq;

import java.util.List;
import org.openhealthexchange.openpixpdq.data.Patient;
import org.openhealthexchange.openpixpdq.ihe.IPdSupplier;

/**
 * This class holds the Patient Demographics Query Result.
 * 
 * @author Wenzhi Li
 * @version 1.0, Dec 15, 2008
 * @see PdqQuery
 * @see IPdSupplier
 */
public class PdqResult {

    /** A list of list of <code>Patient</code>.  The first list is a list 
        of different logic patients, while the second list is a list of 
        the same patient in different domain systems. */
    private List<List<Patient>> patients;

    /** The continue reference number for subsequent PDQ query. */
    private String continuationPointer;

    /**
	 * Constructor.
	 * 
	 * @param patients A list of list of <code>Patient</code>.  
	 * The first list is a list of different logic patients, 
	 * while the second list is a list of the same patient in 
	 * different domain systems.
	 */
    public PdqResult(List<List<Patient>> patients) {
        super();
        this.patients = patients;
    }

    /**
	 * Gets the list of patients of this search result.
	 * 
	 * @return a list of list of <code>Patient</code>.  
	 * The first list is a list of different logic patients, 
	 * while the second list is a list of the same patient in 
	 * different domain systems.
	 */
    public List<List<Patient>> getPatients() {
        return patients;
    }

    /**
	 * Sets a list of patients for the search result.
	 * 
	 * @param patients a list of list of <code>Patient</code> to set.
	 * The first list is a list of different logic patients, 
	 * while the second list is a list of the same patient in 
	 * different domain systems.
	 */
    public void setPatients(List<List<Patient>> patients) {
        this.patients = patients;
    }

    /**
	 * Gets the continuation pointer for this PDQ search.
	 * 
	 * @return the continuationPointer
	 */
    public String getContinuationPointer() {
        return continuationPointer;
    }

    /**
	 * Sets the continuation pointer for this PDQ search.
	 * 
	 * @param continuationPointer the continuation pointer to set
	 */
    public void setContinuationPointer(String continuationPointer) {
        this.continuationPointer = continuationPointer;
    }
}
