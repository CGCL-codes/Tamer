package de.carne.nio.compression;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

/**
 * LZMA LiteralDecoder
 */
final class LZMALiteralDecoder {

    static class Decoder2 {

        private final short[] decoders = new short[0x300];

        public void reset() {
            LZMARangeDecoder.initBitModels(this.decoders);
        }

        public byte decodeNormal(ReadableByteChannel src, LZMARangeDecoder rangeDecoder) throws IOException {
            int symbol = 1;
            do {
                symbol = (symbol << 1) | rangeDecoder.decodeBit(src, this.decoders, symbol);
            } while (symbol < 0x100);
            return (byte) symbol;
        }

        public byte decodeWithMatchByte(ReadableByteChannel src, LZMARangeDecoder rangeDecoder, byte matchByte) throws IOException {
            int symbol = 1;
            byte currentMatchByte = matchByte;
            do {
                final int matchBit = (currentMatchByte >> 7) & 1;
                currentMatchByte <<= 1;
                final int bit = rangeDecoder.decodeBit(src, this.decoders, ((1 + matchBit) << 8) + symbol);
                symbol = (symbol << 1) | bit;
                if (matchBit != bit) {
                    while (symbol < 0x100) {
                        symbol = (symbol << 1) | rangeDecoder.decodeBit(src, this.decoders, symbol);
                    }
                    break;
                }
            } while (symbol < 0x100);
            return (byte) symbol;
        }
    }

    private final Decoder2[] coders;

    private final int numPrevBits;

    private final int numPosBits;

    private final int posMask;

    public LZMALiteralDecoder(int numPosBits, int numPrevBits) {
        this.numPosBits = numPosBits;
        this.posMask = (1 << this.numPosBits) - 1;
        this.numPrevBits = numPrevBits;
        final int numStates = 1 << (this.numPrevBits + this.numPosBits);
        this.coders = new Decoder2[numStates];
        for (int i = 0; i < numStates; i++) {
            this.coders[i] = new Decoder2();
        }
        reset();
    }

    public void reset() {
        final int numStates = 1 << (this.numPrevBits + this.numPosBits);
        for (int i = 0; i < numStates; i++) {
            this.coders[i].reset();
        }
    }

    public Decoder2 getDecoder(int pos, byte prevByte) {
        return this.coders[((pos & this.posMask) << this.numPrevBits) + ((prevByte & 0xFF) >>> (8 - this.numPrevBits))];
    }
}
