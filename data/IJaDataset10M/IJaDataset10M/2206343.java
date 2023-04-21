package com.webmotix.tag.node;

import java.io.IOException;
import javax.jcr.Session;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.lang.StringUtils;
import com.webmotix.core.PathCache;
import com.webmotix.dao.StaticPageDAO;
import com.webmotix.tag.TagLogger;
import com.webmotix.tag.motix.TagTemplate;
import com.webmotix.util.MotixURICache;

/**
 * Recupera o link de uma configura��o de ger�o de p�gina est�tica..
 * @author wsouza
 *
 */
public class TagHrefCache extends TagSupport {

    private static final long serialVersionUID = 6791813686754192371L;

    /**
	 * Log espec�fico para as Tags, permite imprimir para o desenvolvedor as mensagens de erro.
	 */
    private final TagLogger logger = new TagLogger();

    /**
	 * Recupera a propriedade a partir do node declarado identificado
	 * explicitamente (atrav�s do atributo id). Permite recupra��o de um node
	 * desejado em tags aninhadas.
	 */
    protected String url = StringUtils.EMPTY;

    protected String locale = StringUtils.EMPTY;

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    @Override
    public int doEndTag() throws JspTagException {
        try {
            final TagTemplate tagTemplate = (TagTemplate) findAncestorWithClass(this, TagTemplate.class);
            final Session session = tagTemplate.getSession();
            final StaticPageDAO staticPageDAO = StaticPageDAO.findByURLCached(this.url, session.getWorkspace().getName());
            if (staticPageDAO == null) {
                pageContext.getOut().write(MotixURICache.URI_INVALID);
            } else {
                if (StringUtils.isEmpty(this.locale)) {
                    pageContext.getOut().write("/" + PathCache.ROOT_CACHE + "/" + tagTemplate.getLanguageDAO().getLocale().toString().toLowerCase() + "/" + staticPageDAO.getUrl());
                } else {
                    pageContext.getOut().write("/" + PathCache.ROOT_CACHE + "/" + this.locale.toLowerCase() + "/" + staticPageDAO.getUrl());
                }
            }
        } catch (final IOException e) {
            logger.error("Ocorreu um erro de I/O.", e);
        } catch (final IllegalStateException e) {
            logger.error("Ocorreu um erro de transa��o no reposit�rio de conte�do.", e);
        } catch (final Exception e) {
            logger.error("Ocorreu um erro inesperado na tag node:href-cache.", e);
        } finally {
            logger.printStackTrace(this.pageContext);
            this.reset();
        }
        return EVAL_PAGE;
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
        this.url = StringUtils.EMPTY;
        this.locale = StringUtils.EMPTY;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
