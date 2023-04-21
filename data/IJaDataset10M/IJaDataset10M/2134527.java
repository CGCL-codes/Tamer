package helloservice.endpoint;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService()
public class Hello {

    private String message = new String("Hello, ");

    @WebMethod()
    public String sayHello(String name) {
        boolean a = true;
        Hello b = null;
        return message + name + ".";
    }
}
