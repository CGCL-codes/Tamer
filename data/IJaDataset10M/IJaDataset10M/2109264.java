package ch.oblivion.comixviewer.rcpplugin.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import ch.oblivion.comixviewer.engine.model.ComixProfile;
import ch.oblivion.comixviewer.rcpplugin.bindings.FileBindingStrategy;
import ch.oblivion.comixviewer.rcpplugin.bindings.UrlBindingStrategy;
import ch.oblivion.comixviewer.rcpplugin.model.UiComixProfile;
import ch.oblivion.comixviewer.rcpplugin.validators.FileTextSupport;
import ch.oblivion.comixviewer.rcpplugin.validators.NonEmptyTextSupport;
import ch.oblivion.comixviewer.rcpplugin.validators.URLTextSupport;

/**
 * Show/Edit the profile page details.
 * This page allows editing of the main page details.
 * The next page is used to setup the patterns using a preview of the
 * source page obtained from the given initial URL.
 * 
 * @author mark.miller
 */
public class ProfileNamePage extends WizardPage implements PropertyChangeListener {

    private static final int TEXT_WIDTH_HINT = 500;

    private final UiComixProfile profileModel;

    protected ProfileNamePage(UiComixProfile profileModel) {
        super("ProfileEditor");
        this.profileModel = profileModel;
        setPageComplete(isValid());
        rebuildDescription();
    }

    @Override
    public void createControl(Composite parent) {
        final Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout(2, false));
        setControl(container);
        Label nameLabel = new Label(container, SWT.NONE);
        nameLabel.setText("Name");
        nameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        Text nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(createTextGridData());
        NonEmptyTextSupport.enableFor(nameText);
        Label mainUrlLabel = new Label(container, SWT.NONE);
        mainUrlLabel.setText("Main page URL");
        mainUrlLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        Text mainUrlText = new Text(container, SWT.BORDER);
        mainUrlText.setLayoutData(createTextGridData());
        NonEmptyTextSupport.enableFor(mainUrlText);
        URLTextSupport.enableFor(mainUrlText);
        Label initialUrlLabel = new Label(container, SWT.NONE);
        initialUrlLabel.setText("Initial URL");
        initialUrlLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        Text initialUrlText = new Text(container, SWT.BORDER);
        initialUrlText.setLayoutData(createTextGridData());
        NonEmptyTextSupport.enableFor(initialUrlText);
        URLTextSupport.enableFor(initialUrlText);
        Label descriptionLabel = new Label(container, SWT.NONE);
        descriptionLabel.setText("Description");
        descriptionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        Text descriptionText = new Text(container, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd = createTextGridData();
        gd.heightHint = 50;
        descriptionText.setLayoutData(gd);
        Label categoryLabel = new Label(container, SWT.NONE);
        categoryLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        categoryLabel.setText("Category");
        Combo categoryCombo = new Combo(container, SWT.NONE);
        categoryCombo.setLayoutData(createTextGridData());
        NonEmptyTextSupport.enableFor(categoryCombo);
        Label fileLabel = new Label(container, SWT.NONE);
        fileLabel.setText("Local Path");
        fileLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        Composite fileComp = new Composite(container, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        removeLayoutMargins(layout);
        fileComp.setLayout(layout);
        fileComp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        final Text fileText = new Text(fileComp, SWT.BORDER);
        fileText.setLayoutData(createTextGridData());
        Button fileButton = new Button(fileComp, SWT.PUSH);
        fileButton.setText("&Browse");
        fileButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
                String path = dialog.open();
                if (path != null) {
                    fileText.setText(path);
                }
            }
        });
        NonEmptyTextSupport.enableFor(fileText);
        FileTextSupport.enableFor(fileText);
        DataBindingContext dbc = new DataBindingContext();
        IObservableValue nameTextObservable = BeansObservables.observeValue(profileModel, ComixProfile.PROFILE_NAME);
        dbc.bindValue(SWTObservables.observeText(nameText, SWT.Modify), nameTextObservable, null, null);
        IObservableValue mainUrlTextObservable = BeansObservables.observeValue(profileModel, ComixProfile.MAIN_PAGE_URL);
        dbc.bindValue(SWTObservables.observeText(mainUrlText, SWT.Modify), mainUrlTextObservable, UrlBindingStrategy.createStringToUrlStrategy(), UrlBindingStrategy.createUrlToStringStrategy());
        IObservableValue initialUrlTextObservable = BeansObservables.observeValue(profileModel, ComixProfile.INITIAL_URL);
        dbc.bindValue(SWTObservables.observeText(initialUrlText, SWT.Modify), initialUrlTextObservable, UrlBindingStrategy.createStringToUrlStrategy(), UrlBindingStrategy.createUrlToStringStrategy());
        IObservableValue descriptionTextObservable = BeansObservables.observeValue(profileModel, ComixProfile.PROFILE_DESCRIPTION);
        dbc.bindValue(SWTObservables.observeText(descriptionText, SWT.Modify), descriptionTextObservable, null, null);
        IObservableValue categoryTextObservable = BeansObservables.observeValue(profileModel, ComixProfile.PROFILE_CATEGORY);
        dbc.bindValue(SWTObservables.observeText(categoryCombo), categoryTextObservable, null, null);
        IObservableValue fileTextObservable = BeansObservables.observeValue(profileModel, ComixProfile.LOCAL_PATH);
        dbc.bindValue(SWTObservables.observeText(fileText, SWT.Modify), fileTextObservable, FileBindingStrategy.createStringToFileStrategy(), FileBindingStrategy.createFileToStringStrategy());
        profileModel.addPropertyChangeListener(this);
    }

    @Override
    public void dispose() {
        super.dispose();
        profileModel.removePropertyChangeListener(this);
    }

    private void removeLayoutMargins(GridLayout layout) {
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;
        layout.marginBottom = 0;
        layout.marginTop = 0;
    }

    private GridData createTextGridData() {
        GridData gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.horizontalIndent = 5;
        gd.widthHint = TEXT_WIDTH_HINT;
        return gd;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        setPageComplete(isValid());
        rebuildDescription();
        getWizard().getContainer().updateButtons();
    }

    private void rebuildDescription() {
        setDescription("Profile Editor " + profileModel.getProfileName());
    }

    private boolean isValid() {
        if (profileModel.getProfileName() == null || profileModel.getProfileName().isEmpty()) {
            setErrorMessage("Profile name should not be null");
            return false;
        }
        if (profileModel.getMainPageUrl() == null) {
            setErrorMessage("Main page URL should not be null");
            return false;
        }
        if (profileModel.getInitialUrl() == null) {
            setErrorMessage("Initial URL not be null");
            return false;
        }
        if (profileModel.getProfileCategory() == null || profileModel.getProfileCategory().isEmpty()) {
            setErrorMessage("Profile category should not be null");
            return false;
        }
        if (profileModel.getLocalPath() == null) {
            setErrorMessage("Local path should not be null");
            return false;
        }
        setErrorMessage(null);
        return true;
    }
}
