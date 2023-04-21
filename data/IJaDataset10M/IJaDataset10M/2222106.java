package wilos.test.business.services.misc.concreteworkbreakdownelement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkOrderService;
import wilos.business.services.misc.project.ProjectService;
import wilos.hibernate.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElementDao;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkBreakdownElement;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrder;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrderId;
import wilos.model.misc.project.Project;
import wilos.test.TestConfiguration;
import wilos.utils.Constantes;

public class ConcreteWorkOrderServiceTest {

    private ConcreteWorkOrderService concreteWorkOrderService = (ConcreteWorkOrderService) TestConfiguration.getInstance().getApplicationContext().getBean("ConcreteWorkOrderService");

    private ConcreteWorkBreakdownElementDao concreteWorkBreakdownElementDao = (ConcreteWorkBreakdownElementDao) TestConfiguration.getInstance().getApplicationContext().getBean("ConcreteWorkBreakdownElementDao");

    private ProjectService projectService = (ProjectService) TestConfiguration.getInstance().getApplicationContext().getBean("ProjectService");

    private ConcreteWorkBreakdownElement concreteWorkBreakdownElement1 = null;

    private ConcreteWorkBreakdownElement concreteWorkBreakdownElement2 = null;

    private String cwbde1Id = "";

    private String cwbde2Id = "";

    private Date date;

    @Before
    public void setUp() {
        try {
            date = Constantes.DATE_FORMAT.parse("18/01/2007 10:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.concreteWorkBreakdownElement1 = new ConcreteWorkBreakdownElement();
        this.concreteWorkBreakdownElement1.setConcreteName("My concrete name");
        this.concreteWorkBreakdownElement1.setPlannedTime(25.0f);
        this.concreteWorkBreakdownElement1.setPlannedFinishingDate(date);
        this.concreteWorkBreakdownElement2 = new ConcreteWorkBreakdownElement();
        this.concreteWorkBreakdownElement2.setConcreteName("Your concrete name");
        this.concreteWorkBreakdownElement2.setPlannedTime(4.0f);
        this.concreteWorkBreakdownElement2.setPlannedFinishingDate(date);
    }

    @After
    public void tearDown() {
        this.concreteWorkBreakdownElement1 = null;
        this.concreteWorkBreakdownElement2 = null;
    }

    @Test
    public void testStorageOfAConcreteWorkOrderInATransaction() {
        Project p = new Project();
        p.setConcreteName("Wilos");
        p.setDescription("projet de test");
        p.setIsFinished(true);
        this.projectService.getProjectDao().saveOrUpdateProject(p);
        this.cwbde1Id = this.concreteWorkBreakdownElementDao.saveOrUpdateConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement1);
        this.cwbde2Id = this.concreteWorkBreakdownElementDao.saveOrUpdateConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement2);
        ConcreteWorkOrderId concreteWorkOrderId = this.concreteWorkOrderService.saveConcreteWorkOrder(this.cwbde1Id, this.cwbde2Id, "startToFinish", p.getId());
        ConcreteWorkBreakdownElement retrievedConcreteWorkBreakdownElement1 = this.concreteWorkBreakdownElementDao.getConcreteWorkBreakdownElement(this.cwbde1Id);
        assertNotNull("not null", retrievedConcreteWorkBreakdownElement1);
        Set<ConcreteWorkOrder> concreteWorkOrdersS = this.concreteWorkOrderService.getAllConcreteWorkOrdersFromConcreteWorkBreakdownElement(retrievedConcreteWorkBreakdownElement1);
        assertEquals("size of concreteWorkOrders", 1, concreteWorkOrdersS.size());
        assertEquals("size of concreteWorkOrders", 1, this.concreteWorkOrderService.getConcreteSuccessors(retrievedConcreteWorkBreakdownElement1).size());
        ConcreteWorkBreakdownElement retrievedConcreteWorkBreakdownElement2 = this.concreteWorkBreakdownElementDao.getConcreteWorkBreakdownElement(this.cwbde2Id);
        Set<ConcreteWorkOrder> concreteWorkOrdersP = this.concreteWorkOrderService.getAllConcreteWorkOrdersFromConcreteWorkBreakdownElement(retrievedConcreteWorkBreakdownElement2);
        assertEquals("size of concreteWorkOrders", 1, concreteWorkOrdersP.size());
        assertEquals("size of concreteWorkOrders", 1, this.concreteWorkOrderService.getConcretePredecessors(retrievedConcreteWorkBreakdownElement2).size());
        ConcreteWorkOrder concreteWorkOrder = this.concreteWorkOrderService.getConcreteWorkOrder(concreteWorkOrderId);
        this.concreteWorkOrderService.deleteConcreteWorkOrder(concreteWorkOrder);
        this.concreteWorkBreakdownElementDao.deleteConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement2);
        this.concreteWorkBreakdownElementDao.deleteConcreteWorkBreakdownElement(this.concreteWorkBreakdownElement1);
        this.projectService.deleteProject(p.getId());
    }
}
