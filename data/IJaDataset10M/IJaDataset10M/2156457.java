package org.mobicents.servlet.sip.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.join.JoinEvent;
import javax.media.mscontrol.join.JoinEventListener;
import javax.media.mscontrol.join.Joinable.Direction;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mediagroup.Player;
import javax.media.mscontrol.mediagroup.PlayerEvent;
import javax.media.mscontrol.mediagroup.signals.SignalDetector;
import javax.media.mscontrol.mediagroup.signals.SignalDetectorEvent;
import javax.media.mscontrol.networkconnection.NetworkConnection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import org.apache.log4j.Logger;

/**
 * 
 * @author amit bhayani
 * 
 */
public class PromptAndCollectServlet extends PlayerServlet {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(PromptAndCollectServlet.class);

    private static final String WELCOME_MSG = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/dtmf_welcome.wav";

    private static final String DTMF_0 = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/dtmf0.wav";

    private static final String DTMF_1 = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/dtmf1.wav";

    private static final String DTMF_2 = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/dtmf2.wav";

    private static final String DTMF_3 = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/dtmf3.wav";

    private static final String DTMF_4 = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/dtmf4.wav";

    private static final String DTMF_5 = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/dtmf5.wav";

    private static final String DTMF_6 = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/dtmf6.wav";

    private static final String DTMF_7 = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/dtmf7.wav";

    private static final String DTMF_8 = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/dtmf8.wav";

    private static final String DTMF_9 = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/dtmf9.wav";

    private static final String STAR = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/star.wav";

    private static final String POUND = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/pound.wav";

    private static final String A = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/A.wav";

    private static final String B = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/B.wav";

    private static final String C = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/C.wav";

