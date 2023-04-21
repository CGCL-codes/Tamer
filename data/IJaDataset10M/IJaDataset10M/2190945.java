package net.sourceforge.hobbes.client;

import net.sourceforge.hobbes.common.ControllableModel;
import net.sourceforge.hobbes.common.HobbesController;

/**
 * Updates a <code>ClientModel</code> when it receives input from an external source, such as the network or user.
 *
 * @author Daniel M. Hackney
 * @created Sep 18, 2005
 *
 */
public interface ClientController extends HobbesController {

    /**
     * Sets the <code>ClientModel</code> to be controlled by this controller.
     * @param inModel The model to control.
     */
    public void setModel(ClientModel inModel);

    /**
     * Returns the <code>ClientModel</code> being controlled.
     * @return This controller's <code>ClientModel</code>.
     */
    public ControllableModel getModel();

    /**
     * Checks whether this controller is controlling a server or client model.
     * @return HobbesController.CLIENT since this will always be used for clients.
     */
    public String getType();
}
