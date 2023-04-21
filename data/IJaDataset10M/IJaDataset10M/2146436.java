package net.sf.tacos.asset;

import java.io.InputStream;
import org.apache.tapestry.IAsset;
import org.apache.hivemind.Resource;
import org.apache.hivemind.Location;
import org.apache.tapestry.services.AbsoluteURLBuilder;

/**
 * Decorates an asset in order to generate an absolute url.
 * @author Andreas Andreou
 */
public class AbsoluteAsset implements IAsset {

    private IAsset asset;

    private AbsoluteURLBuilder absoluteURLBuilder;

    public AbsoluteAsset(IAsset asset, AbsoluteURLBuilder absoluteURLBuilder) {
        this.asset = asset;
        this.absoluteURLBuilder = absoluteURLBuilder;
    }

    public String buildURL() {
        return absoluteURLBuilder.constructURL(asset.buildURL());
    }

    public InputStream getResourceAsStream() {
        return asset.getResourceAsStream();
    }

    public Resource getResourceLocation() {
        return asset.getResourceLocation();
    }

    public Location getLocation() {
        return asset.getLocation();
    }
}
