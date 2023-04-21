package com.sesca.voip.ua;

import local.media.AudioClipPlayer;
import local.ua.MediaLauncher;
import local.ua.UserAgentListener;
import org.zoolu.sip.call.*;
import org.zoolu.sip.address.*;
import org.zoolu.sip.provider.SipStack;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.provider.TransactionIdentifier;
import org.zoolu.sip.header.StatusLine;
import org.zoolu.sip.message.*;
import org.zoolu.sdp.*;
import org.zoolu.tools.Log;
import org.zoolu.tools.LogLevel;
import org.zoolu.tools.Parser;
import org.zoolu.tools.Archive;
import com.sesca.audio.AudioCodecConfiguration;
import com.sesca.misc.Config;
import com.sesca.misc.Logger;
import com.sesca.voip.media.JAudioLauncher;
import com.sesca.voip.media.TunneledAudioLauncher;
import java.util.Enumeration;
import java.util.Vector;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

/** Simple SIP user agent (UA).
 * It includes audio/video applications.
 * <p>
 * It can use external audio/video tools as media applications.
 * Currently only RAT (Robust Audio Tool) and VIC are supported as external applications.
 */
public class UserAgent extends CallListenerAdapter {

    SocketChannel audiochannel = null;

    Config conf = new Config();

    public boolean tunnel = false;

    /** Event logger. */
    Log log;

    public int lastResponseCode = 0;

    URL codeBase = null;

    /** UserAgentProfile */
    public UserAgentProfile user_profile;

    /** SipProvider */
    protected SipProvider sip_provider;

    protected ExtendedCall call;

    /** Call transfer */
    protected ExtendedCall call_transfer;

    /** Audio application */
    protected MediaLauncher audio_app = null;

    /** Video application */
    protected MediaLauncher video_app = null;

    /** Local sdp */
    protected String local_session = null;

    /** UserAgent listener */
    protected UserAgentListener listener = null;

    /** Media file path */
    final String MEDIA_PATH = "media/local/ua/";

    final String CLIP_ON = "ringing_8k.wav";

    /** Off wav file */
    final String CLIP_OFF = "busy_8k.wav";

    /** Ring wav file */
    final String CLIP_RING = null;

    /** Ring sound */
    public AudioClipPlayer clip_ring;

    /** On sound */
    public AudioClipPlayer clip_on;

    /** Off sound */
    public AudioClipPlayer clip_off;

    public boolean callInProgress = false;

    /** UA_IDLE=0 */
    static final String UA_IDLE = "IDLE";

    /** UA_INCOMING_CALL=1 */
    static final String UA_INCOMING_CALL = "INCOMING_CALL";

    /** UA_OUTGOING_CALL=2 */
    static final String UA_OUTGOING_CALL = "OUTGOING_CALL";

    /** UA_ONCALL=3 */
    static final String UA_ONCALL = "ONCALL";

    /** Call state
	 * <P>UA_IDLE=0, <BR>UA_INCOMING_CALL=1, <BR>UA_OUTGOING_CALL=2, <BR>UA_ONCALL=3 */
    String call_state = UA_IDLE;

    /** Changes the call state */
    protected void changeStatus(String state) {
        call_state = state;
    }

    /** Checks the call state */
    protected boolean statusIs(String state) {
        return call_state.equals(state);
    }

    /** Gets the call state */
    public String getStatus() {
        return call_state;
    }

    /** Sets the automatic answer time (default is -1 that means no auto accept mode) */
    public void setAcceptTime(int accept_time) {
        user_profile.accept_time = accept_time;
    }

    /** Sets the automatic hangup time (default is 0, that corresponds to manual hangup mode) */
    public void setHangupTime(int time) {
        user_profile.hangup_time = time;
    }

    /** Sets the redirection url (default is null, that is no redircetion) */
    public void setRedirection(String url) {
        user_profile.redirect_to = url;
    }

    /** Sets the no offer mode for the invite (default is false) */
    public void setNoOfferMode(boolean nooffer) {
        user_profile.no_offer = nooffer;
    }

    /** Enables audio */
    public void setAudio(boolean enable) {
        user_profile.audio = enable;
    }

    /** Enables video */
    public void setVideo(boolean enable) {
        user_profile.video = enable;
    }

