package nyc3d.street;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * A standard traffic light.
 */
public class TrafficLight {

    /** The name of the traffic light */
    String name;

    Spatial spatial;

    Material material;

    RigidBodyControl control;

    Node parentNode;

    TrafficLightSide sides[];

    boolean changing;

    TrafficLightSide.Type changingType;

    long switchTime, time;

    public enum Type {

        TWO_ONE_TOWARD, TWO_ONE_AWAY
    }

    ;

    private Type type;

    ArrayList<TrafficLight> linkedTrafficLights;

    ArrayList<PedestrianWalkSignal> linkedWalkSignals;

    /**Creates a traffic light with the given name, type, and orientation
     * @param name The name of the traffic light
     * @param type The type of the traffic light
     * @param assetManager The AssetManager used to load models
     * @param translation The translation of the light
     * @param rotation The rotation of the light
     * @param scale The scale of the light
     * @param parentNode The node the light should be attached to
     * @param pSpace The physics space to place the light in
     */
    public TrafficLight(String name, Type type, AssetManager assetManager, Vector3f translation, Vector3f rotation, Vector3f scale, Node parentNode, PhysicsSpace pSpace) {
        this.name = name;
        this.parentNode = parentNode;
        this.type = type;
        linkedTrafficLights = new ArrayList<TrafficLight>();
        linkedWalkSignals = new ArrayList<PedestrianWalkSignal>();
        switch(type) {
            case TWO_ONE_AWAY:
                spatial = assetManager.loadModel("Scenes/TrafficLightScene/TrafficLightScene.j3o");
                break;
            case TWO_ONE_TOWARD:
                spatial = assetManager.loadModel("Scenes/TrafficLightScene2/TrafficLightScene2.j3o");
                break;
            default:
                spatial = assetManager.loadModel("Scenes/TrafficLightScene/TrafficLightScene.j3o");
                break;
        }
        control = new RigidBodyControl(1000);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 100f);
        spatial.setMaterial(mat);
        spatial.setShadowMode(ShadowMode.CastAndReceive);
        spatial.setLocalTranslation(translation);
        spatial.rotate(rotation.x, rotation.y, rotation.z);
        control.setSpatial(spatial);
        switch(type) {
            case TWO_ONE_TOWARD:
                Point2D original1Toward = new Point2D.Float(translation.x + 8.8f, translation.z - 9.2f);
                AffineTransform atToward = AffineTransform.getRotateInstance(-rotation.y, translation.x, translation.z);
                Point2D rotated1Toward = atToward.transform(original1Toward, null);
                Point2D original2Toward = new Point2D.Float(translation.x + 8.84f, translation.z - 7.28f);
                AffineTransform at2Toward = AffineTransform.getRotateInstance(-rotation.y, translation.x, translation.z);
                Point2D rotated2Toward = at2Toward.transform(original2Toward, null);
                Point2D original3Toward = new Point2D.Float(translation.x + 7.85f, translation.z - 8.22f);
                AffineTransform at3Toward = AffineTransform.getRotateInstance(-rotation.y, translation.x, translation.z);
                Point2D rotated3Toward = at3Toward.transform(original3Toward, null);
                sides = new TrafficLightSide[3];
                sides[0] = new TrafficLightSide(TrafficLightSide.Type.MAIN, name, assetManager, new Vector3f((float) rotated1Toward.getX(), translation.y + 14.69f, (float) rotated1Toward.getY()), rotation, this, parentNode);
                sides[1] = new TrafficLightSide(TrafficLightSide.Type.MAIN, name, assetManager, new Vector3f((float) rotated2Toward.getX(), translation.y + 14.69f, (float) rotated2Toward.getY()), rotation, this, parentNode);
                sides[2] = new TrafficLightSide(TrafficLightSide.Type.SECONDARY, name, assetManager, new Vector3f((float) rotated3Toward.getX(), translation.y + 14.69f, (float) rotated3Toward.getY()), rotation, this, parentNode);
                break;
            case TWO_ONE_AWAY:
                Point2D original1Away = new Point2D.Float(translation.x + 7.48f, translation.z - 7.3f);
                AffineTransform atAway = AffineTransform.getRotateInstance(-rotation.y, translation.x, translation.z);
                Point2D rotated1Away = atAway.transform(original1Away, null);
                Point2D original2Away = new Point2D.Float(translation.x + 7.45f, translation.z - 9.2f);
                AffineTransform at2Away = AffineTransform.getRotateInstance(-rotation.y, translation.x, translation.z);
                Point2D rotated2Away = at2Away.transform(original2Away, null);
                Point2D original3Away = new Point2D.Float(translation.x + 8.45f, translation.z - 8.25f);
                AffineTransform at3Away = AffineTransform.getRotateInstance(-rotation.y, translation.x, translation.z);
                Point2D rotated3Away = at3Away.transform(original3Away, null);
                sides = new TrafficLightSide[3];
                sides[0] = new TrafficLightSide(TrafficLightSide.Type.MAIN, name, assetManager, new Vector3f((float) rotated1Away.getX(), translation.y + 14.61f, (float) rotated1Away.getY()), rotation, this, parentNode);
                sides[1] = new TrafficLightSide(TrafficLightSide.Type.MAIN, name, assetManager, new Vector3f((float) rotated2Away.getX(), translation.y + 14.61f, (float) rotated2Away.getY()), rotation, this, parentNode);
                sides[2] = new TrafficLightSide(TrafficLightSide.Type.SECONDARY, name, assetManager, new Vector3f((float) rotated3Away.getX(), translation.y + 14.61f, (float) rotated3Away.getY()), rotation, this, parentNode);
                break;
        }
        System.out.println(rotation);
        parentNode.attachChild(spatial);
        pSpace.add(control);
        control.setKinematic(true);
        changing = false;
        changingType = TrafficLightSide.Type.MAIN;
        switchTime = 1000;
        time = System.currentTimeMillis();
    }

    /** Links this traffic light with the provided light. Note that any changes in this light will affect the provided light
     * @param light2 The light to link to this light
     */
    public void link(TrafficLight light2) {
        linkedTrafficLights.add(light2);
    }

    public void linkPedWalkSignal(PedestrianWalkSignal pedSignal) {
        linkedWalkSignals.add(pedSignal);
    }

    public void update(float tpf) {
        for (int i = 0; i < sides.length; i++) {
            sides[i].update(tpf);
        }
        if (changing && System.currentTimeMillis() - time >= switchTime) {
            switch(changingType) {
                case MAIN:
                    for (int i = 0; i < linkedTrafficLights.size(); i++) {
                        for (int n = 0; n < linkedTrafficLights.get(i).getSides().length; n++) {
                            if (linkedTrafficLights.get(i).getSides()[n].getType().equals(TrafficLightSide.Type.SECONDARY)) {
                                linkedTrafficLights.get(i).getParentNode().detachChild(linkedTrafficLights.get(i).getSides()[n].getCurrentCircle());
                                linkedTrafficLights.get(i).getParentNode().removeLight(linkedTrafficLights.get(i).getSides()[n].getCurrentPointLight());
                                linkedTrafficLights.get(i).getSides()[n].setCurrentCircle(linkedTrafficLights.get(i).getSides()[n].getGreenCircle());
                                linkedTrafficLights.get(i).getSides()[n].setCurrentPointLight(linkedTrafficLights.get(i).getSides()[n].getGreenPointLight());
                                linkedTrafficLights.get(i).getParentNode().attachChild(linkedTrafficLights.get(i).getSides()[n].getCurrentCircle());
                                linkedTrafficLights.get(i).getParentNode().addLight(linkedTrafficLights.get(i).getSides()[n].getCurrentPointLight());
                                linkedTrafficLights.get(i).getSides()[n].setTime(System.currentTimeMillis());
                            }
                        }
                    }
                    for (int i = 0; i < sides.length; i++) {
                        if (sides[i].getType().equals(TrafficLightSide.Type.SECONDARY)) {
                            parentNode.detachChild(sides[i].getCurrentCircle());
                            parentNode.removeLight(sides[i].getCurrentPointLight());
                            sides[i].setCurrentCircle(sides[i].getGreenCircle());
                            sides[i].setCurrentPointLight(sides[i].getGreenPointLight());
                            parentNode.attachChild(sides[i].getCurrentCircle());
                            parentNode.addLight(sides[i].getCurrentPointLight());
                            sides[i].setTime(System.currentTimeMillis());
                            changing = false;
                        }
                    }
                    for (int i = 0; i < linkedWalkSignals.size(); i++) {
                        for (int n = 0; n < linkedWalkSignals.get(i).getSides().length; n++) {
                            if (linkedWalkSignals.get(i).getSides()[n].getType().equals(PedestrianWalkSignalSide.Type.SECONDARY)) {
                                linkedWalkSignals.get(i).getNode().detachChild(linkedWalkSignals.get(i).getSides()[n].getCurrentRect());
                                linkedWalkSignals.get(i).getParentNode().removeLight(linkedWalkSignals.get(i).getSides()[n].getCurrentLight());
                                linkedWalkSignals.get(i).getSides()[n].setCurrentRect(linkedWalkSignals.get(i).getSides()[n].getGoRect());
                                linkedWalkSignals.get(i).getSides()[n].setCurrentLight(linkedWalkSignals.get(i).getSides()[n].getGoLight());
                                linkedWalkSignals.get(i).getNode().attachChild(linkedWalkSignals.get(i).getSides()[n].getCurrentRect());
                                linkedWalkSignals.get(i).getParentNode().addLight(linkedWalkSignals.get(i).getSides()[n].getCurrentLight());
                            }
                        }
                    }
                    break;
                case SECONDARY:
                    for (int i = 0; i < linkedTrafficLights.size(); i++) {
                        for (int n = 0; n < linkedTrafficLights.get(i).getSides().length; n++) {
                            if (linkedTrafficLights.get(i).getSides()[n].getType().equals(TrafficLightSide.Type.MAIN)) {
                                linkedTrafficLights.get(i).getParentNode().detachChild(linkedTrafficLights.get(i).getSides()[n].getCurrentCircle());
                                linkedTrafficLights.get(i).getParentNode().removeLight(linkedTrafficLights.get(i).getSides()[n].getCurrentPointLight());
                                linkedTrafficLights.get(i).getSides()[n].setCurrentCircle(linkedTrafficLights.get(i).getSides()[n].getGreenCircle());
                                linkedTrafficLights.get(i).getSides()[n].setCurrentPointLight(linkedTrafficLights.get(i).getSides()[n].getGreenPointLight());
                                linkedTrafficLights.get(i).getParentNode().attachChild(linkedTrafficLights.get(i).getSides()[n].getCurrentCircle());
                                linkedTrafficLights.get(i).getParentNode().addLight(linkedTrafficLights.get(i).getSides()[n].getCurrentPointLight());
                                linkedTrafficLights.get(i).getSides()[n].setTime(System.currentTimeMillis());
                            }
                        }
                    }
                    for (int i = 0; i < sides.length; i++) {
                        if (sides[i].getType().equals(TrafficLightSide.Type.MAIN)) {
                            parentNode.detachChild(sides[i].getCurrentCircle());
                            parentNode.removeLight(sides[i].getCurrentPointLight());
                            sides[i].setCurrentCircle(sides[i].getGreenCircle());
                            sides[i].setCurrentPointLight(sides[i].getGreenPointLight());
                            parentNode.attachChild(sides[i].getCurrentCircle());
                            parentNode.addLight(sides[i].getCurrentPointLight());
                            sides[i].setTime(System.currentTimeMillis());
                            changing = false;
                        }
                    }
                    for (int i = 0; i < linkedWalkSignals.size(); i++) {
                        for (int n = 0; n < linkedWalkSignals.get(i).getSides().length; n++) {
                            if (linkedWalkSignals.get(i).getSides()[n].getType().equals(PedestrianWalkSignalSide.Type.MAIN)) {
                                linkedWalkSignals.get(i).getNode().detachChild(linkedWalkSignals.get(i).getSides()[n].getCurrentRect());
                                linkedWalkSignals.get(i).getParentNode().removeLight(linkedWalkSignals.get(i).getSides()[n].getCurrentLight());
                                linkedWalkSignals.get(i).getSides()[n].setCurrentRect(linkedWalkSignals.get(i).getSides()[n].getGoRect());
                                linkedWalkSignals.get(i).getSides()[n].setCurrentLight(linkedWalkSignals.get(i).getSides()[n].getGoLight());
                                linkedWalkSignals.get(i).getNode().attachChild(linkedWalkSignals.get(i).getSides()[n].getCurrentRect());
                                linkedWalkSignals.get(i).getParentNode().addLight(linkedWalkSignals.get(i).getSides()[n].getCurrentLight());
                            }
                        }
                    }
                    break;
            }
        }
    }

    /**
     * The method called with a red light is triggered (usually called by a TrafficLightSide)
     * @param type The type of the TrafficLightSide that is calling the method
     */
    protected void triggerRedLight(TrafficLightSide.Type type) {
        changing = true;
        changingType = type;
        time = System.currentTimeMillis();
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }

    public TrafficLightSide[] getSides() {
        return sides;
    }

    public void setSides(TrafficLightSide[] sides) {
        this.sides = sides;
    }

    public long getSwitchTime() {
        return switchTime;
    }

    public void setSwitchTime(long switchTime) {
        this.switchTime = switchTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isChanging() {
        return changing;
    }

    public void setChanging(boolean changing) {
        this.changing = changing;
    }

    public TrafficLightSide.Type getChangingType() {
        return changingType;
    }

    public void setChangingType(TrafficLightSide.Type changingType) {
        this.changingType = changingType;
    }

    public ArrayList<TrafficLight> getLinkedTrafficLights() {
        return linkedTrafficLights;
    }

    public void setLinkedTrafficLights(ArrayList<TrafficLight> linkedTrafficLights) {
        this.linkedTrafficLights = linkedTrafficLights;
    }

    public ArrayList<PedestrianWalkSignal> getLinkedWalkSignals() {
        return linkedWalkSignals;
    }

    public void setLinkedWalkSignals(ArrayList<PedestrianWalkSignal> linkedWalkSignals) {
        this.linkedWalkSignals = linkedWalkSignals;
    }
}
