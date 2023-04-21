package engine.mvc;

import engine.annotations.Page;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author 
 */
public abstract class CommandStrategyBase {

    protected RequestMaster master = null;

    protected String getURL() {
        Page p = this.getClass().getAnnotation(Page.class);
        return p.url();
    }

    protected String[] getCommand() {
        Page p = this.getClass().getAnnotation(Page.class);
        return p.commands();
    }

    public static String[] command(Class c) {
        Page p = (Page) c.getAnnotation(Page.class);
        return p.commands();
    }

    public void process() throws IOException, ServletException, GoToWelcomeException {
    }

    public void forward() throws IOException, ServletException {
        dispatch();
    }

    public void prepare(RequestMaster master) {
        this.master = master;
    }

    protected void dispatch() throws IOException, ServletException {
        master.dispatch(getURL());
    }

    protected RequestMaster getMaster() {
        return master;
    }
}
