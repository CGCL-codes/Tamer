package org.avaje.ebean.net;

/**
 * Constants for the clustering and client server networking.
 */
public interface Constants {

    /**
     * Key used to put session id into headers.
     */
    public static final String SESSION_ID_KEY = "ebean.session.id";

    /**
     * Key used to identify the ebean processor.
     */
    public static final String PROCESS_KEY = "EBEAN";

    /**
     * Key used to identify the ebean server name.
     */
    public static final String SERVER_NAME_KEY = "NAME";
}
