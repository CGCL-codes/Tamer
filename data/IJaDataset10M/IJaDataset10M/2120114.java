package websiteschema.persistence.rdbms;

import java.util.*;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import websiteschema.model.domain.Task;
import websiteschema.utils.DateUtil;

/**
 *
 * @author ray
 */
public class TaskTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-beans.xml");

    TaskMapper taskMapper = ctx.getBean("taskMapper", TaskMapper.class);

    long id = 0;

    @Test
    public void test() {
        update();
        updateStatus();
    }

    private void archive() {
        Date now = new Date();
        Date past = DateUtil.addDate(now, -1);
        System.out.println(past);
        Date date = DateUtil.parseDate(DateUtil.format(past, "yyyy-MM-dd"), "yyyy-MM-dd");
        System.out.println(date);
        taskMapper.batchDelete(date);
    }

    private void update() {
        Task task = taskMapper.getById(23);
        if (null != task) {
            taskMapper.update(task);
        }
    }

    private void updateStatus() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("status", "3");
        List<Task> tasks = taskMapper.getTasks(param);
        List<Long> ids = getList(tasks);
        if (null != ids && !ids.isEmpty()) {
            taskMapper.updateStatus(Task.TIMEOUT, ids);
        }
    }

    private List<Long> getList(List<Task> tasks) {
        List<Long> ret = new ArrayList<Long>();
        for (Task task : tasks) {
            ret.add(task.getId());
        }
        return ret;
    }
}
