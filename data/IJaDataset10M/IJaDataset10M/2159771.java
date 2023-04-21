package local.ua;

import local.media.AudioClipPlayer;
import org.zoolu.sip.call.*;
import org.zoolu.sip.address.*;
import org.zoolu.sip.provider.SipStack;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.header.ExpiresHeader;
import org.zoolu.sip.header.ContactHeader;
import org.zoolu.sip.header.CallIdHeader;
import org.zoolu.sip.header.StatusLine;
import org.zoolu.sip.transaction.TransactionClient;
import org.zoolu.sip.transaction.TransactionClientListener;
import org.zoolu.sip.call.*;
import org.zoolu.sip.message.*;
import org.zoolu.sdp.*;
import org.zoolu.tools.EventLogger;
import org.zoolu.tools.LogLevel;
import org.zoolu.tools.Parser;
import org.zoolu.tools.Archive;
import java.util.Enumeration;
import java.util.Vector;
import java.io.*;
import java.net.DatagramSocket;
import java.net.SocketException;

/** Simple SIP user agent (UA).
  * It includes m_bUseAudio/m_bUseVideo applications.
  * <p>
  * It can use external m_bUseAudio/m_bUseVideo tools as media applications.
  * Currently only RAT (Robust Audio Tool) and VIC are supported as external applications.
  */
public class UserAgent extends CallListenerAdapter {

    /** Event logger. */
    EventLogger m_eventLogger;

    /** UserAgentProfile */
    public UserAgentProfile m_userProfile;

    /** SipProvider */
    public SipProvider m_sipProvider;

    public ExtendedCall call;

    /** Call transfer */
    public ExtendedCall call_transfer;

    /** Audio application */
    public MediaLauncher m_audioLauncher = null;

    /** Video application */
    public MediaLauncher m_videoLauncher = null;

    /** Local sdp */
    public String m_strLocalSession = null;

    /** UserAgent userAgentListener */
    public UserAgentListener m_userAgentListener = null;

    /** Media file path */
    final String MEDIA_PATH = "media/local/ua/";

    /** On wav file */
    final String CLIP_ON = MEDIA_PATH + "on.wav";

    /** Off wav file */
    final String CLIP_OFF = MEDIA_PATH + "off.wav";

    /** Ring wav file */
    final String CLIP_RING = MEDIA_PATH + "ring.wav";

    /** Ring sound */
    AudioClipPlayer clip_ring;

    /** On sound */
    AudioClipPlayer clip_on;

    /** Off sound */
    AudioClipPlayer clip_off;

    /** UA_IDLE=0 */
    public static final String UA_IDLE = "IDLE";

    /** UA_INCOMING_CALL=1 */
    public static final String UA_INCOMING_CALL = "INCOMING_CALL";

    /** UA_OUTGOING_CALL=2 */
    static final String UA_OUTGOING_CALL = "OUTGOING_CALL";

    /** UA_ONCALL=3 */
    static final String UA_ONCALL = "ONCALL";

    /** Call strState
     * <P>UA_IDLE=0, <BR>UA_INCOMING_CALL=1, <BR>UA_OUTGOING_CALL=2, <BR>UA_ONCALL=3 */
    String m_strCallState = UA_IDLE;

    /** Changes the call strState */
    public void changeStatus(String strState) {
        m_strCallState = strState;
        printLog("changeStatus: new state = " + m_strCallState, LogLevel.MEDIUM);
    }

    /** Checks the call strState */
    public boolean statusIs(String strState) {
        return m_strCallState.equals(strState);
    }

    /** Gets the call strState */
    public String getStatus() {
        return m_strCallState;
    }

    /** Sets the automatic answer time (default is -1 that means no auto accept mode) */
    public void setAcceptTime(int nAcceptTime) {
        m_userProfile.m_nAcceptTime = nAcceptTime;
    }

    /** Sets the automatic hangup time (default is 0, that corresponds to manual hangup mode) */
    public void setHangupTime(int time) {
        m_userProfile.m_nHangUpTime = time;
    }

