package com.aelitis.azureus.core.dht.router.impl;

import org.gudy.azureus2.core3.util.SHA1Simple;
import com.aelitis.azureus.core.dht.router.*;

public class Test {

    public static void main(String[] args) {
        randomTest();
    }

    protected static void simpleTest() {
        DHTRouter router = DHTRouterFactory.create(1, 1, 1, new byte[] { 0 }, null, com.aelitis.azureus.core.dht.impl.Test.getLogger());
        router.setAdapter(new DHTRouterAdapter() {

            public void requestAdd(DHTRouterContact contact) {
            }

            public void requestLookup(byte[] id, String description) {
            }

            public void requestPing(DHTRouterContact contact) {
            }
        });
        byte[][] node_ids = { { toByte("11111111") }, { toByte("01111111") }, { toByte("00101111") }, { toByte("00100111") } };
        for (int i = 0; i < node_ids.length; i++) {
            router.contactKnown(node_ids[i], null);
        }
        router.print();
    }

    protected static void randomTest() {
        DHTRouter router = DHTRouterFactory.create(20, 5, 5, getSHA1(), null, com.aelitis.azureus.core.dht.impl.Test.getLogger());
        router.setAdapter(new DHTRouterAdapter() {

            public void requestAdd(DHTRouterContact contact) {
            }

            public void requestLookup(byte[] id, String description) {
            }

            public void requestPing(DHTRouterContact contact) {
            }
        });
        for (int i = 0; i < 1000000; i++) {
            byte[] id = getSHA1();
            router.contactKnown(id, null);
        }
        router.print();
    }

    protected static long next_sha1_seed = 0;

    protected static byte[] getSHA1() {
        return (new SHA1Simple().calculateHash(("" + (next_sha1_seed++)).getBytes()));
    }

    protected static byte toByte(String str) {
        int res = 0;
        for (int i = 0; i < 8; i++) {
            if (str.charAt(i) == '1') {
                res += 1 << (7 - i);
            }
        }
        return ((byte) res);
    }
}
