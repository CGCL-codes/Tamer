package org.remus.infomngmnt.contact.ui.misc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.remus.InfomngmntPackage;
import org.eclipse.remus.common.ui.UIUtil;
import org.eclipse.remus.common.ui.image.ResourceManager;
import org.eclipse.remus.ui.databinding.BindingWidgetFactory;
import org.eclipse.remus.ui.databinding.TextBindingWidget;
import org.eclipse.remus.ui.editors.editpage.AbstractInformationFormPage;
import org.eclipse.remus.ui.widgets.DateCombo;
import org.eclipse.remus.ui.widgets.databinding.AdditionalBindingWidgetFactory;
import org.eclipse.remus.ui.widgets.databinding.DatePickerBindingWidget;
import org.eclipse.remus.util.InformationUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.remus.infomngmnt.contact.ContactActivator;
import org.remus.infomngmnt.contact.core.ImageManipulation;
import org.remus.infomngmnt.contact.messages.Messages;

public class EditMiscPage extends AbstractInformationFormPage {

    private FormToolkit toolkit;

    private Text tx_OpenGpg;

    private Text tx_Smime;

    private Text tx_Department;

    private Text tx_NameManager;

    private Text tx_Bureau;

    private Text tx_Assistant;

    private Text tx_Job;

    private Text tx_Title;

    private Text tx_Nickname;

    private Text tx_NamePartner;

    private Label btImage;

    private DateCombo birthDay;

    private DateCombo jubileeDate;

    public EditMiscPage() {
    }

    @Override
    protected void renderPage(final IManagedForm managedForm) {
        this.toolkit = managedForm.getToolkit();
        ScrolledForm form = managedForm.getForm();
        Composite body = form.getBody();
        body.setLayout(new GridLayout());
        this.toolkit.paintBordersFor(body);
        doCreateDetailSection(body);
        doCreateEncryptionSection(body);
        doCreateMiscSection(body);
    }

