package android.os;

/**
 * Basic interface for finding and publishing system services.
 * 
 * An implementation of this interface is usually published as the
 * global context object, which can be retrieved via
 * BinderNative.getContextObject().  An easy way to retrieve this
 * is with the static method BnServiceManager.getDefault().
 * 
 * @hide
 */
public interface IServiceManager extends IInterface {

    /**
     * Retrieve an existing service called @a name from the
     * service manager.  Blocks for a few seconds waiting for it to be
     * published if it does not already exist.
     */
    public IBinder getService(String name) throws RemoteException;

    /**
     * Retrieve an existing service called @a name from the
     * service manager.  Non-blocking.
     */
    public IBinder checkService(String name) throws RemoteException;

    /**
     * Place a new @a service called @a name into the service
     * manager.
     */
    public void addService(String name, IBinder service) throws RemoteException;

    /**
     * Return a list of all currently running services.
     */
    public String[] listServices() throws RemoteException;

    /**
     * Assign a permission controller to the service manager.  After set, this
     * interface is checked before any services are added.
     */
    public void setPermissionController(IPermissionController controller) throws RemoteException;

    static final String descriptor = "android.os.IServiceManager";

    int GET_SERVICE_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION;

    int CHECK_SERVICE_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 1;

    int ADD_SERVICE_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 2;

    int LIST_SERVICES_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 3;

    int CHECK_SERVICES_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 4;

    int SET_PERMISSION_CONTROLLER_TRANSACTION = IBinder.FIRST_CALL_TRANSACTION + 5;
}
