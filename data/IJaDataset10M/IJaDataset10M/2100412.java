package mx4j.examples.mbeans.rmi;

import javax.naming.InitialContext;

/**
 * @version $Revision: 1.3 $
 */
public class Client {

    public static void main(String[] args) throws Exception {
        InitialContext ctx = new InitialContext();
        MyRemoteService service = (MyRemoteService) ctx.lookup(MyRemoteService.JNDI_NAME);
        service.sayHello("Simon");
    }
}
