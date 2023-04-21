package wesodi.entities.transi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * 
 * @author Etienne Bottke
 * @date 05.03.2009
  */
public class ServerResponse implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8749181969625305951L;

    /** The results. */
    private HashMap<String, Object> results = new HashMap<String, Object>(0);

    /** The errors. */
    private ArrayList<String> errors = new ArrayList<String>(0);

    /**
	 * Instantiates a new server response.
	 */
    public ServerResponse() {
    }

    /**
	 * Gets the results.
	 * 
	 * @return the results
	 */
    public HashMap<String, Object> getResults() {
        return results;
    }

    /**
	 * Sets the results.
	 * 
	 * @param results
	 *            the results
	 */
    @SuppressWarnings("unused")
    private void setResults(HashMap<String, Object> results) {
        this.results = results;
    }

    /**
	 * Gets the errors.
	 * 
	 * @return the errors
	 */
    public ArrayList<String> getErrors() {
        return errors;
    }

    /**
	 * Sets the errors.
	 * 
	 * @param errors
	 *            the new errors
	 */
    @SuppressWarnings("unused")
    private void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }

    /**
	 * Adds the error.
	 * 
	 * @param errorMessage
	 *            the error message
	 */
    public void addError(String errorMessage) {
        this.errors.add(errorMessage);
    }

    /**
	 * Adds the result.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
    public void addResult(String key, Object value) {
        this.results.put(key, value);
    }

    public void addErrors(ArrayList<String> additionalErrors) {
        Iterator<String> it = additionalErrors.iterator();
        while (it.hasNext()) {
            String additional = (String) it.next();
            this.errors.add(additional);
        }
    }

    public boolean equals(ServerResponse response) {
        boolean differences = false;
        System.out.println(this.getErrors() + " == " + response.getErrors() + " --> " + this.getErrors().equals(response.getErrors()));
        System.out.println(this.getResults() + " == " + response.getResults() + " -->" + this.getResults().equals(response.getResults()));
        if (!this.getErrors().equals(response.getErrors())) {
            differences = true;
        } else if (!this.getResults().equals(response.getResults())) {
            differences = true;
        }
        return !differences;
    }
}
