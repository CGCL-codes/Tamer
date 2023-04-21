package org.dcm4chee.web.war.folder;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.ejb.EJBException;
import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.PageMap;
import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.CloseButtonCallback;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.security.hive.authentication.DefaultSubject;
import org.apache.wicket.security.hive.authorization.Principal;
import org.apache.wicket.security.hive.authorization.SimplePrincipal;
import org.apache.wicket.security.swarm.strategies.SwarmStrategy;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.time.Duration;
import org.dcm4che2.audit.message.AuditEvent;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.dcm4che2.data.DateRange;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.PersonName;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.UID;
import org.dcm4che2.data.VR;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4chee.archive.common.PrivateTag;
import org.dcm4chee.archive.conf.AttributeFilter;
import org.dcm4chee.archive.entity.Patient;
import org.dcm4chee.archive.entity.Study;
import org.dcm4chee.archive.entity.StudyPermission;
import org.dcm4chee.archive.util.JNDIUtils;
import org.dcm4chee.icons.ImageManager;
import org.dcm4chee.icons.behaviours.ImageSizeBehaviour;
import org.dcm4chee.web.common.behaviours.CheckOneDayBehaviour;
import org.dcm4chee.web.common.behaviours.MarkInvalidBehaviour;
import org.dcm4chee.web.common.behaviours.SelectableTableRowBehaviour;
import org.dcm4chee.web.common.behaviours.TooltipBehaviour;
import org.dcm4chee.web.common.delegate.BaseCfgDelegate;
import org.dcm4chee.web.common.exceptions.WicketExceptionWithMsgKey;
import org.dcm4chee.web.common.markup.BaseForm;
import org.dcm4chee.web.common.markup.DateTimeLabel;
import org.dcm4chee.web.common.markup.ModalWindowLink;
import org.dcm4chee.web.common.markup.PopupLink;
import org.dcm4chee.web.common.markup.SimpleDateTimeField;
import org.dcm4chee.web.common.markup.ModalWindowLink.DisableDefaultConfirmBehavior;
import org.dcm4chee.web.common.markup.modal.ConfirmationWindow;
import org.dcm4chee.web.common.markup.modal.MessageWindow;
import org.dcm4chee.web.common.secure.SecureSession;
import org.dcm4chee.web.common.secure.SecurityBehavior;
import org.dcm4chee.web.common.util.Auditlog;
import org.dcm4chee.web.common.util.FileUtils;
import org.dcm4chee.web.common.validators.UIDValidator;
import org.dcm4chee.web.common.webview.link.WebviewerLinkProvider;
import org.dcm4chee.web.dao.folder.StudyListFilter;
import org.dcm4chee.web.dao.folder.StudyListLocal;
import org.dcm4chee.web.dao.util.QueryUtil;
import org.dcm4chee.web.war.AuthenticatedWebSession;
import org.dcm4chee.web.war.StudyPermissionHelper;
import org.dcm4chee.web.war.StudyPermissionHelper.StudyPermissionRight;
import org.dcm4chee.web.common.ajax.MaskingAjaxCallBehavior;
import org.dcm4chee.web.war.common.EditDicomObjectPanel;
import org.dcm4chee.web.war.common.IndicatingAjaxFormSubmitBehavior;
import org.dcm4chee.web.war.common.SimpleEditDicomObjectPanel;
import org.dcm4chee.web.war.common.UIDFieldBehavior;
import org.dcm4chee.web.war.common.model.AbstractDicomModel;
import org.dcm4chee.web.war.common.model.AbstractEditableDicomModel;
import org.dcm4chee.web.war.config.delegate.WebCfgDelegate;
import org.dcm4chee.web.war.folder.delegate.ContentEditDelegate;
import org.dcm4chee.web.war.folder.delegate.MppsEmulateDelegate;
import org.dcm4chee.web.war.folder.delegate.TarRetrieveDelegate;
import org.dcm4chee.web.war.folder.model.FileModel;
import org.dcm4chee.web.war.folder.model.InstanceModel;
import org.dcm4chee.web.war.folder.model.PPSModel;
import org.dcm4chee.web.war.folder.model.PatientModel;
import org.dcm4chee.web.war.folder.model.SeriesModel;
import org.dcm4chee.web.war.folder.model.StudyModel;
import org.dcm4chee.web.war.folder.studypermissions.StudyPermissionsPage;
import org.dcm4chee.web.war.folder.webviewer.Webviewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudyListPage extends Panel {

    private static final ResourceReference CSS = new CompressedResourceReference(StudyListPage.class, "folder-style.css");

    private ModalWindow modalWindow;

    private IModel<Integer> pagesize = new IModel<Integer>() {

        private static final long serialVersionUID = 1L;

        private int pagesize = WebCfgDelegate.getInstance().getDefaultFolderPagesize();

        public Integer getObject() {
            return pagesize;
        }

        public void setObject(Integer object) {
            if (object != null) pagesize = object;
        }

        public void detach() {
        }
    };

    private static final String MODULE_NAME = "folder";

    private static final long serialVersionUID = 1L;

    private static Logger log = LoggerFactory.getLogger(StudyListPage.class);

    private static String tooOldAuditMessageText = "Requested editing of an object that should not be edited anymore because of editing time limit. " + "The restriction was overriden because the user has assigned the web right 'swarm.principal.IgnoreEditTimeLimit'.";

    private ViewPort viewport = ((AuthenticatedWebSession) AuthenticatedWebSession.get()).getFolderViewPort();

    private StudyListHeader header;

    private SelectedEntities selected = new SelectedEntities();

    private IModel<Boolean> latestStudyFirst = new AbstractReadOnlyModel<Boolean>() {

        private static final long serialVersionUID = 1L;

        @Override
        public Boolean getObject() {
            return viewport.getFilter().isLatestStudiesFirst();
        }
    };

    private boolean showSearch = true;

    private boolean notSearched = true;

    private BaseForm form;

    private MessageWindow msgWin = new MessageWindow("msgWin");

    private Mpps2MwlLinkPage mpps2MwlLinkWindow = new Mpps2MwlLinkPage("linkPage");

    private ConfirmationWindow<PPSModel> confirmLinkMpps;

    private ConfirmationWindow<PPSModel> confirmUnlinkMpps;

    private ConfirmationWindow<PPSModel> confirmEmulateMpps;

    private ConfirmationWindow<AbstractEditableDicomModel> confirmEdit;

    private ImageSelectionWindow imageSelectionWindow = new ImageSelectionWindow("imgSelection");

    private ModalWindow wadoImageWindow = new ModalWindow("wadoImageWindow");

    private WebviewerLinkProvider[] webviewerLinkProviders;

    private List<WebMarkupContainer> searchTableComponents = new ArrayList<WebMarkupContainer>();

    StudyListLocal dao = (StudyListLocal) JNDIUtils.lookup(StudyListLocal.JNDI_NAME);

    StudyPermissionHelper studyPermissionHelper;

    final MaskingAjaxCallBehavior macb = new MaskingAjaxCallBehavior();

    public StudyListPage(final String id) {
        super(id);
        if (StudyListPage.CSS != null) add(CSSPackageResource.getHeaderContribution(StudyListPage.CSS));
        studyPermissionHelper = StudyPermissionHelper.get();
        add(macb);
        add(modalWindow = new ModalWindow("modal-window"));
        modalWindow.setWindowClosedCallback(new WindowClosedCallback() {

            private static final long serialVersionUID = 1L;

            public void onClose(AjaxRequestTarget target) {
                getPage().setOutputMarkupId(true);
                target.addComponent(getPage());
            }
        });
        initWebviewerLinkProvider();
        final StudyListFilter filter = viewport.getFilter();
        add(form = new BaseForm("form", new CompoundPropertyModel<Object>(filter)));
        form.setResourceIdPrefix("folder.");
        form.add(new AjaxFallbackLink<Object>("searchToggle") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                showSearch = !showSearch;
                for (WebMarkupContainer wmc : searchTableComponents) wmc.setVisible(showSearch);
                target.addComponent(form);
            }
        }.add((new Image("searchToggleImg", new AbstractReadOnlyModel<ResourceReference>() {

            private static final long serialVersionUID = 1L;

            @Override
            public ResourceReference getObject() {
                return showSearch ? ImageManager.IMAGE_COMMON_COLLAPSE : ImageManager.IMAGE_COMMON_EXPAND;
            }
        }).add(new TooltipBehaviour("folder.", "searchToggleImg", new AbstractReadOnlyModel<Boolean>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Boolean getObject() {
                return showSearch;
            }
        }))).add(new ImageSizeBehaviour())));
        addQueryFields(filter, form);
        addQueryOptions(form);
        addNavigation(form);
        addActions(form);
        header = new StudyListHeader("thead", form);
        form.add(header);
        form.add(new PatientListView("patients", viewport.getPatients()));
        msgWin.setTitle("");
        add(msgWin);
        Form<Object> form1 = new Form<Object>("modalForm");
        add(form1);
        form1.add(mpps2MwlLinkWindow);
        add(imageSelectionWindow);
        imageSelectionWindow.setWindowClosedCallback(new WindowClosedCallback() {

            private static final long serialVersionUID = 1L;

            public void onClose(AjaxRequestTarget target) {
                if (!imageSelectionWindow.changeSelection()) imageSelectionWindow.undoSelectionChanges(); else if (imageSelectionWindow.isSelectionChanged()) target.addComponent(form);
            }
        });
        imageSelectionWindow.add(new SecurityBehavior(getModuleName() + ":imageSelectionWindow"));
        add(wadoImageWindow);
        wadoImageWindow.add(new SecurityBehavior(getModuleName() + ":wadoImageWindow"));
    }

    private void initWebviewerLinkProvider() {
        List<String> names = WebCfgDelegate.getInstance().getWebviewerNameList();
        if (names == null) {
            names = WebCfgDelegate.getInstance().getInstalledWebViewerNameList();
        }
        if (names == null || names.isEmpty()) {
            webviewerLinkProviders = null;
        } else {
            webviewerLinkProviders = new WebviewerLinkProvider[names.size()];
            Map<String, String> baseUrls = WebCfgDelegate.getInstance().getWebviewerBaseUrlMap();
            for (int i = 0; i < webviewerLinkProviders.length; i++) {
                webviewerLinkProviders[i] = new WebviewerLinkProvider(names.get(i));
                webviewerLinkProviders[i].setBaseUrl(baseUrls.get(names.get(i)));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addQueryFields(final StudyListFilter filter, final BaseForm form) {
        final IModel<Boolean> enabledModelPat = new AbstractReadOnlyModel<Boolean>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Boolean getObject() {
                return (!filter.isExtendedQuery() || (QueryUtil.isUniversalMatch(filter.getStudyInstanceUID()) && QueryUtil.isUniversalMatch(filter.getSeriesInstanceUID())));
            }
        };
        final IModel<Boolean> enabledModel = new AbstractReadOnlyModel<Boolean>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Boolean getObject() {
                return !filter.isPatientQuery() && (!filter.isExtendedQuery() || (QueryUtil.isUniversalMatch(filter.getStudyInstanceUID()) && QueryUtil.isUniversalMatch(filter.getSeriesInstanceUID())));
            }
        };
        searchTableComponents.add(form.createAjaxParent("searchLabels"));
        form.addInternalLabel("patientName");
        form.addInternalLabel("patientIDDescr");
        form.addInternalLabel("studyDate");
        form.addInternalLabel("accessionNumber");
        searchTableComponents.add(form.createAjaxParent("searchFields"));
        form.addPatientNameField("patientName", new PropertyModel<String>(filter, "patientName"), WebCfgDelegate.getInstance().useFamilyAndGivenNameQueryFields(), enabledModelPat, false);
        form.addTextField("patientID", enabledModelPat, true);
        form.addTextField("issuerOfPatientID", enabledModelPat, true);
        SimpleDateTimeField dtf = form.addDateTimeField("studyDateMin", new PropertyModel<Date>(filter, "studyDateMin"), enabledModel, false, true);
        SimpleDateTimeField dtfEnd = form.addDateTimeField("studyDateMax", new PropertyModel<Date>(filter, "studyDateMax"), enabledModel, true, true);
        dtf.addToDateField(new CheckOneDayBehaviour(dtf, dtfEnd, "onchange"));
        form.addTextField("accessionNumber", enabledModel, false);
        searchTableComponents.add(form.createAjaxParent("searchFuzzy"));
        form.addComponent(new CheckBox("fuzzyPN").setVisible(filter.isFuzzyPNEnabled()));
        form.addInternalLabel("fuzzyPN").setVisible(filter.isFuzzyPNEnabled());
        searchTableComponents.add(form.createAjaxParent("searchDropdowns"));
        form.addInternalLabel("modality");
        form.addInternalLabel("sourceAET");
        form.addDropDownChoice("modality", null, new Model<ArrayList<String>>(new ArrayList<String>(WebCfgDelegate.getInstance().getModalityList())), enabledModel, false).setModelObject("*");
        List<String> aetChoices = viewport.getAetChoices();
        if (aetChoices.size() > 0) form.addDropDownChoice("sourceAET", null, new Model<ArrayList<String>>(new ArrayList<String>(aetChoices)), enabledModel, false).setModelObject(aetChoices.get(0)); else form.addDropDownChoice("sourceAET", null, new Model<ArrayList<String>>(new ArrayList<String>(aetChoices)), new Model<Boolean>(false), false).setNullValid(true);
        searchTableComponents.add(form.createAjaxParent("exactModalities"));
        form.addLabeledCheckBox("exactModalitiesInStudy", null);
        final WebMarkupContainer extendedFilter = new WebMarkupContainer("extendedFilter") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isVisible() {
                return showSearch && filter.isExtendedQuery();
            }
        };
        extendedFilter.add(new Label("birthDate.label", new ResourceModel("folder.extendedFilter.birthDate.label")));
        extendedFilter.add(new Label("birthDateMin.label", new ResourceModel("folder.extendedFilter.birthDateMin.label")));
        extendedFilter.add(new Label("birthDateMax.label", new ResourceModel("folder.extendedFilter.birthDateMax.label")));
        SimpleDateTimeField dtfB = form.getDateTextField("birthDateMin", null, "extendedFilter.", enabledModelPat);
        SimpleDateTimeField dtfBEnd = form.getDateTextField("birthDateMax", null, "extendedFilter.", enabledModelPat);
        dtfB.addToDateField(new CheckOneDayBehaviour(dtfB, dtfBEnd, "onchange"));
        extendedFilter.add(dtfB);
        extendedFilter.add(dtfBEnd);
        extendedFilter.add(new Label("studyInstanceUID.label", new ResourceModel("folder.extendedFilter.studyInstanceUID.label")));
        extendedFilter.add(new TextField<String>("studyInstanceUID") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isEnabled() {
                return !filter.isPatientQuery();
            }
        }.add(new PatternValidator("^[0-9]+(\\.[0-9]+)*$")).add(new UIDFieldBehavior(form)));
        extendedFilter.add(new Label("seriesInstanceUID.label", new ResourceModel("folder.extendedFilter.seriesInstanceUID.label")));
        extendedFilter.add(new TextField<String>("seriesInstanceUID") {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isEnabled() {
                return !filter.isPatientQuery() && QueryUtil.isUniversalMatch(filter.getStudyInstanceUID());
            }
        }.add(new PatternValidator("^[0-9]+(\\.[0-9]+)*$")).add(new UIDFieldBehavior(form)));
        extendedFilter.add(new CheckBox("exactSeriesIuid"));
        extendedFilter.add(new Label("exactSeriesIuid.label", new ResourceModel("folder.extendedFilter.exactSeriesIuid.label")));
        form.add(extendedFilter);
        searchTableComponents.add(form.createAjaxParent("searchFooter"));
        AjaxFallbackLink<?> link = new AjaxFallbackLink<Object>("showExtendedFilter") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                filter.setExtendedQuery(!filter.isExtendedQuery());
                target.addComponent(form);
            }
        };
        link.add((new Image("showExtendedFilterImg", new AbstractReadOnlyModel<ResourceReference>() {

            private static final long serialVersionUID = 1L;

            @Override
            public ResourceReference getObject() {
                return filter.isExtendedQuery() ? ImageManager.IMAGE_COMMON_COLLAPSE : ImageManager.IMAGE_COMMON_EXPAND;
            }
        }).add(new TooltipBehaviour("folder.search.", "showExtendedFilterImg", new AbstractReadOnlyModel<Boolean>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Boolean getObject() {
                return filter.isExtendedQuery();
            }
        }))).add(new ImageSizeBehaviour()));
        form.addComponent(link);
    }

    private void addQueryOptions(final BaseForm form) {
        final CheckBox chkLatestStudyFirst = form.addLabeledCheckBox("latestStudiesFirst", null);
        List<Integer> expandChoices = WebCfgDelegate.getInstance().getAutoExpandLevelChoiceList();
        final DropDownChoice<Integer> autoExpand = new DropDownChoice<Integer>("autoExpandLevel", expandChoices) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isEnabled() {
                return !viewport.getFilter().isPatientQuery();
            }
        };
        form.addComponent(autoExpand);
        Label autoExpandLabel = form.addInternalLabel("autoExpandLevel");
        autoExpand.setChoiceRenderer(new IChoiceRenderer<Integer>() {

            private static final long serialVersionUID = 1L;

            public Object getDisplayValue(Integer object) {
                int level = (Integer) object;
                return level < 0 ? "auto" : form.getString("folder.searchOptions." + AbstractDicomModel.LEVEL_STRINGS[level].toLowerCase());
            }

            public String getIdValue(Integer object, int index) {
                return String.valueOf(index);
            }
        });
        viewport.getFilter().setAutoExpandLevel(expandChoices.get(0));
        if (expandChoices.size() < 2) {
            autoExpand.setVisible(false);
            autoExpandLabel.setVisible(false);
        }
        final IModel<Integer> searchOptionSelected = new IModel<Integer>() {

            private static final long serialVersionUID = 1L;

            public void detach() {
            }

            public Integer getObject() {
                StudyListFilter filter = viewport.getFilter();
                int i = 0;
                if (!filter.isPatientQuery()) {
                    if (filter.isPpsWithoutMwl()) i = 0x01;
                    if (filter.isWithoutPps()) i = i | 0x02;
                    i++;
                }
                return i;
            }

            public void setObject(Integer object) {
                StudyListFilter filter = viewport.getFilter();
                filter.setPatientQuery(object == 0);
                filter.setPpsWithoutMwl((--object & 0x01) > 0);
                filter.setWithoutPps((object & 0x02) > 0);
            }
        };
        final DropDownChoice<Integer> queryType = new DropDownChoice<Integer>("queryType", searchOptionSelected, Arrays.asList(0, 1, 2, 3, 4));
        form.addComponent(queryType);
        form.addInternalLabel("queryType");
        queryType.setChoiceRenderer(new IChoiceRenderer<Integer>() {

            private static final long serialVersionUID = 1L;

            public Object getDisplayValue(Integer object) {
                switch(object) {
                    case 0:
                        return form.getString("folder.searchOptions.patient");
                    case 1:
                        return form.getString("folder.searchOptions.study");
                    case 2:
                        return form.getString("folder.searchOptions.ppsWithoutMwl");
                    case 3:
                        return form.getString("folder.searchOptions.withoutPps");
                    case 4:
                        return form.getString("folder.searchOptions.withoutMwl");
                }
                ;
                return "unknown";
            }

            public String getIdValue(Integer object, int index) {
                return String.valueOf(index);
            }
        });
        queryType.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            protected void onUpdate(AjaxRequestTarget target) {
                chkLatestStudyFirst.setEnabled(!viewport.getFilter().isPatientQuery());
                BaseForm.addFormComponentsToAjaxRequestTarget(target, form);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void addNavigation(final BaseForm form) {
        Button resetBtn = new AjaxButton("resetBtn") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                form.clearInput();
                retainSelectedPatients();
                viewport.getFilter().clear();
                ((DropDownChoice) ((WebMarkupContainer) form.get("searchDropdowns")).get("modality")).setModelObject("*");
                DropDownChoice sourceAETDropDownChoice = ((DropDownChoice) ((WebMarkupContainer) form.get("searchDropdowns")).get("sourceAET"));
                if (sourceAETDropDownChoice.getChoices().size() > 0) sourceAETDropDownChoice.setModelObject(sourceAETDropDownChoice.getChoices().get(0)); else sourceAETDropDownChoice.setNullValid(true);
                pagesize.setObject(WebCfgDelegate.getInstance().getDefaultFolderPagesize());
                notSearched = true;
                form.setOutputMarkupId(true);
                target.addComponent(form);
            }
        };
        resetBtn.setDefaultFormProcessing(false);
        resetBtn.add(new Image("resetImg", ImageManager.IMAGE_COMMON_RESET).add(new ImageSizeBehaviour("vertical-align: middle;")));
        resetBtn.add(new Label("resetText", new ResourceModel("folder.searchFooter.resetBtn.text")).add(new AttributeModifier("style", true, new Model<String>("vertical-align: middle"))));
        form.addComponent(resetBtn);
        IndicatingAjaxButton searchBtn = new IndicatingAjaxButton("searchBtn") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                viewport.setOffset(0);
                viewport.getFilter().setAutoWildcard(WebCfgDelegate.getInstance().getAutoWildcard());
                queryStudies(target);
                Auditlog.logQuery(true, UID.StudyRootQueryRetrieveInformationModelFIND, viewport.getFilter().getQueryDicomObject());
                target.addComponent(form);
            }

            @Override
            public void onError(AjaxRequestTarget target, Form<?> form) {
                BaseForm.addInvalidComponentsToAjaxRequestTarget(target, form);
            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                try {
                    return macb.getAjaxCallDecorator();
                } catch (Exception e) {
                    log.error("Failed to get IAjaxCallDecorator: ", e);
                }
                return null;
            }
        };
        searchBtn.setOutputMarkupId(true);
        searchBtn.add(new Image("searchImg", ImageManager.IMAGE_COMMON_SEARCH).add(new ImageSizeBehaviour("vertical-align: middle;")));
        searchBtn.add(new Label("searchText", new ResourceModel("folder.searchFooter.searchBtn.text")).add(new AttributeModifier("style", true, new Model<String>("vertical-align: middle;"))).setOutputMarkupId(true));
        form.addComponent(searchBtn);
        form.setDefaultButton(searchBtn);
        form.clearParent();
        form.addDropDownChoice("pagesize", pagesize, new Model<ArrayList<String>>(new ArrayList(WebCfgDelegate.getInstance().getPagesizeList())), new Model<Boolean>(true), true).setNullValid(false).add(new IndicatingAjaxFormSubmitBehavior(form, "onchange", searchBtn) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (!WebCfgDelegate.getInstance().isQueryAfterPagesizeChange()) return;
                queryStudies(target);
                target.addComponent(form);
                target.addComponent(header);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                try {
                    return macb.getAjaxCallDecorator();
                } catch (Exception e) {
                    log.error("Failed to get IAjaxCallDecorator: ", e);
                }
                return null;
            }
        });
        form.add(new Link<Object>("prev") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                viewport.setOffset(Math.max(0, viewport.getOffset() - pagesize.getObject()));
                queryStudies(null);
            }

            @Override
            public boolean isVisible() {
                return (!notSearched && !(viewport.getOffset() == 0));
            }
        }.add(new Image("prevImg", ImageManager.IMAGE_COMMON_BACK).add(new ImageSizeBehaviour("vertical-align: middle;")).add(new TooltipBehaviour("folder.search."))));
        form.add(new Link<Object>("next") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                viewport.setOffset(viewport.getOffset() + pagesize.getObject());
                queryStudies(null);
            }

            @Override
            public boolean isVisible() {
                return (!notSearched && !(viewport.getTotal() - viewport.getOffset() <= pagesize.getObject()));
            }
        }.add(new Image("nextImg", ImageManager.IMAGE_COMMON_FORWARD).add(new ImageSizeBehaviour("vertical-align: middle;")).add(new TooltipBehaviour("folder.search."))).setVisible(!notSearched));
        Model<?> keySelectModel = new Model<Serializable>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Serializable getObject() {
                return notSearched ? "folder.search.notSearched" : viewport.getFilter().isPatientQuery() ? (viewport.getTotal() == 0 ? "folder.search.noMatchingPatientsFound" : "folder.search.patientsFound") : (viewport.getTotal() == 0 ? "folder.search.noMatchingStudiesFound" : "folder.search.studiesFound");
            }
        };
        form.add(new Label("viewport", new StringResourceModel("${}", StudyListPage.this, keySelectModel, new Object[] { "dummy" }) {

            private static final long serialVersionUID = 1L;

            @Override
            protected Object[] getParameters() {
                return new Object[] { viewport.getOffset() + 1, Math.min(viewport.getOffset() + pagesize.getObject(), viewport.getTotal()), viewport.getTotal() };
            }
        }).setEscapeModelStrings(false));
        confirmEdit = new ConfirmationWindow<AbstractEditableDicomModel>("confirmEdit") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onConfirmation(AjaxRequestTarget target, final AbstractEditableDicomModel model) {
                logSecurityAlert(model, true, StudyListPage.tooOldAuditMessageText);
            }
        };
        confirmEdit.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            private static final long serialVersionUID = 1L;

            public void onClose(AjaxRequestTarget target) {
                if (confirmEdit.getState() == ConfirmationWindow.CONFIRMED) {
                    modalWindow.setContent(getEditDicomObjectPanel(confirmEdit.getUserObject()));
                    modalWindow.show(target);
                }
            }
        });
        confirmEdit.setInitialHeight(150);
        form.add(confirmEdit);
    }

    private void addActions(final BaseForm form) {
        final ConfirmationWindow<SelectedEntities> confirmDelete = new ConfirmationWindow<SelectedEntities>("confirmDelete") {

            private static final long serialVersionUID = 1L;

            private transient ContentEditDelegate delegate;

            private ContentEditDelegate getDelegate() {
                if (delegate == null) {
                    delegate = ContentEditDelegate.getInstance();
                }
                return delegate;
            }

            @Override
            public void onOk(AjaxRequestTarget target) {
                target.addComponent(form);
            }

            @Override
            public void close(AjaxRequestTarget target) {
                target.addComponent(form);
                super.close(target);
            }

            @Override
            public void onConfirmation(AjaxRequestTarget target, final SelectedEntities selected) {
                if (selected.hasTooOld()) {
                    for (StudyModel st : selected.getStudiesTooOld()) logSecurityAlert(st, true, StudyListPage.tooOldAuditMessageText);
                }
                this.setStatus(new StringResourceModel("folder.message.delete.running", StudyListPage.this, null));
                try {
                    if (getDelegate().moveToTrash(selected)) {
                        setStatus(new StringResourceModel("folder.message.deleteDone", StudyListPage.this, null));
                        if (selected.hasPatients()) {
                            viewport.getPatients().clear();
                            queryStudies(target);
                        } else selected.refreshView(true);
                    } else setStatus(new StringResourceModel("folder.message.deleteFailed", StudyListPage.this, null));
                } catch (Throwable t) {
                    log.error("moveToTrash failed: ", t);
                }
                target.addComponent(getMessageWindowPanel().getMsgLabel());
                target.addComponent(getMessageWindowPanel().getOkBtn());
            }

            @Override
            public void onDecline(AjaxRequestTarget target, SelectedEntities selected) {
                if (selected.getPpss().size() != 0) {
                    if (ContentEditDelegate.getInstance().deletePps(selected)) {
                        this.setStatus(new StringResourceModel("folder.message.deleteDone", StudyListPage.this, null));
                        selected.refreshView(true);
                    } else this.setStatus(new StringResourceModel("folder.message.deleteFailed", StudyListPage.this, null));
                }
            }
        };
        confirmDelete.setInitialHeight(200);
        form.add(confirmDelete);
        AjaxButton deleteBtn = new AjaxButton("deleteBtn") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                boolean hasIgnored = selected.update(studyPermissionHelper.isUseStudyPermissions(), viewport.getPatients(), StudyPermission.DELETE_ACTION);
                selected.deselectChildsOfSelectedEntities();
                selected.computeTooOld();
                if ((hasIgnored || selected.hasDicomSelection() || selected.hasPPS()) && selected.hasTooOld()) {
                    if (StudyPermissionHelper.get().ignoreEditTimeLimit()) {
                        if (selected.hasPPS()) {
                            confirmDelete.confirmWithCancel(target, new StringResourceModel("folder.message.tooOld.confirmPpsDelete", this, null, new Object[] { selected }), selected);
                        } else if (selected.hasDicomSelection()) {
                            confirmDelete.confirm(target, new StringResourceModel("folder.message.tooOld.delete", this, null, new Object[] { selected }), selected);
                        } else {
                            msgWin.setInfoMessage(getString("folder.message.deleteNotAllowed"));
                            msgWin.setColor("#FF0000");
                            msgWin.show(target);
                        }
                    } else {
                        msgWin.setInfoMessage(getString("folder.message.tooOld.delete.denied"));
                        msgWin.setColor("#FF0000");
                        msgWin.show(target);
                    }
                    return;
                }
                confirmDelete.setRemark(hasIgnored ? new StringResourceModel("folder.message.deleteNotAllowed", this, null) : null);
                if (selected.hasPPS()) {
                    confirmDelete.confirmWithCancel(target, new StringResourceModel("folder.message.confirmPpsDelete", this, null, new Object[] { selected }), selected);
                } else if (selected.hasDicomSelection()) {
                    confirmDelete.confirm(target, new StringResourceModel("folder.message.confirmDelete", this, null, new Object[] { selected }), selected);
                } else {
                    if (hasIgnored) {
                        msgWin.setInfoMessage(getString("folder.message.deleteNotAllowed"));
                        msgWin.setColor("#FF0000");
                    } else {
                        msgWin.setInfoMessage(getString("folder.message.noSelection"));
                        msgWin.setColor("");
                    }
                    msgWin.show(target);
                }
            }
        };
        deleteBtn.add(new Image("deleteImg", ImageManager.IMAGE_FOLDER_DELETE).add(new ImageSizeBehaviour("vertical-align: middle;")));
        deleteBtn.add(new Label("deleteText", new ResourceModel("folder.deleteBtn.text")).add(new AttributeModifier("style", true, new Model<String>("vertical-align: middle"))));
        form.add(deleteBtn);
        deleteBtn.add(new SecurityBehavior(getModuleName() + ":deleteButton"));
        AjaxFallbackButton moveBtn = new AjaxFallbackButton("moveBtn", form) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target, Form<?> form) {
                selected.update(false, viewport.getPatients(), StudyPermission.UPDATE_ACTION, true);
                log.debug("Selected Entities:{}", selected);
                if (selected.hasDicomSelection()) {
                    modalWindow.setPageCreator(new ModalWindow.PageCreator() {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public Page createPage() {
                            return new MoveEntitiesPage(modalWindow, selected, viewport.getPatients());
                        }
                    });
                    int[] winSize = WebCfgDelegate.getInstance().getWindowSize("move");
                    modalWindow.setInitialWidth(winSize[0]).setInitialHeight(winSize[1]);
                    modalWindow.setTitle("");
                    modalWindow.show(target);
                } else msgWin.show(target, getString("folder.message.noSelection"));
            }
        };
        moveBtn.add(new Image("moveImg", ImageManager.IMAGE_FOLDER_MOVE).add(new ImageSizeBehaviour("vertical-align: middle;")));
        moveBtn.add(new Label("moveText", new ResourceModel("folder.moveBtn.text")).add(new AttributeModifier("style", true, new Model<String>("vertical-align: middle"))));
        form.add(moveBtn);
        moveBtn.add(new SecurityBehavior(getModuleName() + ":moveButton"));
        PopupLink exportBtn = new PopupLink("exportBtn", "exportPage") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                this.setResponsePage(new ExportPage(viewport.getPatients()));
            }
        };
        int[] winSize = WebCfgDelegate.getInstance().getWindowSize("export");
        exportBtn.setPopupWidth(winSize[0]);
        exportBtn.setPopupHeight(winSize[1]);
        exportBtn.add(new Image("exportImg", ImageManager.IMAGE_FOLDER_EXPORT).add(new ImageSizeBehaviour("vertical-align: middle;")));
        exportBtn.add(new Label("exportText", new ResourceModel("folder.exportBtn.text")).add(new AttributeModifier("style", true, new Model<String>("vertical-align: middle"))));
        form.add(exportBtn);
        exportBtn.add(new SecurityBehavior(getModuleName() + ":exportButton"));
        confirmLinkMpps = new ConfirmationWindow<PPSModel>("confirmLink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onConfirmation(AjaxRequestTarget target, final PPSModel ppsModel) {
                logSecurityAlert(ppsModel, true, StudyListPage.tooOldAuditMessageText);
                setMppsLinkWindow().show(target, ppsModel, form);
                setStatus(new Model<String>(""));
            }
        };
        form.add(confirmLinkMpps.setInitialHeight(150).setInitialWidth(410));
        confirmUnlinkMpps = new ConfirmationWindow<PPSModel>("confirmUnlink") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onOk(AjaxRequestTarget target) {
                target.addComponent(form);
            }

            @Override
            public void close(AjaxRequestTarget target) {
                target.addComponent(form);
                super.close(target);
            }

            @Override
            public void onConfirmation(AjaxRequestTarget target, final PPSModel ppsModel) {
                logSecurityAlert(ppsModel, true, StudyListPage.tooOldAuditMessageText);
                this.setStatus(new StringResourceModel("folder.message.unlink.running", StudyListPage.this, null));
                getMessageWindowPanel().getOkBtn().setVisible(false);
                try {
                    if (ContentEditDelegate.getInstance().unlink(ppsModel)) {
                        setStatus(new StringResourceModel("folder.message.unlinkDone", StudyListPage.this, null));
                        ppsModel.getStudy().expand();
                        ppsModel.getStudy().refresh();
                    } else setStatus(new StringResourceModel("folder.message.unlinkFailed", StudyListPage.this, null));
                } catch (Throwable t) {
                    log.error("Unlink of MPPS failed:" + ppsModel, t);
                }
                target.addComponent(getMessageWindowPanel().getMsgLabel());
                target.addComponent(getMessageWindowPanel().getOkBtn());
            }
        };
        form.add(confirmUnlinkMpps.setInitialHeight(150));
        confirmEmulateMpps = new ConfirmationWindow<PPSModel>("confirmEmulate") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onOk(AjaxRequestTarget target) {
                target.addComponent(form);
            }

            @Override
            public void close(AjaxRequestTarget target) {
                target.addComponent(form);
                super.close(target);
            }

            @Override
            public void onConfirmation(AjaxRequestTarget target, final PPSModel ppsModel) {
                log.info("Emulate MPPS for Study:" + ppsModel.getStudy().getStudyInstanceUID());
                int success = -1;
                try {
                    if (ppsModel.hasForeignPpsInfo()) {
                        ContentEditDelegate.getInstance().removeForeignPpsInfo(ppsModel.getStudy().getPk());
                    }
                    success = MppsEmulateDelegate.getInstance().emulateMpps(ppsModel.getStudy().getPk());
                } catch (Throwable t) {
                    log.error("Emulate MPPS failed!", t);
                }
                setStatus(new StringResourceModel(success < 0 ? "folder.message.emulateFailed" : "folder.message.emulateDone", StudyListPage.this, null, new Object[] { new Integer(success) }));
                if (success > 0) {
                    StudyModel st = ppsModel.getStudy();
                    st.collapse();
                    st.expand();
                }
            }
        };
        confirmEmulateMpps.setInitialHeight(150);
        form.add(confirmEmulateMpps);
    }

    private void queryStudies(AjaxRequestTarget target) {
        try {
            List<String> dicomSecurityRoles = (studyPermissionHelper.applyStudyPermissions() ? studyPermissionHelper.getDicomRoles() : null);
            StudyListFilter filter = viewport.getFilter();
            viewport.setTotal(dao.count(filter, dicomSecurityRoles));
            updatePatients(dao.findPatients(filter, pagesize.getObject(), viewport.getOffset(), dicomSecurityRoles));
            header.expandToLevel(filter.isPatientQuery() ? AbstractDicomModel.PATIENT_LEVEL : AbstractDicomModel.STUDY_LEVEL);
            updateAutoExpandLevel();
            if (filter.isExtendedQuery() && filter.getSeriesInstanceUID() != null) {
                filter.setPpsWithoutMwl(false);
                filter.setWithoutPps(false);
            }
            notSearched = false;
        } catch (Throwable x) {
            if ((x instanceof EJBException) && x.getCause() != null) x = x.getCause();
            if ((x instanceof IllegalArgumentException) && x.getMessage() != null && x.getMessage().indexOf("fuzzy") != -1) x = new WicketExceptionWithMsgKey("fuzzyError", x);
            log.error("Error on queryStudies: ", x);
            msgWin.show(target, new WicketExceptionWithMsgKey("folder.message.searcherror", x), true);
        }
    }

    private void updateStudyPermissions() {
        for (PatientModel patient : viewport.getPatients()) {
            for (StudyModel study : patient.getStudies()) study.setStudyPermissionActions(dao.findStudyPermissionActions((study).getStudyInstanceUID(), studyPermissionHelper.getDicomRoles()));
        }
    }

    private void updatePatients(List<Patient> patients) {
        retainSelectedPatients();
        boolean forceExpandable = WebCfgDelegate.getInstance().forcePatientExpandableForPatientQuery();
        for (Patient patient : patients) {
            PatientModel patientModel = addPatient(patient);
            if (!viewport.getFilter().isPatientQuery() && viewport.getFilter().getAutoExpandLevel() != PatientModel.PATIENT_LEVEL) {
                for (Study study : patient.getStudies()) {
                    List<String> actions = dao.findStudyPermissionActions((study).getStudyInstanceUID(), studyPermissionHelper.getDicomRoles());
                    if (!studyPermissionHelper.applyStudyPermissions() || actions.contains("Q")) {
                        addStudy(study, patientModel, actions);
                        patientModel.setExpandable(true);
                    }
                }
            } else if (forceExpandable) {
                patientModel.setExpandable(true);
            }
        }
    }

    private void retainSelectedPatients() {
        for (int i = 0; i < viewport.getPatients().size(); i++) {
            PatientModel patient = viewport.getPatients().get(i);
            patient.retainSelectedStudies();
            if (patient.isCollapsed() && !patient.isSelected()) {
                viewport.getPatients().remove(i);
                i--;
            }
        }
    }

    private void updateAutoExpandLevel() {
        int level = AbstractDicomModel.PATIENT_LEVEL;
        pat: for (PatientModel patient : viewport.getPatients()) {
            if (!patient.isCollapsed()) {
                for (StudyModel s : patient.getStudies()) {
                    if (level < AbstractDicomModel.STUDY_LEVEL) level = AbstractDicomModel.STUDY_LEVEL;
                    for (PPSModel p : s.getPPSs()) {
                        if (level < AbstractDicomModel.PPS_LEVEL) level = AbstractDicomModel.PPS_LEVEL;
                        for (SeriesModel se : p.getSeries()) {
                            if (se.isCollapsed()) {
                                level = AbstractDicomModel.SERIES_LEVEL;
                            } else {
                                level = AbstractDicomModel.INSTANCE_LEVEL;
                                break pat;
                            }
                        }
                    }
                }
            }
        }
        header.setExpandAllLevel(level);
    }

    private PatientModel addPatient(Patient patient) {
        long pk = patient.getPk();
        for (PatientModel patientModel : viewport.getPatients()) {
            if (patientModel.getPk() == pk) {
                return patientModel;
            }
        }
        PatientModel patientModel = new PatientModel(patient, latestStudyFirst);
        viewport.getPatients().add(patientModel);
        return patientModel;
    }

    private boolean addStudy(Study study, PatientModel patient, List<String> studyPermissionActions) {
        List<StudyModel> studies = patient.getStudies();
        for (StudyModel studyModel : studies) {
            if (studyModel.getPk() == study.getPk()) {
                return false;
            }
        }
        StudyModel m = new StudyModel(study, patient, study.getCreatedTime(), studyPermissionActions);
        StudyListFilter filter = viewport.getFilter();
        if (filter.isExactSeriesIuid()) m.setRestrictChildsBySeriesIuid(viewport.getFilter().getSeriesInstanceUID());
        int expandLevel = filter.getAutoExpandLevel();
        if (expandLevel == -1) {
            if (filter.isExactSeriesIuid()) {
                m.expand();
            } else {
                boolean woMwl = filter.isPpsWithoutMwl();
                boolean woPps = filter.isWithoutPps();
                if (woMwl || woPps) {
                    m.expand();
                    PPSModel pps;
                    for (Iterator<PPSModel> it = m.getPPSs().iterator(); it.hasNext(); ) {
                        pps = it.next();
                        if (pps.getDataset() == null) {
                            if (woPps) {
                                pps.getNumberOfSeries();
                                pps.getNumberOfInstances();
                                pps.collapse();
                            } else it.remove();
                        } else if (woMwl && pps.getAccessionNumber() == null) {
                            pps.collapse();
                        } else {
                            it.remove();
                        }
                    }
                }
            }
        } else if (expandLevel > StudyModel.STUDY_LEVEL) {
            expandToLevel(m, expandLevel);
        }
        studies.add(m);
        return true;
    }

    private void expandToLevel(AbstractDicomModel m, int level) {
        int modelLevel = m.levelOfModel();
        if (modelLevel < level) {
            m.expand();
            if (modelLevel == AbstractDicomModel.STUDY_LEVEL) {
                if (level == AbstractDicomModel.PPS_LEVEL) {
                    for (AbstractDicomModel m1 : m.getDicomModelsOfNextLevel()) {
                        m1.collapse();
                    }
                } else if (level == AbstractDicomModel.INSTANCE_LEVEL) {
                    for (AbstractDicomModel m1 : m.getDicomModelsOfNextLevel()) {
                        for (AbstractDicomModel m2 : m1.getDicomModelsOfNextLevel()) {
                            expandToLevel(m2, level);
                        }
                    }
                }
            } else if (++modelLevel < level) {
                for (AbstractDicomModel m1 : m.getDicomModelsOfNextLevel()) {
                    expandToLevel(m1, level);
                }
            }
        }
    }

    private boolean expandLevelChanged(AbstractDicomModel model) {
        int currLevel = header.getExpandAllLevel();
        int level = model.levelOfModel();
        if (model.isCollapsed() || currLevel > level) {
            level = getExpandedLevel(0, viewport.getPatients());
        } else {
            level = getExpandedLevel(++level, model.getDicomModelsOfNextLevel());
        }
        header.setExpandAllLevel(level);
        return level != currLevel;
    }

    private int getExpandedLevel(int startLevel, List<? extends AbstractDicomModel> list) {
        int level = startLevel;
        if (list != null) {
            startLevel++;
            int l;
            for (AbstractDicomModel m1 : list) {
                if (!m1.isCollapsed()) {
                    l = getExpandedLevel(startLevel, m1.getDicomModelsOfNextLevel());
                    if (l > level) level = l;
                }
            }
        }
        return level;
    }

    public static String getModuleName() {
        return MODULE_NAME;
    }

    private final class PatientListView extends PropertyListView<Object> {

        private static final long serialVersionUID = 1L;

        private PatientListView(String id, List<?> list) {
            super(id, list);
        }

        @Override
        protected void populateItem(final ListItem<Object> item) {
            item.setOutputMarkupId(true);
            final PatientModel patModel = (PatientModel) item.getModelObject();
            WebMarkupContainer row = new WebMarkupContainer("row");
            AjaxCheckBox selChkBox = new AjaxCheckBox("selected") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.addComponent(this.getParent());
                }
            };
            row.add(new SelectableTableRowBehaviour(selChkBox, "patient", "patient_selected"));
            item.add(row);
            WebMarkupContainer cell = new WebMarkupContainer("cell") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.put("rowspan", patModel.getRowspan());
                }
            };
            cell.add(new ExpandCollapseLink("expand", patModel, item).setVisible(patModel.isExpandable()));
            row.add(cell);
            TooltipBehaviour tooltip = new TooltipBehaviour("folder.content.data.patient.");
            row.add(new Label("name").add(tooltip));
            row.add(new Label("id", new AbstractReadOnlyModel<String>() {

                private static final long serialVersionUID = 1L;

                @Override
                public String getObject() {
                    return patModel.getIssuer() == null ? patModel.getId() : patModel.getId() + " / " + patModel.getIssuer();
                }
            }).add(tooltip));
            DateTimeLabel dtl = new DateTimeLabel("birthdate").setWithoutTime(true);
            dtl.add(tooltip.newWithSubstitution(new PropertyModel<String>(dtl, "textFormat")));
            row.add(dtl);
            row.add(new Label("sex").add(tooltip));
            row.add(new Label("comments").add(tooltip));
            row.add(new AjaxFallbackLink<Object>("toggledetails") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    patModel.setDetails(!patModel.isDetails());
                    if (target != null) {
                        target.addComponent(item);
                    }
                }
            }.add(new Image("detailImg", ImageManager.IMAGE_COMMON_DICOM_DETAILS).add(new ImageSizeBehaviour()).add(tooltip)));
            row.add(getEditLink(modalWindow, patModel, tooltip).add(new SecurityBehavior(getModuleName() + ":editPatientLink")).add(tooltip));
            row.add(getStudyPermissionLink(modalWindow, patModel, tooltip).add(new SecurityBehavior(getModuleName() + ":studyPermissionsPatientLink")).add(tooltip));
            row.add(Webviewer.getLink(patModel, webviewerLinkProviders, studyPermissionHelper, tooltip, modalWindow).add(new SecurityBehavior(getModuleName() + ":webviewerPatientLink")));
            row.add(selChkBox.add(tooltip));
            WebMarkupContainer details = new WebMarkupContainer("details") {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isVisible() {
                    return patModel.isDetails();
                }
            };
            item.add(details);
            details.add(new DicomObjectPanel("dicomobject", patModel, false));
            item.add(new StudyListView("studies", patModel.getStudies(), item));
        }
    }

    private final class StudyListView extends PropertyListView<Object> {

        private static final long serialVersionUID = 1L;

        private final ListItem<?> patientListItem;

        private StudyListView(String id, List<StudyModel> list, ListItem<?> patientListItem) {
            super(id, list);
            this.patientListItem = patientListItem;
        }

        @Override
        protected void populateItem(final ListItem<Object> item) {
            item.setOutputMarkupId(true);
            final StudyModel studyModel = (StudyModel) item.getModelObject();
            WebMarkupContainer row = new WebMarkupContainer("row");
            AjaxCheckBox selChkBox = new AjaxCheckBox("selected") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.addComponent(this.getParent());
                }
            };
            row.add(new SelectableTableRowBehaviour(selChkBox, "study", "study_selected"));
            item.add(row);
            WebMarkupContainer cell = new WebMarkupContainer("cell") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.put("rowspan", studyModel.getRowspan());
                }
            };
            cell.add(new ExpandCollapseLink("expand", studyModel, patientListItem));
            row.add(cell);
            TooltipBehaviour tooltip = new TooltipBehaviour("folder.content.data.study.");
            row.add(new DateTimeLabel("datetime").add(tooltip));
            row.add(new Label("id").add(tooltip));
            row.add(new Label("accessionNumber").add(tooltip));
            row.add(new Label("modalities").add(tooltip));
            row.add(new Label("description").add(tooltip));
            row.add(new Label("numberOfSeries").add(tooltip));
            row.add(new Label("numberOfInstances").add(tooltip));
            row.add(new Label("availability").add(tooltip));
            row.add(new AjaxFallbackLink<Object>("toggledetails") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    studyModel.setDetails(!studyModel.isDetails());
                    if (target != null) {
                        target.addComponent(patientListItem);
                    }
                }
            }.add(new Image("detailImg", ImageManager.IMAGE_COMMON_DICOM_DETAILS).add(new ImageSizeBehaviour()).add(tooltip)));
            row.add(getEditLink(modalWindow, studyModel, tooltip).add(new SecurityBehavior(getModuleName() + ":editStudyLink")));
            row.add(getStudyPermissionLink(modalWindow, studyModel, tooltip).add(new SecurityBehavior(getModuleName() + ":studyPermissionsStudyLink")).add(tooltip));
            row.add(new IndicatingAjaxLink<Object>("imgSelect") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    int[] winSize = WebCfgDelegate.getInstance().getWindowSize("imgSelect");
                    imageSelectionWindow.setInitialWidth(winSize[0]).setInitialHeight(winSize[1]);
                    imageSelectionWindow.show(target, studyModel);
                }

                @Override
                protected IAjaxCallDecorator getAjaxCallDecorator() {
                    try {
                        return macb.getAjaxCallDecorator();
                    } catch (Exception e) {
                        log.error("Failed to get IAjaxCallDecorator: ", e);
                    }
                    return null;
                }
            }.add(new Image("selectImg", ImageManager.IMAGE_COMMON_SEARCH).add(new ImageSizeBehaviour()).add(tooltip)).setVisible(studyPermissionHelper.checkPermission(studyModel, StudyPermission.READ_ACTION)).add(new SecurityBehavior(getModuleName() + ":imageSelectionStudyLink")));
            row.add(selChkBox.add(tooltip));
            WebMarkupContainer details = new WebMarkupContainer("details") {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isVisible() {
                    return studyModel.isDetails();
                }
            };
            row.add(Webviewer.getLink(studyModel, webviewerLinkProviders, studyPermissionHelper, tooltip, modalWindow).add(new SecurityBehavior(getModuleName() + ":webviewerStudyLink")));
            item.add(details);
            details.add(new DicomObjectPanel("dicomobject", studyModel, false));
            details.setVisible(studyPermissionHelper.checkPermission(studyModel, StudyPermission.QUERY_ACTION));
            item.add(new PPSListView("ppss", studyModel.getPPSs(), patientListItem));
        }
    }

    private final class PPSListView extends PropertyListView<Object> {

        private static final long serialVersionUID = 1L;

        private final ListItem<?> ppsListItem;

        private PPSListView(String id, List<PPSModel> list, ListItem<?> patientListItem) {
            super(id, list);
            this.ppsListItem = patientListItem;
        }

        @Override
        protected void populateItem(final ListItem<Object> item) {
            item.setOutputMarkupId(true);
            final PPSModel ppsModel = (PPSModel) item.getModelObject();
            final boolean tooOld = selected.tooOld(ppsModel);
            WebMarkupContainer row = new WebMarkupContainer("row");
            AjaxCheckBox selChkBox = new AjaxCheckBox("selected") {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isVisible() {
                    return ppsModel.getDataset() != null;
                }

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.addComponent(this.getParent());
                }
            };
            if (ppsModel.getDataset() != null) row.add(new SelectableTableRowBehaviour(selChkBox, "pps", "pps_selected"));
            item.add(row);
            WebMarkupContainer cell = new WebMarkupContainer("cell") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.put("rowspan", ppsModel.getRowspan());
                }
            };
            cell.add(new ExpandCollapseLink("expand", ppsModel, ppsListItem) {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isVisible() {
                    return ppsModel.getUid() != null;
                }
            });
            row.add(cell);
            TooltipBehaviour tooltip = new TooltipBehaviour("folder.content.data.pps.");
            row.add(new DateTimeLabel("datetime").add(tooltip));
            row.add(new Label("id").add(tooltip));
            row.add(new Label("spsid").add(tooltip));
            row.add(new Label("modality").add(tooltip));
            row.add(new Label("description").add(tooltip));
            row.add(new Label("numberOfSeries").add(tooltip));
            row.add(new Label("numberOfInstances").add(tooltip));
            row.add(new Label("status").add(tooltip));
            row.add(new AjaxFallbackLink<Object>("toggledetails") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    ppsModel.setDetails(!ppsModel.isDetails());
                    if (target != null) {
                        target.addComponent(StudyListPage.this.get("form"));
                    }
                }

                @Override
                public boolean isVisible() {
                    return ppsModel.getDataset() != null;
                }
            }.add(new Image("detailImg", ImageManager.IMAGE_COMMON_DICOM_DETAILS).add(new ImageSizeBehaviour()).add(tooltip)));
            row.add(getEditLink(modalWindow, ppsModel, tooltip).add(new SecurityBehavior(getModuleName() + ":editPPSLink")));
            IndicatingAjaxFallbackLink<?> linkBtn = new IndicatingAjaxFallbackLink<Object>("linkBtn") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    log.debug("#### linkBtn clicked!");
                    if (tooOld) {
                        int[] winSize = WebCfgDelegate.getInstance().getWindowSize("linkToOld");
                        confirmLinkMpps.setInitialWidth(winSize[0]).setInitialHeight(winSize[1]);
                        confirmLinkMpps.confirm(target, new StringResourceModel("folder.message.tooOld.link", this, null), ppsModel);
                    } else {
                        setMppsLinkWindow().show(target, ppsModel, form);
                    }
                    log.debug("#### linkBtn onClick finished!");
                }

                @Override
                protected IAjaxCallDecorator getAjaxCallDecorator() {
                    try {
                        return macb.getAjaxCallDecorator();
                    } catch (Exception e) {
                        log.error("Failed to get IAjaxCallDecorator: ", e);
                    }
                    return null;
                }

                @Override
                public boolean isVisible() {
                    return ppsModel.getDataset() != null && ppsModel.getAccessionNumber() == null;
                }

                @Override
                public boolean isEnabled() {
                    return StudyPermissionHelper.get().ignoreEditTimeLimit() || !tooOld;
                }
            };
            Image image = tooOld ? new Image("linkImg", ImageManager.IMAGE_FOLDER_TIMELIMIT_LINK) : new Image("linkImg", ImageManager.IMAGE_COMMON_LINK);
            image.add(new ImageSizeBehaviour());
            if (tooOld && !StudyPermissionHelper.get().ignoreEditTimeLimit()) image.add(new AttributeModifier("title", true, new ResourceModel("folder.message.tooOld.link.tooltip"))); else if (tooltip != null) image.add(tooltip);
            linkBtn.add(image);
            linkBtn.setVisible(studyPermissionHelper.checkPermission(ppsModel, StudyPermission.UPDATE_ACTION));
            linkBtn.add(new SecurityBehavior(getModuleName() + ":linkPPSLink"));
            row.add(linkBtn);
            IndicatingAjaxFallbackLink<?> unlinkBtn = new IndicatingAjaxFallbackLink<Object>("unlinkBtn") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    confirmUnlinkMpps.confirm(target, new StringResourceModel((tooOld ? "folder.message.tooOld.unlink" : "folder.message.confirmUnlink"), this, null, new Object[] { ppsModel }), ppsModel);
                }

                @Override
                public boolean isVisible() {
                    return ppsModel.getDataset() != null && ppsModel.getAccessionNumber() != null;
                }

                @Override
                public boolean isEnabled() {
                    return StudyPermissionHelper.get().ignoreEditTimeLimit() || !tooOld;
                }
            };
            image = tooOld ? new Image("unlinkImg", ImageManager.IMAGE_FOLDER_TIMELIMIT_UNLINK) : new Image("unlinkImg", ImageManager.IMAGE_FOLDER_UNLINK);
            image.add(new ImageSizeBehaviour());
            if (tooOld && !StudyPermissionHelper.get().ignoreEditTimeLimit()) image.add(new AttributeModifier("title", true, new ResourceModel("folder.message.tooOld.unlink.tooltip"))); else if (tooltip != null) image.add(tooltip);
            unlinkBtn.add(image);
            row.add(unlinkBtn);
            row.add(new AjaxFallbackLink<Object>("emulateBtn") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    confirmEmulateMpps.confirm(target, new StringResourceModel(ppsModel.hasForeignPpsInfo() ? "folder.message.confirmForcedEmulate" : "folder.message.confirmEmulate", this, null), ppsModel);
                }

                @Override
                public boolean isVisible() {
                    return ppsModel.getDataset() == null;
                }
            }.add(new Image("emulateImg", ImageManager.IMAGE_FOLDER_MPPS).add(new ImageSizeBehaviour()).add(tooltip)).setVisible(studyPermissionHelper.checkPermission(ppsModel, StudyPermission.APPEND_ACTION)).add(new SecurityBehavior(getModuleName() + ":emulatePPSLink")));
            row.add(selChkBox.add(tooltip));
            WebMarkupContainer details = new WebMarkupContainer("details") {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isVisible() {
                    return ppsModel.isDetails();
                }
            };
            item.add(details);
            details.add(new DicomObjectPanel("dicomobject", ppsModel, false));
            details.setVisible(studyPermissionHelper.checkPermission(ppsModel, StudyPermission.QUERY_ACTION));
            item.add(new SeriesListView("series", ppsModel.getSeries(), ppsListItem));
        }
    }

    private final class SeriesListView extends PropertyListView<Object> {

        private static final long serialVersionUID = 1L;

        private final ListItem<?> patientListItem;

        private SeriesListView(String id, List<SeriesModel> list, ListItem<?> patientListItem) {
            super(id, list);
            this.patientListItem = patientListItem;
        }

        @Override
        protected void populateItem(final ListItem<Object> item) {
            item.setOutputMarkupId(true);
            final SeriesModel seriesModel = (SeriesModel) item.getModelObject();
            WebMarkupContainer row = new WebMarkupContainer("row");
            AjaxCheckBox selChkBox = new AjaxCheckBox("selected") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.addComponent(this.getParent());
                }
            };
            row.add(new SelectableTableRowBehaviour(selChkBox, "series", "series_selected"));
            item.add(row);
            WebMarkupContainer cell = new WebMarkupContainer("cell") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.put("rowspan", seriesModel.getRowspan());
                }
            };
            cell.add(new ExpandCollapseLink("expand", seriesModel, patientListItem));
            row.add(cell);
            TooltipBehaviour tooltip = new TooltipBehaviour("folder.content.data.series.");
            row.add(new DateTimeLabel("datetime").add(tooltip));
            row.add(new Label("seriesNumber").add(tooltip));
            row.add(new Label("sourceAET").add(tooltip));
            row.add(new Label("modality").add(tooltip));
            row.add(new Label("description").add(tooltip));
            row.add(new Label("numberOfInstances").add(tooltip));
            row.add(new Label("availability").add(tooltip));
            row.add(new AjaxFallbackLink<Object>("toggledetails") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    seriesModel.setDetails(!seriesModel.isDetails());
                    if (target != null) {
                        target.addComponent(patientListItem);
                    }
                }
            }.add(new Image("detailImg", ImageManager.IMAGE_COMMON_DICOM_DETAILS).add(new ImageSizeBehaviour()).add(tooltip)));
            row.add(getEditLink(modalWindow, seriesModel, tooltip).add(new SecurityBehavior(getModuleName() + ":editSeriesLink")));
            row.add(selChkBox.setOutputMarkupId(true).add(tooltip));
            row.add(new IndicatingAjaxLink<Object>("imgSelect") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    int[] winSize = WebCfgDelegate.getInstance().getWindowSize("imgSelect");
                    imageSelectionWindow.setInitialWidth(winSize[0]).setInitialHeight(winSize[1]);
                    imageSelectionWindow.show(target, seriesModel);
                }

                @Override
                protected IAjaxCallDecorator getAjaxCallDecorator() {
                    try {
                        return macb.getAjaxCallDecorator();
                    } catch (Exception e) {
                        log.error("Failed to get IAjaxCallDecorator: ", e);
                    }
                    return null;
                }
            }.add(new Image("selectImg", ImageManager.IMAGE_COMMON_SEARCH).add(new ImageSizeBehaviour()).add(tooltip)).setVisible(studyPermissionHelper.checkPermission(seriesModel, StudyPermission.READ_ACTION)).add(new SecurityBehavior(getModuleName() + ":imageSelectionSeriesLink")));
            final WebMarkupContainer details = new WebMarkupContainer("details") {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isVisible() {
                    return seriesModel.isDetails();
                }
            };
            row.add(Webviewer.getLink(seriesModel, webviewerLinkProviders, studyPermissionHelper, tooltip, modalWindow).add(new SecurityBehavior(getModuleName() + ":webviewerSeriesLink")));
            item.add(details);
            details.add(new DicomObjectPanel("dicomobject", seriesModel, false));
            details.setVisible(studyPermissionHelper.checkPermission(seriesModel, StudyPermission.QUERY_ACTION));
            item.add(new InstanceListView("instances", seriesModel.getInstances(), patientListItem));
        }
    }

    private final class InstanceListView extends PropertyListView<Object> {

        private static final long serialVersionUID = 1L;

        private final ListItem<?> patientListItem;

        private InstanceListView(String id, List<InstanceModel> list, ListItem<?> patientListItem) {
            super(id, list);
            this.patientListItem = patientListItem;
        }

        @Override
        protected void populateItem(final ListItem<Object> item) {
            item.setOutputMarkupId(true);
            final InstanceModel instModel = (InstanceModel) item.getModelObject();
            WebMarkupContainer row = new WebMarkupContainer("row");
            AjaxCheckBox selChkBox = new AjaxCheckBox("selected") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.addComponent(this.getParent());
                }
            };
            row.add(new SelectableTableRowBehaviour(selChkBox, "instance", "instance_selected"));
            item.add(row);
            WebMarkupContainer cell = new WebMarkupContainer("cell") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
                    tag.put("rowspan", instModel.getRowspan());
                }
            };
            cell.add(new ExpandCollapseLink("expand", instModel, patientListItem));
            row.add(cell);
            TooltipBehaviour tooltip = new TooltipBehaviour("folder.content.data.instance.");
            row.add(new DateTimeLabel("datetime").add(tooltip));
            row.add(new Label("instanceNumber").add(tooltip));
            row.add(new Label("sopClassUID").add(tooltip));
            row.add(new Label("description").add(tooltip));
            row.add(new Label("availability").add(tooltip));
            row.add(new AjaxFallbackLink<Object>("toggledetails") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    instModel.setDetails(!instModel.isDetails());
                    if (target != null) {
                        target.addComponent(patientListItem);
                    }
                }
            }.add(new Image("detailImg", ImageManager.IMAGE_COMMON_DICOM_DETAILS).add(new ImageSizeBehaviour()).add(tooltip)));
            row.add(getEditLink(modalWindow, instModel, tooltip).setVisible(studyPermissionHelper.checkPermission(instModel, StudyPermission.UPDATE_ACTION)).add(new SecurityBehavior(getModuleName() + ":editInstanceLink")));
            row.add(Webviewer.getLink(instModel, webviewerLinkProviders, studyPermissionHelper, tooltip, modalWindow).add(new SecurityBehavior(getModuleName() + ":webviewerInstanceLink")));
            row.add(new AjaxLink<Object>("wado") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    int[] winSize = WebCfgDelegate.getInstance().getWindowSize("wado");
                    wadoImageWindow.setInitialWidth(winSize[0]).setInitialHeight(winSize[1]);
                    wadoImageWindow.setPageCreator(new ModalWindow.PageCreator() {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public Page createPage() {
                            return new InstanceViewPage(wadoImageWindow, instModel);
                        }
                    });
                    wadoImageWindow.show(target);
                }
            }.add(new Image("wadoImg", ImageManager.IMAGE_FOLDER_WADO).add(new ImageSizeBehaviour()).add(tooltip)).setVisible(studyPermissionHelper.checkPermission(instModel, StudyPermission.READ_ACTION)).add(new SecurityBehavior(getModuleName() + ":wadoImageInstanceLink")));
            row.add(selChkBox.add(tooltip));
            WebMarkupContainer details = new WebMarkupContainer("details") {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isVisible() {
                    return instModel.isDetails();
                }
            };
            item.add(details);
            details.add(new DicomObjectPanel("dicomobject", instModel, false));
            details.setVisible(studyPermissionHelper.checkPermission(instModel, StudyPermission.QUERY_ACTION));
            item.add(new FileListView("files", instModel.getFiles(), patientListItem));
        }
    }

    private final class FileListView extends PropertyListView<Object> {

        private static final long serialVersionUID = 1L;

        private final ListItem<?> patientListItem;

        private FileListView(String id, List<FileModel> list, ListItem<?> patientListItem) {
            super(id, list);
            this.patientListItem = patientListItem;
        }

        @Override
        protected void populateItem(final ListItem<Object> item) {
            item.setOutputMarkupId(true);
            TooltipBehaviour tooltip = new TooltipBehaviour("folder.content.data.file.");
            final FileModel fileModel = (FileModel) item.getModelObject();
            item.add(new DateTimeLabel("fileObject.createdTime").add(tooltip));
            item.add(new Label("fileObject.fileSize").add(tooltip));
            item.add(new Label("fileObject.transferSyntaxUID").add(tooltip));
            item.add(new Label("fileObject.fileSystem.directoryPath").add(tooltip));
            item.add(new Label("fileObject.filePath").add(tooltip));
            item.add(new Label("fileObject.fileSystem.availability").add(tooltip));
            item.add(new AjaxFallbackLink<Object>("toggledetails") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    fileModel.setDetails(!fileModel.isDetails());
                    if (target != null) {
                        target.addComponent(patientListItem);
                    }
                }
            }.add(new Image("detailImg", ImageManager.IMAGE_COMMON_DICOM_DETAILS).add(new ImageSizeBehaviour()).add(tooltip)));
            item.add(getFileDisplayLink(modalWindow, fileModel, tooltip));
            item.add(new AjaxCheckBox("selected") {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isVisible() {
                    return false;
                }

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.addComponent(this);
                }
            }.setOutputMarkupId(true).add(tooltip));
            WebMarkupContainer details = new WebMarkupContainer("details") {

                private static final long serialVersionUID = 1L;

                @Override
                public boolean isVisible() {
                    return fileModel.isDetails();
                }
            };
            item.add(details);
            details.add(new FilePanel("file", fileModel));
        }
    }

    private Link<Object> getEditLink(final ModalWindow modalWindow, final AbstractEditableDicomModel model, TooltipBehaviour tooltip) {
        final boolean tooOld = selected.tooOld(model);
        int[] winSize = WebCfgDelegate.getInstance().getWindowSize("dcmEdit");
        ModalWindowLink editLink = new ModalWindowLink("edit", modalWindow, winSize[0], winSize[1]) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                if (tooOld) {
                    confirmEdit.confirm(target, new StringResourceModel("folder.message.tooOld.edit", this, null), model);
                    confirmEdit.show(target);
                } else {
                    modalWindow.setContent(getEditDicomObjectPanel(model));
                    modalWindow.setTitle("");
                    modalWindow.show(target);
                    super.onClick(target);
                }
            }

            @Override
            public boolean isVisible() {
                return model.getDataset() != null && (!studyPermissionHelper.isUseStudyPermissions() || checkEditStudyPermission(model));
            }

            @Override
            public boolean isEnabled() {
                return StudyPermissionHelper.get().ignoreEditTimeLimit() || !tooOld;
            }
        };
        Image image = tooOld ? new Image("editImg", ImageManager.IMAGE_FOLDER_TIMELIMIT_EDIT) : new Image("editImg", ImageManager.IMAGE_COMMON_DICOM_EDIT);
        image.add(new ImageSizeBehaviour("vertical-align: middle;"));
        if (tooOld && !StudyPermissionHelper.get().ignoreEditTimeLimit()) image.add(new AttributeModifier("title", true, new ResourceModel("folder.message.tooOld.edit.tooltip"))); else if (tooltip != null) image.add(tooltip);
        editLink.add(image);
        return editLink;
    }

    private Panel getEditDicomObjectPanel(final AbstractEditableDicomModel model) {
        return new EditDicomObjectPanel("content", modalWindow, (DicomObject) model.getDataset(), model.getClass().getSimpleName()) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                model.update(getDicomObject());
                try {
                    ContentEditDelegate.getInstance().doAfterDicomEdit(model);
                } catch (Exception x) {
                    log.warn("doAfterDicomEdit failed!", x);
                }
                super.onCancel();
            }
        };
    }

    private Mpps2MwlLinkPage setMppsLinkWindow() {
        mpps2MwlLinkWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClose(AjaxRequestTarget target) {
                getPage().setOutputMarkupId(true);
                target.addComponent(getPage());
            }
        });
        int[] winSize = WebCfgDelegate.getInstance().getWindowSize("mpps2mwl");
        ((Mpps2MwlLinkPage) mpps2MwlLinkWindow).setInitialWidth(winSize[0]).setInitialHeight(winSize[1]);
        return mpps2MwlLinkWindow;
    }

    private Link<Object> getFileDisplayLink(final ModalWindow modalWindow, final FileModel fileModel, TooltipBehaviour tooltip) {
        int[] winSize = WebCfgDelegate.getInstance().getWindowSize("dcmFileDisplay");
        final String fsID = fileModel.getFileObject().getFileSystem().getDirectoryPath();
        final String fileID = fileModel.getFileObject().getFilePath();
        ModalWindowLink displayLink = new ModalWindowLink("displayFile", modalWindow, winSize[0], winSize[1]) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                DicomInputStream dis = null;
                try {
                    final File file = fsID.startsWith("tar:") ? TarRetrieveDelegate.getInstance().retrieveFileFromTar(fsID, fileID) : FileUtils.resolve(new File(fsID, fileID));
                    dis = new DicomInputStream(file);
                    modalWindow.setContent(new DicomObjectPanel("content", dis.readDicomObject(), true));
                    modalWindow.setTitle(new ResourceModel("folder.dcmfileview.title"));
                    modalWindow.show(target);
                    super.onClick(target);
                } catch (Exception e) {
                    log.error("Error requesting dicom object from file: ", e);
                    msgWin.show(target, getString("folder.message.dcmFileError"));
                } finally {
                    if (dis != null) try {
                        dis.close();
                    } catch (IOException ignore) {
                    }
                }
            }
        };
        Image image = new Image("displayFileImg", ImageManager.IMAGE_FOLDER_DICOM_FILE);
        image.add(new ImageSizeBehaviour("vertical-align: middle;"));
        if (tooltip != null) image.add(tooltip);
        displayLink.add(image);
        displayLink.setVisible(fsID.startsWith("tar:") || FileUtils.resolve(new File(fsID, fileID)).exists());
        return displayLink;
    }

    private Link<Object> getStudyPermissionLink(final ModalWindow modalWindow, final AbstractEditableDicomModel model, TooltipBehaviour tooltip) {
        int[] winSize = WebCfgDelegate.getInstance().getWindowSize("studyPerm");
        ModalWindowLink studyPermissionLink = new ModalWindowLink("studyPermissions", modalWindow, winSize[0], winSize[1]) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                modalWindow.setPageCreator(new ModalWindow.PageCreator() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Page createPage() {
                        return new StudyPermissionsPage(model);
                    }
                });
                modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClose(AjaxRequestTarget target) {
                        updateStudyPermissions();
                        queryStudies(target);
                        modalWindow.getPage().setOutputMarkupId(true);
                        target.addComponent(modalWindow.getPage());
                        target.addComponent(header);
                    }
                });
                modalWindow.add(new ModalWindowLink.DisableDefaultConfirmBehavior());
                modalWindow.setTitle("");
                modalWindow.setCloseButtonCallback(null);
                modalWindow.show(target);
            }

            @Override
            public boolean isVisible() {
                return studyPermissionHelper.isManageStudyPermissions() && model.getDataset() != null && !(model instanceof PatientModel && !((PatientModel) model).isExpandable());
            }
        };
        Image image = new Image("studyPermissionsImg", ImageManager.IMAGE_FOLDER_STUDY_PERMISSIONS);
        image.add(new ImageSizeBehaviour("vertical-align: middle;"));
        if (tooltip != null) image.add(tooltip);
        studyPermissionLink.add(image);
        return studyPermissionLink;
    }

    private boolean checkEditStudyPermission(AbstractDicomModel model) {
        if (!studyPermissionHelper.isUseStudyPermissions() || (model instanceof PatientModel)) return true;
        return studyPermissionHelper.checkPermission(model, StudyPermission.UPDATE_ACTION);
    }

    private class ExpandCollapseLink extends AjaxFallbackLink<Object> {

        private static final long serialVersionUID = 1L;

        private AbstractDicomModel model;

        private ListItem<?> patientListItem;

        private ExpandCollapseLink(String id, AbstractDicomModel m, ListItem<?> patientListItem) {
            super(id);
            this.model = m;
            this.patientListItem = patientListItem;
            add(new Image(id + "Img", new AbstractReadOnlyModel<ResourceReference>() {

                private static final long serialVersionUID = 1L;

                @Override
                public ResourceReference getObject() {
                    return model.isCollapsed() ? ImageManager.IMAGE_COMMON_EXPAND : ImageManager.IMAGE_COMMON_COLLAPSE;
                }
            }).add(new ImageSizeBehaviour()));
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
            if (model.isCollapsed()) model.expand(); else model.collapse();
            if (target != null) {
                target.addComponent(patientListItem);
                if (expandLevelChanged(model)) target.addComponent(header);
            }
        }
    }

    @Override
    protected void onBeforeRender() {
        applyPageParameters();
        super.onBeforeRender();
    }

    private void applyPageParameters() {
        PageParameters paras = getRequestCycle().getPageParameters();
        if (paras != null && !paras.isEmpty()) {
            log.info("applyPageParameters:" + paras);
            StudyListFilter filter = viewport.getFilter();
            filter.setPatientName(paras.getString("patName"));
            filter.setPatientID(paras.getString("patID"));
            filter.setIssuerOfPatientID(paras.getString("issuer"));
            filter.setAccessionNumber(paras.getString("accNr"));
            Date[] studyDate = toDateRange(paras.getString("studyDate"));
            filter.setStudyDateMin(studyDate[0]);
            filter.setStudyDateMax(studyDate[1]);
            filter.setModality(paras.getString("modality"));
            filter.setSourceAET(paras.getString("sourceAET"));
            String studyIUID = paras.getString("studyIUID");
            filter.setStudyInstanceUID(studyIUID);
            String seriesIUID = paras.getString("seriesIUID");
            filter.setSeriesInstanceUID(seriesIUID);
            Date[] birthdate = toDateRange(paras.getString("birthdate"));
            filter.setBirthDateMin(birthdate[0]);
            filter.setBirthDateMax(birthdate[1]);
            filter.setExtendedQuery(studyIUID != null || seriesIUID != null || paras.getString("birthdate") != null);
            filter.setLatestStudiesFirst(Boolean.valueOf(paras.getString("latestStudiesFirst")));
            filter.setPatientQuery(Boolean.valueOf(paras.getString("patQuery")));
            filter.setPpsWithoutMwl(Boolean.valueOf(paras.getString("ppsWithoutMwl")));
            filter.setWithoutPps(Boolean.valueOf(paras.getString("withoutPps")));
            filter.setExactModalitiesInStudy(Boolean.valueOf(paras.getString("exactModalitiesInStudy")));
            filter.setAutoWildcard(WebCfgDelegate.getInstance().getAutoWildcard());
            if (Boolean.valueOf(paras.getString("query"))) {
                queryStudies(null);
            }
        }
    }

    private Date[] toDateRange(String s) {
        Date[] d = new Date[2];
        if (s != null && s.length() > 2) {
            try {
                DateRange dr = VR.DT.toDateRange(s.getBytes());
                d[0] = dr.getStart();
                d[1] = dr.getEnd();
            } catch (Exception x) {
                log.warn("Wrong date range format:" + s + " Must be [yyyy[MM[dd[hh[mm[ss]]]]]][-yyyy[MM[dd[hh[mm[ss]]]]]]");
            }
        }
        return d;
    }

    private void logSecurityAlert(AbstractDicomModel model, boolean success, String desc) {
        try {
            PatientModel patInfoModel;
            AbstractDicomModel studyInfoModel = null;
            if (model.levelOfModel() > AbstractDicomModel.PATIENT_LEVEL) {
                studyInfoModel = model;
                while (studyInfoModel.levelOfModel() > AbstractDicomModel.STUDY_LEVEL) studyInfoModel = studyInfoModel.getParent();
                patInfoModel = (PatientModel) studyInfoModel.getParent();
            } else {
                patInfoModel = (PatientModel) model;
            }
            String studyIUID = studyInfoModel == null ? null : studyInfoModel.getAttributeValueAsString(Tag.StudyInstanceUID);
            Auditlog.logSecurityAlert(AuditEvent.TypeCode.OBJECT_SECURITY_ATTRIBUTES_CHANGED, success, desc, patInfoModel.getId(), patInfoModel.getName(), studyIUID);
        } catch (Exception ignore) {
            log.warn("Audit log of SecurityAlert for overriding editing time limit failed!", ignore);
        }
    }
}
