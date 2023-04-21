package org.jnet.modelset;

import org.jnet.util.Logger;
import org.jnet.util.ArrayUtil;
import org.jnet.viewer.JnetConstants;
import org.jnet.viewer.Token;
import org.jnet.viewer.Viewer;
import org.jnet.g3d.Graphics3D;
import org.jnet.api.Interface;
import org.jnet.api.JnetAdapter;
import org.jnet.api.JnetBioResolver;
import org.jnet.api.SymmetryInterface;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import org.nbrowse.utils.ErrorUtil;
import org.nbrowse.views.JnetManager;

public final class ModelLoader extends ModelSet implements Serializable {

    static final long serialVersionUID = 1L;

    private static final boolean useRawData = false;

    private ModelLoader mergeModelSet;

    private boolean merging;

    private boolean isMultiFile;

    private String jnetData;

    private final int[] specialNodeIndexes = new int[JnetConstants.NODEID_MAX];

    private String[] group3Lists;

    private int[][] group3Counts;

    public ModelLoader(Viewer viewer, String name) {
        this.viewer = viewer;
        initializeInfo(name, 1, null, null);
        initializeModelSet(null, null);
        modelSetName = "zapped";
        viewer.setStringProperty("_fileType", "");
    }

    public ModelLoader(Viewer viewer, JnetAdapter adapter, Object clientFile, ModelLoader mergeModelSet, String modelSetName) {
        this.modelSetName = modelSetName;
        this.mergeModelSet = mergeModelSet;
        merging = (mergeModelSet != null && mergeModelSet.nodeCount > 0);
        this.viewer = viewer;
        initializeInfo(adapter.getFileTypeName(clientFile).toLowerCase().intern(), adapter.getEstimatedNodeCount(clientFile), adapter.getNodeSetCollectionProperties(clientFile), adapter.getNodeSetCollectionAuxiliaryInfo(clientFile));
        initializeModelSet(adapter, clientFile);
        viewer.setStretchMode(true);
    }

    private boolean someModelsHaveUnitcells;

    private boolean isTrajectory;

    private String fileHeader;

    private void initializeInfo(String name, int nNodes, Properties properties, Hashtable info) {
        g3d = viewer.getGraphics3D();
        modelSetTypeName = name;
        isXYZ = (modelSetTypeName == "xyz");
        setModelSetProperties(properties);
        setModelSetAuxiliaryInfo(info);
        isMultiFile = getModelSetAuxiliaryInfoBoolean("isMultiFile");
        isPDB = getModelSetAuxiliaryInfoBoolean("isPDB");
        jnetData = (String) getModelSetAuxiliaryInfo("jnetData");
        fileHeader = (String) getModelSetAuxiliaryInfo("fileHeader");
        trajectories = (Vector) getModelSetAuxiliaryInfo("trajectories");
        isTrajectory = (trajectories != null);
        adapterTrajectoryCount = (trajectories == null ? 0 : trajectories.size());
        someModelsHaveSymmetry = getModelSetAuxiliaryInfoBoolean("someModelsHaveSymmetry");
        someModelsHaveUnitcells = getModelSetAuxiliaryInfoBoolean("someModelsHaveUnitcells");
        someModelsHaveFractionalCoordinates = getModelSetAuxiliaryInfoBoolean("someModelsHaveFractionalCoordinates");
        if (merging) {
            someModelsHaveSymmetry |= mergeModelSet.getModelSetAuxiliaryInfoBoolean("someModelsHaveSymmetry");
            someModelsHaveUnitcells |= mergeModelSet.getModelSetAuxiliaryInfoBoolean("someModelsHaveUnitcells");
            someModelsHaveFractionalCoordinates |= mergeModelSet.getModelSetAuxiliaryInfoBoolean("someModelsHaveFractionalCoordinates");
            someModelsHaveAromaticEdges |= mergeModelSet.someModelsHaveAromaticEdges;
        }
        initializeBuild(nNodes);
    }

    private static final int NODE_GROWTH_INCREMENT = 2000;

    private final Hashtable htNodeMap = new Hashtable();

    private void initializeBuild(int nodeCountEstimate) {
        if (nodeCountEstimate <= 0) nodeCountEstimate = NODE_GROWTH_INCREMENT;
        if (merging) {
            nodes = mergeModelSet.nodes;
            edges = mergeModelSet.edges;
        } else {
            nodes = new Node[nodeCountEstimate];
            edges = new Edge[250 + nodeCountEstimate];
        }
        htNodeMap.clear();
        initializeGroupBuild();
    }

    private static final int defaultGroupCount = 32;

    private Chain[] chainOf;

    private String[] group3Of;

    private int[] seqcodes;

    private int[] firstNodeIndexes;

    private int currentModelIndex;

    private Model currentModel;

    private char currentChainID;

    private Chain currentChain;

    private int currentGroupSequenceNumber;

    private char currentGroupInsertionCode;

    private String currentGroup3;

    /**
   * also from calculateStructures
   * 
   */
    public void initializeGroupBuild() {
        chainOf = new Chain[defaultGroupCount];
        group3Of = new String[defaultGroupCount];
        seqcodes = new int[defaultGroupCount];
        firstNodeIndexes = new int[defaultGroupCount];
        currentChainID = '￿';
        currentChain = null;
        currentGroupInsertionCode = '￿';
        currentGroup3 = "xxxxx";
        currentModelIndex = -1;
        currentModel = null;
    }

    Group nullGroup;

    private int baseModelIndex = 0;

    private int baseModelCount = 0;

    private int baseNodeIndex = 0;

