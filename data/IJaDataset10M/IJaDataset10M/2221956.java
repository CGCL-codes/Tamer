package org.opennms.netmgt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.springframework.core.style.ToStringCreator;

@Entity
@Table(name = "applications")
public class OnmsApplication implements Comparable<OnmsApplication> {

    private Integer m_id;

    private String m_name;

    @Id
    @SequenceGenerator(name = "opennmsSequence", sequenceName = "opennmsNxtId")
    @GeneratedValue(generator = "opennmsSequence")
    public Integer getId() {
        return m_id;
    }

    public void setId(Integer id) {
        m_id = id;
    }

    @Column(name = "name", length = 32, nullable = false, unique = true)
    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public int compareTo(OnmsApplication o) {
        return getName().compareToIgnoreCase(o.getName());
    }

    @Override
    public String toString() {
        ToStringCreator creator = new ToStringCreator(this);
        creator.append("id", getId());
        creator.append("name", getName());
        return creator.toString();
    }
}