    /** Sets the redirection url (default is null, that is no redircetion) */
    public void setRedirection(String url) {
        m_userProfile.m_strRedirectTo = url;
    }

    /** Sets the no offer mode for the invite (default is false) */
    public void setNoOfferMode(boolean nooffer) {
        m_userProfile.m_bNoOffer = nooffer;
    }

    /** Enables m_bUseAudio */
    public void setAudio(boolean enable) {
        m_userProfile.m_bUseAudio = enable;
    }

    /** Enables m_bUseVideo */
    public void setVideo(boolean enable) {
        m_userProfile.m_bUseVideo = enable;
    }

    /** Sets the receive only mode */
    public void setReceiveOnlyMode(boolean r_only) {
        m_userProfile.m_bPlayReceiveOnly = r_only;
    }

    /** Sets the send only mode */
    public void setSendOnlyMode(boolean s_only) {
        m_userProfile.m_bPlaySendOnly = s_only;
    }

    /** Sets the send tone mode */
    public void setSendToneMode(boolean s_tone) {
        m_userProfile.m_SendTone = s_tone;
    }

    /** Sets the send file */
    public void setSendFile(String file_name) {
        m_userProfile.m_strSendFile = file_name;
    }

    /** Sets the recv file */
    public void setRecvFile(String file_name) {
        m_userProfile.m_strReceiveFile = file_name;
    }

    /** Gets the local SDP */
    public String getSessionDescriptor() {
        return m_strLocalSession;
    }

    /** Sets the local SDP */
    public void setSessionDescriptor(String sdp) {
        m_strLocalSession = sdp;
    }

    /** Inits the local SDP (no media spec) */
    public void initSessionDescriptor() {
        SessionDescriptor sessionDescriptor = new SessionDescriptor(m_userProfile.m_strFromURL, m_sipProvider.getViaAddress());
        m_strLocalSession = sessionDescriptor.toString();
    }

    /** Adds a media to the SDP */
    public void addMediaDescriptor(String strMedia, int nPort, int nAVP, String strCodec, int nRate) {
        if (m_strLocalSession == null) initSessionDescriptor();
        SessionDescriptor sessionDescriptor = new SessionDescriptor(m_strLocalSession);
        String strAttributeParam = String.valueOf(nAVP);
        if (strCodec != null) strAttributeParam += " " + strCodec + "/" + nRate;
        sessionDescriptor.addMedia(new MediaField(strMedia, nPort, 0, "RTP/AVP", String.valueOf(nAVP)), new AttributeField("rtpmap", strAttributeParam));
        m_strLocalSession = sessionDescriptor.toString();
    }

    /** Costructs a UA with a default media port */
    public UserAgent(SipProvider sipProvider, UserAgentProfile userProfile, UserAgentListener userAgentListener) {
        m_sipProvider = sipProvider;
        m_eventLogger = sipProvider.getEventLogger();
        m_userAgentListener = userAgentListener;
        m_userProfile = userProfile;
        userProfile.initContactAddress(sipProvider);
        loadSound();
        initSessionDescriptor();
        try {
            if (userProfile.m_nAudioPort == 0) {
                DatagramSocket datagramSocket = new DatagramSocket();
                userProfile.m_nAudioPort = datagramSocket.getLocalPort();
                datagramSocket.close();
            }
        } catch (SocketException e) {
        }
        if (userProfile.m_bUseAudio || !userProfile.m_bUseVideo) addMediaDescriptor("audio", userProfile.m_nAudioPort, userProfile.audio_avp, userProfile.audio_codec, userProfile.audio_sample_rate);
        if (userProfile.m_bUseVideo) addMediaDescriptor("video", userProfile.video_port, userProfile.video_avp, null, 0);
    }

