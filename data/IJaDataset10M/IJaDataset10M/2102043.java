package magoffin.matt.meta.image;

import java.util.Date;
import junit.framework.TestCase;

/**
 * Test case for the {@link ImageMetadataType} class.
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 1.1 $ $Date: 2007/01/17 03:55:48 $
 */
public class ImageMetadataTypeTest extends TestCase {

    /**
	 * Test date object type.
	 */
    public void testDateObjectType() {
        assertEquals(Date.class, ImageMetadataType.DATE_TAKEN.getObjectType());
    }
}
