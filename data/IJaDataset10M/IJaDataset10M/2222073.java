package net.rptools.maptool.client;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import net.rptools.maptool.client.swing.ImagePanelModel;
import net.rptools.maptool.model.AssetGroup;
import net.rptools.maptool.util.ImageManager;

public class AssetGroupImagePanelModel implements ImagePanelModel {

    private AssetGroup assetGroup;

    public AssetGroupImagePanelModel(AssetGroup assetGroup) {
        this.assetGroup = assetGroup;
    }

    public int getImageCount() {
        return assetGroup.getAssetCount();
    }

    public Image getImage(int index) {
        return ImageManager.getImage(assetGroup.getAssets().get(index));
    }

    public Transferable getTransferable(int index) {
        return new TransferableAsset(assetGroup.getAssets().get(index));
    }
}