    private int baseEdgeIndex = 0;

    private int baseTrajectoryCount = 0;

    private boolean appendNew;

    private int adapterModelCount = 0;

    private int adapterTrajectoryCount = 0;

    private void initializeModelSet(JnetAdapter adapter, Object clientFile) {
        adapterModelCount = (adapter == null ? 1 : adapter.getNodeSetCount(clientFile));
        appendNew = (!merging || adapter == null || adapterModelCount > 1 || isTrajectory || viewer.getAppendNew());
        if (merging) mergeModelArrays();
        initializeNodeEdgeModelCounts();
        if (adapter == null) {
            setModelNameNumberProperties(0, -1, "", 1, null, null, false, null);
        } else {
            if (adapterModelCount > 0) {
                Logger.info("ModelSet: haveSymmetry:" + someModelsHaveSymmetry + " haveUnitcells:" + someModelsHaveUnitcells + " haveFractionalCoord:" + someModelsHaveFractionalCoordinates);
                Logger.info(adapterModelCount + " model" + (modelCount == 1 ? "" : "s") + " in this collection. Use getProperty \"modelInfo\" or" + " getProperty \"auxiliaryInfo\" to inspect them.");
            }
            iterateOverAllNewModels(adapter, clientFile);
            iterateOverAllNewNodes(adapter, clientFile);
            iterateOverAllNewEdges(adapter, clientFile);
            iterateOverAllNewStructures(adapter, clientFile);
            if (adapter != null) adapter.finish(clientFile);
            initializeUnitCellAndSymmetry();
            initializeEdgeing();
        }
        finalizeGroupBuild();
        calculatePolymers(null);
        freeze();
        calcBoundBoxDimensions(null);
        finalizeShapes();
        if (mergeModelSet != null) mergeModelSet.releaseModelSet();
        mergeModelSet = null;
    }

    /** Experiment - is it possible to add nodes & edges to existing model set
   * without wreaking havoc?? this is modeled on initModelSet (which is for
   * creating new model set)
   */
    public void addNodesAndEdgesToModelSet(JnetAdapter adapter, Object adapterData) {
        iterateOverAllNewNodes(adapter, adapterData);
    }

    protected void releaseModelSet() {
        group3Lists = null;
        group3Counts = null;
        groups = null;
        super.releaseModelSet();
    }

    private void mergeModelArrays() {
        baseModelCount = mergeModelSet.modelCount;
        baseTrajectoryCount = mergeModelSet.getTrajectoryCount();
        if (baseTrajectoryCount > 0) {
            if (isTrajectory) {
                for (int i = 0; i < trajectories.size(); i++) mergeModelSet.trajectories.addElement(trajectories.elementAt(i));
            }
            trajectories = mergeModelSet.trajectories;
        }
        modelFileNumbers = mergeModelSet.modelFileNumbers;
        modelNumbersForNodeLabel = mergeModelSet.modelNumbersForNodeLabel;
        modelNames = mergeModelSet.modelNames;
        modelNumbers = mergeModelSet.modelNumbers;
        frameTitles = mergeModelSet.frameTitles;
    }

    private void initializeNodeEdgeModelCounts() {
        nodeCount = 0;
        edgeCount = 0;
        int trajectoryCount = adapterTrajectoryCount;
        if (merging) {
            if (appendNew) {
                baseModelIndex = baseModelCount;
                modelCount = baseModelCount + adapterModelCount;
            } else {
                baseModelIndex = viewer.getCurrentModelIndex();
                if (baseModelIndex < 0) baseModelIndex = baseModelCount - 1;
                modelCount = baseModelCount;
            }
            nodeCount = baseNodeIndex = mergeModelSet.nodeCount;
            edgeCount = baseEdgeIndex = mergeModelSet.edgeCount;
            groupCount = baseGroupIndex = mergeModelSet.groupCount;
        } else {
            modelCount = adapterModelCount;
        }
        if (trajectoryCount > 1) modelCount += trajectoryCount - 1;
        models = (Model[]) ArrayUtil.setLength(models, modelCount);
        modelFileNumbers = (int[]) ArrayUtil.setLength(modelFileNumbers, modelCount);
        modelNumbers = (int[]) ArrayUtil.setLength(modelNumbers, modelCount);
        modelNumbersForNodeLabel = (String[]) ArrayUtil.setLength(modelNumbersForNodeLabel, modelCount);
        modelNames = (String[]) ArrayUtil.setLength(modelNames, modelCount);
        frameTitles = (String[]) ArrayUtil.setLength(frameTitles, modelCount);
    }

    private void initializeMerge() {
        merge(mergeModelSet);
        bsSymmetry = mergeModelSet.bsSymmetry;
        Hashtable info = mergeModelSet.getAuxiliaryInfo(null);
        String[] mergeGroup3Lists = (String[]) info.get("group3Lists");
        int[][] mergeGroup3Counts = (int[][]) info.get("group3Counts");
        if (mergeGroup3Lists != null) {
            for (int i = 0; i < baseModelCount; i++) {
                group3Lists[i + 1] = mergeGroup3Lists[i + 1];
                group3Counts[i + 1] = mergeGroup3Counts[i + 1];
                structuresDefinedInFile.set(i);
            }
            group3Lists[0] = mergeGroup3Lists[0];
            group3Counts[0] = mergeGroup3Counts[0];
        }
        if (!appendNew && isPDB) structuresDefinedInFile.clear(baseModelIndex);
        surfaceDistance100s = null;
    }

