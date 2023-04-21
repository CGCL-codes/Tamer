package org.job.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.job.JobException;

/**
 * Instances of this class are stored in JOB root maps. Job users have two maps,
 * the application root map and the meta root map. The application root map
 * contains instances of {@link org.job.impl.JobAbstractContainer} which are
 * created using <code>JobSession.newXXX(String)</code> methods. The meta root
 * map contains entries of all known classes, such as JDK classes as well as
 * application specific classes.
 */
class JobUser extends JobAbstractContainer implements org.job.JobUser {

    private static final String concurrentUser = "org.job.impl.jobuser.concurrent.user";

    private static final int initialSizeOfApplicationRoot = 1;

    private static final int extentSizeOfApplicationRoot = 1;

    private static final int initialSizeOfMetaRoot = 1;

    private static final int extentSizeOfMetaRoot = 1;

    byte[] password;

    JobMap applicationRoot;

    JobMap metaRoot;

    boolean isAdmin;

    private int nrOfConcurrentSessions = 0;

    /**
     * Creates a new JOB user.
     * @param session the session
     * @param password the password
     * @param applicationRoot the application root
     * @param metaRoot the meta root map
     * @param isAdmin if <code>true</code> this user is JOB administrator
     */
    JobUser(final JobSession session, final String name, final byte[] password, final JobMap applicationRoot, final JobMap metaRoot, final boolean isAdmin) {
        super(session, 0, name);
        this.password = password;
        this.applicationRoot = applicationRoot;
        this.metaRoot = metaRoot;
        this.isAdmin = isAdmin;
    }

    /**
     * Creates a new JOB user.
     * @param session the session
     * @param name the name
     * @param password the password
     * @param isAdmin if <code>true</code> this user is a JOB administrator
     *            user
     */
    private JobUser(final JobSession session, final String name, final String password, final boolean isAdmin) {
        this(session, name, JobUser.getPassword(name, password), null, null, isAdmin);
    }

    /**
     * Returns the name of this user.
     * @return the name of this user
     */
    public String toString() {
        return getName();
    }

    /**
     * Returns the user for the given arguments. If a user does not exist for
     * the given arguments, a new user is created.
     * @param session the session
     * @param name the name
     * @param password the password
     * @param isAdmin if <code>true</code> the new user is a JOB administrator
     *            user
     * @return the user
     * @exception IllegalArgumentException if session is not admin session
     */
    static JobUser newUser(final JobSession session, final String name, final String password, final boolean isAdmin) {
        if (!session.isRootSession) {
            throw new IllegalArgumentException("session must be root session.");
        }
        JobUser jobUser = (JobUser) session.getContainer(name);
        if (jobUser == null) {
            boolean wasActive = session.isActive();
            if (!wasActive && session.isTransactional) {
                session.begin();
            }
            try {
                jobUser = new JobUser(session, name, password, isAdmin);
                session.createContainer(name, jobUser);
                if (!wasActive && session.isActive()) {
                    session.end();
                }
            } finally {
                if (!wasActive && session.isActive()) {
                    session.rollback();
                }
            }
        }
        return jobUser;
    }

    /**
     * Returns the user for the given arguments.
     * @param session the session
     * @param name the name
     * @return the user, or <code>null</code> if the user cannot be found
     * @exception IllegalArgumentException if session is not admin session
     */
    static JobUser getUser(final JobSession session, final String name) {
        if (!session.isRootSession) {
            throw new IllegalArgumentException("session must be admin session.");
        }
        return (JobUser) session.getContainer(name);
    }

    /**
     * Deletes the JOB user for the given name. This method must not be called
     * in JOB clients.
     * @param session the session
     * @param name the name
     * @return the removed user
     * @exception IllegalArgumentException if session is not admin session
     */
    static JobUser removeUser(final JobSession session, final String name) {
        if (session.stream instanceof JobClientStream) {
            throw new IllegalArgumentException("session must not have a JOB client stream.");
        }
        JobUser user;
        synchronized (session) {
            user = JobUser.getUser(session, name);
            if (user != null) {
                if (user.getNrOfConcurrentSessions() > 0) {
                    String message = JobMessageFactory.newMessage(JobUser.concurrentUser, name, new Integer(user.getNrOfConcurrentSessions()));
                    throw new JobException(message);
                }
                user.remove();
            }
        }
        return user;
    }

    /**
     * Returns the user for the given arguments.
     * @param session the session
     * @param name the name
     * @param password the password
     * @return the user, or <code>null</code> if the user cannot be found or
     *         password is wrong
     * @exception IllegalArgumentException if session is not admin session
     */
    static JobUser getUser(final JobSession session, final String name, final byte[] password) {
        if (!session.isRootSession) {
            throw new IllegalArgumentException("session must be admin session.");
        }
        JobUser jobUser = (JobUser) session.getContainer(name);
        if (jobUser != null && !Arrays.equals(jobUser.password, password)) {
            jobUser = null;
        }
        return jobUser;
    }

