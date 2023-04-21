package portal.presentation.base;

/**
 * Base class for presentation objects in PIM. You should only extend
 * this base class iff your presentation object can throw {@link
 * PortalExcption}. The PortalException is only used in the PIM
 * application module and therefore it is not known by the Hambo
 * ApplicationFramework.  
 * 
 * */
public abstract class PIMProtectedPortalPage extends PIMPortalPage {

    /**
    * Construct a new PIMRedirectorBase page.
    * @param page_id the id of the presentation object.
    */
    protected PIMProtectedPortalPage(String page_id) {
        super(page_id, true);
    }
}