    /** Initializes stuff for all models. currently for jnet we just have 1 model.
   apparently can have multiple models */
    private void iterateOverAllNewModels(JnetAdapter adapter, Object clientFile) {
        if (modelCount > 0) {
            nullGroup = new Group(new Chain(this, getModel(baseModelIndex), ' '), "", 0, -1, -1);
        }
        group3Lists = new String[modelCount + 1];
        group3Counts = new int[modelCount + 1][];
        structuresDefinedInFile = new BitSet();
        if (merging) initializeMerge();
        int iTrajectory = (isTrajectory ? baseTrajectoryCount : -1);
        int ipt = baseModelIndex;
        for (int i = 0; i < adapterModelCount; ++i, ++ipt) {
            int modelNumber = (appendNew ? adapter.getNodeSetNumber(clientFile, i) : Integer.MAX_VALUE);
            String modelName = adapter.getNodeSetName(clientFile, i);
            Properties modelProperties = adapter.getNodeSetProperties(clientFile, i);
            Hashtable modelAuxiliaryInfo = adapter.getNodeSetAuxiliaryInfo(clientFile, i);
            if (modelAuxiliaryInfo != null) viewer.setStringProperty("_fileType", (String) modelAuxiliaryInfo.get("fileType"));
            if (modelName == null) modelName = (jnetData != null ? jnetData.substring(jnetData.indexOf(":") + 2, jnetData.indexOf(";")) : modelNumber == Integer.MAX_VALUE ? "" : "" + (modelNumber % 1000000));
            boolean isPDBModel = setModelNameNumberProperties(ipt, iTrajectory, modelName, modelNumber, modelProperties, modelAuxiliaryInfo, isPDB, jnetData);
            if (isPDBModel) {
                group3Lists[ipt + 1] = JnetConstants.group3List;
                group3Counts[ipt + 1] = new int[JnetConstants.group3Count + 10];
                if (group3Lists[0] == null) {
                    group3Lists[0] = JnetConstants.group3List;
                    group3Counts[0] = new int[JnetConstants.group3Count + 10];
                }
            }
            if (getModelAuxiliaryInfo(ipt, "periodicOriginXyz") != null) someModelsHaveSymmetry = true;
        }
        if (isTrajectory) {
            int ia = adapterModelCount;
            for (int i = ipt; i < modelCount; i++) {
                models[i] = models[baseModelCount];
                modelNumbers[i] = adapter.getNodeSetNumber(clientFile, ia++);
                structuresDefinedInFile.set(i);
            }
        }
        finalizeModels(baseModelCount);
    }

    boolean setModelNameNumberProperties(int modelIndex, int trajectoryBaseIndex, String modelName, int modelNumber, Properties modelProperties, Hashtable modelAuxiliaryInfo, boolean isPDB, String jnetData) {
        if (modelNumber != Integer.MAX_VALUE) {
            models[modelIndex] = new Model((ModelSet) this, modelIndex, trajectoryBaseIndex, jnetData, modelProperties, modelAuxiliaryInfo);
            modelNumbers[modelIndex] = modelNumber;
            modelNames[modelIndex] = modelName;
        }
        String codes = (String) getModelAuxiliaryInfo(modelIndex, "altLocs");
        models[modelIndex].setNAltLocs(codes == null ? 0 : codes.length());
        codes = (String) getModelAuxiliaryInfo(modelIndex, "insertionCodes");
        models[modelIndex].setNInsertions(codes == null ? 0 : codes.length());
        return models[modelIndex].isPDB = getModelAuxiliaryInfoBoolean(modelIndex, "isPDB");
    }

    /**
   * Model numbers are considerably more complicated in Jnet 11.
   * 
   * int modelNumber
   *  
   *   The adapter gives us a modelNumber, but that is not necessarily
   *   what the user accesses. If a single files is loaded this is:
   *   
   *   a) single file context:
   *   
   *     1) the sequential number of the model in the file , or
   *     2) if a PDB file and "MODEL" record is present, that model number
   *     
   *   b) multifile context:
   *   
   *     always 1000000 * (fileIndex + 1) + (modelIndexInFile + 1)
   *   
   *   
   * int fileIndex
   * 
   *   The 0-based reference to the file containing this model. Used
   *   when doing   "select model=3.2" in a multifile context
   *   
   * int modelFileNumber
   * 
   *   An integer coding both the file and the model:
   *   
   *     file * 1000000 + modelInFile (1-based)
   *     
   *   Used all over the place. Note that if there is only one file,
   *   then modelFileNumber < 1000000.
   * 
   * String modelNumberDotted
   *   
   *   A number the user can use "1.3"
   *   
   * String modelNumberForNodeLabel
   * 
   *   Either the dotted number or the PDB MODEL number, if there is only one file
   *   
   * @param baseModelCount
   *    
   */
    private void finalizeModels(int baseModelCount) {
        if (modelCount == baseModelCount) return;
        String sNum;
        int modelnumber = 0;
        int lastfilenumber = -1;
        if (isTrajectory) for (int i = baseModelCount; ++i < modelCount; ) modelNumbers[i] = modelNumbers[i - 1] + 1;
        if (baseModelCount > 0) {
            if (modelNumbers[0] < 1000000) {
                for (int i = 0; i < baseModelCount; i++) {
                    if (modelNames[i].length() == 0) modelNames[i] = "" + modelNumbers[i];
                    modelNumbers[i] += 1000000;
                    modelNumbersForNodeLabel[i] = "1." + (i + 1);
                }
            }
            int filenumber = modelNumbers[baseModelCount - 1];
            filenumber -= filenumber % 1000000;
            if (modelNumbers[baseModelCount] < 1000000) filenumber += 1000000;
            for (int i = baseModelCount; i < modelCount; i++) modelNumbers[i] += filenumber;
        }
        for (int i = baseModelCount; i < modelCount; ++i) {
            if (fileHeader != null) setModelAuxiliaryInfo(i, "fileHeader", fileHeader);
            int filenumber = modelNumbers[i] / 1000000;
            if (filenumber != lastfilenumber) {
                modelnumber = 0;
                lastfilenumber = filenumber;
            }
            modelnumber++;
            if (filenumber == 0) {
                sNum = "" + getModelNumber(i);
                filenumber = 1;
            } else {
                sNum = filenumber + "." + modelnumber;
            }
            modelNumbersForNodeLabel[i] = sNum;
            models[i].fileIndex = filenumber - 1;
            modelFileNumbers[i] = filenumber * 1000000 + modelnumber;
            if (modelNames[i] == null || modelNames[i].length() == 0) modelNames[i] = sNum;
        }
        if (merging) for (int i = 0; i < baseModelCount; i++) models[i].modelSet = this;
        for (int i = 0; i < modelCount; i++) {
            setModelAuxiliaryInfo(i, "modelName", modelNames[i]);
            setModelAuxiliaryInfo(i, "modelNumber", new Integer(modelNumbers[i] % 1000000));
            setModelAuxiliaryInfo(i, "modelFileNumber", new Integer(modelFileNumbers[i]));
            setModelAuxiliaryInfo(i, "modelNumberDotted", getModelNumberDotted(i));
        }
    }