    /** Sets the receive only mode */
    public void setReceiveOnlyMode(boolean r_only) {
        user_profile.recv_only = r_only;
    }

    /** Sets the send only mode */
    public void setSendOnlyMode(boolean s_only) {
        user_profile.send_only = s_only;
    }

    /** Sets the send tone mode */
    public void setSendToneMode(boolean s_tone) {
        user_profile.send_tone = s_tone;
    }

    /** Sets the send file */
    public void setSendFile(String file_name) {
        user_profile.send_file = file_name;
    }

    /** Sets the recv file */
    public void setRecvFile(String file_name) {
        user_profile.recv_file = file_name;
    }

    /** Gets the local SDP */
    public String getSessionDescriptor() {
        Logger.paranoia("UserAgent.getSessioDescriptor(): local_session=" + local_session);
        return local_session;
    }

    /** Inits the local SDP (no media spec) */
    public void initSessionDescriptor() {
        SessionDescriptor sdp = new SessionDescriptor(user_profile.from_url, sip_provider.getViaAddress());
        local_session = sdp.toString();
        Logger.paranoia("UserAgent.initSessioDescriptor(): local_session=" + local_session);
    }

    /** Costructs a UA with a default media port */
    public UserAgent(SipProvider sip_provider, UserAgentProfile user_profile, UserAgentListener listener, URL codeBase) {
        this.sip_provider = sip_provider;
        log = sip_provider.getLog();
        this.listener = listener;
        this.user_profile = user_profile;
        user_profile.initContactAddress(sip_provider);
        if (!user_profile.use_rat && !user_profile.use_jmf) {
        }
        if (!user_profile.use_rat) {
            try {
                if (true) {
                    if (CLIP_ON != null) clip_on = new AudioClipPlayer(Archive.getAudioInputStream(new URL(codeBase, CLIP_ON)), null);
                    if (CLIP_OFF != null) clip_off = new AudioClipPlayer(Archive.getAudioInputStream(new URL(codeBase, CLIP_OFF)), null);
                    if (CLIP_RING != null) clip_ring = new AudioClipPlayer(Archive.getAudioInputStream(new URL(codeBase, CLIP_RING)), null);
                }
            } catch (Exception e) {
                Logger.error("Error occured while loading audio files");
                e.printStackTrace();
            }
        }
        initSessionDescriptor();
        if (user_profile.audio || !user_profile.video) {
            Logger.paranoia("UserAgent.UserAgent(): local_session=" + local_session);
            SessionDescriptor sdp = new SessionDescriptor(local_session);
            AudioCodecConfiguration acc = new AudioCodecConfiguration();
            String audioSdp = acc.createSdpAudioAttributes();
            if (audioSdp != null) local_session = sdp.toString() + audioSdp; else local_session = sdp.toString();
        }
    }

    /** Makes a new call (acting as UAC). */
    public void call(String target_url) {
        changeStatus(UA_OUTGOING_CALL);
        initSessionDescriptor();
        initMedia();
        call = new ExtendedCall(sip_provider, user_profile.from_url, user_profile.contact_url, user_profile.username, user_profile.realm, user_profile.passwd, this);
        target_url = sip_provider.completeNameAddress(target_url).toString();
        if (user_profile.no_offer) {
            Logger.debug("user_profile.no_offer");
            if (tunnel) preOpenSocketForAudioTunneling();
            call.call(target_url);
        } else {
            Logger.debug("!user_profile.no_offer");
            if (tunnel) preOpenSocketForAudioTunneling();
            call.call(target_url, local_session);
        }
    }

    /** Waits for an incoming call (acting as UAS). */
    public void listen() {
        sip_provider.removeSipProviderListener(new TransactionIdentifier(SipMethods.INVITE));
        Logger.debug("UA is listening");
        changeStatus(UA_IDLE);
        call = new ExtendedCall(sip_provider, user_profile.from_url, user_profile.contact_url, user_profile.username, user_profile.realm, user_profile.passwd, this);
        Logger.paranoia("       call=" + call);
        call.listen();
    }

