package remote.entities.discover;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

/**
 * @author Kasza Miklós
 */
@Entity
public class GraphEntity {

    private Long _id;

    private Collection<EntityAttribute> _attributes;

    @OneToMany
    public Collection<EntityAttribute> getAttributes() {
        return _attributes;
    }

    public void setAttributes(Collection<EntityAttribute> attributes) {
        _attributes = attributes;
    }

    @TableGenerator(name = "graphEntityIdGenerator", table = "IdGenerator", pkColumnName = "IdKey", valueColumnName = "IdValue", pkColumnValue = "NSGraphEntity")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "graphEntityIdGenerator")
    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }
}