    private void iterateOverAllNewNodes(JnetAdapter adapter, Object clientFile) {
        int size = viewer.getDefaultMadNode();
        if (viewer.useDefaultNodeSize()) size = viewer.getDefaultNodeSize();
        for (JnetAdapter.NodeIterator iterNode = adapter.getNodeIterator(clientFile); iterNode.hasNext(); ) {
            short elementNumber = (short) iterNode.getElementNumber();
            if (elementNumber <= 0) elementNumber = JnetConstants.elementNumberFromSymbol(iterNode.getElementSymbol());
            char alternateLocation = iterNode.getAlternateLocationID();
            Node node = addNode(iterNode.getNodeSetIndex() + baseModelIndex, iterNode.getNodeSymmetry(), iterNode.getNodeSite(), iterNode.getUniqueID(), elementNumber, iterNode.getNodeName(), size, iterNode.getFormalCharge(), iterNode.getPartialCharge(), iterNode.getEllipsoid(), iterNode.getOccupancy(), iterNode.getBfactor(), iterNode.getX(), iterNode.getY(), iterNode.getZ(), iterNode.getIsHetero(), iterNode.getNodeSerial(), iterNode.getChainID(), iterNode.getGroup3(), iterNode.getSequenceNumber(), iterNode.getInsertionCode(), iterNode.getVectorX(), iterNode.getVectorY(), iterNode.getVectorZ(), alternateLocation, iterNode.getClientNodeReference(), iterNode.getRadius(), iterNode.getEncodedColix(), iterNode.getNodeStep(), iterNode.getNodeStepTranslucencyFlag());
            if (useRawData) {
                JnetManager.inst().checkRawNodeData(iterNode.getNodeName(), node);
            }
            iterNode.setJnetNode(node);
        }
        int iLast = -1;
        for (int i = 0; i < nodeCount; i++) if (nodes[i].modelIndex != iLast) {
            iLast = nodes[i].modelIndex;
            models[iLast].firstNodeIndex = i;
            models[iLast].bsNodes = null;
        }
        if (useRawData) {
            JnetManager.inst().setUseRawData(false);
        }
    }

    public Node addNode(String id, String name, short step, short colix, boolean transluc, int size, boolean is3D) {
        int nodeSite = getNodeCount() + 1;
        short elem = JnetConstants.getJnetDefaultElementNumber();
        int i = Integer.MIN_VALUE;
        float f = Float.NaN;
        int occ = 100;
        char c = '\0';
        float x = random(), y = random();
        float z = is3D ? random() : 0;
        Node n = addNode(0, null, nodeSite, id, elem, name, size, i, f, null, occ, f, x, y, z, false, i, c, null, i, c, f, f, f, c, null, f, colix, step, transluc);
        return n;
    }

    private static final float randomSpace = 10;

    private static final float halfRndSpace = randomSpace / 2;

    private float random() {
        return (float) (Math.random() * randomSpace) - halfRndSpace;
    }

