package com.deimos.sps;

import java.util.ArrayList;
import java.util.List;
import org.vast.ows.OWSException;
import org.vast.ows.OWSReference;
import org.vast.ows.OWSReferenceGroup;
import org.vast.ows.sps.DescribeResultAccessRequest;
import org.vast.ows.sps.DescribeResultAccessResponse;
import org.vast.util.DateTime;
import org.vast.util.DateTimeFormat;
import com.deimos.HMASPSMessageReceiverInOut;
import com.deimos.UtilsDeimos;
import com.deimos.dataBase.Event;
import com.deimos.dataBase.PolygonD;
import com.deimos.dataBase.Task;
import com.deimos.dataBase.TaskController;
import com.spotimage.eosps.EOReportHelper;
import com.vividsolutions.jts.geom.Coordinate;

public class DescribeResultAccess {

    DescribeResultAccessRequest request = null;

    DescribeResultAccessResponse response = null;

    public DescribeResultAccess(DescribeResultAccessRequest request) {
        this.request = request;
    }

    public DescribeResultAccessResponse buildResponse() throws OWSException {
        TaskController taskController = new TaskController();
        List<Task> tasks = null;
        response = new DescribeResultAccessResponse();
        response.setService("SPS");
        response.setVersion("2.0.0");
        response.setDescription("Testing DescribeResultAccess");
        PolygonD polygonSicilyAndMalta = new PolygonD();
        PolygonD segment = new PolygonD();
        polygonSicilyAndMalta.addPoint(10.0, 35.0);
        polygonSicilyAndMalta.addPoint(10.0, 38.0);
        polygonSicilyAndMalta.addPoint(15.0, 38.0);
        polygonSicilyAndMalta.addPoint(15.0, 35.0);
        if (request.getTaskID() != null) {
            tasks = new ArrayList<Task>();
            Task task = taskController.getTask(request.getTaskID());
            if (task != null) tasks.add(task);
        } else if (request.getProcedureID() != null) tasks = taskController.getTasksBySensor(request.getProcedureID());
        if (tasks == null || tasks.isEmpty()) {
            response.setReasonCode("Data currently unavailable");
            response.setDescription("The information was not found in the data base");
        } else {
            List<OWSReferenceGroup> referenceGroups = new ArrayList<OWSReferenceGroup>();
            OWSReferenceGroup referenceGroup;
            OWSReference reference;
            boolean correctRegion = false;
            String[] segments;
            for (Task task : tasks) {
                Event event = taskController.getLastEvent(task.getTaskId());
                EOReportHelper eohelper = new EOReportHelper(UtilsDeimos.generateReport(event));
                segments = new String[eohelper.getNumSegments()];
                for (int i = 0; i < eohelper.getNumSegments(); i++) {
                    eohelper.loadSegment(i);
                    Coordinate[] coordinates = eohelper.getFootprint().getCoordinates();
                    segments[i] = eohelper.getID();
                    for (int j = 0; j < 4 && coordinates != null && coordinates[j] != null; j++) {
                        segment.addPoint(coordinates[j].x, coordinates[j].y);
                    }
                    if (segment.intersects(polygonSicilyAndMalta.getBounds2D())) {
                        correctRegion = true;
                    } else {
                        correctRegion = false;
                        break;
                    }
                }
                if (correctRegion) {
                    referenceGroup = new OWSReferenceGroup();
                    referenceGroup.setDescription("Testing Reference Group");
                    referenceGroup.setTitle("Reference Group: " + task.getTaskId());
                    referenceGroup.setIdentifier("Reference Group " + DateTimeFormat.formatIso(new DateTime().getJulianTime(), 0));
                    List<OWSReference> references = referenceGroup.getReferenceList();
                    for (String seg : segments) {
                        reference = new OWSReference();
                        reference.setIdentifier("DE002376p_L1T" + seg);
                        reference.setDescription("Reference of segment: " + seg);
                        reference.setFormat("GEOTIFF");
                        reference.setHref(HMASPSMessageReceiverInOut.server + "/DE01_SL6_22P_1T_20101002T093704_20101002T093732_DMI_0_2376.html");
                        references.add(reference);
                    }
                } else {
                    referenceGroup = new OWSReferenceGroup();
                    referenceGroup.setDescription("Testing Reference Group");
                    referenceGroup.setTitle("Reference Group: " + task.getTaskId());
                    referenceGroup.setIdentifier("Reference Group " + DateTimeFormat.formatIso(new DateTime().getJulianTime(), 0));
                    List<OWSReference> references = referenceGroup.getReferenceList();
                    reference = new OWSReference();
                    reference.setIdentifier("Adquired Image #1");
                    reference.setDescription("Testing Reference");
                    reference.setFormat("image/tiff");
                    reference.setHref("http://deimos-space.com/test-endpoint");
                    references.add(reference);
                    reference = new OWSReference();
                    reference.setIdentifier("Adquired Image #2");
                    reference.setDescription("Testing Reference");
                    reference.setFormat("image/jp2");
                    reference.setHref("http://deimos-space.com/test-endpoint");
                    references.add(reference);
                }
                referenceGroups.add(referenceGroup);
            }
            response.setResultGroups(referenceGroups);
        }
        return response;
    }

    public boolean contains(Double x, Double y, PolygonD polygon) {
        return polygon.contains(x, y);
    }
}
