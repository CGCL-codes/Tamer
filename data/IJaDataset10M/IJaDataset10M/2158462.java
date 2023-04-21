package org.eclipsetrader.ui.internal.providers;

import java.text.NumberFormat;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipsetrader.core.views.IDataProvider;
import org.eclipsetrader.core.views.IDataProviderFactory;
import org.eclipsetrader.core.views.IHolding;

public class PositionFactory extends AbstractProviderFactory {

    private NumberFormat formatter = NumberFormat.getInstance();

    public class DataProvider implements IDataProvider {

        public DataProvider() {
        }

        public IDataProviderFactory getFactory() {
            return PositionFactory.this;
        }

        public IAdaptable getValue(IAdaptable adaptable) {
            IHolding element = (IHolding) adaptable.getAdapter(IHolding.class);
            if (element != null && element.getPosition() != null) {
                final Long value = element.getPosition();
                return new IAdaptable() {

                    @SuppressWarnings("unchecked")
                    public Object getAdapter(Class adapter) {
                        if (adapter.isAssignableFrom(String.class)) return formatter.format(value);
                        if (adapter.isAssignableFrom(Long.class)) return value;
                        return null;
                    }

                    @Override
                    public boolean equals(Object obj) {
                        if (!(obj instanceof IAdaptable)) return false;
                        Long s = (Long) ((IAdaptable) obj).getAdapter(Long.class);
                        return s == value || (value != null && value.equals(s));
                    }
                };
            }
            return null;
        }

        public void dispose() {
        }
    }

    public PositionFactory() {
        formatter.setGroupingUsed(true);
        formatter.setMinimumIntegerDigits(1);
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(0);
    }

    public IDataProvider createProvider() {
        return new DataProvider();
    }

    @SuppressWarnings("unchecked")
    public Class[] getType() {
        return new Class[] { Long.class, String.class };
    }
}
