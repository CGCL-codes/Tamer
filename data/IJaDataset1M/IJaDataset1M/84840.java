package ru.goldenforests.forum.actions;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import net.sf.hibernate.HibernateException;
import org.apache.log4j.Logger;
import ru.goldenforests.forum.AccessException;
import ru.goldenforests.forum.ForumUser;
import ru.goldenforests.forum.ForumUserRegistrationRequest;
import ru.goldenforests.forum.engine.ForumUserRegistrationManager;
import ru.goldenforests.forum.engine.ForumUserRegistrationManagerAware;
import com.opensymphony.xwork.Action;

public class ConfirmRegistrationAction extends AbstractAction implements ForumUserRegistrationManagerAware {

    private static final Logger logger = Logger.getLogger(ConfirmRegistrationAction.class);

    private String login;

    private String confirmationToken;

    private ForumUserRegistrationManager registrationManager;

    public void setTheLogin(String login) {
        this.login = login;
    }

    public String getTheLogin() {
        return login;
    }

    public void setTheConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public String getTheConfirmationToken() {
        return this.confirmationToken;
    }

    public void setForumUserRegistrationManager(ForumUserRegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    protected String executeNext() throws HibernateException, AccessException, IOException {
        try {
            ForumUserRegistrationRequest request = this.registrationManager.confirmRequest(super.getSession(), login, confirmationToken);
            ForumUser user = new ForumUser();
            user.setLogin(request.getLogin());
            user.setPasswordHash(request.getPasswordHash());
            user.setEmail(request.getEmail());
            user.setRegistrationTime(new Timestamp(new Date().getTime()));
            super.getSession().save(user);
            logger.info("Registred user '" + request.getLogin() + "'");
        } catch (Exception ex) {
            logger.warn(ex);
            super.addActionError("������ ������� �� ����������� ���.  ��������, �� ��� ������ ����� ��� ��� ��� ����� &mdash; � ���� ������ ��� ������� ������������������ �����.");
            return Action.ERROR;
        }
        return Action.SUCCESS;
    }
}
