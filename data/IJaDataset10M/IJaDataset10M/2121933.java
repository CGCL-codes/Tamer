package com.arsenal.rtcomm.server;

import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.Random;
import java.util.Enumeration;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import com.arsenal.log.Log;
import com.arsenal.rtcomm.server.http.*;

public class MobileSocketServer {

    ConnectionManager connectionManager = null;

    public MobileSocketServer(ConnectionManager connectionManager, int port) {
        this.connectionManager = connectionManager;
        if (port != 0) {
            new MobileSocketListener(this, port);
        } else Log.info(this, "Disabling Mobile Socket Server - clients will be unable to connect with Socket");
    }

    public void processNewConnection(Socket socket) throws Exception {
        connectionManager.addMobileConnection(socket, "Socket");
    }
}
