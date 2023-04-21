package com.volantis.styling.unit.engine.matchers.constraints;

import com.volantis.styling.impl.engine.matchers.constraints.Equals;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraint;

/**
 * Test contains {@link com.volantis.styling.impl.engine.matchers.constraints.Equals}.
 */
public class EqualsTestCase extends ValueComparatorTestCaseAbstract {

    protected ValueConstraint createConstraint(String value) {
        return new Equals(value);
    }
}
