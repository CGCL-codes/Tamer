package edu.gsbme.wasabi.UI.Forms.Generics;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import edu.gsbme.wasabi.UI.UItext;

public class GenericTreeviewerAndButtons {

    public TreeViewer listTreeviewer;

    public Button list_new;

    public Button list_edit;

    public Button list_delete;

    public SelectionAdapter defaultButtonAction;

    public int numberOfDesiredLines = 8;

    public Section createLayout(FormToolkit toolkit, ScrolledForm form, GridData sectionGridData, String SectionTitle, String SectionDescriptions) {
        final ScrolledForm xform = form;
        Section section = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        section.setLayoutData(sectionGridData);
        section.addExpansionListener(new ExpansionAdapter() {

            public void expansionStateChanged(ExpansionEvent e) {
                xform.reflow(true);
            }
        });
        section.setText(SectionTitle);
        section.setDescription(SectionDescriptions);
        Composite sectionClient = toolkit.createComposite(section);
        GridLayout sc = new GridLayout();
        sc.numColumns = 3;
        sectionClient.setLayout(sc);
        Composite composite = new Composite(sectionClient, SWT.NULL);
        GridData c_gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        c_gd.horizontalSpan = 3;
        composite.setLayoutData(c_gd);
        composite.setLayout(new FillLayout());
        listTreeviewer = new TreeViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
        defaultButtonAction = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                buttonBehaviour();
            }
        };
        listTreeviewer.getTree().addSelectionListener(defaultButtonAction);
        list_new = new Button(sectionClient, SWT.PUSH);
        list_new.setText(UItext.new_);
        GridData gd = new GridData();
        gd.horizontalSpan = 1;
        list_new.setLayoutData(gd);
        list_edit = new Button(sectionClient, SWT.PUSH);
        list_edit.setText(UItext.edit);
        gd = new GridData();
        gd.horizontalSpan = 1;
        list_edit.setLayoutData(gd);
        list_delete = new Button(sectionClient, SWT.PUSH);
        list_delete.setText(UItext.delete);
        gd = new GridData();
        gd.horizontalSpan = 1;
        list_delete.setLayoutData(gd);
        section.setClient(sectionClient);
        return section;
    }

    private void buttonBehaviour() {
        if (listTreeviewer.getTree().getSelectionCount() > 0) {
            list_edit.setEnabled(true);
            list_delete.setEnabled(true);
        } else {
            list_edit.setEnabled(false);
            list_delete.setEnabled(false);
        }
    }
}
