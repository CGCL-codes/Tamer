package edu.harvard.fas.rregan.requel.project.impl.command;

import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import edu.harvard.fas.rregan.command.CommandHandler;
import edu.harvard.fas.rregan.requel.annotation.command.AnnotationCommandFactory;
import edu.harvard.fas.rregan.requel.project.Project;
import edu.harvard.fas.rregan.requel.project.ProjectRepository;
import edu.harvard.fas.rregan.requel.project.ProjectUserRole;
import edu.harvard.fas.rregan.requel.project.Stakeholder;
import edu.harvard.fas.rregan.requel.project.StakeholderPermission;
import edu.harvard.fas.rregan.requel.project.UserStakeholder;
import edu.harvard.fas.rregan.requel.project.command.EditReportGeneratorCommand;
import edu.harvard.fas.rregan.requel.project.command.ImportProjectCommand;
import edu.harvard.fas.rregan.requel.project.command.ProjectCommandFactory;
import edu.harvard.fas.rregan.requel.project.impl.ProjectImpl;
import edu.harvard.fas.rregan.requel.project.impl.UserStakeholderImpl;
import edu.harvard.fas.rregan.requel.project.impl.assistant.AssistantFacade;
import edu.harvard.fas.rregan.requel.user.User;
import edu.harvard.fas.rregan.requel.user.UserRepository;
import edu.harvard.fas.rregan.requel.user.exception.NoSuchUserException;
import edu.harvard.fas.rregan.requel.utils.jaxb.UnmarshallerListener;

/**
 * @author ron
 */
@Controller("importProjectCommand")
@Scope("prototype")
public class ImportProjectCommandImpl extends AbstractEditProjectCommand implements ImportProjectCommand {

    private Project project;

    private InputStream inputStream;

    private String name;

    private boolean analysisEnabled = false;

    /**
	 * @param assistantManager
	 * @param userRepository
	 * @param projectRepository
	 * @param projectCommandFactory
	 * @param annotationCommandFactory
	 * @param commandHandler
	 */
    @Autowired
    public ImportProjectCommandImpl(AssistantFacade assistantManager, UserRepository userRepository, ProjectRepository projectRepository, ProjectCommandFactory projectCommandFactory, AnnotationCommandFactory annotationCommandFactory, CommandHandler commandHandler) {
        super(assistantManager, userRepository, projectRepository, projectCommandFactory, annotationCommandFactory, commandHandler);
    }

    /**
	 * @see edu.harvard.fas.rregan.requel.project.command.ImportProjectCommand#getProject()
	 */
    @Override
    public Project getProject() {
        return project;
    }

    /**
	 * @see edu.harvard.fas.rregan.requel.project.command.ImportProjectCommand#setProject(edu.harvard.fas.rregan.requel.project.Project)
	 */
    @Override
    public void setProject(Project project) {
        this.project = project;
    }

    /**
	 * @see edu.harvard.fas.rregan.requel.project.command.ImportProjectCommand#setInputStream(java.io.InputStream)
	 */
    @Override
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    protected InputStream getInputStream() {
        return inputStream;
    }

    protected String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected boolean isAnalysisEnabled() {
        return analysisEnabled;
    }

    public void setAnalysisEnabled(boolean analysisEnabled) {
        this.analysisEnabled = analysisEnabled;
    }

    /**
	 * @see edu.harvard.fas.rregan.command.Command#execute()
	 */
    @Override
    public void execute() {
        try {
            User createdBy = getUserRepository().get(getEditedBy());
            JAXBContext context = JAXBContext.newInstance(ExportProjectCommandImpl.CLASSES_FOR_JAXB);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setListener(new UnmarshallerListener(getProjectRepository(), getUserRepository(), createdBy, getName()));
            ProjectImpl project = (ProjectImpl) unmarshaller.unmarshal(getInputStream());
            if (project.getCreatedBy() == null) {
                project.setCreatedBy(createdBy);
            }
            addUserAsStakeholder(project, createdBy, createdBy);
            try {
                addUserAsStakeholder(project, getUserRepository().findUserByUsername("assistant"), createdBy);
            } catch (NoSuchUserException e) {
                log.warn("The assistant user doesn't exist and could not " + " be added as a stakeholder to " + project.getName());
            }
            setProject(getProjectRepository().persist(project));
            for (Stakeholder stakeholder : project.getStakeholders()) {
                if (stakeholder.isUserStakeholder()) {
                    ((UserStakeholder) stakeholder).getUser().getRoleForType(ProjectUserRole.class).getActiveProjects().add(project);
                }
            }
            if (project.getReportGenerators().isEmpty()) {
                addBuiltinReportGenerator(project, createdBy);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void invokeAnalysis() {
        if (isAnalysisEnabled()) {
            getAssistantManager().analyzeProject(getProject());
        }
    }

    private void addUserAsStakeholder(Project project, User user, User editedBy) {
        if (user.hasRole(ProjectUserRole.class)) {
            boolean alreadyAStakeholder = false;
            for (Stakeholder stakeholder : project.getStakeholders()) {
                if (stakeholder.isUserStakeholder() && user.equals(((UserStakeholder) stakeholder).getUser())) {
                    alreadyAStakeholder = true;
                    break;
                }
            }
            if (!alreadyAStakeholder) {
                UserStakeholder creatorStakeholder = new UserStakeholderImpl(project, editedBy, user);
                getProjectRepository().persist(creatorStakeholder);
                for (StakeholderPermission permission : getProjectRepository().findAvailableStakeholderPermissions()) {
                    creatorStakeholder.grantStakeholderPermission(permission);
                }
                project.getStakeholders().add(creatorStakeholder);
            }
        }
    }

    private void addBuiltinReportGenerator(Project project, User user) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(EditProjectCommandImpl.BUILTIN_REPORT_GENERATOR_PATH);
            EditReportGeneratorCommand command = getProjectCommandFactory().newEditReportGeneratorCommand();
            command.setEditedBy(user);
            command.setProjectOrDomain(project);
            command.setName("HTML Specification");
            command.setText(IOUtils.toString(inputStream));
            getCommandHandler().execute(command);
        } catch (Exception e) {
            log.error("The builtin report generator could not be added to " + project, e);
        }
    }
}