    private void doCreateMiscSection(final Composite body) {
        final Section section_1 = this.toolkit.createSection(body, ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
        section_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        section_1.setText(Messages.EditMiscPage_CompanyLogo);
        final Composite compositeGeneral = this.toolkit.createComposite(section_1, SWT.NONE);
        final GridLayout gridLayoutAdditional = new GridLayout();
        gridLayoutAdditional.numColumns = 2;
        compositeGeneral.setLayout(gridLayoutAdditional);
        this.toolkit.paintBordersFor(compositeGeneral);
        section_1.setClient(compositeGeneral);
        createGroupImage(compositeGeneral);
    }

    private void createGroupImage(final Composite compositeGeneral) {
        this.btImage = this.toolkit.createLabel(compositeGeneral, null, SWT.NONE);
        GridData gd_Image = new GridData(SWT.CENTER, SWT.BEGINNING, true, true);
        final int sizeX = 128;
        final int sizeY = 128;
        this.btImage.setToolTipText(Messages.EditMiscPage_AddLogo);
        gd_Image.widthHint = sizeX;
        gd_Image.heightHint = sizeY;
        this.btImage.setLayoutData(gd_Image);
        this.btImage.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDoubleClick(final MouseEvent e) {
                ImageManipulation.selectImageFromDialog(getSite().getShell(), getModelObject(), ContactActivator.NODE_NAME_RAWDATA_LOGO, (AdapterFactoryEditingDomain) getEditingDomain(), sizeX, sizeY);
            }
        });
    }

    private void doCreateDetailSection(final Composite body) {
        final Section section_1 = this.toolkit.createSection(body, ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
        section_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        section_1.setText(Messages.EditMiscPage_Details);
        final Composite compositeGeneral = this.toolkit.createComposite(body, SWT.NONE);
        final GridLayout gridLayoutAdditional = new GridLayout();
        gridLayoutAdditional.numColumns = 1;
        compositeGeneral.setLayout(gridLayoutAdditional);
        compositeGeneral.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.toolkit.paintBordersFor(compositeGeneral);
        createGroupGeneral(compositeGeneral);
        createSeparator(compositeGeneral, true, 1);
        createGroupPerson(compositeGeneral);
    }

    private void doCreateEncryptionSection(final Composite body) {
        final Section section_1 = this.toolkit.createSection(body, ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
        section_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        section_1.setText(Messages.EditMiscPage_Encryption);
        final Composite compositeEncryption = this.toolkit.createComposite(section_1, SWT.NONE);
        final GridLayout gridLayoutEncryption = new GridLayout();
        gridLayoutEncryption.numColumns = 2;
        compositeEncryption.setLayout(gridLayoutEncryption);
        this.toolkit.paintBordersFor(compositeEncryption);
        section_1.setClient(compositeEncryption);
        createGroupPreferredKeys(compositeEncryption);
    }

    private void createGroupPreferredKeys(final Composite compositeEncryption) {
        GridData gd_text = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        this.toolkit.createLabel(compositeEncryption, Messages.EditMiscPage_OpenPGP);
        this.tx_OpenGpg = this.toolkit.createText(compositeEncryption, null, SWT.BORDER | SWT.FILL);
        this.tx_OpenGpg.setLayoutData(gd_text);
        this.toolkit.createLabel(compositeEncryption, Messages.EditMiscPage_SMIME);
        this.tx_Smime = this.toolkit.createText(compositeEncryption, null, SWT.BORDER);
        this.tx_Smime.setLayoutData(gd_text);
        addControl(this.tx_OpenGpg);
        addControl(this.tx_Smime);
    }

    private void createGroupPerson(final Composite compositeGeneral) {
        final Composite group_Person = this.toolkit.createComposite(compositeGeneral, SWT.NONE);
        group_Person.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group_Person.setLayout(UIUtil.createMarginLessGridLayout(4));
        this.toolkit.adapt(group_Person);
        this.toolkit.createLabel(group_Person, Messages.EditMiscPage_Nickname);
        this.tx_Nickname = this.toolkit.createText(group_Person, null, SWT.BORDER);
        this.tx_Nickname.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        this.toolkit.createLabel(group_Person, Messages.EditMiscPage_Birthday);
        Composite birthDayParent = this.toolkit.createComposite(group_Person);
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 2;
        layout.marginWidth = 1;
        layout.verticalSpacing = 2;
        layout.horizontalSpacing = 2;
        birthDayParent.setLayout(layout);
        this.birthDay = new DateCombo(birthDayParent, SWT.FLAT);
        GridDataFactory.fillDefaults().hint(120, SWT.DEFAULT).grab(true, false).applyTo(this.birthDay);
        this.birthDay.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
        this.toolkit.adapt(this.birthDay, false, false);
        this.toolkit.paintBordersFor(birthDayParent);
        GridData dueDateLayoutData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        birthDayParent.setLayoutData(dueDateLayoutData);
        this.toolkit.adapt(this.birthDay);
        this.toolkit.createLabel(group_Person, Messages.EditMiscPage_Partner);
        this.tx_NamePartner = this.toolkit.createText(group_Person, null, SWT.BORDER);
        this.tx_NamePartner.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        this.toolkit.createLabel(group_Person, Messages.EditMiscPage_Jubilee);
        Composite jubileeParent = this.toolkit.createComposite(group_Person);
        layout = new GridLayout(2, false);
        layout.marginHeight = 2;
        layout.marginWidth = 1;
        layout.verticalSpacing = 2;
        layout.horizontalSpacing = 2;
        jubileeParent.setLayout(layout);
        this.jubileeDate = new DateCombo(jubileeParent, SWT.FLAT);
        GridDataFactory.fillDefaults().hint(120, SWT.DEFAULT).grab(true, false).applyTo(this.jubileeDate);
        this.jubileeDate.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
        this.toolkit.adapt(this.jubileeDate, false, false);
        this.toolkit.paintBordersFor(jubileeParent);
        GridData jubileeDateLayoutData = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        jubileeParent.setLayoutData(jubileeDateLayoutData);
        this.toolkit.adapt(this.jubileeDate);
    }

    private void createGroupGeneral(final Composite compositeGeneral) {
        final Composite group_General = this.toolkit.createComposite(compositeGeneral, SWT.NONE);
        group_General.setLayout(UIUtil.createMarginLessGridLayout(4));
        group_General.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.toolkit.adapt(group_General);
        this.toolkit.createLabel(group_General, Messages.EditMiscPage_Department);
        this.tx_Department = this.toolkit.createText(group_General, null, SWT.BORDER);
        this.tx_Department.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        this.toolkit.createLabel(group_General, Messages.EditMiscPage_NameOfManager);
        this.tx_NameManager = this.toolkit.createText(group_General, null, SWT.BORDER);
        this.tx_NameManager.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        this.toolkit.createLabel(group_General, Messages.EditMiscPage_Bureau);
        this.tx_Bureau = this.toolkit.createText(group_General, null, SWT.BORDER);
        this.tx_Bureau.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        this.toolkit.createLabel(group_General, Messages.EditMiscPage_NameOfAssistant);
        this.tx_Assistant = this.toolkit.createText(group_General, null, SWT.BORDER);
        this.tx_Assistant.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        this.toolkit.createLabel(group_General, Messages.EditMiscPage_Job);
        this.tx_Job = this.toolkit.createText(group_General, null, SWT.BORDER);
        this.tx_Job.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        this.toolkit.createLabel(group_General, Messages.EditMiscPage_Title);
        this.tx_Title = this.toolkit.createText(group_General, null, SWT.BORDER);
        this.tx_Title.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
    }

    public void createSeparator(final Composite compositeGeneral, final boolean isHorizontal, final int span) {
        final Label lb_Separator = new Label(compositeGeneral, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData gd_text = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        if (isHorizontal) {
            gd_text.horizontalSpan = span;
        } else {
            gd_text.verticalSpan = span;
        }
        lb_Separator.setLayoutData(gd_text);
    }

    protected void setBytesToImage(final byte[] value) {
        if (value == null) {
            this.btImage.setImage(ResourceManager.getPluginImage(ContactActivator.getDefault(), "icons/iconexperience/factory.png"));
        } else {
            ByteArrayInputStream bais = new ByteArrayInputStream(value);
            ImageData imageData = new ImageData(bais);
            Image image = new Image(null, imageData);
            this.btImage.setImage(image);
            try {
                bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void bindValuesToUi() {
        super.bindValuesToUi();
        setBytesToImage(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_NAME_RAWDATA_LOGO).getBinaryValue());
        IObservableValue observeValue = EMFObservables.observeValue(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_NAME_RAWDATA_LOGO), InfomngmntPackage.Literals.INFORMATION_UNIT__BINARY_VALUE);
        observeValue.addValueChangeListener(new IValueChangeListener() {

            public void handleValueChange(final ValueChangeEvent event) {
                byte[] value = (byte[]) event.getObservableValue().getValue();
                setBytesToImage(value);
            }
        });
        TextBindingWidget createTextBindingWidget0 = BindingWidgetFactory.createTextBinding(this.tx_Smime, this);
        createTextBindingWidget0.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_MISC_MIME), InfomngmntPackage.Literals.INFORMATION_UNIT__STRING_VALUE);
        TextBindingWidget createTextBindingWidget2 = BindingWidgetFactory.createTextBinding(this.tx_OpenGpg, this);
        createTextBindingWidget2.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_MISC_PUBKEY), InfomngmntPackage.Literals.INFORMATION_UNIT__STRING_VALUE);
        TextBindingWidget createTextBindingWidget1 = BindingWidgetFactory.createTextBinding(this.tx_Assistant, this);
        createTextBindingWidget1.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_DETAILS_NAME_ASSISTANT), InfomngmntPackage.Literals.INFORMATION_UNIT__STRING_VALUE);
        TextBindingWidget createTextBindingWidget3 = BindingWidgetFactory.createTextBinding(this.tx_Bureau, this);
        createTextBindingWidget3.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_DETAILS_BUREAU), InfomngmntPackage.Literals.INFORMATION_UNIT__STRING_VALUE);
        TextBindingWidget createTextBindingWidget4 = BindingWidgetFactory.createTextBinding(this.tx_Department, this);
        createTextBindingWidget4.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_DETAILS_DEPARTMENT), InfomngmntPackage.Literals.INFORMATION_UNIT__STRING_VALUE);
        TextBindingWidget createTextBindingWidget6 = BindingWidgetFactory.createTextBinding(this.tx_NameManager, this);
        createTextBindingWidget6.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_DETAILS_NAME_MANAGER), InfomngmntPackage.Literals.INFORMATION_UNIT__STRING_VALUE);
        TextBindingWidget createTextBindingWidget7 = BindingWidgetFactory.createTextBinding(this.tx_NamePartner, this);
        createTextBindingWidget7.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_DETAILS_NAME_PARTNER), InfomngmntPackage.Literals.INFORMATION_UNIT__STRING_VALUE);
        TextBindingWidget createTextBindingWidget8 = BindingWidgetFactory.createTextBinding(this.tx_Nickname, this);
        createTextBindingWidget8.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_DETAILS_NAME_NICK), InfomngmntPackage.Literals.INFORMATION_UNIT__STRING_VALUE);
        TextBindingWidget createTextBindingWidget10 = BindingWidgetFactory.createTextBinding(this.tx_Job, this);
        createTextBindingWidget10.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_DETAILS_JOB), InfomngmntPackage.Literals.INFORMATION_UNIT__STRING_VALUE);
        TextBindingWidget createTextBindingWidget11 = BindingWidgetFactory.createTextBinding(this.tx_Title, this);
        createTextBindingWidget11.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_DETAILS_TITLE), InfomngmntPackage.Literals.INFORMATION_UNIT__STRING_VALUE);
        DatePickerBindingWidget dateComboBinding = AdditionalBindingWidgetFactory.createDateComboBinding(this.birthDay, this);
        dateComboBinding.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_DETAILS_BIRTHDAY), InfomngmntPackage.Literals.INFORMATION_UNIT__DATE_VALUE);
        DatePickerBindingWidget dateComboBinding2 = AdditionalBindingWidgetFactory.createDateComboBinding(this.jubileeDate, this);
        dateComboBinding2.bindModel(InformationUtil.getChildByType(getModelObject(), ContactActivator.NODE_DETAILS_JUBILEE), InfomngmntPackage.Literals.INFORMATION_UNIT__DATE_VALUE);
    }
}
