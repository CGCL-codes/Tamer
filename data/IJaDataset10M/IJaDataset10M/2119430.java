package com.apelon.apelonserver.test.performance.client;

import com.apelon.apelonserver.client.ApelonException;
import com.apelon.apelonserver.client.ServerConnectionSocket;

/**
 * This class tests the round trip of query server pool check out time.
 * This RTT is ApelonServer's accept time + read time of  a request from the client
 * + check out of query server in the query server pool.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class TestPoolQueryServerClient extends ServerConnectionSocket {

    public static final String HEADER = "test001";

    public TestPoolQueryServerClient(String host, int port) throws ApelonException {
        super(host, port);
    }

    /**
   * The header of TestPoolQueryServer is test001.
   * This method sends a simple message to measure check out time
   * @return
   * @throws ApelonException
   */
    public String sendPerf() throws ApelonException {
        return sendQuery("PERF");
    }

    /**
   * The header of TestPoolQueryServer is test001.
   * This method sends a simple message to measure check out time
   * @return
   * @throws ApelonException
   */
    public String sendQuery(String query) throws ApelonException {
        return executeQueryRemote(HEADER + query);
    }

    public static synchronized long getClientSocketRTT() {
        return rtt;
    }

    public static synchronized void resetClientSocketRTT() {
        rtt = 0;
    }
}
