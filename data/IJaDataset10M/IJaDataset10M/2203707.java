package com.global360.sketchpadbpmn.commandmanager.commands;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import com.global360.sketchpadbpmn.SketchpadApplication;
import com.global360.sketchpadbpmn.SketchpadException;
import com.global360.sketchpadbpmn.WorkflowProcess;
import com.global360.sketchpadbpmn.WorkflowProcessType;
import com.global360.sketchpadbpmn.documents.BPMNWorkflowDocument;
import com.global360.sketchpadbpmn.documents.idmanager.BpmnId;
import com.global360.sketchpadbpmn.documents.idmanager.SketchpadIdException;
import com.global360.sketchpadbpmn.graphic.BPMNLaneGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNPageGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNPoolGraphic;
import com.global360.sketchpadbpmn.graphic.ContainerGraphic;
import com.global360.sketchpadbpmn.graphic.SketchGraphic;
import com.global360.sketchpadbpmn.utility.ILogger;

public class SketchGraphicGraph {

    protected ArrayList<SketchGraphicGraphNode> graphicsGraph = new ArrayList<SketchGraphicGraphNode>();

    protected ArrayList<BPMNFlowGraphicGraphNode> connectorsGraph = new ArrayList<BPMNFlowGraphicGraphNode>();

    private boolean bIsPage = false;

    protected SketchGraphicGraph(ArrayList<SketchGraphic> collapsedSelection, ArrayList<SketchGraphic> selectionOfClones) {
        for (SketchGraphic graphic : collapsedSelection) {
            SketchGraphicGraphNode graphNode = null;
            if (graphic instanceof BPMNPageGraphic) {
                SketchpadApplication.log.warn("SketchGraphicGraph: BPMNPageGraphic found in collapsedSelection!");
            } else if (graphic instanceof BPMNPoolGraphic) {
                graphNode = new BPMNPoolGraphicGraphNode((BPMNPoolGraphic) graphic, connectorsGraph, selectionOfClones);
            } else if (graphic instanceof BPMNLaneGraphic) {
                graphNode = new BPMNLaneGraphicGraphNode((BPMNLaneGraphic) graphic, connectorsGraph, selectionOfClones, true);
            } else {
                graphNode = new SketchGraphicGraphNode(graphic, connectorsGraph, selectionOfClones);
            }
            if (graphNode != null) {
                graphicsGraph.add(graphNode);
            }
        }
    }

    protected SketchGraphicGraph(BPMNPageGraphic page, ArrayList<SketchGraphic> selectionOfClones) throws SketchpadIdException {
        bIsPage = true;
        graphicsGraph.add(new BPMNPageGraphicGraphNode(page, connectorsGraph, selectionOfClones));
    }

    public boolean isPage() {
        return bIsPage;
    }

    public ArrayList<SketchGraphic> restore(BPMNWorkflowDocument destDoc, BPMNPageGraphic destPage, boolean remapIds, boolean internalConns, int displacement) throws SketchpadException {
        HashMap<String, WorkflowProcess> processesMap = new HashMap<String, WorkflowProcess>();
        ArrayList<SketchGraphic> restoredGraphics = new ArrayList<SketchGraphic>();
        for (SketchGraphicGraphNode node : graphicsGraph) {
            if (node instanceof BPMNPageGraphicGraphNode) {
                destPage = (BPMNPageGraphic) node.getGraphic();
                ((BPMNPageGraphicGraphNode) node).restore(destDoc, destPage, null, remapIds, internalConns, processesMap, restoredGraphics);
            } else {
                ContainerGraphic container = node.getContainer();
                if (!(container instanceof BPMNPageGraphic) && (!destPage.contains(container))) {
                    container = destPage.getDefaultLane();
                }
                node.restore(destDoc, destPage, container, remapIds, internalConns, displacement, processesMap, restoredGraphics);
            }
        }
        for (BPMNFlowGraphicGraphNode connector : connectorsGraph) {
            connector.restore(destDoc, destPage, remapIds, internalConns, displacement, processesMap, restoredGraphics);
        }
        for (SketchGraphicGraphNode node : graphicsGraph) {
            node.fixup(this, processesMap);
        }
        for (BPMNFlowGraphicGraphNode connector : connectorsGraph) {
            connector.fixup(this, processesMap);
        }
        return restoredGraphics;
    }

    public SketchGraphic find(BpmnId id) {
        SketchGraphic found = null;
        for (SketchGraphicGraphNode node : graphicsGraph) {
            found = node.find(id);
            if (null != found) {
                return found;
            }
        }
        for (BPMNFlowGraphicGraphNode connector : connectorsGraph) {
            found = connector.find(id);
            if (null != found) {
                return found;
            }
        }
        return found;
    }

    public void dump(ILogger log, String indent) {
        log.debug(indent + "Root Graphics:");
        for (SketchGraphicGraphNode node : graphicsGraph) {
            node.dump(log, indent);
        }
        log.debug(indent + "Root Connectors:");
        for (BPMNFlowGraphicGraphNode connector : connectorsGraph) {
            connector.dump(log, indent);
        }
    }
}

class BPMNPageGraphicGraphNode extends SketchGraphicGraphNode {

    SketchGraphicGraphNode defaultPool = null;

