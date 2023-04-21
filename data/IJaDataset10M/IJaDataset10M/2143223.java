package gov.nasa.jpf.network.cache;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class TraceMilestone {

    private List<Trace> stone = new ArrayList<Trace>();

    public void add(Trace t) {
        stone.add(t);
    }

    public Trace get(int state) {
        return stone.get(state);
    }
}

public class NoCacheLayer extends CacheLayer {

    private static final CacheLayer INSTANCE = new NoCacheLayer();

    private static List<TraceMilestone> traces = new ArrayList<TraceMilestone>();

    private NoCacheLayer() {
    }

    public static CacheLayer getInstance() {
        return INSTANCE;
    }

    public boolean isSocketClosed(int id) {
        return conn_list.get(id).isClosed();
    }

    protected void addNewConnection() {
        TraceMilestone stone = new TraceMilestone();
        int num_state = num_accept.size();
        for (int i = 0; i < num_state; i++) {
            stone.add(null);
        }
        traces.add(stone);
        conn_list.add(null);
    }

    void pollServer(int id) {
    }

    public void changeState(int id) {
        int max_state_id = num_accept.size() - 1;
        int num_conn = conn_list.size();
        if (id < 0) {
            return;
        }
        if (max_state_id < id) {
            max_state_id = id;
            for (int i = 0; i < num_conn; i++) {
                PhysicalConnection pc = conn_list.get(i);
                if (pc != null) {
                    Trace t = pc.getTrace();
                    traces.get(i).add(t);
                } else traces.get(i).add(null);
            }
            num_accept.add(cur_accept);
        }
    }

    public void backtrack(int id) {
        int num_conn = conn_list.size();
        for (int i = 0; i < num_conn; i++) {
            try {
                reconnect(i, id);
            } catch (IOException e) {
            }
        }
        cur_accept = num_accept.get(id);
    }

    void reconnect(int id, int state) throws IOException {
        PhysicalConnection old_conn, new_conn;
        Socket newSocket;
        Trace t = null;
        restart++;
        if (boot_peer_cmd == null) {
            newSocket = createSocket(server_addr, server_port);
        } else {
            WaitConnectionThread wait = new WaitConnectionThread();
            Thread exec = new Thread() {

                public void run() {
                    try {
                        ProcessBuilder builder = new ProcessBuilder(boot_peer_cmd);
                        builder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            wait.start();
            try {
                Thread.sleep(250);
                exec.start();
                Thread.sleep(250);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            newSocket = wait.getSocket();
        }
        new_conn = new PhysicalConnection(newSocket, 0, 0);
        if (state != 0) t = traces.get(id).get(state);
        if (t != null) {
            for (Iterator<String> i = t.iterator(); i.hasNext(); ) {
                String s = i.next();
                if (s.charAt(0) == 'r') {
                    new_conn.read();
                } else {
                    byte[] buffer = s.substring(1).getBytes();
                    new_conn.write(buffer, 0, buffer.length);
                }
            }
        }
        if (id >= conn_list.size()) {
            conn_list.add(new_conn);
        } else {
            old_conn = conn_list.get(id);
            if (old_conn != null) old_conn.close();
            conn_list.set(id, new_conn);
        }
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
        }
    }

    public boolean isNextReadBlocked(int id) {
        PhysicalConnection pc = conn_list.get(id);
        int avail = 0;
        try {
            avail = pc.available();
        } catch (IOException e) {
        }
        return avail == 0;
    }

    public int read(int id, byte[] buffer) throws IOException {
        PhysicalConnection pc;
        int available;
        int num_read;
        if (conn_list.get(id) == null) {
            reconnect(id, 0);
        }
        pc = conn_list.get(id);
        available = pc.available();
        num_read = buffer.length > available ? available : buffer.length;
        for (int i = 0; i < num_read; i++) {
            buffer[i] = (byte) pc.read();
        }
        return num_read == 0 ? -1 : num_read;
    }

    public void write(int id, byte c) throws IOException {
        PhysicalConnection pc;
        if (conn_list.get(id) == null) {
            reconnect(id, 0);
        }
        pc = conn_list.get(id);
        pc.write(c);
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
        }
    }

    void createPhysicalConnection(int id) {
    }

    public void connect(int id, InetAddress addr, int port) throws IOException {
        if (id >= conn_list.size()) {
            addNewConnection();
            server_addr = addr;
            server_port = port;
            if (!lazy_connect) {
                reconnect(id, 0);
            }
        }
    }

    public int accept(int port) throws IOException {
        int num_connection = conn_list.size();
        if (wait_socket == null) {
            wait_socket = new ServerSocket(port);
        }
        assert cur_accept <= num_connection : "there are accept calls more than the number of connections";
        if (cur_accept == num_connection) {
            addNewConnection();
            reconnect(num_connection, 0);
        }
        return cur_accept++;
    }

    public void close(int id) throws IOException {
        conn_list.get(id).close();
    }

    public int numTrees() {
        return 0;
    }
}
