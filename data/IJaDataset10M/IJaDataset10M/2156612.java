package com.esri.gpt.catalog.search;

import com.esri.gpt.catalog.discovery.*;
import com.esri.gpt.framework.context.RequestContext;
import com.esri.gpt.framework.geometry.Envelope;
import com.esri.gpt.framework.util.Val;
import com.esri.gpt.framework.xml.DomUtil;
import com.esri.gpt.framework.xml.XmlIoUtil;
import com.esri.gpt.server.csw.provider.components.CswNamespaces;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Generates a CSW GetRecords request from a discovery query.
 */
public class GetRecordsGenerator {

    /** instance variables ====================================================== */
    private Document dom;

    private String elementSetName = "full";

    private String filterVersion = "1.1.0";

    private String resultType = "RESULTS";

    private String typeNames = "csw:Record";

    private String service = "CSW";

    private String version = "2.0.2";

    /**
   * Constructs with a an active request context.
   * @param context the request context
   */
    public GetRecordsGenerator(RequestContext context) {
    }

    /** 
   * Gets the XML document being constructed.
   * @return the document
   */
    private Document getDom() {
        return this.dom;
    }

    /** 
   * Sets the XML document being constructed.
   * @param dom the document
   */
    private void setDom(Document dom) {
        this.dom = dom;
    }

    /** 
   * Gets the CSW element set name to return.
   * <br/>Default = "full"
   * @return the element set name 
   */
    public String getElementSetName() {
        return this.elementSetName;
    }

    /** 
   * Sets the CSW element set name to return.
   * <br/>Default = "full"
   * @param elementSetName the element set name
   */
    public void setElementSetName(String elementSetName) {
        this.elementSetName = Val.chkStr(elementSetName);
    }

    /** 
   * Gets the CSW filter version.
   * <br/>Default = "1.1.0"
   * @return the filter version
   */
    public String getFilterVersion() {
        return this.filterVersion;
    }

    /** 
   * Sets the CSW filter version.
   * <br/>Default = "1.1.0"
   * <br/>Modifying the filter version will not change the generation logic.
   * @param version the filter version
   */
    public void setFilterVersion(String version) {
        this.filterVersion = Val.chkStr(version);
    }

    /** 
   * Gets the CSW result type.
   * <br/>Default = "RESULTS"
   * @return the result type 
   */
    public String getResultType() {
        return this.resultType;
    }

    /** 
   * Sets the CSW result type.
   * <br/>Default = "RESULTS"
   * @param resultType the result type
   */
    public void setResultType(String resultType) {
        this.resultType = Val.chkStr(resultType);
    }

    /** 
   * Gets the OGC service type.
   * <br/>Default = "CSW"
   * @return the OGC service type
   */
    public String getService() {
        return this.service;
    }

    /** 
   * Sets the OGC service type.
   * <br/>Default = "CSW"
   * <br/>Modifying the OGC service type not change the generation logic.
   * @param service the OGC service type
   */
    public void setService(String service) {
        this.service = Val.chkStr(service);
    }

    /** 
   * Gets the CSW type names to query.
   * <br/>Default = "csw:Record"
   * @return the type names 
   */
    public String getTypeNames() {
        return this.typeNames;
    }

    /** 
   * Sets the CSW type names to query.
   * <br/>Default = "csw:Record"
   * @param typeNames the type names
   */
    public void setTypeNames(String typeNames) {
        this.typeNames = Val.chkStr(typeNames);
    }

    /** 
   * Gets the CSW version.
   * <br/>Default = "2.0.2"
   * @return the CSW version
   */
    public String getVersion() {
        return this.version;
    }

    /** 
   * Sets the CSW version.
   * <br/>Default = "2.0.2"
   * <br/>Modifying the CSW version will not change the generation logic.
   * @param version the CSW version
   */
    public void setVersion(String version) {
        this.version = Val.chkStr(version);
    }

    /**
   * Appends a logical clause to an XML parent element.
   * @param parent the parent element to which the clause will be appended
   * @param logicalClause the logical clause to append
   */
    private void appendLogicalClause(Element parent, LogicalClause logicalClause) {
        if ((logicalClause != null) && (logicalClause.getClauses().size() > 0)) {
            Element elLogical = null;
            if (logicalClause instanceof LogicalClause.LogicalAnd) {
                elLogical = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:And");
            } else if (logicalClause instanceof LogicalClause.LogicalOr) {
                elLogical = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:Or");
            } else if (logicalClause instanceof LogicalClause.LogicalNot) {
                elLogical = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:Not");
            }
            if (elLogical != null) {
                for (DiscoveryClause clause : logicalClause.getClauses()) {
                    if (clause == null) {
                    } else if (clause instanceof LogicalClause) {
                        appendLogicalClause(elLogical, (LogicalClause) clause);
                    } else if (clause instanceof PropertyClause) {
                        appendPropertyClause(elLogical, (PropertyClause) clause);
                    } else if (clause instanceof SpatialClause) {
                        appendSpatialClause(elLogical, (SpatialClause) clause);
                    } else {
                    }
                }
                parent.appendChild(elLogical);
            }
        }
    }

