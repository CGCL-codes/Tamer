package br.com.arsmachina.colloquium.tapestry.pages.forumuser;

import br.com.arsmachina.authorization.annotation.NeedsLoggedInUser;
import br.com.arsmachina.authorization.annotation.NeedsPermission;
import br.com.arsmachina.colloquium.Constants;
import br.com.arsmachina.colloquium.entity.ForumUser;
import java.lang.Integer;
import org.apache.tapestry5.annotations.Mixin;
import br.com.arsmachina.tapestrycrud.base.BaseEditPage;
import br.com.arsmachina.tapestrycrud.hibernatevalidator.mixins.HibernateValidatorMixin;

/**
 * {@link ForumUser} edition page.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@NeedsLoggedInUser
@NeedsPermission(Constants.FORUM_MANAGER_PERMISSION)
public class EditForumUser extends BaseEditPage<ForumUser, Integer> {

    @Mixin
    @SuppressWarnings("unused")
    private HibernateValidatorMixin hibernateValidatorMixin;
}
