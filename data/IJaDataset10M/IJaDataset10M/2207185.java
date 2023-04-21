package com.doculibre.intelligid.wicket.components.form.lookup;

import wicket.Component;
import wicket.markup.html.form.TextField;
import wicket.model.IModel;
import wicket.model.Model;
import wicket.model.PropertyModel;
import wicket.util.lang.PropertyResolver;
import com.doculibre.util.PropertyPatternUtils;

/**
 * Classe de modèle facilitant la transition entre un objet sélectionné
 * via la page de lookup et le champ hidden lié à cet objet. 
 * 
 * @author Vincent Dussault
 */
@SuppressWarnings("serial")
public abstract class LookupPopupModel extends Model {

    private IModel valuePropertyModel;

    private String idPropertyName;

    private String labelPropertyPattern;

    /**
	 * Cas simple ou la valeur récupérée dans la fenêtre de lookup est échangée
	 * avec un bean via un getter/setter.
	 * 
	 * Le premier paramètre, bean, est l'objet sur lequel appeler le getter pour 
	 * peupler le champ hidden et appeler le setter pour transmettre la valeur 
	 * sélectionner dans le lookup.
	 * 
	 * @param bean 
	 * @param valuePropertyName
	 * @param idPropertyName
	 */
    public LookupPopupModel(Object bean, String valuePropertyName, String idPropertyName, String labelPropertyPattern) {
        this(new PropertyModel(bean, valuePropertyName), idPropertyName, labelPropertyPattern);
    }

    /**
	 * Le permier paramètre, valuePropertyModel, est le modèle en charge de recevoir et 
	 * transmettre la valeur sélectionnée dans la fenêtre de lookup.
	 * 
	 * Le second paramètre, idPropertyName, correspond au nom de la méthode getter à appeler
	 * sur l'objet sélectionné dans la fenêtre de lookup pour peupler le champ hidden.
	 * 
	 * @param valuePropertyModel
	 * @param idPropertyName
	 */
    public LookupPopupModel(IModel valuePropertyModel, String idPropertyName, String labelPropertyPattern) {
        this.valuePropertyModel = valuePropertyModel;
        this.idPropertyName = idPropertyName;
        this.labelPropertyPattern = labelPropertyPattern;
    }

    @Override
    public IModel getNestedModel() {
        return valuePropertyModel;
    }

    /**
	 * Méthode appelée par le champ hidden pour obtenir l'identifiant de l'objet 
	 * sélectionné.
	 * 
	 * @see wicket.model.Model#getObject(wicket.Component)
	 */
    public final Object getObject(Component component) {
        String labelOrValue;
        Object selectedObject = valuePropertyModel.getObject(component);
        if (component instanceof TextField) {
            if (selectedObject != null) {
                labelOrValue = getSelectedLabel(selectedObject);
            } else {
                labelOrValue = null;
            }
        } else {
            if (selectedObject != null) {
                labelOrValue = PropertyResolver.getValue(idPropertyName, selectedObject).toString();
            } else {
                labelOrValue = null;
            }
        }
        return labelOrValue;
    }

    /**
	 * Méthode appelée par le champ hidden pour setter la valeur correspondant
	 * à l'id (paramètre object).
	 * 
	 * @see wicket.model.Model#setObject(wicket.Component, java.lang.Object)
	 */
    public final void setObject(Component component, Object object) {
        String selectedId = (String) object;
        Object selectedObject = getSelectedObject(selectedId);
        valuePropertyModel.setObject(component, selectedObject);
    }

    /**
	 * Méthode en charge de récupérer le libellé correspondant à l'objet
	 * sélectionné.
	 * 
	 * @param selectedObject
	 * @return
	 */
    protected String getSelectedLabel(Object selectedObject) {
        String label;
        if (selectedObject != null) {
            label = PropertyPatternUtils.appliquerPattern(labelPropertyPattern, selectedObject);
        } else {
            label = null;
        }
        return label;
    }

    /**
	 * Méthode en charge de récupérer l'objet correspondant à l'identifiant
	 * sélectionné.
	 * 
	 * @param selectedId
	 * @return
	 */
    protected abstract Object getSelectedObject(String selectedId);
}
