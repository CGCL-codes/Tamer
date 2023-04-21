package lx.ghm.xelerator.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import sun.misc.BASE64Encoder;

public class SMTPClient {

    private boolean debug = true;

    BASE64Encoder encode = new BASE64Encoder();

    private Socket socket;

    public SMTPClient(String server, int port) throws UnknownHostException, IOException {
        try {
            socket = new Socket(server, 25);
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Connect failed!");
        }
    }

    public void helo(String server, BufferedReader in, BufferedWriter out) throws IOException {
        int result;
        result = getResult(in);
        if (result != 220) {
            throw new IOException("Connection failed");
        }
        result = sendServer("HELO " + server, in, out);
        if (result != 250) {
            throw new IOException("HELO Failed");
        }
    }

    private int sendServer(String str, BufferedReader in, BufferedWriter out) throws IOException {
        out.write(str);
        out.newLine();
        out.flush();
        if (debug) {
            System.out.println("sendServer Failed!" + str);
        }
        return getResult(in);
    }

    public int getResult(BufferedReader in) {
        String line = "";
        try {
            line = in.readLine();
            if (debug) {
                System.out.println("getResult from sever failed!" + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringTokenizer st = new StringTokenizer(line, " ");
        return Integer.parseInt(st.nextToken());
    }

    public void authLogin(MailMessage message, BufferedReader in, BufferedWriter out) throws IOException {
        int result;
        result = sendServer("AUTH LOGIN", in, out);
        if (result != 334) {
            throw new IOException("AUTH LOGIN failed");
        }
        result = sendServer(encode.encode(message.getUser().getBytes()), in, out);
        if (result != 334) {
            throw new IOException("");
        }
        result = sendServer(encode.encode(message.getPassword().getBytes()), in, out);
        if (result != 235) {
            throw new IOException("auth login failed!");
        }
    }

    public void mailfrom(String source, BufferedReader in, BufferedWriter out) throws IOException {
        int result;
        result = sendServer("MAIL FROM:<" + source + ">", in, out);
        if (result != 250) {
            throw new IOException("MAIL FROM failed!");
        }
    }

    public void rcpt(String touchman, BufferedReader in, BufferedWriter out) throws IOException {
        int result;
        result = sendServer("RCPT TO:<" + touchman + ">", in, out);
        if (result != 250) {
            throw new IOException("RCPT TO: failed!");
        }
    }

    public void data(String from, String to, String subject, String content, BufferedReader in, BufferedWriter out) throws IOException {
        int result;
        result = sendServer("DATA", in, out);
        if (result != 354) {
            throw new IOException("DATA: failed");
        }
        out.write("From: " + from);
        out.newLine();
        out.write("To: " + to);
        out.newLine();
        out.write("Subject: " + subject);
        out.newLine();
        out.newLine();
        out.write(content);
        out.newLine();
        result = sendServer(".", in, out);
        System.out.println(result);
        if (result != 250) {
            throw new IOException("250: failed!");
        }
    }

    public void quit(BufferedReader in, BufferedWriter out) throws IOException {
        int result;
        result = sendServer("QUIT", in, out);
        if (result != 221) {
            throw new IOException("quit error!");
        }
    }

    public boolean sendMail(MailMessage message, String server) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            helo(server, in, out);
            if (message.getUser().length() > 0) {
                authLogin(message, in, out);
            }
            mailfrom(message.getFrom(), in, out);
            rcpt(message.getTo(), in, out);
            data(message.getDatafrom(), message.getDatato(), message.getSubject(), message.getContent(), in, out);
            quit(in, out);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
