package org.peertrust.modeler.policysystem.model.checks;

import java.util.ArrayList;
import java.util.Iterator;
import org.peertrust.modeler.policysystem.model.abtract.PSModelCheck;

public class PSModelCheckContainer implements PSModelCheck {

    protected ArrayList subchecks = new ArrayList();

    protected ArrayList failedCkecks = new ArrayList();

    /**
	 * Runs the subschecks and stop as soon as a subcheck fails
	 * @return true if all subchedk succeed otherwise false
	 * @see PSModelCheck.doCheck() 
	 */
    public boolean doCheck() {
        failedCkecks.clear();
        PSModelCheck check;
        for (Iterator it = subchecks.iterator(); it.hasNext(); ) {
            check = (PSModelCheck) it.next();
            if (!check.doCheck()) {
                failedCkecks.add(check);
            }
        }
        if (failedCkecks.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getMessage() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("failure Nr.:");
        buffer.append(failedCkecks.size());
        buffer.append("\n");
        PSModelCheck check;
        for (Iterator it = failedCkecks.iterator(); it.hasNext(); ) {
            check = (PSModelCheck) it.next();
            buffer.append(check.getMessage());
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public void includeSubCheck(PSModelCheck check) {
        if (check == null) {
            return;
        } else {
            subchecks.add(check);
        }
    }

    public PSModelCheck[] getSubChecks() {
        return (PSModelCheck[]) subchecks.toArray();
    }

    public PSModelCheck[] getFailedChecks() {
        return (PSModelCheck[]) failedCkecks.toArray();
    }
}
