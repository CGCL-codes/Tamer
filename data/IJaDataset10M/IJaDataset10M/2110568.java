package testy.refresh;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@MappedSuperclass
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "visibleId" }) })
public abstract class AbstractVisiblyIdentifiedHibernateObject<Type extends AbstractVisiblyIdentifiedHibernateObject> extends AbstractIdentifiedHibernateObject<Type> {

    protected Integer visibleId;

    @Column(unique = true, nullable = false)
    public Integer getVisibleId() {
        return visibleId;
    }

    public void setVisibleId(Integer visibleId) {
        this.visibleId = visibleId;
    }
}
