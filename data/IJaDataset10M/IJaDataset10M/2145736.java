package com.dqgen.criteria;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import com.dqgen.mapper.engine.JoinType;
import com.dqgen.mapper.engine.Table;
import com.dqgen.metamodel.MetaModel;

/**
 * <p>Represents a from clause join table</p>
 * 
 * @author sganesh
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Join extends From {

    @XmlAttribute
    private JoinType joinType;

    @XmlAttribute
    private boolean inverse;

    protected From parent;

    @XmlElements({ @XmlElement(name = "joinCondition", type = JoinCondition.class), @XmlElement(name = "joinConstraint", type = JoinConstraint.class) })
    private List<Constraint> joinConditions = new ArrayList<Constraint>();

    /**
	 * <p>for Jaxb usage</p>
	 */
    Join() {
    }

    /**
	 * <p>Constructor with meta model</p>
	 * 
	 * @param parent the parent table to join with
	 * @param model the meta model
	 * @param alias the alias of the meta model table
	 * @param joinType the join type
	 */
    public Join(From parent, MetaModel model, String alias, JoinType joinType) {
        super(model, alias);
        this.parent = parent;
        this.joinType = joinType;
    }

    /**
	 * <p>Constructor with table</p>
	 * 
	 * @param parent the parent table to join with
	 * @param table the join table
	 * @param alias the alias of the table
	 * @param joinType the join type
	 */
    public Join(From parent, Table table, String alias, JoinType joinType) {
        super(table, alias);
        this.joinType = joinType;
    }

    /**
	 * <p>Adds the join condition</p>
	 * 
	 * @param leftPath the path to the left column
	 * @param rightPath the path to the right column
	 */
    public void addJoinCondition(Path leftPath, Path rightPath) {
        this.joinConditions.add(new JoinCondition(leftPath.getColumn(), rightPath.getColumn()));
    }

    /**
	 * @return the joinType
	 */
    JoinType getJoinType() {
        return joinType;
    }

    /**
	 * @param joinType the joinType to set
	 */
    void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    /**
	 * @return the joinConditions
	 */
    List<Constraint> getJoinConditions() {
        return joinConditions;
    }

    /**
	 * @param conditions the joinConditions to set 
	 */
    void setJoinConditions(List<Constraint> conditions) {
        this.joinConditions = conditions;
    }

    /**
	 * @return the inverse
	 */
    boolean isInverse() {
        return inverse;
    }

    /**
	 * @param inverse the inverse to set
	 */
    void setInverse(boolean inverse) {
        this.inverse = inverse;
    }

    @Override
    @XmlElement(name = "join")
    List<Join> getJoins() {
        return super.getJoins();
    }

    /**
	 * <p>Invoked by JAXB before unmarshalling this element</p>
	 * 
	 * @param unmarshaller the unmarshaller used by JAXB
	 * @param parent the parent of this element
	 */
    void beforeUnmarshal(Unmarshaller unmarshaller, Object parent) {
        if (parent instanceof From) {
            this.query = ((From) parent).query;
            this.parent = ((From) parent);
        }
    }

    /**
	 * @return the parent
	 */
    From getParent() {
        return parent;
    }

    /**
	 * @param parent the parent to set
	 */
    void setParent(From parent) {
        this.parent = parent;
    }
}
