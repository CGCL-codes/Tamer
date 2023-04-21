package net.community.chest.spring.test.entities;

import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import net.community.chest.lang.StringUtil;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Jan 20, 2011 10:14:15 AM
 */
@Entity
@Table(name = "embedding_entity")
public class EmbeddingEntity extends AbstractBaseEntity {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8480389728851952133L;

    public EmbeddingEntity() {
        super();
    }

    private List<EmbeddedEntity> _embList;

    @ElementCollection
    @CollectionTable(name = "embedded_instances", joinColumns = @JoinColumn(name = "OWNER_ID"))
    public List<EmbeddedEntity> getEmbeddedList() {
        return _embList;
    }

    public void setEmbeddedList(List<EmbeddedEntity> embList) {
        _embList = embList;
    }

    @Override
    public String toString() {
        return super.toString() + "[" + StringUtil.asStringList(getEmbeddedList(), ',') + "]";
    }
}
