package org.aha.mf4j;

/**
 * <p>
 *   Interface for objects that performs authentication.
 * </p>
 * @author Arne Halvorsen (aha42)
 */
public interface Authenticator {

    /**
   * <p>
   *   Performs authentication.
   * </p>
   * @param fs    Session.
   * @param perms Permissions requested.
   * @return {@code true} if authentication or {@code false} if end user aborted
   *         the authentication.
   * @throws FlickrException If fails.
   */
    boolean authenticate(FlickrSession fs, String perms) throws FlickrException;
}
