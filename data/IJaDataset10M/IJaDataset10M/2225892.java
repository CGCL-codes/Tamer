package org.azrul.liquidframe.domain;

import java.util.Map;

/**
 *
 * @author Azrul Hasni MADISA
 */
public class AlwaysTrueCondition implements Condition {

    /** Creates a new instance of AlwaysTrueCondition */
    public AlwaysTrueCondition() {
    }

    public boolean run(Map<String, Object> params, Token token) {
        return true;
    }
}
