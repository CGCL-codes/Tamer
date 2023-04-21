package org.callbackparams;

import java.util.Collection;
import org.callbackparams.combine.annotation.CallbackRecords;
import static org.junit.Assert.*;

/**
 * @author Henrik Kaipe
 */
@org.junit.Ignore
public class TestBoolReturnValues extends ReturnValueCase {

    interface BoolReturns extends CallbackToTest<Boolean> {

        boolean primitive();
    }

    @ParameterizedCallback
    BoolReturns returner;

    enum Case implements Expectation<BoolReturns> {

        ALL_TRUE(true, true, true, true, true, true, true, true), NULL_ALL(null, null, null), ONE_FALSE(false, true, true, false, true), ONE_TRUE(false, false, true, false, false), ALL_FALSE(false, false, false, false), NULL_WITH_TRUE(true, true, null, true, true, true), NULL_MIXED(true, null, false, true, null, false), EMPTY, NULL_ALONE(new Boolean[] { null }), ALL_ONE(true);

        Boolean[] values;

        private Case(Boolean... values) {
            this.values = values;
        }

        public void verify(BoolReturns callback) {
            if (name().startsWith("ALL_")) {
                assertEquals("primitive", values[0].booleanValue(), callback.primitive());
                assertEquals("object", values[0], callback.object());
            } else {
                if (false == name().startsWith("NULL_")) {
                    assertEquals("primitive", false, callback.primitive());
                }
                assertNull("object", callback.object());
            }
        }

        public Object[] returnValues() {
            return values;
        }
    }

    @CallbackRecords
    public static Collection<?> records() {
        return createRecords(Case.values(), BoolReturns.class);
    }
}
