package org.openymsg.legacy.test;

import static org.junit.Assert.assertFalse;
import java.util.Collection;
import java.util.HashSet;
import org.junit.Test;
import org.openymsg.legacy.network.ServiceType;

public class ServiceTypeTest {

    /**
     * Checks if every ServiceType int value is unique.
     */
    @Test
    public void testGetValue() {
        final Collection<Integer> checkedValues = new HashSet<Integer>();
        final ServiceType[] types = ServiceType.values();
        for (int i = 0; i < types.length; i++) {
            final Integer serviceTypeIntValue = Integer.valueOf(types[i].getValue());
            assertFalse("Non-unique ServiceType value " + types[i].getValue(), checkedValues.contains(serviceTypeIntValue));
            checkedValues.add(serviceTypeIntValue);
        }
    }
}
