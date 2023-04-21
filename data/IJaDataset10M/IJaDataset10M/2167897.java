package libsecondlife.assetsystem;

import libsecondlife.*;

public class AssetImage extends Asset {

    public byte[] getJ2CData() {
        return getAssetData();
    }

    public AssetImage(LLUUID assetID, byte[] assetData) {
        super(assetID, Asset.ASSET_TYPE_IMAGE, false, assetData);
    }
}
