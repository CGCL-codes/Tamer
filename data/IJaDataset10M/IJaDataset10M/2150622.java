package net.sourceforge.hlm.gui.math.controllers;

import net.sourceforge.hlm.gui.components.*;
import net.sourceforge.hlm.gui.math.edit.*;
import net.sourceforge.hlm.gui.math.semantic.*;
import net.sourceforge.hlm.gui.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class DefinitionReferenceController extends SemanticController<DefinitionLayoutView> {

    public DefinitionReferenceController(EditFrame frame, EditControllerEnvironment environment, DefinitionLayoutView view) {
        super(frame, environment, view);
    }

    @Override
    public ColorCombination getHighlightColor() {
        return this.getColorManager().getLinkHighlightColor();
    }

    @Override
    public Cursor getCursor() {
        return this.getCursorManager().getLinkCursor();
    }

    @Override
    public ButtonEventReaction buttonPressed(int button) throws Exception {
        if (button == SWT.BUTTON1 || button == SWT.CR) {
            this.linkClicked(this.view.getObject());
            return ButtonEventReaction.ACTION_PERFORMED;
        } else {
            return super.buttonPressed(button);
        }
    }

    @Override
    public ObjectToolTip createToolTip(ObjectToolTip.ActivateListener activateListener) {
        return new MathObjectToolTip(this.getShell(), this.getLibraryManager(), this.view.getObject(), null, this.getDisplayManager(), activateListener, 200);
    }
}
