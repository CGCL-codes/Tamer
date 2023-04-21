package com.webmotix.tag.node;

import java.io.IOException;
import java.net.URLEncoder;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.webmotix.core.MotixNodeTypes;
import com.webmotix.tag.TagContext;
import com.webmotix.tag.TagLogger;
import com.webmotix.tag.motix.TagTemplate;
import com.webmotix.util.CharsetUtils;
import com.webmotix.util.MotixURICache;

/**
 * Cria link para as p�ginas de conte�do do site. Todos link deve ser utilziado atrav�s desta tag, 
 * pois permite fazer o mapemamento das urls do site. Constroi o link para arquivos de upload e 
 * conte�dos com o c�digo de identifica��o (UUID) .
 * @author wsouza
 *
 */
public class TagURIFileRecord extends TagSupport {

    private static final long serialVersionUID = 6791813686754192371L;

    /**
	 * Log espec�fico para as Tags, permite imprimir para o desenvolvedor as mensagens de erro. 
	 */
    private final TagLogger logger = new TagLogger();

    private static final Logger log = LoggerFactory.getLogger(TagURIFileRecord.class);

    /**
	 * Nome do grupo do arquivo.
	 */
    protected String property = StringUtils.EMPTY;

    /**
	 * Recupera a propriedade a partir do node declarado identificado
	 * explicitamente (atrav�s do atributo id). Permite recupra��o de um node
	 * desejado em tags aninhadas.
	 */
    protected String reference = StringUtils.EMPTY;

    protected String encode = StringUtils.EMPTY;

    /**
	 * �ndice da propriedade multi-valorada, come�a de zero e vai at� a
	 * quantidade de itens da propriedade.
	 */
    protected String index = "0";

    /**
	 * Paramteros complementares para a QueryString 
	 */
    protected String parameter = StringUtils.EMPTY;

    /**
	 * Parametro indicador se o arquivo ser� direcionado para download ou exibido.
	 */
    protected String download = StringUtils.EMPTY;

    public String getReference() {
        return reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(final String index) {
        this.index = index;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(final String parameter) {
        this.parameter = parameter;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(final String encode) {
        this.encode = encode;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    @Override
    public int doEndTag() throws JspTagException {
        Node father = null;
        try {
            final TagTemplate tagTemplate = (TagTemplate) findAncestorWithClass(this, TagTemplate.class);
            final Session session = tagTemplate.getSession();
            if (StringUtils.isEmpty(this.reference)) {
                final TagContext tag = (TagContext) findAncestorWithClass(this, TagContext.class);
                if (tag == null) {
                    logger.error("A tag node:uri-file n�o est� no contexto de uma tag (loop, node, search, etc.) que recupere conte�do.");
                } else {
                    father = tag.getContext();
                }
            } else {
                father = (Node) this.pageContext.getAttribute(this.reference);
            }
            if (father == null) {
                return SKIP_BODY;
            }
            final StringBuffer uri = new StringBuffer("");
            if (Boolean.valueOf(this.download)) {
                String name = father.getProperty(MotixNodeTypes.NS_PROPERTY + ":" + property).getString();
                uri.append(MotixURICache.getInstance().getFileDownload(father, this.parameter + "&property=" + property + "_data&index=" + this.index + "&name=" + name));
            } else {
                uri.append(MotixURICache.getInstance().getFileURIRecord(father, this.property, this.index, this.parameter));
            }
            if (Boolean.parseBoolean(this.encode)) {
                pageContext.getOut().write(URLEncoder.encode(uri.toString(), CharsetUtils.ISO_8859_1));
            } else {
                pageContext.getOut().write(uri.toString());
            }
        } catch (final IOException e) {
            logger.error("Ocorreu um erro de I/O.", e);
        } catch (final IllegalStateException e) {
            logger.error("Ocorreu um erro de transa��o no reposit�rio de conte�do.", e);
        } catch (final Exception e) {
            logger.error("Ocorreu um erro inesperado na tag node:uri-file.", e);
        } finally {
            logger.printStackTrace(this.pageContext);
            this.reset();
            father = null;
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
        this.property = StringUtils.EMPTY;
        this.index = "0";
        this.reference = StringUtils.EMPTY;
        this.parameter = StringUtils.EMPTY;
        this.encode = StringUtils.EMPTY;
    }
}
