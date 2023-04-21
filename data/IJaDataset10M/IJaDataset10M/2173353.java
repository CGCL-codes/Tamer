package de.tmtools.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import de.tmtools.ui.AssociationTuple;

public class ConfigTopicDialog extends Dialog {

    /**
     * The code returned from the showDialog method. This value is defined by
     * the values defined in JOptionPane.
     */
    private Text textNames;

    private Text textTypes;

    private Text textAssociations;

    private List listNames;

    private List listTypes;

    private List listOccurrences;

    private List listAssociations;

    private String[] namesStrings;

    private String[] typesStrings;

    private String[] occurStrings;

    class AddSelectionListener implements SelectionListener {

        private Text text;

        private List list;

        public AddSelectionListener(Text text, List list) {
            this.text = text;
            this.list = list;
        }

        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            String s = text.getText();
            if (s.equals("")) return;
            if (listContains(list, s) == false) {
                list.add(s);
                list.select(list.getItemCount() - 1);
                text.setText("");
            }
        }

        protected boolean listContains(List list, String value) {
            for (int i = 0; i < list.getItemCount(); i++) {
                String s = list.getItem(i);
                if (s.equals(value)) return true;
            }
            return false;
        }
    }

    class RemoveSelectionListener implements SelectionListener {

        private List list;

        public RemoveSelectionListener(List list) {
            this.list = list;
        }

        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            if (list.getSelectionCount() > 0) list.remove(list.getSelectionIndex());
        }
    }

    public ConfigTopicDialog(Shell parentShell, String title) {
        super(parentShell);
    }

    @Override
    protected Control createContents(Composite parent) {
        parent.getShell().setText("Configure Topic");
        return super.createContents(parent);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        FormToolkit toolkit = new FormToolkit(parent.getDisplay());
        Form form = toolkit.createForm(parent);
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);
        GridData fgd = new GridData(GridData.FILL_BOTH);
        form.setLayoutData(fgd);
        CTabFolder tabFolder = new CTabFolder(form.getBody(), SWT.FLAT);
        tabFolder.setSimple(false);
        GridData pgd = new GridData(GridData.FILL_BOTH);
        pgd.heightHint = 200;
        pgd.widthHint = 155;
        tabFolder.setLayoutData(pgd);
        CTabItem ti1 = new CTabItem(tabFolder, SWT.BORDER);
        ti1.setText("General");
        CTabItem ti2 = new CTabItem(tabFolder, SWT.BORDER);
        ti2.setText("Scope");
        CTabItem ti3 = new CTabItem(tabFolder, SWT.BORDER);
        ti3.setText("Enrolement");
        Composite tabForm1 = toolkit.createComposite(tabFolder);
        layout = new GridLayout();
        tabForm1.setLayout(layout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 10;
        gd.verticalSpan = 10;
        tabForm1.setLayoutData(gd);
        Section section = toolkit.createSection(tabForm1, Section.DESCRIPTION | Section.TITLE_BAR | Section.EXPANDED);
        GridData sgd = new GridData(GridData.FILL_BOTH);
        sgd.horizontalIndent = 10;
        sgd.minimumHeight = 100;
        section.setLayoutData(sgd);
        section.setText("Names");
        section.setDescription("Edit topic names here.");
        Composite sectionClient = toolkit.createComposite(section);
        GridLayout secLayout = new GridLayout();
        secLayout.numColumns = 3;
        sectionClient.setLayout(secLayout);
        textNames = toolkit.createText(sectionClient, "");
        textNames.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Button btn1 = toolkit.createButton(sectionClient, "Add", SWT.PUSH);
        Button btn2 = toolkit.createButton(sectionClient, "Remove", SWT.PUSH);
        listNames = new List(sectionClient, SWT.BORDER);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        gd.minimumHeight = 70;
        listNames.setLayoutData(gd);
        btn1.addSelectionListener(new AddSelectionListener(textNames, listNames));
        btn2.addSelectionListener(new RemoveSelectionListener(listNames));
        section.setClient(sectionClient);
        section = toolkit.createSection(tabForm1, Section.DESCRIPTION | Section.TITLE_BAR | Section.EXPANDED);
        section.setLayoutData(sgd);
        section.setText("Types");
        section.setDescription("Edit topic types here.");
        sectionClient = toolkit.createComposite(section);
        secLayout = new GridLayout();
        secLayout.numColumns = 3;
        sectionClient.setLayout(secLayout);
        textTypes = toolkit.createText(sectionClient, "");
        textTypes.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btn1 = toolkit.createButton(sectionClient, "Add", SWT.PUSH);
        btn2 = toolkit.createButton(sectionClient, "Remove", SWT.PUSH);
        listTypes = new List(sectionClient, SWT.BORDER);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        gd.minimumHeight = 70;
        listTypes.setLayoutData(gd);
        btn1.addSelectionListener(new AddSelectionListener(textTypes, listTypes));
        btn2.addSelectionListener(new RemoveSelectionListener(listTypes));
        section.setClient(sectionClient);
        ti1.setControl(tabForm1);
        Composite tabForm2 = toolkit.createComposite(tabFolder);
        tabForm2.setLayout(new GridLayout());
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 10;
        gd.verticalSpan = 10;
        tabForm2.setLayoutData(gd);
        section = toolkit.createSection(tabForm2, Section.TITLE_BAR | Section.EXPANDED);
        section.setLayoutData(sgd);
        section.setText("Occurences");
        sectionClient = toolkit.createComposite(section);
        secLayout = new GridLayout();
        secLayout.numColumns = 3;
        sectionClient.setLayout(secLayout);
        Text text = toolkit.createText(sectionClient, "");
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        toolkit.createButton(sectionClient, "Add", SWT.PUSH);
        toolkit.createButton(sectionClient, "Remove", SWT.PUSH);
        listOccurrences = new List(sectionClient, SWT.BORDER);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        gd.minimumHeight = 70;
        listOccurrences.setLayoutData(gd);
        section.setClient(sectionClient);
        ti2.setControl(tabForm2);
        Composite tabForm3 = toolkit.createComposite(tabFolder);
        tabForm3.setLayout(new GridLayout());
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 10;
        gd.verticalSpan = 10;
        tabForm3.setLayoutData(gd);
        section = toolkit.createSection(tabForm3, Section.TITLE_BAR | Section.EXPANDED);
        section.setLayoutData(sgd);
        section.setText("Associations");
        sectionClient = toolkit.createComposite(section);
        secLayout = new GridLayout();
        secLayout.numColumns = 2;
        sectionClient.setLayout(secLayout);
        toolkit.createLabel(sectionClient, "Topic typing association:");
        textAssociations = toolkit.createText(sectionClient, "");
        textAssociations.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        toolkit.createLabel(sectionClient, "Topic defining roleSpec:");
        textAssociations = toolkit.createText(sectionClient, "");
        textAssociations.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        toolkit.createLabel(sectionClient, "Topic playing role:");
        textAssociations = toolkit.createText(sectionClient, "");
        textAssociations.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        btn1 = toolkit.createButton(sectionClient, "Add", SWT.PUSH);
        btn2 = toolkit.createButton(sectionClient, "Remove", SWT.PUSH);
        listAssociations = new List(sectionClient, SWT.BORDER);
        gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 3;
        gd.minimumHeight = 70;
        listAssociations.setLayoutData(gd);
        btn1.addSelectionListener(new AddSelectionListener(textAssociations, listAssociations));
        btn2.addSelectionListener(new RemoveSelectionListener(listAssociations));
        section.setClient(sectionClient);
        ti3.setControl(tabForm3);
        return form.getBody();
    }

    @Override
    protected Point getInitialSize() {
        return new Point(350, 500);
    }

    private final String[] toStringArray(List list) {
        return list.getItems();
    }

    public final String[] getNames() {
        return namesStrings;
    }

    public final String[] getTypes() {
        return typesStrings;
    }

    public final String[] getOccurrences() {
        return occurStrings;
    }

    public final AssociationTuple[] getAssociations() {
        return new AssociationTuple[0];
    }

    @Override
    protected void okPressed() {
        updateStringLists();
        super.okPressed();
    }

    protected void updateStringLists() {
        namesStrings = toStringArray(listNames);
        typesStrings = toStringArray(listTypes);
        occurStrings = toStringArray(listOccurrences);
    }
}
