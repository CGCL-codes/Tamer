package jocundmail;

import java.io.*;
import java.net.*;
import java.lang.*;

/**
 * This is a class to facilitate mail server communication. At the moment it
 * supports only POP communication, with plain text authentication. It should
 * be expanded to other protocols and authentication types.
 *
 * @.progress (5/28/07)
 * There is a very basic implementation that supports plain text AUTH and then
 * a few ways of retrievign messages from the server.
 *
 * @.todo New mail detection / notification
 * @.todo Other AUTH methods
 *
 * @.goals MailReader
 * @.goals 0.4.0 Basic implementation for intial testing. DONE;
 *
 * @.goals 0.8.0 New Mail Detection
 *
 * @.goals 1.0.0 First release version - More specifics to come.
 *
 * @version 0.4.0
 * @author J.J. Sestrich
 */
public class MailReader {

    /** At the moment public, but will probably be changed to be protected */
    public int total_count;

    /** At the moment public, but will probably be changed to be protected */
    public int total_size;

    /** At the moment public, but will probably be changed to be protected */
    public int dirty_count;

    /** At the moment public, but will probably be changed to be protected */
    public int dirty_size;

    /** At the moment public, but will probably be changed to be protected */
    public boolean box_dirty;

    /** At the moment public, but will probably be changed to be protected */
    public Socket mail_serv;

    /** At the moment public, but will probably be changed to be protected */
    public InputStream istream;

    /** At the moment public, but will probably be changed to be protected */
    public OutputStream ostream;

    /** At the moment public, but will probably be changed to be protected */
    public BufferedReader inbuf;

    /** At the moment public, but will probably be changed to be protected */
    public BufferedWriter outbuf;

