package aurora.application.features.cstm;

import uncertain.composite.CompositeMap;

public interface ICustomizationDataProvider {

    public CompositeMap getCustomizationData(String service_name, CompositeMap context);

    public boolean getDefaultCustomizationEnabled();
}
