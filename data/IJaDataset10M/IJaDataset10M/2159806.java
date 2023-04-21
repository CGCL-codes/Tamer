package refresher;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import state.ProgramState;

/**
 * 
 * @author Ice_Phoenix
 * 
 * Creates a Socket connection with the localhost to refresh game ports
 *
 */
public class PacketSender {

    private static ProgramState s = ProgramState.instance();

    private enum slotState {

        AVAILABLE, FULL, STARTED
    }

    private static slotState gameSlotState;

    private static byte[] refreshHeader = new byte[] { (byte) 0xf7, 0x1e };

    private static byte[] refreshPort = new byte[] { 0, 0, 0, 0, 0, (byte) 0xe0, 0x17, 0x08, 0, 0, 0 };

    private static byte[] refreshEnd = new byte[] { 0, 0x01, 0, 0x02, 0, 0x17, (byte) 0xe0, (byte) 0xc0, (byte) 0xa8, 0x01, 0x01, 0, 0, 0, 0, 0, 0, 0, 0 };

    private static final int MAX_SLOTS = 12;

    /**
	 * Refreshes all slots in a b.net game
	 */
    public static void bnetRefresh(String refresherName) {
        byte[] refreshName = refresherName.getBytes();
        byte[] refreshData = new byte[38 + refreshName.length];
        byte[] packetSize = new byte[] { (byte) (refreshData.length & 0xff), (byte) (refreshData.length >> 8) };
        System.arraycopy(refreshHeader, 0, refreshData, 0, 2);
        System.arraycopy(packetSize, 0, refreshData, 2, 2);
        System.arraycopy(s.gameCode, 0, refreshData, 4, 4);
        System.arraycopy(refreshPort, 0, refreshData, 8, 11);
        System.arraycopy(refreshName, 0, refreshData, 19, refreshName.length);
        System.arraycopy(refreshEnd, 0, refreshData, 19 + refreshName.length, 18);
        gameSlotState = slotState.AVAILABLE;
        try {
            Socket[] refreshSocket = new Socket[MAX_SLOTS];
            InputStream[] refreshReader = new InputStream[MAX_SLOTS];
            OutputStream[] refreshWriter = new OutputStream[MAX_SLOTS];
            try {
                for (int i = 0; i < MAX_SLOTS && gameSlotState == slotState.AVAILABLE; i++) {
                    refreshSocket[i] = new Socket("localhost", s.host_port);
                    refreshReader[i] = refreshSocket[i].getInputStream();
                    refreshWriter[i] = refreshSocket[i].getOutputStream();
                    refreshWriter[i].write(refreshData);
                    byte[] responsePacket = new byte[5];
                    for (int h = 0; h < responsePacket.length; h++) {
                        responsePacket[h] = (byte) refreshReader[i].read();
                    }
                    if (responsePacket[1] == 5 && responsePacket[2] == 8) {
                        if (responsePacket[4] == 9) gameSlotState = slotState.FULL; else if (responsePacket[4] == 10) gameSlotState = slotState.STARTED;
                    }
                    Thread.sleep(25);
                }
                ;
            } finally {
                for (int i = 0; i < MAX_SLOTS; i++) {
                    if (refreshWriter[i] != null) refreshWriter[i].close();
                    if (refreshReader[i] != null) refreshReader[i].close();
                    if (refreshSocket[i] != null) refreshSocket[i].close();
                }
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Connection refused")) {
                System.err.println("Error: Connection refused -\n\tMake sure the proper game port is specified under Preferences -> Network");
            }
        }
    }
}
