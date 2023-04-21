package org.remus.infomngmnt.common.ui.databinding;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.edit.EMFEditObservables;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class LabelBindingWidget extends AbstractBindingWidget {

    LabelBindingWidget() {
    }

    @Override
    public void bindModel(final EObject object, final EStructuralFeature feature, UpdateValueStrategy target2Model, final UpdateValueStrategy model2target) {
        ISWTObservableValue textObservable = SWTObservables.observeText(getWrappedControl());
        IObservableValue observeValue = EMFEditObservables.observeValue(getEditingDomain(), object, feature);
        if (target2Model == null) {
            target2Model = new EMFUpdateValueStrategy();
        }
        target2Model.setAfterConvertValidator(getTarget2ModelValidators());
        Binding bindValue = getBindingContext().bindValue(textObservable, observeValue, target2Model, model2target);
        setBinding(bindValue);
    }

    @Override
    public boolean isReadonly() {
        return true;
    }
}