    /** creates Node could return Node that it makes - if want to track
   * @param modelIndex index of node set, can have multiple sets, 0 for jnet
   * @param nodeSymmetry null for jnet
   * @param nodeSite goes into Node, accessed with evals, think its 0 for jnet??
   * @param nodeUid
   * @param nodeicAndIsotopeNumber
   * @param nodeName
   * @param size
   * @param formalCharge
   * @param partialCharge
   * @param ellipsoid
   * @param occupancy
   * @param bfactor
   * @param x
   * @param y
   * @param z
   * @param isHetero
   * @param nodeSerial
   * @param chainID
   * @param group3
   * @param groupSequenceNumber
   * @param groupInsertionCode
   * @param vectorX
   * @param vectorY
   * @param vectorZ
   * @param alternateLocationID
   * @param clientNodeReference
   * @param radius
   * @param colix
   */
    public Node addNode(int modelIndex, BitSet nodeSymmetry, int nodeSite, Object nodeUid, short nodeicAndIsotopeNumber, String nodeName, int size, int formalCharge, float partialCharge, Object[] ellipsoid, int occupancy, float bfactor, float x, float y, float z, boolean isHetero, int nodeSerial, char chainID, String group3, int groupSequenceNumber, char groupInsertionCode, float vectorX, float vectorY, float vectorZ, char alternateLocationID, Object clientNodeReference, float radius, short colix, short step, boolean stepTranslucencyFlag) {
        checkNewGroup(nodeCount, modelIndex, chainID, group3, groupSequenceNumber, groupInsertionCode);
        if (nodeCount == nodes.length) growNodeArrays(NODE_GROWTH_INCREMENT);
        Node node = new Node(viewer, currentModelIndex, nodeCount, nodeSymmetry, nodeSite, nodeicAndIsotopeNumber, size, formalCharge, x, y, z, isHetero, chainID, alternateLocationID, radius);
        nodes[nodeCount] = node;
        setBFactor(nodeCount, bfactor);
        setOccupancy(nodeCount, occupancy);
        setPartialCharge(nodeCount, partialCharge);
        if (ellipsoid != null) setEllipsoid(nodeCount, ellipsoid);
        node.group = nullGroup;
        if (colix == 0) node.colixNode = viewer.getColixNodePalette(node, JnetConstants.PALETTE_CPK); else node.colixNode = colix;
        node.stepNode = step;
        if (nodeName != null) {
            if (nodeNames == null) nodeNames = new String[nodes.length];
            nodeNames[nodeCount] = nodeName.intern();
            byte specialNodeID = lookupSpecialNodeID(nodeName);
            if (specialNodeID == JnetConstants.NODEID_ALPHA_CARBON && group3 != null && group3.equalsIgnoreCase("CA")) specialNodeID = 0;
            if (specialNodeID != 0) {
                if (specialNodeIDs == null) specialNodeIDs = new byte[nodes.length];
                specialNodeIDs[nodeCount] = specialNodeID;
            }
        }
        if (nodeSerial != Integer.MIN_VALUE) {
            if (nodeSerials == null) nodeSerials = new int[nodes.length];
            nodeSerials[nodeCount] = nodeSerial;
        }
        if (clientNodeReference != null) {
            if (clientNodeReferences == null) clientNodeReferences = new Object[nodes.length];
            clientNodeReferences[nodeCount] = clientNodeReference;
        }
        if (!Float.isNaN(vectorX)) setVibrationVector(nodeCount, vectorX, vectorY, vectorZ);
        htNodeMap.put(nodeUid, node);
        nodeCount++;
        return node;
    }

    private static Hashtable htNode = new Hashtable();

    static {
        for (int i = JnetConstants.specialNodeNames.length; --i >= 0; ) {
            String specialNodeName = JnetConstants.specialNodeNames[i];
            if (specialNodeName != null) {
                Integer boxedI = new Integer(i);
                htNode.put(specialNodeName, boxedI);
            }
        }
    }

    private static byte lookupSpecialNodeID(String nodeName) {
        if (nodeName != null) {
            if (nodeName.indexOf('*') >= 0) nodeName = nodeName.replace('*', '\'');
            Integer boxedNodeID = (Integer) htNode.get(nodeName);
            if (boxedNodeID != null) return (byte) (boxedNodeID.intValue());
        }
        return 0;
    }

    private void checkNewGroup(int nodeIndex, int modelIndex, char chainID, String group3, int groupSequenceNumber, char groupInsertionCode) {
        String group3i = (group3 == null ? null : group3.intern());
        if (modelIndex != currentModelIndex) {
            currentModel = getModel(modelIndex);
            currentModelIndex = modelIndex;
            currentChainID = '￿';
        }
        if (chainID != currentChainID) {
            currentChainID = chainID;
            currentChain = getOrAllocateChain(currentModel, chainID);
            currentGroupInsertionCode = '￿';
            currentGroupSequenceNumber = -1;
            currentGroup3 = "xxxx";
        }
        if (groupSequenceNumber != currentGroupSequenceNumber || groupInsertionCode != currentGroupInsertionCode || group3i != currentGroup3) {
            currentGroupSequenceNumber = groupSequenceNumber;
            currentGroupInsertionCode = groupInsertionCode;
            currentGroup3 = group3i;
            if (group3Of == null) ErrorUtil.error(this, "group3Of null in modelLoader.checkNewGroup");
            while (group3Of != null && groupCount >= group3Of.length) {
                chainOf = (Chain[]) ArrayUtil.doubleLength(chainOf);
                group3Of = ArrayUtil.doubleLength(group3Of);
                seqcodes = ArrayUtil.doubleLength(seqcodes);
                firstNodeIndexes = ArrayUtil.doubleLength(firstNodeIndexes);
            }
            firstNodeIndexes[groupCount] = nodeIndex;
            chainOf[groupCount] = currentChain;
            group3Of[groupCount] = group3;
            seqcodes[groupCount] = Group.getSeqcode(groupSequenceNumber, groupInsertionCode);
            ++groupCount;
        }
    }

    private Chain getOrAllocateChain(Model model, char chainID) {
        Chain chain = model.getChain(chainID);
        if (chain != null) return chain;
        if (model.chainCount == model.chains.length) model.chains = (Chain[]) ArrayUtil.doubleLength(model.chains);
        return model.chains[model.chainCount++] = new Chain(this, model, chainID);
    }

