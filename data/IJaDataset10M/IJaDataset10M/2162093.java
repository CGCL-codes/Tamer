package eg.nileu.cis.nilestore.immutable.downloader.segfetcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import eg.nileu.cis.nilestore.common.Status;
import eg.nileu.cis.nilestore.common.StatusMsg;
import eg.nileu.cis.nilestore.immutable.downloader.reader.port.GetBlock;
import eg.nileu.cis.nilestore.immutable.downloader.reader.port.GetBlockResponse;
import eg.nileu.cis.nilestore.immutable.downloader.reader.port.RBProxy;
import eg.nileu.cis.nilestore.immutable.downloader.segfetcher.port.AddShares;
import eg.nileu.cis.nilestore.immutable.downloader.segfetcher.port.GetSegment;
import eg.nileu.cis.nilestore.immutable.downloader.segfetcher.port.GetSegmentResponse;
import eg.nileu.cis.nilestore.immutable.downloader.segfetcher.port.SegmentFetcher;
import eg.nileu.cis.nilestore.immutable.downloader.sharefinder.Share;
import eg.nileu.cis.nilestore.immutable.downloader.sharefinder.port.NoMoreShares;
import eg.nileu.cis.nilestore.immutable.downloader.sharefinder.port.WantMoreShares;
import eg.nileu.cis.nilestore.utils.DumpUtils;
import eg.nileu.cis.nilestore.utils.Tuple;
import eg.nileu.cis.nilestore.utils.logging.Slf4jInstantiator;

/**
 * The Class NsSegmentFetcher.
 * 
 * @author Mahmoud Ismail <mahmoudahmedismail@gmail.com>
 */
public class NsSegmentFetcher extends ComponentDefinition {

    /** The segment fetcher. */
    Negative<SegmentFetcher> segmentFetcher = provides(SegmentFetcher.class);

    /** The rbp port. */
    Positive<RBProxy> rbpPort = requires(RBProxy.class);

    /** The logger. */
    private Logger logger;

    /** The required shares. */
    private int requiredShares;

    /** The storage index. */
    private String storageIndex;

    /** The shares. */
    private List<Share> shares;

    /** The current blocks. */
    private Map<Integer, byte[]> currentBlocks;

    /** The current shares. */
    private List<Share> currentShares;

    /** The active share map. */
    private Map<Integer, Share> activeShareMap;

    /** The no more shares. */
    private boolean noMoreShares;

    /** The max sharesper server. */
    private int maxSharesperServer;

    /** The shares_from_servers. */
    private Map<Address, Set<Share>> shares_from_servers;

    /** The active segment. */
    private int activeSegment;

    /**
	 * Instantiates a new ns segment fetcher.
	 */
    public NsSegmentFetcher() {
        shares = new ArrayList<Share>();
        currentBlocks = new HashMap<Integer, byte[]>();
        currentShares = new ArrayList<Share>();
        activeShareMap = new HashMap<Integer, Share>();
        shares_from_servers = new HashMap<Address, Set<Share>>();
        noMoreShares = false;
        maxSharesperServer = 1;
        activeSegment = -1;
        subscribe(handleInit, control);
        subscribe(handleAddShares, segmentFetcher);
        subscribe(handleNoMoreShares, segmentFetcher);
        subscribe(handleGetSegment, segmentFetcher);
        subscribe(handleGetBlockResponse, rbpPort);
    }

    /** The handle init. */
    Handler<NsSegmentFetcherInit> handleInit = new Handler<NsSegmentFetcherInit>() {

        @Override
        public void handle(NsSegmentFetcherInit init) {
            requiredShares = init.getRequiredShares();
            storageIndex = init.getStorageIndex();
            logger = Slf4jInstantiator.getLogger(NsSegmentFetcher.class, init.getSelf().getNickname());
            logger.info("(SI={}): initiated with K={}", storageIndex, requiredShares);
        }
    };

    /** The handle add shares. */
    Handler<AddShares> handleAddShares = new Handler<AddShares>() {

        @Override
        public void handle(AddShares event) {
            logger.info("SI={}): got AddShares from our parent", storageIndex);
            synchronized (shares) {
                shares.addAll(event.getShares());
                Collections.sort(shares);
            }
            synchronized (currentShares) {
                currentShares.addAll(event.getShares());
                Collections.sort(currentShares);
            }
            logger.debug("SI={}): global Shares={}, currentUsedShares={}", new Object[] { storageIndex, DumpUtils.dumptolog(shares), DumpUtils.dumptolog(currentShares) });
            loop();
        }
    };

    /** The handle no more shares. */
    Handler<NoMoreShares> handleNoMoreShares = new Handler<NoMoreShares>() {

        @Override
        public void handle(NoMoreShares event) {
            logger.info("SI={}): got no more shares response from my parent, currenState={}", storageIndex, String.valueOf(noMoreShares));
            if (noMoreShares) return;
            noMoreShares = true;
            if (activeSegment == -1) {
                trigger(new GetSegmentResponse(new StatusMsg(Status.Failed, "Download Failed: there are no shares for the requested file"), null), segmentFetcher);
                return;
            }
            loop();
        }
    };

