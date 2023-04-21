package ibxm;

import java.io.*;

class ModLoader {

    public static Module loadMOD(byte[] header, DataInput i) throws IOException {
        Module mod = new Module();
        mod.bpm = 125;
        mod.tempo = 6;
        mod.amiga = false;
        mod.xm = false;
        mod.linear = false;
        int numChannels = 4;
        int id = Loader.check(header);
        if (id <= 2) throw new IOException("Not a valid MOD file!");
        if (id == 3) mod.amiga = true;
        if (id >= 4) numChannels = id;
        mod.songName = Loader.ascii2String(header, 0, 20);
        int songLength = header[950] & 0x7F;
        mod.patternOrder = new int[songLength];
        mod.restart = header[951] & 0x7F;
        if (mod.restart >= songLength) mod.restart = 0;
        for (int n = 0; n < songLength; n++) mod.patternOrder[n] = header[952 + n] & 0x7F;
        int numPatterns = 0;
        for (int n = 0; n < 128; n++) {
            int pat = header[952 + n] & 0x7F;
            if (pat >= numPatterns) numPatterns = pat + 1;
        }
        mod.patterns = new Pattern[numPatterns];
        for (int n = 0; n < mod.patterns.length; n++) mod.patterns[n] = readModPatt(i, numChannels);
        mod.instruments = new Instrument[32];
        for (int n = 1; n < 32; n++) mod.instruments[n] = readModInst(header, n, i);
        return mod;
    }

    private static Instrument readModInst(byte[] header, int num, DataInput sdata) throws IOException {
        Instrument inst = new Instrument();
        int offset = (num - 1) * 30 + 20;
        inst.name = Loader.ascii2String(header, offset, 22);
        int sampleLength = ushortbe(header, offset + 22) << 1;
        int finetune = (header[offset + 24] & 0xF) << 4;
        if (finetune > 127) finetune -= 256;
        int volume = header[offset + 25] & 0x7F;
        int loopStart = ushortbe(header, offset + 26) << 1;
        int loopLength = ushortbe(header, offset + 28) << 1;
        boolean loop = true;
        if (loopLength < 4) loop = false;
        int loopEnd = loopStart + loopLength - 1;
        byte[] buf = new byte[sampleLength];
        try {
            sdata.readFully(buf);
        } catch (EOFException e) {
            System.out.println("IBXM: Instrument " + num + " incomplete!");
        }
        short[] samples = new short[sampleLength + 1];
        for (int n = 0; n < buf.length; n++) samples[n] = (short) (buf[n] << 8);
        inst.samples[0] = new Sample(inst.name, samples, loop, loopStart, loopEnd, false, volume, 128, finetune, 0);
        return inst;
    }

    private static Pattern readModPatt(DataInput i, int numChannels) throws IOException {
        Note n = new Note();
        Pattern pat = new Pattern(64, numChannels);
        byte[] buf = new byte[64 * numChannels * 4];
        i.readFully(buf);
        for (int row = 0; row < 64; row++) {
            for (int chn = 0; chn < numChannels; chn++) {
                int idx = (row * numChannels + chn) * 4;
                n.key = ((buf[idx] & 0x0F) << 8) | (buf[idx + 1] & 0xFF);
                n.inst = (buf[idx] & 0x10) | ((buf[idx + 2] & 0xF0) >> 4);
                n.vol = 0;
                n.fx = buf[idx + 2] & 0x0F;
                n.fp = buf[idx + 3] & 0xFF;
                pat.setNote(n, row, chn);
            }
        }
        return pat;
    }

    private static int ushortbe(byte[] buf, int offset) {
        return ((buf[offset] & 0xFF) << 8) | (buf[offset + 1] & 0xFF);
    }
}