    private void growNodeArrays(int byHowMuch) {
        int newLength = nodeCount + byHowMuch;
        nodes = (Node[]) ArrayUtil.setLength(nodes, newLength);
        if (clientNodeReferences != null) clientNodeReferences = (Object[]) ArrayUtil.setLength(clientNodeReferences, newLength);
        if (vibrationVectors != null) vibrationVectors = (Vector3f[]) ArrayUtil.setLength(vibrationVectors, newLength);
        if (occupancies != null) occupancies = ArrayUtil.setLength(occupancies, newLength);
        if (bfactor100s != null) bfactor100s = ArrayUtil.setLength(bfactor100s, newLength);
        if (partialCharges != null) partialCharges = ArrayUtil.setLength(partialCharges, newLength);
        if (ellipsoids != null) {
            Object[][] ellipsoids2 = new Object[newLength][];
            for (int i = 0; i < ellipsoids.length; i++) ellipsoids2[i] = ellipsoids[i];
            ellipsoids = ellipsoids2;
        }
        if (nodeNames != null) nodeNames = ArrayUtil.setLength(nodeNames, newLength);
        if (nodeSerials != null) nodeSerials = ArrayUtil.setLength(nodeSerials, newLength);
        if (specialNodeIDs != null) specialNodeIDs = ArrayUtil.setLength(specialNodeIDs, newLength);
    }

    private void iterateOverAllNewEdges(JnetAdapter adapter, Object clientFile) {
        JnetAdapter.EdgeIterator iterEdge = adapter.getEdgeIterator(clientFile);
        if (iterEdge == null) return;
        short mad = viewer.getMadEdge();
        short order;
        defaultCovalentMad = (jnetData == null ? mad : 0);
        boolean haveMultipleEdges = false;
        while (iterEdge.hasNext()) {
            order = (short) iterEdge.getEncodedOrder();
            short col = iterEdge.getEncodedColix();
            String info = iterEdge.getInfo();
            String width = (String) iterEdge.getEdgeWidthInc();
            short step = (short) iterEdge.getEdgeStep();
            Edge edge = edgeNodes(iterEdge.getNodeUniqueID1(), iterEdge.getNodeUniqueID2(), order, col, info, width, step);
            iterEdge.setJnetEdge(edge);
            if (order > 1) haveMultipleEdges = true;
        }
        if (haveMultipleEdges && someModelsHaveSymmetry && !viewer.getApplySymmetryToEdges()) Logger.info("ModelSet: use \"set appletSymmetryToEdges TRUE \" to apply the file-based multiple edges to symmetry-generated nodes.");
        defaultCovalentMad = mad;
    }

    public Edge addEdge(Node n1, Node n2, short colix, String type, String width, short step) {
        short order = getEdgeOrder(n1, n2);
        short mad = getDefaultMadFromOrder(order);
        return edgeNodes(n1, n2, order, mad, null, colix, type, width, step);
    }

    /** Create new edge connecting 2 nodes - look up nodes from nodeUids
  @return new edge, null if node 1 or 2 uid not found */
    public Edge edgeNodes(Object nodeUid1, Object nodeUid2, short order, short colix, String type, String width, short step) {
        Node node1 = (Node) htNodeMap.get(nodeUid1);
        if (node1 == null) {
            Logger.error("edgeNodes cannot find nodeUid1?:" + nodeUid1);
            return null;
        }
        Node node2 = (Node) htNodeMap.get(nodeUid2);
        if (node2 == null) {
            Logger.error("edgeNodes cannot find nodeUid2?:" + nodeUid2);
            return null;
        }
        order = getEdgeOrder(node1, node2, order);
        Edge edge = edgeMutually(node1, node2, order, getDefaultMadFromOrder(order), colix, type, width, step);
        if (edge.isAromatic()) someModelsHaveAromaticEdges = true;
        if (edgeCount == edges.length) edges = (Edge[]) ArrayUtil.setLength(edges, edgeCount + 2 * NODE_GROWTH_INCREMENT);
        setEdge(edgeCount++, edge);
        return edge;
    }

    private short getEdgeOrder(Node t, Node f) {
        return getEdgeOrder(t, f, (short) 1);
    }

    /** For Jnet order 8 means self cycle, and 5 means >1 edge between nodes */
    private short getEdgeOrder(Node node1, Node node2, short order) {
        if (node1 == node2) return 8;
        if (node1 == null) {
            ErrorUtil.error(this, "null node for loading jnet");
            return order;
        }
        if (node1.isEdgeed(node2, (short) 5)) return 5;
        return order;
    }

    /**
   * Pull in all spans of helix, etc. in the file(s)
   * 
   * We do turn first, because sometimes a group is defined
   * twice, and this way it gets marked as helix or sheet
   * if it is both one of those and turn.
   * 
   * @param adapter
   * @param clientFile
   */
    private void iterateOverAllNewStructures(JnetAdapter adapter, Object clientFile) {
        JnetAdapter.StructureIterator iterStructure = adapter.getStructureIterator(clientFile);
        if (iterStructure != null) while (iterStructure.hasNext()) {
            if (!iterStructure.getStructureType().equals("turn")) {
                defineStructure(iterStructure.getModelIndex(), iterStructure.getStructureType(), iterStructure.getStartChainID(), iterStructure.getStartSequenceNumber(), iterStructure.getStartInsertionCode(), iterStructure.getEndChainID(), iterStructure.getEndSequenceNumber(), iterStructure.getEndInsertionCode());
            }
        }
        iterStructure = adapter.getStructureIterator(clientFile);
        if (iterStructure != null) while (iterStructure.hasNext()) {
            if (iterStructure.getStructureType().equals("turn")) defineStructure(iterStructure.getModelIndex(), iterStructure.getStructureType(), iterStructure.getStartChainID(), iterStructure.getStartSequenceNumber(), iterStructure.getStartInsertionCode(), iterStructure.getEndChainID(), iterStructure.getEndSequenceNumber(), iterStructure.getEndInsertionCode());
        }
    }

