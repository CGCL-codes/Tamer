package org.fonc.client;

import java.io.IOException;
import java.net.Socket;
import org.fonc.DataSocket;
import org.fonc.Manager;

public class FTPClientDataConnection extends DataSocket {

    public FTPClientDataConnection(Socket s, Manager m, ClientConnection c) {
        super(s, m);
        setConnection(c);
    }

    public void Init() throws IOException {
        super.Init();
        while (getConnection() == null) {
        }
    }

    public void WriteIn(byte[] buffer, int len) throws IOException {
        ((ClientConnection) getConnection()).getServerData().WriteOut(buffer, len);
    }

    public boolean _isOK() {
        return super.isOK();
    }

    public boolean isOK() {
        return ((ClientConnection) getConnection()).isDataOK();
    }

    public void Close() {
        super.Close();
        ((ClientConnection) getConnection()).setFTPData(null);
        if (!docloseafter) {
            ClientConnection c = (ClientConnection) getConnection();
            c.getServerControl().SendDataClose(totalbytes);
        }
    }
}
