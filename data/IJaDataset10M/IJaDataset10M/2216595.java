package gov.sns.tools.services;

import com.strangeberry.rendezvous.ServiceInfo;
import java.util.regex.*;

/**
 * ServiceRef wraps the native Rendezvous ServiceInfo and provides a reference to the service
 * that hides a direct reference to Rendezvous.
 *
 * @author  tap
 */
public class ServiceRef {

    /** Pattern matching the protocol and DNS part of the type */
    static final Pattern typePattern;

    /** Property identifying the local service handler */
    static final String serviceKey = "xmlrpc_service_handler";

    /** Redezvous service info */
    private ServiceInfo _serviceInfo;

    static {
        typePattern = Pattern.compile("(\\w++)\\._tcp\\._local\\.");
    }

    /**
	 * Create a new service reference to wrap the specified service info.
	 */
    ServiceRef(ServiceInfo serviceInfo) {
        _serviceInfo = serviceInfo;
    }

    /**
	 * Get the name of the service.
	 * @return The name of the service provided.
	 */
    public String getServiceName() {
        return _serviceInfo.getPropertyString(serviceKey);
    }

    /**
	 * Get the unique raw name of the service provider.
	 * @return The raw name of the service provider.
	 */
    public String getRawName() {
        return _serviceInfo.getName();
    }

    /**
	 * Get the type of service provided.
	 * @return The type of service provided.
	 */
    public String getType() {
        return getBaseType(_serviceInfo.getType());
    }

    /**
	 * Get the fully qualified type
	 * @return the fully qualified type
	 */
    public String getFullType() {
        return _serviceInfo.getType();
    }

    /**
	 * Get the address of the remote service.
	 * @return the address of the remote service
	 */
    public String getHostAddress() {
        return _serviceInfo.getAddress();
    }

    /**
	 * Get the port for connecting to the remote service.
	 * @return the port for connecting to the remote service
	 */
    public int getPort() {
        return _serviceInfo.getPort();
    }

    /**
	 * Get the Rendezvous service info which is wrapped by this instance.
	 * @return The wrapped Rendezvous service info.
	 */
    ServiceInfo getServiceInfo() {
        return _serviceInfo;
    }

    /**
	 * Override equals to compare the wrapped service info instances.
	 * @param other The other service reference against which to compare this one.
	 * @return true if the other service reference refers to the same service provider as the this instance and false otherwise.
	 */
    @Override
    public boolean equals(Object other) {
        return _serviceInfo.equals(((ServiceRef) other)._serviceInfo);
    }

    /**
	 * Internally used to strip the communication protocol and DNS address from the full type name.
	 * @param fullType The full rendezvous type (e.g. "greeting._tcp._local.")
	 * @return Just the simple base type (e.g. "greeting")
	 */
    protected static String getBaseType(final String fullType) {
        Matcher matcher = typePattern.matcher(fullType);
        matcher.matches();
        return matcher.group(1);
    }

    /**
	 * Internally used to strip the type information from the name to get just the base name.
	 * @param fullName  The full rendezvous name (e.g. "Mary.greeting._tcp._local.")
	 * @param fullType  The full rendezvous type (e.g. "greeting._tcp._local.")
	 * @return The simple base name (e.g. "Mary")
	 */
    protected static String getBaseName(final String fullName, final String fullType) {
        int typeIndex = fullName.lastIndexOf(fullType);
        return fullName.substring(0, typeIndex - 1);
    }

    /**
	 * Internally used to construct the full rendezvous type from the simple base type.
	 * @param baseType The simple base type (e.g. "greeting")
	 * @return The full rendezvous type (e.g. "greeting._tcp._local.")
	 */
    protected static String getFullType(final String baseType) {
        return baseType + "._tcp._local.";
    }
}