    protected void defineStructure(int modelIndex, String structureType, char startChainID, int startSequenceNumber, char startInsertionCode, char endChainID, int endSequenceNumber, char endInsertionCode) {
        if (modelIndex >= 0 || isTrajectory) {
            if (isTrajectory) modelIndex = 0;
            modelIndex += baseModelIndex;
            structuresDefinedInFile.set(modelIndex);
            super.defineStructure(modelIndex, structureType, startChainID, startSequenceNumber, startInsertionCode, endChainID, endSequenceNumber, endInsertionCode);
            return;
        }
        for (int i = baseModelIndex; i < modelCount; i++) {
            structuresDefinedInFile.set(i);
            super.defineStructure(i, structureType, startChainID, startSequenceNumber, startInsertionCode, endChainID, endSequenceNumber, endInsertionCode);
        }
    }

    private void initializeUnitCellAndSymmetry() {
        if (someModelsHaveUnitcells) {
            unitCells = new SymmetryInterface[modelCount];
            boolean haveMergeCells = (mergeModelSet != null && mergeModelSet.unitCells != null);
            for (int i = 0; i < modelCount; i++) {
                if (haveMergeCells && i < baseModelCount) {
                    unitCells[i] = mergeModelSet.unitCells[i];
                } else {
                    unitCells[i] = (SymmetryInterface) Interface.getOptionInterface("symmetry.Symmetry");
                    unitCells[i].setSymmetryInfo(i, getModelAuxiliaryInfo(i));
                }
            }
        }
        if (someModelsHaveSymmetry) {
            getNodeBits(Token.symmetry, null);
            for (int iNode = baseNodeIndex, iModel = -1, i0 = 0; iNode < nodeCount; iNode++) {
                if (nodes[iNode].modelIndex != iModel) {
                    iModel = nodes[iNode].modelIndex;
                    i0 = baseNodeIndex + getModelAuxiliaryInfoInt(iModel, "presymmetryNodeIndex") + getModelAuxiliaryInfoInt(iModel, "presymmetryNodeCount");
                }
                if (iNode >= i0) bsSymmetry.set(iNode);
            }
        }
        if (someModelsHaveFractionalCoordinates) {
            for (int i = baseNodeIndex; i < nodeCount; i++) {
                int modelIndex = nodes[i].modelIndex;
                if (!unitCells[modelIndex].getCoordinatesAreFractional()) continue;
                unitCells[modelIndex].toCartesian(nodes[i]);
                if (Logger.debugging) Logger.debug("node " + i + ": " + (Point3f) nodes[i]);
            }
        }
    }

    /** If there are no edges this will actually put in edges for nodes that are
   * close to each other
   */
    private void initializeEdgeing() {
        boolean haveCONECT = (getModelSetAuxiliaryInfo("someModelsHaveCONECT") != null);
        BitSet bsExclude = null;
        if (haveCONECT) setPdbConectEdgeing(baseNodeIndex, baseModelIndex, bsExclude = new BitSet());
        boolean doEdge = (edgeCount == baseEdgeIndex || isMultiFile || isPDB && jnetData == null && (edgeCount - baseEdgeIndex) < (nodeCount - baseNodeIndex) / 2 || someModelsHaveSymmetry && !viewer.getApplySymmetryToEdges());
        if (viewer.getForceAutoEdge() || doEdge && viewer.getAutoEdge() && getModelSetProperty("noautoedge") == null) {
            BitSet bs = null;
            if (merging) {
                bs = new BitSet(nodeCount);
                for (int i = baseNodeIndex; i < nodeCount; i++) bs.set(i);
            }
            Logger.info("ModelSet: autoedgeing; use  autoedge=false  to not generate edges automatically");
            autoEdge(bs, bs, bsExclude, null);
        } else {
            Logger.info("ModelSet: not autoedgeing; use forceAutoedge=true to force automatic edge creation");
        }
    }

    /** private -> public for viewer to use */
    public void finalizeGroupBuild() {
        groups = new Group[groupCount];
        if (merging) {
            for (int i = 0; i < baseGroupIndex; i++) {
                groups[i] = mergeModelSet.groups[i];
                groups[i].setModelSet(this);
            }
        }
        for (int i = baseGroupIndex; i < groupCount; ++i) {
            distinguishAndPropagateGroup(i, chainOf[i], group3Of[i], seqcodes[i], firstNodeIndexes[i], (i == groupCount - 1 ? nodeCount : firstNodeIndexes[i + 1]));
            chainOf[i] = null;
            group3Of[i] = null;
        }
        chainOf = null;
        group3Of = null;
        if (group3Lists != null) {
            Hashtable info = getModelSetAuxiliaryInfo();
            if (info != null) {
                info.put("group3Lists", group3Lists);
                info.put("group3Counts", group3Counts);
            }
        }
        group3Counts = null;
        group3Lists = null;
    }

