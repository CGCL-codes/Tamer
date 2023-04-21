package org.regadou.nalasys.system;

import java.io.*;
import java.util.*;
import java.net.*;
import org.regadou.nalasys.Address;

public class TcpServer extends Thread {

    private int listeningPort;

    private ServerSocket server = null;

    private Map clients = null;

    private boolean running = false;

    public TcpServer(int port) throws IOException {
        listeningPort = port;
        server = new ServerSocket(listeningPort);
        this.start();
    }

    public int getPort() {
        return listeningPort;
    }

    public boolean isRunning() {
        return running;
    }

    public void run() {
        if (running || server == null) return;
        running = true;
        if (clients == null) clients = new LinkedHashMap();
        while (running) {
            try {
                final Socket s = server.accept();
                if (s != null) {
                    new Thread(new Runnable() {

                        public void run() {
                            readClient(s);
                        }
                    }).start();
                }
            } catch (Exception e) {
                Service.debug(e.toString());
            }
            try {
                sleep(1000);
            } catch (Exception e) {
                running = false;
            }
        }
        close();
    }

    public void close() {
        running = false;
        if (server != null) {
            try {
                server.close();
            } catch (Exception e) {
            }
            server = null;
        }
        Iterator iter = clients.keySet().iterator();
        while (iter.hasNext()) close((Socket) iter.next());
    }

    private void close(Socket s) {
        try {
            s.close();
        } catch (Exception e) {
        }
        if (clients != null) {
            Stream st = (Stream) clients.remove(s);
            if (st != null) st.close();
            Context.close();
        }
    }

    private void readClient(Socket s) {
        Stream stream = null;
        try {
            stream = new Stream(s);
            String cmd = stream.getReader().readLine();
            if (cmd != null) {
                clients.put(s, stream);
                Map params = new LinkedHashMap();
                Context cx = Context.getCurrent(false);
                if (cx != null) Service.copy(cx, params); else {
                    String db = Context.getConfigValue("database");
                    if (db != null && !db.trim().equals("")) params.put("database", db.trim());
                }
                params.put("input", stream);
                params.put("output", stream);
                if (cmd.startsWith("#!/")) {
                    String[] parts = cmd.split(" ")[0].split("/");
                    cmd = parts[parts.length - 1];
                } else if (cmd.startsWith("GET ") || cmd.startsWith("POST ")) {
                    runHttpSession(cmd);
                    return;
                }
                if (Parser.getLanguageHandler(cmd) != null) {
                    params.put("language", cmd);
                    Context.init(params);
                    Service.interactive("\n? ");
                } else {
                    Context.init(params);
                    Stream st = new Stream(cmd);
                    if (!st.isReadable()) throw new RuntimeException("Cannot read from " + cmd);
                    String lang = Parser.getLanguageFromMimetype(st.getMimetype());
                    if (lang != null) Context.getCurrent().setLanguage(lang);
                    Service.write(Service.print(new Address(st).getContent(), null), null);
                }
            }
        } catch (Exception e) {
            try {
                stream.write(e + "\n");
            } catch (Exception e2) {
            }
            Service.debug("Error with tcp server communication", e);
        } finally {
            close(s);
        }
    }

    private boolean runHttpSession(String cmd) throws Exception {
        WebResponse reply = new WebResponse();
        String[] parts = cmd.split(" ");
        if (parts.length != 3) return false;
        cmd = parts[0].toUpperCase();
        if (cmd.equals("GET")) ; else if (!cmd.equals("POST") && !cmd.equals("HEAD") && !cmd.equals("PUT") && !cmd.equals("DELETE")) return false; else {
            reply.code = "400 not supported";
            reply.data = "Command " + cmd + " is not supported";
        }
        Map headers = new LinkedHashMap();
        BufferedReader input = Context.getCurrent().getInput().getReader();
        while (!cmd.equals("")) {
            cmd = input.readLine();
            if (cmd == null) break;
            int p = cmd.indexOf(':');
            if (p > 0) headers.put(cmd.substring(0, p).trim(), cmd.substring(p + 1).trim());
        }
        if (reply.code != null) {
            reply.date = new Date();
            reply.type = "text/plain";
        } else {
            try {
                String filename = parts[1].split("#")[0];
                Map params = null;
                int qmark = filename.indexOf('?');
                if (qmark >= 0) {
                    params = Types.toMap(filename.substring(qmark));
                    filename = filename.substring(0, qmark);
                }
                if (filename.startsWith("/")) filename = filename.substring(1);
                filename = filename.replace("..", "");
                String[] folders = filename.split("/");
                if (folders[folders.length - 1].lastIndexOf('.') > 0 && filename.substring(filename.indexOf('.')).toLowerCase().equals(".jsp")) {
                    Parser.webProcess(params, reply);
                } else {
                    Stream file = new Stream(filename);
                    reply.data = new Address(file).getContent();
                    reply.date = file.getDate();
                    reply.type = file.getMimetype();
                    reply.code = "200 OK";
                }
            } catch (Exception e) {
                reply.code = "500 server error";
                reply.data = e.toString();
                reply.date = new Date();
                reply.type = "text/plain";
            }
        }
        if (reply.code == null) {
            reply.code = "200 OK";
            reply.type = "text/html";
            reply.data = "Go to <a href='./'>main page</a> of this folder";
        }
        if (reply.date == null) reply.date = new Date();
        if (reply.type == null) reply.type = "text/plain";
        if (reply.data == null) reply.data = "";
        Stream output = Context.getCurrent().getOutput();
        output.write("HTTP/1.1 " + reply.code + "\nDate: " + reply.date + "\nContent-type: " + reply.type + "\n\n");
        new Address(output).setContent(reply.data);
        return true;
    }
}
