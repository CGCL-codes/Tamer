package reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Project;
import model.Stage;
import model.task.Task;
import model.task.TaskState;
import org.hibernate.HibernateException;
import database.RugidoQuery;

public class ProjectQueryProvider implements QueryProvider {

    private Integer runningStages = 0;

    private Integer finishedStages = 0;

    public List<WrapperBean> getObjects(CriteriaSet criteria) throws HibernateException {
        List<WrapperBean> results = new ArrayList<WrapperBean>();
        Project project = RugidoQuery.getInstance().loadProject(((ProjectCriteriaSet) criteria).getProjectId());
        List<Stage> stages = new ArrayList<Stage>();
        stages.addAll(project.getStages());
        Collections.sort(stages);
        for (Stage stage : stages) {
            boolean isFinished = stage.getTasks().size() > 0;
            WrapperBean wrapper = new WrapperBean();
            wrapper.setName(stage.getName());
            results.add(wrapper);
            for (Task task : stage.getTasks()) {
                wrapper = new WrapperBean();
                wrapper.setName(stage.getName());
                wrapper.setTaskName(task.getName());
                wrapper.setPriority(task.getPriority());
                wrapper.setStartDate(task.getStartDate());
                wrapper.setFinishDate(task.getFinishDate());
                wrapper.setState(task.getState());
                isFinished = ((isFinished) && (task.getState() == TaskState.Finished));
                results.add(wrapper);
            }
            if (isFinished) this.finishedStages++; else this.runningStages++;
        }
        Collections.sort(results);
        return results;
    }

    public Integer getRunningStages() {
        return this.runningStages;
    }

    public Integer getFinishedStages() {
        return this.finishedStages;
    }
}