    /** Closes an ongoing, incoming, or pending call */
    public void hangup() {
        if (clip_off != null) if (!call_state.equals(UA_IDLE) && !call_state.equals(UA_INCOMING_CALL)) clip_off.loop(); else Logger.warning("clip_off==null");
        if (clip_ring != null) {
            clip_ring.stop();
        }
        closeAudioSocket();
        if (call != null) {
            if (clip_on != null) clip_on.stop(); else Logger.warning("clip_on==null");
            call.hangup();
        }
        changeStatus(UA_IDLE);
    }

    /** Closes an ongoing, incoming, or pending call */
    public void accept() {
        if (clip_ring != null) clip_ring.stop();
        if (call != null) call.accept(local_session);
    }

    /** Redirects an incoming call */
    public void redirect(String redirection) {
        if (clip_ring != null) clip_ring.stop();
        if (call != null) call.redirect(redirection);
    }

    /** Launches the Media Application (currently, the RAT audio tool) */
    protected void launchMediaApplication(boolean incoming) {
        Logger.debug("UserAgent.launchMediaApplication");
        if (audio_app != null || video_app != null) {
            Logger.warning("DEBUG: media application is already running");
            Logger.debug("Stopping media application...");
            if (audio_app != null) {
                audio_app.stopMedia();
                audio_app = null;
            }
            if (video_app != null) {
                video_app.stopMedia();
                video_app = null;
            }
        }
        Logger.debug("audio_app=" + audio_app);
        Logger.paranoia("call=" + call);
        String localSdp = call.getLocalSessionDescriptor();
        Logger.paranoia("localSdp=" + localSdp);
        SessionDescriptor local_sdp = new SessionDescriptor(localSdp);
        Logger.paranoia("local_sdp=" + local_sdp);
        String local_media_address = (new Parser(local_sdp.getConnection().toString())).skipString().skipString().getString();
        int local_audio_port = 0;
        int local_video_port = 0;
        int payloadType = -1;
        Logger.paranoia("UserAgent.launcMediaApplication: local sdp=" + local_sdp.toString());
        for (Enumeration e = local_sdp.getMediaDescriptors().elements(); e.hasMoreElements(); ) {
            MediaField media = ((MediaDescriptor) e.nextElement()).getMedia();
            if (media.getMedia().equals("audio")) if (incoming) payloadType = Integer.parseInt(media.getFormatList().elementAt(0).toString());
            local_audio_port = media.getPort();
            if (media.getMedia().equals("video")) local_video_port = media.getPort();
        }
        SessionDescriptor remote_sdp = new SessionDescriptor(call.getRemoteSessionDescriptor());
        String remote_media_address = (new Parser(remote_sdp.getConnection().toString())).skipString().skipString().getString();
        int remote_audio_port = 0;
        int remote_video_port = 0;
        Logger.hysteria("UserAgent.launcMediaApplication: remote sdp=" + remote_sdp.toString());
        for (Enumeration e = remote_sdp.getMediaDescriptors().elements(); e.hasMoreElements(); ) {
            MediaField media = ((MediaDescriptor) e.nextElement()).getMedia();
            if (media.getMedia().equals("audio")) {
                remote_audio_port = media.getPort();
                if (!incoming) payloadType = Integer.parseInt(media.getFormatList().elementAt(0).toString());
                Logger.debug("parsed media format=" + payloadType);
            }
            if (media.getMedia().equals("video")) remote_video_port = media.getPort();
        }
        int dir = 0;
        if (user_profile.recv_only) dir = -1; else if (user_profile.send_only) dir = 1;
        if (user_profile.audio && local_audio_port != 0 && remote_audio_port != 0) {
            if (audio_app == null) {
                String audio_in = null;
                if (user_profile.send_tone) audio_in = JAudioLauncher.TONE; else if (user_profile.send_file != null) audio_in = user_profile.send_file;
                String audio_out = null;
                if (user_profile.recv_file != null) audio_out = user_profile.recv_file;
                Logger.debug("Audio_out=" + audio_out);
                if (tunnel) {
                    Logger.info("New tunneledaudiolauncher");
                    audio_app = new TunneledAudioLauncher(local_audio_port, remote_media_address, remote_audio_port, dir, audio_in, audio_out, payloadType, 8000, 1, 160, log, audiochannel, this);
                } else audio_app = new JAudioLauncher(local_audio_port, remote_media_address, remote_audio_port, dir, audio_in, audio_out, payloadType, 8000, 1, 160, log);
            }
            audio_app.startMedia();
        }
    }

