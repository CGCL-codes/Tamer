package org.jxul.xpcom;

import org.jxul.dom.IDOMXULCommandDispatcher;

/**
 * @author E20199
 *
 */
public interface IControllers {

    IDOMXULCommandDispatcher getCommandDispatcher();

    void setCommandDispatcher(IDOMXULCommandDispatcher commandDispatcher);

    void appendController(IController controller);

    IController getControllerAt(int index);

    IController getControllerById(int controllerID);

    int getControllerCount();

    IController getControllerForCommand(String command);

    int getControllerId(IController controller);

    void insertControllerAt(int index, IController controller);

    void removeController(IController controller);

    IController removeControllerAt(int index);
}
