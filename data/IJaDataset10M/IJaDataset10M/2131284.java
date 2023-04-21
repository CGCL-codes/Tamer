package workflowSim;

import java.io.BufferedWriter;
import java.lang.StringBuffer;
import workflowSim.procInstance.*;

/**
 * �ı���ʽ����־��¼
 * @version 0.1
 * @author Guoshen Kuang
 */
public class TextEventLog implements EventLog {

    public TextEventLog(BufferedWriter writer) {
        this.writer = writer;
    }

    public void writeEntry(Event e) {
        StringBuffer record = new StringBuffer();
        record.append(e.time);
        record.append("\t");
        if (e.type == Event.TYPE_NEW_INSTANCE) {
            record.append("NEW_INSTANCE\t");
            record.append("ProcID:" + e.procInstance.getID() + "\t");
            record.append("ProcDefName:" + e.procInstance.getProcDef().name + "\t");
            record.append("EngineName:" + e.procInstance.getEngine().getName() + "\t");
            record.append("MemLoad:" + e.procInstance.getMemoryLoad());
        } else if (e.type == Event.TYPE_NEW_TASK) {
            record.append("NEW_TASK\t");
            record.append("ProcID:" + e.task.getProcInstance().getID() + "\t");
            record.append("TaskName:" + e.task.getName());
        } else if (e.type == Event.TYPE_TASK_FINISHED) {
            record.append("TASK_FINISH\t");
            record.append("ProcID:" + e.task.getProcInstance().getID() + "\t");
            record.append("TaskName:" + e.task.getName() + "\t");
            if (e.task.getClass().equals(HumanTask.class)) {
                HumanTask temp = (HumanTask) e.task;
                record.append("ActualTime:" + temp.actualTime + "\t");
                record.append("ActualCost:" + temp.actualCost + "\t");
                record.append("TriggerTime:" + temp.triggerTime + "\t");
                record.append("Executor:" + temp.executor);
            } else {
                MachineTask temp = (MachineTask) e.task;
                record.append("ActualCost:" + temp.actualCost + "\t");
                record.append("Executor:" + temp.executor);
            }
        } else {
            record.append("INSTANCE_FINISH\t");
            record.append("ProcID:" + e.procInstance.getID());
        }
        record.append("\n");
        try {
            writer.write(record.toString());
            writer.flush();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public void flush() {
        try {
            writer.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private BufferedWriter writer;
}
