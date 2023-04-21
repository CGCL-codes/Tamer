package sf.net.sinve;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Some simple examples of using Esper to create invariant parsers.
 *
 * @author Teemu Kanstr�n
 */
public class EsperTest {

    public void init() throws Exception {
        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
        String expression = "select avg(price) from sf.net.sinve.VariableEvent.win:time_batch(10 sec)";
        EPStatement statement = epService.getEPAdministrator().createEPL(expression);
        MyListener listener = new MyListener();
        statement.addListener(listener);
        String expression2 = "select price from sf.net.sinve.VariableEvent where price < 51";
        EPStatement statement2 = epService.getEPAdministrator().createEPL(expression2);
        MinInvariantListener listener2 = new MinInvariantListener();
        statement2.addListener(listener2);
    }
}

class MyListener implements UpdateListener {

    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        EventBean event = newEvents[0];
        System.out.println("avg=" + event.get("avg(price)"));
    }
}

class MinInvariantListener implements UpdateListener {

    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        EventBean event = newEvents[0];
        System.out.println("min broken=" + event.get("price"));
    }
}
