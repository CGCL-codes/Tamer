package org.commerce.mismo.xml;

import org.commerce.mismo.Employer;
import org.w3c.dom.Element;

/**
 * Generates a MISMO 2.1 <code>EMPLOYER</code> XML element from a populated
 * <code>Employer</code> object.
 * 
 * 
 * @version $Id: EmployerXMLGenerator.java,v 1.1.1.1 2007/04/16 05:07:04 clafonta Exp $
 */
class EmployerXMLGenerator extends XMLGeneratorSupport {

    /**
     * Creates a new EMPLOYER element.
     * 
     * @param  context the environment in which the EMPLOYER element should be
     *         created
     * @param  employer the <code>Employer</code> containing the data that
     *         should be contained in the resulting EMPLOYER element
     * @return a new, populated BORROWER element
     */
    public Element getElement(XMLGenerationContext context, Employer employer) {
        Element node = context.createElement("EMPLOYER");
        setAttribute(node, "_Name", employer.getName());
        setAddress(node, "_", employer.getAddress(), false);
        setAttribute(node, "_TelephoneNumber", employer.getTelephoneNumber());
        setAttribute(node, "CurrentEmploymentMonthsOnJob", employer.getMonthsOnJob());
        setAttribute(node, "CurrentEmploymentTimeInLineOfWorkYears", employer.getTimeInLineOfWork());
        setAttribute(node, "CurrentEmploymentYearsOnJob", employer.getYearsOnJob());
        setAttribute(node, "EmploymentBorrowerSelfEmployedIndicator", employer.isSelfEmployed());
        setAttribute(node, "EmploymentCurrentIndicator", employer.isCurrent());
        setAttribute(node, "EmploymentPositionDescription", employer.getEmploymentPositionDescription());
        setAttribute(node, "IncomeEmploymentMonthlyAmount", employer.getMonthlyIncome(), 2);
        setAttribute(node, "PreviousEmploymentEndDate", employer.getPreviousEmploymentEndDate());
        setAttribute(node, "PreviousEmploymentStartDate", employer.getPreviousEmploymentStartDate());
        setAttribute(node, "EmploymentPrimaryIndicator", employer.getEmploymentPrimaryIndicator());
        return node;
    }
}
