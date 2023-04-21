package net.sf.cdktools.model;

import net.sf.octet.model.Molecule;

/**
 * A two-way adapter that serves as a Molecule in both Octet and the CDK. To create an instance
 * of this class, use an implementation of <code>CDKAdapterBuilder</code> or
 * <code>OctetAdapterBuilder</code> such as
 * <code>BasicCDKAdapterBuilder</code> or <code>BasicOctetAdapterBuilder</code>.
 * 
 * @author Richard Apodaca
 */
public abstract class Adapter extends org.openscience.cdk.Molecule implements Molecule {

    /**
   * Default constructor.
   */
    protected Adapter() {
        super();
    }
}
