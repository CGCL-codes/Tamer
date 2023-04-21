package freemarker.ide.eclipse.templates;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateProposal;
import org.eclipse.swt.graphics.Image;
import freemarker.ide.eclipse.editors.template.FreemarkerTemplateEditor;

/**
 * Freemarker template proposal.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 */
public class FreemarkerTemplateProposal extends TemplateProposal {

    private FreemarkerTemplateEditor freemarkerTextEditor;

    /**
	 * Creates a template proposal with a template and its context.
	 * 
	 * @param freemarkerTextEditor
	 *            the freemarker text editor
	 * @param template
	 *            the template
	 * @param context
	 *            the context in which the template was requested.
	 * @param region
	 *            the region this proposal is applied to
	 * @param image
	 *            the icon of the proposal.
	 * @param relevance
	 *            the relevance of the proposal
	 */
    public FreemarkerTemplateProposal(FreemarkerTemplateEditor freemarkerTextEditor, Template template, TemplateContext context, IRegion region, Image image, int relevance) {
        super(template, context, region, image, relevance);
        this.freemarkerTextEditor = freemarkerTextEditor;
    }

    /**
	 * Inserts the template offered by this proposal into the viewer's document
	 * and sets up a <code>LinkedModeUI</code> on the viewer to edit any of
	 * the template's unresolved variables.
	 * 
	 * @param viewer
	 *            {@inheritDoc}
	 * @param trigger
	 *            {@inheritDoc}
	 * @param stateMask
	 *            {@inheritDoc}
	 * @param offset
	 *            {@inheritDoc}
	 */
    public void apply(ITextViewer viewer, char trigger, int stateMask, int offset) {
        super.apply(viewer, trigger, stateMask, offset);
    }
}
