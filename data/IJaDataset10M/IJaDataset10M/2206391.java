package ow.dht.memcached;

import java.math.BigInteger;
import java.util.Set;
import ow.dht.DHT;
import ow.dht.ValueInfo;
import ow.id.ID;
import ow.routing.RoutingException;

public interface Memcached extends DHT<Item> {

    public static final String ENCODING = "UTF-8";

    public static final String PUBLIC_SECRET = "secret";

    public static enum Condition {

        NONE, NOT_EXIST, EXIST, APPEND, PREPEND, CAS, INCREMENT, DECREMENT
    }

    public static final BigInteger UINT64_MAX = BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);

    Set<ValueInfo<Item>> put(ID key, Item value, Condition cond) throws Exception;

    Set<ValueInfo<Item>> put(ID key, Item value, int casUnique) throws Exception;

    Set<ValueInfo<Item>> remove(ID key) throws RoutingException;

    public static class PutRequest extends DHT.PutRequest<Item> {

        private final Condition cond;

        private final int casUnique;

        public PutRequest(ID key, Item[] values, Condition cond) {
            this(key, values, cond, 0);
        }

        public PutRequest(ID key, Item[] values, Condition cond, int casUnique) {
            super(key, values);
            this.cond = cond;
            this.casUnique = casUnique;
        }

        public Condition getCondition() {
            return this.cond;
        }

        public int getCasUnique() {
            return this.casUnique;
        }
    }
}
