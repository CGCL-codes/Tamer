package googleEarthCOMApi;

import java.io.BufferedReader;
import java.io.StringReader;
import org.jawin.COMException;
import org.jawin.COMPtr;
import org.jawin.DispatchPtr;
import org.jawin.GUID;
import org.jawin.IdentityManager;

/**
 * Jawin generated code please do not edit
 *
 * Dispatch: ICameraInfoGE
 * Description: ICameraInfoGE Interface
 * Help file:
 *
 * @author JawinTypeBrowser
 */
public class ICameraInfoGE extends DispatchPtr {

    public static final GUID DIID = new GUID("{08d46bcd-af56-4175-999E-6DDC3771C64E}");

    public static final int IID_TOKEN;

    static {
        IID_TOKEN = IdentityManager.registerProxy(DIID, ICameraInfoGE.class);
    }

    /**
	 * The required public no arg constructor.
	 * <br><br>
	 * <b>Important:</b>Should never be used as this creates an uninitialized
	 * ICameraInfoGE (it is required by Jawin for some internal working though).
	 */
    public ICameraInfoGE() {
        super();
    }

    /**
	 * For creating a new COM-object with the given progid and with
	 * the ICameraInfoGE interface.
	 *
	 * @param progid the progid of the COM-object to create.
	 */
    public ICameraInfoGE(String progid) throws COMException {
        super(progid, DIID);
    }

    /**
	 * For creating a new COM-object with the given clsid and with
	 * the ICameraInfoGE interface.
	 *
	 * @param clsid the GUID of the COM-object to create.
	 */
    public ICameraInfoGE(GUID clsid) throws COMException {
        super(clsid, DIID);
    }

    /**
	 * For getting the ICameraInfoGE interface on an existing COM-object.
	 * This is an alternative to calling {@link #queryInterface(Class)}
	 * on comObject.
	 *
	 * @param comObject the COM-object to get the ICameraInfoGE interface on.
	 */
    public ICameraInfoGE(COMPtr comObject) throws COMException {
        super(comObject);
    }

    public int getIIDToken() {
        return IID_TOKEN;
    }

    public String toString() {
        String rep = "";
        try {
            rep += getAzimuth() + "\n";
            rep += getFocusPointAltitude() + "\n";
            rep += getFocusPointAltitudeMode() + "\n";
            rep += getFocusPointLatitude() + "\n";
            rep += getFocusPointLongitude() + "\n";
            rep += getRange() + "\n";
            rep += getTilt() + "\n";
        } catch (Exception p) {
            p.printStackTrace();
        }
        return rep;
    }

    public void readString(String s) {
        try {
            BufferedReader br = new BufferedReader(new StringReader(s));
            setAzimuth(Double.parseDouble(br.readLine()));
            setFocusPointAltitude(Double.parseDouble(br.readLine()));
            setFocusPointAltitudeMode(Integer.parseInt(br.readLine()));
            setFocusPointLatitude(Double.parseDouble(br.readLine()));
            setFocusPointLongitude(Double.parseDouble(br.readLine()));
            setRange(Double.parseDouble(br.readLine()));
            setTilt(Double.parseDouble(br.readLine()));
        } catch (Exception p) {
            p.printStackTrace();
        }
    }

    /**
    *
    * @return double
    **/
    public double getFocusPointLatitude() throws COMException {
        return ((Double) get("FocusPointLatitude")).doubleValue();
    }

    /**
    *
    * @param FocusPointLatitude
    **/
    public void setFocusPointLatitude(double FocusPointLatitude) throws COMException {
        put("FocusPointLatitude", FocusPointLatitude);
    }

    /**
    *
    * @return double
    **/
    public double getFocusPointLongitude() throws COMException {
        return ((Double) get("FocusPointLongitude")).doubleValue();
    }

    /**
    *
    * @param FocusPointLongitude
    **/
    public void setFocusPointLongitude(double FocusPointLongitude) throws COMException {
        put("FocusPointLongitude", FocusPointLongitude);
    }

    /**
    *
    * @return double
    **/
    public double getFocusPointAltitude() throws COMException {
        return ((Double) get("FocusPointAltitude")).doubleValue();
    }

    /**
    *
    * @param FocusPointAltitude
    **/
    public void setFocusPointAltitude(double FocusPointAltitude) throws COMException {
        put("FocusPointAltitude", FocusPointAltitude);
    }

    /**
    *
    * @return int
    **/
    public int getFocusPointAltitudeMode() throws COMException {
        return ((Integer) get("FocusPointAltitudeMode")).intValue();
    }

    /**
    *
    * @param FocusPointAltitudeMode
    **/
    public void setFocusPointAltitudeMode(int FocusPointAltitudeMode) throws COMException {
        put("FocusPointAltitudeMode", FocusPointAltitudeMode);
    }

    /**
    *
    * @return double
    **/
    public double getRange() throws COMException {
        return ((Double) get("Range")).doubleValue();
    }

    /**
    *
    * @param Range
    **/
    public void setRange(double Range) throws COMException {
        put("Range", Range);
    }

    /**
    *
    * @return double
    **/
    public double getTilt() throws COMException {
        return ((Double) get("Tilt")).doubleValue();
    }

    /**
    *
    * @param Tilt
    **/
    public void setTilt(double Tilt) throws COMException {
        put("Tilt", Tilt);
    }

    /**
    *
    * @return double
    **/
    public double getAzimuth() throws COMException {
        return ((Double) get("Azimuth")).doubleValue();
    }

    /**
    *
    * @param Azimuth
    **/
    public void setAzimuth(double Azimuth) throws COMException {
        put("Azimuth", Azimuth);
    }
}
