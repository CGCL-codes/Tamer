package org.openscience.cdk.nonotify;

import org.openscience.cdk.interfaces.IChemObjectListener;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.protein.data.PDBStructure;

/**
 * @cdk.module  nonotify
 * @cdk.githash
 */
public class NNPDBStructure extends PDBStructure {

    private static final long serialVersionUID = -7423565527556262186L;

    public NNPDBStructure() {
        super();
        setNotification(false);
    }

    public IChemObjectBuilder getBuilder() {
        return NoNotificationChemObjectBuilder.getInstance();
    }

    public void addListener(IChemObjectListener col) {
    }
}
