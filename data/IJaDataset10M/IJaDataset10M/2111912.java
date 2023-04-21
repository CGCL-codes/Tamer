package uk.co.ordnancesurvey.confluence.ui.basemvc;

import uk.ac.leeds.comp.ui.base.UIComponent;
import uk.ac.leeds.comp.ui.base.UIModel;
import uk.ac.leeds.comp.ui.base.impl.UIControllerImpl;
import uk.co.ordnancesurvey.confluence.IRooEditorKit;
import uk.co.ordnancesurvey.confluence.RooContext;

/**
 * Base class for all GUI component controllers in Roo
 * 
 * @author rdenaux
 * 
 * @param <ModelType>
 * @param <ViewType>
 */
public abstract class RooUIController<ModelType extends UIModel, ViewType extends UIComponent> extends UIControllerImpl<ModelType, ViewType> {

    private static final long serialVersionUID = 1128407868583133399L;

    private RooContext rooContext;

    public RooUIController(IRooEditorKit aEditorKit) {
        rooContext = new RooContext(aEditorKit);
    }

    protected final RooContext getRooContext() {
        return rooContext;
    }

    protected final IRooEditorKit getEditorKit() {
        return getRooContext().getEditorKit();
    }
}
