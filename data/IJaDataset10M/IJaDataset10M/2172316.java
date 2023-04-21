package maketargeter;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import maketargeter.actions.AddNewTargetAction;
import maketargeter.actions.AddTargetsFileAction;
import maketargeter.actions.BuildTargetAction;
import maketargeter.actions.CopyTargetToClipboardAction;
import maketargeter.actions.RefreshViewAction;
import maketargeter.actions.SetTargetToProjectSettings;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Mikhail Barg
 * 
 */
public class MainView extends ViewPart {

    private static class UpdateJob extends Job {

        private final IProject m_newProject;

        private final boolean m_forceReparse;

        private final MainView m_view;

        public UpdateJob(MainView view, IProject newProject, boolean forceReparse) {
            super(Messages.MainView_updating_view_task + (newProject == null ? null : newProject.getName()));
            m_view = view;
            m_newProject = newProject;
            m_forceReparse = forceReparse;
            this.setPriority(SHORT);
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            monitor.beginTask("", 1);
            PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

                @Override
                public void run() {
                    m_view.update(m_newProject, m_forceReparse);
                }
            });
            monitor.done();
            return Status.OK_STATUS;
        }
    }

    private static enum State {

        /** 
		 this is a normal working state,  
		 during this state 
		 m_currentProject contains reference to the currently selected project (can be null)
		 m_parsingProject is null
		 m_wantedProject is null
		 */
        STATE_ACTUAL, /**
		 the view in this state when it switches to some other project.
		 during this state 
		 m_currentProject is null
		 m_parsingProject contains reference to the project being currently processed
		 m_wantedProject is null but can be set to some new project if project change happens during parse 
		 */
        STATE_UPDATING
    }

    private State m_state = State.STATE_ACTUAL;

    private final Object m_stateLock = new Object();

    private IProject m_currentProject;

    private IProject m_parsingProject;

    private IProject m_wantedProject;

    private TargetDescription m_targetDesciption;

    private String m_captionString = "";

    private boolean m_disableSelectionUpdates = false;

    private static final int SECTION_STYLE = ExpandableComposite.TWISTIE | ExpandableComposite.TITLE_BAR | ExpandableComposite.TITLE_BAR;

    private static final int GROUP_STYLE = SWT.SHADOW_NONE;

    private FormToolkit m_toolkit;

    private ScrolledForm m_form;

    private Group m_targetsGroup;

    private Group m_optionsGroup;

    private AddTargetsFileAction m_addTargetsFileAction;

    private Action m_addNewTargetAction;

    private Action m_buildTargetAction;

    private Action m_copyToClipboardAction;

    private Action m_setToProjectAction;

    private Action m_refreshViewAction;

    private ProjectSelectionListener m_projectSelectionListener;

    private final SelectionListener m_buttonSelectionListener = new SelectionAdapter() {

        @Override
        public void widgetSelected(SelectionEvent e) {
            onSelectionChanged(m_currentProject);
        }
    };

    private final IExpansionListener m_expansionListener = new ExpansionAdapter() {

        @Override
        public void expansionStateChanged(ExpansionEvent e) {
            m_form.reflow(true);
        }
    };

    private final List<Button> m_optionButtonsList = new LinkedList<Button>();

    private final List<Group> m_optionGroupsList = new LinkedList<Group>();

    @Override
    public void createPartControl(Composite parent) {
        initToolBar(parent.getShell());
        m_toolkit = new FormToolkit(parent.getDisplay());
        m_form = m_toolkit.createScrolledForm(parent);
        m_form.getBody().setLayout(new ColumnLayout());
        {
            final Section targetsSection = m_toolkit.createSection(m_form.getBody(), SECTION_STYLE);
            targetsSection.setText(Messages.MainView_targets_section);
            targetsSection.addExpansionListener(m_expansionListener);
            targetsSection.setExpanded(true);
            m_targetsGroup = new Group(targetsSection, GROUP_STYLE);
            m_targetsGroup.setLayout(new RowLayout(SWT.VERTICAL));
            m_toolkit.adapt(m_targetsGroup);
            targetsSection.setClient(m_targetsGroup);
        }
        {
            final Section optionsSection = m_toolkit.createSection(m_form.getBody(), SECTION_STYLE);
            optionsSection.setText(Messages.MainView_options_section);
            optionsSection.addExpansionListener(m_expansionListener);
            optionsSection.setExpanded(true);
            m_optionsGroup = new Group(optionsSection, GROUP_STYLE);
            m_optionsGroup.setLayout(new RowLayout(SWT.VERTICAL));
            m_toolkit.adapt(m_optionsGroup);
            optionsSection.setClient(m_optionsGroup);
        }
        m_projectSelectionListener = new ProjectSelectionListener(this);
        getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(m_projectSelectionListener);
        updateActionsAndForm();
    }

    @Override
    public void dispose() {
        getSite().getWorkbenchWindow().getSelectionService().removePostSelectionListener(m_projectSelectionListener);
        m_toolkit.dispose();
        super.dispose();
    }

    @Override
    public void setFocus() {
        m_form.setFocus();
    }

    private void initToolBar(Shell shell) {
        m_addTargetsFileAction = new AddTargetsFileAction(this);
        m_addNewTargetAction = new AddNewTargetAction(this);
        m_buildTargetAction = new BuildTargetAction(this, shell);
        m_copyToClipboardAction = new CopyTargetToClipboardAction(this);
        m_setToProjectAction = new SetTargetToProjectSettings(this);
        m_refreshViewAction = new RefreshViewAction(this);
        IToolBarManager tm = getViewSite().getActionBars().getToolBarManager();
        tm.add(m_buildTargetAction);
        tm.add(m_setToProjectAction);
        tm.add(m_addNewTargetAction);
        tm.add(m_copyToClipboardAction);
        tm.add(m_refreshViewAction);
        tm.add(m_addTargetsFileAction);
    }

    private void updateActionsAndForm() {
        switch(m_state) {
            case STATE_UPDATING:
                m_addTargetsFileAction.update();
                m_addTargetsFileAction.setEnabled(false);
                m_addNewTargetAction.setEnabled(false);
                m_buildTargetAction.setEnabled(false);
                m_copyToClipboardAction.setEnabled(false);
                m_setToProjectAction.setEnabled(false);
                m_refreshViewAction.setEnabled(false);
                m_form.setVisible(false);
                setContentDescription(Messages.MainView_updating_vew_status);
                break;
            case STATE_ACTUAL:
                {
                    final boolean hasProject = Util.checkProjectOpen(m_currentProject);
                    m_addTargetsFileAction.update();
                    m_addTargetsFileAction.setEnabled(hasProject);
                    if (hasProject) {
                        final boolean hasFile = Util.isFileExists(Util.getTragetsFile(m_currentProject));
                        m_addNewTargetAction.setEnabled(hasFile);
                        m_buildTargetAction.setEnabled(hasFile);
                        m_copyToClipboardAction.setEnabled(hasFile);
                        m_setToProjectAction.setEnabled(hasFile);
                        m_refreshViewAction.setEnabled(hasFile);
                        m_form.setVisible(hasFile);
                        setContentDescription(m_currentProject.getName());
                    } else {
                        m_addNewTargetAction.setEnabled(false);
                        m_buildTargetAction.setEnabled(false);
                        m_copyToClipboardAction.setEnabled(false);
                        m_setToProjectAction.setEnabled(false);
                        m_refreshViewAction.setEnabled(false);
                        m_form.setVisible(false);
                        this.setContentDescription(Messages.MainView_no_project);
                    }
                }
                break;
        }
    }

    /**
	 * Method to be called when we want to notify view that it should 
	 * update the contents because the targets file could have been changed.
	 */
    public void onTargetsFileChanged() {
        scheduleUpdate(m_currentProject, true);
    }

    public void onSelectedProjectChanged(IProject project) {
        scheduleUpdate(project, false);
    }

    public void updateTardetsDescriptionText() {
        updateTardetsDescriptionText(getCurrentProject());
    }

    private void updateTardetsDescriptionText(IProject project) {
        m_form.setText(Messages.MainView_MainView_TargetDescription_1 + constructDispayTargetString(Util.getTargetDescriptionFromProject(project)) + "\n" + Messages.MainView_MainView_TargetDescription_2 + constructDispayTargetString(getTargetDescription()));
    }

    private void scheduleUpdate(IProject newProject, boolean forceReparse) {
        final Job job = new UpdateJob(this, newProject, forceReparse);
        job.schedule();
    }

    /**
	 * not to be run directly, run via call to scheduleUpdate() 
	 */
    private void update(IProject newProject, boolean forceReparse) {
        synchronized (m_stateLock) {
            switch(m_state) {
                case STATE_ACTUAL:
                    if (!forceReparse) {
                        if (m_currentProject == null && newProject == null) {
                            return;
                        }
                        if (m_currentProject != null && m_currentProject.equals(newProject)) {
                            return;
                        }
                    }
                    m_currentProject = null;
                    m_wantedProject = null;
                    m_parsingProject = null;
                    m_parsingProject = newProject;
                    m_state = State.STATE_UPDATING;
                    break;
                case STATE_UPDATING:
                    if (!forceReparse && m_parsingProject.equals(newProject)) {
                        m_wantedProject = null;
                        return;
                    }
                    m_wantedProject = newProject;
                    break;
            }
        }
        try {
            updateActionsAndForm();
            if (!Util.checkProjectOpen(m_parsingProject)) {
                return;
            }
            final IFile targetsFile = Util.getTragetsFile(m_parsingProject);
            if (!Util.isFileExists(targetsFile)) {
                return;
            }
            processParse(targetsFile);
        } finally {
            IProject restartWithProject = null;
            synchronized (m_stateLock) {
                m_currentProject = m_parsingProject;
                m_parsingProject = null;
                if (m_wantedProject != null) {
                    restartWithProject = m_wantedProject;
                    m_wantedProject = null;
                }
                m_state = State.STATE_ACTUAL;
            }
            if (restartWithProject != null) {
                update(restartWithProject, false);
            } else {
                updateActionsAndForm();
            }
        }
    }

    /**
	 * 
	 */
    private void processParse(IFile targetsFile) {
        try {
            m_disableSelectionUpdates = true;
            parseFile(targetsFile);
        } finally {
            m_disableSelectionUpdates = false;
        }
        onSelectionChanged(m_parsingProject);
    }

    /**
	 * @param file
	 */
    private void parseFile(IFile file) {
        if (file == null || !file.isAccessible()) {
            return;
        }
        cleanupView();
        try {
            InputStream inputStream = file.getContents();
            final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            Element rootElement = doc.getDocumentElement();
            if (!rootElement.getNodeName().equals(Plugin.MT_XML_ROOT_ELEMENT_NAME)) {
                inputStream.close();
                throw new IllegalArgumentException(Messages.MainView_error1_1 + rootElement.getNodeName() + Messages.MainView_error1_2 + Plugin.MT_XML_ROOT_ELEMENT_NAME + Messages.MainView_error1_3);
            }
            ISelectedStateData selectedState = Plugin.getInstance().getSelectedState(m_parsingProject);
            {
                NodeList targetSections = rootElement.getElementsByTagName(Plugin.MT_XML_TARGETS_SECTION_ELEMENT_NAME);
                for (int i = 0; i < targetSections.getLength(); ++i) {
                    processTargetsSection((Element) targetSections.item(i), selectedState);
                }
            }
            {
                NodeList optionsSections = rootElement.getElementsByTagName(Plugin.MT_XML_OPTIONS_SECTION_ELEMENT_NAME);
                for (int i = 0; i < optionsSections.getLength(); ++i) {
                    processOptionsSection((Element) optionsSections.item(i), selectedState);
                }
            }
            inputStream.close();
        } catch (CoreException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        m_form.reflow(true);
    }

    private void cleanupView() {
        for (Widget widget : m_targetsGroup.getChildren()) {
            widget.dispose();
        }
        for (Widget widget : m_optionsGroup.getChildren()) {
            widget.dispose();
        }
        m_form.reflow(true);
        m_optionButtonsList.clear();
        m_optionGroupsList.clear();
    }

    private void processTargetsSection(Element section, ISelectedStateData selectedState) {
        if (section == null) {
            return;
        }
        final NodeList targets = section.getElementsByTagName(Plugin.MT_XML_TARGET_ELEMENT_NAME);
        for (int i = 0; i < targets.getLength(); ++i) {
            final Element target = (Element) targets.item(i);
            addButton(m_targetsGroup, true, target.getAttribute(Plugin.MT_XML_TARGET_ELEMENT_TEXT_ATTR), new TargetDescription(target.getAttribute(Plugin.MT_XML_TARGET_ELEMENT_COMMAND_ATTR), target.getAttribute(Plugin.MT_XML_TARGET_ELEMENT_BUILD_COMMAND_ATTR), target.getAttribute(Plugin.MT_XML_TARGET_ELEMENT_BUILD_LOCATION_ATTR)), target.getAttribute(Plugin.MT_XML_TARGET_ELEMENT_HINT_ATTR));
        }
        Util.setSelectedRadioButton(m_targetsGroup, selectedState.getSelectedTarget());
    }

    private void processOptionsSection(Element section, ISelectedStateData selectedState) {
        if (section == null) {
            return;
        }
        final NodeList sectionChildren = section.getChildNodes();
        for (int i = 0; i < sectionChildren.getLength(); ++i) {
            final Node node = sectionChildren.item(i);
            if (node.getNodeName().equals(Plugin.MT_XML_OPTION_ELEMENT_NAME)) {
                final Element element = (Element) node;
                final Button button = addButton(m_optionsGroup, false, element.getAttribute(Plugin.MT_XML_OPTION_ELEMENT_TEXT_ATTR), element.getAttribute(Plugin.MT_XML_OPTION_ELEMENT_COMMAND_ATTR), element.getAttribute(Plugin.MT_XML_OPTION_ELEMENT_HINT_ATTR));
                if (selectedState.isOptionSelected(button.getText())) {
                    button.setSelection(true);
                }
                m_optionButtonsList.add(button);
            } else if (node.getNodeName().equals(Plugin.MT_XML_OPTIONS_GROUP_ELEMENT_NAME)) {
                addOptionsGroup((Element) node, selectedState);
            }
        }
    }

    private void addOptionsGroup(Element groupElement, ISelectedStateData selectedState) {
        final Group group = addOptionsGroup(groupElement.getAttribute(Plugin.MT_XML_OPTIONS_GROUP_ELEMENT_TEXT_ATTR), groupElement.getAttribute(Plugin.MT_XML_OPTIONS_GROUP_ELEMENT_HINT_ATTR));
        final NodeList options = groupElement.getElementsByTagName(Plugin.MT_XML_OPTION_ELEMENT_NAME);
        for (int i = 0; i < options.getLength(); ++i) {
            final Element option = (Element) options.item(i);
            addButton(group, true, option.getAttribute(Plugin.MT_XML_OPTION_ELEMENT_TEXT_ATTR), option.getAttribute(Plugin.MT_XML_OPTION_ELEMENT_COMMAND_ATTR), option.getAttribute(Plugin.MT_XML_OPTION_ELEMENT_HINT_ATTR));
        }
        Util.setSelectedRadioButton(group, selectedState.getSelectedOptionGroupElement(group.getText()));
        m_optionGroupsList.add(group);
    }

    private Button addButton(Composite groupControl, boolean isRadio, String caption, Object data, String hint) {
        final Button button = m_toolkit.createButton(groupControl, caption, isRadio ? SWT.RADIO : SWT.CHECK);
        button.setData(data);
        button.setToolTipText(hint);
        button.addSelectionListener(m_buttonSelectionListener);
        return button;
    }

    private Group addOptionsGroup(String groupName, String hint) {
        final Group group = new Group(m_optionsGroup, GROUP_STYLE);
        group.setLayout(new RowLayout(SWT.VERTICAL));
        m_toolkit.adapt(group);
        group.setText(groupName);
        group.setToolTipText(hint);
        return group;
    }

    private void onSelectionChanged(IProject projectToStoreSelection) {
        if (m_disableSelectionUpdates) {
            return;
        }
        final SelectedState selectedState = new SelectedState();
        final StringBuilder targetStringBuilder = new StringBuilder();
        final StringBuilder captionStringBuilder = new StringBuilder();
        String captionString = "";
        String buildCommand = null;
        String buildLocation = null;
        {
            for (Button button : m_optionButtonsList) {
                if (Util.isButtonSelected(button)) {
                    targetStringBuilder.append(button.getData().toString()).append(Plugin.MT_TARGET_LINE_SEPARATOR);
                    captionStringBuilder.append(button.getText()).append(Plugin.MT_CAPTION_LINE_SEPARATOR);
                    selectedState.addSelectedOption(button.getText());
                }
            }
        }
        {
            for (Group group : m_optionGroupsList) {
                final Button selectedButton = Util.getSelectedRadioButton(group);
                if (Util.isButtonSelected(selectedButton)) {
                    targetStringBuilder.append(selectedButton.getData().toString()).append(Plugin.MT_TARGET_LINE_SEPARATOR);
                    captionStringBuilder.append(selectedButton.getText()).append(Plugin.MT_CAPTION_LINE_SEPARATOR);
                    selectedState.setSelectedOptionGroupElement(group.getText(), selectedButton.getText());
                }
            }
        }
        {
            final Button selectedButton = Util.getSelectedRadioButton(m_targetsGroup);
            if (Util.isButtonSelected(selectedButton)) {
                TargetDescription descr = (TargetDescription) selectedButton.getData();
                targetStringBuilder.append(descr.getTragetCommand());
                buildCommand = descr.getBuildCommand();
                buildLocation = descr.getBuildLocation();
                captionString = selectedButton.getText() + Plugin.MT_CAPTION_LINE_SEPARATOR;
                selectedState.setSelectedTarget(selectedButton.getText());
            }
        }
        String targetString = targetStringBuilder.toString().trim();
        captionString = captionString + captionStringBuilder.toString().trim();
        setTargetString(targetString, buildCommand, buildLocation);
        setCaptionString(captionString);
        Plugin.getInstance().setSelectedState(projectToStoreSelection, selectedState);
        updateTardetsDescriptionText(projectToStoreSelection);
    }

    private String constructDispayTargetString(TargetDescription targetDescr) {
        if (targetDescr == null) {
            return "";
        }
        return constructDispayTargetString(targetDescr.getTragetCommand(), targetDescr.getBuildCommand(), targetDescr.getBuildLocation());
    }

    private String constructDispayTargetString(String targetString, String buildCommand, String buildLocation) {
        return (buildLocation.isEmpty() ? Messages.MainView_MainView_TargetDescription_BuildLocationDefault : Messages.MainView_MainView_TargetDescription_BuildLocationBegin + buildLocation + Messages.MainView_MainView_TargetDescription_BuildLocationEnd) + (buildCommand.isEmpty() ? Messages.MainView_MainView_TargetDescription_BuildCommandDefault : Messages.MainView_MainView_TargetDescription_BuildCommandBegin + buildCommand + Messages.MainView_MainView_TargetDescription_BuildCommandEnd) + targetString;
    }

    /** result is not null*/
    public TargetDescription getTargetDescription() {
        return m_targetDesciption;
    }

    /**
	 * @param targetString cannot be null
	 * @param buildCommand cannot be null
	 * @param buildLocation cannot be null
	 */
    void setTargetString(String targetString, String buildCommand, String buildLocation) {
        if (targetString == null) {
            throw new NullPointerException(Messages.Plugin_error2);
        }
        if (buildCommand == null) {
            throw new NullPointerException(Messages.Plugin_error4);
        }
        if (buildLocation == null) {
            throw new NullPointerException(Messages.Plugin_error5);
        }
        m_targetDesciption = new TargetDescription(targetString, buildCommand, buildLocation);
    }

    /** result is not null*/
    public String getCaptionString() {
        return m_captionString;
    }

    /** param cannot be null*/
    void setCaptionString(String string) {
        if (string == null) {
            throw new NullPointerException(Messages.Plugin_error3);
        }
        m_captionString = string;
    }

    public IProject getCurrentProject() {
        return m_currentProject;
    }
}
