package net.sf.japi.tools.midiMonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;
import net.sf.japi.io.args.ArgParser;
import net.sf.japi.io.args.CommandWithHelp;
import net.sf.japi.io.args.Option;
import net.sf.japi.io.args.OptionType;
import net.sf.japi.io.args.RequiredOptionsMissingException;
import net.sf.japi.midi.MonitorReceiver;
import org.jetbrains.annotations.NotNull;

/** MidiMonitor is a command line program for monitoring MIDI transmitters.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.1
 */
public class MidiMonitor extends CommandWithHelp {

    /** The transmitters that shall be monitored. */
    private final Map<String, List<Transmitter>> transmitters = new HashMap<String, List<Transmitter>>();

    /** Whether or not to print time events (0xF8).
     * @see #setNoTime()
     */
    private boolean time = true;

    /** Main program.
     * @param args Command line arguments (try --help).
     */
    public static void main(final String... args) {
        ArgParser.simpleParseAndRun(new MidiMonitor(), args);
    }

    /** Sets that time events (0xF8) shall be filtered. */
    @Option("notime")
    public void setNoTime() {
        time = false;
    }

    /** Adds all available transmitters to the list of transmitters to monitor.
     * @throws MidiUnavailableException if MIDI is unavaialble.
     */
    @Option({ "a", "all" })
    public void addAllTransmitters() throws MidiUnavailableException {
        for (final MidiDevice.Info deviceInfo : MidiSystem.getMidiDeviceInfo()) {
            addTransmitter(deviceInfo);
        }
    }

    /** Adds a transmitter to the list of transmitters to monitor.
     * If multiple devices have the same name, the transmitter is added for the first device of that name.
     * @param transmitterName Name of the transmitterName to add.
     * @throws MidiUnavailableException If MIDI is unavailable.
     */
    @Option({ "t", "transmitter" })
    public void addTransmitter(@NotNull final String transmitterName) throws MidiUnavailableException {
        for (final MidiDevice.Info deviceInfo : MidiSystem.getMidiDeviceInfo()) {
            if (transmitterName.equals(deviceInfo.getName())) {
                addTransmitter(deviceInfo);
                return;
            }
        }
    }

    /** Adds a transmitter.
     * @param deviceInfo DeviceInfo for the transmitter to add.
     * @return <code>true</code> if a transmitter for the specified device was added, otherwise <code>false</code>.
     * @throws MidiUnavailableException If MIDI is unavailable.
     */
    private boolean addTransmitter(@NotNull final MidiDevice.Info deviceInfo) throws MidiUnavailableException {
        final MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
        if (device.getMaxTransmitters() != 0) {
            device.open();
            final String deviceName = deviceInfo.getName();
            if (transmitters.get(deviceName) == null) {
                transmitters.put(deviceName, new ArrayList<Transmitter>());
            }
            transmitters.get(deviceName).add(device.getTransmitter());
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "InstanceMethodNamingConvention" })
    public int run(@NotNull final List<String> args) throws Exception {
        final Set<Map.Entry<String, List<Transmitter>>> entries = transmitters.entrySet();
        if (entries.size() == 0) {
            throw new RequiredOptionsMissingException("-a or at least once -t");
        }
        for (final Map.Entry<String, List<Transmitter>> entry : entries) {
            for (final Transmitter transmitter : entry.getValue()) {
                if (time) {
                    transmitter.setReceiver(new MonitorReceiver(entry.getKey()));
                } else {
                    transmitter.setReceiver(new TimeFilter(new MonitorReceiver(entry.getKey())));
                }
            }
        }
        try {
            synchronized (this) {
                wait();
            }
        } catch (final InterruptedException ignore) {
        }
        return 0;
    }
}

/** Filter that filters away MIDI time events.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @since 0.2
 */
class TimeFilter implements Receiver {

    /** Whether this TimeFilter is open. */
    private boolean open = true;

    /** The Receiver to which the message shall be forwarded. */
    private Receiver receiver;

    /** Creates a TimeFilter.
     * @param receiver Receiver to which messages shall be forwarded.
     */
    TimeFilter(@NotNull final Receiver receiver) {
        this.receiver = receiver;
    }

    /** {@inheritDoc} */
    public void close() {
        open = false;
    }

    /** {@inheritDoc} */
    public void send(final MidiMessage message, final long timeStamp) throws IllegalStateException {
        if (!open) {
            throw new IllegalStateException("Receiver closed.");
        }
        if (message.getStatus() != 0xF8) {
            receiver.send(message, timeStamp);
        }
    }
}
