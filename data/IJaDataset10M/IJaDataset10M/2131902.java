package net.sf.tacos.link;

import net.sf.tacos.TacosUtil;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.ILinkComponent;
import org.apache.tapestry.link.DefaultLinkRenderer;

/**
 * @author phraktle
 */
public class FixPosLinkRenderer extends DefaultLinkRenderer {

    public static final FixPosLinkRenderer INSTANCE = new FixPosLinkRenderer();

    private static final String ATTRIBUTE = FixPosLinkRenderer.class.getName();

    public void renderLink(IMarkupWriter writer, IRequestCycle cycle, ILinkComponent linkComponent) {
        TacosUtil.renderScript(cycle, "/net/sf/tacos/link/FixPos.script", ATTRIBUTE);
        super.renderLink(writer, cycle, linkComponent);
    }

    protected void beforeBodyRender(IMarkupWriter writer, IRequestCycle cycle, ILinkComponent link) {
        writer.attribute("onclick", "return fixPos(this)");
    }
}
