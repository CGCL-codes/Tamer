package org.monome.pages;

import java.io.IOException;
import com.illposed.osc.OSCMessage;

public class AbletonOSCControl implements AbletonControl {

    Configuration configuration;

    public AbletonOSCControl(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
	 * Sends "/live/arm track" to LiveOSC.
	 * 
	 * @param track The track number to arm (0 = first track)
	 */
    public void armTrack(int track) {
        Object args[] = new Object[2];
        args[0] = new Integer(track);
        args[1] = new Integer(1);
        OSCMessage msg = new OSCMessage("/live/arm", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        args = new Object[2];
        args[0] = new Integer(track);
        msg = new OSCMessage("/live/arm", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Sends "/live/disarm track" to LiveOSC.
	 * 
	 * @param track The track number to disarm (0 = first track)
	 */
    public void disarmTrack(int track) {
        Object args[] = new Object[2];
        args[0] = new Integer(track);
        args[1] = new Integer(0);
        OSCMessage msg = new OSCMessage("/live/arm", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        args = new Object[2];
        args[0] = new Integer(track);
        msg = new OSCMessage("/live/arm", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Sends "/live/play/clip track clip" to LiveOSC.
	 * 
	 * @param track The track number to play (0 = first track)
	 * @param clip The clip number to play (0 = first clip)
	 */
    public void playClip(int track, int clip) {
        Object args[] = new Object[2];
        args[0] = new Integer(track);
        args[1] = new Integer(clip);
        OSCMessage msg = new OSCMessage("/live/play/clipslot", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.refreshClipInfo(track, clip);
    }

    public void stopClip(int track, int clip) {
        Object args[] = new Object[2];
        args[0] = new Integer(track);
        args[1] = new Integer(clip);
        OSCMessage msg = new OSCMessage("/live/stop/clip", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.refreshClipInfo(track, clip);
    }

    /**
	 * Sends "/live/redo" to LiveOSC. 
	 */
    public void redo() {
        OSCMessage msg = new OSCMessage("/live/redo");
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.refreshAbleton();
    }

    public void setOverdub(int overdub) {
        Object args[] = new Object[1];
        args[0] = new Integer(overdub);
        OSCMessage msg = new OSCMessage("/live/overdub", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.refreshState();
    }

    public void tempoUp(float tempo) {
        if (tempo + 1.0 > 999.0) {
            tempo = (float) 998.0;
        }
        Object args[] = new Object[1];
        args[0] = new Float(tempo + 1.0);
        OSCMessage msg = new OSCMessage("/live/tempo", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.refreshState();
        this.refreshState();
        this.refreshState();
        this.refreshState();
        this.refreshState();
    }

    public void tempoDown(float tempo) {
        if (tempo - 1.0 < 20.0) {
            tempo = (float) 21.0;
        }
        Object args[] = new Object[1];
        args[0] = new Float(tempo - 1.0);
        OSCMessage msg = new OSCMessage("/live/tempo", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.refreshState();
        this.refreshState();
        this.refreshState();
        this.refreshState();
        this.refreshState();
    }

    /**
	 * Sends "/live/stop/track track" to LiveOSC.
	 * 
	 * @param track The track number to stop (0 = first track)
	 */
    public void stopTrack(int track) {
        Object args[] = new Object[1];
        args[0] = new Integer(track);
        OSCMessage msg = new OSCMessage("/live/stop/track", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.refreshTrackInfo(track);
    }

    public void muteTrack(int track) {
        Object args[] = new Object[2];
        args[0] = new Integer(track);
        args[1] = new Integer(1);
        OSCMessage msg = new OSCMessage("/live/mute", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        args = new Object[1];
        args[0] = new Integer(track);
        msg = new OSCMessage("/live/mute", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unmuteTrack(int track) {
        Object args[] = new Object[2];
        args[0] = new Integer(track);
        args[1] = new Integer(0);
        OSCMessage msg = new OSCMessage("/live/mute", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        args = new Object[1];
        args[0] = new Integer(track);
        msg = new OSCMessage("/live/mute", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void soloTrack(int track) {
        Object args[] = new Object[2];
        args[0] = new Integer(track);
        args[1] = new Integer(1);
        OSCMessage msg = new OSCMessage("/live/solo", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        args = new Object[1];
        args[0] = new Integer(track);
        msg = new OSCMessage("/live/solo", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unsoloTrack(int track) {
        Object args[] = new Object[2];
        args[0] = new Integer(track);
        args[1] = new Integer(0);
        OSCMessage msg = new OSCMessage("/live/solo", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        args = new Object[1];
        args[0] = new Integer(track);
        msg = new OSCMessage("/live/solo", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Sends "/live/track/view track" to LiveOSC.
	 * 
	 * @param track The track number to stop (0 = first track)
	 */
    public void viewTrack(int track) {
        Object args[] = new Object[1];
        args[0] = new Integer(track);
        OSCMessage msg = new OSCMessage("/live/track/view", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void trackJump(int track, float amount) {
        Object args[] = new Object[2];
        args[0] = new Integer(track);
        args[1] = new Float(amount);
        OSCMessage msg = new OSCMessage("/live/track/jump", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Sends "/live/undo" to LiveOSC. 
	 */
    public void undo() {
        System.out.println("ableton undo()");
        OSCMessage msg = new OSCMessage("/live/undo");
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.refreshAbleton();
    }

    public void launchScene(int scene_num) {
        Object args[] = new Object[1];
        args[0] = new Integer(scene_num);
        OSCMessage msg = new OSCMessage("/live/play/scene", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.refreshState();
    }

    public void refreshClipInfo(int trackNum, int clipNum) {
        Object args[] = new Object[2];
        args[0] = trackNum;
        args[1] = clipNum;
        OSCMessage msg = new OSCMessage("/live/clip/info", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshAllTracks() {
        OSCMessage msg = new OSCMessage("/live/track/info");
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTrackInfo(int trackNum) {
        Object args[] = new Object[1];
        args[0] = trackNum;
        OSCMessage msg = new OSCMessage("/live/track/info", args);
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshState() {
        OSCMessage msg = new OSCMessage("/live/state");
        OSCMessage msg2 = new OSCMessage("/live/tempo");
        OSCMessage msg3 = new OSCMessage("/live/overdub");
        OSCMessage msg4 = new OSCMessage("/live/scene");
        try {
            this.configuration.getAbletonOSCPortOut().send(msg);
            this.configuration.getAbletonOSCPortOut().send(msg2);
            this.configuration.getAbletonOSCPortOut().send(msg3);
            this.configuration.getAbletonOSCPortOut().send(msg4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshAbleton() {
        this.configuration.abletonState.reset();
        refreshAllTracks();
        refreshState();
    }

    public void resetAbleton() {
        this.configuration.abletonState.reset();
    }
}
