package fbIntegration;

import requestHandler.RequestQueue;
import java.net.*;
import java.io.*;

public class Messages {

    private static String fbAppSecret = "5cfac41ccd9679c8e03eaaa387b01cd8";

    private static String profileId = "100002306492505";

    private static String access_token = "195495487137724|ddb762bb76dcc6e99b27583b.1-100002306492505|GzXH5aBY9N2Dexc3KTYZ-_UyeBU";

    public RequestQueue getMessages() {
        RequestQueue messages = new RequestQueue();
        return messages;
    }
}
