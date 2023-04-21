package com.notuvy.file;

import java.io.File;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 * Decompose the total bytes to larger denominations.
 *
 * @author  murali
 */
public class FileSize {

    protected static final Logger LOG = Logger.getLogger(FileSize.class);

    public static final int KILO = 1024;

    public static final int HALF_KILO = KILO / 2;

    public static final int MEGA = KILO * KILO;

    private static final String[] NAMES = { "bytes", "Kb", "Mb", "Gb" };

    public static long mbToBytes(long pBytes) {
        long result = pBytes * MEGA;
        return result;
    }

    public static long bytesToMb(long pBytes) {
        long remainder = pBytes % MEGA;
        long result = pBytes / MEGA;
        if (remainder > 0) {
            result++;
        }
        return result;
    }

    private final long vTotal;

    private final LinkedList vParts = new LinkedList();

    public FileSize(long pTotal) {
        vTotal = pTotal;
        int i = 0;
        do {
            long remainder = pTotal % KILO;
            getParts().addFirst(new Part(remainder, NAMES[i++]));
            pTotal = pTotal / KILO;
            if (remainder >= HALF_KILO) {
                pTotal += 1;
            }
        } while ((i < NAMES.length) && (pTotal != 0));
        if (pTotal != 0) {
            Part part = (Part) getParts().removeFirst();
            long number = (KILO * pTotal) + part.getNumber();
            getParts().addFirst(new Part(number, part.getName()));
        }
    }

    public FileSize(File pFile) {
        this(pFile.length());
    }

    public long getTotal() {
        return vTotal;
    }

    protected LinkedList getParts() {
        return vParts;
    }

    public String toString() {
        Part part = (Part) getParts().getFirst();
        String result = part.toString();
        return result;
    }

    class Part {

        private final long vNumber;

        private final String vName;

        protected Part(long pNumber, String pName) {
            vNumber = pNumber;
            vName = pName;
        }

        public long getNumber() {
            return vNumber;
        }

        public String getName() {
            return vName;
        }

        public String toString() {
            String result = getNumber() + " " + getName();
            return result;
        }

        public boolean shouldRoundNext() {
            boolean result = (getNumber() >= HALF_KILO);
            return result;
        }

        public int giveFirstDecimal() {
            int result = (int) getNumber() / 100;
            return result;
        }
    }
}
