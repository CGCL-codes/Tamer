package com.global360.sketchpadbpmn.documents.loader;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.jdom.Element;
import com.global360.sketchpadbpmn.SketchpadDefaults;
import com.global360.sketchpadbpmn.SketchpadException;
import com.global360.sketchpadbpmn.Utility;
import com.global360.sketchpadbpmn.WorkflowProcess;
import com.global360.sketchpadbpmn.WorkflowProcessType;
import com.global360.sketchpadbpmn.documents.ActivityAssignment;
import com.global360.sketchpadbpmn.documents.ActivityLoop;
import com.global360.sketchpadbpmn.documents.BPMNMessage;
import com.global360.sketchpadbpmn.documents.BPMNWorkflowDocument;
import com.global360.sketchpadbpmn.documents.BlockActivityCall;
import com.global360.sketchpadbpmn.documents.Participant;
import com.global360.sketchpadbpmn.documents.StringEnumeration;
import com.global360.sketchpadbpmn.documents.XPDLConnectorGraphicsInfo;
import com.global360.sketchpadbpmn.documents.XPDLConstants;
import com.global360.sketchpadbpmn.documents.XPDLNodeGraphicsInfo;
import com.global360.sketchpadbpmn.documents.XPDLSubflowCall;
import com.global360.sketchpadbpmn.documents.XPDLTransitionCondition;
import com.global360.sketchpadbpmn.documents.XPDLWorkflowDocument;
import com.global360.sketchpadbpmn.documents.XpdlVersion;
import com.global360.sketchpadbpmn.documents.idmanager.BpmnId;
import com.global360.sketchpadbpmn.documents.idmanager.SketchpadIdException;
import com.global360.sketchpadbpmn.documents.mapping.CoordinateMapperFactory;
import com.global360.sketchpadbpmn.documents.task.XPDLTask;
import com.global360.sketchpadbpmn.documents.xpdl.ActivityViewState;
import com.global360.sketchpadbpmn.documents.xpdl.TimerType;
import com.global360.sketchpadbpmn.documents.xpdl.elements.ActivityElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.ActivitySetElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.ArtifactElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.AssociationBaseElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.AssociationElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.ConditionElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.ConnectorGraphicsInfoElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.DataArtifactElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.EventElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.ExtendedAttributesElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.G360PageInfoElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.GroupArtifactElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.LaneElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.MessageElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.MessageFlowElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.NodeGraphicsInfoElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.PackageHeaderElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.PoolElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.ProcessElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.TextAnnotationElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.TransitionElement;
import com.global360.sketchpadbpmn.documents.xpdl.elements.XpdlElement;
import com.global360.sketchpadbpmn.graphic.BPMNActivityGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNDataGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNEventGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNEventTriggerType;
import com.global360.sketchpadbpmn.graphic.BPMNEventType;
import com.global360.sketchpadbpmn.graphic.BPMNFlowGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNFlowType;
import com.global360.sketchpadbpmn.graphic.BPMNGatewayType;
import com.global360.sketchpadbpmn.graphic.BPMNGatewayXorType;
import com.global360.sketchpadbpmn.graphic.BPMNGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNGroupGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNLaneGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNPageGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNPoolGraphic;
import com.global360.sketchpadbpmn.graphic.BPMNPoolOrientation;
import com.global360.sketchpadbpmn.graphic.BPMNTextGraphic;
import com.global360.sketchpadbpmn.graphic.ContainerGraphic;
import com.global360.sketchpadbpmn.graphic.SketchGraphic;
import com.global360.sketchpadbpmn.utility.EnclosingArea;
import com.global360.sketchpadbpmn.utility.ILogger;

/**
 * @author andya
 *
 */
public class XpdlDocumentLoader_2_0 extends XpdlDocumentLoader {

    private static final String BRUCE_SILVER_1 = "itp commerce + BSilver xslt";

    private static final String BRUCE_SILVER_2 = "Bruce Silver from Visio 2010 vdx";

    private ArrayList<BPMNPageGraphic> pagesThatNeedFixedBounds = new ArrayList<BPMNPageGraphic>();

    protected ArrayList<BPMNActivityGraphic> activityCalls = new ArrayList<BPMNActivityGraphic>();

    private Preprocessor preprocessor = null;

    public XpdlDocumentLoader_2_0(ILogger logger, XPDLWorkflowDocument source, BPMNWorkflowDocument target) {
        super(logger, source, target);
        this.version = XpdlVersion.find(2, 0, XpdlVersion.NULL_MODIFIER);
        this.coordinateMapper = CoordinateMapperFactory.make(this.xpdlDocument.getVendor(), this.bpmnDocument);
        this.activitesWithSplitTransitionRestrictions = new ArrayList<ActivityElement>();
    }

    public void loadDocument() throws SketchpadException {
        preprocess();
        repairAnomalies();
        this.bpmnDocument.setErrorLog(this.messages);
        loadPackageLevelData();
        int pageCount = loadPages();
        this.bCreatePagesForProcesses = (pageCount == 0);
        loadProcesses();
        loadPools();
        loadProcessNodes();
        loadArtifacts();
        loadProcessConnectors();
        loadMessageFlows();
        loadAssociations();
        fixActivityCalls(activityCalls);
        this.bpmnDocument.updateAllGroupGraphics();
        fixPageBounds();
        this.bpmnDocument.setFileName(this.xpdlDocument.getPath());
        this.bpmnDocument.setErrorLog(null);
    }

    protected Preprocessor getPreprocessor() {
        if (preprocessor == null) {
            preprocessor = new Preprocessor(xpdlDocument);
            preprocessor.analyse();
        }
        return preprocessor;
    }

    private void preprocess() {
        Preprocessor p = getPreprocessor();
        p.report();
        p.resolvePreprocesserAnomalies();
        this.setDocumentWasAltered(p.getDocumentWasAltered());
    }

