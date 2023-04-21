package net.sareweb.acab.actions.user;

import net.sareweb.acab.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("usersBean")
public class UsersBean extends EntityQuery<User> {

    private static final String EJBQL = "select user from User user";

    private static final String[] RESTRICTIONS = { "lower(user.login) like lower(concat(#{usersBean.user.login},'%'))", "lower(user.name) like lower(concat(#{usersBean.user.name},'%'))", "lower(user.surname) like lower(concat(#{usersBean.user.surname},'%'))", "lower(user.type) = #{usersBean.user.type}" };

    private User user = new User();

    public UsersBean() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
    }

    public User getUser() {
        return user;
    }
}
