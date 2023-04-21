package net.recaptcha.jca.cci;

import net.recaptcha.jca.cci.RecaptchaInteractionSpec.InputRecordFieldNames;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static net.recaptcha.jca.cci.RecaptchaInputOutputFactory.createRecaptchaInput;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.both;
import static org.junit.matchers.JUnitMatchers.each;
import static org.junit.matchers.JUnitMatchers.either;
import javax.resource.ResourceException;
import javax.resource.cci.IndexedRecord;
import javax.resource.cci.MappedRecord;
import javax.resource.cci.RecordFactory;
import net.recaptcha.jca.api.RecaptchaVerifyInput;

/**
 * Tests <code>RecaptchaInputOutputFactory</code> regarding input creation.
 * @see RecaptchaInputOutputFactory#createRecaptchaInput(Record)
 * @author Marcus Andersson (lucaf3rr@gmail.com)
 */
public class RecaptchaInputOutputFactoryInputTest {

    /**
     * Test input field.
     */
    public static final String RECAPTCHA_CHALLENGE_TEST = "recaptchaChallengeTest";

    /**
     * Test input field.
     */
    public static final String RECAPTCHA_RESPONSE_TEST = "recaptchaResponseTest";

    /**
     * Test input field.
     */
    public static final String REMOTE_IP_ADDRESS_TEST = "remoteIpAddressTest";

    private MappedRecord mappedRecord;

    private RecordFactory recordFactory;

    /**
     * Setup state.
     * @throws javax.resource.ResourceException An unforeseen error occurred in setting up state
     */
    @Before
    public void before() throws ResourceException {
        recordFactory = new RecordFactoryImpl();
        mappedRecord = recordFactory.createMappedRecord(RecordTypeMapped.INPUT.name());
    }

    /**
     * Tears down state.
     */
    @After
    public void after() {
        recordFactory = null;
        mappedRecord = null;
    }

    /**
     * Calls method with remote IP address field <code>null</code>.
     * @throws net.recaptcha.jca.cci.RecaptchaInputException Expected
     */
    @Test(expected = RecaptchaInputException.class)
    public void testCreateRecaptchaInputEmptyRemoteIp() throws RecaptchaInputException {
        mappedRecord.put(InputRecordFieldNames.RECAPTCHA_CHALLENGE, RECAPTCHA_CHALLENGE_TEST);
        mappedRecord.put(InputRecordFieldNames.RECAPTCHA_RESPONSE, RECAPTCHA_RESPONSE_TEST);
        createRecaptchaInput(mappedRecord);
    }

    /**
     * Calls method with reCAPTCHA challenge field <code>null</code>.
     * @throws net.recaptcha.jca.cci.RecaptchaInputException Expected
     */
    @Test(expected = RecaptchaInputException.class)
    public void testCreateRecaptchaInputEmptyChallenge() throws RecaptchaInputException {
        mappedRecord.put(InputRecordFieldNames.REMOTE_IP_ADDRESS, REMOTE_IP_ADDRESS_TEST);
        mappedRecord.put(InputRecordFieldNames.RECAPTCHA_RESPONSE, RECAPTCHA_RESPONSE_TEST);
        createRecaptchaInput(mappedRecord);
    }

    /**
     * Calls method with reCAPTCHA response field <code>null</code>.
     * @throws net.recaptcha.jca.cci.RecaptchaInputException Expected
     */
    @Test(expected = RecaptchaInputException.class)
    public void testCreateRecaptchaInputEmptyResponse() throws RecaptchaInputException {
        mappedRecord.put(InputRecordFieldNames.REMOTE_IP_ADDRESS, REMOTE_IP_ADDRESS_TEST);
        mappedRecord.put(InputRecordFieldNames.RECAPTCHA_CHALLENGE, RECAPTCHA_CHALLENGE_TEST);
        createRecaptchaInput(mappedRecord);
    }

    /**
     * Calls method with <code>null</code> input argument.
     * @throws net.recaptcha.jca.cci.RecaptchaInputException Expected
     */
    @Test(expected = RecaptchaInputException.class)
    public void testCreateRecaptchaInputNullInput() throws RecaptchaInputException {
        createRecaptchaInput(null);
    }

    /**
     * Calls method with an incorrect argument type.
     * @throws javax.resource.ResourceException Expected
     */
    @Test(expected = RecaptchaInputException.class)
    public void testCreateRecaptchaInputInvalidInputType() throws ResourceException {
        IndexedRecord wrongType = recordFactory.createIndexedRecord(RecordTypeIndexed.OUTPUT.name());
        createRecaptchaInput(wrongType);
    }

    /**
     * Creates a <code>RecaptchaVerifyInput</code> object and verifies its properties.
     * 
     * @throws net.recaptcha.jca.cci.RecaptchaInputException An unforeseen error occurred in creating input
     */
    @Test
    public void testCreateRecaptchaInput() throws RecaptchaInputException {
        mappedRecord.put(InputRecordFieldNames.RECAPTCHA_CHALLENGE, RECAPTCHA_CHALLENGE_TEST);
        mappedRecord.put(InputRecordFieldNames.RECAPTCHA_RESPONSE, RECAPTCHA_RESPONSE_TEST);
        mappedRecord.put(InputRecordFieldNames.REMOTE_IP_ADDRESS, REMOTE_IP_ADDRESS_TEST);
        RecaptchaVerifyInput recaptchaInput = createRecaptchaInput(mappedRecord);
        assertThat(recaptchaInput.getRecaptchaChallenge(), is(equalTo(RECAPTCHA_CHALLENGE_TEST)));
        assertThat(recaptchaInput.getRecaptchaResponse(), is(equalTo(RECAPTCHA_RESPONSE_TEST)));
        assertThat(recaptchaInput.getRemoteIpAddress(), is(equalTo(REMOTE_IP_ADDRESS_TEST)));
    }
}
