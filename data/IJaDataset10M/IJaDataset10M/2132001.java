package uk.org.ogsadai.client.toolkit.activities.security;

import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.client.toolkit.Activity;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.activity.ActivityInput;
import uk.org.ogsadai.client.toolkit.activity.BaseActivity;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityInput;
import uk.org.ogsadai.client.toolkit.activity.SimpleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIOIllegalStateException;
import uk.org.ogsadai.data.BooleanData;
import uk.org.ogsadai.data.StringData;

/**
 * An activity that applies GSI transport level security to a resource proxy.  
 * GSI transport level security will then be used when the proxy is used to
 * communicate with a remote OGSA-DAI resource.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>resource</code>. Type: {@link uk.org.ogsadai.client.toolkit.Resource}.
 * The resource to which GSI transport level security is to be applied.
 * </li>
 * <li>
 * <code>encrypt</code>. Type: {@link java.lang.Boolean}. Should the data 
 * be encrypted?  <code>Boolean.TRUE</code> specifies that the data should be
 * encrypted, <code>Boolean.FLASE</code> specifies data integrity only with
 * no encryption.  This is an optional input.   If not specified the default
 * is to encrypt the data.
 * </li>
 * <li>
 * <code>credential</code>. Type: 
 * {@link uk.org.ogsadai.authorization.GSSCredentialProvider}. Given access to
 * the credential that should be used when communicating with the remote server.
 * </li>
 * <li>
 * <code>authorization</code>. Type: {@link java.lang.String}.  How the activity 
 * should authorize the remote service.  <code>HOST</code> specifies host 
 * authorization there the service's identity must match the host in the URL 
 * used to contact the service.  <code>SELF</code> specifies self authorization 
 * where the service's identity must match that of the local OGSA-DAI server.  
 * <code>NONE</code> specifies no authorization.  Any other value specifies
 * identity authorization with the value given being the expected identity.
 * This is an optional input.  If not specified HOST authorization will be used.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>output</code>. Type: {@link uk.org.ogsadai.client.toolkit.Resource}.  
 * The same <code>Resource</code> object as was received as input but now 
 * configured to apply GSI transport level security to all communication.
 * </li>
 * </ul>
 * <p>
 * Expected name of activity on server:
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.ApplyGSITransportLevelSecurity</code>
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource: none.
 * </p>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * Applies GSI transport level security to the given resource.
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class ApplyGSITransportLevelSecurity extends BaseActivity implements Activity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008.";

    /** Default activity name. */
    public static final ActivityName DEFAULT_ACTIVITY_NAME = new ActivityName("uk.org.ogsadai.ApplyGSITransportLevelSecurity");

    /**
     * Activity input name (<code>resource</code>) - the resource to have
     * transport level security applied to it.
     */
    public static final String INPUT_RESOURCE = "resource";

    /** Resource Input. */
    private ActivityInput mResourceInput;

    /**
     * Activity input name (<code>encrypt</code>) - should the communication
     * to be the resource be encrypted.
     */
    public static final String INPUT_ENCRYPT = "encrypt";

    /** Encrypt Input. */
    private ActivityInput mEncryptInput;

    /**
     * Activity input name (<code>credential</code>) - credential to use when
     * communicating with the remote resource.
     */
    public static final String INPUT_CREDENTIAL = "credential";

    /** Credential Input. */
    private ActivityInput mCredentialInput;

    /**
     * Activity input name (<code>authorization</code>) - how OGSA-DAI should
     * authorize the remote server.
     */
    public static final String INPUT_AUTHORIZATION = "authorization";

    /** Authorization Input. */
    private ActivityInput mAuthorizationInput;

    /**
     * Activity output name (<code>output</code>) - the resource configured to
     * use GSI transport level security.
     */
    public static final String OUTPUT_OUTPUT = "output";

    /** Resource output. */
    private ActivityOutput mOutput;

    /**
     * Constructor.
     */
    public ApplyGSITransportLevelSecurity() {
        super(DEFAULT_ACTIVITY_NAME);
        mResourceInput = new SimpleActivityInput(INPUT_RESOURCE);
        mEncryptInput = new SimpleActivityInput(INPUT_ENCRYPT, true);
        mCredentialInput = new SimpleActivityInput(INPUT_CREDENTIAL);
        mAuthorizationInput = new SimpleActivityInput(INPUT_AUTHORIZATION, true);
        mOutput = new SimpleActivityOutput(OUTPUT_OUTPUT);
    }

    /**
     * Adds a value to the <tt>encrypt</tt> input.  This input specifies
     * if the data is to be encrypted or not.  If the value is <tt>true</tt>
     * then the data will be encrypted, otherwise the data will not be 
     * encrypted (but its integrity will be preserved).
     * 
     * @param encrypt <tt>true</tt> for data encryption, <tt>false</tt> for data
     *        integrity only.
     */
    public void addEncrypt(boolean encrypt) {
        mEncryptInput.add(new BooleanData(encrypt));
    }

    /**
     * Adds a value to the <tt>authorization</tt> input.  This input specifies
     * how the remote server is to be authorized.  The following values are
     * supported:
     * <ul>
     *   <li><tt>HOST</tt> - specifies host authorization.  This is the
     *   default if no authorization value is specified.</li>
     *   <li><tt>SELF</tt> - specifies self authorization</li>
     *   <li><tt>NONE</tt> - specifies no authorization</li>
     *   <li>any other value specifies identity authorization and the the value
     *   contain the expected server identity.
     * </ul>
     * 
     * @param authorization the authorization to use.
     */
    public void addAuthorization(String authorization) {
        mAuthorizationInput.add(new StringData(authorization));
    }

    /**
     * Connects the <tt>resource</tt> input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectResourceInput(final SingleActivityOutput output) {
        mResourceInput.connect(output);
    }

    /**
     * Connects the <tt>encrypt</tt> input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectEncryptInput(final SingleActivityOutput output) {
        mEncryptInput.connect(output);
    }

    /**
     * Connects the <tt>credential</tt> input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectCredentialInput(final SingleActivityOutput output) {
        mCredentialInput.connect(output);
    }

    /**
     * Connects the <tt>authorization</tt> input to the given output.
     * 
     * @param output output to connect to.
     */
    public void connectAuthorizationInput(final SingleActivityOutput output) {
        mAuthorizationInput.connect(output);
    }

    /**
     * Gets the output. This output will the resource proxy configured to use
     * GSI transport level security.
     * 
     * @return resource output.
     */
    public SingleActivityOutput getOutput() {
        return mOutput.getSingleActivityOutputs()[0];
    }

    /**
     * {@inheritDoc}
     */
    protected void validateIOState() throws ActivityIOIllegalStateException {
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getInputs() {
        return new ActivityInput[] { mResourceInput, mEncryptInput, mAuthorizationInput, mCredentialInput };
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityOutput[] getOutputs() {
        return new ActivityOutput[] { mOutput };
    }
}
