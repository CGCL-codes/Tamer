package eg.nileu.cis.nilestore.immutable.uploader.writer.port;

import eg.nileu.cis.nilestore.common.Status;
import eg.nileu.cis.nilestore.immutable.common.ReqCompleted;

/**
 * The Class PutBlockHashDataCompleted.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class PutBlockHashDataCompleted extends ReqCompleted {

    /**
	 * Instantiates a new put block hash data completed.
	 * 
	 * @param sharenum
	 *            the sharenum
	 * @param status
	 *            the status
	 */
    public PutBlockHashDataCompleted(int sharenum, Status status) {
        super(sharenum, status);
    }
}
