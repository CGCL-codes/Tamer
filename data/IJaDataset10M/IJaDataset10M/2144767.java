package org.mobicents.eclipslee.servicecreation.ui;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProjectModulesPanel extends Composite {

    private static final String EVENTS_MODULE_TEXT = "&Events";

    private static final String SBB_MODULE_TEXT = "&SBB (Service Building Block)";

    private static final String PROFILE_SPEC_MODULE_TEXT = "&Profile Specification";

    private static final String RA_TYPE_MODULE_TEXT = "Resource Adaptor &Type";

    private static final String RA_MODULE_TEXT = "&Resource Adaptor";

    private static final String LIBRARY_MODULE_TEXT = "&Library";

    private static final String DEPLOYABLE_UNIT_MODULE_TEXT = "Deployable Unit";

    private Button sleeExtCheckbox;

    private Button eventsCheckbox;

    private Button sbbCheckbox;

    private Button profileSpecCheckbox;

    private Button raTypeCheckbox;

    private Button raCheckbox;

    private Button libraryCheckbox;

    private Button deployableUnitCheckbox;

    private Button hlService;

    private Button hlEnabler;

    private Button hlResourceAdaptor;

    private Button hlCustom;

    public ProjectModulesPanel(Composite parent, int style) {
        super(parent, style);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        setLayout(layout);
        Group sleeExtGroup = new Group(this, SWT.SHADOW_ETCHED_IN);
        sleeExtGroup.setText("Extensions");
        sleeExtCheckbox = new Button(sleeExtGroup, SWT.CHECK);
        sleeExtCheckbox.setText("Use Mobicents JAIN SLEE 1.1 Extensions");
        sleeExtCheckbox.setSelection(true);
        sleeExtCheckbox.pack();
        new Button(this, SWT.CHECK).setVisible(false);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        Label hlLabel = new Label(this, SWT.NONE);
        hlLabel.setText("Select a High-Level Component Type");
        new Label(this, SWT.NONE);
        hlService = new Button(this, SWT.RADIO);
        hlService.setText("JAIN SLEE Service");
        hlService.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent se) {
                eventsCheckbox.setEnabled(true);
                eventsCheckbox.setSelection(false);
                sbbCheckbox.setEnabled(false);
                sbbCheckbox.setSelection(true);
                profileSpecCheckbox.setEnabled(true);
                profileSpecCheckbox.setSelection(false);
                libraryCheckbox.setEnabled(true);
                libraryCheckbox.setSelection(false);
                raTypeCheckbox.setEnabled(false);
                raTypeCheckbox.setSelection(false);
                raCheckbox.setEnabled(false);
                raCheckbox.setSelection(false);
                ;
            }

            public void widgetDefaultSelected(SelectionEvent se) {
            }
        });
        hlService.pack();
        hlEnabler = new Button(this, SWT.RADIO);
        hlEnabler.setText("JAIN SLEE Enabler");
        hlEnabler.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent se) {
                eventsCheckbox.setEnabled(true);
                eventsCheckbox.setSelection(false);
                sbbCheckbox.setEnabled(false);
                sbbCheckbox.setSelection(true);
                profileSpecCheckbox.setEnabled(true);
                profileSpecCheckbox.setSelection(false);
                libraryCheckbox.setEnabled(true);
                libraryCheckbox.setSelection(false);
                raTypeCheckbox.setEnabled(false);
                raTypeCheckbox.setSelection(false);
                raCheckbox.setEnabled(false);
                raCheckbox.setSelection(false);
                ;
            }

            public void widgetDefaultSelected(SelectionEvent se) {
            }
        });
        hlEnabler.pack();
        hlResourceAdaptor = new Button(this, SWT.RADIO);
        hlResourceAdaptor.setText("JAIN SLEE Resource Adaptor");
        hlResourceAdaptor.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent se) {
                eventsCheckbox.setEnabled(true);
                eventsCheckbox.setSelection(true);
                raTypeCheckbox.setEnabled(true);
                raTypeCheckbox.setSelection(true);
                raCheckbox.setEnabled(false);
                raCheckbox.setSelection(true);
                ;
                libraryCheckbox.setEnabled(true);
                libraryCheckbox.setSelection(false);
                sbbCheckbox.setEnabled(false);
                sbbCheckbox.setSelection(false);
                profileSpecCheckbox.setEnabled(false);
                profileSpecCheckbox.setSelection(false);
            }

            public void widgetDefaultSelected(SelectionEvent se) {
            }
        });
        hlResourceAdaptor.pack();
        hlCustom = new Button(this, SWT.RADIO);
        hlCustom.setText("Custom...");
        hlCustom.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent se) {
                eventsCheckbox.setEnabled(true);
                raTypeCheckbox.setEnabled(true);
                raCheckbox.setEnabled(true);
                libraryCheckbox.setEnabled(true);
                sbbCheckbox.setEnabled(true);
                profileSpecCheckbox.setEnabled(true);
            }

            public void widgetDefaultSelected(SelectionEvent se) {
            }
        });
        hlCustom.setSelection(true);
        hlCustom.pack();
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        new Label(this, SWT.NONE).setText("Components:");
        new Label(this, SWT.NONE);
        eventsCheckbox = new Button(this, SWT.CHECK);
        eventsCheckbox.setText(EVENTS_MODULE_TEXT);
        eventsCheckbox.setSelection(false);
        profileSpecCheckbox = new Button(this, SWT.CHECK);
        profileSpecCheckbox.setText(PROFILE_SPEC_MODULE_TEXT);
        profileSpecCheckbox.setSelection(false);
        raTypeCheckbox = new Button(this, SWT.CHECK);
        raTypeCheckbox.setText(RA_TYPE_MODULE_TEXT);
        raTypeCheckbox.setSelection(false);
        raCheckbox = new Button(this, SWT.CHECK);
        raCheckbox.setText(RA_MODULE_TEXT);
        raCheckbox.setSelection(false);
        sbbCheckbox = new Button(this, SWT.CHECK);
        sbbCheckbox.setText(SBB_MODULE_TEXT);
        sbbCheckbox.setSelection(true);
        libraryCheckbox = new Button(this, SWT.CHECK);
        libraryCheckbox.setText(LIBRARY_MODULE_TEXT);
        libraryCheckbox.setSelection(false);
        deployableUnitCheckbox = new Button(this, SWT.CHECK);
        deployableUnitCheckbox.setText(DEPLOYABLE_UNIT_MODULE_TEXT);
        deployableUnitCheckbox.setSelection(true);
        deployableUnitCheckbox.setEnabled(false);
    }

    public ArrayList<String> getModules() {
        ArrayList<String> modules = new ArrayList<String>();
        if (eventsCheckbox.getSelection()) {
            modules.add("events");
        }
        if (sbbCheckbox.getSelection()) {
            modules.add("sbb");
        }
        if (profileSpecCheckbox.getSelection()) {
            modules.add("profile-spec");
        }
        if (raTypeCheckbox.getSelection()) {
            modules.add("ratype");
        }
        if (raCheckbox.getSelection()) {
            modules.add("ra");
        }
        if (libraryCheckbox.getSelection()) {
            modules.add("library");
        }
        if (deployableUnitCheckbox.getSelection()) {
            modules.add("du");
        }
        return modules;
    }

    public boolean getUseExtensions() {
        return sleeExtCheckbox.getSelection();
    }
}
