package org.usca.workshift.webservices.jaxws;

import org.usca.workshift.database.model.Workshift;
import org.usca.workshift.database.dao.WorkshiftDAO;
import javax.jws.WebService;

@WebService
public class WorkshiftService extends GenericService<Workshift, WorkshiftDAO> {
}