    private void loadSound() {
        if (!m_userProfile.use_rat && !m_userProfile.use_jmf) {
            if (m_userProfile.m_bUseAudio && !m_userProfile.m_bPlayReceiveOnly && m_userProfile.m_strSendFile == null && !m_userProfile.m_SendTone) local.media.AudioInput.initAudioLine();
            if (m_userProfile.m_bUseAudio && !m_userProfile.m_bPlaySendOnly && m_userProfile.m_strReceiveFile == null) local.media.AudioOutput.initAudioLine();
        }
        if (!m_userProfile.use_rat) {
            try {
                String jar_file = m_userProfile.ua_jar;
                clip_on = new AudioClipPlayer(Archive.getAudioInputStream(Archive.getJarURL(jar_file, CLIP_ON)), null);
                clip_off = new AudioClipPlayer(Archive.getAudioInputStream(Archive.getJarURL(jar_file, CLIP_OFF)), null);
                clip_ring = new AudioClipPlayer(Archive.getAudioInputStream(Archive.getJarURL(jar_file, CLIP_RING)), null);
            } catch (Exception e) {
                printException(e, LogLevel.HIGH);
            }
        }
    }

    /** Makes a new call (acting as UAC). */
    public void call(String target_url) {
        changeStatus(UA_OUTGOING_CALL);
        call = new ExtendedCall(m_sipProvider, m_userProfile.m_strFromURL, m_userProfile.m_strContactURL, m_userProfile.m_strUserName, m_userProfile.realm, m_userProfile.m_strPassword, this);
        target_url = m_sipProvider.completeNameAddress(target_url).toString();
        if (m_userProfile.m_bNoOffer) call.call(target_url); else call.call(target_url, m_strLocalSession);
    }

    /** Waits for an incoming call (acting as UAS). */
    public void listen() {
        changeStatus(UA_IDLE);
        call = new ExtendedCall(m_sipProvider, m_userProfile.m_strFromURL, m_userProfile.m_strContactURL, m_userProfile.m_strUserName, m_userProfile.realm, m_userProfile.m_strPassword, this);
        call.listen();
    }

    /** Closes an ongoing, incoming, or pending call */
    public void hangup() {
        if (clip_ring != null) clip_ring.stop();
        closeMediaApplication();
        if (call != null) call.hangup();
        changeStatus(UA_IDLE);
    }

    /** Closes an ongoing, incoming, or pending call */
    public void accept() {
        if (clip_ring != null) clip_ring.stop();
        if (call != null) call.accept(m_strLocalSession);
    }

    /** Redirects an incoming call */
    public void redirect(String redirection) {
        if (clip_ring != null) clip_ring.stop();
        if (call != null) call.redirect(redirection);
    }

