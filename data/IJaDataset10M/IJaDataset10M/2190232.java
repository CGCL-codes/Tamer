package fr.umlv.jee.hibou.bean;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bean class for Task list object.
 * @author micka, alex, nak, matt
 *
 */
@XmlRootElement(name = "taskListBean", namespace = "http://javax.hibou/jaxws")
public class TaskListBean {

    @XmlElement(name = "list", namespace = "")
    @XmlList
    private TaskBean[] list;

    public TaskListBean(List<TaskBean> list) {
        this.list = list.toArray(new TaskBean[list.size()]);
    }

    /**
	 * @return the list
	 */
    public TaskBean[] getList() {
        return list;
    }

    /**
	 * @param list the list to set
	 */
    public void setList(TaskBean[] list) {
        this.list = list;
    }
}
