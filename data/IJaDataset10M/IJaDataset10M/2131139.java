package be.lassi.cues;

import org.apache.commons.lang.builder.HashCodeBuilder;
import be.lassi.context.ShowContext;
import be.lassi.mac.AppleScript;
import be.lassi.mac.AppleScriptImpl;
import be.lassi.ui.util.mac.MacApplication;
import be.lassi.util.Util;
import be.lassi.util.equals.EqualsTester;

/**
 * Details about an audio cue (currently just a placeholder for
 * possible future implementation).
 */
public class AudioCueDetail extends CueDetail {

    public enum Command {

        START("Start"), STOP("Stop");

        private String name;

        private Command(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private final Command command;

    private final String track;

    private final AppleScript script;

    public AudioCueDetail(final Command command) {
        this(command, "");
    }

    public AudioCueDetail(final Command command, final String track) {
        this(command, track, new AppleScriptImpl());
    }

    public AudioCueDetail(final Command command, final String track, final AppleScript script) {
        this.command = command;
        this.track = track;
        this.script = script;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CueDetail copy() {
        return new AudioCueDetail(command, track);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void append(final StringBuilder b) {
        b.append("audio");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return "Audio";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = { "BC" })
    public boolean equals(final Object object) {
        boolean result = this == object;
        if (!result) {
            EqualsTester tester = EqualsTester.get(this, object);
            if (tester.isEquals()) {
                AudioCueDetail other = (AudioCueDetail) object;
                tester.test(command, other.command);
                tester.test(track, other.track);
            }
            result = tester.isEquals();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        HashCodeBuilder b = new HashCodeBuilder(-1315605179, 708886931);
        b.append(command);
        b.append(track);
        return b.toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(getClass().getName());
        b.append("(");
        b.append(command.getName());
        if (command == Command.START) {
            b.append(" ");
            b.append(track);
        }
        b.append(")");
        String string = b.toString();
        return string;
    }

    public Command getCommand() {
        return command;
    }

    public String getTrack() {
        return track;
    }

    public void go(final ShowContext context) {
        if (command == Command.STOP) {
            context.getAudio().stop();
            goITunes();
        } else if (track.endsWith(".WAV")) {
            context.getAudio().play(track);
        } else if (MacApplication.isMac()) {
            goITunes();
        }
    }

    private void goITunes() {
        StringBuilder b = new StringBuilder();
        b.append("tell application \"iTunes\" to ");
        if (command == Command.STOP) {
            b.append("stop");
        } else {
            b.append("play track ");
            if (Util.isNumeric(track)) {
                b.append(track);
            } else {
                b.append("\"");
                b.append(track);
                b.append("\"");
            }
            b.append(" in playlist \"Lassi\" once true");
        }
        String string = b.toString();
        script.execute(string);
    }
}
