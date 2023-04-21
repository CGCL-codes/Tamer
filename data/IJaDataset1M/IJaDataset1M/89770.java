package org.radrails.rails.internal.ui.railsplugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.Page;
import org.radrails.rails.core.railsplugins.RailsPluginDescriptor;
import org.radrails.rails.core.railsplugins.RailsPluginsManager;
import org.radrails.rails.internal.core.RailsPlugin;
import org.radrails.rails.internal.ui.RailsUILog;
import org.radrails.rails.internal.ui.RailsUIPlugin;
import org.rubypeople.rdt.core.util.Util;

public class RailsPluginsPage extends Page implements ISelectionChangedListener {

    private ListViewer fPluginsList;

    private Button fSvnextButton;

    private Button fSvnCheckoutButton;

    private Button fInstallButton;

    private Button fRemoveButton;

    private Button fGoButton;

    private Browser browser;

    private SashForm fPluginsView;

    private Color background;

    @Override
    public void createControl(Composite parent) {
        fPluginsView = new SashForm(parent, SWT.NULL);
        fPluginsView.setLayout(new GridLayout(2, false));
        fPluginsView.setLayoutData(new GridData(GridData.FILL_BOTH));
        refreshPlugins();
        createListViewer(fPluginsView);
        createListControls(fPluginsView);
        fPluginsView.setWeights(new int[] { 20, 80 });
    }

    protected void createListViewer(Composite parent) {
        fPluginsList = new ListViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        fPluginsList.setContentProvider(new ArrayContentProvider());
        fPluginsList.setLabelProvider(new RailsPluginsLabelProvider());
        GridData pluginsListData = new GridData(GridData.FILL_VERTICAL);
        pluginsListData.widthHint = 125;
        fPluginsList.getList().setLayoutData(pluginsListData);
        fPluginsList.addSelectionChangedListener(this);
    }

