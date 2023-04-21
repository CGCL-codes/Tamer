package fr.free.online.sun.frozen.model.dto.message.spyingRepport;

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
import fr.free.online.sun.frozen.model.dto.buildable.building.Building;

/**
 * {@link AbstractNumberedAssociation} between a {@link SpyingRepport} and a {@link Building}.
 * @author $Author: JBGIRAUD $
 * @version $Rev: 43 $
 * @since $Date: 2010-03-12 10:14:24 +0100 (ven., 12 mars 2010) $
 * @see AbstractDTO
 * @see ISpyingRepportNeededObject
 * @see AbstractSpyingRepportNumberedAssociation
 * @see AbstractNumberedAssociation
 */
@Entity
@Table(name = "FS_SPYING_REPPORT_BUILDING", uniqueConstraints = @UniqueConstraint(columnNames = { "SPYING_REPPORT_ID", "BUILDING_ID" }))
public class SpyingRepportBuilding extends AbstractSpyingRepportNumberedAssociation<Building> implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5099391104426402625L;

    /**
	 * {@link AbstractAssociationDTOId} identifying the association.
	 */
    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "mainObjectId", column = @Column(name = "SPYING_REPPORT_ID", nullable = false)), @AttributeOverride(name = "neededObjectId", column = @Column(name = "BUILDING_ID", nullable = false)) })
    protected AbstractAssociationDTOId id;

    /**
	 * Master of the association.
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPYING_REPPORT_ID", nullable = false)
    protected SpyingRepport mainObject;

    /**
	 * Slave of the association.
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUILDING_ID", nullable = false)
    protected Building neededObject;

    /**
	 * Number of slaves for a master.
	 */
    @Column(name = "BUILDING_LEVEL", nullable = false)
    protected Integer number;
}
