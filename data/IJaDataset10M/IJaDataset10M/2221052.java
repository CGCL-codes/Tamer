package playground.ou.signalcontrolnetwork;

import org.matsim.core.api.network.Link;
import org.matsim.core.network.LinkImpl;
import org.matsim.core.network.NetworkLayer;
import playground.ou.volcreation.DirectionalVolume;

public class ControledLink extends LinkImpl {

    int greentime;

    int offset;

    LinkMovement linkMovement = new LinkMovement();

    public ControledLink(final NetworkLayer network, Link link) {
        super(link.getId(), link.getFromNode(), link.getToNode(), network, link.getLength(), link.getFreespeed(0), link.getCapacity(0), link.getNumberOfLanes(0));
    }

    public void setLinkParameter(int green, int offset) {
        this.greentime = green;
        this.offset = offset;
    }

    public int getGreentime() {
        return this.greentime;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLinkVolume() {
        return (int) (linkMovement.getTurnLeftVolume() + linkMovement.getGoThroughVolume() + linkMovement.getTurnRightVolume());
    }

    public LinkMovement getLinkMovement() {
        return linkMovement;
    }

    public void setLinkVolume(DirectionalVolume linkvolume) {
        linkMovement.setGoThroughVolume(linkvolume.getGothruVol());
        linkMovement.setTurnLeftVolume(linkvolume.getTurnleftVol());
        linkMovement.setTurnRightVolume(linkvolume.getTurnrightVol());
    }

    public void setLinkVolume(double lane, DirectionalVolume linkvolume) {
        linkMovement.setGoThroughVolume((int) (linkvolume.getGothruVol() / lane));
        linkMovement.setTurnLeftVolume((int) (linkvolume.getTurnleftVol() / lane));
        linkMovement.setTurnRightVolume((int) (linkvolume.getTurnrightVol() / lane));
    }
}
