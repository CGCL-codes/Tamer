package javax.media.nativewindow;

/** A interface describing a graphics device in a
    toolkit-independent manner.
 */
public interface AbstractGraphicsDevice extends Cloneable {

    /** Dummy connection value for a default connection where no native support for multiple devices is available */
    public static String DEFAULT_CONNECTION = "decon";

    /** Dummy connection value for an external connection where no native support for multiple devices is available */
    public static String EXTERNAL_CONNECTION = "excon";

    /** Default unit id for the 1st device: 0 */
    public static int DEFAULT_UNIT = 0;

    /**
     * Returns the type of the underlying subsystem, ie
     * NativeWindowFactory.TYPE_KD, NativeWindowFactory.TYPE_X11, ..
     */
    public String getType();

    /**
     * Returns the semantic GraphicsDevice connection.<br>
     * On platforms supporting remote devices, eg via tcp/ip network,
     * the implementation shall return a unique name for each remote address.<br>
     * On X11 for example, the connection string should be as the following example.<br>
     * <ul>
     *   <li><code>:0.0</code> for a local connection</li>
     *   <li><code>remote.host.net:0.0</code> for a remote connection</li>
     * </ul>
     *
     * To support multiple local device, see {@link #getUnitID()}.
     */
    public String getConnection();

    /**
     * Returns the graphics device <code>unit ID</code>.<br>
     * The <code>unit ID</code> support multiple graphics device configurations
     * on a local machine.<br>
     * To support remote device, see {@link #getConnection()}.
     * @return
     */
    public int getUnitID();

    /**
     * Returns a unique ID String of this device using {@link #getType() type},
     * {@link #getConnection() connection} and {@link #getUnitID() unitID}.<br>
     * The unique ID does not reflect the instance of the device, hence the handle is not included.<br>
     * The unique ID may be used as a key for semantic device mapping.
     */
    public String getUniqueID();

    /**
     * Returns the native handle of the underlying native device,
     * if such thing exist.
     */
    public long getHandle();

    /**
     * Optionally locking the device, utilizing eg {@link javax.media.nativewindow.ToolkitLock}.
     * The lock implementation must be recursive.
     */
    public void lock();

    /** 
     * Optionally unlocking the device, utilizing eg {@link javax.media.nativewindow.ToolkitLock}.
     * The lock implementation must be recursive.
     */
    public void unlock();

    /**
     * Optionally closing the device.<br>
     * The default implementation is a NOP operation, returning false.<br>
     * The specific implementing, ie {@link javax.media.nativewindow.x11.X11GraphicsDevice},
     * shall have a enable/disable like {@link javax.media.nativewindow.x11.X11GraphicsDevice#setCloseDisplay(boolean, boolean)},<br>
     * which shall be invoked at creation time to determine ownership/role of freeing the resource.<br>
     *
     * @return true if a specialized closing operation was successfully issued, otherwise false,
     * ie no native closing operation was issued, which doesn't imply an error at all.
     */
    public boolean close();
}
