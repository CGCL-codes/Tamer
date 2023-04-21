package fr.free.online.sun.frozen.model.dto.univers.planet;

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
 * {@link AbstractNumberedAssociation} between a {@link Building} and a {@link Planet}.
 * @author $Author: JBGIRAUD $
 * @version $Rev: 43 $
 * @since $Date: 2010-03-12 10:14:24 +0100 (ven., 12 mars 2010) $
 * @see AbstractDTO
 * @see IPlanetNeededObject
 * @see AbstractPlanetNumberedAssociation
 * @see AbstractNumberedAssociation
 */
@Entity
@Table(name = "FS_PLANET_BUILDING", uniqueConstraints = @UniqueConstraint(columnNames = { "PLANET_ID", "BUILDING_ID" }))
public class PlanetBuilding extends AbstractPlanetNumberedAssociation<Building> implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -654936716292586894L;

    /**
	 * {@link AbstractAssociationDTOId} identifying the association.
	 */
    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "mainObjectId", column = @Column(name = "PLANET_ID", nullable = false)), @AttributeOverride(name = "neededObjectId", column = @Column(name = "BUILDING_ID", nullable = false)) })
    protected AbstractAssociationDTOId id;

    /**
	 * Master of the association.
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLANET_ID", nullable = false)
    protected Planet mainObject;

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
