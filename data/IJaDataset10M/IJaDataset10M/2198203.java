package de.uni_leipzig.lots.webfrontend.actions;

import de.uni_leipzig.lots.common.exceptions.NoSuchImageException;
import de.uni_leipzig.lots.common.exceptions.NoSuchUserException;
import de.uni_leipzig.lots.common.objects.User;
import de.uni_leipzig.lots.common.objects.task.*;
import de.uni_leipzig.lots.common.xml.Resolver;
import de.uni_leipzig.lots.common.xml.XmlTaskImport;
import de.uni_leipzig.lots.server.persist.TaskCollectionRepository;
import de.uni_leipzig.lots.server.persist.TaskRepository;
import de.uni_leipzig.lots.server.services.TaskManager;
import de.uni_leipzig.lots.webfrontend.formbeans.*;
import de.uni_leipzig.lots.webfrontend.http.LOTSHttpSession;
import de.uni_leipzig.lots.webfrontend.views.ImportTasksView;
import de.uni_leipzig.lots.webfrontend.views.TaskImportView;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.xml.sax.SAXException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

/**
 * Action zum Importieren von Aufgaben.
 *
 * @author Alexander Kiel
 * @version $Id: ImportTaskCollectionAction.java,v 1.6 2007/10/23 06:29:54 mai99bxd Exp $
 */
public final class ImportTaskCollectionAction extends AdministrationAction {

    private static final String VIEW = ImportTaskCollectionAction.class.getName() + ".VIEW";

    private TaskManager taskManager;

    private TaskRepository taskRepository;

    private TaskCollectionRepository taskCollectionRepository;

    private Resolver resolver;

