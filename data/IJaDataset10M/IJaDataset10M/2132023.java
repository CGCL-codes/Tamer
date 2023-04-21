package nu.esox.gui.aspect;

import java.util.*;
import java.lang.reflect.*;
import nu.esox.util.*;
import javax.swing.*;

public abstract class AbstractAdapter implements ObservableListener, ModelOwnerIF.Listener {

    private final ModelOwnerIF m_modelOwner;

    private final AspectIF m_aspect;

    private final String m_aspectName;

    private final Object m_nullValue;

    private final Object m_undefinedValue;

    public AbstractAdapter(ModelOwnerIF modelOwner, Class modelClass, String getAspectMethodName, String setAspectMethodName, Class aspectClass, String aspectName, Object nullValue, Object undefinedValue) {
        this(modelOwner, new Aspect(modelClass, getAspectMethodName, setAspectMethodName, aspectClass), aspectName, nullValue, undefinedValue);
    }

    public AbstractAdapter(ModelOwnerIF modelOwner, AspectIF aspect, String aspectName, Object nullValue, Object undefinedValue) {
        m_modelOwner = modelOwner;
        m_aspect = aspect;
        m_aspectName = aspectName;
        m_nullValue = (nullValue == null) ? EQUALS_NOTHING : nullValue;
        m_undefinedValue = undefinedValue;
        if (m_modelOwner.getModel() != null) m_modelOwner.getModel().addObservableListener(this);
        m_modelOwner.addListener(this);
    }

    public final void modelAssigned(ObservableIF oldModel, ObservableIF newModel) {
        if (oldModel != null) oldModel.removeObservableListener(this);
        if (newModel != null) newModel.addObservableListener(this);
        valueChanged(null);
    }

    public final void valueChanged(ObservableEvent ev) {
        if ((ev instanceof ObservableTransactionEvent) && (m_aspectName != null)) {
            ((ObservableTransactionEvent) ev).dispatch(this);
        } else {
            if ((m_aspectName == null) || (ev == null) || (m_aspectName.equals(ev.getInfo()))) {
                update();
            }
        }
    }

    protected final void update() {
        if (m_modelOwner.getModel() == null) {
            update(m_undefinedValue);
        } else {
            update(deriveProjectedValue(m_aspect.getAspectValue(m_modelOwner.getModel())));
        }
    }

    protected abstract void update(Object projectedValue);

    protected final void setAspectValue(Object projectedValue) {
        if (m_modelOwner.getModel() == null) return;
        m_aspect.setAspectValue(m_modelOwner.getModel(), deriveAspectValue(projectedValue));
    }

    protected Object deriveProjectedValue(Object aspectValue) {
        Object projectedValue = aspectValue;
        if (aspectValue == null) {
            if (m_nullValue != EQUALS_NOTHING) projectedValue = m_nullValue;
        }
        return projectedValue;
    }

    protected Object deriveAspectValue(Object projectedValue) {
        if (projectedValue == m_nullValue) return null;
        if (m_nullValue.equals(projectedValue)) return null;
        return projectedValue;
    }

    private static final Object EQUALS_NOTHING = new Object() {

        public boolean equals(Object o) {
            return false;
        }
    };
}
