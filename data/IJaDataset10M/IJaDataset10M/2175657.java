package sywico.core.checksumreport;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import sywico.core.message.Message;

/**
 * 
 * A check sum report is a list of all filenames found in one directory and subdirectories, associated with their checksum.
 * In addition it contains the Filter which was used to produce the list of filenames.
 * Its main purpose is to detect if a Message sent by the other party applies to the correct set of files. 
 * 
 */
public class FileAndDirReport implements Serializable {

    static final long serialVersionUID = Message.serialVersionUID;

    private String comments;

    private Filter filter;

    private List<ReportEntry> checksumList;

    public List<ReportEntry> getChecksumList() {
        return checksumList;
    }

    public Filter getFilter() {
        return filter;
    }

    public FileAndDirReport(String comments, Filter filter) {
        this(comments, filter, new LinkedList<ReportEntry>());
    }

    public FileAndDirReport(String comments, Filter filter, List<ReportEntry> fileAndChecksums) {
        this.comments = comments;
        this.filter = filter;
        this.checksumList = fileAndChecksums;
    }

    public String toString() {
        String retVal = "report:\ninfo\n:" + comments + "\n" + filter;
        retVal += "\n" + checksumList.size() + " files";
        for (ReportEntry fileAndChecksum : checksumList) retVal += "\n" + fileAndChecksum;
        return retVal;
    }

    public String getComments() {
        return comments;
    }

    /**
     * the comment field is not taken into account for the equals() method
     * 
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof FileAndDirReport)) return false;
        FileAndDirReport checksumReport = (FileAndDirReport) obj;
        return filter.equals(checksumReport.filter) && checksumList.equals(checksumReport.checksumList);
    }

    public int hashCode() {
        return checksumList.hashCode();
    }
}
