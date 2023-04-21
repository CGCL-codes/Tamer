package blueprint4j.comm.tcp;

import blueprint4j.db.*;
import blueprint4j.utils.*;
import java.sql.*;
import java.net.*;
import java.io.IOException;

/**
 * All processable can be placed inside a processing factory
 */
public interface Processable extends ThreadScheduable {

    int getListenPort();

    Processable getNewInstance(DBConnection dbcon, Socket socket) throws IOException;
}
