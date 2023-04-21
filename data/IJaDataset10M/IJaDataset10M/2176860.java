package com.webmotix.tag.loop;

import java.io.IOException;
import java.util.Calendar;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.webmotix.core.MotixNodeTypes;
import com.webmotix.dao.MetaDataDAO;
import com.webmotix.tag.TagContext;
import com.webmotix.tag.TagLogger;
import com.webmotix.tag.motix.TagTemplate;
import com.webmotix.util.DateUtil;
import com.webmotix.util.JcrUtils;
import com.webmotix.util.QueryUtil;
import com.webmotix.util.TagUtils;

/**
 * Permite a listagem de conte�dos de uma �rea
 * @author wsouza
 *
 */
public class TagLoopReference extends BodyTagSupport implements TagContext, TagLoop {

    private static final long serialVersionUID = -9195006980793735196L;

    /**
	 * Log espec�fico para as Tags, permite imprimir para o desenvolvedor as mensagens de erro.
	 */
    private final TagLogger logger = new TagLogger();

    private static final Logger log = LoggerFactory.getLogger(TagLoopReference.class);

    private String id = "item_reference";

    /**
	 * Nome da propriedade
	 */
    protected String name = StringUtils.EMPTY;

    /**
	 * Nome completo da propriedade (namespace + nome) 
	 */
    protected String property = StringUtils.EMPTY;

    protected String orderBy = StringUtils.EMPTY;

    protected String sort = TagUtils.SORT_ASCENDING;

    protected Node father = null;

    protected Node context = null;

    protected NodeIterator children = null;

    /**
	 * Prefixo da propriedade. O padr�o � mpt (Motix). Outros exemplos: nt e
	 * jcr.
	 */
    protected String nameSpace = MotixNodeTypes.NS_PROPERTY;

    protected int count = 0;

    private int alternate = 0;

    private int alternateCount = 0;

    public boolean isAlternate() {
        if (alternate == alternateCount++) {
            return true;
        } else {
            return false;
        }
    }

    public String getName() {
        return this.name;
    }

    /**
	 * Atribui o nome
	 * @param name
	 */
    public void setName(final String name) {
        this.name = name;
    }

    public TagLoopReference() {
        super();
    }

    public Node getFather() {
        return father;
    }

    public void setFather(final Node father) {
        this.father = father;
    }

    public NodeIterator getChildren() {
        return children;
    }

    public void setChildren(final NodeIterator children) {
        this.children = children;
    }

    public Node getContext() {
        return context;
    }

