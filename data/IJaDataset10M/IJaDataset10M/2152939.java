package net.sf.doolin.app.mt.back.dao;

import java.util.List;
import net.sf.doolin.app.mt.MTHistoryType;
import org.joda.time.DateTime;

public class HistoryDAO extends AbstractDAO {

    private TaskDAO task;

    private DateTime date;

    private MTHistoryType code;

    private List<String> parameters;

    public MTHistoryType getCode() {
        return this.code;
    }

    public DateTime getDate() {
        return this.date;
    }

    public List<String> getParameters() {
        return this.parameters;
    }

    public TaskDAO getTask() {
        return this.task;
    }

    public void setCode(MTHistoryType code) {
        this.code = code;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public void setTask(TaskDAO task) {
        this.task = task;
    }
}
