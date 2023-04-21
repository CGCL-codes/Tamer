package org.jcvi.auth;

import java.util.Arrays;

public class DefaultJCVIAuthorizer implements JCVIAuthorizer {

    /**
     * Default user to use at JCVI (User name is {@code datasupt} ).
     * Please note that this user may not have full
     * permissions on many systems
     */
    public static final JCVIAuthorizer DEFAULT_USER = new UncloseableJCVIAuthorizer("datasupt", new char[] { 'f', 'o', 'r', 's', 'c', 'r', 'i', 'p', 't', 's' });

    public static final JCVIAuthorizer JOE_USER = new UncloseableJCVIAuthorizer("joeuser", new char[] { 'j', 'o', 'e', 'u', 's', 'e', 'r' });

    public static final JCVIAuthorizer DEFAULT_TIGR_USER = new UncloseableJCVIAuthorizer("access", new char[] { 'a', 'c', 'c', 'e', 's', 's' });

    private final String username;

    private final char[] pass;

    private boolean closed = false;

    /**
     * @param username
     * @param pass
     */
    public DefaultJCVIAuthorizer(String username, char[] pass) {
        if (username == null || pass == null) {
            throw new NullPointerException("parameters can not be null");
        }
        this.username = username;
        this.pass = Arrays.copyOf(pass, pass.length);
    }

    @Override
    public synchronized char[] getPassword() {
        if (isClosed()) {
            throw new IllegalStateException("Authorizer is closed");
        }
        return Arrays.copyOf(pass, pass.length);
    }

    @Override
    public synchronized String getUsername() {
        if (isClosed()) {
            throw new IllegalStateException("Authorizer is closed");
        }
        return username;
    }

    @Override
    public synchronized void close() {
        closed = true;
        Arrays.fill(pass, '0');
    }

    @Override
    public synchronized boolean isClosed() {
        return closed;
    }

    /**
     * Makes sure password is cleared from
     * memory.
     */
    @Override
    protected void finalize() throws Throwable {
        if (!isClosed()) {
            close();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (closed ? 1231 : 1237);
        result = prime * result + Arrays.hashCode(pass);
        result = prime * result + username.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof DefaultJCVIAuthorizer)) return false;
        DefaultJCVIAuthorizer other = (DefaultJCVIAuthorizer) obj;
        if (closed != other.closed) {
            return false;
        }
        if (!Arrays.equals(pass, other.pass)) {
            return false;
        }
        if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("DefaultJCVIAuthorizer for user %s", username);
    }

    /**
     * {@code UncloseableJCVIAuthorizer} is an implementation of
     * DeaultJCVIAuthorizer that can never be closed.
     * @author dkatzel
     *
     *
     */
    public static final class UncloseableJCVIAuthorizer extends DefaultJCVIAuthorizer {

        public UncloseableJCVIAuthorizer(String username, char[] pass) {
            super(username, pass);
        }

        @Override
        public synchronized void close() {
        }
    }
}
