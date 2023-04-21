package no.eirikb.sfs.client;

import java.io.File;
import no.eirikb.sfs.share.Share;

/**
 *
 * @author eirikb
 * @author <a href="mailto:eirikb@google.com">eirikb@google.com</a>
 */
public class LocalShare {

    private File file;

    private Share share;

    private int shares;

    private int totalShares;

    public LocalShare(File file, Share share) {
        this.file = file;
        this.share = share;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    public int getShares() {
        return shares;
    }

    public synchronized void incShares() {
        shares++;
    }

    public int getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(int totalShares) {
        this.totalShares = totalShares;
    }
}
