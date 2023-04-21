package de.uni_leipzig.lots.webfrontend.actions;

import de.uni_leipzig.lots.common.exceptions.NoSuchDatabaseException;
import de.uni_leipzig.lots.common.exceptions.ConnectException;
import de.uni_leipzig.lots.common.objects.ConnectionPoolConfig;
import de.uni_leipzig.lots.common.objects.Database;
import de.uni_leipzig.lots.common.objects.Role;
import de.uni_leipzig.lots.server.persist.DatabaseRepository;
import de.uni_leipzig.lots.server.services.UserAdministration;
import de.uni_leipzig.lots.webfrontend.formbeans.*;
import de.uni_leipzig.lots.webfrontend.formbeans.component.PoolConfigComponent;
import de.uni_leipzig.lots.webfrontend.http.LOTSHttpSession;
import de.uni_leipzig.lots.webfrontend.struts.EntityRedirect;
import de.uni_leipzig.lots.webfrontend.struts.LastPageRedirect;
import de.uni_leipzig.lots.webfrontend.views.PageData;
import de.uni_leipzig.lots.webfrontend.container.UserContainer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.sql.SQLException;

/**
 * @author Alexander Kiel
 * @version $Id: DatabaseAction.java,v 1.22 2007/10/23 06:29:58 mai99bxd Exp $
 */
public final class DatabaseAction extends AdministrationAction {

    private UserAdministration userAdministration;

    private DatabaseRepository databaseRepository;

    @Required
    public void setUserAdministration(@NotNull UserAdministration userAdministration) {
        this.userAdministration = userAdministration;
    }

