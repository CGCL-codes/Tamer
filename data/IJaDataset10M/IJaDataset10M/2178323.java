package com.global360.sketchpadbpmn.documents.xpdl.elements;

import java.awt.geom.Rectangle2D;
import java.util.*;
import org.jdom.*;
import com.global360.sketchpadbpmn.Utility;
import com.global360.sketchpadbpmn.documents.*;
import com.global360.sketchpadbpmn.documents.idmanager.BpmnId;
import com.global360.sketchpadbpmn.documents.idmanager.SketchpadIdException;
import com.global360.sketchpadbpmn.graphic.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PoolElement extends NodeElement {

    /**
   * makePool
   *
   * @param processId String
   * @param poolId String
   * @param poolName String
   * @param info XPDLNodeGraphicsInfo
   * @param boundaryVisible boolean
   * @return PoolElement
   */
    public static PoolElement makePool(String processId, String poolId, String poolName, BPMNPoolOrientation orientation, XPDLNodeGraphicsInfo info, boolean boundaryVisible, Namespace namespace) {
        PoolElement result = new PoolElement(namespace);
        result.set(processId, poolId, poolName, orientation, info, boundaryVisible);
        return result;
    }

    /**
   * makePool
   *
   * @param source Element
   * @return PoolElement
   */
    public static PoolElement makePool(Element source) {
        PoolElement result = null;
        if (source != null) {
            result = new PoolElement(source);
        }
        return result;
    }

    public PoolElement(Namespace namespace) {
        super(XPDLConstants.S_POOL, namespace);
        addLanesElement();
    }

    public void addLanesElement() {
        Element lanesElement = new Element(XPDLConstants.S_LANES, getNamespace());
        this.insertChildElement(lanesElement, XPDLConstants.S_LANES, false);
    }

    public PoolElement(Element sourceElement) {
        super(sourceElement);
        setElement(sourceElement);
    }

    public Element getLanesElement() {
        return this.myGetChild(XPDLConstants.S_LANES);
    }

    private static ArrayList<String> childElementOrder = null;

    public ArrayList<String> getChildElementOrder() {
        if (childElementOrder == null) {
            childElementOrder = new ArrayList<String>();
            childElementOrder.add(XPDLConstants.S_LANES);
            childElementOrder.add(XPDLConstants.S_OBJECT);
            childElementOrder.add(XPDLConstants.S_NODE_GRAPHICS_INFOS);
        }
        return childElementOrder;
    }

    public Boolean getBoundaryVisible() {
        Boolean result = this.getBooleanAttributeValue(XPDLConstants.S_BOUNDARY_VISIBLE);
        return result;
    }

    /**
   * getProcessId
   *
   * @return String
   */
    public String getProcessId() {
        return this.getStringAttributeValue(XPDLConstants.S_PROCESS);
    }

    /**
   * getOrientation
   *
   * @return String
   */
    public String getOrientation() {
        return this.getStringAttributeValue(XPDLConstants.S_ORIENTATION);
    }

    /**
   * getParticipantId
   *
   * @return String
   */
    public String getParticipantId() {
        return this.getStringAttributeValue(XPDLConstants.S_PARTICIPANT);
    }

    /**
   * getLaneElements
   *
   * @return List
   */
    public List<LaneElement> getLaneElements() {
        List<LaneElement> result = new ArrayList<LaneElement>();
        if (getLanesElement() != null) {
            List<Element> children = XpdlElement.getChildren(getLanesElement(), XPDLConstants.S_LANE);
            for (Element child : children) {
                LaneElement laneElement = new LaneElement(child);
                result.add(laneElement);
            }
        }
        return result;
    }

    public Rectangle2D.Double getLaneBounds() {
        Rectangle2D.Double result = null;
        List<LaneElement> lanes = this.getLaneElements();
        for (LaneElement lane : lanes) {
            Rectangle2D.Double laneBounds = lane.getBounds(null);
            if (Utility.isValid(laneBounds)) {
                if (result == null) {
                    result = laneBounds;
                } else {
                    result.add(laneBounds);
                }
            } else {
                return null;
            }
        }
        return result;
    }

    /**
   * setBoundaryVisible
   *
   * @param boundaryVisible boolean
   */
    public void setBoundaryVisible(boolean boundaryVisible) {
        this.setBooleanAttribute(XPDLConstants.S_BOUNDARY_VISIBLE, new Boolean(boundaryVisible));
    }

    public void setOrientation(BPMNPoolOrientation orientation) {
        this.setStringAttribute(XPDLConstants.S_ORIENTATION, orientation.getValue());
    }

    /**
   * setProcessId
   *
   * @param processId String
   */
    public void setProcessId(String processId) {
        this.setStringAttribute(XPDLConstants.S_PROCESS, processId);
    }

    /**
   * set
   *
   * @param processId String
   * @param poolId String
   * @param poolName String
   * @param info XPDLNodeGraphicsInfo
   * @param boundaryVisible boolean
   */
    public void set(String processId, String poolId, String poolName, BPMNPoolOrientation orientation, XPDLNodeGraphicsInfo info, boolean boundaryVisible) {
        this.setProcessId(processId);
        this.setId(poolId);
        this.setName(poolName);
        this.setOrientation(orientation);
        this.setInfo(info);
        this.setBoundaryVisible(boundaryVisible);
    }

    public void set(String toolId, String processId, String poolId, String poolName, XPDLNodeGraphicsInfo info, boolean boundaryVisible) {
        this.setProcessId(processId);
        this.setId(poolId);
        this.setName(poolName);
        this.setInfo(info);
        this.setBoundaryVisible(boundaryVisible);
    }

    public void set(String toolId, String processId, String poolId, String poolName, XPDLNodeGraphicsInfo info, boolean boundaryVisible, double paperWidth, double paperHeight, int paperOrientation, double marginLeft, double marginTop, double marginRight, double marginBottom) {
        this.set(toolId, processId, poolId, poolName, info, boundaryVisible);
        this.setPageInfo(toolId, paperWidth, paperHeight, paperOrientation, marginLeft, marginTop, marginRight, marginBottom);
    }

    public void setPageInfo(String toolId, double paperWidth, double paperHeight, Integer paperOrientation, double marginLeft, double marginTop, double marginRight, double marginBottom) {
        NodeGraphicsInfoElement infoElement = this.getNodeGraphicsInfoElement(toolId);
        if (infoElement != null) {
            infoElement.getElement().removeChild(XPDLConstants.S_PAGEINFO, getG360Namespace());
            G360PageInfoElement pageInfoElement = new G360PageInfoElement();
            pageInfoElement.setPaperWidth(paperWidth);
            pageInfoElement.setPaperHeight(paperHeight);
            pageInfoElement.setPaperOrientation(paperOrientation);
            pageInfoElement.setMarginLeft(marginLeft);
            pageInfoElement.setMarginTop(marginTop);
            pageInfoElement.setMarginRight(marginRight);
            pageInfoElement.setMarginBottom(marginBottom);
            infoElement.insertChildElement(pageInfoElement.getElement(), XPDLConstants.S_PAGEINFO, true);
        }
    }

    public Boolean getIsMainPool() {
        return this.getBooleanAttributeValue(XPDLConstants.S_MAIN_POOL);
    }

    public void setIsMainPool(Boolean value) {
        this.setBooleanAttribute(XPDLConstants.S_MAIN_POOL, value);
    }

    /**
   * removeAllLanes
   */
    public void removeAllLanes() {
        if (getLanesElement() != null) {
            getLanesElement().removeContent();
        }
    }

    /**
   * addLane
   *
   * @param laneElement LaneElement
   */
    public void addLane(LaneElement laneElement) {
        if (getLanesElement() == null) {
            addLanesElement();
        }
        getLanesElement().addContent(laneElement.getElement());
    }

    /**
   * getLaneCount
   *
   * @return int
   */
    public int getLaneCount() {
        int result = 0;
        if (getLanesElement() != null) {
            List<Element> lanes = XpdlElement.getChildren(getLanesElement(), XPDLConstants.S_LANE);
            if (lanes != null) {
                result = lanes.size();
            }
        }
        return result;
    }

    public LaneElement getFirstLane() {
        LaneElement result = null;
        List<LaneElement> lanes = this.getLaneElements();
        if (lanes.size() > 0) {
            result = lanes.get(0);
        }
        return result;
    }

    /**
   * makeGraphic
   *
   * @param parentGraphic ContainerGraphic
   * @return BPMNPoolGraphic
 * @throws SketchpadIdException 
   */
    public BPMNPoolGraphic makeGraphic(ContainerGraphic parentGraphic) throws SketchpadIdException {
        BPMNPoolGraphic result = new BPMNPoolGraphic(parentGraphic);
        XPDLNodeGraphicsInfo info = this.getNodeGraphicsInfo();
        result.setId(new BpmnId(this.getId()));
        result.setName(this.getName());
        if (null != info) {
            result.setBounds(info.getBounds());
            result.setLaneId(new BpmnId(info.getLaneId()));
            result.setFillColor(info.getFillColor());
        }
        result.setParent(parentGraphic);
        String orientationString = this.getOrientation();
        if (orientationString == null) {
            orientationString = BPMNPoolOrientation.S_HORIZONTAL;
        }
        result.setOrientation(new BPMNPoolOrientation(orientationString));
        result.setBoundaryVisible(this.getBoundaryVisible());
        return result;
    }

    /**
   * findLane
   *
   * @param laneId String
   * @return LaneElement
   */
    public LaneElement findLane(String laneId) {
        LaneElement result = null;
        if (this.getLanesElement() != null) {
            Element element = this.findChildWithAttribute(this.getLanesElement(), XPDLConstants.S_LANE, XPDLConstants.S_ID, laneId);
            result = LaneElement.makeLane(element);
        }
        return result;
    }

    /**
   * removeLane
   *
   * @param laneElement LaneElement
   */
    public void removeLane(LaneElement laneElement) {
        if (this.getLanesElement() != null) {
            this.getLanesElement().removeContent(laneElement.getElement());
        }
    }

    public String getPageId() {
        String result = null;
        XPDLNodeGraphicsInfo info = this.getNodeGraphicsInfo();
        if (info != null) {
            result = info.getPageId();
        }
        return result;
    }
}
