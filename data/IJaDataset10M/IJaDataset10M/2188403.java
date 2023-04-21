package com.webmotix.tag.loop;

import java.io.IOException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import com.webmotix.tag.TagLogger;

public class TagLoopIsNotFirst extends BodyTagSupport {

    private static final long serialVersionUID = -4975747807612495690L;

    /**
	 * Log espec�fico para as Tags, permite imprimir para o desenvolvedor as
	 * mensagens de erro.
	 */
    private final TagLogger logger = new TagLogger();

    @Override
    public int doStartTag() throws JspTagException {
        try {
            final TagLoop tagLoop = (TagLoop) findAncestorWithClass(this, TagLoop.class);
            if (tagLoop.getCount() != 1) {
                return EVAL_BODY_INCLUDE;
            } else {
                return SKIP_BODY;
            }
        } catch (final Exception e) {
            logger.error("Falha inesperada na tag loop:isNotFirst.", e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspTagException {
        try {
            if (this.bodyContent != null) {
                this.bodyContent.writeOut(this.bodyContent.getEnclosingWriter());
            }
        } catch (final IOException e) {
            logger.error("Ocorreu um erro de I/O.", e);
        } finally {
            logger.printStackTrace(this.pageContext);
        }
        return EVAL_PAGE;
    }
}
