package net.sourceforge.wildlife.log.problemsview.actions;

import net.sourceforge.wildlife.log.problemsview.MarkerContentProvider;
import org.eclipse.swt.widgets.Event;

/**
 * 
 */
public class FilterWarnAction extends AbstractFilterAction {

    /**
	 * @param contentProvider_p
	 */
    public FilterWarnAction(MarkerContentProvider contentProvider_p) {
        super(contentProvider_p);
    }

    @Override
    protected String getIconPath() {
        return Messages.getString("FilterWarnAction.0");
    }

    /**
	 * @see org.eclipse.jface.action.IAction#getAccelerator()
	 */
    public int getAccelerator() {
        return 0;
    }

    /**
	 * @see org.eclipse.jface.action.IAction#getActionDefinitionId()
	 */
    public String getActionDefinitionId() {
        return null;
    }

    /**
	 * @see org.eclipse.jface.action.IAction#getDescription()
	 */
    public String getDescription() {
        return Messages.getString("FilterWarnAction.1");
    }

    /**
	 * @see org.eclipse.jface.action.IAction#runWithEvent(org.eclipse.swt.widgets.Event)
	 */
    public void runWithEvent(Event event) {
        getProvider().setFilter(MarkerContentProvider.DISPLAY_WARNING, isChecked());
    }

    /**
	 * @see org.eclipse.jface.action.IAction#getId()
	 */
    public String getId() {
        return Messages.getString("FilterWarnAction.2");
    }

    /**
	 * @see org.eclipse.jface.action.IAction#getText()
	 */
    public String getText() {
        return Messages.getString("FilterWarnAction.3");
    }

    /**
	 * @see org.eclipse.jface.action.IAction#getToolTipText()
	 */
    public String getToolTipText() {
        return Messages.getString("FilterWarnAction.4");
    }

    /**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
    public void run() {
    }
}
