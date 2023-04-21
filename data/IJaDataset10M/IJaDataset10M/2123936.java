package j3dworkbench.j3dextensions;

import j3dworkbench.core.J3DWorkbenchConstants;
import j3dworkbench.core.J3DWorkbenchUtility;
import j3dworkbench.proxy.BehaviorProxy;
import j3dworkbench.proxy.NodeProxy;
import j3dworkbench.proxy.RotationInterpolatorProxy;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import com.sun.j3d.utils.scenegraph.io.SceneGraphIO;
import com.sun.j3d.utils.scenegraph.io.SceneGraphObjectReferenceControl;

public class CloneFactory extends ElapsedTimeBehavior implements SceneGraphIO {

    public static final int INDEX_OF_ROT_INTERP_BG = 1;

    private static final int INDEX_OF_PROTOTYPE = 0;

    private static final int INDEX_OF_SWITCH = 1;

    private static final int INDEX_OF_CLONES = 2;

    private transient Map<Long, NodeProxy> clones = new TreeMap<Long, NodeProxy>();

    private long lifespan = 10000;

    private long millisPerClone = 1000;

    private int maxClones = -1;

    private int numClonesProduced;

    private final Transform3D transTemp = new Transform3D();

    private transient RotationInterpolator rotInterp;

    private transient BranchGroup clonesBranch = null;

    private transient boolean mutexStopPending = false;

    private transient volatile boolean pause;

    private transient long timestamp;

    private static final int VERSION = 1;

    public int getMaxClones() {
        return maxClones;
    }

    public void setMaxClones(int maxClones) {
        this.maxClones = maxClones;
    }

    public CloneFactory() {
        super(1000);
    }

    public CloneFactory(long l) {
        super(l);
    }

    public void initInternals() {
        Group g = (Group) getParent();
        Switch swit = new Switch();
        swit.setCapability(Group.ALLOW_CHILDREN_WRITE);
        swit.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        swit.setWhichChild(Switch.CHILD_NONE);
        g.addChild(swit);
        resetClonesBranch();
        RotationInterpolatorProxy rotProx = RotationInterpolatorProxy.createInstance();
        rotInterp = rotProx.getJ3DNode();
        rotInterp.setSchedulingBounds(getSchedulingBounds());
        rotInterp.setTarget(rotProx.getTransformGroup());
        getParentBranchGroup().addChild(rotProx.getBranchGroup());
    }

    private void processClonesLifeCycle() {
        if (lifespan == -1 || clones.isEmpty()) {
            return;
        }
        Iterator<Long> iterator = clones.keySet().iterator();
        long birth = 0;
        NodeProxy prox = null;
        while (iterator.hasNext()) {
            birth = iterator.next();
            if (System.nanoTime() > birth + (lifespan * J3DWorkbenchConstants.NANOS_PER_MILLI)) {
                prox = clones.get(birth);
                iterator.remove();
                prox.unSelected(false);
                prox.detach(true, false);
            } else {
                return;
            }
        }
    }

    @Override
    protected void doWork() {
        synchronized (this) {
            if (pause || !getEnable()) {
                return;
            }
            if (mutexStopPending == true) {
                super.setEnable(false);
                mutexStopPending = false;
                return;
            }
        }
        processClonesLifeCycle();
        if (numClonesProduced == maxClones) {
            if (clones.isEmpty()) {
                J3DWorkbenchUtility.findProxyNode(this).setEnabled(false);
            }
            return;
        }
        if (System.nanoTime() - timestamp < (millisPerClone * J3DWorkbenchConstants.NANOS_PER_MILLI)) {
            return;
        }
        NodeProxy clonedProx = null;
        try {
            clonedProx = J3DWorkbenchUtility.findProxyNode(getPrototype()).copy();
        } catch (Exception ex) {
            setEnable(false);
            return;
        }
        if (clonedProx.isTriggeredByParent()) {
            clonedProx.setEnabled(true);
        }
        numClonesProduced++;
        if (rotInterp.getEnable()) {
            rotInterp.getTarget().getTransform(transTemp);
            transTemp.setTranslation(clonedProx.getPosition());
            clonedProx.setTransformQuietly(transTemp);
        }
        BranchGroup cbranch = getClonesBranch();
        cbranch.addChild(clonedProx.getBranchGroup());
        timestamp = System.nanoTime();
        clones.put(timestamp, clonedProx);
    }

    /**
	 * 
	 * @return the BranchGroup holding the active clones
	 */
    public BranchGroup getClonesBranch() {
        return (BranchGroup) ((Group) getParent()).getChild(INDEX_OF_CLONES);
    }

