package javaclient2;

import java.io.IOException;
import javaclient2.xdr.OncRpcException;
import javaclient2.xdr.XdrBufferDecodingStream;
import javaclient2.xdr.XdrBufferEncodingStream;
import javaclient2.structures.PlayerMsgHdr;
import javaclient2.structures.log.PlayerLogGetState;

/**
 * The log interface provides start/stop control of data logging/playback.
 * A log device either writes data from one or more devices to a file, or 
 * it reads logged data from a file and plays it back as if it were being 
 * produced live by one or more devices.
 * @author Radu Bogdan Rusu
 * @version
 * <ul>
 *      <li>v2.0 - Player 2.0 supported
 * </ul>
 */
public class LogInterface extends PlayerDevice {

    private static final boolean isDebugging = PlayerClient.isDebugging;

    private PlayerLogGetState plgs;

    private boolean readyPlgs = false;

    /**
     * Constructor for LogInterface.
     * @param pc a reference to the PlayerClient object
     */
    public LogInterface(PlayerClient pc) {
        super(pc);
    }

    /**
     * Request/reply: Set write state.
     * <br><br>
     * To start or stop data logging, send a PLAYER_LOG_REQ_SET_WRITE_STATE 
     * request. Null response. 
     * <br><br>
     * See the player_log_set_write_state structure from player.h
     * @param state 0=disabled, 1=enabled 
     */
    public void setWriteState(int state) {
        try {
            sendHeader(PLAYER_MSGTYPE_REQ, PLAYER_LOG_REQ_SET_WRITE_STATE, 4);
            XdrBufferEncodingStream xdr = new XdrBufferEncodingStream(4);
            xdr.beginEncoding(null, 0);
            xdr.xdrEncodeByte((byte) state);
            xdr.endEncoding();
            os.write(xdr.getXdrData(), 0, xdr.getXdrLength());
            xdr.close();
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Log] : Couldn't request PLAYER_LOG_REQ_SET_WRITE_STATE: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[Log] : Error while XDR-encoding WRITE_STATE request: " + e.toString(), e);
        }
    }

    /**
     * Request/reply: Set playback state.
     * <br><br>
     * To start or stop data playback, send a PLAYER_LOG_REQ_SET_READ_STATE 
     * request. Null response. 
     * <br><br>
     * See the player_log_set_read_state structure from player.h
     * @param state 0=disabled, 1=enabled 
     */
    public void setReadState(int state) {
        try {
            sendHeader(PLAYER_MSGTYPE_REQ, PLAYER_LOG_REQ_SET_READ_STATE, 4);
            XdrBufferEncodingStream xdr = new XdrBufferEncodingStream(4);
            xdr.beginEncoding(null, 0);
            xdr.xdrEncodeByte((byte) state);
            xdr.endEncoding();
            os.write(xdr.getXdrData(), 0, xdr.getXdrLength());
            xdr.close();
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Log] : Couldn't request PLAYER_LOG_REQ_SET_READ_STATE: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[Log] : Error while XDR-encoding READ_STATE request: " + e.toString(), e);
        }
    }

    /**
     * Request/reply: Rewind playback.
     * <br><br>
     * TO rewind log playback to beginning of logfile, send a 
     * PLAYER_LOG_REQ_SET_READ_REWIND request. Does not affect playback state 
     * (i.e., whether it is started or stopped). Null response. 
     * <br><br>
     */
    public void rewindPlayback() {
        try {
            sendHeader(PLAYER_MSGTYPE_REQ, PLAYER_LOG_REQ_SET_READ_REWIND, 0);
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Log] : Couldn't request PLAYER_LOG_REQ_SET_READ_REWIND: " + e.toString(), e);
        }
    }

    /**
     * Request/reply: Get state.
     * <br><br>
     * To find out whether logging/playback is enabled or disabled, send a 
     * null PLAYER_LOG_REQ_GET_STATE request.
     * <br><br>
     * See the player_log_get_state structure from player.h
     */
    public void queryState() {
        try {
            sendHeader(PLAYER_MSGTYPE_REQ, PLAYER_LOG_REQ_GET_STATE, 0);
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Log] : Couldn't request PLAYER_LOG_REQ_GET_STATE: " + e.toString(), e);
        }
    }

    /**
     * Request/reply: Set filename.
     * <br><br>
     * To set the name of the file to write to when logging, send a 
     * PLAYER_LOG_REQ_SET_FILENAME request. Null response.
     * <br><br>
     * @param fileName the name of the file (max 255 chars + terminating NULL) 
     */
    public void setFileName(String fileName) {
        String temp = fileName;
        if (fileName.length() > 255) temp = fileName.substring(0, 255);
        try {
            int leftOvers = 0;
            if ((temp.length() % 4) != 0) leftOvers = 4 - (temp.length() % 4);
            int size = 4 + 4 + leftOvers + temp.length();
            sendHeader(PLAYER_MSGTYPE_CMD, PLAYER_LOG_REQ_SET_FILENAME, size);
            XdrBufferEncodingStream xdr = new XdrBufferEncodingStream(8);
            xdr.beginEncoding(null, 0);
            xdr.xdrEncodeInt(size);
            xdr.xdrEncodeByte((byte) size);
            xdr.endEncoding();
            os.write(xdr.getXdrData(), 0, xdr.getXdrLength());
            xdr.close();
            byte[] buf = new byte[leftOvers];
            os.writeBytes(temp);
            os.write(buf, 0, leftOvers);
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Log] : Couldn't request PLAYER_LOG_REQ_SET_FILENAME: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[Log] : Error while XDR-encoding SET_FILENAME request: " + e.toString(), e);
        }
    }

    /**
     * Handle acknowledgement response messages.
     * @param header Player header
     */
    protected void handleResponse(PlayerMsgHdr header) {
        try {
            switch(header.getSubtype()) {
                case PLAYER_LOG_REQ_SET_WRITE_STATE:
                    {
                        break;
                    }
                case PLAYER_LOG_REQ_SET_READ_STATE:
                    {
                        break;
                    }
                case PLAYER_LOG_REQ_SET_READ_REWIND:
                    {
                        break;
                    }
                case PLAYER_LOG_REQ_GET_STATE:
                    {
                        byte[] buffer = new byte[8];
                        is.readFully(buffer, 0, 8);
                        plgs = new PlayerLogGetState();
                        XdrBufferDecodingStream xdr = new XdrBufferDecodingStream(buffer);
                        xdr.beginDecoding();
                        plgs.setType(xdr.xdrDecodeByte());
                        plgs.setState(xdr.xdrDecodeByte());
                        xdr.endDecoding();
                        xdr.close();
                        readyPlgs = false;
                        break;
                    }
                case PLAYER_LOG_REQ_SET_FILENAME:
                    {
                        break;
                    }
                default:
                    {
                        if (isDebugging) System.err.println("[Log][Debug] : " + "Unexpected response " + header.getSubtype() + " of size = " + header.getSize());
                        break;
                    }
            }
        } catch (IOException e) {
            throw new PlayerException("[Log] : Error reading payload: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[Log] : Error while XDR-decoding payload: " + e.toString(), e);
        }
    }

    /**
     * Get the state data.
     * @return an object of type PlayerLogGetState containing the requested data 
     */
    public PlayerLogGetState getState() {
        return this.plgs;
    }

    /**
     * Check if data is available.
     * @return true if ready, false if not ready 
     */
    public boolean isDataReady() {
        if (readyPlgs) {
            readyPlgs = false;
            return true;
        }
        return false;
    }
}
