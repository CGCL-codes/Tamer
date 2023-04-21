package common;

import java.util.Vector;

public class FastRead extends Read {

    public Vector<WordM> wp = new Vector<WordM>();

    static long score_mask = 0;

    static int segments = 0;

    static int read_width = 0;

    static int right_most_bit = 0;

    public FastRead() {
        WordM wpi;
        for (int i = 0; i < segments; ++i) {
            wpi = new WordM();
            wp.add(wpi);
        }
        wpi = new WordM(Utils.all_ones << right_most_bit);
        wp.add(wpi);
        this.words = wp;
    }

    public FastRead(FastRead other) {
        wp = new Vector<WordM>(other.wp);
        this.words = wp;
    }

    public FastRead(String s_) {
        assert (s_.length() > 0);
        WordM wpi;
        for (int i = 0; i < segments; ++i) {
            String this_seg = s_.substring(i * Utils.word_size, i * Utils.word_size + Utils.word_size);
            wpi = new WordM(this_seg);
            wp.add(wpi);
        }
        wpi = new WordM(s_.substring(segments * Utils.word_size));
        wp.add(wpi);
        this.words = wp;
    }

    public FastRead(char[] s_) {
        assert (s_.length > 0);
        WordM wpi;
        char[] this_seg = new char[Utils.word_size];
        for (int i = 0; i < segments; ++i) {
            for (int j = 0; j < Utils.word_size; j++) this_seg[j] = s_[i * Utils.word_size + j];
            wpi = new WordM(this_seg);
            wp.add(wpi);
        }
        int i = 0;
        char[] last_seg = new char[s_.length - segments * Utils.word_size];
        for (int j = segments * Utils.word_size; j < s_.length; j++) last_seg[i++] = s_[j];
        wpi = new WordM(last_seg);
        wp.add(wpi);
        this.words = wp;
    }

    boolean isSsmaller(FastRead rhs) {
        for (int i = 0; i <= segments; ++i) if (wp.elementAt(i).isSmaller(rhs.wp.elementAt(i))) return true;
        return false;
    }

    String tostring_bases() {
        String ss = "";
        for (int i = 0; i < segments; ++i) ss += wp.elementAt(i).tostring_bases(Utils.all_ones);
        ss += wp.elementAt(segments).tostring_bases(score_mask);
        return ss;
    }

    String tostring_bits() {
        String ss = "";
        for (int i = 0; i < segments; ++i) ss += wp.elementAt(i).tostring_bits(Utils.all_ones) + "\n";
        ss += wp.elementAt(segments).tostring_bits(score_mask) + "\n";
        return ss;
    }

    @Override
    public void shift(int idx) {
        for (int i = 0; i < segments; ++i) wp.elementAt(i).shift(wp.elementAt(i + 1));
        wp.elementAt(segments).shift(idx, right_most_bit);
    }

    @Override
    public int score(Read otherR) {
        if (!otherR.getClass().equals(FastRead.class)) return -1;
        FastRead other = (FastRead) otherR;
        int ss = 0;
        for (int i = 0; i < segments; ++i) ss += wp.elementAt(i).score(other.wp.elementAt(i), Utils.all_ones);
        return ss + wp.elementAt(segments).score(other.wp.elementAt(segments), score_mask);
    }

    @Override
    public int score_tc(Read otherR) {
        if (!otherR.getClass().equals(FastRead.class)) return -1;
        FastRead other = (FastRead) otherR;
        int ss = 0;
        for (int i = 0; i < segments; ++i) ss += wp.elementAt(i).score_tc(other.wp.elementAt(i), Utils.all_ones);
        return ss + wp.elementAt(segments).score_tc(other.wp.elementAt(segments), score_mask);
    }

    @Override
    public int score_ag(Read otherR) {
        if (!otherR.getClass().equals(FastRead.class)) return -1;
        FastRead other = (FastRead) otherR;
        int ss = 0;
        for (int i = 0; i < segments; ++i) ss += wp.elementAt(i).score_ag(other.wp.elementAt(i), Utils.all_ones);
        return ss + wp.elementAt(segments).score_ag(other.wp.elementAt(segments), score_mask);
    }

    public static void set_read_width(final int m) {
        read_width = m;
        score_mask = (Utils.low_bit << (m % Utils.word_size)) - 1;
        right_most_bit = (Utils.word_size - (m % Utils.word_size));
        score_mask <<= right_most_bit;
        segments = (int) Math.ceil(m / (float) (Utils.word_size)) - 1;
    }
}