    /** The handle get segment. */
    Handler<GetSegment> handleGetSegment = new Handler<GetSegment>() {

        @Override
        public void handle(GetSegment event) {
            activeSegment = event.getSegmentNum();
            logger.info("SI={}): my parent need me to fetch new segment SegmentNum={}", storageIndex, activeSegment);
            synchronized (currentShares) {
                currentShares.clear();
                currentShares.addAll(shares);
            }
            synchronized (currentBlocks) {
                currentBlocks.clear();
            }
            synchronized (activeShareMap) {
                activeShareMap.clear();
            }
            logger.debug("SI={}): global Shares={}, currentUsedShares={}", new Object[] { storageIndex, DumpUtils.dumptolog(shares), DumpUtils.dumptolog(currentShares) });
            loop();
        }
    };

    /** The handle get block response. */
    Handler<GetBlockResponse> handleGetBlockResponse = new Handler<GetBlockResponse>() {

        @Override
        public void handle(GetBlockResponse event) {
            int shnum = event.getSharenum();
            Share sh = activeShareMap.remove(shnum);
            if (event.isSucceeded()) {
                synchronized (currentBlocks) {
                    currentBlocks.put(shnum, event.getData());
                }
                logger.debug("SI={}): got block from share {}, currentBlocks.size={} for segment {}", new Object[] { storageIndex, shnum, currentBlocks.size(), activeSegment });
                if (currentBlocks.size() == requiredShares) {
                    logger.debug("SI={}): we have the required num of blocks so we can send it now", storageIndex);
                    StatusMsg status = new StatusMsg(Status.Succeeded, String.format("segment fetcher got the needed blocks for reconstructing segment%s from Shares %s", activeSegment, DumpUtils.dumptolog(currentBlocks.keySet())));
                    trigger(new GetSegmentResponse(status, currentBlocks), segmentFetcher);
                } else {
                    loop();
                }
            } else {
                logger.debug("SI={}): it seems that share {} failed, currentBlocks.size={} for segment {}", new Object[] { storageIndex, shnum, currentBlocks.size(), activeSegment });
                shares.remove(sh);
            }
        }
    };

    /**
	 * Loop.
	 */
    private void loop() {
        if (activeSegment == -1) {
            return;
        }
        while (getAllShares() < requiredShares) {
            Tuple<Boolean, Boolean> tuple = find_and_use_shares();
            logger.debug("SI={}): allShares.size={}, [currentBlocks.size={},activeShareMap.size={}], " + "(sentSomething,wantMoreDiversity)={}, maxSharesperServer={}", new Object[] { storageIndex, getAllShares(), currentBlocks.size(), activeShareMap.size(), tuple, maxSharesperServer });
            if (tuple.getLeft()) {
                continue;
            }
            if (tuple.getRight()) {
                maxSharesperServer += 1;
                askForMoreShares();
                continue;
            }
            askForMoreShares();
            if (noMoreShares) {
                StatusMsg status = new StatusMsg(Status.Failed, "Download Failed: number of current blocks + number of blocks we are waiting is less than the required blocks to reconstruct the segment");
                logger.debug("SI={}): {}", storageIndex, status);
                logger.debug("SI={}): global Shares={}, currentUsedShares={}", new Object[] { storageIndex, DumpUtils.dumptolog(shares), DumpUtils.dumptolog(currentShares) });
                trigger(new GetSegmentResponse(status, null), segmentFetcher);
                return;
            }
            return;
        }
    }

    /**
	 * Gets the all shares.
	 * 
	 * @return the all shares
	 */
    private int getAllShares() {
        Set<Integer> all = new HashSet<Integer>(currentBlocks.keySet());
        all.addAll(activeShareMap.keySet());
        return all.size();
    }

    /**
	 * Find_and_use_shares.
	 * 
	 * @return the tuple
	 */
    private Tuple<Boolean, Boolean> find_and_use_shares() {
        Tuple<Boolean, Boolean> tuple = new Tuple<Boolean, Boolean>(false, false);
        for (Share sh : currentShares) {
            int shnum = sh.getShareNum();
            if (currentBlocks.containsKey(shnum)) {
                logger.debug("SI={}): already got share {} in currentBlocks", storageIndex, shnum);
                continue;
            }
            if (activeShareMap.containsKey(shnum)) {
                logger.debug("SI={}): already got share {} in activeShareMap", storageIndex, shnum);
                continue;
            }
            Set<Share> shs = shares_from_servers.get(sh.getSelf().getAddress());
            if (shs != null) {
                if (shs.size() >= maxSharesperServer) {
                    logger.debug("SI={}): i want more diversity", storageIndex);
                    tuple.setRight(true);
                    continue;
                }
            }
            synchronized (currentShares) {
                currentShares.remove(sh);
            }
            synchronized (activeShareMap) {
                activeShareMap.put(shnum, sh);
            }
            if (shs == null) {
                shs = new HashSet<Share>();
            }
            shs.add(sh);
            synchronized (shares_from_servers) {
                shares_from_servers.put(sh.getSelf().getAddress(), shs);
            }
            start_share(sh);
            tuple.setLeft(true);
            break;
        }
        return tuple;
    }

    /**
	 * Start_share.
	 * 
	 * @param share
	 *            the share
	 */
    private void start_share(Share share) {
        logger.debug("SI={}): send GetBlock request for segment#{} to {} (Shnum={})", new Object[] { storageIndex, activeSegment, share.getSelf(), share.getShareNum() });
        trigger(new GetBlock(share.getSelf().getId(), activeSegment), rbpPort);
    }

    /**
	 * Ask for more shares.
	 */
    private void askForMoreShares() {
        if (!noMoreShares) {
            trigger(new WantMoreShares(), segmentFetcher);
        }
    }
}
