package com.sun.tools.jdi;

import com.sun.jdi.*;

/**
 * This class is used by the back end to do thread synchronization.
 * We don't want to use java.lang.Object(s) for two reasons: we can't
 * filter them out, and this class should use less heap.
 */
public class LockObject {
}
