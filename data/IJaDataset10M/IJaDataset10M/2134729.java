package org.wikiup.modules.php;

import java.io.IOException;
import javax.servlet.ServletException;
import org.wikiup.core.Wikiup;
import org.wikiup.core.util.Assert;
import org.wikiup.servlet.ServletProcessorContext;
import org.wikiup.servlet.inf.ServletProcessor;

public class PhpServletProcessor implements ServletProcessor {

    public void process(ServletProcessorContext context) {
        try {
            PhpServletContainer phpServletContainer = Wikiup.getModel(PhpServletContainer.class);
            phpServletContainer.getQuercusServlet().service(context.getServletRequest(), context.getServletResponse());
        } catch (ServletException e) {
            Assert.fail(e);
        } catch (IOException e) {
            Assert.fail(e);
        }
    }
}
