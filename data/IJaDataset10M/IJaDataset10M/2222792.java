package tribler.overlay.peer.chunkSelection;

import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import protopeer.Finger;
import protopeer.Peerlet;
import protopeer.util.quantities.Time;
import tribler.data.Movie;
import tribler.overlay.peer.VoDPeer;
import tribler.overlay.peerlets.DropPlayoutPeerlet;
import tribler.overlay.util.VoDPeerState;
import bittorrent.data.AbstractChunk;
import bittorrent.data.BlockDescriptor;
import bittorrent.overlay.peer.FileSharingPeer;
import bittorrent.overlay.peer.chunkSelection.AbstractChunkSelection;
import bittorrent.overlay.peer.chunkSelection.ChunkDistribution;
import bittorrent.overlay.peer.chunkSelection.strategies.LeastRequested;
import bittorrent.overlay.peer.chunkSelection.strategies.RarestFirst;
import bittorrent.overlay.peerlets.RemoteChunksPeerlet;

/**
 * Implementation of the Give-to-Get chunk selection algorithm. Starting from
 * the current playout position it divides the remaining chunks in 3 priority
 * sets.
 * 
 * High priority: In-order selection Med priority: Rarest-first Low priority:
 * Rarest-first
 * 
 * Chunks from the high priority set are always preferred to the medium priority
 * set which in turn are preferred to the low priority chunks.
 */
public class TriblerChunkSelection extends AbstractChunkSelection {

    private static final Logger logger = Logger.getLogger(TriblerChunkSelection.class);

    private ChunkDistribution chunkDistribution;

    private Movie movie;

    private Time highPrioDuration = Time.inSeconds(10);

    private Time mediumPrioDuration = Time.inSeconds(40);

    private boolean endgamemode = true;

    public TriblerChunkSelection(FileSharingPeer peer) {
        super(peer);
        Peerlet p = peer.getPeerletOfType(RemoteChunksPeerlet.class);
        if (p != null) {
            RemoteChunksPeerlet rcp = (RemoteChunksPeerlet) p;
            chunkDistribution = new ChunkDistribution(rcp, peer.getPeerState());
        } else {
            logger.error("This AbstractChunkSelection requires an instance of " + RemoteChunksPeerlet.class.getSimpleName());
        }
    }

    @Override
    protected VoDPeerState getPeerState() {
        return (VoDPeerState) super.getPeerState();
    }

    protected Movie getMovie() {
        if (movie == null) {
            movie = (Movie) getPeerState().getStorageWrapper().getSharedFile();
        }
        return movie;
    }

    /**
	 * Calculates High, medium and low priority chunks according to current
	 * Frame being played, duration in playback seconds of different priorities
	 * and actual playback length of frames. Within a set of chunks with same
	 * priority, local rarest first is applied.
	 * 
	 */
    @Override
    public BlockDescriptor getBlockToRequest(BitSet availableChunks, Finger remotePeer) {
        List<Integer> highPrio = getHighPriorityChunks();
        List<Integer> mediumPrio = getMediumPriorityChunks();
        for (Integer chunk : highPrio) {
            if (sw.getNotCompletelyRequestedChunks().contains(chunk)) {
                if (availableChunks.get(chunk)) {
                    for (BlockDescriptor block : sw.getBlocksToRequest(chunk)) {
                        return block;
                    }
                }
            }
        }
        if (endgamemode) {
            Set<Integer> highPrioInProgress = new LinkedHashSet<Integer>(sw.getChunksInprogress());
            highPrioInProgress.retainAll(highPrio);
            if (!highPrioInProgress.isEmpty()) {
                BitSet highPrioInProgressBitSet = new BitSet(availableChunks.size());
                for (Integer i : highPrioInProgress) {
                    if (availableChunks.get(i)) {
                        highPrioInProgressBitSet.set(i);
                    }
                }
                return new LeastRequested(sw).getLeastRequestedBlock(highPrioInProgressBitSet, remotePeer);
            }
        }
        List<Integer> chunksToRequest = new LinkedList<Integer>();
        List<Integer> allInterestingChunks = getInterestingChunks(availableChunks, false);
        List<Integer> mediumInterestingChunks = new LinkedList<Integer>(allInterestingChunks);
        mediumInterestingChunks.retainAll(mediumPrio);
        chunksToRequest.addAll((new RarestFirst(chunkDistribution, sw)).getChunks(mediumInterestingChunks));
        List<Integer> lowInterestingChunks = new LinkedList<Integer>(allInterestingChunks);
        lowInterestingChunks.removeAll(mediumPrio);
        lowInterestingChunks.removeAll(highPrio);
        chunksToRequest.addAll((new RarestFirst(chunkDistribution, sw)).getChunks(lowInterestingChunks));
        return getFirstUnrequestedBlock(chunksToRequest);
    }

    public List<Integer> getHighPriorityChunks() {
        List<AbstractChunk> highPrioChunks = getMovie().getChunksForTimerange(getPlaybackPosition(), highPrioDuration);
        return chunkList2IntegerList(highPrioChunks);
    }

    public List<Integer> getMediumPriorityChunks() {
        return chunkList2IntegerList(getMovie().getChunksForTimerange(getPlaybackPosition().add(highPrioDuration), mediumPrioDuration));
    }

    private Time getPlaybackPosition() {
        Time playbackPosition = getPeerState().getPlaybackPosition();
        if (getPeer() instanceof VoDPeer && (((VoDPeer) getPeer()).getPlayoutPeerlet() instanceof DropPlayoutPeerlet)) {
            playbackPosition = playbackPosition.add(Time.inSeconds(2));
        }
        return playbackPosition;
    }

    private List<Integer> chunkList2IntegerList(List<AbstractChunk> chunks) {
        List<Integer> result = new LinkedList<Integer>();
        for (AbstractChunk chunk : chunks) {
            result.add(chunk.getChunkindex());
        }
        return result;
    }

    @Override
    protected List<Integer> getInterestingChunks(BitSet availableChunks, boolean multipleRequests) {
        List<AbstractChunk> l = getMovie().getChunksForTimerange(getPlaybackPosition(), Time.inSeconds(0.0));
        int nowChunkIndex = l.get(0).getChunkindex();
        BitSet newAvailableChunks = (BitSet) availableChunks.clone();
        newAvailableChunks.clear(0, nowChunkIndex);
        return super.getInterestingChunks(newAvailableChunks, multipleRequests);
    }

    public boolean isEndgamemode() {
        return endgamemode;
    }

    public void setEndgamemode(boolean endgamemode) {
        this.endgamemode = endgamemode;
    }
}
