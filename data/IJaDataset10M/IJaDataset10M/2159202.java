package pspdash.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.Vector;
import pspdash.Settings;
import pspdash.StringUtils;

public class RepositoryServer extends Thread {

    int port = 2467;

    DataRepository data = null;

    ServerSocket serverSocket = null;

    Vector serverThreads = new Vector();

    private static final String RESTORE_DEFAULT_TOKEN = DataInterpreter.RESTORE_DEFAULT_TOKEN.saveString();

    private class RepositoryServerThread extends Thread implements DataListener {

        DataRepository data = null;

        Socket clientSocket = null;

        BufferedReader in = null;

        ObjectOutputStream out = null;

        public String dataPath = null;

        public RepositoryServerThread(DataRepository data, Socket clientSocket) {
            this.data = data;
            this.clientSocket = clientSocket;
            try {
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.flush();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String ID = in.readLine();
                String requiredTag = in.readLine();
                if (ID.startsWith("/")) dataPath = URLDecoder.decode(ID); else dataPath = data.getPath(ID);
                if (dataPath == null) dataPath = "//anonymous//";
                out.writeObject(dataPath);
                if (requiredTag.length() == 0) out.writeBoolean(true); else {
                    SimpleData d = data.getSimpleValue(data.createDataName(dataPath, requiredTag));
                    out.writeBoolean(d != null && d.test());
                }
                out.writeObject(Settings.getSettings());
                out.flush();
            } catch (IOException e) {
                printError(e);
            }
            try {
                setName(getName() + "(RepositoryServerThread" + dataPath + ")");
            } catch (Exception e) {
            }
        }

        private void debug(String msg) {
        }

        private void printError(Exception e) {
            if (threadIsRunning) {
                System.err.println("Exception: " + e);
                e.printStackTrace(System.err);
            }
        }

        private volatile boolean threadIsRunning = true;

        public void run() {
            String methodName = null;
            String dataName = null;
            String value = null;
            String prefix = null;
            while (threadIsRunning) try {
                try {
                    methodName = null;
                    methodName = in.readLine();
                } catch (SocketException se) {
                    threadIsRunning = false;
                }
                if (methodName == null || methodName.equals("quit")) threadIsRunning = false; else if (methodName.equals("putValue")) {
                    dataName = in.readLine();
                    value = in.readLine();
                    if (RESTORE_DEFAULT_TOKEN.equals(value)) data.restoreDefaultValue(dataName); else data.userPutValue(dataName, ValueFactory.create(null, value, null, null));
                } else if (methodName.equals("removeValue")) {
                    dataName = in.readLine();
                    data.removeValue(dataName);
                } else if (methodName.equals("addDataListener")) {
                    dataName = in.readLine();
                    data.addDataListener(dataName, this);
                } else if (methodName.equals("removeDataListener")) {
                    dataName = in.readLine();
                    data.removeDataListener(dataName, this);
                } else if (methodName.equals("maybeCreateValue")) {
                    dataName = in.readLine();
                    value = in.readLine();
                    prefix = in.readLine();
                    data.maybeCreateValue(dataName, value, prefix);
                } else if (methodName.equals("listDataNames")) {
                    prefix = in.readLine();
                    Vector dataNames = data.listDataNames(prefix);
                    synchronized (out) {
                        out.writeObject(dataNames);
                    }
                } else if (methodName.equals("logMessage")) {
                    value = LOG_PREFIX + in.readLine();
                    value = StringUtils.findAndReplace(value, "", "\n" + LOG_PREFIX);
                    System.out.println(value);
                } else System.err.println("RepositoryServerThread: I don't understand " + methodName);
            } catch (Exception e) {
                printError(e);
            }
            cleanup();
        }

        private static final String LOG_PREFIX = "DApplet:";

        public void dataValueChanged(DataEvent e) {
            try {
                synchronized (out) {
                    sendDataEvent(e);
                    out.flush();
                }
            } catch (Exception ex) {
                printError(ex);
            }
        }

        public void dataValuesChanged(Vector v) {
            if (v == null || !threadIsRunning) return;
            synchronized (out) {
                for (int i = v.size(); i > 0; ) try {
                    sendDataEvent((DataEvent) v.elementAt(--i));
                } catch (Exception ex) {
                    printError(ex);
                }
                try {
                    out.flush();
                } catch (Exception ex) {
                    printError(ex);
                }
            }
        }

        private void sendDataEvent(DataEvent e) throws IOException {
            if (e.getValue() == null) {
                SaveableData d = data.getValue(data.getAliasedName(e.getName()));
                if (d != null && !d.isEditable()) e = new DataEvent((Repository) e.getSource(), e.getName(), e.getID(), ImmutableStringData.EMPTY_STRING);
            }
            out.writeObject(e);
        }

        private void cleanup() {
            try {
                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                printError(e);
            }
            data.deleteDataListener(this);
        }

        public void quit() {
            threadIsRunning = false;
            interrupt();
            cleanup();
        }
    }

    private void debug(String msg) {
    }

    private void printError(Exception e) {
        if (serverIsRunning) {
            System.err.println("Exception: " + e);
            e.printStackTrace(System.err);
        }
    }

    public RepositoryServer(DataRepository r, ServerSocket socket) {
        data = r;
        serverSocket = socket;
        if (socket != null) port = socket.getLocalPort(); else port = -1;
    }

    public int getPort() {
        return port;
    }

    private volatile boolean serverIsRunning = true;

    public void handle(Socket clientSocket) {
        RepositoryServerThread newServerThread = new RepositoryServerThread(data, clientSocket);
        newServerThread.start();
        serverThreads.addElement(newServerThread);
    }

    public void run() {
        Socket clientSocket = null;
        if (serverSocket == null) return;
        while (serverIsRunning) try {
            clientSocket = serverSocket.accept();
            handle(clientSocket);
        } catch (IOException e) {
            printError(e);
        }
    }

    public void deletePrefix(String prefix) {
        for (int i = serverThreads.size(); i-- > 0; ) {
            RepositoryServerThread thread = (RepositoryServerThread) serverThreads.elementAt(i);
            if (thread.dataPath.equals(prefix)) {
                thread.quit();
                serverThreads.removeElement(thread);
            }
        }
    }

    public synchronized void quit() {
        serverIsRunning = false;
        interrupt();
        for (int i = serverThreads.size(); i-- > 0; ) ((RepositoryServerThread) serverThreads.elementAt(i)).quit();
    }
}
