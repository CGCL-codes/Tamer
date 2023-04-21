package org.extwind.osgi.tapestry.components.ext;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.internal.util.IdAllocator;
import org.apache.tapestry5.ioc.internal.util.TapestryException;

/**
 * @author donf.yang
 * 
 */
public class OnReady extends Container {

    @Environmental
    private RenderSupport renderSupport;

    @Parameter
    private boolean initQuickTips;

    @Parameter
    private String msgTarget;

    protected void setupComponentRender() {
        OnReady existing = getEnvironment().peek(OnReady.class);
        if (existing != null) throw new TapestryException(getMessages().get("nesting-not-allowed"), existing, null);
        getEnvironment().push(IdAllocator.class, new IdAllocator());
        getEnvironment().push(OnReady.class, this);
    }

    protected void beginComponentRender(MarkupWriter writer) {
        writer.element("script", "type", "text/javascript");
        writer.write("Ext.onReady(function(){");
        if (initQuickTips) {
            writer.write("Ext.QuickTips.init();");
        }
        if (msgTarget != null) {
            writer.write("Ext.form.Field.prototype.msgTarget = '" + msgTarget + "';");
        }
    }

    protected void afterComponentRender(MarkupWriter writer) {
        writer.write(" });");
        writer.end();
    }

    protected void cleanupRender() {
        getEnvironment().pop(OnReady.class);
        getEnvironment().pop(IdAllocator.class);
    }
}
