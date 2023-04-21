package lzma;

import java.io.IOException;
import SevenZip.ICodeProgress;
import Benchmark.Benchmark;

public class PostCompression implements ICodeProgress {

    public static long sizeToLZMA;

    public static boolean DecompressFile(String in_filename, String out_filename) {
        java.io.File inFile = new java.io.File(in_filename);
        java.io.File outFile = new java.io.File(out_filename);
        try {
            java.io.BufferedInputStream inStream = new java.io.BufferedInputStream(new java.io.FileInputStream(inFile));
            java.io.BufferedOutputStream outStream = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outFile));
            int propertiesSize = 5;
            byte[] properties = new byte[propertiesSize];
            if (inStream.read(properties, 0, propertiesSize) != propertiesSize) throw new Exception("input .lzma file is too short");
            SevenZip.Compression.LZMA.Decoder decoder = new SevenZip.Compression.LZMA.Decoder();
            if (!decoder.SetDecoderProperties(properties)) throw new Exception("Incorrect stream properties");
            long outSize = 0;
            for (int i = 0; i < 8; i++) {
                int v = inStream.read();
                if (v < 0) throw new Exception("Can't read stream size");
                outSize |= ((long) v) << (8 * i);
            }
            if (!decoder.Code(inStream, outStream, outSize)) throw new Exception("Error in data stream");
            outStream.flush();
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean CompressFile(String in_filename, String out_filename) {
        System.out.println("Encoding file " + in_filename + " with LZMA");
        try {
            java.io.File inFile = new java.io.File(in_filename);
            java.io.File outFile = new java.io.File(out_filename);
            java.io.BufferedInputStream inStream = new java.io.BufferedInputStream(new java.io.FileInputStream(inFile));
            java.io.BufferedOutputStream outStream = new java.io.BufferedOutputStream(new java.io.FileOutputStream(outFile));
            boolean eos = true;
            SevenZip.Compression.LZMA.Encoder encoder = new SevenZip.Compression.LZMA.Encoder();
            if (!encoder.SetAlgorithm(2)) throw new Exception("Incorrect compression mode");
            if (!encoder.SetDictionarySize(1 << 23)) throw new Exception("Incorrect dictionary size");
            if (!encoder.SeNumFastBytes(128)) throw new Exception("Incorrect -fb value");
            if (!encoder.SetMatchFinder(1)) throw new Exception("Incorrect -mf value");
            int Lc = 3;
            int Lp = 0;
            int Pb = 2;
            if (!encoder.SetLcLpPb(Lc, Lp, Pb)) throw new Exception("Incorrect -lc or -lp or -pb value");
            encoder.SetEndMarkerMode(eos);
            encoder.WriteCoderProperties(outStream);
            long fileSize;
            if (eos) fileSize = -1; else fileSize = inFile.length();
            for (int i = 0; i < 8; i++) outStream.write((int) (fileSize >>> (8 * i)) & 0xFF);
            encoder.Code(inStream, outStream, -1, -1, new PostCompression());
            outStream.flush();
            outStream.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void SetProgress(long inSize, long outSize) {
        System.out.print(".");
    }
}
