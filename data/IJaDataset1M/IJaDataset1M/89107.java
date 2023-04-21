package javaclient3;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaclient3.structures.PlayerMsgHdr;
import javaclient3.structures.rfid.PlayerRfidData;
import javaclient3.structures.rfid.PlayerRfidTag;
import javaclient3.xdr.OncRpcException;
import javaclient3.xdr.XdrBufferDecodingStream;

/**
 * The RFID interface provides access to a RFID reader (driver implementations
 * include RFID readers such as Skyetek M1 and Inside M300).
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class RFIDInterface extends PlayerDevice {

    private static final boolean isDebugging = PlayerClient.isDebugging;

    private Logger logger = Logger.getLogger(RFIDInterface.class.getName());

    private PlayerRfidData prdata;

    private boolean readyPrdata = false;

    /**
     * Constructor for RFIDInterface.
     * @param pc a reference to the PlayerClient object
     */
    public RFIDInterface(PlayerClient pc) {
        super(pc);
    }

    /**
     * Read the RFID data packet.
     */
    public synchronized void readData(PlayerMsgHdr header) {
        try {
            switch(header.getSubtype()) {
                case PLAYER_RFID_DATA_TAGS:
                    {
                        this.timestamp = header.getTimestamp();
                        byte[] buffer = new byte[8];
                        is.readFully(buffer, 0, 8);
                        XdrBufferDecodingStream xdr = new XdrBufferDecodingStream(buffer);
                        xdr.beginDecoding();
                        int tagsCount = xdr.xdrDecodeInt();
                        xdr.endDecoding();
                        xdr.close();
                        PlayerRfidTag[] prts = new PlayerRfidTag[tagsCount];
                        for (int i = 0; i < tagsCount; i++) {
                            PlayerRfidTag prt = new PlayerRfidTag();
                            buffer = new byte[12];
                            is.readFully(buffer, 0, 12);
                            xdr = new XdrBufferDecodingStream(buffer);
                            xdr.beginDecoding();
                            prt.setType(xdr.xdrDecodeInt());
                            int guidCount = xdr.xdrDecodeInt();
                            xdr.endDecoding();
                            xdr.close();
                            buffer = new byte[guidCount];
                            is.readFully(buffer, 0, guidCount);
                            if ((guidCount % 4) != 0) is.readFully(buffer, 0, 4 - (guidCount % 4));
                            prt.setGuid(buffer);
                            prts[i] = prt;
                        }
                        xdr.endDecoding();
                        xdr.close();
                        prdata = new PlayerRfidData();
                        prdata.setTags(prts);
                        readyPrdata = true;
                        break;
                    }
            }
        } catch (IOException e) {
            throw new PlayerException("[RFID] : Error reading payload: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[RFID] : Error while XDR-decoding payload: " + e.toString(), e);
        }
    }

    /**
     * Get the RFID data.
     * @return an object of type PlayerRfidData containing the requested data
     */
    public PlayerRfidData getData() {
        return this.prdata;
    }

    /**
     * Check if data is available.
     * @return true if ready, false if not ready
     */
    public boolean isDataReady() {
        if (readyPrdata) {
            readyPrdata = false;
            return true;
        }
        return false;
    }

    /**
     * Handle acknowledgement response messages
     * @param header Player header
     */
    public void handleResponse(PlayerMsgHdr header) {
        switch(header.getSubtype()) {
            case PLAYER_RFID_REQ_POWER:
                {
                    break;
                }
            case PLAYER_RFID_REQ_READTAG:
                {
                    break;
                }
            case PLAYER_RFID_REQ_WRITETAG:
                {
                    break;
                }
            case PLAYER_RFID_REQ_LOCKTAG:
                {
                    break;
                }
            default:
                {
                    if (isDebugging) logger.log(Level.FINEST, "[RFID][Debug] : " + "Unexpected response " + header.getSubtype() + " of size = " + header.getSize());
                    break;
                }
        }
    }
}
