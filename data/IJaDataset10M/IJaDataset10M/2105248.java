package org.remus.infomngmnt.pdf.ui;

import java.awt.Dimension;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import org.apache.jempbox.xmp.XMPMetadata;
import org.apache.jempbox.xmp.XMPSchemaBasic;
import org.apache.jempbox.xmp.XMPSchemaDublinCore;
import org.apache.jempbox.xmp.XMPSchemaPDF;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.remus.InformationUnit;
import org.eclipse.remus.core.model.InformationStructureRead;
import org.eclipse.remus.ui.databinding.BindingWidgetFactory;
import org.eclipse.remus.ui.databinding.ComboBindingWidget;
import org.eclipse.remus.ui.databinding.SpinnerSliderBindingWidget;
import org.eclipse.remus.ui.editors.editpage.AbstractInformationFormPage;
import org.eclipse.remus.ui.progress.CancelableRunnable;
import org.eclipse.remus.util.InformationUtil;
import org.eclipse.remus.util.StatusCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.remus.infomngmnt.pdf.Activator;
import org.remus.infomngmnt.pdf.extension.IPdf2ImageRenderer;
import org.remus.infomngmnt.pdf.extension.IPdfImageRenderer;
import org.remus.infomngmnt.pdf.messages.Messages;
import org.remus.infomngmnt.pdf.preferences.PreferenceInitializer;
import org.remus.infomngmnt.pdf.service.IPDF2ImageExtensionService;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class PdfMetadaEditPage extends AbstractInformationFormPage {

    private Spinner widthSpinner;

    private Combo comboViewer;

    private Text authorText;

    private Text titleText;

    private Text creatorText;

    private Text producerText;

    /**
	 * 
	 */
    public PdfMetadaEditPage() {
    }

    @Override
    protected void renderPage(final IManagedForm managedForm) {
        FormToolkit toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        Composite body = form.getBody();
        body.setLayout(new GridLayout());
        toolkit.paintBordersFor(body);
        doCreateDocumentInformationSection(body, toolkit);
        doCreateGeneralSection(body, toolkit);
        doCreateSemanticSection(body, toolkit);
        form.reflow(true);
    }

    private void doCreateDocumentInformationSection(final Composite body, final FormToolkit toolkit) {
        final Section generalSection = toolkit.createSection(body, ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
        final GridData gd_generalSection = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        generalSection.setLayoutData(gd_generalSection);
        generalSection.setText(Messages.PdfMetadaEditPage_DocumentInformation);
        final Composite client = toolkit.createComposite(generalSection, SWT.NONE);
        client.setLayout(new GridLayout(2, false));
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        client.setLayoutData(gridData);
        generalSection.setClient(client);
        Label authorLabel = toolkit.createLabel(client, Messages.PdfMetadaEditPage_Author);
        authorLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        this.authorText = toolkit.createText(client, "", SWT.BORDER);
        this.authorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        Label titleLabel = toolkit.createLabel(client, Messages.PdfMetadaEditPage_Title);
        titleLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        this.titleText = toolkit.createText(client, "", SWT.BORDER);
        this.titleText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        Label creatolrLabelLabel = toolkit.createLabel(client, Messages.PdfMetadaEditPage_Creator);
        creatolrLabelLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        this.creatorText = toolkit.createText(client, "", SWT.BORDER);
        this.creatorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        Label producerLabel = toolkit.createLabel(client, Messages.PdfMetadaEditPage_Producer);
        producerLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        this.producerText = toolkit.createText(client, "", SWT.BORDER);
        this.producerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        Button createButton = toolkit.createButton(client, Messages.PdfMetadaEditPage_WirteMetadata, SWT.PUSH);
        createButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(final Event event) {
                CancelableRunnable runnable = new CancelableRunnable() {

                    @Override
                    protected IStatus runCancelableRunnable(final IProgressMonitor monitor) {
                        IFile binaryReferenceFile = InformationUtil.getBinaryReferenceFile(getModelObject());
                        PDDocument document = null;
                        monitor.beginTask(Messages.PdfMetadaEditPage_WriteInformation, IProgressMonitor.UNKNOWN);
                        try {
                            document = PDDocument.load(binaryReferenceFile.getLocationURI().toURL());
                            PDDocumentCatalog catalog = document.getDocumentCatalog();
                            PDDocumentInformation info = document.getDocumentInformation();
                            XMPMetadata metadata = new XMPMetadata();
                            XMPSchemaPDF pdfSchema = metadata.addPDFSchema();
                            pdfSchema.setKeywords(info.getKeywords());
                            pdfSchema.setProducer(info.getProducer());
                            XMPSchemaBasic basicSchema = metadata.addBasicSchema();
                            basicSchema.setModifyDate(info.getModificationDate());
                            basicSchema.setCreateDate(info.getCreationDate());
                            basicSchema.setCreatorTool(info.getCreator());
                            basicSchema.setMetadataDate(new GregorianCalendar());
                            XMPSchemaDublinCore dcSchema = metadata.addDublinCoreSchema();
                            dcSchema.setTitle(info.getTitle());
                            dcSchema.addCreator(Messages.PdfMetadaEditPage_PDFBox);
                            dcSchema.setDescription(info.getSubject());
                            PDMetadata metadataStream = new PDMetadata(document);
                            metadataStream.importXMPMetadata(metadata);
                            catalog.setMetadata(metadataStream);
                            document.save(binaryReferenceFile.getLocation().toOSString());
                        } catch (Exception e) {
                            return StatusCreator.newStatus(Messages.PdfMetadaEditPage_ErrorWritingPDF, e);
                        } finally {
                            if (document != null) try {
                                document.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return Status.OK_STATUS;
                    }
                };
                ProgressMonitorDialog dialog = new ProgressMonitorDialog(getSite().getShell());
                try {
                    dialog.run(true, true, runnable);
                } catch (InvocationTargetException e) {
                    ErrorDialog.openError(getSite().getShell(), Messages.PdfMetadaEditPage_Error, e.getCause().getMessage(), e.getCause() instanceof CoreException ? ((CoreException) e.getCause()).getStatus() : null);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void doCreateGeneralSection(final Composite body, final FormToolkit toolkit) {
        final Section generalSection = toolkit.createSection(body, ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
        final GridData gd_generalSection = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        generalSection.setLayoutData(gd_generalSection);
        generalSection.setText(Messages.PdfMetadaEditPage_RenderingOptions);
        final Composite client = toolkit.createComposite(generalSection, SWT.NONE);
        client.setLayout(new GridLayout(3, false));
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        client.setLayoutData(gridData);
        generalSection.setClient(client);
        Label subjectLabel = toolkit.createLabel(client, Messages.PdfMetadaEditPage_SliderWidth);
        subjectLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        this.widthSpinner = new Spinner(client, SWT.BORDER);
        toolkit.adapt(this.widthSpinner);
        this.widthSpinner.setMinimum(100);
        this.widthSpinner.setIncrement(10);
        this.widthSpinner.setMaximum(3000);
        this.widthSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        Button createButton = toolkit.createButton(client, Messages.PdfMetadaEditPage_CalculateWidth, SWT.PUSH);
        createButton.addListener(SWT.Selection, new Listener() {

            public void handleEvent(final Event event) {
                IPDF2ImageExtensionService service = Activator.getDefault().getServiceTracker().getService(IPDF2ImageExtensionService.class);
                InformationStructureRead read = InformationStructureRead.newSession(getModelObject());
                String renderer = (String) read.getValueByNodeId(Activator.RENDERER);
                IPdfImageRenderer rendererById = null;
                if (renderer != null) {
                    rendererById = service.getRendererById(renderer);
                }
                if (rendererById == null) {
                    String string = Activator.getDefault().getPreferenceStore().getString(PreferenceInitializer.DEFAULT_RENDERER);
                    rendererById = service.getRendererById(string);
                }
                if (rendererById == null) {
                    rendererById = service.getAllRender()[0];
                }
                IFile binaryReferenceFile = InformationUtil.getBinaryReferenceFile(getModelObject());
                Dimension convert = rendererById.getRenderer().firstSlid(binaryReferenceFile);
                InformationUnit childByNodeId = read.getChildByNodeId(Activator.SLIDER_WIDTH);
                Command command = SetCommand.create(getEditingDomain(), childByNodeId, read.getFeatureByNodeId(Activator.SLIDER_WIDTH), ((Integer) convert.width).longValue());
                getEditingDomain().getCommandStack().execute(command);
            }
        });
        Label rendererLabel = toolkit.createLabel(client, Messages.PdfMetadaEditPage_ImageRenderer);
        rendererLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        this.comboViewer = new Combo(client, SWT.READ_ONLY);
        this.comboViewer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    }

    @Override
    public void bindValuesToUi() {
        InformationStructureRead read = InformationStructureRead.newSession(getModelObject());
        UpdateValueStrategy setConverter = new EMFUpdateValueStrategy().setConverter(new IConverter() {

            public Object getToType() {
                return null;
            }

            public Object getFromType() {
                return null;
            }

            public Object convert(final Object fromObject) {
                if (fromObject instanceof Long) {
                    return ((Long) fromObject).intValue();
                } else if (fromObject instanceof Integer) {
                    return ((Integer) fromObject).longValue();
                }
                return fromObject;
            }
        });
        SpinnerSliderBindingWidget createSpinner = BindingWidgetFactory.createSpinner(this.widthSpinner, getDatabindingContext(), getEditingDomain());
        bind(createSpinner, Activator.SLIDER_WIDTH, setConverter, setConverter);
        IPDF2ImageExtensionService imageExtensionService = Activator.getDefault().getServiceTracker().getService(IPDF2ImageExtensionService.class);
        final ArrayList<IPdfImageRenderer> arrayList = new ArrayList<IPdfImageRenderer>(Arrays.asList(imageExtensionService.getAllRender()));
        final IPdfImageRenderer defaultRenderer = new IPdfImageRenderer() {

            public IPdf2ImageRenderer getRenderer() {
                return null;
            }

            public String getName() {
                return Messages.PdfMetadaEditPage_GlobalPreference;
            }

            public String getId() {
                return null;
            }
        };
        arrayList.add(defaultRenderer);
        ComboBindingWidget createComboBinding = BindingWidgetFactory.createComboBinding(this.comboViewer, this);
        createComboBinding.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(final Object element) {
                return ((IPdfImageRenderer) element).getName();
            }
        });
        createComboBinding.setInput(arrayList);
        UpdateValueStrategy target2Model = new EMFUpdateValueStrategy().setConverter(new IConverter() {

            public Object getToType() {
                return null;
            }

            public Object getFromType() {
                return null;
            }

            public Object convert(final Object fromObject) {
                return ((IPdfImageRenderer) fromObject).getId();
            }
        });
        UpdateValueStrategy model2Target = new EMFUpdateValueStrategy().setConverter(new IConverter() {

            public Object getToType() {
                return null;
            }

            public Object getFromType() {
                return null;
            }

            public Object convert(final Object fromObject) {
                for (IPdfImageRenderer iPdfImageRenderer : arrayList) {
                    if (iPdfImageRenderer.getId() != null && iPdfImageRenderer.getId().equals(fromObject)) {
                        return iPdfImageRenderer;
                    }
                }
                return defaultRenderer;
            }
        });
        createComboBinding.bindModel(read.getChildByNodeId(Activator.RENDERER), read.getFeatureByNodeId(Activator.RENDERER), target2Model, model2Target);
        bind(BindingWidgetFactory.createTextBinding(this.authorText, this), Activator.AUTHOR);
        bind(BindingWidgetFactory.createTextBinding(this.titleText, this), Activator.TITLE);
        bind(BindingWidgetFactory.createTextBinding(this.creatorText, this), Activator.CREATOR);
        bind(BindingWidgetFactory.createTextBinding(this.producerText, this), Activator.PRODUCER);
        super.bindValuesToUi();
    }
}
