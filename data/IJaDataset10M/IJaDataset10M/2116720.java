package radeox.filter.context;

import radeox.engine.context.RenderContext;
import radeox.macro.parameter.MacroParameter;

/**
 * FilterContext stores basic data for the context
 * filters are called in. FilterContext is used to allow
 * filters be called in different enviroments, inside
 * SnipSnap or outside. Special enviroments should
 * implement FilterContext
 *
 * @author Stephan J. Schmidt
 * @version $Id: FilterContext.java,v 1.7 2003/10/07 08:20:24 stephan Exp $
 */
public interface FilterContext {

    public MacroParameter getMacroParameter();

    public void setRenderContext(RenderContext context);

    public RenderContext getRenderContext();
}
