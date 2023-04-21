package net.sf.cplab.core.util;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import java.awt.image.BufferedImage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jtse
 *
 */
public class ImagePoolImplTest {

    private ImageFactory mockFactory;

    private ImagePool pool;

    private String key1;

    private String key2;

    private BufferedImage image1;

    private BufferedImage image2;

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        mockFactory = createStrictMock(ImageFactory.class);
        pool = new ImagePoolImpl(mockFactory);
        key1 = "one";
        key2 = "two";
        image1 = new BufferedImage(160, 120, BufferedImage.TYPE_INT_ARGB);
        image2 = new BufferedImage(160, 120, BufferedImage.TYPE_INT_ARGB);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for {@link net.sf.cplab.core.util.ImagePoolImpl#borrowImage(java.lang.String)}.
	 * @throws Exception 
	 */
    @Test
    public void testBorrowAndReturnImage() throws Exception {
        expect(mockFactory.makeObject(key1)).andReturn(image1);
        mockFactory.activateObject(key1, image1);
        mockFactory.passivateObject(key1, image1);
        expect(mockFactory.makeObject(key2)).andReturn(image2);
        mockFactory.activateObject(key2, image2);
        mockFactory.activateObject(key1, image1);
        expect(mockFactory.makeObject(key1)).andReturn(image1);
        mockFactory.activateObject(key1, image1);
        mockFactory.passivateObject(key2, image2);
        mockFactory.passivateObject(key1, image1);
        mockFactory.passivateObject(key1, image1);
        mockFactory.activateObject(key1, image1);
        mockFactory.activateObject(key1, image1);
        expect(mockFactory.makeObject(key1)).andReturn(image1);
        mockFactory.activateObject(key1, image1);
        replay(mockFactory);
        BufferedImage expected;
        BufferedImage actual;
        expected = image1;
        actual = pool.borrowImage(key1);
        assertTrue(expected == actual);
        pool.returnImage(key1, actual);
        expected = image2;
        actual = pool.borrowImage(key2);
        assertTrue(expected == actual);
        expected = image1;
        actual = pool.borrowImage(key1);
        assertTrue(expected == actual);
        expected = image1;
        actual = pool.borrowImage(key1);
        assertTrue(expected == actual);
        pool.returnImage(key2, image2);
        pool.returnImage(key1, image1);
        pool.returnImage(key1, image1);
        expected = image1;
        actual = pool.borrowImage(key1);
        assertTrue(expected == actual);
        expected = image1;
        actual = pool.borrowImage(key1);
        assertTrue(expected == actual);
        expected = image1;
        actual = pool.borrowImage(key1);
        assertTrue(expected == actual);
        verify(mockFactory);
    }
}
