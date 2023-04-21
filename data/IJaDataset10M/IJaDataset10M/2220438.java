package org.itver.arm.controls.controllers.actions;

import org.itver.arm.models.elements.Element;
import org.openide.util.NbBundle;

/**
 * Inserta un nuevo Joint en el Elemento seleccionado en el Navigator. el
 * Elemento puede ser un Arm o Joint.
 * @author pablo
 */
public final class InsertJointAction extends InsertPiece {

    public InsertJointAction(Element element) {
        super(element, true);
    }

    @Override
    protected void setName() {
        this.putValue(NAME, NbBundle.getMessage(InsertJointAction.class, "CTL_InsertJointAction"));
    }
}
