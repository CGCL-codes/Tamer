package org.siberia.trans.type;

import org.siberia.trans.TransSiberia;
import org.siberia.trans.type.plugin.PluginBuild;
import org.siberia.type.AbstractSibType;
import org.siberia.type.SibList;
import org.siberia.type.annotation.bean.Bean;
import org.siberia.type.annotation.bean.BeanProperty;

/**
 *
 * @author alexis
 */
@Bean(name = "TransSiberian context", internationalizationRef = "org.siberia.rc.i18n.type.TransSiberianContext", expert = true, hidden = true, preferred = true, propertiesClassLimit = Object.class, methodsClassLimit = Object.class)
public class TransSiberianContext extends SibList {

    /** property transSiberia */
    public static final String PROPERTY_TRANSSIBERIA = "trans-siberia";

    /** transSiberia */
    @BeanProperty(name = PROPERTY_TRANSSIBERIA, internationalizationRef = "org.siberia.rc.i18n.property.TransSiberianContext_transsiberian", expert = false, hidden = false, preferred = true, bound = true, constrained = true, writeMethodName = "setTransSiberia", writeMethodParametersClass = { TransSiberia.class }, readMethodName = "getTransSiberia", readMethodParametersClass = {  })
    private TransSiberia transSiberian = null;

    /** Creates a new instance of TransSiberianContext */
    public TransSiberianContext() {
        this.setAllowedClass(PluginBuild.class);
    }

    /** return the transSiberia of the context
     *	@return a TransSiberia
     */
    public TransSiberia getTransSiberia() {
        return this.transSiberian;
    }

    /** initialize the transSiberia of the context
     *	@param trans a TransSiberia
     */
    public void setTransSiberia(TransSiberia trans) {
        if (trans != this.getTransSiberia()) {
            TransSiberia old = this.getTransSiberia();
            this.transSiberian = trans;
            this.firePropertyChange(PROPERTY_TRANSSIBERIA, old, this.getTransSiberia());
        }
    }
}
