package org.hip.kernel.bom.impl.test;

import static org.junit.Assert.*;
import org.hip.kernel.bom.impl.InObjectImpl;
import org.junit.Test;

/**
 *
 * @author Luthiger
 * Created: 14.10.2006
 */
public class InObjectImplTest {

    /**
	 * Test method for {@link org.hip.kernel.bom.impl.InObjectImpl#toString()}.
	 */
    @Test
    public void testToString() {
        InObjectImpl<String> lInObject1 = new InObjectImpl<String>(new String[] {});
        assertEquals("in 1", "IN (0)", lInObject1.toString());
        InObjectImpl<Integer> lInObject2 = new InObjectImpl<Integer>(new Integer[] { new Integer(14), new Integer(15), new Integer(22) });
        assertEquals("in 2", "IN (14, 15, 22)", lInObject2.toString());
        InObjectImpl<String> lInObject3 = new InObjectImpl<String>(new String[] { "Hallo", "14", "15", "22", "Adam" });
        assertEquals("in 3", "IN ('Hallo', '14', '15', '22', 'Adam')", lInObject3.toString());
    }

    @Test
    public void testToPrepared() {
        InObjectImpl<String> lInObject1 = new InObjectImpl<String>(new String[] {});
        assertEquals("in 1", "IN ()", lInObject1.toPrepared());
        InObjectImpl<Integer> lInObject2 = new InObjectImpl<Integer>(new Integer[] { new Integer(14), new Integer(15), new Integer(22) });
        assertEquals("in 2", "IN (?, ?, ?)", lInObject2.toPrepared());
        InObjectImpl<String> lInObject3 = new InObjectImpl<String>(new String[] { "Hallo", "14", "15", "22", "Adam" });
        assertEquals("in 3", "IN (?, ?, ?, ?, ?)", lInObject3.toPrepared());
    }
}
