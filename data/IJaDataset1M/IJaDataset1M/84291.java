package org.wwweeeportal.util.ws.rs;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;
import org.w3c.dom.*;
import javax.ws.rs.core.*;
import org.wwweeeportal.util.*;
import org.wwweeeportal.util.collection.*;
import org.wwweeeportal.util.xml.dom.*;

/**
 * A resource access definition.
 */
public abstract class RSAccessControl implements Serializable {

    /**
   * @see Serializable
   */
    private static final long serialVersionUID = 1L;

    /**
   * An optional identifier for this access control.
   */
    protected final String id;

    /**
   * Construct a new <code>RSAccessControl</code>.
   * 
   * @param id An optional identifier for this access control.
   */
    protected RSAccessControl(final String id) {
        this.id = id;
        return;
    }

    /**
   * Get the identifier for this access control.
   * 
   * @return The identifier, or <code>null</code> if there is none.
   */
    public String getID() {
        return id;
    }

    /**
   * Is the user represented by the <code>securityContext</code> allowed access to this resource?
   * 
   * @param securityContext The {@link SecurityContext} representing the user.
   * @return <code>true</code> if the user should be allowed access to the resource.
   */
    public abstract boolean hasAccess(SecurityContext securityContext);

    /**
   * Parse an access control from the given DOM tree.
   * 
   * @param accessControlElement The {@link Element} to parse.
   * @param nsURI The <a href="http://www.w3.org/TR/xml-names/">namespace</a> {@link URI} being used by the access
   * control.
   * @return The new access control, or <code>null</code> if the element doesn't represent one.
   */
    protected static final RSAccessControl parseAccessControlXMLChild(final Element accessControlElement, final URI nsURI) {
        final String name = accessControlElement.getLocalName();
        final String id = StringUtil.mkNull(accessControlElement.getAttributeNS(null, "id"), false);
        if (name.equals("user")) {
            final String userRole = StringUtil.mkNull(accessControlElement.getAttributeNS(null, "role"), false);
            final String login = StringUtil.mkNull(accessControlElement.getAttributeNS(null, "login"), false);
            if ((userRole != null) && (login != null)) {
                final List<RSAccessControl> operands = new ArrayList<RSAccessControl>(2);
                operands.add(new UserRole(null, userRole));
                operands.add(new UserLogin(null, login));
                return new LogicalAND(id, operands);
            } else if (userRole != null) {
                return new UserRole(id, userRole);
            } else if (login != null) {
                return new UserLogin(id, login);
            }
        } else if (name.equals("not")) {
            final List<RSAccessControl> operands = parseAccessControlXMLChildren(accessControlElement, nsURI);
            if (operands != null) {
                final RSAccessControl operand = (operands.size() > 1) ? new LogicalOR(null, operands) : operands.get(0);
                return new LogicalNOT(id, operand);
            }
        } else if (name.equals("and")) {
            final List<RSAccessControl> operands = parseAccessControlXMLChildren(accessControlElement, nsURI);
            if (operands != null) {
                return new LogicalAND(id, operands);
            }
        } else if (name.equals("or")) {
            final List<RSAccessControl> operands = parseAccessControlXMLChildren(accessControlElement, nsURI);
            if (operands != null) {
                return new LogicalOR(id, operands);
            }
        }
        return null;
    }

    /**
   * Parse any access controls under the given <code>parentNode</code>.
   * 
   * @param parentNode The {@link Node} containing access control children.
   * @param nsURI The <a href="http://www.w3.org/TR/xml-names/">namespace</a> {@link URI} being used by the access
   * controls.
   * @return A List of access controls.
   */
    protected static final List<RSAccessControl> parseAccessControlXMLChildren(final Node parentNode, final URI nsURI) {
        final List<Element> accessControlElements = DOMUtil.getChildElements(parentNode, nsURI, null);
        if (accessControlElements == null) return null;
        final ArrayList<RSAccessControl> accessControls = new ArrayList<RSAccessControl>(accessControlElements.size());
        for (Element accessControlElement : accessControlElements) {
            final RSAccessControl accessControl = parseAccessControlXMLChild(accessControlElement, nsURI);
            if (accessControl == null) continue;
            accessControls.add(accessControl);
        }
        if (accessControls.isEmpty()) return null;
        accessControls.trimToSize();
        return accessControls;
    }

