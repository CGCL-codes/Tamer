package bg.unisofia.fmi.kanban.entity.util;

import bg.unisofia.fmi.kanban.cumulativeflow.CumulativeFlowDiagram;
import bg.unisofia.fmi.kanban.entity.Project;
import bg.unisofia.fmi.kanban.entity.Task;
import bg.unisofia.fmi.kanban.entity.TaskHistoryRecord;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectUtil {

    public static Project getProjectForDate(Project aProject, Date aDate) {
        if (null == aDate) {
            return aProject;
        }
        Project project = new Project();
        project.setId(aProject.getId());
        project.setProjectName(aProject.getProjectName());
        project.setSprintLength(aProject.getSprintLength());
        project.setStartDate(aProject.getStartDate());
        project.setPhases(aProject.getPhases());
        List<Task> tasks = aProject.getTasks();
        List<Task> newTasks = new ArrayList<Task>();
        for (Task task : tasks) {
            if (null != task.getPhase(aDate)) {
                newTasks.add(getTaskForDate(task, aDate));
            }
        }
        project.setTasks(newTasks);
        return project;
    }

    private static Task getTaskForDate(Task aTask, Date aDate) {
        Task task = new Task();
        task.setTaskName(aTask.getTaskName());
        task.setDetailedDescription(aTask.getDetailedDescription());
        task.setAssignedPeople(aTask.getAssignedPeople());
        List<TaskHistoryRecord> historyRecords = aTask.getHistoryRecords();
        List<TaskHistoryRecord> newHistoryRecords = new ArrayList<TaskHistoryRecord>();
        for (TaskHistoryRecord record : historyRecords) {
            if (record.isAfterStart(aDate)) {
                newHistoryRecords.add(getTaskHistoryRecordForDate(record, aDate));
            }
        }
        task.setHistoryRecords(newHistoryRecords);
        return task;
    }

    private static TaskHistoryRecord getTaskHistoryRecordForDate(TaskHistoryRecord aTask, Date aDate) {
        TaskHistoryRecord task = new TaskHistoryRecord();
        task.setId(aTask.getId());
        task.setPhase(aTask.getPhase());
        task.setStartPhaseDate(aTask.getStartPhaseDate());
        if (aTask.getEndPhaseDate() != null && aDate.after(aTask.getEndPhaseDate())) {
            task.setEndPhaseDate(aTask.getEndPhaseDate());
        } else {
            task.setEndPhaseDate(null);
        }
        return task;
    }

    public static void main(String[] args) {
        new CumulativeFlowDiagram(kanban.Main.getFirstProject()).createChart();
    }
}
