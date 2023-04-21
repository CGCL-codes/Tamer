package ubersoldat.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import ubersoldat.data.DatabaseEntry;
import ubersoldat.data.DatabaseFilter;
import ubersoldat.io.SoldatInputStream;
import ubersoldat.io.SoldatOutputStream;

public class DatabaseServer extends TcpListener {

    public DatabaseServer(ServerSocket socket) {
        super(socket);
    }

    @Override
    public void newConnection(Socket socket) {
        new Thread(new DatabaseClient(socket)).start();
    }

    public boolean add(DatabaseEntry query) {
        return false;
    }

    public boolean update(String string, DatabaseEntry query) {
        return false;
    }

    public DatabaseEntry[] select(DatabaseFilter query) {
        return null;
    }

    private class DatabaseClient extends TcpClient implements Runnable {

        public DatabaseClient(Socket socket) {
            super(socket);
        }

        @Override
        public void run() {
            try {
                connect();
                InputStream tmpIn = getSocket().getInputStream();
                SoldatInputStream in = new SoldatInputStream(tmpIn);
                OutputStream tmpOut = getSocket().getOutputStream();
                SoldatOutputStream out = new SoldatOutputStream(tmpOut);
                in.mark(1);
                int identifier = in.read();
                in.reset();
                switch(identifier) {
                    case DatabaseHelper.idListRequest:
                        DatabaseFilter lr = DatabaseHelper.readSelectQuery(in);
                        DatabaseHelper.writeSelectQueryResponse(out, select(lr));
                        break;
                    case DatabaseHelper.idListAddEntry:
                        DatabaseEntry e = DatabaseHelper.readAddQuery(in);
                        e.host = getSocket().getInetAddress().getHostAddress();
                        boolean accepted = add(e);
                        DatabaseHelper.writeAddQueryRespone(out, accepted);
                        break;
                    case DatabaseHelper.idListRefreshEntry:
                        e = DatabaseHelper.readUpdateQuery(in);
                        accepted = update(getSocket().getInetAddress().getHostAddress(), e);
                        DatabaseHelper.writeUpdateQueryResponse(out, accepted);
                        break;
                    case DatabaseHelper.idListRefreshEntry2:
                        break;
                    case DatabaseHelper.idSayHi:
                        break;
                }
                in.close();
                out.close();
                disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
