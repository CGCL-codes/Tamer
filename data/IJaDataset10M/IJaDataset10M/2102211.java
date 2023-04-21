package workflowSim;

import java.io.Writer;
import java.io.IOException;
import org.jdom.*;
import org.jdom.output.*;
import workflowSim.procInstance.HumanTask;
import workflowSim.procInstance.MachineTask;

/**
 * @version 0.1
 * @author Guoshen Kuang
 */
public class XMLEventLog implements EventLog {

    public XMLEventLog(Writer writer) {
        this.writer = writer;
        doc = new Document(new Element("EventLog"));
    }

    public void writeEntry(Event e) throws IOException {
        Element newElement = new Element("Event");
        newElement.setAttribute("Time", String.valueOf(e.time));
        if (e.type == Event.TYPE_NEW_INSTANCE) {
            newElement.setAttribute("Type", "NEW_INSTANCE");
            newElement.setAttribute("ProcID", String.valueOf(e.procInstance.getID()));
            newElement.setAttribute("ProcDefName", e.procInstance.getProcDef().name);
            newElement.setAttribute("EngineName", e.procInstance.getEngine().getName());
            newElement.setAttribute("MemLoad", String.valueOf(e.procInstance.getMemoryLoad()));
        } else if (e.type == Event.TYPE_NEW_TASK) {
            newElement.setAttribute("Type", "NEW_TASK");
            newElement.setAttribute("ProcID", String.valueOf(e.task.getProcInstance().getID()));
            newElement.setAttribute("TaskName", e.task.getName());
        } else if (e.type == Event.TYPE_TASK_FINISHED) {
            newElement.setAttribute("Type", "TASK_FINISH");
            newElement.setAttribute("ProcID", String.valueOf(e.task.getProcInstance().getID()));
            newElement.setAttribute("TaskName", String.valueOf(e.task.getName()));
            if (e.task.getClass().equals(HumanTask.class)) {
                HumanTask temp = (HumanTask) e.task;
                newElement.setAttribute("ActualTime", String.valueOf(temp.actualTime));
                newElement.setAttribute("ActualCost", String.valueOf(temp.actualCost));
                newElement.setAttribute("TriggerTime", String.valueOf(temp.triggerTime));
                newElement.setAttribute("Executor", temp.executor);
            } else {
                MachineTask temp = (MachineTask) e.task;
                newElement.setAttribute("ActualCost", String.valueOf(temp.actualCost));
                newElement.setAttribute("Executor", temp.executor);
            }
        } else {
            newElement.setAttribute("Type", "INSTANCE_FINISH");
            newElement.setAttribute("ProcID", String.valueOf(e.procInstance.getID()));
        }
        doc.getRootElement().addContent(newElement);
    }

    public void close() throws IOException {
        outputter.setFormat(Format.getPrettyFormat());
        outputter.output(doc, writer);
        writer.close();
    }

    public void flush() throws IOException {
        writer.flush();
    }

    private XMLOutputter outputter = new XMLOutputter();

    private Writer writer;

    private Document doc;
}
