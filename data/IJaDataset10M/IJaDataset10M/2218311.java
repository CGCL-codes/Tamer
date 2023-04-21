package dsrwebserver.pages.appletcomm;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import dsr.comms.CommFuncs;
import dsr.playback.ClientGameDataCommFunctions;
import dsr.playback.ClientTurnDataCommFunctions;
import dsrwebserver.DSRWebServer;
import dsrwebserver.pages.AbstractPage;

/**
 * This is used for the comms for the 3D playback.
 *
 */
public class PlaybackComms3D extends AbstractPage {

    public static final String GET_GAME_DATA = "getgamedata";

    public static final String GET_TURN_DATA = "getturndata";

    public static final String OK = "ok";

    public static final String ERROR = "error";

    private String response = "";

    public PlaybackComms3D() {
        this.content_type = "text/text";
    }

    @Override
    public void process() throws Exception {
        String post_raw = this.headers.getPostValueAsString("post");
        String post_b64_decoded = CommFuncs.Decode(post_raw);
        String post_a[] = post_b64_decoded.split(MiscCommsPage.SEP);
        try {
            for (int i = 0; i < post_a.length; i++) {
                HashMap<String, String> hashmap = MiscCommsPage.ExtractParams(post_a[i]);
                this.processCmd(hashmap);
            }
            if (response.length() == 0) {
                response = OK;
            }
        } catch (Exception ex) {
            DSRWebServer.HandleError(ex, true);
            response = ERROR;
        }
        this.content_length = response.length();
    }

    /**
	 * We only append something if it's not an OK.
	 * 
	 */
    private void processCmd(HashMap<String, String> hashmap) throws SQLException, Base64DecodingException {
        String cmd = hashmap.get("cmd");
        if (cmd == null) {
        } else if (cmd.equalsIgnoreCase(GET_GAME_DATA)) {
            response = ClientGameDataCommFunctions.GetGameDataResponse(dbs, hashmap);
        } else if (cmd.equalsIgnoreCase(GET_TURN_DATA)) {
            response = ClientTurnDataCommFunctions.GetTurnDataResponse(dbs, hashmap);
        } else {
            throw new RuntimeException("Unknown command: " + cmd);
        }
    }

    @Override
    protected void writeContent() throws IOException {
        this.writeString(response.toString());
    }
}
