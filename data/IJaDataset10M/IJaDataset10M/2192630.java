package scrum.server;

import ilarkesto.auth.Auth;
import ilarkesto.base.time.TimePeriod;
import ilarkesto.core.logging.Log;
import ilarkesto.gwt.server.AGwtConversation;
import ilarkesto.persistence.AEntity;
import scrum.client.DataTransferObject;
import scrum.client.communication.Pinger;
import scrum.server.admin.ProjectUserConfig;
import scrum.server.admin.SystemConfig;
import scrum.server.admin.User;
import scrum.server.collaboration.Emoticon;
import scrum.server.collaboration.EmoticonDao;
import scrum.server.project.Project;

public class GwtConversation extends AGwtConversation {

    private static final Log LOG = Log.get(GwtConversation.class);

    private TimePeriod TIMEOUT = new TimePeriod(Pinger.MAX_DELAY * 10);

    private Project project;

    private EmoticonDao emoticonDao;

    public void setEmoticonDao(EmoticonDao emoticonDao) {
        this.emoticonDao = emoticonDao;
    }

    public GwtConversation(WebSession session, int number) {
        super(session, number);
    }

    @Override
    protected void filterEntityProperties(AEntity entity, java.util.Map propertiesMap) {
        super.filterEntityProperties(entity, propertiesMap);
        User user = getSession().getUser();
        if (entity instanceof SystemConfig) {
            if (user == null || !user.isAdmin()) {
                propertiesMap.remove("smtpPassword");
            }
        } else if (entity instanceof User) {
            if (user == null || (user != entity && !user.isAdmin())) {
                propertiesMap.remove("password");
                propertiesMap.remove("email");
                propertiesMap.remove("loginToken");
            }
        }
    }

    @Override
    protected boolean isEntityVisible(AEntity entity) {
        return Auth.isVisible(entity, getSession().getUser());
    }

    public void sendUserScopeDataToClient(User user) {
        getNextData().setUserId(user.getId());
        ScrumWebApplication app = ScrumWebApplication.get();
        getNextData().systemMessage = app.getSystemMessage();
        sendToClient(user);
        sendToClient(app.getProjectDao().getEntitiesVisibleForUser(user));
        sendToClient(app.getUserDao().getEntitiesVisibleForUser(user));
    }

    @Override
    public synchronized void sendToClient(AEntity entity) {
        super.sendToClient(entity);
        for (Emoticon emoticon : emoticonDao.getEmoticonsByParent(entity)) {
            super.sendToClient(emoticon);
        }
    }

    @Override
    public void invalidate() {
        WebSession session = getSession();
        User user = session.getUser();
        if (user != null && project != null && session.getGwtConversations().size() < 2) {
            ProjectUserConfig config = project.getUserConfig(user);
            config.reset();
            ScrumWebApplication.get().sendToOtherConversationsByProject(this, config);
        }
        super.invalidate();
    }

    @Override
    protected DataTransferObject createDataTransferObject() {
        return new DataTransferObject();
    }

    @Override
    public WebSession getSession() {
        return (WebSession) super.getSession();
    }

    @Override
    public DataTransferObject getNextData() {
        return (DataTransferObject) super.getNextData();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        LOG.info("Project selected:", project);
        this.project = project;
    }

    @Override
    protected TimePeriod getTimeout() {
        return TIMEOUT;
    }
}
