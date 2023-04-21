package uk.org.ogsadai.resource.dataresource.xmldb;

import uk.org.ogsadai.authorization.LoginProvider;
import uk.org.ogsadai.common.ID;
import uk.org.ogsadai.common.MalformedIDException;
import uk.org.ogsadai.config.ConfigurationValueIllegalException;
import uk.org.ogsadai.config.ConfigurationValueMissingException;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.config.KeyValueUnknownException;
import uk.org.ogsadai.context.ContextValueUnknownException;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.exception.DAIClassMissingInterfaceException;
import uk.org.ogsadai.resource.dataresource.DataResourceState;

/**
 * XML:DB data resource state represented as a convenience wrapper
 * around generic data resource state.
 * 
 * @author The OGSA-DAI Team.
 */
public class SimpleXMLDBDataResourceState implements XMLDBDataResourceState {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Key to access driver class name */
    public static Key DRIVER_CLASS = new Key("dai.driver.class");

    /** Key to access database base collection */
    public static Key BASE_COLLECTION_URI = new Key("dai.data.resource.uri");

    /** Login provider key */
    public static Key DATABASE_LOGIN_PROVIDER = new Key("dai.login.provider");

    /** Configuration of the JDBC data resource */
    protected DataResourceState mDataResourceState;

    /**
     * Constructor.
     */
    protected SimpleXMLDBDataResourceState() {
    }

    /**
     * Constructor.
     *
     * @param dataResourceState
     *     Generic data resource state
     * @throws IllegalArgumentException
     *     If <tt>dataResourceState</tt> is <tt>null</tt>.
     */
    public SimpleXMLDBDataResourceState(DataResourceState dataResourceState) {
        if (dataResourceState == null) {
            throw new IllegalArgumentException("dataResourceState must not be null");
        }
        mDataResourceState = dataResourceState;
    }

    /**
     * @throws ConfigurationValueMissingException if there is no
     * data resource state with key <tt>BASE_COLLECTION_URI</tt>.
     */
    public String getBaseCollectionURI() {
        String collectionURI = null;
        try {
            collectionURI = (String) mDataResourceState.getConfiguration().get(BASE_COLLECTION_URI);
        } catch (KeyValueUnknownException e) {
            throw new ConfigurationValueMissingException(BASE_COLLECTION_URI);
        }
        return collectionURI;
    }

    /**
     * @throws IllegalArgumentException
     *     If <tt>collectionURI</tt> is <tt>null</tt>.
     */
    public void setBaseCollectionURI(String collectionURI) {
        if (collectionURI == null) {
            throw new IllegalArgumentException("collectionURI must not be null");
        }
        mDataResourceState.getConfiguration().put(BASE_COLLECTION_URI, collectionURI);
    }

    /**
     * @throws ConfigurationValueMissingException if there is no
     * data resource state with key <tt>DRIVER_CLASS</tt>.
     */
    public String getDriverClass() {
        String driverClass = null;
        try {
            driverClass = (String) mDataResourceState.getConfiguration().get(DRIVER_CLASS);
        } catch (KeyValueUnknownException e) {
            throw new ConfigurationValueMissingException(BASE_COLLECTION_URI);
        }
        return driverClass;
    }

    /**
     * @throws IllegalArgumentException
     *     If <tt>driverClass</tt> is <tt>null</tt>.
     */
    public void setDriverClass(String driverClass) {
        if (driverClass == null) {
            throw new IllegalArgumentException("driverClass must not be null");
        }
        mDataResourceState.getConfiguration().put(DRIVER_CLASS, driverClass);
    }

    /**
     * Get data resource state with key <tt>DATABASE_LOGIN_PROVIDER</tt>,
     * convert this to an ID and use this ID to get a login provider
     * from the global OGSA-DAI context.
     *
     * @throws ConfigurationValueMissingException if there is no
     *     state with key <tt>DATABASE_LOGIN_PROVIDER</tt>.
     * @throws ConfigurationValueIllegalException if there is
     *     state with key <tt>DATABASE_LOGIN_PROVIDER</tt>
     *     but the value is not a valid ID.
     * @throws ContextValueUnknownException if there is no
     *     entry in the global OGSA-DAI context with the ID.
     * @throws ConfigurationValueIllegalException if there is
     *     an entry in the global OGSA-DAI context with the ID but
     *     it does not cast to <tt>LoginProvider</tt>
     */
    public LoginProvider getLoginProvider() {
        ID loginProviderID = null;
        try {
            String loginProviderIDStr = (String) mDataResourceState.getConfiguration().get(DATABASE_LOGIN_PROVIDER);
            loginProviderID = new ID(loginProviderIDStr);
        } catch (KeyValueUnknownException e) {
            throw new ConfigurationValueMissingException(DATABASE_LOGIN_PROVIDER);
        } catch (MalformedIDException e) {
            throw new ConfigurationValueIllegalException(DATABASE_LOGIN_PROVIDER, e);
        }
        LoginProvider provider = null;
        try {
            provider = (LoginProvider) (OGSADAIContext.getInstance().get(loginProviderID));
        } catch (ContextValueUnknownException e) {
            throw new ConfigurationValueMissingException(loginProviderID);
        } catch (ClassCastException e) {
            throw new ConfigurationValueIllegalException(loginProviderID, new DAIClassMissingInterfaceException(provider.getClass().getName(), LoginProvider.class.getName()));
        }
        return provider;
    }

    public DataResourceState getDataResourceState() {
        return mDataResourceState;
    }
}
