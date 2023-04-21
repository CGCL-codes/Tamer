package atodolist.exports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import atodolist.date.ToDoDate;
import atodolist.model.ToDoBook;
import atodolist.model.ToDoConstants;
import atodolist.model.ToDoTask;

/**
 * @author Usuario
 *
 */
public class XMLExport implements IExport {

    static Logger logger = Logger.getLogger("atodolist.exports.XMLExport");

    public boolean saveFile(ToDoBook book, File file) {
        File outFile = file;
        if (file == null) {
            logger.error("XMLExport: not file to save!!!!... we use example.xml");
            outFile = new File("example.xml");
        }
        OutputStreamWriter fw;
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(outFile);
            fw = new OutputStreamWriter(fos, "UTF8");
            bw = new BufferedWriter(fw);
            bw.write(this.bookToString(book));
        } catch (IOException e) {
            logger.error("Can't write to output file " + outFile.getAbsolutePath(), e);
            return false;
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                logger.error("Can't close output file " + outFile.getAbsolutePath(), e);
            }
            try {
                fos.close();
            } catch (IOException e) {
                logger.error("Can't close output file " + outFile.getAbsolutePath(), e);
            }
        }
        return true;
    }

    protected String bookToString(ToDoBook tdb) {
        Element root = new Element("TODOLIST");
        root.setAttribute(ToDoConstants.FILEFORMAT, String.valueOf(tdb.getFileFormat()));
        root.setAttribute(ToDoConstants.FILEVERSION, String.valueOf(tdb.getFileVersion()));
        root.setAttribute(ToDoConstants.NEXTUNIQUEID, String.valueOf(tdb.getNextUniqueID()));
        root.setAttribute(ToDoConstants.PROJECTNAME, tdb.getProjectName());
        Date lastModified = tdb.getLastModified();
        if (tdb.getLastModified() == null) lastModified = new Date();
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        root.setAttribute(ToDoConstants.LASTMODIFIED, formatter.format(lastModified));
        root.setAttribute(ToDoConstants.CUSTOMCOMMENTSTYPE, tdb.getCustomCommentsType());
        ToDoTask mainTask = tdb.getTaskBook();
        int numTask = mainTask.getNumChilds();
        for (int i = 0; i < numTask; i++) {
            root.addContent(taskToString(mainTask.getChild(i)));
        }
        XMLOutputter xmlOut = new XMLOutputter();
        xmlOut.setFormat(Format.getPrettyFormat());
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xmlOut.outputString(root);
    }

    private Element taskToString(ToDoTask task) {
        Element node = new Element(ToDoConstants.TASK);
        setAttribute(node, ToDoConstants.PRIORITYCOLOR, task.getPriorityColor());
        setAttribute(node, ToDoConstants.STARTDATESTRING, task.getStartDate());
        setAttribute(node, ToDoConstants.RISK, task.getRisk());
        setAttribute(node, ToDoConstants.PRIORITY, task.getPriority());
        setAttribute(node, ToDoConstants.STATUS, task.getStatus());
        setAttribute(node, ToDoConstants.DUEDATESTRING, task.getDueDate());
        setAttribute(node, ToDoConstants.ALLOCATEDBY, task.getAllocatedBy());
        setAttribute(node, ToDoConstants.LASTMOD, task.getLastModifiedDate());
        setAttribute(node, ToDoConstants.LASTMODSTRING, task.getLastModifiedDate(), true);
        setAttribute(node, ToDoConstants.TITLE, task.getTitle());
        setAttribute(node, ToDoConstants.CREATIONDATE, task.getCreationDate());
        setAttribute(node, ToDoConstants.CREATIONDATESTRING, task.getCreationDate(), true);
        setAttribute(node, ToDoConstants.TIMESPENTUNITS, task.getTimeSpentUnits());
        setAttribute(node, ToDoConstants.PERSON, task.getPerson());
        setAttribute(node, ToDoConstants.TIMEESTIMATE, task.getTimeEstimated());
        setAttribute(node, ToDoConstants.CATEGORY, task.getCategory());
        setAttribute(node, ToDoConstants.COMMENTS, task.getComments());
        setAttribute(node, ToDoConstants.PRIORITYWEBCOLOR, task.getPriorityWebColor());
        setAttribute(node, ToDoConstants.TIMESPENT, task.getTimeSpent());
        setAttribute(node, ToDoConstants.FILEREFPATH, task.getFileReferingPath());
        setAttribute(node, ToDoConstants.ID, String.valueOf(task.getId()));
        setAttribute(node, ToDoConstants.DUEDATE, task.getDueDate());
        setAttribute(node, ToDoConstants.COST, task.getCost());
        setAttribute(node, ToDoConstants.PERCENTDONE, task.getPercentDone());
        setAttribute(node, ToDoConstants.DEPENDS, task.getDepends());
        setAttribute(node, ToDoConstants.STARTDATE, task.getStartDate());
        setAttribute(node, ToDoConstants.DONEDATE, task.getDoneDate());
        setAttribute(node, ToDoConstants.DONEDATESTRING, task.getDoneDate());
        setAttribute(node, ToDoConstants.POS, task.getPos());
        int numTasks = task.getNumChilds();
        for (int i = 0; i < numTasks; i++) {
            node.addContent(taskToString(task.getChild(i)));
        }
        return node;
    }

    private void setAttribute(Element node, String attribute, Object obj) {
        if (obj == null) return;
        if (obj instanceof Date) {
            if (attribute.endsWith("STRING")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                node.setAttribute(attribute, formatter.format((Date) obj));
            } else {
                double date = ToDoDate.getToDoListDoubleFromDate((Date) obj);
                date = this.round(date, 8);
                node.setAttribute(attribute, String.valueOf((double) date));
            }
        } else if (obj instanceof String) {
            node.setAttribute(attribute, (String) obj);
        }
    }

    /**
     * Format a Date attribute
     * @param node XML node
     * @param attribute The node attribute's name
     * @param obj The Date of the attribute
     * @param time <code>true</code> to format with yyyy-mm-dd (hh:mm:ss), and <code>false</code>
     *        to code as yyyy-mm-dd
     */
    private void setAttribute(Element node, String attribute, Date obj, boolean time) {
        if (obj == null) return;
        if (time) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy (H:mm:ss)");
            node.setAttribute(attribute, formatter.format((Date) obj));
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            node.setAttribute(attribute, formatter.format((Date) obj));
        }
    }

    private void setAttribute(Element node, String attribute, int i) {
        node.setAttribute(attribute, String.valueOf(i));
    }

    private void setAttribute(Element node, String attribute, double i) {
        node.setAttribute(attribute, String.valueOf(i));
    }

    double round(double value, int decimalPlace) {
        double power_of_ten = 1;
        while (decimalPlace-- > 0) power_of_ten *= 10.0;
        return Math.round(value * power_of_ten) / power_of_ten;
    }
}
