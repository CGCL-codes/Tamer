package org.magnos.asset.props;

import java.io.InputStream;
import java.util.Properties;
import org.magnos.asset.AssetInfo;
import org.magnos.asset.base.BaseAssetFormat;

/**
 * A format for loading {@link Properties} from PROPERTIES and XML files. If 
 * the application also loads other XML files this should be added before the
 * XmlFormat to ensure that any XML files are not loaded by this format.
 * 
 * @author Philip Diffenderfer
 *
 */
public class PropertyFormat extends BaseAssetFormat {

    /**
	 * Instantiates a new PropertyFormat.
	 */
    public PropertyFormat() {
        super(new String[] { "properties", "xml", "config" }, Properties.class, Config.class);
    }

    @Override
    public Properties loadAsset(InputStream input, AssetInfo assetInfo) throws Exception {
        Properties props = null;
        if (assetInfo.isType(Properties.class)) {
            props = new Properties();
        }
        if (assetInfo.isType(Config.class)) {
            props = new Config();
        }
        if (isXml(assetInfo)) {
            props.loadFromXML(input);
        } else {
            props.load(input);
        }
        return props;
    }

    /**
	 * Returns true if the given AssetInfo represents an XML asset.
	 * 
	 * @param info
	 * 		The AssetInfo to inspect.
	 * @return
	 * 		True if the AssetInfo ends with XML.
	 */
    private boolean isXml(AssetInfo info) {
        return info.getRequest().toLowerCase().endsWith(".xml");
    }
}