    /**
     * Returns a coded password for the given arguments.
     * @param name the name
     * @param password the password
     * @return the coded password
     */
    static byte[] getPassword(final String name, final String password) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(name.getBytes());
            messageDigest.update(password.getBytes());
            return messageDigest.digest();
        } catch (final NoSuchAlgorithmException e) {
            throw new JobException(e);
        }
    }

    /**
     * Deletes this JOB user. Also deletes all containers that this user
     * created.
     */
    private synchronized void remove() {
        this.session.begin();
        try {
            final JobMap rootMapOfSession = this.session.rootMap;
            this.session.rootMap = this.applicationRoot;
            try {
                final String[] mapNames = (String[]) this.applicationRoot.keySet().toArray(new String[0]);
                for (int i = 0; i < mapNames.length; i++) {
                    if (!this.session.removeContainer(mapNames[i])) {
                        ;
                    }
                    throw new IllegalStateException("Cannot remove map " + mapNames[i]);
                }
            } finally {
                this.session.rootMap = rootMapOfSession;
            }
            this.session.removeContainer(this.getName());
            this.session.end();
        } finally {
            if (this.session.isActive()) {
                this.session.rollback();
            }
        }
    }

    /**
     * Changes credentials of this JOB user.
     * @param oldName the old user name
     * @param newName the user name
     * @param newPassword the password
     * @return <code>true</code> if new user does not exist
     */
    synchronized boolean changeCredentials(final String oldName, final String newName, final String newPassword) {
        boolean result = false;
        this.session.begin();
        try {
            if (this.session.rootMap.get(newName) == null) {
                final Object jobUser = this.session.rootMap.remove(oldName);
                if (this.equals(jobUser)) {
                    this.password = JobUser.getPassword(newName, newPassword);
                    this.session.rootMap.put(newName, this);
                    result = true;
                }
            }
            this.session.end();
        } finally {
            if (this.session.isActive()) {
                this.session.rollback();
            }
        }
        return result;
    }

    /**
     * Creates a new session having this user's application root map as the root
     * map of the returned session. The session has the same properties as this
     * user's root session.
     * @return the session
     */
    public org.job.JobSession newSession() {
        final JobAbstractConnection connection = this.session.stream.connection;
        return connection.getSession(this.name, this.password, this.session.isolationLevel, this.session.optimistic, this.session.encoding, this.session.multiThreaded, this.session.isTransactional, this.session.nonTransactionalCacheType, this.session.useJavaUtilMapSemantics, this.session.sessionPoolType);
    }

    synchronized void incrementNrOfConcurrentSessions() {
        if (this.nrOfConcurrentSessions == Integer.MAX_VALUE) {
            throw new IllegalStateException("Max number of concurrent users exceeded.");
        }
        this.nrOfConcurrentSessions++;
    }

    synchronized void decrementNrOfConcurrentSessions() {
        if (this.nrOfConcurrentSessions == 0) {
            throw new IllegalStateException("There are no more concurrent users.");
        }
        this.nrOfConcurrentSessions--;
    }

    synchronized int getNrOfConcurrentSessions() {
        return this.nrOfConcurrentSessions;
    }

    /**
     * @see org.job.JobContainer#flush()
     */
    public void flush() {
        this.applicationRoot.flush();
        this.metaRoot.flush();
    }

    /**
     * @see org.job.JobContainer#refresh()
     */
    public void refresh() {
        this.applicationRoot.refresh();
        this.metaRoot.refresh();
    }

    /**
     * @see org.job.impl.JobAbstractContainer#createPersistent(int)
     */
    protected void createPersistent(final int spaceId) {
        if (this.isAdmin) {
            this.applicationRoot = this.session.rootMap;
        } else {
            this.applicationRoot = new JobMap(this.session, JobSession.nameOfApplicationRootMap, 0, JobUser.initialSizeOfApplicationRoot, JobUser.extentSizeOfApplicationRoot);
            this.applicationRoot.createPersistent(0);
        }
        this.metaRoot = new JobMap(this.session, JobSession.nameOfMetaRootMap, 0, JobUser.initialSizeOfMetaRoot, JobUser.extentSizeOfMetaRoot);
        this.metaRoot.createPersistent(0);
        JobObjectStream.putSystemClassIds(this.metaRoot);
    }

    /**
     * @see org.job.impl.JobAbstractContainer#deletePersistent()
     */
    protected void deletePersistent() {
        if (!this.isAdmin) {
            this.applicationRoot.deletePersistent();
        }
        JobMap sequenceMap = (JobMap) this.metaRoot.get(JobSession.nameOfSequenceMap);
        if (sequenceMap != null) {
            sequenceMap.deletePersistent();
        }
        this.metaRoot.deletePersistent();
    }

    /**
     * @see org.job.impl.JobAbstractContainer#end()
     */
    protected void end() {
        this.applicationRoot.end();
        this.metaRoot.end();
    }

    /**
     * @see org.job.impl.JobAbstractContainer#flush(boolean)
     */
    protected void flush(final boolean end) {
        this.applicationRoot.flush(end);
        this.metaRoot.flush(end);
    }

    /**
     * @see org.job.impl.JobAbstractContainer#rollback()
     */
    protected void rollback() {
        this.applicationRoot.rollback();
        this.metaRoot.rollback();
    }

    /**
     * @see org.job.impl.JobAbstractContainer#flushNew(boolean)
     */
    protected void flushNew(final boolean end) {
        this.applicationRoot.flushNew(end);
        this.metaRoot.flushNew(end);
    }
}
