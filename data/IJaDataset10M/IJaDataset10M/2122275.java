package org.peaseplate.templateengine.parser;

import java.util.Locale;
import org.peaseplate.queryengine.QueryCompilerContext;
import org.peaseplate.templateengine.Template;
import org.peaseplate.utils.resource.ResourceMonitor;

public interface ParserContext extends ParserServiceProvider, QueryCompilerContext {

    public Template getTemplate();

    public Locale getLocale();

    public ResourceMonitor getResourceMonitor();
}
