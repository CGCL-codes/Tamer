package org.linkedgeodata.tagmapping.client.entity;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author raven
 *
 */
@MappedSuperclass
public abstract class AbstractTagMapperState implements IEntity, Serializable, IsSerializable {

    private static final long serialVersionUID = 1L;

    private int id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
