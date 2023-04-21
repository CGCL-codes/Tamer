package edu.indiana.cs.b534.torrent.impl;

import edu.indiana.cs.b534.torrent.context.TorrentContext;
import java.util.List;
import java.util.Map;

/**
 * @author : Eran Chinthaka (echintha@cs.indiana.edu)
 * @Date : Mar 25, 2007
 */
public class ChokingPolicyManager {

    private TorrentContext torrentContext;

    private InfoCollector infoCollector;

    public ChokingPolicyManager(TorrentContext torrentContext) {
        this.torrentContext = torrentContext;
        infoCollector = torrentContext.getInfoCollector();
    }

    public boolean shouldIChoke(PeerInstance peerInstance) {
        int chokingPolicy = torrentContext.getChokingPolicy();
        switch(chokingPolicy) {
            case Constants.CHOKING_POLICY_ALLOW_ALL:
                return true;
            case Constants.CHOKING_POLICY_PEERS_HAVING_MOST_INTERESTED_PIECES:
                return handleChokingPolicyPeersHavingMostInterestedPieces(peerInstance);
            case Constants.CHOKING_POLICY_SHARE_RATIO:
                return handleChokingPolicyShareRatio(peerInstance);
        }
        return true;
    }

    private boolean handleChokingPolicyShareRatio(PeerInstance peerInstance) {
        String peerId = peerInstance.getID();
        System.out.println("peerId = " + peerId);
        int downloadCount = 0;
        int uploadedCount = 0;
        double shareRatio = 0;
        Map<String, List<Integer>> downloadInformation = infoCollector.getDownloadInformation();
        List<Integer> pieceList = downloadInformation.get(peerId);
        if (pieceList != null) downloadCount = pieceList.size();
        Map<String, List<Integer>> uploadInformation = infoCollector.getUploadInformation();
        pieceList = uploadInformation.get(peerId);
        if (pieceList != null) uploadedCount = pieceList.size();
        System.out.println("uploadedCount = " + uploadedCount);
        System.out.println("downloadCount = " + downloadCount);
        if (downloadCount > 0) {
            shareRatio = uploadedCount / downloadCount;
            System.out.println("shareRatio = " + shareRatio);
            return shareRatio < torrentContext.getShareRatio();
        } else {
            System.out.println("uploadedCount = " + uploadedCount);
            return uploadedCount > 2;
        }
    }

    private boolean handleChokingPolicyPeersHavingMostInterestedPieces(PeerInstance peerInstance) {
        return false;
    }
}
