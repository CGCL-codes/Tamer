package org.nightlabs.jfire.base.ui.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.nightlabs.base.ui.composite.FadeableComposite;
import org.nightlabs.base.ui.job.Job;
import org.nightlabs.jfire.base.JFireBaseEAR;
import org.nightlabs.jfire.base.ui.editlock.EditLockCallback;
import org.nightlabs.jfire.base.ui.editlock.EditLockMan;
import org.nightlabs.jfire.base.ui.resource.Messages;
import org.nightlabs.jfire.config.ConfigModule;
import org.nightlabs.jfire.config.id.ConfigID;
import org.nightlabs.progress.ProgressMonitor;
import org.nightlabs.progress.SubProgressMonitor;

/**
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 *
 */
public class ConfigPreferencesEditComposite2 extends FadeableComposite implements ConfigPreferenceChangedListener, IStoreChangedConfigModule {

    private List<IConfigModuleChangedListener> cfModChangedListeners = new LinkedList<IConfigModuleChangedListener>();

    protected ConfigPreferencesTreeComposite treeComposite;

    protected ConfigPreferencesComposite preferencesComposite;

    private SashForm wrapper;

    protected ConfigModule currentConfigModule;

    protected String currentcfModID;

    protected ConfigID currentConfigID;

    protected AbstractConfigModulePreferencePage currentPage;

    protected Map<String, ConfigModule> involvedConfigModules = new HashMap<String, ConfigModule>();

    /**
	 * (ConfigPreferenceNode, AbstractConfigModulePreferencePage)
	 */
    protected Map<ConfigPreferenceNode, AbstractConfigModulePreferencePage> involvedPages = new HashMap<ConfigPreferenceNode, AbstractConfigModulePreferencePage>();

    protected Set<ConfigModule> dirtyConfigModules = new HashSet<ConfigModule>();

    /**
	 * Set of ConfigModules that have been updated on the server side and for which the update
	 * is not yet reflected by the gui.
	 */
    private Set<ConfigModule> changedModules;

