package org.nakedobjects.runtime.userprofile;

public interface OptionsAware {

    void loadOptions(Options viewOptions);

    void saveOptions(Options viewOptions);
}
