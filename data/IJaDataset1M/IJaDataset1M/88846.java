package de.objectcode.time4u.server.ejb.entities;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import de.objectcode.time4u.server.api.MetaProperty;
import de.objectcode.time4u.server.api.MetaType;

@Entity
@Table(name = "TASKS")
public class Task implements Comparable<Task> {

    private long m_id;

    private String m_name;

    private String m_description;

    private boolean m_active;

    private boolean m_deleted;

    private boolean m_generic;

    private Project m_project;

    private Map<String, TaskProperty> m_metaProperties;

    @GeneratedValue
    @Id
    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    public boolean isActive() {
        return m_active;
    }

    public void setActive(boolean active) {
        m_active = active;
    }

    public boolean isDeleted() {
        return m_deleted;
    }

    public void setDeleted(boolean deleted) {
        m_deleted = deleted;
    }

    public boolean isGeneric() {
        return m_generic;
    }

    public void setGeneric(boolean generic) {
        m_generic = generic;
    }

    @Column(length = 30, nullable = false)
    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    @Column(length = 1000, nullable = true)
    public String getDescription() {
        return m_description;
    }

    public void setDescription(String description) {
        m_description = description;
    }

    @JoinColumn(name = "project_id", nullable = true)
    @ManyToOne
    public Project getProject() {
        return m_project;
    }

    public void setProject(Project project) {
        m_project = project;
    }

    @MapKey(name = "name")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "task")
    public Map<String, TaskProperty> getMetaProperties() {
        return m_metaProperties;
    }

    public void setMetaProperties(Map<String, TaskProperty> metaProperties) {
        m_metaProperties = metaProperties;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj == null) || !(obj instanceof Task)) {
            return false;
        }
        Task castObj = (Task) obj;
        return m_id == castObj.m_id;
    }

    public int compareTo(Task o) {
        if (m_id != o.m_id) {
            return (m_id < o.m_id) ? -1 : 1;
        }
        return 0;
    }

    public void toAPI(de.objectcode.time4u.server.api.Task task) {
        task.setId(m_id);
        task.setActive(m_active);
        task.setDeleted(m_deleted);
        task.setGeneric(m_generic);
        task.setName(m_name);
        task.setProjectId((m_project != null) ? m_project.getId() : null);
        task.setDescription(m_description);
        if (m_metaProperties != null) {
            for (TaskProperty property : m_metaProperties.values()) {
                if (property.getBoolValue() != null) {
                    task.addMetaProperties(new MetaProperty(property.getName(), property.getBoolValue()));
                } else if (property.getStrValue() != null) {
                    task.addMetaProperties(new MetaProperty(property.getName(), property.getStrValue()));
                } else if (property.getDateValue() != null) {
                    task.addMetaProperties(new MetaProperty(property.getName(), property.getDateValue()));
                } else if (property.getIntValue() != null) {
                    task.addMetaProperties(new MetaProperty(property.getName(), property.getIntValue()));
                }
            }
        }
    }

    public void fromAPI(Context context, de.objectcode.time4u.server.api.Task task) {
        m_active = task.isActive();
        m_deleted = task.isDeleted();
        m_generic = task.isGeneric();
        m_name = (task.getName() != null) ? task.getName() : "";
        if (context != null) {
            if (task.getProjectId() != null) {
                m_project = context.getManager().find(Project.class, task.getProjectId());
            } else {
                m_project = null;
            }
        }
        m_description = task.getDescription();
        if (m_metaProperties == null) {
            m_metaProperties = new HashMap<String, TaskProperty>();
        }
        if (task.getMetaProperties() != null) {
            for (MetaProperty property : task.getMetaProperties()) {
                TaskProperty taskProperty = m_metaProperties.get(property.getName());
                if (taskProperty == null) {
                    taskProperty = new TaskProperty();
                    taskProperty.setName(property.getName());
                    taskProperty.setTask(this);
                    m_metaProperties.put(property.getName(), taskProperty);
                }
                switch(MetaType.valueOf(property.getType())) {
                    case STRING:
                        {
                            taskProperty.setStrValue(property.getValue());
                            break;
                        }
                    case INTEGER:
                        {
                            taskProperty.setIntValue(Integer.parseInt(property.getValue()));
                            break;
                        }
                    case BOOLEAN:
                        {
                            taskProperty.setBoolValue(Boolean.parseBoolean(property.getValue()));
                            break;
                        }
                    case DATE:
                        {
                            try {
                                taskProperty.setDateValue(MetaProperty.g_format.parse(property.getValue()));
                            } catch (ParseException e) {
                            }
                            break;
                        }
                }
            }
        }
    }
}