    /**
   * Parse any access controls under the given <code>parentNode</code>.
   * 
   * @param parentNode The {@link Node} containing access control children.
   * @param nsURI The <a href="http://www.w3.org/TR/xml-names/">namespace</a> {@link URI} being used by the access
   * controls.
   * @return The resulting access control. If more than one was found, they will be {@linkplain LogicalOR OR}'d
   * together.
   */
    public static final RSAccessControl parseAccessControlXML(final Node parentNode, final URI nsURI) {
        if (parentNode == null) return null;
        synchronized (DOMUtil.getDocument(parentNode)) {
            final Element accessControlElement = DOMUtil.getChildElement(parentNode, nsURI, "access-control");
            final List<RSAccessControl> operands = parseAccessControlXMLChildren(accessControlElement, nsURI);
            return (operands != null) ? ((operands.size() > 1) ? new LogicalOR(null, operands) : operands.get(0)) : null;
        }
    }

    /**
   * Construct a DOM Element to represent the XML for this access control.
   * 
   * @param parentNode The {@link Node} which will serve as {@linkplain Node#getParentNode() parent} to the created
   * Element.
   * @param nsURI The <a href="http://www.w3.org/TR/xml-names/">namespace</a> {@link URI} to use for the created
   * Element.
   * @param nsPrefix The namespace prefix to use for the created Element.
   * @param topLevel Is this control being printed directly under the root &quot;access-control&quot; Element?
   * @throws IllegalArgumentException If <code>parentNode</code> is <code>null</code>.
   * @throws DOMException If there was a problem creating the Elements.
   */
    protected abstract void printAccessControlXMLImpl(Node parentNode, URI nsURI, String nsPrefix, boolean topLevel) throws IllegalArgumentException, DOMException;

    /**
   * Construct a DOM Element to represent the XML for this access control.
   * 
   * @param parentNode The {@link Node} which will serve as {@linkplain Node#getParentNode() parent} to the created
   * Element.
   * @param nsURI The <a href="http://www.w3.org/TR/xml-names/">namespace</a> {@link URI} to use for the created
   * Element.
   * @param nsPrefix The namespace prefix to use for the created Element.
   * @return The DOM {@link Element} representing this control.
   * @throws IllegalArgumentException If <code>parentNode</code> is <code>null</code>.
   * @throws DOMException If there was a problem creating the Elements.
   */
    public Element printAccessControlXML(final Node parentNode, final URI nsURI, final String nsPrefix) throws IllegalArgumentException, DOMException {
        final Element accessControlElement = DOMUtil.createElement(nsURI, nsPrefix, "access-control", parentNode);
        printAccessControlXMLImpl(accessControlElement, nsURI, nsPrefix, true);
        return accessControlElement;
    }

    /**
   * Access provision based on user {@linkplain SecurityContext#isUserInRole(String) role}.
   */
    public static final class UserRole extends RSAccessControl {

        /**
     * @see Serializable
     */
        private static final long serialVersionUID = 1L;

        /**
     * The {@linkplain SecurityContext#isUserInRole(String) role} which is allowed to access the resource.
     */
        protected final String userRole;

        /**
     * Construct a new <code>UserRoleAccessControl</code>.
     * 
     * @param id An optional identifier for this access control.
     * @param userRole The {@linkplain SecurityContext#isUserInRole(String) role} which is allowed to access the
     * resource.
     * @throws IllegalArgumentException If <code>userRole</code> is <code>null</code>.
     */
        public UserRole(final String id, final String userRole) throws IllegalArgumentException {
            super(id);
            if (userRole == null) throw new IllegalArgumentException("null userRole");
            this.userRole = userRole.intern();
            return;
        }

        /**
     * Get the {@linkplain SecurityContext#isUserInRole(String) role} which is allowed to access the resource.
     * 
     * @return The role name.
     */
        public String getUserRole() {
            return userRole;
        }

        @Override
        public boolean hasAccess(final SecurityContext securityContext) {
            return securityContext.isUserInRole(userRole);
        }

