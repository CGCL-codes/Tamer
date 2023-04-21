package org.jmetis.template.compiler;

import org.jmetis.template.context.ITemplateContext;
import org.jmetis.template.model.ITemplateNode;
import org.jmetis.template.model.TemplateNodeDecorator;

/**
 * Default {@link ITemplateNode} implementation.
 * 
 * @author aerlach
 */
class CachedTemplate extends TemplateNodeDecorator {

    private ITemplateCompiler templateCompiler;

    private String templateName;

    private ITemplateContext templateContext;

    private ITemplateNode templateNode;

    private long creationTime;

    /**
	 * Constructs a new {@code CachedTemplate} instance.
	 * 
	 * @param templateCompiler
	 * @param templateURL
	 */
    public CachedTemplate(ITemplateCompiler templateCompiler, String templateName, ITemplateContext templateContext) {
        super();
        this.templateCompiler = templateCompiler;
        this.templateName = templateName;
        this.templateContext = templateContext;
    }

    @Override
    protected ITemplateNode getTemplateNode() {
        if (this.templateNode == null) {
            this.creationTime = System.currentTimeMillis();
            this.templateNode = this.templateCompiler.compileTemplateIn(this.templateName, this.templateContext);
        }
        return this.templateNode;
    }

    /**
	 * Returns the time in milliseconds when the receiver was created.
	 * 
	 * @return the time in milliseconds when the receiver was created
	 */
    public long getCreationTime() {
        return this.creationTime;
    }
}
