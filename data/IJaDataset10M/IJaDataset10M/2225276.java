package ru.aslanov.schedule.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Created: Feb 2, 2010 11:33:05 AM
 *
 * @author Sergey Aslanov
 */
@XmlRootElement(name = "data")
public class DataList<T> {

    private Collection<T> data;

    public DataList() {
        data = new ArrayList<T>();
    }

    public DataList(Collection data) {
        this.data = data;
    }

    @XmlElements({ @XmlElement(name = "teacher", type = Teacher.class), @XmlElement(name = "location", type = Location.class), @XmlElement(name = "dance", type = Dance.class), @XmlElement(name = "level", type = Level.class), @XmlElement(name = "group", type = Group.class), @XmlElement(name = "schedule-item", type = ScheduleItem.class), @XmlElement(name = "item", type = DataItem.class) })
    public Collection getData() {
        return data;
    }

    public void addItem(T item) {
        data.add(item);
    }

    public void setData(Collection data) {
        this.data = data;
    }
}
