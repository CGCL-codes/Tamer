package tk.pak0.audiomidifier.model.midi;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MidiTools {

    byte midiChannel = 0;

    boolean finePitch = true;

    byte pitchBendRage = 2;

    boolean velocitySensible = true;

    private MidiDevice.Info[] midiDevsInfo;

    private MidiDevice[] midiDevs;

    private List<List<Receiver>> midiReceivers;

    void setVelocitySensible(boolean b) {
        velocitySensible = b;
    }

    boolean getVelocitySensible() {
        return velocitySensible;
    }

    void setMidiChannel(byte midiChan) {
        midiChannel = midiChan;
    }

    byte getMidiChannel() {
        return midiChannel;
    }

    void setPitchBendRange(byte newRange) {
        pitchBendRage = newRange;
    }

    byte getPitchBendRange() {
        return pitchBendRage;
    }

    void setFinePitch(boolean b) {
        finePitch = b;
    }

    boolean getFinePitch() {
        return finePitch;
    }

    public MidiTools() {
        midiDevsInfo = initMidiDevsInfo();
        midiDevs = initMidiDevs();
        midiReceivers = initMidiReceivers();
    }

    public MidiDevice.Info[] getMidiDevsInfo() {
        return midiDevsInfo;
    }

    public void setMidiDevsInfo(MidiDevice.Info[] midiDevInfo) {
        this.midiDevsInfo = midiDevInfo;
    }

    public void setMidiDevs(MidiDevice midiDevs[]) {
        this.midiDevs = midiDevs;
    }

    public MidiDevice[] getMidiDevs() {
        return midiDevs;
    }

    public void setMidiReceivers(List<List<Receiver>> midiReceivers) {
        this.midiReceivers = midiReceivers;
    }

    public List<List<Receiver>> getMidiReceivers() {
        return midiReceivers;
    }

    MidiDevice.Info[] initMidiDevsInfo() {
        MidiDevice.Info[] midiDevInfo = MidiSystem.getMidiDeviceInfo();
        return midiDevInfo;
    }

    MidiDevice[] initMidiDevs() {
        MidiDevice[] midiDevs = new MidiDevice[getMidiDevsInfo().length];
        int realLength = midiDevs.length;
        for (int i = 0; i < midiDevs.length; i++) {
            try {
                MidiDevice dev = MidiSystem.getMidiDevice(getMidiDevsInfo()[i]);
                midiDevs[i] = dev;
                if (dev.getClass().toString().equals("class com.sun.media.sound.MidiOutDevice")) {
                    midiDevs[i] = dev;
                } else {
                    midiDevs[i] = null;
                    realLength--;
                }
            } catch (Exception e) {
                e.printStackTrace();
                midiDevs[i] = null;
                realLength--;
            }
        }
        MidiDevice[] midiDevsTrim = new MidiDevice[realLength];
        MidiDevice.Info[] midiDevsInfoTrim = new MidiDevice.Info[realLength];
        int j = 0;
        for (int i = 0; i < midiDevs.length; i++) {
            if (j < realLength && midiDevs[i] != null) {
                midiDevsTrim[j] = midiDevs[i];
                midiDevsInfoTrim[j] = midiDevsInfo[i];
                j++;
            }
        }
        this.midiDevsInfo = midiDevsInfoTrim;
        return midiDevsTrim;
    }

    public List<List<Receiver>> initMidiReceivers() {
        List<List<Receiver>> midiReceivers = new ArrayList<List<Receiver>>();
        for (int i = 0; i < midiDevs.length; i++) {
            if (!midiDevs[i].getReceivers().isEmpty()) {
                midiReceivers.add(midiDevs[i].getReceivers());
            }
        }
        return midiReceivers;
    }

    /**
     * Convierte de frecuencia a Nota, para luego pasarlo a midi
     * @param freq volumen
     * @return Nota
     */
    Note freqToMidi(float freq, byte volumen) {
        double rawNote;
        rawNote = (69 + 12 * Math.log10(freq / 440) / Math.log10(2));
        double absNote = Math.abs(rawNote);
        double tail = rawNote - absNote;
        if (tail > 0.5) {
            double absNote2 = absNote + 1;
            tail = absNote2 - rawNote;
            absNote = absNote2;
        }
        Note ret = new Note((byte) absNote, tail, volumen);
        return ret;
    }

    /**
     * Este método crea un mensaje Midi que puede ser noteOn o noteOff
     * @param noteOn n
     * @author ffuentes
     * @return midiMes
     */
    ShortMessage creaMensajeMidi(boolean noteOn, Note n) {
        ShortMessage midiMes = new ShortMessage();
        if (noteOn) {
            byte velocity = 64;
            if (velocitySensible) velocity = (byte) (((127 * n.volumen) / 256));
            try {
                midiMes.setMessage(ShortMessage.NOTE_ON, midiChannel, (byte) n.midiNote, (byte) velocity);
            } catch (Exception e) {
                System.err.print(e.toString());
            }
        } else {
            try {
                midiMes.setMessage(ShortMessage.NOTE_OFF, midiChannel, (byte) n.midiNote, 0);
            } catch (Exception e) {
                System.err.print(e.toString());
            }
        }
        return midiMes;
    }

    /**
     * Dada una nota, devuelve un mensaje midi corto, con el valor del pitch bend
     * @author ffuentes
     * @params n
     * @return msg
     */
    ShortMessage creaPitchBend(Note n) {
        if (!finePitch) return null; else {
            ShortMessage msg = new ShortMessage();
            float mult = (float) (n.tail + 1);
            int bend = (int) (8192 * mult);
            byte[] wBend = convierteIntAWord(bend);
            wBend = convierteFormatoBend(wBend);
            try {
                msg.setMessage(ShortMessage.PITCH_BEND, wBend[0], wBend[1]);
            } catch (Exception e) {
                System.err.print(e.toString());
                return null;
            }
            return msg;
        }
    }

    /**
     * Convierte de int a word (un array de dos bytes
     * @author ffuentes
     * @params n
     * @return out
     */
    byte[] convierteIntAWord(int n) throws NumberFormatException {
        if (n > 65535) throw new NumberFormatException("Número int mayor que 65535 / 0xFFFF"); else {
            String hexStr = Integer.toHexString(n);
            String msbStr = hexStr.substring(12, 14);
            String lsbStr = hexStr.substring(14);
            msbStr = new String("0x" + msbStr);
            lsbStr = new String("0x" + lsbStr);
            byte[] out = new byte[2];
            out[1] = Byte.decode(msbStr);
            out[0] = Byte.decode(lsbStr);
            return out;
        }
    }

    /**
     * Convierte un int, al formato word manejado por los mensajes Pitchwheel
     * @author ffuentes
     * @params word
     * @return word
     */
    byte[] convierteFormatoBend(byte[] word) {
        if (word[1] > Byte.decode("0x7F")) {
            byte[] max = new byte[2];
            max[0] = Byte.decode("0xFF");
            max[1] = Byte.decode("0x7F");
            return max;
        } else {
            int msb = (byte) (word[1]);
            msb = msb << 1;
            word[1] = (byte) msb;
            if (word[0] >= 128) word[1] = (byte) (word[1] + 1);
            word[0] = (byte) (word[0] & Byte.decode("0x7F"));
            return word;
        }
    }
}