    protected void createListControls(Composite parent) {
        Composite ctrlComp = new Composite(parent, SWT.NULL);
        background = new Color(Display.getCurrent(), 27, 27, 27);
        ctrlComp.setBackground(background);
        ctrlComp.setBackgroundMode(SWT.INHERIT_FORCE);
        GridLayout layout = new GridLayout(1, true);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        ctrlComp.setLayout(layout);
        ctrlComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        try {
            browser = new Browser(ctrlComp, SWT.NONE);
            browser.setLayoutData(new GridData(GridData.FILL_BOTH));
            browser.setText("<html><body style=\"background-color: #1b1b1b;\"><h1 style=\"color: white; font: bold 136% verdana\">Please select a plugin from the left-hand list.</h1></body></html>");
        } catch (Exception e) {
            MessageDialog.openError(Display.getDefault().getActiveShell(), "Unable to create embedded browser", "It appears that you do not have an embeddable browser. Please see http://www.eclipse.org/swt/faq.php#browserlinux for more information if you are on Linux.");
        }
        Composite optsComp = new Composite(ctrlComp, SWT.NULL);
        layout = new GridLayout(4, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        optsComp.setLayout(layout);
        optsComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        optsComp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        Label blank = new Label(optsComp, SWT.NULL);
        blank.setLayoutData(new GridData(GridData.FILL_BOTH));
        blank.setBackground(background);
        Group optsGroup = new Group(optsComp, SWT.NULL);
        optsGroup.setText("Options");
        optsGroup.setLayout(new GridLayout(2, false));
        fSvnextButton = new Button(optsGroup, SWT.CHECK);
        fSvnextButton.setText("svn:externals");
        fSvnCheckoutButton = new Button(optsGroup, SWT.CHECK);
        fSvnCheckoutButton.setText("svn:checkout");
        Group btnGroup = new Group(optsComp, SWT.NULL);
        btnGroup.setText("Configuration");
        btnGroup.setLayout(new GridLayout(2, false));
        fInstallButton = new Button(btnGroup, SWT.RADIO);
        fInstallButton.setText("Install");
        fInstallButton.setSelection(true);
        fRemoveButton = new Button(btnGroup, SWT.RADIO);
        fRemoveButton.setText("Remove");
        fGoButton = new Button(optsComp, SWT.PUSH);
        fGoButton.setText("Go");
        GridData goBtnData = new GridData();
        goBtnData.widthHint = 50;
        fGoButton.setLayoutData(goBtnData);
        fGoButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                if (fInstallButton.getSelection()) {
                    executeInstallCommand(fSvnextButton.getSelection(), fSvnCheckoutButton.getSelection());
                } else {
                    executeRemoveCommand();
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    /**
	 * Installs the selected plugin
	 * 
	 * @param externals
	 *            true if an svn:externals entry is to be added, false otherwise
	 */
    private void executeInstallCommand(boolean externals, boolean checkout) {
        IStructuredSelection sel = (IStructuredSelection) fPluginsList.getSelection();
        if (sel != null) {
            RailsPluginDescriptor plugin = (RailsPluginDescriptor) sel.getFirstElement();
            final IProject project = RailsPlugin.getSelectedOrOnlyRailsProject();
            if (project != null) {
                RailsPluginsHelper.installPlugin(project, plugin, externals, checkout);
            }
        }
    }

    /**
	 * Removes the selected plugin
	 */
    private void executeRemoveCommand() {
        IStructuredSelection sel = (IStructuredSelection) fPluginsList.getSelection();
        if (sel != null) {
            RailsPluginDescriptor plugin = (RailsPluginDescriptor) sel.getFirstElement();
            IProject project = RailsPlugin.getSelectedOrOnlyRailsProject();
            if (project != null) {
                RailsPluginsHelper.removePlugin(project, plugin);
            }
        }
    }

    @Override
    public Control getControl() {
        return fPluginsView;
    }

    @Override
    public void setFocus() {
        fPluginsList.getList().setFocus();
    }

    /**
	 * Refreshes the list of plugins.
	 */
    protected void refreshPlugins() {
        Job j = new Job("Refresh plugins") {

            protected IStatus run(IProgressMonitor monitor) {
                try {
                    monitor.beginTask("Loading Rails plugins", 3);
                    monitor.worked(1);
                    RailsPluginsManager.getInstance().updatePlugins(monitor);
                    monitor.worked(1);
                    final List plugins = RailsPluginsManager.getInstance().getPlugins();
                    PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

                        public void run() {
                            fPluginsList.setInput(plugins);
                        }
                    });
                    monitor.worked(1);
                } catch (Exception e) {
                    RailsUILog.logError("Error loading Rails plugin list", e);
                    MessageDialog.openError(getSite().getShell(), "Error refreshing plugin list", e.getMessage());
                } finally {
                    monitor.done();
                }
                return Status.OK_STATUS;
            }
        };
        j.schedule();
    }

    public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection sel = (IStructuredSelection) event.getSelection();
        RailsPluginDescriptor plg = (RailsPluginDescriptor) sel.getFirstElement();
        if (plg == null) return;
        String contents = RailsUIPlugin.getInstance().getFileContents("html/rails_plugins.html");
        String css = RailsUIPlugin.getInstance().getFileContents("html/style.css");
        copyImageIfNotExist("bg_hdr_bod.gif");
        copyImageIfNotExist("bg_hdr_lt.gif");
        copyImageIfNotExist("bg_hdr_rt.gif");
        copyImageIfNotExist("bg_ratebox.png");
        css = css.replace("PATH", RailsUIPlugin.getInstance().getStateLocation().toPortableString());
        contents = contents.replace("CSS", css);
        if (plg.getProperty(RailsPluginDescriptor.NAME) != null) {
            contents = contents.replace("PLUGIN_NAME", plg.getProperty(RailsPluginDescriptor.NAME));
        }
        if (plg.getProperty(RailsPluginDescriptor.RATING) != null && plg.getProperty(RailsPluginDescriptor.RATING).trim().length() > 0) {
            String rating = plg.getProperty(RailsPluginDescriptor.RATING);
            double value = Double.parseDouble(rating);
            value = Math.round(value * 10) / 10.0;
            contents = contents.replace("RATING", Double.toString(value));
        } else {
            contents = contents.replace("RATING", "?");
        }
        if (plg.getProperty(RailsPluginDescriptor.REPOSITORY) != null) {
            contents = contents.replace("SVN_URL", plg.getProperty(RailsPluginDescriptor.REPOSITORY));
        }
        if (plg.getProperty(RailsPluginDescriptor.HOME) != null && plg.getProperty(RailsPluginDescriptor.HOME).trim().length() > 0) {
            contents = contents.replace("PLUGIN_HOMEPAGE", plg.getProperty(RailsPluginDescriptor.HOME));
        } else {
            contents = contents.replace("PLUGIN_HOMEPAGE", "");
        }
        if (plg.getProperty(RailsPluginDescriptor.LICENSE) != null && plg.getProperty(RailsPluginDescriptor.LICENSE).trim().length() > 0) {
            contents = contents.replace("LICENSE", plg.getProperty(RailsPluginDescriptor.LICENSE));
        } else {
            contents = contents.replace("LICENSE", "Unknown");
        }
        browser.setText(contents);
    }

    private void copyImageIfNotExist(String string) {
        File destination = RailsUIPlugin.getInstance().getStateLocation().append(new Path(string)).toFile();
        if (destination.exists() && destination.isFile()) return;
        URL url = FileLocator.find(RailsUIPlugin.getInstance().getBundle(), new Path("html/img/" + string), null);
        FileOutputStream stream = null;
        try {
            byte[] image = Util.getInputStreamAsByteArray(url.openStream(), -1);
            stream = new FileOutputStream(destination);
            stream.write(image);
        } catch (IOException e) {
            RailsUILog.log(e);
        } finally {
            try {
                if (stream != null) stream.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        super.dispose();
    }
}
