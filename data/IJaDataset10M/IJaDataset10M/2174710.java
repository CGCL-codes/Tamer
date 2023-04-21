package br.com.linkcom.neo.view.template;

import br.com.linkcom.neo.controller.crud.CrudController;
import br.com.linkcom.neo.core.web.NeoWeb;
import br.com.linkcom.neo.util.Util;

/**
 * @author rogelgarcia
 * @since 03/02/2006
 * @version 1.1
 */
public class ResultadoTag extends TemplateTag {

    private static final String URL_LISTAGEM = "urlListagem";

    private static final String URL_EDICAO = "URL_EDICAO";

    private static final String ATTR_TAG = "TtabelaResultados";

    private boolean showEditarLink = Util.config.getDefaultConfigBoolean(true, "showEditarLink");

    private boolean showExcluirLink = Util.config.getDefaultConfigBoolean(true, "showExcluirLink");

    private boolean showConsultarLink = Util.config.getDefaultConfigBoolean(false, "showConsultarLink");

    private boolean openInDialog = Util.config.getDefaultConfigBoolean(true, "openInDialog");

    ;

    private Object itens;

    private String name;

    private Class<?> valueType;

    public Class<?> getValueType() {
        return valueType;
    }

    public void setValueType(Class<?> valueType) {
        this.valueType = valueType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getItens() {
        return itens;
    }

    public void setItens(Object itens) {
        this.itens = itens;
    }

    public boolean isShowEditarLink() {
        return showEditarLink;
    }

    public void setShowEditarLink(boolean showEditarLink) {
        this.showEditarLink = showEditarLink;
    }

    public boolean isShowExcluirLink() {
        return showExcluirLink;
    }

    public void setShowExcluirLink(boolean showExcluirLink) {
        this.showExcluirLink = showExcluirLink;
    }

    @Override
    protected void doComponent() throws Exception {
        if (Util.strings.isEmpty(this.getId())) this.setId(generateUniqueId());
        String urlListagem = NeoWeb.getRequestContext().getServletRequest().getContextPath() + NeoWeb.getRequestContext().getRequestQuery() + "?ACAO=" + CrudController.LISTAGEM;
        if (openInDialog) {
            String fullUrl = Util.web.getFirstFullUrl();
            String separator = fullUrl.contains("?") ? "&" : "?";
            fullUrl += separator + CrudController.ACTION_PARAMETER + "=ler";
            pushAttribute(URL_EDICAO, fullUrl);
        }
        autowireValues();
        pushAttribute(ATTR_TAG, this);
        pushAttribute(URL_LISTAGEM, urlListagem);
        includeJspTemplate();
        popAttribute(URL_LISTAGEM);
        popAttribute(ATTR_TAG);
        if (openInDialog) popAttribute(URL_EDICAO);
    }

    private void autowireValues() {
        if (itens == null) {
            itens = getRequest().getAttribute(CrudController.ATRIBUTO_LISTA_ITENS);
        }
        if (valueType == null) {
            valueType = (Class<?>) getRequest().getAttribute(CrudController.ATRIBUTO_TIPO_ITEM);
        }
        if (Util.strings.isEmpty(name) && Util.objects.isNotEmpty(valueType)) {
            name = Util.strings.uncaptalize(valueType.getSimpleName());
        }
    }

    public boolean isShowConsultarLink() {
        return showConsultarLink;
    }

    public void setShowConsultarLink(boolean showConsultarLink) {
        this.showConsultarLink = showConsultarLink;
    }

    public boolean isOpenInDialog() {
        return openInDialog;
    }

    public void setOpenInDialog(boolean openInDialog) {
        this.openInDialog = openInDialog;
    }
}
