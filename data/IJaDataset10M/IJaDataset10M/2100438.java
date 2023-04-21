package net.bnubot.util.music;

import net.bnubot.util.OperatingSystem;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;

/**
 * @author scotta
 */
class MCiTunesWindows implements MusicController {

    static {
        if (!OperatingSystem.userOS.equals(OperatingSystem.WINDOWS)) throw new IllegalStateException("Only supported by Windows");
    }

    private void comCommand(String cmd) {
        try {
            ComThread.InitMTA(true);
            Dispatch.call(new ActiveXComponent("iTunes.Application").getObject(), cmd);
            ComThread.Release();
        } catch (Throwable t) {
            throw new IllegalStateException(t);
        }
    }

    public void play() {
        comCommand("Play");
    }

    public void pause() {
        comCommand("Pause");
    }

    public String getCurrentlyPlaying() {
        return "[unsupported]";
    }
}
