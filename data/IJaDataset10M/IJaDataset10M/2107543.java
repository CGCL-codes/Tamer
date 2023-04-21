package es.eucm.eadventure.comm.manager.commManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import es.eucm.eadventure.comm.AdventureApplet;
import es.eucm.eadventure.common.data.assessment.AssessmentProperty;
import es.eucm.eadventure.engine.adaptation.AdaptationEngine;
import es.eucm.eadventure.engine.core.control.Game;

/**
 * This class will handle communication with GAME�TEL system. 
 * For more details, see Ref doc: 
 * https://docs.google.com/document/d/15Lel0zUvmLpAcj3vaZuSjSbNYnlJnWkS29fXKBbintQ/edit?hl=es&authkey=CKbl47oN
 */
public class CommManagerGAMETEL extends AdventureApplet {

    /**
     * Generated
     */
    private static final long serialVersionUID = -2898258306208612357L;

    private HashMap<String, GametelCommData> dataToSend;

    private String returnURI;

    protected boolean connect;

    /**
     * Read the default parameters for the Applet
     * It first invokes superclass method to fetch attributes that are common for 
     * all Applet modes: USER_ID, RUN_ID and WINDOWED. Then it will read GAME�TEL
     * parameters: userId (tag "user-id") and returnURI (tag "return-uri").
     */
    @Override
    public void readParameters() {
        super.readParameters();
        this.returnURI = getParameter("return-uri");
        if (userId == null || userId.equals("")) this.userId = getParameter("user-id");
    }

    public boolean connect(HashMap<String, String> info) {
        if (userId != null && returnURI != null) connect = true; else connect = false;
        return false;
    }

    /**
     * Returns the type of comm layer: GAMETEL
     * @see #CommManagerApi.GAMETEL_TYPE
     */
    public int getCommType() {
        return CommManagerApi.GAMETEL_TYPE;
    }

    /**
     * Returns whether connection with the back-end was successfully established
     * or not. 
     * In this case, connection will be established if userId and runId could be
     * obtained (@see {@link #connect})
     */
    public boolean isConnected() {
        return connect;
    }

    /**
     * Sets the value of the given parameter in the back-end server. Basically it sends via POST the instruction "varName=data" to the backEnd.
     * Communication is asynchronous. Data is pushed immediately. 
     * @param data The name of the attribute to be set
     * @param varName The value of the attribute
     */
    private void sendData(String data, String varName) {
        String urlParameters = "userid=" + userId + "&" + varName + "=" + data;
        StringBuffer response = doPostCall(urlParameters);
        System.out.println("[GAMETEL] POST: " + returnURI + "?" + urlParameters + (response != null ? (" RESPONSE RECEIVED: " + response) : (" COMMUNICATION FAILED - NULL RESPONSE")));
    }

    /**
     * Makes a single post call to the back-end server, with all the data that has to be set.
     * Only params starting with #global will be set multiple times.
     * Communication is asynchronous. Data is pushed immediately.
     * 
     *  The URL is formed following the pattern:
     *  RETURN_URI?userid=USER_ID&ATTRIBUTE_1=VALUE_1&ATTRIBUTE_2=VAUE2&...&ATTRIBUTE_N=VALUE_N
     *  Example:
     *  http://gametel.eu/backend.php?userid="Manolito"&score=70&total-time=20&completed=true
     */
    private void sendStoredData() {
        if (dataToSend == null) dataToSend = new HashMap<String, GametelCommData>();
        dataToSend.put("total-time", new GametelCommData(String.valueOf(Game.getInstance().getTime()), false));
        String urlParameters = "userid=" + userId;
        for (String varName : dataToSend.keySet()) {
            if (!dataToSend.get(varName).isSent() || varName.toLowerCase().startsWith("#global")) {
                urlParameters += "&" + varName + "=" + dataToSend.get(varName).getValue();
            }
        }
        StringBuffer response = doPostCall(urlParameters);
        if (response != null) {
            for (String varName : dataToSend.keySet()) {
                dataToSend.get(varName).setSent(true);
            }
        }
        System.out.println("[GAMETEL] POST: " + returnURI + "?" + urlParameters + (response != null ? (" RESPONSE RECEIVED: " + response) : (" COMMUNICATION FAILED - NULL RESPONSE")));
    }

    /**
     * Builds and sends a POST call to the returnURI @. 
     * @param urlParameters
     * @return A StringBuffer containing the server's response, null if connection failed
     */
    private StringBuffer doPostCall(String urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        StringBuffer response = null;
        try {
            url = new URL(returnURI);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-length", Integer.toString(urlParameters.getBytes().length));
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }

    /**
     * This method is invoked when assessment rules are triggered. 
     * notifyRelevantState() receives the properties that have to be set
     * in the LMS and stores them to be sent when the game is finished.
     * When a property is set multiple times, only last value will be sent
     * to the LMS.
     */
    public void notifyRelevantState(List<AssessmentProperty> list) {
        if (dataToSend == null) {
            dataToSend = new HashMap<String, GametelCommData>();
        }
        Iterator<AssessmentProperty> it = list.iterator();
        while (it.hasNext()) {
            AssessmentProperty assessProp = it.next();
            String attribute = assessProp.getId();
            String value = assessProp.getValue();
            if (attribute != null && value != null) {
                if (dataToSend.get(attribute) != null) {
                    dataToSend.get(attribute).setValue(value);
                } else dataToSend.put(attribute, new GametelCommData(value, false));
            }
        }
    }

    public void flush() {
        sendStoredData();
    }

    public void connectionEstablished(String serverComment) {
    }

    public void connectionFailed(String serverComment) {
    }

    public void dataFromLMS(String key, String value) {
    }

    public void dataSendingFailed(String serverComment) {
    }

    public void dataSendingOK(String serverComment) {
    }

    public boolean disconnect(HashMap<String, String> info) {
        return false;
    }

    public void getAdaptedState(Set<String> properties) {
    }

    public HashMap<String, String> getInitialStates() {
        return null;
    }

    public void sendHTMLReport(String report) {
    }

    public void setAdaptationEngine(AdaptationEngine engine) {
    }

    private class GametelCommData {

        private String value;

        private boolean sent;

        public GametelCommData(String value, boolean sent) {
            super();
            this.value = value;
            this.sent = sent;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isSent() {
            return sent;
        }

        public void setSent(boolean sent) {
            this.sent = sent;
        }
    }
}
