package org.telscenter.sail.webapp.presentation.web.controllers.teacher.run;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.sail.webapp.dao.ObjectNotFoundException;
import net.sf.sail.webapp.domain.User;
import net.sf.sail.webapp.domain.group.Group;
import net.sf.sail.webapp.domain.impl.CurnitGetCurnitUrlVisitor;
import net.sf.sail.webapp.mail.IMailFacade;
import net.sf.sail.webapp.presentation.web.controllers.ControllerUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.telscenter.sail.webapp.domain.Run;
import org.telscenter.sail.webapp.domain.authentication.impl.TeacherUserDetails;
import org.telscenter.sail.webapp.domain.brainstorm.Brainstorm;
import org.telscenter.sail.webapp.domain.brainstorm.answer.Answer;
import org.telscenter.sail.webapp.domain.brainstorm.answer.PreparedAnswer;
import org.telscenter.sail.webapp.domain.brainstorm.answer.Revision;
import org.telscenter.sail.webapp.domain.brainstorm.answer.impl.AnswerImpl;
import org.telscenter.sail.webapp.domain.brainstorm.answer.impl.RevisionImpl;
import org.telscenter.sail.webapp.domain.impl.DefaultPeriodNames;
import org.telscenter.sail.webapp.domain.impl.RunParameters;
import org.telscenter.sail.webapp.domain.project.Project;
import org.telscenter.sail.webapp.domain.project.ProjectMetadata;
import org.telscenter.sail.webapp.domain.workgroup.WISEWorkgroup;
import org.telscenter.sail.webapp.service.brainstorm.BrainstormService;
import org.telscenter.sail.webapp.service.offering.RunService;
import org.telscenter.sail.webapp.service.project.ProjectService;
import org.telscenter.sail.webapp.service.workgroup.WISEWorkgroupService;

/**
 * Controller for the wizard to "create a run"
 * 
 * The default getTargetPage() method is used to find out which page to navigate to, so
 * the controller looks for a request parameter starting with "_target" and ending with
 * a number (e.g. "_target1"). The jsp pages should provide these parameters.
 *
 * General method invocation flow (when user clicks on "prev" and "next"): 
 *  1) onBind
 *  2) onBindAndValidate
 *  3) validatePage
 *  4) referenceData
 * Note that on user's first visit to the first page of the wizard, only referenceData will be
 * invoked, and steps 1-3 are bypassed.
 *
 * @author Hiroki Terashima
 * @version $Id: CreateRunController.java 941 2007-08-16 14:03:11Z laurel $
 */
public class CreateRunController extends AbstractWizardFormController {

    private RunService runService = null;

    private WISEWorkgroupService workgroupService = null;

    private ProjectService projectService = null;

    private BrainstormService brainstormService = null;

    private static final String COMPLETE_VIEW_NAME = "teacher/run/create/createrunfinish";

    private static final String CANCEL_VIEW_NAME = "../../teacher/index.html";

    private static final String RUN_KEY = "run";

    private IMailFacade javaMail = null;

    private Properties emaillisteners = null;

    protected Properties uiHTMLProperties = null;

    protected Properties portalProperties;

    private static final Boolean DEBUG = false;

    private static final String DEBUG_EMAIL = "youremail@email.com";

    private static final Long[] IMPLEMENTED_POST_LEVELS = { 5l, 1l };

    private static final Map<Long, String> POST_LEVEL_TEXT_MAP = new HashMap<Long, String>();

    static {
        POST_LEVEL_TEXT_MAP.put(5l, "High (more storage intensive; student activity in every step is recorded)");
        POST_LEVEL_TEXT_MAP.put(1l, "Low  (less storage intensive; only work directly submitted by student is recorded)");
    }

    /**
	 * Constructor
	 *  - Specify the pages in the wizard
	 *  - Specify the command name
	 */
    public CreateRunController() {
        setBindOnNewForm(true);
        setPages(new String[] { "teacher/run/create/createrunconfirm", "teacher/run/create/createrunarchive", "teacher/run/create/createrunperiods", "teacher/run/create/createrunconfigure", "teacher/run/create/createrunreview" });
        setSessionForm(true);
    }

