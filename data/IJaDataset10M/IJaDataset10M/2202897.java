package de.suse.swamp.core.data.datatypes;

import java.util.*;
import org.apache.jcs.access.exception.*;
import de.suse.swamp.core.container.SecurityManager;
import de.suse.swamp.core.data.*;
import de.suse.swamp.core.util.*;
import de.suse.swamp.util.*;

/**
 * This Databit represents a person (login or email) 
 * or a list of them
 *
 * @author Thomas Schmidt
 *
 */
public class personDatabit extends Databit {

    public personDatabit(String name, String desc, String value, Integer state) throws Exception {
        super(name, desc, value, state.intValue());
        setModified(true);
    }

    /**
     * Checks if the given value v fits this Databit's Type.
     * 
     * @param v
     */
    public String checkDataType(String v) throws InvalidArgumentException, Exception {
        if (!v.equals("") && !checkedValues.contains(v)) {
            StringTokenizer st = new StringTokenizer(v, ",");
            while (st.hasMoreTokens()) {
                String tokenvalue = st.nextToken().trim();
                if (tokenvalue.indexOf('@') < 0) {
                    try {
                        SecurityManager.getUser(tokenvalue);
                    } catch (UnknownElementException e) {
                        throw new InvalidArgumentException(i18n.tr("Unknown User: ") + tokenvalue + i18n.tr(" in field ") + this.getName());
                    } catch (Exception e) {
                        Logger.ERROR("Exception from backend: " + e.getMessage());
                        e.printStackTrace();
                        throw e;
                    }
                } else {
                    if (!(org.apache.commons.validator.EmailValidator.getInstance().isValid(tokenvalue))) {
                        throw new InvalidArgumentException(tokenvalue + i18n.tr(" is not a valid E-Mail address."));
                    }
                }
            }
            checkedValues.add(v);
        }
        return v;
    }
}
