package com.onionnetworks.fec;

import com.onionnetworks.util.Util;
import com.onionnetworks.util.Buffer;

/**
 * This class, along with FECMath, provides the implementation of the pure
 * Java 8 bit FEC codes.  This is heavily dervied from Luigi Rizzos original
 * C implementation.  See the file "LICENSE" along with this distribution for
 * additional copyright information.
 *
 * (c) Copyright 2001 Onion Networks
 * (c) Copyright 2000 OpenCola
 *
 * @author Justin F. Chapweske (justin@chapweske.com)
 */
public class PureCode extends FECCode {

    public static final int FEC_MAGIC = 0xFECC0DEC;

    protected static final FECMath fecMath = new FECMath(8);

    protected char[] encMatrix;

    public PureCode(int k, int n) {
        this(k, n, fecMath.createEncodeMatrix(k, n));
    }

    public PureCode(int k, int n, char[] encMatrix) {
        super(k, n);
        this.encMatrix = encMatrix;
    }

    /**
     * encode accepts as input pointers to n data packets of size sz,
     * and produces as output a packet pointed to by fec, computed
     * with index "index".
     */
    protected void encode(byte[][] src, int[] srcOff, byte[][] repair, int[] repairOff, int[] index, int packetLength) {
        for (int i = 0; i < repair.length; i++) {
            encode(src, srcOff, repair[i], repairOff[i], index[i], packetLength);
        }
    }

    protected void encode(byte[][] src, int[] srcOff, byte[] repair, int repairOff, int index, int packetLength) {
        if (index < k) {
            System.arraycopy(src[index], srcOff[index], repair, repairOff, packetLength);
        } else {
            int pos = index * k;
            Util.bzero(repair, repairOff, packetLength);
            for (int i = 0; i < k; i++) {
                fecMath.addMul(repair, repairOff, src[i], srcOff[i], (byte) encMatrix[pos + i], packetLength);
            }
        }
    }

    protected void decode(byte[][] pkts, int[] pktsOff, int[] index, int packetLength, boolean shuffled) {
        if (!shuffled) {
            shuffle(pkts, pktsOff, index, k);
        }
        char[] decMatrix = fecMath.createDecodeMatrix(encMatrix, index, k, n);
        byte[][] tmpPkts = new byte[k][];
        for (int row = 0; row < k; row++) {
            if (index[row] >= k) {
                tmpPkts[row] = new byte[packetLength];
                for (int col = 0; col < k; col++) {
                    fecMath.addMul(tmpPkts[row], 0, pkts[col], pktsOff[col], (byte) decMatrix[row * k + col], packetLength);
                }
            }
        }
        for (int row = 0; row < k; row++) {
            if (index[row] >= k) {
                System.arraycopy(tmpPkts[row], 0, pkts[row], pktsOff[row], packetLength);
                index[row] = row;
            }
        }
    }

    public String toString() {
        return new String("PureCode[k=" + k + ",n=" + n + "]");
    }
}
