package br.com.linkcom.neo4.view;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import javax.persistence.Id;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.springframework.core.Conventions;
import org.springframework.web.servlet.tags.form.TagWriter;
import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.controller.crud.FiltroListagem;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.util.Util;
import br.com.linkcom.neo4.util.ReflectionUtil;

public class ListagemTag extends NeoFormTag {

    private static final long serialVersionUID = -2411861570844826224L;

    private static final String TAG_TABLE = "table";

    private static final String TAG_TBODY = "tbody";

    private static final String TAG_THEAD = "thead";

    private static final String TAG_TR = "tr";

    private static final String TAG_TD = "td";

    private static final String TAG_SPAN = "span";

    public static final String ATRIBUTO_PAGINA_ATUAL = "currentPage";

    public static final String ATRIBUTO_NUM_PAGINAS = "numberOfPages";

    /**
	 * Atributo usado para armazenar o Bean da linha que est� sendo renderizada.
	 */
    public static final String ATRIBUTO_ITEM_ATUAL = Conventions.getQualifiedAttributeName(ListagemTag.class, "bean");

    private static final String CONFIRMATION = "Deseja realmente excluir esse registro?";

    private Object itens;

    private Iterator<?> iterator;

    private Boolean showExcluir = true;

    private Boolean showEditar = true;

    private TagWriter tagWriter;

    private Stack<String> tagAberta = new Stack<String>();

    private PropertyDescriptor idPropert;

    private String var = "item";

    private FiltroListagem filtroListagem;

    private boolean useDialog = false;

    @Override
    @SuppressWarnings("unchecked")
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        if (itens == null) {
            itens = pageContext.findAttribute(CrudController.ATRIBUTO_LISTA_ITENS);
        }
        filtroListagem = (FiltroListagem) pageContext.findAttribute(CrudController.ATRIBUTO_BEAN);
        this.tagWriter = tagWriter;
        super.writeTagContent(tagWriter);
        tagWriter.startTag(TAG_TABLE);
        tagWriter.writeAttribute("id", getTableId());
        tagWriter.writeAttribute("width", "100%");
        tagWriter.forceBlock();
        tagAberta.push(TAG_TABLE);
        if (itens != null && iterator == null) {
            if (itens.getClass().isArray()) {
                iterator = Arrays.asList(itens).iterator();
            } else if (itens instanceof Collection) {
                iterator = ((Collection) itens).iterator();
            } else if (itens instanceof Iterator) {
                iterator = (Iterator) itens;
            } else if (itens instanceof Map) {
                iterator = ((Map) itens).keySet().iterator();
            } else if (itens instanceof Class && itens.getClass().isEnum()) {
                iterator = Arrays.asList(((Class) itens).getEnumConstants()).iterator();
            } else {
                throw new JspException("O valor informado para o atributo 'itens' n�o � uma cole��o v�lida.");
            }
        }
        tagWriter.startTag(TAG_THEAD);
        tagWriter.forceBlock();
        tagAberta.push(TAG_THEAD);
        tagWriter.startTag(TAG_TR);
        tagWriter.forceBlock();
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doAfterBody() throws JspException {
        if (TAG_THEAD.equals(tagAberta.peek())) {
            if (showEditar || showExcluir) {
                tagWriter.startTag("th");
                tagWriter.forceBlock();
                tagWriter.appendValue("A��es");
                tagWriter.endTag();
            }
            tagWriter.endTag();
            tagWriter.endTag();
            tagAberta.pop();
            tagAberta.pop();
            tagWriter.startTag(TAG_TBODY);
            tagWriter.forceBlock();
            tagAberta.push(TAG_TBODY);
        } else if (TAG_TR.equals(tagAberta.peek())) {
            if (showEditar || showExcluir) {
                tagWriter.startTag(TAG_TD);
                tagWriter.forceBlock();
                if (showEditar) {
                    TagUtil.writeActionLink(tagWriter, CrudController.LER, "Editar", getIdParameter(), null, useDialog);
                }
                if (showExcluir) {
                    tagWriter.appendValue("&nbsp;");
                    TagUtil.writeActionLink(tagWriter, CrudController.EXCLUIR, "Excluir", getIdParameter(), CONFIRMATION, false);
                }
                tagWriter.endTag();
            }
            tagWriter.endTag();
            tagAberta.pop();
        }
        if (hasMoreElements()) {
            return EVAL_BODY_AGAIN;
        } else return SKIP_BODY;
    }

    /**
	 * Monta o par�metro para a propriedade com <code>@Id</code>.
	 * 
	 * @return
	 * @throws JspException
	 */
    private String getIdParameter() throws JspException {
        Object item = this.pageContext.findAttribute(ATRIBUTO_ITEM_ATUAL);
        if (idPropert == null) idPropert = ReflectionUtil.getPropertyWithAnnotation(item.getClass(), Id.class);
        try {
            return idPropert.getName() + "=" + idPropert.getReadMethod().invoke(item);
        } catch (IllegalArgumentException e) {
            throw new JspException("N�o foi poss�vel ler a propriedade identificadora.", e);
        } catch (IllegalAccessException e) {
            throw new JspException("N�o foi poss�vel ler a propriedade identificadora.", e);
        } catch (InvocationTargetException e) {
            throw new JspException("N�o foi poss�vel ler a propriedade identificadora.", e);
        }
    }

