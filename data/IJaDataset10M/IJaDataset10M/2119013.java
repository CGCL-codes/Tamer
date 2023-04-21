package net.yura.android.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.Vector;
import net.yura.android.AndroidMeApp;
import net.yura.mobile.logging.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class SocketConnection implements javax.microedition.io.SocketConnection {

    private static ConnectivityBroadcastReceiver socketBroadcastReceiver;

    protected Socket socket;

    public SocketConnection(String host, int port) throws IOException {
        System.out.println(">>> SocketConnection: Constructor1()");
        ConnectivityBroadcastReceiver.addSocketConnection(this);
        this.socket = new Socket(host, port);
    }

    public SocketConnection(Socket socket) throws IOException {
        System.out.println(">>> SocketConnection: Constructor2()");
        if (!socket.isBound()) {
            ConnectivityBroadcastReceiver.addSocketConnection(this);
        }
        this.socket = socket;
    }

    public String getAddress() throws IOException {
        checkState();
        return socket.getInetAddress().toString();
    }

    public String getLocalAddress() throws IOException {
        checkState();
        return socket.getLocalAddress().toString();
    }

    public int getLocalPort() throws IOException {
        checkState();
        return socket.getLocalPort();
    }

    public int getPort() throws IOException {
        checkState();
        return socket.getPort();
    }

    public int getSocketOption(byte option) throws IllegalArgumentException, IOException {
        checkState();
        switch(option) {
            case DELAY:
                return (socket.getTcpNoDelay()) ? 1 : 0;
            case LINGER:
                int value = socket.getSoLinger();
                return (value == -1) ? 0 : value;
            case KEEPALIVE:
                return (socket.getKeepAlive()) ? 1 : 0;
            case RCVBUF:
                return socket.getReceiveBufferSize();
            case SNDBUF:
                return socket.getSendBufferSize();
            default:
                throw new IllegalArgumentException();
        }
    }

    public void setSocketOption(byte option, int value) throws IllegalArgumentException, IOException {
        checkState();
        switch(option) {
            case DELAY:
                socket.setTcpNoDelay(value != 0);
                break;
            case LINGER:
                if (value < 0) {
                    throw new IllegalArgumentException();
                }
                socket.setSoLinger(value != 0, value);
                break;
            case KEEPALIVE:
                socket.setKeepAlive(value != 0);
                break;
            case RCVBUF:
                if (value <= 0) {
                    throw new IllegalArgumentException();
                }
                socket.setReceiveBufferSize(value);
                break;
            case SNDBUF:
                if (value <= 0) {
                    throw new IllegalArgumentException();
                }
                socket.setSendBufferSize(value);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void close() throws IOException {
        System.out.println(">>> SocketConnection: close()");
        try {
            socket.shutdownInput();
        } catch (Throwable e) {
        }
        try {
            socket.shutdownOutput();
        } catch (Throwable e) {
        }
        try {
            socket.close();
        } catch (Throwable e) {
        }
    }

    public InputStream openInputStream() throws IOException {
        checkState();
        return socket.getInputStream();
    }

    public DataInputStream openDataInputStream() throws IOException {
        checkState();
        return new DataInputStream(openInputStream());
    }

    public OutputStream openOutputStream() throws IOException {
        checkState();
        return socket.getOutputStream();
    }

    public DataOutputStream openDataOutputStream() throws IOException {
        checkState();
        return new DataOutputStream(openOutputStream());
    }

    private void checkState() throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new IOException();
        }
    }

    /**
	 *  See Android Issues 6144, 7935, 7933, etc. The socket does not throw IOException
	 * when a thread is reading from it (and is blocked), and the connection is drop. It just
	 * hangs forever reading... Writing does not throw any exception either.
	 *  To work around this, we keep a list of all open socket connections (in weak refs)
	 *  and we listen for CONNECTIVITY broadcast events. When the connection is dropped,
	 *  changes status or there is a new active connection (e.g. moving from WIFI to
	 *  mobile), we close all socket connections.
	 */
    static class ConnectivityBroadcastReceiver extends BroadcastReceiver {

        private static synchronized void addSocketConnection(SocketConnection socket) throws IOException {
            if (socketBroadcastReceiver == null) {
                socketBroadcastReceiver = new ConnectivityBroadcastReceiver();
            }
            socketBroadcastReceiver.addSocketConnectionImpl(socket);
        }

        private Vector<WeakReference<SocketConnection>> socketWeakList = new Vector<WeakReference<SocketConnection>>();

        private int networkType;

        private State networkState;

        private boolean isConnected;

        private ConnectivityBroadcastReceiver() throws IOException {
            updateConnectivity();
            System.out.println(">>> SocketBroadcastReceiver: registerReceiver");
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            AndroidMeApp.getIntance().registerReceiver(this, intentFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                System.out.println(">>> SocketBroadcastReceiver: received " + intent.getAction() + ": " + intent.getExtras());
                updateConnectivity();
            } catch (Throwable e) {
                Logger.warn(e);
            }
        }

        private void addSocketConnectionImpl(SocketConnection socket) throws IOException {
            cleanSocketConnections(!isConnected);
            if (!isConnected) {
                throw new IOException("Not Connected");
            }
            socketWeakList.add(new WeakReference<SocketConnection>(socket));
        }

        private void updateConnectivity() {
            boolean isConnected = false;
            boolean hasStateChanged = false;
            ConnectivityManager connectivityManager = (ConnectivityManager) AndroidMeApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
                if (netInfo != null) {
                    State state = netInfo.getState();
                    int type = netInfo.getType();
                    isConnected = netInfo.isConnected() && netInfo.isAvailable();
                    System.out.println(">>> SocketBroadcastReceiver: state = " + state + " detail = " + netInfo.getDetailedState() + " type = " + type + " isConnected = " + isConnected + " name = " + netInfo.getTypeName());
                    if (type != networkType || state != networkState) {
                        networkType = type;
                        networkState = state;
                        hasStateChanged = true;
                    }
                }
            }
            this.isConnected = isConnected;
            if (!isConnected || hasStateChanged) {
                cleanSocketConnections(true);
            }
        }

        private void cleanSocketConnections(boolean closeConnections) {
            System.out.println(">>> SocketBroadcastReceiver: closeSocketConnections() " + socketWeakList.size());
            synchronized (socketWeakList) {
                for (int i = socketWeakList.size() - 1; i >= 0; i--) {
                    SocketConnection conn = socketWeakList.elementAt(i).get();
                    if (closeConnections) {
                        try {
                            conn.close();
                        } catch (Throwable e) {
                        }
                        conn = null;
                    }
                    if (conn == null) {
                        socketWeakList.removeElementAt(i);
                    }
                }
            }
        }
    }
}
