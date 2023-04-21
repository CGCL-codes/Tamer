package net.sourceforge.mpango.entity.technology;

import javax.persistence.Entity;
import net.sourceforge.mpango.entity.Shield;
import net.sourceforge.mpango.entity.Technology;

@Entity
public abstract class ShieldTechnology extends Technology {

    public abstract Shield createShield();
}
