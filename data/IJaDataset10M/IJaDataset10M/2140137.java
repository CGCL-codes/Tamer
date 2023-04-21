package upm.fi.oeg.test;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.Vector;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.DataSourceDataValueIterator;
import uk.org.ogsadai.client.toolkit.DataSourceResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.ServerProxy;
import uk.org.ogsadai.client.toolkit.activities.delivery.WriteToDataSource;
import uk.org.ogsadai.client.toolkit.activities.transform.TupleToByteArrays;
import uk.org.ogsadai.client.toolkit.resource.ResourceFactory;
import uk.org.ogsadai.resource.ResourceID;

public class NetworkedGraphsQuery {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        System.out.println(ts);
        URL serverBaseUrl;
        serverBaseUrl = new URL("http://localhost:3080/dai/services/");
        ResourceID drerId = new ResourceID("DataRequestExecutionResource");
        ServerProxy serverProxy = new ServerProxy();
        serverProxy.setDefaultBaseServicesURL(serverBaseUrl);
        DataRequestExecutionResource drer = serverProxy.getDataRequestExecutionResource(drerId);
        DataSourceResource dataSource = ResourceFactory.createDataSource(serverProxy, drer);
        String SPARQLquery = "";
        SPARQLquery = "SELECT ?coauthor ?birthdate " + "WHERE { " + "       { SERVICE <dbpedia> {" + "               ?person <http://xmlns.com/foaf/0.1/name> ?name ." + "               ?person <http://dbpedia.org/property/birth> ?birthdate . }}" + "       { SERVICE <dblp> {" + "               ?paper <http://purl.org/dc/elements/1.1/creator> <http://www4.wiwiss.fu-berlin.de/dblp/resource/person/100007> ." + "               ?paper <http://purl.org/dc/elements/1.1/creator> ?coauthor ." + "               ?coauthor <http://xmlns.com/foaf/0.1/name> ?name . } } " + "}";
        RDFActivity rdfActivity = new RDFActivity(SPARQLquery);
        TupleToByteArrays tupleToByteArrays = new TupleToByteArrays();
        tupleToByteArrays.connectDataInput(rdfActivity.getDataOutput());
        tupleToByteArrays.addSize(5000);
        WriteToDataSource writeToDS = new WriteToDataSource();
        writeToDS.setResourceID(dataSource.getResourceID());
        writeToDS.connectInput(tupleToByteArrays.getResultOutput());
        PipelineWorkflow pipeline = new PipelineWorkflow();
        pipeline.add(rdfActivity);
        pipeline.add(tupleToByteArrays);
        pipeline.add(writeToDS);
        RequestResource requestResource = null;
        long startTime = 0;
        long responseAfter = 0;
        try {
            startTime = System.currentTimeMillis();
            System.out.println("Start Time: " + startTime);
            requestResource = drer.execute(pipeline, RequestExecutionType.ASYNCHRONOUS);
            requestResource.pollUntilRequestStarted(100);
            responseAfter = System.currentTimeMillis() - startTime;
            System.out.println("Response after: " + responseAfter);
        } catch (Throwable e) {
            System.out.println("A problem has occured...");
            while (e != null) {
                System.out.println(e.getMessage());
                e = e.getCause();
            }
            System.exit(1);
        }
        System.out.println("Data Source Status... " + dataSource.getStatus().toString());
        System.out.println(requestResource.getRequestExecutionStatus().toString());
        DataSourceDataValueIterator dataValueIterator = new DataSourceDataValueIterator(dataSource);
        dataValueIterator.setNumBlocksPerCall(20);
        tupleToByteArrays.getResultOutput().setDataValueIterator(dataValueIterator);
        if (tupleToByteArrays.hasNextResult()) {
            ResultSet rs = tupleToByteArrays.nextResultAsResultSet();
            ResultSetMetaData md = rs.getMetaData();
            int numColumns = md.getColumnCount();
            String[] columns = new String[numColumns];
            int[] widths = new int[numColumns];
            for (int i = 0; i < numColumns; i++) {
                String column = md.getColumnLabel(i + 1);
                columns[i] = column;
                widths[i] = column.length();
            }
            Vector<String[]> rows = new Vector<String[]>();
            while (rs.next()) {
                String[] fields = new String[numColumns];
                for (int i = 0; i < numColumns; i++) {
                    fields[i] = rs.getString(i + 1);
                    widths[i] = Math.max(widths[i], fields[i].length());
                }
                rows.add(fields);
            }
            rs.close();
            long totalTime1 = System.currentTimeMillis() - startTime;
            System.out.println("total time before printing results: " + totalTime1);
            String tableHeading = "| ";
            for (int i = 0; i < numColumns; i++) {
                tableHeading += (pad(columns[i], widths[i]) + " | ");
            }
            System.out.println(tableHeading);
            int j = 0;
            for (j = 0; j < rows.size(); j++) {
                String[] row = rows.get(j);
                String rowString = "| ";
                for (int i = 0; i < numColumns; i++) {
                    rowString += (pad(row[i], widths[i]) + " | ");
                }
                System.out.println(rowString);
            }
            System.out.println("result number: " + j);
        }
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("total time: " + totalTime);
    }

    /**
     * Pad a string out to a given width with space characters.
     * 
     * @param base
     *            String to pad.
     * @param width
     *            If greater than length of <code>base</code> then append
     *            <code>width - base.size()</code> spaces to <code>base</code>.
     * @return padded string.
     */
    public static String pad(String base, int width) {
        StringBuffer baseBuffer = new StringBuffer(base);
        int padLength = width - base.length();
        for (int i = 0; i < padLength; i++) {
            baseBuffer.append(" ");
        }
        return baseBuffer.toString();
    }
}
