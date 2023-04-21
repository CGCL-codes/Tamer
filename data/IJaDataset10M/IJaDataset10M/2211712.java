package net.redlightning.dht.kad.messages;

import java.util.Map;
import net.redlightning.dht.kad.DHT;
import net.redlightning.dht.kad.Key;

/**
 * @author Damokles
 *
 */
public class GetPeersRequest extends AbstractLookupRequest {

    boolean noSeeds;

    boolean scrape;

    /**
	 * @param id
	 * @param info_hash
	 */
    public GetPeersRequest(Key info_hash) {
        super(info_hash, Method.GET_PEERS);
    }

    @Override
    public void apply(DHT dh_table) {
        dh_table.getPeers(this);
    }

    @Override
    public Map<String, Object> getInnerMap() {
        Map<String, Object> innerMap = super.getInnerMap();
        if (noSeeds) innerMap.put("noseed", Long.valueOf(1));
        if (scrape) innerMap.put("scrape", Long.valueOf(1));
        return innerMap;
    }

    public boolean isNoSeeds() {
        return noSeeds;
    }

    public void setNoSeeds(boolean noSeeds) {
        this.noSeeds = noSeeds;
    }

    public boolean isScrape() {
        return scrape;
    }

    public void setScrape(boolean scrape) {
        this.scrape = scrape;
    }

    @Override
    protected String targetBencodingName() {
        return "info_hash";
    }

    /**
	 * @return the info_hash
	 */
    public Key getInfoHash() {
        return target;
    }
}
