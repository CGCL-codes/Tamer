package adv.runtime.accesors;

import adv.language.*;
import adv.web.*;

/**
 * Alberto Vilches Ratón
 * User: avilches
 * Date: 06-nov-2006
 * Time: 21:43:07
 * To change this template use File | Settings | File Templates.
 */
public interface Funcionable {

    Object invoke(AdvOgnlscriptContext request, String funcName, Object args[]) throws NoSuchMethodException, RuntimeGameException;

    boolean hasFunction(AdvOgnlscriptContext request, String funcName);

    Class getExpossedMethods();
}
