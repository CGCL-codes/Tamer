package pcgen.cdom.facet;

import pcgen.cdom.testsupport.AbstractListFacetTest;
import pcgen.core.Kit;

public class KitFacetTest extends AbstractListFacetTest<Kit> {

    private KitFacet facet = new KitFacet();

    @Override
    protected AbstractListFacet<Kit> getFacet() {
        return facet;
    }

    private int n = 0;

    @Override
    protected Kit getObject() {
        Kit t = new Kit();
        t.setName("Kit" + n++);
        return t;
    }
}
