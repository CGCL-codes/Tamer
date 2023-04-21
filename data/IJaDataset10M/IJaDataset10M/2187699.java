package org.gems.designer;

import java.util.List;

public interface ListConfigurable extends Configurable {

    public List<String> getAllowedValues();

    public void setValue(int index);

    public int getSelectedIndex();

    public void setAllowedValues(List<String> av);
}