    /** Launches the Media Application (currently, the RAT m_bUseAudio tool) */
    public void launchMediaApplication() {
        System.out.println("Lauching media....!");
        loadSound();
        if (m_audioLauncher != null || m_videoLauncher != null) {
            printLog("DEBUG: media application is already running", LogLevel.HIGH);
            return;
        }
        SessionDescriptor local_sdp = new SessionDescriptor(call.getLocalSessionDescriptor());
        String local_media_address = (new Parser(local_sdp.getConnection().toString())).skipString().skipString().getString();
        int local_audio_port = 0;
        int local_video_port = 0;
        for (Enumeration e = local_sdp.getMediaDescriptors().elements(); e.hasMoreElements(); ) {
            MediaField media = ((MediaDescriptor) e.nextElement()).getMedia();
            if (media.getMedia().equals("audio")) local_audio_port = media.getPort();
            if (media.getMedia().equals("video")) local_video_port = media.getPort();
        }
        SessionDescriptor remote_sdp = new SessionDescriptor(call.getRemoteSessionDescriptor());
        String remote_media_address = (new Parser(remote_sdp.getConnection().toString())).skipString().skipString().getString();
        int remote_audio_port = 0;
        int remote_video_port = 0;
        for (Enumeration e = remote_sdp.getMediaDescriptors().elements(); e.hasMoreElements(); ) {
            MediaField media = ((MediaDescriptor) e.nextElement()).getMedia();
            if (media.getMedia().equals("audio")) remote_audio_port = media.getPort();
            if (media.getMedia().equals("video")) remote_video_port = media.getPort();
        }
        int dir = 0;
        if (m_userProfile.m_bPlayReceiveOnly) dir = -1; else if (m_userProfile.m_bPlaySendOnly) dir = 1;
        if (m_userProfile.m_bUseAudio && local_audio_port != 0 && remote_audio_port != 0) {
            if (m_userProfile.use_rat) {
                m_audioLauncher = new RATLauncher(m_userProfile.bin_rat, local_audio_port, remote_media_address, remote_audio_port, m_eventLogger);
            } else if (m_userProfile.use_jmf) {
                try {
                    Class myclass = Class.forName("local.ua.JMFAudioLauncher");
                    Class[] parameter_types = { java.lang.Integer.TYPE, Class.forName("java.lang.String"), java.lang.Integer.TYPE, java.lang.Integer.TYPE, Class.forName("org.zoolu.tools.Log") };
                    Object[] parameters = { new Integer(local_audio_port), remote_media_address, new Integer(remote_audio_port), new Integer(dir), m_eventLogger };
                    java.lang.reflect.Constructor constructor = myclass.getConstructor(parameter_types);
                    m_audioLauncher = (MediaLauncher) constructor.newInstance(parameters);
                } catch (Exception e) {
                    printException(e, LogLevel.HIGH);
                    printLog("Error trying to create the JMFAudioLauncher", LogLevel.HIGH);
                }
            }
            if (m_audioLauncher == null) {
                String audio_in = null;
                if (m_userProfile.m_SendTone) audio_in = JAudioLauncher.TONE; else if (m_userProfile.m_strSendFile != null) audio_in = m_userProfile.m_strSendFile;
                String audio_out = null;
                if (m_userProfile.m_strReceiveFile != null) audio_out = m_userProfile.m_strReceiveFile;
                m_audioLauncher = new JAudioLauncher(local_audio_port, remote_media_address, remote_audio_port, dir, audio_in, audio_out, m_userProfile.audio_sample_rate, m_userProfile.audio_sample_size, m_userProfile.audio_frame_size, m_eventLogger);
            }
            m_audioLauncher.startMedia();
        }
        if (m_userProfile.m_bUseVideo && local_video_port != 0 && remote_video_port != 0) {
            if (m_userProfile.use_vic) {
                m_videoLauncher = new VICLauncher(m_userProfile.bin_vic, local_video_port, remote_media_address, remote_video_port, m_eventLogger);
            } else if (m_userProfile.use_jmf) {
                try {
                    Class myclass = Class.forName("local.ua.JMFVideoLauncher");
                    Class[] parameter_types = { java.lang.Integer.TYPE, Class.forName("java.lang.String"), java.lang.Integer.TYPE, java.lang.Integer.TYPE, Class.forName("org.zoolu.tools.Log") };
                    Object[] parameters = { new Integer(local_video_port), remote_media_address, new Integer(remote_video_port), new Integer(dir), m_eventLogger };
                    java.lang.reflect.Constructor constructor = myclass.getConstructor(parameter_types);
                    m_videoLauncher = (MediaLauncher) constructor.newInstance(parameters);
                } catch (Exception e) {
                    printException(e, LogLevel.HIGH);
                    printLog("Error trying to create the JMFVideoLauncher", LogLevel.HIGH);
                }
            }
            if (m_videoLauncher == null) {
                printLog("No external video application nor JMF " + "has been provided: Video not started", LogLevel.HIGH);
                return;
            }
            m_videoLauncher.startMedia();
        }
    }

    /** Close the Media Application  */
    public void closeMediaApplication() {
        if (m_audioLauncher != null) {
            m_audioLauncher.stopMedia();
            m_audioLauncher = null;
        }
        if (m_videoLauncher != null) {
            m_videoLauncher.stopMedia();
            m_videoLauncher = null;
        }
    }

