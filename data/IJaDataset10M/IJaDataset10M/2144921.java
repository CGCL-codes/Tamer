package com.apelon.apelonserver.test.performance.client;

import com.apelon.apelonserver.client.ApelonException;
import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.apelonserver.test.performance.dtd.DTDINFO;

/**
 * TestParseQueryServerClientImpl is based on the TestParseQueryServerClient.
 * <br>A TestParseQueryServerClient class does two thing:
 * <LI> Connects the TestParseQueryServer with a socket server or directly with an
 *      oracle database according to the server connection type.
 * <LI> Sets DTD file.<P>
 * Copyright:     Copyright (c) 2001<P>
 * Company:       Apelon <P>
 *
 *
 * @author        Apelon Inc.
 * @version       1.0.0
 */
final class TestParseQueryServerClientImpl extends TestParseQueryServerClient {

    private ServerConnection srvr;

    public TestParseQueryServerClientImpl(ServerConnection sc) throws ApelonException {
        this(sc, true);
    }

    /**
   * Constructs a new TestParseQueryServerClientImpl.
   * <br>Depending on the type of ServerConnection object passed in the
   * argument, the server may be local(JDBC), socket, or another implementation.
   * <br>The second argument is a boolean value for xml validation.
   *
   * @param         sc              the type of server connection.
   * @param         isValidating    a boolean value. It is true if xml needs to
   *                                be validated, otherwise it is false.
   * @throws     ApelonException    error
   *
   */
    public TestParseQueryServerClientImpl(ServerConnection sc, boolean isValidating) throws ApelonException {
        srvr = sc;
        initializeDtds(DTDINFO.PLAY, DTDINFO.class, DTDINFO.RESULT, DTDINFO.RESULT_FILE, isValidating);
    }

    /**
   * Gets the server connection type.
   *
   * @return        the type of the server connection.
   *
   */
    protected ServerConnection getServerConnection() {
        return srvr;
    }
}
