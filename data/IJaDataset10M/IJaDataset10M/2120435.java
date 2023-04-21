package com.triplyx.volume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TripleXOROutputStream extends OutputStream {

    private final InputStream randomA, randomB, randomC;

    private final OutputStream outputDA, outputDB, outputDC;

    public TripleXOROutputStream(final InputStream randomA, final InputStream randomB, final InputStream randomC, final OutputStream outputDA, final OutputStream outputDB, final OutputStream outputDC) {
        if (randomA == null) throw new NullPointerException();
        if (randomB == null) throw new NullPointerException();
        if (randomC == null) throw new NullPointerException();
        if (outputDA == null) throw new NullPointerException();
        if (outputDB == null) throw new NullPointerException();
        if (outputDC == null) throw new NullPointerException();
        this.randomA = randomA;
        this.randomB = randomB;
        this.randomC = randomC;
        this.outputDA = outputDA;
        this.outputDB = outputDB;
        this.outputDC = outputDC;
    }

    @Override
    public void write(int d) throws IOException {
        int a = randomA.read();
        int b = randomB.read();
        int c = randomC.read();
        if ((a == -1) || (b == -1) || (c == -1)) {
            throw new IOException("A random stream ran out of data: a=" + a + " b=" + b + " c=" + c);
        }
        outputDA.write(d ^ a);
        outputDB.write(d ^ b);
        outputDC.write(d ^ c);
    }

    @Override
    public void flush() throws IOException {
        outputDA.flush();
        outputDB.flush();
        outputDC.flush();
    }

    @Override
    public void close() throws IOException {
        flush();
        outputDA.close();
        outputDB.close();
        outputDC.close();
    }
}
