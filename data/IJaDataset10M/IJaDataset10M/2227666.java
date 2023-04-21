package fi.tuska.jalkametri.gui.command;

import fi.tuska.jalkametri.gui.IScreen;

public interface ItemSelectedAction {

    public void itemSelected(Object item, IScreen parent, Object[] params);
}
