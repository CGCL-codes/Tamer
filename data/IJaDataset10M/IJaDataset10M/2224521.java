package N4M;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test encode/decode of requests
 */
@RunWith(Parameterized.class)
public class DonahooN4MQueryTest extends DonahooN4MMessageTest {

    protected String expBusinessName;

    /**
	 * Constructor for parameterized test to set expected values
	 * 
	 * @param expBusinessName expected business name
	 * @param expSerialized expected serialized value
	 */
    public DonahooN4MQueryTest(int expMsgId, String expBusinessName, byte[] expSerialized) {
        super(0, expMsgId, expSerialized);
        this.expBusinessName = expBusinessName;
    }

    /**
	 * Set up parameterized tests
	 * 
	 * @return constructor parameters 
	 */
    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] { { 0, "", new byte[] { 0x20, 0, 0 } }, { 255, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", new byte[] { 0x20, (byte) 0xFF, (byte) 0xFF, 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x' } } };
        return Arrays.asList(data);
    }

    @Override
    protected void verifyExpectedMessage(N4MMessage msg) {
        super.verifyExpectedMessage(msg);
        N4MQuery rMsg = (N4MQuery) msg;
        assertEquals(expBusinessName, rMsg.getBusinessName());
    }

    @Override
    public N4MMessage generateMessage() throws N4MException {
        return new N4MQuery(expMsgId, expBusinessName);
    }

    /**
     * Test non-zero error code
     * 
     * @throws N4MException
     *             expected for non-zero error code
     */
    @Test(expected = N4MException.class)
    public void testDecodeNZErrorCode() throws N4MException {
        byte[] badErrorBuffer = Arrays.copyOf(expSerialized, expSerialized.length);
        badErrorBuffer[0] &= 0x1;
        N4MMessage.decode(badErrorBuffer);
    }
}
