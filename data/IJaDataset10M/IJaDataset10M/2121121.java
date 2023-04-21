package spindles.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class StubServlet extends RemoteServiceServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3273519437822534505L;

    public void init() {
        ServletUtil.init(this.getServletContext());
    }
}