    /**
     * Attempts to start a mail reading session with the host supplied. If a
     * port is not given, 110 is assumed.
     *
     * @param mail_host Should be a host in host:port form.
     */
    public MailReader(String mail_host) {
        total_count = 0;
        total_size = 0;
        box_dirty = false;
        String[] host_port = mail_host.split(":");
        String in;
        try {
            if (host_port.length > 1) {
                System.out.println("Connection to " + host_port[0] + " on port " + host_port[1]);
                mail_serv = new Socket(host_port[0], Integer.parseInt(host_port[1]));
            } else {
                System.out.println("Connection to " + host_port[0] + " on port 110");
                mail_serv = new Socket(host_port[0], 110);
            }
            istream = mail_serv.getInputStream();
            ostream = mail_serv.getOutputStream();
            inbuf = new BufferedReader(new InputStreamReader(istream));
            outbuf = new BufferedWriter(new OutputStreamWriter(ostream));
            in = inbuf.readLine();
            if (!in.startsWith("+OK")) {
                System.out.println("Didn't recieve proper ack!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * Closes the connection with the server.
     */
    public void close() {
        String in;
        try {
            sendLine("QUIT");
            in = inbuf.readLine();
            if (!in.startsWith("+OK")) {
                System.out.println("QUIT not acked!\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * Sends a line to the server after appending "\r\n"
     * @param line Line to send to the server 
     */
    public void sendLine(String line) {
        try {
            String line2 = new String(line + "\r\n");
            System.out.print(line2);
            outbuf.write(line2, 0, line2.length());
            outbuf.flush();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * Logs into the server using plain text AUTH, and given credentials
     *
     * @param user Username to login with
     * @param pass Password to login with
     */
    public boolean login(String user, String pass) {
        String in;
        try {
            System.out.println("Logging in as " + user);
            sendLine("USER " + user);
            in = inbuf.readLine();
            if (!in.startsWith("+OK")) {
                System.out.println("User not acked!\n");
                return false;
            }
            sendLine("PASS " + pass);
            in = inbuf.readLine();
            if (!in.startsWith("+OK")) {
                System.out.println("Pass not acked!\n");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * POP - Stat command
     *
     * @return The response from the server.
     */
    public String stat() {
        String in;
        try {
            System.out.println("Getting mailbox stats!");
            sendLine("STAT");
            in = inbuf.readLine();
            if (!in.startsWith("+OK")) {
                System.out.println("STAT not acked!\n");
                return null;
            }
            String[] substrs = in.split(" ");
            if (Integer.parseInt(substrs[1]) != total_count) {
                dirty_count = Integer.parseInt(substrs[1]);
                box_dirty = true;
            } else if (Integer.parseInt(substrs[2]) != total_size) {
                dirty_count = Integer.parseInt(substrs[2]);
                box_dirty = true;
            }
            return in.substring(4);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }

    /**
     * POP - Retr command...I won't garuntee this works...
     *
     * @return The message in String form.
     *
     * @param i The index of the message on the server
     */
    public String retr(int i) {
        String in;
        String[] ina = new String[3];
        String list = "";
        try {
            System.out.println("Retrieving Message " + i);
            sendLine("RETR " + i);
            in = inbuf.readLine();
            if (!in.startsWith("+OK")) {
                System.out.println("RETR not acked!\n");
                return null;
            }
            ina[1] = inbuf.readLine();
            ina[0] = inbuf.readLine();
            while (!(ina[0].compareTo(".") == 0)) {
                list = list + "\n" + ina[1];
                ina[1] = ina[0];
                ina[0] = inbuf.readLine();
                System.out.print(".");
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }

    /**
     * A debugging function...has some additional info when downloading the
     * message from the server, as well as prints out the message in byte array
     * form.
     *
     * @param msg Index of the message on the server
     */
    public void retr_test(int msg) {
        byte[] data = new byte[512];
        byte[] in = new byte[10];
        String in_data;
        int i = 0;
        int a = 0;
        int c = 0;
        int w = 0;
        int l = 0;
        String[] split;
        try {
            sendLine("LIST " + msg);
            in_data = inbuf.readLine();
            System.out.println("S: " + in_data);
            split = in_data.split(" ");
            if (split.length == 3) {
                if (split[0].compareTo("+OK") == 0) {
                    l = Integer.parseInt(split[2]);
                } else {
                    System.out.println("RETRtest: LIST not acked!\n");
                    return;
                }
            } else {
                return;
            }
            sendLine("RETR " + msg);
            in_data = inbuf.readLine();
            System.out.println("S: " + in_data);
            if (!in_data.startsWith("+OK")) {
                System.out.println("RETR not acked!\n");
            }
            in[9] = 0;
            in[8] = 0;
            in[7] = 0;
            in[6] = 0;
            in[5] = 0;
            in[4] = 0;
            in[3] = 0;
            in[2] = 0;
            in[1] = 0;
            in[0] = 0;
            int d = 0;
            int p = 0;
            int n = 0;
            while (!(in[4] == '\r' && in[3] == '\n' && in[2] == '.' && in[1] == '\r' && in[0] == '\n')) {
                in[9] = in[8];
                in[8] = in[7];
                in[7] = in[6];
                in[6] = in[5];
                in[5] = in[4];
                in[4] = in[3];
                in[3] = in[2];
                in[2] = in[1];
                in[1] = in[0];
                in[0] = (byte) istream.read();
                if (d < 9) {
                    d++;
                } else {
                    System.out.print("data[" + (i - 9) + "] = ");
                    for (int dd = 9; dd >= 0; dd--) {
                        System.out.print(in[dd] + " ");
                    }
                    for (int dd = 9; dd >= 0; dd--) {
                        if (in[dd] == '\n') {
                            System.out.print("\\n");
                        } else if (in[dd] == '\r') {
                            System.out.print("\\r");
                        } else if (in[dd] == '\t') {
                            System.out.print("\\t");
                        } else {
                            System.out.print(((char) in[dd]));
                        }
                    }
                    d = 0;
                    System.out.println("");
                }
                i++;
                if (in[1] == '\r' && in[0] == '\n') {
                    a++;
                }
                if (in[1] == '\r' && in[0] == '\n' && in[3] == '\r' && in[2] == '\n') {
                    c++;
                }
                if (in[2] == '\r' && in[1] == '\n' && in[0] == '.') {
                    p++;
                }
                if (in[0] == '\r' || in[0] == '\n' || in[0] == '\t') {
                    w++;
                }
                if ((in[0] < ' ' || in[0] > '~')) {
                    n++;
                }
            }
            System.out.print("data[" + (i - 9) + "] = ");
            for (int dd = 9; dd >= 0; dd--) {
                System.out.print(in[dd] + " ");
            }
            for (int dd = 9; dd >= 0; dd--) {
                if (in[dd] == '\n') {
                    System.out.print("\\n");
                } else if (in[dd] == '\r') {
                    System.out.print("\\r");
                } else if (in[dd] == '\t') {
                    System.out.print("\\t");
                } else {
                    System.out.print(((char) in[dd]));
                }
            }
            d = 0;
            System.out.println("");
            System.out.println("i: " + i + "  a: " + a + "  c: " + c + "  p: " + p + "  n: " + n + "  w: " + w);
            System.out.println(i + " - " + a + " - " + p + " - 2 = " + (i - a - p - 2));
            System.out.println(i + " - " + l + " = " + (i - l));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * POP - retr command -- might not work
     *
     * @return The message in byte array form
     *
     * @param i The index of the message on the server 
     */
    public byte[] retr_byte(int i) {
        byte[] in = new byte[5];
        byte[] data;
        int a, cnt;
        String in_data;
        String[] split;
        try {
            System.out.println("Retrieving Message " + i);
            sendLine("LIST " + i);
            in_data = inbuf.readLine();
            System.out.println("S: " + in_data);
            split = in_data.split(" ");
            if (split.length == 3) {
                if (split[0].compareTo("+OK") == 0) {
                    data = new byte[Integer.parseInt(split[2]) + 1024];
                    System.out.println("Message Length: " + split[2] + " bytes");
                } else {
                    System.out.println("RETR: LIST not acked!\n");
                    return null;
                }
            } else {
                return null;
            }
            sendLine("RETR " + i);
            in_data = inbuf.readLine();
            System.out.println("S: " + in_data);
            if (!in_data.startsWith("+OK")) {
                System.out.println("RETR not acked!\n");
                return null;
            }
            a = 0;
            cnt = 0;
            int cnt2 = 0;
            int datai = 0;
            int cnt3 = 0;
            istream.read(in, 0, 1);
            while (!(in[4] == '\r' && in[3] == '\n' && in[2] == '.' && in[1] == '\r' && in[0] == '\n')) {
                if (in[3] == '\r' && in[2] == '\n' && in[1] == '.' && in[0] == '\r') {
                } else if (in[3] == '\r' && in[2] == '\n' && in[1] == '.' && in[0] != '\r') {
                    data[a++] = '\n';
                    data[a++] = in[0];
                } else if (in[2] == '\r' && in[1] == '\n' && in[0] == '.') {
                } else if (in[2] == '\r' && in[1] == '\n' && in[0] == '\r') {
                    data[a++] = '\n';
                } else if (in[2] == '\r' && in[1] == '\n' && in[0] != '.') {
                    data[a++] = '\n';
                    data[a++] = in[0];
                } else if (in[1] == '\r' && in[0] == '\n') {
                } else if (in[1] == '\r' && in[0] != '\n') {
                    data[a++] = in[1];
                    data[a++] = in[0];
                } else if (in[0] == '\r') {
                } else {
                    data[a++] = in[0];
                }
                in[4] = in[3];
                in[3] = in[2];
                in[2] = in[1];
                in[1] = in[0];
                in[0] = (byte) istream.read();
                datai++;
            }
            if (a != data.length - 1024) {
                System.out.println(": " + a + " : " + datai + " : " + (data.length - 1024) + " : " + cnt3);
            }
            byte[] data2 = new byte[a];
            for (int ii = 0; ii < data2.length; ii++) {
                data2[ii] = data[ii];
            }
            return data2;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }

    /**
     * POP - retr command -- best solution
     * Downloads the message from the server into File supplied
     *
     * @param i The index of the message on the server
     * @param file The file the message is to be saved to.
     */
    public void retr(int i, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            byte[] in = new byte[5];
            int a, cnt;
            String in_data;
            System.out.println("Retrieving Message " + i);
            sendLine("RETR " + i);
            in_data = inbuf.readLine();
            System.out.println("S: " + in_data);
            if (!in_data.startsWith("+OK")) {
                System.out.println("RETR not acked!\n");
                return;
            }
            a = 0;
            cnt = 0;
            int cnt2 = 0;
            int datai = 0;
            int cnt3 = 0;
            istream.read(in, 0, 1);
            while (!(in[4] == '\r' && in[3] == '\n' && in[2] == '.' && in[1] == '\r' && in[0] == '\n')) {
                if (in[3] == '\r' && in[2] == '\n' && in[1] == '.' && in[0] == '\r') {
                } else if (in[3] == '\r' && in[2] == '\n' && in[1] == '.' && in[0] != '\r') {
                    out.write('\n');
                    out.write(in[0]);
                    a += 2;
                } else if (in[2] == '\r' && in[1] == '\n' && in[0] == '.') {
                } else if (in[2] == '\r' && in[1] == '\n' && in[0] == '\r') {
                    out.write('\n');
                    a++;
                } else if (in[2] == '\r' && in[1] == '\n' && in[0] != '.') {
                    out.write('\n');
                    out.write(in[0]);
                    a += 2;
                } else if (in[1] == '\r' && in[0] == '\n') {
                } else if (in[1] == '\r' && in[0] != '\n') {
                    out.write(in[1]);
                    out.write(in[0]);
                    a += 2;
                } else if (in[0] == '\r') {
                } else {
                    out.write(in[0]);
                    a++;
                }
                in[4] = in[3];
                in[3] = in[2];
                in[2] = in[1];
                in[1] = in[0];
                in[0] = (byte) istream.read();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /**
     * POP - list command
     *
     * @return The server response in String form
     */
    public String list() {
        String in;
        String[] ina = new String[3];
        String list = "";
        try {
            System.out.println("Listing...");
            sendLine("LIST");
            in = inbuf.readLine();
            if (!in.startsWith("+OK")) {
                System.out.println("LIST not acked!\n");
                return null;
            }
            ina[1] = inbuf.readLine();
            ina[0] = inbuf.readLine();
            while (!(ina[0].compareTo(".") == 0)) {
                list = list + "\n" + ina[1];
                ina[1] = ina[0];
                ina[0] = inbuf.readLine();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return null;
    }

    /**
     * No Implementation
     */
    public int readMail() {
        return 0;
    }

    /**
     * A test program for this class
     *
     * @param args Retrieves index args[0]
     */
    public static void main(String[] args) {
        MailReader mr = new MailReader("");
        byte[] data;
        mr.login("emailaddress", "password");
        mr.retr_test(Integer.parseInt(args[0]));
        data = mr.retr_byte(Integer.parseInt(args[0]));
        System.out.println("" + data.length);
        mr.close();
    }
}
