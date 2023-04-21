package name.pachler.nio.file;

import name.pachler.nio.file.ext.ExtendedWatchEventKind;
import name.pachler.nio.file.impl.VoidWatchEventKind;
import name.pachler.nio.file.impl.PathWatchEventKind;

/**
 * This class contains the standard watch event kinds, which are basically
 * flags that indicate which events a WatchService should report when a
 * Watchable is registered with a WatchService.
 * The kinds are also used to indicate the kind of event on events that
 * are reported back.</br>
 * Note that the event kinds defined in this class are supported on all platforms
 * @author count
 * @see Watchable#register
 * @see WatchEvent$Kind
 */
public class StandardWatchEventKind {

    /**
	 * Indicates that a file has been created under the watched path.
	 */
    public static final WatchEvent.Kind<Path> ENTRY_CREATE = new PathWatchEventKind("ENTRY_CREATE");

    /**
	 * Indicates that a file has been deleted under the watched path. Note that
	 * on file rename the old file name will be reported as deleted if no other
	 * (extended) watch event kinds are specified.
	 * @see ExtendedWatchEventKind
	 */
    public static final WatchEvent.Kind<Path> ENTRY_DELETE = new PathWatchEventKind("ENTRY_DELETE");

    /**
	 * Indicates that a file under the watched path has been modified. Note that
	 * modification can never be byte-accurate, which means that you won't
	 * receive a modification event for each byte written to a file. It is
	 * higly implementation dependent how many modification events are produced.
	 */
    public static final WatchEvent.Kind<Path> ENTRY_MODIFY = new PathWatchEventKind("ENTRY_MODIFY");

    /**
	 * Indicates queue overflow in the WatchService. If the event queue
	 * overflows (because, for example,the WatchService runs
	 * out of space to store events because they occur faster than the client
	 * code can retreives them from the designated watch keys), additional
	 * events are dropped, and this event is reported.
	 * Note that WatchKeys are always subscribed to this event, regardless
	 * of whether it is specified to <code>register()</code> or not.
	 * @see Watchable#register
	 */
    public static final WatchEvent.Kind<Void> OVERFLOW = new VoidWatchEventKind("OVERFLOW");
}
