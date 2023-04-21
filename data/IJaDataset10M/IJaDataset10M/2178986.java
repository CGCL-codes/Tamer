package org.jcompany.view.jsf.tag;

import javax.el.ValueExpression;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.component.core.output.CoreOutputLabel;
import org.apache.myfaces.trinidadinternal.taglib.core.output.CoreOutputLabelTag;
import org.jcompany.commons.PlcConstantsCommons;
import org.jcompany.commons.aop.PlcAopProfilingHelper;
import org.jcompany.view.jsf.component.PlcTitle;
import org.jcompany.view.jsf.helper.PlcComponentHelper;
import org.jcompany.view.jsf.helper.PlcTagHelper;
import org.jcompany.view.jsf.renderer.PlcTitleRenderer;

/**
 * Especializa��o da tag base CoreOutputLabelTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza o texto referente ao t�tulo na tela.!
 * @Exemplo <plcf:titulo tituloChave="label.descricao"/>!
 * @Tag titulo!
 */
public class PlcTitleTag extends CoreOutputLabelTag {

    protected static final Logger logVisao = Logger.getLogger(PlcConstantsCommons.LOGGERs.JCOMPANY_VIEW);

    private ValueExpression title;

    private String columns;

    private String classCSS;

    private String titleKey;

    protected String bundle;

    private String alias;

    private String propOrdination;

    private String order;

    /**
	 * Recupera qual � o componente associado a esta tag
	 */
    @Override
    public String getComponentType() {
        return PlcTitle.COMPONENT_TYPE;
    }

    /**
	 *  Recupera qual � o renderer associado a esta tag
	 */
    @Override
    public String getRendererType() {
        return PlcTitleRenderer.RENDERER_TYPE;
    }

    /**
	 *  Registrando valores para propriedades espec�ficas da tag
	 */
    @Override
    protected void setProperties(FacesBean bean) {
        if (logVisao.isDebugEnabled()) logVisao.debug(PlcAopProfilingHelper.getInstance().showLogInitial("(Trinidad)" + this.getClass().getSimpleName() + ":setProperties"));
        super.setProperties(bean);
        bean.setProperty(bean.getType().findKey(PlcTagHelper.BUNDLE), PlcTagHelper.getBundleDefault(bundle));
        if (StringUtils.isNotBlank(this.titleKey)) {
            bean.setProperty(PlcTitle.TITULO_CHAVE_KEY, this.titleKey);
        }
        if (!PlcComponentHelper.getInstance().isValorDefinido(bean, UIXValue.VALUE_KEY)) {
            if (title != null) {
                setProperty(bean, UIXValue.VALUE_KEY, title);
            } else {
                if (!StringUtils.isBlank(this.titleKey)) {
                    String valorChave = PlcComponentHelper.getInstance().montaMensagemLocalizada(bean, this.titleKey);
                    bean.setProperty(UIXValue.VALUE_KEY, valorChave);
                    if (valorChave == null || (valorChave.startsWith("???") && this.titleKey.startsWith("jcompany"))) {
                        String bundleDefault = (String) bean.getProperty(bean.getType().findKey(PlcTagHelper.BUNDLE));
                        bean.setProperty(bean.getType().findKey(PlcTagHelper.BUNDLE), "jCompanyResources");
                        valorChave = PlcComponentHelper.getInstance().montaMensagemLocalizada(bean, this.titleKey);
                        bean.setProperty(bean.getType().findKey(PlcTagHelper.BUNDLE), bundleDefault);
                    }
                    bean.setProperty(UIXValue.VALUE_KEY, valorChave);
                }
            }
        }
        String propriedade = PlcComponentHelper.getInstance().getPropriedadeDeValue(bean);
        if (StringUtils.isNotBlank(propriedade)) {
            bean.setProperty(PlcTitle.PROPRIEDADE_KEY, propriedade);
        }
        if (!StringUtils.isBlank(this.columns)) bean.setProperty(CoreInputText.COLUMNS_KEY, Integer.valueOf(this.columns));
        if (!StringUtils.isBlank(this.classCSS)) bean.setProperty(CoreOutputLabel.STYLE_CLASS_KEY, this.classCSS);
        if (!StringUtils.isBlank(this.propOrdination)) bean.setProperty(PlcTitle.PROP_ORDENACAO, this.propOrdination);
        if (!StringUtils.isBlank(this.alias)) bean.setProperty(PlcTitle.ALIAS, this.alias);
        if (!StringUtils.isBlank(this.order)) bean.setProperty(PlcTitle.ORDEM, this.order);
        if (logVisao.isDebugEnabled()) logVisao.debug(PlcAopProfilingHelper.getInstance().showLogFinal("(Trinidad)" + this.getClass().getSimpleName() + ":setProperties"));
    }

    public ValueExpression getTitle() {
        return title;
    }

    public void setTitle(ValueExpression title) {
        this.title = title;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getClassCSS() {
        return classCSS;
    }

    public void setClassCSS(String classCSS) {
        this.classCSS = classCSS;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPropOrdination() {
        return propOrdination;
    }

    public void setPropOrdination(String propOrdination) {
        this.propOrdination = propOrdination;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
