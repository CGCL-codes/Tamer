package org.vrforcad;

import java.io.File;
import java.util.ArrayList;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import org.vrforcad.lib.SouthMessage;
import org.vrforcad.model.VRforCADWorkSpace;
import org.vrforcad.model.cad.ShapeVfC;
import org.vrforcad.model.cad.format.input.InputShape3D;
import org.vrforcad.model.cad.format.output.OutputShape3D;
import org.vrforcad.model.scene.SceneLights;

/**
 * The VRforCAD application Model.
 * @version 1.1 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class VRforCADModel extends VRforCADWorkSpace implements ModelInterface {

    private ArrayList<VRforCADObserver> observers = new ArrayList<VRforCADObserver>();

    private boolean fullScreenFlag = false;

    private SouthMessage currentMessage;

    /**
	 * Default constructor.
	 */
    public VRforCADModel() {
        super();
    }

    @Override
    public void registerObserver(VRforCADObserver obs) {
        observers.add(obs);
    }

    @Override
    public void removeObserver(VRforCADObserver obs) {
        if (observers.size() > 0) {
            int observerID = observers.indexOf(obs);
            observers.remove(observerID);
        }
    }

    @Override
    public void notifyVRforCADObservers(int um) {
        for (int i = 0; i < observers.size(); i++) observers.get(i).update(um);
    }

    @Override
    public void setAppMode(int appMode) {
    }

    @Override
    public void setBackgroundColor(Color3f bgColor) {
        getScene3D().setBackgroundColor(bgColor);
        saveSettings();
    }

    @Override
    public Color3f getBackgroundColor() {
        return getScene3D().getBackgroundColor();
    }

    @Override
    public Canvas3D getCanvas3D() {
        return getWorkSpace();
    }

    @Override
    public void setFullScreen(boolean fs) {
        fullScreenFlag = fs;
        notifyVRforCADObservers(VRforCADObserver.FULL_SCREEN);
    }

    @Override
    public void setSceneControll() {
        notifyVRforCADObservers(VRforCADObserver.SCENE_CONTROLL);
    }

    @Override
    public boolean getFullScreen() {
        return fullScreenFlag;
    }

    @Override
    public void addNewShapeToScene(File path) {
        ShapeVfC svfc = new InputShape3D(path).getShape3D();
        inportFileMessage(svfc);
        if (svfc != null) {
            getScene3D().addNewShape3D(svfc);
            notifyVRforCADObservers(VRforCADObserver.NEW_SHAPE_ADDED);
        }
    }

    @Override
    public void addSingleShape(File path) {
        ShapeVfC svfc = new InputShape3D(path).getShape3D();
        inportFileMessage(svfc);
        if (svfc != null) {
            getScene3D().addNewShape3DFromMenu(svfc);
        }
    }

    /**
	 * A message to be shown with the file import result. 
	 * @param svfc
	 */
    private void inportFileMessage(ShapeVfC svfc) {
        if (svfc != null) {
            currentMessage = new SouthMessage("CAD file imported", "No errors", SouthMessage.TYPE_INFO);
        } else {
            currentMessage = new SouthMessage("", "The selected CAD file can't be imported", SouthMessage.TYPE_ERROR);
        }
        notifyVRforCADObservers(VRforCADObserver.SOUTH_MESSAGE);
    }

    @Override
    protected void stereoMessage(String info) {
        currentMessage = new SouthMessage(info, "", SouthMessage.TYPE_INFO);
    }

    @Override
    public void showBoundingBox(int node, boolean sbb) {
        try {
            getScene3D().getObjNode(node).showBoundingBox(sbb);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Appearance getShapeApperance(int node) {
        Appearance ap = null;
        try {
            ap = getScene3D().getShapeVfC(node).getAppearance();
        } catch (IllegalAccessException e) {
            currentMessage = new SouthMessage("Add a shape to scene", "Can't set material for no shape", SouthMessage.TYPE_ERROR);
            notifyVRforCADObservers(VRforCADObserver.SOUTH_MESSAGE);
        }
        return ap;
    }

    @Override
    public SceneLights getSceneLights() {
        return getScene3D().getSceneLights();
    }

    @Override
    public ShapeVfC getShapeVfC(int shapeID) {
        ShapeVfC sVfC = null;
        try {
            sVfC = getScene3D().getShapeVfC(shapeID);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sVfC;
    }

    @Override
    public int getSceneShapesCount() {
        return getScene3D().getShapesCount();
    }

    @Override
    public void removeShape(int shapeID) {
        getScene3D().removeObjNode(shapeID);
    }

    @Override
    public BranchGroup getSceneBG() {
        return getScene3D().getSceneBG();
    }

    @Override
    public TransformGroup getSceneTG() {
        return getScene3D().getSceneTG();
    }

    @Override
    public BoundingSphere getSceneBS() {
        return getScene3D().getSceneBS();
    }

    @Override
    public TransformGroup getObjVRTG(int node) {
        TransformGroup tg = null;
        try {
            tg = getScene3D().getObjNode(node).getObjVRTG();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return tg;
    }

    @Override
    public void save3DModel(int shapeID, String filePath) {
        new OutputShape3D(getShapeVfC(shapeID), filePath);
    }

    @Override
    public void save3DModels(String filePath) {
    }

    @Override
    public SouthMessage getMessage() {
        return currentMessage;
    }

    @Override
    public void setSceneToCenter() {
        getScene3D().resetSceneTG();
    }

    @Override
    public void showCoordSys(boolean show) {
        getScene3D().showCoordSys(show);
    }

    @Override
    public float getScaleValue(int shapeID) {
        try {
            return getScene3D().getObjNode(shapeID).getShape().getScale();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void setScaleValue(int shapeID, float scaleValue) {
        try {
            getScene3D().getObjNode(shapeID).getShape().setScale(scaleValue);
            getScene3D().getObjNode(shapeID).scaleObjVR();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
