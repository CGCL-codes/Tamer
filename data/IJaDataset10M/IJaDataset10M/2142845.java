package clouditup.partnership;

import clouditup.InternalCommunication.InternalQueueManager;
import clouditup.RestManager.RestControler;
import clouditup.Video.VideoItem;
import clouditup.linkbdd.ControleurBDD;
import java.io.BufferedWriter;
import java.io.PipedWriter;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.restlet.resource.DomRepresentation;

/**
 *
 * @author mascou
 */
public class PartnershipControler {

    private ControleurBDD m_ControleurBDD;

    private DomRepresentation m_MessageToSend;

    private PipedWriter m_InterThreadPipeWriter;

    private BufferedWriter m_BufferedPipeWriter;

    private ConcurrentLinkedQueue<String> m_MessageBox;

    public PartnershipControler(ConcurrentLinkedQueue<String> messages) {
        m_MessageBox = messages;
        m_ControleurBDD = new ControleurBDD(m_MessageBox);
    }

    public PipedWriter getPipedWriter() {
        return m_InterThreadPipeWriter;
    }

    public void sendAllCurrentDvdsTo(Partner newPartner) {
        VideoItem[] videolist = new VideoItem[1];
        m_ControleurBDD.getVideos().toArray(videolist);
        prepareMessage(videolist);
        sendMessageTo(newPartner);
    }

    public void sendNewVideoOfferToVODPartners(VideoItem newVideo) throws Exception {
        VideoItem[] videoList = new VideoItem[1];
        videoList[0] = newVideo;
        prepareMessage(videoList);
        sendMessageToAllPartners();
    }

    private void prepareMessage(VideoItem[] videoList) {
        try {
            m_MessageToSend = XMLGenerator.generateDomRepresentationFromVideoList(videoList);
        } catch (Exception ex) {
            InternalQueueManager.writeErrorMessageInQueue(m_MessageBox, "PARTNERSHIP:Impossible de creer un message pour ce DVD");
        }
    }

    private void sendMessageTo(Partner newPartner) {
        try {
            InternalQueueManager.writeProgressMessageInQueue(m_MessageBox, "PARTNERSHIP:Notification de " + newPartner.getName());
            RestControler.postMessageToPartnerWith(newPartner.getUrlQueue(), m_MessageToSend, newPartner.getWriteKey());
            InternalQueueManager.writeSuccessMessageInQueue(m_MessageBox, "PARTNERSHIP:Notification postee!");
        } catch (Exception ex) {
            System.out.println("ERREUR : " + ex.getLocalizedMessage());
            System.out.println("Erreur envoi vers : " + newPartner.getName());
            InternalQueueManager.writeErrorMessageInQueue(m_MessageBox, "PARTNERSHIP:Erreur envoi vers " + newPartner.getName());
        }
    }

    private void sendMessageToAllPartners() {
        List<Partner> partnerList = m_ControleurBDD.getPartners();
        for (Partner partner : partnerList) {
            try {
                InternalQueueManager.writeProgressMessageInQueue(m_MessageBox, "PARTNERSHIP:Notification de " + partner.getName());
                RestControler.postMessageToPartnerWith(partner.getUrlQueue(), m_MessageToSend, partner.getWriteKey());
                InternalQueueManager.writeSuccessMessageInQueue(m_MessageBox, "PARTNERSHIP:Notification postee!");
            } catch (Exception ex) {
                System.out.println("ERREUR : " + ex.getLocalizedMessage());
                System.out.println("Erreur envoi vers : " + partner.getName());
                InternalQueueManager.writeErrorMessageInQueue(m_MessageBox, "PARTNERSHIP:Erreur envoi vers " + partner.getName());
            }
        }
    }
}
