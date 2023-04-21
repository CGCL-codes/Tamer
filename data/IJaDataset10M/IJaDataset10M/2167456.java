package ch.ethz.dcg.spamato.peerato.peer.msg;

import java.io.IOException;
import java.util.ArrayList;
import ch.ethz.dcg.spamato.peerato.common.msg.data.Hash;
import ch.ethz.dcg.spamato.peerato.peer.*;
import ch.ethz.dcg.spamato.peerato.peer.msg.data.ChunkList;

/**
 * Contains information about a chunk list request.
 * This includes the <code>fileId</code>.
 * A peer sends a ChunkListRequest to another peer when he wants to know which chunks the other has of the file.
 * 
 * @author Michelle Ackermann
 */
public class ChunkListRequestMessage extends PeerMessage {

    private Hash[] fileIds;

    public ChunkListRequestMessage() {
    }

    /**
	 * Creates a ChunkListRequestMessage from the specified <code>portNumber</code> and <code>fileIds</code>.
	 * @param portNumber port number of the sending peer
	 * @param fileIds ids of the files the peer is interested
	 */
    public ChunkListRequestMessage(int portNumber, Hash[] fileIds) {
        super(portNumber);
        this.fileIds = fileIds;
    }

    /**
	 * Handles a ChunkListRequest.
	 * If a <code>fileId</code> of the ChunkListRequest exists, a ChunkList is created.
	 * If there is at least one ChunkList, a ChunkListAnswer is sent.
	 */
    public void handle(PeerConnection connection, int length) throws IOException {
        Peer peer = connection.getPeer();
        getLogger().fine("<- " + peer.getInetSocketAddress() + " ChunkListRequest");
        ArrayList<ChunkList> chunkListArray = new ArrayList<ChunkList>();
        for (int i = 0; i < fileIds.length; i++) {
            Hash fileId = fileIds[i];
            FileStore fileStore = getDownloadManager().getFileStore(fileId);
            if (fileStore != null) {
                boolean[] completeChunks = fileStore.getCompleteChunks();
                ChunkList answer = new ChunkList(fileId, completeChunks);
                chunkListArray.add(answer);
            }
        }
        if (chunkListArray.size() > 0) {
            peer.getCommandConnection().sendChunkListAnswer(createChunkListArray(chunkListArray));
        }
    }

    private ChunkList[] createChunkListArray(ArrayList<ChunkList> chunkListArray) {
        ChunkList[] chunkLists = new ChunkList[chunkListArray.size()];
        for (int i = 0; i < chunkLists.length; i++) {
            chunkLists[i] = (ChunkList) chunkListArray.get(i);
        }
        return chunkLists;
    }
}
