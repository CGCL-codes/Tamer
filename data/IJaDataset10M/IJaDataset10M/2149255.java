package de.zeroseven.abc;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author t.uhlmann
 *
 */
public class ABCFileTest {

    private static ABCFile abc;

    private static ABCCoder coder;

    /**
	 * @throws java.lang.Exception
	 */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        FileInputStream in = new FileInputStream("test/res/simple.abc");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        byte[] abcBytes = out.toByteArray();
        in.close();
        out.close();
        coder = new ABCCoder(abcBytes);
        abc = new ABCFile();
        abc.decode(coder);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        coder.adjustPointer(-1);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for {@link de.zeroseven.abc.ABCFile#length()}.
	 */
    @Test
    public final void testLength() {
        Assert.assertEquals(coder.getCapacity(), abc.length());
    }

    /**
	 * Test method for {@link de.zeroseven.abc.ABCFile#encode(de.zeroseven.abc.ABCCoder)}.
	 */
    @Test
    public final void testEncode() {
        byte[] data = new byte[coder.getCapacity()];
        ABCCoder out = new ABCCoder(data);
        abc.encode(out);
        Assert.assertArrayEquals(coder.getData(), data);
    }

    /**
	 * Test method for {@link de.zeroseven.abc.ABCFile#decode(de.zeroseven.abc.ABCCoder)}.
	 */
    @Test
    public final void testDecode() {
        abc.decode(coder);
    }
}
