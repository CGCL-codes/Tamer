package sshtest;

import com.trilead.ssh2.ChannelCondition;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.Session;
import java.io.IOException;
import java.io.InputStream;

/**
 * This example shows how to use the Session.waitForCondition
 * method to implement a state machine approach for
 * proper stdout/stderr output handling in a single thread.
 * 
 * @author Christian Plattner, plattner@trilead.com
 * @version $Id: SingleThreadStdoutStderr.java,v 1.6 2007/10/15 12:49:57 cplattne Exp $
 */
public class SingleThreadStdoutStderr {

    public static void main(String[] args) {
        String hostname = "127.0.0.1";
        String username = "joe";
        String password = "joespass";
        try {
            Connection conn = new Connection(hostname);
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (isAuthenticated == false) throw new IOException("Authentication failed.");
            Session sess = conn.openSession();
            sess.execCommand("echo \"Huge amounts of text on STDOUT\"; echo \"Huge amounts of text on STDERR\" >&2");
            InputStream stdout = sess.getStdout();
            InputStream stderr = sess.getStderr();
            byte[] buffer = new byte[8192];
            while (true) {
                if ((stdout.available() == 0) && (stderr.available() == 0)) {
                    int conditions = sess.waitForCondition(ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA | ChannelCondition.EOF, 2000);
                    if ((conditions & ChannelCondition.TIMEOUT) != 0) {
                        throw new IOException("Timeout while waiting for data from peer.");
                    }
                    if ((conditions & ChannelCondition.EOF) != 0) {
                        if ((conditions & (ChannelCondition.STDOUT_DATA | ChannelCondition.STDERR_DATA)) == 0) {
                            break;
                        }
                    }
                }
                while (stdout.available() > 0) {
                    int len = stdout.read(buffer);
                    if (len > 0) System.out.write(buffer, 0, len);
                }
                while (stderr.available() > 0) {
                    int len = stderr.read(buffer);
                    if (len > 0) System.err.write(buffer, 0, len);
                }
            }
            sess.close();
            conn.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(2);
        }
    }
}
