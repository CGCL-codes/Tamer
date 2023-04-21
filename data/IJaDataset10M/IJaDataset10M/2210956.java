package progranet.ganesa.metamodel;

import progranet.ganesa.model.ElementImpl;
import progranet.ganesa.model.Ganesa;
import progranet.omg.core.types.NamedElement;

public abstract class ElementViewImpl extends ElementImpl implements ElementView {

    public String getLabel() {
        return (String) this.get(Ganesa.ELEMENT_VIEW_LABEL);
    }

    public String getDescription() {
        return (String) this.get(Ganesa.ELEMENT_VIEW_DESCRIPTION);
    }

    public NamedElement getElement() {
        return (NamedElement) this.get(Ganesa.ELEMENT_VIEW_ELEMENT);
    }

    public ViewImpl getView() {
        return (ViewImpl) this.get(Ganesa.ELEMENT_VIEW_VIEW);
    }

    public String getName() {
        if (this.getElement() == null || this.getView() == null) return this.getLabel() + "[" + this.getMetaClass().getQualifiedName() + "]";
        return this.getLabel() + "[" + this.getElement().getQualifiedName() + "@" + this.getView().getName() + "]";
    }

    public String getQualifiedName() {
        return this.getName();
    }

    public String toString() {
        return this.getQualifiedName();
    }
}
