package eg.nileu.cis.nilestore.immutable.uploader.writer;

import se.sics.kompics.Init;
import eg.nileu.cis.nilestore.common.ComponentAddress;
import eg.nileu.cis.nilestore.common.NilestoreAddress;
import eg.nileu.cis.nilestore.immutable.file.FileInfo;

/**
 * The Class NsWriteBucketProxyInit.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class NsWriteBucketProxyInit extends Init {

    /** The self address. */
    private final NilestoreAddress selfAddress;

    /** The dest. */
    private final ComponentAddress dest;

    /** The self. */
    private final ComponentAddress self;

    /** The fileinfo. */
    private final FileInfo fileinfo;

    /** The sharenum. */
    private final int sharenum;

    /**
	 * Instantiates a new ns write bucket proxy init.
	 * 
	 * @param dest
	 *            the dest
	 * @param self
	 *            the self
	 * @param selfAddress
	 *            the self address
	 * @param fileinfo
	 *            the fileinfo
	 * @param sharenum
	 *            the sharenum
	 */
    public NsWriteBucketProxyInit(ComponentAddress dest, ComponentAddress self, NilestoreAddress selfAddress, FileInfo fileinfo, int sharenum) {
        this.dest = dest;
        this.self = self;
        this.selfAddress = selfAddress;
        this.fileinfo = fileinfo;
        this.sharenum = sharenum;
    }

    /**
	 * Gets the self address.
	 * 
	 * @return the self address
	 */
    public NilestoreAddress getSelfAddress() {
        return selfAddress;
    }

    /**
	 * Gets the sharenum.
	 * 
	 * @return the sharenum
	 */
    public int getSharenum() {
        return sharenum;
    }

    /**
	 * Gets the dest.
	 * 
	 * @return the dest
	 */
    public ComponentAddress getDest() {
        return dest;
    }

    /**
	 * Gets the self.
	 * 
	 * @return the self
	 */
    public ComponentAddress getSelf() {
        return self;
    }

    /**
	 * Gets the fileinfo.
	 * 
	 * @return the fileinfo
	 */
    public FileInfo getFileinfo() {
        return fileinfo;
    }
}
