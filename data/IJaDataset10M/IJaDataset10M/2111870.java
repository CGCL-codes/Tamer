package sample.chapter6.patterMatching;

public class PrimitiveWrapper {

    public static void main(String[] args) {
        Short i = 126;
        Short j = 126;
        Long l = 343l;
        assert (!i.equals(l));
        assert (l < 345);
        assert (i == j) : "The two Shorts cannot be compared. ";
    }
}
