package net.taylor.agile.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import net.taylor.agile.entity.Backlog;
import net.taylor.agile.entity.Card;
import net.taylor.agile.entity.CardType;
import net.taylor.identity.partition.Partition;
import net.taylor.validator.Required;
import net.taylor.validator.Unique;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.NaturalId;
import org.hibernate.envers.Audited;

/**
 * @todo add comment for javadoc
 *
 * @author jgilbert
 * @generated
 */
@Entity
@Audited
@Unique(message = "Card must be unique.")
public class Card implements Serializable, Cloneable {

    /** @generated */
    private static final long serialVersionUID = 1L;

    /** @generated */
    public Card() {
    }

    /**
	 * ------------------------------------------
	 * The primary key.
	 *
	 * @generated
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /** @generated */
    public void setId(final Long id) {
        this.id = id;
    }

    /** @generated */
    private Long id = null;

    /**
	 * ------------------------------------------
	 * @todo add comment for javadoc
	 *
	 * @generated
	 */
    @Index(name = "CARD_TITLE_IDX")
    @Required
    public String getTitle() {
        return title;
    }

    /** @generated */
    public void setTitle(final String title) {
        this.title = title;
    }

    /** @generated */
    private String title = null;

    /**
	 * ------------------------------------------
	 * @todo add comment for javadoc
	 *
	 * @generated
	 */
    @Index(name = "CARD_STATUS_IDX")
    public String getStatus() {
        return status;
    }

    /** @generated */
    public void setStatus(final String status) {
        this.status = status;
    }

    /** @generated */
    private String status = null;

    /**
	 * ------------------------------------------
	 * @todo add comment for javadoc
	 *
	 * @generated
	 */
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @Index(name = "CARD_TYPE_IDX")
    public CardType getType() {
        return type;
    }

    /** @generated */
    public void setType(final CardType type) {
        this.type = type;
    }

    /** @generated */
    private CardType type = null;

    /**
	 * ------------------------------------------
	 * @todo add comment for javadoc
	 *
	 * @generated
	 */
    public Long getEstimate() {
        return estimate;
    }

    /** @generated */
    public void setEstimate(final Long estimate) {
        this.estimate = estimate;
    }

    /** @generated */
    private Long estimate = null;

    /**
	 * ------------------------------------------
	 * @todo add comment for javadoc
	 *
	 * @generated
	 */
    public String getText() {
        return text;
    }

    /** @generated */
    public void setText(final String text) {
        this.text = text;
    }

    /** @generated */
    private String text = null;

    /**
	 * ------------------------------------------
	 * @todo add comment for javadoc
	 *
	 * @generated
	 */
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @Index(name = "CARD_BACKLOG_IDX")
    public Backlog getBacklog() {
        return backlog;
    }

    /** @generated */
    public void setBacklog(final Backlog backlog) {
        this.backlog = backlog;
    }

    /** @generated */
    private Backlog backlog = null;

    /**
	 * ------------------------------------------
	 * @todo add comment for javadoc
	 *
	 * @generated
	 */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
    public List<Card> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<Card>();
        }
        return children;
    }

    /** @generated */
    public void setChildren(final List<Card> children) {
        this.children = children;
    }

    /**
	 * Associate Card with Card
	 * @generated
	 */
    public void addChildren(Card children) {
        if (children == null) {
            return;
        }
        getChildren().add(children);
        children.setParent(this);
    }

    /**
	 * Unassociate Card from Card
	 * @generated
	 */
    public void removeChildren(Card children) {
        if (children == null) {
            return;
        }
        getChildren().remove(children);
        children.setParent(null);
    }

    /**
	 * @generated
	 */
    public void removeAllChildren() {
        List<Card> remove = new ArrayList<Card>();
        remove.addAll(getChildren());
        for (Card element : remove) {
            removeChildren(element);
        }
    }

    /** @generated */
    private List<Card> children = null;

    /**
	 * ------------------------------------------
	 * @todo add comment for javadoc
	 *
	 * @generated
	 */
    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @NaturalId(mutable = true)
    @Index(name = "CARD_PARENT_IDX")
    public Card getParent() {
        return parent;
    }

    /** @generated */
    public void setParent(final Card parent) {
        this.parent = parent;
    }

    /** @generated */
    private Card parent = null;

    /**
	 * ------------------------------------------
	 * @todo add comment for javadoc
	 *
	 * @generated
	 */
    @Index(name = "CARD_NUM_IDX")
    @Required
    public String getNumber() {
        return number;
    }

    /** @generated */
    public void setNumber(final String number) {
        this.number = number;
    }

    /** @generated */
    private String number = null;

    /**
	 * ------------------------------------------
	 * @todo add comment for javadoc
	 *
	 * @generated
	 */
    @Index(name = "CARD_RANK_IDX")
    public Long getRank() {
        return rank;
    }

    /** @generated */
    public void setRank(final Long rank) {
        this.rank = rank;
    }

    /** @generated */
    private Long rank = null;

    /**
	 * ------------------------------------------
	 * The security group that is used to partition data for restricting access.
	 *
	 * @generated
	 */
    @Partition
    @Index(name = "CARD_PARTITION_IDX")
    @NaturalId(mutable = true)
    public String getPartition() {
        return partition;
    }

    /** @generated */
    public void setPartition(final String partition) {
        this.partition = partition;
    }

    /** @generated */
    private String partition = null;

    /** @generated */
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
        builder.append("id", getId());
        builder.append("title", getTitle());
        builder.append("status", getStatus());
        builder.append("estimate", getEstimate());
        builder.append("text", getText());
        builder.append("number", getNumber());
        builder.append("rank", getRank());
        builder.append("partition", getPartition());
        builder.append("type", getType());
        builder.append("backlog", getBacklog());
        builder.append("parent", getParent());
        return builder.toString();
    }

    /** @generated */
    public Card deepClone() throws Exception {
        Card clone = (Card) super.clone();
        clone.setId(null);
        clone.setChildren(null);
        for (Card kid : this.getChildren()) {
            clone.addChildren(kid.deepClone());
        }
        return clone;
    }

    /** @generated */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((id == null) ? super.hashCode() : id.hashCode());
        return result;
    }

    /** @generated */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Card)) return false;
        final Card other = (Card) obj;
        if (id == null) {
            if (other.getId() != null) return false;
        } else if (!id.equals(other.getId())) return false;
        return true;
    }
}
