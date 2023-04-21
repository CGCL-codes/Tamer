package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class MultiServer {

    public static Hashtable<String, IdClient> nameList;

    public static Hashtable<Integer, IdClient> idList;

    private static int connectionCount = 0;

    public static final int serverId = 0;

    public static void main(String[] args) throws IOException {
        int port = 22224;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        ServerSocket serverSocket = null;
        boolean listening = true;
        nameList = new Hashtable<String, IdClient>();
        idList = new Hashtable<Integer, IdClient>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("N�o pode escutar porta: 4444.");
            System.exit(-1);
        }
        while (listening) {
            Socket s = serverSocket.accept();
            System.out.println("requisicao de " + s.getInetAddress().getHostAddress());
            IdClient l = new IdClient(++connectionCount, s);
            new MultiServerThread(l).start();
            System.out.println("conectou");
        }
        serverSocket.close();
    }

    public static void inserir(IdClient c) {
        nameList.put(c.getName(), c);
        idList.put(c.getId(), c);
    }

    public static void remover(IdClient c) {
        nameList.remove(c.getName());
        idList.remove(c.getId());
        System.out.println("removeu " + c.getName());
    }

    public static IdClient localizar(String n) {
        System.out.println("procurando " + n);
        return nameList.get(n);
    }

    public static IdClient localizar(int id) {
        System.out.println("procurando " + id);
        return idList.get(id);
    }
}
