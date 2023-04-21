package com.softwoehr.pigiron.webobj;

import java.lang.reflect.Constructor;
import com.softwoehr.pigiron.webobj.topview.*;
import com.softwoehr.pigiron.webobj.topview.functions.FunctionProxy;
import com.softwoehr.pigiron.webobj.topview.functions.FunctionTable;
import org.json.JSONException;

/**
 *  An executor of JSON descriptions of PigIron VSMAPI Functions
 * that consumes a Requestor and returns a Response.
 *
 * @author     jax
 * @see com.softwoehr.pigiron.webobj.topview.Requestor
 * @see com.softwoehr.pigiron.webobj.topview.Response
 */
public class Engine {

    /**
     *Constructor for the Engine object
     */
    public Engine() {
    }

    /**
     *  Execute from a Requestor, (a JSON description of a proposed PigIron
     * VSMAPI Function instance), and return a Response which contains (among
     * other things) the original Requestor object with the output param fields
     * filled out.
     *
     * The Response is created here and passed to the Function instance.
     * The Function instance recreates the Response and passes it back.
     * This is done to make the structure of <tt>Engine.execute()</tt> a little simpler
     * because the Response object passed back out of <tt>execute()</tt> might be the one
     * getting filled in by the Function instance or might be the one <tt>execute()</tt>
     * started with but filled in on some failure local to the Engine.
     *
     * @param  requestor                   Description of the Parameter
     * @return                             a Response which contains (among other things) the
     * original Requestor object with the output param fields filled out
     * @exception  org.json.JSONException  on JSON error creating the empty Response
     */
    public Response execute(Requestor requestor) throws org.json.JSONException {
        Response response = new Response();
        Function function = null;
        String functionName = null;
        String jsonErr = "";
        try {
            response = new Response(requestor);
            function = requestor.getFunction();
            functionName = function.get_function_name();
            try {
                if (functionName != null) {
                    Class<? extends FunctionProxy> functionProxy = FunctionTable.get(functionName);
                    if (functionProxy != null) {
                        Constructor ctor = functionProxy.getConstructor(new Class[] { Requestor.class, Response.class });
                        FunctionProxy proxy = FunctionProxy.class.cast(ctor.newInstance(new Object[] { requestor, response }));
                        response = proxy.execute();
                    } else {
                        response.setResult(Response.Results.PIGIRON_ERR.name());
                        response.setMessageText("No function found mapping to function name " + functionName + ".");
                    }
                } else {
                    response.setResult(Response.Results.JSON_ERR.name());
                    response.setMessageText("No function name found in JSON stream");
                }
            } catch (java.lang.InstantiationException ex) {
                response.setResult(Response.Results.PIGIRON_ERR.name());
                response.setMessageText("InstantiationException instancing FunctionProxy: " + ex.getMessage());
            } catch (java.lang.IllegalAccessException ex) {
                response.setResult(Response.Results.PIGIRON_ERR.name());
                response.setMessageText("IllegalAccessException instancing FunctionProxy: " + ex.getMessage());
            } catch (java.lang.IllegalArgumentException ex) {
                response.setResult(Response.Results.PIGIRON_ERR.name());
                response.setMessageText("IllegalArgumentException instancing FunctionProxy: " + ex.getMessage());
            } catch (java.lang.reflect.InvocationTargetException ex) {
                response.setResult(Response.Results.PIGIRON_ERR.name());
                response.setMessageText("InvocationTargetException instancing FunctionProxy: " + ex.getMessage());
            } catch (java.lang.NoSuchMethodException ex) {
                response.setResult(Response.Results.PIGIRON_ERR.name());
                response.setMessageText("NoSuchMethodException instancing FunctionProxy: " + ex.getMessage());
            }
        } catch (JSONException ex) {
            jsonErr = ex.getMessage();
            response.setResult(Response.Results.JSON_ERR.name());
            response.setMessageText(jsonErr);
        }
        return response;
    }

    /**
     *  A simple main program for testing the Engine class. It can be tested
     * on any supported function, e.g:<br>
     *
     * <tt>java -cp dist/pigiron.jar com.softwoehr.pigiron.webobj.Engine '{"function":{"function_name":"CheckAuthentication"},"host":{"dns_name":"mybox.mydomain.com","port_number":12345},"user":{"uid":"FRED","password":"FOOBAR"}}'</tt>
     *
     * @param  args                        arg0 JSON string of a Requestor
     * @exception  org.json.JSONException  on JSON error
     */
    public static void main(String[] args) throws org.json.JSONException {
        Requestor requestor = new Requestor(args[0]);
        Engine engine = new Engine();
        Response response = engine.execute(requestor);
        System.out.println(response);
    }
}
