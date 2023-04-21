package org.opennms.netmgt.config;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import javax.servlet.http.HttpSession;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.opennms.netmgt.config.kscReports.Graph;
import org.opennms.netmgt.config.kscReports.Report;

public class KscReportEditor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * This is a working report that may be used to hold a report & its index
     * temporarily while moving between jsp's
     */
    private Report m_workingReport = null;

    private int m_workingGraphIndex = -1;

    /**
     * This is a working graph that may be used to hold a report graph & its index temporarily while moving between jsp's
     */
    private Graph m_workingGraph = null;

    public KscReportEditor() {
    }

    /** Create a new blank report & initialize it */
    private static Report getNewReport() {
        Report new_report = new Report();
        new_report.setTitle("New Report Title");
        new_report.setShow_graphtype_button(false);
        new_report.setShow_timespan_button(false);
        return new_report;
    }

    /** Returns the working report object */
    public Report getWorkingReport() {
        return m_workingReport;
    }

    private void setWorkingReport(Report report) {
        m_workingReport = report;
    }

    /** Returns the working graph object */
    public Graph getWorkingGraph() {
        return m_workingGraph;
    }

    /** Returns the working graph index */
    public int getWorkingGraphIndex() {
        return m_workingGraphIndex;
    }

    /** Create a new blank graph & initialize it */
    public static Graph getNewGraph() {
        Graph new_graph = new Graph();
        new_graph.setTitle("");
        new_graph.setTimespan("7_day");
        return new_graph;
    }

    /**
     * Loads the indexed graph from the working report into the working graph
     * object or creates a new one if the object does not exist
     */
    public void loadWorkingGraph(int index) throws MarshalException, ValidationException {
        int total_graphs = m_workingReport.getGraphCount();
        m_workingGraphIndex = index;
        if ((m_workingGraphIndex < 0) || (m_workingGraphIndex >= total_graphs)) {
            m_workingGraph = getNewGraph();
            m_workingGraphIndex = -1;
        } else {
            m_workingGraph = duplicateCastorObject(m_workingReport.getGraph(m_workingGraphIndex), Graph.class);
        }
    }

    /**
     * Unloads the working graph into the working report list at the requested
     * graph number. If the graph was modified from an existing graph, then the
     * old one is replaced. A new blank working graph is then created
     */
    public void unloadWorkingGraph(int requested_graphnum) throws MarshalException, ValidationException {
        int total_graphs = m_workingReport.getGraphCount();
        int insert_location = requested_graphnum--;
        if ((m_workingGraphIndex >= 0) && (m_workingGraphIndex < total_graphs)) {
            m_workingReport.removeGraph(m_workingReport.getGraph(m_workingGraphIndex));
        }
        if ((insert_location < 0) || (insert_location >= total_graphs)) {
            m_workingReport.addGraph(m_workingGraph);
        } else {
            m_workingReport.addGraph(insert_location, m_workingGraph);
        }
        m_workingGraph = getNewGraph();
        m_workingGraphIndex = -1;
    }

    /**
     * Loads the source report into the working report object as a new report.
     */
    public void loadWorkingReport(Report report) throws MarshalException, ValidationException {
        setWorkingReport(duplicateCastorObject(report, Report.class));
        getWorkingReport().deleteId();
    }

    /**
     * Loads the indexed report into the working report object.
     */
    public void loadWorkingReport(KSC_PerformanceReportFactory factory, int index) throws MarshalException, ValidationException {
        Report report = factory.getReportByIndex(index);
        if (report == null) {
            throw new IllegalArgumentException("Could not find report with ID " + index);
        }
        setWorkingReport(duplicateCastorObject(report, Report.class));
    }

    /**
     * Loads the indexed report into the working report object as a duplicate
     * report.  The ID in the loaded report will be removed so a new ID will
     * be created when the duplicated report is saved.
     */
    public void loadWorkingReportDuplicate(KSC_PerformanceReportFactory factory, int index) throws MarshalException, ValidationException {
        loadWorkingReport(factory, index);
        getWorkingReport().deleteId();
    }

    /**
     * Loads a newly created report into the working report object.
     */
    public void loadNewWorkingReport() {
        setWorkingReport(getNewReport());
        getWorkingReport().deleteId();
    }

    /**
     * Unloads the working report into the indexed report list at the point
     * identified by working_index (this should have been set when the working
     * report was loaded), then create a new blank working report
     */
    public void unloadWorkingReport(KSC_PerformanceReportFactory factory) throws MarshalException, ValidationException {
        if (getWorkingReport().hasId()) {
            factory.setReport(getWorkingReport().getId(), getWorkingReport());
        } else {
            factory.addReport(getWorkingReport());
        }
        loadNewWorkingReport();
    }

    @SuppressWarnings("unchecked")
    private <T> T duplicateCastorObject(T object, Class<T> clazz) throws MarshalException, ValidationException {
        StringWriter stringWriter = new StringWriter();
        Marshaller.marshal(object, stringWriter);
        StringReader stringReader = new StringReader(stringWriter.toString());
        return (T) Unmarshaller.unmarshal(clazz, stringReader);
    }

    public static KscReportEditor getFromSession(HttpSession session, boolean required) {
        String attributeName = KscReportEditor.class.getName();
        if (session.getAttribute(attributeName) == null) {
            if (required) {
                throw new IllegalStateException("The KSC report editing session is not open--please restart your edits.  This could be due to your session expiring on the server due to inactivity or the server being restarted.");
            } else {
                session.setAttribute(attributeName, new KscReportEditor());
            }
        }
        return (KscReportEditor) session.getAttribute(attributeName);
    }
}
