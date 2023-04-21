package sc.document;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;
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

/**
 * @author Tien
 */
public class SCHypertext extends Component {

    private static final int magic = 0x48595054;

    protected VersionedRegion region;

    public static final SymmetricEdgeDigraph graph = HypertextGraphBundle.graph;

    public static final Bundle hypertextGraphBundle = HypertextGraphBundle.getBundle();

    private static Bundle hypertextAttrBundle = HypertextAttrBundle.getBundle();

    private static SlotInfo htNameAttr = HypertextAttrBundle.name_si;

    private static SlotInfo htNodeAttr = HypertextAttrBundle.nodeattr_si;

    private static SlotInfo htDestDocAttr = HypertextAttrBundle.destdoc_si;

    private static Hashtable region2HypertextTable = new Hashtable();

    private static int BRANCH_FACTOR = 0;

    private static int ANCHOR = 0;

    private static int CAUSAL_LINK = 1;

    private static int NON_CAUSAL = 2;

    private static int EDGE = 3;

    private static int HTROOT = 4;

    private Slot rootSlot;

    protected static final int ROOTBIT = 1;

    /** Existing one */
    public SCHypertext(UniqueID id) {
        super(magic, id);
        region = null;
        rootSlot = VersionedSlotFactory.prototype.predefinedSlot(null);
    }

    /** Totally new one */
    public SCHypertext() {
        super(magic, true);
        region = new VersionedRegion();
        region2HypertextTable.put(region, this);
        IRNode root = new PlainIRNode(region);
        graph.initNode(root, ~0, ~0);
        setHTNodeName(root, "SCHTRoot");
        rootSlot = VersionedSlotFactory.prototype.predefinedSlot(null);
        rootSlot = rootSlot.setValue(root);
    }

    /** Get the pseudo root of this HT */
    public IRNode getRoot() {
        if (rootSlot.isValid()) return (IRNode) rootSlot.getValue(); else return null;
    }

    public ComponentFactory getFactory() {
        return Factory.prototype;
    }

    public String getFileName() {
        return this.getID() + ".hyp";
    }

    /** Get the region of the hypertext nodes */
    public VersionedRegion getRegion() {
        return region;
    }

