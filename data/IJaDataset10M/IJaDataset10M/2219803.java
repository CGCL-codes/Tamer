package JCPC.core.device.speech;

import javax.sound.sampled.*;
import java.util.*;
import java.net.*;

public class SPO256 {

    private SourceDataLine line = null;

    public static void main(String args[]) {
        SPO256 SPO = new SPO256();
        SPO.Output("|JH|EY|PA3|VV|AA|PA2|SS|SS|IY|PA2|PA3|PP" + "|IY|PA2|SS|SS|IY|PA5|PA5|PA5|EH|SS|SS|PA2|PA3|PP" + "|IY|PA2|OW|PA2|PA3|TT2|UW2|FF|AY|PA3|VV|SS" + "|IH|PA3|KK1|SS|PA2|SS|PP|IY|CH|PA2|SS|YY1" + "|NN1|TH|EH|SS|IH|SS|PA2|CH|IH|PP|PA2|RR2|EH" + "|DD1|IY|PA2|PA3|TT2|UW2|PA2|EH|MM|AX|LL|EY|" + "TT2|PA1|PA1|");
        System.exit(0);
    }

    public void Output(String word) {
        byte[] previousSound = null;
        StringTokenizer st = new StringTokenizer(word, "|", false);
        while (st.hasMoreTokens()) {
            String thisPhoneFile = st.nextToken();
            thisPhoneFile = "wav/" + thisPhoneFile.toLowerCase() + ".wav";
            byte[] thisSound = getSound(thisPhoneFile);
            if (previousSound != null) {
                int mergeCount = 0;
                if (previousSound.length >= 500 && thisSound.length >= 500) {
                    mergeCount = 500;
                }
                for (int i = 0; i < mergeCount; i++) {
                    previousSound[previousSound.length - mergeCount + i] = (byte) ((previousSound[previousSound.length - mergeCount + i] + thisSound[i]) / 2);
                }
                playSound(previousSound);
                byte[] newSound = new byte[thisSound.length - mergeCount];
                for (int ii = 0; ii < newSound.length; ii++) {
                    newSound[ii] = thisSound[ii + mergeCount];
                }
                previousSound = newSound;
            } else {
                previousSound = thisSound;
            }
        }
        playSound(previousSound);
        SSA1.LRQ = 0x00;
        SSA1.SBY = 0;
        DKTronics.LRQ = 0x7f;
        drain();
    }

    private void drain() {
        if (line != null) {
            line.drain();
        }
        try {
            Thread.sleep(100);
            SSA1.LRQ = 0x80;
        } catch (Exception e) {
        }
    }

    private void playSound(byte[] data) {
        if (data != null && data.length > 0) {
            line.write(data, 0, data.length);
        }
    }

    private byte[] getSound(String fileName) {
        try {
            URL url = SPO256.class.getResource(fileName);
            AudioInputStream stream = AudioSystem.getAudioInputStream(url);
            AudioFormat format = stream.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat tmpFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(tmpFormat, stream);
                format = tmpFormat;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, format, ((int) stream.getFrameLength() * format.getFrameSize()));
            if (line == null) {
                DataLine.Info outInfo = new DataLine.Info(SourceDataLine.class, format);
                if (!AudioSystem.isLineSupported(outInfo)) {
                    System.out.println("Line matching " + outInfo + " not supported.");
                    throw new Exception("Line matching " + outInfo + " not supported.");
                }
                line = (SourceDataLine) AudioSystem.getLine(outInfo);
                line.open(format, 50000);
                line.start();
            }
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead = 0;
            if ((numBytesRead = stream.read(data)) != -1) {
                int numBytesRemaining = numBytesRead;
            }
            byte maxByte = 0;
            byte[] newData = new byte[numBytesRead];
            for (int i = 0; i < numBytesRead; i++) {
                newData[i] = data[i];
                if (newData[i] > maxByte) {
                    maxByte = newData[i];
                }
            }
            return newData;
        } catch (Exception e) {
            System.err.println("File not found:" + fileName);
            return new byte[0];
        }
    }
}
