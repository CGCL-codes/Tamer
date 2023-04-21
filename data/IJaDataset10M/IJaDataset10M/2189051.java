package com.xavax.xstore;

/**
 * AccessKey facilitates the implementation of the private interface
 * pattern.  An AccessKey must be passed to methods we must expose to
 * the public but still want to restrict access.  Only classes within
 * the persistence package can create an AccessKey.
 */
public final class AccessKey {

    /**
   * Constructs an AccessKey.
   */
    AccessKey() {
        _key = _master;
    }

    /**
   * Returns true if this is a valid AccessKey.
   *
   * @return true if this is a valid AccessKey.
   */
    public boolean isValid() {
        return _key == _master;
    }

    private int _key;

    private static int _master = 0xDEADBEEF;
}
