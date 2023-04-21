package org.authorsite.domain.content;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import org.hibernate.annotations.IndexColumn;

/**
 *
 * @author jejking
 */
@Entity
public class ContainerNode extends AbstractNode {

    private List<AbstractNode> children;

    public ContainerNode() {
        super();
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    @IndexColumn(name = "node_position", base = 1)
    public List<AbstractNode> getChildren() {
        return children;
    }

    public void setChildren(List<AbstractNode> children) {
        this.children = children;
    }
}
