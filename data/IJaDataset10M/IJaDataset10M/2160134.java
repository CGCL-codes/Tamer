package up2p.peer.gnutella.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;
import com.echomine.gnutella.MsgQuery;
import com.echomine.gnutella.ShareFile;
import com.echomine.gnutella.ShareFileController;

public class TestShareList implements ShareFileController {

    String FILENAME = "britney.mpg";

    String FILEPATH = "z:\\generic-master.mp3";

    ArrayList list = new ArrayList(1);

    public TestShareList() {
    }

    public int getFileCount() {
        return 1;
    }

    public String getFilename(int fileidx) {
        return FILENAME;
    }

    public String getFilePath(int fileidx) {
        return FILEPATH;
    }

    public Collection getFiles(MsgQuery query) {
        Vector listed = new Vector();
        listed.add(new ShareFile(1, FILENAME, 4000000, null));
        return listed;
    }

    public int getMaxUploads() {
        return 1;
    }

    public int getTotalSize() {
        return 4000000;
    }
}
