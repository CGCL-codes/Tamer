package net.sf.tacos.formatter;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IScriptSource;

/**
 * Adds formatting and mask client-side capabilities to a component.
 * @author lquijano
 */
public interface JsFormatter {

    /**
     * Renders the formatter contributions (usually JavaScript functions).
     * @param cycle Tapestry request cycle.
     * @param scriptSource Tapestry script source.
     * @param comp the component.
     * @param uniqueId optional unique Id to override the component's id.
     */
    void renderContributions(IRequestCycle cycle, IScriptSource scriptSource, IComponent comp, String uniqueId);
}
