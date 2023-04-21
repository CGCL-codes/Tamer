package Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.Vector;
import javax.sql.rowset.CachedRowSet;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import parser.PConstants;
import NetWork.*;

public class ClientTerminal implements NetWorkDelegate {

    public TransmissionUtil transmission;

    public String controSiteAddr;

    public int controlSitePort;

    public static long importStartTime;

    public static long importEndTime;

    public long bytesTransfered;

    public ClientTerminal() {
        transmission = new TransmissionUtil(true);
        importStartTime = 0;
        importEndTime = 0;
        bytesTransfered = 0;
    }

    public SocketConnector connectToSever(String host, int port) {
        return transmission.getConnectorByAddr(host, port);
    }

    public void didReceiveFile(int siteType, String host, int port, String fileName) {
    }

    public void didReceiveObject(int siteType, String host, int port, Object obj) {
    }

    public void didReceiveImportData(int siteType, String host, int port, Vector<String> data) {
        System.out.printf("%s\n", data.elementAt(1));
        System.out.printf("%d bytes data Transfered\n", data.elementAt(data.size() - 1));
        importEndTime = System.currentTimeMillis();
        System.out.printf("Select: Cost = %f seconds\n", (float) (importEndTime - importStartTime) / 1000.0);
        System.out.printf("Select: %d rows selected\n", data.size());
    }

    public void didReceiveString(int siteType, String host, int port, String strVal) {
        if (port == 3721 || port == 4721) {
            if (strVal.startsWith("end")) {
                ClientTerminal.importEndTime = System.currentTimeMillis();
                System.out.printf("Import Cost = %f seconds\n", (float) (importEndTime - importStartTime) / 1000.0);
                System.out.print(">");
                return;
            } else if (strVal.matches("[0-9]+")) {
                bytesTransfered = Integer.parseInt(strVal);
                return;
            }
            System.out.println(strVal);
            System.out.print(">");
        }
    }

    public void importFile(String fileName, String tableName) {
        Vector<String> vector = new Vector<String>();
        vector.add(tableName);
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
            String line = null;
            while ((line = bf.readLine()) != null) {
                vector.add(line);
            }
            SocketConnector connector = transmission.getConnectorByAddr(controSiteAddr, controlSitePort);
            connector.sendImportData(vector, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        ClientTerminal terminal = new ClientTerminal();
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        terminal.transmission.delegateVector.add(terminal);
        System.out.print("Sever:");
        terminal.controSiteAddr = inputReader.readLine();
        System.out.print("Port:");
        terminal.controlSitePort = Integer.parseInt(inputReader.readLine());
        SocketConnector connector = terminal.connectToSever(terminal.controSiteAddr, terminal.controlSitePort);
        String userInput = "";
        System.out.print(">");
        while (true) {
            userInput = inputReader.readLine();
            if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("quit")) {
                System.exit(0);
            } else {
                System.out.print(">");
                String[] splitStrings = userInput.split(" ");
                if (splitStrings[0].equalsIgnoreCase("import")) {
                    terminal.importFile(splitStrings[1].trim(), splitStrings[2].trim());
                    terminal.importStartTime = System.currentTimeMillis();
                } else {
                    if (userInput.toUpperCase().startsWith("SELECT")) {
                        terminal.importStartTime = System.currentTimeMillis();
                    }
                    connector.sendString(userInput, PConstants.CLIENT);
                }
            }
        }
    }

    public void didReceiveResultSet(int siteType, String host, int port, CachedRowSet crs) {
        try {
            int num = crs.getMetaData().getColumnCount();
            while (crs.next()) {
                for (int i = 1; i < num; i++) {
                    System.out.printf("%s\t", crs.getString(i));
                }
                System.out.printf("%s\n", crs.getString(num));
            }
            importEndTime = System.currentTimeMillis();
            System.out.printf("Select: Cost = %f seconds\n", (float) (importEndTime - importStartTime) / 1000.0);
            System.out.printf("Select: %d rows selected\n", crs.size());
            System.out.printf("Select: %d bytes data transfered\n", bytesTransfered);
        } catch (Exception e) {
        }
    }
}
