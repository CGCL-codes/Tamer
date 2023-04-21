package org.akrogen.tkui.core.ui.bindings;

import org.eclipse.core.databinding.Binding;
import org.ufacekit.ui.databinding.events.observable.IObservableEvent;

public interface IEventBindable {

    public Binding bindEvent(int eventType, IObservableEvent targetObservableEvent);

    public boolean canSupportEvent(int eventType);
}
