package rasmus;

import com.frinika.voiceserver.AudioContext;
import com.frinika.voiceserver.Voice;
import com.frinika.voiceserver.VoiceServer;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

public class SoundfontSynthTest {

    /**
         * @param args
         */
    public static void main(String[] args) throws Exception {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        MidiDevice.Info info = null;
        for (int i = 0; i < infos.length; i++) {
            if (infos[i].getName().equals("RasmusDSP Synthesizer")) info = infos[i];
        }
        if (info == null) return;
        MidiDevice mididevice = MidiSystem.getMidiDevice(info);
        mididevice.open();
        TargetDataLine dataline = null;
        boolean pcm_float = false;
        if (mididevice instanceof Mixer) {
            dataline = (TargetDataLine) ((Mixer) mididevice).getLine(new Line.Info(TargetDataLine.class));
            try {
                AudioFormat.Encoding PCM_FLOAT = new AudioFormat.Encoding("PCM_FLOAT");
                AudioFormat format = new AudioFormat(PCM_FLOAT, 44100, 32, 2, 4 * 2, 4 * 2 * 44100, true);
                dataline.open(format);
                pcm_float = true;
            } catch (Exception e) {
                System.out.println("PCM_FLOAT not supported - " + e.toString());
                AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
                dataline.open(format);
            }
        }
        if (dataline != null) {
            VoiceServer voiceServer = new AudioContext().getVoiceServer();
            final TargetDataLine line = dataline;
            if (pcm_float) {
                System.out.println("PCM_FLOAT Encoding used!");
                voiceServer.addTransmitter(new Voice() {

                    byte[] streamBuffer = null;

                    FloatBuffer floatBuffer = null;

                    @Override
                    public void fillBuffer(int startBufferPos, int endBufferPos, float[] buffer) {
                        if (streamBuffer == null || streamBuffer.length != buffer.length * 4) {
                            ByteBuffer bytebuffer = ByteBuffer.allocate(buffer.length * 4);
                            streamBuffer = bytebuffer.array();
                            floatBuffer = bytebuffer.asFloatBuffer();
                        }
                        line.read(streamBuffer, 0, (endBufferPos - startBufferPos) * 4);
                        floatBuffer.position(0);
                        floatBuffer.get(buffer, startBufferPos, endBufferPos - startBufferPos);
                    }
                });
            } else voiceServer.addTransmitter(new Voice() {

                byte[] streamBuffer = null;

                @Override
                public void fillBuffer(int startBufferPos, int endBufferPos, float[] buffer) {
                    if (streamBuffer == null || streamBuffer.length != buffer.length * 2) streamBuffer = new byte[buffer.length * 2];
                    line.read(streamBuffer, startBufferPos * 2, (endBufferPos - startBufferPos) * 2);
                    for (int n = startBufferPos; n < endBufferPos; n++) {
                        buffer[n] = (((short) ((0xff & streamBuffer[n * 2 + 0]) + ((0xff & streamBuffer[n * 2 + 1]) * 256))) / 65536f);
                    }
                }
            });
        }
        Thread.sleep(1000);
        if (mididevice instanceof Synthesizer) {
            Soundbank soundbank = MidiSystem.getSoundbank(new File("soundfonts/Club.SF2"));
            ((Synthesizer) mididevice).loadAllInstruments(soundbank);
        }
        Receiver receiver = mididevice.getReceiver();
        ShortMessage shm = new ShortMessage();
        Instrument instr = ((Synthesizer) mididevice).getAvailableInstruments()[0];
        System.out.println(instr.getName());
        shm.setMessage(ShortMessage.CONTROL_CHANGE, 9, 0, 1);
        receiver.send(shm, -1);
        shm.setMessage(ShortMessage.CONTROL_CHANGE, 9, 0x20, 0);
        receiver.send(shm, -1);
        shm.setMessage(ShortMessage.PROGRAM_CHANGE, 9, instr.getPatch().getProgram(), 0);
        receiver.send(shm, -1);
        boolean evenBeat = true;
        while (true) {
            System.out.println("Kick");
            shm = new ShortMessage();
            shm.setMessage(ShortMessage.NOTE_ON, 9, 36, 100);
            receiver.send(shm, -1);
            shm = new ShortMessage();
            shm.setMessage(ShortMessage.NOTE_ON, 9, 42, 100);
            receiver.send(shm, -1);
            Thread.sleep(300);
            shm = new ShortMessage();
            shm.setMessage(ShortMessage.NOTE_ON, 9, 46, 100);
            receiver.send(shm, -1);
            Thread.sleep(300);
            shm = new ShortMessage();
            shm.setMessage(ShortMessage.NOTE_ON, 9, 40, 100);
            receiver.send(shm, -1);
            shm = new ShortMessage();
            shm.setMessage(ShortMessage.NOTE_ON, 9, 42, 100);
            receiver.send(shm, -1);
            Thread.sleep(300);
            shm = new ShortMessage();
            shm.setMessage(ShortMessage.NOTE_ON, 9, 46, 100);
            receiver.send(shm, -1);
            Thread.sleep(150);
            if (evenBeat) {
                shm = new ShortMessage();
                shm.setMessage(ShortMessage.NOTE_ON, 9, 36, 70);
                receiver.send(shm, -1);
                Thread.sleep(150);
            } else {
                Thread.sleep(150);
            }
            evenBeat = !evenBeat;
        }
    }
}
