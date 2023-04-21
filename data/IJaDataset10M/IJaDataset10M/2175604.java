package org.eclipse.ice.properties;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.ice.ICE_Plugin;
import org.eclipse.ice.nature.ICENature;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

public class ICEFileProperties extends PropertyPage {

    private ICEDefinitionsGroup iceDefGroup;

    private ICEOutputGroup iceOutGroup;

    private ICEOtherOptionsGroup iceOOptionsGroup;

    private ICEIncludesGroup iceIncludesGroup;

    private boolean ICEReachable;

    private Button buttonExclude;

    private static QualifiedName qNameICEExclusion = new QualifiedName("org.eclipse.ice.builder", "ICEExclusion");

    public static boolean loadICEExclusion(IResource res) {
        String Out = null;
        try {
            Out = res.getPersistentProperty(qNameICEExclusion);
            if (Out == null) Out = res.getProject().getPersistentProperty(qNameICEExclusion);
        } catch (CoreException e) {
        }
        if (Out == null) return false; else if (Out.equals("0")) return false; else return true;
    }

    private void load() {
        try {
            String res = ((IResource) getElement()).getPersistentProperty(qNameICEExclusion);
            boolean enable;
            if (res == null) enable = true; else {
                if (res.equals("0")) enable = true; else enable = false;
            }
            buttonExclude.setSelection(!enable);
            iceDefGroup.setEnabled(enable);
            iceOutGroup.setEnabled(enable);
            iceOOptionsGroup.setEnabled(enable);
            iceIncludesGroup.setEnabled(enable);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private void save() {
        try {
            if (buttonExclude.getSelection()) ((IResource) getElement()).setPersistentProperty(qNameICEExclusion, "1"); else ((IResource) getElement()).setPersistentProperty(qNameICEExclusion, "0");
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    protected Control createContents(Composite parent) {
        ICEReachable = true;
        Composite maincontainer = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout();
        maincontainer.setLayout(gridLayout);
        if (ICE_Plugin.getICEVersion() == null) {
            Label labelErrorInformation = new Label(maincontainer, SWT.NONE);
            labelErrorInformation.setText("Impossible to reach the ICE programs like slice2java, icecpp, etc ...\n\nNote: Have you set the bin directory of ICE in the Path environment variable of Windows ?");
            ICEReachable = false;
            return maincontainer;
        }
        buttonExclude = new Button(maincontainer, SWT.CHECK);
        buttonExclude.setText("Exclude from ICE building");
        buttonExclude.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                iceDefGroup.setEnabled(!buttonExclude.getSelection());
                iceOutGroup.setEnabled(!buttonExclude.getSelection());
                iceIncludesGroup.setEnabled(!buttonExclude.getSelection());
                iceOOptionsGroup.setEnabled(!buttonExclude.getSelection());
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        iceDefGroup = new ICEDefinitionsGroup(maincontainer, SWT.NONE, (IResource) getElement(), ((IResource) getElement()).getProject());
        iceDefGroup.build();
        iceOutGroup = new ICEOutputGroup(maincontainer, SWT.NONE, (IResource) getElement(), ((IResource) getElement()).getProject());
        iceOutGroup.build();
        iceIncludesGroup = new ICEIncludesGroup(maincontainer, SWT.NONE, (IResource) getElement(), ((IResource) getElement()).getProject());
        iceIncludesGroup.build();
        iceOOptionsGroup = new ICEOtherOptionsGroup(maincontainer, SWT.NONE, (IResource) getElement(), ((IResource) getElement()).getProject());
        iceOOptionsGroup.build();
        if (!isProjectHaveIceNature() || !ICEReachable) {
            iceDefGroup.setEnabled(false);
            iceOutGroup.setEnabled(false);
            iceOOptionsGroup.setEnabled(false);
            iceIncludesGroup.setEnabled(false);
        }
        load();
        return maincontainer;
    }

    private void finishPage() {
        if (ICEReachable == false) return;
        iceDefGroup.save();
        iceOutGroup.save();
        iceOOptionsGroup.save();
        iceIncludesGroup.save();
        save();
    }

    private void restoreDefault() {
        if (isProjectHaveIceNature() && ICEReachable) {
            iceDefGroup.restoreDefault();
            iceOutGroup.restoreDefault();
            iceOOptionsGroup.restoreDefault();
            iceIncludesGroup.restoreDefault();
            buttonExclude.setSelection(false);
            iceDefGroup.setEnabled(true);
            iceOutGroup.setEnabled(true);
            iceOOptionsGroup.setEnabled(true);
            iceIncludesGroup.setEnabled(true);
        }
    }

    protected void performDefaults() {
        super.performDefaults();
        restoreDefault();
    }

    public boolean performOk() {
        finishPage();
        return super.performOk();
    }

    protected void performApply() {
        finishPage();
        super.performApply();
    }

    private boolean isProjectHaveIceNature() {
        try {
            IProject project = ((IResource) getElement()).getProject();
            IProjectDescription description = project.getDescription();
            String[] natures = description.getNatureIds();
            for (int i = 0; i < natures.length; i++) {
                if (natures[i].equals(ICENature.NATURE_ID)) return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
