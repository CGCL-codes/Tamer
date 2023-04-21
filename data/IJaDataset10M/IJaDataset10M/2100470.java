package net.redlightning.dht.kad.messages;

import java.util.Map;
import java.util.TreeMap;
import net.redlightning.dht.kad.DHT;
import net.redlightning.dht.kad.Key;

/**
 * @author Damokles
 */
public class AnnounceRequest extends GetPeersRequest {

    protected int port;

    boolean isSeed;

    protected byte[] token;

    /**
	 * @param id
	 * @param info_hash
	 * @param port
	 * @param token
	 */
    public AnnounceRequest(Key info_hash, int port, byte[] token) {
        super(info_hash);
        this.port = port;
        this.token = token;
        this.method = Method.ANNOUNCE_PEER;
    }

    public boolean isSeed() {
        return isSeed;
    }

    public void setSeed(boolean isSeed) {
        this.isSeed = isSeed;
    }

    @Override
    public void apply(DHT dh_table) {
        dh_table.announce(this);
    }

    @Override
    public Map<String, Object> getInnerMap() {
        Map<String, Object> inner = new TreeMap<String, Object>();
        inner.put("id", id.getHash());
        inner.put("info_hash", target.getHash());
        inner.put("port", port);
        inner.put("token", token);
        inner.put("seed", Long.valueOf(isSeed ? 1 : 0));
        return inner;
    }

    /**
	 * @return the token
	 */
    public byte[] getToken() {
        return token;
    }

    public int getPort() {
        return port;
    }
}