    @Required
    public void setTaskManager(@NotNull TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Required
    public void setTaskRepository(@NotNull TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Required
    public void setTaskCollectionRepository(@NotNull TaskCollectionRepository taskCollectionRepository) {
        this.taskCollectionRepository = taskCollectionRepository;
    }

    @Required
    public void setResolver(@NotNull Resolver resolver) {
        this.resolver = resolver;
    }

    @Nullable
    @Override
    protected ActionForward execute(ActionMapping mapping, BaseForm form, LOTSHttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = mapping.getPath();
        if (path.endsWith("import/")) {
            return handlePage(mapping, session, request);
        } else if (path.endsWith("reviewTaskCollection/")) {
            return handlePage(mapping, session, request);
        } else if (path.endsWith("select/")) {
            return handlePage(mapping, session, request);
        } else if (path.endsWith("confirm/")) {
            return handlePage(mapping, session, request);
        } else if (path.endsWith("report/")) {
            return handlePage(mapping, session, request);
        } else if (path.endsWith("image/")) {
            return handleShowImage(mapping, (ImageIdForm) form, session, request, response);
        } else if (path.startsWith("/post")) {
            if (form.isCancelPressed()) {
                session.removeAttributes(getClass());
                return getCancelForward(form, mapping);
            } else if ("/post/taskCollectionUploadForm".equals(path)) {
                return handleUploadPost(mapping, (TaskCollectionUploadForm) form, session, request);
            } else if ("/post/taskCollectionReviewForm".equals(path)) {
                return handleReviewPost(mapping, (TaskCollectionReviewForm) form, session, request);
            } else if ("/post/taskCollectionSelectForm".equals(path)) {
                return handleSelectPost(mapping, (TaskCollectionSelectForm) form, session, request);
            } else if ("/post/taskCollectionConfirmForm".equals(path)) {
                return handleConfirmPost(mapping, (EmptyForm) form, session, request);
            }
        }
        throw new IllegalArgumentException("Unknown command \"" + path + "\".");
    }

    private ActionForward handlePage(ActionMapping mapping, LOTSHttpSession session, HttpServletRequest request) {
        return mapping.findForward("page");
    }

    @Nullable
    private ActionForward handleShowImage(ActionMapping mapping, ImageIdForm form, LOTSHttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchImageException {
        TaskId imageTaskId = new TaskId(wrapRequiredValue(form.getTaskId()));
        String imageName = form.getImageName();
        Image image = null;
        ImportTasksView view = (ImportTasksView) session.getAttribute(VIEW);
        for (Object o : view.getTaskImportViews()) {
            TaskImportView taskImportView = (TaskImportView) o;
            Task task = taskImportView.getTask();
            if (imageTaskId.equals(task.getTaskId())) {
                Map<String, Image> imageMap = task.getImageMap();
                image = imageMap.get(imageName);
            }
        }
        if (image == null) {
            throw new NoSuchImageException(imageTaskId);
        }
        response.addHeader("Content-Type", image.getContentType().toString());
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(image.getData());
        outputStream.flush();
        return null;
    }

    private ActionForward handleUploadPost(ActionMapping mapping, TaskCollectionUploadForm taskCollectionUploadForm, LOTSHttpSession session, HttpServletRequest request) throws IOException, SAXException {
        FormFile file = taskCollectionUploadForm.getFile();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("try to import tasks from file: " + file.getFileName());
        }
        XmlTaskImport xmlTaskImport = new XmlTaskImport();
        xmlTaskImport.setResolver(resolver);
        TaskCollection taskCollection = xmlTaskImport.load(file.getInputStream());
        file.destroy();
        ImportTasksView view = new ImportTasksView(taskCollection, file.getFileName(), taskCollectionRepository, taskRepository);
        session.setAttribute(VIEW, view);
        return mapping.findForward("ok");
    }

    private ActionForward handleReviewPost(ActionMapping mapping, TaskCollectionReviewForm form, LOTSHttpSession session, HttpServletRequest request) {
        ImportTasksView view = (ImportTasksView) session.getAttribute(VIEW);
        view.getTaskCollectionView().setImported(form.isImported());
        view.getTaskCollectionView().setNewId(form.getNewId());
        if (form.isNextPressed()) {
            if (!form.isImported()) {
                ActionMessages messages = new ActionMessages();
                messages.add("success", new ActionMessage("action.message.taskmanager.NoImport"));
                saveMessages(request, messages);
                return mapping.findForward("noimport");
            }
            return mapping.findForward("next");
        }
        throw new IllegalArgumentException("Unknown form submit action \"" + form.getSubmitAction() + "\".");
    }

    private ActionForward handleSelectPost(ActionMapping mapping, TaskCollectionSelectForm form, LOTSHttpSession session, HttpServletRequest request) {
        ImportTasksView view = (ImportTasksView) session.getAttribute(VIEW);
        view.getCurrent().setImported(form.isImported());
        view.getCurrent().setNewId(form.getNewId());
        if (form.isNextPressed()) {
            return mapping.findForward("next");
        }
        if (form.isNavPressed()) {
            if (form.isNavBeginPressed()) {
                view.goBegin();
            } else if (form.isNavBackPressed()) {
                view.goPrev();
            } else if (form.isNavNextPressed()) {
                view.goNext();
            } else if (form.isNavEndPressed()) {
                view.goEnd();
            }
            return mapping.findForward("nav");
        }
        if (form.isBackPressed()) {
            return mapping.findForward("back");
        }
        throw new IllegalArgumentException("Unknown form submit action \"" + form.getSubmitAction() + "\".");
    }

    private ActionForward handleConfirmPost(ActionMapping mapping, EmptyForm form, LOTSHttpSession session, HttpServletRequest request) throws NoSuchUserException {
        if (form.isOkPressed()) {
            try {
                ImportTasksView view = (ImportTasksView) session.getAttribute(VIEW);
                TaskCollection taskCollection = view.getTaskCollectionToImport();
                User user = getUser(session);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("creator of taskcollection is: " + user.getUsername());
                }
                setCreator(taskCollection, user);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("try to save TaskCollection ...");
                    logger.finer(taskCollection.toString());
                }
                taskManager.doImport(taskCollection);
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("... taskcollection successfully saved ");
                }
                ActionMessages messages = new ActionMessages();
                messages.add("success", new ActionMessage("action.message.taskmanager.ImportSuccess"));
                saveMessages(request, messages);
                return mapping.findForward(form.getSubmitAction());
            } finally {
                session.removeAttribute(VIEW);
            }
        } else {
            return mapping.findForward(form.getSubmitAction());
        }
    }

    private void setCreator(@NotNull TaskCollection taskCollection, @NotNull User creator) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("set creator on taskcollection: " + taskCollection.getTaskCollectionId());
        }
        taskCollection.updateCreator(creator);
        for (TaskAssignment taskAssignment : taskCollection.getTaskAssignments()) {
            Task task = taskAssignment.getTask();
            task.updateCreator(creator);
            for (Image image : task.getImages()) {
                image.updateCreator(creator);
            }
        }
    }
}
