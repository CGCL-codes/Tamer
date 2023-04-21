package net.sf.joafip.performance.hashmap;

public final class Stress {

    private final StressSunHashMapWithAKey stressSunHashMapWithAKey;

    private final StressSunHashMapWithAKey2 stressSunHashMapWithAKey2;

    private final StressPHashMapWithAKey stressPHashMapWithAKey;

    private final StressPHashMapWithAKey2 stressPHashMapWithAKey2;

    public static void main(final String[] args) {
        final Stress main = new Stress();
        main.stress();
    }

    private Stress() {
        super();
        stressSunHashMapWithAKey = new StressSunHashMapWithAKey();
        stressSunHashMapWithAKey2 = new StressSunHashMapWithAKey2();
        stressPHashMapWithAKey = new StressPHashMapWithAKey();
        stressPHashMapWithAKey2 = new StressPHashMapWithAKey2();
    }

    private void stress() {
        stressSunHashMapWithAKey.stress("sun key1");
        stressSunHashMapWithAKey2.stress("sun key2");
        stressPHashMapWithAKey.stress("joafip key1");
        stressPHashMapWithAKey2.stress("joafip key2");
    }
}