        @Override
        protected void printAccessControlXMLImpl(final Node parentNode, final URI nsURI, final String nsPrefix, final boolean topLevel) throws IllegalArgumentException, DOMException {
            final Element accessControlElement = DOMUtil.createElement(nsURI, nsPrefix, "user", parentNode);
            if (id != null) DOMUtil.createAttribute(null, null, "id", id, accessControlElement);
            DOMUtil.createAttribute(null, null, "role", userRole, accessControlElement);
            return;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof UserRole)) return false;
            return (userRole.equals(((UserRole) obj).getUserRole()));
        }

        @Override
        public int hashCode() {
            return userRole.hashCode();
        }

        @Override
        public String toString() {
            return "[UserRole=" + userRole + ']';
        }
    }

    /**
   * Access provision based on remote {@linkplain SecurityContext#getUserPrincipal() user} login.
   */
    public static final class UserLogin extends RSAccessControl {

        /**
     * @see Serializable
     */
        private static final long serialVersionUID = 1L;

        /**
     * The login of the user which is allowed access to the resource.
     */
        protected final String login;

        /**
     * Construct a new <code>UserLoginAccessControl</code>.
     * 
     * @param id An optional identifier for this access control.
     * @param login The login of the user which is allowed access to the resource.
     * @throws IllegalArgumentException If <code>login</code> is <code>null</code>.
     */
        public UserLogin(final String id, final String login) throws IllegalArgumentException {
            super(id);
            if (login == null) throw new IllegalArgumentException("null login");
            this.login = login.intern();
            return;
        }

        /**
     * Get the login of the user which is allowed access to the resource.
     * 
     * @return The login name.
     */
        public String getUserLogin() {
            return login;
        }

        @Override
        public boolean hasAccess(final SecurityContext securityContext) {
            final Principal principal = securityContext.getUserPrincipal();
            return (principal != null) ? login.equals(principal.getName()) : false;
        }

        @Override
        protected void printAccessControlXMLImpl(final Node parentNode, final URI nsURI, final String nsPrefix, final boolean topLevel) throws IllegalArgumentException, DOMException {
            final Element accessControlElement = DOMUtil.createElement(nsURI, nsPrefix, "user", parentNode);
            if (id != null) DOMUtil.createAttribute(null, null, "id", id, accessControlElement);
            DOMUtil.createAttribute(null, null, "login", login, accessControlElement);
            return;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof UserLogin)) return false;
            return (login.equals(((UserLogin) obj).getUserLogin()));
        }

        @Override
        public int hashCode() {
            return login.hashCode();
        }

        @Override
        public String toString() {
            return "[UserLogin=" + login + ']';
        }
    }

    /**
   * Access provision through a logic based combination of several access control operands.
   */
    public abstract static class LogicalConnective extends RSAccessControl {

        /**
     * @see Serializable
     */
        private static final long serialVersionUID = 1L;

        /**
     * The other controls which are combined to form this one.
     */
        protected final List<RSAccessControl> operands;

        /**
     * Construct a new <code>LogicalConnectiveAccessControl</code>.
     * 
     * @param id An optional identifier for this access control.
     * @param operands The other controls which are combined to form this one.
     * @throws IllegalArgumentException If <code>operands</code> are <code>null</code> or {@linkplain List#isEmpty()
     * empty}.
     */
        protected LogicalConnective(final String id, final List<RSAccessControl> operands) throws IllegalArgumentException {
            super(id);
            this.operands = Collections.unmodifiableList(new ArrayList<RSAccessControl>(operands));
            if ((this.operands == null) || (this.operands.isEmpty())) throw new IllegalArgumentException("null operands");
            return;
        }

        /**
     * Get the other controls which are combined to form this one.
     * 
     * @return A {@link List} of controls.
     */
        public List<RSAccessControl> getOperands() {
            return operands;
        }

        /**
     * Get the name for the logical operation being performed on the {@linkplain #getOperands() operands}.
     * 
     * @return The short name of the connection.
     */
        protected abstract String getConnectiveName();

        @Override
        protected void printAccessControlXMLImpl(final Node parentNode, final URI nsURI, final String nsPrefix, final boolean topLevel) throws IllegalArgumentException, DOMException {
            final Element accessControlElement = DOMUtil.createElement(nsURI, nsPrefix, getConnectiveName().toLowerCase(Locale.ROOT), parentNode);
            if (id != null) DOMUtil.createAttribute(null, null, "id", id, accessControlElement);
            for (RSAccessControl operand : operands) {
                operand.printAccessControlXMLImpl(accessControlElement, nsURI, nsPrefix, false);
            }
            return;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof LogicalConnective)) return false;
            final LogicalConnective otherConnective = (LogicalConnective) obj;
            return (MiscUtil.equal(getConnectiveName(), otherConnective.getConnectiveName())) && (operands.equals(otherConnective.getOperands()));
        }

        @Override
        public int hashCode() {
            return operands.hashCode();
        }

        @Override
        public String toString() {
            return getConnectiveName() + CollectionUtil.toString(operands);
        }
    }

    /**
   * Access provision through logical negation of some other access control operand.
   */
    public static final class LogicalNOT extends LogicalConnective {

        /**
     * @see Serializable
     */
        private static final long serialVersionUID = 1L;

        /**
     * Construct a new <code>LogicalNOTAccessControl</code>.
     * 
     * @param id An optional identifier for this access control.
     * @param operand The control to be negated.
     * @throws IllegalArgumentException If <code>operand</code> is <code>null</code>.
     */
        public LogicalNOT(final String id, final RSAccessControl operand) throws IllegalArgumentException {
            super(id, (operand != null) ? Collections.singletonList(operand) : null);
            return;
        }

        @Override
        protected String getConnectiveName() {
            return "NOT";
        }

        /**
     * The control to be negated.
     * 
     * @return The control to be negated.
     */
        public RSAccessControl getOperand() {
            return CollectionUtil.first(operands, null);
        }

        @Override
        public boolean hasAccess(final SecurityContext securityContext) {
            return !getOperand().hasAccess(securityContext);
        }
    }

    /**
   * Access provision through a logical OR of other access control operands.
   */
    public static final class LogicalOR extends LogicalConnective {

        /**
     * @see Serializable
     */
        private static final long serialVersionUID = 1L;

        /**
     * Construct a new <code>LogicalORAccessControl</code>.
     * 
     * @param id An optional identifier for this access control.
     * @param operands The other controls which are OR'd by this one.
     * @throws IllegalArgumentException If <code>operands</code> are <code>null</code> or {@linkplain List#isEmpty()
     * empty}.
     */
        public LogicalOR(final String id, final List<RSAccessControl> operands) throws IllegalArgumentException {
            super(id, operands);
            return;
        }

        @Override
        protected String getConnectiveName() {
            return "OR";
        }

        @Override
        public boolean hasAccess(final SecurityContext securityContext) {
            for (RSAccessControl operand : operands) {
                if (operand.hasAccess(securityContext)) return true;
            }
            return false;
        }

        @Override
        protected void printAccessControlXMLImpl(final Node parentNode, final URI nsURI, final String nsPrefix, final boolean topLevel) throws IllegalArgumentException, DOMException {
            if ((!topLevel) || (id != null)) {
                super.printAccessControlXMLImpl(parentNode, nsURI, nsPrefix, topLevel);
                return;
            }
            for (RSAccessControl operand : operands) {
                operand.printAccessControlXMLImpl(parentNode, nsURI, nsPrefix, false);
            }
            return;
        }
    }

    /**
   * Access provision through a logical AND of other access control operands.
   */
    public static final class LogicalAND extends LogicalConnective {

        /**
     * @see Serializable
     */
        private static final long serialVersionUID = 1L;

        /**
     * Construct a new <code>LogicalANDAccessControl</code>.
     * 
     * @param id An optional identifier for this access control.
     * @param operands The other controls which are AND'd by this one.
     * @throws IllegalArgumentException If <code>operands</code> are <code>null</code> or {@linkplain List#isEmpty()
     * empty}.
     */
        public LogicalAND(final String id, final List<RSAccessControl> operands) throws IllegalArgumentException {
            super(id, operands);
            return;
        }

        @Override
        protected String getConnectiveName() {
            return "AND";
        }

        @Override
        public boolean hasAccess(final SecurityContext securityContext) {
            for (RSAccessControl operand : operands) {
                if (!operand.hasAccess(securityContext)) return false;
            }
            return true;
        }
    }
}