    /** Close the Media Application  */
    protected void closeMediaApplication() {
        if (audio_app != null) {
            audio_app.stopMedia();
            audio_app = null;
        }
        if (video_app != null) {
            video_app.stopMedia();
            video_app = null;
        }
    }

    /** Callback function called when arriving a new INVITE method (incoming call) */
    public void onCallIncoming(Call call, NameAddress callee, NameAddress caller, String sdp, Message invite) {
        Logger.paranoia("UserAgent.onCallIncoming");
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            Logger.paranoia("NOT the current call");
            Logger.paranoia("       incoming=" + call);
            Logger.paranoia("       current=" + this.call);
            return;
        }
        printLog("INCOMING", LogLevel.HIGH);
        changeStatus(UA_INCOMING_CALL);
        Logger.paranoia("       invoking call.ring()");
        call.ring();
        if (sdp != null) {
            SessionDescriptor remote_sdp = new SessionDescriptor(sdp);
            SessionDescriptor local_sdp = new SessionDescriptor(local_session);
            SessionDescriptor new_sdp = new SessionDescriptor(remote_sdp.getOrigin(), remote_sdp.getSessionName(), local_sdp.getConnection(), local_sdp.getTime());
            local_session = new_sdp.toString() + new AudioCodecConfiguration().createSdpAudioAttributes(remote_sdp);
            Logger.paranoia("UserAgent.onCallIncoming(): local_session=" + local_session);
        }
        if (clip_ring != null) clip_ring.loop();
        if (listener != null) {
            listener.onUaCallIncoming(this, callee, caller);
            Logger.paranoia("       listener=" + listener);
        } else Logger.paranoia("       listener=NULL!");
    }

    /** Callback function called when arriving a new Re-INVITE method (re-inviting/call modify) */
    public void onCallModifying(Call call, String sdp, Message invite) {
        Logger.paranoia("UserAgent.onCallModifying");
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("RE-INVITE/MODIFY", LogLevel.HIGH);
        super.onCallModifying(call, sdp, invite);
    }

    /** Callback function that may be overloaded (extended). Called when arriving a 180 Ringing */
    public void onCallRinging(Call call, Message resp) {
        Logger.paranoia("UserAgent.onCallRinging");
        if (call != this.call && call != call_transfer) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        if (listener != null) listener.onUaCallRinging(this);
    }

    /** Callback function called when arriving a 2xx (call accepted) */
    public void onCallAccepted(Call call, String sdp, Message resp) {
        Logger.paranoia("UserAgent.onCallAccepted: sdp=" + sdp);
        printLog("onCallAccepted()", LogLevel.LOW);
        if (call != this.call && call != call_transfer) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        changeStatus(UA_ONCALL);
        if (user_profile.no_offer) {
            Logger.debug("UserAgent.onCallAccepted: user:profile.no_offer");
            SessionDescriptor remote_sdp = new SessionDescriptor(sdp);
            SessionDescriptor local_sdp = new SessionDescriptor(local_session);
            SessionDescriptor new_sdp = new SessionDescriptor(remote_sdp.getOrigin(), remote_sdp.getSessionName(), local_sdp.getConnection(), local_sdp.getTime());
            new_sdp.addMediaDescriptors(local_sdp.getMediaDescriptors());
            new_sdp = SdpTools.sdpMediaProduct(new_sdp, remote_sdp.getMediaDescriptors());
            new_sdp = SdpTools.sdpAttirbuteSelection(new_sdp, "rtpmap");
            local_session = new_sdp.toString();
            call.ackWithAnswer(local_session);
        }
        if (clip_on != null) clip_on.stop(); else Logger.warning("clip_on==null");
        if (listener != null) listener.onUaCallAccepted(this);
        launchMediaApplication(false);
        if (call == call_transfer) {
            StatusLine status_line = resp.getStatusLine();
            int code = status_line.getCode();
            String reason = status_line.getReason();
            this.call.notify(code, reason);
        }
    }

    /** Callback function called when arriving an ACK method (call confirmed) */
    public void onCallConfirmed(Call call, String sdp, Message ack) {
        Logger.paranoia("onCallConfirmed()");
        Logger.paranoia("sdp=" + sdp);
        Logger.paranoia("local_session=" + local_session);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("CONFIRMED/CALL", LogLevel.HIGH);
        changeStatus(UA_ONCALL);
        launchMediaApplication(true);
        if (user_profile.hangup_time > 0) this.automaticHangup(user_profile.hangup_time);
    }

    /** Callback function called when arriving a 2xx (re-invite/modify accepted) */
    public void onCallReInviteAccepted(Call call, String sdp, Message resp) {
        Logger.paranoia("UserAgent.onCallReInviteAccepted");
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("RE-INVITE-ACCEPTED/CALL", LogLevel.HIGH);
    }

    /** Callback function called when arriving a 4xx (re-invite/modify failure) */
    public void onCallReInviteRefused(Call call, String reason, Message resp) {
        Logger.paranoia("UserAgent.onCallReInviteRefused");
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("RE-INVITE-REFUSED (" + reason + ")/CALL", LogLevel.HIGH);
        if (listener != null) listener.onUaCallFailed(this);
    }

    /** Callback function called when arriving a 4xx (call failure) */
    public void onCallRefused(Call call, String reason, Message resp) {
        Logger.paranoia("UserAgent.onCallRefused");
        lastResponseCode = resp.getStatusLine().getCode();
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
        if (clip_on != null) clip_on.stop(); else Logger.warning("clip_on==null");
        if (clip_off != null) {
            if (lastResponseCode == 486 || lastResponseCode == 487 && callInProgress) clip_off.loop();
        } else Logger.warning("clip_off==null");
        if (listener != null) listener.onUaCallFailed(this);
    }

    /** Callback function called when arriving a 3xx (call redirection) */
    public void onCallRedirection(Call call, String reason, Vector contact_list, Message resp) {
        Logger.paranoia("UserAgent.onCallRedirection");
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("REDIRECTION (" + reason + ")", LogLevel.HIGH);
        call.call(((String) contact_list.elementAt(0)));
    }

    /** Callback function that may be overloaded (extended). Called when arriving a CANCEL request */
    public void onCallCanceling(Call call, Message cancel) {
        Logger.paranoia("UserAgent.onCallCanceling");
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("CANCEL", LogLevel.HIGH);
        changeStatus(UA_IDLE);
        if (clip_ring != null) clip_ring.stop(); else Logger.warning("clip_ring==null");
        if (clip_off != null) clip_off.loop(); else Logger.warning("clip_off==null");
        if (listener != null) listener.onUaCallCancelled(this);
    }

    /** Callback function called when arriving a BYE request */
    public void onCallClosing(Call call, Message bye) {
        Logger.paranoia("UserAgent.onCallClosing");
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
        if (clip_off != null) {
            if (!callInProgress) Logger.error("!! Call is not in progress !!");
            clip_off.loop();
        } else Logger.warning("clip_off==null");
        if (listener != null) listener.onUaCallClosed(this);
        changeStatus(UA_IDLE);
    }

    /** Callback function called when arriving a response after a BYE request (call closed) */
    public void onCallClosed(Call call, Message resp) {
        Logger.paranoia("UserAgent.onCallClosed");
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("CLOSE/OK", LogLevel.HIGH);
        if (listener != null) listener.onUaCallClosed(this);
        changeStatus(UA_IDLE);
    }

    /** Callback function called when the invite expires */
    public void onCallTimeout(Call call) {
        Logger.paranoia("UserAgent.onCallTimeout");
        lastResponseCode = 408;
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
        if (clip_on != null) clip_on.stop(); else Logger.warning("clip_on==null");
        if (clip_off != null) clip_off.replay(); else Logger.warning("clip_off==null");
        if (listener != null) listener.onUaCallFailed(this);
    }

    /** Callback function called when arriving a new REFER method (transfer request) */
    public void onCallTransfer(ExtendedCall call, NameAddress refer_to, NameAddress refered_by, Message refer) {
        Logger.paranoia("UserAgent.onCallTransfer: local_session=" + local_session);
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        initSessionDescriptor();
        initMedia();
        printLog("Transfer to " + refer_to.toString(), LogLevel.HIGH);
        call.acceptTransfer();
        call_transfer = new ExtendedCall(sip_provider, user_profile.from_url, user_profile.contact_url, this);
        call_transfer.call(refer_to.toString(), local_session);
    }

    protected void initMedia() {
        if (user_profile.audio || !user_profile.video) {
            Logger.paranoia("UserAgent.initMedia(): local_session=" + local_session);
            SessionDescriptor sdp = new SessionDescriptor(local_session);
            AudioCodecConfiguration acc = new AudioCodecConfiguration();
            String audioSdp = acc.createSdpAudioAttributes();
            if (audioSdp != null) local_session = sdp.toString() + audioSdp; else local_session = sdp.toString();
        }
    }

    /** Callback function called when a call transfer is accepted. */
    public void onCallTransferAccepted(ExtendedCall call, Message resp) {
        Logger.paranoia("UserAgent.onCallTransferAccepted");
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("Transfer accepted", LogLevel.HIGH);
    }

    /** Callback function called when a call transfer is refused. */
    public void onCallTransferRefused(ExtendedCall call, String reason, Message resp) {
        Logger.paranoia("UserAgent.onCallTransferRefused");
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("Transfer refused", LogLevel.HIGH);
    }

    /** Callback function called when a call transfer is successfully completed */
    public void onCallTransferSuccess(ExtendedCall call, Message notify) {
        Logger.paranoia("UserAgent.onCallTransferSuccess");
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("Transfer successed", LogLevel.HIGH);
        call.hangup();
        if (listener != null) listener.onUaCallTrasferred(this);
    }

    /** Callback function called when a call transfer is NOT sucessfully completed */
    public void onCallTransferFailure(ExtendedCall call, String reason, Message notify) {
        Logger.paranoia("UserAgent.onCallTransferFailure");
        if (call != this.call) {
            printLog("NOT the current call", LogLevel.LOW);
            return;
        }
        printLog("Transfer failed", LogLevel.HIGH);
    }

    /** Schedules a re-inviting event after <i>delay_time</i> secs. */
    void reInvite(final String contact_url, final int delay_time) {
        SessionDescriptor sdp = new SessionDescriptor(local_session);
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
    void callTransfer(final String transfer_to, final int delay_time) {
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
    void automaticAccept(final int delay_time) {
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

    /** Adds a new string to the default Log */
    void printLog(String str) {
        printLog(str, LogLevel.HIGH);
    }

    /** Adds a new string to the default Log */
    void printLog(String str, int level) {
        if (log != null) log.println("UA: " + str, level + SipStack.LOG_LEVEL_UA);
    }

    /** Adds the Exception message to the default Log */
    void printException(Exception e, int level) {
        if (log != null) log.printException(e, level + SipStack.LOG_LEVEL_UA);
    }

    public void onCallProvisionalResponse(Call call, int code) {
        Logger.paranoia("UserAgent.onCallProvisionalResponse");
        listener.onUaCallProvisionalResponse(code, this);
    }

    public void preOpenSocketForAudioTunneling() {
        Logger.paranoia("UserAgent.onCallOpenSocketForAudioTunneling");
        try {
            Logger.info("Preparing connection to http tunnel");
            audiochannel = SocketChannel.open();
            audiochannel.configureBlocking(false);
            audiochannel.connect(new InetSocketAddress(user_profile.tunnelServer, user_profile.tunnelPort));
            while (!audiochannel.finishConnect()) {
            }
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void onTunnelTerminated() {
        return;
    }

    public void closeAudioSocket() {
        if (audiochannel != null) {
            try {
                Logger.info("Trying to close audiosocket");
                audiochannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else Logger.info("Audiosocketti oli jo kiinni");
        closeMediaApplication();
    }

    void generateTone(int i, int duration) {
        if (audio_app instanceof JAudioLauncher) {
            JAudioLauncher jau = (JAudioLauncher) audio_app;
            jau.walker(i, duration);
        } else if (audio_app instanceof TunneledAudioLauncher) {
            TunneledAudioLauncher tau = (TunneledAudioLauncher) audio_app;
            tau.walker(i, duration);
        } else System.out.println("audio_app ei tue dtmf:��");
    }
}
