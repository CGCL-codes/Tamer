package eg.nileu.cis.nilestore.storage.immutable.reader;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Positive;
import se.sics.kompics.network.Network;
import eg.nileu.cis.nilestore.common.ComponentAddress;
import eg.nileu.cis.nilestore.connectionfd.port.CFailureDetector;
import eg.nileu.cis.nilestore.connectionfd.port.CancelNotifyonFailure;
import eg.nileu.cis.nilestore.connectionfd.port.ConnectionFailure;
import eg.nileu.cis.nilestore.connectionfd.port.NotifyonFailure;
import eg.nileu.cis.nilestore.storage.AbstractStorageServer;
import eg.nileu.cis.nilestore.storage.CloseShareFile;
import eg.nileu.cis.nilestore.storage.immutable.ImmutableShareFile;
import eg.nileu.cis.nilestore.storage.immutable.UnkownImmutableContainerVersionError;
import eg.nileu.cis.nilestore.storage.immutable.reader.port.RemoteRead;
import eg.nileu.cis.nilestore.storage.immutable.reader.port.RemoteReadResponse;

/**
 * The Class NsBucketReader.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class NsBucketReader extends ComponentDefinition {

    /** The net. */
    Positive<Network> net = requires(Network.class);

    /** The cfd. */
    Positive<CFailureDetector> cfd = requires(CFailureDetector.class);

    /** The self. */
    private ComponentAddress self;

    /** The dest. */
    private ComponentAddress dest;

    /** The filepath. */
    private String filepath;

    /** The filename. */
    private String filename;

    /** The sharefile. */
    private ImmutableShareFile sharefile;

    /** The parent. */
    private AbstractStorageServer parent;

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(NsBucketReader.class);

    /** The first read. */
    private boolean firstRead;

    /** The last accessed time. */
    private long lastAccessedTime;

    /** The max rtt. */
    private long maxRTT;

    /** The notifyon failure. */
    private NotifyonFailure notifyonFailure;

    /**
	 * Instantiates a new ns bucket reader.
	 */
    public NsBucketReader() {
        firstRead = true;
        maxRTT = 10000;
        subscribe(handleInit, control);
        subscribe(handleRead, net);
        subscribe(handleClose, net);
        subscribe(handleFailure, cfd);
    }

    /** The handle init. */
    Handler<NsBucketReaderInit> handleInit = new Handler<NsBucketReaderInit>() {

        @Override
        public void handle(NsBucketReaderInit init) {
            self = init.getSelf();
            dest = init.getDest();
            filepath = init.getFilepath();
            filename = init.getFilename();
            parent = init.getParent();
            try {
                sharefile = new ImmutableShareFile(filepath, 0, false);
                logger.info("Reader for ShareFile ({})", filename);
            } catch (IOException e) {
                logger.error("Exception while reading sharefile", e);
            } catch (UnkownImmutableContainerVersionError e) {
                logger.error("Exception while reading sharefile", e);
            }
        }
    };

    /** The handle read. */
    Handler<RemoteRead> handleRead = new Handler<RemoteRead>() {

        @Override
        public void handle(RemoteRead event) {
            update();
            int offset = event.getOffset();
            int length = event.getLength();
            logger.debug("{} got remote read [offset={}, length={}] ", new Object[] { self, offset, length });
            try {
                byte[] data = sharefile.read_share_data(offset, length);
                trigger(new RemoteReadResponse(self, dest, event.getRequestId(), data), net);
            } catch (IOException e) {
                logger.error("Exception while reading sharefile", e);
            }
        }
    };

    /** The handle close. */
    Handler<CloseShareFile> handleClose = new Handler<CloseShareFile>() {

        @Override
        public void handle(CloseShareFile event) {
            logger.info("{} got close share file from {}", self, dest);
            parent.bucketClosed(self.getId());
        }
    };

    /** The handle failure. */
    Handler<ConnectionFailure> handleFailure = new Handler<ConnectionFailure>() {

        @Override
        public void handle(ConnectionFailure event) {
            logger.info("{}: connection to {} failed", self, dest);
            parent.bucketClosed(self.getId());
        }
    };

    /**
	 * Update.
	 */
    private void update() {
        long now = System.currentTimeMillis();
        if (firstRead) {
            lastAccessedTime = now;
            firstRead = false;
        } else {
            logger.debug("{}: send cancel notifyonFailure to CFD", self);
            trigger(new CancelNotifyonFailure(notifyonFailure), cfd);
        }
        long elapsed = now - lastAccessedTime;
        if (elapsed > maxRTT) {
            maxRTT = elapsed;
        }
        lastAccessedTime = now;
        parent.updateAccessTime(self.getId(), lastAccessedTime, maxRTT);
        logger.debug("{}: maxRTT={}", self, maxRTT);
        notifyonFailure = new NotifyonFailure(maxRTT * 2, dest.getAddress());
        logger.debug("{}: trigger {}", self, notifyonFailure);
        trigger(notifyonFailure, cfd);
    }
}
