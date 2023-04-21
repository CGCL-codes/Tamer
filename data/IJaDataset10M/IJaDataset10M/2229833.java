package net.sf.extcos.internal;

import static net.sf.extcos.util.Assert.iae;
import java.util.HashSet;
import net.sf.extcos.selector.TypeFilter;
import net.sf.extcos.util.Assert;

public class TypeFilterDisjunction extends AbstractTypeFilterJunction {

    public TypeFilterDisjunction(TypeFilter... filters) {
        Assert.notEmpty(filters, iae());
        typeFilters = new HashSet<TypeFilter>();
        for (TypeFilter filter : filters) {
            typeFilters.add(filter);
        }
    }
}
