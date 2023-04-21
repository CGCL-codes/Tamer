package com.dfruits.varpool.ui.databinding;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import com.dfruits.varpool.VarBinding;

public class FilterVarObservables {

    public static IObservableValue observeVar(VarBinding var) {
        return new FilterVarObservableValue(var);
    }
}
