package com.metanology.mde.ui.pimExplorer;

import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import com.metanology.mde.core.ui.plugin.MDEPlugin;
import com.metanology.mde.core.metaModel.MetaModelCache;

public class ComponentPropertiesTab {

    /**
     * Creates a new ComponentPropertiesTab.
     */
    public ComponentPropertiesTab(Composite parent) {
        this.parent = parent;
        init();
    }

    public void populateUIControls() {
        if (mcomp != null) {
            tagEntrys.setMetaObject(mcomp);
        }
        tagEntrys.populateUIControls();
        if (mcomp != null && MDEPlugin.getDefault().getRuntime().getModel() != null) {
            componentDependenciesTreeView.setMmodel(MDEPlugin.getDefault().getRuntime().getModel());
            componentDependenciesTreeView.setMcomp(mcomp);
        }
        componentDependenciesTreeView.populateUIControls();
        if (mcomp != null && MDEPlugin.getDefault().getRuntime().getModel() != null) {
            componentAssignedClassesTreeView.setMmodel(MDEPlugin.getDefault().getRuntime().getModel());
            componentAssignedClassesTreeView.setMcomp(mcomp);
        }
        componentAssignedClassesTreeView.populateUIControls();
        tagEntrys.setReadOnly(isReadOnly());
    }

    public void populateServerObjects() {
        tagEntrys.populateServerObjects();
        componentDependenciesTreeView.populateServerObjects();
        componentAssignedClassesTreeView.populateServerObjects();
    }

    private void init() {
        folder = new TabFolder(parent, SWT.NULL);
        folder.setLayoutData(new GridData(GridData.FILL_BOTH));
        Composite composite = null;
        TabItem item = null;
        composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new GridLayout());
        componentAssignedClassesTreeView = new ComponentAssignedClassesTreeView(composite);
        componentAssignedClassesTreeView.setLayout(new GridLayout());
        componentAssignedClassesTreeView.setLayoutData(new GridData(GridData.FILL_BOTH));
        item = new TabItem(folder, SWT.NULL);
        item.setText(MDEPlugin.getResourceString(ComponentAssignedClassesTreeView.MSG_TITLE));
        item.setControl(composite);
        composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new GridLayout());
        componentDependenciesTreeView = new ComponentDependenciesTreeView(composite);
        componentDependenciesTreeView.setLayout(new GridLayout());
        componentDependenciesTreeView.setLayoutData(new GridData(GridData.FILL_BOTH));
        item = new TabItem(folder, SWT.NULL);
        item.setText(MDEPlugin.getResourceString(ComponentDependenciesTreeView.MSG_TITLE));
        item.setControl(composite);
        composite = new Composite(folder, SWT.NONE);
        composite.setLayout(new GridLayout());
        tagEntrys = new TagEntryGrid(composite);
        item = new TabItem(folder, SWT.NULL);
        item.setText(MDEPlugin.getResourceString(TagEntryGrid.MSG_TITLE));
        item.setControl(composite);
    }

    public TagEntryGrid getTagEntrys() {
        return tagEntrys;
    }

    public ComponentDependenciesTreeView getComponentDependenciesTreeView() {
        return componentDependenciesTreeView;
    }

    public ComponentAssignedClassesTreeView getComponentAssignedClassesTreeView() {
        return componentAssignedClassesTreeView;
    }

    public com.metanology.mde.core.metaModel.Component getMcomp() {
        if (mcomp == null) {
            mcomp = new com.metanology.mde.core.metaModel.Component();
        }
        return mcomp;
    }

    public void setMcomp(com.metanology.mde.core.metaModel.Component val) {
        mcomp = val;
    }

    public boolean isReadOnly() {
        if (mcomp != null) {
            MetaModelCache cache = MDEPlugin.getDefault().getRuntime().getModelCache();
            if (cache != null) {
                return !cache.canEdit(mcomp);
            }
        }
        return false;
    }

    public void setReadOnly(boolean val) {
        isReadOnly = val;
    }

    private boolean isReadOnly = false;

    private Composite parent;

    private TabFolder folder;

    private TagEntryGrid tagEntrys;

    private ComponentDependenciesTreeView componentDependenciesTreeView;

    private ComponentAssignedClassesTreeView componentAssignedClassesTreeView;

    private com.metanology.mde.core.metaModel.Component mcomp;
}
