package com.fasteasytrade.JRandTest.Tests;

public class Count4Bits extends Base {

    /**
	 * @see com.fasteasytrade.JRandTest.Tests.Base#help()
	 */
    public void help() {
        puts("\n\t|-------------------------------------------------------------|");
        puts("\t|    This is part of the Count test.  It counts consecutive 4 |");
        puts("\t|bits. The sums and the differences are reported. The         |");
        puts("\t|expection is 1/16, each sum from total 4 bits.               |");
        puts("\t|-------------------------------------------------------------|\n");
    }

    /**
	 * @param filename input file with random data
	 */
    public void test(String filename) throws Exception {
        final int no_seqs = 16;
        double[] v6 = new double[no_seqs];
        int j;
        long length = 0;
        printf("\t\t\tThe Count4Bits test for file " + filename + "\n");
        openInputStream();
        byte b;
        int temp;
        while (true) {
            b = readByte();
            if (!isOpen()) break;
            length += 2;
            temp = 0xff & b;
            v6[temp & 0x0f]++;
            temp = temp >>> 4;
            v6[temp & 0x0f]++;
        }
        closeInputStream();
        double pv = KStest(v6, no_seqs);
        printf("\t ks test for " + no_seqs + " p's: " + d4(pv) + "\n");
        long k = length / no_seqs;
        printf("\n\t found " + length + " 4 bits.");
        printf("\n\t expected avg for 4 bits: " + k);
        printf("\n\t found avg for 4 bits: " + (long) avg(v6));
        for (j = 0; j < v6.length; j++) printf("\n\t 4 bits " + j + ": " + d4(v6[j]) + "\tdelta: " + d4(v6[j] - k) + "\t%: " + d4(100.00 * v6[j] / k - 100.00));
        double t = stdev(v6, k);
        printf("\n\t stdev for 4 bits\t: " + d4(t));
        printf("\n\t % stdev for 4 bits\t: %" + d4(100.00 * t / k));
        printf("\n\t chitest for 4 bits\t: " + d4(chitest(v6, k)));
        printf("\n\t r2 for 4 bits\t\t: " + d4(r2_double(v6)));
        return;
    }
}