    private void distinguishAndPropagateGroup(int groupIndex, Chain chain, String group3, int seqcode, int firstNodeIndex, int maxNodeIndex) {
        int lastNodeIndex = maxNodeIndex - 1;
        if (lastNodeIndex < firstNodeIndex) throw new NullPointerException();
        int modelIndex = nodes[firstNodeIndex].modelIndex;
        Group group = null;
        if (group3 != null && specialNodeIDs != null && haveBioClasses) {
            if (jbr == null && haveBioClasses) {
                try {
                    Class shapeClass = Class.forName("org.jnet.modelsetbio.Resolver");
                    jbr = (JnetBioResolver) shapeClass.newInstance();
                    haveBioClasses = true;
                } catch (Exception e) {
                    Logger.error("developer error: org.jnet.modelsetbio.Resolver could not be found");
                    haveBioClasses = false;
                }
            }
            if (haveBioClasses) {
                group = jbr.distinguishAndPropagateGroup(chain, group3, seqcode, firstNodeIndex, maxNodeIndex, modelIndex, specialNodeIndexes, specialNodeIDs, nodes);
            }
        }
        String key;
        if (group == null) {
            group = new Group(chain, group3, seqcode, firstNodeIndex, lastNodeIndex);
            key = "o>";
        } else {
            key = (group.isProtein() ? "p>" : group.isNucleic() ? "n>" : group.isCarbohydrate() ? "c>" : "o>");
        }
        if (group3 != null) countGroup(modelIndex, key, group3);
        addGroup(chain, group);
        groups[groupIndex] = group;
        for (int i = maxNodeIndex; --i >= firstNodeIndex; ) nodes[i].setGroup(group);
    }

    private void addGroup(Chain chain, Group group) {
        if (chain.groupCount == chain.groups.length) chain.groups = (Group[]) ArrayUtil.doubleLength(chain.groups);
        chain.groups[chain.groupCount++] = group;
    }

    private void countGroup(int modelIndex, String code, String group3) {
        int ptm = modelIndex + 1;
        if (group3Lists == null || group3Lists[ptm] == null) return;
        String g3code = (group3 + "   ").substring(0, 3);
        int pt = group3Lists[ptm].indexOf(g3code);
        if (pt < 0) {
            group3Lists[ptm] += ",[" + g3code + "]";
            pt = group3Lists[ptm].indexOf(g3code);
            group3Counts[ptm] = (int[]) ArrayUtil.setLength(group3Counts[ptm], group3Counts[ptm].length + 10);
        }
        group3Counts[ptm][pt / 6]++;
        pt = group3Lists[ptm].indexOf(",[" + g3code);
        if (pt >= 0) group3Lists[ptm] = group3Lists[ptm].substring(0, pt) + code + group3Lists[ptm].substring(pt + 2);
        if (modelIndex >= 0) countGroup(-1, code, group3);
    }

    private void freeze() {
        htNodeMap.clear();
        if (nodeCount < nodes.length) growNodeArrays(0);
        if (edgeCount < edges.length) edges = (Edge[]) ArrayUtil.setLength(edges, edgeCount);
        for (int i = MAX_EDGES_LENGTH_TO_CACHE; --i > 0; ) {
            numCached[i] = 0;
            Edge[][] edgesCache = freeEdges[i];
            for (int j = edgesCache.length; --j >= 0; ) edgesCache[j] = null;
        }
        setNodeNamesAndNumbers();
        findElementsPresent();
        if (isPDB) calculateStructuresAllExcept(structuresDefinedInFile, true);
        molecules = null;
        moleculeCount = 0;
        currentModel = null;
        currentChain = null;
    }

    private void setNodeNamesAndNumbers() {
        if (nodeSerials == null) nodeSerials = new int[nodeCount];
        boolean isZeroBased = isXYZ && viewer.getZeroBasedXyzRasmol();
        int lastModelIndex = Integer.MAX_VALUE;
        int nodeNo = 1;
        for (int i = 0; i < nodeCount; ++i) {
            Node node = nodes[i];
            if (node.modelIndex != lastModelIndex) {
                lastModelIndex = node.modelIndex;
                nodeNo = (isZeroBased ? 0 : 1);
            }
            if (nodeSerials[i] == 0) nodeSerials[i] = (i < baseNodeIndex ? mergeModelSet.nodeSerials[i] : nodeNo++);
        }
        if (nodeNames == null) nodeNames = new String[nodeCount];
        for (int i = 0; i < nodeCount; ++i) if (nodeNames[i] == null) {
            Node node = nodes[i];
            nodeNames[i] = node.getElementSymbol() + node.getNodeNumber();
        }
    }

    private void findElementsPresent() {
        elementsPresent = new BitSet[modelCount];
        for (int i = 0; i < modelCount; i++) elementsPresent[i] = new BitSet();
        for (int i = nodeCount; --i >= 0; ) {
            int n = nodes[i].getNodeicAndIsotopeNumber();
            if (n >= 128) n = JnetConstants.elementNumberMax + JnetConstants.altElementIndexFromNumber(n);
            elementsPresent[nodes[i].modelIndex].set(n);
        }
    }

    /** Loads/initializes shapes[] array in ModelSet/Loader,
   * jnet only really uses balls & sticks (halos are not loaded here)
   * shouldnt viewer deal with shapes, and model set just nodes 
   * & edges ??*/
    private void finalizeShapes() {
        if (someModelsHaveAromaticEdges && viewer.getSmartAromatic()) assignAromaticEdges(false);
        if (merging) {
            for (int i = 0; i < JnetConstants.SHAPE_MAX; i++) if ((shapes[i] = mergeModelSet.shapes[i]) != null) shapes[i].setModelSet(this);
            viewer.getFrameRenderer().clear();
            merging = false;
            return;
        }
        loadShape(JnetConstants.SHAPE_BALLS);
        loadShape(JnetConstants.SHAPE_STICKS);
        loadShape(JnetConstants.SHAPE_MEASURES);
        loadShape(JnetConstants.SHAPE_BBCAGE);
        loadShape(JnetConstants.SHAPE_UCCAGE);
    }
}
