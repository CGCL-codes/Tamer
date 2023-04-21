package clientresponse;

import android.util.Log;
import clientcore.AndroidMain;
import clientcore.Question;
import java.io.IOException;
import java.util.Vector;

public class ResponseChatGlobal extends ResponseFromServer {

    private static final String TAG = ResponseChatGlobal.class.getSimpleName();

    @Override
    public short process() {
        return -2;
    }
}
