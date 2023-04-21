package org.helianto.inventory;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * A special inventory movement, where a previous process agreement
 * is referenced.
 * 
 * <p>
 * A shipment is appropriate to work as an invoice item because it exposes
 * properties like price and taxes.
 * </p>
 * 
 * @author Mauricio Fernandes de Castro
 */
@javax.persistence.Entity
@DiscriminatorValue("S")
public class Shipment extends Movement implements Comparable<Shipment> {

    private static final long serialVersionUID = 1L;

    private ProcessAgreement processAgreement;

    private int sequence;

    private String info;

    /**
	 * Default constructor.
	 */
    public Shipment() {
        super();
    }

    /**
	 * Process agreement.
	 */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "processAgreementId")
    public ProcessAgreement getProcessAgreement() {
        return processAgreement;
    }

    public void setProcessAgreement(ProcessAgreement processAgreement) {
        this.processAgreement = processAgreement;
    }

    /**
	 * Process agreement.
	 */
    @Column(precision = 4)
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
	 * Additional information concerning the shipment.
	 */
    @Column(length = 512)
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    /**
	 * Implementation of Comparable interface.
	 */
    public int compareTo(Shipment other) {
        return this.getSequence() - other.getSequence();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Shipment) {
            return super.equals(other);
        }
        return false;
    }
}
