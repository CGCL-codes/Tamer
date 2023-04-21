package org.torweg.pulse.component.core.accesscontrol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jdom.Element;
import org.torweg.pulse.accesscontrol.Role;
import org.torweg.pulse.bundle.Result;

/**
 * the {@code Result} of the {@code UserSelfEditController}.
 * 
 * @author Daniel Dietz
 * @version $Revision: 1448 $
 */
public class UserSelfEditControllerResult implements Result {

    /**
	 * a map of attributes to be added to the result.
	 */
    private final Map<String, String> attributes = new HashMap<String, String>();

    /**
	 * a set of error identifiers to be added to the result.
	 */
    private final Set<String> errors = new HashSet<String>();

    /**
	 * The added triggered {@code Role}s.
	 */
    private final Set<Role> addedTriggeredRoles = new HashSet<Role>();

    /**
	 * The removed triggered {@code Role}s.
	 */
    private final Set<Role> removedTriggeredRoles = new HashSet<Role>();

    /**
	 * returns the result-{@code Element} of the
	 * {@code UserSelfEditController}.
	 * 
	 * @see org.torweg.pulse.bundle.JDOMable#deserializeToJDOM()
	 * 
	 * @return the result-{@code Element} of the
	 *         {@code UserSelfEditController}
	 */
    public Element deserializeToJDOM() {
        Element result = new Element("result").setAttribute("class", getClass().getCanonicalName());
        for (Map.Entry<String, String> entry : this.attributes.entrySet()) {
            result.setAttribute(entry.getKey(), entry.getValue());
        }
        if (!this.errors.isEmpty()) {
            Element errorsEl = new Element("errors");
            for (String s : this.errors) {
                Element value = new Element("error").setAttribute("value", s);
                errorsEl.addContent(value);
            }
            result.addContent(errorsEl);
        }
        if (!this.addedTriggeredRoles.isEmpty()) {
            Element added = new Element("added-triggered-roles");
            for (Role r : this.addedTriggeredRoles) {
                added.addContent(r.deserializeToJDOM());
            }
            result.addContent(added);
        }
        if (!this.removedTriggeredRoles.isEmpty()) {
            Element removed = new Element("removed-triggered-roles");
            for (Role r : this.removedTriggeredRoles) {
                removed.addContent(r.deserializeToJDOM());
            }
            result.addContent(removed);
        }
        return result;
    }

    /**
	 * adds a attribute to the root-container of this result.
	 * 
	 * @param attributeName
	 *            the name of the attribute
	 * @param attributeValue
	 *            the value of the attribute
	 */
    public final void addAttribute(final String attributeName, final String attributeValue) {
        this.attributes.put(attributeName, attributeValue);
    }

    /**
	 * adds a new error-identifier to the errors of the result.
	 * 
	 * @param e
	 *            the error-identifier
	 */
    public final void addError(final String e) {
        this.errors.add(e);
    }

    /**
	 * Adds the passed {@code Role} to the added triggered
	 * {@code Role}s for the result.
	 * 
	 * @param role
	 *            the {@code Role} to add
	 */
    public final void addAddedTriggeredRole(final Role role) {
        this.addedTriggeredRoles.add(role);
    }

    /**
	 * Adds the passed {@code Role} to the removed triggered
	 * {@code Role}s for the result.
	 * 
	 * @param role
	 *            the {@code Role} to add
	 */
    public final void addRemovedTriggeredRole(final Role role) {
        this.removedTriggeredRoles.add(role);
    }
}
