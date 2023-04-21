package br.com.arsmachina.colloquium.authorization;

import br.com.arsmachina.authentication.service.UserService;
import br.com.arsmachina.colloquium.entity.Forum;

/**
 * {@link Forum} authorizer class.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ForumUserAuthorizer extends BaseAuthorizer<Forum> {

    /**
	 * Single constructor of this class.
	 * 
	 * @param userService an {@link UserService}. It cannot be null.
	 */
    public ForumUserAuthorizer(UserService userService) {
        super(userService);
    }
}
