package de.bea.domingo.map;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Location of a Notes database.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public final class NotesLocation {

    /** Protocol of a notes location. */
    public static final String NOTES_PROTOCOL = "notes:///";

    /** Protocol of a notes location. */
    public static final String HTTP_PROTOCOL = "http://";

    /** Regular expression pattern for protocol section. */
    private static final String PROTOCOL_PATTERN = "(" + NOTES_PROTOCOL + "|" + HTTP_PROTOCOL + ")";

    /** Regulare expression pattern for credentials section. */
    private static final String CREDENTIALS_PATTERN = "(([^!:@]*):([^!:@]*)(@?))?";

    /** Regular expression pattern for parsing server section. */
    private static final String SERVER_PATTERN1 = "([^!:@]*(:\\d+)?)";

    /** Regular expression pattern for parsing server section. */
    private static final String SERVER_PATTERN2 = "([^!:@/]*(:\\d+)?)";

    /** Regular expression pattern for parsing path section. */
    private static final String PATH_PATTERN1 = "((!!)(.*))?";

    /** Regular expression pattern for parsing path section. */
    private static final String PATH_PATTERN2 = "((/)(.*))?";

    /** Regular expression pattern for parsing locations. */
    private static final String LOCATION_PATTERN1 = PROTOCOL_PATTERN + CREDENTIALS_PATTERN + SERVER_PATTERN1 + PATH_PATTERN1;

    /** Regular expression pattern for parsing locations. */
    private static final String LOCATION_PATTERN2 = PROTOCOL_PATTERN + CREDENTIALS_PATTERN + SERVER_PATTERN2 + PATH_PATTERN2;

    /** Position of protokol token in location pattern matcher. */
    private static final int PROTOCOL_TOKEN = 1;

    /** Position of username token in location pattern matcher. */
    private static final int USERNAME_TOKEN = 3;

    /** Position of password token in location pattern matcher. */
    private static final int PASSWORD_TOKEN = 4;

    /** Position of server token in location pattern matcher. */
    private static final int SERVER_TOKEN = 6;

    /** Position of path token in location pattern matcher. */
    private static final int PATH_TOKEN = 10;

    /** Alternative identifier for the local host. */
    private static final String LOKAL_HOST = "lokal";

    /** Standard identifier for the local host. */
    public static final String LOCAL_HOST = "local";

    /**
     * The unparsed location string of a location.
     *
     * <p><b>Example:</b><br/>
     * <code>notes:///mail-server/acme!!mail/my-mail-db.nsf</code></p>
     */
    private String mLocation = null;

    /**
     * The protocol of the location.
     *
     * <p><b>Example:</b><br/>
     * <code>notes:</code>.
     */
    private String mProtocol = null;

    /**
     * The server of the location. <p> <b>Example: </b> <br/><code>MyServer/Acme</code>.
     */
    private String mServer = null;

    /**
     * The path of the location.
     *
     * <p><b>Example:</b>
     * <br/><code>mail/my-mail-db.nsf</code>.</p>
     */
    private String mPath = null;

    /** the IIOP username */
    private String mUsername = null;

    /** the IIOP password */
    private String mPassword = null;

    /**
     * Factory method for Location objects.
     *
     * @param location the location as url-like string
     * @return the location object
     */
    public static synchronized NotesLocation getInstance(final String location) {
        return new NotesLocation((NotesLocation) null, location);
    }

    /**
     * Factory method for local Location.
     *
     * @param context parent location
     * @param server the server
     * @param path the path
     * @return the new Location
     */
    public static synchronized NotesLocation getInstance(final NotesLocation context, final String server, final String path) {
        if (context != null && context.isLocal()) {
            return new NotesLocation(server, path);
        } else {
            return new NotesLocation(context, path);
        }
    }

    /**
     * Constructor.
     *
     * @param locationUri URI of location of database.
     */
    public NotesLocation(final String locationUri) {
        setLocation(locationUri);
    }

    /**
     * Constructor.
     *
     * @param context parent location
     * @param path path relative to context
     */
    public NotesLocation(final NotesLocation context, final String path) {
        if (context != null) {
            String locationUri = context.toString();
            int seperatorPos = locationUri.indexOf(getPathSep());
            if (seperatorPos >= 0) {
                locationUri = locationUri.substring(0, seperatorPos);
            }
            locationUri = locationUri + getPathSep() + path;
            setLocation(locationUri);
        } else if (path != null) {
            setLocation(path);
        } else {
            throw new IllegalArgumentException("Must specify at least one of context or path");
        }
    }

    /**
     * Creates a new local location.
     * This method doesn't handle IIOP locations.
     *
     * @param context parent location
     * @param server the server
     * @param path path relative to context
     */
    public NotesLocation(final NotesLocation context, final String server, final String path) {
        if (context != null) {
            String locationUri = NOTES_PROTOCOL + server + getPathSep() + path;
            setLocation(locationUri);
        } else if (path != null) {
            setLocation(path);
        } else {
            throw new RuntimeException("Invalid arguments to create a Notes location");
        }
    }

    /**
     * Constructor for local locations.
     *
     * @param server the server
     * @param path the path
     */
    public NotesLocation(final String server, final String path) {
        setLocation(NOTES_PROTOCOL + server + getPathSep() + path);
    }

    /**
     * Sets the location string.
     *
     * @param locationUri URI of location of database.
     */
    protected void setLocation(final String locationUri) {
        mLocation = locationUri;
        parseLocation(mLocation);
    }

    /**
     * Parses a notes location. <p> A notes location is built up like this:
     * <br/><code>"notes:///" + server + "!!" + path</code> <br/>where server
     * must be a valid notes server name or an empty string and path must be a
     * valid path and file name of a notes database. </p>
     *
     * @throws IllegalArgumentException if the location string is invalid
     */
    private void parseLocation(final String location) throws IllegalArgumentException {
        String pattern = location.indexOf("!!") >= 0 ? LOCATION_PATTERN1 : LOCATION_PATTERN2;
        Matcher matcher = Pattern.compile(pattern).matcher(location);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Cannot parse location");
        }
        mProtocol = matcher.group(PROTOCOL_TOKEN);
        mUsername = matcher.group(USERNAME_TOKEN);
        mPassword = matcher.group(PASSWORD_TOKEN);
        mServer = matcher.group(SERVER_TOKEN);
        mPath = matcher.group(PATH_TOKEN);
        normalize();
        validate();
    }

    /**
     * Normalizes the components of the location to empty or <code>null</code>-values.
     */
    private void normalize() {
        if (mUsername != null && mUsername.length() == 0) {
            mUsername = null;
        }
        if (mPassword != null && mPassword.length() == 0) {
            mPassword = null;
        }
        if (mServer == null) {
            mServer = "";
        }
        if (mServer.endsWith("/")) {
            mServer = mServer.substring(0, mServer.length() - 1);
        }
        if (LOCAL_HOST.equals(mServer) || LOKAL_HOST.equals(mServer)) {
            mServer = "";
        }
        if (mPath == null) {
            mPath = "";
        }
    }

    /**
     * Validates the location.
     *
     * @throws IllegalArgumentException if the location is invalid
     */
    private void validate() throws IllegalArgumentException {
        if (!HTTP_PROTOCOL.equals(mProtocol) && !NOTES_PROTOCOL.equals(mProtocol)) {
            throw new IllegalArgumentException("Unknown protocol: " + mProtocol);
        }
        if (HTTP_PROTOCOL.equals(mProtocol) && mServer.length() == 0) {
            throw new IllegalArgumentException("Http locations need a server");
        }
        if (mPassword != null && mUsername == null) {
            throw new IllegalArgumentException("username missing");
        }
        if ((mUsername != null && mUsername.length() > 0) && (mServer.length() == 0)) {
            throw new IllegalArgumentException("IIOP locations must specify a server");
        }
    }

    /**
     * Returns the IIOP Password.
     *
     * @return String
     * @deprecated use {@link #getPassword()} instead
     */
    public String getIIOPPasswd() {
        return mPassword;
    }

    /**
     * Returns the IIOP Password.
     *
     * @return String
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * Returns the IIOP Username.
     *
     * @return String
     * @deprecated use {@link #getUsername()} instead
     */
    public String getIIOPUser() {
        return mUsername;
    }

    /**
     * Returns the IIOP Username.
     *
     * @return String
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * Returns the path of the notes location.
     *
     * @return the path of the notes location.
     */
    public String getPath() {
        return mPath;
    }

    /**
     * Returns the local location corresponding to this location.
     * If the location is already local, this instance is returned.
     * If the location is not local, a new location is returned where
     * the host of the location is changed to the local host.
     *
     * @return local location
     */
    public NotesLocation getLocalLocation() {
        if (isIIOP() || isLocal()) {
            return this;
        }
        return new NotesLocation(getProtocol() + LOCAL_HOST + "!!" + getPath());
    }

    /**
     * Checks if a notes location is local or not.
     *
     * @return <code>true</code> if the location is local, else
     *         <code>false</code>
     */
    public boolean isLocal() {
        return LOCAL_HOST.equals(mServer) || LOKAL_HOST.equals(mServer) || "".equals(mServer);
    }

    /**
     * Checks if the notes location is a IIOP location or not.
     *
     * @return <code>true</code> if it is a IIOP location, else <code>false</code>
     */
    public boolean isIIOP() {
        return mUsername != null;
    }

    /**
     * Checks if the notes location is using the HTTP protocol.
     * @return <code>true</code> if the protocol is HTTP, else <code>false</code>
     */
    public boolean isHttp() {
        return mProtocol.equals(HTTP_PROTOCOL);
    }

    /**
     * Returns the host of the notes location or <code>null</code> if it's an
     * IIOP location.
     *
     * @return the host of the notes location.
     */
    public String getHost() {
        return mServer;
    }

    /**
     * Returns the server part of a location. If the location is a local
     * location or an IIOP location, then the server the empty string, else the
     * name of the server.
     *
     * @return server part of a location
     */
    public String getServer() {
        if (isIIOP()) {
            return "";
        } else {
            return mServer;
        }
    }

    /**
     * Returns a string representing the local server.
     * In case of DIIOP (Corba), this must be the empty string,
     * in case of local call this must be the string <code>"local"</code>.
     *
     * @return local server
     */
    public String getLocalServer() {
        if (isIIOP()) {
            return "";
        } else {
            return LOCAL_HOST;
        }
    }

    /**
     * Returns the separator string between host and path
     * in locations.
     *
     * @return separator string between host and path
     */
    protected String getPathSep() {
        return "!!";
    }

    /**
     * Returns the protocol of the location.
     *
     * @return protocol of location
     */
    public String getProtocol() {
        return mProtocol;
    }

    /**
     * @return the location as a string
     */
    public String toString() {
        return mLocation;
    }
}
