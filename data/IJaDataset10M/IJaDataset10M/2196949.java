package simple.web;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.controller.LifecycleStage;
import org.testng.collections.Lists;
import simple.persistence.entity.User;
import java.util.List;

@UrlBinding("/Users.action")
public class UsersActionBean extends BaseActionBean {

    private List<User> users = Lists.newArrayList();

    @SuppressWarnings({ "UnusedDeclaration" })
    public final List<User> getUsers() {
        return users;
    }

    public final void setUsers(List<User> users) {
        this.users = users;
    }

    @Before(stages = LifecycleStage.BindingAndValidation)
    public void setUpUsers() {
        setUsers(getUserManager().getUsers());
    }

    @DefaultHandler
    @HandlesEvent("view")
    public Resolution view() {
        return new ForwardResolution("stripes_users.jsp");
    }

    @HandlesEvent("autocomplete")
    public Resolution autocomplete() {
        return new ForwardResolution("autocomplete_users.jsp");
    }

    public Resolution forward() {
        return new ForwardResolution(UsersAjaxActionBean.class).addParameter("doStuff", true);
    }
}
