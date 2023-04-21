package org.objectwiz.plugin.uibuilder.runtime.result;

import java.io.IOException;
import java.net.URL;
import org.objectwiz.plugin.uibuilder.EvaluationContext;
import org.objectwiz.utils.Platforms;

/**
 * Result that tells the client to open the given URL.
 *
 * @author Vincent Laugier <vincent.laugier at helmet.fr>
 */
public class OpenURLActionResult extends ActionResult {

    private URL url;

    public OpenURLActionResult(URL url) {
        this.url = url;
    }

    public void openUrl() throws IOException {
        Platforms.instance().openUrl(this.url);
    }

    @Override
    public Object perform(String operationName, EvaluationContext trustedCtx, EvaluationContext parameters) throws Exception {
        throw new UnsupportedOperationException("Not supported.");
    }
}
