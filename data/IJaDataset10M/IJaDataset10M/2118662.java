package ode.ws.test.hello;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author jrmacias <jrmacias@cnb.csic.es>
 */
@Stateless
@LocalBean
public class HelloWorldEJB {

    public String hello(final String message) {
        return "Hello " + message;
    }

    public String reverse(final String directString) {
        StringBuilder sb = new StringBuilder(directString);
        return sb.reverse().toString();
    }
}