    /**
   * Appends a property clause to an XML parent element.
   * @param parent the parent element to which the clause will be appended
   * @param propertyClause the property clause to append
   */
    private void appendPropertyClause(Element parent, PropertyClause propertyClause) {
        Element elClause;
        Element elLiteral;
        Element elLower;
        Element elUpper;
        String sName = propertyClause.getTarget().getMeaning().getDcElement().getElementName();
        Element elName = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:PropertyName");
        elName.setTextContent(sName);
        if (propertyClause instanceof PropertyClause.PropertyIsBetween) {
            PropertyClause.PropertyIsBetween between;
            between = (PropertyClause.PropertyIsBetween) propertyClause;
            elClause = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:PropertyIsBetween");
            elLower = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:LowerBoundary");
            elUpper = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:UpperBoundary");
            elLower.setTextContent(between.getLowerBoundary());
            elUpper.setTextContent(between.getUpperBoundary());
            elClause.appendChild(elName);
            elClause.appendChild(elLower);
            elClause.appendChild(elUpper);
            parent.appendChild(elClause);
        } else if (propertyClause instanceof PropertyClause.PropertyIsNull) {
            elClause = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:PropertyIsNull");
            elClause.appendChild(elName);
            parent.appendChild(elClause);
        } else {
            String opName = "";
            if (propertyClause instanceof PropertyClause.PropertyIsEqualTo) {
                opName = "PropertyIsEqualTo";
            } else if (propertyClause instanceof PropertyClause.PropertyIsGreaterThan) {
                opName = "PropertyIsGreaterThan";
            } else if (propertyClause instanceof PropertyClause.PropertyIsGreaterThanOrEqualTo) {
                opName = "PropertyIsGreaterThanOrEqualTo";
            } else if (propertyClause instanceof PropertyClause.PropertyIsLessThan) {
                opName = "PropertyIsLessThan";
            } else if (propertyClause instanceof PropertyClause.PropertyIsLessThanOrEqualTo) {
                opName = "PropertyIsLessThanOrEqualTo";
            } else if (propertyClause instanceof PropertyClause.PropertyIsLike) {
                opName = "PropertyIsLike";
            } else if (propertyClause instanceof PropertyClause.PropertyIsNotEqualTo) {
                opName = "PropertyIsNotEqualTo";
            } else {
            }
            elClause = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:" + opName);
            elLiteral = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:Literal");
            elLiteral.setTextContent(propertyClause.getLiteral());
            elClause.appendChild(elName);
            elClause.appendChild(elLiteral);
            parent.appendChild(elClause);
            if (propertyClause instanceof PropertyClause.PropertyIsLike) {
                elClause.setAttribute("wildCard", "*");
                elClause.setAttribute("escape", "\\");
                elClause.setAttribute("singleChar", "?");
            }
        }
    }

    /**
   * Appends a sort by clause to an XML parent element.
   * @param parent the parent element to which the clause will be appended
   * @param sortables the sortables to append
   */
    private void appendSortByClause(Element parent, Sortables sortables) {
        if ((sortables != null) && (sortables.size() > 0)) {
            Element elSortBy = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:SortBy");
            for (Sortable sortable : sortables) {
                Element elSort = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:SortProperty");
                Element elName = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:PropertyName");
                Element elOrder = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:SortOrder");
                elName.setTextContent(sortable.getMeaning().getDcElement().getElementName());
                elOrder.setTextContent(sortable.getDirection().name());
                elSort.appendChild(elName);
                elSort.appendChild(elOrder);
                elSortBy.appendChild(elSort);
            }
            parent.appendChild(elSortBy);
        }
    }

