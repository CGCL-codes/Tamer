package restful.struts2.sample.echo;

import javax.ws.rs.*;
import org.apache.struts2.config.*;

@Path("/echo")
@Results(@Result(name = "success", value = "/echo.jsp"))
public class EchoAction {

    private String message;

    public EchoAction() {
    }

    public String getHello() {
        return "hello";
    }

    @POST
    public String create() {
        return "success";
    }

    @GET
    @Path("{message:.*}")
    public String echo() {
        return "success";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
