package edu.ups.gamedev.game;

import com.jme.intersection.PickResults;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import edu.ups.gamedev.scene.Collidable;

/**
 * Static tools for convenience.
 * 
 * @author stefan
 * 
 */
public class Tools {

    private static PickResults pickResults = new TrianglePickResults();

    /**
	 * Crawls up the scene graph and finds the first parent Collidable of this Node.
	 * @param node
	 * @return
	 */
    public static Collidable getCollidableParent(Node node) {
        while (!(node instanceof Collidable) && node.getParent() != null) {
            node = node.getParent();
        }
        if (node instanceof Collidable) {
            return (Collidable) node;
        } else {
            return null;
        }
    }

    /**
	 * Returns the world coordinates of the closest intersection of the specified <code>Ray</code> and any object under the given root <code>Node</code>. Stores the resulting points in the
	 * specified <code>Vector3f</code>.
	 * 
	 * @param vector
	 * @param rootNode
	 * @param toStore
	 */
    public static void pointPick(Ray vector, Node rootNode, Vector3f toStore) {
        pickResults.setCheckDistance(true);
        pickResults.clear();
        rootNode.findPick(vector, pickResults);
        float distance = 100000000;
        for (int i = 0; i < pickResults.getNumber(); i++) {
            distance = Math.min(pickResults.getPickData(i).getDistance(), distance);
        }
        toStore.set(vector.getDirection().clone().multLocal(distance).addLocal(vector.origin));
    }
}