    public void setContext(final Node node) {
        this.context = node;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(final String sort) {
        this.sort = sort;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(final String nameSpace) {
        this.nameSpace = nameSpace;
    }

    /**
	 * Returna verdadeiro se ainda existe itens para serem exibidos neste loop. 
	 * Este teste � relativo a p�gina que est� sendo exibida.
	 */
    public boolean hasNext() {
        return this.children.hasNext();
    }

    @Override
    public int doStartTag() throws JspTagException {
        try {
            if (StringUtils.isNotEmpty(this.nameSpace)) {
                this.property = this.nameSpace + ":" + this.name;
            } else {
                this.property = MotixNodeTypes.NS_PROPERTY + ":" + this.name;
            }
            this.count = 1;
            final TagTemplate tagTemplate = (TagTemplate) findAncestorWithClass(this, TagTemplate.class);
            final Session session = tagTemplate.getSession();
            final TagContext tag = (TagContext) findAncestorWithClass(this, TagContext.class);
            if (tag == null) {
                logger.error("A tag loop:reference n�o est� no contexto de uma tag (loop, node, search, etc.) que recupere conte�do.");
                return SKIP_BODY;
            } else {
                this.father = tag.getContext();
            }
            if (this.father == null) {
                return SKIP_BODY;
            }
            if (this.father.hasProperty(this.property)) {
                final Property propery = this.father.getProperty(this.property);
                if (propery.getDefinition().isMultiple()) {
                    final Value[] values = propery.getValues();
                    final StringBuffer xpath = new StringBuffer("//element(*,nt:base)[");
                    if (values.length > 0) {
                        xpath.append(" ( ");
                        for (int i = 0; i < values.length; i++) {
                            final Value value = values[i];
                            xpath.append("@jcr:uuid=\'" + value.getString() + "\'");
                            if ((i + 1) < values.length) {
                                xpath.append(" or ");
                            }
                        }
                        xpath.append(" ) and ");
                    } else {
                        return SKIP_BODY;
                    }
                    if (this.father.getPrimaryNodeType().getName().equals(MotixNodeTypes.CONTENT)) {
                        final Calendar calendar = DateUtil.getCurrentUTCCalendar();
                        final String dateActual = org.apache.jackrabbit.util.ISO8601.format(calendar);
                        xpath.append(" (@mpt:status=" + MetaDataDAO.STATUS_PUBLISHED + ") and ");
                        xpath.append(" (not(@mpt:availableStart) or (@mpt:availableStart <= xs:dateTime(\'" + dateActual + "\') and @mpt:availableEnd >= xs:dateTime(\'" + dateActual + "\')) or (@mpt:availableStart <= xs:dateTime(\'" + dateActual + "\')))");
                    } else {
                        xpath.setLength(xpath.length() - 5);
                    }
                    xpath.append("]");
                    if (StringUtils.isNotEmpty(this.orderBy)) {
                        if (!(this.sort.equals(TagUtils.SORT_ASCENDING) || this.sort.equals(TagUtils.SORT_DESCENDING))) {
                            logger.error("N�o � permitido o valor [" + this.sort + "] para a propriedade sort da tag loop:reference.");
                            return SKIP_BODY;
                        }
                        xpath.append(" order by @").append(this.nameSpace).append(":").append(this.orderBy).append(" ").append(this.sort);
                    }
                    this.children = QueryUtil.query(session, xpath.toString(), Query.XPATH);
                    if (this.children != null && this.children.hasNext()) {
                        this.context = this.children.nextNode();
                        pageContext.setAttribute(this.id, this.context, PageContext.PAGE_SCOPE);
                        return EVAL_BODY_INCLUDE;
                    } else {
                        return SKIP_BODY;
                    }
                } else {
                    logger.error("A propriedade " + this.property + " n�o � multi-valorada, apenas � poss�vel utilizar com a tag loop:reference as propriedades que permitam mais de um valor.");
                    return SKIP_BODY;
                }
            } else {
                if (!JcrUtils.hasProperty(this.father, this.property)) {
                    logger.warn("A propriedade " + this.property + " n�o existe, ver tag loop:reference.");
                }
            }
        } catch (final ItemNotFoundException e) {
            logger.error("Nodo n�o encontrado, verifique se o UUID est� correto.", e);
        } catch (final PathNotFoundException e) {
            logger.error("Nodo n�o encontrado, verifique se o UUID est� correto.", e);
        } catch (final RepositoryException e) {
            logger.error("Ocorreu um erro no reposit�rio de conte�do.", e);
        } catch (final Exception e) {
            logger.error("Ocorreu um erro inesperado na tag loop:reference.", e);
        } finally {
        }
        return SKIP_BODY;
    }

    @Override
    public int doAfterBody() throws JspTagException {
        if (this.children.hasNext()) {
            this.context = this.children.nextNode();
            this.count = (int) this.children.getSize();
            if (this.alternateCount > 0) {
                this.alternate++;
                if (this.alternate == this.alternateCount) {
                    this.alternate = 0;
                }
                this.alternateCount = 0;
            }
            pageContext.setAttribute(this.id, this.context, PageContext.PAGE_SCOPE);
            return EVAL_BODY_BUFFERED;
        } else {
            return SKIP_BODY;
        }
    }

    @Override
    public int doEndTag() throws JspTagException {
        try {
            if (bodyContent != null) {
                bodyContent.writeOut(bodyContent.getEnclosingWriter());
            }
        } catch (final IOException e) {
            logger.error("Ocorreu um erro de I/O.", e);
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
        this.id = "item_reference";
        this.orderBy = StringUtils.EMPTY;
        this.name = StringUtils.EMPTY;
        this.property = StringUtils.EMPTY;
        this.nameSpace = MotixNodeTypes.NS_PROPERTY;
        this.sort = TagUtils.SORT_ASCENDING;
        this.father = null;
        this.context = null;
        this.children = null;
        this.count = 0;
        this.alternate = 0;
        this.alternateCount = 0;
    }
}
