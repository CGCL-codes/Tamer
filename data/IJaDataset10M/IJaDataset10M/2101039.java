package com.javapathfinder.vjp.config.editors.defaultproperties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import com.javapathfinder.vjp.config.editors.ModePropertyConfiguration;

/**
 * The tab that displays the properties loaded from jpf.properties and 
 * default.properties
 * @author Sandro Badame
 */
public class DefaultPropertiesTab extends Composite {

    private DefaultPropertiesViewer defaultViewer;

    private ModePropertyConfiguration properties;

    /**
   * Constructs this Tab
   * @param parent
   * @param properties
   */
    public DefaultPropertiesTab(Composite parent, ModePropertyConfiguration properties) {
        super(parent, SWT.NULL);
        this.properties = properties;
        createContent(parent);
    }

    /**
   * (non-Javadoc)
   * 
   * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
   */
    public void createContent(Composite parent) {
        setLayout(new FormLayout());
        setFont(parent.getFont());
        Composite propertyUI = createPropertyUI(this);
        FormData layoutData = new FormData();
        layoutData.left = new FormAttachment(0, 5);
        layoutData.right = new FormAttachment(100, -5);
        layoutData.top = new FormAttachment(0, 10);
        layoutData.bottom = new FormAttachment(100, -10);
        propertyUI.setLayoutData(layoutData);
    }

    private Composite createPropertyUI(Composite parent) {
        Composite propertyUI = new Composite(parent, SWT.NULL);
        propertyUI.setLayout(new FormLayout());
        Composite viewerParent = new Composite(propertyUI, SWT.NULL);
        FormData viewerLayout = new FormData();
        viewerLayout.top = new FormAttachment(0, 10);
        viewerLayout.right = new FormAttachment(100, -10);
        viewerLayout.left = new FormAttachment(0, 10);
        viewerLayout.bottom = new FormAttachment(100, -10);
        viewerParent.setLayoutData(viewerLayout);
        viewerParent.setLayout(new FillLayout());
        defaultViewer = new DefaultPropertiesViewer(viewerParent, properties);
        defaultViewer.setInput(properties);
        return propertyUI;
    }

    /**
   * returns the name of this tab
   * (non-Javadoc)
   * 
   * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
   * @return the name of the tab to be displayed.
   */
    public String getTabName() {
        return "Default Properties";
    }

    /**
   * Refreshes the table viewer to be updated.
   */
    public void refresh() {
        defaultViewer.refresh();
    }
}
