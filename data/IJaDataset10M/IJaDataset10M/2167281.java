package br.com.promove.view.form;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import br.com.promove.entity.ResponsabilidadeAvaria;
import br.com.promove.exception.PromoveException;
import br.com.promove.service.ImportacaoService;
import br.com.promove.service.ServiceFactory;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class ImportVeiculoImportadoForm extends BaseForm {

    private VerticalLayout layout = new VerticalLayout();

    private Label state = new Label();

    private Label fileName = new Label();

    private Label textualProgress = new Label();

    private ComboBox cmbTipo;

    private PopupDateField txtData;

    private Button cancelProcessing;

    private ByteArrayOutputStream file = new ByteArrayOutputStream();

    private ImportacaoService importService;

    private ProgressIndicator pi = new ProgressIndicator();

    private Upload upload = new Upload(null, new ImportImportadolUploader(this));

    public ImportVeiculoImportadoForm() {
        buildLayout();
        importService = ServiceFactory.getService(ImportacaoService.class);
    }

    private void buildLayout() {
        Item i;
        setWriteThrough(false);
        setImmediate(true);
        layout.setSpacing(true);
        Label tit = new Label("<h3>Importar Veículos</h3><br><i><b>Nacionais:</b> Chassi; Modelo<br><b>Importados:</b> Chassi; Modelo; Navio; Valor</i><br><br>");
        tit.setContentMode(Label.CONTENT_XHTML);
        layout.addComponent(tit);
        txtData = new PopupDateField("Data para Registro");
        txtData.setLocale(new Locale("pt", "BR"));
        txtData.setResolution(DateField.RESOLUTION_DAY);
        txtData.setValue(new Date());
        txtData.setRequired(true);
        txtData.setRequiredError("Informe a data!");
        cmbTipo = new ComboBox("Tipo de Veículo");
        cmbTipo.addContainerProperty("label", String.class, null);
        i = cmbTipo.addItem(1);
        i.getItemProperty("label").setValue("Nacional");
        i = cmbTipo.addItem(2);
        i.getItemProperty("label").setValue("Importado");
        cmbTipo.setValue(2);
        cmbTipo.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
        cmbTipo.setImmediate(true);
        cmbTipo.setNullSelectionAllowed(false);
        cmbTipo.setItemCaptionPropertyId("label");
        addField("cmbTipo", cmbTipo);
        addField("txtData", txtData);
        layout.addComponent(this);
        upload.setImmediate(true);
        upload.setButtonCaption("Selecione...");
        layout.addComponent(upload);
        cancelProcessing = new Button("Cancelar");
        cancelProcessing.addListener(new Button.ClickListener() {

            public void buttonClick(ClickEvent event) {
                upload.interruptUpload();
            }
        });
        cancelProcessing.setVisible(false);
        cancelProcessing.setStyleName("small");
        Panel p = createStatusPanel(cancelProcessing);
        layout.addComponent(p);
        upload.addListener(new UploadStartListener(this));
        upload.addListener(new UploadProgressListener(this));
        upload.addListener(new UploadSucessListener(this));
        upload.addListener(new UploadFailedListener(this));
    }

    private Panel createStatusPanel(final Button cancelProcessing) {
        Panel p = new Panel("Status");
        p.setSizeUndefined();
        FormLayout l = new FormLayout();
        l.setMargin(true);
        p.setContent(l);
        HorizontalLayout stateLayout = new HorizontalLayout();
        stateLayout.setSpacing(true);
        stateLayout.addComponent(state);
        stateLayout.addComponent(cancelProcessing);
        stateLayout.setCaption("Status atual:");
        state.setValue("Aguardando.");
        l.addComponent(stateLayout);
        fileName.setCaption("Nome do arquivo:");
        l.addComponent(fileName);
        pi.setCaption("Progresso:");
        pi.setVisible(false);
        l.addComponent(pi);
        textualProgress.setVisible(false);
        l.addComponent(textualProgress);
        return p;
    }

    public VerticalLayout getLayout() {
        return layout;
    }

    public void setLayout(VerticalLayout layout) {
        this.layout = layout;
    }

    class ImportImportadolUploader implements Receiver {

        private String fileName;

        private String mtype;

        private ImportVeiculoImportadoForm view;

        public ImportImportadolUploader(ImportVeiculoImportadoForm view) {
            this.view = view;
        }

        @Override
        public OutputStream receiveUpload(String filename, String MIMEType) {
            try {
                Date data = txtData.getValue() != null ? (Date) txtData.getValue() : null;
                if (data == null) {
                    throw new PromoveException("Informe a data para registro");
                }
                if (!filename.endsWith("csv")) {
                    throw new PromoveException("Formato de arquivo não reconhecido.");
                }
            } catch (Exception e) {
                showErrorMessage(view.getLayout(), e.getMessage());
                upload.interruptUpload();
            }
            this.fileName = filename;
            this.mtype = MIMEType;
            return file;
        }
    }

    class UploadStartListener implements StartedListener {

        private ImportVeiculoImportadoForm view;

        public UploadStartListener(ImportVeiculoImportadoForm view) {
            this.view = view;
        }

        @Override
        public void uploadStarted(StartedEvent event) {
            pi.setValue(0f);
            pi.setVisible(true);
            pi.setPollingInterval(500);
            textualProgress.setVisible(true);
            state.setValue("Enviando...");
            fileName.setValue(event.getFilename());
            cancelProcessing.setVisible(true);
        }
    }

    class UploadProgressListener implements ProgressListener {

        private ImportVeiculoImportadoForm view;

        public UploadProgressListener(ImportVeiculoImportadoForm view) {
            this.view = view;
        }

        @Override
        public void updateProgress(long readBytes, long contentLength) {
            pi.setValue(new Float(readBytes / (float) contentLength));
            textualProgress.setValue("Recebido " + readBytes + " bytes de " + contentLength);
        }
    }

    class UploadSucessListener implements SucceededListener {

        private ImportVeiculoImportadoForm view;

        public UploadSucessListener(ImportVeiculoImportadoForm view) {
            this.view = view;
        }

        @Override
        public void uploadSucceeded(SucceededEvent event) {
            Integer tipo = (Integer) cmbTipo.getValue();
            try {
                Date data = txtData.getValue() != null ? (Date) txtData.getValue() : null;
                cancelProcessing.setVisible(false);
                state.setValue("Recebido.");
                pi.setValue(100f);
                importService.importVeiculosImportados(new String(file.toByteArray()), data, tipo);
                showSuccessMessage(view.getLayout(), "Arquivo importado com sucesso");
            } catch (PromoveException pe) {
                pe.printStackTrace();
                showErrorMessage(view, pe.getMessage());
            }
        }
    }

    class UploadFailedListener implements FailedListener {

        private ImportVeiculoImportadoForm view;

        public UploadFailedListener(ImportVeiculoImportadoForm view) {
            this.view = view;
        }

        @Override
        public void uploadFailed(FailedEvent event) {
            pi.setValue(0f);
            pi.setVisible(false);
            textualProgress.setVisible(false);
            state.setValue("Falhou.");
            fileName.setValue(event.getFilename());
            cancelProcessing.setVisible(false);
        }
    }
}