    /**
	 * @param parent
	 * @param style
	 * @param setLayoutData
	 */
    public ConfigPreferencesEditComposite2(Composite parent, int style, boolean setLayoutData) {
        super(parent, style, LayoutMode.TIGHT_WRAPPER);
        this.setLayoutData(new GridData(GridData.FILL_BOTH));
        wrapper = new SashForm(this, SWT.HORIZONTAL);
        wrapper.setLayoutData(new GridData(GridData.FILL_BOTH));
        treeComposite = new ConfigPreferencesTreeComposite(wrapper, SWT.BORDER, false, null);
        GridData treeGD = new GridData(GridData.FILL_BOTH);
        treeComposite.setLayoutData(treeGD);
        treeComposite.getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                ConfigPreferenceNode selection = treeComposite.getFirstSelectedElement();
                if (selection == null || selection.getElement() == null) {
                    currentPage = null;
                    updatePreferencesComposite();
                    return;
                }
                try {
                    if (!involvedPages.containsKey(selection)) {
                        currentPage = selection.createPreferencePage();
                        currentcfModID = selection.getConfigModuleCfModID();
                        currentPage.getConfigModuleController().setConfigID(currentConfigID, true, currentcfModID);
                        currentPage.createPartContents(preferencesComposite.getWrapper());
                        currentPage.addConfigPreferenceChangedListener(ConfigPreferencesEditComposite2.this);
                        involvedPages.put(selection, currentPage);
                    } else {
                        currentPage = involvedPages.get(selection);
                    }
                } catch (CoreException e) {
                    throw new RuntimeException("Couldn't create an AbstractPreferencePage: ", e);
                }
                updateCurrentConfigModule();
                updatePreferencesComposite();
            }
        });
        preferencesComposite = new ConfigPreferencesComposite(wrapper, SWT.NONE, true);
        wrapper.setWeights(new int[] { 3, 7 });
    }

    private void updatePreferencesGUI() {
        AbstractConfigModulePreferencePage selectedPage = getCurrentPage();
        if (selectedPage == null) {
            preferencesComposite.setNoEditGUI();
            return;
        }
        preferencesComposite.getStackLayout().topControl = selectedPage.getControl();
        preferencesComposite.getWrapper().layout();
    }

    private void updateCurrentConfigModule() {
        if (currentConfigModule != null && currentPage != null) {
            if (currentConfigModule.getClass().equals(currentPage.getConfigModuleController().getClass())) {
                currentPage.updateConfigModule();
            }
        }
    }

    public void updatePreferencesComposite() {
        Job job = new Job(Messages.getString("org.nightlabs.jfire.base.ui.config.ConfigPreferencesEditComposite2.updateingJob")) {

            @Override
            protected IStatus run(ProgressMonitor monitor) {
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        updatePreferencesGUI();
                    }
                });
                return Status.OK_STATUS;
            }
        };
        job.schedule();
    }

    protected ConfigModule getCurrentConfigModule() {
        return currentConfigModule;
    }

    public ConfigID getCurrentConfigID() {
        return currentConfigID;
    }

    /**
	 * This method must be called on the UI thread.
	 *
	 * @param currentConfigID The new current configID.
	 */
    public void setCurrentConfigID(final ConfigID currentConfigID) {
        this.currentConfigID = currentConfigID;
        setFaded(true);
        Job lockJob = new Job(Messages.getString("org.nightlabs.jfire.base.ui.config.ConfigPreferencesEditComposite2.lockCheckJob")) {

            @Override
            protected IStatus run(ProgressMonitor monitor) {
                monitor.beginTask(Messages.getString("org.nightlabs.jfire.base.ui.config.ConfigPreferencesEditComposite2.initialising"), 2);
                treeComposite.setConfigID(currentConfigID, monitor);
                monitor.worked(1);
                EditLockMan.sharedInstance().acquireEditLock(JFireBaseEAR.EDIT_LOCK_TYPE_ID_CONFIG, getCurrentConfigID(), Messages.getString("org.nightlabs.jfire.base.ui.config.ConfigPreferencesEditComposite2.editLockWarning"), (EditLockCallback) null, new SubProgressMonitor(monitor, 1));
                Display.getDefault().asyncExec(new Runnable() {

                    public void run() {
                        if (isDisposed()) return;
                        setFaded(false);
                    }
                });
                return Status.OK_STATUS;
            }
        };
        lockJob.setPriority(org.eclipse.core.runtime.jobs.Job.SHORT);
        lockJob.schedule();
        wrapper.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                EditLockMan.sharedInstance().releaseEditLock(currentConfigID);
            }
        });
    }

    protected AbstractConfigModulePreferencePage getCurrentPage() {
        return currentPage;
    }

    private String getCfModKey(AbstractConfigModulePreferencePage page) {
        return ConfigModule.getCfModKey(page.getConfigModuleController().getConfigModuleClass(), page.getConfigModuleController().getConfigModuleID());
    }

    public void configPreferenceChanged(AbstractConfigModulePreferencePage preferencePage) {
        String cfModKey = getCfModKey(preferencePage);
        ConfigModule cfMod = involvedConfigModules.get(cfModKey);
        if (cfMod != null) dirtyConfigModules.add(cfMod);
        notifyChangedListeners(cfMod);
    }

    public void addConfigModuleChangedListener(IConfigModuleChangedListener listener) {
        cfModChangedListeners.add(listener);
    }

    public void removeConfigModuleChangedListener(IConfigModuleChangedListener listener) {
        cfModChangedListeners.remove(listener);
    }

    protected void notifyChangedListeners(ConfigModule configModule) {
        for (IConfigModuleChangedListener listener : cfModChangedListeners) {
            listener.configModuleChanged(configModule);
        }
    }

    public void clear() {
        involvedConfigModules.clear();
        dirtyConfigModules.clear();
        currentConfigModule = null;
    }

    public Set<ConfigModule> getDirtyConfigModules() {
        updateCurrentConfigModule();
        return new HashSet<ConfigModule>(dirtyConfigModules);
    }

    public Set<AbstractConfigModulePreferencePage> getInvolvedPages() {
        return new HashSet<AbstractConfigModulePreferencePage>(involvedPages.values());
    }

    /**
	 * Stored the changed ConfigModule to ask the user later about whether to reload these modules or not.
	 * @see org.nightlabs.jfire.base.ui.config.IStoreChangedConfigModule#addChangedConfigModule(org.nightlabs.jfire.config.ConfigModule)
	 */
    public void addChangedConfigModule(ConfigModule module) {
        if (changedModules == null) changedModules = new HashSet<ConfigModule>();
        changedModules.add(module);
    }
}