    /**
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#onBind(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException)
	 */
    @Override
    protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
        super.onBind(request, command, errors);
    }

    /**
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
    @Override
    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
        super.onBindAndValidate(request, command, errors, page);
    }

    /**
	 * This method is called after the onBind and onBindAndValidate method. It acts 
	 * in the same way as the validator
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#validatePage(java.lang.Object, org.springframework.validation.Errors, int)
	 */
    @Override
    protected void validatePage(Object command, Errors errors, int page) {
        super.validatePage(command, errors, page);
        RunParameters runParameters = (RunParameters) command;
        switch(page) {
            case 0:
                User user = ControllerUtil.getSignedInUser();
                if (!this.projectService.canCreateRun(runParameters.getProject(), user)) {
                    errors.rejectValue("project", "not.authorized", "You are not authorized to set up a run with this project.");
                }
                break;
            case 1:
                break;
            case 2:
                if (runParameters.getPeriodNames() == null || runParameters.getPeriodNames().size() == 0) {
                    if (runParameters.getManuallyEnteredPeriods() == "") {
                        errors.rejectValue("periodNames", "setuprun.error.selectperiods", "You must select one or more periods or manually" + " create your period names.");
                    } else {
                        if (runParameters.getManuallyEnteredPeriods() == null || StringUtils.trim(runParameters.getManuallyEnteredPeriods()).length() == 0 || StringUtils.trim(runParameters.getManuallyEnteredPeriods()).equals(",")) {
                            errors.rejectValue("periodNames", "setuprun.error.selectperiods", "You must select one or more periods or manually" + " create your period names.");
                        } else {
                            String[] parsed = StringUtils.split(runParameters.getManuallyEnteredPeriods(), ",");
                            if (parsed.length == 0) {
                                errors.rejectValue("periodNames", "setuprun.error.whitespaceornonalphanumeric", "Manually entered" + " periods cannot contain whitespace or non-alphanumeric characters.");
                                break;
                            }
                            Set<String> parsedAndTrimmed = new TreeSet<String>();
                            for (String current : parsed) {
                                String trimmed = StringUtils.trim(current);
                                if (trimmed.length() == 0 || StringUtils.contains(trimmed, " ") || !StringUtils.isAlphanumeric(trimmed) || trimmed.equals(",")) {
                                    errors.rejectValue("periodNames", "setuprun.error.whitespaceornonalphanumeric", "Manually entered" + " periods cannot contain whitespace or non-alphanumeric characters.");
                                    break;
                                } else {
                                    parsedAndTrimmed.add(trimmed);
                                }
                            }
                            runParameters.setPeriodNames(parsedAndTrimmed);
                            runParameters.setManuallyEnteredPeriods("");
                        }
                    }
                } else if (runParameters.getManuallyEnteredPeriods() != "") {
                    errors.rejectValue("periodNames", "setuprun.error.notsupported", "Selecting both periods AND" + " manually entering periods is not supported.");
                }
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }
    }

    /**
	 * This method is called right before the view is rendered to the user
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#referenceData(javax.servlet.http.HttpServletRequest, int)
	 */
    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors, int page) {
        String projectId = request.getParameter("projectId");
        RunParameters runParameters = (RunParameters) command;
        Project project = null;
        Map<String, Object> model = new HashMap<String, Object>();
        User user = ControllerUtil.getSignedInUser();
        switch(page) {
            case 0:
                try {
                    project = (Project) this.projectService.getById(projectId);
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }
                model.put("project", project);
                Set<User> owners = new HashSet<User>();
                owners.add(user);
                runParameters.setOwners(owners);
                runParameters.setProject(project);
                runParameters.setName(project.getProjectInfo().getName());
                String ownerUsernames = "";
                Set<User> allOwners = project.getOwners();
                allOwners.addAll(project.getSharedowners());
                for (User currentOwner : allOwners) {
                    ownerUsernames += currentOwner.getUserDetails().getUsername() + ",";
                }
                model.put("projectOwners", ownerUsernames.substring(0, ownerUsernames.length() - 1));
                boolean forceCleaning = false;
                boolean isAllowedToClean = (project.getOwners().contains(user) || project.getSharedowners().contains(user));
                ProjectMetadata metadata = project.getMetadata();
                if (metadata != null) {
                    Date lastCleaned = metadata.getLastCleaned();
                    Date lastEdited = metadata.getLastEdited();
                    if (lastCleaned != null && lastEdited != null && lastCleaned.before(lastEdited)) {
                        forceCleaning = true;
                    }
                }
                forceCleaning = false;
                model.put("currentUsername", user.getUserDetails().getUsername());
                model.put("forceCleaning", forceCleaning);
                model.put("isAllowedToClean", isAllowedToClean);
                break;
            case 1:
                List<Run> allRuns = runService.getRunList();
                List<Run> currentRuns = new ArrayList<Run>();
                for (Run run : allRuns) {
                    if (run.getOwners().contains(user) && !run.isEnded()) {
                        currentRuns.add(run);
                    }
                }
                model.put("existingRunList", currentRuns);
                break;
            case 2:
                model.put("periodNames", DefaultPeriodNames.values());
                break;
            case 3:
                try {
                    project = (Project) this.projectService.getById(projectId);
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }
                model.put("implementedPostLevels", IMPLEMENTED_POST_LEVELS);
                model.put("postLevelTextMap", POST_LEVEL_TEXT_MAP);
                model.put("minPostLevel", this.getMinPostLevel(project));
                break;
            case 4:
                try {
                    project = (Project) this.projectService.getById(projectId);
                } catch (ObjectNotFoundException e) {
                    e.printStackTrace();
                }
                String curriculumBaseDir = this.portalProperties.getProperty("curriculum_base_dir");
                String relativeProjectFilePath = (String) project.getCurnit().accept(new CurnitGetCurnitUrlVisitor());
                int ndx = relativeProjectFilePath.lastIndexOf("/");
                String srcProjectRootFolder = curriculumBaseDir + "/" + relativeProjectFilePath.substring(0, ndx);
                String projectJSONFilename = relativeProjectFilePath.substring(ndx + 1, relativeProjectFilePath.length());
                String projectName = project.getName();
                projectName = projectName.replaceAll("\\'", "\\\\'");
                model.put("projectId", projectId);
                model.put("projectType", project.getProjectType());
                model.put("projectName", projectName);
                model.put("srcProjectRootFolder", srcProjectRootFolder);
                model.put("projectJSONFilename", projectJSONFilename);
                model.put("curriculumBaseDir", curriculumBaseDir);
                break;
            default:
                break;
        }
        return model;
    }

    /**
	 * Retrieves the post level from the project metadata if it exists and determines
	 * the minimum post level that the user can set for the run.
	 * 
	 * @param project
	 * @return
	 */
    private Long getMinPostLevel(Project project) {
        Long level = 1l;
        ProjectMetadata metadata = project.getMetadata();
        if (metadata != null && metadata.getPostLevel() != null) {
            level = metadata.getPostLevel();
        }
        return level;
    }

    /**
	 * Creates a run.
	 * 
	 * This method is called if there is a submit that validates and contains the "_finish"
	 * request parameter.
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
    @Override
    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        RunParameters runParameters = (RunParameters) command;
        Run run = null;
        try {
            String newProjectId = request.getParameter("newProjectId");
            Project newProject = projectService.getById(new Long(newProjectId));
            runParameters.setProject(newProject);
            run = this.runService.createRun(runParameters);
            WISEWorkgroup teacherWISEWorkgroup = workgroupService.createWISEWorkgroup("teacher", runParameters.getOwners(), run, null);
            Set<Brainstorm> brainstormsForProject = brainstormService.getParentBrainstormsForProject(run.getProject());
            for (Brainstorm brainstorm : brainstormsForProject) {
                Brainstorm brainstormCopy = brainstorm.getCopy();
                brainstormCopy.setRun(run);
                Set<PreparedAnswer> preparedAnswers = brainstorm.getPreparedAnswers();
                for (PreparedAnswer preparedAnswer : preparedAnswers) {
                    Answer answer = new AnswerImpl();
                    Revision revision = new RevisionImpl();
                    answer.setWorkgroup(teacherWISEWorkgroup);
                    revision.setBody(preparedAnswer.getBody());
                    revision.setTimestamp(new Date());
                    revision.setDisplayname(preparedAnswer.getDisplayname());
                    answer.addRevision(revision);
                    brainstormCopy.addAnswer(answer);
                }
                brainstormService.createBrainstorm(brainstormCopy);
            }
        } catch (ObjectNotFoundException e) {
            errors.rejectValue("curnitId", "error.curnit-not_found", new Object[] { runParameters.getCurnitId() }, "Curnit Not Found.");
            return showForm(request, response, errors);
        }
        ModelAndView modelAndView = new ModelAndView(COMPLETE_VIEW_NAME);
        modelAndView.addObject(RUN_KEY, run);
        Set<String> runIdsToArchive = runParameters.getRunIdsToArchive();
        if (runIdsToArchive != null) {
            for (String runIdStr : runIdsToArchive) {
                Long runId = Long.valueOf(runIdStr);
                Run runToArchive = runService.retrieveById(runId);
                runService.endRun(runToArchive);
            }
        }
        User user = ControllerUtil.getSignedInUser();
        CreateRunEmailService emailService = new CreateRunEmailService(command, run, user);
        Thread thread = new Thread(emailService);
        thread.start();
        return modelAndView;
    }

    class CreateRunEmailService implements Runnable {

        private Object command;

        private Run run;

        private User user;

        public CreateRunEmailService(Object command, Run run, User user) {
            this.command = command;
            this.run = run;
            this.user = user;
        }

        public void run() {
            try {
                sendEmail();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        /**
		 * sends an email to individuals to notify them of a new project run
		 * having been set up by a teacher
		 */
        private void sendEmail() throws MessagingException {
            RunParameters runParameters = (RunParameters) command;
            String teacherName = null;
            String teacherEmail = null;
            Serializable projectID = null;
            String schoolName = null;
            String schoolCity = null;
            String schoolState = null;
            String schoolPeriods = null;
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMMMM d, yyyy");
            TeacherUserDetails teacherUserDetails = (TeacherUserDetails) user.getUserDetails();
            teacherName = teacherUserDetails.getFirstname() + " " + teacherUserDetails.getLastname();
            teacherEmail = teacherUserDetails.getEmailAddress();
            schoolName = teacherUserDetails.getSchoolname();
            schoolCity = teacherUserDetails.getCity();
            schoolState = teacherUserDetails.getState();
            schoolPeriods = runParameters.printAllPeriods();
            Set<String> projectcodes = new TreeSet<String>();
            String runcode = run.getRuncode();
            Set<Group> periods = run.getPeriods();
            for (Group period : periods) {
                projectcodes.add(runcode + "-" + period.getName());
            }
            projectID = runParameters.getProject().getId();
            Long runID = run.getId();
            String[] recipients = { emaillisteners.getProperty("project_setup") };
            String subject = uiHTMLProperties.getProperty("setuprun.confirmation.email.subject") + " (" + portalProperties.getProperty("portal.name") + ")";
            String message = uiHTMLProperties.getProperty("setuprun.confirmation.email.message") + "\n\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.portal_name") + ": " + portalProperties.getProperty("portal.name") + "\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.teacher_name") + ": " + teacherName + "\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.teacher_username") + ": " + teacherUserDetails.getUsername() + "\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.teacher_email") + ": " + teacherEmail + "\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.school_name") + ": " + schoolName + "\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.school_location") + ": " + schoolCity + ", " + schoolState + "\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.school_periods") + ": " + schoolPeriods + "\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.project_codes") + ": " + projectcodes.toString() + "\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.project_name") + ": " + run.getProject().getProjectInfo().getName() + "\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.project_id") + ": " + projectID + "\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.run_id") + ": " + runID + "\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.run_created") + ": " + sdf.format(date) + "\n" + "\n\n" + uiHTMLProperties.getProperty("setuprun.confirmation.email.end_blurb");
            String fromEmail = teacherEmail;
            if (DEBUG) {
                recipients[0] = DEBUG_EMAIL;
            }
            javaMail.postMail(recipients, subject, message, fromEmail);
        }
    }

    /**
	 * This method is called if there is a submit that contains the "_cancel"
	 * request parameter.
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
    @Override
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) {
        return new ModelAndView(new RedirectView(CANCEL_VIEW_NAME));
    }

    /**
	 * @param runService the runService to set
	 */
    public void setRunService(RunService runService) {
        this.runService = runService;
    }

    /**
	 * @return the javaMail
	 */
    public IMailFacade getJavaMail() {
        return javaMail;
    }

    /**
	 * @param javaMail the javaMail to set
	 */
    public void setJavaMail(IMailFacade javaMail) {
        this.javaMail = javaMail;
    }

    /**
	 * @return the emaillisteners
	 */
    public Properties getEmaillisteners() {
        return emaillisteners;
    }

    /**
	 * @param emaillisteners the emaillisteners to set
	 */
    public void setEmaillisteners(Properties emaillisteners) {
        this.emaillisteners = emaillisteners;
    }

    /**
	 * @param projectService the projectService to set
	 */
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
	 * @param uiHTMLProperties the uiHTMLProperties to set
	 */
    public void setUiHTMLProperties(Properties uiHTMLProperties) {
        this.uiHTMLProperties = uiHTMLProperties;
    }

    /**
	 * @param portalProperties the portalProperties to set
	 */
    public void setPortalProperties(Properties portalProperties) {
        this.portalProperties = portalProperties;
    }

    /**
	 * @param workgroupService the workgroupService to set
	 */
    public void setWorkgroupService(WISEWorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

    /**
	 * @param brainstormService the brainstormService to set
	 */
    public void setBrainstormService(BrainstormService brainstormService) {
        this.brainstormService = brainstormService;
    }
}
