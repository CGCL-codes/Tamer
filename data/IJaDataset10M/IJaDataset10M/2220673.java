package nyc3d;

import nyc3d.street.*;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import nyc3d.vehicle.Sedan;
import nyc3d.vehicle.Vehicle;

/**
 *
 * @author Joseph
 */
public class Builder {

    public static void buildRoadsFromFile(String fileName, AssetManager assetManager, PhysicsSpace pSpace, ArrayList<Node> nodes) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line = "", name = "", centerX = "", centerY = "", centerZ = "", halfXSize = "", halfYSize = "", halfZSize = "", scaleX = "", scaleY = "", scaleZ = "", rotX = "", rotY = "", rotZ = "", texture = "", textureNormal = "";
        int start = 0, end = 0;
        while ((line = in.readLine()) != null) {
            if (!line.equals("") && line.charAt(0) != '/') {
                start = end = 0;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                name = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                texture = line.substring(start, end);
                start = end + 1;
                Node pivotNode = new Node(name + "_PivotNode");
                Box box = new Box(Float.valueOf(scaleX) / 2f, Float.valueOf(scaleY) / 2f, Float.valueOf(scaleZ) / 2f);
                Geometry geometry = new Geometry(name, box);
                geometry.setShadowMode(ShadowMode.Receive);
                geometry.setLocalTranslation(new Vector3f(Float.valueOf(centerX), Float.valueOf(centerY), Float.valueOf(centerZ)));
                Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
                mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/" + texture));
                mat.setFloat("Shininess", 5f);
                geometry.setMaterial(mat);
                pivotNode.attachChild(geometry);
                pivotNode.rotate(Float.valueOf(rotX), Float.valueOf(rotY), Float.valueOf(rotZ));
                RigidBodyControl physicsBox = new RigidBodyControl(new BoxCollisionShape(new Vector3f(Float.valueOf(scaleX) / 2f, Float.valueOf(scaleY) / 2f, Float.valueOf(scaleZ) / 2f)), 0);
                pSpace.add(physicsBox);
                nodes.get(1).attachChild(pivotNode);
                Quaternion q1 = new Quaternion();
                q1.fromAngleAxis(Float.valueOf(rotY), Vector3f.UNIT_Y);
                physicsBox.setSpatial(geometry);
                physicsBox.createDebugShape(assetManager);
                physicsBox.setKinematic(true);
            }
        }
    }

    public static ArrayList<TrafficLight> buildTrafficSignalsFromFile(String fileName, AssetManager assetManager, PhysicsSpace pSpace, ArrayList<Node> nodes) throws IOException {
        ArrayList<TrafficLight> lightList = new ArrayList<TrafficLight>();
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line = "", name = "", type = "", centerX = "", centerY = "", centerZ = "", scaleX = "", scaleY = "", scaleZ = "", rotX = "", rotY = "", rotZ = "", link = "";
        int start = 0, end = 0;
        while ((line = in.readLine()) != null) {
            if (!line.equals("") && line.charAt(0) != '/') {
                start = end = 0;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                name = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                type = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                link = line.substring(start, end);
                start = end + 1;
                lightList.add(new TrafficLight(name, TrafficLight.Type.valueOf(type), assetManager, new Vector3f(Float.valueOf(centerX), Float.valueOf(centerY), Float.valueOf(centerZ)), new Vector3f(Float.valueOf(rotX), Float.valueOf(rotY), Float.valueOf(rotZ)), new Vector3f(Float.valueOf(scaleX), Float.valueOf(scaleY), Float.valueOf(scaleZ)), nodes.get(0), pSpace));
                if (!link.equals("null")) {
                    for (TrafficLight light : lightList) {
                        if (light.getName().equals(link)) {
                            light.link(lightList.get(lightList.size() - 1));
                            break;
                        }
                    }
                }
            }
        }
        return lightList;
    }

    public static void buildSidewalksFromFile(String fileName, AssetManager assetManager, PhysicsSpace pSpace, ArrayList<Node> nodes) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line = "", name = "", centerX = "", centerY = "", centerZ = "", halfXSize = "", halfYSize = "", halfZSize = "", scaleX = "", scaleY = "", scaleZ = "", rotX = "", rotY = "", rotZ = "", texture = "", textureNormal = "", endCenterX = "", endCenterY = "", endCenterZ = "";
        int start = 0, end = 0;
        while ((line = in.readLine()) != null) {
            if (!line.equals("") && line.charAt(0) != '/') {
                start = end = 0;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                name = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                halfXSize = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                halfYSize = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                halfZSize = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                texture = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                textureNormal = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                endCenterX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                endCenterY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                endCenterZ = line.substring(start, end);
                start = end + 1;
                if (endCenterX.equals("0") && endCenterY.equals("0") && endCenterZ.equals("0")) {
                    Sidewalk s = new Sidewalk(nodes.get(2), name, texture, new Vector3f(Float.valueOf(centerX), Float.valueOf(centerY), Float.valueOf(centerZ)), new Vector3f(Float.valueOf(rotX), Float.valueOf(rotY), Float.valueOf(rotZ)), new Vector3f(Float.valueOf(scaleX), Float.valueOf(scaleY), Float.valueOf(scaleZ)), assetManager, pSpace);
                } else Sidewalk.buildLinear(nodes.get(2), name, texture, new Vector3f(Float.valueOf(centerX), Float.valueOf(centerY), Float.valueOf(centerZ)), new Vector3f(Float.valueOf(endCenterX), Float.valueOf(endCenterY), Float.valueOf(endCenterZ)), new Vector3f(Float.valueOf(rotX), Float.valueOf(rotY), Float.valueOf(rotZ)), new Vector3f(Float.valueOf(scaleX), Float.valueOf(scaleY), Float.valueOf(scaleZ)), assetManager, pSpace);
            }
        }
    }

    public static void buildCarsFromFile(ArrayList<Vehicle> vehicleList, String fileName, AppSettings settings, InputManager inputManager, ViewPort viewPort, AssetManager assetManager, PhysicsSpace pSpace, ArrayList<Node> nodes) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line = "", name = "", model = "", centerX = "", centerY = "", centerZ = "", rotX = "", rotY = "", rotZ = "";
        int start = 0, end = 0;
        while ((line = in.readLine()) != null) {
            if (!line.equals("") && line.charAt(0) != '/') {
                start = end = 0;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                name = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                model = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotZ = line.substring(start, end);
                start = end + 1;
                vehicleList.add(new Sedan(name, model, new Vector3f(Float.valueOf(centerX), Float.valueOf(centerY), Float.valueOf(centerZ)), new Vector3f(Float.valueOf(rotX), Float.valueOf(rotY), Float.valueOf(rotZ)), settings, inputManager, viewPort, assetManager, nodes.get(3), pSpace));
            }
        }
    }

    public static void buildBuildingsFromFile(String fileName, AssetManager assetManager, PhysicsSpace pSpace, ArrayList<Node> nodes) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line = "", name = "", centerX = "", centerY = "", centerZ = "", halfXSize = "", halfYSize = "", halfZSize = "", scaleX = "", scaleY = "", scaleZ = "", rotX = "", rotY = "", rotZ = "", color = "";
        int start = 0, end = 0;
        while ((line = in.readLine()) != null) {
            if (!line.equals("") && line.charAt(0) != '/') {
                start = end = 0;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                name = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                centerZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                halfXSize = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                halfYSize = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                halfZSize = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                scaleZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotX = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotY = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                rotZ = line.substring(start, end);
                start = end + 1;
                for (end = start; end < line.length() && line.charAt(end) != ' '; end++) ;
                color = line.substring(start, end);
                start = end + 1;
                Node pivotNode = new Node(name + "_PivotNode");
                Box car = new Box(Vector3f.ZERO, Float.valueOf(scaleX) / 2f, Float.valueOf(scaleY) / 2f, Float.valueOf(scaleZ) / 2f);
                Geometry geom = new Geometry(name, car);
                geom.setShadowMode(ShadowMode.CastAndReceive);
                geom.setLocalTranslation(new Vector3f(Float.valueOf(centerX), Float.valueOf(centerY), Float.valueOf(centerZ)));
                Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
                material.setFloat("Shininess", 100f);
                material.setBoolean("UseMaterialColors", true);
                if (color.toLowerCase().equals("red")) material.setColor("Diffuse", ColorRGBA.Red);
                if (color.toLowerCase().equals("blue")) material.setColor("Diffuse", ColorRGBA.Blue);
                if (color.toLowerCase().equals("brown")) material.setColor("Diffuse", ColorRGBA.Brown);
                if (color.toLowerCase().equals("white")) material.setColor("Diffuse", ColorRGBA.White);
                geom.setMaterial(material);
                pivotNode.attachChild(geom);
                pivotNode.rotate(Float.valueOf(rotX), Float.valueOf(rotY), Float.valueOf(rotZ));
                RigidBodyControl physicsBox = new RigidBodyControl(new BoxCollisionShape(new Vector3f(Float.valueOf(scaleX) / 2f, Float.valueOf(scaleY) / 2f, Float.valueOf(scaleZ) / 2f)), 0);
                physicsBox.setSpatial(geom);
                physicsBox.setKinematic(true);
                pSpace.add(physicsBox);
                nodes.get(4).attachChild(pivotNode);
            }
        }
    }
}
