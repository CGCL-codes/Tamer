package school.chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer extends Thread {

    private static Vector<PrintWriter> manager = new Vector<PrintWriter>();

    private int port;

    private volatile Thread t;

    private ChatThread ct;

    private ArrayList<ClientStatus> clientList = new ArrayList<ClientStatus>(0);

    public boolean shutdown = false;

    public ArrayList<ChatThread> clients = new ArrayList<ChatThread>(0);

    public static ServerSocket server;

    private boolean http = false;

    protected school.chat.MySQL ms;

    public ChatServer(int port) {
        String dburl = "" + getClass().getResource("auth.db");
        if (dburl.length() >= 5 && dburl.substring(0, 5).equals("file:")) {
            dburl = dburl.substring(5);
        }
        ms = new MySQL("org.sqlite.JDBC", dburl, true);
        this.port = port;
    }

    public void startServer() {
        try {
            server = new ServerSocket(port);
            InetAddress addr = InetAddress.getLocalHost();
            System.out.println("Chat server started @ " + addr.getHostName() + "/" + addr.getHostAddress() + ":" + port);
            while (!this.shutdown) {
                Socket client = server.accept();
                if (!isBanned(client.getInetAddress().getHostAddress())) {
                    ChatThread cl = new ChatThread(client);
                    this.clients.add(cl);
                    cl.start();
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void stopServer() {
        this.shutdown = true;
        System.out.println("Shutting down...");
        for (int i = 0; i < this.clients.size(); i++) {
            ((ChatThread) this.clients.get(i)).out.println("none~.~server~.~none~.~!connectionClosed");
        }
        closeServer();
    }

    public void closeServer() {
        try {
            server.close();
        } catch (IOException e) {
        } finally {
            System.exit(0);
        }
    }

    public void showClients() {
        for (int i = 0; i < clients.size(); i++) {
            System.err.print("\nClient #" + i + " :: " + clients.get(i) + " :: " + clients.get(i).getUserName());
        }
    }

    public void setChatRights(String uName, int rightID) {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getUserName().equals(uName)) {
                clients.get(i).setRights(rightID);
            }
        }
    }

    public boolean isBanned(String ip) {
        boolean blub = false;
        try {
            ms.connect();
            ms.exec("SELECT banntime FROM bann WHERE bannip = '" + ip + "'");
            ms.closeConnection();
            if (ms.getResult().length > 0) {
                String[][] result = ms.getResult();
                int time = Integer.parseInt(result[0][0]);
                if (time >= (System.currentTimeMillis() / 1000) - 86400) {
                    blub = true;
                }
            }
        } catch (Exception ex) {
        }
        return blub;
    }

    public boolean existChatUser(String uName) {
        return existChatUser(uName, false);
    }

    public boolean existChatUser(String uName, boolean login) {
        int i = 0;
        int j = 0;
        int nums = (login ? 1 : 0);
        boolean exist = false;
        while (!exist && i < clients.size()) {
            if (clients.get(i).getUserName().equals(uName)) {
                j++;
            }
            if (j > nums) {
                exist = true;
            }
            i++;
        }
        return exist;
    }

    public String getUserIP(String uName) {
        int i = 0;
        String ip = "";
        boolean exist = false;
        while (!exist && i < clients.size()) {
            clients.get(i).getUserName().equals(uName);
            if (clients.get(i).getUserName().equals(uName)) {
                exist = true;
                ip = clients.get(i).getIP();
            }
            i++;
        }
        return (ip.equals("") ? "FAIL" : ip);
    }

    public int getChatUserRight(String uName) {
        int i = 0;
        int right = 0;
        boolean exist = false;
        while (!exist && i < clients.size()) {
            if (clients.get(i).getUserName().equals(uName)) {
                right = clients.get(i).getRights();
                exist = true;
            }
            i++;
        }
        return right;
    }

    private String createColor() {
        String col = "";
        for (int i = 0; i < 6; i++) {
            int ran = (int) Math.round(Math.random() * ((int) (i / 2) == (i / 2) ? 12 : 15));
            switch(ran) {
                case 10:
                    col += "A";
                    break;
                case 11:
                    col += "B";
                    break;
                case 12:
                    col += "C";
                    break;
                case 13:
                    col += "D";
                    break;
                case 14:
                    col += "E";
                    break;
                case 15:
                    col += "F";
                    break;
                default:
                    col += Integer.toString(ran);
                    break;
            }
        }
        return col;
    }

    private class ClientStatus {

        private String name = "";

        private int status = -1;

        private String statusMessage = "";

        private String color = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private class ChatThread extends Thread {

        private static final int TIMEOUT = 600000;

        private Socket client;

        private String name;

        private int rights = 2;

        private boolean loggedByAuth = false;

        public BufferedReader in;

        public PrintWriter out;

        private String byeMessage = "";

        String clientAddr = "";

        private ClientStatus cs = new ClientStatus();

        final int RIGHT_ADMIN = 64;

        final int RIGHT_OPP = 32;

        final int RIGHT_OP = 16;

        final int RIGHT_USER = 2;

        final int RIGHT_UNAUTH = 1;

        final int RIGHT_NONE = 0;

        public ChatThread(Socket client) {
            this.client = client;
        }

        public int getRights() {
            return rights;
        }

        public void setRights(int rightID) {
            rights = rightID;
        }

        public String getUserName() {
            return name;
        }

        public String getIP() {
            return clientAddr;
        }

        @Override
        public void run() {
            clientAddr = client.getInetAddress().getHostAddress();
            int clientPort = client.getPort();
            try {
                client.setSoTimeout(TIMEOUT);
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
                login();
                System.out.println("connected to " + name + " @ " + clientAddr + ":" + clientPort);
                String message;
                String[] inComing;
                while ((inComing = in.readLine().split("~.~")) != null && !this.isInterrupted()) {
                    message = inComing[2];
                    if (inComing[1].equals("server") && message.charAt(0) == '/') {
                        if (parseCommand(message.substring(1), inComing[0]) && cs.status == 2) {
                            try {
                                client.setSoTimeout(TIMEOUT);
                            } catch (SocketException se) {
                            }
                            cs.statusMessage = "";
                            cs.status = 1;
                        }
                    } else if (inComing[1].equals("public") && rights > RIGHT_UNAUTH) {
                        sendMessage(inComing[0] + "~.~" + inComing[1] + "~.~" + name + "~.~" + message);
                        if (cs.status == 2) {
                            try {
                                client.setSoTimeout(TIMEOUT);
                            } catch (SocketException se) {
                            }
                            cs.statusMessage = "";
                            cs.status = 1;
                        }
                    }
                }
                in.close();
                out.close();
            } catch (IOException e) {
                System.err.println(e);
            } finally {
                logout();
                try {
                    if (client != null) client.close();
                } catch (IOException e) {
                }
                System.out.println("Disconnected from " + clientAddr + ":" + clientPort + " : " + name);
            }
        }

        private boolean existUser(String uName) {
            boolean exist = false;
            try {
                ms.connect();
                ms.exec("SELECT userid FROM user WHERE nick = '" + uName + "'");
                ms.closeConnection();
                String[][] ergebnis = ms.getResult();
                if (ergebnis.length > 0) {
                    exist = true;
                }
            } catch (Exception ex) {
                exist = false;
            }
            return exist;
        }

        private boolean register(String uName, String password) {
            password = school.chat.BCrypt.hashpw(password, school.chat.BCrypt.gensalt());
            boolean result = false;
            try {
                if (existUser(uName)) {
                    result = false;
                } else {
                    ms.connect();
                    ms.exec("INSERT INTO user (nick,pass,rights) VALUES ('" + uName + "', '" + password + "',2)");
                    ms.closeConnection();
                    result = true;
                }
            } catch (Exception ex) {
                System.err.println("Register Error: " + ex);
            }
            return result;
        }

        private int login(String userName, String password) {
            int result = 0;
            try {
                ms.connect();
                ms.exec("SELECT rights,pass FROM user WHERE nick = '" + userName + "'");
                ms.closeConnection();
                String ergebnis[][] = ms.getResult();
                if (school.chat.BCrypt.checkpw(password, ergebnis[0][1])) {
                    result = Integer.parseInt(ergebnis[0][0]);
                    rights = result;
                    loggedByAuth = true;
                    int i = 0;
                    boolean bs = true;
                    while (i < clientList.size() && bs) {
                        if (clientList.get(i).getName().equals(name)) {
                            bs = false;
                            clientList.get(i).setName(userName);
                        }
                        i++;
                    }
                } else {
                    result = 0;
                }
            } catch (Exception ex) {
            }
            return result;
        }

        private void login() throws IOException {
            manager.add(out);
            name = in.readLine();
            if (name.length() >= 8 && name.substring(name.length() - 8, name.length()).equals("HTTP/1.1")) {
                http = true;
                out.println("<html>");
                out.println("\t<head>");
                out.println("\t\t<title>You need the Chat client to join the Server!</title>");
                out.println("\t</head>");
                out.println("\t<body>");
                out.println("\t\tYou need the Chat client to join this server!<br/>");
                out.println("\t\t<a href=\"http://code.google.com/p/school-java-chat/downloads/list\">Please download here!</a><br/>");
                out.println("\t\tOr <a href=\"http://code.google.com/p/school-java-chat/\">visit our google code page!</a>");
                out.println("\t</body>");
                out.println("</html>");
            } else {
                cs.name = name;
                cs.status = 1;
                cs.color = createColor();
                clientList.add(cs);
                if (existUser(name)) {
                    out.println("none~.~server~.~none~.~!login auth &Please authentify!");
                    rights = RIGHT_UNAUTH;
                } else if (!existChatUser(name, true) && !existUser(name)) {
                    sendPlain("none~.~server~.~none~.~!joined " + cs.color + "&" + name);
                } else {
                    out.println("none~.~server~.~none~.~!login fail true &Name already in use!");
                    manager.remove(out);
                }
            }
        }

        private void logout() {
            if (http) {
            } else {
                clientList.remove(cs);
                if (!shutdown) {
                    sendPlain("none~.~server~.~none~.~!left " + name + "&" + byeMessage);
                }
            }
            clients.remove(this);
            manager.remove(out);
            if (shutdown) {
                closeServer();
            }
        }

        private boolean setRights(String uname, String rightID) {
            boolean result = false;
            setChatRights(uname, Integer.parseInt(rightID));
            try {
                ms.connect();
                ms.exec("UPDATE user SET rights = " + rightID + " WHERE nick = '" + uname + "'");
                ms.closeConnection();
                result = true;
            } catch (Exception ex) {
                result = false;
            }
            return result;
        }

        private boolean setDescription(String description, String uname) {
            boolean result = false;
            try {
                ms.connect();
                ms.exec("UPDATE user SET description = " + description + " WHERE nick = '" + uname + "'");
                ms.closeConnection();
                result = true;
            } catch (Exception ex) {
                result = false;
            }
            return result;
        }

        private boolean setNewPassword(String password, String uname) {
            boolean result = false;
            password = school.chat.BCrypt.hashpw(password, school.chat.BCrypt.gensalt());
            try {
                ms.connect();
                ms.exec("UPDATE user SET pass = " + password + " WHERE nick = '" + uname + "'");
                ms.closeConnection();
                result = true;
            } catch (Exception ex) {
                result = false;
            }
            return result;
        }

        private void bannUser(String uName, String banner) {
            try {
                ms.connect();
                ms.exec("SELECT userid FROM user WHERE nick == '" + banner + "'");
                ms.closeConnection();
                String result[][] = ms.getResult();
                ms.connect();
                ms.exec("INSERT INTO bann (bannip,banntime,bannedName,bannedBy) VALUES ('" + getUserIP(uName) + "','" + (System.currentTimeMillis() / 1000) + "','" + uName + "'," + result[0][0] + ")");
                ms.closeConnection();
            } catch (Exception ex) {
            }
        }

        public boolean parseCommand(String comm, String room) {
            boolean goOnline = false;
            String[] command = comm.split(" ");
            if (command[0].equals("shutdown") && rights >= RIGHT_ADMIN) {
                stopServer();
            } else if (command[0].equals("scl") && rights >= RIGHT_ADMIN) {
                showClients();
            } else if (command[0].equals("kick") && rights >= RIGHT_OP) {
                if (command.length == 2) {
                    if (existChatUser(command[1])) {
                        int oppRight = getChatUserRight(command[1]);
                        if (oppRight < rights) {
                            sendPlain(room + "~.~server~.~none~.~!kick " + command[1] + " " + name);
                        } else {
                            out.println(room + "~.~server~.~none~.~!kick fail &You have no sufficient rights!");
                        }
                    } else {
                        out.println(room + "~.~server~.~none~.~!kick fail &" + command[1] + " not logged in!");
                    }
                } else {
                    out.println(room + "~.~server~.~none~.~!kick fail &Missing parameters!");
                }
            } else if (command[0].equals("bann") && rights >= RIGHT_OPP) {
                if (command.length == 2) {
                    if (existChatUser(command[1])) {
                        int oppRight = getChatUserRight(command[1]);
                        if (oppRight < rights) {
                            bannUser(command[1], name);
                            sendPlain(room + "~.~server~.~none~.~!bann " + command[1] + " " + name);
                        } else {
                            out.println(room + "~.~server~.~none~.~!bann fail &You have no sufficient rights!");
                        }
                    } else {
                        out.println(room + "~.~server~.~none~.~!bann fail &" + command[1] + " not logged in!");
                    }
                } else {
                    out.println(room + "~.~server~.~none~.~!bann fail &Missing parameters!");
                }
            } else if (command[0].equals("login") && rights > RIGHT_NONE) {
                if (command.length == 3) {
                    int logg = login(command[1], command[2]);
                    if (logg > 0) {
                        out.println(room + "~.~server~.~none~.~!login success " + command[1] + " " + logg);
                        sendPlain(room + "~.~server~.~none~.~!login all " + name + " " + command[1]);
                        name = command[1];
                    } else {
                        out.println(room + "~.~server~.~none~.~!login fail &Komisch O_o!");
                    }
                } else {
                    out.println(room + "~.~server~.~none~.~!login fail &Missing parameters!");
                }
            } else if (command[0].equals("register") && rights > RIGHT_UNAUTH) {
                if (command.length == 3) {
                    boolean logg = register(command[1], command[2]);
                    if (logg) {
                        out.println(room + "~.~server~.~none~.~!register success " + command[1]);
                    } else {
                        out.println(room + "~.~server~.~none~.~!register fail &Komisch O_o!");
                    }
                } else {
                    out.println(room + "~.~server~.~none~.~!register fail &Missing parameters!");
                }
            } else if (command[0].equals("sr") && rights >= RIGHT_OPP) {
                if (command.length == 3) {
                    if (setRights(command[1], command[2])) {
                        out.println(room + "~.~server~.~none~.~!sr success " + command[1] + " " + command[2]);
                    } else {
                        out.println(room + "~.~server~.~none~.~!sr fail &Komisch O_o");
                    }
                } else {
                    out.println(room + "~.~server~.~none~.~!sr fail &Missing parameters!");
                }
            } else if (command[0].equals("me") && rights > RIGHT_UNAUTH) {
                sendPlain(room + "~.~server~.~none~.~!me " + name + (comm.length() > 3 ? "&" + comm.substring(3, comm.length()) : ""));
                goOnline = true;
            } else if (command[0].equals("afk") && rights > RIGHT_UNAUTH) {
                sendPlain(room + "~.~server~.~none~.~!afk " + name + "&" + (comm.length() > 4 ? comm.substring(4, comm.length()) : ""));
                try {
                    client.setSoTimeout(0);
                    cs.statusMessage = (comm.length() > 4 ? comm.substring(4, comm.length()) : "");
                    cs.status = 2;
                } catch (SocketException se) {
                }
            } else if (command[0].equals("quit")) {
                out.println(room + "~.~server~.~none~.~!quit");
                if (command.length > 1) {
                    byeMessage = comm.substring(5, comm.length());
                }
            } else if (command[0].equals("topic")) {
                if (command.length > 1) {
                    sendPlain(room + "~.~server~.~none~.~!topic " + comm.substring(6, comm.length()));
                }
            } else if (command[0].equals("userlist") && rights > RIGHT_NONE) {
                String tmp = "";
                if (command.length >= 2 && command[1].equals("plain")) {
                    tmp = "userlist ";
                    for (int i = 0; i < clientList.size(); i++) {
                        ClientStatus cs = (ClientStatus) clientList.get(i);
                        tmp += cs.name + "&" + cs.color + "&" + cs.status + "&" + cs.statusMessage;
                        if (i < clientList.size() - 1) {
                            tmp += "~";
                        }
                    }
                } else {
                    tmp = "online user:<br/>";
                    for (int i = 0; i < clientList.size(); i++) {
                        ClientStatus cs = (ClientStatus) clientList.get(i);
                        tmp += cs.name;
                        switch(cs.status) {
                            case 2:
                                tmp += " &lt;afk&gt; " + cs.statusMessage;
                        }
                        tmp += " <span style=\"color:#" + cs.color + ";\">" + cs.color + "</span>";
                        if (i < clientList.size() - 1) {
                            tmp += "<br/>";
                        }
                    }
                }
                out.println(room + "~.~server~.~none~.~!" + tmp);
            } else if (command[0].equals("help")) {
                out.println(room + "~.~server~.~none~.~!help <ol><li>/me <i>msg</i> - Say what you are doing right now!</li><li>/afk <i>msg</i> - Lets you go idle with leaving a message</i></li><li>/quit <i>msg</i> - Lets you leave the server with leaving a message </li><li>/register <i>username password</i> - Let's you register an account on the current joined server.</li><li>/login <i>username password</i> - Let's you login with your registered account</li><li>/userlist - Let's you know which users are online.</li></ol>");
            } else {
                out.println(room + "~.~server~.~none~.~!command '" + comm + "' not found!");
            }
            return goOnline;
        }

        private void sendMessage(String message) {
            synchronized (manager) {
                for (PrintWriter out : manager) {
                    out.println(message);
                }
            }
        }

        private void sendPlain(String message) {
            synchronized (manager) {
                for (PrintWriter out : manager) {
                    out.println(message);
                }
            }
        }
    }

    @Override
    public void run() {
        this.startServer();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            new school.chat.ChatServerGUI("ChatServer");
        } else {
            int port = Integer.parseInt(args[0]);
            new ChatServer(port).startServer();
        }
    }
}
