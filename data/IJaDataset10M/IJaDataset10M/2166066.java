package net.sf.edevtools.lib.baselib.ui.actions.commonactions;

import org.eclipse.jface.action.IAction;

/**
 * @author Christoph Graupner
 * 
 */
public interface ICommonSelectAction<CompType, ActionType> extends ICommonAction<CompType, ActionType> {

    public static final String SELECTED = IAction.CHECKED;

    public boolean isSelected();

    public void setSelected(boolean aValue);

    public boolean toggleSelected();
}
