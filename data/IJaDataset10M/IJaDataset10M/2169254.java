package org.omg.PortableInterceptor.ORBInitInfoPackage;

import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.IDLEntity;
import java.io.Serializable;

/**
 * This exception is raised by methods in
 * {@link org.omg.PortableInterceptor.ORBInitInfoOperations} on the attempt to
 * register or resolve an invalid name like empty string. The already
 * registered names (including the default names, defined by OMG) are also
 * invalid for registration.
 *
 * @author Audrius Meskauskas, Lithiania (AudriusA@Bioinformatics.org)
 */
public class InvalidName extends UserException implements IDLEntity, Serializable {

    /**
   * Use serialVersionUID (v1.4) for interoperability.
   */
    private static final long serialVersionUID = -4599417794753377115L;

    /**
   * Create InvalidName with no explaining message.
   */
    public InvalidName() {
    }

    /**
   * Create the InvalidName with explaining message.
   *
   * @param why a string, explaining, why the name is invalid.
   */
    public InvalidName(String why) {
        super(why);
    }
}