    /**
	 * 
	 * @return the BranchGroup of the 'prototype'
	 */
    public BranchGroup getPrototype() {
        return (BranchGroup) getSwitch().getChild(INDEX_OF_PROTOTYPE);
    }

    /**
	 * @return the Switch holding the invisible 'prototype' to be cloned
	 */
    public Switch getSwitch() {
        return (Switch) ((Group) getParent()).getChild(INDEX_OF_SWITCH);
    }

    /**
	 * Returns a reference to the NodeClonerBehaviorProxy BG<br>
	 * BG --> TG --> this
	 */
    private BranchGroup getParentBranchGroup() {
        return (BranchGroup) getParent().getParent();
    }

    @Override
    public void setEnable(boolean state) {
        if (getEnable() == state) {
            return;
        }
        if (state) {
            super.setEnable(true);
            return;
        }
        synchronized (this) {
            mutexStopPending = true;
        }
    }

    public void reset() {
        synchronized (this) {
            if (getEnable()) {
                setEnable(false);
            }
            cleanUp();
        }
    }

    private void cleanUp() {
        Set<Long> keys = clones.keySet();
        NodeProxy prox = null;
        for (Iterator<Long> iterator = keys.iterator(); iterator.hasNext(); ) {
            prox = clones.get(iterator.next());
            prox.detach(true, false);
        }
        clones.clear();
        numClonesProduced = 0;
    }

    public long getLifeSpan() {
        return lifespan;
    }

    public void setLifeSpan(long l) {
        lifespan = l;
        setWakeUpTime();
    }

    @Override
    public void duplicateNode(Node originalNode, boolean forceDuplicate) {
        super.duplicateNode(originalNode, forceDuplicate);
        CloneFactory ncb = (CloneFactory) originalNode;
        lifespan = ncb.lifespan;
        maxClones = ncb.maxClones;
        setWakeUpTime();
    }

    private void setWakeUpTime() {
        if (lifespan == -1) {
            super.setMillisPerFrame(millisPerClone);
            return;
        }
        super.setMillisPerFrame(Math.min(millisPerClone, lifespan));
    }

    @Override
    public Node cloneNode(boolean forceDuplicate) {
        CloneFactory ncb = new CloneFactory(getMillisPerFrame());
        ncb.duplicateNode(this, forceDuplicate);
        return ncb;
    }

    public void createSceneGraphObjectReferences(SceneGraphObjectReferenceControl ref) {
    }

    public void readSceneGraphObject(DataInput in) throws IOException {
        in.readUnsignedByte();
        maxClones = in.readInt();
        millisPerClone = in.readLong();
        lifespan = in.readLong();
        setWakeUpTime();
    }

    public void restoreSceneGraphObjectReferences(SceneGraphObjectReferenceControl ref) {
        resetClonesBranch();
        getRotationInterpolator();
    }

    /**
	 * Cannot be called during clone operation.
	 * 
	 * @return the internal {@link RotationInterpolator}
	 */
    public RotationInterpolator getRotationInterpolator() {
        if (rotInterp == null) {
            BranchGroup bg = (BranchGroup) getParentBranchGroup().getChild(INDEX_OF_ROT_INTERP_BG);
            rotInterp = (RotationInterpolator) ((TransformGroup) bg.getChild(0)).getChild(0);
        }
        return rotInterp;
    }

    public boolean saveChildren() {
        return false;
    }

    public void writeSceneGraphObject(DataOutput out) throws IOException {
        out.write(VERSION);
        out.writeInt(maxClones);
        out.writeLong(millisPerClone);
        out.writeLong(lifespan);
    }

    private void resetClonesBranch() {
        BranchGroup l_branch = new BranchGroup();
        l_branch.setCapability(Group.ALLOW_CHILDREN_WRITE);
        l_branch.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        l_branch.setCapability(BranchGroup.ALLOW_DETACH);
        ((Group) getParent()).addChild(l_branch);
    }

    public void preSave() {
        synchronized (this) {
            pause = true;
        }
        clonesBranch = getClonesBranch();
        clonesBranch.detach();
    }

    public void postSave() {
        ((Group) getParent()).addChild(clonesBranch);
        clonesBranch = null;
        synchronized (this) {
            pause = false;
        }
    }

    public long getMillisPerClone() {
        return millisPerClone;
    }

    public void setMillisPerClone(long ms) {
        millisPerClone = ms;
        setWakeUpTime();
    }
}
