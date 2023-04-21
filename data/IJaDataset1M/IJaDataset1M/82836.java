package com.aricom.core;

import java.io.*;

public class Compressor {

    int count = 0, count_bytes;

    char buffer = 0;

    public Compressor() {
    }

    public void encode(File inputFile, File outputFile, Header header, int Pi) throws IOException {
        long high, low, range;
        int bits;
        long q1, q2, q3, q4;
        q1 = 1 << (Pi - 2);
        q2 = q1 << 1;
        q3 = q1 + q2;
        q4 = (q2 << 1) - 1;
        high = q4;
        low = 0;
        range = high - low + 1;
        bits = 0;
        FileInputStream in = new FileInputStream(inputFile);
        header.writeHeader(outputFile);
        FileOutputStream out = new FileOutputStream(outputFile, true);
        int car;
        int n_bytes = 0;
        count_bytes = 0;
        int totalFreq = (int) header.maxCumFreq();
        System.out.println("File to compress: " + header.realFileSize() + " bytes (" + header.realFileSize() / 1024 + " kb) - Compressing...");
        do {
            n_bytes++;
            if (n_bytes % (1024 * 1024 / 4) == 0) System.out.println((1.0 * n_bytes) / (1024 * 1024) + "compressed MBytes");
            car = in.read();
            if (car == -1) car = 256;
            range = high - low + 1;
            high = low + (range * header.cumFreq(car)) / totalFreq - 1;
            low = low + (range * header.cumFreq(car + 1)) / totalFreq;
            while ((low >= q2) || (high < q2) || (low >= q1 && high < q3)) {
                if (low >= q2) {
                    push_bit(0x01, out);
                    while (bits > 0) {
                        push_bit(0x00, out);
                        bits--;
                    }
                    high = ((high << 1) & q4) | 0x01;
                    low = (low << 1) & q4;
                } else if (high < q2) {
                    push_bit(0x00, out);
                    while (bits > 0) {
                        push_bit(0x01, out);
                        bits--;
                    }
                    high = (high << 1) | 0x01;
                    low = low << 1;
                } else {
                    bits++;
                    high = ((high - q1) << 1) | 0x01;
                    low = (low - q1) << 1;
                }
            }
        } while (car < 256);
        push_bit(low < q1 ? 0 : 1, out);
        push_bit(low < q1 ? 0 : 1, out);
        if (low < q1) {
            while (bits >= 0) {
                push_bit(low < q1 ? 1 : 0, out);
                bits--;
            }
        }
        if (buffer != 0) while (count > 0) push_bit(0, out);
        in.close();
        out.close();
        System.out.println("Compression finished.");
        System.out.println("Original file:\t " + n_bytes + " bytes (" + n_bytes / 1024 + " kb)");
        System.out.println("Compressed file:\t " + count_bytes + " bytes (" + count_bytes / 1024 + " kb)");
        System.out.println("Compression ratio:\t " + (100 - ((count_bytes * 100) / n_bytes)) + "%");
    }

    private void push_bit(int bit, FileOutputStream out) throws IOException {
        count++;
        buffer = (char) ((buffer << 1) + bit);
        if (count == 8) {
            count = 0;
            out.write(buffer);
            count_bytes++;
            buffer = 0;
        }
    }
}
