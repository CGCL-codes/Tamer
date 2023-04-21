package br.com.arsmachina.example.tapestry.pages.project;

import java.util.List;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.ioc.annotations.Inject;
import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.example.entity.Manager;
import br.com.arsmachina.example.entity.Project;
import br.com.arsmachina.tapestrycrud.base.BaseEditPage;
import br.com.arsmachina.tapestrycrud.hibernatevalidator.mixins.HibernateValidatorMixin;

public class EditProject extends BaseEditPage<Project, Integer> {

    @Mixin
    @SuppressWarnings("unused")
    private HibernateValidatorMixin hibernateValidatorMixin;

    @Inject
    private UserController userController;

    /**
	 * Returns the {@link SelectModel} for the <code>manager</code> property.
	 * 
	 * @return a {@link SelectModel}.
	 */
    public SelectModel getManagerSM() {
        final List<User> users = userController.findByRole(Manager.class);
        return getSelectModelFactory().create(User.class, users);
    }
}
