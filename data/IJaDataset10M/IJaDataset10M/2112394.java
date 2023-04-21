package net.sf.brightside.instantevents.service.usecases;

import net.sf.brightside.instantevents.metamodel.User;
import net.sf.brightside.instantevents.service.crud.RetriveResults;
import net.sf.brightside.instantevents.service.usecases.exceptions.UserAuthenticationFailedException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class LoginCommandImpl implements LoginCommand<User> {

    private String username;

    private String password;

    private RetriveResults<User> retriveResults;

    public RetriveResults<User> getRetriveResults() {
        return retriveResults;
    }

    public void setRetriveResults(RetriveResults<User> retriveResults) {
        this.retriveResults = retriveResults;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public User execute() throws UserAuthenticationFailedException {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(User.class);
        detachedCriteria.add(Restrictions.like("username", username));
        detachedCriteria.add(Restrictions.like("password", password));
        try {
            return retriveResults.retriveResults(detachedCriteria).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new UserAuthenticationFailedException();
        }
    }
}
