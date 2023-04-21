package ar.com.greeneuron.nzbgui.nzbclient;

import java.io.IOException;
import java.io.InputStream;
import static ar.com.greeneuron.nzbgui.nzbclient.IOUtils.*;

class SNZBSetDownloadRateResponse extends SNZBResponseBase {

    private boolean success;

    private String text;

    public SNZBSetDownloadRateResponse() {
        super();
    }

    public SNZBSetDownloadRateResponse(InputStream in) throws IOException {
        super(in);
        this.success = readBoolean(in);
        int textLength = readInt(in);
        this.text = readString(in, textLength);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
