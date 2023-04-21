package org.plazmaforge.bsolution.employee.common.services;

import org.plazmaforge.bsolution.base.common.services.AttributeServiceHolder;
import org.plazmaforge.bsolution.base.common.services.BusinessableService;
import org.plazmaforge.bsolution.base.common.services.FileServiceHolder;
import org.plazmaforge.bsolution.employee.common.beans.Employee;
import org.plazmaforge.bsolution.organization.common.services.OrganizableService;

/**
 * @author Oleh Hapon
 * Date: 22.02.2004
 * Time: 10:56:15
 * $Id: EmployeeService.java,v 1.2 2010/04/28 06:24:30 ohapon Exp $
 */
public interface EmployeeService extends OrganizableService<Employee, Integer>, BusinessableService<Employee, Integer>, AttributeServiceHolder, FileServiceHolder {
}