    @Required
    public void setDatabaseRepository(@NotNull DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    @Override
    protected void checkIfUserIsAuthorized(UserContainer userContainer, ActionMapping mapping, ActionForm form) throws UnauthorizedException {
        if (!userContainer.hasCurrentRole(Role.admin)) {
            throw new UnauthorizedActionInvocationException(userContainer.getUser(), mapping);
        }
    }

    @Nullable
    @Override
    protected ActionForward execute(ActionMapping mapping, BaseForm form, LOTSHttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = mapping.getPath();
        if (path.endsWith("database/")) {
            return handleStartPage(mapping, request);
        } else if (path.endsWith("show/")) {
            return handleShowPage(mapping, (PersistentLongIdForm) form, request);
        } else if (path.endsWith("create/")) {
            return handleCreatePage(mapping, session);
        } else if (path.endsWith("edit/")) {
            return handleEditPage(mapping, (PersistentLongIdForm) form, session, request);
        } else if (path.endsWith("delete/")) {
            return handleDeletePage(mapping, (PersistentLongIdForm) form, session, request);
        } else if (path.startsWith("/post")) {
            if (form.isCancelPressed()) {
                session.removeAttributes(getClass());
                return mapping.findForward(form.getSubmitAction());
            } else if ("/post/databaseCreateForm".equals(path)) {
                return handleCreatePost(mapping, (DatabaseCreateForm) form, session);
            } else if ("/post/databaseEditForm".equals(path)) {
                return handleEditPost(mapping, (DatabaseEditForm) form, session);
            } else if ("/post/databaseDeleteForm".equals(path)) {
                return handleDeletePost(mapping, (DatabaseDeleteForm) form);
            } else if ("/post/databaseResetForm".equals(path)) {
                return handleResetPost(mapping, (DatabaseResetForm) form);
            }
        }
        throw new IllegalArgumentException("Unknown command \"" + path + "\".");
    }

    private ActionForward handleStartPage(ActionMapping mapping, HttpServletRequest request) {
        StartPageData pageData = new StartPageData(databaseRepository.getAll());
        request.setAttribute("pageData", pageData);
        return mapping.findForward("page");
    }

    private ActionForward handleShowPage(ActionMapping mapping, PersistentLongIdForm form, HttpServletRequest request) throws NoSuchDatabaseException, ConnectException, SQLException {
        Database database = databaseRepository.load(new Long(form.getId()));
        request.setAttribute("pageData", new ShowPageData(database));
        return mapping.findForward("page");
    }

    private ActionForward handleCreatePage(ActionMapping mapping, LOTSHttpSession session) throws IllegalAccessException, InvocationTargetException {
        DatabaseCreateForm createForm = getForm(session, DatabaseCreateForm.class);
        if (createForm == null) {
            createForm = new DatabaseCreateForm();
            populateValues(createForm);
            setForm(session, createForm);
        }
        return mapping.findForward("page");
    }

    private ActionForward handleEditPage(ActionMapping mapping, PersistentLongIdForm form, LOTSHttpSession session, HttpServletRequest request) throws NoSuchDatabaseException {
        Database database = databaseRepository.load(new Long(form.getId()));
        DatabaseEditForm editForm = getForm(session, DatabaseEditForm.class);
        if (editForm == null || !wrapRequiredValue(form.getId()).equals(editForm.getId())) {
            editForm = new DatabaseEditForm();
            populateValues(editForm, database);
            setForm(session, editForm);
        }
        request.setAttribute("pageData", new EditPageData(database));
        return mapping.findForward("page");
    }

    private ActionForward handleDeletePage(ActionMapping mapping, PersistentLongIdForm form, LOTSHttpSession session, HttpServletRequest request) throws NoSuchDatabaseException {
        Database database = databaseRepository.load(new Long(form.getId()));
        DatabaseDeleteForm databaseDeleteForm = new DatabaseDeleteForm();
        databaseDeleteForm.setId(String.valueOf(database.getId()));
        setForm(session, databaseDeleteForm);
        DeletePageData pageData = new DeletePageData(database);
        request.setAttribute("pageData", pageData);
        return mapping.findForward("page");
    }

    private ActionForward handleCreatePost(ActionMapping mapping, DatabaseCreateForm form, HttpSession session) throws IllegalAccessException, InvocationTargetException {
        try {
            if (form.isOkPressed()) {
                Database database = new Database(wrapRequiredValue(form.getName()));
                populateValues(database, form);
                Long id = databaseRepository.save(database);
                return new EntityRedirect(mapping.findForward(form.getSubmitAction())).setId(id);
            } else {
                return mapping.findForward(form.getSubmitAction());
            }
        } finally {
            session.removeAttribute("databaseCreateForm");
        }
    }

    private ActionForward handleEditPost(ActionMapping mapping, DatabaseEditForm form, HttpSession session) throws NoSuchDatabaseException, IllegalAccessException, InvocationTargetException {
        try {
            if (form.isOkPressed()) {
                Database database = databaseRepository.load(new Long(form.getId()));
                database.setName(wrapRequiredValue(form.getName()));
                populateValues(database, form);
                database.resetConnectionPool();
                databaseRepository.update(database);
                return findEntityRedirect(mapping, form);
            } else {
                return mapping.findForward(form.getSubmitAction());
            }
        } finally {
            session.removeAttribute("databaseEditForm");
        }
    }

    private ActionForward handleDeletePost(ActionMapping mapping, DatabaseDeleteForm form) throws NoSuchDatabaseException {
        if (form.isYesPressed()) {
            userAdministration.delete(databaseRepository.load(new Long(form.getId())));
        }
        return new LastPageRedirect(form, mapping);
    }

    private ActionForward handleResetPost(ActionMapping mapping, PersistentLongIdForm form) throws NoSuchDatabaseException {
        databaseRepository.load(new Long(form.getId())).resetConnectionPool();
        return mapping.findForward("success");
    }

    @NotNull
    private DatabaseEditForm populateValues(@NotNull DatabaseEditForm form, @NotNull Database database) {
        polulateIdAndLastChange(form, database);
        form.setName(database.getName());
        form.setUrl(database.getUrl());
        form.setDriverName(database.getDriverName());
        form.setUsername(database.getUsername());
        form.setPassword(database.getPassword());
        form.setNote(database.getNote());
        ConnectionPoolConfig connectionPoolConfig = database.getConnectionPoolConfig();
        form.getPoolConfig().setAcquireIncrement(String.valueOf(connectionPoolConfig.getAcquireIncrement()));
        form.getPoolConfig().setInitialPoolSize(String.valueOf(connectionPoolConfig.getInitialPoolSize()));
        form.getPoolConfig().setMaxIdleTime(String.valueOf(connectionPoolConfig.getMaxIdleTime()));
        form.getPoolConfig().setMaxPoolSize(String.valueOf(connectionPoolConfig.getMaxPoolSize()));
        form.getPoolConfig().setMinPoolSize(String.valueOf(connectionPoolConfig.getMinPoolSize()));
        return form;
    }

    private void populateValues(@NotNull DatabaseCreateForm form) {
        ConnectionPoolConfig connectionPoolConfig = new ConnectionPoolConfig();
        form.getPoolConfig().setAcquireIncrement(String.valueOf(connectionPoolConfig.getAcquireIncrement()));
        form.getPoolConfig().setInitialPoolSize(String.valueOf(connectionPoolConfig.getInitialPoolSize()));
        form.getPoolConfig().setMaxIdleTime(String.valueOf(connectionPoolConfig.getMaxIdleTime()));
        form.getPoolConfig().setMaxPoolSize(String.valueOf(connectionPoolConfig.getMaxPoolSize()));
        form.getPoolConfig().setMinPoolSize(String.valueOf(connectionPoolConfig.getMinPoolSize()));
    }

    private void populateValues(@NotNull Database database, @NotNull DatabaseForm form) {
        database.setUrl(wrapRequiredValue(form.getUrl()));
        database.setDriverName(wrapRequiredValue(form.getDriverName()));
        database.setUsername(form.getUsername());
        database.setPassword(form.getPassword());
        database.setNote(form.getNote());
        ConnectionPoolConfig connectionPoolConfig = database.getConnectionPoolConfig();
        PoolConfigComponent poolConfig = form.getPoolConfig();
        connectionPoolConfig.setAcquireIncrement(Integer.parseInt(poolConfig.getAcquireIncrement()));
        connectionPoolConfig.setInitialPoolSize(Integer.parseInt(poolConfig.getInitialPoolSize()));
        connectionPoolConfig.setMaxIdleTime(Integer.parseInt(poolConfig.getMaxIdleTime()));
        connectionPoolConfig.setMaxPoolSize(Integer.parseInt(poolConfig.getMaxPoolSize()));
        connectionPoolConfig.setMinPoolSize(Integer.parseInt(poolConfig.getMinPoolSize()));
    }

    public static class StartPageData implements PageData {

        private final Collection<Database> databases;

        public StartPageData(Collection<Database> databases) {
            this.databases = databases;
        }

        public Collection<Database> getDatabases() {
            return databases;
        }
    }

    public static class ShowPageData implements PageData {

        private final Database database;

        public ShowPageData(Database database) {
            this.database = database;
        }

        public Database getDatabase() {
            return database;
        }
    }

    public static class EditPageData implements PageData {

        private final Database database;

        public EditPageData(Database database) {
            this.database = database;
        }

        public Database getDatabase() {
            return database;
        }
    }

    public static class DeletePageData implements PageData {

        private final Database database;

        public DeletePageData(Database database) {
            this.database = database;
        }

        public Database getDatabase() {
            return database;
        }
    }
}
