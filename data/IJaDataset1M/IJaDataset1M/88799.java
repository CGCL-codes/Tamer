package sc.document;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Hashtable;
import fluid.FluidRuntimeException;
import fluid.ir.Bundle;
import fluid.ir.IRInput;
import fluid.ir.IRNode;
import fluid.ir.IRNodeType;
import fluid.ir.IROutput;
import fluid.ir.IRPersistent;
import fluid.ir.PlainIRNode;
import fluid.ir.Slot;
import fluid.ir.SlotInfo;
import fluid.tree.SymmetricEdgeDigraph;
import fluid.util.UniqueID;
import fluid.version.Era;
import fluid.version.Version;
import fluid.version.VersionedChunk;
import fluid.version.VersionedRegion;
import fluid.version.VersionedSlotFactory;

public class GoalModel extends Component {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5108637166200388114L;

    private static final int magic = 0x474F414C;

    protected VersionedRegion region;

    public static final SymmetricEdgeDigraph graph = GoalModelGraphBundle.graph;

    private static Hashtable<String, Integer> elementCounts = new Hashtable<String, Integer>();

    public static Bundle goalModelGraphBundle = GoalModelGraphBundle.getBundle();

    public static Bundle goalModelAttrBundle = GoalModelAttrBundle.getBundle();

    private static SlotInfo gmNameAttr = GoalModelAttrBundle.name_si;

    private static Hashtable<VersionedRegion, GoalModel> region2GoalModelTable = new Hashtable<VersionedRegion, GoalModel>();

    private static int BRANCH_FACTOR = 0;

    private static final int HARDGOAL = 0;

    private static final int SOFTGOAL = 1;

    private static final int EDGE = 2;

    public static final int PLUS = 0;

    public static final int PLUSPLUS = 1;

    public static final int MINUS = 2;

    public static final int MINUSMINUS = 3;

    public static final int AND = 4;

    public static final int OR = 5;

    private Slot rootSlot;

    protected static final int ROOTBIT = 1;

    /** Existing one */
    public GoalModel(UniqueID id) {
        super(magic, id);
        region = null;
        rootSlot = VersionedSlotFactory.prototype.predefinedSlot(null);
    }

    /** Totally new one */
    public GoalModel() {
        super(magic, true);
        clearCount();
        region = new VersionedRegion();
        region2GoalModelTable.put(region, this);
    }

    public void setRoot(IRNode root) {
        rootSlot = VersionedSlotFactory.prototype.predefinedSlot(null);
        rootSlot = rootSlot.setValue(root);
    }

    public Bundle getGraphBundle() {
        return goalModelGraphBundle;
    }

    public Bundle getAttrBundle() {
        return goalModelAttrBundle;
    }

    /** Get the pseudo root of this graph */
    public IRNode getRoot() {
        if (rootSlot.isValid()) return (IRNode) rootSlot.getValue(); else return null;
    }

    public ComponentFactory getFactory() {
        return Factory.prototype;
    }

    public String getFileName() {
        return this.getID() + ".gmo";
    }

    /** Get the region of the nodes */
    public VersionedRegion getRegion() {
        return region;
    }

    /** get the graph */
    public SymmetricEdgeDigraph getGraph() {
        return graph;
    }

