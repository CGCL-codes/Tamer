package jalgebrava.group;

import static jalgebrava.util.collections.IterableMixins.*;
import jalgebrava.util.F;

public class GroupUtil {

    @SuppressWarnings("unused")
    private static GroupUtil instance = new GroupUtil();

    private GroupUtil() {
    }

    public static <G> Subset<G> mul(final G x, final Subset<G> h2) {
        F<G, G> leftMulByX = new F<G, G>() {

            @Override
            public G apply(G a) {
                return h2.getGroup().mul(x, a);
            }
        };
        return new Subset<G>(h2.getGroup(), map(h2, leftMulByX));
    }

    public static <G> Subset<G> mul(final GroupElement<G> x, final Subset<G> h) {
        return mul(x.g, h);
    }

    public static <G> Subset<G> mul(final Subset<G> h1, final G x) {
        F<G, G> rightMulByX = new F<G, G>() {

            @Override
            public G apply(G a) {
                return h1.getGroup().mul(a, x);
            }
        };
        return new Subset<G>(h1.getGroup(), map(h1, rightMulByX));
    }

    public static <G> Subset<G> mul(final Subset<G> h1, final GroupElement<G> x) {
        return mul(h1, x.g);
    }
}
