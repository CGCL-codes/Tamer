package world;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;

public class House {

    /**
	 * returns a pre-built house
	 * @return house
	 */
    public Node getHouse() {
        Node house = new Node();
        Node floor = getFloor();
        Node walls = getWalls(28, 10, 2);
        house.attachChild(walls);
        house.attachChild(floor);
        return house;
    }

    /**
	 * returns a Box which is the floor
	 * @return floor
	 */
    private Node getFloor() {
        Box floor = new Box("Box", new Vector3f(0, 0, 0), 30, 2, 30);
        floor.setDefaultColor(ColorRGBA.brown);
        floor.setModelBound(new BoundingBox());
        floor.updateModelBound();
        Node floorNode = new Node();
        floorNode.attachChild(floor);
        return floorNode;
    }

    private Node getWalls(float length, float height, float depth) {
        Node walls = new Node();
        Box xNegWall = new Box("xnW", new Vector3f(-30, 0, 0), depth, height, length);
        Box xPosWall = new Box("xpW", new Vector3f(30, 0, 0), depth, height, length);
        Box zNegWall = new Box("znW", new Vector3f(0, 0, 30), length, height, depth);
        Box zPosWall = new Box("zpW", new Vector3f(0, 0, -30), length, height, depth);
        xNegWall.setDefaultColor(ColorRGBA.blue);
        xPosWall.setDefaultColor(ColorRGBA.red);
        zNegWall.setDefaultColor(ColorRGBA.green);
        zPosWall.setDefaultColor(ColorRGBA.cyan);
        xNegWall.setModelBound(new BoundingBox());
        xPosWall.setModelBound(new BoundingBox());
        zNegWall.setModelBound(new BoundingBox());
        zPosWall.setModelBound(new BoundingBox());
        walls.attachChild(xNegWall);
        walls.attachChild(xPosWall);
        walls.attachChild(zNegWall);
        walls.attachChild(zPosWall);
        walls.setModelBound(new BoundingBox());
        walls.updateModelBound();
        return walls;
    }
}
