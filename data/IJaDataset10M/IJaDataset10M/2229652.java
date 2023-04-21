package iaik.pkcs.pkcs11;

/**
 * The interface that an object must implement to be a valid parameter for the
 * initialize method of a Module object.
 *
 * @author <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
 * @version 1.0
 * @invariants
 */
public interface InitializeArgs {

    /**
   * This method returns the object that implements the functionality for
   * handling mutexes. It returns null, if no handler is set. If this method
   * returns null, the wrapper does not pass any callback functions to the
   * underlying module; i.e. is passes null-pointer for the functions.
   *
   * @return The handler object for mutex functionality, or null, if there is
   *         no handler for mutexes.
   * @preconditions
   * @postconditions
   */
    public MutexHandler getMutexHandler();

    /**
   * Check, if application threads which are executing calls to the library may
   * not use native operating system calls to spawn new threads.
   *
   * @return True, if application threads which are executing calls to the
   *         library may not use native operating system calls to spawn new
   *         threads. False, if they may.
   * @preconditions
   * @postconditions
   */
    public boolean isLibraryCantCreateOsThreads();

    /**
   * Check, if the library can use the native operation system threading model
   * for locking.
   *
   * @return True, if the library can use the native operation system threading
   *         model for locking. Fasle, otherwise.
   * @preconditions
   * @postconditions
   */
    public boolean isOsLockingOk();

    /**
   * Reserved parameter.
   *
   * @return Should be null in this version.
   * @preconditions
   * @postconditions (result == null)
   */
    public Object getReserved();
}
