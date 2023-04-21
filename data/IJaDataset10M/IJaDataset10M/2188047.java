package com.magnetstreet.ws.mdverify.phone;

import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.Properties;
import com.melissadata.mdPhone;
import com.magnetstreet.ws.mdverify.util.OutputFormatterUtilities;
import static junit.framework.Assert.assertEquals;

/**
 * PhoneValidatorTest
 *
 * @author Martin Dale Lyness <martin.lyness@gmail.com>
 * @version 0.1.0 Apr 29, 2009
 * @since Apr 29, 2009
 */
public class PhoneValidatorTest {

    private PhoneValidator validator;

    @Before
    public void setUp() throws IOException {
        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("melissadata.properties"));
        System.out.println(props.getProperty("mdPhone_license"));
        validator = new PhoneValidator(props.getProperty("mdPhone_license"), props.getProperty("mdAddr_dataLoc"));
    }

    @Test
    public void testValidateEmail() {
        mdPhone po = validator.verifyPhone("9208552837");
        assertEquals("No error", po.GetInitializeErrorString());
        assertEquals(OutputFormatterUtilities.generatePlainTextDump(po), "", po.GetErrorCode().trim());
        assertEquals("920", po.GetAreaCode());
    }
}
