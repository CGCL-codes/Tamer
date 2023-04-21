package tw.bennu.feeler.vote;

import java.util.ArrayList;
import java.util.List;
import tw.bennu.feeler.apps.service.AppsService;
import tw.bennu.feeler.apps.service.IAppsPacket;
import tw.bennu.feeler.vote.apps.packet.VoteTopicAppsPacket;
import tw.bennu.feeler.vote.service.VoteService;

public class VoteServiceImpl implements VoteService, AppsService {

    private List<IAppsPacket> voteTopicAppsPacketList = new ArrayList<IAppsPacket>();

    private List<IAppsPacket> voteNewTopicAppsPacketList = new ArrayList<IAppsPacket>();

    @Override
    public synchronized void appendAppsPacket(IAppsPacket appsPack) {
        if (appsPack instanceof VoteTopicAppsPacket) {
            this.voteNewTopicAppsPacketList.add((VoteTopicAppsPacket) appsPack);
            this.voteTopicAppsPacketList.add((VoteTopicAppsPacket) appsPack);
            while (this.voteTopicAppsPacketList.size() > 100) {
                this.voteTopicAppsPacketList.remove(0);
            }
        }
    }

    @Override
    public List<IAppsPacket> listKnowAppsPacket() {
        return this.voteTopicAppsPacketList;
    }

    @Override
    public synchronized List<IAppsPacket> listNewAppsPacket() {
        List<IAppsPacket> retList = this.voteNewTopicAppsPacketList;
        this.voteNewTopicAppsPacketList = new ArrayList<IAppsPacket>();
        return retList;
    }
}