    BPMNPageGraphicGraphNode(BPMNPageGraphic page, ArrayList<BPMNFlowGraphicGraphNode> deletedConnectors, ArrayList<SketchGraphic> selection) throws SketchpadIdException {
        this.graphic = page.clone();
        Command.findAndReplace(selection, page, this.graphic);
        this.defaultPool = new BPMNPoolGraphicGraphNode(page.getDefaultPool(), deletedConnectors, selection);
        children = new ArrayList<SketchGraphicGraphNode>();
        for (BPMNPoolGraphic child : page.getNonDefaultPools()) {
            if (child.getProcess().isIndependent()) {
                children.add(new BPMNPoolGraphicGraphNode(child, deletedConnectors, selection));
            }
        }
        for (BPMNPoolGraphic child : page.getNonDefaultPools()) {
            if (child.getProcess().isEmbedded()) {
                children.add(new BPMNPoolGraphicGraphNode(child, deletedConnectors, selection));
            }
        }
        for (SketchGraphic child : page.getAllGraphicsIgnoringPools()) {
            if (child.getParent() == page) {
                children.add(new SketchGraphicGraphNode(child, deletedConnectors, selection));
            }
        }
    }

    void restore(BPMNWorkflowDocument document, BPMNPageGraphic page, ContainerGraphic container, boolean remapId, boolean isPaste, HashMap<String, WorkflowProcess> processesMap, ArrayList<SketchGraphic> restoredGraphics) throws SketchpadException {
        BPMNPageGraphic restoredPage = (BPMNPageGraphic) this.graphic;
        if (remapId) {
            restoredPage.setId(document.makeNextId());
        }
        restoredPage.getGraphicsStore().clear();
        restoredPage.removeAllProcesses();
        document.addPageToList(restoredPage);
        if (defaultPool != null) {
            defaultPool.restore(document, restoredPage, null, remapId, isPaste, 0, processesMap, restoredGraphics);
        }
        restoredGraphics.add(restoredPage);
        restoreChildren(document, restoredPage, restoredPage, remapId, isPaste, 0, processesMap, restoredGraphics);
    }

    public void fixup(SketchGraphicGraph graph, HashMap<String, WorkflowProcess> processesMap) {
        if (defaultPool != null) {
            defaultPool.fixup(graph, processesMap);
        }
        super.fixup(graph, processesMap);
    }

    public void dump(ILogger log, String indent) {
        log.debug(indent + graphic.getDumpString());
        if (defaultPool != null) {
            log.debug(indent + "    Default Pool:");
            defaultPool.dump(log, indent + "    ");
        }
        if (children != null) {
            log.debug(indent + "    Children:");
            for (SketchGraphicGraphNode child : children) {
                child.dump(log, indent + "    ");
            }
        }
    }
}

class BPMNPoolGraphicGraphNode extends SketchGraphicGraphNode {

    BPMNPoolGraphicGraphNode(BPMNPoolGraphic pool, ArrayList<BPMNFlowGraphicGraphNode> deletedConnectors, ArrayList<SketchGraphic> selection) {
        super(pool, deletedConnectors, selection);
    }

    void restore(BPMNWorkflowDocument document, BPMNPageGraphic page, ContainerGraphic container, boolean remapId, boolean isPaste, int displacement, HashMap<String, WorkflowProcess> processesMap, ArrayList<SketchGraphic> restoredGraphics) throws SketchpadException {
        BPMNPoolGraphic pool = (BPMNPoolGraphic) this.graphic;
        pool.resetIsClone();
        if (displacement > 0) {
            pool.displace(new Point2D.Double(displacement, displacement));
        }
        if (remapId) {
            pool.setId(document.makeNextId());
            pool.setName(displacement > 0 ? "Copy of " + pool.getName() : pool.getName());
            WorkflowProcess process = pool.getProcess();
            process.getGraphicsStore().clear();
            processesMap.put(process.getId().toString(), process);
            process.setId(document.makeNextId());
            process.setDocument(document);
            process.setName("Copy of " + process.getName());
            if (process.getType().equals(WorkflowProcessType.EMBEDDED)) {
                BpmnId id = process.getParentProcess().getId();
                process.setParentProcess(processesMap.get(id));
            }
        } else {
            WorkflowProcess process = pool.getProcess();
            process.setDocument(document);
        }
        document.insertPool(page, pool);
        restoredGraphics.add(pool);
        restoreChildren(document, page, pool, remapId, isPaste, displacement, processesMap, restoredGraphics);
    }
}

class BPMNLaneGraphicGraphNode extends SketchGraphicGraphNode {

    int lanePosition;

    boolean isPrimary;

    BPMNLaneGraphicGraphNode(BPMNLaneGraphic lane, ArrayList<BPMNFlowGraphicGraphNode> deletedConnectors, ArrayList<SketchGraphic> selection, boolean isPrimary) {
        super(lane, deletedConnectors, selection);
        BPMNPoolGraphic pool = (BPMNPoolGraphic) lane.getParent();
        this.lanePosition = pool.getChildren().indexOf(lane);
        this.isPrimary = isPrimary;
    }

    void restore(BPMNWorkflowDocument document, BPMNPageGraphic page, ContainerGraphic container, boolean remapId, boolean isPaste, int displacement, HashMap<String, WorkflowProcess> processesMap, ArrayList<SketchGraphic> restoredGraphics) throws SketchpadException {
        SketchGraphic graphic = this.graphic;
        graphic.resetIsClone();
        if (remapId) {
            graphic.setId(document.makeNextId());
        }
        if (isPaste && isPrimary) {
            if (displacement > 0) {
                container = (ContainerGraphic) container.getChildren().get(lanePosition);
                restoredGraphics.add(container);
            } else {
                container = page.getDefaultLane();
            }
        } else {
            document.insertLane(page, (BPMNPoolGraphic) container, (BPMNLaneGraphic) graphic, lanePosition, false);
            restoredGraphics.add(graphic);
            container = (ContainerGraphic) graphic;
        }
        restoreChildren(document, page, container, remapId, isPaste, displacement, processesMap, restoredGraphics);
    }
}
