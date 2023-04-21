package es.eucm.eadventure.editor.gui.assetchooser;

import java.awt.Container;
import javax.swing.BorderFactory;
import es.eucm.eadventure.common.auxiliar.AssetsConstants;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.controllers.AssetsController;
import es.eucm.eadventure.editor.gui.otherpanels.imagepanels.ImagePanel;

public class BackgroundChooser extends AssetChooser {

    private ImagePanel imagePanel;

    public BackgroundChooser(int filter) {
        super(AssetsConstants.CATEGORY_BACKGROUND, filter, AssetChooser.PREVIEW_LOCATION_SOUTH, TC.get("AssetsChooser.Background"));
    }

    @Override
    protected void createPreviewPanel(Container parent) {
        imagePanel = new ImagePanel();
        imagePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), TC.get("BackgroundAssets.Preview")));
        parent.add(imagePanel);
    }

    @Override
    protected void updatePreview() {
        if (getSelectedAsset() != null) {
            String[] assetFilenames = AssetsController.getAssetFilenames(AssetsConstants.CATEGORY_BACKGROUND);
            String[] assetPaths = AssetsController.getAssetsList(AssetsConstants.CATEGORY_BACKGROUND);
            int assetIndex = -1;
            for (int i = 0; i < assetFilenames.length; i++) if (assetFilenames[i].equals(getSelectedAsset())) assetIndex = i;
            imagePanel.loadImage(assetPaths[assetIndex]);
        } else if (getSelectedFile() != null) {
            imagePanel.loadImage(getSelectedFile().getAbsolutePath());
        } else {
            imagePanel.removeImage();
        }
    }
}