    /**
	 * Load the versioned region of this hypertext
	 */
    protected void loadRegion(fluid.util.FileLocator floc) {
        try {
            if (region == null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(floc.openFileRead(this.getID().toString() + ".region")));
                String region_name = br.readLine();
                UniqueID id = UniqueID.parseUniqueID(region_name);
                region = VersionedRegion.loadVersionedRegion(id, floc);
                region2HypertextTable.put(region, this);
            } else if ((region != null) && (!region.isDefined())) region.load(floc);
        } catch (IOException e) {
            System.err.println("Exception occured: " + e);
            e.printStackTrace();
        }
    }

    /**
	 * Save the region of the hypertext
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
        VersionedChunk vc = VersionedChunk.get(region, hypertextGraphBundle);
        vc.getDelta(era).load(floc);
        vc = VersionedChunk.get(region, hypertextAttrBundle);
        vc.getDelta(era).load(floc);
        vc.describe(System.out);
    }

    /** saveDelta */
    public void saveDelta(Era era, fluid.util.FileLocator floc) throws IOException {
        saveRegion(floc);
        VersionedChunk ch = VersionedChunk.get(region, hypertextGraphBundle);
        IRPersistent vcd = ch.getDelta(era);
        vcd.store(floc);
        ch = VersionedChunk.get(region, hypertextAttrBundle);
        vcd = ch.getDelta(era);
        vcd.store(floc);
    }

    /** Load the snapshot of this component for the given version. */
    public void loadSnapshot(Version v, fluid.util.FileLocator floc) throws IOException {
        loadRegion(floc);
        VersionedChunk vc = VersionedChunk.get(region, hypertextGraphBundle);
        ((IRPersistent) vc.getSnapshot(v)).load(floc);
        System.out.println("Loading snapshot for HYPERTEXT NODE ATTRIBUTE ...");
        vc = VersionedChunk.get(region, hypertextAttrBundle);
        ((IRPersistent) vc.getSnapshot(v)).load(floc);
        vc.describe(System.out);
    }

    /** Store a snapshot of this component for the given version. */
    public void saveSnapshot(Version v, fluid.util.FileLocator floc) throws IOException {
        saveRegion(floc);
        VersionedChunk ch = VersionedChunk.get(region, hypertextGraphBundle);
        IRPersistent vcs = ch.getSnapshot(v);
        vcs.store(floc);
        ch = VersionedChunk.get(region, hypertextAttrBundle);
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
            region2HypertextTable.put(region, this);
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

    /** Return an anchor node for a docnode if existing */
    public IRNode docNode2AnchorNode(IRNode docnode) {
        IRNode root = this.getRoot();
        int numChildren = graph.numChildren(root);
        for (int i = 0; i < numChildren; i++) {
            IRNode htnode = graph.getChild(root, i);
            int nodeType = this.getHTNodeType(htnode);
            if (nodeType == ANCHOR) {
                if (getDocumentNode(htnode).equals(docnode)) {
                    return htnode;
                }
            }
        }
        return null;
    }

    /** Return the name of an HT node */
    public String getHTNodeName(IRNode htnode) {
        if (htnode.valueExists(htNameAttr)) return (String) htnode.getSlotValue(htNameAttr); else return null;
    }

    /** Set name for an HT node (either an anchor or a link) */
    private void setHTNodeName(IRNode htnode, String name) {
        htnode.setSlotValue(htNameAttr, name);
    }

    /** Set name for a link */
    public void setLinkName(SCLink link, String name) {
        if (link instanceof SCCausalLink) setHTNodeName(link.getIRNode(), "SCCausalLink_" + name);
        if (link instanceof SCNonCausalLink) setHTNodeName(link.getIRNode(), "SCNonCausalLink_" + name);
    }

    /** Set name for an anchor */
    public void setAnchorName(SCAnchor anchor, String name) {
        setHTNodeName(anchor.getIRNode(), "SCAnchor_" + name);
    }

    /** Does an HT Node belong to this hypertext */
    public boolean hasHTNode(IRNode htnode) {
        IRNode root = this.getRoot();
        int numChildren = graph.numChildren(root);
        for (int i = 0; i < numChildren; i++) {
            IRNode child = graph.getChild(root, i);
            int nodeType = this.getHTNodeType(child);
            if (nodeType == CAUSAL_LINK || nodeType == NON_CAUSAL || nodeType == ANCHOR) {
                if (child.equals(htnode)) return true;
            }
        }
        return false;
    }

    /** Get the type (anchor, causal link, non-causal link) of an HTNode */
    public int getHTNodeType(IRNode htnode) {
        String name = getHTNodeName(htnode);
        if (name == null) return -1;
        if (name.startsWith("SCAnchor_")) return ANCHOR;
        if (name.startsWith("SCCausalLink_")) return CAUSAL_LINK;
        if (name.startsWith("SCNonCausalLink_")) return NON_CAUSAL;
        if (name.startsWith("SCEdge_")) return EDGE;
        if (name.startsWith("SCHTRoot")) return HTROOT;
        return -1;
    }

    /** Get all of link nodes (IRNodes) contained in 
   *  this hypertext at the current version
	 */
    public Vector getLinkNodes() {
        IRNode root = this.getRoot();
        int numChildren = graph.numChildren(root);
        Vector links = new Vector();
        for (int i = 0; i < numChildren; i++) {
            IRNode htnode = graph.getChild(root, i);
            int nodeType = this.getHTNodeType(htnode);
            if (nodeType == CAUSAL_LINK || nodeType == NON_CAUSAL) {
                links.addElement(htnode);
            }
        }
        return links;
    }

    /** Get all of links contained in this hypertext at 
   *  current version
	 */
    public Vector getLinks() {
        IRNode root = this.getRoot();
        int numChildren = graph.numChildren(root);
        Vector links = new Vector();
        for (int i = 0; i < numChildren; i++) {
            IRNode htnode = graph.getChild(root, i);
            int nodeType = this.getHTNodeType(htnode);
            if (nodeType == CAUSAL_LINK) {
                SCCausalLink l = new SCCausalLink(htnode, this);
                links.addElement(l);
            } else if (nodeType == NON_CAUSAL) {
                SCNonCausalLink l = new SCNonCausalLink(htnode, this);
                links.addElement(l);
            }
        }
        return links;
    }

    /** Get all of anchor nodes (IRNodes) 
	 *  contained in this hypertext at the current version
	 */
    public Vector getAnchorNodes() {
        IRNode root = this.getRoot();
        int numChildren = graph.numChildren(root);
        Vector anchors = new Vector();
        for (int i = 0; i < numChildren; i++) {
            IRNode htnode = graph.getChild(root, i);
            int nodeType = this.getHTNodeType(htnode);
            if (nodeType == ANCHOR) {
                anchors.addElement(htnode);
            }
        }
        return anchors;
    }

    /** Get all of anchors 
	 *  contained in this hypertext at the current version
	 */
    public Vector getAnchors() {
        IRNode root = this.getRoot();
        int numChildren = graph.numChildren(root);
        Vector anchors = new Vector();
        for (int i = 0; i < numChildren; i++) {
            IRNode htnode = graph.getChild(root, i);
            int nodeType = this.getHTNodeType(htnode);
            if (nodeType == ANCHOR) {
                SCAnchor anchor = new SCAnchor(htnode, this);
                anchors.addElement(anchor);
            }
        }
        return anchors;
    }

    /** Get all of causal link nodes (IRNodes) 
	 *  contained in this hypertext at 
	 *  the current version
	 */
    public Vector getCausalLinkNodes() {
        IRNode root = this.getRoot();
        int numChildren = graph.numChildren(root);
        Vector links = new Vector();
        for (int i = 0; i < numChildren; i++) {
            IRNode htnode = graph.getChild(root, i);
            int nodeType = this.getHTNodeType(htnode);
            if (nodeType == CAUSAL_LINK) {
                links.addElement(htnode);
            }
        }
        return links;
    }

    /** Get all of causal links contained in this hypertext 
	 *  at the current version
	 */
    public Vector getCausalLinks() {
        IRNode root = this.getRoot();
        int numChildren = graph.numChildren(root);
        Vector links = new Vector();
        for (int i = 0; i < numChildren; i++) {
            IRNode htnode = graph.getChild(root, i);
            int nodeType = this.getHTNodeType(htnode);
            if (nodeType == CAUSAL_LINK) {
                SCCausalLink l = new SCCausalLink(htnode, this);
                links.addElement(l);
            }
        }
        return links;
    }

    /** Get all of non-causal link nodes (IRNodes) 
   *  contained in this hypertext at 
	 *  the current version
	 */
    public Vector getNonCausalLinkNodes() {
        IRNode root = this.getRoot();
        int numChildren = graph.numChildren(root);
        Vector links = new Vector();
        for (int i = 0; i < numChildren; i++) {
            IRNode htnode = graph.getChild(root, i);
            int nodeType = this.getHTNodeType(htnode);
            if (nodeType == NON_CAUSAL) {
                links.addElement(htnode);
            }
        }
        return links;
    }

    /** Get all of non-causal links contained in this hypertext 
	 *  at the current version
	 */
    public Vector getNonCausalLinks() {
        IRNode root = this.getRoot();
        int numChildren = graph.numChildren(root);
        Vector links = new Vector();
        for (int i = 0; i < numChildren; i++) {
            IRNode htnode = graph.getChild(root, i);
            int nodeType = this.getHTNodeType(htnode);
            if (nodeType == NON_CAUSAL) {
                SCNonCausalLink l = new SCNonCausalLink(htnode, this);
                links.addElement(l);
            }
        }
        return links;
    }

    /** Get the number of links contained in this hypertext
	 */
    public int numberOfLinks() {
        Vector links = this.getLinkNodes();
        return links.size();
    }

    /** Get the number of causal links contained in this hypertext
	 */
    public int numberOfCausalLinks(Version v) {
        Vector links = this.getCausalLinkNodes();
        return links.size();
    }

    /** Get the number of non-causal links contained in this hypertext
	 */
    public int numberOfNonCausalLinks(Version v) {
        Vector links = this.getNonCausalLinkNodes();
        return links.size();
    }

    /** Returns the number of anchors contained in this hypertext */
    public int numberOfAnchors(Version v) {
        return this.getAnchorNodes().size();
    }

    public SCAnchor createAnchor(SCDocument destdoc, IRNode docnode, String name) {
        if (docNode2AnchorNode(docnode) == null) {
            if (region == null) return null;
            PlainIRNode anchornode = new PlainIRNode(region);
            setHTNodeName(anchornode, "SCAnchor_" + name);
            graph.initNode(anchornode, ~BRANCH_FACTOR, ~BRANCH_FACTOR);
            SCAnchor anchor = new SCAnchor(anchornode, this);
            setDocumentNode(anchor, docnode);
            setDestDocument(anchor, destdoc);
            SCEdge edge = createEdge();
            if (edge == null) return null;
            graph.setSource(edge.getIRNode(), this.getRoot());
            graph.setSink(edge.getIRNode(), anchornode);
            return anchor;
        } else {
            IRNode n = (IRNode) docNode2AnchorNode(docnode);
            return new SCAnchor(n, this);
        }
    }

    /** Return the document IR node that an anchor points to */
    public IRNode getDocumentNode(SCAnchor anchor) {
        if (anchor.getIRNode().valueExists(htNodeAttr)) return (IRNode) anchor.getIRNode().getSlotValue(htNodeAttr); else return null;
    }

    /** Return the document IR node that an anchor node points to */
    public IRNode getDocumentNode(IRNode anchornode) {
        if (anchornode.valueExists(htNodeAttr)) return (IRNode) anchornode.getSlotValue(htNodeAttr); else return null;
    }

    /** Associate an anchor to an IRNode. */
    public void setDocumentNode(SCAnchor anchor, IRNode docnode) {
        anchor.getIRNode().setSlotValue(htNodeAttr, docnode);
    }

    /** Check if the docnode already has an anchor defined on it */
    public boolean anchorExists(IRNode docnode) {
        if (docNode2AnchorNode(docnode) != null) return true; else return false;
    }

    /** Associate an anchor to a (SCDocument,IRNode) */
    public void setDestDocument(SCAnchor anchor, SCDocument destdoc) {
        anchor.getIRNode().setSlotValue(htDestDocAttr, destdoc);
    }

    /** Return the dest doc that anchor node points to */
    public SCDocument getDestDocument(SCAnchor anchor) {
        if (anchor.getIRNode().valueExists(htDestDocAttr)) return (SCDocument) (anchor.getIRNode().getSlotValue(htDestDocAttr));
        return null;
    }

    /** Delete the specified anchor in this hypertext
   *  The anchor is removed from any links which contain it    
   */
    public boolean deleteAnchor(SCAnchor anchor) {
        Vector related_links = getLinksWith(anchor);
        int num = related_links.size();
        for (int i = 0; i < num; i++) {
            SCLink link = (SCLink) related_links.elementAt(i);
            if (link instanceof SCCausalLink) {
                SCCausalLink causal_link = (SCCausalLink) link;
                if (isSourceOf(anchor, causal_link)) {
                } else if (isDestOf(anchor, causal_link)) {
                }
            }
            if (link instanceof SCNonCausalLink) {
            }
        }
        graph.removeNode(anchor.getIRNode());
        return true;
    }

    /** If an anchor is a source of the causal link */
    public boolean isSourceOf(SCAnchor anchor, SCCausalLink link) {
        IRNode anchornode = anchor.getIRNode();
        IRNode linknode = link.getIRNode();
        int numChildren = graph.numChildren(anchornode);
        for (int i = 0; i < numChildren; i++) {
            IRNode child = graph.getChild(anchornode, i);
            if (child.equals(linknode)) return true;
        }
        return false;
    }

    /** If an anchor is a dest of the causal link */
    public boolean isDestOf(SCAnchor anchor, SCCausalLink link) {
        IRNode anchornode = anchor.getIRNode();
        IRNode linknode = link.getIRNode();
        int numChildren = graph.numChildren(linknode);
        for (int i = 0; i < numChildren; i++) {
            IRNode child = graph.getChild(linknode, i);
            if (child.equals(anchornode)) return true;
        }
        return false;
    }

    /** Create a causal link */
    public SCCausalLink createCausalLink(String name) {
        if (region == null) return null;
        PlainIRNode linknode = new PlainIRNode(region);
        setHTNodeName(linknode, "SCCausalLink_" + name);
        graph.initNode(linknode, ~BRANCH_FACTOR, ~BRANCH_FACTOR);
        SCCausalLink link = new SCCausalLink(linknode, this);
        SCEdge edge = createEdge();
        if (edge == null) return null;
        graph.setSource(edge.getIRNode(), this.getRoot());
        graph.setSink(edge.getIRNode(), linknode);
        return link;
    }

    /** Create a non-causal link */
    public SCNonCausalLink createNonCausalLink(String name) {
        if (region == null) return null;
        PlainIRNode linknode = new PlainIRNode(region);
        setHTNodeName(linknode, "SCNonCausalLink_" + name);
        graph.initNode(linknode, ~BRANCH_FACTOR, ~BRANCH_FACTOR);
        SCNonCausalLink link = new SCNonCausalLink(linknode, this);
        SCEdge edge = createEdge();
        if (edge == null) return null;
        graph.setSource(edge.getIRNode(), this.getRoot());
        graph.setSink(edge.getIRNode(), linknode);
        return link;
    }

    /** Create edges in this graph */
    public SCEdge createEdge() {
        if (region == null) return null;
        PlainIRNode edgenode = new PlainIRNode(region);
        setHTNodeName(edgenode, "SCEdge_");
        graph.initEdge(edgenode);
        SCEdge edge = new SCEdge(edgenode, this);
        return edge;
    }

    /** Add an anchor to a link */
    public boolean addAnchorToNonCausalLink(SCAnchor anchor, SCNonCausalLink link) {
        if (containsAnchor(link, anchor)) return false;
        SCEdge edge = createEdge();
        if (edge == null) return false;
        graph.setSource(edge.getIRNode(), anchor.getIRNode());
        graph.setSink(edge.getIRNode(), link.getIRNode());
        return true;
    }

    public boolean addAnchorAsSource(SCAnchor anchor, SCCausalLink link) {
        if (containsAnchor(link, anchor)) return false;
        SCEdge edge = createEdge();
        if (edge == null) return false;
        graph.setSource(edge.getIRNode(), anchor.getIRNode());
        graph.setSink(edge.getIRNode(), link.getIRNode());
        return true;
    }

    public boolean addAnchorAsDest(SCAnchor anchor, SCCausalLink link) {
        if (containsAnchor(link, anchor)) return false;
        SCEdge edge = createEdge();
        if (edge == null) return false;
        graph.setSource(edge.getIRNode(), link.getIRNode());
        graph.setSink(edge.getIRNode(), anchor.getIRNode());
        return true;
    }

    /** Get all source anchors of a causal link */
    public Vector getSourceAnchors(SCCausalLink link) {
        Vector anchors = new Vector();
        int numParents = graph.numParents(link.getIRNode());
        for (int i = 0; i < numParents; i++) {
            IRNode sourcenode = graph.getParent(link.getIRNode(), i);
            int nodeType = this.getHTNodeType(sourcenode);
            if (nodeType == ANCHOR) {
                SCAnchor anchor = new SCAnchor(sourcenode, this);
                anchors.addElement(anchor);
            }
        }
        return anchors;
    }

    /** Get all source anchor nodes of a causal link */
    public Vector getSourceAnchorNodes(SCCausalLink link) {
        Vector anchornodes = new Vector();
        int numParents = graph.numParents(link.getIRNode());
        for (int i = 0; i < numParents; i++) {
            IRNode sourcenode = graph.getParent(link.getIRNode(), i);
            int nodeType = this.getHTNodeType(sourcenode);
            if (nodeType == ANCHOR) {
                anchornodes.addElement(sourcenode);
            }
        }
        return anchornodes;
    }

    /** return the number of source anchors of a causal link */
    public int numberOfSourceAnchors(SCCausalLink link) {
        return getSourceAnchorNodes(link).size();
    }

    /** Get all dest anchors of a causal link */
    public Vector getDestAnchors(SCCausalLink link) {
        Vector anchors = new Vector();
        int numAnchors = graph.numChildren(link.getIRNode());
        for (int i = 0; i < numAnchors; i++) {
            IRNode destnode = graph.getChild(link.getIRNode(), i);
            int nodeType = this.getHTNodeType(destnode);
            if (nodeType == ANCHOR) {
                SCAnchor anchor = new SCAnchor(destnode, this);
                anchors.addElement(anchor);
            }
        }
        return anchors;
    }

    /** Get all dest nodes of a causal link */
    public Vector getDestAnchorNodes(SCCausalLink link) {
        Vector anchornodes = new Vector();
        int numAnchors = graph.numChildren(link.getIRNode());
        for (int i = 0; i < numAnchors; i++) {
            IRNode destnode = graph.getChild(link.getIRNode(), i);
            int nodeType = this.getHTNodeType(destnode);
            if (nodeType == ANCHOR) {
                anchornodes.addElement(destnode);
            }
        }
        return anchornodes;
    }

    /** return the number of dest anchors of a causal link */
    public int numberOfDestAnchors(SCCausalLink link) {
        return getDestAnchorNodes(link).size();
    }

    /** Get all anchors of a non-causal link */
    public Vector getAnchorsInNonCausalLink(SCNonCausalLink link) {
        Vector anchors = new Vector();
        int numParents = graph.numParents(link.getIRNode());
        for (int i = 0; i < numParents; i++) {
            IRNode sourcenode = graph.getParent(link.getIRNode(), i);
            int nodeType = this.getHTNodeType(sourcenode);
            if (nodeType == ANCHOR) {
                SCAnchor anchor = new SCAnchor(sourcenode, this);
                anchors.addElement(anchor);
            }
        }
        return anchors;
    }

    /** Get all anchor nodes of a non-causal link */
    public Vector getAnchorNodesInNonCausalLink(SCNonCausalLink link) {
        Vector anchornodes = new Vector();
        int numParents = graph.numParents(link.getIRNode());
        for (int i = 0; i < numParents; i++) {
            IRNode sourcenode = graph.getParent(link.getIRNode(), i);
            int nodeType = this.getHTNodeType(sourcenode);
            if (nodeType == ANCHOR) {
                anchornodes.addElement(sourcenode);
            }
        }
        return anchornodes;
    }

    /** Return the number of anchors in a non-causal link */
    public int numberOfAnchorsInNonCausalLink(SCNonCausalLink link) {
        return getAnchorNodesInNonCausalLink(link).size();
    }

    /** Retrieve all anchors contained in the specified link */
    public Vector getAnchorsOfLink(SCLink link) {
        Vector anchors = new Vector();
        if (link instanceof SCNonCausalLink) {
            SCNonCausalLink non_causal = (SCNonCausalLink) link;
            return getAnchorsInNonCausalLink(non_causal);
        } else if (link instanceof SCCausalLink) {
            SCCausalLink causal_link = (SCCausalLink) link;
            Vector sources = this.getSourceAnchors(causal_link);
            int num = sources.size();
            for (int i = 0; i < num; i++) anchors.addElement(sources.elementAt(i));
            Vector dests = this.getDestAnchors(causal_link);
            num = dests.size();
            for (int i = 0; i < num; i++) anchors.addElement(dests.elementAt(i));
            return anchors;
        } else return anchors;
    }

    /** Returns the number of anchors contained in the specified link */
    public int numberOfAnchorsInLink(SCLink link) {
        return getAnchorsOfLink(link).size();
    }

    /** return TRUE if the link contains the specified anchor */
    public boolean containsAnchor(SCLink link, SCAnchor anchor) {
        IRNode anchor_node = anchor.getIRNode();
        if (link instanceof SCCausalLink) {
            SCCausalLink causal_link = (SCCausalLink) link;
            Vector sources = this.getSourceAnchorNodes(causal_link);
            if (sources.contains(anchor_node)) return true;
            Vector dests = this.getDestAnchorNodes(causal_link);
            if (dests.contains(anchor_node)) return true;
        }
        if (link instanceof SCNonCausalLink) {
            SCNonCausalLink non_causal = (SCNonCausalLink) link;
            Vector anchors = this.getAnchorNodesInNonCausalLink(non_causal);
            if (anchors.contains(anchor_node)) return true;
        }
        return false;
    }

    /** Retrieve all of the links which contain the specified anchor */
    public Vector getLinksWith(SCAnchor anchor) {
        Vector links = new Vector();
        IRNode anchor_node = anchor.getIRNode();
        int numParents = graph.numParents(anchor_node);
        for (int i = 0; i < numParents; i++) {
            IRNode parent = graph.getParent(anchor_node, i);
            int nodeType = getHTNodeType(parent);
            if (nodeType == CAUSAL_LINK) {
                SCCausalLink causal_link = new SCCausalLink(parent, this);
                links.addElement(causal_link);
            }
        }
        int numChildren = graph.numChildren(anchor_node);
        for (int i = 0; i < numChildren; i++) {
            IRNode child = graph.getChild(anchor_node, i);
            int nodeType = getHTNodeType(child);
            if (nodeType == CAUSAL_LINK) {
                SCCausalLink causal_link = new SCCausalLink(child, this);
                links.addElement(causal_link);
            }
            if (nodeType == NON_CAUSAL) {
                SCNonCausalLink non_causal = new SCNonCausalLink(child, this);
                links.addElement(non_causal);
            }
        }
        return links;
    }

    /** Return the number of links which contain the specified anchor */
    public int numberOfLinksWith(SCAnchor anchor) {
        return getLinksWith(anchor).size();
    }

    /** Removes the specified anchor from the specified link 
   *  and checking degeneration conditions
   */
    public boolean removeAnchorFromLink(SCLink link, SCAnchor anchor) {
        if (this.containsAnchor(link, anchor) == false) return false;
        IRNode linknode = link.getIRNode();
        IRNode anchornode = anchor.getIRNode();
        if (link instanceof SCCausalLink) {
            java.util.Enumeration anchornodes = graph.parents(linknode);
            int i = 0;
            while (anchornodes.hasMoreElements()) {
                IRNode anode = (IRNode) anchornodes.nextElement();
                if (getHTNodeType(anode) == ANCHOR && anchornode.equals(anode)) {
                    IRNode edge = graph.getParentEdge(linknode, i);
                    graph.disconnect(edge);
                    return true;
                }
                i++;
            }
            anchornodes = graph.children(linknode);
            i = 0;
            while (anchornodes.hasMoreElements()) {
                IRNode anode = (IRNode) anchornodes.nextElement();
                if (anchornode.equals(anode)) {
                    IRNode edge = graph.getChildEdge(linknode, i);
                    graph.disconnect(edge);
                    return true;
                }
                i++;
            }
        }
        if (link instanceof SCNonCausalLink) {
            java.util.Enumeration anchornodes = graph.parents(linknode);
            int i = 0;
            while (anchornodes.hasMoreElements()) {
                IRNode anode = (IRNode) anchornodes.nextElement();
                if (getHTNodeType(anode) == ANCHOR && anchornode.equals(anode)) {
                    IRNode edge = graph.getParentEdge(linknode, i);
                    graph.disconnect(edge);
                    return true;
                }
                i++;
            }
        }
        return false;
    }

    /** Delete the specified link in this hypertext */
    public boolean deleteLink(SCLink link) {
        IRNode linknode = link.getIRNode();
        graph.removeNode(linknode);
        this.setHTNodeName(linknode, "Deleted");
        return true;
    }

    private static class Factory extends ComponentFactory {

        public static final Factory prototype = new Factory();

        private Factory() {
            super();
        }

        /** Create an SCHypertext based on input in */
        public Component create(UniqueID id, DataInput in) {
            SCHypertext htstructure = new SCHypertext(id);
            return htstructure;
        }
    }
}
