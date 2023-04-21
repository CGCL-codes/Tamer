package corner.orm.tapestry.component.prototype;

import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.asset.AssetFactory;
import org.apache.tapestry.markup.MarkupWriterSource;
import org.apache.tapestry.services.RequestLocaleManager;
import org.apache.tapestry.web.WebResponse;

/**
 * 对prototype类型的请求响应.
 * 
 * @author <a href=mailto:xf@bjmaxinfo.com>xiafei</a>
 * @version $Revision: 3919 $
 * @since 2.3.7
 */
public class PrototypeResponseBuilder extends org.apache.tapestry.services.impl.PrototypeResponseBuilder {

    public PrototypeResponseBuilder(IRequestCycle cycle, IMarkupWriter writer, List parts) {
        super(cycle, writer, parts);
    }

    public PrototypeResponseBuilder(IRequestCycle cycle, RequestLocaleManager localeManager, MarkupWriterSource markupWriterSource, WebResponse webResponse, AssetFactory assetFactory, String namespace) {
        super(cycle, localeManager, markupWriterSource, webResponse, assetFactory, namespace);
    }

    /**
	 * {@inheritDoc}
	 */
    public void beginBodyScript(IMarkupWriter writer, IRequestCycle cycle) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void endBodyScript(IMarkupWriter writer, IRequestCycle cycle) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void writeBodyScript(IMarkupWriter writer, String script, IRequestCycle cycle) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void writeExternalScript(IMarkupWriter normalWriter, String url, IRequestCycle cycle) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void writeImageInitializations(IMarkupWriter writer, String script, String preloadName, IRequestCycle cycle) {
    }

    /**
	 * {@inheritDoc}
	 */
    public void writeInitializationScript(IMarkupWriter writer, String script) {
    }

    public void addStatusMessage(IMarkupWriter writer, String category, String text) {
    }
}
