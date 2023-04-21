package jwu2.net.cache;

import java.io.Serializable;
import lombok.Getter;

/**
 *
 * @author Rolf
 */
public class SiteCacheElement implements Serializable {

    @Getter
    private long timestamp;

    @Getter
    private String site;

    public SiteCacheElement(String siteCacheData) {
        this.site = siteCacheData;
        timestamp = System.currentTimeMillis();
    }
}
