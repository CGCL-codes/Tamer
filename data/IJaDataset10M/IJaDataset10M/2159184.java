package org.exolab.jms.net.invoke;

import org.exolab.jms.net.CallbackService;

/**
 * Callback which is invoked recursively.
 *
 * @version     $Revision: 1.1 $ $Date: 2005/04/02 13:50:16 $
 * @author      <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 */
public class RecursiveCallback extends LoggingCallback {

    /**
     * The callback service.
     */
    private final CallbackService _service;

    /**
     * The depth of recursion.
     */
    private final int _depth;

    /**
     * The no. of times invoked. On reaching {@link #_depth}, is reset to
     * <code>0</code>.
     */
    private int _count;

    /**
     * Construct a new <code>RecursiveCallback</code>.
     *
     * @param depth the depth of the recursion
     */
    public RecursiveCallback(CallbackService service, int depth) {
        _service = service;
        _depth = depth;
    }

    /**
     * Invoke the callback
     *
     * @param object the invocation data
     */
    public void invoke(Object object) {
        super.invoke(object);
        if (++_count < _depth) {
            _service.invoke(object);
        } else {
            _count = 0;
        }
    }
}
