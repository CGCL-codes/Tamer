package easyaccept.webcontrol.client.widgets.project;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Hidden;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.ContainerLayout;
import easyaccept.webcontrol.client.msg.MessageKeys;
import easyaccept.webcontrol.client.services.projectmanagement.ProjectManagementService;
import easyaccept.webcontrol.client.util.DefaultCallback;
import easyaccept.webcontrol.client.util.GWTUtil;
import easyaccept.webcontrol.client.util.ext.DesktopWindowManager;
import easyaccept.webcontrol.pojo.ProjectGwt;

/**
 * Project Register Widget
 * 
 * @author Fabrício Silva
 * 
 */
public class ProjectRegisterWindow extends Window {

    static final MessageKeys MSG = GWTUtil.MSG;

    private ProjectForm projForm;

    private ProjectRegisterWindow _thisWindow = this;

    public ProjectRegisterWindow() {
        super();
        init();
        configLayout();
        DesktopWindowManager.register(this);
    }

    public ProjectRegisterWindow(ProjectGwt project) {
        super();
        init();
        configLayout();
        DesktopWindowManager.register(this);
        populateProject(project);
    }

    private void populateProject(ProjectGwt project) {
        this.projForm.populate(project);
    }

    public void init() {
        setTitle(MSG.app_form_create() + MSG.project_entity());
        setId("ProjectRegisterWindow");
        projForm = newFormInstance();
        BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
        centerData.setMargins(3, 0, 3, 3);
        add(projForm, centerData);
    }

    protected ProjectForm newFormInstance() {
        return new ProjectForm();
    }

    public void configLayout() {
        if (!this.isRendered()) {
            setIconCls("update-icon");
            setClosable(true);
            setWidth(600);
            setHeight(420);
            setPlain(true);
            setMaximizable(true);
            setResizable(true);
            setCloseAction(Window.CLOSE);
            ContainerLayout layout = new BorderLayout();
            setLayout(layout);
        }
    }

    /**
	 * Project Form
	 * 
	 * @author Fabrício Silva
	 * 
	 */
    class ProjectForm extends FormPanel {

        private Hidden idProject;

        private TextField name;

        private TextField shortName;

        private TextArea description;

        private Button registerBtn;

        public ProjectForm() {
            initForm();
        }

        public void initForm() {
            idProject = new Hidden("idProject", null);
            add(idProject);
            name = new TextField(MSG.project_name(), "name");
            name.setAllowBlank(false);
            name.setMaxLength(300);
            name.setMaxLengthText(MSG.errors_maxlength(MSG.project_name(), "300"));
            add(name, new AnchorLayoutData("95%"));
            shortName = new TextField(MSG.project_shortName(), "shortName");
            shortName.setAllowBlank(false);
            shortName.setMaxLength(30);
            shortName.setMaxLengthText(MSG.errors_maxlength(MSG.project_shortName(), "30"));
            add(shortName, new AnchorLayoutData("95%"));
            description = new TextArea(MSG.project_descr(), "description");
            description.setMaxLength(2000);
            description.setMaxLengthText(MSG.errors_maxlength(MSG.project_descr(), "2000"));
            description.setTitle(MSG.errors_required(MSG.project_descr()));
            description.setAllowBlank(false);
            add(description, new AnchorLayoutData("98% -120"));
            registerBtn = new Button(MSG.app_button_save());
            registerBtn.addListener(new ButtonListenerAdapter() {

                public void onClick(Button button, EventObject e) {
                    doSave();
                }
            });
            addButton(registerBtn);
            registerBtn.setFormBind(true);
            setMonitorValid(true);
            setFrame(true);
            setPaddings(5, 5, 5, 0);
            setLabelAlign(Position.TOP);
        }

        public void populate(ProjectGwt project) {
            idProject.setValue(project.getId() + "");
            name.setValue(project.getName());
            shortName.setValue(project.getShortName());
            description.setValue(project.getDescription());
        }

        public ProjectGwt createProjectGwt() {
            ProjectGwt project = new ProjectGwt();
            project.setId(GWTUtil.parseInt(idProject.getValueAsString()));
            project.setName(name.getValueAsString());
            project.setShortName(shortName.getValueAsString());
            project.setDescription(description.getValueAsString());
            return project;
        }

        public void doSave() {
            if (getForm().isValid()) {
                ProjectGwt project = createProjectGwt();
                ProjectManagementService.ASYNC.i().createNewProject(project, new DefaultCallback<ProjectGwt>() {

                    public void onSuccess(ProjectGwt project) {
                        populate(project);
                        showSuccess();
                        _thisWindow.destroy();
                    }
                });
            }
        }
    }

    public ProjectForm getProjectForm() {
        return this.projForm;
    }
}
