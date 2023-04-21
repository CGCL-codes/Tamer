package homework.james.xiao.client;

import homework.james.xiao.util.Util;
import java.net.*;
import java.io.*;

public class Client_Socket {

    public static String HOST = "localhost";

    public static int PORT = 1234;

    public Socket socket;

    public Client_Socket() throws UnknownHostException, IOException {
        this.socket = new Socket(HOST, PORT);
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream socketOut = socket.getOutputStream();
        return new PrintWriter(socketOut, true);
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn));
    }

    public void request() {
        try {
            BufferedReader br = getReader(socket);
            PrintWriter pw = getWriter(socket);
            System.out.println("connecting to server");
            System.out.println("***************************************\n");
            String command;
            BufferedReader inBr = new BufferedReader(new InputStreamReader(new DataInputStream(System.in)));
            while (true) {
                String line;
                while (!(line = br.readLine()).equals(Util.END)) {
                    System.out.println(line);
                }
                System.out.println("input your command: ");
                command = inBr.readLine();
                pw.println(command);
                if (command.trim().equals("exit")) break;
            }
            System.out.println("Bye Bye");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws UnknownHostException, IOException {
        if (args.length == 1) {
            HOST = args[0];
        } else if (args.length == 2) {
            HOST = args[0];
            PORT = Integer.valueOf(args[1]);
        }
        new Client_Socket().request();
        Socket socket;
        String command = "";
        InputStream is;
        OutputStream os;
        DataInputStream dis;
        PrintWriter ps;
        try {
            socket = new Socket(HOST, PORT);
            System.out.println("connecting to server");
            System.out.println("************************************************");
            System.out.println("");
            is = socket.getInputStream();
            os = socket.getOutputStream();
            dis = new DataInputStream(is);
            ps = new PrintWriter(os, true);
            DataInputStream in = new DataInputStream(System.in);
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
            BufferedReader disBr = new BufferedReader(new InputStreamReader(dis));
            while (true) {
                String line;
                while (!(line = disBr.readLine()).equals(Util.END)) {
                    System.out.println(line);
                }
                System.out.println("input your command: ");
                command = inBr.readLine();
                System.out.println("you wrote: " + command);
                ps.println(command);
                if (command.trim().equals("exit")) break;
            }
            System.out.println("Bye Bye");
            dis.close();
            ps.close();
            is.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
    }
}