    /**
   * Appends a spatial clause to an XML parent element.
   * @param parent the parent element to which the clause will be appended
   * @param spatialClause the spatial clause to append
   */
    private void appendSpatialClause(Element parent, SpatialClause spatialClause) {
        Envelope envelope = spatialClause.getBoundingEnvelope();
        if ((envelope == null) || envelope.isEmpty()) {
        }
        String opName = "";
        if (spatialClause instanceof SpatialClause.GeometryBBOXIntersects) {
            opName = "BBOX";
        } else if (spatialClause instanceof SpatialClause.GeometryContains) {
            opName = "Contains";
        } else if (spatialClause instanceof SpatialClause.GeometryIntersects) {
            opName = "Intersects";
        } else if (spatialClause instanceof SpatialClause.GeometryIsDisjointTo) {
            opName = "Disjoint";
        } else if (spatialClause instanceof SpatialClause.GeometryIsEqualTo) {
            opName = "Equal";
        } else if (spatialClause instanceof SpatialClause.GeometryIsWithin) {
            opName = "Within";
        } else if (spatialClause instanceof SpatialClause.GeometryOverlaps) {
            opName = "Overlaps";
        } else {
        }
        Element elClause = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:" + opName);
        String sName = spatialClause.getTarget().getMeaning().getDcElement().getElementName();
        Element elName = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:PropertyName");
        elName.setTextContent(sName);
        elClause.appendChild(elName);
        Element elEnv = getDom().createElementNS(CswNamespaces.URI_GML, "gml:Envelope");
        Element elLower = getDom().createElementNS(CswNamespaces.URI_GML, "gml:lowerCorner");
        Element elUpper = getDom().createElementNS(CswNamespaces.URI_GML, "gml:upperCorner");
        elLower.setTextContent(envelope.getMinX() + " " + envelope.getMinY());
        elUpper.setTextContent(envelope.getMaxX() + " " + envelope.getMaxY());
        elEnv.appendChild(elLower);
        elEnv.appendChild(elUpper);
        elClause.appendChild(elEnv);
        parent.appendChild(elClause);
    }

    /**
   * Generates a CSW GetRecords request string from a DiscoveryQuery.
   * @param query the discovery query
   * @return the CSW GetRecords request string
   * @throws Exception if an exception occurs
   */
    public String generateCswRequest(DiscoveryQuery query) throws Exception {
        DiscoveryFilter filter = query.getFilter();
        setDom(DomUtil.newDocument());
        Element root = getDom().createElementNS(CswNamespaces.URI_CSW, "csw:GetRecords");
        root.setAttribute("xmlns:gml", CswNamespaces.URI_GML);
        root.setAttribute("xmlns:ows", CswNamespaces.URI_OWS);
        root.setAttribute("xmlns:ogc", CswNamespaces.URI_OGC);
        getDom().appendChild(root);
        root.setAttribute("service", getService());
        root.setAttribute("version", getVersion());
        root.setAttribute("resultType", getResultType());
        root.setAttribute("startPosition", "" + filter.getStartRecord());
        root.setAttribute("maxRecords", "" + filter.getMaxRecords());
        Element elQuery = getDom().createElementNS(CswNamespaces.URI_CSW, "csw:Query");
        elQuery.setAttribute("typeNames", getTypeNames());
        root.appendChild(elQuery);
        Element elSetName = getDom().createElementNS(CswNamespaces.URI_CSW, "csw:ElementSetName");
        elSetName.setTextContent(getElementSetName());
        elQuery.appendChild(elSetName);
        Element elConstraint = getDom().createElementNS(CswNamespaces.URI_CSW, "csw:Constraint");
        elConstraint.setAttribute("version", getFilterVersion());
        elQuery.appendChild(elConstraint);
        Element elFilter = getDom().createElementNS(CswNamespaces.URI_OGC, "ogc:Filter");
        elConstraint.appendChild(elFilter);
        appendLogicalClause(elFilter, filter.getRootClause());
        appendSortByClause(elQuery, query.getSortables());
        return XmlIoUtil.domToString(getDom());
    }

    /**
   * Generates a CSW GetRecordById request.
   * @param id the record ID
   * @return the CSW GetRecordById request string
   * @throws Exception if an exception occurs
   */
    public String generateCswByIdRequest(String id) throws Exception {
        setDom(DomUtil.newDocument());
        Element root = getDom().createElementNS(CswNamespaces.URI_CSW, "csw:GetRecordById");
        root.setAttribute("xmlns:gml", CswNamespaces.URI_GML);
        root.setAttribute("xmlns:ows", CswNamespaces.URI_OWS);
        root.setAttribute("xmlns:ogc", CswNamespaces.URI_OGC);
        getDom().appendChild(root);
        root.setAttribute("service", getService());
        root.setAttribute("version", getVersion());
        Element elId = getDom().createElementNS(CswNamespaces.URI_CSW, "csw:Id");
        elId.setTextContent(id);
        root.appendChild(elId);
        Element elSetName = getDom().createElementNS(CswNamespaces.URI_CSW, "csw:ElementSetName");
        elSetName.setTextContent(getElementSetName());
        root.appendChild(elSetName);
        return XmlIoUtil.domToString(getDom());
    }
}
