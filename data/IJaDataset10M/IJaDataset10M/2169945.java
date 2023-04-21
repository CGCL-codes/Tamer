package au.gov.naa.digipres.virus;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.TimeZone;
import au.gov.naa.digipres.dpr.util.virus.ScanInformation;

public class ScannerTester {

    private static final int SEND_DATAGRAM_LENGTH = 257;

    private static final int SCAN_DATAGRAM_LENGTH = 257;

    private static final int INFO_DATAGRAM_LENGTH = 172;

    private static final int PING_DATAGRAM_LENGTH = 1;

    private static final int SCAN_VIRUS = 1;

    private static final int SCAN_ERROR = 2;

    private static final int PING_REQUEST = 0;

    private static final int INFO_REQUEST = 1;

    private static final int SCAN_REQUEST = 2;

    private static final int INFO_REQUEST_TIMEOUT = 5000;

    private static final int PING_REQUEST_TIMEOUT = 2000;

    private static final int SCAN_REQUEST_TIMEOUT = 60000;

    private static final int PING_CHAR = 6;

    public static final String SCANNER_PROVIDER_NAME = "scanner.provider";

    public static final String SCANNER_VERSION_NUMBER = "scanner.version.number";

    public static final String SCANNER_ENGINE_VERSION = "scanner.definitions.version";

    public static final String SCANNER_DEFINITIONS_DATE = "scanner.definitions.date";

    public static final int SCANNER_PORT = 1500;

    public ScannerTester() {
        try {
            if (pingScanner(SCANNER_PORT)) {
                System.out.println("Virus Scanner is present");
            } else {
                System.out.println("Virus Scanner is not present");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            Properties scannerProps = getScannerProperties(SCANNER_PORT);
            System.out.println(scannerProps);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        scanDirectory("D:/xena_data/source/images");
    }

    private void scanDirectory(String dirName) {
        File dir = new File(dirName);
        if (dir.exists() && dir.isDirectory()) {
            File[] fileList = dir.listFiles();
            for (File element : fileList) {
                scanFile(element.getAbsolutePath());
            }
        } else {
            System.out.println(dirName + " is not a directory!");
        }
    }

    private void scanFile(String filename) {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(SCAN_REQUEST_TIMEOUT);
            InetAddress hostAddr = InetAddress.getLocalHost();
            byte[] sendBuff = new byte[SEND_DATAGRAM_LENGTH];
            sendBuff[0] = SCAN_REQUEST;
            byte[] dataArr = filename.getBytes();
            System.arraycopy(dataArr, 0, sendBuff, 1, dataArr.length);
            DatagramPacket dp = new DatagramPacket(sendBuff, sendBuff.length, hostAddr, SCANNER_PORT);
            socket.send(dp);
            byte[] recvBuff = new byte[SCAN_DATAGRAM_LENGTH];
            dp = new DatagramPacket(recvBuff, SCAN_DATAGRAM_LENGTH, hostAddr, SCANNER_PORT);
            socket.receive(dp);
            ScanInformation scanInfo;
            if (recvBuff[0] == SCAN_VIRUS) {
                boolean fileDisinfectable = recvBuff[1] == 1;
                String virusName = new String(recvBuff, 2, SCAN_DATAGRAM_LENGTH - 2).trim();
                scanInfo = new ScanInformation(filename, false, virusName, fileDisinfectable);
                System.out.println(scanInfo);
            } else if (recvBuff[0] == SCAN_ERROR) {
                String errorName = new String(recvBuff, 2, SCAN_DATAGRAM_LENGTH - 2).trim();
                System.out.println("Error scanning file: " + filename + ":\n" + errorName);
            } else {
                scanInfo = new ScanInformation(filename, true);
                System.out.println(scanInfo);
            }
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
	 * Return a Properties object containing information about the scanner such as name, version,
	 * definitions version etc
	 * @return
	 * @throws IOException 
	 */
    public static Properties getScannerProperties(int portNumber) throws IOException {
        pingScanner(portNumber);
        Properties scannerProps = new Properties();
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(INFO_REQUEST_TIMEOUT);
        InetAddress hostAddr = InetAddress.getLocalHost();
        byte[] sendBuff = new byte[SEND_DATAGRAM_LENGTH];
        sendBuff[0] = INFO_REQUEST;
        DatagramPacket dp = new DatagramPacket(sendBuff, SEND_DATAGRAM_LENGTH, hostAddr, portNumber);
        socket.send(dp);
        byte[] recvBuff = new byte[INFO_DATAGRAM_LENGTH];
        dp = new DatagramPacket(recvBuff, INFO_DATAGRAM_LENGTH, hostAddr, portNumber);
        socket.receive(dp);
        long majorVersion = (0xff & recvBuff[0]) + ((0xff & recvBuff[1]) << 8);
        long minorVersion = (0xff & recvBuff[2]) + ((0xff & recvBuff[3]) << 8);
        long patchVersion = (0xff & recvBuff[4]) + ((0xff & recvBuff[5]) << 8);
        long revisionVersion = (0xff & recvBuff[6]) + ((0xff & recvBuff[7]) << 8);
        StringBuilder versionBuilder = new StringBuilder();
        versionBuilder.append(majorVersion);
        versionBuilder.append("." + minorVersion);
        if (patchVersion != 0xffff) {
            versionBuilder.append("." + patchVersion);
            if (revisionVersion != 0xffff) {
                versionBuilder.append("." + revisionVersion);
            }
        }
        String scannerVersion = new String(recvBuff, 8, 80).trim();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        int year = (0xff & recvBuff[90]) + ((0xff & recvBuff[91]) << 8);
        Calendar definitionsCal = new GregorianCalendar(year, recvBuff[89] - 1, recvBuff[88]);
        definitionsCal.setTimeZone(TimeZone.getTimeZone("GMT"));
        String scannerName = new String(recvBuff, 92, 80).trim();
        scannerProps.put(SCANNER_ENGINE_VERSION, versionBuilder.toString());
        scannerProps.put(SCANNER_PROVIDER_NAME, scannerName);
        scannerProps.put(SCANNER_VERSION_NUMBER, scannerVersion);
        scannerProps.put(SCANNER_DEFINITIONS_DATE, formatter.format(definitionsCal.getTime()));
        socket.close();
        return scannerProps;
    }

    /**
	 * Sends a simple message to the virus scanner. If the correct reply is received then the method
	 * returns true. If an incorrect reply or the scanner times out then an IOException is thrown.
	 * @return
	 * @throws IOException
	 */
    public static boolean pingScanner(int portNumber) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(PING_REQUEST_TIMEOUT);
        InetAddress hostAddr = InetAddress.getLocalHost();
        byte[] sendBuff = new byte[SEND_DATAGRAM_LENGTH];
        sendBuff[0] = PING_REQUEST;
        DatagramPacket dp = new DatagramPacket(sendBuff, SEND_DATAGRAM_LENGTH, hostAddr, portNumber);
        socket.send(dp);
        byte[] recvBuff = new byte[PING_DATAGRAM_LENGTH];
        dp = new DatagramPacket(recvBuff, PING_DATAGRAM_LENGTH, hostAddr, portNumber);
        socket.receive(dp);
        if (recvBuff[0] != PING_CHAR) {
            throw new IOException("Invalid reply received from virus scanner ping");
        }
        socket.close();
        return true;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new ScannerTester();
    }
}
