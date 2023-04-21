package jpcsp.Allegrex.compiler.nativeCode;

/**
 * @author gid15
 *
 */
public class Memmove extends AbstractNativeCodeSequence {

    public static void call() {
        int dstAddr = getGprA0();
        int srcAddr = getGprA1();
        int n = getGprA2();
        getMemory().memmove(dstAddr, srcAddr, n);
        setGprV0(dstAddr);
    }

    public static void alignMemory(int alignment, int addrOffset) {
        int addr = getGprA0();
        int n = getGprA1();
        int dest = (addr + alignment) & ~alignment;
        getMemory().memmove(dest, addr + addrOffset, n - addrOffset);
    }
}
