package com.germinus.portlet.lms.model.predicates;

import com.germinus.portlet.lms.model.ScormDataModelMismatchException;
import com.germinus.portlet.lms.model.ScormReadOnlyException;
import com.germinus.portlet.lms.model.ValueAndProperties;

public interface WriteValuePredicate {

    public boolean checkValue(ValueAndProperties vap) throws ScormReadOnlyException, ScormDataModelMismatchException;
}