    private static final String D = "http://" + System.getProperty("jboss.bind.address", "127.0.0.1") + ":8080/media-jsr309-servlet/audio/D.wav";

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }

    @Override
    protected void doAck(SipServletRequest req) throws ServletException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Received ACK for INVITE " + req);
        }
        SipSession sipSession = req.getSession();
        MediaSession ms = (MediaSession) sipSession.getAttribute("MEDIA_SESSION");
        try {
            MediaGroup mg = ms.createMediaGroup(MediaGroup.PLAYER_SIGNALDETECTOR);
            mg.addListener(new MyJoinEventListener());
            NetworkConnection nc = (NetworkConnection) sipSession.getAttribute("NETWORK_CONNECTION");
            mg.joinInitiate(Direction.DUPLEX, nc, this);
            sipSession.setAttribute("MediaGroup", mg);
        } catch (MsControlException e) {
            logger.error(e);
            terminate(sipSession, ms);
        }
    }

    @Override
    protected void doInfo(SipServletRequest request) throws ServletException, IOException {
        int responseCode = SipServletResponse.SC_OK;
        String messageContent = new String((byte[]) request.getContent());
        logger.info("got INFO request with following content " + messageContent);
        int signalIndex = messageContent.indexOf("Signal=");
        if (messageContent != null && messageContent.length() > 0 && signalIndex != -1) {
            String signal = messageContent.substring("Signal=".length()).trim();
            signal = signal.substring(0, 1);
            logger.info("Signal received " + signal);
            MediaGroup mediaGroup = (MediaGroup) request.getSession().getAttribute("MediaGroup");
            try {
                playDTMF(mediaGroup.getPlayer(), signal);
            } catch (MsControlException e) {
                logger.error("Problem playing the stream corresponding to the following DTMF " + signal, e);
                responseCode = SipServletResponse.SC_SERVER_INTERNAL_ERROR;
            }
        }
        SipServletResponse response = request.createResponse(responseCode);
        response.send();
    }

    /**
	 * @param mg
	 * @param dtmf
	 * @throws MsControlException
	 */
    private void playDTMF(Player player, String dtmf) throws MsControlException {
        URI prompt = null;
        if (dtmf.equals("0")) {
            prompt = URI.create(DTMF_0);
        } else if (dtmf.equals("1")) {
            prompt = URI.create(DTMF_1);
        } else if (dtmf.equals("2")) {
            prompt = URI.create(DTMF_2);
        } else if (dtmf.equals("3")) {
            prompt = URI.create(DTMF_3);
        } else if (dtmf.equals("4")) {
            prompt = URI.create(DTMF_4);
        } else if (dtmf.equals("5")) {
            prompt = URI.create(DTMF_5);
        } else if (dtmf.equals("6")) {
            prompt = URI.create(DTMF_6);
        } else if (dtmf.equals("7")) {
            prompt = URI.create(DTMF_7);
        } else if (dtmf.equals("8")) {
            prompt = URI.create(DTMF_8);
        } else if (dtmf.equals("9")) {
            prompt = URI.create(DTMF_9);
        } else if (dtmf.equals("#")) {
            prompt = URI.create(POUND);
        } else if (dtmf.equals("*")) {
            prompt = URI.create(STAR);
        } else if (dtmf.equals("A")) {
            prompt = URI.create(A);
        } else if (dtmf.equals("B")) {
            prompt = URI.create(B);
        } else if (dtmf.equals("C")) {
            prompt = URI.create(C);
        } else if (dtmf.equals("D")) {
            prompt = URI.create(D);
        } else {
            throw new MsControlException("This DigitMap is not recognized " + dtmf);
        }
        Parameters p = player.getMediaSession().createParameters();
        p.put(Player.BEHAVIOUR_IF_BUSY, Player.STOP_IF_BUSY);
        player.play(prompt, null, p);
        SignalDetector sg = player.getContainer().getSignalDetector();
        sg.addListener(new SignalDetectorListener());
        player.getContainer().getSignalDetector().receiveSignals(1, null, null, null);
    }

    private class MyJoinEventListener implements JoinEventListener {

        public void onEvent(JoinEvent event) {
            MediaGroup mg = (MediaGroup) event.getThisJoinable();
            if (event.isSuccessful()) {
                if (JoinEvent.JOINED == event.getEventType()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("NC joined to MG. Start Player");
                    }
                    try {
                        Player player = mg.getPlayer();
                        player.addListener(new PlayerListener());
                        URI prompt = getPrompt();
                        Parameters p = player.getMediaSession().createParameters();
                        p.put(Player.BEHAVIOUR_IF_BUSY, Player.STOP_IF_BUSY);
                        player.play(prompt, null, p);
                        SignalDetector sg = mg.getSignalDetector();
                        sg.addListener(new SignalDetectorListener());
                        mg.getSignalDetector().receiveSignals(1, null, null, null);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                } else if (JoinEvent.UNJOINED == event.getEventType()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Un-Joined MG and NC");
                    }
                }
            } else {
                logger.error("Joining of MG and NC failed");
            }
        }
    }

    protected URI getPrompt() throws Exception {
        return URI.create(WELCOME_MSG);
    }

    private class PlayerListener implements MediaEventListener<PlayerEvent> {

        public void onEvent(PlayerEvent event) {
            try {
                logger.info("PlayerListener Received event " + isBye + " " + event.getEventType() + "Event: " + event);
                Player player = event.getSource();
                MediaGroup mg = player.getContainer();
                if (!isBye) {
                    logger.info("Is not BYE " + event.isSuccessful() + " " + event.getEventType());
                    if (event.isSuccessful() && (PlayerEvent.PLAY_COMPLETED == event.getEventType())) {
                        logger.info("Received PlayComplete event");
                        try {
                            SignalDetector sg = mg.getSignalDetector();
                            sg.addListener(new SignalDetectorListener());
                            sg.receiveSignals(1, null, null, null);
                        } catch (MsControlException e) {
                            logger.error(e);
                        }
                    } else {
                        logger.error("Player didn't complete successfully ");
                    }
                }
            } catch (Exception e) {
                logger.error("Broken", e);
            }
        }
    }

    class SignalDetectorListener implements MediaEventListener<SignalDetectorEvent> {

        public void onEvent(SignalDetectorEvent event) {
            try {
                MediaGroup mg = (MediaGroup) event.getSource().getContainer();
                SignalDetector sg = mg.getSignalDetector();
                sg.removeListener(this);
                if (event.isSuccessful() && (SignalDetectorEvent.RECEIVE_SIGNALS_COMPLETED == event.getEventType())) {
                    String seq = event.getSignalString();
                    playDTMF(mg.getPlayer(), seq);
                } else {
                    logger.error("DTMF detection failed " + event.getSignalString());
                }
            } catch (MsControlException e) {
                e.printStackTrace();
            }
        }
    }
}
