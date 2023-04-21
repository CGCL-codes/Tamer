package ru.goldenforests.forum.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author svv
 *
 */
public class SessionMessages implements Serializable {

    private final Collection errors = new ArrayList();

    private final Collection notices = new ArrayList();

    private final Map fieldErrors = new HashMap();

    private final Map savedParameters = new HashMap();

    public final void addError(String error) {
        this.errors.add(error);
    }

    public final void addNotice(String n) {
        this.notices.add(n);
    }

    public final void addFieldError(String field, String error) {
        this.fieldErrors.put(field, error);
    }

    public Collection getErrors() {
        return this.errors;
    }

    public Collection getNotices() {
        return this.notices;
    }

    public Map getFieldErrors() {
        return this.fieldErrors;
    }

    public Map getSavedParameters() {
        return this.savedParameters;
    }

    public boolean isEmpty() {
        return this.errors.isEmpty() && this.notices.isEmpty() && this.fieldErrors.isEmpty();
    }
}
