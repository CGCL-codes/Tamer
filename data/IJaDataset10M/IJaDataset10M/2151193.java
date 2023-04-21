package fr.xebia.demo.objectgrid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import junit.framework.Assert;
import com.ibm.websphere.objectgrid.BackingMap;
import com.ibm.websphere.objectgrid.ClientClusterContext;
import com.ibm.websphere.objectgrid.ObjectGrid;
import com.ibm.websphere.objectgrid.ObjectGridManager;
import com.ibm.websphere.objectgrid.ObjectGridManagerFactory;
import com.ibm.websphere.objectgrid.PartitionManager;
import com.ibm.websphere.objectgrid.Session;
import com.ibm.websphere.objectgrid.datagrid.AgentManager;
import com.ibm.websphere.objectgrid.datagrid.MapGridAgent;
import com.ibm.websphere.objectgrid.em.EntityManager;
import com.ibm.websphere.objectgrid.em.Query;
import fr.xebia.demo.objectgrid.data.Employee;

public class EntityManagerTest extends AbstractObjectGridTest {

    public void testObjectQueryOnPartitionedData() throws Exception {
        Session session = this.objectGrid.getSession();
        EntityManager entityManager = session.getEntityManager();
        BackingMap employeeBackingMap = objectGrid.getMap("Employee");
        PartitionManager partitionManager = employeeBackingMap.getPartitionManager();
        int actualEmployeeCount = 0;
        for (int partitionId = 0; partitionId < partitionManager.getNumOfPartitions(); partitionId++) {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("select e from Employee e where e.lastName=:lastName");
            query.setParameter("lastName", "Doe");
            query.setPartition(partitionId);
            Iterator<Employee> iterator = query.getResultIterator();
            while (iterator.hasNext()) {
                Employee employee = iterator.next();
                System.out.println("Partition " + partitionId + " : " + employee);
                actualEmployeeCount++;
            }
            entityManager.getTransaction().commit();
        }
        assertEquals(1, actualEmployeeCount);
    }

    public void testObjectQueryOnPartitionedGridWithAgent() throws Exception {
        ObjectGridManager objectGridManager = ObjectGridManagerFactory.getObjectGridManager();
        ClientClusterContext clientClusterContext = objectGridManager.connect("localhost:2809", null, null);
        ObjectGrid objectGrid = objectGridManager.getObjectGrid(clientClusterContext, "xebiaGrid");
        Session session = objectGrid.getSession();
        AgentManager agentManager = session.getMap(QueryAgent.AGENT_RUNNER_MAP_NAME).getAgentManager();
        MapGridAgent agent = new QueryAgent<Employee>("select e from Employee e where e.lastName=:lastName", "lastName", "Doe");
        Map<Integer, List<Employee>> employeesByPartitionId = agentManager.callMapAgent(agent);
        List<Employee> result = new ArrayList<Employee>();
        for (Entry<Integer, List<Employee>> entry : employeesByPartitionId.entrySet()) {
            Integer partitionId = entry.getKey();
            List<Employee> partitionResults = entry.getValue();
            System.out.println("Found " + partitionResults.size() + " entries on partition " + partitionId);
            result.addAll(partitionResults);
        }
        for (Employee employee : result) {
            System.out.println(employee);
        }
        int actualEmployeeCount = result.size();
        assertEquals(1, actualEmployeeCount);
    }
}
