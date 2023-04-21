package molmaster.gui;

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import molmaster.*;
import molmaster.model.*;
import molmaster.gui.input.*;

/** An instance of this class handles basic
 *  functions related to the current scene -
 *  it provides for adding and removing objects,
 *  as well as some basic utility functions.
 *
 * @author  Russell
 */
public class SceneControl implements ModelListener {

    MolUniverse uni;

    MolModel model;

    public Group rootGroup;

    int currentAtoms = 0;

    public Map fileMap = new HashMap();

    public Map atomMap = new WeakHashMap();

    public static SceneControl activeScene;

    /** Creates a new instance of SceneControl */
    public SceneControl(MolModel model, Group root, MolUniverse universe) {
        this.model = model;
        this.uni = universe;
        this.rootGroup = root;
        activeScene = this;
        model.addListener(this);
    }

    /** Returns an AtomView attached to a TransformGroup.  The
     *  returned TransformGroup has been translated (relative to the
     *  origin) to the position of the given Atom in space. */
    protected void addAtomView(Atom a, Group root) {
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(a.getLocation()));
        currentAtoms++;
        TransformGroup tg = new TransformGroup();
        tg.setTransform(trans);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg.clearCapabilityIsFrequent(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.clearCapabilityIsFrequent(TransformGroup.ALLOW_TRANSFORM_READ);
        AtomView av = new AtomView(a, tg);
        root.addChild(tg);
    }

    /** Processes model events.  SceneControl watches for various
     *  events that from the model that are relevant to updating the
     *  scene.  It then acts to make the scene conform with the model,
     *  if possible.
     */
    public void modelUpdated(MolModel scene, int opType, Object opValue) {
        if ((opType & ATOM_ADDED) != 0) {
            Atom a = (Atom) opValue;
            BranchGroup childGroup = new BranchGroup();
            addAtomView(a, childGroup);
            childGroup.compile();
            rootGroup.addChild(childGroup);
        } else if ((opType & FILE_ADDED) != 0) {
            MoleculeFile mf = (MoleculeFile) opValue;
            Collection c = mf.getAtoms();
            Iterator i = c.iterator();
            BranchGroup fileGroup = new BranchGroup();
            fileGroup.setCapability(BranchGroup.ALLOW_DETACH);
            fileGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
            fileGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
            fileGroup.clearCapabilityIsFrequent(BranchGroup.ALLOW_DETACH);
            fileGroup.clearCapabilityIsFrequent(BranchGroup.ALLOW_CHILDREN_WRITE);
            fileGroup.clearCapabilityIsFrequent(BranchGroup.ALLOW_CHILDREN_EXTEND);
            fileMap.put(opValue, fileGroup);
            while (i.hasNext()) {
                Atom a = (Atom) i.next();
                addAtomView(a, fileGroup);
            }
            fileGroup.addChild(mf.getAdditionalData());
            fileGroup.compile();
            rootGroup.addChild(fileGroup);
        } else if ((opType & FILE_REMOVED) != 0) {
            BranchGroup g = (BranchGroup) fileMap.get(opValue);
            g.detach();
        } else if ((opType & BOND_GROUP_ADDED) != 0) {
            BranchGroup bondGroup = new BranchGroup();
            bondGroup.setCapability(BranchGroup.ALLOW_DETACH);
            bondGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
            bondGroup.clearCapabilityIsFrequent(BranchGroup.ALLOW_DETACH);
            bondGroup.clearCapabilityIsFrequent(BranchGroup.ALLOW_CHILDREN_WRITE);
            MoleculeFile owner = null;
            Collection l = (Collection) opValue;
            if (l.size() < 1) {
                return;
            }
            Iterator i = l.iterator();
            while (i.hasNext()) {
                Bond b = (Bond) i.next();
                if (owner == null) {
                    owner = b.getOwner();
                }
                connectBond(b, bondGroup);
            }
            bondGroup.compile();
            BranchGroup g = (BranchGroup) fileMap.get(owner);
            g.addChild(bondGroup);
        } else if ((opType & this.BOND_ADDED) != 0) {
            Bond b = (Bond) opValue;
            Logger.writeLog("Adding " + b + " to scene.");
            connectBond(b, (BranchGroup) fileMap.get(b.getOwner()));
        } else if ((opType & BOND_REMOVED) != 0) {
            BondView bv = BondView.bondToView(opValue);
            if (bv != null) {
                bv.removeFromScene();
                BranchGroup g = (BranchGroup) AttachPoints.bondMap.get(bv);
                AttachPoints.bondMap.remove(opValue);
                if (g != null) g.detach();
            } else {
                Logger.writeLog("huh?");
            }
        } else if ((opType & ATOM_REMOVED) != 0) {
            AtomView av = AtomView.atomToView((Atom) opValue);
            av.removeFromScene();
        }
    }

    protected void connectBond(Bond b, Group root) {
        Vector3d start = b.getStart().getLocation();
        Vector3d end = b.getEnd().getLocation();
        AttachPoints.attach(start.x, start.y, start.z, end.x, end.y, end.z, root, b.getStart(), b.getEnd(), b);
    }
}
