package verjinxer;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.LongBuffer;
import verjinxer.sequenceanalysis.Alphabet;
import verjinxer.sequenceanalysis.BigSuffixDLL;
import verjinxer.sequenceanalysis.BigSuffixXorDLL;
import verjinxer.sequenceanalysis.IBigSuffixDLL;
import verjinxer.util.ArrayFile;
import verjinxer.util.FileTypes;
import verjinxer.util.HugeByteArray;
import com.spinn3r.log5j.Logger;

/**
 * Class responsible for checking the correctness of a huge suffix array of a long text/sequence.
 * 
 * @author Markus Kemmerling
 */
public class BigSuffixTrayChecker {

    private static HugeByteArray sequence = null;

    private static Alphabet alphabet = null;

    private static Logger log = null;

    public static void setLogger(Logger log) {
        BigSuffixTrayChecker.log = log;
    }

    /**
    * Checks the given suffix list with the given method.
    * 
    * @param suffixDLL
    *           Suffix list to check.
    * @param method
    *           How the suffix list shall be checked. Valid methods are 'L', 'R', 'minLR', 'bothLR'
    *           and 'bothLR2'.
    * @param specialCharacterOrder
    *           How to compare/order special characters. Valid methods are 'pos' and 'suffix'.
    * @return 0 iff everything was okay.<br>
    *         The lowest bit is set to 1 when there are failures in the sorting of the suffixes.<br>
    *         The second bit is set to 1 when no first character can be found or when not all
    *         suffixes of the with the list associated text/sequences are included in the list.
    * @throws IllegalArgumentException
    *            when the given method is not valid or when the given method does not suits to the
    *            type of suffixDLL.
    */
    public static int checkpos(IBigSuffixDLL suffixDLL, String method, String specialCharacterOrder) throws IllegalArgumentException {
        BigSuffixTrayChecker.sequence = suffixDLL.getSequence();
        BigSuffixTrayChecker.alphabet = suffixDLL.getAlphabet();
        int returnvalue = 0;
        if (method.equals("L")) {
            if (suffixDLL instanceof BigSuffixDLL) {
                returnvalue = checkpos_R((BigSuffixDLL) suffixDLL, specialCharacterOrder);
            } else {
                throw new IllegalArgumentException("Method '" + method + "' only suits to type 'SuffixDLL'!");
            }
        } else if (method.equals("R")) {
            if (suffixDLL instanceof BigSuffixDLL) {
                returnvalue = checkpos_R((BigSuffixDLL) suffixDLL, specialCharacterOrder);
            } else {
                throw new IllegalArgumentException("Method '" + method + "' only suits to type 'SuffixDLL'!");
            }
        } else if (method.equals("minLR")) {
            if (suffixDLL instanceof BigSuffixDLL) {
                returnvalue = checkpos_R((BigSuffixDLL) suffixDLL, specialCharacterOrder);
            } else {
                throw new IllegalArgumentException("Method '" + method + "' only suits to type 'SuffixDLL'!");
            }
        } else if (method.equals("bothLR")) {
            if (suffixDLL instanceof BigSuffixXorDLL) {
                returnvalue = checkpos_bothLR((BigSuffixXorDLL) suffixDLL, specialCharacterOrder);
            } else {
                throw new IllegalArgumentException("Method '" + method + "' only suits to type 'SuffixXorDLL'!");
            }
        } else if (method.equals("bothLR2")) {
            if (suffixDLL instanceof BigSuffixDLL) {
                returnvalue = checkpos_R((BigSuffixDLL) suffixDLL, specialCharacterOrder);
            } else {
                throw new IllegalArgumentException("Method '" + method + "' only suits to type 'SuffixDLL'!");
            }
        } else {
            BigSuffixTrayChecker.sequence = null;
            BigSuffixTrayChecker.alphabet = null;
            throw new IllegalArgumentException("Unsupported construction method '" + method + "'!");
        }
        BigSuffixTrayChecker.sequence = null;
        BigSuffixTrayChecker.alphabet = null;
        return returnvalue;
    }

    /**
    * Checks correctness of the given suffix list.
    * 
    * @param suffixDLL
    *           Suffix list to check.
    * @param specialCharacterOrder 
    *           How to compare/order special characters. Valid methods are 'pos' and 'suffix'.
    * @return 0 iff everything was okay.<br>
    *         The lowest bit is set to 1 when there are failures in the sorting of the suffixes.<br>
    *         The second bit is set to 1 when no first character can be found or when not all
    *         suffixes of the with the list associated text/sequences are included in the list.
    */
    private static int checkpos_R(BigSuffixDLL suffixDLL, String specialCharacterOrder) {
        int chi, nn;
        long comp;
        int returnvalue = 0;
        chi = suffixDLL.getLowestCharacter();
        if (chi >= 256) {
            if (sequence.length == 0) {
                return 0;
            }
            if (log != null) {
                log.warn("suffixcheck: no first character found, but |s|!=0.");
            }
            return 2;
        }
        suffixDLL.resetToBegin();
        assert (suffixDLL.getCurrentPosition() != -1);
        nn = 1;
        while (suffixDLL.hasNextUp()) {
            if (!((comp = suffixcmp(suffixDLL.getCurrentPosition(), suffixDLL.getSuccessor(), specialCharacterOrder)) < 0)) {
                if (log != null) {
                    log.warn("suffixcheck: sorting error at ranks %d, %d; pos %d, %d; text %d, %d; cmp %d", nn - 1, nn, suffixDLL.getCurrentPosition(), suffixDLL.getSuccessor(), sequence.get(suffixDLL.getCurrentPosition()), sequence.get(suffixDLL.getSuccessor()), comp);
                }
                returnvalue = 1;
            }
            suffixDLL.nextUp();
            nn++;
        }
        if (nn != sequence.length) {
            if (log != null) {
                log.warn("suffixcheck: missing some suffixes; have %d / %d.", nn, sequence.length);
            }
            returnvalue += 2;
        }
        return returnvalue;
    }

