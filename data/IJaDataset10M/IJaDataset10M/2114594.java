package fr.free.online.sun.frozen.model.dto.buildable.shipPlan.hull;

import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import fr.free.online.sun.frozen.model.dto.abstractDTO.AbstractAssociationDTOId;
import fr.free.online.sun.frozen.model.dto.abstractDTO.AbstractDTO;
import fr.free.online.sun.frozen.model.dto.abstractDTO.AbstractNumberedAssociation;
import fr.free.online.sun.frozen.model.dto.buildable.characteristic.Characteristic;

/**
 * {@link AbstractNumberedAssociation} between a {@link Hull} and a {@link Characteristic}.
 * @author $Author: JBGIRAUD $
 * @version $Rev: 43 $
 * @since $Date: 2010-03-12 10:14:24 +0100 (ven., 12 mars 2010) $
 * @see AbstractDTO
 * @see IHullNeededObject
 * @see AbstractHullNumberedAssociation
 * @see AbstractNumberedAssociation
 */
@Entity
@Table(name = "FS_HULL_CHARACTERISTIC", uniqueConstraints = @UniqueConstraint(columnNames = { "CHARACTERISTIC_ID", "HULL_ID" }))
public class HullCharacteristic extends AbstractHullNumberedAssociation<Characteristic> implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2017416353481025768L;

    /**
	 * {@link AbstractAssociationDTOId} identifying the association.
	 */
    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "mainObjectId", column = @Column(name = "HULL_ID", nullable = false)), @AttributeOverride(name = "neededObjectId", column = @Column(name = "CHARACTERISTIC_ID", nullable = false)) })
    protected AbstractAssociationDTOId id;

    /**
	 * Master of the association.
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HULL_ID", nullable = false)
    protected Hull mainObject;

    /**
	 * Slave of the association.
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CHARACTERISTIC_ID", nullable = false)
    protected Characteristic neededObject;

    /**
	 * Number of slaves for a master.
	 */
    @Column(name = "CHARACTERISTIC_LEVEL", nullable = false)
    protected Integer number;
}
