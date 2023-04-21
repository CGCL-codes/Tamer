package javaclient3;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaclient3.structures.*;
import javaclient3.structures.fiducial.PlayerFiducialData;
import javaclient3.structures.fiducial.PlayerFiducialFov;
import javaclient3.structures.fiducial.PlayerFiducialGeom;
import javaclient3.structures.fiducial.PlayerFiducialItem;
import javaclient3.xdr.OncRpcException;
import javaclient3.xdr.XdrBufferDecodingStream;
import javaclient3.xdr.XdrBufferEncodingStream;

/**
 * The fiducial interface provides access to devices that detect coded fiducials
 * (markers) placed in the environment. It can also be used for devices the
 * detect natural landmarks.
 * @author Radu Bogdan Rusu, Maxim Batalin, Moshe Sayag
 * @version
 * <ul>
 *      <li>v3.0 - Player 3.0 supported
 * </ul>
 */
public class FiducialInterface extends PlayerDevice {

    private static final boolean isDebugging = PlayerClient.isDebugging;

    private Logger logger = Logger.getLogger(FiducialInterface.class.getName());

    private PlayerFiducialData pfdata;

    private boolean readyPfdata = false;

    private PlayerFiducialGeom pfgeom;

    private boolean readyPfgeom = false;

    private PlayerFiducialFov pffov;

    private boolean readyPffov = false;

    private int pfid;

    private boolean readyPfid = false;

    /**
     * Constructor for FiducialInterface.
     * @param pc a reference to the PlayerClient object
     */
    public FiducialInterface(PlayerClient pc) {
        super(pc);
    }

    /**
     * Read the fiducial data packet (all fiducials).
     */
    public synchronized void readData(PlayerMsgHdr header) {
        try {
            switch(header.getSubtype()) {
                case PLAYER_FIDUCIAL_DATA_SCAN:
                    {
                        this.timestamp = header.getTimestamp();
                        byte[] buffer = new byte[8];
                        is.readFully(buffer, 0, 8);
                        XdrBufferDecodingStream xdr = new XdrBufferDecodingStream(buffer);
                        xdr.beginDecoding();
                        int fiducialsCount = xdr.xdrDecodeInt();
                        xdr.endDecoding();
                        xdr.close();
                        buffer = new byte[fiducialsCount * 100];
                        is.readFully(buffer, 0, fiducialsCount * 100);
                        xdr = new XdrBufferDecodingStream(buffer);
                        xdr.beginDecoding();
                        PlayerFiducialItem[] pfis = new PlayerFiducialItem[fiducialsCount];
                        for (int i = 0; i < fiducialsCount; i++) {
                            PlayerFiducialItem pfi = new PlayerFiducialItem();
                            pfi.setId(xdr.xdrDecodeInt());
                            PlayerPose3d pose = new PlayerPose3d();
                            PlayerPose3d upose = new PlayerPose3d();
                            pose.setPx(xdr.xdrDecodeDouble());
                            pose.setPy(xdr.xdrDecodeDouble());
                            pose.setPz(xdr.xdrDecodeDouble());
                            pose.setProll(xdr.xdrDecodeDouble());
                            pose.setPpitch(xdr.xdrDecodeDouble());
                            pose.setPyaw(xdr.xdrDecodeDouble());
                            upose.setPx(xdr.xdrDecodeDouble());
                            upose.setPy(xdr.xdrDecodeDouble());
                            upose.setPz(xdr.xdrDecodeDouble());
                            upose.setProll(xdr.xdrDecodeDouble());
                            upose.setPpitch(xdr.xdrDecodeDouble());
                            upose.setPyaw(xdr.xdrDecodeDouble());
                            pfi.setPose(pose);
                            pfi.setUpose(upose);
                            pfis[i] = pfi;
                        }
                        xdr.endDecoding();
                        xdr.close();
                        pfdata = new PlayerFiducialData();
                        pfdata.setFiducials_count(fiducialsCount);
                        pfdata.setFiducials(pfis);
                        readyPfdata = true;
                        break;
                    }
            }
        } catch (IOException e) {
            throw new PlayerException("[Fiducial] : Error reading payload: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[Fiducial] : Error while XDR-decoding payload: " + e.toString(), e);
        }
    }

