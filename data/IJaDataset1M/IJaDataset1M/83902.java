package fr.free.online.sun.frozen.model.dto.money;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import fr.free.online.sun.frozen.model.dto.abstractDTO.AbstractBuildable;
import fr.free.online.sun.frozen.model.dto.abstractDTO.AbstractExternationnalizableDTO;
import fr.free.online.sun.frozen.model.dto.buildable.building.IBuildingNeededObject;
import fr.free.online.sun.frozen.model.dto.buildable.defense.IDefenseNeededObject;
import fr.free.online.sun.frozen.model.dto.buildable.search.ISearchNeededObject;
import fr.free.online.sun.frozen.model.dto.buildable.shipPlan.component.IComponentNeededObject;
import fr.free.online.sun.frozen.model.dto.buildable.shipPlan.hull.IHullNeededObject;
import fr.free.online.sun.frozen.model.dto.message.raidRepport.IRaidRepportNeededObject;
import fr.free.online.sun.frozen.model.dto.message.spyingRepport.ISpyingRepportNeededObject;
import fr.free.online.sun.frozen.model.dto.raid.IRaidNeededObject;
import fr.free.online.sun.frozen.model.dto.univers.planet.IPlanetNeededObject;

/**
 * Money types available.
 * @author $Author: JBGIRAUD $
 * @version $Rev: 47 $
 * @since $Date: 2010-03-12 18:22:14 +0100 (ven., 12 mars 2010) $
 * @see AbstractBuildable
 * @see AbstractExternationnalizableDTO
 */
@Entity
@Table(name = "FS_MONEY")
public class Money extends AbstractExternationnalizableDTO implements IBuildingNeededObject, IDefenseNeededObject, ISearchNeededObject, IHullNeededObject, IRaidRepportNeededObject, ISpyingRepportNeededObject, IPlanetNeededObject, IRaidNeededObject, Serializable, IComponentNeededObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4050376125859259966L;

    /**
	 * Code used to identity internationalization Strings.
	 */
    @Column(name = "CODE", unique = true, nullable = false)
    protected String code;

    /**
	 * ID of the Money.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false)
    protected Integer id;

    /**
	 * Return true if a Money has the same ID of another.
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Money) {
            Money _obj = (Money) obj;
            boolean result = _obj.getId().equals(this.getId());
            return result;
        }
        return super.equals(obj);
    }
}
