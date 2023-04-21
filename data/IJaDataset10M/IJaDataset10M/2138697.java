package com.nordija.tapestry.bayeux.downloads;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * Renders a link to the downloads service.
 * <p/>
 * The link will include an id for the resource to be downloaded.
 *
 * @see DownloadService
 * @author Per Olesen www.nordija.com
 * @version $Id: DownloadLink.java,v 1.1 2004/10/05 11:52:34 perolesen Exp $
 */
public abstract class DownloadLink extends AbstractComponent {

    public abstract String getDownloadResourceId();

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        if (cycle.isRewinding()) {
            return;
        }
        IEngineService service = cycle.getEngine().getService(DownloadService.SERVICE_NAME);
        ILink link = service.getLink(cycle, this, new Object[] { getDownloadResourceId() });
        writer.begin("a");
        writer.attribute("href", link.getURL());
        renderInformalParameters(writer, cycle);
        renderBody(writer, cycle);
        writer.end();
    }
}
