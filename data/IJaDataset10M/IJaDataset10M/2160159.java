package pi.eclipse.cle.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import pi.eclipse.cle.util.AbstractWidget;
import pi.eclipse.cle.util.WidgetListener;

/**
 * @author <a href="mailto:pa314159&#64;sf.net">Paπ &lt;pa314159&#64;sf.net&gt;</a>
 */
abstract class AbstractWizardPage extends WizardPage implements WidgetListener {

    private final IContainer container;

    /**
	 * @param container
	 * @param pageName
	 */
    public AbstractWizardPage(IContainer container, String pageName) {
        super(pageName);
        this.container = container;
    }

    /**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
    public final void createControl(Composite parent) {
        final Composite widget = createWidget(this.container, parent);
        if (widget instanceof AbstractWidget) {
            final AbstractWidget aww = (AbstractWidget) widget;
            aww.addWidgetListener(this);
            aww.updateWidgetContainer();
        }
        setControl(widget);
    }

    /**
	 * @see pi.eclipse.cle.util.WidgetListener#updateContainer(java.lang.String)
	 */
    public void updateContainer(String status) {
        setErrorMessage(status);
        setPageComplete(status == null);
    }

    /**
	 * @param container
	 *            TODO
	 * @param parent
	 * @return
	 */
    protected abstract Composite createWidget(IContainer container, Composite parent);

    protected void finishWizard() {
    }
}
