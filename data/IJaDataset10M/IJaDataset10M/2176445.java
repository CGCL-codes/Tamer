package de.innot.avreclipse.ui.propertypages;

import java.util.List;
import org.eclipse.cdt.ui.newui.AbstractPage;
import org.eclipse.cdt.ui.newui.ICPropertyTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.dialogs.PropertyPage;
import de.innot.avreclipse.core.properties.AVRProjectProperties;
import de.innot.avreclipse.core.properties.ProjectPropertyManager;

/**
 * This is the parent for all AVR Project property pages.
 * <p>
 * This class extends CDT AbstractPage to participate in the build configuration
 * handling.
 * </p>
 * <p>
 * It acts as an interface to the {@link ProjectPropertyManager}, which manages
 * the list of all {@link AVRProjectProperties} of the current project. It also
 * maintains the current status of the "per Config" flag and informs all other
 * registered AVR Pages when the flag is changed via the
 * {@link AbstractAVRPage#setPerConfig(boolean)} method.
 * </p>
 * 
 * @see AbstractPage
 * 
 * @author Thomas Holland
 * @since 2.2
 * 
 */
public abstract class AbstractAVRPage extends AbstractPage {

    private static final String TEXT_COPYBUTTON = "Copy &Project Settings";

    /** The configuration selection group from the AbstractPage class */
    private Group fConfigGroup;

    /** The "Copy from Project" Button */
    private Button fCopyButton;

    /** The ProjectPropertyManager for the current project */
    protected ProjectPropertyManager fPropertiesManager = null;

    @Override
    protected void contentForCDT(Composite composite) {
        super.contentForCDT(composite);
        fConfigGroup = findFirstGroup(composite);
        loadPropertiesManager();
        internalSetPerConfig(fPropertiesManager.isPerConfig());
    }

    @Override
    protected void contributeButtons(Composite parent) {
        fCopyButton = new Button(parent, SWT.NONE);
        fCopyButton.setText(TEXT_COPYBUTTON);
        fCopyButton.setEnabled(isPerConfig());
        fCopyButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                performCopy();
            }
        });
        ((GridLayout) parent.getLayout()).numColumns++;
    }

    @Override
    public boolean performCancel() {
        AVRPropertyPageManager.performCancel(this);
        return super.performCancel();
    }

    @Override
    public boolean performOk() {
        AVRPropertyPageManager.performOK(this, getCfgsEditable());
        return super.performOk();
    }

    /**
	 * Notifies that the "Copy from Project" button has been pressed.
	 */
    public void performCopy() {
        AVRProjectProperties projectprops = fPropertiesManager.getProjectProperties();
        if (!noContentOnPage && displayedConfig) forEach(AbstractAVRPropertyTab.COPY, projectprops);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            loadPropertiesManager();
        }
        super.setVisible(visible);
    }

    /**
	 * Returns the value of the "per config" flag for this project.
	 * 
	 * @return <code>true</code> if each build configuration has its own
	 *         properties.
	 */
    protected boolean isPerConfig() {
        loadPropertiesManager();
        return fPropertiesManager.isPerConfig();
    }

    /**
	 * Set the project "per config" flag.
	 * <p>
	 * This method will set the flag and inform all its tabs and all other AVR
	 * Property pages registered with the page manager about the change.
	 * </p>
	 * 
	 * @param flag
	 *            <code>true</code> to enable "per config" settings.
	 */
    protected void setPerConfig(boolean flag) {
        if (flag == isPerConfig()) {
            return;
        }
        List<PropertyPage> allpages = AVRPropertyPageManager.getPages();
        for (PropertyPage page : allpages) {
            if ((page != null) && (page instanceof AbstractAVRPage)) {
                AbstractAVRPage ap = (AbstractAVRPage) page;
                ap.internalSetPerConfig(flag);
            }
        }
    }

    /**
	 * Set the "per config" flag for this page and inform all child tabs about
	 * the change.
	 * 
	 * @param flag
	 *            New value of the "per config" flag.
	 */
    private void internalSetPerConfig(boolean flag) {
        fPropertiesManager.setPerConfig(flag);
        if (fConfigGroup != null) {
            setEnabled(fConfigGroup, flag);
        }
        forEach(ICPropertyTab.UPDATE, getResDesc());
        if (fCopyButton != null) {
            fCopyButton.setEnabled(flag);
        }
    }

    /**
	 * get the Properties Manager from the page manager.
	 */
    private void loadPropertiesManager() {
        checkElement();
        fPropertiesManager = AVRPropertyPageManager.getPropertyManager(this, getProject());
    }

    /**
	 * Get the configuration selection group from the parent.
	 * <p>
	 * This is a hack to get a reference to the configuration selection group of
	 * a standard {@link AbstractPage}. The returned reference can be used to
	 * enable/disable the group as required.
	 * </p>
	 * 
	 * @param parent
	 *            a composite having the configuration selection as its first
	 *            group.
	 * @return A reference to the first group within the given composite
	 */
    private Group findFirstGroup(Composite parent) {
        Control[] children = parent.getChildren();
        if (children == null || children.length == 0) {
            return null;
        }
        for (Control child : children) {
            if (child instanceof Group) {
                return (Group) child;
            }
            if (child instanceof Composite) {
                Group recursive = findFirstGroup((Composite) child);
                if (recursive != null) {
                    return recursive;
                }
            }
        }
        return null;
    }

    /**
	 * Enable / Disable the given Composite.
	 * 
	 * @param compo
	 *            A <code>Composite</code> with some controls.
	 * @param value
	 *            <code>true</code> to enable, <code>false</code> to disable
	 *            the given group.
	 */
    private void setEnabled(Composite compo, boolean value) {
        Control[] children = compo.getChildren();
        for (Control child : children) {
            child.setEnabled(value);
        }
    }
}