    /**
	 * Gera a linha para o objeto atual, se existir um objeto.
	 * 
	 * @return <code>true</code> se a linha foi gerada, <code>false</code> caso
	 *         contr�rio.
	 * @throws JspException
	 */
    private boolean hasMoreElements() throws JspException {
        if (iterator != null && iterator.hasNext()) {
            this.tagWriter.startTag(TAG_TR);
            this.tagAberta.push(TAG_TR);
            this.tagWriter.forceBlock();
            Object item = iterator.next();
            this.pageContext.setAttribute(ATRIBUTO_ITEM_ATUAL, item, PageContext.PAGE_SCOPE);
            if (!Util.strings.isEmpty(var)) {
                this.pageContext.setAttribute(var, item, PageContext.PAGE_SCOPE);
            }
            return true;
        }
        return false;
    }

    @Override
    public int doEndTag() throws JspException {
        this.pageContext.removeAttribute(CrudController.ATRIBUTO_BEAN, PageContext.PAGE_SCOPE);
        tagWriter.endTag();
        tagWriter.endTag();
        super.doEndTag();
        String urlListagem = NeoWeb.getRequestContext().getServletRequest().getContextPath() + NeoWeb.getRequestContext().getRequestQuery() + "?ACAO=" + CrudController.LISTAGEM;
        StringBuilder inicializacao = new StringBuilder();
        inicializacao.append("$(document).ready(function() {\n");
        inicializacao.append("$('#").append(getTableId()).append("').ingrid({\n");
        inicializacao.append("  url: '").append(urlListagem).append("',\n");
        inicializacao.append("  initialLoad: false,\n");
        inicializacao.append("  rowClasses: ['grid-row-style1','grid-row-style2'],\n");
        inicializacao.append("  savedStateLoad: true,\n");
        inicializacao.append("  sorting: true,\n");
        inicializacao.append("  paging: true,\n");
        inicializacao.append("  sortType: 'server',\n");
        inicializacao.append("  pageNumber: ").append(this.filtroListagem.getCurrentPage()).append(",\n");
        inicializacao.append("  recordsPerPage: ").append(this.filtroListagem.getPageSize()).append(",\n");
        inicializacao.append("  totalRecords: ").append(this.filtroListagem.getNumberOfResults()).append(" ,\n");
        inicializacao.append("  extraParams: {\n");
        inicializacao.append("    notFirstTime : true\n    ");
        inicializacao.append("  }, \n");
        inicializacao.append("  onPageChange: function(table){\n");
        inicializacao.append("  } \n");
        inicializacao.append("}); \n");
        inicializacao.append("}); \n");
        tagWriter.startTag("script");
        tagWriter.writeOptionalAttributeValue("type", "text/javascript");
        tagWriter.forceBlock();
        tagWriter.appendValue(inicializacao.toString());
        tagWriter.endTag();
        return EVAL_PAGE;
    }

    private String getTableId() {
        return getName() + "TableId";
    }

    /**
	 * Cria a barra de pagina��o.
	 * 
	 * @throws JspException
	 */
    private void criarPaginacao() throws JspException {
        int currentPage = (Integer) pageContext.findAttribute(ATRIBUTO_PAGINA_ATUAL);
        int totalNumberOfPages = (Integer) pageContext.findAttribute(ATRIBUTO_NUM_PAGINAS);
        tagWriter.startTag(TAG_SPAN);
        int start = Math.max(1, currentPage + 1 - 4) - 1;
        boolean start3pontos = start != 0;
        int fim = Math.min(9 - (currentPage - start) + currentPage, totalNumberOfPages);
        boolean fim3pontos = fim < totalNumberOfPages;
        if (start3pontos) tagWriter.appendValue("...&nbsp;");
        for (int i = start; i < fim; i++) {
            if (i == currentPage) {
                tagWriter.startTag(TAG_SPAN);
                tagWriter.appendValue(String.valueOf(i + 1));
                tagWriter.endTag();
            } else {
                String url = getRequest().getContextPath() + NeoWeb.getRequestContext().getRequestQuery() + "?currentPage=" + i;
                tagWriter.appendValue("&nbsp;");
                TagUtil.writeLink(tagWriter, url, String.valueOf(i + 1));
            }
        }
        if (fim3pontos) tagWriter.appendValue("...&nbsp;");
        tagWriter.endTag();
    }

    @Override
    public void doFinally() {
        super.doFinally();
        this.tagWriter = null;
        this.itens = null;
        this.iterator = null;
        this.idPropert = null;
        this.filtroListagem = null;
    }

    public Boolean getShowExcluir() {
        return showExcluir;
    }

    public void setShowExcluir(Boolean showExcluir) {
        this.showExcluir = showExcluir;
    }

    public Boolean getShowEditar() {
        return showEditar;
    }

    public void setShowEditar(Boolean showEditar) {
        this.showEditar = showEditar;
    }

    public Object getItens() {
        return itens;
    }

    public void setItens(Object itens) {
        this.itens = itens;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public boolean isUseDialog() {
        return useDialog;
    }

    public void setUseDialog(boolean useDialog) {
        this.useDialog = useDialog;
    }
}
