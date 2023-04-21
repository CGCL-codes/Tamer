package org.jgenesis.beanset;

public class BeanSetEditAction extends AbstractBeanSetAction {

    boolean byFieldChanged;

    public BeanSetEditAction(BeanSet beanSet, boolean byFieldChanged) {
        super(beanSet);
        this.byFieldChanged = byFieldChanged;
    }

    public void execute() {
        this.beanSet.processChangeToEditState(byFieldChanged);
    }
}
