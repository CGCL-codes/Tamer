package jemfis.fs.themes;

import jemfis.Resources;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import jemfis.App;
import jemfis.Navigator;
import jemfis.fs.FSObject;
import jemfis.fs.FSObjectGroup;
import org.ogre4j.IManualObject;
import org.ogre4j.ISceneNode;
import org.ogre4j.IVector3;
import org.ogre4j.Quaternion;
import org.ogre4j.RenderOperation;
import org.ogre4j.Vector3;

/**
 *
 * @author Max Vyaznikov
 */
public class DefaultDirGroup extends FSObjectGroup<DefaultDirNode, DefaultDirNode> {

    /** Link to the instance of DefaultTree */
    DefaultTree tree;

    /** Opened subdirectories */
    Vector<DefaultDirNode> openedNodes;

    /** boundAngle is limit for child groups */
    float boundAngle;

    /** current boundAngle */
    float activeBoundAngle;

    /** boundAngle for each child */
    float childBoundAngle;

    /** distantion between parent and child */
    float radius;

    /** boundParam - if 3 then we use only boundAngle,
	 *               if 0 then 180 degree,
	 *               if 1 or 2 then (90 + boundAngle/2)
	 */
    byte boundParam = 3;

    public DefaultDirGroup(DefaultTree tree, DefaultDirNode owner) {
        super(owner);
        this.tree = tree;
    }

    public void setLabels() {
        for (DefaultDirNode node : this) {
            tree.writer.createLabel(node);
        }
    }

    public void updatePositions() {
        childBoundAngle = activeBoundAngle / this.size();
        setupRadius();
        for (DefaultDirNode node : this) {
            setupAngles(node);
            setupPosition(node);
        }
    }

    void setupRadius() {
        radius = (float) (2 * MESH_WEIGHT / (boundAngle * Math.PI / 180));
        if (radius < DEFAULT_RADIUS) {
            radius = DEFAULT_RADIUS;
        }
    }

    float getChildDirAngle(int directoryNumber) {
        return owner.angle - activeBoundAngle / 2 + childBoundAngle / 2 + directoryNumber * childBoundAngle + owner.angleShift;
    }

    void setupAngles(DefaultDirNode node) {
        node.angle = getChildDirAngle(node.dirNumber);
        if (boundParam == 0) {
            if (node.dirNumber == (int) ((this.size() - 1) / 2 + 1)) {
                node.dirsGroup.activeBoundAngle = activeBoundAngle < 181.0f ? activeBoundAngle : 180.0f;
            } else {
                node.dirsGroup.activeBoundAngle = activeBoundAngle / 2;
            }
            node.angleShift = 0.0f;
        } else if (boundParam == 1) {
            node.dirsGroup.activeBoundAngle = boundAngle + activeBoundAngle < 181.0f ? activeBoundAngle / 2 : 90.0f;
            node.angleShift = -node.dirsGroup.activeBoundAngle / 2;
        } else if (boundParam == 2) {
            node.dirsGroup.activeBoundAngle = node.dirsGroup.boundAngle + activeBoundAngle < 181.0f ? activeBoundAngle / 2 : 90.0f;
            node.angleShift = activeBoundAngle / 2;
        } else {
            node.dirsGroup.activeBoundAngle = node.dirsGroup.boundAngle;
            node.angleShift = 0.0f;
        }
    }

    void setupPosition(DefaultDirNode node) {
        double angleRad = node.angle * Math.PI / 180;
        IVector3 pos = node.getSceneNode().getPosition();
        IVector3 ownerPos = owner.getSceneNode().getPosition();
        pos.setx((float) (radius * Math.cos(angleRad)) + ownerPos.getx());
        pos.sety((float) (radius * Math.sin(angleRad)) + ownerPos.gety());
        pos.setz(ownerPos.getz());
        for (DefaultFileNode fileNode : owner.filesGroup) {
            IVector3 position = fileNode.getSceneNode().getPosition();
            position.setx(position.getx());
            position.sety(position.gety());
        }
    }

    public static void addDirNodes(DefaultTree tree, DefaultDirGroup ownerDirsGroup, ArrayList<File> dirs) {
        int dirsNum = dirs.size();
        ownerDirsGroup.childBoundAngle = ownerDirsGroup.activeBoundAngle / dirsNum;
        float childAngle = ownerDirsGroup.getChildDirAngle(0);
        for (int i = 0; i < dirsNum; i++, childAngle += ownerDirsGroup.childBoundAngle) {
            DefaultDirNode node = new DefaultDirNode(tree, ownerDirsGroup, Resources.get(DefaultTree.DIRNODE), dirs.get(i), childAngle, ownerDirsGroup.childBoundAngle, i);
            ownerDirsGroup.add(node);
            tree.add(node);
        }
    }

    static int manualObjectsCount = 0;

    /** Linking node with it's parent by line */
    public static void createVisualLinks(Navigator navigator, DefaultDirGroup dirGroup) {
        DefaultDirNode parent = dirGroup.getOwner();
        for (DefaultDirNode node : dirGroup) {
            IManualObject line = navigator.getSceneManager().createManualObject("man" + manualObjectsCount++);
            ISceneNode sceneNode = navigator.getSceneManager().getRootSceneNode().createChildSceneNode(Vector3.getZERO(), Quaternion.getIDENTITY());
            line.begin("BaseWhiteNoLighting", RenderOperation.OperationType.OT_LINE_LIST);
            line.position(parent.getSceneNode().getPosition());
            line.normal(Vector3.getUNIT_Z());
            line.position(node.getSceneNode().getPosition());
            line.normal(Vector3.getUNIT_Z());
            line.end();
            sceneNode.attachObject(line);
        }
    }

    /** Dirty constant. If you'll find the way to remove it - do it! */
    static final float MESH_WEIGHT = 10.0f;

    /** It's minimum distance between parent and child */
    static final float DEFAULT_RADIUS = 10.0f;
}
