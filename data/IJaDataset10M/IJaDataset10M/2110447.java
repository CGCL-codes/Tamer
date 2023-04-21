package de.mpicbg.buchholz.phenofam.client;

import java.util.Set;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.i18n.client.DateTimeFormat;
import de.mpicbg.buchholz.phenofam.client.PhenoFamDataProvider.FailureAcceptor;

public class PhenoFam implements EntryPoint {

    private final PhenoFamDataProvider dataProvider = new PhenoFamDataProvider();

    private final ListBox organismListBox = new ListBox(false);

    private final ListBox idTypeListBox = new ListBox(false);

    private final FormPanel uploadForm = new FormPanel();

    private final FileUpload fileUpload = new FileUpload();

    private final TextArea commandTextArea = new TextArea();

    private UserDataPanel userDataPanel = new UserDataPanel(dataProvider);

    private EnrichmentPanel enrichmentPanel = new EnrichmentPanel(userDataPanel);

    private DataRefPanel dataRefPanel = new DataRefPanel(dataProvider, enrichmentPanel);

    private TabPanel mainPanel = new TabPanel();

    private HTML versionBox = new HTML();

    private static final String[] VIP_SPECIES = new String[] { "Homo sapiens", "Mus musculus" };

    public void onModuleLoad() {
        dataProvider.setFailureHandler(new PhenoFamDataProvider.GlobalFailureHandler() {

            public void handleFailure(String method, Throwable caught, FailureAcceptor acceptor) {
                if (!method.equals("getAvailableLists")) {
                    dataRefPanel.update();
                }
                MessageBox.getMessageBox().showError("Error (" + method + ")", caught);
                acceptor.onFailure(caught);
            }
        });
        uploadForm.setMethod(FormPanel.METHOD_POST);
        uploadForm.setEncoding("multipart/form-data");
        uploadForm.setAction("upload");
        uploadForm.addFormHandler(new FormHandler() {

            public void onSubmit(FormSubmitEvent event) {
            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                mainPanel.selectTab(0);
                String res = event.getResults();
                boolean error = false;
                if (res == null || res.length() == 0) {
                    res = "Data transfer failed.";
                    error = true;
                } else if (res.startsWith("ERROR")) {
                    res = res.substring(5);
                    error = true;
                }
                MessageBox.getMessageBox().showMessage(error ? "Error" : "Upload Complete", res);
                dataRefPanel.update();
                Tracker.trackOnUploadComplete();
            }
        });
        final VerticalPanel leftPanel = new VerticalPanel();
        leftPanel.addStyleName("command-panel");
        uploadForm.add(leftPanel);
        versionBox.addStyleName("version-box");
        leftPanel.add(versionBox);
        dataProvider.executeGetDatabaseVersion(new PhenoFamDataProvider.DatabaseVersionAcceptor() {

            public void onFailure(Throwable caught) {
            }

            public void onSuccess(DatabaseVersion databaseVersion) {
                String html = "Realease " + databaseVersion.getRelease() + " from " + DateTimeFormat.getMediumDateFormat().format(databaseVersion.getReleaseDate()) + "<br />" + "Ensembl release: " + databaseVersion.getEnsemblRelease() + "<br />";
                versionBox.setHTML(html);
            }
        });
        organismListBox.addStyleName("command-id-list-box");
        organismListBox.setName("organism");
        leftPanel.add(organismListBox);
        leftPanel.setCellHeight(organismListBox, "30px");
        idTypeListBox.addStyleName("command-id-list-box");
        idTypeListBox.setName("idType");
        leftPanel.add(idTypeListBox);
        leftPanel.setCellHeight(idTypeListBox, "40px");
        InlineLabel label = new InlineLabel("Select a table to upload:");
        label.setTitle("Select a file containing two columns: one with ids and another with associated values");
        leftPanel.add(label);
        leftPanel.setCellHeight(label, "20px");
        fileUpload.addStyleName("file-upload");
        fileUpload.setName("dataFile");
        leftPanel.add(fileUpload);
        leftPanel.setCellHeight(fileUpload, "20px");
        label = new InlineLabel("Or paste your table here:");
        label.setTitle("Paste a table containing two columns: one with ids and another with associated values");
        leftPanel.add(label);
        leftPanel.setCellHeight(label, "20px");
        commandTextArea.addStyleName("command-text-area");
        commandTextArea.setName("dataText");
        leftPanel.add(commandTextArea);
        final Hidden exampleField = new Hidden("example", "false");
        leftPanel.add(exampleField);
        final Button uploadButton = new Button("Upload", new ClickListener() {

            public void onClick(Widget sender) {
                exampleField.setValue("false");
                uploadForm.submit();
            }
        });
        final Button clearFormButton = new Button("Clear", new ClickListener() {

            public void onClick(Widget sender) {
                FormElement.as(uploadForm.getElement()).reset();
            }
        });
        final Button exampleButton = new Button("Example", new ClickListener() {

            public void onClick(Widget sender) {
                exampleField.setValue("true");
                uploadForm.submit();
            }
        });
        FlowPanel buttonsPanel = new FlowPanel();
        buttonsPanel.add(clearFormButton);
        buttonsPanel.add(uploadButton);
        buttonsPanel.add(exampleButton);
        leftPanel.add(buttonsPanel);
        leftPanel.setCellHeight(uploadButton, "20px");
        leftPanel.setCellHorizontalAlignment(uploadButton, HasHorizontalAlignment.ALIGN_CENTER);
        final VerticalPanel resultsPanel = new VerticalPanel();
        resultsPanel.addStyleName("right-panel");
        resultsPanel.add(dataRefPanel);
        resultsPanel.setCellWidth(dataRefPanel, "100%");
        resultsPanel.add(enrichmentPanel);
        resultsPanel.add(userDataPanel);
        mainPanel.add(resultsPanel, "Data Panel");
        TabGenerator tabGenerator = new TabGenerator(mainPanel);
        tabGenerator.addTabAndSelect("guide/guide.html", "Users Guide");
        tabGenerator.addTab("guide/workflow.html", "Algorithm");
        HorizontalPanel panel = new HorizontalPanel();
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        panel.add(uploadForm);
        panel.add(mainPanel);
        panel.setCellWidth(mainPanel, "100%");
        panel.addStyleName("main-panel");
        RootPanel.get("PhenoFam").add(panel);
        dataProvider.executeGetSupportedOrganisms(new PhenoFamDataProvider.SupportedOrganismsDataAcceptor() {

            public void onSuccess(Set<String> supportedOrganisms) {
                idTypeListBox.clear();
                organismListBox.clear();
                for (String vip : VIP_SPECIES) {
                    if (supportedOrganisms.contains(vip)) {
                        organismListBox.addItem(vip);
                        supportedOrganisms.remove(vip);
                    }
                }
                for (String organism : supportedOrganisms) organismListBox.addItem(organism);
                updateSupportedIds();
            }

            public void onFailure(Throwable caught) {
            }
        });
        organismListBox.addChangeListener(new ChangeListener() {

            public void onChange(Widget sender) {
                updateSupportedIds();
            }
        });
        dataRefPanel.update();
    }

    private void updateSupportedIds() {
        idTypeListBox.clear();
        int organismIdx = organismListBox.getSelectedIndex();
        if (organismIdx == -1) return;
        dataProvider.executeGetSupportedIdTypes(organismListBox.getValue(organismIdx), new PhenoFamDataProvider.SupportedIdTypesDataAcceptor() {

            public void onSuccess(Set<DataType> supportedDataTypes) {
                idTypeListBox.clear();
                for (DataType type : supportedDataTypes) idTypeListBox.addItem(type.getDbName());
            }

            public void onFailure(Throwable caught) {
            }
        });
    }
}
