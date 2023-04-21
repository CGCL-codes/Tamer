package net.sf.webwarp.modules.partner.partner;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import net.sf.webwarp.util.hibernate.dao.impl.AIDMutationTypeImpl;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Proxy;

/**
 * Entity object that models a relation instance between two partner instances.
 * 
 * @author mos
 */
@Entity
@Proxy(lazy = false)
public class Relation extends AIDMutationTypeImpl<Long> {

    private static final long serialVersionUID = 1L;

    private Partner partner1;

    private Partner partner2;

    private RelationType relationType;

    @Override
    @Id
    @GeneratedValue(generator = "autoGen")
    @GenericGenerator(name = "autoGen", strategy = "native", parameters = { @Parameter(name = "sequence", value = "Relation_seq") })
    public Long getId() {
        return super.id;
    }

    /**
	 * Get the related partner instance 1.
	 * 
	 * @return
	 */
    @ManyToOne
    @ForeignKey(name = "Relation_Partner1_FK")
    @JoinColumn(name = "Partner1_ID")
    public Partner getPartner1() {
        return partner1;
    }

    /**
	 * Set the related partner instance 1
	 * 
	 * @param partner1
	 */
    public void setPartner1(Partner partner1) {
        this.partner1 = partner1;
    }

    /**
	 * Get the related partner instance 2.
	 * 
	 * @return
	 */
    @ManyToOne
    @ForeignKey(name = "Relation_Partner2_FK")
    @JoinColumn(name = "Partner2_ID")
    public Partner getPartner2() {
        return partner2;
    }

    /**
	 * Set the related partner instance 2
	 * 
	 * @param partner2
	 */
    public void setPartner2(Partner partner2) {
        this.partner2 = partner2;
    }

    /**
	 * Get the type or relation described by this instance.
	 * 
	 * @return
	 */
    @ManyToOne
    @ForeignKey(name = "Relation_RelationType_FK")
    @JoinColumn(name = "RelationType")
    public RelationType getRelationType() {
        return relationType;
    }

    /**
	 * Sets the type of relation betwwen the two partner instances.
	 * 
	 * @param type
	 */
    public void setRelationType(RelationType type) {
        this.relationType = type;
    }
}
