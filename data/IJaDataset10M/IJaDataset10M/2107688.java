package vqwiki.plugin.helloworld;

import vqwiki.WikiAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Test action.
 *
 * @author garethc
 * @version $Revision: 1012 $ $Date: 2008-11-10 06:43:02 -0500 (Mon, 10 Nov 2008) $
 * @since 6/06/2004 09:15:10
 */
public class HelloWorldAction implements WikiAction {

    /**
     * Carry out the action resulting from the given request and then pass back
     * the response
     *
     * @param request request
     * @param response response
     */
    public void doAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream()));
        writer.println("Hello world!");
        writer.close();
    }
}
