package com.spotimage.eosps;

import org.vast.ogc.OGCRegistry;
import org.vast.util.DateTime;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

public class EOReportWriter implements EOConstants {

    public Element writeReport(EOReportInfo report) {
        DOMHelper dom = new DOMHelper("root");
        dom.addUserPrefix(EOT_PREFIX, EOT_NAMESPACE);
        dom.addUserPrefix(EOP_PREFIX, EOP_NAMESPACE);
        dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(OGCRegistry.GML, "3.2"));
        if (report instanceof FeasibilityStudy) return writeFeasibilityStudy(dom, (FeasibilityStudy) report); else if (report instanceof ProgrammingStatus) return writeProgrammingStatus(dom, (ProgrammingStatus) report); else throw new IllegalArgumentException("Unsupported report type: " + report.getClass().getCanonicalName());
    }

    protected Element writeFeasibilityStudy(DOMHelper dom, FeasibilityStudy report) {
        Element reportElt = dom.createElement(FEASIBILITY_STUDY_ELT);
        String val;
        val = report.getExpirationDate().formatIso(0);
        dom.setElementValue(reportElt, EXPIRATION_DATE_ELT, val);
        val = Float.toString((float) report.getEstimatedCost());
        if (!val.equals("NaN")) {
            Element elt = dom.setElementValue(reportElt, ESTIMATED_COST_ELT, val);
            elt.setAttribute(UOM_ATT, UNIT_CURRENCY);
        }
        val = Float.toString((float) report.getSuccessRate());
        dom.setElementValue(reportElt, SUCCESS_RATE_ELT, val);
        for (InformationType info : report.getInformationUsed()) {
            Element infoElt = dom.addElement(reportElt, "+" + INFO_USED_ELT);
            dom.setElementValue(infoElt, info.name().replace("_", " "));
        }
        writeCellsAndSegments(dom, reportElt, report);
        return reportElt;
    }

    protected Element writeProgrammingStatus(DOMHelper dom, ProgrammingStatus report) {
        Element reportElt = dom.createElement(PROGRAMMING_STATUS_ELT);
        String val;
        val = Float.toString((float) report.getPercentCompletion());
        dom.setElementValue(reportElt, PERCENT_COMPLETION_ELT, val);
        writeCellsAndSegments(dom, reportElt, report);
        return reportElt;
    }

    protected void writeCellsAndSegments(DOMHelper dom, Element reportElt, EOReportInfo eoReport) {
        for (Segment seg : eoReport.getSegments()) writeSegment(dom, reportElt, seg);
        for (GridCell cell : eoReport.getCells()) writeGridCell(dom, reportElt, cell);
    }

    protected void writeSegment(DOMHelper dom, Element reportElt, Segment segment) {
        Element segmentElt = dom.addElement(reportElt, "+" + SEGMENT_ELT);
        String val;
        val = segment.getId();
        dom.setAttributeValue(segmentElt, ID_ATT, val);
        Element polyElt = dom.addElement(segmentElt, FOOTPRINT_POLY_ELT);
        val = writePolygonCoords(segment.getFootprint());
        dom.setElementValue(polyElt, FOOTPRINT_COORDS_ELT, val);
        dom.setAttributeValue(polyElt, ID_ATT, "F" + segment.getId());
        dom.setAttributeValue(polyElt, SRS_ATT, CRS_4326);
        val = segment.getAcquisitionStartTime().formatIso(0);
        dom.setElementValue(segmentElt, ACQ_START_ELT, val);
        val = segment.getAcquisitionStopTime().formatIso(0);
        dom.setElementValue(segmentElt, ACQ_STOP_ELT, val);
        Element acqMethodElt = dom.addElement(segmentElt, ACQ_METHOD_ELT);
        if (acqMethodElt != null) {
            val = segment.getId();
            dom.setAttributeValue(acqMethodElt, ID_ATT, "M" + val);
            val = segment.getPlatformName();
            if (val != null) dom.setElementValue(acqMethodElt, PLATFORM_NAME_ELT, val);
            val = segment.getInstrumentName();
            if (val != null) dom.setElementValue(acqMethodElt, INSTRUMENT_NAME_ELT, val);
            val = segment.getInstrumentMode();
            if (val != null) dom.setElementValue(acqMethodElt, INSTRUMENT_MODE_ELT, val);
            if (!Float.isNaN(segment.getGroundResolution())) {
                val = Float.toString(segment.getGroundResolution());
                dom.setElementValue(acqMethodElt, RESOLUTION_ELT, val);
            }
            if (segment.getOrbitNumber() >= 0) {
                val = Integer.toString(segment.getOrbitNumber());
                dom.setElementValue(acqMethodElt, ORBIT_NUM_ELT, val);
            }
            if (!Float.isNaN(segment.getIncidenceAngle())) {
                val = Float.toString(segment.getIncidenceAngle());
                Element incElt = dom.setElementValue(acqMethodElt, INC_ANGLE_ELT, val);
                incElt.setAttribute(UOM_ATT, UNIT_DEG);
            }
            if (!Float.isNaN(segment.getPitch())) {
                val = Float.toString(segment.getPitch());
                Element elt = dom.setElementValue(acqMethodElt, PITCH_ANGLE_ELT, val);
                elt.setAttribute(UOM_ATT, UNIT_DEG);
            }
            if (!Float.isNaN(segment.getRoll())) {
                val = Float.toString(segment.getRoll());
                Element elt = dom.setElementValue(acqMethodElt, ROLL_ANGLE_ELT, val);
                elt.setAttribute(UOM_ATT, UNIT_DEG);
            }
            if (!Float.isNaN(segment.getYaw())) {
                val = Float.toString(segment.getYaw());
                Element elt = dom.setElementValue(acqMethodElt, YAW_ANGLE_ELT, val);
                elt.setAttribute(UOM_ATT, UNIT_DEG);
            }
        }
        val = segment.getStatus().name();
        if (val.startsWith("$")) val = val.replace("$", "other:");
        dom.setElementValue(segmentElt, STATUS_ELT, val);
        if (!Float.isNaN(segment.getSuccessRate())) {
            val = Float.toString(segment.getSuccessRate());
        }
    }

    protected void writeGridCell(DOMHelper dom, Element reportElt, GridCell cell) {
        Element cellElt = dom.addElement(reportElt, "+" + CELL_ELT);
        String val;
        DateTime date;
        val = cell.getId();
        dom.setAttributeValue(cellElt, ID_ATT, val);
        Element polyElt = dom.addElement(cellElt, FOOTPRINT_POLY_ELT);
        val = writePolygonCoords(cell.getFootprint());
        dom.setElementValue(polyElt, FOOTPRINT_COORDS_ELT, val);
        dom.setAttributeValue(polyElt, ID_ATT, "F" + cell.getId());
        dom.setAttributeValue(polyElt, SRS_ATT, CRS_4326);
        val = cell.getStatus().name();
        if (val.startsWith("$")) val = val.replace("$", "other:");
        dom.setElementValue(cellElt, STATUS_ELT, val);
        val = Float.toString(cell.getSuccessRate());
        dom.setElementValue(cellElt, SUCCESS_RATE_ELT, val);
        date = cell.getEstimatedSuccessDate();
        if (date != null) dom.setElementValue(cellElt, SUCCESS_DATE_ELT, date.formatIso(0));
        date = cell.getLastAttemptDate();
        if (date != null) dom.setElementValue(cellElt, LAST_ATTEMPT_ELT, date.formatIso(0));
        date = cell.getNextAttemptDate();
        if (date != null) dom.setElementValue(cellElt, NEXT_ATTEMPT_ELT, date.formatIso(0));
        if (cell.getRemainingAttempts() >= 0) dom.setElementValue(cellElt, REMAINING_ATTEMPTS_ELT, Integer.toString(cell.getRemainingAttempts()));
    }

    protected String writePolygonCoords(Polygon poly) {
        StringBuffer gmlPosList = new StringBuffer();
        int numPoints = poly.getExteriorRing().getNumPoints();
        for (int p = 0; p < numPoints; p++) {
            Coordinate coord = poly.getExteriorRing().getCoordinateN(p);
            gmlPosList.append(coord.y);
            gmlPosList.append(' ');
            gmlPosList.append(coord.x);
            if (p < numPoints - 1) gmlPosList.append(' ');
        }
        return gmlPosList.toString();
    }
}
