package org.dspace.app.webui.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.log4j.Logger;
import org.dspace.app.util.SubmissionInfo;
import org.dspace.app.util.SubmissionStepConfig;
import org.dspace.app.webui.submit.JSPStepManager;
import org.dspace.app.webui.util.FileUploadRequest;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.WorkspaceItem;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.workflow.WorkflowItem;
import org.dspace.submit.AbstractProcessingStep;

/**
 * Submission Manager servlet for DSpace. Handles the initial submission of
 * items, as well as the editing of items further down the line.
 * <p>
 * Whenever the submit servlet receives a GET request, this is taken to indicate
 * the start of a fresh new submission, where no collection has been selected,
 * and the submission process is started from scratch.
 * <p>
 * All other interactions happen via POSTs. Part of the post will normally be a
 * (hidden) "step" parameter, which will correspond to the form that the user
 * has just filled out. If this is absent, step 0 (select collection) is
 * assumed, meaning that it's simple to place "Submit to this collection"
 * buttons on collection home pages.
 * <p>
 * According to the step number of the incoming form, the values posted from the
 * form are processed (using the process* methods), and the item updated as
 * appropriate. The servlet then forwards control of the request to the
 * appropriate JSP (from jsp/submit) to render the next stage of the process or
 * an error if appropriate. Each of these JSPs may require that attributes be
 * passed in. Check the comments at the top of a JSP to see which attributes are
 * needed. All submit-related forms require a properly initialised
 * SubmissionInfo object to be present in the the "submission.info" attribute.
 * This holds the core information relevant to the submission, e.g. the item,
 * personal workspace or workflow item, the submitting "e-person", and the
 * target collection.
 * <p>
 * When control of the request reaches a JSP, it is assumed that all checks,
 * interactions with the database and so on have been performed and that all
 * necessary information to render the form is in memory. e.g. The
 * SubmitFormInfo object passed in must be correctly filled out. Thus the JSPs
 * do no error or integrity checking; it is the servlet's responsibility to
 * ensure that everything is prepared. The servlet is fairly diligent about
 * ensuring integrity at each step.
 * <p>
 * Each step has an integer constant defined below. The main sequence of the
 * submission procedure always runs from 0 upwards, until SUBMISSION_COMPLETE.
 * Other, not-in-sequence steps (such as the cancellation screen and the
 * "previous version ID verification" screen) have numbers much higher than
 * SUBMISSION_COMPLETE. These conventions allow the progress bar component of
 * the submission forms to render the user's progress through the process.
 * 
 * @see org.dspace.app.util.SubmissionInfo
 * @see org.dspace.app.util.SubmissionConfig
 * @see org.dspace.app.util.SubmissionStepConfig
 * @see org.dspace.app.webui.submit.JSPStepManager
 * 
 * @author Tim Donohue
 * @version $Revision: 6151 $
 */
public class SubmissionController extends DSpaceServlet {

    /** Selection collection step */
    public static final int SELECT_COLLECTION = 0;

    /** First step after "select collection" */
    public static final int FIRST_STEP = 1;

    /** For workflows, first step is step #0 (since Select Collection is already filtered out) */
    public static final int WORKFLOW_FIRST_STEP = 0;

    /** path to the JSP shown once the submission is completed */
    private static final String COMPLETE_JSP = "/submit/complete.jsp";

    /** log4j logger */
    private static Logger log = Logger.getLogger(SubmissionController.class);

