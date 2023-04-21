package jssh;

public class SSHClientTest {

    public static void main(String[] argv) {
        Options options = new Options();
        options.setHostname("oasis.vanderbilt.edu");
        options.setPort(22);
        options.setUser("secure");
        options.setTerminalSize(80, 23);
        options.setTerminalType("xterm");
        SSHClient client = new SSHClient(options);
        try {
            client.connect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.print("OH NOES!");
        }
        System.out.print("Success!");
    }
}
