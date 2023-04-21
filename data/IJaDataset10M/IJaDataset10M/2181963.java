package net.sf.jabs.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

public class SSHTest extends AbstractTestCase {

    public SSHTest(String method) {
        super(method);
    }

    public void setUp() {
        initPath();
    }

    private Connection getConnection() throws IOException {
        String hostname = "linux2";
        String username = "tester";
        String password = "t2ster!";
        Connection conn = new Connection(hostname);
        conn.connect();
        boolean isAuthenticated = conn.authenticateWithPassword(username, password);
        if (isAuthenticated == false) {
            throw new IOException("Authentication failed.");
        }
        return conn;
    }

    public void testSshConnect() {
        Connection conn = null;
        Session sess = null;
        try {
            conn = getConnection();
            sess = conn.openSession();
            sess.execCommand("uname -a && date && uptime && who");
            System.out.println("Here is some information about the remote host:");
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            while (true) {
                String line = br.readLine();
                if (line == null) break;
                System.out.println(line);
            }
            System.out.println("ExitCode: " + sess.getExitStatus());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sess.close();
            conn.close();
        }
    }

    public void testScpConnect() {
        Connection conn = null;
        try {
            conn = getConnection();
            SCPClient scp = new SCPClient(conn);
            scp.put("test/data/project-import.xml", "/tmp", "0755");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new SSHTest("testSshConnect"));
        suite.addTest(new SSHTest("testScpConnect"));
        return suite;
    }
}
