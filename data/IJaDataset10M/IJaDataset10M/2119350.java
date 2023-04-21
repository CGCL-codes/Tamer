package uk.ac.ed.csbe.plasmo;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class PlasmoSearchPage extends DialogPage implements ISearchPage {

    private static final Color back = new Color(Display.getCurrent(), new RGB(222, 222, 222));

    private static final Color front = new Color(Display.getCurrent(), new RGB(100, 100, 100));

    @SuppressWarnings("unused")
    private ISearchPageContainer container;

    private Text searchEntryBox;

    private String username;

    private String password;

    private PlasmoHistoryManagement history = PlasmoHistoryManagement.getInstance();

    private Label serviceType;

    private FormToolkit toolkit;

    private Form form;

    public PlasmoSearchPage() {
    }

    public PlasmoSearchPage(String title) {
        super(title);
    }

    public PlasmoSearchPage(String title, ImageDescriptor image) {
        super(title, image);
    }

    public boolean performAction() {
        if ((username == null || username.trim().equals("")) && (password == null || password.trim().equals(""))) {
            NewSearchUI.runQueryInBackground(new PlasmoQuery(searchEntryBox.getText()));
        } else {
            if (username == null || username.trim().length() == 0 || password == null || password.trim().length() == 0) {
                MessageDialog.openError(Display.getCurrent().getActiveShell(), "Incomplete", "Fill in a username and a password on the preferences page.");
                return false;
            }
            PlasmoQuery pq = new PlasmoQuery(searchEntryBox.getText(), username, password);
            NewSearchUI.runQueryInBackground(pq);
        }
        return true;
    }

    public void setContainer(ISearchPageContainer container) {
        this.container = container;
    }

    public void createControl(Composite parent) {
        parent.setBackground(back);
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createForm(parent);
        form.setText("Search the Plasmo database.");
        TableWrapLayout tllayout = new TableWrapLayout();
        tllayout.numColumns = 1;
        form.getBody().setLayout(tllayout);
        TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.grabHorizontal = true;
        td.grabVertical = true;
        form.getBody().setLayoutData(td);
        form.setBackground(back);
        setupQTypeSection(form);
        createTextBox(form);
        Composite sectionClient = setupHelpSection(form, "Enter text " + "to find models which 'contain' that text.\n\n" + "Enter '*' or leave blank to select all models. \n\n" + "Select row(s) from the search results and double click to download the models from Plasmo. ", false);
        setControl(form);
    }

    private void setupQTypeSection(final Form form) {
        Section statusSection = toolkit.createSection(form.getBody(), Section.NO_TITLE | Section.COMPACT);
        statusSection.setVisible(true);
        statusSection.setBackground(back);
        TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.grabHorizontal = true;
        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        statusSection.setLayout(layout);
        statusSection.setLayoutData(td);
        statusSection.setLayout(new TableWrapLayout());
        statusSection.layout();
        final Composite sectionClient = toolkit.createComposite(statusSection);
        sectionClient.setLayout(new TableWrapLayout());
        statusSection.setClient(sectionClient);
        sectionClient.layout();
        sectionClient.setBackground(back);
        serviceType = toolkit.createLabel(sectionClient, "");
        TableWrapData td2 = new TableWrapData(TableWrapData.FILL_GRAB);
        td2.grabHorizontal = true;
        serviceType.setLayoutData(td2);
        serviceType.setBackground(back);
        serviceType.setForeground(front);
        serviceType.setText("\n");
    }

    private Composite setupHelpSection(final Form form, String string, boolean b) {
        Section s1 = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TWISTIE | Section.COMPACT);
        s1.setVisible(true);
        s1.setText("help");
        s1.setBackground(back);
        TableWrapData td = new TableWrapData(TableWrapData.FILL);
        td.grabHorizontal = true;
        td.colspan = 1;
        s1.setLayoutData(td);
        s1.addExpansionListener(new ExpansionAdapter() {

            public void expansionStateChanged(ExpansionEvent e) {
                form.redraw();
            }
        });
        final Composite sectionClient = toolkit.createComposite(s1);
        sectionClient.setLayout(new TableWrapLayout());
        s1.setClient(sectionClient);
        sectionClient.layout();
        sectionClient.setBackground(back);
        Text help = new Text(sectionClient, SWT.READ_ONLY);
        help.setText(string);
        help.setBackground(back);
        help.setForeground(front);
        TableWrapData td2 = new TableWrapData();
        td2.grabVertical = true;
        help.setLayoutData(td2);
        return sectionClient;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        String[] userpass = history.loadUsernamePassFromDialogSettings();
        if (userpass != null) {
            username = userpass[0];
            password = userpass[1];
            serviceType.setText("Querying public and private models.");
            form.redraw();
        } else {
            serviceType.setText("Querying public models only: \nenter a name and password on the preference page to query private models as well.");
            form.redraw();
        }
    }

    private void createTextBox(Composite com) {
        Section query = toolkit.createSection(form.getBody(), Section.DESCRIPTION);
        query.setVisible(true);
        query.setBackground(back);
        final Composite sectionClient = toolkit.createComposite(query);
        query.setClient(sectionClient);
        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        sectionClient.setLayout(layout);
        sectionClient.setBackground(back);
        Label lab = toolkit.createLabel(sectionClient, "Enter search term:");
        lab.setBackground(back);
        lab.setForeground(front);
        searchEntryBox = toolkit.createText(sectionClient, "");
        searchEntryBox.setBackground(back);
        TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.grabHorizontal = true;
        searchEntryBox.setLayoutData(td);
        sectionClient.layout();
    }
}
