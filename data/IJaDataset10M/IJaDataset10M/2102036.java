package cnerydb;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Dave Oxley <dave@daveoxley.co.uk>
 */
public class BaseEntity implements Serializable {

    private UUID id;

    public BaseEntity() {
        id = UUID.randomUUID();
    }

    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    protected UUID getUUID() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return (o == this || (o instanceof BaseEntity && id.equals(((BaseEntity) o).id)));
    }
}
