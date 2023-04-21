package net.sf.parc.pipeline.support;

import java.util.ArrayList;
import java.util.List;
import net.sf.parc.pipeline.Record;

/**
 * Helper class for implementing Record interface.
 * 
 * @author pmckinstry
 */
public abstract class AbstractRecord implements Record {

    private List<Error> errors = new ArrayList<Error>();

    public void addError(Error error) {
        errors.add(error);
    }

    public List<Error> getErrors() {
        return errors;
    }

    public boolean isErrored() {
        return !isValid();
    }

    public boolean isValid() {
        return errors.size() == 0;
    }
}