    /** Callback function called when arriving a new INVITE method (incoming call) */
    public void onCallIncoming(Call call, NameAddress callee, NameAddress caller, String sdp, Message invite) {
        printLog("onCallIncoming()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("INCOMING", LogLevel.HIGH);
        changeStatus(UA_INCOMING_CALL);
        call.ring();
        if (sdp != null) {
            SessionDescriptor remote_sdp = new SessionDescriptor(sdp);
            SessionDescriptor local_sdp = new SessionDescriptor(m_strLocalSession);
            SessionDescriptor new_sdp = new SessionDescriptor(remote_sdp.getOrigin(), remote_sdp.getSessionName(), local_sdp.getConnection(), local_sdp.getTime());
            new_sdp.addMediaDescriptors(local_sdp.getMediaDescriptors());
            new_sdp = SdpTools.sdpMediaProduct(new_sdp, remote_sdp.getMediaDescriptors());
            new_sdp = SdpTools.sdpAttirbuteSelection(new_sdp, "rtpmap");
            m_strLocalSession = new_sdp.toString();
        }
        if (clip_ring != null) clip_ring.loop();
        if (m_userAgentListener != null) m_userAgentListener.onUaCallIncoming(this, callee, caller);
    }

    /** Callback function called when arriving a new Re-INVITE method (re-inviting/call modify) */
    public void onCallModifying(Call call, String sdp, Message invite) {
        printLog("onCallModifying()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("RE-INVITE/MODIFY", LogLevel.HIGH);
        super.onCallModifying(call, sdp, invite);
    }

    /** Callback function that may be overloaded (extended). Called when arriving a 180 Ringing */
    public void onCallRinging(Call call, Message resp) {
        printLog("onCallRinging()", LogLevel.LOW);
        if (call != this.call && call != call_transfer) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("RINGING", LogLevel.HIGH);
        if (clip_on != null) clip_on.replay();
        if (m_userAgentListener != null) m_userAgentListener.onUaCallRinging(this);
    }

    /** Callback function called when arriving a 2xx (call accepted) */
    public void onCallAccepted(Call call, String sdp, Message resp) {
        printLog("onCallAccepted()", LogLevel.LOW);
        if (call != this.call && call != call_transfer) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("ACCEPTED/CALL", LogLevel.HIGH);
        changeStatus(UA_ONCALL);
        if (m_userProfile.m_bNoOffer) {
            SessionDescriptor remote_sdp = new SessionDescriptor(sdp);
            SessionDescriptor local_sdp = new SessionDescriptor(m_strLocalSession);
            SessionDescriptor new_sdp = new SessionDescriptor(remote_sdp.getOrigin(), remote_sdp.getSessionName(), local_sdp.getConnection(), local_sdp.getTime());
            new_sdp.addMediaDescriptors(local_sdp.getMediaDescriptors());
            new_sdp = SdpTools.sdpMediaProduct(new_sdp, remote_sdp.getMediaDescriptors());
            new_sdp = SdpTools.sdpAttirbuteSelection(new_sdp, "rtpmap");
            m_strLocalSession = new_sdp.toString();
        }
        call.ackWithAnswer(m_strLocalSession);
        if (clip_on != null) clip_on.replay();
        if (m_userAgentListener != null) m_userAgentListener.onUaCallAccepted(this);
        launchMediaApplication();
        if (call == call_transfer) {
            StatusLine status_line = resp.getStatusLine();
            int code = status_line.getCode();
            String reason = status_line.getReason();
            this.call.notify(code, reason);
        }
    }

    /** Callback function called when arriving an ACK method (call confirmed) */
    public void onCallConfirmed(Call call, String sdp, Message ack) {
        printLog("onCallConfirmed()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("CONFIRMED/CALL", LogLevel.HIGH);
        changeStatus(UA_ONCALL);
        if (clip_on != null) clip_on.replay();
        launchMediaApplication();
        if (m_userProfile.m_nHangUpTime > 0) this.automaticHangup(m_userProfile.m_nHangUpTime);
    }

    /** Callback function called when arriving a 2xx (re-invite/modify accepted) */
    public void onCallReInviteAccepted(Call call, String sdp, Message resp) {
        printLog("onCallReInviteAccepted()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("RE-INVITE-ACCEPTED/CALL", LogLevel.HIGH);
    }

    /** Callback function called when arriving a 4xx (re-invite/modify failure) */
    public void onCallReInviteRefused(Call call, String reason, Message resp) {
        printLog("onCallReInviteRefused()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("RE-INVITE-REFUSED (" + reason + ")/CALL", LogLevel.HIGH);
        if (m_userAgentListener != null) m_userAgentListener.onUaCallFailed(this);
    }

    /** Callback function called when arriving a 4xx (call failure) */
    public void onCallRefused(Call call, String reason, Message resp) {
        printLog("onCallRefused()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("REFUSED (" + reason + ")", LogLevel.HIGH);
        changeStatus(UA_IDLE);
        if (call == call_transfer) {
            StatusLine status_line = resp.getStatusLine();
            int code = status_line.getCode();
            this.call.notify(code, reason);
            call_transfer = null;
        }
        if (clip_off != null) clip_off.replay();
        if (m_userAgentListener != null) m_userAgentListener.onUaCallFailed(this);
    }

    /** Callback function called when arriving a 3xx (call redirection) */
    public void onCallRedirection(Call call, String reason, Vector contact_list, Message resp) {
        printLog("onCallRedirection()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("REDIRECTION (" + reason + ")", LogLevel.HIGH);
        call.call(((String) contact_list.elementAt(0)));
    }

    /** Callback function that may be overloaded (extended). Called when arriving a CANCEL request */
    public void onCallCanceling(Call call, Message cancel) {
        printLog("onCallCanceling()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("CANCEL", LogLevel.HIGH);
        changeStatus(UA_IDLE);
        if (clip_ring != null) clip_ring.stop();
        if (clip_off != null) clip_off.replay();
        if (m_userAgentListener != null) m_userAgentListener.onUaCallCancelled(this);
    }

    /** Callback function called when arriving a BYE request */
    public void onCallClosing(Call call, Message bye) {
        printLog("onCallClosing()", LogLevel.LOW);
        if (call != this.call && call != call_transfer) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        if (call != call_transfer && call_transfer != null) {
            printLog("CLOSE PREVIOUS CALL", LogLevel.HIGH);
            this.call = call_transfer;
            call_transfer = null;
            return;
        }
        printLog("CLOSE", LogLevel.HIGH);
        closeMediaApplication();
        if (clip_off != null) clip_off.replay();
        if (m_userAgentListener != null) m_userAgentListener.onUaCallClosed(this);
        changeStatus(UA_IDLE);
    }

    /** Callback function called when arriving a response after a BYE request (call closed) */
    public void onCallClosed(Call call, Message resp) {
        printLog("onCallClosed()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("CLOSE/OK", LogLevel.HIGH);
        if (m_userAgentListener != null) m_userAgentListener.onUaCallClosed(this);
        changeStatus(UA_IDLE);
    }

    /** Callback function called when the invite expires */
    public void onCallTimeout(Call call) {
        printLog("onCallTimeout()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("NOT FOUND/TIMEOUT", LogLevel.HIGH);
        changeStatus(UA_IDLE);
        if (call == call_transfer) {
            int code = 408;
            String reason = "Request Timeout";
            this.call.notify(code, reason);
            call_transfer = null;
        }
        if (clip_off != null) clip_off.replay();
        if (m_userAgentListener != null) m_userAgentListener.onUaCallFailed(this);
    }

    /** Callback function called when arriving a new REFER method (transfer request) */
    public void onCallTransfer(ExtendedCall call, NameAddress refer_to, NameAddress refered_by, Message refer) {
        printLog("onCallTransfer()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("Transfer to " + refer_to.toString(), LogLevel.HIGH);
        call.acceptTransfer();
        call_transfer = new ExtendedCall(m_sipProvider, m_userProfile.m_strFromURL, m_userProfile.m_strContactURL, this);
        call_transfer.call(refer_to.toString(), m_strLocalSession);
    }

    /** Callback function called when a call transfer is accepted. */
    public void onCallTransferAccepted(ExtendedCall call, Message resp) {
        printLog("onCallTransferAccepted()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("Transfer accepted", LogLevel.HIGH);
    }

    /** Callback function called when a call transfer is refused. */
    public void onCallTransferRefused(ExtendedCall call, String reason, Message resp) {
        printLog("onCallTransferRefused()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("Transfer refused", LogLevel.HIGH);
    }

    /** Callback function called when a call transfer is successfully completed */
    public void onCallTransferSuccess(ExtendedCall call, Message notify) {
        printLog("onCallTransferSuccess()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("Transfer successed", LogLevel.HIGH);
        call.hangup();
        if (m_userAgentListener != null) m_userAgentListener.onUaCallTrasferred(this);
    }

    /** Callback function called when a call transfer is NOT sucessfully completed */
    public void onCallTransferFailure(ExtendedCall call, String reason, Message notify) {
        printLog("onCallTransferFailure()", LogLevel.LOW);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("Transfer failed", LogLevel.HIGH);
    }

    /** Schedules a re-inviting event after <i>delay_time</i> secs. */
    public void reInvite(final String contact_url, final int delay_time) {
        SessionDescriptor sdp = new SessionDescriptor(m_strLocalSession);
        final SessionDescriptor new_sdp = new SessionDescriptor(sdp.getOrigin(), sdp.getSessionName(), new ConnectionField("IP4", "0.0.0.0"), new TimeField());
        new_sdp.addMediaDescriptors(sdp.getMediaDescriptors());
        (new Thread() {

            public void run() {
                runReInvite(contact_url, new_sdp.toString(), delay_time);
            }
        }).start();
    }

    /** Re-invite. */
    private void runReInvite(String contact, String body, int delay_time) {
        try {
            if (delay_time > 0) Thread.sleep(delay_time * 1000);
            printLog("RE-INVITING/MODIFING");
            if (call != null && call.isOnCall()) {
                printLog("REFER/TRANSFER");
                call.modify(contact, body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Schedules a call-transfer event after <i>delay_time</i> secs. */
    public void callTransfer(final String transfer_to, final int delay_time) {
        (new Thread() {

            public void run() {
                runCallTransfer(transfer_to, delay_time);
            }
        }).start();
    }

    /** Call-transfer. */
    private void runCallTransfer(String transfer_to, int delay_time) {
        try {
            if (delay_time > 0) Thread.sleep(delay_time * 1000);
            if (call != null && call.isOnCall()) {
                printLog("REFER/TRANSFER");
                call.transfer(transfer_to);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Schedules an automatic answer event after <i>delay_time</i> secs. */
    public void automaticAccept(final int delay_time) {
        (new Thread() {

            public void run() {
                runAutomaticAccept(delay_time);
            }
        }).start();
    }

    /** Automatic answer. */
    private void runAutomaticAccept(int delay_time) {
        try {
            if (delay_time > 0) Thread.sleep(delay_time * 1000);
            if (call != null) {
                printLog("AUTOMATIC-ANSWER");
                accept();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Schedules an automatic hangup event after <i>delay_time</i> secs. */
    void automaticHangup(final int delay_time) {
        (new Thread() {

            public void run() {
                runAutomaticHangup(delay_time);
            }
        }).start();
    }

    /** Automatic hangup. */
    private void runAutomaticHangup(int delay_time) {
        try {
            if (delay_time > 0) Thread.sleep(delay_time * 1000);
            if (call != null && call.isOnCall()) {
                printLog("AUTOMATIC-HANGUP");
                hangup();
                listen();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Adds a new string to the default EventLogger */
    public void printLog(String str) {
        printLog(str, LogLevel.HIGH);
    }

    /** Adds a new string to the default EventLogger */
    public void printLog(String str, int level) {
        if (m_eventLogger != null) m_eventLogger.println("UA: " + str, level + SipStack.LOG_LEVEL_UA);
        if ((m_userProfile == null || !m_userProfile.m_bNoPrompt) && level <= LogLevel.HIGH) System.out.println("UA: " + str);
    }

    /** Adds the Exception message to the default EventLogger */
    void printException(Exception e, int level) {
        if (m_eventLogger != null) m_eventLogger.printException(e, level + SipStack.LOG_LEVEL_UA);
    }
}
