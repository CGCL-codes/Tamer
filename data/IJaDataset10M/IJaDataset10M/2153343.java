package frost.hyperocha;

import frost.Core;
import frost.fileTransfer.download.FrostDownloadItem;
import hyperocha.freenet.fcp.FreenetKey;
import hyperocha.freenet.fcp.NodeMessage;
import hyperocha.freenet.fcp.dispatcher.job.CHKFileRequestJob;
import java.io.File;
import java.util.logging.Logger;

/**
 * @version $Id$
 */
public class FrostCHKFileRequestJob extends CHKFileRequestJob {

    private static Logger logger = Logger.getLogger(FrostCHKFileRequestJob.class.getName());

    private FrostDownloadItem dlItem = null;

    public FrostCHKFileRequestJob(String key, File target, FrostDownloadItem dli) {
        super(Core.getFcpVersion(), makeID(dli), FreenetKey.CHKfromString(key), target);
        dlItem = dli;
    }

    public void incomingMessage(String id, NodeMessage msg) {
        if (msg.isMessageName("DataFound")) {
            dlItem.setFileSize(new Long(msg.getLongValue("DataLength")));
            dlItem.fireValueChanged();
        }
        super.incomingMessage(id, msg);
    }

    private static String makeID(FrostDownloadItem dlItem) {
        if (dlItem == null) {
            return FHUtil.getNextJobID();
        }
        return dlItem.getGqIdentifier();
    }

    public void onSimpleProgress(boolean isFinalized, long totalBlocks, long requiredBlocks, long doneBlocks, long failedBlocks, long fatallyFailedBlocks) {
        dlItem.setFinalized(isFinalized);
        dlItem.setTotalBlocks((int) totalBlocks);
        dlItem.setRequiredBlocks((int) requiredBlocks);
        dlItem.setDoneBlocks((int) doneBlocks);
        dlItem.fireValueChanged();
    }
}
