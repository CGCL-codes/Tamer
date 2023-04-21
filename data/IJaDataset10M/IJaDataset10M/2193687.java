package org.protune.api;

import java.text.ParseException;
import org.protune.core.Action;
import org.protune.core.FilteredPolicy;
import org.protune.core.Goal;
import org.protune.core.Notification;
import test.org.protune.DummyAction;

public class MapperAdapter implements Mapper {

    public Action parseAction(String s) throws ParseException {
        return new DummyAction(s);
    }

    public FilteredPolicy parseFilteredPolicy(String s) throws ParseException {
        try {
            return new FilteredPolicy(s);
        } catch (LoadTheoryException e) {
            throw new ParseException(null, 0);
        }
    }

    public String toPrologRepresentation(FilteredPolicy fp) {
        return fp.toString();
    }

    public String toPrologRepresentation(Notification n) {
        return n.getAction().toString();
    }

    public String toPrologRepresentation(Goal g) {
        return g.toString();
    }
}
