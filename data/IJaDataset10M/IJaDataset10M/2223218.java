package com.tjoris.bpmstudio;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import com.tjoris.midigateway.IMidiAction;

class BpmPlayPauseAction implements IMidiAction {

    private final String fID;

    private final String fName;

    private final URL fPlayURI;

    private final URL fPauseURI;

    private final URL fGetState;

    public BpmPlayPauseAction(final String id, final String name, final String playerNumber) throws MalformedURLException {
        fID = id;
        fName = name;
        fPlayURI = new URL("http://localhost/PLAY" + playerNumber);
        fPauseURI = new URL("http://localhost/PAUSE" + playerNumber);
        fGetState = new URL("http://localhost/GETPLAYSTATE" + playerNumber);
    }

    public String getName() {
        return fName;
    }

    public String getID() {
        return fID;
    }

    public void performAction(final int value) {
        try {
            final InputStream inputStream = fGetState.openStream();
            final char ch = (char) inputStream.read();
            inputStream.close();
            if (ch == '1') {
                fPauseURI.getContent();
            } else {
                fPlayURI.getContent();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
