package org.echarts.edt.ui.projectproperties;

import java.util.ArrayList;
import org.echarts.edt.core.EChartsCore;
import org.echarts.edt.core.IDevelopmentKit;
import org.echarts.edt.core.IEChartsPathEntry;
import org.echarts.edt.ui.swt.controls.EChartsProjectPropertiesControl;
import org.echarts.edt.ui.wizards.EChartsPropertiesHolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * PropertyPage class for manipulating the eCharts build environment.
 * 
 * @author Stephen Gevers
 *
 */
public class EChartsProjectPropertiesPage extends PropertyPage {

    EChartsProjectPropertiesControl control;

    EChartsPropertiesHolder propertiesHolder = new EChartsPropertiesHolder();

    Shell parentShell = null;

    boolean applied = false;

    ArrayList list;

    public EChartsProjectPropertiesPage() {
        super();
    }

    protected Control createContents(Composite parent) {
        parentShell = parent.getShell();
        IAdaptable adaptable = getElement();
        try {
            propertiesHolder.setProject(EChartsCore.create((IProject) adaptable));
        } catch (CoreException e) {
            ErrorDialog.openError(parentShell, null, null, e.getStatus());
            return null;
        }
        EChartsProjectPropertiesControl control = new EChartsProjectPropertiesControl(parent, SWT.NONE);
        this.control = control;
        control.setProperties(propertiesHolder);
        return control;
    }

    public boolean performOk() {
        boolean ret = super.performOk();
        if (!applied) {
            System.out.println("Oops, apply didn't happen");
            performApply();
        }
        return ret;
    }

    public EChartsPropertiesHolder getProjectProperties() throws CoreException {
        propertiesHolder.echartsPath = getEChartsPath();
        propertiesHolder.defaultOutput = new Path(getDefaultOut());
        propertiesHolder.projectDK = getDK();
        return propertiesHolder;
    }

    public IEChartsPathEntry[] getEChartsPath() throws CoreException {
        return (IEChartsPathEntry[]) control.getPathEntries().toArray(new IEChartsPathEntry[0]);
    }

    public String getDefaultOut() {
        return control.getDefaultOutputLocation();
    }

    public IDevelopmentKit getDK() {
        return control.getDK();
    }

    protected void performApply() {
        applied = true;
        try {
            getProjectProperties().updateProject(parentShell);
        } catch (CoreException e) {
            ErrorDialog.openError(parentShell, null, null, e.getStatus());
        }
        super.performApply();
    }
}