    protected void doDSGet(Context context, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {
        String workspaceID = request.getParameter("resume");
        String workflowID = request.getParameter("workflow");
        if (workspaceID != null) {
            try {
                WorkspaceItem wi = WorkspaceItem.find(context, Integer.parseInt(workspaceID));
                SubmissionInfo si = SubmissionInfo.load(request, wi);
                if (getStepReached(si) >= si.getSubmissionConfig().getNumberOfSteps()) {
                    int lastStep = si.getSubmissionConfig().getNumberOfSteps() - 1;
                    wi.setStageReached(lastStep);
                    wi.setPageReached(AbstractProcessingStep.LAST_PAGE_REACHED);
                    wi.update();
                    context.commit();
                    si.setSubmissionItem(wi);
                }
                setBeginningOfStep(request, true);
                doStep(context, request, response, si, FIRST_STEP);
            } catch (NumberFormatException nfe) {
                log.warn(LogManager.getHeader(context, "bad_workspace_id", "bad_id=" + workspaceID));
                JSPManager.showInvalidIDError(request, response, workspaceID, -1);
            }
        } else if (workflowID != null) {
            try {
                WorkflowItem wi = WorkflowItem.find(context, Integer.parseInt(workflowID));
                SubmissionInfo si = SubmissionInfo.load(request, wi);
                setBeginningOfStep(request, true);
                doStep(context, request, response, si, WORKFLOW_FIRST_STEP);
            } catch (NumberFormatException nfe) {
                log.warn(LogManager.getHeader(context, "bad_workflow_id", "bad_id=" + workflowID));
                JSPManager.showInvalidIDError(request, response, workflowID, -1);
            }
        } else {
            doDSPost(context, request, response);
        }
    }

    protected void doDSPost(Context context, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {
        SubmissionStepConfig currentStepConfig;
        String contentType = request.getContentType();
        if ((contentType != null) && (contentType.indexOf("multipart/form-data") != -1)) {
            try {
                request = wrapMultipartRequest(request);
            } catch (FileSizeLimitExceededException e) {
                log.warn("Upload exceeded upload.max");
                JSPManager.showFileSizeLimitExceededError(request, response, e.getMessage(), e.getActualSize(), e.getPermittedSize());
            }
            uploadFiles(context, request);
        }
        SubmissionInfo subInfo = getSubmissionInfo(context, request);
        if (subInfo == null) {
            if (request.getSession().getAttribute("removed_thesis") != null) {
                request.getSession().removeAttribute("removed_thesis");
                JSPManager.showJSP(request, response, "/submit/thesis-removed-workaround.jsp");
                return;
            } else {
                log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                JSPManager.showIntegrityError(request, response);
                return;
            }
        }
        if (UIUtil.getSubmitButton(request, "").equals(AbstractProcessingStep.CANCEL_BUTTON)) {
            currentStepConfig = getCurrentStepConfig(request, subInfo);
            doCancelOrSave(context, request, response, subInfo, currentStepConfig);
        } else if (subInfo.getSubmissionItem() == null) {
            doStep(context, request, response, subInfo, SELECT_COLLECTION);
        } else {
            currentStepConfig = getCurrentStepConfig(request, subInfo);
            if (UIUtil.getBoolParameter(request, "cancellation")) {
                processCancelOrSave(context, request, response, subInfo, currentStepConfig);
            } else if (UIUtil.getSubmitButton(request, "").startsWith(AbstractProcessingStep.PREVIOUS_BUTTON)) {
                doPreviousStep(context, request, response, subInfo, currentStepConfig);
            } else if (UIUtil.getSubmitButton(request, "").startsWith(AbstractProcessingStep.PROGRESS_BAR_PREFIX)) {
                doStepJump(context, request, response, subInfo, currentStepConfig);
            } else {
                doStep(context, request, response, subInfo, currentStepConfig.getStepNumber());
            }
        }
    }

    /**
     * Forward processing to the specified step.
     * 
     * @param context
     *            DSpace context
     * @param request
     *            the request object
     * @param response
     *            the response object
     * @param subInfo
     *            SubmissionInfo pertaining to this submission
     * @param stepNumber
     *            The number of the step to perform
     */
    private void doStep(Context context, HttpServletRequest request, HttpServletResponse response, SubmissionInfo subInfo, int stepNumber) throws ServletException, IOException, SQLException, AuthorizeException {
        SubmissionStepConfig currentStepConfig = null;
        if (subInfo.getSubmissionConfig() != null) {
            currentStepConfig = subInfo.getSubmissionConfig().getStep(stepNumber);
        } else {
            log.fatal(LogManager.getHeader(context, "no_submission_process", "trying to load step=" + stepNumber + ", but submission process is null"));
            JSPManager.showInternalError(request, response);
        }
        if (!subInfo.isInWorkflow() && (currentStepConfig.getStepNumber() > getStepReached(subInfo))) {
            userHasReached(subInfo, currentStepConfig.getStepNumber());
            context.commit();
            setBeginningOfStep(request, true);
        }
        saveCurrentStepConfig(request, currentStepConfig);
        log.debug("Calling Step Class: '" + currentStepConfig.getProcessingClassName() + "'");
        try {
            JSPStepManager stepManager = JSPStepManager.loadStep(currentStepConfig);
            boolean stepFinished = stepManager.processStep(context, request, response, subInfo);
            if (stepFinished) {
                if (request instanceof FileUploadRequest) {
                    request = ((FileUploadRequest) request).getOriginalRequest();
                }
                subInfo = getSubmissionInfo(context, request);
                doNextStep(context, request, response, subInfo, currentStepConfig);
            } else {
                context.complete();
            }
        } catch (Exception e) {
            log.error("Error loading step class'" + currentStepConfig.getProcessingClassName() + "':", e);
            JSPManager.showInternalError(request, response);
        }
    }

    /**
     * Forward processing to the next step.
     * 
     * @param context
     *            DSpace context
     * @param request
     *            the request object
     * @param response
     *            the response object
     * @param subInfo
     *            SubmissionInfo pertaining to this submission
     */
    private void doNextStep(Context context, HttpServletRequest request, HttpServletResponse response, SubmissionInfo subInfo, SubmissionStepConfig currentStepConfig) throws ServletException, IOException, SQLException, AuthorizeException {
        int currentStepNum;
        if (currentStepConfig == null) {
            currentStepNum = -1;
        } else {
            currentStepNum = currentStepConfig.getStepNumber();
        }
        if (subInfo.getSubmissionConfig().hasMoreSteps(currentStepNum)) {
            currentStepNum++;
            setBeginningOfStep(request, true);
            doStep(context, request, response, subInfo, currentStepNum);
        } else {
            if (subInfo.isInWorkflow()) {
                request.setAttribute("workflow.item", subInfo.getSubmissionItem());
                JSPManager.showJSP(request, response, "/mydspace/perform-task.jsp");
            } else {
                saveSubmissionInfo(request, subInfo);
                showProgressAwareJSP(request, response, subInfo, COMPLETE_JSP);
            }
        }
    }

    /**
     * Forward processing to the previous step. This method is called if it is
     * determined that the "previous" button was pressed.
     * 
     * @param context
     *            DSpace context
     * @param request
     *            the request object
     * @param response
     *            the response object
     * @param subInfo
     *            SubmissionInfo pertaining to this submission
     */
    private void doPreviousStep(Context context, HttpServletRequest request, HttpServletResponse response, SubmissionInfo subInfo, SubmissionStepConfig currentStepConfig) throws ServletException, IOException, SQLException, AuthorizeException {
        int result = doSaveCurrentState(context, request, response, subInfo, currentStepConfig);
        int currentStepNum;
        if (currentStepConfig == null) {
            currentStepNum = -1;
        } else {
            currentStepNum = currentStepConfig.getStepNumber();
        }
        int currPage = AbstractProcessingStep.getCurrentPage(request);
        double currStepAndPage = Double.parseDouble(currentStepNum + "." + currPage);
        double stepAndPageReached = -1;
        if (!subInfo.isInWorkflow()) {
            stepAndPageReached = Float.parseFloat(getStepReached(subInfo) + "." + JSPStepManager.getPageReached(subInfo));
        }
        if (result != AbstractProcessingStep.STATUS_COMPLETE && currStepAndPage != stepAndPageReached) {
            doStep(context, request, response, subInfo, currentStepNum);
        }
        int currentPageNum = AbstractProcessingStep.getCurrentPage(request);
        boolean foundPrevious = false;
        if (currentPageNum > 1) {
            AbstractProcessingStep.setCurrentPage(request, currentPageNum - 1);
            foundPrevious = true;
            setBeginningOfStep(request, true);
            doStep(context, request, response, subInfo, currentStepNum);
        } else if (currentStepNum > FIRST_STEP) {
            currentStepConfig = getPreviousVisibleStep(request, subInfo);
            if (currentStepConfig != null) {
                currentStepNum = currentStepConfig.getStepNumber();
                foundPrevious = true;
            }
            if (foundPrevious) {
                request.setAttribute("step.backwards", Boolean.TRUE);
                setBeginningOfStep(request, true);
                doStep(context, request, response, subInfo, currentStepNum);
            }
        }
        if (!foundPrevious) {
            log.error(LogManager.getHeader(context, "no_previous_visible_step", "Attempting to go to previous step for step=" + currentStepNum + "." + "NO PREVIOUS VISIBLE STEP OR PAGE FOUND!"));
            JSPManager.showIntegrityError(request, response);
        }
    }

    /**
     * Process a click on a button in the progress bar. This jumps to the step
     * whose button was pressed.
     * 
     * @param context
     *            DSpace context object
     * @param request
     *            the request object
     * @param response
     *            the response object
     * @param subInfo
     *            SubmissionInfo pertaining to this submission
     */
    private void doStepJump(Context context, HttpServletRequest request, HttpServletResponse response, SubmissionInfo subInfo, SubmissionStepConfig currentStepConfig) throws ServletException, IOException, SQLException, AuthorizeException {
        String buttonPressed = UIUtil.getSubmitButton(request, "");
        int nextStep = -1;
        int nextPage = -1;
        if (buttonPressed.startsWith("submit_jump_")) {
            try {
                String stepAndPage = buttonPressed.substring(12);
                String[] fields = stepAndPage.split("\\.");
                nextStep = Integer.parseInt(fields[0]);
                nextPage = Integer.parseInt(fields[1]);
            } catch (NumberFormatException ne) {
                nextStep = -1;
                nextPage = -1;
            }
            if ((!subInfo.isInWorkflow() && nextStep < FIRST_STEP) || (subInfo.isInWorkflow() && nextStep < WORKFLOW_FIRST_STEP)) {
                nextStep = -1;
                nextPage = -1;
            }
            if (!subInfo.isInWorkflow() && (nextStep > getStepReached(subInfo))) {
                nextStep = -1;
            }
        }
        if (nextStep == -1) {
            log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
            JSPManager.showIntegrityError(request, response);
        } else {
            int result = doSaveCurrentState(context, request, response, subInfo, currentStepConfig);
            if (request instanceof FileUploadRequest) {
                FileUploadRequest fur = (FileUploadRequest) request;
                request = fur.getOriginalRequest();
            }
            int currStep = currentStepConfig.getStepNumber();
            int currPage = AbstractProcessingStep.getCurrentPage(request);
            double currStepAndPage = Float.parseFloat(currStep + "." + currPage);
            double stepAndPageReached = -1;
            if (!subInfo.isInWorkflow()) {
                stepAndPageReached = Float.parseFloat(getStepReached(subInfo) + "." + JSPStepManager.getPageReached(subInfo));
            }
            if (result != AbstractProcessingStep.STATUS_COMPLETE && currStepAndPage != stepAndPageReached) {
                doStep(context, request, response, subInfo, currStep);
            } else {
                AbstractProcessingStep.setCurrentPage(request, nextPage);
                setBeginningOfStep(request, true);
                log.debug("Jumping to Step " + nextStep + " and Page " + nextPage);
                doStep(context, request, response, subInfo, nextStep);
            }
        }
    }

    /**
     * Respond to the user clicking "cancel/save" 
     * from any of the steps.  This method first calls
     * the "doPostProcessing()" method of the step, in 
     * order to ensure any inputs are saved.
     * 
     * @param context
     *            DSpace context
     * @param request
     *            current servlet request object
     * @param response
     *            current servlet response object
     * @param subInfo
     *            SubmissionInfo object
     * @param stepConfig
     *            config of step who's page the user clicked "cancel" on.
     */
    private void doCancelOrSave(Context context, HttpServletRequest request, HttpServletResponse response, SubmissionInfo subInfo, SubmissionStepConfig stepConfig) throws ServletException, IOException, SQLException, AuthorizeException {
        if (subInfo.isInWorkflow()) {
            int result = doSaveCurrentState(context, request, response, subInfo, stepConfig);
            if (result == AbstractProcessingStep.STATUS_COMPLETE) {
                request.setAttribute("workflow.item", subInfo.getSubmissionItem());
                JSPManager.showJSP(request, response, "/mydspace/perform-task.jsp");
            } else {
                int currStep = stepConfig.getStepNumber();
                doStep(context, request, response, subInfo, currStep);
            }
        } else {
            if (subInfo.getSubmissionItem() == null) {
                JSPManager.showJSP(request, response, "/submit/cancelled-removed.jsp");
            } else {
                setCancellationInProgress(request, true);
                int result = doSaveCurrentState(context, request, response, subInfo, stepConfig);
                int currStep = stepConfig.getStepNumber();
                int currPage = AbstractProcessingStep.getCurrentPage(request);
                double currStepAndPage = Float.parseFloat(currStep + "." + currPage);
                double stepAndPageReached = Float.parseFloat(getStepReached(subInfo) + "." + JSPStepManager.getPageReached(subInfo));
                if (result != AbstractProcessingStep.STATUS_COMPLETE && currStepAndPage < stepAndPageReached) {
                    setReachedStepAndPage(subInfo, currStep, currPage);
                }
                context.complete();
                saveSubmissionInfo(request, subInfo);
                saveCurrentStepConfig(request, stepConfig);
                showProgressAwareJSP(request, response, subInfo, "/submit/cancel.jsp");
            }
        }
    }

    private int doSaveCurrentState(Context context, HttpServletRequest request, HttpServletResponse response, SubmissionInfo subInfo, SubmissionStepConfig stepConfig) throws ServletException {
        int result = -1;
        try {
            log.debug("Cancel/Save or Jump/Previous Request: calling processing for Step: '" + stepConfig.getProcessingClassName() + "'");
            try {
                ClassLoader loader = this.getClass().getClassLoader();
                Class stepClass = loader.loadClass(stepConfig.getProcessingClassName());
                AbstractProcessingStep step = (AbstractProcessingStep) stepClass.newInstance();
                result = step.doProcessing(context, request, response, subInfo);
            } catch (Exception e) {
                log.error("Error loading step class'" + stepConfig.getProcessingClassName() + "':", e);
                JSPManager.showInternalError(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        return result;
    }

    /**
     * Process information from "submission cancelled" page.
     * This saves the item if the user decided to "cancel & save",
     * or removes the item if the user decided to "cancel & remove".
     * 
     * @param context
     *            current DSpace context
     * @param request
     *            current servlet request object
     * @param response
     *            current servlet response object
     * @param subInfo
     *            submission info object
     */
    private void processCancelOrSave(Context context, HttpServletRequest request, HttpServletResponse response, SubmissionInfo subInfo, SubmissionStepConfig currentStepConfig) throws ServletException, IOException, SQLException, AuthorizeException {
        String buttonPressed = UIUtil.getSubmitButton(request, "submit_back");
        if (buttonPressed.equals("submit_back")) {
            setBeginningOfStep(request, true);
            doStep(context, request, response, subInfo, currentStepConfig.getStepNumber());
        } else if (buttonPressed.equals("submit_remove")) {
            WorkspaceItem wi = (WorkspaceItem) subInfo.getSubmissionItem();
            wi.deleteAll();
            JSPManager.showJSP(request, response, "/submit/cancelled-removed.jsp");
            context.complete();
        } else if (buttonPressed.equals("submit_keep")) {
            JSPManager.showJSP(request, response, "/submit/saved.jsp");
        } else {
            doStepJump(context, request, response, subInfo, currentStepConfig);
        }
    }

    /**
     * Show a JSP after setting attributes needed by progress bar
     * 
     * @param request
     *            the request object
     * @param response
     *            the response object
     * @param subInfo
     *            the SubmissionInfo object
     * @param jspPath
     *            relative path to JSP
     */
    private static void showProgressAwareJSP(HttpServletRequest request, HttpServletResponse response, SubmissionInfo subInfo, String jspPath) throws ServletException, IOException {
        saveSubmissionInfo(request, subInfo);
        JSPManager.showJSP(request, response, jspPath);
    }

    /**
     * Reloads a filled-out submission info object from the parameters in the
     * current request. If there is a problem, <code>null</code> is returned.
     * 
     * @param context
     *            DSpace context
     * @param request
     *            HTTP request
     * 
     * @return filled-out submission info, or null
     */
    public static SubmissionInfo getSubmissionInfo(Context context, HttpServletRequest request) throws SQLException, ServletException {
        SubmissionInfo info = null;
        if (request.getAttribute("submission.info") != null) {
            info = (SubmissionInfo) request.getAttribute("submission.info");
        } else {
            if (request.getParameter("workflow_id") != null) {
                int workflowID = UIUtil.getIntParameter(request, "workflow_id");
                info = SubmissionInfo.load(request, WorkflowItem.find(context, workflowID));
            } else if (request.getParameter("workspace_item_id") != null) {
                int workspaceID = UIUtil.getIntParameter(request, "workspace_item_id");
                info = SubmissionInfo.load(request, WorkspaceItem.find(context, workspaceID));
            } else {
                info = SubmissionInfo.load(request, null);
            }
            if ((getStepReached(info) > FIRST_STEP) && (info.getSubmissionItem() == null)) {
                log.warn(LogManager.getHeader(context, "cannot_load_submission_info", "InProgressSubmission is null!"));
                return null;
            }
            if (request.getParameter("bundle_id") != null) {
                int bundleID = UIUtil.getIntParameter(request, "bundle_id");
                info.setBundle(Bundle.find(context, bundleID));
            }
            if (request.getParameter("bitstream_id") != null) {
                int bitstreamID = UIUtil.getIntParameter(request, "bitstream_id");
                info.setBitstream(Bitstream.find(context, bitstreamID));
            }
            saveSubmissionInfo(request, info);
        }
        return info;
    }

    /**
     * Saves the submission info object to the current request.
     * 
     * @param request
     *            HTTP request
     * @param si
     *            the current submission info
     * 
     */
    public static void saveSubmissionInfo(HttpServletRequest request, SubmissionInfo si) {
        request.setAttribute("submission.info", si);
    }

    /**
     * Get the configuration of the current step from parameters in the request, 
     * along with the current SubmissionInfo object. 
     * If there is a problem, <code>null</code> is returned.
     * 
     * @param request
     *            HTTP request
     * @param si
     *            The current SubmissionInfo object
     * 
     * @return the current SubmissionStepConfig
     */
    public static SubmissionStepConfig getCurrentStepConfig(HttpServletRequest request, SubmissionInfo si) {
        int stepNum = -1;
        SubmissionStepConfig step = (SubmissionStepConfig) request.getAttribute("step");
        if (step == null) {
            stepNum = UIUtil.getIntParameter(request, "step");
            if (stepNum < 0 || si == null || si.getSubmissionConfig() == null) {
                return null;
            } else {
                return si.getSubmissionConfig().getStep(stepNum);
            }
        } else {
            return step;
        }
    }

    /**
     * Saves the current step configuration into the request.
     * 
     * @param request
     *            HTTP request
     * @param step
     *            The current SubmissionStepConfig
     */
    public static void saveCurrentStepConfig(HttpServletRequest request, SubmissionStepConfig step) {
        request.setAttribute("step", step);
    }

    /**
     * Checks if the current step is also the first "visibile" step in the item submission
     * process.
     * 
     * @param request
     *            HTTP request
     * @param si
     *            The current Submission Info
     * 
     * @return whether or not the current step is the first step
     */
    public static boolean isFirstStep(HttpServletRequest request, SubmissionInfo si) {
        SubmissionStepConfig step = getCurrentStepConfig(request, si);
        return ((step != null) && (getPreviousVisibleStep(request, si) == null));
    }

    /**
     * Return the previous "visibile" step in the item submission
     * process if any, <code>null</code> otherwise.
     * 
     * @param request
     *            HTTP request
     * @param si
     *            The current Submission Info
     * 
     * @return the previous step in the item submission process if any
     */
    public static SubmissionStepConfig getPreviousVisibleStep(HttpServletRequest request, SubmissionInfo si) {
        SubmissionStepConfig step = getCurrentStepConfig(request, si);
        SubmissionStepConfig currentStepConfig, previousStep = null;
        int currentStepNum = step.getStepNumber();
        while (currentStepNum > FIRST_STEP) {
            currentStepNum--;
            currentStepConfig = si.getSubmissionConfig().getStep(currentStepNum);
            if (currentStepConfig.isVisible()) {
                previousStep = currentStepConfig;
                break;
            }
        }
        return previousStep;
    }

    /**
     * Get whether or not the current step has just begun. This helps determine
     * if we've done any pre-processing yet. If the step is just started, we
     * need to do pre-processing, otherwise we should be doing post-processing.
     * If there is a problem, <code>false</code> is returned.
     * 
     * @param request
     *            HTTP request
     * 
     * @return true if the step has just started (and JSP has not been loaded
     *         for this step), false otherwise.
     */
    public static boolean isBeginningOfStep(HttpServletRequest request) {
        Boolean stepStart = (Boolean) request.getAttribute("step.start");
        if (stepStart != null) {
            return stepStart.booleanValue();
        } else {
            return false;
        }
    }

    /**
     * Get whether or not the current step has just begun. This helps determine
     * if we've done any pre-processing yet. If the step is just started, we
     * need to do pre-processing, otherwise we should be doing post-processing.
     * If there is a problem, <code>false</code> is returned.
     * 
     * @param request
     *            HTTP request
     * @param beginningOfStep
     *            true if step just began
     */
    public static void setBeginningOfStep(HttpServletRequest request, boolean beginningOfStep) {
        request.setAttribute("step.start", Boolean.valueOf(beginningOfStep));
    }

    /**
     * Get whether or not a cancellation is in progress (i.e. the 
     * user clicked on the "Cancel/Save" button from any submission
     * page).
     * 
     * @param request
     *            HTTP request
     *            
     * @return true if a cancellation is in progress
     */
    public static boolean isCancellationInProgress(HttpServletRequest request) {
        Boolean cancellation = (Boolean) request.getAttribute("submission.cancellation");
        if (cancellation != null) {
            return cancellation.booleanValue();
        } else {
            return false;
        }
    }

    /**
     * Sets whether or not a cancellation is in progress (i.e. the 
     * user clicked on the "Cancel/Save" button from any submission
     * page).
     * 
     * @param request
     *            HTTP request
     * @param cancellationInProgress
     *            true if cancellation is in progress
     */
    private static void setCancellationInProgress(HttpServletRequest request, boolean cancellationInProgress) {
        request.setAttribute("submission.cancellation", Boolean.valueOf(cancellationInProgress));
    }

    /**
     * Return the submission info as hidden parameters for an HTML form on a JSP
     * page.
     * 
     * @param context
     *            DSpace context
     * @param request
     *            HTTP request
     * @return HTML hidden parameters
     */
    public static String getSubmissionParameters(Context context, HttpServletRequest request) throws SQLException, ServletException {
        SubmissionInfo si = getSubmissionInfo(context, request);
        SubmissionStepConfig step = getCurrentStepConfig(request, si);
        String info = "";
        if ((si.getSubmissionItem() != null) && si.isInWorkflow()) {
            info = info + "<input type=\"hidden\" name=\"workflow_id\" value=\"" + si.getSubmissionItem().getID() + "\"/>";
        } else if (si.getSubmissionItem() != null) {
            info = info + "<input type=\"hidden\" name=\"workspace_item_id\" value=\"" + si.getSubmissionItem().getID() + "\"/>";
        }
        if (si.getBundle() != null) {
            info = info + "<input type=\"hidden\" name=\"bundle_id\" value=\"" + si.getBundle().getID() + "\"/>";
        }
        if (si.getBitstream() != null) {
            info = info + "<input type=\"hidden\" name=\"bitstream_id\" value=\"" + si.getBitstream().getID() + "\"/>";
        }
        if (step != null) {
            info = info + "<input type=\"hidden\" name=\"step\" value=\"" + step.getStepNumber() + "\"/>";
        }
        int page = AbstractProcessingStep.getCurrentPage(request);
        info = info + "<input type=\"hidden\" name=\"page\" value=\"" + page + "\"/>";
        String jspDisplayed = JSPStepManager.getLastJSPDisplayed(request);
        info = info + "<input type=\"hidden\" name=\"jsp\" value=\"" + jspDisplayed + "\"/>";
        return info;
    }

    /**
     * Indicate the user has advanced to the given stage. This will only
     * actually do anything when it's a user initially entering a submission. It
     * will only increase the "stage reached" column - it will not "set back"
     * where a user has reached. Whenever the "stage reached" column is
     * increased, the "page reached" column is reset to 1, since you've now
     * reached page #1 of the next stage.
     * 
     * @param subInfo
     *            the SubmissionInfo object pertaining to the current submission
     * @param step
     *            the step the user has just reached
     */
    private void userHasReached(SubmissionInfo subInfo, int step) throws SQLException, AuthorizeException, IOException {
        if (!subInfo.isInWorkflow() && subInfo.getSubmissionItem() != null) {
            WorkspaceItem wi = (WorkspaceItem) subInfo.getSubmissionItem();
            if (step > wi.getStageReached()) {
                wi.setStageReached(step);
                wi.setPageReached(1);
                wi.update();
            }
        }
    }

    /**
    * Set a specific step and page as reached. 
    * It will also "set back" where a user has reached.
    * 
    * @param subInfo
     *            the SubmissionInfo object pertaining to the current submission
    * @param step the step to set as reached, can be also a previous reached step
    * @param page the page (within the step) to set as reached, can be also a previous reached page
    */
    private void setReachedStepAndPage(SubmissionInfo subInfo, int step, int page) throws SQLException, AuthorizeException, IOException {
        if (!subInfo.isInWorkflow() && subInfo.getSubmissionItem() != null) {
            WorkspaceItem wi = (WorkspaceItem) subInfo.getSubmissionItem();
            wi.setStageReached(step);
            wi.setPageReached(page);
            wi.update();
        }
    }

    /**
     * Find out which step a user has reached in the submission process. If the
     * submission is in the workflow process, this returns -1.
     * 
     * @param subInfo
     *            submission info object
     * 
     * @return step reached
     */
    public static int getStepReached(SubmissionInfo subInfo) {
        if (subInfo == null || subInfo.isInWorkflow() || subInfo.getSubmissionItem() == null) {
            return -1;
        } else {
            WorkspaceItem wi = (WorkspaceItem) subInfo.getSubmissionItem();
            int i = wi.getStageReached();
            if (i == -1) {
                i = FIRST_STEP;
            }
            return i;
        }
    }

    /**
     * Wraps a multipart form request, so that its attributes and parameters can
     * still be accessed as normal.
     * 
     * @return wrapped multipart request object
     * 
     * @throws ServletException
     *             if there are no more pages in this step
     */
    private HttpServletRequest wrapMultipartRequest(HttpServletRequest request) throws ServletException, FileSizeLimitExceededException {
        HttpServletRequest wrappedRequest;
        try {
            if (!Class.forName("org.dspace.app.webui.util.FileUploadRequest").isInstance(request)) {
                wrappedRequest = new FileUploadRequest(request);
                return (HttpServletRequest) wrappedRequest;
            } else {
                return request;
            }
        } catch (FileSizeLimitExceededException e) {
            throw new FileSizeLimitExceededException(e.getMessage(), e.getActualSize(), e.getPermittedSize());
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
     * Upload any files found on the Request, and save them back as 
     * Request attributes, for further processing by the appropriate user interface.
     * 
     * @param context
     *            current DSpace context
     * @param request
     *            current servlet request object
     */
    public void uploadFiles(Context context, HttpServletRequest request) throws ServletException {
        FileUploadRequest wrapper = null;
        String filePath = null;
        InputStream fileInputStream = null;
        try {
            if (Class.forName("org.dspace.app.webui.util.FileUploadRequest").isInstance(request)) {
                wrapper = (FileUploadRequest) request;
            } else {
                wrapper = new FileUploadRequest(request);
            }
            Enumeration fileParams = wrapper.getFileParameterNames();
            while (fileParams.hasMoreElements()) {
                String fileName = (String) fileParams.nextElement();
                File temp = wrapper.getFile(fileName);
                if (temp != null && temp.length() > 0) {
                    fileInputStream = new BufferedInputStream(new FileInputStream(temp));
                    filePath = wrapper.getFilesystemName(fileName);
                    if (!temp.delete()) {
                        log.error("Unable to delete temporary file");
                    }
                    request.setAttribute(fileName + "-path", filePath);
                    request.setAttribute(fileName + "-inputstream", fileInputStream);
                    request.setAttribute(fileName + "-description", wrapper.getParameter("description"));
                }
            }
        } catch (Exception e) {
            log.warn(LogManager.getHeader(context, "upload_error", ""), e);
            throw new ServletException(e);
        }
    }
}
