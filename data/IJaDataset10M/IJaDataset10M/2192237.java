package com.webmotix.tag.loop;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.webmotix.tag.TagLogger;
import com.webmotix.util.FilterParameterItem;

public class TagLoopFilter extends TagSupport {

    private static final Logger log = LoggerFactory.getLogger(TagLoopFilter.class);

    private static final long serialVersionUID = -8208719591950920350L;

    /**
	 * Log espec�fico para as Tags, permite imprimir para o desenvolvedor as mensagens de erro.
	 */
    private final TagLogger logger = new TagLogger();

    private String id = StringUtils.EMPTY;

    private Collection<FilterParameterItem> parameters = null;

    public void addParameter(FilterParameterItem parameter) {
        this.parameters.add(parameter);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int doStartTag() throws JspTagException {
        parameters = new ArrayList<FilterParameterItem>(2);
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspTagException {
        try {
            Collection<FilterParameterItem> values = new ArrayList<FilterParameterItem>(this.parameters);
            this.pageContext.setAttribute("filter." + this.id, values, PageContext.PAGE_SCOPE);
        } catch (final Exception e) {
            logger.error("Ocorreu um erro de I/O.", e);
        } finally {
            logger.printStackTrace(this.pageContext);
            this.reset();
        }
        return EVAL_BODY_INCLUDE;
    }

    /**
	 * Libera recursos.
	 */
    @Override
    public void release() {
        super.release();
        this.reset();
    }

    /**
	 * Limpa as vari�veis.
	 */
    private void reset() {
        this.id = StringUtils.EMPTY;
        parameters = null;
    }
}
