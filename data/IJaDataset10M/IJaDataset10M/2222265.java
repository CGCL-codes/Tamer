package org.panopticode;

public interface Supplement {

    void loadData(PanopticodeProject project, String[] arguments);

    SupplementDeclaration getDeclaration();
}