    /**
    * Checks correctness of the given suffix list.
    * 
    * @param suffixDLL
    *           Suffix list to check.
    * @param specialCharacterOrder 
    *           How to compare/order special characters. Valid methods are 'pos' and 'suffix'.
    * @return 0 iff everything was okay.<br>
    *         The lowest bit is set to 1 when there are failures in the sorting of the suffixes.<br>
    *         The second bit is set to 1 when no first character can be found or when not all
    *         suffixes of the with the list associated text/sequences are included in the list.
    */
    private static int checkpos_bothLR(BigSuffixXorDLL suffixDLL, String specialCharacterOrder) {
        int chi, nn;
        long comp;
        int returnvalue = 0;
        chi = suffixDLL.getLowestCharacter();
        if (chi >= 256) {
            if (sequence.length == 0) {
                return 0;
            }
            return 2;
        }
        nn = 1;
        suffixDLL.resetToBegin();
        assert (suffixDLL.getCurrentPosition() != -1);
        while (suffixDLL.hasNextUp()) {
            if (!((comp = suffixcmp(suffixDLL.getCurrentPosition(), suffixDLL.getSuccessor(), specialCharacterOrder)) < 0)) {
                if (log != null) {
                    log.warn("suffixcheck: sorting error at ranks %d, %d; pos %d, %d; text %d, %d; cmp %d", nn - 1, nn, suffixDLL.getCurrentPosition(), suffixDLL.getSuccessor(), sequence.get(suffixDLL.getCurrentPosition()), sequence.get(suffixDLL.getSuccessor()), comp);
                }
                returnvalue = 1;
            }
            suffixDLL.nextUp();
            nn++;
        }
        if (nn != sequence.length) {
            if (log != null) {
                log.warn("suffixcheck: missing some suffixes; have %d / %d.", nn, sequence.length);
            }
            returnvalue += 2;
        }
        return returnvalue;
    }

    /**
    * Checks correctness of a suffix array on disk outputs warning messages if errors are found
    * 
    * @param project
    *           Project for that the suffix array shall be checked.
    * @return 0 on success, 1 on sorting error, 2 on count error
    * @throws IOException
    *            When the pos file can not be read.
    */
    public static int checkpos(Project project) throws IOException {
        int returnvalue = 0;
        ArrayFile fpos = null;
        LongBuffer pos = null;
        fpos = new ArrayFile(project.makeFile(FileTypes.POS), 0);
        pos = fpos.mapR().asLongBuffer();
        sequence = Globals.slurpHugeByteArray(project.makeFile(FileTypes.SEQ));
        alphabet = project.readAlphabet();
        final String specialCharacterOrder = project.getStringProperty("specialCharacterOrder4Suffix");
        long p = pos.get();
        long nextp, comp;
        long nn = 1;
        while (true) {
            try {
                nextp = pos.get();
            } catch (BufferUnderflowException ex) {
                break;
            }
            if (!((comp = suffixcmp(p, nextp, specialCharacterOrder)) < 0)) {
                if (log != null) {
                    log.error("suffixcheck: sorting error at ranks %d, %d; pos %d, %d; text %d, %d; cmp %d%n", nn - 1, nn, p, nextp, sequence.get(p), sequence.get(nextp), comp);
                }
                returnvalue = 1;
            }
            nn++;
            p = nextp;
        }
        if (nn != sequence.length) {
            if (log != null) {
                log.error("suffixcheck: missing some suffixes; have %d / %d.%n", nn, sequence.length);
            }
            returnvalue += 2;
        }
        sequence = null;
        alphabet = null;
        return returnvalue;
    }

    private static final long scmp(final long i, final long j, final String specialCharacterOrder) {
        final byte si = sequence.get(i);
        final int d = si - sequence.get(j);
        if (d != 0 || alphabet.isSymbol(si)) {
            return d;
        }
        if (specialCharacterOrder.equals("pos")) {
            return i - j;
        } else {
            assert specialCharacterOrder.equals("suffix");
            return d;
        }
    }

    /**
    * Compares two suffixes of the associated text/sequence. "Symbols" are compared according to
    * their order in the alphabet map. "Special" characters (wildcards, separators, endofline) are
    * compared by position or by their order in the alphabet map (depends on the given
    * 'specialCharacterOrder').
    * 
    * @param i
    *           Position of first suffix.
    * @param j
    *           Position of second suffix.
    * @param specialCharacterOrder 
    *           How to compare/order special characters. Valid methods are 'pos' and 'suffix'.
    * @return Any value &lt; 0 iff suffix(i)&lt;suffix(j) lexicographically, zero (0) iff i==j any
    *         value &gt; 0 iff suffix(i)&gt;suffix(j) lexicographically.
    */
    private static final long suffixcmp(final long i, final long j, String specialCharacterOrder) {
        if (i == j) return 0;
        long off, c;
        for (off = 0; (c = scmp(i + off, j + off, specialCharacterOrder)) == 0; off++) {
        }
        return c;
    }
}
