package wilos.application.console;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import wilos.business.services.misc.concretetask.ConcreteTaskDescriptorService;
import wilos.business.services.misc.concreteworkbreakdownelement.ConcreteWorkOrderService;
import wilos.model.misc.concretetask.ConcreteTaskDescriptor;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrder;
import wilos.model.misc.concreteworkbreakdownelement.ConcreteWorkOrderId;
import wilos.utils.Constantes.State;

public class DependenciesButtonsHandlerTest {

    private ConcreteWorkOrderService concreteWOS;

    @SuppressWarnings("unused")
    private ConcreteTaskDescriptorService concreteTDS;

    private ConcreteWorkOrder cwo;

    private ConcreteWorkOrderId cwoID;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        DependenciesButtonsHandlerTest dbh = new DependenciesButtonsHandlerTest();
        dbh.concreteWOS = (ConcreteWorkOrderService) ctx.getBean("ConcreteWorkOrderService");
        dbh.concreteTDS = (ConcreteTaskDescriptorService) ctx.getBean("ConcreteTaskDescriptorService");
        ConcreteTaskDescriptor ctd = dbh.concreteTDS.getConcreteTaskDescriptor("40288182149d807d01149d81eee2015a");
        dbh.cwo = dbh.concreteWOS.getConcreteWorkOrder(dbh.cwoID);
        dbh.concreteWOS.deleteConcreteWorkOrder(dbh.cwo);
        ctd.setState(State.CREATED);
        dbh.cwo = dbh.concreteWOS.getConcreteWorkOrder(dbh.cwoID);
        dbh.concreteWOS.deleteConcreteWorkOrder(dbh.cwo);
        dbh.concreteTDS.finishConcreteTaskDescriptor(ctd);
        dbh.cwo = dbh.concreteWOS.getConcreteWorkOrder(dbh.cwoID);
        dbh.concreteWOS.deleteConcreteWorkOrder(dbh.cwo);
        dbh.concreteTDS.suspendConcreteTaskDescriptor(ctd);
        dbh.cwo = dbh.concreteWOS.getConcreteWorkOrder(dbh.cwoID);
        dbh.concreteWOS.deleteConcreteWorkOrder(dbh.cwo);
        dbh.concreteTDS.startConcreteTaskDescriptor(ctd);
        dbh.cwo = dbh.concreteWOS.getConcreteWorkOrder(dbh.cwoID);
        dbh.concreteWOS.deleteConcreteWorkOrder(dbh.cwo);
    }
}
