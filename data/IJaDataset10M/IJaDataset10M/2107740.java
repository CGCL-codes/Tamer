package scamsoft.squadleader.rules;

/**
 * User: Andreas Mross
 * Date: Sep 14, 2003
 * Time: 11:17:51 AM
 */
public class FacingTest extends EnumerationTest {

    protected Object[] getValues() {
        return new Object[] { Facing.E, Facing.NE, Facing.NW, Facing.SE, Facing.SW, Facing.W };
    }
}
