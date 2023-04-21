package es.eucm.eadventure.editor.control.tools.general.assets;

import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.tools.Tool;
import es.eucm.eadventure.editor.data.AssetInformation;

/**
 * Abstract class for Resources modification. It contains the common data that
 * tools EditResourceTool, SetResourceTool and DeleteResourceTool will use.
 * 
 * @author Javier
 * 
 */
public abstract class ResourcesTool extends Tool {

    /**
     * Controller
     */
    protected Controller controller;

    /**
     * Contained resources. This field is kept updated all the time.
     */
    protected Resources resources;

    /**
     * Old resources. This is a backup copy that is done when the tool is built
     * (for undo)
     */
    protected Resources oldResources;

    /**
     * The assets information of the resources.
     */
    protected AssetInformation[] assetsInformation;

    /**
     * indicates if the resource block belongs to a NPC, the player or other
     * element
     */
    protected int resourcesType;

    /**
     * The index of the resource to be modified
     */
    protected int index;

    /**
     * Default constructor
     * 
     * @throws CloneNotSupportedException
     */
    public ResourcesTool(Resources resources, AssetInformation[] assetsInformation, int resourcesType, int index) throws CloneNotSupportedException {
        this.resources = resources;
        this.assetsInformation = assetsInformation;
        this.resourcesType = resourcesType;
        this.controller = Controller.getInstance();
        this.index = index;
        this.oldResources = (Resources) (resources.clone());
    }

    @Override
    public boolean undoTool() {
        try {
            Resources temp = (Resources) (resources.clone());
            resources.clearAssets();
            String[] oldResourceTypes = oldResources.getAssetTypes();
            for (String type : oldResourceTypes) {
                resources.addAsset(type, oldResources.getAssetPath(type));
            }
            oldResources.clearAssets();
            oldResourceTypes = temp.getAssetTypes();
            for (String type : oldResourceTypes) {
                oldResources.addAsset(type, temp.getAssetPath(type));
            }
            return true;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean redoTool() {
        return undoTool();
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public boolean combine(Tool other) {
        return false;
    }
}
