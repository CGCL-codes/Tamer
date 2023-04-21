package de.uni_leipzig.lots.server.persist.hibernate.type;

import de.uni_leipzig.lots.common.objects.Role;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;

/**
 * @author Alexander Kiel
 * @version $Id: RoleEnumType.java,v 1.5 2007/10/23 06:30:30 mai99bxd Exp $
 */
public class RoleEnumType extends EnumType<Role> {

    public static final Type TYPE = Hibernate.custom(RoleEnumType.class);

    public RoleEnumType() {
        super(Role.class);
    }
}
