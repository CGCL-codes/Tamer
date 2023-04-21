package org.torweg.pulse.accesscontrol.attributes;

import java.util.regex.Pattern;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.jdom.Element;
import org.torweg.pulse.service.request.Command;

/**
 * tests a {@code AbstractValue<String>} against a <a
 * href="http://java.sun.com/docs/books/tutorial/essential/regex/">regular
 * expression</a>.
 * 
 * @author Thomas Weber, Daniel Dietz
 * @version $Revision: 1832 $
 */
@Entity
@XmlRootElement(name = "regex-string-check")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.FIELD)
public class RegexStringCheck extends AbstractTypedCheck<String> {

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = 8283164116299702084L;

    /**
	 * the regex.
	 */
    @Basic
    @XmlElement(name = "regex")
    private String regex;

    /**
	 * the compiled regex.
	 */
    @Transient
    @XmlTransient
    private Pattern pattern;

    /**
	 * used by Hibernate<sup>TM</sup>.
	 */
    @Deprecated
    protected RegexStringCheck() {
        super();
    }

    /**
	 * creates a new check with the given regular expression.
	 * 
	 * @param r
	 *            the regular expression
	 */
    public RegexStringCheck(final String r) {
        super();
        this.pattern = Pattern.compile(r);
        this.regex = r;
    }

    /**
	 * returns the parameter name.
	 * 
	 * @return the parameter name
	 */
    private String getParameterName() {
        return new StringBuilder().append("check_").append(getId()).append("_regex").toString();
    }

    /**
	 * returns the regular expression.
	 * 
	 * @return the regular expression
	 */
    public final String getRegex() {
        return this.regex;
    }

    /**
	 * sets the the regular expression.
	 * 
	 * @param r
	 *            the regular expression
	 */
    public final void setRegex(final String r) {
        this.pattern = Pattern.compile(r);
        this.regex = r;
    }

    /**
	 * returns the initialised check or {@code null}.
	 * 
	 * @param c
	 *            the command
	 * @return the initialised check or {@code null}
	 * @see org.torweg.pulse.accesscontrol.attributes.AbstractTypedCheck#checkFromCommand(org.torweg.pulse.service.request.Command)
	 */
    @Override
    public AbstractTypedCheck<String> checkFromCommand(final Command c) {
        String parameterName = getParameterName();
        if (c.getParameter(parameterName) != null) {
            return new RegexStringCheck(c.getParameter(parameterName).getFirstValue());
        }
        return null;
    }

    /**
	 * sets the values for the check from a given {@code Command} and returns
	 * the check with the newly set check.
	 * 
	 * @param c
	 *            the command
	 * @return the initialised check with the newly set check
	 * @see org.torweg.pulse.accesscontrol.attributes.AbstractTypedCheck#setCheckFromCommand(org.torweg.pulse.service.request.Command)
	 */
    @Override
    public AbstractTypedCheck<String> setCheckFromCommand(final Command c) {
        String parameterName = getParameterName();
        if ((c.getParameter(parameterName) != null) && (c.getParameter(parameterName).getFirstValue() != null)) {
            this.setRegex(c.getParameter(parameterName).getFirstValue());
        }
        return this;
    }

    /**
	 * returns whether the given value matches the regular expression of the
	 * check.
	 * 
	 * @param value
	 *            the value to check
	 * @return {@code true}, if and only if the entire value matches the regular
	 *         expression
	 * @see org.torweg.pulse.accesscontrol.attributes.AbstractTypedCheck#isValid(org.torweg.pulse.accesscontrol.attributes.AbstractValue)
	 */
    @Override
    public boolean isValid(final AbstractValue<?> value) {
        if (!(value instanceof StringValue)) {
            return false;
        }
        if (this.regex == null) {
            return true;
        }
        if (this.pattern == null) {
            this.pattern = Pattern.compile(this.regex);
        }
        return this.pattern.matcher(((StringValue) value).getValue()).matches();
    }

    /**
	 * returns a JDOM representation of the check.
	 * 
	 * @return a JDOM representation of the check
	 * @see org.torweg.pulse.bundle.JDOMable#deserializeToJDOM()
	 */
    public Element deserializeToJDOM() {
        Element check = new Element("TypedCheck").setAttribute("class", this.getClass().getCanonicalName());
        check.addContent(new Element("parameter").setAttribute("name", getParameterName()));
        Element regexElement = new Element("regex");
        if (this.regex != null) {
            regexElement.setAttribute("value", this.regex);
        }
        check.addContent(regexElement);
        return check;
    }
}
