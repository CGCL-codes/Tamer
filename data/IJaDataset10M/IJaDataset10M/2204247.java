package org.neodatis.odb.core.server.connection;

public class ConnectionIdGenerator {

    public static String newId(String ip, long dateTime, int sequence) {
        return ip + "-" + dateTime + "-" + sequence;
    }
}
