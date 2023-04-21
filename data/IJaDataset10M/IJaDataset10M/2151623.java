package old;

public class WoodStock extends Stock<Wood> {

    protected WoodStock[] createArrayofMiddle(int capacity) {
        return new WoodStock[capacity];
    }
}
