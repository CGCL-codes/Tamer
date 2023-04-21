package org.pfyshnet.user2;

import java.io.File;
import org.pfyshnet.core.DownloadDecodeRequest;

public class DownloadDecodeRequester extends DownloadDecodeRequest {

    private UserReturnInterface UserReturn;

    private long UserID;

    public DownloadDecodeRequester(UserReturnInterface i, long id) {
        super();
        UserReturn = i;
        UserID = id;
    }

    public void Fail(String msg) {
        UserReturn.DownloadFailed(msg, UserID);
    }

    public void Success(Object data) {
        UserReturn.DownloadReturned((File) data, UserID);
    }
}
