package org.rococoa.cocoa;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.rococoa.Rococoa;
import org.rococoa.cocoa.foundation.NSArray;
import org.rococoa.cocoa.foundation.NSNumber;
import org.rococoa.test.RococoaTestCase;

public class NSArrayTest extends RococoaTestCase {

    @Test
    public void test() {
        NSArray array = NSArray.CLASS.arrayWithObjects(NSNumber.CLASS.numberWithInt(42), NSNumber.CLASS.numberWithInt(64));
        assertEquals(2, array.count());
        NSNumber second = Rococoa.cast(array.objectAtIndex(1), NSNumber.class);
        assertEquals(64, second.intValue());
    }
}
