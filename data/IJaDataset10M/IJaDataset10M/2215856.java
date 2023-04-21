package com.cjssolutions.plex.plugin.classpath;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.NewElementWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import com.cjssolutions.plex.plugin.Constants;
import com.cjssolutions.plex.plugin.PlexPlugin;
import com.cjssolutions.plex.plugin.PluginText;

public class PlexRuntimeClasspathWizardPage extends NewElementWizardPage implements IClasspathContainerPage {

    protected static Image fExternalJarImage;

    protected IClasspathEntry fContainerEntry;

    public PlexRuntimeClasspathWizardPage() {
        super("PlexRuntimeClasspathWiz");
        setTitle(PluginText.getString("ClasspathWizardPageTitle"));
        setImageDescriptor(JavaPluginImages.DESC_WIZBAN_ADD_LIBRARY);
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        setControl(composite);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);
        Group group = createGroup(composite, 2);
        group.setText(PluginText.getString(Constants.PLEX_RUNTIME_CLASSPATH_GROUPNAME));
        if (fExternalJarImage == null) {
            fExternalJarImage = JavaPlugin.getDefault().getImageRegistry().get("org.eclipse.jdt.ui.jar_l_obj.gif");
        }
        String entries[][] = PlexRuntime.getEntries();
        String pluginPackageLabel = ".../" + PlexPlugin.class.getPackage().getName() + "/";
        for (int i = 0; i < entries.length; i++) {
            (new Label(group, SWT.NONE)).setImage(fExternalJarImage);
            (new Label(group, SWT.NONE)).setText(pluginPackageLabel + entries[i][1]);
        }
    }

    protected Group createGroup(Composite c, int rows) {
        Group g = new Group(c, 0);
        g.setLayout(new GridLayout(rows, false));
        GridData gData = new GridData(768);
        gData.horizontalSpan = 2;
        g.setLayoutData(gData);
        return g;
    }

    public boolean finish() {
        Path path = new Path(Constants.PLEX_RUNTIME_CLASSPATH);
        setSelection(JavaCore.newContainerEntry(path));
        return true;
    }

    public IClasspathEntry getSelection() {
        return fContainerEntry;
    }

    public void setSelection(IClasspathEntry containerEntry) {
        fContainerEntry = containerEntry;
    }
}
