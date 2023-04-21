package repeatmap.controller;

import repeatmap.types.Statics;
import repeatmap.util.Compression;
import repeatmap.util.KmerReader;
import repeatmap.util.KmerWriter;
import java.util.Arrays;

public class test {

    public test(String s1, String s2) {
        if (s1.length() != s2.length()) {
            System.out.println("The strings need to be the same length");
            return;
        }
        byte[] b1 = string2byte(s1);
        byte[] b2 = string2byte(s2);
        System.out.println(s1 + "(" + s1.length() + ") -> " + Compression.getLongValue(b1));
        System.out.println(s2 + "(" + s2.length() + ") -> " + Compression.getLongValue(b2));
        KmerByte kb1 = new KmerByte(b1);
        KmerByte kb2 = new KmerByte(b2);
        String tf = kb1.compareTo(kb2) == 1 ? "TRUE" : "FALSE";
        System.out.println(s1 + " > " + s2 + "? (" + tf + ")");
    }

    protected byte[] string2byte(String s) {
        int byte_sz = (int) Math.round(Math.ceil((float) s.length() / (float) Compression.BYTE_SIZE));
        byte[] b = new byte[byte_sz];
        Arrays.fill(b, (byte) 0);
        Compression.encodeDna(s.toCharArray(), b, byte_sz);
        return b;
    }

    protected char[] nucs = { 'A', 'C', 'G', 'T' };

    protected String[] add_chars(String s, boolean addAtEnd) {
        String[] sarr = new String[nucs.length];
        for (int i = 0; i < nucs.length; i++) {
            if (addAtEnd) {
                sarr[i] = s + nucs[i];
            } else {
                sarr[i] = nucs[i] + s;
            }
        }
        return sarr;
    }

    protected String[] add_char_for_arr(String[] sarr, boolean addAtEnd) {
        String[] newarr = new String[sarr.length * nucs.length];
        for (int i = 0; i < sarr.length; i++) {
            String[] sa = add_chars(sarr[i], addAtEnd);
            for (int j = 0; j < nucs.length; j++) {
                newarr[i * nucs.length + j] = sa[j];
            }
        }
        return newarr;
    }

    /**
     * Test many possible strings.
     */
    public void check_all_strings(String s, int len, boolean addAtEnd) {
        long[] vals = new long[(int) Math.round(Math.pow(nucs.length, (float) len))];
        String[] sarr = new String[1];
        sarr[0] = s;
        System.out.println("ADDING " + len + " CHARACTERS TO '" + s + "'");
        for (int i = 0; i < len; i++) {
            sarr = add_char_for_arr(sarr, addAtEnd);
            System.out.println("OUTPUTING LENGTHS FOR ITER " + i);
            for (int j = 0; j < sarr.length; j++) {
            }
        }
        byte[] b = null;
        for (int i = 0; i < sarr.length; i++) {
            b = string2byte(sarr[i]);
            vals[i] = Compression.getLongValue(b);
        }
        Arrays.sort(vals);
        long diff = vals[1] - vals[0];
        for (int i = 1; i < vals.length; i++) {
            if (vals[i - 1] != vals[i] - diff) {
                System.out.println("VALUES DON'T MATCH UP FOR " + i);
                System.out.println(sarr[i - 1] + "(" + sarr[i - 1].length() + ") ->" + vals[i - 1]);
                System.out.println(sarr[i] + "(" + sarr[i].length() + ") ->" + vals[i]);
                System.exit(2);
            }
        }
    }

    public static void test_strings() {
        test t;
        String s1, s2;
        s1 = "AAAAAAAAAAAATTTATTTATGTATTGT";
        s2 = "AAAAAAAAAAAATTTATTTATGTATTGC";
        t = new test(s1, s2);
        s1 = "AAAAAAAAAAAATTTATTTATGTATTGC";
        s2 = "AAAAAAAAAAAATTTATTTATGTATTGC";
        t = new test(s1, s2);
        s1 = "AAAAAAAAAAAATTTATTTATGTATTGC";
        s2 = "AAAAAAAAAAAATTTATTTATGTATTGG";
        t = new test(s1, s2);
        s1 = "TAAAAAAAAAAATTTATTTATGTATTGT";
        s2 = "TAAAAAAAAAAATTTATTTATGTATTGT";
        t = new test(s1, s2);
        s1 = "AAAAAAAAAAAATTTA";
        s2 = "AAAAAAAAAAAATTTA";
        t = new test(s1, s2);
        s1 = "TAAAAAAAAAAATTTA";
        s2 = "TAAAAAAAAAAATTTG";
        t = new test(s1, s2);
        s1 = "AAAA";
        s2 = "AAAC";
        t = new test(s1, s2);
        s1 = "AAAG";
        s2 = "AAAT";
        t = new test(s1, s2);
        s1 = "AACA";
        s2 = "AACC";
        t = new test(s1, s2);
        s1 = "AACG";
        s2 = "AACT";
        t = new test(s1, s2);
        s1 = "AAGA";
        s2 = "AAGC";
        t = new test(s1, s2);
        s1 = "AAGG";
        s2 = "AAGT";
        t = new test(s1, s2);
        s1 = "TTGG";
        s2 = "TTGT";
        t = new test(s1, s2);
        s1 = "TTTG";
        s2 = "TTTT";
        t = new test(s1, s2);
        s1 = "TAAAAAAAAAAATTTATTTATGTATTGT";
        s2 = "TAAAAAAAAAAATTTATTTATGTATTGA";
        t = new test(s1, s2);
        s1 = "TAAAAAAAAAAATTTATTTATGTATTTT";
        s2 = "TAAAAAAAAAAATTTATTTATGTATTTG";
        t = new test(s1, s2);
    }

    public static void main(String[] args) throws java.io.IOException {
        test_strings();
        test t = new test("", "");
        t.check_all_strings("AAAAAAAAAAAATTTATTTA", 8, true);
        t.check_all_strings("", 8, true);
        t.check_all_strings("A", 7, true);
        t.check_all_strings("A", 7, false);
        t.check_all_strings("AAAATTTATTTATGTATTGT", 8, false);
    }

    /**
   * Class to encapsulate a Kmer represented in bytes.
   */
    public class KmerByte implements Comparable {

        private byte[] kmer;

        /**
       * @param bytes the byte representation of a kmer.
       */
        public KmerByte(byte[] bytes) {
            this.kmer = new byte[bytes.length];
            System.arraycopy(bytes, 0, this.kmer, 0, bytes.length);
        }

        /**
       * Returns the double representation of the kmer.
       *
       * @return the double representation of the kmer.
       */
        public long getLongValue() {
            return Compression.getLongValue(this.kmer);
        }

        /**
       * Returns the byte representation of the kmer.
       *
       * @return the byte representation of the kmer.
       */
        public byte[] getKmer() {
            return this.kmer;
        }

        public int compareTo(Object o) {
            KmerByte other = (KmerByte) o;
            long this_val = this.getLongValue();
            long other_val = other.getLongValue();
            if (this_val < other_val) return -1;
            if (this_val > other_val) return 1;
            return 0;
        }
    }
}