    /**
     * Request/reply: Get geometry.
     * <br><br>
     * The geometry (pose and size) of the fiducial device can be queried
     * by sending a null PLAYER_FIDUCIAL_REQ_GET_GEOM request.
     * <br><br>
     * See the player_fiducial_geom structure from player.h
     */
    public void queryGeometry() {
        try {
            sendHeader(PLAYER_MSGTYPE_REQ, PLAYER_FIDUCIAL_REQ_GET_GEOM, 0);
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Fiducial] : Couldn't request PLAYER_FIDUCIAL_REQ_GET_GEOM: " + e.toString(), e);
        }
    }

    /**
     * Request/reply: Get/set sensor field of view.
     * <br><br>
     * The field of view of the fiducial device can be set using the
     * PLAYER_FIDUCIAL_REQ_GET_FOV request (response will be null), and
     * queried using a null PLAYER_FIDUCIAL_REQ_GET_FOV request.
     * <br><br>
     * See the player_fiducial_fov structure from player.h
     */
    public void queryFOV() {
        try {
            sendHeader(PLAYER_MSGTYPE_REQ, PLAYER_FIDUCIAL_REQ_GET_FOV, 0);
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Fiducial] : Couldn't request PLAYER_FIDUCIAL_REQ_GET_FOV: " + e.toString(), e);
        }
    }

    /**
     * Request/reply: Get/set sensor field of view.
     * <br><br>
     * The field of view of the fiducial device can be set using the
     * PLAYER_FIDUCIAL_REQ_GET_FOV request (response will be null), and
     * queried using a null PLAYER_FIDUCIAL_REQ_GET_FOV request.
     * <br><br>
     * See the player_fiducial_fov structure from player.h
     * @param pff a PlayerFiducialFov structure holding the required data
     */
    public void setFov(PlayerFiducialFov pff) {
        try {
            sendHeader(PLAYER_MSGTYPE_REQ, PLAYER_FIDUCIAL_REQ_SET_FOV, 12);
            XdrBufferEncodingStream xdr = new XdrBufferEncodingStream(12);
            xdr.beginEncoding(null, 0);
            xdr.xdrEncodeFloat(pff.getMin_range());
            xdr.xdrEncodeFloat(pff.getMax_range());
            xdr.xdrEncodeFloat(pff.getView_angle());
            xdr.endEncoding();
            os.write(xdr.getXdrData(), 0, xdr.getXdrLength());
            xdr.close();
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Fiducial] : Couldn't request PLAYER_FIDUCIAL_REQ_SET_FOV: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[Fiducial] : Error while XDR-encoding SET_FOV request: " + e.toString(), e);
        }
    }

    /**
     * Request/reply: Get/set fiducial ID.
     * <br><br>
     * Some fiducial finder devices display their own fiducial. Send a null
     * PLAYER_FIDUCIAL_REQ_GET_ID request to get the identifier displayed by
     * the fiducial.
     * <br><br>
     * Some devices can dynamically change the identifier they display. They
     * can use the PLAYER_FIDUCIAL_REQ_SET_ID request to allow a client to set
     * the  currently displayed value. Make the request with the
     * player_fiducial_id_t structure. The device replies with the same
     * structure with the id field set to the value it actually used. You
     * should check this value, as the device may not be able to display the
     * value you requested.
     * <br><br>
     * Currently supported by the stg_fiducial driver.
     * <br><br>
     * See the player_fiducial_id structure from player.h
     */
    public void queryFiducialVal() {
        try {
            sendHeader(PLAYER_MSGTYPE_REQ, PLAYER_FIDUCIAL_REQ_GET_ID, 0);
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Fiducial] : Couldn't request PLAYER_FIDUCIAL_REQ_GET_ID: " + e.toString(), e);
        }
    }

    /**
     * Request/reply: Get/set fiducial ID.
     * <br><br>
     * Some fiducial finder devices display their own fiducial. Send a null
     * PLAYER_FIDUCIAL_REQ_GET_ID request to get the identifier displayed by
     * the fiducial.
     * <br><br>
     * Some devices can dynamically change the identifier they display. They
     * can use the PLAYER_FIDUCIAL_REQ_SET_ID request to allow a client to set
     * the  currently displayed value. Make the request with the
     * player_fiducial_id_t structure. The device replies with the same
     * structure with the id field set to the value it actually used. You
     * should check this value, as the device may not be able to display the
     * value you requested.
     * <br><br>
     * Currently supported by the stg_fiducial driver.
     * <br><br>
     * See the player_fiducial_id structure from player.h
     * @param id the fiducial ID to be displayed
     */
    public void setFiducialVal(int id) {
        try {
            sendHeader(PLAYER_MSGTYPE_REQ, PLAYER_FIDUCIAL_REQ_SET_ID, 4);
            XdrBufferEncodingStream xdr = new XdrBufferEncodingStream(4);
            xdr.beginEncoding(null, 0);
            xdr.xdrEncodeInt(id);
            xdr.endEncoding();
            os.write(xdr.getXdrData(), 0, xdr.getXdrLength());
            xdr.close();
            os.flush();
        } catch (IOException e) {
            throw new PlayerException("[Fiducial] : Couldn't request PLAYER_FIDUCIAL_REQ_SET_ID: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[Fiducial] : Error while XDR-encoding SET_ID request: " + e.toString(), e);
        }
    }

    /**
     * Handle acknowledgement response messages.
     * @param header Player header
     */
    protected void handleResponse(PlayerMsgHdr header) {
        try {
            switch(header.getSubtype()) {
                case PLAYER_FIDUCIAL_REQ_GET_GEOM:
                    {
                        byte[] buffer = new byte[88];
                        is.readFully(buffer, 0, 88);
                        pfgeom = new PlayerFiducialGeom();
                        PlayerPose3d pose = new PlayerPose3d();
                        PlayerBbox3d size = new PlayerBbox3d();
                        PlayerBbox2d fiducial_size = new PlayerBbox2d();
                        XdrBufferDecodingStream xdr = new XdrBufferDecodingStream(buffer);
                        xdr.beginDecoding();
                        pose.setPx(xdr.xdrDecodeDouble());
                        pose.setPy(xdr.xdrDecodeDouble());
                        pose.setPz(xdr.xdrDecodeDouble());
                        pose.setProll(xdr.xdrDecodeDouble());
                        pose.setPpitch(xdr.xdrDecodeDouble());
                        pose.setPyaw(xdr.xdrDecodeDouble());
                        pfgeom.setPose(pose);
                        size.setSw(xdr.xdrDecodeDouble());
                        size.setSl(xdr.xdrDecodeDouble());
                        size.setSh(xdr.xdrDecodeDouble());
                        pfgeom.setSize(size);
                        fiducial_size.setSw(xdr.xdrDecodeDouble());
                        fiducial_size.setSl(xdr.xdrDecodeDouble());
                        pfgeom.setFiducial_size(fiducial_size);
                        xdr.endDecoding();
                        xdr.close();
                        readyPfgeom = true;
                        break;
                    }
                case PLAYER_FIDUCIAL_REQ_GET_FOV:
                    {
                        byte[] buffer = new byte[12];
                        is.readFully(buffer, 0, 12);
                        pffov = new PlayerFiducialFov();
                        XdrBufferDecodingStream xdr = new XdrBufferDecodingStream(buffer);
                        xdr.beginDecoding();
                        pffov.setMin_range(xdr.xdrDecodeFloat());
                        pffov.setMax_range(xdr.xdrDecodeFloat());
                        pffov.setView_angle(xdr.xdrDecodeFloat());
                        xdr.endDecoding();
                        xdr.close();
                        readyPffov = true;
                        break;
                    }
                case PLAYER_FIDUCIAL_REQ_SET_FOV:
                    {
                        break;
                    }
                case PLAYER_FIDUCIAL_REQ_GET_ID:
                    {
                        byte[] buffer = new byte[4];
                        is.readFully(buffer, 0, 4);
                        XdrBufferDecodingStream xdr = new XdrBufferDecodingStream(buffer);
                        xdr.beginDecoding();
                        pfid = xdr.xdrDecodeInt();
                        xdr.endDecoding();
                        xdr.close();
                        readyPfid = true;
                        break;
                    }
                case PLAYER_FIDUCIAL_REQ_SET_ID:
                    {
                        break;
                    }
                default:
                    {
                        if (isDebugging) logger.log(Level.FINEST, "[Fiducial][Debug] : " + "Unexpected response " + header.getSubtype() + " of size = " + header.getSize());
                        break;
                    }
            }
        } catch (IOException e) {
            throw new PlayerException("[Fiducial] : Error reading payload: " + e.toString(), e);
        } catch (OncRpcException e) {
            throw new PlayerException("[Fiducial] : Error while XDR-decoding payload: " + e.toString(), e);
        }
    }

    /**
     * Get the fiducial data.
     * @return an object of type PlayerFiducialData containing the requested data
     */
    public PlayerFiducialData getData() {
        return this.pfdata;
    }

    /**
     * Get the geometry data.
     * @return an object of type PlayerFiducialGeom containing the requested data
     */
    public PlayerFiducialGeom getGeom() {
        return this.pfgeom;
    }

    /**
     * Get the FOV data.
     * @return an object of type PlayerFiducialFov containing the requested data
     */
    public PlayerFiducialFov getFOV() {
        return this.pffov;
    }

    /**
     * Get the ID data.
     * @return an integer (player_fiducial_id) containing the requested data
     */
    public int getID() {
        return this.pfid;
    }

    /**
     * Check if data is available.
     * @return true if ready, false if not ready
     */
    public boolean isDataReady() {
        if (readyPfdata) {
            readyPfdata = false;
            return true;
        }
        return false;
    }

    /**
     * Check if geometry data is available.
     * @return true if ready, false if not ready
     */
    public boolean isGeomReady() {
        if (readyPfgeom) {
            readyPfgeom = false;
            return true;
        }
        return false;
    }

    /**
     * Check if FOV data is available.
     * @return true if ready, false if not ready
     */
    public boolean isFOVReady() {
        if (readyPffov) {
            readyPffov = false;
            return true;
        }
        return false;
    }

    /**
     * Check if ID data is available.
     * @return true if ready, false if not ready
     */
    public boolean isIDReady() {
        if (readyPfid) {
            readyPfid = false;
            return true;
        }
        return false;
    }
}
