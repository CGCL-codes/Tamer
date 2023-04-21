package fr.xebia.demo.objectgrid.data;

import java.math.BigDecimal;
import java.util.Map;
import org.apache.log4j.Logger;
import com.ibm.websphere.objectgrid.ObjectMap;
import com.ibm.websphere.objectgrid.Session;
import com.ibm.websphere.objectgrid.datagrid.AgentManager;
import com.ibm.websphere.projector.Tuple;
import com.ibm.websphere.projector.md.TupleAttribute;
import fr.xebia.demo.objectgrid.AbstractObjectGridTest;

/**
 * @author <a href="mailto:cyrille.leclerc@pobox.com">Cyrille Le Clerc</a>
 */
public class EmployeeSalaryProcessorAgentTest extends AbstractObjectGridTest {

    private static final Logger logger = Logger.getLogger(EmployeeSalaryProcessorAgentTest.class);

    @SuppressWarnings("unchecked")
    public void testAgent() throws Exception {
        EmployeeSalaryProcessorAgent agent = new EmployeeSalaryProcessorAgent();
        Session session = objectGrid.getSession();
        session.begin();
        ObjectMap employeeMap = session.getMap("Employee");
        AgentManager agentManager = employeeMap.getAgentManager();
        Map<Tuple, BigDecimal> cumulativeSalaryPerEmployee = agentManager.callMapAgent(agent);
        for (Tuple employeeTuple : cumulativeSalaryPerEmployee.keySet()) {
            BigDecimal cumulativeSalary = cumulativeSalaryPerEmployee.get(employeeTuple);
            String msg = "MapAgent result: ";
            for (int i = 0; i < employeeTuple.getMetadata().getNumAttributes(); i++) {
                TupleAttribute tupleAttribute = employeeTuple.getMetadata().getAttribute(i);
                msg += tupleAttribute.getName() + "=" + employeeTuple.getAttribute(i) + ",";
            }
            msg += "cumulative salary: " + cumulativeSalary;
            logger.debug(msg);
        }
        session.commit();
    }
}
