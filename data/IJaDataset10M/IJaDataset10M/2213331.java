package uk.ac.ed.rapid.jsp.output;

public class JobStatus {

    public static final String ELEMENT = "status";

    public static String start() {
        String result = "";
        result += "<% jobManager = jobQueueTable.getJobManager(rapidData, selection.getJobID());%>\n";
        result += "<%=jobManager.monitor().get(selection.getSubJobIndex())%>";
        return result;
    }
}
