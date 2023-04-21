package com.mu.jacob.core.renderer;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.mu.jacob.core.generator.GeneratorException;

/**
 * Base renderer class 
 * - manages context and paths
 * - initializes renderer
 * 
 * @author Adam Smyczek
 */
public abstract class AbstractRenderer implements IRenderer {

    private final Map<String, Object> contextMap = new HashMap<String, Object>();

    private File templateRoot = new File(".");

    /**
	 * Initializes renderer before rendering process
	 */
    protected abstract void initEngine();

    /**
	 * Initialize renderer, set template root
	 */
    public void initRenderer(File templateRoot) {
        if (templateRoot == null || !templateRoot.exists()) {
            throw new GeneratorException("Template root path does not exist!");
        }
        this.templateRoot = templateRoot;
        intiDefaultContext();
    }

    /**
	 *  Initialize engine
	 */
    private void intiDefaultContext() {
        addContext(CONTEXT_TOOLS, ContextTools.getInstance());
        addContext(CONTEXT_DATE, dateFormat.format(new Date()));
    }

    /**
	 * Add renderer context
	 * @param name
	 * @param ctx
	 */
    public final IRenderer addContext(final String name, final Object ctx) {
        contextMap.put(name, ctx);
        return this;
    }

    /**
	 * @return the contextMap
	 */
    public Map<String, Object> getContextMap() {
        return Collections.unmodifiableMap(contextMap);
    }

    /**
	 * @return template root
	 */
    protected File getTemplateRoot() {
        return templateRoot;
    }

    private static final DateFormat dateFormat = new SimpleDateFormat();
}
