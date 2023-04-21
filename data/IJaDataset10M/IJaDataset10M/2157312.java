package es.eucm.eadventure.editor.control.tools.general.assets;

import es.eucm.eadventure.common.auxiliar.AssetsConstants;
import es.eucm.eadventure.common.data.adventure.AdventureData;
import es.eucm.eadventure.common.data.adventure.CustomArrow;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.AssetsController;
import es.eucm.eadventure.editor.data.AssetInformation;

public class SelectArrowTool extends SelectResourceTool {

    protected AdventureData adventureData;

    protected String type;

    protected boolean removed;

    protected static AssetInformation[] createAssetInfoArray(String type) {
        AssetInformation[] array = new AssetInformation[1];
        array[0] = new AssetInformation("", type, true, AssetsConstants.CATEGORY_BUTTON, AssetsController.FILTER_NONE);
        return array;
    }

    protected static Resources createResources(AdventureData adventureData, String type) {
        Resources resources = new Resources();
        boolean introduced = false;
        for (int i = 0; i < adventureData.getArrows().size(); i++) {
            CustomArrow customArrow = adventureData.getArrows().get(i);
            if (customArrow.getType().equals(type)) {
                resources.addAsset(type, customArrow.getPath());
                introduced = true;
                break;
            }
        }
        if (!introduced) {
            resources.addAsset(type, null);
        }
        return resources;
    }

    public SelectArrowTool(AdventureData adventureData, String type) throws CloneNotSupportedException {
        super(createResources(adventureData, type), createAssetInfoArray(type), Controller.RESOURCES, 0);
        this.adventureData = adventureData;
        this.type = type;
    }

    @Override
    public boolean undoTool() {
        boolean done = super.undoTool();
        if (!done) return false; else {
            for (int i = 0; i < adventureData.getArrows().size(); i++) {
                if (adventureData.getArrows().get(i).getType().equals(type)) {
                    if (removed) adventureData.getArrows().remove(i); else adventureData.getArrows().get(i).setPath(resources.getAssetPath(type));
                    break;
                }
            }
            controller.updatePanel();
            controller.dataModified();
            return true;
        }
    }

    @Override
    public boolean redoTool() {
        if (removed) adventureData.addArrow(type, "");
        boolean done = super.redoTool();
        if (!done) return false; else {
            for (int i = 0; i < adventureData.getArrows().size(); i++) {
                if (adventureData.getArrows().get(i).getType().equals(type)) {
                    adventureData.getArrows().get(i).setPath(resources.getAssetPath(type));
                }
            }
            controller.updatePanel();
            return true;
        }
    }

    @Override
    public boolean doTool() {
        if (resources.getAssetPath(type) == null) {
            removed = false;
        } else {
            for (int i = 0; i < adventureData.getArrows().size(); i++) {
                CustomArrow arrow = adventureData.getArrows().get(i);
                if (arrow.getType().equals(type)) {
                    adventureData.getArrows().remove(arrow);
                    break;
                }
            }
            removed = true;
        }
        boolean done = super.doTool();
        if (!done) return false; else {
            setArrow(type, resources.getAssetPath(type));
            return true;
        }
    }

    public void setArrow(String type, String path) {
        CustomArrow arrow = new CustomArrow(type, path);
        CustomArrow temp = null;
        for (CustomArrow cb : adventureData.getArrows()) {
            if (cb.equals(arrow)) temp = cb;
        }
        if (temp != null) adventureData.getArrows().remove(temp);
        adventureData.addArrow(arrow);
    }
}