    /**
  	 * Load the versioned region
  	 */
    protected void loadRegion(fluid.util.FileLocator floc) {
        try {
            if (region == null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(floc.openFileRead(this.getID().toString() + ".region")));
                String region_name = br.readLine();
                UniqueID id = UniqueID.parseUniqueID(region_name);
                region = VersionedRegion.loadVersionedRegion(id, floc);
                region2GoalModelTable.put(region, this);
            } else if ((region != null) && (!region.isDefined())) region.load(floc);
        } catch (IOException e) {
            System.err.println("Exception occured: " + e);
            e.printStackTrace();
        }
    }

    /**
  	 * Save the region
  	 */
    protected void saveRegion(fluid.util.FileLocator floc) {
        if (region.isStored() == false) {
            try {
                region.store(floc);
                DataOutputStream os = new DataOutputStream(floc.openFileWrite(this.getID().toString() + ".region"));
                PrintWriter w = new PrintWriter(os);
                w.write(region.getID().toString());
                w.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** loadDelta */
    public void loadDelta(Era era, fluid.util.FileLocator floc) throws IOException {
        loadRegion(floc);
        VersionedChunk vc = VersionedChunk.get(region, goalModelGraphBundle);
        vc.getDelta(era).load(floc);
        vc = VersionedChunk.get(region, goalModelAttrBundle);
        vc.getDelta(era).load(floc);
    }

    /** saveDelta */
    public void saveDelta(Era era, fluid.util.FileLocator floc) throws IOException {
        saveRegion(floc);
        VersionedChunk ch = VersionedChunk.get(region, goalModelGraphBundle);
        IRPersistent vcd = ch.getDelta(era);
        vcd.store(floc);
        ch = VersionedChunk.get(region, goalModelAttrBundle);
        vcd = ch.getDelta(era);
        vcd.store(floc);
    }

    /** Load the snapshot of this component for the given version. */
    public void loadSnapshot(Version v, fluid.util.FileLocator floc) throws IOException {
        loadRegion(floc);
        VersionedChunk vc = VersionedChunk.get(region, goalModelGraphBundle);
        ((IRPersistent) vc.getSnapshot(v)).load(floc);
        vc = VersionedChunk.get(region, goalModelAttrBundle);
        ((IRPersistent) vc.getSnapshot(v)).load(floc);
    }

    /** Store a snapshot of this component for the given version. */
    public void saveSnapshot(Version v, fluid.util.FileLocator floc) throws IOException {
        saveRegion(floc);
        VersionedChunk ch = VersionedChunk.get(region, goalModelGraphBundle);
        IRPersistent vcs = ch.getSnapshot(v);
        vcs.store(floc);
        ch = VersionedChunk.get(region, goalModelAttrBundle);
        vcs = ch.getSnapshot(v);
        vcs.store(floc);
    }

    public void writeContents(IROutput out) throws IOException {
        super.writeContents(out);
        int flags = 0;
        if (rootSlot.isValid()) flags |= ROOTBIT;
        out.writeByte(flags);
        if ((flags & ROOTBIT) != 0) rootSlot.writeValue(IRNodeType.prototype, out);
        region.writeReference(out);
    }

    public void readContents(IRInput in) throws IOException {
        super.readContents(in);
        int flags = in.readByte();
        if ((flags & ROOTBIT) != 0) {
            rootSlot = rootSlot.readValue(IRNodeType.prototype, in);
        }
        region = (VersionedRegion) in.readPersistentReference();
        if (region == null) throw new FluidRuntimeException("region is null"); else {
            region2GoalModelTable.put(region, this);
        }
    }

    public void writeChangedContents(IROutput out) throws IOException {
        super.writeChangedContents(out);
        int flags = 0;
        if (rootSlot.isChanged()) flags |= ROOTBIT;
        out.writeByte(flags);
        if ((flags & ROOTBIT) != 0) rootSlot.writeValue(IRNodeType.prototype, out);
    }

    public void readChangedContents(IRInput in) throws IOException {
        super.readChangedContents(in);
        int flags = in.readByte();
        if ((flags & ROOTBIT) != 0) {
            rootSlot = rootSlot.readValue(IRNodeType.prototype, in);
        }
    }

    public boolean isChanged() {
        return super.isChanged() || rootSlot.isChanged();
    }

    /** Return the name of an entity in this GM */
    public String getGMNodeName(IRNode gmnode) {
        if (gmnode.valueExists(gmNameAttr)) return (String) gmnode.getSlotValue(gmNameAttr); else return null;
    }

    /** Set name for an entity (node or edge) */
    private void setGMNodeName(IRNode gmnode, String name) {
        gmnode.setSlotValue(gmNameAttr, name);
    }

    /** Set name for a hard goal node */
    public void setHardGoalName(IRNode node, String name) {
        setGMNodeName(node, "HardGoal_" + name);
        if (!name.equals("virtual")) {
            elementCounts.put("modify-node", elementCounts.get("modify-node") + 1);
        }
    }

    /** Set name for a soft goal node */
    public void setSoftGoalName(IRNode node, String name) {
        setGMNodeName(node, "SoftGoal_" + name);
        if (!name.equals("virtual")) {
            elementCounts.put("modify-node", elementCounts.get("modify-node") + 1);
        }
    }

    /** Get the type of a GMNode */
    public int getGMNodeType(IRNode gmnode) {
        String name = getGMNodeName(gmnode);
        if (name == null) return -1;
        if (name.startsWith("HardGoal_")) return HARDGOAL;
        if (name.startsWith("SoftGoal_")) return SOFTGOAL;
        if (name.startsWith("GMEdge_")) return EDGE;
        return -1;
    }

    /** Do not check an existing name yet */
    public IRNode createAGoal(String name, boolean hardgoal) {
        if (region == null) return null;
        PlainIRNode node = new PlainIRNode(region);
        if (hardgoal) setHardGoalName(node, name); else setSoftGoalName(node, name);
        graph.initNode(node, ~BRANCH_FACTOR, ~BRANCH_FACTOR);
        elementCounts.put("add-node", elementCounts.get("add-node") + 1);
        return node;
    }

    /** @name: either AND, OR, +, -, ++, -- */
    public IRNode createEdge(String name) {
        if (region == null) return null;
        PlainIRNode edgenode = new PlainIRNode(region);
        setGMNodeName(edgenode, "GMEdge_" + name);
        graph.initEdge(edgenode);
        if (!name.equals("virtual")) elementCounts.put("add-edge", elementCounts.get("add-edge") + 1);
        return edgenode;
    }

    /** Change the type of an edge */
    public void setEdgeType(IRNode edgenode, int type) {
        String name;
        switch(type) {
            case AND:
                name = "GMEdge_AND";
                break;
            case OR:
                name = "GMEdge_OR";
                break;
            case PLUS:
                name = "GMEdge_+";
                break;
            case MINUS:
                name = "GMEdge_-";
                break;
            case PLUSPLUS:
                name = "GMEdge_++";
                break;
            case MINUSMINUS:
                name = "GMEdge_--";
                break;
            default:
                return;
        }
        setGMNodeName(edgenode, name);
        elementCounts.put("modify-edge", elementCounts.get("modify-edge") + 1);
    }

    /** Connect from n1 to n2 */
    public void connect(IRNode n1, IRNode n2, IRNode edgenode) {
        graph.setSource(edgenode, n1);
        graph.setSink(edgenode, n2);
        elementCounts.put("modify-edge", elementCounts.get("modify-edge") + 1);
    }

    /** Delete an edge */
    public boolean deleteAnEdge(IRNode edgenode) {
        if (edgenode == null) return false;
        graph.setSource(edgenode, null);
        graph.setSink(edgenode, null);
        elementCounts.put("delete-edge", elementCounts.get("delete-edge") + 1);
        return true;
    }

    /** Remove a goal and all of its connecting edges */
    public boolean deleteAGoal(IRNode node) {
        if (node == null) return false;
        int i = graph.numChildren(node);
        int j = graph.numParents(node);
        if (i > 1) i--;
        if (j > 1) j--;
        elementCounts.put("delete-edge", elementCounts.get("delete-edge") + i + j);
        elementCounts.put("delete-node", elementCounts.get("delete-node") + 1);
        graph.removeNode(node);
        return true;
    }

    public void printChildrenName(IRNode node) {
        int numChildren = graph.numChildren(node);
        for (int i = 0; i < numChildren; i++) {
            IRNode edgenode = graph.getChildEdge(node, i);
            IRNode childnode = graph.getChild(node, i);
            String nodename = getGMNodeName(childnode);
            String edgetype = getGMNodeName(edgenode);
            System.out.println("Child number " + i + " is " + nodename + "; connected by " + edgetype);
        }
    }

    public void traverseGM(IRNode node, IRNode edgenode) {
        if (node == null) return;
        String nodename = getGMNodeName(node);
        if (edgenode == null) {
            System.out.println(nodename);
        } else {
        }
        int numChildren = graph.numChildren(node);
        for (int i = 0; i < numChildren; i++) {
            IRNode childedgenode = graph.getChildEdge(node, i);
            IRNode childnode = graph.getChild(node, i);
            traverseGM(childnode, childedgenode);
        }
    }

    /**
     * @author nernst
     * Clear the diff statistics Hashtable
     */
    public static void clearCount() {
        elementCounts.put("delete-edge", new Integer(0));
        elementCounts.put("delete-node", new Integer(0));
        elementCounts.put("add-node", new Integer(0));
        elementCounts.put("add-edge", new Integer(0));
        elementCounts.put("modify-node", new Integer(0));
        elementCounts.put("modify-edge", new Integer(0));
    }

    /**
     * @author nernst
     * @return String - a string repr of the counts of various elements
     */
    public static String getCount() {
        String s = elementCounts.toString();
        return s;
    }

    public IRNode getIthChild(IRNode node, int i) {
        if (node == null) return null; else {
            IRNode c = graph.getChild(node, i);
            return c;
        }
    }

    private static class Factory extends ComponentFactory {

        public static final Factory prototype = new Factory();

        private Factory() {
            super();
        }

        /** Create Goal Model instance from the input */
        public Component create(UniqueID id, DataInput in) {
            GoalModel goalmodel = new GoalModel(id);
            return goalmodel;
        }
    }
}
