package net.sourceforge.rafc.spi.inbound;

import java.io.Serializable;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.InvalidPropertyException;
import net.sourceforge.rafc.spi.AbstractResourceAdapterAssociation;

/**
 * Abstract implementation of an activation spec.
 * 
 * @author Markus KARG (markus-karg@users.sourceforge.net)
 */
public class AbstractActivationSpec extends AbstractResourceAdapterAssociation implements ActivationSpec, Serializable {

    /**
     * Creates an empty instance of this bean as wanted by the JCA
     * specification. To prevent descendents to hide the default constructor,
     * this empty implementation is provided.
     */
    public AbstractActivationSpec() {
    }

    /**
     * Actually does nothing yet.
     */
    public void validate() throws InvalidPropertyException {
    }
}
