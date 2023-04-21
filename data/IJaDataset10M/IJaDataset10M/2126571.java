package org.plazmaforge.studio.reportdesigner.dialogs;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

/** 
 * @author Oleh Hapon
 * $Id: AbstractParentFolderProvider.java,v 1.8 2010/11/16 11:17:07 ohapon Exp $
 */
public abstract class AbstractParentFolderProvider extends AbstractFolderProvider {

    private List<IFolderProvider> folderProviders = new ArrayList<IFolderProvider>();

    public Composite createPanel(Composite parent) {
        Composite panel = super.createPanel(parent);
        GridLayout gridLayout = new GridLayout();
        panel.setLayout(gridLayout);
        createTabFolder(panel);
        return panel;
    }

    protected TabFolder createTabFolder(Composite panel) {
        TabFolder tabFolder = new TabFolder(panel, SWT.NULL);
        initLayoutData(tabFolder);
        createItems(tabFolder);
        return tabFolder;
    }

    protected void initLayoutData(TabFolder tabFolder) {
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    protected abstract void createItems(TabFolder tabFolder);

    /**
     * Add IFolderProvider to provider storage
     */
    protected void addFolderProvider(IFolderProvider provider) {
        provider.setDesignElement(getDesignElement());
        provider.setReportEditor(getReportEditor());
        provider.setForceChange(isForceChange());
        if (isSingleElement()) {
            provider.setSingleElement(true);
            provider.setReport(getReport());
        }
        folderProviders.add(provider);
    }

    protected List<IFolderProvider> getFolderProviders() {
        return folderProviders;
    }

    /**
     * Load data in providers
     *
     */
    public void loadData() {
        List<IFolderProvider> providers = getFolderProviders();
        for (IFolderProvider provider : providers) {
            provider.loadData();
        }
    }

    /**
     * Store data in providers 
     *
     */
    public void storeData() {
        List<IFolderProvider> providers = getFolderProviders();
        for (IFolderProvider provider : providers) {
            provider.storeData();
        }
    }

    public boolean validate() {
        if (!super.validate()) {
            return false;
        }
        List<IFolderProvider> providers = getFolderProviders();
        for (IFolderProvider provider : providers) {
            if (!provider.validate()) {
                return false;
            }
        }
        return true;
    }
}