    protected boolean isDefaultPoolAnomalous(PoolElement pool) {
        if (pool != null) {
            if (true) return false;
            if (pool.getLaneCount() > 1) {
                return true;
            }
            XPDLNodeGraphicsInfo info = this.getNodeGraphicsInfo(pool);
            if (info != null) {
                if ((info.getX() != 0.0) || (info.getY() != 0.0)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void repairAnomalies() throws SketchpadException {
        List<Element> poolElements = this.xpdlDocument.getPools();
        if (poolElements != null) {
            for (Element element : poolElements) {
                PoolElement poolElement = new PoolElement(element);
                if (this.isPagePool(poolElement)) {
                    if (isDefaultPoolAnomalous(poolElement)) {
                        String processId = poolElement.getProcessId();
                        BpmnId poolId = this.makeNextId();
                        String poolIdString = Utility.toString(poolId);
                        String poolName = poolElement.getName();
                        BPMNPoolOrientation orientation = new BPMNPoolOrientation(poolElement.getOrientation());
                        XPDLNodeGraphicsInfo oldInfo = this.getNodeGraphicsInfo(poolElement);
                        XPDLNodeGraphicsInfo newInfo = new XPDLNodeGraphicsInfo(oldInfo);
                        newInfo.setLocation(new Point2D.Double(0, 0));
                        newInfo.setWidth(SketchpadDefaults.getPageWidth());
                        newInfo.setHeight(SketchpadDefaults.getPageHeight());
                        boolean boundaryVisible = false;
                        PoolElement newDefaultPool = PoolElement.makePool(processId, poolIdString, poolName, orientation, newInfo, boundaryVisible, poolElement.getNamespace());
                        this.xpdlDocument.getPackage().addPool(newDefaultPool);
                        poolElement.setBoundaryVisible(true);
                        poolElement.setId(poolIdString);
                        poolElement.setName(poolName);
                    }
                }
            }
        }
    }

    protected void fixActivityCalls(ArrayList<BPMNActivityGraphic> calls) {
        for (BPMNActivityGraphic caller : calls) {
            if (caller.getViewState().equals(ActivityViewState.Expanded)) {
                WorkflowProcess process = caller.getCalledProcess();
                if (process != null) {
                    process.addContentToActivity(caller);
                }
            }
        }
    }

    protected void loadPackageLevelData() throws SketchpadException {
        String packageId = this.xpdlDocument.getPackageId();
        if (!Utility.isStringEmpty(packageId)) {
            this.bpmnDocument.setPackageId(new BpmnId(packageId));
        }
        String packageName = this.xpdlDocument.getPackageName();
        if (!Utility.isStringEmpty(packageName)) {
            this.bpmnDocument.setPackageName(packageName);
        }
        ExtendedAttributesElement extendedAttributesElement = this.xpdlDocument.getExtendedAttributesElement();
        if (extendedAttributesElement != null) {
            this.bpmnDocument.setExtendedAttributes(extendedAttributesElement.getStore());
        }
        Helpers.copyDataFields(this.xpdlDocument.getDataFieldsElement(), this.bpmnDocument, null);
        Helpers.copyParticipants(this.xpdlDocument.getParticipantsElement(), this.bpmnDocument, null);
        Helpers.copyApplications(this.xpdlDocument.getApplicationsElement(), this.bpmnDocument, null);
        PackageHeaderElement packageHeaderElement = this.xpdlDocument.getPackage().getPackageHeaderElement(false);
        if (packageHeaderElement != null) {
            this.bpmnDocument.setDotsPerInch(packageHeaderElement.getDotsPerInch());
        }
    }

    protected int loadPages() throws SketchpadIdException {
        int pageCount = 0;
        List<Element> poolElements = this.xpdlDocument.getPools();
        if (poolElements != null) {
            for (Element element : poolElements) {
                PoolElement poolElement = new PoolElement(element);
                if (this.isPagePool(poolElement)) {
                    if (checkPagePool(poolElement)) {
                        makePageFromPool(poolElement, this.bpmnDocument);
                        pageCount++;
                    } else {
                        this.xpdlDocument.removePool(poolElement.getId());
                    }
                }
            }
        }
        return pageCount;
    }

    protected void fixPageBounds() throws SketchpadIdException {
        for (BPMNPageGraphic page : this.bpmnDocument.getPages()) {
            Rectangle2D.Double contentBounds = page.getContentBoundsIgnoringDefaultPool();
            contentBounds = page.getContentBounds();
            Rectangle2D.Double pageBounds = page.getBounds();
            if (!pageBounds.contains(contentBounds)) {
                double paddingPercentage = 0.02;
                double xPadding = 0.0;
                double yPadding = 0.0;
                double pageLabelHeight = page.getTextBlockBounds().height;
                if (pageBounds.getWidth() < contentBounds.getWidth()) xPadding = contentBounds.getWidth() * paddingPercentage;
                if ((pageBounds.getHeight() - pageLabelHeight) < contentBounds.getHeight()) {
                    yPadding = (contentBounds.getHeight() * paddingPercentage) + pageLabelHeight;
                }
                this.bpmnDocument.resizePage(page, (int) (contentBounds.getWidth() + xPadding), (int) (contentBounds.getHeight() + yPadding));
            }
        }
    }

    private boolean checkPagePool(PoolElement poolElement) {
        return true;
    }

    /**
	 * makePageFromPool
	 *
	 * @param poolElement PoolElement
	 * @param targetDocument WorkflowDocument
	 * @throws SketchpadIdException 
	 */
    private void makePageFromPool(PoolElement poolElement, BPMNWorkflowDocument targetDocument) throws SketchpadIdException {
        XPDLNodeGraphicsInfo poolInfo = this.getNodeGraphicsInfo(poolElement);
        String laneId = null;
        if (poolInfo == null) {
            laneId = "";
            if (!poolElement.getBoundaryVisible()) {
                poolInfo = new XPDLNodeGraphicsInfo();
                BpmnId pageId = this.makeNextId();
                poolInfo.set(pageId.toString(), 0, 0, 2400, 2400, null, null, null, null, null, null, "", null);
            }
        } else {
            laneId = poolInfo.getLaneId();
        }
        if (!poolElement.getBoundaryVisible()) {
            if (poolInfo.getPageId() == null) {
                BpmnId pageId = this.makeNextId();
                poolInfo.setPageId(pageId.toString());
            }
        }
        BPMNPageGraphic page = targetDocument.loadPage(poolInfo.getPageId(), poolElement.getName(), poolElement.getId(), laneId, poolElement.getProcessId(), poolInfo);
        loadLanes(poolElement, page.getDefaultPool());
        if ((poolInfo != null) && ((poolInfo.getWidth() == 0.0) || (poolInfo.getHeight() == 0.0))) {
            this.pagesThatNeedFixedBounds.add(page);
        }
        G360PageInfoElement pageInfoElement = poolElement.getPageInfoElement(getToolId());
        if (pageInfoElement != null) {
            page.setPaperSize(pageInfoElement.getPaperWidth(), pageInfoElement.getPaperHeight());
            page.setPaperOrientation(pageInfoElement.getPaperOrientation());
            page.setMarginLeft(pageInfoElement.getMarginLeft());
            page.setMarginTop(pageInfoElement.getMarginTop());
            page.setMarginRight(pageInfoElement.getMarginRight());
            page.setMarginBottom(pageInfoElement.getMarginBottom());
        }
    }

    /**
	 * @throws SketchpadException 
	 *
	 */
    private void loadProcesses() throws SketchpadException {
        List<ActivitySetElement> processElements = this.xpdlDocument.getAllProcesses(false);
        for (ActivitySetElement process : processElements) {
            this.bpmnDocument.insertIndependentProcess(process.getId(), process.getName());
        }
        List<ActivitySetElement> processesAndActvitySets = this.xpdlDocument.getAllProcesses(false);
        List<ActivitySetElement> processesAndActvitySets2 = this.xpdlDocument.getAllProcesses(false);
        for (ActivitySetElement activitySetOrProcess : processesAndActvitySets2) {
            String processId = activitySetOrProcess.getId();
            String parentPageId = loadProcess(activitySetOrProcess);
            if (activitySetOrProcess.isProcess()) {
                ProcessElement process = (ProcessElement) activitySetOrProcess;
                List<ActivitySetElement> activitySets = process.getActivitySets();
                processesAndActvitySets.addAll(activitySets);
                ArrayList<ActivitySetElement> activitySetList = process.getActivitySets();
                for (ActivitySetElement activitySet : activitySetList) {
                    loadActivitySet(processId, parentPageId, activitySet);
                }
            }
            loadProcessLevelData(activitySetOrProcess, processId);
        }
    }

    protected void loadProcessLevelData(ActivitySetElement activitySetOrProcess, String processId) throws SketchpadException {
        Helpers.copyDataFields(activitySetOrProcess.getDataFieldsElement(), this.bpmnDocument, processId);
        Helpers.copyFormalParameters(activitySetOrProcess.getFormalParametersElement(), this.bpmnDocument, processId);
        Helpers.copyParticipants(activitySetOrProcess.getParticipantsElement(), this.bpmnDocument, processId);
        Helpers.copyApplications(activitySetOrProcess.getApplicationsElement(), this.bpmnDocument, processId);
    }

    private String loadActivitySet(String parentProcessId, String parentPageId, ActivitySetElement activitySet) throws SketchpadException {
        log.debug("copyActivitySetTo: parentProcessId=" + parentProcessId + " element=" + activitySet);
        String id = activitySet.getId();
        String name = activitySet.getName();
        WorkflowProcess process = (WorkflowProcess) this.bpmnDocument.insertEmbeddedProcess(id, name, parentProcessId, parentPageId, activitySet.getIsAdHoc(), activitySet.getAdHocOrdering(), activitySet.getAdHocCompletionCondition());
        log.debug("copyActivitySetTo: Created embedded process:" + process);
        return id;
    }

    public String loadProcess(ActivitySetElement processElement) throws SketchpadIdException {
        log.debug("copyProcessTo: " + processElement);
        String processId = processElement.getId();
        String pageId = processId;
        String processName = processElement.getName();
        String parentId = null;
        XPDLNodeGraphicsInfo info = this.getNodeGraphicsInfo(processElement);
        if ((info != null) && (info.getPageId() != null)) {
            pageId = info.getPageId();
        }
        WorkflowProcess process = null;
        process = (WorkflowProcess) this.bpmnDocument.insertProcess(pageId, WorkflowProcessType.INDEPENDENT, processId, processName, info, parentId);
        if (this.bCreatePagesForProcesses && false) {
            BPMNPageGraphic page = (BPMNPageGraphic) this.bpmnDocument.loadPage(null, null, null, null, processId, null);
            pageId = page.getIdString();
        }
        process.setDescription(processElement.getDescription());
        ExtendedAttributesElement extendedAttributesElement = processElement.getExtendedAttributesElement(false);
        if (extendedAttributesElement != null) {
            process.setExtendedAttributes(extendedAttributesElement.getStore());
        }
        return pageId;
    }

    public int loadPools() throws SketchpadException {
        int poolCount = 0;
        List<Element> poolElements = this.xpdlDocument.getPools();
        for (Element element : poolElements) {
            PoolElement poolElement = new PoolElement(element);
            element.setNamespace(element.getNamespace());
            if (!this.isPagePool(poolElement)) {
                loadPool(poolElement);
            }
            poolCount++;
        }
        return poolCount;
    }

    private boolean isPagePool(PoolElement poolElement) {
        Boolean boundaryVisible = poolElement.getBoundaryVisible();
        boolean isPagePool = false;
        XPDLNodeGraphicsInfo info = this.getNodeGraphicsInfo(poolElement);
        if (boundaryVisible == null) {
            if (info == null) {
                isPagePool = true;
            }
        } else {
            isPagePool = !boundaryVisible.booleanValue();
        }
        return isPagePool;
    }

    private void loadPool(PoolElement poolElement) throws SketchpadException {
        log.debug("loadPool: " + " pool=" + poolElement);
        BPMNPoolGraphic pool = null;
        XPDLNodeGraphicsInfo info = null;
        String pageId = null;
        String poolId = poolElement.getId();
        String name = poolElement.getName();
        String processId = poolElement.getProcessId();
        Participant participant = new Participant(this.bpmnDocument);
        participant.setId(new BpmnId(poolElement.getParticipantId()));
        BPMNPageGraphic page = null;
        Boolean boundaryVisible = poolElement.getBoundaryVisible();
        boolean isPagePool = false;
        boolean isWithoutBounds = false;
        info = this.getNodeGraphicsInfo(poolElement);
        if (null == info) {
            if (boundaryVisible) {
                isWithoutBounds = true;
            } else {
                info = derivePoolInfoFromLanes(poolElement);
                poolElement.setInfo(info);
            }
        }
        if (null != info) {
            pageId = info.getPageId();
            if (Double.isNaN(info.getX())) info.setX(0);
            if (Double.isNaN(info.getY())) info.setY(0);
            if (Double.isNaN(info.getWidth())) info.setWidth(Utility.convertPointsToInternal(BPMNPageGraphic.getDefaultPaperSize().getWidth()));
            if (Double.isNaN(info.getHeight())) info.setHeight(Utility.convertPointsToInternal(BPMNPageGraphic.getDefaultPaperSize().getHeight()));
        }
        if (boundaryVisible == null) {
            if (info == null) {
                isPagePool = true;
            }
        } else {
            isPagePool = !boundaryVisible.booleanValue();
        }
        if (info != null) {
            page = this.bpmnDocument.getPage(info.getPageId(), processId);
        }
        if (isPagePool == true) {
            if (page == null) {
                page = (BPMNPageGraphic) this.bpmnDocument.loadPage(pageId, name, poolId, null, processId, info);
                pool = loadPool(poolElement, page);
                page.setDefaultPool(pool);
                pool.setAllBounds(page.getBounds());
                this.bpmnDocument.loadPool(pool, page);
                if (true) {
                    fixAnomalousDefaultPool(poolElement, poolId, page);
                }
            } else if (isWithoutBounds) {
                Rectangle2D.Double pageBounds = page.getBounds();
                info.setBounds(pageBounds);
                isWithoutBounds = false;
            }
        } else {
            if (page == null) {
            } else {
                BPMNPoolGraphic parentPool = page.getDefaultPool();
                pool = loadPool(poolElement, parentPool);
                pool.setAllBounds(pool.getBounds());
                this.bpmnDocument.loadPool(pool, parentPool);
            }
        }
        loadLanes(poolElement, pool);
    }

    private BPMNPoolOrientation getOrientation(PoolElement poolElement) {
        BPMNPoolOrientation result = null;
        String orientationString = poolElement.getOrientation();
        if (Utility.isStringEmpty(orientationString)) {
            result = new BPMNPoolOrientation(BPMNPoolOrientation.HORIZONTAL);
        } else {
            result = new BPMNPoolOrientation(orientationString);
        }
        return result;
    }

    protected XPDLNodeGraphicsInfo derivePoolInfoFromLanes(PoolElement poolElement) {
        XPDLNodeGraphicsInfo result = null;
        BPMNPoolOrientation orientation = this.getOrientation(poolElement);
        String pageId = null;
        Rectangle2D.Double bounds = null;
        EnclosingArea area = new EnclosingArea();
        List<LaneElement> laneElementList = poolElement.getLaneElements();
        for (LaneElement laneElement : laneElementList) {
            XPDLNodeGraphicsInfo laneInfo = this.getNodeGraphicsInfo(laneElement);
            if (null == laneInfo) {
                laneInfo = new XPDLNodeGraphicsInfo();
                if (this.isFromBpmn()) {
                    int index = laneElement.getId().lastIndexOf('.');
                    if (index != -1) {
                        pageId = "Page" + laneElement.getId().substring(index + 1);
                    }
                }
                laneInfo.setPageId(pageId);
                laneInfo.setBounds(this.getContentBounds(laneElement));
                laneElement.setInfo(laneInfo);
            }
            if (null != laneInfo) {
                pageId = laneInfo.getPageId();
                area.enclose(laneInfo.getBounds());
            }
        }
        if (area.isSet()) {
            area.expand(10);
            bounds = area.getBounds();
        } else {
            bounds = getContentBounds(poolElement);
        }
        bounds = BPMNPoolGraphic.getExternalBounds(bounds, orientation);
        result = new XPDLNodeGraphicsInfo();
        result.setPageId(pageId);
        result.setBounds(bounds);
        Boolean isVisible = poolElement.getIsMainPool();
        if (isVisible == null) isVisible = Boolean.FALSE;
        result.setIsVisible(isVisible);
        return result;
    }

    private Rectangle2D.Double getContentBounds(PoolElement poolElement) {
        double smallestLeft = Double.MAX_VALUE;
        double smallestTop = Double.MAX_VALUE;
        double biggestRight = 0;
        double biggestBottom = 0;
        ArrayList<String> laneIdList = new ArrayList<String>();
        List<LaneElement> laneElementList = poolElement.getLaneElements();
        for (LaneElement laneElement : laneElementList) {
            laneIdList.add(laneElement.getId());
        }
        List<ActivitySetElement> comprehensiveProcessList = this.xpdlDocument.getAllProcesses(true);
        for (ActivitySetElement process : comprehensiveProcessList) {
            for (ActivityElement activity : process.getActivitiesList()) {
                XPDLNodeGraphicsInfo activityInfo = this.getNodeGraphicsInfo(activity);
                if ((activityInfo != null) && matches(activityInfo.getLaneId(), laneIdList)) {
                    if (activityInfo.getX() < smallestLeft) smallestLeft = activityInfo.getX();
                    if (activityInfo.getY() < smallestTop) smallestTop = activityInfo.getY();
                    double right = activityInfo.getX() + activityInfo.getWidth();
                    if (right > biggestRight) biggestRight = right;
                    double bottom = activityInfo.getY() + activityInfo.getHeight();
                    if (bottom > biggestBottom) biggestBottom = bottom;
                }
            }
        }
        double PAD_SIZE = 20;
        smallestLeft -= PAD_SIZE;
        if (smallestLeft < 0) smallestLeft = 0;
        smallestTop -= PAD_SIZE;
        if (smallestTop < 0) smallestTop = 0;
        double width = (biggestRight - smallestLeft) + (2 * PAD_SIZE);
        double height = (biggestBottom - smallestTop) + (2 * PAD_SIZE);
        Rectangle2D.Double bounds = new Rectangle2D.Double(smallestLeft, smallestTop, width, height);
        return bounds;
    }

    private Rectangle2D.Double getContentBounds(LaneElement laneElement) {
        double smallestLeft = Double.MAX_VALUE;
        double smallestTop = Double.MAX_VALUE;
        double biggestRight = 0;
        double biggestBottom = 0;
        String laneId = laneElement.getId();
        List<ActivitySetElement> comprehensiveProcessList = this.xpdlDocument.getAllProcesses(true);
        for (ActivitySetElement process : comprehensiveProcessList) {
            for (ActivityElement activity : process.getActivitiesList()) {
                XPDLNodeGraphicsInfo activityInfo = this.getNodeGraphicsInfo(activity);
                if ((activityInfo != null) && activityInfo.getLaneId().equalsIgnoreCase(laneId)) {
                    if (activityInfo.getX() < smallestLeft) smallestLeft = activityInfo.getX();
                    if (activityInfo.getY() < smallestTop) smallestTop = activityInfo.getY();
                    double right = activityInfo.getX() + activityInfo.getWidth();
                    if (right > biggestRight) biggestRight = right;
                    double bottom = activityInfo.getY() + activityInfo.getHeight();
                    if (bottom > biggestBottom) biggestBottom = bottom;
                }
            }
        }
        double PAD_SIZE = 20;
        smallestLeft -= PAD_SIZE;
        if (smallestLeft < 0) smallestLeft = 0;
        smallestTop -= PAD_SIZE;
        if (smallestTop < 0) smallestTop = 0;
        double width = (biggestRight - smallestLeft) + (2 * PAD_SIZE);
        double height = (biggestBottom - smallestTop) + (2 * PAD_SIZE);
        Rectangle2D.Double bounds = new Rectangle2D.Double(smallestLeft, smallestTop, width, height);
        return bounds;
    }

    private boolean matches(String targetId, ArrayList<String> idList) {
        if (Utility.isStringEmpty(targetId)) return false;
        for (String id : idList) {
            if (targetId.equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    protected void fixAnomalousDefaultPool(PoolElement poolElement, String poolId, BPMNPageGraphic page) throws SketchpadException {
        BPMNPoolGraphic defaultPool = page.getDefaultPool();
        BPMNLaneGraphic defaultLane = defaultPool.getLane(0);
        defaultLane.setId(this.makeNextId());
        Element duplicateElement = (Element) poolElement.getElement().clone();
        PoolElement duplicatePoolElement = PoolElement.makePool(duplicateElement);
        duplicatePoolElement.setBoundaryVisible(true);
        duplicatePoolElement.setId(poolId + "-duplicate");
        duplicatePoolElement.setName(poolElement.getName() + "-duplicate");
        BPMNPoolGraphic parentPool = page.getDefaultPool();
        BPMNPoolGraphic duplicatePool = loadPool(duplicatePoolElement, parentPool);
        duplicatePool.setAllBounds(duplicatePool.getBounds());
        this.bpmnDocument.loadPool(duplicatePool, parentPool);
        loadLanes(duplicatePoolElement, duplicatePool);
    }

    protected void fixDefaultPool(PoolElement poolElement) throws SketchpadException {
        BPMNPoolGraphic pool = null;
        XPDLNodeGraphicsInfo info = null;
        String pageId = null;
        String poolId = poolElement.getId();
        String name = poolElement.getName();
        String processId = poolElement.getProcessId();
        Participant participant = new Participant(this.bpmnDocument);
        participant.setId(new BpmnId(poolElement.getParticipantId()));
        BPMNPageGraphic page = null;
        Boolean boundaryVisible = poolElement.getBoundaryVisible();
        boolean isPagePool = false;
        info = this.getNodeGraphicsInfo(poolElement);
        if (null != info) {
            pageId = info.getPageId();
            if (Double.isNaN(info.getWidth())) info.setWidth(Utility.convertPointsToInternal(BPMNPageGraphic.getDefaultPaperSize().getWidth()));
            if (Double.isNaN(info.getHeight())) info.setHeight(Utility.convertPointsToInternal(BPMNPageGraphic.getDefaultPaperSize().getHeight()));
        }
        if (boundaryVisible == null) {
            if (info == null) {
                isPagePool = true;
            }
        } else {
            isPagePool = !boundaryVisible.booleanValue();
        }
        XPDLNodeGraphicsInfo poolInfo = this.getNodeGraphicsInfo(poolElement);
        if (poolInfo != null) {
            page = this.bpmnDocument.getPage(poolInfo.getPageId(), processId);
        }
        if (isPagePool == true) {
            if (page == null) {
                page = (BPMNPageGraphic) this.bpmnDocument.loadPage(pageId, name, poolId, null, processId, info);
                pool = loadPool(poolElement, page);
                page.setDefaultPool(pool);
                pool.setAllBounds(page.getBounds());
                this.bpmnDocument.loadPool(pool, page);
                fixAnomalousDefaultPool(poolElement, poolId, page);
            }
        } else {
            BPMNPoolGraphic parentPool = page.getDefaultPool();
            pool = loadPool(poolElement, parentPool);
            pool.setAllBounds(pool.getBounds());
            this.bpmnDocument.loadPool(pool, parentPool);
        }
        loadLanes(poolElement, pool);
    }

    protected void loadLanes(PoolElement poolElement, BPMNPoolGraphic pool) throws SketchpadIdException {
        List<LaneElement> laneElements = poolElement.getLaneElements();
        BPMNLaneGraphic lane;
        boolean isLaneSingle = (laneElements.size() == 1);
        if (laneElements.size() > 0) {
            ContainerGraphic parent = pool.getParent();
            BPMNPageGraphic parentPage = null;
            if (parent instanceof BPMNPageGraphic) {
                parentPage = (BPMNPageGraphic) parent;
            }
            for (BPMNLaneGraphic preexistingLane : pool.getLanes()) {
                if (parentPage == null) {
                    pool.removeLane(preexistingLane);
                } else {
                    parentPage.remove(preexistingLane);
                }
            }
        }
        for (LaneElement laneElement : laneElements) {
            String laneId = laneElement.getId();
            String name = laneElement.getName();
            log.debug("XPDL DOC copyPoolTo: Copying Lane id=" + laneId + " name: " + name);
            lane = laneElement.makeGraphic(pool);
            lane.setId(new BpmnId(laneId));
            lane.setName(name);
            this.coordinateMapper.mapGraphic(pool, lane);
            pool.setLaneBounds(lane, lane.getBounds(), false);
            lane.setIsSingle(isLaneSingle);
            this.bpmnDocument.loadLane(lane, pool);
        }
        pool.layoutLanesAndUpdateSelf();
    }

    private BPMNPoolGraphic loadPool(PoolElement poolElement, ContainerGraphic parent) throws SketchpadException {
        BPMNPoolGraphic result = poolElement.makeGraphic(parent);
        BpmnId participantId = new BpmnId(poolElement.getParticipantId());
        Participant participant = this.bpmnDocument.findParticipant(participantId);
        result.setParticipant(participant);
        String processIdString = poolElement.getProcessId();
        WorkflowProcess process = null;
        boolean bCreateProcess = false;
        if (Utility.isStringEmpty(processIdString)) {
            if (bCreateProcess) {
                BpmnId processId = this.makeNextId();
                processIdString = processId.toString();
                process = this.bpmnDocument.addNewProcess(processId, WorkflowProcessType.INDEPENDENT);
            }
        } else {
            process = this.bpmnDocument.findProcess(null, processIdString);
            if (process == null) {
                process = this.bpmnDocument.insertProcess(processIdString, null, WorkflowProcessType.INDEPENDENT);
            }
        }
        if ((process == null) && bCreateProcess) {
            throw new SketchpadException("Can not find or create process " + processIdString + " when loading " + result.getIdString());
        }
        result.setProcess(process);
        return result;
    }

    protected void loadProcessNodes() throws SketchpadException {
        loadActivities();
    }

    public void loadActivities() throws SketchpadException {
        List<ActivitySetElement> processesAndActvitySets = this.xpdlDocument.getAllProcesses(true);
        for (ActivitySetElement processElement : processesAndActvitySets) {
            loadActivities(processElement);
        }
    }

    public void loadActivities(ActivitySetElement processElement) throws SketchpadException {
        resetAssignedNodeFrame();
        ArrayList<ActivityElement> attachedEvents = new ArrayList<ActivityElement>();
        ArrayList<ActivityElement> activitiesList = processElement.getActivitiesList();
        for (ActivityElement activityElement : activitiesList) {
            try {
                loadActivity(processElement, activityElement, attachedEvents);
            } catch (SketchpadException e) {
                e.printStackTrace();
            }
        }
        for (ActivityElement attachedActivityElement : attachedEvents) {
            EventElement eventElement = attachedActivityElement.getEventElement();
            XPDLNodeGraphicsInfo attachedActivityInfo = this.getNodeGraphicsInfo(attachedActivityElement);
            String activityPageId = attachedActivityInfo.getPageId();
            BPMNEventGraphic event = this.bpmnDocument.findEventGraphic(attachedActivityElement.getId(), activityPageId);
            BPMNActivityGraphic parentActivity = this.bpmnDocument.findActivityGraphic(eventElement.getTarget(), activityPageId);
            if ((event != null) && (parentActivity != null)) {
                if (event.getParent() != null) {
                    event.getParent().removeChild(event);
                }
                event.setParent(parentActivity);
                event.setTargetActivity(parentActivity);
                parentActivity.addChild(event);
            }
        }
    }

    public void loadActivity(ActivitySetElement processElement, ActivityElement activityElement, ArrayList<ActivityElement> attachedEvents) throws SketchpadException {
        String id = activityElement.getId();
        String name = activityElement.getName();
        if ("SubProcessApproveOrder".equals(id)) {
            log.debug("SubProcessApproveOrder");
        }
        ActivityLoop loop = activityElement.getLoop();
        XPDLSubflowCall subflow = activityElement.getSubprocess(this.bpmnDocument);
        XPDLTask task = activityElement.getTask();
        BlockActivityCall blockcall = activityElement.getBlockActivityCall(this.bpmnDocument);
        ActivityViewState viewState = activityElement.getViewState();
        ArrayList<ActivityAssignment> assignments = activityElement.getAssignments();
        TreeSet<BpmnId> performers = this.getPerformers(activityElement);
        ArrayList<Element> splitElementList = activityElement.getSplitElements();
        if (splitElementList.size() > 0) {
            this.activitesWithSplitTransitionRestrictions.add(activityElement);
        }
        BPMNGatewayType gatewayType = activityElement.getGatewayType();
        EventElement eventElement = activityElement.getEventElement();
        XPDLNodeGraphicsInfo info = this.getNodeGraphicsInfo(processElement, activityElement);
        WorkflowProcess process = this.bpmnDocument.findProcess(null, processElement.getId());
        ElementLocation location = locateElement(processElement, info, activityElement);
        double xpos = this.coordinateMapper.mapX(location.getPool(), info.getX());
        double ypos = this.coordinateMapper.mapY(location.getPool(), info.getY());
        if (java.lang.Double.isNaN(xpos) || java.lang.Double.isNaN(ypos)) {
        }
        info.setX(xpos);
        info.setY(ypos);
        BPMNGraphic graphic = null;
        if (gatewayType != null) {
            BPMNGatewayXorType xorType = activityElement.getGatewayXorType();
            Boolean markerVisible = activityElement.getGatewayMarkerVisible();
            graphic = this.bpmnDocument.insertGateway(location.getPageIdString(), processElement.getId(), id, name, info.getShape(), info.getX(), info.getY(), info.getWidth(), info.getHeight(), location.getPoolIdString(), location.getLaneIdString(), gatewayType, xorType, markerVisible, null, null);
        } else if (eventElement != null) {
            BPMNEventType eventType = eventElement.getType();
            BPMNEventTriggerType eventTriggerType = eventElement.getTriggerType();
            if (eventTriggerType == null) {
                eventTriggerType = BPMNEventTriggerType.make(eventType);
                eventTriggerType.setValue(BPMNEventTriggerType.S_NONE);
            } else if (eventTriggerType.equals(StringEnumeration.INVALID_INDEX)) {
                eventTriggerType.setValue(BPMNEventTriggerType.S_NONE);
            }
            TimerType timerType = eventElement.getTimerType();
            String targetActivityId = eventElement.getTarget();
            Boolean isInterrupting = eventElement.getIsInterrupting();
            graphic = this.bpmnDocument.insertEvent(location.getPageIdString(), processElement.getId(), id, name, info.getShape(), info.getX(), info.getY(), info.getWidth(), info.getHeight(), location.getPoolIdString(), location.getLaneIdString(), eventType, eventTriggerType, timerType, targetActivityId, isInterrupting);
            if (Utility.isStringNotEmpty(targetActivityId)) {
                attachedEvents.add(activityElement);
            }
        } else {
            graphic = this.bpmnDocument.insertActivity(location.getPageIdString(), processElement.getId(), id, name, info.getShape(), info.getX(), info.getY(), info.getWidth(), info.getHeight(), null, null, location.getPoolIdString(), location.getLaneIdString(), loop, assignments, performers, subflow, task, blockcall, viewState);
            if (graphic != null) {
                ((BPMNActivityGraphic) graphic).setStartQuantity(activityElement.getStartQuantity());
                ((BPMNActivityGraphic) graphic).setIsStartActivity(activityElement.getIsStartActivity());
                ((BPMNActivityGraphic) graphic).setIsTransaction(activityElement.getIsTransaction());
                ((BPMNActivityGraphic) graphic).setIsForCompensation(activityElement.getIsForCompensation());
                ((BPMNActivityGraphic) graphic).setTaskApplication(activityElement.getTaskApplication());
                ((BPMNActivityGraphic) graphic).setTaskApplicationParameters(activityElement.getTaskApplicationParameters());
                if ((subflow != null) && Utility.isStringNotEmpty(subflow.getCalledProcessId()) || (blockcall != null) && Utility.isStringNotEmpty(blockcall.getCalledProcessId())) {
                    activityCalls.add((BPMNActivityGraphic) graphic);
                }
            }
        }
        if (graphic != null) {
            graphic.setDescription(activityElement.getDescription());
            ExtendedAttributesElement attributesElement = activityElement.getExtendedAttributesElement(false);
            if (attributesElement != null) {
                graphic.setExtendedAttributes(attributesElement.getStore());
            }
        }
        if (info != null) {
            info.copyTo(graphic);
        }
    }

    protected ElementLocation locateElement(ActivitySetElement processElement, XPDLNodeGraphicsInfo info, ActivityElement activityElement) throws SketchpadIdException {
        ElementLocation location = new ElementLocation();
        location.setProcess(this.bpmnDocument.findProcess(null, processElement.getId()));
        String laneIdString = info.getLaneId();
        String pageIdString = info.getPageId();
        location.setPage(this.bpmnDocument.findPage(pageIdString));
        if (Utility.isStringEmpty(laneIdString)) {
            if (location.getPage() == null) {
                location.setPool(this.bpmnDocument.findPoolFor(location.getProcess()));
                location.setPage(this.bpmnDocument.findPageFor(location.getPool()));
                if (location.getPage() == null) {
                    location.setPage(this.bpmnDocument.findPageFor(location.getProcess()));
                }
            }
            if (location.getPage() != null) {
                location.setLane(location.getPage().getLaneEnclosing(activityElement.getBounds(this.getToolId())));
            }
            if (location.getLane() == null) {
                System.out.println("XpdlDocumentLoader: Cannot find lane for activity " + activityElement.getId());
            } else {
                laneIdString = location.getLane().getIdString();
            }
        } else {
            location.setLane(this.bpmnDocument.findLane(laneIdString));
            if (location.getLane() == null) {
                if (location.getPage() != null) {
                    BPMNPoolGraphic defaultPool = location.getPage().getDefaultPool();
                    if (defaultPool == null) {
                        location.getPage().setDefaultProcess(location.getProcess());
                        defaultPool = this.bpmnDocument.insertPool(location.getPage(), location.getPage().getPoolOrientation());
                        defaultPool.setProcess(location.getProcess());
                        BpmnId laneId = this.bpmnDocument.loadId(laneIdString);
                        location.setLane(defaultPool.getLane(0));
                        location.getLane().setId(laneId);
                    } else {
                        location.getPage().setDefaultProcess(location.getProcess());
                        defaultPool.setProcess(location.getProcess());
                        BpmnId laneId = this.bpmnDocument.loadId(laneIdString);
                        location.setLane(defaultPool.getLane(0));
                        location.getLane().setId(laneId);
                    }
                }
            } else {
            }
        }
        location.setPool(findPool(processElement, location.getLane()));
        if (location.getPool() != null) {
            if (location.getLane() == null) {
                location.setLane(location.getPool().getLane(0));
            }
        }
        return location;
    }

    protected BPMNPoolGraphic findPool(ActivitySetElement processElement, BPMNLaneGraphic lane) {
        BPMNPoolGraphic result = null;
        if (lane == null) {
            WorkflowProcess process = this.bpmnDocument.findProcess(null, processElement.getId());
            if (process != null) {
                result = this.bpmnDocument.findPoolFor(process);
            }
        } else {
            result = lane.getParentPool();
        }
        return result;
    }

    public void setOutgoingTransitionOrder(ActivityElement activityElement, BPMNWorkflowDocument targetDocument) throws SketchpadIdException {
        ArrayList<BPMNFlowGraphic> newOrder = new ArrayList<BPMNFlowGraphic>();
        if (activityElement != null) {
            XPDLNodeGraphicsInfo activityInfo = this.getNodeGraphicsInfo(activityElement);
            if (activityInfo != null) {
                String activityPageId = activityInfo.getPageId();
                SketchGraphic graphic = targetDocument.findNodeGraphic(activityElement.getId(), activityPageId);
                if (graphic != null) {
                    ArrayList<Element> splitElementList = activityElement.getSplitElements();
                    for (Element splitElement : splitElementList) {
                        Element transitionRefsElement = splitElement.getChild(XPDLConstants.S_TRANSITION_REFERENCES);
                        if (transitionRefsElement != null) {
                            List<Element> transitionReferences = XpdlElement.getChildren(transitionRefsElement, XPDLConstants.S_TRANSITION_REFERENCE);
                            for (Element transitionRefElement : transitionReferences) {
                                String id = transitionRefElement.getAttributeValue(XPDLConstants.S_ID);
                                if (Utility.isStringNotEmpty(id)) {
                                    BPMNFlowGraphic connectorGraphic = targetDocument.findConnectorGraphic(id, activityPageId);
                                    if (connectorGraphic != null) {
                                        newOrder.add(connectorGraphic);
                                    } else {
                                        log.error("XPDLWorkflowDocument.setOutgoingTransitionOrder: Error.  Did not find connector id=" + id);
                                    }
                                }
                            }
                        }
                    }
                    graphic.setOutgoingTransitionOrder(newOrder);
                }
            }
        }
    }

    /**
	 * getNodeGraphicsInfo - read the node graphics info from an activity element and
	 * return it.  If it is missing or incomplete, assign values where needed.
	 * 
	 * @param processElement ProcessElement
	 * @param activityElement activityElement
	 */
    protected XPDLNodeGraphicsInfo getNodeGraphicsInfo(ActivitySetElement processElement, ActivityElement activityElement) {
        XPDLNodeGraphicsInfo result = this.getNodeGraphicsInfo(activityElement);
        if (result == null) {
            result = new XPDLNodeGraphicsInfo();
            activityElement.setInfo(result);
        }
        if (Double.isNaN(result.getX()) || Double.isNaN(result.getY()) || Double.isNaN(result.getWidth()) || Double.isNaN(result.getHeight())) {
            Rectangle2D.Double frame = getNextAssignedNodeFrame();
            if (Double.isNaN(result.getX()) || Double.isNaN(result.getY())) {
                result.setX(frame.x);
                result.setY(frame.y);
            }
            if (Double.isNaN(result.getWidth())) {
                result.setWidth(frame.width);
            }
            if (Double.isNaN(result.getHeight())) {
                result.setHeight(frame.height);
            }
            activityElement.setInfo(result);
        }
        if (Utility.isStringEmpty(result.getPageId())) {
        }
        if (Utility.isStringEmpty(result.getLaneId())) {
        }
        if (Utility.isStringEmpty(result.getShape())) {
        }
        return result;
    }

    public static double ASSIGNED_NODE_POSITION_OFFSET_X = 30.0;

    public static double ASSIGNED_NODE_POSITION_OFFSET_Y = 60.0;

    public static double ASSIGNED_NODE_POSITION_PAGE_WIDTH = 1000.0;

    public static double ASSIGNED_NODE_POSITION_WIDTH = 80.0;

    public static double ASSIGNED_NODE_POSITION_HEIGHT = 60.0;

    private Rectangle2D.Double lastAssignedNodeFrame = null;

    public Rectangle2D.Double getNextAssignedNodeFrame() {
        if (lastAssignedNodeFrame == null) {
            resetAssignedNodeFrame();
        } else {
            double x = lastAssignedNodeFrame.x + ASSIGNED_NODE_POSITION_WIDTH + ASSIGNED_NODE_POSITION_OFFSET_X;
            double y = lastAssignedNodeFrame.y;
            if (x > ASSIGNED_NODE_POSITION_PAGE_WIDTH) {
                x = ASSIGNED_NODE_POSITION_OFFSET_X;
                y = lastAssignedNodeFrame.y + ASSIGNED_NODE_POSITION_HEIGHT + ASSIGNED_NODE_POSITION_OFFSET_Y;
            }
            lastAssignedNodeFrame.x = x;
            lastAssignedNodeFrame.y = y;
        }
        return lastAssignedNodeFrame;
    }

    public void resetAssignedNodeFrame() {
        lastAssignedNodeFrame = new Rectangle2D.Double(ASSIGNED_NODE_POSITION_OFFSET_X, ASSIGNED_NODE_POSITION_OFFSET_Y, ASSIGNED_NODE_POSITION_WIDTH, ASSIGNED_NODE_POSITION_HEIGHT);
    }

    /**
	 * loadArtifacts()
		 * @throws SketchpadException 
	 *
	 */
    private void loadArtifacts() throws SketchpadException {
        ArrayList<ArtifactElement> artifactList = this.xpdlDocument.getArtifactList();
        for (ArtifactElement artifactElement : artifactList) {
            String id = artifactElement.getId();
            String name = null;
            String poolId = artifactElement.getPoolId();
            XPDLNodeGraphicsInfo info = this.getNodeGraphicsInfo(artifactElement);
            String laneId = info.getLaneId();
            String pageId = info.getPageId();
            if (pageId == null) {
                ArrayList<BPMNPageGraphic> pages = this.bpmnDocument.getAllPages();
                if (pages.size() == 1) {
                    pageId = pages.get(0).getIdString();
                }
            }
            double x = info.getX();
            double y = info.getY();
            double width = info.getWidth();
            double height = info.getHeight();
            if (artifactElement.isTextAnnotation()) {
                TextAnnotationElement textElement = artifactElement.makeTextAnnotationElement();
                name = textElement.getTextAnnotation();
                BPMNTextGraphic graphic = this.bpmnDocument.insertTextObject(pageId, null, id, name, x, y, width, height, poolId, laneId);
                if (graphic != null) {
                    info.copyTo(graphic);
                }
            } else if (artifactElement.isDataObject()) {
                DataArtifactElement dataArtifactElement = artifactElement.makeDataArtifactElement();
                name = dataArtifactElement.getDataObjectName();
                if (name == null) {
                    name = dataArtifactElement.getName();
                    if ((name != null) && this.getUpdateDocument()) {
                        artifactElement.setName(null);
                        dataArtifactElement.setDataObjectName(name);
                        this.setDocumentWasAltered(true);
                    }
                }
                BPMNDataGraphic graphic = this.bpmnDocument.insertDataObject(pageId, id, name, x, y, width, height, poolId, laneId, dataArtifactElement.getState(), dataArtifactElement.getRequiredForStart(), dataArtifactElement.getProducedAtCompletion(), null, null);
                if (graphic != null) {
                    info.copyTo(graphic);
                }
            } else if (artifactElement.isGroup()) {
                GroupArtifactElement groupElement = artifactElement.makeGroupArtifactElement();
                name = groupElement.getName();
                ArrayList<BpmnId> bpmnIdList = BpmnId.makeIdList(groupElement.getObjectList());
                String groupIdString = groupElement.getGroupId();
                BPMNGroupGraphic graphic = this.bpmnDocument.insertGroup(pageId, id, groupIdString, name, groupElement.getCategoryId(), groupElement.getCategoryName(), bpmnIdList, x, y, width, height, poolId, laneId);
                if (info != null) {
                    info.copyTo(graphic);
                }
            }
        }
    }

    protected void loadProcessConnectors() throws SketchpadException {
        loadTransitions();
    }

    public void loadTransitions() throws SketchpadException {
        List<ActivitySetElement> processesAndActvitySets = this.xpdlDocument.getAllProcesses(true);
        for (ActivitySetElement processOrActivitySet : processesAndActvitySets) {
            ArrayList<TransitionElement> transitionList = processOrActivitySet.getTransitionsList();
            for (TransitionElement transistionElement : transitionList) {
                loadTransition(processOrActivitySet.getId(), transistionElement);
            }
        }
        for (ActivityElement activity : this.activitesWithSplitTransitionRestrictions) {
            this.setOutgoingTransitionOrder(activity, this.bpmnDocument);
        }
    }

    @Override
    protected String getTransitionConditionExpression(ConditionElement condition) {
        String result = null;
        result = condition.getElement().getText();
        return result;
    }

    @Override
    protected XPDLTransitionCondition getTransitionCondition(TransitionElement transition) {
        XPDLTransitionCondition result = null;
        ConditionElement condition = transition.getConditionElement();
        if (condition != null) {
            result = new XPDLTransitionCondition();
            String conditionType = condition.getType();
            if (Utility.isStringEmpty(conditionType)) {
                result.setValue(XPDLTransitionCondition.TYPE_NONE_UC);
            } else {
                result.setValue(conditionType);
            }
            String expression = this.getTransitionConditionExpression(condition);
            result.setCondition(expression);
        }
        return result;
    }

    private void loadTransition(String processId, TransitionElement transition) throws SketchpadException {
        log.debug("loadTransition: process=" + processId + " transition=" + transition);
        BPMNFlowGraphic flow = null;
        XPDLConnectorGraphicsInfo info = null;
        String pageId = null;
        String id = transition.getId();
        String name = transition.getName();
        String from = transition.getFrom();
        String to = transition.getTo();
        Integer quantity = transition.getQuantity();
        XPDLTransitionCondition condition = this.getTransitionCondition(transition);
        info = this.getConnectorGraphicsInfo(transition);
        if (info != null) {
            pageId = info.getPageId();
        }
        if (pageId == null) {
            ActivityElement activityElement = this.xpdlDocument.findActivity(from);
            if (activityElement == null) {
                log.error("XPDLWorkflowDocument.copyTransition: Can not find from activity element.");
            } else {
                XPDLNodeGraphicsInfo nodeInfo = this.getNodeGraphicsInfo(activityElement);
                if (nodeInfo != null) {
                    pageId = nodeInfo.getPageId();
                }
            }
        }
        flow = (BPMNFlowGraphic) this.bpmnDocument.insertTransition(pageId, processId, id, name, from, to, condition, quantity, info);
        if (flow == null) {
            log.error("loadTransition: FAILED pageId=" + pageId + "; processId=" + processId + "; id=" + id + "; from=" + from + "; to=" + to);
        } else if (info != null) {
            this.xpdlDocument.setPage(this.bpmnDocument, flow, pageId);
            info.copyTo(flow);
            flow.setDescription(transition.getDescription());
            ExtendedAttributesElement extendedAttributesElement = transition.getExtendedAttributesElement(false);
            if (extendedAttributesElement != null) {
                flow.setExtendedAttributes(extendedAttributesElement.getStore());
            }
        }
    }

    /**
	 * copyMessageFlowsTo
	 *
	 * @param targetDocument WorkflowDocument
	 * @throws SketchpadException 
	 */
    private void loadMessageFlows() throws SketchpadException {
        List<MessageFlowElement> messageFlowList = this.xpdlDocument.getMessageFlowList();
        for (MessageFlowElement messageFlowElement : messageFlowList) {
            loadMessageFlow(messageFlowElement);
        }
    }

    /**
	 * loadMessageFlow
	 *
	 * @param messageFlowElement Element
	 * @throws SketchpadException 
	 */
    private void loadMessageFlow(MessageFlowElement messageFlowElement) throws SketchpadException {
        log.debug("loadMessageFlow: element=" + messageFlowElement);
        BPMNFlowGraphic flow = null;
        XPDLConnectorGraphicsInfo info = null;
        String pageId = null;
        String id = messageFlowElement.getId();
        String name = messageFlowElement.getName();
        String from = messageFlowElement.getSource();
        String to = messageFlowElement.getTarget();
        BPMNMessage message = null;
        MessageElement messageElement = messageFlowElement.getMessageElement();
        if (messageElement != null) {
            message = BPMNMessage.make(messageElement);
        }
        info = this.getConnectorGraphicsInfo(messageFlowElement);
        if (info != null) {
            pageId = info.getPageId();
        }
        flow = (BPMNFlowGraphic) this.bpmnDocument.insertMessageFlow(pageId, id, name, from, to, message);
        if (flow == null) {
            log.error("copyMessageFlowTo: FAILED pageId=" + pageId + "; id=" + id + "; from=" + from + "; to=" + to);
        } else if (info != null) {
            this.xpdlDocument.setPage(this.bpmnDocument, flow, pageId);
            info.copyTo(flow);
            flow.setDescription(messageFlowElement.getDescription());
        }
    }

    /**
	 * loadAssociations
	 * @throws SketchpadException 
	 *
	 */
    private void loadAssociations() throws SketchpadException {
        List<AssociationElement> associationList = this.xpdlDocument.getAssociationList();
        for (AssociationBaseElement association : associationList) {
            loadAssociation(null, BPMNFlowType.ASSOCIATION, association);
        }
    }

    /**
	 * loadAssociation
	 * @param processOrActivitySet 
	 * @param flowType 
	 *
	 * @param associationElement AssociationElement
	 * @throws SketchpadException 
	 */
    protected void loadAssociation(ActivitySetElement processOrActivitySet, int flowType, AssociationBaseElement associationElement) throws SketchpadException {
        log.debug("loadAssociation: element=" + associationElement);
        BPMNFlowGraphic flow = null;
        XPDLConnectorGraphicsInfo info = null;
        String pageId = null;
        String id = associationElement.getId();
        String name = associationElement.getName();
        String from = associationElement.getSource();
        String to = associationElement.getTarget();
        String directionString = associationElement.getDirection();
        info = this.getConnectorGraphicsInfo(associationElement);
        if (info != null) {
            pageId = info.getPageId();
        }
        String processId = null;
        if (processOrActivitySet != null) processId = processOrActivitySet.getId();
        flow = (BPMNFlowGraphic) this.bpmnDocument.insertAssociation(flowType, processId, pageId, id, name, from, to, directionString);
        if (flow == null) {
            log.error("loadAssociation: FAILED pageId=" + pageId + "; id=" + id + "; from=" + from + "; to=" + to);
        } else {
            flow.setFlowType(new BPMNFlowType(flowType));
            if (info != null) {
                this.xpdlDocument.setPage(this.bpmnDocument, flow, pageId);
                info.copyTo(flow);
                flow.setDescription(associationElement.getDescription());
            }
        }
    }

    @Override
    public XPDLConnectorGraphicsInfo getConnectorGraphicsInfo(XpdlElement element) {
        if (element == null) {
            return null;
        }
        XPDLConnectorGraphicsInfo info = null;
        ConnectorGraphicsInfoElement infoElement = element.getConnectorGraphicsInfoElement(getToolId());
        if (infoElement != null) {
            info = new XPDLConnectorGraphicsInfo(infoElement);
        }
        return info;
    }

    @Override
    public XPDLNodeGraphicsInfo getNodeGraphicsInfo(XpdlElement element) {
        XPDLNodeGraphicsInfo info = null;
        NodeGraphicsInfoElement infoElement = element.getNodeGraphicsInfoElement(getToolId());
        if (infoElement == null) {
            info = null;
        }
        if (infoElement != null) {
            info = XPDLNodeGraphicsInfo.make(infoElement);
        }
        return info;
    }

    @Override
    public TreeSet<BpmnId> getPerformers(ActivityElement activityElement) throws SketchpadException {
        Element performerElement = activityElement.getPerformerElement();
        TreeSet<BpmnId> result = activityElement.getPerformerList();
        if (result.size() == 0) {
            if (performerElement != null) {
                String participantIdString = performerElement.getText();
                if (Utility.isStringNotEmpty(participantIdString)) {
                    result.add(new BpmnId(participantIdString));
                }
            }
        }
        if ((this.bUpdateDocument) && (performerElement != null)) {
            activityElement.setPerformers(result);
            activityElement.setPerformerDEPRECATED(null);
            ;
            this.setDocumentWasAltered(true);
        }
        return result;
    }

    protected boolean isFromBpmn() {
        String vendor = this.xpdlDocument.getVendor();
        boolean result = vendor.equalsIgnoreCase(BRUCE_SILVER_1) || vendor.equalsIgnoreCase(BRUCE_SILVER_2);
        return result;
    }
}
