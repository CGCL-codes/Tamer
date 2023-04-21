package net.sourceforge.webflowtemplate.reporting.securitymatrix.business;

import java.util.List;
import java.util.Map;
import javax.servlet.jsp.jstl.sql.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sourceforge.webflowtemplate.constants.security.SecurityRole;
import net.sourceforge.webflowtemplate.refdata.bean.ListRefDatum;
import net.sourceforge.webflowtemplate.reporting.securitymatrix.criteria.SecurityMatrixReportCriteria;
import net.sourceforge.webflowtemplate.reporting.securitymatrix.dao.SecurityMatrixReportDAO;
import net.sourceforge.webflowtemplate.reporting.securitymatrix.dao.SecurityMatrixReportDAOFactory;
import net.sourceforge.webflowtemplate.service.AbstractBusinessService;
import net.sourceforge.webflowtemplate.service.BusinessService;

public class SecurityMatrixReportByRoleMgrService extends AbstractBusinessService implements BusinessService {

    private final Logger LOGGER = LoggerFactory.getLogger(SecurityMatrixReportByRoleMgrService.class);

    public SecurityMatrixReportByRoleMgrService() {
    }

    public Map<String, List<ListRefDatum>> getListRefData(String pUsername, String pPassword) {
        final String METHOD_NAME = "getListRefData()";
        LOGGER.debug("{} -> called", METHOD_NAME);
        Map<String, List<ListRefDatum>> listRefData = null;
        setDAO(new SecurityMatrixReportDAOFactory(), pUsername, pPassword);
        LOGGER.debug("{} -> DAO constructed", METHOD_NAME);
        listRefData = getListRefData(getDAO(), SecurityRole.ROLE_SECURITY_MATRIX_BY_ROLE_REPORT);
        return listRefData;
    }

    public Result getReport(String pUsername, String pPassword, SecurityMatrixReportCriteria pSecurityMatrixReportCriteria) {
        final String METHOD_NAME = "getReport()";
        LOGGER.debug("{} -> called", METHOD_NAME);
        setDAO(new SecurityMatrixReportDAOFactory(), pUsername, pPassword);
        return ((SecurityMatrixReportDAO) getDAO()).getSecurityMatrixByRoleReport(pSecurityMatrixReportCriteria);
    }
}
