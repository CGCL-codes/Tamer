package fr.free.online.sun.frozen.model.dto.player;

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
import fr.free.online.sun.frozen.model.dto.abstractDTO.AbstractAssociation;
import fr.free.online.sun.frozen.model.dto.abstractDTO.AbstractAssociationDTOId;
import fr.free.online.sun.frozen.model.dto.abstractDTO.AbstractDTO;
import fr.free.online.sun.frozen.model.dto.quest.Quest;

/**
 * {@link AbstractAssociation} between a {@link Player} a {@link Quest}.
 * @author $Author: JBGIRAUD $
 * @version $Rev: 43 $
 * @since $Date: 2010-03-12 10:14:24 +0100 (ven., 12 mars 2010) $
 * @see AbstractDTO
 * @see IPlayerNeededObject
 * @see AbstractPlayerAssociation
 * @see AbstractAssociation
 */
@Entity
@Table(name = "FS_PLAYER_QUEST", uniqueConstraints = @UniqueConstraint(columnNames = { "PLAYER_ID", "QUEST_ID" }))
public class PlayerQuest extends AbstractPlayerAssociation<Quest> implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5962656469202877054L;

    /**
	 * {@link AbstractAssociationDTOId} identifying the association.
	 */
    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "mainObjectId", column = @Column(name = "MAIN_OBJECT_ID", nullable = false)), @AttributeOverride(name = "neededObjectId", column = @Column(name = "NEEDED_OBJECT_ID", nullable = false)) })
    protected AbstractAssociationDTOId id;

    /**
	 * Master of the association.
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    protected Player mainObject;

    /**
	 * Slave of the association.
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUEST_ID", nullable = false)
    protected Quest neededObject;
}
