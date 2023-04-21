package org.openliberty.arisid.policy.application;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.axiom.om.OMElement;
import org.apache.neethi.PolicyComponent;
import org.openliberty.arisid.stack.PolicyException;

public class LifetimeConstraint extends PrivacyConstraint {

    private static final String INVALID_LIFETIME_CONSTRAINT = "Invalid Lifetime Constraint - Missing a value";

    protected static final String NAMESPACE = PolicyUtil.AppIdPolicy_NS;

    protected static final String ELEMENT = "LifetimeConstraint";

    protected static final String ELEMENT_STARTDATE = "StartTime";

    protected static final String ELEMENT_ENDDATE = "EndTime";

    protected static final String ELEMENT_MINS = "Minutes";

    protected static final String ELEMENT_HOURS = "Hours";

    public static final QName qelement = new QName(NAMESPACE, ELEMENT);

    protected static final QName qStart = new QName(NAMESPACE, ELEMENT_STARTDATE);

    protected static final QName qEnd = new QName(NAMESPACE, ELEMENT_ENDDATE);

    protected static final QName qMins = new QName(NAMESPACE, ELEMENT_MINS);

    protected static final QName qHours = new QName(NAMESPACE, ELEMENT_HOURS);

    private int _hours = -1;

    private int _minutes = -1;

    private Date _sDate = null;

    private Date _eDate = null;

    public LifetimeConstraint(int hours, int minutes) {
        this._hours = hours;
        this._minutes = minutes;
        this._issuer = null;
    }

    public LifetimeConstraint(Date startDate, Date endDate) {
        this._eDate = endDate;
        this._sDate = startDate;
    }

    public LifetimeConstraint(OMElement element) throws PolicyException {
        OMElement mins = element.getFirstChildWithName(qMins);
        if (mins != null) {
            try {
                this._minutes = Integer.parseInt(mins.getText());
                OMElement hours = element.getFirstChildWithName(qHours);
                if (hours != null) this._hours = Integer.parseInt(hours.getText());
            } catch (Exception e) {
                throw new PolicyException("Invalid LifetimeConstraint minutes or hours value. Must be an integer.");
            }
        } else {
            OMElement start = element.getFirstChildWithName(qStart);
            OMElement end = element.getFirstChildWithName(qEnd);
            if (start == null || end == null) {
                throw new PolicyException("Invalid LifetimeConstraint. Missing start or end time.");
            }
            try {
                this._sDate = DateFormat.getDateInstance().parse(start.getText());
                this._eDate = DateFormat.getDateInstance().parse(end.getText());
            } catch (ParseException e) {
                throw new PolicyException("Invalid LifetimeConstraint. Unable to parse start or end time.");
            }
        }
    }

    /**
	 * @return The number of minutes this constraint is defined for or -1 if this constraint is defined by a DateTime instead.
	 */
    public int getMinutes() {
        return this._minutes;
    }

    /**
	 * @return The number of hours this constraint is defined for or -1 if this constraint is defined by a DateTime instead.
	 */
    public int getHours() {
        return this._hours;
    }

    public Date getStartDate() {
        return this._sDate;
    }

    public Date getEndDate() {
        return this._eDate;
    }

    public QName getName() {
        return qelement;
    }

    public PolicyComponent normalize() {
        return this;
    }

    public void serialize(XMLStreamWriter xmlwriter) throws XMLStreamException {
        super.serialize(xmlwriter);
        if (this._minutes > -1) {
            xmlwriter.writeStartElement(NAMESPACE, ELEMENT);
            xmlwriter.writeStartElement(NAMESPACE, ELEMENT_MINS);
            xmlwriter.writeCharacters("" + this._minutes);
            xmlwriter.writeEndElement();
            xmlwriter.writeStartElement(NAMESPACE, ELEMENT_HOURS);
            xmlwriter.writeCharacters("" + this._hours);
            xmlwriter.writeEndElement();
            xmlwriter.writeEndElement();
        } else if (this._eDate != null) {
            xmlwriter.writeStartElement(NAMESPACE, ELEMENT);
            xmlwriter.writeStartElement(NAMESPACE, ELEMENT_STARTDATE);
            xmlwriter.writeCharacters(DateFormat.getDateInstance().format(this._sDate));
            xmlwriter.writeEndElement();
            xmlwriter.writeStartElement(NAMESPACE, ELEMENT_ENDDATE);
            xmlwriter.writeCharacters(DateFormat.getDateInstance().format(this._eDate));
            xmlwriter.writeEndElement();
            xmlwriter.writeEndElement();
        } else throw new XMLStreamException(INVALID_LIFETIME_CONSTRAINT);
    }

    public boolean equal(PolicyComponent policyAssertion) {
        if (!policyAssertion.getClass().equals(this.getClass())) return false;
        LifetimeConstraint pa = (LifetimeConstraint) policyAssertion;
        if (this._minutes > -1) {
            return (this._minutes == pa.getMinutes() && this._hours == pa.getHours());
        }
        return (this._sDate.equals(pa.getStartDate()) && this._eDate.equals(pa.getEndDate()));
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("LifetimeConstraint: ");
        if (this._minutes > -1) {
            buf.append("hours=" + this._hours);
            buf.append(", minutres=" + this._minutes);
        } else {
            buf.append("start=").append(this._sDate.toString());
            buf.append(", end=").append(this._eDate.toString());
        }
        buf.append(" issuer=").append(this._issuer == null ? "undefined" : this._issuer);
        return buf.toString();
    }
}
