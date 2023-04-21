package simple.framework.service;

/**
 * This is an interface to be implemented by all services. The "key" property is
 * used to identify the service and to build the service hierarchy.
 * @author Serg Bogatyrjov
 */
public interface KeyAware {

    /**
	 * Returns the service identifier expected to be used as a key in service
	 * collections.
	 * @return the service identirier.
	 */
    String getKey();
}
