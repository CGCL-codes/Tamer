package page;

import org.illico.common.display.DisplayNotifiable;
import org.illico.web.common.Context;

public class MyRequestPage implements DisplayNotifiable {

    public MyRequestPage() {
        System.out.println(getClass().getName() + ".init");
    }

    public void notifyDisplay() {
        Context.getResponseWriter().print("(Req) Hello World !");
    }
}
