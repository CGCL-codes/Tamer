package org.apache.wicket.examples.authentication;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

/**
 * A page only accessible by a user in the ADMIN role.
 * 
 * @author Jonathan Locke
 */
@AuthorizeInstantiation("ADMIN")
public class AdminPage extends BasePage {
}
