package net.sf.springlayout.web.layout;

import javax.servlet.http.HttpServletRequest;

/**
 * Defines a single panel on a muli-panel page.  
 * @author Rob Monie
 * 
*/
public interface PanelForm extends Form {

    /**
    * Sets the index of the panel to be used for ordering in the panel group list
    * 
    * @param index
    */
    public void setIndex(int index);

    /**
    * Gets the index of the panel to be used for ordering in the panel group list
    * @return the index of the panel
    */
    public int getIndex();

    /**
     * Sets the panelTabStateManager for the LayoutPanelForm
     * @param panelTabStateManager that is injected
     */
    public void setPanelFormTabStateManager(PanelFormTabStateManager panelFormTabStateManager);

    /**
    * Check if panel is disabled.  If no panelTabStateManager is set then return true
    * 
    * @param request the current HttpServletRequest
    * @return true/false boolean based on panel being enabled
    */
    public boolean isDisabled(HttpServletRequest request);

    /**
    * Retrieves the redirectFormViewName for this LayoutPanelForm.
    * @return Returns the redirectFormViewName for this LayoutPanelForm.
    */
    public String getRedirectFormViewName();

    /**
    *Sets the redirectFormViewName for this LayoutPanelForm.
    * @param redirectFormViewName Sets the redirectFormViewName for this LayoutPanelForm.
    */
    public void setRedirectFormViewName(String redirectFormViewName);

    /**
    * Retrieves the panelLabel for this LayoutPanelForm.
    * @return Returns the panelLabel for this LayoutPanelForm.
    */
    public String getPanelLabel();

    /**
    * Sets the panelLabel for this LayoutPanelForm.
    * @param panelLabel Sets the panelLabel for this LayoutPanelForm.
    */
    public void setPanelLabel(String panelLabel);
}
